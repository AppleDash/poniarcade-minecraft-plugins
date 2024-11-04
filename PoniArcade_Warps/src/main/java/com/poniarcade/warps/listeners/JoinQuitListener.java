package com.poniarcade.warps.listeners;

import org.bukkit.event.Listener;
import com.poniarcade.warps.PoniArcade_Warps;
import com.poniarcade.warps.TeleportLocation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

/**
 * Created by appledash on 11/1/16.
 * Blackjack is still best pony.
 */
public class JoinQuitListener implements Listener {
    private final PoniArcade_Warps plugin;

    public JoinQuitListener(PoniArcade_Warps plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        if (!evt.getPlayer().hasPlayedBefore()) {
            Optional<TeleportLocation> warpInfo = this.plugin.getTeleportManager().getTeleportLocation(TeleportLocation.Type.WARP, "info");

            if (warpInfo.isPresent()) {
                evt.getPlayer().teleport(warpInfo.get().getLocation());
            } else {
                this.plugin.getTeleportManager().getRandomTeleportLocation(TeleportLocation.Type.SPAWN).ifPresent((loc) -> {
                    evt.getPlayer().teleport(loc.getLocation());
                });
            }
        }
    }
}
