package com.poniarcade.classesng.classes.type;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityFlight;
import com.poniarcade.classesng.classes.ability.type.AbilityFoodSteal;
import com.poniarcade.classesng.classes.ability.type.AbilityLifeSteal;
import com.poniarcade.classesng.classes.ability.type.AbilityNoNaturalRegen;
import com.poniarcade.classesng.classes.power.type.SaddlePowerDisguise;

import java.util.Optional;

/**
 * Created by appledash on 8/5/16.
 * Blackjack is still best pony.
 */
public class ClassChangeling extends Class {
    public ClassChangeling() {
        super(
            ClassType.NORMAL,
            "Changeling",
            ImmutableList.of(
                new AbilityFlight(0.10f),
                new AbilityNoNaturalRegen(),
                new AbilityLifeSteal(4.0f),
                new AbilityFoodSteal(1)
            ),
            "Fly around, change into mobs, and heal by killing things (no natural regen.)"
        );
	    this.setSaddlePower(new SaddlePowerDisguise());
	    this.addRecipes(ClassRecipe.CHANGELING);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("Turns into mobs to scare small children.");
    }
}
