package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {
    private final PlayStatsPlugin plugin;

    public CommandListener(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().split(" ")[0].substring(1);

        // Add exclusions for messaging
        if(command.startsWith("msg") || command.startsWith("reply") || command.startsWith("r")) {
            return;
        }

        Event commandEvent = new Event("player:command")
                .setMetadata("command", command);

        plugin.triggerEvent(commandEvent, player);
    }
}
