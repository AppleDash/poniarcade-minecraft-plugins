package com.poniarcade.warps.commands.warp;

import com.poniarcade.core.commands.Command;
import com.poniarcade.warps.PoniArcade_Warps;
import com.poniarcade.warps.TeleportLocation;
import com.poniarcade.warps.commands.DeleteLocationCommand;

/**
 * Created by appledash on 9/27/16.
 * Blackjack is still best pony.
 */
@Command(name = "delwarp", description = "Delete warps.", basePermission = "poniarcade.warps.delete", aliases = {"warpdel", "removewarp", "deletewarp"})
public class DeleteWarpCommand extends DeleteLocationCommand {
    public DeleteWarpCommand(PoniArcade_Warps plugin) {
        super(plugin, TeleportLocation.Type.WARP);
    }
}