package com.poniarcade.core.commands.type;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.handlers.PlayerFlags;
import com.poniarcade.core.structs.PowerTools;
import com.poniarcade.core.utils.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

@Command(name = "powertool", description = "Bind commands to items.", basePermission = "core.powertool", aliases = "pt")
public class PowertoolCommand extends BaseCommand {

    private final PoniArcade_Core plugin;

    public PowertoolCommand(PoniArcade_Core plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand(params = String[].class)
    @CommandHelp(usage = "<command>", help = "Add the given command as a power tool to your currently held item.")
    @CommandHelp(usage = "clear", help = "Clear all power tools from your currently held item.")
    @CommandHelp(usage = "list", help = "List the power tools bound to your currently held item.")
    public void handlePowerTool(Player sender, String[] commandParts) {
        Material material = sender.getInventory().getItemInMainHand().getType();

        if (material == Material.AIR) {
            messageTo(sender).red("You must be holding an item.").send();
            return;
        }

        String command = StringUtil.combine(commandParts, " ");

        PowerTools powerTools = this.plugin.getPlayerFlags().getFlag(sender, PlayerFlags.FlagType.POWER_TOOLS);

        if (command.equalsIgnoreCase("clear")) {
            this.plugin.getPlayerFlags().setFlag(sender, PlayerFlags.FlagType.POWER_TOOLS, powerTools.clearPowerTool(material));
            messageTo(sender).aqua("All commands removed from ").gold(material.toString()).aqua(".").send();
            return;
        } else if (command.equalsIgnoreCase("list")) {
            List<String> commands = powerTools.getPowerTools(material);

            if (commands.isEmpty()) {
                messageTo(sender).aqua("You have no commands bound to your ").gold(material.toString()).aqua(".").send();
            } else {
                messageTo(sender)
                .aqua("You have the following commands bound to your ")
                .gold(material.toString()).aqua(": ")
                .gold(StringUtil.combine(commands.toArray(new String[0]), ChatColor.AQUA + ", " + ChatColor.GOLD))
                .aqua(".")
                .send();
            }

            return;
        } else if (StringUtil.matchAny(command, "pt", "powertool")) {
            messageTo(sender).red("You may not power tool a power tool command!").send();
            return;
        }

        if (powerTools.getPowerTools(material).size() >= 16) {
            messageTo(sender).red("You may have at most 16 commands bound to one item.").send();
            return;
        }

        this.plugin.getPlayerFlags().setFlag(sender, PlayerFlags.FlagType.POWER_TOOLS, powerTools.addPowerTool(material, command));

        messageTo(sender).aqua("I have added the command ").gold(command).aqua(" to your ").gold(material.toString()).aqua(".").send();
    }
}
