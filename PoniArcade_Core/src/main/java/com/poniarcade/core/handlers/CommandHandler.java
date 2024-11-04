package com.poniarcade.core.handlers;

import com.poniarcade.core.commands.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.List;

public final class CommandHandler {
    private static CommandMap commandMap = CommandHandler.getBukkitCommandMap();

	private CommandHandler() {
	}

	public static CommandMap getBukkitCommandMap() {
        if (CommandHandler.commandMap == null) {
            try {
                Field field = Bukkit.getServer().getPluginManager().getClass().getDeclaredField("commandMap");
                field.setAccessible(true);
                CommandHandler.commandMap = (CommandMap) field.get(Bukkit.getServer().getPluginManager());
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Failed to get commandMap from Bukkit PluginManager", e);
            }
        }

        return CommandHandler.commandMap;
    }


    public static void addCommands(Plugin plugin, BaseCommand ...commands) {
        for (BaseCommand command : commands) {
            CommandMap commandMap = CommandHandler.getBukkitCommandMap();
            Command conflictingCommand = commandMap.getCommand(command.getName());
            CommandHandler.removeConflictingCommand(plugin, conflictingCommand);

            for (String alias : command.getAliases()) {
                conflictingCommand = commandMap.getCommand(alias);
                CommandHandler.removeConflictingCommand(plugin, conflictingCommand);
            }

            commandMap.register(plugin.getName().replaceAll("\\s", "").toLowerCase(), command);

            if (!command.getClass().isAnnotationPresent(com.poniarcade.core.commands.Command.class)) {
                plugin.getLogger().warning("Command " + command.getClass().getName() + " is not using the new command handling system. It will stop working soon.");
            }

            plugin.getLogger().info("Registered command: " + command.getClass().getSimpleName());
        }
    }

    private static void removeConflictingCommand(Plugin plugin, Command command) {
        if (command != null && !(command instanceof BaseCommand)) {
            plugin.getLogger().info("Found conflicting command: " + command.getClass() + " (/" + command.getName() + ")");
            List<String> aliases = command.getAliases();
            aliases.remove(command.getName());
            command.setAliases(aliases);
            plugin.getLogger().info("New aliases: " + command.getAliases());
        }
    }
}
