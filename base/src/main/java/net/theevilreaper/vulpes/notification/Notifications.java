package net.theevilreaper.vulpes.notification;

import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.AdvancementManager;
import net.minestom.server.advancements.AdvancementTab;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class represents an api to send or remove {@link net.minestom.server.advancements.Advancement} to a player.
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.0
 */
@ApiStatus.Experimental
public final class Notifications {

    private static final Logger LOGGER = LoggerFactory.getLogger(Notifications.class);

    private static final AdvancementManager ADVANCEMENT_MANAGER = MinecraftServer.getAdvancementManager();

    private Notifications() {}

    /**
     * Send an {@link net.minestom.server.advancements.Advancement} to the given {@link Player}.
     * @param player the player who should retrieve that
     * @param identifier the string from the advancement
     */
    public static void send(@NotNull Player player, @NotNull String identifier) {
        var advancementTab = ADVANCEMENT_MANAGER.getTab(identifier);
        if (advancementTab == null) {
            LOGGER.warn("The given advancement with the identifier: {} does not exists!", identifier);
            return;
        }
        advancementTab.addViewer(player);
    }

    /**
     * Send a {@link AdvancementTab} to a given {@link Player}.
     * @param player the player who should receive the tab
     * @param advancementTab the tab to send
     */
    public static void send(@NotNull Player player, @NotNull AdvancementTab advancementTab) {
        advancementTab.addViewer(player);
    }

    /**
     * Removes a visible {@link AdvancementTab} from the a {@link Player}
     * @param player the player who should receive the remove
     * @param identifier the identifier from the {@link net.minestom.server.advancements.Advancement}
     */
    public static void remove(@NotNull Player player, @NotNull String identifier) {
        var advancementTab = ADVANCEMENT_MANAGER.getTab(identifier);
        if (advancementTab == null) {
            LOGGER.warn("The given advancement with the identifier: {} does not exists!", identifier);
            return;
        }
        advancementTab.removeViewer(player);
    }

    /**
     * Removes a visible {@link AdvancementTab} from the a {@link Player}
     * @param player the player who should receive the remove
     * @param tab the {@link AdvancementTab} to remove
     */
    public static void remove(@NotNull Player player, @NotNull AdvancementTab tab) {
        tab.removeViewer(player);
    }
}
