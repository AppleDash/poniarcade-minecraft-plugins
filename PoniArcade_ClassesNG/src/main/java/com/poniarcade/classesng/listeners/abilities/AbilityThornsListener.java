package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityThorns;
import org.bukkit.event.Listener;
import com.poniarcade.core.utils.EnhancedRandom;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by appledash on 8/11/16.
 * Blackjack is still best pony.
 */
public class AbilityThornsListener implements Listener {
    private final PoniArcade_ClassesNG plugin;
    private final EnhancedRandom rngesus = new EnhancedRandom();

    public AbilityThornsListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageByEntityEvent evt) {
        if (!(evt.getEntity() instanceof Player damagee)) {
            return;
        }

        if ((evt.getCause() == DamageCause.ENTITY_ATTACK) && (evt.getDamager() instanceof LivingEntity damager)) {
            this.plugin.getClassManager().getEffectiveAbility(damagee, AbilityThorns.class).ifPresent(ability -> { // Thorns
                if (this.rngesus.percentChance(ability.getPercentChance())) {
                    float dmg = this.rngesus.nextFloat(ability.getMinDamage(), ability.getMaxDamage());
                    damager.setLastDamageCause(new EntityDamageByEntityEvent(damagee, damager, DamageCause.THORNS, dmg));
                    damager.setHealth(Math.max(0, damager.getHealth() - dmg));
                }
            });
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(PlayerDeathEvent evt) { // Hack to show the right death message for Gryphon Thorns
        Player died = evt.getEntity();
        if ((died.getLastDamageCause() instanceof EntityDamageByEntityEvent cause) && (died.getLastDamageCause().getCause() == DamageCause.THORNS)) {
            if (cause.getDamager() instanceof Player) {
                evt.setDeathMessage(String.format("%s was killed trying to hurt %s.", died.getDisplayName(), ((Player) cause.getDamager()).getDisplayName()));
            }
        }
    }
}
