package com.poniarcade.warps.commands.spawn;

import com.poniarcade.core.commands.Command;
import com.poniarcade.warps.PoniArcade_Warps;
import com.poniarcade.warps.TeleportLocation;
import com.poniarcade.warps.commands.SetLocationCommand;

/**
 * Created by appledash on 10/30/16.
 * Blackjack is still best pony.
 */
@Command(name = "setspawn", description = "Set new spawns.", basePermission = "poniarcade.spawns.set", aliases = {"spawnset", "createspawn"})
public class SetSpawnCommand extends SetLocationCommand {
    public SetSpawnCommand(PoniArcade_Warps plugin) {
        super(plugin, TeleportLocation.Type.SPAWN);
    }
}
