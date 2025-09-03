package com.tuservidor.interfacecore.managers;

import com.tuservidor.interfacecore.InterfaceCore;
import com.tuservidor.ranksystem.objects.Rank;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TablistManager {

    private final InterfaceCore plugin;
    private BukkitTask updateTask;
    private final MiniMessage miniMessage;

public TablistManager(InterfaceCore plugin) {
    this.plugin = plugin;
    this.miniMessage = MiniMessage.miniMessage();
}

    public void startUpdating() {
        this.updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                updateAllPlayers();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 40L);
    }

    public void stopUpdating() {
        if (updateTask != null) updateTask.cancel();
    }

    private void updateAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerNameAndHeaderFooter(player);
        }
    }

    private void updatePlayerNameAndHeaderFooter(Player player) {
        String worldName = player.getWorld().getName().toLowerCase();
        ConfigurationSection worldConfig = plugin.getConfig().getConfigurationSection("worlds." + worldName);

        // Si no hay configuración para este mundo, usamos la 'default'
        if (worldConfig == null) {
            worldConfig = plugin.getConfig().getConfigurationSection("default");
        }
        
        if (worldConfig == null) return; // Si tampoco hay default, no hacemos nada

        // Obtener header y footer del config
        String headerText = worldConfig.getString("tablist.header", "");
        String footerText = worldConfig.getString("tablist.footer", "");

        // Reemplazar placeholders
        footerText = footerText.replace("%online_players%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                               .replace("%player_ping%", String.valueOf(player.getPing()));
        headerText = headerText.replace("%player_name%", player.getName());

        Component header = miniMessage.deserialize(headerText);
        Component footer = miniMessage.deserialize(footerText);

        player.sendPlayerListHeaderAndFooter(header, footer);

        // Actualizar el nombre del jugador en la lista
        String rankName = plugin.getRankSystem().getPlayerDataManager().getPlayerRank(player.getUniqueId());
        Rank rank = plugin.getRankSystem().getRankManager().getRank(rankName);

        if (rank == null) {
            player.playerListName(Component.text(player.getName()));
            return;
        }

        String playerListFormat = rank.getPrefix() + " <white>" + player.getName();
        Component finalListName = miniMessage.deserialize(playerListFormat);
        player.playerListName(finalListName);
    }
}