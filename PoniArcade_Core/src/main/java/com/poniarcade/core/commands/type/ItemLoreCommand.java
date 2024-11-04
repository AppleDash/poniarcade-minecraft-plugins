package com.poniarcade.core.commands.type;

import com.google.common.collect.ImmutableList;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.StringUtil;
import com.poniarcade.core.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

@Command(name = "itemlore", description = "Modify an item's lore.", basePermission = "core.meta", aliases = { "ilore", "lore" })
public class ItemLoreCommand extends BaseCommand {
    public ItemLoreCommand(Plugin owning_plugin) {
        super(owning_plugin);
    }

    @HandlesCommand(subCommand = "set", params = String[].class)
    @CommandHelp(usage = "<lore>", help = "Set your held item's lore to the given lore.")
    public void handleLore(Player sender, String[] loreParts) {
        ItemStack itemStack = sender.getInventory().getItemInMainHand();

        if (Utils.isStackEmpty(itemStack)) {
            messageTo(sender).red("You must be holding an item!").send();
            return;
        }

        String lore = ChatColor.translateAlternateColorCodes('&', StringUtil.combine(loreParts, " "));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(ImmutableList.of(lore));
        itemStack.setItemMeta(itemMeta);
        messageTo(sender).aqua("Lore set!").send();
    }

    @HandlesCommand(subCommand = "add")
    @CommandHelp(usage = "<lore>", help = "Add the given lore to your held item's lore.")
    public void handleAdd(Player sender, String[] loreParts) {
        ItemStack itemStack = sender.getInventory().getItemInMainHand();

        if (Utils.isStackEmpty(itemStack)) {
            messageTo(sender).red("You must be holding an item!").send();
            return;
        }

        String lore = ChatColor.translateAlternateColorCodes('&', StringUtil.combine(loreParts, " "));

        this.addLore(itemStack, lore);

        messageTo(sender).aqua("Lore added!").send();
    }

    private void addLore(ItemStack stack, String lore) {
        ItemMeta itemMeta = stack.getItemMeta();
        List<String> loreStrings;

        if (!itemMeta.hasLore()) {
            loreStrings = new ArrayList<>();
        } else {
            loreStrings = new ArrayList<>(itemMeta.getLore());
        }

        loreStrings.add(lore);
        itemMeta.setLore(loreStrings);
        stack.setItemMeta(itemMeta);
    }

}
