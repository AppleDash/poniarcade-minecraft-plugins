package com.poniarcade.core.handlers;

import com.poniarcade.core.structs.PowerTools;
import com.poniarcade.core.structs.TeleportRequest;
import com.poniarcade.core.utils.LocationWrapper;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Created by appledash on 12/16/16.
 * Blackjack is best pony.
 *
 * Represents a bunch of data, or 'flags', attached to a player.
 * Examples are current gamemode, godmode status, etc.
 *
 * TODO: Persist these to the database.
 */
public class PlayerFlags {
    private final Map<UUID, Map<FlagType<?>, Object>> playerFlagCache = new ConcurrentHashMap<>();
    private final Map<UUID, Map<FlagType<?>, Map<?, ?>>> playerMapFlagCache = new ConcurrentHashMap<>();

    /**
     * Get the current state of a flag on a player.
     * @param player Player
     * @param type FlagType to get the state of
     * @return Flag state or default if not set.
     */
    public <T> T getFlag(Player player, FlagType<T> type) {
        if (!this.playerFlagCache.containsKey(player.getUniqueId())) {
            return type.getDefaultValue(player);
        }

        Map<FlagType<?>, Object> playerFlags = this.playerFlagCache.get(player.getUniqueId());

        if (!playerFlags.containsKey(type)) {
            return type.getDefaultValue(player);
        }

        //noinspection unchecked
        return (T) playerFlags.get(type);
    }

    /**
     * Set the state of a flag on a player.
     * @param player The player to set the flag on
     * @param type FlagType to set the state of
     * @param value New flag state
     * @return Old value
     */
    public <T> T setFlag(Player player, FlagType<T> type, T value) {
        Map<FlagType<?>, Object> playerFlags;

        if (this.playerFlagCache.containsKey(player.getUniqueId())) {
            playerFlags = this.playerFlagCache.get(player.getUniqueId());
        } else {
            playerFlags = new ConcurrentHashMap<>();
            this.playerFlagCache.put(player.getUniqueId(), playerFlags);
        }

        //noinspection unchecked
        return (T) playerFlags.put(type, value);
    }

    /**
     * Toggle a flag on a player, if that FlagType supports toggling.
     * @param player Player to toggle the flag on
     * @param type FlagType to toggle
     * @return New flag value
     * @throws IllegalStateException if you try to toggle a FlagType that isn't toggleable
     */
    public <T extends Serializable> T toggleFlag(Player player, FlagType<T> type) {
        T oldValue = this.getFlag(player, type);
        T newValue = type.toggle(oldValue);
        this.setFlag(player, type, newValue);
        return newValue;
    }

    public <T> void clearFlag(Player player, FlagType<T> type) {
        if (!this.playerFlagCache.containsKey(player.getUniqueId())) {
            return;
        }

        this.playerFlagCache.get(player.getUniqueId()).remove(type);
    }

    public static class FlagType<T> {
        // Whether the player is invincible or not.
        public static final FlagType<Boolean> GOD_MODE = new FlagType<>(Boolean.class, false);
        // What game mode the player is in
        public static final FlagType<GameMode> GAME_MODE = new FlagType<GameMode>(GameMode.class, player -> player.getServer().getDefaultGameMode());
        // Whether the player has cinematic flight enabled or not
        public static final FlagType<Boolean> FLIGHT_CINEMATIC = new FlagType<>(Boolean.class, false);
        // The player's last teleport location. Serialized to and from a LocationWrapper.
        public static final FlagType<Location> BACK_LOCATION = new FlagTypeSerialized<>(Location.class, null,
            (location) -> (location == null) ? null : new LocationWrapper(location),
            (wrapper) -> (wrapper == null) ? null : wrapper.getLocation());
        // Whether the player has CommandSpy on.
        public static final FlagType<Boolean> COMMAND_SPY = new FlagType<>(Boolean.class, false);
        // The last incoming teleport request sent to this player.
        public static final FlagType<TeleportRequest> INCOMING_TELEPORT_REQUEST = new FlagType<>(TeleportRequest.class, (TeleportRequest) null);
        // The current power tools that a player has.
        public static final FlagType<PowerTools> POWER_TOOLS = new FlagType<>(PowerTools.class, PowerTools.EMPTY);

        private final Class<T> clazz;
        private final Function<Player, T> defaultValue;

        private FlagType(Class<T> clazz, T defaultValue) {
            this.clazz = clazz;
            this.defaultValue = (p) -> defaultValue;
        }

        private FlagType(Class<T> clazz, Function<Player, T> defaultValue) {
            this.clazz = clazz;
            this.defaultValue = defaultValue;
        }

        public T getDefaultValue(Player player) {
            return this.defaultValue.apply(player);
        }

        public boolean supportsToggling() {
            return this.clazz == Boolean.class;
        }

        @SuppressWarnings("all")
        public T toggle(T oldValue) {
            if (!supportsToggling()) {
                throw new IllegalStateException("Cannot toggle a FlagType that doesn't support toggling!");
            }

            if (oldValue instanceof Boolean) {
                return (T)(Boolean)(!(Boolean)oldValue); // I promise this is safe.
            }

            throw new IllegalStateException("Don't know how to toggle this type of flag!");
        }
    }

    private static final class FlagTypeSerialized<T, S extends Serializable> extends FlagType<T> {
        private final FlagSerializer<T, S> serializer;
        private final FlagDeserializer<S, T> deserializer;

        private FlagTypeSerialized(Class<T> clazz, T defaultValue, FlagSerializer<T, S> serializer, FlagDeserializer<S, T> deserializer) {
            super(clazz, defaultValue);
            this.serializer = serializer;
            this.deserializer = deserializer;
        }

        public FlagSerializer<T, S> getSerializer() {
            return this.serializer;
        }

        public FlagDeserializer<S, T> getDeserializer() {
            return this.deserializer;
        }
    }

    @FunctionalInterface
    private interface FlagSerializer<I, O extends Serializable> {
        O serialize(I input);
    }

    @FunctionalInterface
    private interface FlagDeserializer<I extends Serializable, O> {
        O deserialize(I input);
    }
}
