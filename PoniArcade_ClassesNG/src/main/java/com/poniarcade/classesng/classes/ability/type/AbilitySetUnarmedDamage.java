package com.poniarcade.classesng.classes.ability.type;

import com.poniarcade.classesng.classes.ability.Ability;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class AbilitySetUnarmedDamage extends Ability {
    private final float staticValue;

    public AbilitySetUnarmedDamage(float staticValue) {
        this.staticValue = staticValue;
    }

    public float getDamage() {
        return this.staticValue;
    }
}
