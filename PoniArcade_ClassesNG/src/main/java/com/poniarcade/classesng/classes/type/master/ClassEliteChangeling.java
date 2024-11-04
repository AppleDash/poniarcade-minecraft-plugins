package com.poniarcade.classesng.classes.type.master;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.*;
import com.poniarcade.classesng.classes.power.type.SaddlePowerDisguise;

import java.util.Optional;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class ClassEliteChangeling extends Class {
    public ClassEliteChangeling() {
        super(
            ClassType.MASTER,
            "Elite Changeling",
            ImmutableList.of(
                new AbilityFlight(0.13f, 0.5f),
                new AbilityDamageReduction(20),
                new AbilityIncreaseMeleeDamage(10),
                new AbilityFoodSteal(2),
                new AbilityLifeSteal(6.0f),
                new AbilityNoNaturalRegen()
            ),
            "Fly faster, take less damage, and get even more heath when you eat mobs."
        );
	    this.setSaddlePower(new SaddlePowerDisguise(0.80f));
	    this.addRecipes(ClassRecipe.CHANGELING);
	    this.addRecipes(ClassRecipe.ELITE_CHANGELING);
	    this.setPrice(675_000.0D);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("You're now even scarier to small children. You monster.");
    }
}
