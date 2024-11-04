package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Command(name = "spawnmob", description = "Spawn some mobs.", basePermission = "core.spawnmob", aliases = {"mob", "mobspawn"})
public class MobCommand extends BaseCommand {
    public MobCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand(params = String.class)
    @CommandHelp(usage = "<mob name>", help = "Spawn one of the given mob at your location.")
    public void handleSpawnOne(Player sender, String mobName) {
	    this.handleSpawnAmount(sender, mobName, 1);
    }

    @HandlesCommand(params = {String.class, Double.class, Double.class, Double.class})
    @CommandHelp(usage = "<mob name> <x> <y> <z>", help = "Spawn one of the givem mob at the given coordinates.")
    public void handleSpawnOneLocation(Player sender, String mobName, double posX, double posY, double posZ) {
	    this.handleSpawnLocation(sender, mobName, posX, posY, posZ, 1);
    }

    @HandlesCommand(params = {String.class, Double.class, Double.class, Double.class, Integer.class})
    @CommandHelp(usage = "<mob name> <x> <y> <z> <amount>", help = "Spawn the given amount of the given mob at the given coordinates.")
    public void handleSpawnLocation(Player sender, String mobName, double posX, double posY, double posZ, int amount) {
        EntityType entityType = Utils.getMob(mobName);

        if (entityType == null) {
            sender.sendMessage(ColorHelper.red("'%s' is an invalid entity type.", mobName).toString());
            return;
        }

        if (amount > 128) {
            sender.sendMessage(ColorHelper.red("You may not spawn more than 128 total entities.").toString());
            return;
        }

        Location location = new Location(sender.getWorld(), posX, posY, posZ);

        for (int i = 0; i < amount; i++) {
            sender.getWorld().spawnEntity(location, entityType);
        }

        sender.sendMessage(ColorHelper.aqua("%d %s(s) spawned.", amount, entityType.name()).toString());
    }

    @HandlesCommand(params = {String.class, Integer.class})
    @CommandHelp(usage = "<mob name> <amount>", help = "Spawn the given amount of the given mob at your location.")
    public void handleSpawnAmount(Player sender, String mobName, int amount) {
        EntityType entityType = Utils.getMob(mobName);

        if (entityType == null) {
            sender.sendMessage(ColorHelper.red("'%s' is an invalid entity type.", mobName).toString());
            return;
        }

        if (amount > 128) {
            sender.sendMessage(ColorHelper.red("You may not spawn more than 128 total entities.").toString());
            return;
        }

        Location location = sender.getTargetBlock(Utils.getTransparentBlocks(), 100).getLocation();

        for (int i = 0; i < amount; i++) {
            sender.getWorld().spawnEntity(location, entityType);
        }

        sender.sendMessage(ColorHelper.aqua("%d %s(s) spawned.", amount, entityType.name()).toString());
    }
}
