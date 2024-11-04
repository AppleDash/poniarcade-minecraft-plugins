package com.poniarcade.classesng.classes.ability.type;

import com.poniarcade.classesng.classes.ability.Ability;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class AbilityChangeMovementSpeed extends Ability {
    private final int speedLevel;

    public AbilityChangeMovementSpeed(int speedLevel) {
        this.speedLevel = speedLevel;
    }

    public int getSpeedLevel() {
        return this.speedLevel;
    }
}
