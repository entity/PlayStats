package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDamageListener implements Listener {
    private final PlayStatsPlugin plugin;

    public PlayerDamageListener(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker) || !(event.getEntity() instanceof Player damaged)) {
            return;
        }

        ItemStack weapon = attacker.getInventory().getItemInMainHand();

        Event pvpEvent = new Event("pvp:damage")
                .setMetadata("weapon", weapon.getType().toString())
                .setMetadata("damage", event.getDamage())
                .setMetadata("world", attacker.getWorld().getName());

        plugin.triggerEvent(pvpEvent, attacker);
    }
}
