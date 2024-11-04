package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Command(name = "enderchest", description = "Open your or another player's ender chest.", basePermission = "core.enderchest", aliases = "echest")
public class EnderChestCommand extends BaseCommand {

    public EnderChestCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand
    @CommandHelp(help = "Open your own ender chest.")
    public void handleOpenSelf(Player sender) {
        sender.openInventory(sender.getEnderChest());
    }

    @HandlesCommand(params = Player.class, permission = "core.enderchest.other")
    @CommandHelp(usage = "<player>", help = "Open the given player's ender chest.")
    public void handleOpenOther(Player sender, Player target) {
        sender.openInventory(target.getEnderChest());
    }
}
