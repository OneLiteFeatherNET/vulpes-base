package net.theevilreaper.vulpes.entity.animation;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.0
 */
@ApiStatus.Internal
public enum AnimationType {

    IDLE(0, "idle"),
    RUN(1, "run"),
    JUMP(2, "jump"),
    DEATH(3, "death"),
    ATTACK(4, "attack"),
    ATTACK_2(5, "attack_2"),
    ATTACK_3(6, "attack_3"),
    SPECIAL_ATTACK(7, "special_attack");

    private static final AnimationType[] VALUES = values();

    private final int id;
    private final String name;

    AnimationType(int id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public static AnimationType getType(int id) {
        return VALUES[id];
    }
}
