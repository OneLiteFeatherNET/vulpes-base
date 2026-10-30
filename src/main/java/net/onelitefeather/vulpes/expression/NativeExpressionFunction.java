package net.onelitefeather.vulpes.expression;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.parser.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A {@link NativeFunction} the engine offers to formulas as a function.
 */
final class NativeExpressionFunction extends AbstractFunction {

    private final String name;
    private final NativeSignature signature;
    private final List<FunctionParameterDefinition> parameterDefinitions;

    /**
     * Creates the function for a native signature.
     *
     * @param name      the name formulas call the function by
     * @param signature the native function and its arity
     */
    NativeExpressionFunction(String name, NativeSignature signature) {
        this.name = name;
        this.signature = signature;
        this.parameterDefinitions = IntStream.range(0, signature.arity())
                .mapToObj(index -> FunctionParameterDefinition.builder().name("argument" + (index + 1)).build())
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
        List<Object> arguments = new ArrayList<>(parameterValues.length);
        for (EvaluationValue value : parameterValues) {
            Object argument = ExpressionValues.toNativeArgument(value);
            if (argument == null) {
                throw new EvaluationException(functionToken,
                        "Native function '%s' does not accept %s arguments".formatted(name, value.getDataType()));
            }
            arguments.add(argument);
        }

        EvaluationValue result;
        try {
            result = ExpressionValues.toEvaluationResult(signature.function().call(List.copyOf(arguments)));
        } catch (RuntimeException failure) {
            EvaluationException exception = new EvaluationException(functionToken,
                    "Native function '%s' failed: %s".formatted(name, failure.getMessage()));
            exception.initCause(failure);
            throw exception;
        }
        if (result == null) {
            throw new EvaluationException(functionToken,
                    "Native function '%s' returned a value that is not a number, boolean or string".formatted(name));
        }
        return result;
    }

    private void requireArgumentCount(Token functionToken, EvaluationValue... parameterValues)
            throws EvaluationException {
        if (parameterValues.length != signature.arity()) {
            throw new EvaluationException(functionToken, "Native function '%s' expects %d argument(s), but got %d"
                    .formatted(name, signature.arity(), parameterValues.length));
        }
    }
}
