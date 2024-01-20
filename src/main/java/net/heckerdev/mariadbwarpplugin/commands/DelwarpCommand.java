package net.heckerdev.mariadbwarpplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static net.heckerdev.mariadbwarpplugin.database.DataSource.getConnection;


@CommandAlias("delwarp|removewarp")
public class DelwarpCommand extends BaseCommand {

    @Default
    @Syntax("<warpname>")
    public void onDefault(CommandSender sender, String[] args) {
        if (!sender.hasPermission("warpplugin.command.delwarp")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l⚠&c You do not have permission to use this command!"));
        } else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l⚠&c You need to specify a warp name!&7 Usage: /delwarp &n<warpname>"));
                } else {
                    String warpName = args[0];
                    try {
                        Connection connection = getConnection();
                        PreparedStatement preparedStatement;
                        preparedStatement = connection.prepareStatement("DELETE FROM WarpsTable WHERE WarpName = ?");
                        preparedStatement.setString(1, warpName.toLowerCase());
                        preparedStatement.execute();
                        connection.close();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l✔&a Successfully deleted warp &6" + warpName + "&a!"));
                    } catch (SQLException ex) {
                        Bukkit.getLogger().warning("Error deleting warp: " + ex);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l❌&c An error occurred while deleting the warp, check the console for more information!"));
                    }
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l⚠&c You must be a player to use this command!"));
            }
        }
    }
}
