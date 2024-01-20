package net.heckerdev.mariadbwarpplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static net.heckerdev.mariadbwarpplugin.database.DataSource.getConnection;

@CommandAlias("warp")
public class WarpCommand extends BaseCommand{

    @Default
    @Syntax("<warpname>")
    public void onDefault(CommandSender sender, String[] args) {
        if (!sender.hasPermission("warpplugin.command.warp")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l⚠&c You do not have permission to use this command!"));
        } else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l⚠&c You need to specify a warp name!&7 Usage: /warp &n<warpname>"));
                } else {
                    String warpName = args[0];
                    try {
                        Connection connection = getConnection();
                        String worldName = null;
                        double x = 0.0;
                        double y = 0.0;
                        double z = 0.0;
                        double yaw = 0.0;
                        double pitch = 0.0;
                        PreparedStatement queryStatement = connection.prepareStatement("SELECT * FROM WarpsTable WHERE WarpName = ?");
                        queryStatement.setString(1, warpName.toLowerCase());
                        ResultSet rows = queryStatement.executeQuery();
                        while (rows.next()) {
                            worldName = rows.getString("WorldName");
                            x = rows.getDouble("X");
                            y = rows.getDouble("Y");
                            z = rows.getDouble("Z");
                            yaw = rows.getDouble("Yaw");
                            pitch = rows.getDouble("Pitch");
                        }
                        rows.close();
                        if (worldName == null) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l❌&c That warp does not exist!"));
                            return;
                        }
                        Location location = new Location(Bukkit.getWorld(worldName), x, y, z, (float) yaw, (float) pitch);
                        player.teleport(location);
                        connection.close();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l✔&a You have been warped to &6" + warpName + "&a!"));
                    } catch (SQLException ex) {
                        Bukkit.getLogger().warning("Error warping player: " + ex);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l⚠&c There was an error warping you, check the console for more information!"));
                    }
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l⚠&c You must be a player to use this command!"));
            }
        }
    }
}
