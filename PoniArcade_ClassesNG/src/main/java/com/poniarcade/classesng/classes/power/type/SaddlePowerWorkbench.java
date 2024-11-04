package com.poniarcade.classesng.classes.power.type;

import com.poniarcade.classesng.classes.power.SaddlePower;
import org.bukkit.entity.Player;

/**
 * Created by appledash on 7/27/16.
 * Blackjack is still best pony.
 */
public class SaddlePowerWorkbench extends SaddlePower {
    public SaddlePowerWorkbench() {
        super("Workbench", "Open a portable crafting table.");
    }

    @Override
    public void activatePrimary(Player player) {
        player.openWorkbench(null, true);
    }
}
