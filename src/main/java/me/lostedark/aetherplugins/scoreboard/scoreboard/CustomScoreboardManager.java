package me.lostedark.aetherplugins.scoreboard.scoreboard;

import me.lostedark.aetherplugins.scoreboard.utils.Placeholders;
import me.lostedark.aetherplugins.scoreboard.utils.StringUtils;
import me.lostedark.aetherplugins.scoreboard.utils.VersionHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.List;

public class CustomScoreboardManager {
    private final Scoreboard scoreboard;
    private final List<String> titleAnimation;
    private final Placeholders placeholders;
    private int animationIndex;
    private Objective objective;

    public CustomScoreboardManager(List<String> titleAnimation, Placeholders placeholders) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = scoreboardManager.getNewScoreboard();
        this.titleAnimation = titleAnimation;
        this.placeholders = placeholders;
        this.animationIndex = 0;

        createObjective();
    }

    public void createScoreboard(List<String> lines, Player player) {
        int lineNumber = lines.size();
        for (String line : lines) {
            Team team = scoreboard.registerNewTeam("line" + lineNumber);
            team.addEntry(ChatColor.values()[lineNumber].toString());
            line = placeholders.replacePlaceholders(player, line);
            line = StringUtils.color(line);
            String prefix = line;

            prefix = prefix.substring(0, Math.min(prefix.length(), VersionHandler.getMaxFixLength()));
            team.setPrefix(prefix);
            objective.getScore(ChatColor.values()[lineNumber].toString()).setScore(lineNumber);
            lineNumber--;
        }
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void updateTitle(Player player) {
        String title = getNextAnimatedTitle();
        title = ChatColor.translateAlternateColorCodes('&', title);
        title = placeholders.replacePlaceholders(player, title);

        if (title.length() > VersionHandler.getMaxTitleLength()) {
            title = title.substring(0, VersionHandler.getMaxTitleLength());
        }

        objective.setDisplayName(title);
        player.setScoreboard(scoreboard);
    }

    private void createObjective() {
        objective = scoreboard.registerNewObjective("scoreboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        String animatedTitle = getNextAnimatedTitle();
        String translatedTitle = ChatColor.translateAlternateColorCodes('&', animatedTitle);

        if (translatedTitle.length() > VersionHandler.getMaxTitleLength()) {
            translatedTitle = translatedTitle.substring(0, VersionHandler.getMaxTitleLength());
        }

        objective.setDisplayName(translatedTitle);
    }

    private String getNextAnimatedTitle() {
        String title = titleAnimation.get(animationIndex);
        animationIndex++;
        if (animationIndex >= titleAnimation.size()) {
            animationIndex = 0;
        }
        return title;
    }
}
