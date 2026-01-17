package mc.play.stats.hytale.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for block-related operations.
 * TODO: Update with Hytale block types when API is available.
 */
public class BlockUtil {

    /**
     * Set of block types that should be skipped for tracking.
     * TODO: Replace with Hytale block type enum/identifiers.
     */
    public static final Set<String> SKIP_BLOCKS = new HashSet<>();

    static {
        // Administrative/technical blocks
        SKIP_BLOCKS.add("BARRIER");
        SKIP_BLOCKS.add("COMMAND_BLOCK");
        SKIP_BLOCKS.add("STRUCTURE_BLOCK");
        SKIP_BLOCKS.add("STRUCTURE_VOID");
        SKIP_BLOCKS.add("BEDROCK");
        SKIP_BLOCKS.add("LIGHT");

        // Natural vegetation (too common to track individually)
        SKIP_BLOCKS.add("TALL_GRASS");
        SKIP_BLOCKS.add("SHORT_GRASS");
        SKIP_BLOCKS.add("GRASS");
        SKIP_BLOCKS.add("FERN");
        SKIP_BLOCKS.add("LARGE_FERN");
        SKIP_BLOCKS.add("DEAD_BUSH");

        // Flowers
        SKIP_BLOCKS.add("DANDELION");
        SKIP_BLOCKS.add("POPPY");
        SKIP_BLOCKS.add("BLUE_ORCHID");
        SKIP_BLOCKS.add("ALLIUM");
        SKIP_BLOCKS.add("AZURE_BLUET");
        SKIP_BLOCKS.add("RED_TULIP");
        SKIP_BLOCKS.add("ORANGE_TULIP");
        SKIP_BLOCKS.add("WHITE_TULIP");
        SKIP_BLOCKS.add("PINK_TULIP");
        SKIP_BLOCKS.add("OXEYE_DAISY");
        SKIP_BLOCKS.add("CORNFLOWER");
        SKIP_BLOCKS.add("LILY_OF_THE_VALLEY");
        SKIP_BLOCKS.add("SUNFLOWER");
        SKIP_BLOCKS.add("LILAC");
        SKIP_BLOCKS.add("ROSE_BUSH");
        SKIP_BLOCKS.add("PEONY");

        // Aquatic vegetation
        SKIP_BLOCKS.add("SEAGRASS");
        SKIP_BLOCKS.add("TALL_SEAGRASS");
        SKIP_BLOCKS.add("KELP");

        // Other
        SKIP_BLOCKS.add("VINE");
        SKIP_BLOCKS.add("COBWEB");
        SKIP_BLOCKS.add("MOSS_CARPET");
        SKIP_BLOCKS.add("MOSS_BLOCK");
        SKIP_BLOCKS.add("CAVE_AIR");
        SKIP_BLOCKS.add("VOID_AIR");
        SKIP_BLOCKS.add("AIR");
    }

    /**
     * Checks if a block type should be skipped for tracking.
     *
     * @param blockType The block type identifier.
     * @return true if the block should be skipped, false otherwise.
     */
    public static boolean shouldSkipBlock(String blockType) {
        return SKIP_BLOCKS.contains(blockType.toUpperCase());
    }

    /**
     * Checks if a block type is climbable.
     * TODO: Update with Hytale climbable block types.
     *
     * @param blockType The block type identifier.
     * @return true if the block is climbable, false otherwise.
     */
    public static boolean isClimbing(String blockType) {
        String type = blockType.toUpperCase();
        return type.equals("LADDER") || type.equals("VINE") || type.equals("SCAFFOLDING");
    }
}
