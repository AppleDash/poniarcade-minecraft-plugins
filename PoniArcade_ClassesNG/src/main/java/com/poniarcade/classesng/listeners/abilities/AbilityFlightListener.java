package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassManager;
import com.poniarcade.classesng.classes.ability.type.AbilityFlight;
import com.poniarcade.classesng.classes.power.SaddlePower;
import org.bukkit.event.Listener;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 *
 * Handles allowing Classes to fly.
 * @see AbilityFlight
 */
public class AbilityFlightListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilityFlightListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent evt) {
        ClassManager classManager = this.plugin.getClassManager();
        Player player = evt.getPlayer();

        Optional<Class> spoofedClass = classManager.getClassData(player).getSpoofedClass();
        Optional<Class> playerClass = classManager.getEffectiveClassForPlayer(player);

        // If they have a spoofed class and that spoofed class has no flight, set their flight to their real class' flight.
        if (playerClass.isPresent() && spoofedClass.isPresent() && (spoofedClass.get() != playerClass.get()) && spoofedClass.get().getAbility(AbilityFlight.class).isEmpty()) {
            classManager.getRealClassForPlayer(player).flatMap(realClass -> realClass.getAbility(AbilityFlight.class)).ifPresent(ability -> {
                this.doFlight(player, evt, ability);
            });

            return;
        }

        classManager.getEffectiveAbility(player, AbilityFlight.class).ifPresent(ability -> {
            Optional<SaddlePower> saddlePowerOptional = classManager.getEffectiveSaddlePower(player);
            if (saddlePowerOptional.isPresent() && saddlePowerOptional.get().modifiesAbility(AbilityFlight.class) && saddlePowerOptional.get().canActivate() && classManager.isSaddlePowerActive(player)) {
                return;
            }

            this.doFlight(player, evt, ability);
        });
    }

    private void doFlight(Player player, PlayerMoveEvent evt, AbilityFlight ability) {
        if ((player.getGameMode() == GameMode.CREATIVE) || (player.getGameMode() == GameMode.SPECTATOR)) {
            player.setFlySpeed(ability.getSpeed(player));
            return;
        }

        if (player.getFoodLevel() >= 4 && !evt.getPlayer().getLocation().getBlock().isLiquid()) {
            player.setAllowFlight(true);
            player.setFlySpeed(ability.getSpeed(player));

            if (this.isDifferentBlock(evt.getFrom(), evt.getTo()) && player.isFlying()) {
                player.setExhaustion(player.getExhaustion() + (0.2f * ability.getHungerMultiplier())); // Twice sprinting
            }
        } else {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }

    private boolean isDifferentBlock(Location from, Location to) {
        return (from.getBlockX() != to.getBlockX()) || (from.getBlockY() != to.getBlockY()) || (from.getBlockZ() != to.getBlockZ());
    }
}
