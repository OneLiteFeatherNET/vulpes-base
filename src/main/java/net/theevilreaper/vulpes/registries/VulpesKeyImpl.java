package net.theevilreaper.vulpes.registries;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

record VulpesKeyImpl<T>(@NotNull Key key) implements VulpesKey<T> {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VulpesKey<?> that)) return false;
        return Objects.equals(key, that.key());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }
}
