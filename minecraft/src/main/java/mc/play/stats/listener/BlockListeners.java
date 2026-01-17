package mc.play.stats.listener;

import com.jeff_media.customblockdata.CustomBlockData;
import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.BlockAction;
import mc.play.stats.obj.Event;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

import static mc.play.stats.util.BlockUtil.SKIP_BLOCKS;

public class BlockListeners implements Listener {
    private final PlayStatsPlugin plugin;

    public BlockListeners(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    private void handleBlockPlaceEvent(Player player, Block block) {
        handleBlockEvent(player, block, BlockAction.PLACE, null);
    }

    private void handleBlockBreakEvent(Player player, Block block, BlockBreakEvent breakEvent) {
        handleBlockEvent(player, block, BlockAction.BREAK, breakEvent);
    }

    private void handleBlockEvent(Player player, Block block, BlockAction action, BlockBreakEvent breakEvent) {
        if (SKIP_BLOCKS.contains(block.getType())) {
            return;
        }

        UUID playerUUID = player.getUniqueId();
        String blockKeyString = String.format("stats_%s_%s_%s", playerUUID, action.getAction(), block.getType());
        NamespacedKey blockKey = new NamespacedKey(plugin, blockKeyString);
        PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);

        if (customBlockData.getOrDefault(blockKey, PersistentDataType.BOOLEAN, false)) {
            return;
        }

        customBlockData.set(blockKey, PersistentDataType.BOOLEAN, true);

        switch (action) {
            case PLACE:
                triggerPlaceEvent(player, block);
                break;
            case BREAK:
                triggerBreakEvent(player, block, breakEvent);
                break;
        }
    }

    private void triggerPlaceEvent(Player player, Block block) {
        Event blockPlaceEvent = new Event("block:place")
                .setMetadata("blockType", block.getType().toString())
                .setMetadata("world", player.getWorld().getName());
        plugin.triggerEvent(blockPlaceEvent, player);
    }

    private void triggerBreakEvent(Player player, Block block, BlockBreakEvent breakEvent) {
        String brokenBy = player.getInventory().getItemInMainHand().getType() == Material.AIR ? "hand" : player.getInventory().getItemInMainHand().getType().name();
        Event blockBreakEvent = new Event("block:break")
                .setMetadata("blockType", block.getType().toString())
                .setMetadata("brokenBy", brokenBy.toUpperCase())
                .setMetadata("world", player.getWorld().getName());

        int expDrop = breakEvent.getExpToDrop();
        if (expDrop > 0) {
            blockBreakEvent.setMetadata("expDrop", expDrop);
        }

        plugin.triggerEvent(blockBreakEvent, player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        handleBlockPlaceEvent(event.getPlayer(), event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        handleBlockBreakEvent(event.getPlayer(), event.getBlock(), event);
    }
}