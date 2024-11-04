package com.poniarcade.core.listeners;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.handlers.PlayerFlags;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class FlightListener implements Listener {
    @EventHandler
    public void flightCinematic(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (PoniArcade_Core.getInstance().getPlayerFlags().getFlag(player, PlayerFlags.FlagType.FLIGHT_CINEMATIC)
                && player.isFlying()) {
            // Taken from our old flight code. Seems to work, probably needs tweaking
            // We could just make this a PlayerInteractEvent and have it old-school flight
            Vector vector = player.getVelocity();

            vector.setY(-player.getLocation().getPitch() / 100);
            //Not sure if needed, Velocity of X and Z seem to always be 0 when flying.
            vector.setX(player.getLocation().getDirection().getX() / 1.85);
            vector.setZ(player.getLocation().getDirection().getZ() / 1.85);

            player.setVelocity(vector);
        }
    }
}
