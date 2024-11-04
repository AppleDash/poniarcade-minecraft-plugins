package com.poniarcade.core.structs;

import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by appledash on 7/17/17.
 * Blackjack is best pony.
 */
public class Suggestion {
    private final UUID creatorUuid;
    private final String creatorName;
    private final String title;
    private final List<String> pages;
    private final LocalDateTime createdTime;
    private int id;

    public Suggestion(Player player, BookMeta bookMeta) {
        this.creatorUuid = player.getUniqueId();
        this.creatorName = player.getName();
        this.title = bookMeta.getTitle();
        this.pages = ImmutableList.copyOf(bookMeta.getPages());
        this.createdTime = LocalDateTime.now();
    }

    public Suggestion(UUID creatorUuid, String creatorName, String title, List<String> pages, LocalDateTime createdTime) {
        this.creatorUuid = creatorUuid;
        this.creatorName = creatorName;
        this.title = title;
        this.pages = pages;
        this.createdTime = createdTime;
    }

    public UUID getCreatorUuid() {
        return this.creatorUuid;
    }

    public String getTitle() {
        return this.title;
    }

    public List<String> getPages() {
        return this.pages;
    }

    public LocalDateTime getCreatedTime() {
        return this.createdTime;
    }

    public BookMeta toBookMeta(BookMeta original) {

        // original.setGeneration(BookMeta.Generation.ORIGINAL);
        original.setAuthor(this.creatorName);
        original.setTitle(this.title);
        original.setPages(this.pages);

        return original;
    }

    public String getCreatorName() {
        return this.creatorName;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BookMeta bm) {
            return bm.hasAuthor() && bm.hasTitle()
                && Objects.equals(bm.getAuthor(), this.creatorName)
                && bm.getPages().equals(this.pages)
                && Objects.equals(bm.getTitle(), this.title);
        } else if (o instanceof Suggestion s) {
            return s.creatorName.equals(this.creatorName) && s.pages.equals(this.pages) && s.title.equals(this.title);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.creatorName, this.title, this.pages);
    }

    public static Suggestion fromSql(ResultSet rs) throws SQLException {
        List<String> pages = ImmutableList.copyOf(rs.getString("pages").split("\\$sep\\$"));
        Suggestion suggestion = new Suggestion((UUID) rs.getObject("creator_uuid"), rs.getString("creator_name"), rs.getString("title"), pages, rs.getTimestamp("created_time").toLocalDateTime());
        suggestion.id = rs.getInt("id");
        return suggestion;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
