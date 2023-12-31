package me.lostedark.aetherplugins.scoreboard.events;


import me.lostedark.aetherplugins.scoreboard.Main;
import me.lostedark.aetherplugins.scoreboard.type.CustomConfig;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    private final Main instance;
    private final CustomConfig statsConfig;

    public DeathEvent(Main instance) {
        this.instance = instance;
        this.statsConfig = instance.getStatsConfig();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(instance.getConfigValues().getDisableStats())
            return;

        Player killed = event.getEntity();
        Entity killer = killed.getKiller();
        Configuration config = statsConfig.getConfig();
        String killedPath = "Stats." + killed.getUniqueId() + ".deaths";
        int killedPrevCount = config.getInt(killedPath);

        config.set(killedPath, ++killedPrevCount);

        if(killer != null) {
            String killerPath = "Stats." + killer.getUniqueId() + ".kills";
            int killerPrevCount = config.getInt(killerPath);
            config.set(killerPath, ++killerPrevCount);
        }

        statsConfig.saveConfig();
    }
}