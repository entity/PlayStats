package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener {
    private final PlayStatsPlugin plugin;

    public CraftListener(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCraft(CraftItemEvent event) {
        if(event.getAction() == InventoryAction.NOTHING) {
            return;
        }

        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        ItemStack result = event.getRecipe().getResult();

        Event craftEvent = new Event("player:craft")
                .setMetadata("item", result.getType().name())
                .setMetadata("world", player.getWorld().getName());

        plugin.triggerEvent(craftEvent, player);
    }
}
