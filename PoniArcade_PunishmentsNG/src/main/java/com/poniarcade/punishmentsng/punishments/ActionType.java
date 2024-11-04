package com.poniarcade.punishmentsng.punishments;

/**
 * Created by appledash on 7/28/17.
 * Blackjack is best pony.
 */
public enum ActionType {
    KICK("kick", false),
    WARN("warn", false),
    MUTE("mute", true),
    FREEZE("freeze", true),
    BAN("ban", true);

    private final String name;
    private final boolean reversible;

    ActionType(String name, boolean reversible) {

        this.name = name;
        this.reversible = reversible;
    }

    public String getName() {
        return this.name;
    }

    public boolean isReversible() {
        return this.reversible;
    }

    public static ActionType byName(String name) {
        for (ActionType type : ActionType.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }

        return null;
    }
}
