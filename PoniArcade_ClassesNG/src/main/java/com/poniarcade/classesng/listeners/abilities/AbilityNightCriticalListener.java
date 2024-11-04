package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityNightCriticalHit;
import org.bukkit.event.Listener;
import com.poniarcade.core.utils.EnhancedRandom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class AbilityNightCriticalListener implements Listener {
    private final PoniArcade_ClassesNG plugin;
    private final EnhancedRandom rngesus = new EnhancedRandom();

    public AbilityNightCriticalListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void attackDamage(EntityDamageByEntityEvent evt) {
        if (!(evt.getDamager() instanceof Player damager)) {
            return;
        }


        if (evt.getCause() == DamageCause.ENTITY_ATTACK) {

            this.plugin.getClassManager().getEffectiveAbility(damager, AbilityNightCriticalHit.class).ifPresent(ability -> { // Better hits in the night
                if (damager.getLocation().getBlock().getLightLevel() <= 7) {
                    evt.setDamage(evt.getDamage() * this.rngesus.nextDouble(1.0, 1.5));
                }
            });
        }
    }
}
