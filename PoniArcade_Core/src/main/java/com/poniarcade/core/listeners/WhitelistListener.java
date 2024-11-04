package com.poniarcade.core.listeners;

import com.poniarcade.core.PoniArcade_Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

@SuppressWarnings("unused")
public class WhitelistListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent evt) {
        if (PoniArcade_Core.getInstance().isWhitelistEnabled()) {
            if (!evt.getPlayer().hasPermission("core.whitelist.allow")) {
                evt.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
                evt.setKickMessage("You are not whitelisted on this server!");
                PoniArcade_Core.getInstance().getLogger().warning("Player " + evt.getPlayer().getName() + " disallowed from joining due to whitelist.");
            }
        }
    }
}
