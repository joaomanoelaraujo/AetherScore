package me.lostedark.aetherplugins.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lostedark.aetherplugins.scoreboard.scoreboard.CustomScoreboardManager;
import me.lostedark.aetherplugins.scoreboard.scoreboard.config.ScoreboardConfig;
import me.lostedark.aetherplugins.scoreboard.type.CustomConfig;
import me.lostedark.aetherplugins.scoreboard.utils.ConfigValues;
import me.lostedark.aetherplugins.scoreboard.utils.Placeholders;
import me.lostedark.aetherplugins.scoreboard.utils.StringUtils;
import me.lostedark.aetherplugins.scoreboard.utils.VersionHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main extends JavaPlugin {

   private ConfigValues configValues;
   private CustomConfig statsConfig;
   private boolean placeholderAPI;
   private int scoreboardTaskID;

   public boolean canUsePlaceholderAPI() {
      return placeholderAPI;
   }

   public ConfigValues getConfigValues() {
      return configValues;
   }

   public CustomConfig getStatsConfig() {
      return statsConfig;
   }

   public int getScoreboardTaskID() {
      return scoreboardTaskID;
   }

   private CustomScoreboardManager scoreboardManager;
   private int tickInterval; // Intervalo de tempo em ticks para alterar o título

   public void onEnable() {

      ScoreboardConfig config;
      try {
         File configFile = new File(getDataFolder(), "scoreboard.yml");
         if (!configFile.exists()) {
            saveResource("scoreboard.yml", false);
         }
         config = ScoreboardConfig.load(getDataFolder(), "scoreboard.yml");
      } catch (IOException e) {
         getLogger().severe("Erro ao carregar scoreboard.yml: " + e.getMessage());
         return;
      }

      getConfig().options().copyDefaults(true);
      saveDefaultConfig();
      configValues = new ConfigValues(this);
      checkPlaceholderAPI();

      statsConfig = getNewConfig("stats.yml");
      statsConfig.saveDefaultConfig();

      tickInterval = config.getTickInterval();
      scoreboardManager = new CustomScoreboardManager(config.getTitleAnimation(), new Placeholders(this));
      loadScoreboard(config);

      startTitleAnimation();
   }

   private void loadScoreboard(ScoreboardConfig config) {
      Scoreboard scoreboard = scoreboardManager.getScoreboard();
      Objective objective = scoreboard.getObjective("scoreboard");
      if (objective == null) {
         objective = scoreboard.registerNewObjective("scoreboard", "dummy");
         objective.setDisplaySlot(DisplaySlot.SIDEBAR);
      }

      String title = config.getScoreboardTitle();
      title = PlaceholderAPI.setPlaceholders(null, title); // Aplica os placeholders

      if (title.length() > VersionHandler.getMaxTitleLength()) {
         title = title.substring(0, VersionHandler.getMaxTitleLength());
      }

      objective.setDisplayName(title);

      List<String> lines = config.getLines();
      int lineNumber = lines.size();
      for (String line : lines) {
         Team team = scoreboard.getTeam("line" + lineNumber);
         if (team == null) {
            team = scoreboard.registerNewTeam("line" + lineNumber);
            team.addEntry(ChatColor.values()[lineNumber].toString());
         }

         line = StringUtils.color(line);
         String prefix = PlaceholderAPI.setPlaceholders(null, line); // Aplica os placeholders
         prefix = prefix.substring(0, Math.min(prefix.length(), VersionHandler.getMaxFixLength()));
         team.setPrefix(prefix);

         objective.getScore(ChatColor.values()[lineNumber].toString()).setScore(lineNumber);
         lineNumber--;
      }

      for (Player player : Bukkit.getOnlinePlayers()) {
         player.setScoreboard(scoreboard);
      }
   }


   private CustomConfig getNewConfig(String name) {
      return new CustomConfig(this, new File(this.getDataFolder(), name));
   }

   public void checkPlaceholderAPI() {
      placeholderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
   }

   private void startTitleAnimation() {
      scoreboardTaskID = Bukkit.getScheduler().runTaskTimer(this, () -> {
         for (Player player : Bukkit.getOnlinePlayers()) {
            scoreboardManager.updateTitle(player);
         }
      }, 0, tickInterval).getTaskId();
   }
}
