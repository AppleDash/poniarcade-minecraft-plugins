package com.poniarcade.core.listeners;

import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.SpawnEgg;

/**
 * Created by appledash on 4/26/17.
 * Blackjack is still best pony.
 */
public class MobHatListener implements Listener {
    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent evt) {
        Player player = evt.getPlayer();

        if ((evt.getAction() != Action.RIGHT_CLICK_AIR) || !player.isSneaking()) {
            return;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (!heldItem.hasItemMeta() || !(heldItem.getItemMeta() instanceof SpawnEggMeta spawnEggMeta)) {
            return;
        }

        if (!player.hasPermission("core.donator.mob-hats")) {
            return;
        }

        player.getPassengers().forEach(player::removePassenger);

        Entity spawned = player.getWorld().spawnEntity(player.getLocation(), spawnEggMeta.getSpawnedType());
        player.addPassenger(spawned);

        player.sendMessage(ColorHelper.blue("You now have a ").gold("%s", spawned.getName()).aqua(" on your head!").toString());
    }
}
