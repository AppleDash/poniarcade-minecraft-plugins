package com.poniarcade.poniarcade_database;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by appledash on 7/15/17.
 * Blackjack is best pony.
 */
public final class SerializationUtils {
	private SerializationUtils() {
	}

	public static Location locFromResultSet(ResultSet rs) throws SQLException {
        return new Location(
                   Bukkit.getServer().getWorld((UUID) rs.getObject("world")),
                   rs.getDouble("position_x"), rs.getDouble("position_y"), rs.getDouble("position_z"),
                   rs.getFloat("yaw"), rs.getFloat("pitch")
               );
    }
}
