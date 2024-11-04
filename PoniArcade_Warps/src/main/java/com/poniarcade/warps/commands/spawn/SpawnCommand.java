package com.poniarcade.warps.commands.spawn;

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
@Command(name = "spawn", description = "Teleport yourself to a spawn.", basePermission = "poniarcade.spawns.spawn", aliases = "spawns")
public class SpawnCommand extends TeleportLocationCommand {
    private final PoniArcade_Warps plugin;

    public SpawnCommand(PoniArcade_Warps plugin) {
        super(plugin, TeleportLocation.Type.SPAWN);
        this.plugin = plugin;
    }

    // A bit dumb but eh?
    @HandlesCommand(params = {Player.class, String.class}, permission = "poniarcade.spawns.spawn.other")
    public void handleTeleportOther(Player sender, Player target, String locationName) {
        Optional<TeleportLocation> warp = this.plugin.getTeleportManager().getTeleportLocation(TeleportLocation.Type.SPAWN, locationName);

        if (warp.isEmpty()) {
            sender.sendMessage(ColorHelper.aqua("No %s named ", "spawn").gold(locationName).aqua(" exists!").toString());
            return;
        }

        this.teleportPlayer(sender, target, warp.get());
    }

    @HandlesCommand
    public void handleRandomSpawn(Player sender) {
        Optional<TeleportLocation> spawn = this.plugin.getTeleportManager().getRandomTeleportLocation(TeleportLocation.Type.SPAWN);
        if (spawn.isEmpty()) {
            sender.sendMessage(ColorHelper.aqua("There are no spawns!").toString());
            return;
        }

        sender.sendMessage(ColorHelper.aqua("Sending you to spawn ").gold(spawn.get().getName()).aqua("...").toString());
        sender.teleport(spawn.get().getLocation());
    }
}
