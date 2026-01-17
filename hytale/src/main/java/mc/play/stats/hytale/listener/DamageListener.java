package mc.play.stats.hytale.listener;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import mc.play.stats.hytale.HytaleStatsPlugin;
import mc.play.stats.obj.Event;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Listens for player damage events.
 */
public class DamageListener {
    private final HytaleStatsPlugin plugin;

    public DamageListener(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(@Nonnull ComponentRegistryProxy<EntityStore> registry) {
        registry.registerSystem(new PlayerDamageSystem());
    }

    private class PlayerDamageSystem extends DamageEventSystem {
        @Override
        public Query<EntityStore> getQuery() {
            return PlayerRef.getComponentType();
        }

        @Override
        public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                           Store<EntityStore> store,
                           CommandBuffer<EntityStore> buffer,
                           Damage damage) {
            if (damage.isCancelled()) {
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

            // Get damage amount
            float amount = damage.getAmount();

            // Get damage cause
            DamageCause damageCause = damage.getCause();
            String cause = damageCause != null ? damageCause.getId() : "unknown";

            // Get attacker info if available
            String attackerType = null;
            String attackerName = null;
            UUID attackerUuid = null;

            Damage.Source source = damage.getSource();
            if (source instanceof Damage.EntitySource entitySource) {
                Ref<EntityStore> attackerRef = entitySource.getRef();

                if (attackerRef.isValid()) {
                    // Check if attacker is a player
                    Player attackerPlayer = store.getComponent(attackerRef, Player.getComponentType());
                    if (attackerPlayer != null) {
                        PlayerRef attackerPlayerRef = store.getComponent(attackerRef, PlayerRef.getComponentType());
                        if (attackerPlayerRef != null) {
                            attackerType = "player";
                            attackerName = attackerPlayerRef.getUsername();
                            attackerUuid = attackerPlayerRef.getUuid();
                        }
                    } else {
                        // Check if attacker is an NPC
                        NPCEntity npcEntity = store.getComponent(attackerRef, NPCEntity.getComponentType());
                        if (npcEntity != null) {
                            attackerType = "npc";
                            attackerName = npcEntity.getNPCTypeId();
                        }
                    }
                }
            }

            Event damageEvent = new Event("player:damage")
                    .setMetadata("amount", amount)
                    .setMetadata("cause", cause)
                    .setMetadata("world", worldName);

            if (attackerType != null) {
                damageEvent.setMetadata("attackerType", attackerType);
                if (attackerName != null) {
                    damageEvent.setMetadata("attackerName", attackerName);
                }
                if (attackerUuid != null) {
                    damageEvent.setMetadata("attackerUuid", attackerUuid.toString());
                }
            }

            plugin.triggerEvent(damageEvent, playerName, playerUuid);
        }
    }
}
