package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

@Command(name = "more", description = "Set the stack size of your held item to 64.", basePermission = "core.more")
public class MoreCommand extends BaseCommand {

    public MoreCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand
    @CommandHelp(help = "Set the stack size of your held item to 64.")
    public void handleMore(Player sender) {
        PlayerInventory inv = sender.getInventory();

        if ((inv.getItemInMainHand() == null) || (inv.getItemInMainHand().getType() == Material.AIR)) {
            sender.sendMessage(ColorHelper.red("You are not holding any items.").toString());
            return;
        }

        inv.getItemInMainHand().setAmount(64);
    }
}
