package com.poniarcade.punishmentsng.punishments;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.poniarcade.core.structs.Reloadable;
import com.poniarcade.poniarcade_database.PoniArcade_Database;
import com.poniarcade.punishmentsng.PoniArcade_PunishmentsNG;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by appledash on 7/28/17.
 * Blackjack is best pony.
 */
public class PunishmentManager implements Reloadable {
    private final Map<UUID, List<PunishmentData>> activePunishments = new ConcurrentHashMap<>();
    private final PoniArcade_PunishmentsNG plugin;

    public PunishmentManager(PoniArcade_PunishmentsNG plugin) {
        this.plugin = plugin;
    }

    public void reloadData() {
        try (Connection conn = PoniArcade_Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM punishments ORDER BY time ASC");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                this.activePunishments.computeIfAbsent((UUID) rs.getObject("player_uuid"), k -> new CopyOnWriteArrayList<>()).add(
                    new PunishmentData(ActionType.byName(rs.getString("action")), ActionDirection.valueOf(rs.getString("direction")), rs.getString("reason"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reload punishments data!", e);
        }
    }

    public void punishUser(OfflinePlayer player, CommandSender punisher, ActionType actionType, ActionDirection direction, String reason) {
        PunishmentData punishmentData = new PunishmentData(actionType, direction, reason);
        this.activePunishments.computeIfAbsent(player.getUniqueId(), k -> new CopyOnWriteArrayList<>()).add(punishmentData);

        PoniArcade_Database.runDatabaseOperationAsync("punish_user_" + actionType + "_" + player.getUniqueId(), () -> {
            try (Connection conn = PoniArcade_Database.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO punishments (player_uuid, moderator_uuid, time, action, direction, reason) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setObject(1, player.getUniqueId());
                ps.setObject(2, (punisher instanceof Player) ? ((Entity) punisher).getUniqueId() : UUID.fromString("00000000-0000-0000-0000-000000000000"));
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(4, actionType.toString());
                ps.setString(5, direction.toString());
                ps.setString(6, reason);
                ps.executeUpdate();

                this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
                    Player onlinePlayer;

                    if ((onlinePlayer = Iterables.getFirst(this.plugin.getServer().getOnlinePlayers(), null)) != null) {
                        ByteArrayDataOutput bado = ByteStreams.newDataOutput();
                        bado.writeUTF("Forward");
                        bado.writeUTF("ALL");
                        bado.writeUTF("PArcPunishments");
                        bado.writeUTF("reload");
                        // TODO: What do I do here?
                        onlinePlayer.sendPluginMessage(this.plugin, "BungeeCord", bado.toByteArray());
                    }
                });
            } catch (SQLException e) {
                this.activePunishments.get(player.getUniqueId()).remove(punishmentData);
                throw new RuntimeException("Failed to punish user!", e);
            }
        });
    }

    public void punishUser(Player player, CommandSender punisher, ActionType actionType, String reason) {
        this.punishUser(player, punisher, actionType, ActionDirection.PUNISH, reason);
    }

    public PunishResult tryPunishUser(OfflinePlayer player, CommandSender sender, ActionType actionType, ActionDirection actionDirection, String reason) {
        if (player == sender) {
            return PunishResult.CANNOT_PUNISH_SELF;
        }

        boolean hasActivePunishment = this.hasActivePunishment(player, actionType);

        if (hasActivePunishment && (actionDirection == ActionDirection.PUNISH)) {
            return PunishResult.ALREADY_PUNISHED;
        }

        if (!hasActivePunishment && (actionDirection == ActionDirection.REVOKE_PUNISHMENT)) {
            return PunishResult.NOT_PUNISHED;
        }

        this.punishUser(player, sender, actionType, actionDirection, reason);

        return PunishResult.SUCCESS;
    }

    public PunishResult tryPunishUser(OfflinePlayer player, CommandSender punisher, ActionType actionType, String reason) {
        return this.tryPunishUser(player, punisher, actionType, ActionDirection.PUNISH, reason);
    }

    public Optional<PunishmentData> getActivePunishment(OfflinePlayer player, ActionType actionType) {
        if (!this.activePunishments.containsKey(player.getUniqueId())) {
            return Optional.empty();
        }

        PunishmentData activePunishmentData = null;

        for (PunishmentData punishmentData : this.activePunishments.get(player.getUniqueId())) {
            if (punishmentData.actionType() == actionType) {
                activePunishmentData = (punishmentData.actionDirection() == ActionDirection.PUNISH) ? punishmentData : null;
            }
        }

        return Optional.ofNullable(activePunishmentData);
    }

    public boolean hasActivePunishment(OfflinePlayer player, ActionType actionType) {
        return this.getActivePunishment(player, actionType).isPresent();
    }


    public enum PunishResult {
        CANNOT_PUNISH_SELF("You cannot punish yourself."),
        ALREADY_PUNISHED("That player already has that punishment active."),
        NOT_PUNISHED("That player doesn't currently have that punishment active."),
        SUCCESS(null);

        private final String message;

        PunishResult(String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }
    }
}
