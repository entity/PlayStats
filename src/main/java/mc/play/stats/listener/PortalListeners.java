package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PortalListeners implements Listener {
    private final PlayStatsPlugin plugin;

    public PortalListeners(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPortalEnter(PlayerPortalEvent event) {
        Player player = event.getPlayer();

        Event portalEnterEvent = new Event("player:portal_enter")
                .setMetadata("portalType", event.getCause().toString())
                .setMetadata("world", player.getWorld().getName());

        plugin.triggerEvent(portalEnterEvent, player);
    }
}
