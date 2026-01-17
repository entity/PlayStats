package mc.play.stats.hytale.manager;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import mc.play.stats.hytale.HytaleStatsPlugin;
import mc.play.stats.obj.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Manages periodic heartbeat statistics collection for online players.
 */
public class PlayerStatisticHeartbeatManager {
    private static final int HEARTBEAT_INTERVAL_SECONDS = 30;
    private final HytaleStatsPlugin plugin;
    private final List<UUID> players;
    private ScheduledExecutorService scheduler;

    public PlayerStatisticHeartbeatManager(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
    }

    public void addPlayer(UUID uuid) {
        if (!players.contains(uuid)) {
            players.add(uuid);
        }
    }

    public void removePlayer(UUID uuid) {
        sendStatistics(uuid);
        players.remove(uuid);
    }

    public void start() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if (players.isEmpty()) {
                return;
            }

            for (UUID uuid : new ArrayList<>(players)) {
                try {
                    sendStatistics(uuid);
                } catch (Exception e) {
                    plugin.debug("Error sending statistics for player " + uuid + ": " + e.getMessage());
                }
            }
        }, HEARTBEAT_INTERVAL_SECONDS, HEARTBEAT_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    public void sendStatistics(UUID uuid) {
        PlayerRef playerRef = Universe.get().getPlayer(uuid);
        if (playerRef == null) {
            return;
        }

        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null || !ref.isValid()) {
            return;
        }

        Store<EntityStore> store = ref.getStore();
        if (store == null) {
            return;
        }

        EntityStore entityStore = store.getExternalData();
        if (entityStore == null) {
            return;
        }

        World world = entityStore.getWorld();
        if (world == null) {
            return;
        }

        // Execute on the world thread to safely access ECS components
        world.execute(() -> collectAndSendStatistics(playerRef, ref, store, world));
    }

    private void collectAndSendStatistics(PlayerRef playerRef, Ref<EntityStore> ref,
                                          Store<EntityStore> store, World world) {
        if (!ref.isValid()) {
            return;
        }

        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) {
            return;
        }

        String playerName = playerRef.getUsername();
        UUID uuid = playerRef.getUuid();

        Event playerEvent = new Event("player:update");

        // Game mode
        GameMode gameMode = player.getGameMode();
        if (gameMode != null) {
            playerEvent.setMetadata("gamemode", gameMode.name().toLowerCase());
        }

        // World
        playerEvent.setMetadata("world", world.getName());

        // Position
        TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
        if (transform != null) {
            Vector3d position = transform.getPosition();
            if (position != null) {
                playerEvent.setMetadata("pos_x", position.getX());
                playerEvent.setMetadata("pos_y", position.getY());
                playerEvent.setMetadata("pos_z", position.getZ());
            }
        }

        // Entity stats (health, stamina, mana, oxygen)
        EntityStatMap statMap = store.getComponent(ref, EntityStatMap.getComponentType());
        if (statMap != null) {
            // Health
            EntityStatValue healthStat = statMap.get(DefaultEntityStatTypes.getHealth());
            if (healthStat != null) {
                playerEvent.setMetadata("health", healthStat.get());
                playerEvent.setMetadata("health_max", healthStat.getMax());
            }

            // Stamina
            EntityStatValue staminaStat = statMap.get(DefaultEntityStatTypes.getStamina());
            if (staminaStat != null) {
                playerEvent.setMetadata("stamina", staminaStat.get());
                playerEvent.setMetadata("stamina_max", staminaStat.getMax());
            }

            // Mana
            EntityStatValue manaStat = statMap.get(DefaultEntityStatTypes.getMana());
            if (manaStat != null) {
                playerEvent.setMetadata("mana", manaStat.get());
                playerEvent.setMetadata("mana_max", manaStat.getMax());
            }

            // Oxygen
            EntityStatValue oxygenStat = statMap.get(DefaultEntityStatTypes.getOxygen());
            if (oxygenStat != null) {
                playerEvent.setMetadata("oxygen", oxygenStat.get());
                playerEvent.setMetadata("oxygen_max", oxygenStat.getMax());
            }
        }

        plugin.triggerEvent(playerEvent, playerName, uuid);
    }
}
