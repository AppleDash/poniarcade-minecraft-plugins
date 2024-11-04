package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityNoNaturalRegen;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class AbilityNoNaturalRegenListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilityNoNaturalRegenListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void disableHealthRegain(EntityRegainHealthEvent evt) { // Disable natural regen
        if ((evt.getEntity() instanceof Player) && (evt.getRegainReason() == RegainReason.SATIATED)) {
            this.plugin.getClassManager().getEffectiveAbility(((Player) evt.getEntity()), AbilityNoNaturalRegen.class).ifPresent(ability -> {
                evt.setCancelled(true);
            });
        }
    }
}
