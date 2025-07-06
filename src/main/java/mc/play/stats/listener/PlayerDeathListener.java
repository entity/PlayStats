package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private final PlayStatsPlugin plugin;

    public PlayerDeathListener(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        Event deathEvent = new Event("player:death")
                .setMetadata("world", player.getWorld().getName())
                .setMetadata("causeOfDeath", player.getLastDamageCause() != null ? player.getLastDamageCause().getCause().toString() : "UNKNOWN");

        if (player.getKiller() != null) {
            deathEvent
                    .setMetadata("killerName", player.getKiller().getName())
                    .setMetadata("killerUuid", player.getKiller().getUniqueId().toString());
        }

        plugin.triggerEvent(deathEvent, player);
    }
}
