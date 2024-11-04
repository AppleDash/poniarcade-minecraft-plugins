package com.poniarcade.classesng.classes.ability.type;

import com.poniarcade.classesng.classes.ability.Ability;

/**
 * Created by appledash on 8/10/16.
 * Blackjack is still best pony.
 */
public class AbilityFoodSteal extends Ability {
    private final int amount;

    public AbilityFoodSteal(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }
}
