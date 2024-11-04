package com.poniarcade.classesng.classes.power.type;

import com.poniarcade.classesng.classes.power.SaddlePower;
import com.poniarcade.core.utils.Utils;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class SaddlePowerFireball extends SaddlePower {
    private float foodMultiplier = 1.0f;

    public SaddlePowerFireball() {
        super("Fireball", "Shoot a fireball where you're looking.");
    }

    public SaddlePowerFireball(float foodMultiplier) {
        this();
        this.foodMultiplier = foodMultiplier;
    }

    @Override
    public void activatePrimary(Player player) {
        // Borrowed from old Classes plugin
        if ((player.getFoodLevel() >= 4) && (player.getGameMode() != GameMode.CREATIVE)) {
            Vector to = player.getEyeLocation().getDirection();
            to.multiply(3);
            player.getWorld().spawn(player.getEyeLocation().add(to.getX(), to.getY(), to.getZ()), Fireball.class);
            player.setFoodLevel(player.getFoodLevel() - (int)(4 * this.foodMultiplier));
            if (!Utils.isVanished(player)) {
                player.getWorld().playEffect(player.getEyeLocation(), Effect.SMOKE, 10);
                player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_GHAST_SHOOT, 2, 1);
            }
        }
    }
}
