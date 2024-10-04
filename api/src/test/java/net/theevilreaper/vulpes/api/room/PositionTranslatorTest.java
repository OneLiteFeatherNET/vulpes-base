package net.theevilreaper.vulpes.api.room;

import net.minestom.server.coordinate.Vec;
import net.theevilreaper.vulpes.api.util.PosTranslators;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionTranslatorTest {

    private static final Vec ORIGIN_VEC = new Vec(100, 100, 100);

    private static final Stream<Arguments> TO_RELATIVE = Stream.of(
            Arguments.of(
                    PosTranslators.translate(ORIGIN_VEC, new Vec(20, 20, 20), PositionTranslator.Mode.TO_RELATIVE),
                    new Vec(120, 120, 120)
            ),
            Arguments.of(
                    PosTranslators.translate(ORIGIN_VEC, new Vec(50, 20, 20), PositionTranslator.Mode.TO_RELATIVE),
                    new Vec(150, 120, 120)
            ),
            Arguments.of(
                    PosTranslators.translate(ORIGIN_VEC, new Vec(-10, 30, 10), PositionTranslator.Mode.TO_RELATIVE),
                    new Vec(90, 130, 110)
            )
    );

    private static final Stream<Arguments> TO_ABSOLUTE = Stream.of(
            Arguments.of(
                    PosTranslators.translate(ORIGIN_VEC, new Vec(20, 20, 20), PositionTranslator.Mode.TO_ABSOLUTE),
                    new Vec(80, 80, 80)
            ),
            Arguments.of(
                    PosTranslators.translate(ORIGIN_VEC, new Vec(0, 0, 0), PositionTranslator.Mode.TO_ABSOLUTE),
                    ORIGIN_VEC
            ),
            Arguments.of(
                    PosTranslators.translate(ORIGIN_VEC, new Vec(20, 50, 20), PositionTranslator.Mode.TO_ABSOLUTE),
                    new Vec(80, 50, 80)
            )
    );

    private static @NotNull Stream<Arguments> getToRelative() {
        return TO_RELATIVE;
    }

    private static @NotNull Stream<Arguments> getToAbsolute() {
        return TO_ABSOLUTE;
    }

    @ParameterizedTest
    @MethodSource("getToRelative")
    void testToRelative(@NotNull Vec translatedPoint, @NotNull Vec expectedPoint) {
        assertEquals(expectedPoint, translatedPoint);
    }

    @ParameterizedTest
    @MethodSource("getToAbsolute")
    void testToAbsolute(@NotNull Vec translatedPoint, @NotNull Vec expectedPoint) {
        assertEquals(expectedPoint, translatedPoint);
    }
}
