package com.poniarcade.messaging.commands;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.messaging.PoniArcade_Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by appledash on 7/2/17.
 * Blackjack is best pony.
 */
@Command(name = "realname", description = "Find out a player's IGN from their nickname.", basePermission = "poniarcade.messaging.realname", aliases = "rn")
public class RealnameCommand extends BaseCommand {
    private final PoniArcade_Messaging plugin;

    public RealnameCommand(PoniArcade_Messaging plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand(params = String.class)
    public void handleRealname(CommandSender sender, String nickname) {
        String realName = this.getRealName(nickname);

        if (realName == null) {
            sender.sendMessage(ColorHelper.red("I can't find a player whose nickname is ").gold(nickname).red(".").toString());
            return;
        }

        sender.sendMessage(ColorHelper.gold(nickname).aqua("'s real name is ").gold(realName).aqua(".").toString());
    }

    private String getRealName(String nickname) {
        if (nickname.charAt(0) == '~') {
            nickname = nickname.substring(1);
        }

        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            String playerDisplay = player.getDisplayName();

            if (playerDisplay.toLowerCase().contains(nickname.toLowerCase())) {
                return player.getName();
            }
        }

        return null;
    }
}
