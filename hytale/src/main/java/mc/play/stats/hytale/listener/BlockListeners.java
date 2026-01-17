package mc.play.stats.hytale.listener;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import mc.play.stats.hytale.HytaleStatsPlugin;
import mc.play.stats.hytale.util.BlockUtil;
import mc.play.stats.obj.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Listens for block place and break events.
 */
public class BlockListeners {
    private final HytaleStatsPlugin plugin;

    public BlockListeners(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(@Nonnull ComponentRegistryProxy<EntityStore> registry) {
        registry.registerSystem(new PlaceBlockSystem());
        registry.registerSystem(new BreakBlockSystem());
    }

    /**
     * Gets the block type name at a given position from the world chunk.
     */
    @Nullable
    private static String getBlockTypeFromWorld(World world, Vector3i blockPos) {
        long chunkIndex = ChunkUtil.indexChunkFromBlock(blockPos.getX(), blockPos.getZ());
        WorldChunk chunk = world.getChunk(chunkIndex);
        if (chunk == null) {
            return null;
        }
        BlockType blockType = chunk.getBlockType(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (blockType == null) {
            return null;
        }
        return blockType.getId();
    }

    /**
     * Gets the item ID of the player's held item.
     */
    @Nullable
    private static String getHeldItemId(Store<EntityStore> store, Ref<EntityStore> playerEntityRef) {
        if (playerEntityRef == null || !playerEntityRef.isValid()) {
            return null;
        }
        Player player = store.getComponent(playerEntityRef, Player.getComponentType());
        if (player == null) {
            return null;
        }
        Inventory inventory = player.getInventory();
        if (inventory == null) {
            return null;
        }
        ItemStack heldItem = inventory.getItemInHand();
        if (heldItem == null || heldItem.isEmpty()) {
            return null;
        }
        return heldItem.getItemId();
    }

    private class PlaceBlockSystem extends EntityEventSystem<EntityStore, PlaceBlockEvent> {
        PlaceBlockSystem() {
            super(PlaceBlockEvent.class);
        }

        @Override
        public Query<EntityStore> getQuery() {
            return Query.any();
        }

        @Override
        public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                           Store<EntityStore> store,
                           CommandBuffer<EntityStore> buffer,
                           PlaceBlockEvent event) {
            if (event.isCancelled()) {
                return;
            }

            PlayerRef playerRef = chunk.getComponent(index, PlayerRef.getComponentType());
            if (playerRef == null) {
                return;
            }

            UUID playerUuid = playerRef.getUuid();
            String playerName = playerRef.getUsername();

            EntityStore entityStore = store.getExternalData();
            World world = entityStore.getWorld();
            String worldName = world.getName();

            // Get block type from the player's held item (what they're placing)
            Ref<EntityStore> playerEntityRef = playerRef.getReference();
            String blockType = getHeldItemId(store, playerEntityRef);

            if (blockType == null || BlockUtil.shouldSkipBlock(blockType)) {
                return;
            }

            Event blockPlaceEvent = new Event("block:place")
                    .setMetadata("blockType", blockType)
                    .setMetadata("world", worldName);

            plugin.triggerEvent(blockPlaceEvent, playerName, playerUuid);
        }
    }

    private class BreakBlockSystem extends EntityEventSystem<EntityStore, BreakBlockEvent> {
        BreakBlockSystem() {
            super(BreakBlockEvent.class);
        }

        @Override
        public Query<EntityStore> getQuery() {
            return Query.any();
        }

        @Override
        public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                           Store<EntityStore> store,
                           CommandBuffer<EntityStore> buffer,
                           BreakBlockEvent event) {
            if (event.isCancelled()) {
                return;
            }

            PlayerRef playerRef = chunk.getComponent(index, PlayerRef.getComponentType());
            if (playerRef == null) {
                return;
            }

            UUID playerUuid = playerRef.getUuid();
            String playerName = playerRef.getUsername();

            EntityStore entityStore = store.getExternalData();
            World world = entityStore.getWorld();
            String worldName = world.getName();

            // Get block type from the world at the target position (before it's broken)
            Vector3i targetBlock = event.getTargetBlock();
            String blockType = getBlockTypeFromWorld(world, targetBlock);

            if (blockType == null || BlockUtil.shouldSkipBlock(blockType)) {
                return;
            }

            Event blockBreakEvent = new Event("block:break")
                    .setMetadata("blockType", blockType)
                    .setMetadata("world", worldName);

            plugin.triggerEvent(blockBreakEvent, playerName, playerUuid);
        }
    }
}
