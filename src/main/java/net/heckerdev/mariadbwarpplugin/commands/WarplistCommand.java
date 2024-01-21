package net.heckerdev.mariadbwarpplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.mariadbwarpplugin.database.DataSource;
import net.heckerdev.mariadbwarpplugin.util.TextColorUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@CommandAlias("warplist|warpl")
public class WarplistCommand extends BaseCommand {

    @Default
    @Syntax("[page]")
    @CommandCompletion("1|2|3")
    public void onDefault(CommandSender sender, String[] args) {
        if (!sender.hasPermission("warpplugin.command.WarplistCommand")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c⚠ You do not have permission to use this command!"));
        } else {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6-----------&aWarps page: 1&6-----------"));
                try {
                    Connection connection = DataSource.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM WarpsTable WHERE Hidden != true LIMIT 10 OFFSET 0");
                    ResultSet rows = preparedStatement.executeQuery();
                    while (rows.next()) {
                        sender.sendMessage(Component.text(" • ").color(TextColorUtil.Gold).append(Component.text(rows.getString("WarpName")).color(TextColorUtil.Green)).append(Component.text(" - ").color(TextColorUtil.Gold).append(Component.text("Warp").color(TextColorUtil.Green).hoverEvent(Component.text("Click to warp!").color(TextColorUtil.Green)).clickEvent(ClickEvent.runCommand("/warp " + rows.getString("WarpName"))))).append(Component.text(" - ").color(TextColorUtil.Gold)).append(Component.text("Delete").color(TextColorUtil.Red).hoverEvent(Component.text("Click to delete!").color(TextColorUtil.Red)).clickEvent(ClickEvent.runCommand("/delwarp " + rows.getString("WarpName")))));
                    }
                    rows.close();
                    connection.close();
                } catch (SQLException ex) {
                    Bukkit.getLogger().warning("Error getting warps: " + ex);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l❌&c An error occurred while getting the warps, check the console for more information!"));
                }
                sender.sendMessage(Component.text("--- ").color(TextColorUtil.Gold).append(Component.text("← Page back").color(TextColorUtil.Green).hoverEvent(Component.text("You can't go further back.").color(TextColorUtil.Red))).append(Component.text(" - ").color(TextColorUtil.Gold)).append(Component.text("Page forward →").color(TextColorUtil.Green).hoverEvent(Component.text("Go to page 2.").color(TextColorUtil.Green)).clickEvent(ClickEvent.runCommand("/warpl 2"))).append(Component.text(" ---").color(TextColorUtil.Gold)));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6-----------&aWarps page: " + args[0] + "&6-----------"));
                int page = Integer.parseInt(args[0]);
                try {
                    Connection connection = DataSource.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM WarpsTable WHERE Hidden != true LIMIT 10 OFFSET " + (page-1)*10);
                    ResultSet rows = preparedStatement.executeQuery();
                    while (rows.next()) {
                        sender.sendMessage(Component.text(" • ").color(TextColorUtil.Gold).append(Component.text(rows.getString("WarpName")).color(TextColorUtil.Green)).append(Component.text(" - ").color(TextColorUtil.Gold).append(Component.text("Warp").color(TextColorUtil.Green).hoverEvent(Component.text("Click to warp!").color(TextColorUtil.Green)).clickEvent(ClickEvent.runCommand("/warp " + rows.getString("WarpName"))))).append(Component.text(" - ").color(TextColorUtil.Gold)).append(Component.text("Delete").color(TextColorUtil.Red).hoverEvent(Component.text("Click to delete!").color(TextColorUtil.Red)).clickEvent(ClickEvent.runCommand("/delwarp " + rows.getString("WarpName")))));
                    }
                    rows.close();
                    connection.close();
                } catch (SQLException ex) {
                    Bukkit.getLogger().warning("Error getting warps: " + ex);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l❌&c An error occurred while getting the warps, check the console for more information!"));
                }
                if (page == 1){
                    sender.sendMessage(Component.text("--- ").color(TextColorUtil.Gold).append(Component.text("← Page back").color(TextColorUtil.Green).hoverEvent(Component.text("You can't go further back.").color(TextColorUtil.Red))).append(Component.text(" - ").color(TextColorUtil.Gold)).append(Component.text("Page forward →").color(TextColorUtil.Green).hoverEvent(Component.text("Go to page 2.").color(TextColorUtil.Green)).clickEvent(ClickEvent.runCommand("/warpl 2"))).append(Component.text(" ---").color(TextColorUtil.Gold)));
                } else {
                    String nextPage = String.valueOf(page + 1);
                    String previousPage = String.valueOf(page - 1);
                    sender.sendMessage(Component.text("--- ").color(TextColorUtil.Gold).append(Component.text("← Page back").color(TextColorUtil.Green).hoverEvent(Component.text("Go to page " + previousPage + ".").color(TextColorUtil.Green)).clickEvent(ClickEvent.runCommand("/warpl " + previousPage))).append(Component.text(" - ").color(TextColorUtil.Gold)).append(Component.text("Page forward →").color(TextColorUtil.Green).hoverEvent(Component.text("Go to page " + nextPage + ".").color(TextColorUtil.Green)).clickEvent(ClickEvent.runCommand("/warpl " + nextPage))).append(Component.text(" ---").color(TextColorUtil.Gold)));
                }
            }
        }
    }
}
