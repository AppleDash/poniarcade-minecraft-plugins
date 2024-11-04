package com.poniarcade.core.handlers;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.structs.PlayerData;
import com.poniarcade.core.structs.Reloadable;
import com.poniarcade.poniarcade_database.PoniArcade_Database;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by appledash on 7/24/17.
 * Blackjack is best pony.
 */
public class PlayerManager implements Reloadable {
    private final Map<String, PlayerData> nameToData = new HashMap<>();

    @Override
    public void reloadData() {
        try (Connection conn = PoniArcade_Database.getConnection()) {
            conn.prepareStatement("BEGIN WORK; LOCK TABLE players").execute();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM players");
            ResultSet rs = ps.executeQuery();

            List<PlayerData> data = new ArrayList<>();

            while (rs.next()) {
                data.add(PlayerData.fromSql(rs));
            }

            synchronized (this.nameToData) {
                this.nameToData.clear();
                data.forEach(datum -> {
                    this.nameToData.put(datum.lastName().toLowerCase(), datum);
                });
            }

            conn.prepareStatement("COMMIT WORK").execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reload player data!", e);
        }
    }


    public void handlePlayerJoin(Player player) {
        PlayerData playerData = new PlayerData(player.getUniqueId(), player.getName(), LocalDateTime.now(), PoniArcade_Core.getInstance().getServerName());

        this.nameToData.put(player.getName().toLowerCase(), playerData);

        PoniArcade_Database.runDatabaseOperationAsync("update_player_" + player.getUniqueId(), () -> {
            try (Connection conn = PoniArcade_Database.getConnection()) {
                conn.prepareStatement("BEGIN WORK; LOCK TABLE players").execute();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO players (uuid, last_name, last_seen, last_server_name) VALUES (?, ?, ?, ?) " +
                        "ON CONFLICT (uuid) DO UPDATE SET last_name = ?, last_seen = ?, last_server_name = ?");
                ps.setObject(1, playerData.uuid());
                ps.setString(2, playerData.lastName());
                ps.setTimestamp(3, Timestamp.valueOf(playerData.lastPlayed()));
                ps.setString(4, playerData.lastServerName());

                ps.setString(5, playerData.lastName());
                ps.setTimestamp(6, Timestamp.valueOf(playerData.lastPlayed()));
                ps.setString(7, playerData.lastServerName());

                ps.executeUpdate();

                conn.prepareStatement("COMMIT WORK").execute();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save player data!", e);
            }
        });
    }

    public PlayerData getOfflinePlayer(String name) {
        return this.nameToData.get(name.toLowerCase());
    }
}
