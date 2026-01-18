package mc.play.stats.hytale.listener;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import mc.play.stats.hytale.HytaleStatsPlugin;
import mc.play.stats.obj.Event;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Listens for player chat events.
 */
public class ChatListener {
    private final HytaleStatsPlugin plugin;

    public ChatListener(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(@Nonnull EventRegistry eventRegistry) {
        // PlayerChatEvent is async - use registerAsyncGlobal
        eventRegistry.<String, PlayerChatEvent>registerAsyncGlobal(PlayerChatEvent.class, future ->
                future.thenApply(event -> {
                    handleChat(event);
                    return event;
                })
        );
    }

    private void handleChat(PlayerChatEvent event) {
        PlayerRef playerRef = event.getSender();

        UUID playerUuid = playerRef.getUuid();
        String playerName = playerRef.getUsername();

        Event chatEvent = new Event("player:chat")
                .setMetadata("words", event.getContent().split(" ").length);

        plugin.triggerEvent(chatEvent, playerName, playerUuid);
    }
}
