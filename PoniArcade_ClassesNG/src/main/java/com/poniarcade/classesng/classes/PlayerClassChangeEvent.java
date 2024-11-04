package com.poniarcade.classesng.classes;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class PlayerClassChangeEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Class oldClass;
    private final Class newClass;
    private final Player player;

    public PlayerClassChangeEvent(Player player, Class oldClass, Class newClass) {
        this.player = player;
        this.oldClass = oldClass;
        this.newClass = newClass;
    }

    public Class getOldClass() {
        return this.oldClass;
    }

    public Class getNewClass() {
        return this.newClass;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return PlayerClassChangeEvent.HANDLER_LIST;
    }

    public Player getPlayer() {
        return this.player;
    }

    @SuppressWarnings("unused") // Bukkit uses this internally via reflection
    public static HandlerList getHandlerList() {
        return PlayerClassChangeEvent.HANDLER_LIST;
    }
}
