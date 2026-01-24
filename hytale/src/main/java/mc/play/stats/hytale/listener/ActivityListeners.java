package mc.play.stats.hytale.listener;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.protocol.HostAddress;
import com.hypixel.hytale.server.core.auth.PlayerAuthentication;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import mc.play.stats.hytale.HytaleStatsPlugin;
import mc.play.stats.obj.Event;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Listens for player connect and disconnect events.
 */
public class ActivityListeners {
    private final HytaleStatsPlugin plugin;
    private final Map<UUID, Long> joinTimes = new ConcurrentHashMap<>();

    public ActivityListeners(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(@Nonnull EventRegistry eventRegistry) {
        // PlayerConnectEvent and PlayerDisconnectEvent have Void key type (global events)
        eventRegistry.registerGlobal(PlayerConnectEvent.class, this::onPlayerConnect);
        eventRegistry.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);

        // PlayerReadyEvent has String key type (world name) - register globally to catch all worlds
        eventRegistry.registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
    }

    private void onPlayerConnect(PlayerConnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        UUID playerUuid = playerRef.getUuid();

        // Store join time for calculating play time on quit
        joinTimes.put(playerUuid, System.currentTimeMillis());
    }

    private void onPlayerReady(PlayerReadyEvent event) {
        Ref<EntityStore> ref = event.getPlayerRef();
        if (!ref.isValid()) {
            return;
        }

        Store<EntityStore> store = ref.getStore();
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) {
            return;
        }

        UUID playerUuid = playerRef.getUuid();
        String playerName = playerRef.getUsername();

        // Add player to heartbeat manager for statistics tracking
        plugin.getPlayerStatisticHeartbeatManager().addPlayer(playerUuid);

        // Trigger join event
        Event joinEvent = new Event("player:join")
                .setMetadata("lastJoined", System.currentTimeMillis());

        PacketHandler packetHandler = playerRef.getPacketHandler();
        PlayerAuthentication auth = packetHandler.getAuth();

        if(auth != null) {
            HostAddress referralSource = auth.getReferralSource();
            if(referralSource != null) {
                String domain = referralSource.host;
                int port = referralSource.port;

                joinEvent.setMetadata("domain", domain + ":" + port);
            }
        }

        plugin.triggerEvent(joinEvent, playerName, playerUuid);
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        UUID playerUuid = playerRef.getUuid();
        String playerName = playerRef.getUsername();

        // Calculate play time
        Long joinTime = joinTimes.remove(playerUuid);
        long playTime = joinTime != null ? System.currentTimeMillis() - joinTime : 0;

        // Trigger quit event
        Event quitEvent = new Event("player:quit")
                .setMetadata("lastJoined", System.currentTimeMillis())
                .setMetadata("playTime", playTime);

        plugin.triggerEvent(quitEvent, playerName, playerUuid);

        // Remove player from heartbeat manager
        plugin.getPlayerStatisticHeartbeatManager().removePlayer(playerUuid);
    }

    public Map<UUID, Long> getJoinTimes() {
        return joinTimes;
    }
}
