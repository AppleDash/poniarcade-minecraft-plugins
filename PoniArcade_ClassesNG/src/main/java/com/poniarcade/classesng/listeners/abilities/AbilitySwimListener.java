package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilitySwim;
import org.bukkit.event.Listener;
import com.poniarcade.core.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.poniarcade.core.utils.Utils.hasBetterEffect;
import static com.poniarcade.core.utils.Utils.isInWater;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 *
 * Handles making Classes able to swim really fast underwater, and have night vision when they enter water.
 * @see AbilitySwim
 */
public class AbilitySwimListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilitySwimListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent evt) {
        Player player = evt.getPlayer();

        this.plugin.getClassManager().getEffectiveAbility(player, AbilitySwim.class).ifPresent(ability -> {
            if (isInWater(evt.getTo()) && (player.getFoodLevel() >= 10)) {
                if (!hasBetterEffect(player, PotionEffectType.SLOW, 0)) {
                    player.removePotionEffect(PotionEffectType.SLOW);
                }

                if (Utils.hasNegativePotionEffect(player)) {
                    return;
                }

                player.setAllowFlight(true);
                player.setFlying(true);

                if ((player.getGameMode() == GameMode.SURVIVAL) || (player.getGameMode() == GameMode.ADVENTURE)) {
                    player.setExhaustion(player.getExhaustion() + 0.07f);
                    player.setFallDistance(0); // Fix for dying when you leave water?
                }
            } else if ((player.getGameMode() != GameMode.CREATIVE) && !isInWater(evt.getTo())) {
                player.setFlying(false);
                player.setAllowFlight(false);
            }
        });
    }

    @EventHandler
    public void addNightVision(PlayerMoveEvent evt) {
        Player player = evt.getPlayer();

        this.plugin.getClassManager().getEffectiveAbility(player, AbilitySwim.class).ifPresent(ability -> {
            if (!isInWater(evt.getFrom()) && isInWater(evt.getTo())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, (20 * 30), 0));
            } else if (isInWater(evt.getFrom()) && !isInWater(evt.getTo())) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent evt) {
        if ((evt.getEntity() instanceof Player player) && (evt.getCause() == DamageCause.DROWNING)) { // Prevent drowning with Swim ability.
            this.plugin.getClassManager().getEffectiveAbility(player, AbilitySwim.class).ifPresent(ability -> {
                evt.setCancelled(true);
            });
        }
    }
}
