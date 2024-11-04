package com.poniarcade.classesng.classes.type;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityChangeMovementSpeed;
import com.poniarcade.classesng.classes.ability.type.AbilityDamageReduction;
import com.poniarcade.classesng.classes.ability.type.AbilityIncreaseMeleeDamage;
import com.poniarcade.classesng.classes.ability.type.AbilityInstantBonemeal;
import com.poniarcade.classesng.classes.power.type.SaddlePowerWorkbench;

import java.util.Optional;

/**
 * Created by appledash on 7/18/16.
 * Blackjack is still best pony.
 */
public class ClassEarthPony extends Class {
    public ClassEarthPony() {
        super(
            ClassType.NORMAL,
            "Earth Pony",
            ImmutableList.of(
                new AbilityDamageReduction(20),
                new AbilityIncreaseMeleeDamage(10),
                new AbilityChangeMovementSpeed(1),
                new AbilityInstantBonemeal()
            ),
            "Take less damage, deal more damage, walk a bit faster, and instantly grow things. Also, you can craft certain spawn eggs."
        );

	    this.setSaddlePower(new SaddlePowerWorkbench());
	    this.addRecipes(ClassRecipe.EARTH_PONY);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("You can beat things up easier and grow things better.");
    }
}
