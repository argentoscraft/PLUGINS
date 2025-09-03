package com.tuserverdor.interfacecore.commands;

import com.tuserverdor.interfacecore.InterfaceCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreboardToggleCommand implements CommandExecutor {
    private final InterfaceCore plugin;

    public ScoreboardToggleCommand(InterfaceCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }
        Player player = (Player) sender;
        boolean newState = plugin.getPlayerDataManager().toggleScoreboard(player);
        if (newState) {
            player.sendMessage(ChatColor.GREEN + "Scoreboard activado.");
            plugin.getScoreboardManager().setScoreboard(player);
        } else {
            player.sendMessage(ChatColor.RED + "Scoreboard desactivado.");
            player.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard()); // Limpia la scoreboard
        }
        return true;
    }
}