package com.poniarcade.classesng.classes.type;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityIncreaseRangedDamage;
import com.poniarcade.classesng.classes.ability.type.AbilityIncreaseXP;
import com.poniarcade.classesng.classes.power.type.SaddlePowerTeleport;

import java.util.Optional;

/**
 * Created by appledash on 7/24/16.
 * Blackjack is still best pony.
 */
public class ClassUnicorn extends Class {
    public ClassUnicorn() {
        super(
            ClassType.NORMAL,
            "Unicorn",
            ImmutableList.of(
                new AbilityIncreaseRangedDamage(20.0),
                new AbilityIncreaseXP()
            ),
            "You get more XP and your bows do more damage. You can also teleport around using a saddle."
        );

	    this.setSaddlePower(new SaddlePowerTeleport());
	    this.addRecipes(ClassRecipe.UNICORN);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("Your bows are 20% cooler.");
    }
}
