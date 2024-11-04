package com.poniarcade.core.utils.nms;

import org.bukkit.Bukkit;

/**
 * Created by appledash on 12/30/16.
 * Blackjack is still best pony.
 */
public final class NMSUtil {
	private NMSUtil() {
	}

	public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getName().split("org\\.bukkit\\.craftbukkit\\.")[1].split("\\.CraftServer")[0] + "." + name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
