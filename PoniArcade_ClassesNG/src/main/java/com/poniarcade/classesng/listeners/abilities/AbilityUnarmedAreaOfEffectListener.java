package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityUnarmedAreaOfEffect;
import org.bukkit.event.Listener;
import com.poniarcade.core.utils.Utils;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 *
 * Handles the unarmed area of effect ability.
 * @see AbilityUnarmedAreaOfEffect
 */
public class AbilityUnarmedAreaOfEffectListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public AbilityUnarmedAreaOfEffectListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent evt) {
        if (!(evt.getDamager() instanceof Player player)) {
            return;
        }

        if (evt.getCause() != DamageCause.ENTITY_ATTACK) {
            return;
        }


        this.plugin.getClassManager().getEffectiveAbility(player, AbilityUnarmedAreaOfEffect.class).ifPresent(ability -> {
            if (Utils.isHoldingNothing(player)) {
                this.doAreaOfEffect(player, evt.getEntity());
            }
        });
    }

    private void doAreaOfEffect(Player player, Entity damaged) {
        for (Entity nearby : damaged.getNearbyEntities(2, 2, 2)) {
            if ((nearby != player) && (nearby != damaged) && (nearby instanceof Damageable)) {
                ((Damageable) nearby).damage(1.0);
            }
        }
    }
}
