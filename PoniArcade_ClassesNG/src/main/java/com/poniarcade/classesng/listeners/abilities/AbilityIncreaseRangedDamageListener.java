package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityIncreaseRangedDamage;
import org.bukkit.event.Listener;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by appledash on 8/11/16.
 * Blackjack is still best pony.
 */
public class AbilityIncreaseRangedDamageListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilityIncreaseRangedDamageListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageByEntityEvent evt) {
        if (!(evt.getDamager() instanceof Arrow arrow)) {
            return;
        }

        if (arrow.getShooter() instanceof Player shooter) {
            this.plugin.getClassManager().getEffectiveAbility(shooter, AbilityIncreaseRangedDamage.class).ifPresent(ability -> { // Increase ranged damage
                double newDmg = evt.getDamage() + (evt.getDamage() / (100.0D / ability.getPercent()));
                evt.setDamage(newDmg);
            });
        }
    }
}
