package mc.play.stats.hytale.listener;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.DrainPlayerFromWorldEvent;
import mc.play.stats.hytale.HytaleStatsPlugin;

import javax.annotation.Nonnull;

/**
 * Listens for world-related player events.
 * Tracks when players are added to or removed from worlds (e.g., world changes, teleports).
 */
public class WorldListener {
    private final HytaleStatsPlugin plugin;

    public WorldListener(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(@Nonnull EventRegistry eventRegistry) {
        // AddPlayerToWorldEvent and DrainPlayerFromWorldEvent have String key type (world name)
        // Register globally to catch events for all worlds
        eventRegistry.registerGlobal(AddPlayerToWorldEvent.class, this::onAddPlayerToWorld);
        eventRegistry.registerGlobal(DrainPlayerFromWorldEvent.class, this::onDrainPlayerFromWorld);
    }

    private void onAddPlayerToWorld(AddPlayerToWorldEvent event) {
        // Note: AddPlayerToWorldEvent API requires further investigation
        // The player ref access pattern may differ from other events
        String worldName = event.getWorld().getName();

        // For now, just log the world change event without player details
        // Player details will be added once API is confirmed
        plugin.debug("Player added to world: " + worldName);
    }

    private void onDrainPlayerFromWorld(DrainPlayerFromWorldEvent event) {
        // Note: DrainPlayerFromWorldEvent API requires further investigation
        String worldName = event.getWorld().getName();

        // For now, just log the world change event without player details
        plugin.debug("Player drained from world: " + worldName);
    }
}
