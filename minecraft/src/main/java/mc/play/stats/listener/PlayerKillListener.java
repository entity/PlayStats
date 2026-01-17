package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerKillListener implements Listener {
    private final PlayStatsPlugin plugin;

    public PlayerKillListener(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();

        // Early return if there is no killer (i.e., the entity was not killed by a player)
        if (killer == null) {
            return;
        }

        String causeOfDeath = entity.getLastDamageCause() != null ? entity.getLastDamageCause().getCause().toString() : "UNKNOWN";
        Location entityLocation = entity.getLocation();
        World world = entityLocation.getWorld();
        ItemStack itemInUse = killer.getInventory().getItemInMainHand();

        Event customEvent;

        // Determine whether the killed entity is a player
        if (entity instanceof Player) {
            customEvent = new Event("player:kill")
                    .setMetadata("victimUuid", entity.getUniqueId().toString())
                    .setMetadata("world", world == null ? "UNKNOWN" : world.getName())
                    .setMetadata("causeOfDeath", causeOfDeath);

        } else {
            // Handling the death of non-player entities (e.g., mobs)
            customEvent = new Event("mob:kill")
                    .setMetadata("mobType", entity.getType().name())
                    .setMetadata("world", world == null ? "UNKNOWN" : world.getName())
                    .setMetadata("causeOfDeath", causeOfDeath);

        }

        String blockType = itemInUse.getType() == Material.AIR ? "hand" : itemInUse.getType().name();
        customEvent.setMetadata("weaponUsed", blockType.toUpperCase());

        plugin.triggerEvent(customEvent, killer);
    }
}
