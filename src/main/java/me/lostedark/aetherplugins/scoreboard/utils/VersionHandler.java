package me.lostedark.aetherplugins.scoreboard.utils;

import org.bukkit.Bukkit;

public class VersionHandler {

    private VersionHandler() { }

    public static int getMaxFixLength() {
        return colorsWillDisappear() ? 64 : 16;
    }

    public static int getMaxTitleLength() {
        return colorsWillDisappear() ? 128 : 32;
    }

    public static boolean colorsWillDisappear() {
        String[] version = Bukkit.getBukkitVersion().split("\\.");
        int v = Integer.parseInt(version[0] + version[1]);
        return v >= 113;
    }
}
