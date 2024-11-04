package com.poniarcade.classesng.listeners;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityCraftAquaHelmet;
import org.bukkit.event.Listener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import java.util.Optional;

/**
 * Created by appledash on 8/12/16.
 * Blackjack is still best pony.
 *
 * Handles crafting events for Classes that have recipes, and ensuring that Classes can only craft their own items.
 */
public class CraftListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public CraftListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent evt) {
        if (!(evt.getView().getPlayer() instanceof Player player)) {
            return;
        }

        if (this.plugin.getClassManager().isClassRecipe(evt.getRecipe())) {
            Optional<com.poniarcade.classesng.classes.Class> playerClass = this.plugin.getClassManager().getEffectiveClassForPlayer(player);
            if (playerClass.isEmpty() || !playerClass.get().hasRecipe(evt.getRecipe())) {
                // first check is necessary because otherwise classless players can craft anything
                evt.getInventory().setResult(new ItemStack(Material.AIR, 0));
            }
        }
    }

    @EventHandler
    public void onItemCraft(CraftItemEvent evt) {
        if (evt.getInventory().getResult() == null) {
            return;
        }

        if (this.isHelmet(evt.getInventory().getResult().getType())) {
            if (evt.getWhoClicked() instanceof Player) {
                this.plugin.getClassManager().getEffectiveClassForPlayer(((Player) evt.getWhoClicked())).ifPresent(playerClass -> {
                    if (playerClass.getAbility(AbilityCraftAquaHelmet.class).isPresent()) {
                        evt.getInventory().getResult().addEnchantment(Enchantment.WATER_WORKER, 1);
                    }
                });
            }
        }
    }

    private boolean isHelmet(Material material) {
        return (material == Material.LEATHER_HELMET) ||
               (material == Material.IRON_HELMET) ||
               (material == Material.CHAINMAIL_HELMET) ||
               (material == Material.GOLDEN_HELMET) ||
               (material == Material.DIAMOND_HELMET);
    }
}
