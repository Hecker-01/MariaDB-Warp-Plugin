package net.heckerdev.mariadbwarpplugin;

import co.aikar.commands.PaperCommandManager;
import net.heckerdev.mariadbwarpplugin.commands.*;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import static net.heckerdev.mariadbwarpplugin.database.DataSource.initializeDatabase;

public final class MariaDB_Warp_Plugin extends JavaPlugin {

    private static Permission perms = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        initializeDatabase();
        setupCommands();
        setupPermissions();
        Bukkit.getLogger().info("[MariaDB_Warp_Plugin] has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("[MariaDB_Warp_Plugin] has been disabled!");
    }

    private void setupCommands() {
        // Registering commands.
        PaperCommandManager manager= new PaperCommandManager(this);
        manager.registerCommand(new SetwarpCommand());
        manager.registerCommand(new WarpCommand());
        manager.registerCommand(new DelwarpCommand());
        manager.registerCommand(new WarplistCommand());
    }

    private boolean setupPermissions() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().warning("- Disabled because Vault is not installed!");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        perms = rsp.getProvider();
        return perms != null;
    }

    public static Permission getPermissions() {
        return perms;
    }
}
