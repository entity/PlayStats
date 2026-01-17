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
     */
    public static final Set<String> SKIP_BLOCKS = new HashSet<>();

    static {
        SKIP_BLOCKS.add("EMPTY");
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
