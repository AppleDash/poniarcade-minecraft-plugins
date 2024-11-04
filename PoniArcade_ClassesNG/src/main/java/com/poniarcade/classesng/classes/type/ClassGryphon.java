package com.poniarcade.classesng.classes.type;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityFlight;
import com.poniarcade.classesng.classes.ability.type.AbilityIncreaseMeleeDamage;
import com.poniarcade.classesng.classes.ability.type.AbilitySetUnarmedDamage;
import com.poniarcade.classesng.classes.ability.type.AbilityThorns;

import java.util.Optional;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class ClassGryphon extends Class {
    public ClassGryphon() {
        super(
            ClassType.NORMAL,
            "Gryphon",
            ImmutableList.of(
                new AbilityFlight(0.11f),
                new AbilityIncreaseMeleeDamage(10),
                new AbilitySetUnarmedDamage(6.0f),
                new AbilityThorns(25, 1.0f, 4.0f)
            ),
            "You can fly fast, do more melee damage, and hurt things pretty badly with your claws when unarmed. Also, you've got a chance to deal damage back to others (like Thorns.)"
        );

	    this.addRecipes(ClassRecipe.GRYPHON);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("You're pretty rude.");
    }
}
