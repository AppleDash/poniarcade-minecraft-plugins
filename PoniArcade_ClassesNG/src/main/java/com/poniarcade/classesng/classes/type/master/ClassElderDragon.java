package com.poniarcade.classesng.classes.type.master;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.*;
import com.poniarcade.classesng.classes.power.type.SaddlePowerFireball;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class ClassElderDragon extends Class {
    public ClassElderDragon() {
        super(
            ClassType.MASTER,
            "Elder Dragon",
            ImmutableList.of(
                new AbilityFlight(0.12f, 0.5f),
                new AbilityDamageReduction(40),
                new AbilityIncreaseMeleeDamage(10),
                new AbilitySetUnarmedDamage(6),
                new AbilityNoHeatDamage()
            ),
            "An upgraded version of the Dragon. Take even less damage, do even more damage, fly a bit faster, and now you can hurt people badly when unarmed using your claws."
        );
	    this.setSaddlePower(new SaddlePowerFireball(0.75f));
	    this.addRecipes(ClassRecipe.DRAGON);
	    this.addRecipes(ClassRecipe.ELDER_DRAGON);
	    this.setPrice(675_000.0D);
    }
}
