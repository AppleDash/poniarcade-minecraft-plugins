package com.poniarcade.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.Serializable;
import java.util.UUID;

public class LocationWrapper implements Serializable {
    private UUID world_id;
    private String world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public LocationWrapper(Location location) {
        this.world_id = location.getWorld().getUID();
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public LocationWrapper(UUID worldId, double x, double y, double z) {
        this(worldId, x, y, z, 0, 0);
    }

    public LocationWrapper(UUID worldId, double x, double y, double z, float yaw, float pitch) {
        this.world_id = worldId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.getLocation();
    }

    public Location getLocation() {
        if ((this.world == null) && (this.world_id == null)) {
            return null;
        }

        if (this.world == null) {
	        this.world = Bukkit.getWorld(this.world_id).getName();
        }

        if (this.world_id == null) {
	        this.world_id = Bukkit.getWorld(this.world).getUID();
        }

        return new Location(Bukkit.getWorld(this.world_id), this.x, this.y, this.z, this.yaw, this.pitch);
    }

    @Override
    public String toString() {
        return "[name=\"" + this.world + "\", uuid=\"" + this.world_id + "\", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", yaw=" + this.yaw + ", pitch=" + this.pitch + "]";
    }
}
