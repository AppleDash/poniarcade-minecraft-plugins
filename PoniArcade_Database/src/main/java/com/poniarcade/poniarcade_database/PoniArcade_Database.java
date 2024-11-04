package com.poniarcade.poniarcade_database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Created by appledash on 7/23/16.
 * Blackjack is still best pony.
 */
public class PoniArcade_Database extends JavaPlugin {
    private static PoniArcade_Database INSTANCE;
    private HikariDataSource hikariDataSource;
    private ThreadCheckDatabaseConnection threadCheckDatabaseConnection;
    private ThreadRunDatabaseOperation threadRunDatabaseOperation;
    private final Queue<Runnable> databaseOperations = new ConcurrentLinkedQueue<>();

    public PoniArcade_Database() {
        PoniArcade_Database.INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.initDataSource(this.getConfig());
        this.threadCheckDatabaseConnection = new ThreadCheckDatabaseConnection(this.hikariDataSource);
        this.threadCheckDatabaseConnection.start();
        this.threadRunDatabaseOperation = new ThreadRunDatabaseOperation(this.databaseOperations);
        this.threadRunDatabaseOperation.start();
        this.getLogger().info("Initialized Hikari data source.");
    }

    @Override
    public void onDisable() {
        if (this.hikariDataSource != null) {
            this.threadCheckDatabaseConnection.abort();
            this.threadRunDatabaseOperation.abort();
            this.getLogger().info("Closing Hikari data source.");
            this.hikariDataSource.close();
            this.hikariDataSource = null;
        }
    }

    private void initDataSource(Configuration configuration) {
        if (this.hikariDataSource != null) {
            throw new IllegalStateException("Cannot re-initialize data source!");
        }

        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(configuration.getInt("database.pool_size", 8));

        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.addDataSourceProperty("serverName", configuration.get("database.host"));
        config.addDataSourceProperty("databaseName", configuration.get("database.database"));
        config.addDataSourceProperty("portNumber", configuration.get("database.port", 0));
        config.addDataSourceProperty("user", configuration.get("database.username"));
        config.addDataSourceProperty("password", configuration.get("database.password"));
        config.setConnectionTimeout(configuration.getInt("database.connection_timeout", 1500));
        config.setIdleTimeout(configuration.getInt("database.idle_timeout", (60 * 5) * 1000));
        config.setIdleTimeout(configuration.getInt("database.max_lifetime", (60 * 30) * 1000));
        this.hikariDataSource = new HikariDataSource(config);
    }

    /**
     * Check whether we currently have a connection to the database.
     * @return True if we can reasonably assume the database is connected, false otherwise
     */
    public static boolean isDatabaseConnected() {
        return (PoniArcade_Database.INSTANCE != null) && (PoniArcade_Database.INSTANCE.threadCheckDatabaseConnection != null) && PoniArcade_Database.INSTANCE.threadCheckDatabaseConnection.isConnected();

    }

    /**
     * Get a handle to a database connection from the connection pool.
     * @return Database connection
     * @throws SQLException If the connection can't be opened for some reason
     * @throws IllegalStateException If the plugin or database have not been initialized yet
     */
    public static Connection getConnection() throws SQLException {
        if ((PoniArcade_Database.INSTANCE == null) || (PoniArcade_Database.INSTANCE.hikariDataSource == null)) {
            throw new IllegalStateException("Cannot get a database connection before Hikari is initialized!");
        }

        return PoniArcade_Database.INSTANCE.hikariDataSource.getConnection();
    }

    public static void runDatabaseOperationAsync(String tag, Runnable callback) {
        if (PoniArcade_Database.INSTANCE == null) {
            throw new IllegalStateException("Cannot run a database operation before the plugin is initialized!");
        }

        PoniArcade_Database.INSTANCE.databaseOperations.add(() -> {
            DatabaseDebug.startDebug(tag);
            try {
                callback.run();
            } finally {
                DatabaseDebug.finishDebug(tag);
            }
        });
    }

    public static Logger logger() {
        return PoniArcade_Database.INSTANCE.getLogger();
    }
}
