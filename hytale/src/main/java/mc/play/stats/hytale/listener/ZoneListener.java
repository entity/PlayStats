package mc.play.stats.hytale.listener;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.server.core.event.events.ecs.DiscoverZoneEvent;
import mc.play.stats.hytale.HytaleStatsPlugin;

/**
 * Listens for zone discovery events.
 * This is a Hytale-specific event for when players discover new zones/areas.
 * TODO: Update when Hytale API methods are documented.
 */
public class ZoneListener {
    private final HytaleStatsPlugin plugin;

    public ZoneListener(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getEventRegistry().register(EventPriority.LAST, DiscoverZoneEvent.class, this::onDiscoverZone);
    }

    private void onDiscoverZone(DiscoverZoneEvent event) {
        // TODO: Implement when Hytale API methods are known
        // Need: player info, zone details
    }
}
