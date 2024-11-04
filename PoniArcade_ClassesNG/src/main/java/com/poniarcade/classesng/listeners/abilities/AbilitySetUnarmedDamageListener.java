package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilitySetUnarmedDamage;
import org.bukkit.event.Listener;
import com.poniarcade.core.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 *
 * Handles setting the unarmed damage of a Player.
 * TODO: Take into account charge? (This is impossible, find an alternative) - WTF is charge?
 * @see AbilitySetUnarmedDamage
 */
public class AbilitySetUnarmedDamageListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilitySetUnarmedDamageListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void attackDamage(EntityDamageByEntityEvent evt) {
        if (!(evt.getDamager() instanceof Player damager)) {
            return;
        }

        if (evt.getCause() == DamageCause.ENTITY_ATTACK) {
            if (Utils.isHoldingNothing(damager)) { // Set unarmed damage
                this.plugin.getClassManager().getEffectiveAbility(damager, AbilitySetUnarmedDamage.class).ifPresent(ability -> {
                    evt.setDamage(ability.getDamage());
                });
            }
        }
    }
}
