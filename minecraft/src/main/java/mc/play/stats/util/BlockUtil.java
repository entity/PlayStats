package mc.play.stats.util;

import org.bukkit.Material;

import java.util.EnumSet;
import java.util.Set;

public class BlockUtil {
    public static boolean isClimbing(Material blockType) {
        return blockType == Material.LADDER || blockType == Material.VINE || blockType == Material.SCAFFOLDING;
    }

    public static final Set<Material> SKIP_BLOCKS = EnumSet.of(
            Material.BARRIER,
            Material.COMMAND_BLOCK,
            Material.COMMAND_BLOCK_MINECART,
            Material.CHAIN_COMMAND_BLOCK,
            Material.REPEATING_COMMAND_BLOCK,
            Material.STRUCTURE_BLOCK,
            Material.STRUCTURE_VOID,
            Material.JIGSAW,
            Material.DEBUG_STICK,
            Material.END_PORTAL,
            Material.END_GATEWAY,
            Material.BEDROCK,
            Material.PETRIFIED_OAK_SLAB,
            Material.LIGHT,
            Material.KNOWLEDGE_BOOK,
            Material.TALL_GRASS,
            Material.SHORT_GRASS,
            Material.FERN,
            Material.LARGE_FERN,
            Material.DEAD_BUSH,
            Material.DANDELION,
            Material.POPPY,
            Material.BLUE_ORCHID,
            Material.ALLIUM,
            Material.AZURE_BLUET,
            Material.RED_TULIP,
            Material.ORANGE_TULIP,
            Material.WHITE_TULIP,
            Material.PINK_TULIP,
            Material.OXEYE_DAISY,
            Material.CORNFLOWER,
            Material.LILY_OF_THE_VALLEY,
            Material.WITHER_ROSE,
            Material.SUNFLOWER,
            Material.LILAC,
            Material.ROSE_BUSH,
            Material.PEONY,
            Material.VINE,
            Material.SEAGRASS,
            Material.TALL_SEAGRASS,
            Material.KELP,
            Material.COBWEB,
            Material.MOSS_CARPET,
            Material.MOSS_BLOCK,
            Material.CAVE_AIR,
            Material.VOID_AIR
    );
}
