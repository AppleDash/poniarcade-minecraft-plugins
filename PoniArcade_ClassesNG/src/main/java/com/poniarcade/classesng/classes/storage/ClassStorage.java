package com.poniarcade.classesng.classes.storage;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassManager;
import com.poniarcade.classesng.classes.PlayerClassData;
import com.poniarcade.poniarcade_database.DatabaseDebug;
import com.poniarcade.poniarcade_database.PoniArcade_Database;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Created by appledash on 7/18/16.
 * Blackjack is still best pony.
 *
 * Handles the storage and retrieval of player class information.
 */
public class ClassStorage {
    private final ClassManager classManager;
    private final Map<UUID, PlayerClassData> playerData = new ConcurrentHashMap<>();
    private final Set<UUID> syncingPlayers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public ClassStorage(ClassManager classManager) {
        this.classManager = classManager;
    }

    private Connection getConnection() throws SQLException {
        return PoniArcade_Database.getConnection();
    }

    public void syncPlayerData() {
        this.playerData.forEach((uuid, data) -> {
            if (data.isDirty()) {
                this.updateDataForPlayer(uuid, data);
            }
        });

        try (Connection conn = this.getConnection()) {
            DatabaseDebug.startDebug("classes_data_reload");
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM player_class_data");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID uuid = (UUID)rs.getObject("player_uuid");

                String className = rs.getString("class_name");
                Class playerClass;

                if (Strings.isNullOrEmpty(className)) {
                    playerClass = null;
                } else {
                    Optional<Class> classOptional = this.classManager.getClassByName(className);

                    if (classOptional.isEmpty()) {
                        PoniArcade_ClassesNG.instance().getLogger().warning("Player with UUID " + uuid + " has invalid class " + className);
                        continue;
                    }

                    playerClass = classOptional.get();
                }

                String spoofedClassName = rs.getString("spoofed_class");
                Class spoofedClass;

                if (Strings.isNullOrEmpty(spoofedClassName)) {
                    spoofedClass = null;
                } else {
                    Optional<Class> spoofedClassOptional = this.classManager.getClassByName(spoofedClassName);

                    if (spoofedClassOptional.isEmpty()) {
                        PoniArcade_ClassesNG.instance().getLogger().warning("Player with UUID " + uuid + " has invalid spoofed class " + spoofedClassName);
                        continue;
                    }

                    spoofedClass = spoofedClassOptional.get();
                }

                long spoofedClassExpiry = rs.getLong("spoofed_class_expiry");

                long nextClassChange = rs.getLong("next_class_change");

                Set<Class> purchasedClasses = new HashSet<>();
                String purchasedClassesStr = rs.getString("purchased_classes");
                if (!Strings.isNullOrEmpty(purchasedClassesStr)) {
                    for (String purchasedClassName : purchasedClassesStr.split(",")) {
                        Optional<Class> purchasedClass = this.classManager.getClassByName(purchasedClassName);

                        if (purchasedClass.isEmpty()) {
                            PoniArcade_ClassesNG.instance().getLogger().warning("Player with UUID " + uuid + " has invalid purchased class " + purchasedClassName);
                            continue;
                        }

                        purchasedClasses.add(purchasedClass.get());
                    }
                }

                PlayerClassData playerClassData = new PlayerClassData(playerClass, nextClassChange, spoofedClass, spoofedClassExpiry, purchasedClasses);

                if (this.playerData.containsKey(uuid)) {
                    this.playerData.get(uuid).merge(playerClassData);
                } else {
                    this.playerData.put(uuid, playerClassData);
                }
            }
        } catch (SQLException e) {
            PoniArcade_ClassesNG.instance().getLogger().log(Level.SEVERE, "Failed to load class data", e);
        } finally {
            DatabaseDebug.finishDebug("classes_data_reload");
        }
    }

    private void updateDataForPlayer(UUID uuid, PlayerClassData data) {
        this.syncingPlayers.add(uuid);
        try (Connection conn = this.getConnection()) {
            DatabaseDebug.startDebug("update_data_" + uuid);
            PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM player_class_data WHERE player_uuid = ?");
            ps.setObject(1, uuid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ps = conn.prepareStatement("UPDATE player_class_data SET class_name = ?, next_class_change = ?, spoofed_class = ?, spoofed_class_expiry = ?, purchased_classes = ? WHERE player_uuid = ?");
            } else {
                ps = conn.prepareStatement("INSERT INTO player_class_data (class_name, next_class_change, spoofed_class, spoofed_class_expiry, purchased_classes, player_uuid) VALUES (?, ?, ?, ?, ?, ?)");
            }

            ps.setString(1, data.getPlayerClass().isPresent() ? data.getPlayerClass().get().getSanitizedName() : null);
            ps.setLong(2, data.getNextClassChange());
            ps.setString(3, data.getSpoofedClass().isPresent() ? data.getSpoofedClass().get().getSanitizedName() : null);
            ps.setLong(4, data.getSpoofedClassExpiry());
            ps.setString(5, Joiner.on(",").join(data.getPurchasedClasses().stream().map(Class::getFriendlyName).collect(Collectors.toList())));
            ps.setObject(6, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            PoniArcade_ClassesNG.instance().getLogger().log(Level.SEVERE, "Failed to save player class data for UUID " + uuid, e);
        } finally {
            DatabaseDebug.finishDebug("update_data_" + uuid);
        }
        data.setDirty(false);
        this.syncingPlayers.remove(uuid);
    }

    public PlayerClassData getClassData(OfflinePlayer player) {
        if (this.playerData.containsKey(player.getUniqueId())) {
            return this.playerData.get(player.getUniqueId());
        }

        PlayerClassData playerClassData = new PlayerClassData(null, 0, null, 0, new HashSet<>());
        this.playerData.put(player.getUniqueId(), playerClassData);
        return playerClassData;
    }

    public Map<UUID, PlayerClassData> getAllClassDatas() {
        return this.playerData;
    }

    public boolean isSyncingPlayer(UUID playerUUID) {
        return this.syncingPlayers.contains(playerUUID);
    }

    public void syncPlayerAsync(UUID playerUUID) {
        PoniArcade_Database.runDatabaseOperationAsync("sync_player_async_" + playerUUID, () -> {
            this.updateDataForPlayer(playerUUID, this.playerData.get(playerUUID));
        });
    }
}
