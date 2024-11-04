package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityIncreaseXP;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;

/**
 * Created by appledash on 8/12/16.
 * Blackjack is still best pony.
 */
public class AbilityIncreaseXPListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilityIncreaseXPListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onXPChange(PlayerExpChangeEvent evt) {
        this.plugin.getClassManager().getEffectiveAbility(evt.getPlayer(), AbilityIncreaseXP.class).ifPresent(ability -> evt.setAmount((int) Math.ceil((evt.getAmount() * 1.5))));
    }
}
