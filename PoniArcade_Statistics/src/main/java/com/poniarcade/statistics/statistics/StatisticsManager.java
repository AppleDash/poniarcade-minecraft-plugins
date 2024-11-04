package com.poniarcade.statistics.statistics;

import com.google.common.collect.ImmutableMap;
import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.structs.Reloadable;
import com.poniarcade.poniarcade_database.PoniArcade_Database;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by appledash on 7/7/17.
 * Blackjack is best pony.
 */
public class StatisticsManager implements Reloadable {
    private Map<UUID, PlayerStatistics> statistics = new HashMap<>();
    private final Map<UUID, PlayerStatistics> changedStatistics = new HashMap<>();

    @Override
    public void reloadData() {
        try (Connection conn = PoniArcade_Database.getConnection()) {
            conn.prepareStatement("BEGIN WORK; LOCK TABLE player_statistics").execute();

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM player_statistics WHERE server_name = ?");
            ps.setString(1, PoniArcade_Core.getInstance().getServerName());
            ResultSet rs = ps.executeQuery();
            Map<UUID, PlayerStatistics> stats = new HashMap<>();

            while (rs.next()) {
                UUID playerUuid = (UUID) rs.getObject("player_id");
                PlayerStatistics playerStatistics = PlayerStatistics.fromDatabase(rs);
                stats.put(playerUuid, playerStatistics);
            }

            conn.prepareStatement("COMMIT WORK").execute();

            this.statistics = stats;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reload player statistics!", e);
        }
    }

    public PlayerStatistics getStatistics(OfflinePlayer player) {
        if (this.changedStatistics.containsKey(player.getUniqueId())) {
            return new PlayerStatistics(
                this.changedStatistics.get(player.getUniqueId())
            );
        }

        return new PlayerStatistics(
            this.statistics.getOrDefault(player.getUniqueId(), new PlayerStatistics())
        );
    }

    public void putStatistics(OfflinePlayer player, PlayerStatistics statistics) {
        this.changedStatistics.put(player.getUniqueId(), statistics);
    }

    public void saveStatistics(UUID uuid) {
        if (!this.changedStatistics.containsKey(uuid)) {
            return;
        }

        PlayerStatistics statistics = this.changedStatistics.get(uuid);

        this.statistics.put(uuid, statistics);
        this.changedStatistics.remove(uuid);

        PoniArcade_Database.runDatabaseOperationAsync("statistics_save_" + uuid, () -> {
            try (Connection conn = PoniArcade_Database.getConnection()) {
                conn.prepareStatement("BEGIN WORK; LOCK TABLE player_statistics").execute();
                // Sorry
                // Need to rename column and make unique index on (player_id, server_name)
                PreparedStatement ps = conn.prepareStatement("INSERT INTO player_statistics (player_id, server_name, times_joined, minutes_online, blocks_broken, blocks_placed, mobs_killed) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) ON CONFLICT (player_id, server_name) DO UPDATE SET times_joined = ?, minutes_online = ?, blocks_broken = ?, blocks_placed = ?, mobs_killed = ?");
                ps.setObject(1, uuid);
                ps.setString(2, PoniArcade_Core.getInstance().getServerName());
                ps.setInt(3, statistics.getTimesJoined());
                ps.setInt(4, statistics.getMinutesOnline());
                ps.setInt(5, statistics.getBlocksBroken());
                ps.setInt(6, statistics.getBlocksPlaced());
                ps.setInt(7, statistics.getMobsKilled());
                ps.setInt(8, statistics.getTimesJoined());
                ps.setInt(9, statistics.getMinutesOnline());
                ps.setInt(10, statistics.getBlocksBroken());
                ps.setInt(11, statistics.getBlocksPlaced());
                ps.setInt(12, statistics.getMobsKilled());
                ps.executeUpdate();
                conn.prepareStatement("COMMIT WORK").execute();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save player statistics!", e);
            }
        });
    }

    public void syncPendingPlayers() {
        ImmutableMap.copyOf(this.changedStatistics).forEach((uuid, statistics) -> {
            this.saveStatistics(uuid);
        });
    }
}
