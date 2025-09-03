package com.tuserverdor.interfacecore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class RelojCommand implements CommandExecutor {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        LocalTime now = LocalTime.now(zoneId);
        sender.sendMessage(ChatColor.AQUA + "La hora actual del servidor es: " + ChatColor.WHITE + now.format(formatter));
        return true;
    }
}