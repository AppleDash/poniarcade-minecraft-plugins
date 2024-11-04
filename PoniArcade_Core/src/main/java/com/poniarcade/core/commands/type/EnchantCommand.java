package com.poniarcade.core.commands.type;


import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

@Command(name = "Enchant", description = "Enchant an item.", basePermission = "core.enchant")
public class EnchantCommand extends BaseCommand {

    public EnchantCommand(Plugin owning_plugin) {
        super(owning_plugin);
    }

    @CommandHelp(help = "Show a list of all possible enchantments.")
    @HandlesCommand(subCommand = "list")
    public void handleList(CommandSender sender) {
        boolean shouldYellow = true;

        for (Enchantment enchantment : Enchantment.values()) {
            messageTo(sender).put(shouldYellow ? ChatColor.YELLOW : ChatColor.GOLD, this.getEnchantmentInfo(enchantment)).send();
            shouldYellow = !shouldYellow;
        }
    }

    // TODO: /enchant clear
    @CommandHelp(usage = "<enchantment> <level>", help = "Enchant your currently-held item with the given enchantment to the given level.")
    @HandlesCommand(params = { String.class, int.class })
    public void handleEnchant(Player sender, String enchantName, int level) {
        ItemStack itemStack = sender.getInventory().getItemInMainHand();

        if (Utils.isStackEmpty(itemStack)) {
            messageTo(sender).aqua("You must be holding an item.").send();
            return;
        }

        Enchantment enchantment = Utils.getEnchantment(enchantName);

        if (enchantment == null) {
            messageTo(sender).aqua("There is no enchantment called ").gold(enchantName).aqua(".").send();
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();

        if (meta instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) meta).addStoredEnchant(enchantment, level, true);
        } else {
            meta.addEnchant(enchantment, level, true);
        }

        messageTo(sender).aqua("Enchantment added!").send();
    }

    private String getEnchantmentInfo(Enchantment enchantment) {
        //noinspection deprecation
        StringBuilder stringBuilder = new StringBuilder(enchantment.getName())
        .append("(")
        .append(enchantment.getKey().getKey())
        .append(") Min=")
        .append(enchantment.getStartLevel())
        .append(" Max=")
        .append(enchantment.getMaxLevel());
        if (enchantment.getItemTarget() != null) {
            stringBuilder.append(" Tool=")
            .append(enchantment.getItemTarget());
        }
        return stringBuilder.toString();
    }
}
