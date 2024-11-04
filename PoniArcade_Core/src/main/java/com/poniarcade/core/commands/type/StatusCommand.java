package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import java.lang.management.ManagementFactory;
import java.util.*;

@Command(name = "status", description = "View status information about the server.", basePermission = "core.status", aliases = {"serverstatus", "stat"})
public class StatusCommand extends BaseCommand {

    public StatusCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand
    @CommandHelp(help = "View status information about the server.")
    public void handleStatus(CommandSender sender) {
        List<World> worlds = Bukkit.getWorlds();
        int totalChunksLoaded = 0;
        int totalEntities = 0;
        int totalMobs = 0;
        for (World world : worlds) {
            totalChunksLoaded += world.getLoadedChunks().length;
            totalEntities += world.getEntities().size();
            totalMobs += world.getLivingEntities().size();
        }

        // TODO: Better formatting.
        sender.sendMessage(ColorHelper.aqua(
                               "%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s",
                               "====================",
                               "Uptime: " + Utils.millisToDHMS(System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime()),
                               "Mem Max: " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB",
                               "Mem Alloc: " + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "MB",
                               "Mem Used: " + ((Runtime.getRuntime().totalMemory() / 1024 / 1024) - (Runtime.getRuntime().freeMemory() / 1024 / 1024)) + "MB",
                               "Mem Free: " + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "MB",
                               "Worlds: " + worlds.size(),
                               "Chunks: " + totalChunksLoaded,
                               "Entities: " + totalEntities,
                               "Mobs: " + totalMobs,
                               "====================").toString());


    }

    @HandlesCommand(subCommand = "entities")
    @CommandHelp(usage = "entities", help = "View status information about entities on the server.")
    public void handleEntityStatus(CommandSender sender) {
        for (World world : Bukkit.getServer().getWorlds()) {
            sender.sendMessage(this.getEntityInfo(world));
        }
    }

    @HandlesCommand(subCommand = "chunks")
    @CommandHelp(usage = "chunks", help = "View status information about chunks on the server.")
    public void handleChunkStatus(CommandSender sender) {
        for (World world : Bukkit.getServer().getWorlds()) {
            sender.sendMessage(this.getChunkInfo(world));
        }
    }

    private String getChunkInfo(World world) {
        return ColorHelper.gold("========== Chunk Info (World: %s) ==========\n", world.getName()).aqua(
            """
                Chunks loaded: %d
                Total entities: %d
                Living entities: %d""",
                   world.getLoadedChunks().length,
                   world.getEntities().size(),
                   world.getLivingEntities().size()

               ).toString();
    }

    private String getEntityInfo(World world) {
        Map<EntityType, Integer> entityCounts = new EnumMap<>(EntityType.class);

        for (Entity entity : world.getEntities()) {
            if (!entityCounts.containsKey(entity.getType())) {
                entityCounts.put(entity.getType(), 1);
            } else {
                entityCounts.put(entity.getType(), entityCounts.get(entity.getType()) + 1);
            }
        }

        entityCounts = StatusCommand.sortByValue(entityCounts);

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Total entities: %d\n", world.getEntities().size()));
        sb.append("Type breakdown:\n");

        entityCounts.forEach((type, count) -> {
            sb.append(String.format("%s: %d\n", type, count));
        });

        return ColorHelper.gold("========== Entity info (World: %s) ==========\n", world.getName()).aqua(sb.toString()).toString();
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();

        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
