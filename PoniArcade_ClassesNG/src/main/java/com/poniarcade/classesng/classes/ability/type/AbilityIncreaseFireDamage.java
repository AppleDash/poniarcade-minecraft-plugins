package com.poniarcade.classesng.classes.ability.type;

import com.poniarcade.classesng.classes.ability.Ability;

/**
 * Created by appledash on 8/12/16.
 * Blackjack is still best pony.
 */
public class AbilityIncreaseFireDamage extends Ability {
    private final int percent;

    public AbilityIncreaseFireDamage(int percent) {
        this.percent = percent;
    }

    public int getPercent() {
        return this.percent;
    }
}
