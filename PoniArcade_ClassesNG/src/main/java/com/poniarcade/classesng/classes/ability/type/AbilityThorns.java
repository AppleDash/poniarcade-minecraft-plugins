package com.poniarcade.classesng.classes.ability.type;

import com.poniarcade.classesng.classes.ability.Ability;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class AbilityThorns extends Ability {
    private final int percentChance;
    private final float minDamage;
    private final float maxDamage;

    public AbilityThorns(int percentChance, float minDamage, float maxDamage) {
        this.percentChance = percentChance;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }

    public int getPercentChance() {
        return this.percentChance;
    }

    public float getMinDamage() {
        return this.minDamage;
    }

    public float getMaxDamage() {
        return this.maxDamage;
    }
}
