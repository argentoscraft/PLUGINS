package com.tuserverdor.interfacecore.listeners;

import com.tuserverdor.interfacecore.InterfaceCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnectionListener implements Listener {
    private final InterfaceCore plugin;

    public PlayerConnectionListener(InterfaceCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerDataManager().loadPlayerData(player);
        if (plugin.getPlayerDataManager().hasScoreboardEnabled(player)) {
            plugin.getScoreboardManager().setScoreboard(player);
        }
        plugin.getTablistManager().setTablist(player);
    }
}