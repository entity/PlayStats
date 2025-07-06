package mc.play.stats.manager;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerStatisticHeartbeatManager {
    private static final int HEARTBEAT_INTERVAL = 30 * 20; // 30 seconds, assuming 20 ticks per second
    private final PlayStatsPlugin plugin;
    private final List<UUID> players;
    private final LuckPerms luckPerms;
    private ScheduledTask task;

    public PlayerStatisticHeartbeatManager(PlayStatsPlugin plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
        this.start();

        this.luckPerms = LuckPermsProvider.get();
    }

    public void addPlayer(UUID uuid) {
        players.add(uuid);
    }

    public void removePlayer(UUID uuid) {
        sendStatistics(uuid);
        players.remove(uuid);
    }

    public void start() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            addPlayer(player.getUniqueId());
        }

        task = plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin,
                scheduledTask -> {
                    int playerCount = players.size();

                    if (playerCount == 0) {
                        return;
                    }

                    for (UUID uuid : players) {
                        sendStatistics(uuid);
                    }
                },
                1,
                HEARTBEAT_INTERVAL);
    }

    public void stop() {
        if (task == null) return;
        task.cancel();
    }

    public void sendStatistics(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) {
            plugin.getLogger().warning("Player " + uuid + " is not online, cannot send statistics.");
            return;
        }

        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        int totalArrowsShotWithBow = player.getStatistic(Statistic.USE_ITEM, Material.BOW);
        int totalArrowsShotWithCrossbow = player.getStatistic(Statistic.USE_ITEM, Material.CROSSBOW);
        String rank = user != null ? user.getPrimaryGroup() : null;

        Event playerEvent = new Event("player:update")
                .setMetadata("xp", player.getTotalExperience())
                .setMetadata("level", player.getLevel())
                .setMetadata("health", player.getHealth())
                .setMetadata("food_level", player.getFoodLevel())
                .setMetadata("saturation", player.getSaturation())
                .setMetadata("exhaustion", player.getExhaustion())
                .setMetadata("rank", rank)

                .setMetadata("beds_entered", player.getStatistic(org.bukkit.Statistic.SLEEP_IN_BED))
                .setMetadata("eggs_thrown", player.getStatistic(org.bukkit.Statistic.USE_ITEM, org.bukkit.Material.EGG))
                .setMetadata("fish_caught", player.getStatistic(org.bukkit.Statistic.FISH_CAUGHT))
                .setMetadata("player_kills", player.getStatistic(org.bukkit.Statistic.PLAYER_KILLS))
                .setMetadata("mob_kills", player.getStatistic(org.bukkit.Statistic.MOB_KILLS))

                .setMetadata("arrows_shot_bow", totalArrowsShotWithBow)
                .setMetadata("arrows_shot_crossbow", totalArrowsShotWithCrossbow)
                .setMetadata("arrows_shot_total", totalArrowsShotWithBow + totalArrowsShotWithCrossbow)

                .setMetadata("glide_distance", player.getStatistic(org.bukkit.Statistic.AVIATE_ONE_CM) / 100.0)
                .setMetadata("walk_distance", player.getStatistic(org.bukkit.Statistic.WALK_ONE_CM) / 100.0)
                .setMetadata("sprint_distance", player.getStatistic(org.bukkit.Statistic.SPRINT_ONE_CM) / 100.0)
                .setMetadata("swim_distance", player.getStatistic(org.bukkit.Statistic.SWIM_ONE_CM) / 100.0)
                .setMetadata("fall_distance", player.getStatistic(org.bukkit.Statistic.FALL_ONE_CM) / 100.0)
                .setMetadata("climb_distance", player.getStatistic(org.bukkit.Statistic.CLIMB_ONE_CM) / 100.0)
                .setMetadata("dive_distance", player.getStatistic(org.bukkit.Statistic.SWIM_ONE_CM) / 100.0)
                .setMetadata("minecart_distance", player.getStatistic(org.bukkit.Statistic.MINECART_ONE_CM) / 100.0)
                .setMetadata("boat_distance", player.getStatistic(org.bukkit.Statistic.BOAT_ONE_CM) / 100.0)
                .setMetadata("pig_distance", player.getStatistic(org.bukkit.Statistic.PIG_ONE_CM) / 100.0)
                .setMetadata("horse_distance", player.getStatistic(org.bukkit.Statistic.HORSE_ONE_CM) / 100.0)
                .setMetadata("strider_distance", player.getStatistic(org.bukkit.Statistic.STRIDER_ONE_CM) / 100.0)
                .setMetadata("trident_thrown", player.getStatistic(org.bukkit.Statistic.USE_ITEM, org.bukkit.Material.TRIDENT))
                .setMetadata("dispenser_inspected", player.getStatistic(org.bukkit.Statistic.DISPENSER_INSPECTED))
                .setMetadata("dropper_inspected", player.getStatistic(org.bukkit.Statistic.DROPPER_INSPECTED))
                .setMetadata("hopper_inspected", player.getStatistic(org.bukkit.Statistic.HOPPER_INSPECTED))
                .setMetadata("item_enchanted", player.getStatistic(org.bukkit.Statistic.ITEM_ENCHANTED))
                .setMetadata("furnace_interaction", player.getStatistic(org.bukkit.Statistic.FURNACE_INTERACTION))
                .setMetadata("crafting_table_interaction", player.getStatistic(org.bukkit.Statistic.CRAFTING_TABLE_INTERACTION))
                .setMetadata("chest_opened", player.getStatistic(org.bukkit.Statistic.CHEST_OPENED))
                .setMetadata("trapped_chest_triggered", player.getStatistic(org.bukkit.Statistic.TRAPPED_CHEST_TRIGGERED))
                .setMetadata("enderchest_opened", player.getStatistic(org.bukkit.Statistic.ENDERCHEST_OPENED))
                .setMetadata("shulker_box_opened", player.getStatistic(org.bukkit.Statistic.SHULKER_BOX_OPENED))
                .setMetadata("barrel_opened", player.getStatistic(Statistic.OPEN_BARREL));

        plugin.triggerEvent(playerEvent, player);
    }
}