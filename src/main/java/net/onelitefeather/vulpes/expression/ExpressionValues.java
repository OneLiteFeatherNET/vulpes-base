package net.onelitefeather.vulpes.expression;

import com.ezylang.evalex.data.EvaluationValue;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Converts between the Java values of the engine API and EvalEx values.
 */
final class ExpressionValues {

    private ExpressionValues() {
    }

    /**
     * Converts an argument passed to the engine into an EvalEx value. Integral and floating point numbers become
     * {@link BigDecimal}.
     *
     * @param formulaName the formula the argument is for, used in the error message
     * @param argument    the argument
     * @return the EvalEx value
     * @throws IllegalArgumentException if the argument is {@code null} or of an unsupported type
     */
    static EvaluationValue toEvaluationValue(String formulaName, Object argument) {
        if (argument instanceof Number number) {
            return EvaluationValue.numberValue(toBigDecimal(number));
        }
        if (argument instanceof Boolean value) {
            return EvaluationValue.booleanValue(value);
        }
        if (argument instanceof String value) {
            return EvaluationValue.stringValue(value);
        }
        String type = argument == null ? "null" : argument.getClass().getName();
        throw new IllegalArgumentException(
                "Argument of formula '%s' has unsupported type %s".formatted(formulaName, type));
    }

    /**
     * Converts an EvalEx value into the argument type passed to a native function.
     *
     * @param value the EvalEx value
     * @return the argument, or {@code null} if the value is not a number, boolean or string
     */
    static Object toNativeArgument(EvaluationValue value) {
        if (value.isNumberValue()) {
            return value.getNumberValue();
        }
        if (value.isBooleanValue()) {
            return value.getBooleanValue();
        }
        if (value.isStringValue()) {
            return value.getStringValue();
        }
        return null;
    }

    /**
     * Converts the result of a native function into an EvalEx value.
     *
     * @param result the result of the native function
     * @return the EvalEx value, or {@code null} if the result is not a number, boolean or string
     */
    static EvaluationValue toEvaluationResult(Object result) {
        if (result instanceof Number number) {
            return EvaluationValue.numberValue(toBigDecimal(number));
        }
        if (result instanceof Boolean value) {
            return EvaluationValue.booleanValue(value);
        }
        if (result instanceof String value) {
            return EvaluationValue.stringValue(value);
        }
        return null;
    }

    static BigDecimal toBigDecimal(Number number) {
        if (number instanceof BigDecimal value) {
            return value;
        }
        if (number instanceof BigInteger value) {
            return new BigDecimal(value);
        }
        if (number instanceof Double || number instanceof Float) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        if (number instanceof Long || number instanceof Integer || number instanceof Short || number instanceof Byte) {
            return BigDecimal.valueOf(number.longValue());
        }
        return new BigDecimal(number.toString());
    }
}
