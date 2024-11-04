package com.poniarcade.classesng.classes.type.master;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityChangeMovementSpeed;
import com.poniarcade.classesng.classes.ability.type.AbilityDamageReduction;
import com.poniarcade.classesng.classes.ability.type.AbilityIncreaseMeleeDamage;
import com.poniarcade.classesng.classes.power.SaddlePower;
import com.poniarcade.classesng.classes.power.type.SaddlePowerBonemeal;
import com.poniarcade.classesng.classes.power.type.SaddlePowerWorkbench;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class ClassGrandEarth extends Class {
    public ClassGrandEarth() {
        super(
            ClassType.MASTER,
            "Grand Earth Pony",
            ImmutableList.of(
                new AbilityDamageReduction(40),
                new AbilityIncreaseMeleeDamage(10),
                new AbilityChangeMovementSpeed(1)
            ),
            "An upgraded version of the Earth Pony. You now deal more damage and take even less damage."
        );

        this.setSaddlePower(new SaddlePowerGrandEarth());
        this.addRecipes(ClassRecipe.EARTH_PONY);
        this.addRecipes(ClassRecipe.GRAND_EARTH_PONY);
        this.setPrice(675_000.0D);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("You beat things up easier. How rude.");
    }

    private static final class SaddlePowerGrandEarth extends SaddlePower {
        private final SaddlePower workbench = new SaddlePowerWorkbench();
        private final SaddlePower bonemeal = new SaddlePowerBonemeal();

        private SaddlePowerGrandEarth() {
            super("Workbench & Bonemeal", "Allows you to right click to open a portable workbench, or left click to instantly grow a plant.");
        }

        @Override
        public void activatePrimary(Player player) {
            this.workbench.activatePrimary(player);
        }

        @Override
        public void activateSecondary(Player player, PlayerInteractEvent evt) {
            this.bonemeal.activateSecondary(player, evt);
        }
    }
}
