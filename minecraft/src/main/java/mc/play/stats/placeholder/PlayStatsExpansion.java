package mc.play.stats.placeholder;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Leaderboard;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayStatsExpansion extends PlaceholderExpansion {
    private final PlayStatsPlugin plugin;
    private final ConcurrentHashMap<String, CachedLeaderboard> leaderboardCache = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION = 1800000; // 30 minutes
    
    // regex: lb_<leaderboard>_(username|total)_<position>
    private static final Pattern LB_PATTERN =
            Pattern.compile("^lb_([a-zA-Z0-9_]+)_(username|total)_(\\d+)$");
    
    private static class CachedLeaderboard {
        final Leaderboard leaderboard;
        final long timestamp;
        
        CachedLeaderboard(Leaderboard leaderboard) {
            this.leaderboard = leaderboard;
            this.timestamp = System.currentTimeMillis();
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_DURATION;
        }
    }

    public PlayStatsExpansion(PlayStatsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public String getIdentifier() {
        return "playstats";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    private Leaderboard getLeaderboard(String leaderboardName) {
        CachedLeaderboard cached = leaderboardCache.get(leaderboardName);
        
        if (cached != null && !cached.isExpired()) {
            return cached.leaderboard;
        }
        
        try {
            Leaderboard leaderboard = plugin.getSdk().getLeaderboards(leaderboardName).get();
            if (leaderboard != null) {
                leaderboardCache.put(leaderboardName, new CachedLeaderboard(leaderboard));
            }
            return leaderboard;
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        Matcher m = LB_PATTERN.matcher(params);

        if (!m.matches()) {
            return "";
        }

        String leaderboardName = m.group(1);
        String type = m.group(2);
        int position;
        
        try {
            position = Integer.parseInt(m.group(3));
        } catch (NumberFormatException e) {
            return "---";
        }

        if (position <= 0) {
            return "---";
        }

        Leaderboard leaderboard = getLeaderboard(leaderboardName);
        
        if (leaderboard == null || leaderboard.getData().size() < position) {
            return "---";
        }

        Leaderboard.Player lbPlayer = leaderboard.getData().get(position - 1);
        
        if (lbPlayer == null) {
            return "---";
        }

        return "username".equals(type) ? lbPlayer.getUsername() : String.valueOf(lbPlayer.getTotal());
    }
}
