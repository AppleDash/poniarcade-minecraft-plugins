package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityLifeSteal;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class AbilityLifeStealListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilityLifeStealListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void gainHealthOnKillMob(EntityDeathEvent evt) { // Gain health when you kill others
        if (evt.getEntity().getKiller() != null) {
            Player killer = evt.getEntity().getKiller();
            this.plugin.getClassManager().getEffectiveAbility(killer, AbilityLifeSteal.class).ifPresent(ability -> {
                killer.setHealth(Math.min(killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), killer.getHealth() + ability.getHeal()));
            });
        }
    }
}
