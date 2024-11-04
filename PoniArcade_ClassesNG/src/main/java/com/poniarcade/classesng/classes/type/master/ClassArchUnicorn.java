package com.poniarcade.classesng.classes.type.master;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityArrowSplash;
import com.poniarcade.classesng.classes.ability.type.AbilityIncreaseRangedDamage;
import com.poniarcade.classesng.classes.ability.type.AbilityIncreaseXP;
import com.poniarcade.classesng.classes.power.type.SaddlePowerTeleport;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class ClassArchUnicorn extends Class {
    public ClassArchUnicorn() {
        super(
            ClassType.MASTER,
            "Arch Unicorn",
            ImmutableList.of(
                new AbilityIncreaseRangedDamage(40),
                new AbilityArrowSplash(),
                new AbilityIncreaseXP()
            ),
            "An upgraded version of the Unicorn. Does even more damage with bows, uses less food to teleport, and arrows now do splash damage."
        );
	    this.setSaddlePower(new SaddlePowerTeleport(0.75f));
	    this.addRecipes(ClassRecipe.UNICORN);
	    this.addRecipes(ClassRecipe.ARCH_UNICORN);
	    this.setPrice(675_000.0D);
    }
}
