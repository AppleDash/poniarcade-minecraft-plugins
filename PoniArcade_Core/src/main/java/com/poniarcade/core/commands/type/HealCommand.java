package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Command(name = "Heal", description = "Heal a player", basePermission = "core.heal")
public class HealCommand extends BaseCommand {

    public HealCommand(Plugin owning_plugin) {
        super(owning_plugin);
    }

    @HandlesCommand(params = Player.class)
    @CommandHelp(usage = "<player>", help = "Heal another player.")
    public void healOther(CommandSender sender, Player target) {
        target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        target.setFoodLevel(20);

        if (sender != target) {
            sender.sendMessage(ColorHelper.gold(target.getDisplayName()).aqua(" has been healed.").toString());
            target.sendMessage(ColorHelper.aqua("You have been healed by ").gold(sender.getName()).aqua(".").toString());
        } else {
            target.sendMessage(ColorHelper.aqua("You have been healed.").toString());
        }
    }

    @HandlesCommand
    @CommandHelp(help = "Heal yourself.")
    public void healSelf(Player sender) {
	    this.healOther(sender, sender);
    }
}
