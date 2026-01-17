package mc.play.stats.hytale.listener;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import mc.play.stats.hytale.HytaleStatsPlugin;
import mc.play.stats.hytale.util.BlockUtil;
import mc.play.stats.obj.Event;

import javax.annotation.Nonnull;
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

            // Note: Block type details require further API investigation
            Event blockPlaceEvent = new Event("block:place")
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

            // Note: Block type details require further API investigation
            Event blockBreakEvent = new Event("block:break")
                    .setMetadata("world", worldName);

            plugin.triggerEvent(blockBreakEvent, playerName, playerUuid);
        }
    }
}
