package com.poniarcade.warps.commands.spawn;

import com.poniarcade.core.commands.Command;
import com.poniarcade.warps.PoniArcade_Warps;
import com.poniarcade.warps.TeleportLocation;
import com.poniarcade.warps.commands.DeleteLocationCommand;

/**
 * Created by appledash on 10/30/16.
 * Blackjack is still best pony.
 */
@Command(name = "delspawn", description = "Delete spawns.", basePermission = "poniarcade.warps.delete", aliases = {"spwawndel", "deletespawn", "removespawn"})
public class DeleteSpawnCommand extends DeleteLocationCommand {
    public DeleteSpawnCommand(PoniArcade_Warps plugin) {
        super(plugin, TeleportLocation.Type.SPAWN);
    }
}
