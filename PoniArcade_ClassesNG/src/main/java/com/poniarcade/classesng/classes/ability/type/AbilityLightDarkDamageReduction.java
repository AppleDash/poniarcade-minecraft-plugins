package com.poniarcade.classesng.classes.ability.type;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 */
public class AbilityLightDarkDamageReduction extends AbilityDamageReduction {
    public AbilityLightDarkDamageReduction(int lightSpeed, int darkSpeed) {
        super((player) -> ((player.getLocation().getBlock().getLightLevel() <= 7) ? darkSpeed : lightSpeed));
    }
}
