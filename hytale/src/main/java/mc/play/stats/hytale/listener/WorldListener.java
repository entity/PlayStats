package mc.play.stats.hytale.listener;

import mc.play.stats.hytale.HytaleStatsPlugin;

/**
 * Listens for world-related player events.
 * TODO: Update when Hytale API methods are documented.
 * Note: AddPlayerToWorldEvent may have different registration signature.
 */
public class WorldListener {
    private final HytaleStatsPlugin plugin;

    public WorldListener(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        // TODO: Register AddPlayerToWorldEvent when API is better understood
    }
}
