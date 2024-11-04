package com.poniarcade.core;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class PoniArcadePlugin extends JavaPlugin {
    protected static final Server server = Bukkit.getServer();

    public static void broadcastMessage(String message) {
        PoniArcadePlugin.server.broadcastMessage(message);
    }

    protected void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
