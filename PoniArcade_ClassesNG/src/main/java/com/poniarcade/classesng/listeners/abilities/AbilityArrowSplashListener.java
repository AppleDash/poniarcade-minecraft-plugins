package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityArrowSplash;
import org.bukkit.event.Listener;
import com.poniarcade.core.utils.Utils;
import org.bukkit.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class AbilityArrowSplashListener implements Listener {
    private static final double SPLASH_DAMAGE_AMOUNT = 1.0d;
    private final PoniArcade_ClassesNG plugin;

    public AbilityArrowSplashListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent evt) {
        if (!(evt.getDamager() instanceof Arrow arrow)) {
            return;
        }

        if (!(arrow.getShooter() instanceof Player shooter)) {
            return;
        }

        this.plugin.getClassManager().getEffectiveAbility(shooter, AbilityArrowSplash.class).ifPresent(ability -> {
            this.doSplashDamage(shooter, arrow);
        });
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent evt) {
        if (!(evt.getEntity() instanceof Arrow arrow)) {
            return;
        }

        if (!(arrow.getShooter() instanceof Player shooter)) {
            return;
        }

        this.plugin.getClassManager().getEffectiveAbility(shooter, AbilityArrowSplash.class).ifPresent(ability -> {
            this.doSplashDamage(shooter, arrow);
        });

    }

    private void doSplashDamage(Player source, Entity arrow) {
        if (!Utils.isVanished(source)) {
            // TODO arrow.getWorld().playEffect(arrow.getLocation(), Effect.WITCH_MAGIC, 10);
            arrow.getWorld().playEffect(arrow.getLocation(), Effect.PORTAL_TRAVEL, 10);
        }

        for (Entity nearby : arrow.getNearbyEntities(3, 3, 3)) {
            if ((nearby instanceof Damageable) && (nearby != source)) {
                ((Damageable) nearby).damage(AbilityArrowSplashListener.SPLASH_DAMAGE_AMOUNT, source);
            }
        }
    }
}
