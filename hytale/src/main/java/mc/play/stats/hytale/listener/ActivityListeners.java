package mc.play.stats.hytale.listener;

import mc.play.stats.hytale.HytaleStatsPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Listens for player connect and disconnect events.
 * TODO: Update when Hytale API methods are documented.
 * Note: PlayerConnectEvent, PlayerReadyEvent, PlayerDisconnectEvent may have different
 * registration signatures (IBaseEvent with key types) that need investigation.
 */
public class ActivityListeners {
    private final HytaleStatsPlugin plugin;
    private final Map<UUID, Long> joinTimes = new HashMap<>();

    public ActivityListeners(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        // TODO: Register events when API is better understood
        // PlayerConnectEvent, PlayerReadyEvent, PlayerDisconnectEvent
        // These may need keyed registration: register(EventClass, key, handler)
    }

    public Map<UUID, Long> getJoinTimes() {
        return joinTimes;
    }
}
