package com.poniarcade.classesng.classes.type;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.*;

import java.util.Optional;

/**
 * Created by appledash on 8/12/16.
 * Blackjack is still best pony.
 */
public class ClassSeapony extends Class {
    public ClassSeapony() {
        super(
            ClassType.NORMAL,
            "Seapony",
            ImmutableList.of(
                new AbilityCraftAquaHelmet(),
                new AbilitySwim(),
                new AbilityChangeMovementSpeed(-1),
                new AbilityIncreaseFireDamage(20),
                new AbilitySnowballDamage()
            ),
            "You can swim really well and see under water, but on land you are slower than everyone else."
        );

	    this.addRecipes(ClassRecipe.SEAPONY);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("Shoo-be-doo...");
    }
}
