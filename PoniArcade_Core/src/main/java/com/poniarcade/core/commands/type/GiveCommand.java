package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

@Command(name = "give", description = "Give yourself or another player some items.", basePermission = "poniarcade.core.give", aliases = {"item", "i"})
public class GiveCommand extends BaseCommand {

    public GiveCommand(Plugin plugin) {
        super(plugin);
    }

    // /give golden_apple
    @CommandHelp(usage = "<item>", help = "Give yourself one of the given item.")
    @HandlesCommand(params = String.class)
    public void handleGive(Player sender, String itemName) {
        this.handleGive(sender, sender, itemName, 1); // Give them 1 by default
    }

    // /give golden_apple 64
    @CommandHelp(usage = "<item> <amount>", help = "Give yourself the given amount of the given item.")
    @HandlesCommand(params = {String.class, Integer.class})
    public void handleGive(Player sender, String itemName, int amount) {
        this.handleGive(sender, sender, itemName, amount);
    }

    // /give Blackjack golden_apple 64
    @CommandHelp(usage = "<player> <item> <amount>", help = "Give a player the given amount of the given item.")
    @HandlesCommand(params = {Player.class, String.class, Integer.class})
    public void handleGive(CommandSender sender, Player target, String itemName, int amount) {
        ItemStack stack;

        try {
            stack = this.parseGive(itemName);
        } catch (InvalidItemException e) {
            sender.sendMessage(ColorHelper.aqua("%s", e.getMessage()).toString());
            return;
        }

        // stack = Utils.addCustomNBTString(stack, "poniarcade-given", "command");

        stack.setAmount(amount);
        target.getInventory().addItem(stack);

        sender.sendMessage(ColorHelper.aqua("Giving %s ", target.getDisplayName()).aqua("some ").gold("%s", stack.getType().name()).aqua("...").toString());

        if (sender != target) {
            String senderName = (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();
            target.sendMessage(ColorHelper.aqua("%s ", senderName).aqua("has given you some ").gold("%s", stack.getType().name()).aqua(".").toString());
        }
    }

    private ItemStack parseGive(String rawItemName) throws InvalidItemException {
        String itemName;
        short damage;

        if (rawItemName.contains(":")) {
            String[] splitItemName = rawItemName.split(":");
            itemName = splitItemName[0];
            if (splitItemName.length == 1) { // They just typed 'tnt:'
                damage = 0;
            } else { // They typed 'tnt:something'
                try {
                    damage = Short.parseShort(splitItemName[1]);
                } catch (NumberFormatException e) {
                    throw new InvalidItemException("Damage value must be a number.");
                }
            }
        } else { // No damage value
            itemName = rawItemName;
            damage = 0;
        }

        Optional<Material> materialOptional = this.parseMaterialFromName(itemName);


        if (materialOptional.isEmpty()) {
            // TODO: Use a new item database that actually gives us the names and not just the IDs?
            // Optional<Pair<Integer, Short>> parsedItem = ItemDatabase.getIDAndDamageForName(normalizeItemName(itemName));
            // if (!parsedItem.isPresent()) {
            throw new InvalidItemException("Item by that name does not exist.");
            // }
            // return new ItemStack(parsedItem.get().getLeft(), 1, parsedItem.get().getRight());
        }

        ItemStack stack = new ItemStack(materialOptional.get(), 1);
        if (stack.getItemMeta() instanceof Damageable damageable) {
            damageable.setDamage(damage);
            stack.setItemMeta(damageable);
        }

        return stack;
    }

    // find a material whose name when normalized matches materialName when normalized
    private Optional<Material> parseMaterialFromName(String materialName) {
        for (Material mat : Material.values()) {
            if (this.normalizeItemName(mat.name()).equals(this.normalizeItemName(materialName))) {
                return Optional.of(mat);
            }
        }

        return Optional.empty();
    }

    private String normalizeItemName(String itemName) {
        return itemName.toLowerCase().replace("_", "").replace(" ", "");
    }

    private static class InvalidItemException extends Exception {
        public InvalidItemException(String message) {
            super(message);
        }
    }
}
