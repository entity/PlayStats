package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishListener implements Listener {
    private final PlayStatsPlugin plugin;

    public FishListener(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerFish(PlayerFishEvent event) {
        PlayerFishEvent.State state = event.getState();

        // Only process event if state is CAUGHT_FISH or FISHING
        if (state == PlayerFishEvent.State.CAUGHT_FISH) {
            Player player = event.getPlayer();
            String caughtEntity = "none";
            String caughtItem = "none";

            Entity caught = event.getCaught();
            if (caught != null) {
                caughtEntity = caught.getType().toString();

                if (caught instanceof Item item) {
                    caughtItem = item.getType().toString();
                }
            }

            Event fishEvent = new Event("player:fish")
                    .setMetadata("state", state.toString())
                    .setMetadata("caughtEntity", caughtEntity)
                    .setMetadata("caughtItem", caughtItem);
            plugin.triggerEvent(fishEvent, player);
        }
    }
}