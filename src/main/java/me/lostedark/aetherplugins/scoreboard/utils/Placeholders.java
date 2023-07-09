package me.lostedark.aetherplugins.scoreboard.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lostedark.aetherplugins.scoreboard.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Placeholders {

    private final Main plugin;
    private final ConfigValues configValues;
    private final Configuration statsConfig;

    public Placeholders(Main plugin) {
        this.plugin = plugin;
        this.configValues = plugin.getConfigValues();
        this.statsConfig = plugin.getStatsConfig().getConfig();
    }

    public String replacePlaceholders(Player player, String input) {
        if (player == null) {
            return input;
        }

        UUID uuid = player.getUniqueId();

        String replacedInput = PlaceholderAPI.setPlaceholders(player, input);
        replacedInput = replacedInput.replace("{player}", player.getName())
                .replace("{displayname}", player.getDisplayName())
                .replace("{online-players}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("{max-players}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{time}", getTime())
                .replace("{x}", String.valueOf(player.getLocation().getBlockX()))
                .replace("{y}", String.valueOf(player.getLocation().getBlockY()))
                .replace("{z}", String.valueOf(player.getLocation().getBlockZ()))
                .replace("{flight}", String.valueOf(player.isFlying()))
                .replace("{health}", String.valueOf(player.getHealth()))
                .replace("{kills}", String.valueOf(getKills(uuid)))
                .replace("{deaths}", String.valueOf(getDeaths(uuid)))
                .replace("{blocks-broken}", String.valueOf(getBlocksBroken(uuid)))
                .replace("{blocks-placed}", String.valueOf(getBlocksPlaced(uuid)))
                .replace("{usedram}", getUsedRAM())
                .replace("{maxram}", getMaxRAM())
                .replace("{server-version}", Bukkit.getVersion())
                .replace("{port}", String.valueOf(Bukkit.getPort()));

        return ChatColor.translateAlternateColorCodes('&', replacedInput);
    }

    public String getUsedRAM() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        return String.valueOf(usedMemory) + " MB";
    }

    public String getMaxRAM() {
        return String.valueOf(Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " MB";
    }

    public String getTime() {
        try {
            ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of(configValues.getTimezone()));
            return DateTimeFormatter.ofPattern(configValues.getTimeFormat()).format(zdt);
        } catch (Exception e) {
            return "N/A";
        }
    }

    public int getKills(UUID uuid) {
        return statsConfig.getInt("Stats." + uuid + ".kills");
    }

    public int getDeaths(UUID uuid) {
        return statsConfig.getInt("Stats." + uuid + ".deaths");
    }

    public int getBlocksBroken(UUID uuid) {
        return statsConfig.getInt("Stats." + uuid + ".blocks-broken");
    }

    public int getBlocksPlaced(UUID uuid) {
        return statsConfig.getInt("Stats." + uuid + ".blocks-placed");
    }
}
