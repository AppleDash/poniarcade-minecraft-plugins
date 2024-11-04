package com.poniarcade.core.commands.type;

import com.google.common.collect.ImmutableSet;
import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.StringUtil;
import com.poniarcade.core.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;

import java.util.Set;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

@Command(name = "setcommand", description = "Set the command in a command block.", basePermission = "poniarcade.core.commandblock")
public class CommandBlockCommand extends BaseCommand {
    private final Set<String> commandWhitelist = ImmutableSet.of(
                "achievement",
                "clear",
                "clone",
                "effect",
                "enchant",
                "entitydata",
                "gamemode",
                "give",
                "kill",
                "particle",
                "playsound",
                "replaceitem",
                "scoreboard",
                "setblock",
                "spawnpoint",
                "spreadplayers",
                "summon",
                "tell",
                "testfor",
                "title",
                "tp",
                "xp"
            );

    public CommandBlockCommand(PoniArcade_Core plugin) {
        super(plugin);
    }

    @HandlesCommand(params = String[].class)
    @CommandHelp(usage = "<command>", help = "Set the command in the command block you are looking at to the given command.")
    public void handleSetCommand(Player sender, String[] commandParts) {
        Block block = sender.getTargetBlock(Utils.getTransparentBlocks(), 8);

        if (block.getType() != Material.COMMAND_BLOCK) {
            messageTo(sender).red("You are not looking at a command block.").send();
            return;
        }

        if (!this.commandWhitelist.contains(commandParts[0].trim().replace("/", "").toLowerCase())) {
            messageTo(sender).red("That command is blacklisted.").send();
            return;
        }

        CommandBlock commandBlock = (CommandBlock) block.getState();

        commandBlock.setCommand(StringUtil.combine(commandParts, " "));
        messageTo(sender).aqua("Command set.").send();
    }
}
