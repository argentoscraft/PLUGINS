package com.tuservidor.interfacecore.managers;

import com.tuservidor.interfacecore.InterfaceCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerDataManager {

    private final InterfaceCore plugin;
    private final Set<UUID> scoreboardEnabled = new HashSet<>();
    private final Set<UUID> clockEnabled = new HashSet<>();
    private File dataFile;
    private FileConfiguration dataConfig;

    public PlayerDataManager(InterfaceCore plugin) {
        this.plugin = plugin;
        setupDataFile();
        loadData();
    }

    private void setupDataFile() {
        dataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        if (!dataFile.exists()) {
            try { dataFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void loadData() {
        dataConfig.getStringList("scoreboard-enabled").forEach(uuid -> scoreboardEnabled.add(UUID.fromString(uuid)));
        dataConfig.getStringList("clock-enabled").forEach(uuid -> clockEnabled.add(UUID.fromString(uuid)));
    }

    public void saveData() {
        dataConfig.set("scoreboard-enabled", scoreboardEnabled.stream().map(UUID::toString).collect(Collectors.toList()));
        dataConfig.set("clock-enabled", clockEnabled.stream().map(UUID::toString).collect(Collectors.toList()));
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Métodos para la Scoreboard
    public boolean hasScoreboardEnabled(Player player) { return scoreboardEnabled.contains(player.getUniqueId()); }
    public void toggleScoreboard(Player player) {
        if (hasScoreboardEnabled(player)) {
            scoreboardEnabled.remove(player.getUniqueId());
        } else {
            scoreboardEnabled.add(player.getUniqueId());
        }
    }

    // Métodos para el Reloj
    public boolean hasClockEnabled(Player player) { return clockEnabled.contains(player.getUniqueId()); }
    public void toggleClock(Player player) {
        if (hasClockEnabled(player)) {
            clockEnabled.remove(player.getUniqueId());
        } else {
            clockEnabled.add(player.getUniqueId());
        }
    }
    
    public Set<UUID> getPlayersWithClockEnabled() {
        return clockEnabled;
    }
}