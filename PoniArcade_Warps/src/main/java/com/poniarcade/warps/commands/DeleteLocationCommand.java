package com.poniarcade.warps.commands;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.StringUtil;
import com.poniarcade.warps.PoniArcade_Warps;
import com.poniarcade.warps.TeleportLocation;
import org.bukkit.entity.Player;

/**
 * Created by appledash on 7/23/16.
 * Blackjack is still best pony.
 */
public class DeleteLocationCommand extends BaseCommand {
    private final PoniArcade_Warps plugin;
    private final TeleportLocation.Type type;

    protected DeleteLocationCommand(PoniArcade_Warps plugin, TeleportLocation.Type type) {
        super(plugin);
        this.plugin = plugin;
        this.type = type;
    }

    @HandlesCommand(params = String.class)
    @CommandHelp(usage = "<name>", help = "Delete the given teleport point.")
    public void handleDelWarp(Player player, String locationName) {
        if (this.plugin.getTeleportManager().getTeleportLocation(this.type, locationName).isEmpty()) {
            player.sendMessage(ColorHelper.aqua("That %s doesn't exist!", this.type.getSingular()).toString());
            return;
        }

	    this.plugin.getTeleportManager().deleteLocation(this.type, locationName);

        player.sendMessage(ColorHelper.aqua("%s deleted.", StringUtil.capitalizeWord(this.type.getSingular())).toString());
    }
}
