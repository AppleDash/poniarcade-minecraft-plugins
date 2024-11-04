package com.poniarcade.classesng.classes.type;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityChangeMovementSpeed;
import com.poniarcade.classesng.classes.ability.type.AbilityDamageReduction;

import java.util.Optional;

/**
 * Created by appledash on 7/27/16.
 * Blackjack is still best pony.
 */
public class ClassZebra extends Class {
    public ClassZebra() {
        super(
            ClassType.NORMAL,
            "Zebra",
            ImmutableList.of(
                new AbilityDamageReduction(10),
                new AbilityChangeMovementSpeed(1)
            ),
            "You take less damage and can walk slightly faster. That's it for now."
        );
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("You're basically useless.");
    }
}
