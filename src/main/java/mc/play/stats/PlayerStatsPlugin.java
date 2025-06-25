package mc.play.stats;

import com.google.common.collect.Lists;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import mc.play.stats.http.SDK;
import mc.play.stats.listener.*;
import mc.play.stats.manager.PlayerStatisticHeartbeatManager;
import mc.play.stats.obj.Event;

import mc.play.stats.placeholder.PlayStatsExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlayerStatsPlugin extends JavaPlugin {
    private final List<Event> events;
    private SDK sdk;
    private ScheduledTask task;
    private PlayerStatisticHeartbeatManager playerStatisticHeartbeatManager;

    public PlayerStatsPlugin() {
        this.events = new ArrayList<>();
    }

    public PlayerStatisticHeartbeatManager getPlayerStatisticHeartbeatManager() {
        return playerStatisticHeartbeatManager;
    }

    @Override
    public void onEnable() {
        sdk = new SDK("TO-BE-CHANGED");

        task = getServer().getAsyncScheduler().runAtFixedRate(this,
                scheduledTask -> {
                    List<Event> runEvents = Lists.newArrayList(events.subList(0, Math.min(events.size(), 250)));
                    if (runEvents.isEmpty()) return;

                    getLogger().info("Sending events..");
                    getLogger().info(SDK.getGson().toJson(runEvents));

                    sdk.sendEvents(runEvents)
                            .thenAccept(aVoid -> {
                                events.removeAll(runEvents);
                                getLogger().info("Successfully sent events.");
                            })
                            .exceptionally(throwable -> {
                                getLogger().info("Failed to send join events: " + throwable.getMessage());
                                return null;
                            });
                },
                0, 10, TimeUnit.SECONDS);

        Arrays.asList(
                new ActivityListeners(this),
                new AdvancementListener(this),
                new AnvilUseListener(this),
                new BedListener(this),
                new BlockListeners(this),
                new ChatListener(this),
                new CommandListener(this),
                new FishListener(this),
                new ItemConsumeListener(this),
                new ItemEnchantListener(this),
                new ItemListeners(this),
                new LevelUpListener(this),
                new PlayerDamageListener(this),
                new PlayerDeathListener(this),
                new PlayerKillListener(this),
                new PortalListeners(this),
                new RespawnListener(this),
                new ShearListener(this),
                new CraftListener(this)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        // Load the PlayerStatisticHeartbeatManager
        playerStatisticHeartbeatManager = new PlayerStatisticHeartbeatManager(this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlayStatsExpansion(this).register();
        }

        sdk.getLeaderboards("").thenAccept(leaderboards -> {
           getLogger().info("Showing leaderboards.");
            leaderboards.getData().forEach(player -> {
               getLogger().info(player.getUsername() + " - " + player.getTotal());
            });
        }).exceptionally(err -> {
            getLogger().info("Failed to get leaderboards: " + err.getMessage());

            return null;
        });
    }

    @Override
    public void onDisable() {
        task.cancel();
        playerStatisticHeartbeatManager.stop();
    }

    public void triggerEvent(Event event, Player player) {
        event.setMetadata("playerName", player.getName());
        event.setMetadata("playerUuid", player.getUniqueId().toString());

        addEvent(event);
    }

    public void addEvent(Event event) {
        getLogger().info("Triggered event: " + event.toString());
        events.add(event);
    }

    public SDK getSdk() {
        return sdk;
    }
}