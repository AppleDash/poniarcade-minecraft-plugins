package com.poniarcade.classesng.classes.power.type;

import com.poniarcade.classesng.classes.power.SaddlePower;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class SaddlePowerNightVision extends SaddlePower {
    private final int durationSeconds;
    private float foodMultiplier = 1.0f;

    public SaddlePowerNightVision(int durationSeconds) {
        super("Night Vision", "Allows you to see in the dark for a short period of time.");
        this.durationSeconds = durationSeconds;
    }

    public SaddlePowerNightVision(int durationSeconds, float foodMultiplier) {
        this(durationSeconds);
        this.foodMultiplier = foodMultiplier;
    }

    @Override
    public void activatePrimary(Player player) {
        if ((player.getFoodLevel() >= 2) && !player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, this.durationSeconds * 20, 0, false)); // 20 ticks = 1 second
            player.setFoodLevel((int) (player.getFoodLevel() - (2 * this.foodMultiplier)));
        }
    }
}
