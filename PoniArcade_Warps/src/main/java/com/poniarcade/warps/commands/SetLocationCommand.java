package com.poniarcade.warps.commands;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.StringUtil;
import com.poniarcade.warps.PoniArcade_Warps;
import com.poniarcade.warps.TeleportLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by appledash on 5/16/16.
 * Blackjack is still best pony.
 */
public class SetLocationCommand extends BaseCommand {
    private final PoniArcade_Warps plugin;
    private final TeleportLocation.Type type;

    protected SetLocationCommand(PoniArcade_Warps plugin, TeleportLocation.Type type) {
        super(plugin);
        this.plugin = plugin;
        this.type = type;
    }

    @HandlesCommand(params = String.class)
    @CommandHelp(usage = "<name>", help = "Set a new teleport point at your current location.")
    public void handleSet(Player sender, String name) {
        this.plugin.getTeleportManager().setLocation(this.type, name, sender.getLocation());
        sender.sendMessage(ColorHelper.aqua("%s set.", StringUtil.capitalizeWord(this.type.getSingular())).toString());
    }

    @HandlesCommand(params = {String.class, Double.class, Double.class, Double.class})
    @CommandHelp(usage = "<name> <x> <y> <z>", help = "Set a new teleport point at the given coordinates.")
    private void handleSet(Player sender, String name, double posX, double posY, double posZ) {
        this.plugin.getTeleportManager().setLocation(this.type, name, new Location(sender.getWorld(), posX, posY, posZ));
        sender.sendMessage(ColorHelper.aqua("%s set.", StringUtil.capitalizeWord(this.type.getSingular())).toString());
    }
}
