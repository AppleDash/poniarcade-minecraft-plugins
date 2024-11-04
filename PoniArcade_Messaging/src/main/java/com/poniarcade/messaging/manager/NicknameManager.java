package com.poniarcade.messaging.manager;

import com.poniarcade.poniarcade_database.PoniArcade_Database;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by appledash on 7/1/17.
 * Blackjack is best pony.
 */
public class NicknameManager {
    private Set<String> takenNames = new HashSet<>();
    private Map<UUID, String> nicknames = new HashMap<>();

    public void reloadNicknames() {
        try (Connection conn = PoniArcade_Database.getConnection()) {
            Map<UUID, String> tempNicks = new HashMap<>();
            Set<String> tempTaken = new HashSet<>();

            conn.prepareStatement("BEGIN WORK").execute();
            conn.prepareStatement("LOCK TABLE player_nicknames").execute();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM player_nicknames");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID playerId = (UUID) rs.getObject("player_id");
                String playerNick = rs.getString("nickname");

                tempNicks.put(playerId, playerNick);
                tempTaken.add(playerNick);
            }

            conn.prepareStatement("COMMIT WORK").execute();

            this.nicknames = tempNicks;
            this.takenNames = tempTaken;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reload nicknames!", e);
        }
    }

    public Optional<String> getNickname(OfflinePlayer player) {
        return Optional.ofNullable(this.nicknames.get(player.getUniqueId()));
    }

    public void setNickname(OfflinePlayer player, String nickname) {
        this.getNickname(player).ifPresent(this.takenNames::remove);
        this.nicknames.put(player.getUniqueId(), nickname);
        this.takenNames.add(nickname);

        PoniArcade_Database.runDatabaseOperationAsync("set_nickname_" + player.getUniqueId(), () -> {
            try (Connection conn = PoniArcade_Database.getConnection()) {
                conn.prepareStatement("BEGIN WORK; LOCK TABLE player_nicknames;").execute();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO player_nicknames (player_id, nickname) VALUES (?, ?) ON CONFLICT (player_id) DO UPDATE SET nickname = ?");
                ps.setObject(1, player.getUniqueId());
                ps.setString(2, nickname);
                ps.setString(3, nickname);
                ps.executeUpdate();
                conn.prepareStatement("COMMIT WORK").execute();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save nickname for player with ID " + player.getUniqueId() + "!");
            }
        });
    }

    public void removeNickname(OfflinePlayer player) {
        this.getNickname(player).ifPresent(this.takenNames::remove);
        this.nicknames.remove(player.getUniqueId());

        PoniArcade_Database.runDatabaseOperationAsync("remove_nickname_" + player.getUniqueId(), () -> {
            try (Connection conn = PoniArcade_Database.getConnection()) {
                conn.prepareStatement("BEGIN WORK; LOCK TABLE player_nicknames;").execute();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM player_nicknames WHERE player_id = ?");
                ps.setObject(1, player.getUniqueId());
                ps.executeUpdate();
                conn.prepareStatement("COMMIT WORK").execute();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to remove nickname for player with ID " + player.getUniqueId() + "!");
            }
        });
    }

    public boolean isNicknameTaken(String nickname) {
        return this.takenNames.contains(nickname.toLowerCase());
    }
}
