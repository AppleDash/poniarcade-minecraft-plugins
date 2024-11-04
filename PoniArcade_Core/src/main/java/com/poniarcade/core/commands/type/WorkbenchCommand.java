package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Command(name = "workbench", description = "Open a workbench interface.", basePermission = "core.craft", aliases = {"ctable", "craft", "wtable", "worktable", "bench", "craftingbench", "wb", "cb", "ct", "crafttable", "crafterino", "wt", "crafter", "craftingengine", "autocrafer", "DoriTheCrafter", "TimTheCrafter", "[BuildCraft-Crafting]"})
public class WorkbenchCommand extends BaseCommand {

    public WorkbenchCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand
    @CommandHelp(help = "Open a workbench/crafting interface.")
    public void handleWorkbench(Player sender) {
        sender.openWorkbench(sender.getLocation(), true);
    }
}
