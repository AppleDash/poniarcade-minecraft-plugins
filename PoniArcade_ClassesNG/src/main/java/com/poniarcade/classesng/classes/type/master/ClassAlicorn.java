package com.poniarcade.classesng.classes.type.master;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.*;
import com.poniarcade.classesng.classes.power.SaddlePower;
import com.poniarcade.classesng.classes.power.type.SaddlePowerTeleport;
import com.poniarcade.classesng.classes.power.type.SaddlePowerWorkbench;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

/**
 * Created by appledash on 7/24/16.
 * Blackjack is still best pony.
 */
public class ClassAlicorn extends Class {
    public ClassAlicorn() {
        super(
            ClassType.MASTER,
            "Alicorn",
            ImmutableList.of(
                new AbilityDamageReduction(30),
                new AbilityIncreaseMeleeDamage(10),
                new AbilityChangeMovementSpeed(1),
                new AbilityFlight(0.14f, 0.5f),
                new AbilityIncreaseRangedDamage(30.0),
                new AbilityIncreaseXP(),
                new AbilityWeatherChange(),
                new AbilityArrowSplash()
            ),
            "An Alicorn. Has all the abilities of Earth, Unicorn, and Pegasus, minus any debuffs they may incur. Also does splash damage with arrows."
        );

        this.setSaddlePower(new SaddlePowerAlicorn());
        this.addRecipes(ClassRecipe.UNICORN);
        this.addRecipes(ClassRecipe.ALICORN);
        this.addRecipes(ClassRecipe.EARTH_PONY);
        this.setPrice(900_000.0D);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("You're basically God, what more do you even want from me?!");
    }

    private static final class SaddlePowerAlicorn extends SaddlePower {
        private final SaddlePower workbench = new SaddlePowerWorkbench();
        private final SaddlePower teleport = new SaddlePowerTeleport(0.75f);

        private SaddlePowerAlicorn() {
            super("Workbench & Teleport", "Allows you to open a workbench by right clicking, and teleport by left clicking.");
        }

        @Override
        public void activatePrimary(Player player) {
	        this.workbench.activatePrimary(player);
        }

        @Override
        public void activateSecondary(Player player, PlayerInteractEvent evt) {
	        this.teleport.activateSecondary(player, evt);
        }
    }
}
