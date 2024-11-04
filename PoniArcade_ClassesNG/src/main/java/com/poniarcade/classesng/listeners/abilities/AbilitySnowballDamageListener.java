package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilitySnowballDamage;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class AbilitySnowballDamageListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilitySnowballDamageListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void snowballDamage(EntityDamageByEntityEvent evt) {
        if ((evt.getDamager() instanceof Snowball snowball) && !(evt.getEntity() instanceof Player)) {

            if (snowball.getShooter() instanceof Player) {
                this.plugin.getClassManager().getEffectiveAbility((Player) snowball.getShooter(), AbilitySnowballDamage.class).ifPresent(ability -> evt.setDamage(1.0));
            }
        }
    }
}
