package com.poniarcade.classesng.classes.type.master;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityFlight;
import com.poniarcade.classesng.classes.ability.type.AbilityWeatherChange;
import com.poniarcade.classesng.classes.power.type.SaddlePowerSpeedBurst;

import java.util.Optional;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class ClassMasterPegasus extends Class {
    public ClassMasterPegasus() {
        super(
            ClassType.MASTER,
            "Master Pegasus",
            ImmutableList.of(
                new AbilityFlight(0.15f, 0.5f),
                new AbilityWeatherChange()
            ),
            "An upgraded version of the Pegasus. Now you can fly and speed burst even faster - the fastest of any class."
        );

        this.setSaddlePower(new SaddlePowerSpeedBurst(5, 1.75f, 0.75f));
        this.addRecipes(ClassRecipe.PEGASUS);
        this.setPrice(675_000.0D);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("Your default rediculously-fast flight can be made even faster using your saddle!");
    }
}
