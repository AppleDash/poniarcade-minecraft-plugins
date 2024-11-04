package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityDamageReduction;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 *
 * Handles reducing the overall damage taken by a Player.
 */
public class AbilityDamageReductionListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilityDamageReductionListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent evt) {
        if (evt.getEntity() instanceof Player damagedPlayer) {

            this.plugin.getClassManager().getEffectiveAbility(damagedPlayer, AbilityDamageReduction.class).ifPresent(ability -> {
                double newDmg = evt.getDamage() - (evt.getDamage() / (100.0D / ability.getPercent(damagedPlayer)));
                if (newDmg < 1) { // Make sure they can't god mode
                    newDmg = 1;
                }
                evt.setDamage(newDmg);
            });
        }
    }

}
