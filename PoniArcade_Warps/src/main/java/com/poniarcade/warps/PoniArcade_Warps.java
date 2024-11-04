package com.poniarcade.warps;

import com.poniarcade.core.PoniArcadePlugin;
import com.poniarcade.core.handlers.CommandHandler;
import com.poniarcade.warps.commands.spawn.DeleteSpawnCommand;
import com.poniarcade.warps.commands.spawn.SetSpawnCommand;
import com.poniarcade.warps.commands.spawn.SpawnCommand;
import com.poniarcade.warps.commands.warp.DeleteWarpCommand;
import com.poniarcade.warps.commands.warp.SetWarpCommand;
import com.poniarcade.warps.commands.warp.WarpCommand;
import com.poniarcade.warps.listeners.JoinQuitListener;

import java.util.logging.Logger;

/**
 * Created by appledash on 5/16/16.
 * Blackjack is still best pony.
 */
public class PoniArcade_Warps extends PoniArcadePlugin {
    private static PoniArcade_Warps INSTANCE;
    private final TeleportManager teleportManager;

    public PoniArcade_Warps() {
        PoniArcade_Warps.INSTANCE = this;
	    this.teleportManager = new TeleportManager();
    }

    @Override
    public void onEnable() {
        CommandHandler.addCommands(
            this,
            new DeleteSpawnCommand(this),
            new SetSpawnCommand(this),
            new SpawnCommand(this),
            new DeleteWarpCommand(this),
            new SetWarpCommand(this),
            new WarpCommand(this)
        );

        PoniArcadePlugin.server.getPluginManager().registerEvents(new JoinQuitListener(this), this);
        PoniArcadePlugin.server.getScheduler().runTaskTimerAsynchronously(this, this.teleportManager::reloadTeleportLocations, 0, 20 * (60 * 5)); /* Reload warps every 5 minutes */
    }

    public static Logger logger() {
        return PoniArcade_Warps.INSTANCE.getLogger();
    }

    public TeleportManager getTeleportManager() {
        return this.teleportManager;
    }
}
