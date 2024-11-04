package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Command(name = "clearinventory", description = "Clear your inventory.", basePermission = "core.clearinventory", aliases = {"ci", "clearinv"})
public class ClearInventoryCommand extends BaseCommand {

    public ClearInventoryCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand
    @CommandHelp(help = "Clear your inventory.")
    public void handleClearInventory(Player sender) {
        sender.getInventory().clear();
        sender.sendMessage(ColorHelper.aqua("Inventory cleared.").toString());
    }
}
