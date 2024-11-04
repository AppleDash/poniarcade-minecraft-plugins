package com.poniarcade.classesng.classes.type;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityDamageReduction;
import com.poniarcade.classesng.classes.ability.type.AbilityFlight;
import com.poniarcade.classesng.classes.ability.type.AbilityNoHeatDamage;
import com.poniarcade.classesng.classes.power.type.SaddlePowerFireball;

import java.util.Optional;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class ClassDragon extends Class {
    public ClassDragon() {
        super(
            ClassType.NORMAL,
            "Dragon",
            ImmutableList.of(
                new AbilityFlight(0.08f),
                new AbilityDamageReduction(20),
                new AbilityNoHeatDamage()
            ),
            "Fly somewhat slowly, take less damage, be immune to all fire damage, and shoot fire balls at things."
        );

        this.setSaddlePower(new SaddlePowerFireball());
        this.addRecipes(ClassRecipe.DRAGON);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("Basically the best minding class ever.");
    }
}
