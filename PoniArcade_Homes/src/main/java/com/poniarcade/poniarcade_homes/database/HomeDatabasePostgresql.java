package com.poniarcade.poniarcade_homes.database;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.poniarcade_database.PoniArcade_Database;
import com.poniarcade.poniarcade_homes.Home;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by appledash on 7/15/17.
 * Blackjack is best pony.
 */
public class HomeDatabasePostgresql implements HomeDatabase {
    private Map<UUID, Map<String, Home>> homes = new HashMap<>();

    @Override
    public void reloadData() {
        try (Connection conn = PoniArcade_Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM homes WHERE server_name = ?");
            ps.setString(1, PoniArcade_Core.getInstance().getServerName());
            Map<UUID, Map<String, Home>> tempHomes = new HashMap<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID ownerId = (UUID) rs.getObject("owner_id");
                Home home = Home.fromSql(rs);
                tempHomes.computeIfAbsent(ownerId, (uuid) -> new HashMap<>()).put(home.name().toLowerCase(), home);
            }

            this.homes = tempHomes;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to sync homes!", e);
        }
    }

    @Override
    public List<Home> getAllHomes(OfflinePlayer player) {
        return this.homes.getOrDefault(player.getUniqueId(), ImmutableMap.of()).values().stream()
            .sorted().collect(ImmutableList.toImmutableList());
    }

    @Override
    public Optional<Home> getHome(OfflinePlayer player, String name) {
        return Optional.ofNullable(this.homes.getOrDefault(player.getUniqueId(), ImmutableMap.of()).getOrDefault(name.toLowerCase(), null));
    }

    @Override
    public void setHome(OfflinePlayer player, Home home) {
        this.homes.computeIfAbsent(player.getUniqueId(), (uuid) -> new HashMap<>()).put(home.name().toLowerCase(), home);
        PoniArcade_Database.runDatabaseOperationAsync("set_home_" + player.getUniqueId() + "_" + home.name(), () -> {
            Location location = home.location();

            try (Connection conn = PoniArcade_Database.getConnection()) {
                // Changes: Got rid of server_id, created server_name
                // Need to make a unique index on (server_name, owner_id, name)
                // Need to make server_name contain the right server names corresponding to IDs.
                PreparedStatement ps = conn.prepareStatement("INSERT INTO homes (server_name, owner_id, name, world, position_x, position_y, position_z, yaw, pitch) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON CONFLICT (server_name, owner_id, name) DO UPDATE SET world = ?, position_x = ?, position_y = ?, position_z = ?, yaw = ?, pitch = ?");

                ps.setString(1, PoniArcade_Core.getInstance().getServerName());
                ps.setObject(2, player.getUniqueId());
                ps.setString(3, home.name());
                ps.setObject(4, location.getWorld().getUID());
                ps.setDouble(5, location.getX());
                ps.setDouble(6, location.getY());
                ps.setDouble(7, location.getZ());
                ps.setDouble(8, location.getYaw());
                ps.setDouble(9, location.getPitch());

                ps.setObject(10, location.getWorld().getUID());
                ps.setDouble(11, location.getX());
                ps.setDouble(12, location.getY());
                ps.setDouble(13, location.getZ());
                ps.setDouble(14, location.getYaw());
                ps.setDouble(15, location.getPitch());

                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to set home!", e);
            }
        });
    }

    @Override
    public void deleteHome(OfflinePlayer player, Home home) {
        if (!this.homes.containsKey(player.getUniqueId())) {
            return;
        }

        this.homes.get(player.getUniqueId()).remove(home.name().toLowerCase());

        try (Connection conn = PoniArcade_Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM HOMES WHERE server_name = ? AND owner_id = ? AND name = ?");
            ps.setString(1, PoniArcade_Core.getInstance().getServerName());
            ps.setObject(2, player.getUniqueId());
            ps.setString(3, home.name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete home!", e);
        }
    }
}
