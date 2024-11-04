package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

@Command(name = "hat", description = "Put the item you are holding on your head.", basePermission = "core.hat")
public class HatCommand extends BaseCommand {

    public HatCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand
    @CommandHelp(help = "Put the item you are holding on your head.")
    public void handleHat(Player sender) {
        if (sender.getInventory().getItemInMainHand().getType() != Material.AIR) {
            ItemStack newHat = new ItemStack(sender.getInventory().getItemInMainHand());
            sender.getInventory().setItemInMainHand(sender.getInventory().getHelmet());
            sender.getInventory().setHelmet(newHat);

            sender.sendMessage(ColorHelper.aqua("Enjoy your new hat!").toString());
        }
    }
}
