package com.poniarcade.classesng.classes.ability;

/**
 * Created by appledash on 7/18/16.
 * Blackjack is still best pony.
 */
public abstract class Ability {
    @SuppressWarnings("unchecked")
    public java.lang.Class<? extends Ability> getRootAbilityClass() {
        java.lang.Class<? extends Ability> clazz = this.getClass();

        while (clazz.getSuperclass() != Ability.class) {
            clazz = (java.lang.Class<? extends Ability>) clazz.getSuperclass();
        }

        return clazz;
    }
}
