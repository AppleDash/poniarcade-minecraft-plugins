package com.poniarcade.classesng.classes.type.master;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityCraftAquaHelmet;
import com.poniarcade.classesng.classes.ability.type.AbilityIncreaseFireDamage;
import com.poniarcade.classesng.classes.ability.type.AbilitySnowballDamage;
import com.poniarcade.classesng.classes.ability.type.AbilitySwim;

import java.util.Optional;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class ClassAbyssalSeapony extends Class {
    public ClassAbyssalSeapony() {
        super(
            ClassType.MASTER,
            "Abyssal Seapony",
            ImmutableList.of(
                new AbilitySwim(),
                new AbilityIncreaseFireDamage(10),
                new AbilityCraftAquaHelmet(),
                new AbilitySnowballDamage()
            ),
            "An upgraded version of the Seapony."
        );
	    this.addRecipes(ClassRecipe.SEAPONY);
	    this.addRecipes(ClassRecipe.ABYSSAL_SEAPONY);
	    this.setPrice(675_000.0D);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("You finally grew legs so you don't walk so slowly on land.");
    }
}
