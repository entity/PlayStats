package mc.play.stats.hytale;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import mc.play.stats.hytale.listener.*;
import mc.play.stats.hytale.manager.PlayerStatisticHeartbeatManager;
import mc.play.stats.http.SDK;
import mc.play.stats.obj.Event;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Main plugin class for Hytale PlayStats.
 */
public class HytaleStatsPlugin extends JavaPlugin {
    private final List<Event> events;
    private SDK sdk;
    private ScheduledExecutorService scheduler;
    private PlayerStatisticHeartbeatManager playerStatisticHeartbeatManager;

    public HytaleStatsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        this.events = new ArrayList<>();
    }

    public PlayerStatisticHeartbeatManager getPlayerStatisticHeartbeatManager() {
        return playerStatisticHeartbeatManager;
    }

    @Override
    protected void setup() {
        // TODO: Load config - for now using defaults
        String secretKey = ""; // TODO: Load from config
        String baseUrl = "http://talesmp.test/api/v1"; // TODO: Load from config

        sdk = new SDK(secretKey, baseUrl);

        // Register all event listeners
        registerListeners();

        // Initialize the heartbeat manager
        playerStatisticHeartbeatManager = new PlayerStatisticHeartbeatManager(this);
    }

    private void registerListeners() {
        // Register EventRegistry listeners (player lifecycle events)
        new ActivityListeners(this).register(getEventRegistry());
        new ChatListener(this).register(getEventRegistry());
        new WorldListener(this).register(getEventRegistry());

        // Register EntityStoreRegistry listeners (ECS events)
        new BlockListeners(this).register(getEntityStoreRegistry());
        new ItemListeners(this).register(getEntityStoreRegistry());
        new UseBlockListener(this).register(getEntityStoreRegistry());
        new GameModeListener(this).register(getEntityStoreRegistry());
        new ZoneListener(this).register(getEntityStoreRegistry());
        new InventoryListener(this).register(getEntityStoreRegistry());
        new CraftListener(this).register(getEntityStoreRegistry());
        new DeathListener(this).register(getEntityStoreRegistry());
        new DamageListener(this).register(getEntityStoreRegistry());
        new KillListener(this).register(getEntityStoreRegistry());

        // Events not available in Hytale (no direct equivalents):
        // - Advancement/Achievement events
        // - Command execution events (no events dispatched)
        // - Fishing events
        // - Item consume events (eating/drinking)
        // - Enchanting events
        // - Level/XP events
        // - Portal events
        // - Respawn events (no ECS event)
        // - Shearing events
    }

    @Override
    protected void start() {
        // Start the event sending scheduler
        scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            List<Event> runEvents = new ArrayList<>(events.subList(0, Math.min(events.size(), 250)));
            if (runEvents.isEmpty()) return;

            debug("Sending events..");
            debug(SDK.getGson().toJson(runEvents));

            sdk.sendEvents(runEvents)
                    .thenAccept(success -> {
                        events.removeAll(runEvents);
                        debug("Successfully sent events.");
                    })
                    .exceptionally(throwable -> {
                        getLogger().at(Level.WARNING).log("Failed to send events: " + throwable.getMessage());
                        return null;
                    });
        }, 0, 10, TimeUnit.SECONDS);

        // Start the heartbeat manager
        playerStatisticHeartbeatManager.start();

        getLogger().at(Level.INFO).log("[PlayStats] Plugin started!");
    }

    @Override
    protected void shutdown() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
        if (playerStatisticHeartbeatManager != null) {
            playerStatisticHeartbeatManager.stop();
        }
    }

    /**
     * Triggers an event with player metadata.
     */
    public void triggerEvent(Event event, String playerName, UUID playerUuid) {
        event.setMetadata("playerName", playerName);
        event.setMetadata("playerUuid", playerUuid.toString());
        addEvent(event);
    }

    public void addEvent(Event event) {
        debug("Triggered event: " + event.toString());
        events.add(event);
    }

    public void debug(String message) {
        // TODO: Check config for debug setting
        getLogger().at(Level.INFO).log("[DEBUG] " + message);
    }

    public SDK getSdk() {
        return sdk;
    }

    public Universe getUniverse() {
        return Universe.get();
    }

    public HytaleServer getHytaleServer() {
        return HytaleServer.get();
    }
}
