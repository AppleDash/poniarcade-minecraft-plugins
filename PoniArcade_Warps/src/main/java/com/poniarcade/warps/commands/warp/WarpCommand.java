package com.poniarcade.warps.commands.warp;

import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.warps.PoniArcade_Warps;
import com.poniarcade.warps.TeleportLocation;
import com.poniarcade.warps.commands.TeleportLocationCommand;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by appledash on 10/30/16.
 * Blackjack is still best pony.
 */
@Command(name = "warp", description = "Warp yourself to a location.", basePermission = "poniarcade.warps.warp", aliases = "warps")
public class WarpCommand extends TeleportLocationCommand {
    private final PoniArcade_Warps plugin;

    public WarpCommand(PoniArcade_Warps plugin) {
        super(plugin, TeleportLocation.Type.WARP);
        this.plugin = plugin;
    }

    // A bit dumb but eh?
    @HandlesCommand(params = {Player.class, String.class}, permission = "poniarcade.warps.warp.other")
    public void handleTeleportOther(Player sender, Player target, String locationName) {
        Optional<TeleportLocation> warp = this.plugin.getTeleportManager().getTeleportLocation(TeleportLocation.Type.WARP, locationName);

        if (warp.isEmpty()) {
            sender.sendMessage(ColorHelper.aqua("No %s named ", "warp").gold(locationName).aqua(" exists!").toString());
            return;
        }

        this.teleportPlayer(sender, target, warp.get());
    }
}
