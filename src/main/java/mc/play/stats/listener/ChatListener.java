package mc.play.stats.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Event;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    private final PlayStatsPlugin plugin;
    private final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();

    public ChatListener(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        String message = plainTextComponentSerializer.serialize(event.message());
        Event chatEvent = new Event("player:chat")
                .setMetadata("words", message.split(" ").length);

        Event chatContentsEvent = new Event("player:chat_contents")
                .setMetadata("message", message);

        plugin.triggerEvent(chatEvent, player);
        plugin.triggerEvent(chatContentsEvent, player);
    }
}
