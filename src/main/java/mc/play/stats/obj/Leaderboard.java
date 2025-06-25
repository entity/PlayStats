package mc.play.stats.obj;

import java.util.List;
import java.util.UUID;

/**
 * Represents an Analytics leaderboard.
 */
public class Leaderboard {
    private final int currentPage;
    private final List<Player> data;
    private final int total;

    /**
     * Constructs a Leaderboard instance.
     *
     * @param currentPage The current page number.
     * @param data The list of players on this page.
     * @param total The total number of players on the leaderboard.
     */
    public Leaderboard(int currentPage, List<Player> data, int total) {
        this.currentPage = currentPage;
        this.data = data;
        this.total = total;
    }

    /**
     * Returns the current page number.
     *
     * @return The current page number.
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Returns the list of players on this page.
     *
     * @return The list of players on this page.
     */
    public List<Player> getData() {
        return data;
    }

    /**
     * Returns the total number of players on the leaderboard.
     *
     * @return The total number of players on the leaderboard.
     */
    public int getTotal() {
        return total;
    }

    /**
     * Represents a player on the leaderboard.
     */
    public static class Player {
        private final String username;
        private final UUID uuid;
        private final int total;

        /**
         * Constructs a Player instance.
         *
         * @param username The player's name.
         * @param uuid The player's UUID.
         * @param total The player's value (score) on the leaderboard.
         */
        public Player(String username, UUID uuid, int total) {
            this.username = username;
            this.uuid = uuid;
            this.total = total;
        }

        /**
         * Returns the player's name.
         *
         * @return The player's name.
         */
        public String getUsername() {
            return username;
        }

        /**
         * Returns the player's UUID.
         *
         * @return The player's UUID.
         */
        public UUID getUniqueId() {
            return uuid;
        }

        /**
         * Returns the player's value (score) on the leaderboard.
         *
         * @return The player's value (score) on the leaderboard.
         */
        public int getTotal() {
            return total;
        }
    }
}