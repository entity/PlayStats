package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class ShearListener implements Listener {
    private final PlayStatsPlugin plugin;

    public ShearListener(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        Sheep sheep = (Sheep) event.getEntity();
        DyeColor color = sheep.getColor();

        Event shearEvent = new Event("player:shear")
                .setMetadata("entityType", sheep.getType().toString())
                .setMetadata("world", player.getWorld().getName())
                .setMetadata("color", color != null ? color.name() : "UNKNOWN");

        plugin.triggerEvent(shearEvent, player);
    }
}
