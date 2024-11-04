package com.poniarcade.classesng.classes.power.type;

import com.poniarcade.classesng.classes.power.SaddlePower;
import com.poniarcade.core.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class SaddlePowerBonemeal extends SaddlePower {
    public SaddlePowerBonemeal() {
        super("Bonemeal", "Allows you to instantly grow a plant by clicking on it.");
    }

    @Override
    public void activateSecondary(Player player, PlayerInteractEvent evt) {
        if ((evt.getAction() == Action.LEFT_CLICK_BLOCK) && Utils.isGrowable(evt.getClickedBlock())) {
            if (player.getFoodLevel() > 2) {
                if (Utils.growGrowable(evt.getClickedBlock())) {
                    player.setFoodLevel(player.getFoodLevel() - 2);
                    evt.setCancelled(true);
                }
            }
        }
    }
}
