package net.onelitefeather.vulpes.expression;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ASTNode;
import com.ezylang.evalex.parser.ParseException;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Evaluates the named formulas and expressions of a game's content with EvalEx.
 *
 * <p>The EvalEx default configuration is used, with every formula and every native function added as a function.
 * Formulas can call each other and the native functions; expressions can be evaluated by name but cannot be called
 * from formulas. All sources are parsed and checked when the engine is created, so an invalid content definition fails
 * at startup.
 *
 * <p>An engine keeps no global state and can be used from several threads at once. Sources are parsed once, when the
 * engine is created, and each evaluation reuses the parsed form.
 *
 * @since 0.6.0
 */
public final class ExpressionEngine {

    private final Map<String, ParsedFormula> callables;
    private final ThreadLocal<Integer> callDepth;

    private ExpressionEngine(Map<String, ParsedFormula> callables, ThreadLocal<Integer> callDepth) {
        this.callables = callables;
        this.callDepth = callDepth;
    }

    /**
     * Creates an engine and validates every source.
     *
     * @param functions   the formulas that other formulas can call
     * @param expressions the expressions that can only be evaluated by name
     * @param natives     the native functions formulas can call, by name
     * @return the engine
     * @throws NullPointerException     if an argument or an element is {@code null}
     * @throws IllegalArgumentException if a name is used twice, a native name is not an identifier, a source has a
     *                                  syntax error, calls an unknown function or a function with a wrong number of
     *                                  arguments, or uses an undeclared variable
     */
    public static ExpressionEngine of(
            List<Formula> functions,
            List<Formula> expressions,
            Map<String, NativeSignature> natives
    ) {
        List<Formula> formulaFunctions = List.copyOf(Objects.requireNonNull(functions, "functions"));
        List<Formula> formulaExpressions = List.copyOf(Objects.requireNonNull(expressions, "expressions"));
        Map<String, NativeSignature> nativeFunctions = Map.copyOf(Objects.requireNonNull(natives, "natives"));

        Set<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        Map<String, Integer> arities = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Formula function : formulaFunctions) {
            claim(names, function.name(), "Formula");
            arities.put(function.name(), function.parameters().size());
        }
        for (Formula expression : formulaExpressions) {
            claim(names, expression.name(), "Formula");
        }
        for (Map.Entry<String, NativeSignature> nativeEntry : nativeFunctions.entrySet()) {
            if (!Formula.isIdentifier(nativeEntry.getKey())) {
                throw new IllegalArgumentException(
                        "Native function name '%s' is not an identifier".formatted(nativeEntry.getKey()));
            }
            claim(names, nativeEntry.getKey(), "Native function");
            arities.put(nativeEntry.getKey(), nativeEntry.getValue().arity());
        }

        ThreadLocal<Integer> callDepth = ThreadLocal.withInitial(() -> 0);
        ExpressionConfiguration configuration = ExpressionConfiguration.defaultConfiguration();
        List<FormulaFunction> functionBodies = new ArrayList<>();
        for (Formula function : formulaFunctions) {
            FormulaFunction functionBody = new FormulaFunction(function, callDepth);
            functionBodies.add(functionBody);
            configuration = configuration.withAdditionalFunctions(Map.entry(function.name(), functionBody));
        }
        for (Map.Entry<String, NativeSignature> nativeEntry : nativeFunctions.entrySet()) {
            configuration = configuration.withAdditionalFunctions(
                    Map.entry(nativeEntry.getKey(), new NativeExpressionFunction(nativeEntry.getKey(), nativeEntry.getValue())));
        }

