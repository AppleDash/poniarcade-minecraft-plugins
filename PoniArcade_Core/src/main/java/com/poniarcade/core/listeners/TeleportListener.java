package com.poniarcade.core.listeners;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.handlers.PlayerFlags;
import com.poniarcade.core.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by appledash on 12/21/16.
 * Blackjack is best pony.
 */
public class TeleportListener implements Listener {
    private final PoniArcade_Core plugin;
    private final Map<UUID, Long> lastTeleports = new HashMap<>();

    public TeleportListener(PoniArcade_Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void storePlayerBackLocation(PlayerTeleportEvent evt) {
        if ((evt.getCause() != PlayerTeleportEvent.TeleportCause.COMMAND) && (evt.getCause() != PlayerTeleportEvent.TeleportCause.PLUGIN)) {
            return;
        }

        long now = System.currentTimeMillis();
        UUID uuid = evt.getPlayer().getUniqueId();

        if (this.lastTeleports.containsKey(uuid)) {
            if ((now - this.lastTeleports.get(uuid)) < 1000) { // if they last teleported less than a second ago, don't count this as a valid teleport.
                return;
            }
        }

	    this.lastTeleports.put(uuid, now);

	    this.plugin.getPlayerFlags().setFlag(evt.getPlayer(), PlayerFlags.FlagType.BACK_LOCATION, evt.getFrom());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void adjustTeleports(PlayerTeleportEvent evt) {
        // TODO: What was this used for?
        // if (evt.getPlayer().isInsideVehicle()) {
        //     evt.getPlayer().leaveVehicle();
        // }

        if (evt.getPlayer().isInsideVehicle()) {
            evt.getPlayer().leaveVehicle();
        }

        evt.setTo(Utils.getSafeDestination(evt.getTo()));

        double radius = 12;

        for (Entity entity : evt.getPlayer().getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity livingEntity) {

                if (livingEntity.isLeashed() && livingEntity.getLeashHolder().getUniqueId().equals(evt.getPlayer().getUniqueId())) {
                    livingEntity.teleport(evt.getTo());
                    livingEntity.setFallDistance(0);
                    livingEntity.setLeashHolder(evt.getPlayer());
                }
            }
        }
    }
}
