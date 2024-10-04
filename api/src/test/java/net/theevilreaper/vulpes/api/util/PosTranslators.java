package net.theevilreaper.vulpes.api.util;

import net.minestom.server.coordinate.Vec;
import net.theevilreaper.vulpes.api.room.PositionTranslator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static net.theevilreaper.vulpes.api.room.PositionTranslator.Mode.TO_ABSOLUTE;
import static net.theevilreaper.vulpes.api.room.PositionTranslator.Mode.TO_RELATIVE;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public final class PosTranslators {

    private PosTranslators() {
        // Nothing to do
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull Vec translate(@NotNull Vec mainPoint, @NotNull Vec translatePoint, @NotNull PositionTranslator.Mode mode) {
        return switch (mode) {
            case TO_ABSOLUTE -> translateToAbsolute(mainPoint, translatePoint);
            case TO_RELATIVE -> translateToRelative(mainPoint, translatePoint);
        };
    }

    @Contract(value = "_, _ -> new", pure = true)
    private static @NotNull Vec translateToRelative(@NotNull Vec mainPoint, @NotNull Vec translatePoint) {
        return mainPoint.add(translatePoint);
    }

    @Contract(value = "_, _ -> new", pure = true)
    private static @NotNull Vec translateToAbsolute(@NotNull Vec mainPoint, @NotNull Vec translatePoint) {
        return mainPoint.sub(translatePoint);
    }
}
