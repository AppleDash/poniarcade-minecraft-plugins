package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ClassManager;
import com.poniarcade.classesng.classes.ability.type.AbilityChangeMovementSpeed;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.poniarcade.core.utils.Utils.hasBetterEffect;
import static com.poniarcade.core.utils.Utils.isInWater;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 *
 * Handles making Classes move faster or slower on land.
 * @see AbilityChangeMovementSpeed
 */
public class AbilityMovementSpeedListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilityMovementSpeedListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent evt) {
        ClassManager classManager = this.plugin.getClassManager();
        Player player = evt.getPlayer();

        classManager.getEffectiveAbility(player, AbilityChangeMovementSpeed.class).ifPresent(ability -> {
            if (isInWater(evt.getTo())) {
                return;
            }

            int amplifier = Math.abs(ability.getSpeedLevel()) - 1;

            if (ability.getSpeedLevel() > 0) {
                if (!hasBetterEffect(player, PotionEffectType.SPEED, amplifier)) { // If they have taken a speed potion, don't remove it from them.
                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, amplifier));
                }
            } else if (ability.getSpeedLevel() < 0) {
                if (!hasBetterEffect(player, PotionEffectType.SLOW, amplifier)) {
                    player.removePotionEffect(PotionEffectType.SLOW);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, amplifier));
                }
            }
        });
    }
}
