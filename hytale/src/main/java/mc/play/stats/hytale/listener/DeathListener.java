package mc.play.stats.hytale.listener;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import mc.play.stats.hytale.HytaleStatsPlugin;
import mc.play.stats.obj.Event;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Listens for player death events.
 */
public class DeathListener {
    private final HytaleStatsPlugin plugin;

    public DeathListener(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(@Nonnull ComponentRegistryProxy<EntityStore> registry) {
        registry.registerSystem(new PlayerDeathSystem());
    }

    private class PlayerDeathSystem extends DeathSystems.OnDeathSystem {
        @Override
        public Query<EntityStore> getQuery() {
            return PlayerRef.getComponentType();
        }

        @Override
        public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull DeathComponent component,
                                     @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
            PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef == null) {
                return;
            }

            UUID playerUuid = playerRef.getUuid();
            String playerName = playerRef.getUsername();

            EntityStore entityStore = store.getExternalData();
            World world = entityStore.getWorld();
            String worldName = world.getName();

            // Get death info
            Damage deathInfo = component.getDeathInfo();
            if (deathInfo == null) {
                return;
            }

            // Get cause of death
            DamageCause damageCause = component.getDeathCause();
            String deathCause = damageCause != null ? damageCause.getId() : null;

            // Get damage source for killer info
            Damage.Source source = deathInfo.getSource();

            // Get killer info if available
            String killerType = null;
            String killerName = null;
            UUID killerUuid = null;

            if (source instanceof Damage.EntitySource entitySource) {
                Ref<EntityStore> killerRef = entitySource.getRef();

                if (killerRef.isValid()) {
                    // Check if killer is a player
                    Player killerPlayer = store.getComponent(killerRef, Player.getComponentType());
                    if (killerPlayer != null) {
                        PlayerRef killerPlayerRef = store.getComponent(killerRef, PlayerRef.getComponentType());
                        if (killerPlayerRef != null) {
                            killerType = "player";
                            killerName = killerPlayerRef.getUsername();
                            killerUuid = killerPlayerRef.getUuid();
                        }
                    } else {
                        // Check if killer is an NPC
                        NPCEntity npcEntity = store.getComponent(killerRef, NPCEntity.getComponentType());
                        if (npcEntity != null) {
                            killerType = "npc";
                            killerName = npcEntity.getNPCTypeId();
                        }
                    }
                }
            }

            Event deathEvent = new Event("player:death")
                    .setMetadata("world", worldName);

            if (deathCause != null) {
                deathEvent.setMetadata("cause", deathCause);
            }

            if (killerType != null) {
                deathEvent.setMetadata("killerType", killerType);
                if (killerName != null) {
                    deathEvent.setMetadata("killerName", killerName);
                }
                if (killerUuid != null) {
                    deathEvent.setMetadata("killerUuid", killerUuid.toString());
                }
            }

            plugin.triggerEvent(deathEvent, playerName, playerUuid);
        }
    }
}
