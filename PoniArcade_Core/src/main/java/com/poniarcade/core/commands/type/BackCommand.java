package com.poniarcade.core.commands.type;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.handlers.PlayerFlags;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Command(name = "back", description = "Teleport back to your previous location.", basePermission = "core.teleport")
public class BackCommand extends BaseCommand {
    private final PoniArcade_Core plugin;

    public BackCommand(PoniArcade_Core plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand
    @CommandHelp(help = "Teleport back to your previous location.")
    public void handleBack(Player sender) {
        Location location = this.plugin.getPlayerFlags().getFlag(sender, PlayerFlags.FlagType.BACK_LOCATION);
        if (location == null) {
            sender.sendMessage(ColorHelper.aqua("You have no previous location.").toString());
            return;
        }

        sender.teleport(location);
    }
}
