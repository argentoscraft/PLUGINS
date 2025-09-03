package com.tuservidor.interfacecore.tasks;

import com.tuservidor.interfacecore.InterfaceCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ClockTask extends BukkitRunnable {

    private final InterfaceCore plugin;
    private final ZoneId zoneId;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public ClockTask(InterfaceCore plugin) {
        this.plugin = plugin;
        this.zoneId = ZoneId.of(plugin.getConfig().getString("reloj.timezone", "UTC"));
    }

    @Override
    public void run() {
        if (plugin.getPlayerDataManager().getPlayersWithClockEnabled().isEmpty()) return;

        LocalTime now = LocalTime.now(zoneId);
        Component clockComponent = Component.text("Hora: ", NamedTextColor.AQUA, TextDecoration.BOLD)
            .append(Component.text(now.format(formatter), NamedTextColor.YELLOW));

        for (UUID uuid : plugin.getPlayerDataManager().getPlayersWithClockEnabled()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.sendActionBar(clockComponent);
            }
        }
    }
}