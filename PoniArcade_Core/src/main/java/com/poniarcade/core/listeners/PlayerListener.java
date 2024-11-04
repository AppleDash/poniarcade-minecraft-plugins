package com.poniarcade.core.listeners;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.handlers.PlayerFlags;
import com.poniarcade.core.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

public class PlayerListener implements Listener {

    private final PoniArcade_Core plugin;

    public PlayerListener(PoniArcade_Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.hasPermission("core.teleport")) {
            PoniArcade_Core.getInstance().getPlayerFlags().setFlag(player, PlayerFlags.FlagType.BACK_LOCATION, Utils.getSafeDestination(player.getLocation()));
        }
    }

    @EventHandler
    public void playerDamage(EntityDamageEvent event) {
        this.cancelIfGodded(event.getEntity(), event);
    }

    @EventHandler
    public void playerDamage(FoodLevelChangeEvent event) {
        this.cancelIfGodded(event.getEntity(), event);
    }

    private void cancelIfGodded(Entity entity, Cancellable event) {
        if (entity instanceof Player player) {
            PlayerFlags playerFlags = PoniArcade_Core.getInstance().getPlayerFlags();
            if (playerFlags.getFlag(player, PlayerFlags.FlagType.GOD_MODE)) {
                event.setCancelled(true);
            }
        }
    }

    // Blocks editing in invsee for those without permission
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity clicker = event.getWhoClicked();
        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (event.getInventory().getType() == InventoryType.PLAYER && clicker instanceof Player clickerPlayer && inventoryHolder instanceof Player holderPlayer) {
            if (clickerPlayer != holderPlayer && !clickerPlayer.hasPermission("core.invsee.edit")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void colorSigns(SignChangeEvent event) {
        for (int i = 0; i < event.getLines().length; i++) {
            event.setLine(i, ChatColor.translateAlternateColorCodes('&', event.getLine(i)));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.useInteractedBlock() == Event.Result.DENY && event.getAction() != Action.LEFT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        Material tool = event.getMaterial();
        if (tool != Material.AIR) {
            this.plugin.getPlayerFlags().getFlag(event.getPlayer(), PlayerFlags.FlagType.POWER_TOOLS).getPowerTools(tool).forEach(cmd -> {
                this.plugin.getServer().dispatchCommand(event.getPlayer(), cmd.charAt(0) == '/' ? cmd.substring(1) : cmd);
            });
        }
    }

    @EventHandler
    public void onPlayerGamemode(PlayerGameModeChangeEvent evt) {
        PoniArcade_Core.getInstance().getPlayerFlags().setFlag(evt.getPlayer(), PlayerFlags.FlagType.GAME_MODE, evt.getNewGameMode());
    }

    /*
    @EventHandler
    public void onInventoryCreativeEvent(InventoryCreativeEvent event) {
        if (event.getCurrentItem() != null) {
            event.setCurrentItem(Utils.addCustomNBTString(event.getCurrentItem(), "poniarcade-given", "creative"));
        }

        if (event.getCursor() != null) {
            event.setCursor(Utils.addCustomNBTString(event.getCurrentItem(), "poniarcade-given", "creative"));
        }
    }
    */
}
