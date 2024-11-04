package com.poniarcade.punishmentsng.listeners;

import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.punishmentsng.PoniArcade_PunishmentsNG;
import com.poniarcade.punishmentsng.punishments.ActionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovementListener implements Listener {
    private final PoniArcade_PunishmentsNG plugin;

    public PlayerMovementListener(PoniArcade_PunishmentsNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent evt) {
        if (this.plugin.getPunishmentManager().hasActivePunishment(evt.getPlayer(), ActionType.FREEZE)) {
            evt.setCancelled(true);
            evt.getPlayer().sendMessage(ColorHelper.red("You have been frozen!").toString());
        }
    }
}
