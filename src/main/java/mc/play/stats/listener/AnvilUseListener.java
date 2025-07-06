package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.stream.Collectors;

public class AnvilUseListener implements Listener {
    private final PlayStatsPlugin plugin;

    public AnvilUseListener(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("removal")
    @EventHandler(priority = EventPriority.MONITOR)
    public void onAnvilUse(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (event.getInventory().getType() != InventoryType.ANVIL) return;

        AnvilInventory inventory = (AnvilInventory) event.getInventory();
        if (event.getSlotType() != InventoryType.SlotType.RESULT) return;

        // Ensure the click type is one that would take the result item
        if (event.getClick() != ClickType.LEFT && event.getClick() != ClickType.RIGHT) return;

        ItemStack result = event.getCurrentItem();
        if (result == null || result.getType() == Material.AIR) return;

        // Check if player has enough levels to complete the operation
        if (player.getLevel() < inventory.getRepairCost()) return;

        // Ensure the result item is being taken (meaning the operation is complete)
        if (event.getCursor().getType() != Material.AIR) return;

        ItemStack firstItem = inventory.getItem(0);
        ItemStack secondItem = inventory.getItem(1);
        int expCost = inventory.getRepairCost();

        // Check if the first and second items have been consumed
        if (firstItem == null || firstItem.getType() == Material.AIR) return;
        if (secondItem != null && secondItem.getType() != Material.AIR && secondItem.getAmount() > 1) return;

        // Collect detailed metadata
        String firstItemType = firstItem.getType().toString();
        String secondItemType = secondItem != null ? secondItem.getType().toString() : "none";
        String enchantments = "";

        if (secondItem != null && secondItem.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) secondItem.getItemMeta();
            if (meta != null) {
                enchantments = meta.getStoredEnchants().entrySet().stream()
                        .map(entry -> entry.getKey().getKey().getKey() + " " + entry.getValue())
                        .collect(Collectors.joining(", "));
            }
        }

        Event anvilEvent = new Event("player:use_anvil")
                .setMetadata("resultItem", result.getType().toString())
                .setMetadata("firstItem", firstItemType)
                .setMetadata("secondItem", secondItemType)
                .setMetadata("expCost", String.valueOf(expCost))
                .setMetadata("enchantments", enchantments);

        plugin.getLogger().info(anvilEvent.toString());

        plugin.triggerEvent(anvilEvent, player);
    }
}