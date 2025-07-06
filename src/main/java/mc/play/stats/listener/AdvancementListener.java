package mc.play.stats.listener;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementListener implements Listener {
    private final PlayStatsPlugin plugin;
    private final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();

    public AdvancementListener(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        Advancement advancement = event.getAdvancement();

        if(advancement.getDisplay() == null) {
            return;
        }

        String advancementTitle = plainTextComponentSerializer.serialize(advancement.getDisplay().title());
        Event advancementEvent = new Event("player:advancement")
                .setMetadata("advancement", advancementTitle);

        plugin.triggerEvent(advancementEvent, player);
    }
}
