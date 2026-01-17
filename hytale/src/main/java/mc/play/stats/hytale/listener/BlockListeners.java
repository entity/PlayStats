package mc.play.stats.hytale.listener;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import mc.play.stats.hytale.HytaleStatsPlugin;

/**
 * Listens for block place and break events.
 * TODO: Update when Hytale API methods are documented.
 */
public class BlockListeners {
    private final HytaleStatsPlugin plugin;

    public BlockListeners(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getEventRegistry().register(EventPriority.LAST, PlaceBlockEvent.class, this::onBlockPlace);
        plugin.getEventRegistry().register(EventPriority.LAST, BreakBlockEvent.class, this::onBlockBreak);
    }

    private void onBlockPlace(PlaceBlockEvent event) {
        // TODO: Implement when Hytale API methods are known
        // Need: player info, block type, world name
    }

    private void onBlockBreak(BreakBlockEvent event) {
        // TODO: Implement when Hytale API methods are known
        // Need: player info, block type, world name
    }
}
