package com.poniarcade.classesng.classes.type.master;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityChangeMovementSpeed;
import com.poniarcade.classesng.classes.ability.type.AbilityDamageReduction;
import com.poniarcade.classesng.classes.ability.type.AbilityIncreaseRangedDamage;

import java.util.Optional;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class ClassTribalZebra extends Class {
    public ClassTribalZebra() {
        super(
            ClassType.MASTER,
            "Tribal Zebra",
            ImmutableList.of(
                new AbilityDamageReduction(20),
                new AbilityIncreaseRangedDamage(20),
                new AbilityChangeMovementSpeed(1)
            ),
            "An upgraded version of Zebra. You now take less damage and do extra damage with a bow."
        );
	    this.setPrice(675_000.0D);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("Still useless...");
    }
}
