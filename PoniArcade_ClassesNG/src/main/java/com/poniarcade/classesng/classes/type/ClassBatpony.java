package com.poniarcade.classesng.classes.type;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityLightDarkFlight;
import com.poniarcade.classesng.classes.power.type.SaddlePowerNightVision;

import java.util.Optional;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class ClassBatpony extends Class {
    public ClassBatpony() {
        super(
            ClassType.NORMAL,
            "Batpony",
            ImmutableList.of(
                new AbilityLightDarkFlight(0.08f, 0.11f)
            ),
            "Slow flight in day, faster at night, can use your saddle to obtain night vision."
        );

	    this.setSaddlePower(new SaddlePowerNightVision(60));
	    this.addRecipes(ClassRecipe.BATPONY);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("EeEEEEEEEEeeeEEeeeEEeeeEeeeEEeeEEEeeEEeeEEeeEEEeeEEEeeeee.");
    }
}
