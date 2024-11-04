package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

/**
 * Created by appledash on 1/6/17.
 * Blackjack is still best pony.
 */
@Command(name = "ItemInfo", description = "Obtains basic information about the item you are holding.", basePermission = "core.iteminfo")
public class ItemInfoCommand extends BaseCommand {
    public ItemInfoCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand
    @CommandHelp(help = "Obtain basic information about the item you are holding.")
    public void handleItemInfo(Player sender) {
        Optional<ItemStack> heldItem = this.getHeldItem(sender);

        if (heldItem.isEmpty()) {
            sender.sendMessage(ColorHelper.aqua("You are not holding an item.").toString());
            return;
        }

        ItemStack stack = heldItem.get();
        ItemMeta itemMeta = stack.getItemMeta();

        ColorHelper.Builder builder = ColorHelper.aqua("Item Information:").newLine();
        builder.aqua("Material: ").gold(stack.getType().toString()).newLine();
        // builder.aqua("ID: ").gold(String.valueOf(stack.getTypeId())).newLine();
        if (itemMeta instanceof Damageable damageable) {
            builder.aqua("Damage: ").gold("%d", damageable.getDamage()).newLine();
        }
        builder.aqua("Amount: ").gold(String.valueOf(stack.getAmount())).newLine();
        if (itemMeta != null && itemMeta.hasDisplayName()) {
            builder.aqua("Custom Name: ").gold(itemMeta.getDisplayName()).newLine();
        }

        builder.aqua("Enchanted: ").gold("%b", itemMeta != null && itemMeta.hasEnchants()).newLine();
        builder.aqua("Obtained by staff: ").gold(String.valueOf(Utils.hasCustomNBTString(stack, "poniarcade-given")));

        sender.sendMessage(builder.toString());
    }

    private Optional<ItemStack> getHeldItem(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if ((mainHand != null) && (mainHand.getType() != Material.AIR)) {
            return Optional.of(mainHand);
        }

        if ((offHand != null) && (offHand.getType() != Material.AIR)) {
            return Optional.of(offHand);
        }

        return Optional.empty();
    }
}
