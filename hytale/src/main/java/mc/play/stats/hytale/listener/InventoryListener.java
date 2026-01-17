package mc.play.stats.hytale.listener;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.server.core.event.events.ecs.SwitchActiveSlotEvent;
import mc.play.stats.hytale.HytaleStatsPlugin;

/**
 * Listens for inventory-related events.
 * This includes hotbar slot switches.
 * TODO: Update when Hytale API methods are documented.
 */
public class InventoryListener {
    private final HytaleStatsPlugin plugin;

    public InventoryListener(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getEventRegistry().register(EventPriority.LAST, SwitchActiveSlotEvent.class, this::onSwitchActiveSlot);
    }

    private void onSwitchActiveSlot(SwitchActiveSlotEvent event) {
        // TODO: Implement when Hytale API methods are known
        // Need: player info, old slot, new slot
    }
}
