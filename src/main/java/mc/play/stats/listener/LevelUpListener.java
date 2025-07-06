package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class LevelUpListener implements Listener {
    private final PlayStatsPlugin plugin;

    public LevelUpListener(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        Player player = event.getPlayer();

        Event levelChangeEvent = new Event("player:level_change")
                .setMetadata("oldLevel", event.getOldLevel())
                .setMetadata("newLevel", event.getNewLevel())
                .setMetadata("world", player.getWorld().getName());
        plugin.triggerEvent(levelChangeEvent, player);
    }
}
