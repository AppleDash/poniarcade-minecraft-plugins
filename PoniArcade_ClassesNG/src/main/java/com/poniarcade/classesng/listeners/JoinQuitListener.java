package com.poniarcade.classesng.listeners;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityFlight;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class JoinQuitListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public JoinQuitListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent evt) {
        // If we're in the middle of syncing their class data (Very unlucky), disallow the join so that weird things
        // don't happen.
        if (this.plugin.getClassManager().getClassStorage().isSyncingPlayer(evt.getPlayer().getUniqueId())) {
            evt.disallow(Result.KICK_OTHER, "You joined at a bad time! Please try connecting again.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();

        this.plugin.getClassManager().ensureExpiries(evt.getPlayer().getUniqueId(), this.plugin.getClassManager().getClassData(player));

        this.plugin.getClassManager().getEffectiveAbility(player, AbilityFlight.class).ifPresent(ability -> {
            player.setAllowFlight(true);
            player.setFlySpeed(ability.getSpeed(player));
        });
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent evt) {
        // Make sure their saddle power is gone and their data gets synced.
        this.plugin.getClassManager().deactivateSaddlePower(evt.getPlayer());
        this.plugin.getClassManager().getClassStorage().syncPlayerAsync(evt.getPlayer().getUniqueId());
    }
}
