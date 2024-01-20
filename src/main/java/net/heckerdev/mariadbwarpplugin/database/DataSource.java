package net.heckerdev.mariadbwarpplugin.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataSource {

    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource ds = null;

    private static void startthething() {
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setJdbcUrl("jdbc:mariadb://localhost:3306/WarpPlugin");
        config.setUsername("root");
        config.setPassword("");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);
    }

    private DataSource() {}

    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            startthething();
        }
        return ds.getConnection();
    }

    public static void initializeDatabase(){
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS WarpsTable(ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, WarpName VARCHAR(30), WorldName VARCHAR(255), X DOUBLE, Y DOUBLE, Z DOUBLE, Yaw DOUBLE, Pitch DOUBLE, Hidden BOOLEAN)");
            preparedStatement.executeQuery();

            connection.close();
        } catch (SQLException ex) {
            Bukkit.getLogger().warning("Error creating table: " + ex);
        }
    }
}
