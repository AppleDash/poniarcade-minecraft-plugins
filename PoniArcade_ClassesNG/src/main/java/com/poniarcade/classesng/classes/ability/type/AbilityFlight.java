package com.poniarcade.classesng.classes.ability.type;

import com.poniarcade.classesng.classes.ability.Ability;
import com.poniarcade.classesng.classes.util.AbilityParamProducer;
import org.bukkit.entity.Player;

/**
 * Created by appledash on 7/18/16.
 * Blackjack is still best pony.
 */
public class AbilityFlight extends Ability {
    private final AbilityParamProducer<Float> speed;
    protected float hungerMultiplier = 1.0f;

    public AbilityFlight(float speed) {
        this((player) -> speed);
    }

    public AbilityFlight(float speed, float hungerMultiplier) {
        this(speed);
        this.hungerMultiplier = hungerMultiplier;
    }

    public AbilityFlight(AbilityParamProducer<Float> speed) {
        this.speed = speed;
    }

    public float getSpeed(Player player) {
        return this.speed.produce(player);
    }

    public float getHungerMultiplier() {
        return this.hungerMultiplier;
    }
}
