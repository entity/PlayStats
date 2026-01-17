package mc.play.stats.hytale.manager;

import mc.play.stats.hytale.HytaleStatsPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Manages periodic heartbeat statistics collection for online players.
 * TODO: Implement player statistics when Hytale API methods are documented.
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
        // Start the heartbeat scheduler
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if (players.isEmpty()) {
                return;
            }

            for (UUID uuid : new ArrayList<>(players)) {
                sendStatistics(uuid);
            }
        }, HEARTBEAT_INTERVAL_SECONDS, HEARTBEAT_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    public void sendStatistics(UUID uuid) {
        // TODO: Implement when Hytale API methods are documented
        // Need to:
        // 1. Get player from UUID via Universe
        // 2. Collect player statistics (health, position, world, game mode, inventory)
        // 3. Send via plugin.triggerEvent()
    }
}
