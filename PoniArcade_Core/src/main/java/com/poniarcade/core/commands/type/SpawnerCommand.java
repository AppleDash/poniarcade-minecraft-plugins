package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

@Command(name = "spawner", description = "Set the properties of a mob spawner.", basePermission = "core.spawner")
public class SpawnerCommand extends BaseCommand {
    public SpawnerCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand(params = String.class)
    public void handleSetType(Player sender, String type) {
        Block target = Utils.getPointedBlock(sender, 5, Utils.getTransparentBlocks(), true);

        if (target == null || target.getType() != Material.SPAWNER || !(target.getState() instanceof CreatureSpawner spawner)) {
            messageTo(sender).red("You are not looking at a mob spawner.").send();
            return;
        }
        EntityType entityType = Utils.getMob(type);

        if (entityType == null) {
            messageTo(sender).red("Unknown entity type ").gold(type).red(".").send();
            return;
        }

        spawner.setSpawnedType(entityType);
        spawner.update();

        messageTo(sender).aqua("Spawner type set to ").gold(entityType.toString()).aqua(".").send();
    }
}
