package com.poniarcade.classesng.classes.type.master;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityLightDarkDamageReduction;
import com.poniarcade.classesng.classes.ability.type.AbilityLightDarkFlight;
import com.poniarcade.classesng.classes.ability.type.AbilityLightDarkIncreaseMeleeDamage;
import com.poniarcade.classesng.classes.power.type.SaddlePowerNightVision;

import java.util.Optional;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class ClassKnightBatpony extends Class {
    public ClassKnightBatpony() {
        super(
            ClassType.MASTER,
            "Knight Batpony",
            ImmutableList.of(
                new AbilityLightDarkFlight(0.12f, 0.14f, 0.5f),
                new AbilityLightDarkDamageReduction(20, 40),
                new AbilityLightDarkIncreaseMeleeDamage(10, 20)
            ),
            "An upgraded version of Batpony. You now fly faster, take even less damage, and do slightly more damage depending on whether it's day or night."
        );
	    this.setSaddlePower(new SaddlePowerNightVision(120, 0.75f));
	    this.addRecipes(ClassRecipe.BATPONY);
	    this.setPrice(675_000.0D);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("SkreeEEEEEEEeeeeeeEEeeeEEeeEEeeeEeeeEEeeEEeeEEeeeEEeeEEEEEEEEEEEEEEEeeeeeeeEEEeeeeeeeeeeeeee!");
    }
}
