package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityIncreaseMeleeDamage;
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
public class AbilityIncreaseMeleeDamageListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilityIncreaseMeleeDamageListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void attackDamage(EntityDamageByEntityEvent evt) {
        if (!(evt.getDamager() instanceof Player damager)) {
            return;
        }

        if (evt.getCause() == DamageCause.ENTITY_ATTACK) {
            this.plugin.getClassManager().getEffectiveAbility(damager, AbilityIncreaseMeleeDamage.class).ifPresent(ability -> { // Increase melee damage
                double newDmg = evt.getDamage() + (evt.getDamage() / (100.0D / ability.getPercent(damager)));
                evt.setDamage(newDmg);
            });
        }
    }
}
