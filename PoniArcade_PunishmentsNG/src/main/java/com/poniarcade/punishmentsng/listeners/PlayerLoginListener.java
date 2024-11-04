package com.poniarcade.punishmentsng.listeners;

import com.poniarcade.punishmentsng.PoniArcade_PunishmentsNG;
import com.poniarcade.punishmentsng.punishments.ActionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Created by appledash on 7/28/17.
 * Blackjack is best pony.
 */
public class PlayerLoginListener implements Listener {
    private final PoniArcade_PunishmentsNG plugin;

    public PlayerLoginListener(PoniArcade_PunishmentsNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent evt) {
        while (this.plugin.getJoinLock().get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent evt) {
        if (this.plugin.getPunishmentManager().hasActivePunishment(evt.getPlayer(), ActionType.BAN)) {
            evt.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You have been banned from this server!");
        }
    }
}
