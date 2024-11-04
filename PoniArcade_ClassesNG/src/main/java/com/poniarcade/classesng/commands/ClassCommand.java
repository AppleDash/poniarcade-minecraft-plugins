package com.poniarcade.classesng.commands;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.StringUtil;
import org.appledash.saneeconomy.economy.economable.Economable;
import org.appledash.saneeconomy.economy.transaction.Transaction;
import org.appledash.saneeconomy.economy.transaction.TransactionReason;
import org.appledash.saneeconomy.economy.transaction.TransactionResult;
import org.bukkit.Effect;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by appledash on 7/18/16.
 * Blackjack is still best pony.
 */
@Command(name = "class", description = "Manage your current class.", basePermission = "poniarcade.classes.class")
public class ClassCommand extends BaseCommand {
    private final PoniArcade_ClassesNG plugin;

    public ClassCommand(PoniArcade_ClassesNG plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @CommandHelp(help = "Display this help message.")
    @HandlesCommand
    public void handleHelp(CommandSender sender) {
        // FIXME: This is bad
        sender.sendMessage(PoniArcade_Core.getInstance().getCommandHelpGenerator().getHelpForCommand(sender, "class"));
        sender.sendMessage(ColorHelper.aqua("When using the ").gold("buy ").aqua("or ").gold("learn ").aqua("commands, do not include spaces in the name.").toString());
    }

    @CommandHelp(help = "Display a list of all possible classes.")
    @HandlesCommand(subCommand = "list")
    public void handleList(CommandSender sender) {
        for (ClassType type : ClassType.values()) {
            if (type != ClassType._ANY) {
                List<String> classNames = this.plugin.getClassManager().getAllClassNames(type);
                sender.sendMessage(ColorHelper.aqua("%s classes: %s", type.getName(), StringUtil.commafy(classNames.toArray())).toString());
            }
        }
    }

    @CommandHelp(help = "Display your current class.")
    @HandlesCommand(subCommand = "status")
    public void handleStatus(Player player) {
        Optional<Class> classOptional = this.plugin.getClassManager().getRealClassForPlayer(player);

        if (classOptional.isEmpty()) {
            player.sendMessage(ColorHelper.aqua("You do not currently have a class.").toString());
        } else {
            Optional<Class> spoofed = this.plugin.getClassManager().getEffectiveClassForPlayer(player);
            if (spoofed.isPresent() && (spoofed.get() != classOptional.get())) {
                player.sendMessage(ColorHelper.aqua("Your current class is ").gold(classOptional.get().getFriendlyName()).aqua(", randomly chosen to be a ").append(spoofed.get().getInflectedColoredName()).aqua(" for the next ").gold("%d", (this.plugin.getClassManager().getClassData(player).getSpoofedClassExpiry() - System.currentTimeMillis()) / 1000 / 60).aqua(" minutes.").toString());
            } else {
                player.sendMessage(ColorHelper.aqua("Your current class is ").gold(classOptional.get().getFriendlyName()).aqua(".").toString());
            }
        }
    }

    @CommandHelp(usage = "<player>", help = "Display a player's current class.")
    @HandlesCommand(subCommand = "status", params = Player.class, permission = "poniarcade.classes.class.status.other")
    public void handleStatusOther(Player sender, Player target) {
        Optional<Class> classOptional = this.plugin.getClassManager().getRealClassForPlayer(target);

        if (classOptional.isEmpty()) {
            sender.sendMessage(ColorHelper.gold(target.getDisplayName()).aqua(" does not currently have a class.").toString());
        } else {
            Optional<Class> spoofed = this.plugin.getClassManager().getEffectiveClassForPlayer(target);
            if (spoofed.isPresent() && (spoofed.get() != classOptional.get())) {
                sender.sendMessage(ColorHelper.gold(target.getDisplayName()).aqua("'s current class is ").gold(classOptional.get().getFriendlyName()).aqua(", randomly chosen to be a ").append(spoofed.get().getInflectedColoredName()).aqua(" for the next ").gold("%d", (this.plugin.getClassManager().getClassData(target).getSpoofedClassExpiry() - System.currentTimeMillis()) / 1000 / 60).aqua(" minutes.").toString());
            } else {
                sender.sendMessage(ColorHelper.gold(target.getDisplayName()).aqua("'s current class is ").gold(classOptional.get().getFriendlyName()).aqua(".").toString());
            }
        }
    }

    @CommandHelp(usage = "<class>", help = "Display information about the given class. Do not use spaces in the class name.")
    @HandlesCommand(subCommand = "info", params = String.class)
    public void handleInfo(CommandSender sender, String className) {
        Optional<Class> classOptional = this.plugin.getClassManager().getClassByName(className);
        if (classOptional.isEmpty()) {
            sender.sendMessage(ColorHelper.aqua("There is no class called ").gold(className).aqua(". Did you use spaces in the name by mistake?").toString());
        } else {
            Class theClass = classOptional.get();
            double price = theClass.getPrice();
            sender.sendMessage(ColorHelper.aqua("Name: ").gold(theClass.getFriendlyName()).toString());
            sender.sendMessage(ColorHelper.aqua("Description: ").gold(theClass.getDescription()).toString());
            sender.sendMessage(ColorHelper.aqua("Abilities: ").gold(StringUtil.commafy(theClass.getAbilities().stream().map(ability -> ability.getClass().getSimpleName().replace("Ability", "")).collect(Collectors.toList()))).toString());
            theClass.getSaddlePower().ifPresent(saddlePower -> {
                sender.sendMessage(ColorHelper.aqua("Saddle Power: ").gold(saddlePower.getName()).aqua(" - ").gold(saddlePower.getDescription()).toString());
            });
            sender.sendMessage(ColorHelper.aqua("Price: ").gold((price == 0) ? "Free!" : this.plugin.getSaneEconomyManager().getCurrency().formatAmount(BigDecimal.valueOf(price))).toString());
            theClass.getExtraDescription().ifPresent(description -> {
                sender.sendMessage(ColorHelper.aqua("Extra: ").gold(description).toString());
            });
        }
    }

    @CommandHelp(help = "Remove your current class.")
    @HandlesCommand(subCommand = "none")
    public void handleNone(Player sender) {
        Optional<Class> classOptional = this.plugin.getClassManager().getRealClassForPlayer(sender);

        if (classOptional.isEmpty()) {
            sender.sendMessage(ColorHelper.aqua("You do not currently have a class.").toString());
            return;
        }

        this.plugin.getClassManager().removeClassForPlayer(sender);
        sender.sendMessage(ColorHelper.aqua("Your class has been removed.").toString());
    }

    @CommandHelp(usage = "<class name>", help = "Change your class to the given class. Do not use spaces in the class name.")
    @HandlesCommand(subCommand = "learn", params = String.class)
    public void handleLearn(Player player, String className) {
        this.doLearn(player, player, className, false);
    }

    @CommandHelp(usage = "<class name>", help = "Buy the given master class with bits. This will confirm before buying. Do not use spaces in the class name.")
    @HandlesCommand(subCommand = "buy", params = String.class)
    public void handleBuy(Player player, String className) {
        Optional<Class> classOptional = this.plugin.getClassManager().getClassByName(className);

        if (classOptional.isEmpty()) {
            player.sendMessage(ColorHelper.aqua("There is no class called ").gold(className).aqua(". Did you use spaces in the name by mistake?").toString());
            return;
        }

        Class newClass = classOptional.get();

        double price = newClass.getPrice();

        if (price == 0.0D) {
            player.sendMessage(ColorHelper.aqua("Your cannot buy that class. Instead, you can learn it for free with ").gold("/class learn %s", newClass.getSanitizedName()).aqua(".").toString());
            return;
        }

        Optional<Class> currentClassOptional = this.plugin.getClassManager().getRealClassForPlayer(player);

        if (currentClassOptional.isPresent() && (currentClassOptional.get() == newClass)) {
            player.sendMessage(ColorHelper.aqua("You are already ").append(newClass.getInflectedColoredName()).aqua("!").toString());
            return;
        }

        if (this.plugin.getClassManager().getClassData(player).getPurchasedClasses().contains(newClass)) {
            this.plugin.getClassManager().setClassForPlayer(player, newClass);
            player.sendMessage(ColorHelper.aqua("Poof! You are now %s (Already previously purchased!)", newClass.getInflectedColoredName()).toString());
            return;
        }

        if (!this.plugin.getSaneEconomyManager().hasBalance(Economable.wrap(player), BigDecimal.valueOf(price))) {
            player.sendMessage(ColorHelper.aqua("You can't afford to buy that class. It costs %s, and you only have %s.", this.plugin.getSaneEconomyManager().getCurrency().formatAmount(BigDecimal.valueOf(price)), this.plugin.getSaneEconomyManager().getFormattedBalance(Economable.wrap(player))).toString());
            return;
        }


        this.plugin.getClassManager().addPendingClassPurchase(player, newClass);

        player.sendMessage(ColorHelper.aqua("Are you sure you want to buy the class ").gold(classOptional.get().getFriendlyName()).aqua(" for ").gold(this.plugin.getSaneEconomyManager().getCurrency().formatAmount(BigDecimal.valueOf(price))).aqua("? Type /class confirm to accept, or /class decline to decline.").toString());
    }

    @CommandHelp(usage = "<class name>", help = "Force your class to be the given class, ignoring any prices and cooldowns.")
    @HandlesCommand(subCommand = "force", params = String.class, permission = "poniarcade.classes.class.force")
    public void handleForce(Player sender, String className) {
        this.doLearn(sender, sender, className, true);
    }

    @CommandHelp(usage = "<player> <class name>", help = "Force a player's class to be the given class, ignoring any prices and cooldowns.")
    @HandlesCommand(subCommand = "force", params = {Player.class, String.class}, permission = "poniarcade.classes.class.force")
    public void handleForce(Player sender, Player target, String className) {
        this.doLearn(sender, target, className, true);
    }

    @CommandHelp(help = "Confirm your pending master class purchase.")
    @HandlesCommand(subCommand = "confirm")
    public void handleConfirm(Player player) {
        if (this.plugin.getClassManager().getPendingClassPurchase(player).isEmpty()) {
            player.sendMessage(ColorHelper.aqua("You do not have a pending class purchase! Perhaps you waited too long?").toString());
            return;
        }

        Optional<Class> classOptional = this.plugin.getClassManager().getPendingClassPurchase(player);

        if (classOptional.isEmpty()) {
            throw new IllegalStateException("Invalid class in pendingBuyConfirmations!");
        }

        Class theClass = classOptional.get();
        if (!this.plugin.getSaneEconomyManager().hasBalance(Economable.wrap(player), BigDecimal.valueOf(theClass.getPrice()))) {
            player.sendMessage(ColorHelper.aqua("You can't afford to buy that class!").toString());
            return;
        }

        TransactionResult transactionResult = this.plugin.getSaneEconomyManager().transact(new Transaction(
            this.plugin.getSaneEconomyManager().getCurrency(), Economable.wrap(player), Economable.PLUGIN, BigDecimal.valueOf(theClass.getPrice()), TransactionReason.PLUGIN_TAKE
        ));

        if (transactionResult.getStatus() != TransactionResult.Status.SUCCESS) {
            player.sendMessage(ColorHelper.red("An error occured with that transaction: %s. Please contact an admin.", transactionResult.getStatus().toString()).toString());
            return;
        }

        this.plugin.getClassManager().setClassForPlayer(player, theClass);
        this.plugin.getClassManager().getClassData(player).addPurchasedClass(theClass);
        player.sendMessage(ColorHelper.aqua("Poof! You have bought the class ").gold(theClass.getFriendlyName()).aqua(" for ").gold(this.plugin.getSaneEconomyManager().getCurrency().formatAmount(BigDecimal.valueOf(theClass.getPrice()))).toString());
    }

    @CommandHelp(help = "Decline your pending master class purchase.")
    @HandlesCommand(subCommand = "decline")
    public void handleDecline(Player player) {
        if (this.plugin.getClassManager().getPendingClassPurchase(player).isEmpty()) {
            player.sendMessage(ColorHelper.aqua("You do not have a pending class purchase! Perhaps you waited too long?").toString());
            return;
        }

        this.plugin.getClassManager().removePendingClassPurchase(player);
        player.sendMessage(ColorHelper.aqua("Class purchase successfully declined.").toString());
    }

    private void doLearn(Player sender, Player player, String className, boolean force) {
        Optional<Class> classOptional = this.plugin.getClassManager().getClassByName(className);

        if (classOptional.isEmpty()) {
            player.sendMessage(ColorHelper.aqua("There is no class called '%s'.", className).toString());
            return;
        }

        Optional<Class> currentClassOptional = this.plugin.getClassManager().getRealClassForPlayer(player);

        if (currentClassOptional.isPresent() && (currentClassOptional.get() == classOptional.get())) {
            player.sendMessage(ColorHelper.aqua("You are already ").append(currentClassOptional.get().getInflectedColoredName()).aqua("!").toString());
            return;
        }

        if (!force && (classOptional.get().getPrice() != 0.0D)) {
            if (this.plugin.getClassManager().getClassData(player).getPurchasedClasses().contains(classOptional.get())) {
                this.plugin.getClassManager().setClassForPlayer(player, classOptional.get());
                player.sendMessage(ColorHelper.aqua("Poof! You are now %s (Already previously purchased!)", classOptional.get().getInflectedColoredName()).toString());
                return;
            }

            player.sendMessage(ColorHelper.aqua("Your cannot learn that class for free. Instead, you must buy it with ").gold("/class buy %s", classOptional.get().getSanitizedName()).aqua(".").toString());
            return;
        }

        long remainingCooldown = this.plugin.getClassManager().getRemainingCooldown(player) / 1000;

        if (!force && (remainingCooldown != 0)) {
            player.sendMessage(ColorHelper.aqua("You cannot learn a new class right now - you must wait ").gold("%s", String.format("%d:%02d:%02d", remainingCooldown / 3600, (remainingCooldown % 3600) / 60, (remainingCooldown % 60))).aqua(".").toString());
            return;
        }

        player.playEffect(player.getLocation(), Effect.SMOKE, BlockFace.UP);
        this.plugin.getClassManager().setClassForPlayer(player, classOptional.get());
        if (!force) {
            this.plugin.getClassManager().setNextClassChange(player, (System.currentTimeMillis()) + (3 * 24 * 60 * 60 * 1000)); // 3 days
        }

        if (force && (sender != player)) {
            sender.sendMessage(ColorHelper.gold(player.getDisplayName()).aqua(" is now ").append(classOptional.get().getInflectedColoredName()).aqua(".").toString());
        }

        player.sendMessage(ColorHelper.aqua("Poof! You are now ").append(classOptional.get().getInflectedColoredName()).aqua("!").toString());
    }
}
