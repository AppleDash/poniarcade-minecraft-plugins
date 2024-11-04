package com.poniarcade.classesng.classes.ability.type;

import com.poniarcade.classesng.classes.ability.Ability;

/**
 * Created by appledash on 8/5/16.
 * Blackjack is still best pony.
 */
public class AbilityLifeSteal extends Ability {
    private final float heal;

    public AbilityLifeSteal(float heal) {
        this.heal = heal;
    }

    public float getHeal() {
        return this.heal;
    }
}
