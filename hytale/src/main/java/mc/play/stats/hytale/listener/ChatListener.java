package mc.play.stats.hytale.listener;

import mc.play.stats.hytale.HytaleStatsPlugin;

/**
 * Listens for player chat events.
 * TODO: Update when Hytale API methods are documented.
 * Note: PlayerChatEvent may have different registration signature.
 */
public class ChatListener {
    private final HytaleStatsPlugin plugin;

    public ChatListener(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        // TODO: Register PlayerChatEvent when API is better understood
    }
}
