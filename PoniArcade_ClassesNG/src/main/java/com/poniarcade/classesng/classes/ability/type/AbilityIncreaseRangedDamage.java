package com.poniarcade.classesng.classes.ability.type;

import com.poniarcade.classesng.classes.ability.Ability;

/**
 * Created by appledash on 7/24/16.
 * Blackjack is still best pony.
 */
public class AbilityIncreaseRangedDamage extends Ability {
    private final double percent;

    public AbilityIncreaseRangedDamage(double percent) {
        this.percent = percent;
    }

    public double getPercent() {
        return this.percent;
    }
}