        Map<String, ParsedFormula> callables = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (int index = 0; index < formulaFunctions.size(); index++) {
            Formula function = formulaFunctions.get(index);
            Expression template = validate(function, configuration, arities);
            functionBodies.get(index).bind(template);
            callables.put(function.name(), new ParsedFormula(function, template));
        }
        for (Formula expression : formulaExpressions) {
            callables.put(expression.name(), new ParsedFormula(expression, validate(expression, configuration, arities)));
        }
        return new ExpressionEngine(Collections.unmodifiableMap(callables), callDepth);
    }

    /**
     * Evaluates a formula or expression that yields a number.
     *
     * @param name      the name of the formula or expression
     * @param arguments the arguments, one per parameter, in parameter order
     * @return the result
     * @throws IllegalArgumentException if the name is unknown, the argument count is wrong or an argument has an
     *                                  unsupported type
     * @throws IllegalStateException    if the evaluation fails or the result is not a number
     */
    public BigDecimal number(String name, Object... arguments) {
        EvaluationValue value = evaluate(name, arguments);
        requireType(name, value, EvaluationValue.DataType.NUMBER);
        return value.getNumberValue();
    }

    /**
     * Evaluates a formula or expression that yields a boolean.
     *
     * @param name      the name of the formula or expression
     * @param arguments the arguments, one per parameter, in parameter order
     * @return the result
     * @throws IllegalArgumentException if the name is unknown, the argument count is wrong or an argument has an
     *                                  unsupported type
     * @throws IllegalStateException    if the evaluation fails or the result is not a boolean
     */
    public boolean bool(String name, Object... arguments) {
        EvaluationValue value = evaluate(name, arguments);
        requireType(name, value, EvaluationValue.DataType.BOOLEAN);
        return value.getBooleanValue();
    }

    /**
     * Evaluates a formula or expression that yields a string.
     *
     * @param name      the name of the formula or expression
     * @param arguments the arguments, one per parameter, in parameter order
     * @return the result
     * @throws IllegalArgumentException if the name is unknown, the argument count is wrong or an argument has an
     *                                  unsupported type
     * @throws IllegalStateException    if the evaluation fails or the result is not a string
     */
    public String string(String name, Object... arguments) {
        EvaluationValue value = evaluate(name, arguments);
        requireType(name, value, EvaluationValue.DataType.STRING);
        return value.getStringValue();
    }

    private EvaluationValue evaluate(String name, Object... arguments) {
        ParsedFormula parsed = callables.get(name);
        if (parsed == null) {
            throw new IllegalArgumentException("No formula or expression named '%s'".formatted(name));
        }
        Formula formula = parsed.formula();
        if (arguments.length != formula.parameters().size()) {
            throw new IllegalArgumentException("Formula '%s' expects %d argument(s), but got %d"
                    .formatted(name, formula.parameters().size(), arguments.length));
        }

        try {
            Expression expression = new Expression(parsed.template());
            for (int index = 0; index < arguments.length; index++) {
                expression.with(formula.parameters().get(index),
                        ExpressionValues.toEvaluationValue(name, arguments[index]));
            }
            return expression.evaluate();
        } catch (EvaluationException | ParseException failure) {
            throw new IllegalStateException(
                    "Formula '%s' could not be evaluated: %s".formatted(name, failure.getMessage()), failure);
        }
    }

    private static void requireType(String name, EvaluationValue value, EvaluationValue.DataType expected) {
        if (value.getDataType() != expected) {
            throw new IllegalStateException("Formula '%s' returns %s, but %s was expected"
                    .formatted(name, value.getDataType(), expected));
        }
    }

    private static void claim(Set<String> names, String name, String kind) {
        if (!names.add(name)) {
            throw new IllegalArgumentException("%s '%s' is declared more than once".formatted(kind, name));
        }
    }

    /**
     * Parses and checks a source. The returned expression is the parsed template that evaluations copy.
     */
    private static Expression validate(Formula formula, ExpressionConfiguration configuration, Map<String, Integer> arities) {
        Expression expression = new Expression(formula.source(), configuration);
        try {
            expression.validate();
            for (ASTNode node : expression.getAllASTNodes()) {
                Token token = node.getToken();
                if (token.getType() != Token.TokenType.FUNCTION || !arities.containsKey(token.getValue())) {
                    continue;
                }
                int expected = arities.get(token.getValue());
                if (node.getParameters().size() != expected) {
                    throw new IllegalArgumentException("Formula '%s' calls '%s' with %d argument(s), but it takes %d"
                            .formatted(formula.name(), token.getValue(), node.getParameters().size(), expected));
                }
            }
            Set<String> declared = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            declared.addAll(formula.parameters());
            for (String variable : expression.getUsedVariables()) {
                if (!declared.contains(variable)) {
                    throw new IllegalArgumentException(
                            "Formula '%s' uses undeclared variable '%s'".formatted(formula.name(), variable));
                }
            }
            return expression;
        } catch (ParseException failure) {
            throw new IllegalArgumentException(
                    "Formula '%s' is invalid: %s".formatted(formula.name(), failure.getMessage()), failure);
        }
    }

    /**
     * A formula or expression with its source parsed once. Evaluations copy the template instead of parsing again.
     */
    private record ParsedFormula(Formula formula, Expression template) {
    }
}
