package mc.play.stats.hytale.listener;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.event.KillFeedEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import mc.play.stats.hytale.HytaleStatsPlugin;
import mc.play.stats.obj.Event;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Listens for kill events (when a player kills something).
 */
public class KillListener {
    private final HytaleStatsPlugin plugin;

    public KillListener(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(@Nonnull ComponentRegistryProxy<EntityStore> registry) {
        registry.registerSystem(new PlayerKillSystem());
    }

    /**
     * Listens for KillFeedEvent.KillerMessage which fires on the killer entity
     * when they kill something.
     */
    private class PlayerKillSystem extends EntityEventSystem<EntityStore, KillFeedEvent.KillerMessage> {
        PlayerKillSystem() {
            super(KillFeedEvent.KillerMessage.class);
        }

        @Override
        public Query<EntityStore> getQuery() {
            return PlayerRef.getComponentType();
        }

        @Override
        public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                           Store<EntityStore> store,
                           CommandBuffer<EntityStore> buffer,
                           KillFeedEvent.KillerMessage event) {
            if (event.isCancelled()) {
                return;
            }

            // The killer (this entity)
            PlayerRef killerPlayerRef = chunk.getComponent(index, PlayerRef.getComponentType());
            if (killerPlayerRef == null) {
                return;
            }

            UUID killerUuid = killerPlayerRef.getUuid();
            String killerName = killerPlayerRef.getUsername();

            EntityStore entityStore = store.getExternalData();
            World world = entityStore.getWorld();
            String worldName = world.getName();

            // Get damage info
            Damage damage = event.getDamage();
            DamageCause damageCause = damage != null ? damage.getCause() : null;
            String cause = damageCause != null ? damageCause.getId() : "unknown";

            // Get victim info from the target ref
            Ref<EntityStore> victimRef = event.getTargetRef();
            String victimType = null;
            String victimName = null;
            UUID victimUuid = null;

            if (victimRef != null && victimRef.isValid()) {
                // Check if victim is a player
                Player victimPlayer = store.getComponent(victimRef, Player.getComponentType());
                if (victimPlayer != null) {
                    PlayerRef victimPlayerRef = store.getComponent(victimRef, PlayerRef.getComponentType());
                    if (victimPlayerRef != null) {
                        victimType = "player";
                        victimName = victimPlayerRef.getUsername();
                        victimUuid = victimPlayerRef.getUuid();
                    }
                } else {
                    // Check if victim is an NPC
                    NPCEntity npcEntity = store.getComponent(victimRef, NPCEntity.getComponentType());
                    if (npcEntity != null) {
                        victimType = "npc";
                        victimName = npcEntity.getNPCTypeId();
                    }
                }
            }

            Event killEvent = new Event("player:kill")
                    .setMetadata("cause", cause)
                    .setMetadata("world", worldName);

            if (victimType != null) {
                killEvent.setMetadata("victimType", victimType);
                if (victimName != null) {
                    killEvent.setMetadata("victimName", victimName);
                }
                if (victimUuid != null) {
                    killEvent.setMetadata("victimUuid", victimUuid.toString());
                }
            }

            plugin.triggerEvent(killEvent, killerName, killerUuid);
        }
    }
}
