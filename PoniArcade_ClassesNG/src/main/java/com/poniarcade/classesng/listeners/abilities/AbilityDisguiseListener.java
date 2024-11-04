package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.power.type.SaddlePowerDisguise;
import org.bukkit.event.Listener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 *
 * Handles events for players who have the Disguise Saddle Power activated.
 * @see SaddlePowerDisguise
 */
public class AbilityDisguiseListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilityDisguiseListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void disableTargetingDisguised(EntityTargetEvent evt) { // Disable monsters targeting disguised players
        Entity target = evt.getTarget();
        if (target instanceof Player player) {
            this.plugin.getClassManager().getEffectiveSaddlePower(player).ifPresent(saddlePower -> {
                if ((saddlePower instanceof SaddlePowerDisguise) && this.plugin.getClassManager().isSaddlePowerActive(player)) {
                    evt.setCancelled(true);
                    evt.setTarget(null);
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void undisguiseOnDealDamage(EntityDamageByEntityEvent evt) {
        if (evt.getDamager() instanceof Player) { // Undisguise players when they hit others.
            this.undisguise(((Player) evt.getDamager()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void undisguiseOnDamage(EntityDamageEvent evt) {
        if (evt.getEntity() instanceof Player damagedPlayer) { // Undisguise players when they are damaged in any way.
            this.undisguise(damagedPlayer);
        }
    }

    /**
     * Deactivate a Player's Disguise SaddlePower, if they have it.
     * @param player Player to deactivate Disguise for.
     */
    private void undisguise(Player player) {
        this.plugin.getClassManager().getEffectiveSaddlePower(player).ifPresent(saddlePower -> {
            if ((saddlePower instanceof SaddlePowerDisguise) && this.plugin.getClassManager().isSaddlePowerActive(player)) {
                this.plugin.getClassManager().deactivateSaddlePower(player);
            }
        });
    }
}
