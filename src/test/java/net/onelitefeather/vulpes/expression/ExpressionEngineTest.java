package net.onelitefeather.vulpes.expression;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpressionEngineTest {

    private static Formula armorDamage() {
        return new Formula("armorDamage", List.of("base", "armor"), "base * (1 - armor / 100)");
    }

    private static Map<String, NativeSignature> noNatives() {
        return Map.of();
    }

    private static BigDecimal number(ExpressionEngine engine, String name, Object... arguments) {
        return engine.number(name, arguments);
    }

    private static Throwable rootCause(Throwable failure) {
        Throwable current = failure;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current;
    }

    @Test
    void testFormulaWithArgumentsReturnsComputedNumber() {
        ExpressionEngine engine = ExpressionEngine.of(List.of(armorDamage()), List.of(), noNatives());

        BigDecimal result = number(engine, "armorDamage", 100, 25);

        assertEquals(0, new BigDecimal("75").compareTo(result), "armorDamage(100, 25) must be 75 but was " + result);
    }

    @Test
    void testFormulaAcceptsJavaNumberTypesAsArguments() {
        Formula identity = new Formula("identity", List.of("value"), "value");
        ExpressionEngine engine = ExpressionEngine.of(List.of(identity), List.of(), noNatives());

        assertEquals(0, new BigDecimal("7").compareTo(number(engine, "identity", 7)), "int argument must be a number");
        assertEquals(0, new BigDecimal("8").compareTo(number(engine, "identity", 8L)), "long argument must be a number");
        assertEquals(0, new BigDecimal("1.5").compareTo(number(engine, "identity", 1.5d)), "double argument must be a number");
    }

    @Test
    void testFormulaRejectsArgumentOfUnsupportedType() {
        Formula identity = new Formula("identity", List.of("value"), "value");
        ExpressionEngine engine = ExpressionEngine.of(List.of(identity), List.of(), noNatives());

        IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
                () -> engine.number("identity", List.of(1)), "a list is not an argument type");

        assertTrue(failure.getMessage().contains("identity"), "message must name the formula: " + failure.getMessage());
    }

    @Test
    void testWrongArgumentCountIsRejectedNamingFormula() {
        ExpressionEngine engine = ExpressionEngine.of(List.of(armorDamage()), List.of(), noNatives());

        IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
                () -> engine.number("armorDamage", 100), "one argument for two parameters must fail");

        assertTrue(failure.getMessage().contains("armorDamage"), "message must name the formula: " + failure.getMessage());
    }

    @Test
    void testUnknownNameIsRejectedNamingIt() {
        ExpressionEngine engine = ExpressionEngine.of(List.of(armorDamage()), List.of(), noNatives());

        IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
                () -> engine.number("missing"), "an unknown name must fail");

        assertTrue(failure.getMessage().contains("missing"), "message must name the unknown name: " + failure.getMessage());
    }

    @Test
    void testExpressionCallsFormulaAndUsesItsValue() {
        ExpressionEngine engine = ExpressionEngine.of(
                List.of(armorDamage()),
                List.of(new Formula("total", List.of("base"), "armorDamage(base, 25)")),
                noNatives());

        assertEquals(0, new BigDecimal("75").compareTo(number(engine, "total", 100)),
                "the expression must use the value of the formula");
    }

    @Test
    void testExpressionIsNotCallableFromFormula() {
        Formula caller = new Formula("caller", List.of(), "helper(1)");
        Formula helper = new Formula("helper", List.of("x"), "x");

        IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
                () -> ExpressionEngine.of(List.of(caller), List.of(helper), noNatives()),
                "a formula must not call an expression");

        assertTrue(failure.getMessage().contains("caller"), "message must name the formula: " + failure.getMessage());
    }

    @Test
    void testNativeBooleanFunctionIsSeenByFormula() {
        Map<String, NativeSignature> natives = Map.of("inArena",
                new NativeSignature(1, arguments -> "spawn".equals(arguments.getFirst())));
        ExpressionEngine engine = ExpressionEngine.of(List.of(), List.of(
                new Formula("gate", List.of(), "inArena(\"spawn\")")), natives);

        assertTrue(engine.bool("gate"), "the native function returned true, so the formula must see true");
    }

    @Test
    void testNativeFunctionReceivesEachArgumentWithItsJavaType() {
        List<Object> received = new ArrayList<>();
        Map<String, NativeSignature> natives = Map.of("probe", new NativeSignature(3, arguments -> {
            received.addAll(arguments);
            return true;
        }));
        ExpressionEngine engine = ExpressionEngine.of(List.of(), List.of(
                new Formula("call", List.of(), "probe(2, \"text\", true)")), natives);

        engine.bool("call");

        assertEquals(3, received.size(), "all three arguments must reach the native function");
        assertEquals(new BigDecimal("2"), received.get(0), "a number must arrive as BigDecimal");
        assertEquals("text", received.get(1), "a string must arrive as String");
        assertEquals(Boolean.TRUE, received.get(2), "a boolean must arrive as Boolean");
    }

    @Test
    void testNativeIntegerResultIsAcceptedAsNumber() {
        Map<String, NativeSignature> natives = Map.of("five", new NativeSignature(0, arguments -> 5));
        ExpressionEngine engine = ExpressionEngine.of(List.of(), List.of(
                new Formula("value", List.of(), "five()")), natives);

        assertEquals(0, new BigDecimal("5").compareTo(number(engine, "value")), "an Integer result must be read as a number");
    }

    @Test
    void testNativeResultOfUnsupportedTypeFailsNamingFormula() {
        Map<String, NativeSignature> natives = Map.of("broken", new NativeSignature(0, arguments -> List.of()));
        ExpressionEngine engine = ExpressionEngine.of(List.of(), List.of(
                new Formula("value", List.of(), "broken()")), natives);

        IllegalStateException failure = assertThrows(IllegalStateException.class,
                () -> engine.number("value"), "a list result must fail");

        assertTrue(failure.getMessage().contains("value"), "message must name the formula: " + failure.getMessage());
    }

    @Test
    void testNativeFailureKeepsItsCauseAndNamesFormula() {
        Map<String, NativeSignature> natives = Map.of("explode", new NativeSignature(0, arguments -> {
            throw new IllegalStateException("boom");
        }));
        ExpressionEngine engine = ExpressionEngine.of(List.of(), List.of(
                new Formula("value", List.of(), "explode()")), natives);

        IllegalStateException failure = assertThrows(IllegalStateException.class,
                () -> engine.number("value"), "a failing native function must fail the evaluation");

        assertTrue(failure.getMessage().contains("value"), "message must name the formula: " + failure.getMessage());
        assertEquals("boom", rootCause(failure).getMessage(), "the cause of the native failure must be kept");
    }

    @Test
    void testNativeCallWithWrongArityFailsAtCreation() {
        Map<String, NativeSignature> natives = Map.of("inArena", new NativeSignature(1, arguments -> true));

        IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
                () -> ExpressionEngine.of(List.of(), List.of(
                        new Formula("gate", List.of(), "inArena(1, 2)")), natives),
                "two arguments for an arity of one must fail at creation");

        assertTrue(failure.getMessage().contains("gate"), "message must name the formula: " + failure.getMessage());
    }

    @Test
    void testSyntaxErrorFailsAtCreationNamingFormula() {
        IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
                () -> ExpressionEngine.of(List.of(new Formula("broken", List.of(), "1 +")), List.of(), noNatives()),
                "an incomplete source must fail at creation");

        assertTrue(failure.getMessage().contains("broken"), "message must name the formula: " + failure.getMessage());
    }

    @Test
    void testDuplicateNameFailsAtCreationNamingFormula() {
        Formula function = new Formula("total", List.of(), "1");
        Formula expression = new Formula("total", List.of(), "2");

        IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
                () -> ExpressionEngine.of(List.of(function), List.of(expression), noNatives()),
                "a formula and an expression with the same name must fail");

        assertTrue(failure.getMessage().contains("total"), "message must name the formula: " + failure.getMessage());
    }

    @Test
    void testNativeNameClashingWithFormulaFailsAtCreation() {
        Map<String, NativeSignature> natives = Map.of("total", new NativeSignature(0, arguments -> BigDecimal.ONE));

        IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
                () -> ExpressionEngine.of(List.of(new Formula("total", List.of(), "1")), List.of(), natives),
                "a native with the name of a formula must fail");

        assertTrue(failure.getMessage().contains("total"), "message must name the duplicate: " + failure.getMessage());
    }

    @Test
    void testUnknownFunctionFailsAtCreationNamingFormula() {
        IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
                () -> ExpressionEngine.of(List.of(new Formula("caller", List.of(), "missing(1)")), List.of(), noNatives()),
                "a call to an unknown function must fail at creation");

        assertTrue(failure.getMessage().contains("caller"), "message must name the formula: " + failure.getMessage());
    }

    @Test
    void testUndeclaredVariableFailsAtCreationNamingFormula() {
        IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
                () -> ExpressionEngine.of(List.of(new Formula("bonus", List.of("base"), "base + extra")), List.of(), noNatives()),
                "a variable that is not a parameter must fail at creation");

        assertTrue(failure.getMessage().contains("bonus"), "message must name the formula: " + failure.getMessage());
    }

    @Test
    void testDefaultConstantsStayAllowed() {
        Formula circle = new Formula("circle", List.of("radius"), "2 * PI * radius");
        ExpressionEngine engine = ExpressionEngine.of(List.of(circle), List.of(), noNatives());

        BigDecimal result = number(engine, "circle", 1);

        assertTrue(result.compareTo(new BigDecimal("6.28")) > 0 && result.compareTo(new BigDecimal("6.29")) < 0,
                "PI must be a known constant, result was " + result);
    }

    @Test
    void testStringResultIsReturnedAsString() {
        ExpressionEngine engine = ExpressionEngine.of(List.of(), List.of(
                new Formula("zone", List.of(), "\"spawn\"")), noNatives());

        assertEquals("spawn", engine.string("zone"), "a string result must be returned as String");
    }

    @Test
    void testBooleanResultIsReturnedAsBoolean() {
        ExpressionEngine engine = ExpressionEngine.of(List.of(), List.of(
                new Formula("positive", List.of("value"), "value > 0")), noNatives());

        assertTrue(engine.bool("positive", 1), "1 is greater than 0");
    }

    @Test
    void testWrongResultTypeFailsNamingFormulaAndBothTypes() {
        ExpressionEngine engine = ExpressionEngine.of(List.of(), List.of(
                new Formula("positive", List.of("value"), "value > 0")), noNatives());

        IllegalStateException failure = assertThrows(IllegalStateException.class,
                () -> engine.number("positive", 1), "a boolean result must not be read as a number");

        assertTrue(failure.getMessage().contains("positive"), "message must name the formula: " + failure.getMessage());
        assertTrue(failure.getMessage().contains("BOOLEAN"), "message must name the actual type: " + failure.getMessage());
        assertTrue(failure.getMessage().contains("NUMBER"), "message must name the expected type: " + failure.getMessage());
    }

    @Test
    void testRecursiveFormulaFailsNamingFormula() {
        Formula loop = new Formula("loop", List.of("x"), "loop(x)");
        ExpressionEngine engine = ExpressionEngine.of(List.of(loop), List.of(), noNatives());

        IllegalStateException failure = assertThrows(IllegalStateException.class,
                () -> engine.number("loop", 1), "an endless recursion must fail");

        assertTrue(failure.getMessage().contains("loop"), "message must name the formula: " + failure.getMessage());
    }

    @Test
    void testEngineStaysUsableAfterRecursionFailure() {
        Formula loop = new Formula("loop", List.of("x"), "loop(x)");
        Formula twice = new Formula("twice", List.of("x"), "x * 2");
        ExpressionEngine engine = ExpressionEngine.of(List.of(loop, twice), List.of(), noNatives());
        assertThrows(IllegalStateException.class, () -> engine.number("loop", 1), "precondition: recursion fails");

        BigDecimal result = number(engine, "twice", 3);

        assertEquals(0, new BigDecimal("6").compareTo(result), "the depth counter must be reset after a failure");
    }

    @Test
    void testCallChainOf32FormulasIsAccepted() {
        ExpressionEngine engine = chainEngine(32);

        BigDecimal result = number(engine, "chain32", 4);

        assertEquals(0, new BigDecimal("4").compareTo(result), "a chain of 32 nested calls is within the limit");
    }

    @Test
    void testCallChainDeeperThan32FormulasFailsNamingFormula() {
        ExpressionEngine engine = chainEngine(33);

        IllegalStateException failure = assertThrows(IllegalStateException.class,
                () -> engine.number("chain33", 4), "a chain of 33 nested calls exceeds the limit");

        assertTrue(failure.getMessage().contains("chain33"), "message must name the formula: " + failure.getMessage());
    }

    @Test
    void testEnginesWithSameNameKeepTheirOwnSource() {
        ExpressionEngine doubling = ExpressionEngine.of(List.of(new Formula("factor", List.of("x"), "x * 2")),
                List.of(), noNatives());
        ExpressionEngine tripling = ExpressionEngine.of(List.of(new Formula("factor", List.of("x"), "x * 3")),
                List.of(), noNatives());

        assertEquals(0, new BigDecimal("10").compareTo(number(doubling, "factor", 5)), "the first engine keeps x * 2");
        assertEquals(0, new BigDecimal("15").compareTo(number(tripling, "factor", 5)), "the second engine keeps x * 3");
    }

    @Test
    void testFormulaParametersAreCopied() {
        List<String> parameters = new ArrayList<>(List.of("base"));
        Formula formula = new Formula("identity", parameters, "base");

        parameters.add("extra");

        assertEquals(List.of("base"), formula.parameters(), "changing the source list must not change the formula");
    }

    @Test
    void testFormulaRejectsDuplicateParameter() {
        assertThrows(IllegalArgumentException.class,
                () -> new Formula("sum", List.of("a", "a"), "a + a"),
                "two parameters with the same name must be rejected");
    }

    @Test
    void testFormulaRejectsInvalidName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Formula("1abc", List.of(), "1"),
                "a name that starts with a digit must be rejected");
    }

    @Test
    void testFormulaRejectsNullSource() {
        assertThrows(NullPointerException.class,
                () -> new Formula("empty", List.of(), null),
                "a formula needs a source");
    }

    @Test
    void testNativeSignatureRejectsNegativeArity() {
        assertThrows(IllegalArgumentException.class,
                () -> new NativeSignature(-1, arguments -> null),
                "an arity below zero must be rejected");
    }

    @Test
    void testBooleanFalseIsNotMistakenForTrue() {
        ExpressionEngine engine = ExpressionEngine.of(List.of(), List.of(
                new Formula("never", List.of(), "1 > 2")), noNatives());

        assertFalse(engine.bool("never"), "1 > 2 must be false");
    }

    /**
     * Builds an engine whose formula {@code chain<calls>} makes {@code calls} nested formula calls, ending in the
     * identity formula {@code chain0}.
     */
    private static ExpressionEngine chainEngine(int calls) {
        List<Formula> formulas = new ArrayList<>();
        formulas.add(new Formula("chain0", List.of("x"), "x"));
        for (int level = 1; level <= calls; level++) {
            formulas.add(new Formula("chain" + level, List.of("x"), "chain" + (level - 1) + "(x)"));
        }
        return ExpressionEngine.of(formulas, List.of(), Map.of());
    }
}
