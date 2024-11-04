package com.poniarcade.classesng.classes.ability.type;

import com.poniarcade.classesng.classes.ability.Ability;
import com.poniarcade.classesng.classes.util.AbilityParamProducer;
import org.bukkit.entity.Player;

/**
 * Created by appledash on 7/18/16.
 * Blackjack is still best pony.
 */
public class AbilityIncreaseMeleeDamage extends Ability {
    private final AbilityParamProducer<Integer> percentProducer;

    public AbilityIncreaseMeleeDamage(int percent) {
        this((player) -> percent);
    }

    public AbilityIncreaseMeleeDamage(AbilityParamProducer<Integer> percentProducer) {
        this.percentProducer = percentProducer;
    }

    public int getPercent(Player player) {
        return this.percentProducer.produce(player);
    }
}
