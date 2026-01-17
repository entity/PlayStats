package mc.play.stats.hytale.listener;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.server.core.event.events.ecs.UseBlockEvent;
import mc.play.stats.hytale.HytaleStatsPlugin;

/**
 * Listens for block use/interaction events.
 * TODO: Update when Hytale API methods are documented.
 */
public class UseBlockListener {
    private final HytaleStatsPlugin plugin;

    public UseBlockListener(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getEventRegistry().register(EventPriority.LAST, UseBlockEvent.class, this::onUseBlock);
    }

    private void onUseBlock(UseBlockEvent event) {
        // TODO: Implement when Hytale API methods are known
        // Need: player info, block type, world name, interaction type
    }
}
