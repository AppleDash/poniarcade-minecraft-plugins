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

@Command(name = "book", description = "Toggles a book's signed state", basePermission = "core.book")
public class BookCommand extends BaseCommand {
    public BookCommand(Plugin owning_plugin) {
        super(owning_plugin);
    }

    @HandlesCommand
    @CommandHelp(help = "Toggle your held book's signed state.")
    public void exec(Player sender) {
        ItemStack hand = sender.getInventory().getItemInMainHand();

        if ((hand.getType() != Material.WRITTEN_BOOK) && (hand.getType() != Material.WRITABLE_BOOK)) {
            sender.sendMessage(ColorHelper.red("You don't seem to be holding a book in your main hand!").toString());
            return;
        }

        hand.setType((hand.getType() == Material.WRITTEN_BOOK) ? Material.WRITABLE_BOOK : Material.WRITTEN_BOOK);

        sender.sendMessage(ColorHelper.aqua("Done!").toString());
    }
}
