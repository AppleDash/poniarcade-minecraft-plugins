package com.poniarcade.core.handlers;

import com.poniarcade.core.structs.MailMessage;
import com.poniarcade.poniarcade_database.PoniArcade_Database;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by appledash on 7/24/17.
 * Blackjack is best pony.
 */
public class MailManager {
    public List<MailMessage> getUnreadMessages(Player receiver) {
        try (Connection conn = PoniArcade_Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                                       "SELECT rplayers.last_name AS receiver_name, splayers.last_name AS sender_name, mail.body AS body, mail.time_sent AS time_sent FROM mail " +
                                       "INNER JOIN players AS rplayers ON rplayers.uuid = ? AND mail.receiver_uuid = rplayers.uuid " +
                                       "INNER JOIN players AS splayers ON splayers.uuid = mail.sender_uuid " +
                                       "WHERE mail.read = FALSE ORDER BY time_sent DESC"
                                   );

            ps.setObject(1, receiver.getUniqueId());
            List<MailMessage> messages = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                messages.add(new MailMessage(rs.getString("sender_name"), rs.getString("receiver_name"), rs.getTimestamp("time_sent").toLocalDateTime(), rs.getString("body")));
            }

            return Collections.unmodifiableList(messages);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get unread messages!", e);
        }
    }

    public void storeMailMessage(Player sender, OfflinePlayer receiver, String message) {
        try (Connection conn = PoniArcade_Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO mail (sender_uuid, receiver_uuid, time_sent, body) VALUES (?, ?, ?, ?)");
            ps.setObject(1, sender.getUniqueId());
            ps.setObject(2, receiver.getUniqueId());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(4, message);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to store mail message!", e);
        }
    }

    public void markAllIncomingMessagesAsRead(Player receiver) {
        try (Connection conn = PoniArcade_Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE mail SET read = TRUE WHERE receiver_uuid = ?");
            ps.setObject(1, receiver.getUniqueId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to mark all incoming messages as read!", e);
        }
    }

    public List<MailMessage> findArchivedMessages(Player receiver, String search, int page) {
        try (Connection conn = PoniArcade_Database.getConnection()) {
            int perPage = 10;

            PreparedStatement ps = conn.prepareStatement(
                                       String.format("SELECT rplayers.last_name AS receiver_name, splayers.last_name AS sender_name, mail.body AS body, mail.time_sent AS time_sent FROM mail " +
                                               "INNER JOIN players AS rplayers ON rplayers.uuid = ? AND mail.receiver_uuid = rplayers.uuid " +
                                               "INNER JOIN players AS splayers ON splayers.uuid = mail.sender_uuid " +
                                               "WHERE mail.read = TRUE AND mail.body LIKE ? ORDER BY time_sent DESC LIMIT %d OFFSET %d", perPage, (perPage * Math.max(0, page - 1)))
                                   );

            ps.setObject(1, receiver.getUniqueId());
            ps.setString(2, "%" + search + "%");
            List<MailMessage> messages = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                messages.add(new MailMessage(rs.getString("sender_name"), rs.getString("receiver_name"), rs.getTimestamp("time_sent").toLocalDateTime(), rs.getString("body")));
            }

            return Collections.unmodifiableList(messages);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find archived messages!", e);
        }
    }
}
