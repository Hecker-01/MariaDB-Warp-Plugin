package net.heckerdev.mariadbwarpplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static net.heckerdev.mariadbwarpplugin.database.DataSource.getConnection;

@CommandAlias("warplist|warpl")
public class WarplistCommand extends BaseCommand {

    @Default
    @Syntax("[page]")
    @CommandCompletion("1|2|3")
    public void onDefault(CommandSender sender, String[] args) {
        if (!sender.hasPermission("warpplugin.command.WarplistCommand")) {
            sender.sendMessage("§c&l⚠&c You do not have permission to use this command!");
        } else {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6-------------&ePage: 1&6--------------"));
                try {
                    Connection connection = getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM WarpsTable WHERE Hidden != true LIMIT 10");
                    ResultSet rows = preparedStatement.executeQuery();
                    int warps = 0;
                    while (rows.next()) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + rows.getString("WarpName")));
                        warps++;
                    }
                    rows.close();
                    connection.close();
                } catch (SQLException ex) {
                    Bukkit.getLogger().warning("Error getting warps: " + ex);
                    sender.sendMessage("§c&l❌&c An error occurred while getting the warps, check the console for more information!");
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6---------------------------------"));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6-------------Page: " + args[0] + "--------------"));
                int page = Integer.parseInt(args[0]);
                if (page == 1) {
                    sender.sendMessage("§a&l✔&a Warps: §6test");
                } else {
                    sender.sendMessage("§c&l⚠&c That page does not exist!");
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6---------------------------------"));
            }
        }
    }
}
