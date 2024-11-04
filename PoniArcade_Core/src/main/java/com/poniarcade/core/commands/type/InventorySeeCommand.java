package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

@Command(name = "inventorysee", description = "View another player's inventory", aliases = "invsee", basePermission = "core.invsee")
public class InventorySeeCommand extends BaseCommand {
    public InventorySeeCommand(Plugin owning_plugin) {
        super(owning_plugin);
    }

    @HandlesCommand(params = Player.class)
    @CommandHelp(usage = "<player>", help = "View another player's inventory.")
    public void exec(Player sender, Player target) {
        if (sender == target) {
            messageTo(sender).red("You cannot invsee yourself!").send();
            return;
        }

        sender.openInventory(target.getInventory());
    }
}
