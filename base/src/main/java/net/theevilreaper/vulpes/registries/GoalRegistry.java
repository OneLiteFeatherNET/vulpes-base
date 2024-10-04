package net.theevilreaper.vulpes.registries;

import net.minestom.server.entity.ai.GoalSelector;
import net.theevilreaper.vulpes.entity.MetaKey;
import net.theevilreaper.vulpes.exception.MetaKeyNotFoundException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author phillippglanz
 * @since 1.0.0
 * @version 1.0.0
 */
@ApiStatus.Experimental
public final class GoalRegistry {

    private final Map<String, GoalSelector> selectors = new HashMap<>();

    /**
     * Add a {@link GoalSelector} to the registry.
     * @param selector to add
     */
    public void addGoal(@NotNull GoalSelector selector) {
        if (selector.getClass().isAnnotationPresent(MetaKey.class)) {
            MetaKey metaKey = selector.getClass().getAnnotation(MetaKey.class);
            this.selectors.putIfAbsent(metaKey.value(), selector);
        } else {
            throw new MetaKeyNotFoundException("No goal key annotation found");
        }
    }

    @Nullable
    public GoalSelector getSelector(@NotNull String key) {
        return this.selectors.getOrDefault(key, null);
    }

    public boolean hasSelector(@NotNull String key) {
        return this.selectors.containsKey(key);
    }

    public boolean hasSelector(@NotNull GoalSelector selector) {
        return this.selectors.containsValue(selector);
    }

    public void removeSelector(@NotNull String key) {
        this.selectors.remove(key);
    }

}
