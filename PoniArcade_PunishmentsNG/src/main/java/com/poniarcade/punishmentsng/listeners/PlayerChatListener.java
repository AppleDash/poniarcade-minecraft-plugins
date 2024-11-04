package com.poniarcade.punishmentsng.listeners;

import com.google.common.collect.ImmutableList;
import com.poniarcade.punishmentsng.PoniArcade_PunishmentsNG;
import com.poniarcade.punishmentsng.punishments.ActionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

/**
 * Created by appledash on 5/17/18.
 * Blackjack is best pony.
 */
public class PlayerChatListener implements Listener {
    private static final List<String> BLACKLISTED_COMMANDS = ImmutableList.of("msg", "tell", "m", "t", "r", "reply", "whisper");
    private final PoniArcade_PunishmentsNG plugin;

    public PlayerChatListener(PoniArcade_PunishmentsNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent evt) {
        if (this.plugin.getPunishmentManager().hasActivePunishment(evt.getPlayer(), ActionType.MUTE)) {
            evt.setCancelled(true);
            messageTo(evt.getPlayer()).red("You have been muted!").send();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent evt) {
        if (!this.plugin.getPunishmentManager().hasActivePunishment(evt.getPlayer(), ActionType.MUTE)) {
            return;
        }

        String command = evt.getMessage();

        while (!command.isEmpty() && command.charAt(0) == '/') {
            command = command.substring(1);
        }

        if (command.isEmpty()) {
            return;
        }

        command = command.split(" ")[0].toLowerCase().trim();

        if (PlayerChatListener.BLACKLISTED_COMMANDS.contains(command)) {
            evt.setCancelled(true);
            messageTo(evt.getPlayer()).red("You have been muted!").send();
        }
    }
}
