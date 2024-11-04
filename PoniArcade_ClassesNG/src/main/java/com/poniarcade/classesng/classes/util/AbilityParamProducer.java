package com.poniarcade.classesng.classes.util;

import org.bukkit.entity.Player;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
@FunctionalInterface
public interface AbilityParamProducer<T> {
    T produce(Player player);
}
