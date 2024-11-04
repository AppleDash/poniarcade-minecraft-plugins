package com.poniarcade.messaging.listeners;

import com.poniarcade.messaging.PoniArcade_Messaging;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

/**
 * Created by appledash on 7/1/17.
 * Blackjack is best pony.
 */
public class JoinListener implements Listener {
    private final PoniArcade_Messaging plugin;

    public JoinListener(PoniArcade_Messaging plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();
        Optional<String> nickOptional = this.plugin.getNicknameManager().getNickname(player);

        if (nickOptional.isEmpty()) {
            return;
        }

        String nickname = "~" + nickOptional.get() + ChatColor.RESET;

        player.setDisplayName(nickname);
        player.setPlayerListName(nickname);
    }
}
