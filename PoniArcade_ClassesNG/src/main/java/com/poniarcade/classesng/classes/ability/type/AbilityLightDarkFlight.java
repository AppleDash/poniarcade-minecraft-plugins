package com.poniarcade.classesng.classes.ability.type;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class AbilityLightDarkFlight extends AbilityFlight {
    public AbilityLightDarkFlight(float lightSpeed, float darkSpeed) {

        super((player) -> (player.getLocation().getBlock().getLightLevel() <= 7) ? darkSpeed : lightSpeed);
    }

    public AbilityLightDarkFlight(float lightSpeed, float darkSpeed, float hungerMultiplier) {
        this(lightSpeed, darkSpeed);
        this.hungerMultiplier = hungerMultiplier;
    }
}
