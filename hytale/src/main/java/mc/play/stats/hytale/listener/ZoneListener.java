package mc.play.stats.hytale.listener;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.event.events.ecs.DiscoverZoneEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import mc.play.stats.hytale.HytaleStatsPlugin;
import mc.play.stats.obj.Event;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Listens for zone discovery events.
 * This is a Hytale-specific event for when players discover new zones/areas.
 */
public class ZoneListener {
    private final HytaleStatsPlugin plugin;

    public ZoneListener(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(@Nonnull ComponentRegistryProxy<EntityStore> registry) {
        registry.registerSystem(new DiscoverZoneSystem());
    }

    private class DiscoverZoneSystem extends EntityEventSystem<EntityStore, DiscoverZoneEvent> {
        DiscoverZoneSystem() {
            super(DiscoverZoneEvent.class);
        }

        @Override
        public Query<EntityStore> getQuery() {
            return Query.any();
        }

        @Override
        public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                           Store<EntityStore> store,
                           CommandBuffer<EntityStore> buffer,
                           DiscoverZoneEvent event) {
            // DiscoverZoneEvent may not be cancellable - skip cancelled check
            PlayerRef playerRef = chunk.getComponent(index, PlayerRef.getComponentType());
            if (playerRef == null) {
                return;
            }

            UUID playerUuid = playerRef.getUuid();
            String playerName = playerRef.getUsername();

            EntityStore entityStore = store.getExternalData();
            World world = entityStore.getWorld();
            String worldName = world.getName();

            // Note: Zone details require further API investigation
            Event zoneDiscoverEvent = new Event("zone:discover")
                    .setMetadata("world", worldName);

            plugin.triggerEvent(zoneDiscoverEvent, playerName, playerUuid);
        }
    }
}
