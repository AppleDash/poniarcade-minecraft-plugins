package com.poniarcade.poniarcade_database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by appledash on 7/27/16.
 * Blackjack is still best pony.
 */
public class ThreadCheckDatabaseConnection extends Thread {
    private final HikariDataSource hikariDataSource;
    private final AtomicBoolean isConnected = new AtomicBoolean(false);
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public ThreadCheckDatabaseConnection(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    @Override
    public void run() {
	    this.isRunning.set(true);

        while (this.isRunning.get()) {
            try {
                Connection connection = this.hikariDataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT 1");
                ps.execute();
                ResultSet rs = ps.getResultSet();
                if (!rs.next() || (rs.getInt(1) != 1)) {
	                this.isRunning.set(false);
                    PoniArcade_Database.logger().severe("Lost database connection: ResultSet for SELECT 1 returned wrong value!");
                } else {
                    if (!this.isRunning.get()) {
	                    this.isRunning.set(true);
                        PoniArcade_Database.logger().info("Database connection restored after it has been lost.");
                    }
                }

                connection.close();
            } catch (SQLException e) {
                PoniArcade_Database.logger().severe("Lost database connection due to SQLException: " + e.getMessage());
	            this.isRunning.set(false);
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) { }
        }
    }

    public void abort() {
	    this.isRunning.set(false);
    }

    public boolean isConnected() {
        return this.isConnected.get();
    }
}
