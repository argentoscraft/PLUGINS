package com.tuserverdor.interfacecore;

import com.tuserverdor.interfacecore.managers.ScoreboardManager;
import com.tuserverdor.ranksystem.RankSystem;
import com.tuserverdor.simpleeconomy.SimpleEconomy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class InterfaceCore extends JavaPlugin {
    private ScoreboardManager scoreboardManager;
    private RankSystem rankSystem;
    private SimpleEconomy simpleEconomy;

    @Override
    public void onEnable() {
        // Cargar dependencias
        if (!hookPlugins()) {
            getLogger().severe("Faltan dependencias! Desactivando InterfaceCore.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Cargar managers y el resto
        this.scoreboardManager = new ScoreboardManager(this);
        getServer().getScheduler().runTaskTimer(this, () -> scoreboardManager.updateAllScoreboards(), 0L, 20L);
        
        getLogger().info("InterfaceCore ha sido habilitado y conectado con sus dependencias.");
    }
    
    private boolean hookPlugins() {
        // Conexión con RankSystem
        if (getServer().getPluginManager().getPlugin("RankSystem") == null) {
            getLogger().severe("RankSystem no encontrado!");
            return false;
        }
        this.rankSystem = (RankSystem) getServer().getPluginManager().getPlugin("RankSystem");
        
        // Conexión con SimpleEconomy
        if (getServer().getPluginManager().getPlugin("SimpleEconomy") == null) {
            getLogger().severe("SimpleEconomy no encontrado!");
            return false;
        }
        this.simpleEconomy = (SimpleEconomy) getServer().getPluginManager().getPlugin("SimpleEconomy");
        
        return true;
    }

    public ScoreboardManager getScoreboardManager() { return scoreboardManager; }
    public RankSystem getRankSystem() { return rankSystem; }
    public SimpleEconomy getSimpleEconomy() { return simpleEconomy; }
}