package mc.play.stats.placeholder;

import mc.play.stats.PlayStatsPlugin;
import mc.play.stats.obj.Leaderboard;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayStatsExpansion extends PlaceholderExpansion {
    private final PlayStatsPlugin plugin;
    // regex: lb_<leaderboard>_(username|total)_<position>
    private static final Pattern LB_PATTERN =
            Pattern.compile("^lb_([a-zA-Z0-9_]+)_(username|total)_(\\d+)$");

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

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        Matcher m = LB_PATTERN.matcher(params);

        if (m.matches()) {
            String leaderboardName = m.group(1);
            String type = m.group(2);
            int position = Integer.parseInt(m.group(3));

            try {
                Leaderboard leaderboard = plugin.getSdk().getLeaderboards(leaderboardName).get();

                if (leaderboard == null) {
                    return "---";
                }

                if(leaderboard.getData().size() < position) {
                    return "---";
                }

                Leaderboard.Player lbPlayer = leaderboard.getData().get(position-1);

                if (lbPlayer == null) {
                    return "---";
                }

                if(Objects.equals(type, "username")) {
                    return lbPlayer.getUsername();
                }

                return lbPlayer.getTotal() + "";
            } catch (InterruptedException | ExecutionException e) {
                return "---";
            }
        }

        return "";
    }
}
