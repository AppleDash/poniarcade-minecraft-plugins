package com.poniarcade.classesng.listeners.abilities;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityIncreaseFireDamage;
import com.poniarcade.classesng.classes.ability.type.AbilityNoHeatDamage;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 *
 * Handles both increasing and removal of fire damage.
 * @see AbilityNoHeatDamage
 * @see AbilityIncreaseFireDamage
 */
public class FireDamageListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public FireDamageListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent evt) {
        if (!(evt.getEntity() instanceof Player damagedPlayer)) {
            return;
        }

        if (this.isDamageCauseFire(evt.getCause())) {
            this.plugin.getClassManager().getEffectiveAbility(damagedPlayer, AbilityNoHeatDamage.class).ifPresent(ability -> {
                evt.setCancelled(true);
                damagedPlayer.setFireTicks(0);
            }); // No damage from fire sources
            this.plugin.getClassManager().getEffectiveAbility(damagedPlayer, AbilityIncreaseFireDamage.class).ifPresent(ability -> evt.setDamage(evt.getDamage() + (evt.getDamage() / (100.0D / ability.getPercent())))); // Increased damage from fire sources
        }
    }

    /**
     * Check if a DamageCause is "fire"-related. (Fire, lava, magma blocks.)
     * @param damageCause DamageCause to check.
     * @return True if fire-related, false otherwise.
     */
    private boolean isDamageCauseFire(DamageCause damageCause) {
        return (damageCause == DamageCause.FIRE) ||
               (damageCause == DamageCause.FIRE_TICK) ||
               (damageCause == DamageCause.LAVA) ||
               (damageCause == DamageCause.MELTING);
    }
}
