package com.poniarcade.warps;

import com.poniarcade.core.utils.LocationWrapper;
import org.bukkit.Location;

/**
 * Created by appledash on 5/16/16.
 * Blackjack is still best pony.
 */
public class TeleportLocation {
    private final short id;
    private final Type type;
    private final String name;
    private final LocationWrapper location;

    public TeleportLocation(Type type, String name, LocationWrapper location) {
        this((short) - 1, type, name, location);
    }

    public TeleportLocation(short id, Type type, String name, LocationWrapper location) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.location = location;
    }

    public short getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Location getLocation() {
        return this.location.getLocation();
    }

    public Type getType() {
        return this.type;
    }

    public enum Type {
        WARP("warp", "warps"), SPAWN("spawn", "spawns");

        private final String singular;
        private final String plural;

        Type(String singular, String plural) {
            this.singular = singular;
            this.plural = plural;
        }

        public String getSingular() {
            return this.singular;
        }

        public String getPlural() {
            return this.plural;
        }
    }
}
