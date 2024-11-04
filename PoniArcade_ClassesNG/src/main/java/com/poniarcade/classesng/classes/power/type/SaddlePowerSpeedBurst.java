package com.poniarcade.classesng.classes.power.type;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.type.AbilityFlight;
import com.poniarcade.classesng.classes.power.SaddlePower;
import org.bukkit.entity.Player;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class SaddlePowerSpeedBurst extends SaddlePower {
    private final int seconds;
    private final float speedMultiplier;
    private float foodMultiplier = 1.0f;

    public SaddlePowerSpeedBurst(int seconds, float speedMultiplier) {
        super("Speed Burst", "Gives you a burst of increased speed for a short time. Can only be used while flying.");
        this.seconds = seconds;
        this.speedMultiplier = speedMultiplier;
        this.setActivatable();
        this.setModifiesAbility(AbilityFlight.class);
    }

    public SaddlePowerSpeedBurst(int seconds, float speedMultiplier, float foodMultiplier) {
        this(seconds, speedMultiplier);
        this.foodMultiplier = foodMultiplier;
    }

    @Override
    public void activatePrimary(Player player) {
        if (PoniArcade_ClassesNG.instance().getClassManager().isSaddlePowerActive(player)) {
            return;
        }

        if (player.getFoodLevel() > 4) {
            player.setFoodLevel((int) (player.getFoodLevel() - (4 * this.foodMultiplier)));
            player.setFlySpeed(player.getFlySpeed() * this.speedMultiplier);
            PoniArcade_ClassesNG.instance().getClassManager().setSaddlePowerActive(player, this, (long) this.seconds * 1000);
        }
    }

    @Override
    public void deactivateSaddlePower(Player player) {
        player.setFlySpeed(player.getFlySpeed() / this.speedMultiplier);
    }
}
