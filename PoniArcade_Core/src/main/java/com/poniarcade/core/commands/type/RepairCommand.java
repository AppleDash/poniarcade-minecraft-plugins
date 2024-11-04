package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.Plugin;

@Command(name = "repair", description = "Repairs the item held in your hand.", basePermission = "core.repair")
public class RepairCommand extends BaseCommand {

    public RepairCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand(params = Boolean.class)
    @CommandHelp(usage = "true", help = "Repair all items in your inventory.")
    public void handleRepair(Player player, boolean all) {
        if (all) {
            this.repair(player.getInventory().getContents());
            player.sendMessage(ColorHelper.aqua("I have repaired all of your items.").toString());
            return;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (Utils.isStackEmpty(heldItem)) {
            player.sendMessage(ColorHelper.aqua("You are not holding an item!").toString());
            return;
        }

        this.repair(heldItem);

        player.sendMessage(ColorHelper.aqua("I have repaired your ").gold(heldItem.getType().toString()).aqua(".").toString());
    }

    @HandlesCommand
    @CommandHelp(help = "Repair your currently held item.")
    public void handleRepair(Player player) {
        this.handleRepair(player, false);
    }

    private void repair(ItemStack... itemStacks) {
        for (ItemStack stack : itemStacks) {
            if (!Utils.isStackEmpty(stack) && stack.getItemMeta() instanceof Damageable damageable) {
                damageable.setDamage(0);
            }
        }
    }
}
