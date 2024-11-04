package com.poniarcade.core.commands.type;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.handlers.PlayerFlags;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.entity.Player;

/**
 * Created by appledash on 7/12/17.
 * Blackjack is best pony.
 */
@Command(name = "CommandSpy", description = "Spy on players' commands.", basePermission = "poniarcade.core.commandspy")
public class CommandSpyCommand extends BaseCommand {
    private final PoniArcade_Core plugin;

    public CommandSpyCommand(PoniArcade_Core plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand
    @CommandHelp(help = "Toggle your current Command Spy status.")
    public void handleCommandSpy(Player sender) {
        boolean spy = this.plugin.getPlayerFlags().toggleFlag(sender, PlayerFlags.FlagType.COMMAND_SPY);

        sender.sendMessage(ColorHelper.aqua("Command spy has been ").gold(spy ? "enabled" : "disabled").aqua(".").toString());
    }
}
