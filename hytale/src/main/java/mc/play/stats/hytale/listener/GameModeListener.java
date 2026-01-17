package mc.play.stats.hytale.listener;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.server.core.event.events.ecs.ChangeGameModeEvent;
import mc.play.stats.hytale.HytaleStatsPlugin;

/**
 * Listens for game mode change events.
 * This is a Hytale-specific event.
 * TODO: Update when Hytale API methods are documented.
 */
public class GameModeListener {
    private final HytaleStatsPlugin plugin;

    public GameModeListener(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getEventRegistry().register(EventPriority.LAST, ChangeGameModeEvent.class, this::onGameModeChange);
    }

    private void onGameModeChange(ChangeGameModeEvent event) {
        // TODO: Implement when Hytale API methods are known
        // Need: player info, old game mode, new game mode
    }
}
