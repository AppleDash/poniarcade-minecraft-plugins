package com.poniarcade.classesng.classes.ability.type;

import com.poniarcade.classesng.classes.ability.Ability;
import com.poniarcade.classesng.classes.util.AbilityParamProducer;
import org.bukkit.entity.Player;

/**
 * Created by appledash on 7/18/16.
 * Blackjack is still best pony.
 */
public class AbilityDamageReduction extends Ability {
    private final AbilityParamProducer<Integer> percentSupplier;

    public AbilityDamageReduction(AbilityParamProducer<Integer> percentSupplier) {
        this.percentSupplier = percentSupplier;
    }

    public AbilityDamageReduction(int percent) {
        this((player) -> percent);
    }

    public int getPercent(Player player) {
        return this.percentSupplier.produce(player);
    }
}
