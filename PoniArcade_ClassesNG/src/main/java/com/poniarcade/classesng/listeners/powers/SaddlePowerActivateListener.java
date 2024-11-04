package com.poniarcade.classesng.listeners.powers;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import org.bukkit.event.Listener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by appledash on 7/27/16.
 * Blackjack is still best pony.
 */
public class SaddlePowerActivateListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public SaddlePowerActivateListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent evt) {
        Player player = evt.getPlayer();

        if (this.isHoldingSaddle(player)) {
            this.plugin.getClassManager().getEffectiveSaddlePower(player).ifPresent(saddlePower -> {
                if ((evt.getAction() == Action.RIGHT_CLICK_AIR) || (evt.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                    saddlePower.activatePrimary(player);
                } else if ((evt.getAction() == Action.LEFT_CLICK_AIR) || (evt.getAction() == Action.LEFT_CLICK_BLOCK)) {
                    saddlePower.activateSecondary(player, evt);
                }
            });
        }

    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent evt) {
        Player player = evt.getPlayer();

        if (this.isHoldingSaddle(player)) {
            this.plugin.getClassManager().getEffectiveSaddlePower(player).ifPresent(saddlePower -> {
                boolean shouldCancel = saddlePower.activatePrimaryAtEntity(player, evt.getRightClicked());

                evt.setCancelled(shouldCancel);
            });
        }
    }

    /**
     * Check if a Player is holding a Saddle in their main hand.
     * @param player Player to check.
     * @return True if they are currently holding a Saddle, false otherwise.
     */
    private boolean isHoldingSaddle(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        return (item != null) && (item.getType() == Material.SADDLE);
    }
}
