package com.poniarcade.classesng.listeners;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.PlayerClassChangeEvent;
import com.poniarcade.classesng.classes.ability.type.AbilityFlight;
import org.bukkit.event.Listener;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class ClassListener implements Listener {
    @EventHandler
    public void onPlayerClassChange(PlayerClassChangeEvent evt) {
        Player player = evt.getPlayer();
        if (evt.getOldClass() != null) {
            PoniArcade_ClassesNG.instance().getClassManager().getClassData(player).removeSpoofedClass();

            // If their new class is allowed to fly, make sure they can.
            if (evt.getNewClass().getAbility(AbilityFlight.class).isPresent()) {
                player.setAllowFlight(true);
            } else {
                // Previously could fly, can't fly now and shouldn't due to their gamemode
                // TODO: Hook into future /fly support?
                if (evt.getOldClass().getAbility(AbilityFlight.class).isPresent() &&
                        ((player.getGameMode() != GameMode.CREATIVE) && (player.getGameMode() != GameMode.SPECTATOR))) {
                    evt.getPlayer().setAllowFlight(false);
                    evt.getPlayer().setFlying(false);
                }
            }

            /* Make sure they don't retain their saddle power if they have one */
            evt.getOldClass().getSaddlePower().ifPresent(saddlePower -> {
                if (saddlePower.canActivate()) {
                    PoniArcade_ClassesNG.instance().getClassManager().deactivateSaddlePower(evt.getPlayer());
                }
            });
        } else if (evt.getNewClass().getAbility(AbilityFlight.class).isPresent()) {
            player.setAllowFlight(true);
        }
    }
}
