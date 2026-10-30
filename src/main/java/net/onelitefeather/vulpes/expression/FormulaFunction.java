package net.onelitefeather.vulpes.expression;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.parser.ParseException;
import com.ezylang.evalex.parser.Token;

import java.util.List;

/**
 * A formula the engine offers to other formulas as a function.
 *
 * <p>A call evaluates the formula body with the arguments bound to its parameters. Nested calls are counted per thread
 * and a chain deeper than {@link #MAX_CALL_DEPTH} is rejected.
 */
final class FormulaFunction extends AbstractFunction {

    /**
     * The number of nested formula calls a single evaluation may make.
     */
    static final int MAX_CALL_DEPTH = 32;

    private final Formula formula;
    private final ThreadLocal<Integer> callDepth;
    private final List<FunctionParameterDefinition> parameterDefinitions;

    /**
     * Creates the function for a formula.
     *
     * @param formula   the formula, already validated
     * @param callDepth the call depth of the engine that owns the function
     */
    FormulaFunction(Formula formula, ThreadLocal<Integer> callDepth) {
        this.formula = formula;
        this.callDepth = callDepth;
        this.parameterDefinitions = formula.parameters().stream()
                .map(parameter -> FunctionParameterDefinition.builder().name(parameter).build())
                .toList();
    }

    @Override
    public List<FunctionParameterDefinition> getFunctionParameterDefinitions() {
        return parameterDefinitions;
    }

    @Override
    public void validatePreEvaluation(Token functionToken, EvaluationValue... parameterValues)
            throws EvaluationException {
        requireArgumentCount(functionToken, parameterValues);
    }

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues)
            throws EvaluationException {
        requireArgumentCount(functionToken, parameterValues);
        int depth = callDepth.get();
        if (depth >= MAX_CALL_DEPTH) {
            throw new EvaluationException(functionToken, "Formula '%s' exceeds the call depth of %d (recursive formula?)"
                    .formatted(formula.name(), MAX_CALL_DEPTH));
        }
        callDepth.set(depth + 1);
        try {
            Expression body = new Expression(formula.source(), expression.getConfiguration());
            for (int index = 0; index < parameterValues.length; index++) {
                body.with(formula.parameters().get(index), parameterValues[index]);
            }
            return body.evaluate();
        } catch (ParseException failure) {
            throw new EvaluationException(functionToken,
                    "Formula '%s' is invalid: %s".formatted(formula.name(), failure.getMessage()));
        } finally {
            if (depth == 0) {
                callDepth.remove();
            } else {
                callDepth.set(depth);
            }
        }
    }

    private void requireArgumentCount(Token functionToken, EvaluationValue... parameterValues)
            throws EvaluationException {
        if (parameterValues.length != formula.parameters().size()) {
            throw new EvaluationException(functionToken, "Formula '%s' expects %d argument(s), but got %d"
                    .formatted(formula.name(), formula.parameters().size(), parameterValues.length));
        }
    }
}
