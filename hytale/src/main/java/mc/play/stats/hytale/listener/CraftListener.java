package mc.play.stats.hytale.listener;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.asset.type.item.config.CraftingRecipe;
import com.hypixel.hytale.server.core.event.events.ecs.CraftRecipeEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import mc.play.stats.hytale.HytaleStatsPlugin;
import mc.play.stats.obj.Event;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Listens for crafting events.
 */
public class CraftListener {
    private final HytaleStatsPlugin plugin;

    public CraftListener(HytaleStatsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(@Nonnull ComponentRegistryProxy<EntityStore> registry) {
        registry.registerSystem(new CraftRecipeSystem());
    }

    private class CraftRecipeSystem extends EntityEventSystem<EntityStore, CraftRecipeEvent.Post> {
        CraftRecipeSystem() {
            super(CraftRecipeEvent.Post.class);
        }

        @Override
        public Query<EntityStore> getQuery() {
            return Query.any();
        }

        @Override
        public void handle(int index, ArchetypeChunk<EntityStore> chunk,
                           Store<EntityStore> store,
                           CommandBuffer<EntityStore> buffer,
                           CraftRecipeEvent.Post event) {
            if (event.isCancelled()) {
                return;
            }

            PlayerRef playerRef = chunk.getComponent(index, PlayerRef.getComponentType());
            if (playerRef == null) {
                return;
            }

            UUID playerUuid = playerRef.getUuid();
            String playerName = playerRef.getUsername();

            EntityStore entityStore = store.getExternalData();
            World world = entityStore.getWorld();
            String worldName = world.getName();

            CraftingRecipe craftedRecipe = event.getCraftedRecipe();

            Event craftEvent = new Event("player:craft")
                    .setMetadata("item", craftedRecipe.getPrimaryOutput().getItemId())
                    .setMetadata("world", worldName);

            plugin.triggerEvent(craftEvent, playerName, playerUuid);
        }
    }
}
