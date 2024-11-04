package com.poniarcade.warps.commands.warp;

import com.poniarcade.core.commands.Command;
import com.poniarcade.warps.PoniArcade_Warps;
import com.poniarcade.warps.TeleportLocation;
import com.poniarcade.warps.commands.SetLocationCommand;

/**
 * Created by appledash on 9/27/16.
 * Blackjack is still best pony.
 */
@Command(name = "setwarp", description = "Set new warps.", basePermission = "poniarcade.warps.set", aliases = {"warpset", "createwarp"})
public class SetWarpCommand extends SetLocationCommand {
    public SetWarpCommand(PoniArcade_Warps plugin) {
        super(plugin, TeleportLocation.Type.WARP);
    }
}
