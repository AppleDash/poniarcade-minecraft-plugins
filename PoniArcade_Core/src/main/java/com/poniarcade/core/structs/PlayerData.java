package com.poniarcade.core.structs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by appledash on 7/24/17.
 * Blackjack is best pony.
 */
public record PlayerData(UUID uuid, String lastName, LocalDateTime lastPlayed, String lastServerName) {
    public static PlayerData fromSql(ResultSet rs) throws SQLException {
        return new PlayerData((UUID) rs.getObject("uuid"), rs.getString("last_name"), rs.getTimestamp("last_seen").toLocalDateTime(), rs.getString("last_server_name"));
    }
}
