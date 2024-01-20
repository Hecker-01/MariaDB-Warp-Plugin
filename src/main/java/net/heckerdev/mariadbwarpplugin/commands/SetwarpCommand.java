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
import java.sql.SQLException;

import static net.heckerdev.mariadbwarpplugin.database.DataSource.getConnection;

@CommandAlias("setwarp|createwarp")
public class SetwarpCommand extends BaseCommand {
    @Default
    @Syntax("<warpname> [hidden]")
    @CommandCompletion(" true|false")
    public void onDefault(CommandSender sender, String[] args) {
        if (!sender.hasPermission("warpplugin.command.setwarp")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l⚠&c You do not have permission to use this command!"));
        } else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l⚠&c You need to specify a warp name!&7 Usage: /setwarp &n<warpname>&r&7 [hidden]"));
                } else {
                    String warpName = args[0];
                    Location loc = player.getLocation();
                    String worldName = loc.getWorld().getName();
                    double x = loc.getX();
                    double y = loc.getY();
                    double z = loc.getZ();
                    double pitch = loc.getPitch();
                    double yaw = loc.getYaw();
                    boolean hidden = false;
                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("true")) {
                            hidden = true;
                        }
                    }
                    try {
                        Connection connection = getConnection();
                        PreparedStatement preparedStatement;
                        preparedStatement = connection.prepareStatement("INSERT INTO WarpsTable(WarpName, WorldName, X, Y, Z, Yaw, Pitch, Hidden) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                        preparedStatement.setString(1, warpName.toLowerCase());
                        preparedStatement.setString(2, worldName);
                        preparedStatement.setDouble(3, x);
                        preparedStatement.setDouble(4, y);
                        preparedStatement.setDouble(5, z);
                        preparedStatement.setDouble(6, yaw);
                        preparedStatement.setDouble(7, pitch);
                        preparedStatement.setBoolean(8, hidden);
                        preparedStatement.execute();
                        connection.close();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l✔&a Successfully created warp &6" + warpName + "&a!"));
                    } catch (SQLException ex) {
                        Bukkit.getLogger().warning("Error adding warp: " + ex);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l❌&c An error occurred while adding the warp, check the console for more information!"));
                    }
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l⚠&c You must be a player to use this command!"));
            }
        }
    }
}
