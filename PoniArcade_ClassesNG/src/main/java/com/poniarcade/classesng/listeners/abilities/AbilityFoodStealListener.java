package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityFoodSteal;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class AbilityFoodStealListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilityFoodStealListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void attackDamage(EntityDamageByEntityEvent evt) {
        if (!(evt.getDamager() instanceof Player damager)) {
            return;
        }

        if (evt.getCause() == DamageCause.ENTITY_ATTACK) {
            this.plugin.getClassManager().getEffectiveAbility(damager, AbilityFoodSteal.class).ifPresent(ability -> { // Regen food on attack
                if (damager.getFoodLevel() < 20.0) {
                    damager.setFoodLevel(Math.min(20, damager.getFoodLevel() + ability.getAmount()));
                }
            });
        }
    }
}
