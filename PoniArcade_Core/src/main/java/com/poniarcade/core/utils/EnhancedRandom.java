package com.poniarcade.core.utils;

import java.util.Random;

/**
 * Created by appledash on 8/12/16.
 * Blackjack is still best pony.
 */
public class EnhancedRandom extends Random {
    public float nextFloat(float min, float max) {
        return (this.nextFloat() * (max - min)) + min;
    }

    public double nextDouble(double min, double max) {
        return (this.nextDouble() * (max - min)) + min;
    }

    public boolean percentChance(int percentChance) {
        float percentFloat = percentChance / 100.0f;
        return this.nextFloat() <= percentFloat;
    }
}
