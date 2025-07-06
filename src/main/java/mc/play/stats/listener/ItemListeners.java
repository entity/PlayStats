package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListeners implements Listener {
    private final PlayStatsPlugin plugin;

    public ItemListeners(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        ItemStack itemStack = event.getItem().getItemStack();

        Event pickupEvent = new Event("item:pickup")
                .setMetadata("item", itemStack.getType().toString())
                .setMetadata("remainingStack", itemStack.getAmount())
                .setMetadata("world", player.getWorld().getName());
        plugin.triggerEvent(pickupEvent, player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemDrop().getItemStack();

        Event dropEvent = new Event("item:drop")
                .setMetadata("item", itemStack.getType().toString())
                .setMetadata("dropCause", "player")
                .setMetadata("itemCount", itemStack.getAmount())
                .setMetadata("world", player.getWorld().getName());
        plugin.triggerEvent(dropEvent, player);
    }
}
