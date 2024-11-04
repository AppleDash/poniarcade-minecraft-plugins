package com.poniarcade.warps;

import com.google.common.collect.ImmutableList;
import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.utils.LocationWrapper;
import com.poniarcade.poniarcade_database.DatabaseDebug;
import com.poniarcade.poniarcade_database.PoniArcade_Database;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Created by appledash on 5/16/16.
 * Blackjack is still best pony.
 */
public class TeleportManager {
    private final Map<TeleportLocation.Type, Map<String, TeleportLocation>> cachedLocations = new ConcurrentHashMap<>();

    public void reloadTeleportLocations() {
        for (TeleportLocation.Type type : TeleportLocation.Type.values()) {
            this.cachedLocations.put(type, this.loadTeleportLocations(type));
        }
    }

    private Map<String, TeleportLocation> loadTeleportLocations(TeleportLocation.Type type) {
        try (Connection conn = PoniArcade_Database.getConnection()) {
            DatabaseDebug.startDebug("teleportlocation_" + type + "_load");
            // Need to rename column and update it with the new values
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM teleport_locations WHERE server_name = ? AND type = ?");
            ps.setString(1, PoniArcade_Core.getInstance().getServerName());
            ps.setString(2, type.toString());
            DatabaseDebug.printStatement(ps);

            ResultSet rs = ps.executeQuery();
            Map<String, TeleportLocation> tempLocations = new ConcurrentHashMap<>();

            while (rs.next()) {
                try {
                    tempLocations.put(rs.getString("name").toLowerCase(), new TeleportLocation(
                            rs.getShort("id"),
                            type,
                            rs.getString("name"),
                            new LocationWrapper(
                                    (UUID) rs.getObject("world"),
                                    rs.getDouble("position_x"),
                                    rs.getDouble("position_y"),
                                    rs.getDouble("position_z"),
                                    rs.getFloat("yaw"),
                                    rs.getFloat("pitch")
                            )
                    ));
                } catch (NullPointerException e) {
                    System.err.println("weird teleport location, skipping it!");
                }
            }
            rs.close();

            return tempLocations;
        } catch (SQLException e) {
            PoniArcade_Warps.logger().log(Level.SEVERE, "Internal SQL error while reloading TeleportLocations of type " + type, e);
            throw new RuntimeException("Internal SQL error while reloading TeleportLocations of type " + type, e);
        } finally {
            DatabaseDebug.finishDebug("teleportlocation_" + type + "_load");
        }
    }

    public List<TeleportLocation> getAllLocations(TeleportLocation.Type type) {
        return ImmutableList.copyOf(this.cachedLocations.get(type).values());
    }

    public Optional<TeleportLocation> getTeleportLocation(TeleportLocation.Type type, String name) {
        return Optional.ofNullable(this.cachedLocations.get(type).get(name.toLowerCase()));
    }

    public Optional<TeleportLocation> getRandomTeleportLocation(TeleportLocation.Type type) {
        if (!this.cachedLocations.containsKey(type) || this.cachedLocations.get(type).isEmpty()) {
            return Optional.empty();
        }

        Collection<TeleportLocation> locs = this.cachedLocations.get(type).values();

        return Optional.of(locs.toArray(new TeleportLocation[0])[new Random().nextInt(locs.size())]);
    }

    public TeleportLocation setLocation(TeleportLocation.Type type, String name, Location location) {
        if (this.getTeleportLocation(type, name).isPresent()) {
            this.deleteLocation(type, name);
        }

        TeleportLocation teleportLocation = new TeleportLocation(type, name, new LocationWrapper(location));
        this.cachedLocations.get(type).put(name.toLowerCase(), teleportLocation);

        PoniArcade_Database.runDatabaseOperationAsync("teleportlocation_set_" + type + "_" + name, () -> {
            try (Connection conn = PoniArcade_Database.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO teleport_locations (type, name, world, position_x, position_y, position_z, yaw, pitch, server_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, type.toString());
                ps.setString(2, name);
                ps.setObject(3, location.getWorld().getUID());
                ps.setDouble(4, location.getX());
                ps.setDouble(5, location.getY());
                ps.setDouble(6, location.getZ());
                ps.setDouble(7, location.getYaw());
                ps.setDouble(8, location.getPitch());
                ps.setString(9, PoniArcade_Core.getInstance().getServerName());
                ps.executeUpdate();
            } catch (SQLException e) {
                PoniArcade_Warps.logger().log(Level.SEVERE, "Internal SQL error occurred while setting teleport location of type " + type + ": " + name, e);
                this.cachedLocations.get(type).remove(name.toLowerCase());
            }

            this.loadTeleportLocations(type);
        });


        return teleportLocation;
    }

    public void deleteLocation(TeleportLocation.Type type, String name) {
        Optional<TeleportLocation> oldLocationOptional = this.getTeleportLocation(type, name);

        if (oldLocationOptional.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete a teleport location that doesn't exist!");
        }

        TeleportLocation oldLocation = oldLocationOptional.get();

        this.cachedLocations.get(type).remove(name.toLowerCase());

        PoniArcade_Database.runDatabaseOperationAsync("teleportlocation_delete_" + type + "_" + name, () -> {
            try (Connection conn = PoniArcade_Database.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM teleport_locations WHERE id = ?");
                ps.setShort(1, oldLocation.getId());
                DatabaseDebug.printStatement(ps);
                ps.executeUpdate();
            } catch (SQLException e) {
                PoniArcade_Warps.logger().log(Level.SEVERE, "Internal SQL error occurred while deleting teleport location of type: " + type + ": " + name, e);
                this.cachedLocations.get(type).put(name.toLowerCase(), oldLocation); /* Roll it back so that the local state reflects the database state */
            }
        });
    }
}
