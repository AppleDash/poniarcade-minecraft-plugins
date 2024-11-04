package com.poniarcade.core.handlers;

import com.google.common.collect.ImmutableList;
import com.poniarcade.core.structs.Reloadable;
import com.poniarcade.core.structs.Suggestion;
import com.poniarcade.core.utils.StringUtil;
import com.poniarcade.poniarcade_database.PoniArcade_Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by appledash on 7/17/17.
 * Blackjack is best pony.
 */
public class SuggestionHandler implements Reloadable {
    private Map<Integer, Suggestion> suggestions = new ConcurrentHashMap<>();

    public void reloadData() {
        try (Connection conn = PoniArcade_Database.getConnection()) {
            conn.prepareStatement("BEGIN WORK; LOCK TABLE suggestions").executeUpdate();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM suggestions WHERE viewed = FALSE");
            ResultSet rs = ps.executeQuery();
            Map<Integer, Suggestion> map = new HashMap<>();

            while (rs.next()) {
                map.put(rs.getInt("id"), Suggestion.fromSql(rs));
            }

            this.suggestions = map;
            conn.prepareStatement("COMMIT WORK").executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to reload suggestion data!", e);
        }
    }

    public void saveSuggestion(Suggestion suggestion) {
        PoniArcade_Database.runDatabaseOperationAsync("save_suggestion_" + suggestion.getCreatorName(), () -> {
            try (Connection conn = PoniArcade_Database.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO suggestions (creator_uuid, creator_name, created_time, title, pages) VALUES (?, ?, ?, ?, ?) RETURNING id");
                ps.setObject(1, suggestion.getCreatorUuid());
                ps.setString(2, suggestion.getCreatorName());
                ps.setTimestamp(3, Timestamp.valueOf(suggestion.getCreatedTime()));
                ps.setString(4, suggestion.getTitle());
                ps.setString(5, StringUtil.combine(suggestion.getPages().toArray(new String[0]), "$sep$"));
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new IllegalStateException("What?");
                }
                suggestion.setId(rs.getInt("id"));
                this.suggestions.put(rs.getInt("id"), suggestion);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save suggestion!", e);
            }
        });
    }

    public List<Suggestion> getSuggestions() {
        return this.suggestions.values().stream().sorted(Comparator.comparingInt(Suggestion::getId)).collect(ImmutableList.toImmutableList());
    }

    public boolean hasTooManyRecentSuggestions(UUID playerUuid) {
        return this.suggestions.values().stream()
            .filter(suggestion -> suggestion.getCreatorUuid().equals(playerUuid))
            .anyMatch(suggestion -> ChronoUnit.SECONDS.between(suggestion.getCreatedTime(), LocalDateTime.now()) < (60 * 60));
    }

    public void removeSuggestion(Suggestion suggestion) {
        this.suggestions.remove(suggestion.getId());
        PoniArcade_Database.runDatabaseOperationAsync("remove_suggestion_" + suggestion.getId(), () -> {
            try (Connection conn = PoniArcade_Database.getConnection()) {
                conn.prepareStatement("BEGIN WORK; LOCK TABLE suggestions").executeUpdate();
                PreparedStatement ps = conn.prepareStatement("UPDATE suggestions SET viewed = TRUE WHERE id = ?");
                ps.setInt(1, suggestion.getId());
                ps.executeUpdate();
                conn.prepareStatement("COMMIT WORK").executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to remove suggestion!");
            }
        });
    }

    public Optional<Suggestion> getSuggestion(int id) {
        return Optional.ofNullable(this.suggestions.get(id));
    }
}
