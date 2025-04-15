package net.theevilreaper.vulpes.entity.goal;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.utils.chunk.ChunkUtils;
import net.theevilreaper.vulpes.entity.MetaKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@MetaKey("target")
public class TargetGoal extends TargetSelector {

    private final int distance;

    public TargetGoal(@NotNull EntityCreature creature, int distance) {
        super(creature);

        this.distance = distance;
    }

    @Override
    public @Nullable Entity findTarget() {
        var instance = getEntityCreature().getInstance();
        var entityPos = getEntityCreature().getPosition();

        if (entityCreature.isDead() || instance == null || !ChunkUtils.isLoaded(entityCreature.getChunk())) return null;

        var currentChunk = instance.getChunkAt(entityPos);

        if (currentChunk == null) return null;

        var entitiesNearBy = instance.getNearbyEntities(entityPos, this.distance);

        return entitiesNearBy
                .stream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .filter(player -> player.getGameMode() == GameMode.SURVIVAL)
                .min((p1, p2) -> (int) (p1.getDistance(entityPos) - p2.getDistance(entityPos)))
                .filter(p -> p.getPosition().distance(entityPos) < this.distance).orElse(null);
    }
}
