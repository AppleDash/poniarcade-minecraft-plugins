package com.poniarcade.classesng.classes.ability.type;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class AbilityLightDarkIncreaseMeleeDamage extends AbilityIncreaseMeleeDamage {
    public AbilityLightDarkIncreaseMeleeDamage(int lightPercent, int darkPercent) {
        super((player) -> (player.getLocation().getBlock().getLightLevel() <= 7) ? darkPercent : lightPercent);
    }
}
