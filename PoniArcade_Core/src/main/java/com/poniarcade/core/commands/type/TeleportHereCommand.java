package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.HandlesCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

/**
 * Created by appledash on 7/16/17.
 * Blackjack is best pony.
 */
@Command(name = "TeleportHere", description = "Teleport a player to you.", basePermission = "poniarcade.core.teleport", aliases = { "tphere", "bring" })
public class TeleportHereCommand extends BaseCommand {

    public TeleportHereCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand(params = Player.class)
    public void handleTpHere(Player sender, Player target) {
        if (sender == target) {
            messageTo(sender).red("You may not teleport yourself to yourself!").send();
        }

        target.teleport(sender);
        messageTo(sender).aqua("Teleporting ").gold(target.getDisplayName()).aqua(" to you.").send();
        messageTo(target).gold(sender.getDisplayName()).aqua(" is teleporting you to them.").send();
    }
}
