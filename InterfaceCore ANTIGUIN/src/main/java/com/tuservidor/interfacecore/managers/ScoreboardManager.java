package com.tuserverdor.interfacecore.managers;

import com.tuserverdor.interfacecore.InterfaceCore;
import com.tuserverdor.ranksystem.objects.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardManager {
    private final InterfaceCore plugin;
    private final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public ScoreboardManager(InterfaceCore plugin) {
        this.plugin = plugin;
    }
    
    public void setScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("main_sb", "dummy", "TITLE");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);
    }
    
    public void updateAllScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateScoreboard(player);
        }
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) {
            setScoreboard(player);
            objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
            if (objective == null) return;
        }
        
        // Limpiar scores viejos
        scoreboard.getEntries().forEach(scoreboard::resetScores);
        
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lARGENTOS&f&lCRAFT"));
        
        List<String> lines = new ArrayList<>();
        lines.add("&7&m--------------------");
        lines.add(" &fBienvenido, &b" + player.getName());
        lines.add(" ");

        // Obtener Rango
        String rankName = plugin.getRankSystem().getPlayerDataManager().getPlayerRank(player.getUniqueId());
        Rank rank = plugin.getRankSystem().getRankManager().getRank(rankName);
        String rankPrefix = (rank != null) ? rank.getPrefix() : "&7Default";
        lines.add(" &fRango: " + rankPrefix);

        // Obtener Dinero
        double balance = plugin.getSimpleEconomy().getDatabaseManager().getMoney(player);
        lines.add(" &fDinero: &e$" + formatter.format(balance));

        lines.add("  ");
        lines.add(" &fMundo: &a" + player.getWorld().getName());
        lines.add(" &fJugadores: &a" + Bukkit.getOnlinePlayers().size());
        lines.add("   ");
        lines.add(" &eIP: &bjugar.argentoscraft.com");
        lines.add("&7&m-------------------- ");

        // Aplicar líneas a la scoreboard
        for (int i = 0; i < lines.size(); i++) {
            objective.getScore(ChatColor.translateAlternateColorCodes('&', lines.get(i))).setScore(lines.size() - i);
        }
    }
}