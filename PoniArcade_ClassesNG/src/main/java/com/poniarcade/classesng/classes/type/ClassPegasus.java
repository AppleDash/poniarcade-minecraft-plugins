package com.poniarcade.classesng.classes.type;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityFlight;
import com.poniarcade.classesng.classes.ability.type.AbilityWeatherChange;
import com.poniarcade.classesng.classes.power.type.SaddlePowerSpeedBurst;

import java.util.Optional;

/**
 * Created by appledash on 7/18/16.
 * Blackjack is still best pony.
 */
public class ClassPegasus extends Class {
    public ClassPegasus() {
        super(
            ClassType.NORMAL,
            "Pegasus",
            ImmutableList.of(
                new AbilityFlight(0.12f),
                new AbilityWeatherChange()
            ),
            "You fly the fastest of any base class, and you can burst that even faster with your saddle."
        );

	    this.setSaddlePower(new SaddlePowerSpeedBurst(3, 1.75F));
	    this.addRecipes(ClassRecipe.PEGASUS);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("Just pick it, come on, everyone else does.");
    }
}
