package mc.play.stats.hytale.listener;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.server.core.event.events.ecs.DropItemEvent;
import com.hypixel.hytale.server.core.event.events.ecs.InteractivelyPickupItemEvent;
import mc.play.stats.hytale.HytaleStatsPlugin;

/**
 * Listens for item pickup and drop events.
 * TODO: Update when Hytale API methods are documented.
 */
public class ItemListeners {
    private final HytaleStatsPlugin plugin;

    public ItemListeners(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getEventRegistry().register(EventPriority.LAST, InteractivelyPickupItemEvent.class, this::onItemPickup);
        plugin.getEventRegistry().register(EventPriority.LAST, DropItemEvent.class, this::onItemDrop);
    }

    private void onItemPickup(InteractivelyPickupItemEvent event) {
        // TODO: Implement when Hytale API methods are known
        // Need: player info, item type, amount
    }

    private void onItemDrop(DropItemEvent event) {
        // TODO: Implement when Hytale API methods are known
        // Need: player info, item type, amount
    }
}
