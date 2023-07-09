package me.lostedark.aetherplugins.scoreboard.scoreboard.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ScoreboardConfig {
    private final List<String> lines;
    private final List<String> titleAnimation;
    private final int tickInterval;
    private final String scoreboardTitle;

    public ScoreboardConfig(List<String> lines, List<String> titleAnimation, int tickInterval, String scoreboardTitle) {
        this.lines = lines;
        this.titleAnimation = titleAnimation;
        this.tickInterval = tickInterval;
        this.scoreboardTitle = scoreboardTitle;
    }


    public List<String> getLines() {
        return lines;
    }

    public List<String> getTitleAnimation() {
        return titleAnimation;
    }

    public int getTickInterval() {
        return tickInterval;
    }

    public String getScoreboardTitle() {
        return scoreboardTitle;
    }

    public static ScoreboardConfig load(File dataFolder, String fileName) throws IOException {
        File configFile = new File(dataFolder, fileName);
        if (!configFile.exists()) {
            throw new IOException("Arquivo de configuração não encontrado: " + fileName);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        List<String> lines = config.getStringList("lines");
        List<String> titleAnimation = config.getStringList("scoreboard-title");
        int tickInterval = config.getInt("tick-interval");
        String scoreboardTitle = config.getString("scoreboard-title");

        return new ScoreboardConfig(lines, titleAnimation, tickInterval, scoreboardTitle);
    }
}