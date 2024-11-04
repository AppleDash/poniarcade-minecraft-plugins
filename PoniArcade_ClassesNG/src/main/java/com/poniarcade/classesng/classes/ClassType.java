package com.poniarcade.classesng.classes;

/**
 * Created by appledash on 7/24/16.
 * Blackjack is still best pony.
 */
public enum ClassType {
    _ANY("Any"), NORMAL("Normal"), MASTER("Master");

    private final String name;

    ClassType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
