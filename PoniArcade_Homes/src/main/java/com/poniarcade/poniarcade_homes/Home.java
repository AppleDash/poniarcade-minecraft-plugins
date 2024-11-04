package com.poniarcade.poniarcade_homes;

import com.poniarcade.poniarcade_database.SerializationUtils;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by appledash on 7/15/17.
 * Blackjack is best pony.
 */
public record Home(UUID ownerUuid, String name, Location location) implements Comparable<Home> {
    public static Home fromSql(ResultSet rs) throws SQLException {
        return new Home((UUID) rs.getObject("owner_id"), rs.getString("name"), SerializationUtils.locFromResultSet(rs));
    }

    @Override
    public int compareTo(Home other) {
        return this.name.compareTo(other.name);
    }
}
