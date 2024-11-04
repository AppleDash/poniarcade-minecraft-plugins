package com.poniarcade.messaging.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by appledash on 7/6/17.
 * Blackjack is best pony.
 */
public class ChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent evt) {
        if (evt.getPlayer().hasPermission("poniarcade.messaging.chatcolor")) {
            evt.setMessage(ChatColor.translateAlternateColorCodes('&', evt.getMessage()));
        }
    }
}
