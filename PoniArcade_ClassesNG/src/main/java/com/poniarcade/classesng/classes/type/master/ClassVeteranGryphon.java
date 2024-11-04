package com.poniarcade.classesng.classes.type.master;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.*;

import java.util.Optional;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class ClassVeteranGryphon extends Class {
    public ClassVeteranGryphon() {
        super(
            ClassType.MASTER,
            "Veteran Gryphon",
            ImmutableList.of(
                new AbilityFlight(0.14f, 0.5f),
                new AbilityIncreaseMeleeDamage(20),
                new AbilitySetUnarmedDamage(7.0f),
                new AbilityThorns(50, 1.0f, 4.0f),
                new AbilityUnarmedAreaOfEffect()

            ),
            "Do more damage with melee, more unarmed damage, your unarmed attacks now do splash damage, and you have an even greater chance to do more Thorns damage. Also, you fly a bit faster."
        );
	    this.addRecipes(ClassRecipe.GRYPHON);
	    this.setPrice(675_000.0D);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("You're the rudest of any class.");
    }
}
