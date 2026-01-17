package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.stream.Collectors;

public class ItemEnchantListener implements Listener {
    private final PlayStatsPlugin plugin;

    public ItemEnchantListener(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerItemEnchant(EnchantItemEvent event) {
        Player player = event.getEnchanter();

        // get list of enchantments that were added in a human-readable format
        String enchantments = event.getEnchantsToAdd().entrySet().stream()
                .map(entry -> entry.getKey().getKey().getKey() + " " + entry.getValue())
                .collect(Collectors.joining(", "));

        Event itemEnchantEvent = new Event("player:item_enchant")
                .setMetadata("item", event.getItem().getType().toString())
                .setMetadata("enchantments", enchantments)
                .setMetadata("world", player.getWorld().getName());
        plugin.triggerEvent(itemEnchantEvent, player);
    }
}
