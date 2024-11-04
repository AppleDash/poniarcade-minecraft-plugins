package com.poniarcade.classesng.listeners;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ability.type.AbilityInstantBonemeal;
import com.poniarcade.classesng.classes.ability.type.AbilityWeatherChange;
import org.bukkit.event.Listener;
import com.poniarcade.core.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by appledash on 8/12/16.
 * Blackjack is still best pony.
 */
public class InteractListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public InteractListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent evt) {
        Player player = evt.getPlayer();

        if (!this.plugin.getClassManager().hasEffectiveAbility(player, AbilityInstantBonemeal.class)) {
            return;
        }

        if ((evt.getAction() == Action.RIGHT_CLICK_BLOCK) && Utils.isGrowable(evt.getClickedBlock())) {
            if (!Utils.isHoldingBonemeal(player.getInventory())) {
                return;
            }

            if (Utils.growGrowable(evt.getClickedBlock())) {
                player.getInventory().removeItem(new ItemStack(player.getInventory().getItemInMainHand().getType(), 1));
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void handleWeather(PlayerInteractEvent evt) {
        Player player = evt.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if ((evt.getClickedBlock() != null) && (evt.getClickedBlock().getType() != Material.AIR)) { // They clicked on a block, probably trying to pick up water
            return;
        }

        if ((heldItem == null) || ((heldItem.getType() != Material.GLASS_BOTTLE) && (heldItem.getType() != Material.POTION))) { // They aren't holding the right item
            return;
        }

        if ((heldItem.getType() == Material.GLASS_BOTTLE) && player.getWorld().hasStorm()) { // Collect weather into bottle
            if (this.plugin.getClassManager().hasEffectiveAbility(player, AbilityWeatherChange.class)) {
                if (player.getWorld().hasStorm()) {
                    // Charged Rainwater
                    player.getInventory().setItemInMainHand(new ItemStack(ClassRecipe.Pegasus.CHARGED_RAINWATER_ITEM));
                } else {
                    // Regular Rainwater
                    player.getInventory().setItemInMainHand(new ItemStack(ClassRecipe.Pegasus.RAINWATER_ITEM));
                }

                player.setPlayerWeather(WeatherType.CLEAR);
            }
        } else if (heldItem.getType() == Material.POTION) { // Deploy full bottle of weather
            PlayerWeatherType playerWeatherType = this.getWeatherTypeFromItem(heldItem);
            if (playerWeatherType == null) {
                return;
            }

            if (playerWeatherType == PlayerWeatherType.SERVER) {
                player.resetPlayerWeather();
            } else {
                player.setPlayerWeather(playerWeatherType.getBukkitType());
            }

            if (player.getGameMode() != GameMode.CREATIVE) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.GLASS_BOTTLE));
            }
        }

        player.updateInventory();
        evt.setCancelled(true);
    }

    private PlayerWeatherType getWeatherTypeFromItem(ItemStack item) {
        if (item.isSimilar(ClassRecipe.Pegasus.RAINWATER_ITEM)) {
            return PlayerWeatherType.DOWNFALL;
        }

        if (item.isSimilar(ClassRecipe.Pegasus.CLEAR_SKIES_ITEM)) {
            return PlayerWeatherType.CLEAR;
        }

        if (item.isSimilar(ClassRecipe.Pegasus.CHARGED_RAINWATER_ITEM)) {
            return PlayerWeatherType.THUNDER; // TODO: Actually make it thunder?
        }

        if (item.isSimilar(ClassRecipe.Pegasus.ATMOSPHERIC_BALANCE_ITEM)) {
            return PlayerWeatherType.SERVER;
        }

        return null;
    }

    private enum PlayerWeatherType {
        DOWNFALL, THUNDER, CLEAR, SERVER;

        public WeatherType getBukkitType() {
            return switch (this) {
                case DOWNFALL, THUNDER -> WeatherType.DOWNFALL;
                case CLEAR -> WeatherType.CLEAR;
                case SERVER ->
                    throw new IllegalStateException("No Bukkit WeatherType exists for PlayerWeatherType " + this);
            };
        }
    }
}
