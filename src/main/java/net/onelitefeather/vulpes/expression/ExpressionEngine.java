package net.onelitefeather.vulpes.expression;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ASTNode;
import com.ezylang.evalex.parser.ParseException;
import com.ezylang.evalex.parser.Token;

import java.math.BigDecimal;
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
 * <p>An engine keeps no global state and can be used from several threads at once. Each evaluation parses its source
 * anew.
 *
 * @since 0.6.0
 */
public final class ExpressionEngine {

    private final ExpressionConfiguration configuration;
    private final Map<String, Formula> callables;
    private final ThreadLocal<Integer> callDepth;

    private ExpressionEngine(
            ExpressionConfiguration configuration,
            Map<String, Formula> callables,
            ThreadLocal<Integer> callDepth
    ) {
        this.configuration = configuration;
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
        Map<String, Formula> callables = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Formula function : formulaFunctions) {
            claim(names, function.name(), "Formula");
            arities.put(function.name(), function.parameters().size());
            callables.put(function.name(), function);
        }
        for (Formula expression : formulaExpressions) {
            claim(names, expression.name(), "Formula");
            callables.put(expression.name(), expression);
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
        for (Formula function : formulaFunctions) {
            configuration = configuration.withAdditionalFunctions(
                    Map.entry(function.name(), new FormulaFunction(function, callDepth)));
        }
        for (Map.Entry<String, NativeSignature> nativeEntry : nativeFunctions.entrySet()) {
            configuration = configuration.withAdditionalFunctions(
                    Map.entry(nativeEntry.getKey(), new NativeExpressionFunction(nativeEntry.getKey(), nativeEntry.getValue())));
        }

        for (Formula function : formulaFunctions) {
            validate(function, configuration, arities);
        }
        for (Formula expression : formulaExpressions) {
            validate(expression, configuration, arities);
        }
        return new ExpressionEngine(configuration, Collections.unmodifiableMap(callables), callDepth);
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
        Formula formula = callables.get(name);
        if (formula == null) {
            throw new IllegalArgumentException("No formula or expression named '%s'".formatted(name));
        }
        if (arguments.length != formula.parameters().size()) {
            throw new IllegalArgumentException("Formula '%s' expects %d argument(s), but got %d"
                    .formatted(name, formula.parameters().size(), arguments.length));
        }

        Expression expression = new Expression(formula.source(), configuration);
        for (int index = 0; index < arguments.length; index++) {
            expression.with(formula.parameters().get(index),
                    ExpressionValues.toEvaluationValue(name, arguments[index]));
        }
        try {
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

    private static void validate(Formula formula, ExpressionConfiguration configuration, Map<String, Integer> arities) {
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
        } catch (ParseException failure) {
            throw new IllegalArgumentException(
                    "Formula '%s' is invalid: %s".formatted(formula.name(), failure.getMessage()), failure);
        }
    }
}
