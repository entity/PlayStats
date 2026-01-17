package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.block.Furnace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

public class FurnaceListener implements Listener {
    private final PlayStatsPlugin plugin;

    public FurnaceListener(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCraft(FurnaceSmeltEvent event) {
        ItemStack result = event.getResult();
        Furnace furnace = (Furnace) event.getBlock().getState();

        if(furnace.getInventory().getViewers().isEmpty()) {
            return;
        }

        HumanEntity humanEntity = furnace.getInventory().getViewers().getFirst();

        if(humanEntity == null) {
            return;
        }

        if(!(humanEntity instanceof Player player)) {
            return;
        }

        Event craftEvent = new Event("player:smelt")
                .setMetadata("item", result.getType().name())
                .setMetadata("world", player.getWorld().getName());

        plugin.triggerEvent(craftEvent, player);
    }
}
