package com.poniarcade.classesng.listeners.powers;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ClassManager;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 *
 * Handles movement events for Saddle Powers that do something whenever the Player moves.
 */
public class SaddlePowerMoveListener implements Listener {
    private final PoniArcade_ClassesNG plugin;

    public SaddlePowerMoveListener(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent evt) {
        Player player = evt.getPlayer();
        ClassManager classManager = this.plugin.getClassManager();

        classManager.getEffectiveSaddlePower(player).ifPresent(saddlePower -> {
            if (classManager.isSaddlePowerActive(player)) {
                saddlePower.doSaddlePowerOnMove(player);
            }
        });
    }
}
