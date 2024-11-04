package com.poniarcade.poniarcade_homes.commands;

import com.google.common.collect.ImmutableMap;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.poniarcade_homes.PoniArcade_Homes;
import org.appledash.saneeconomy.economy.EconomyManager;
import org.appledash.saneeconomy.economy.economable.Economable;
import org.appledash.saneeconomy.economy.transaction.Transaction;
import org.appledash.saneeconomy.economy.transaction.TransactionReason;
import org.appledash.saneeconomy.economy.transaction.TransactionResult;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by appledash on 7/16/17.
 * Blackjack is best pony.
 */
@Command(name = "buyhome", description = "Allows you to buy more home points.", basePermission = "poniarcade.homes.buy")
public class BuyHomeCommand extends BaseCommand {
    // This is a map of number of homes they will have after the buy -> how much the buy will cost them.
    // If they have 3 homes now, and want to buy one more for a total of 4, it would cost 25,000.
    private static final Map<Integer, Double> HOME_COSTS = ImmutableMap.<Integer, Double>builder()
            .put(4, 25_000.0D)
            .put(5, 100_000.0D)
            .put(6, 250_000.0D)
            .put(7, 500_000.0D)
            .put(8, 1_000_000.0D)
            .build();
    private final Set<UUID> pendingConfirmations = new HashSet<>();
    private final PoniArcade_Homes plugin;

    public BuyHomeCommand(PoniArcade_Homes plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @CommandHelp(help = "Buy an additional home point with Bits. Will show you how many homes you will have and how much it will cost before you are charged.")
    @HandlesCommand
    public void handleBuyHome(Player sender) {
        int currentHomeCount = PoniArcade_Homes.getNumberOfAllowedHomes(sender);
        int newHomeCount = currentHomeCount + 1;

        if (!BuyHomeCommand.HOME_COSTS.containsKey(newHomeCount)) {
            ColorHelper.red("You have reached the maximum number of homes (").gold("%d", currentHomeCount).red(") that you may buy!").sendTo(sender);
            return;
        }

        double cost = BuyHomeCommand.HOME_COSTS.get(newHomeCount);

        if (!this.plugin.getSaneEconomy().getEconomyManager().hasBalance(Economable.wrap(sender), BigDecimal.valueOf(cost))) {
            ColorHelper.red("You do not have enough money to buy an additional home! (Required: ").gold(this.plugin.getSaneEconomy().getEconomyManager().getCurrency().formatAmount(BigDecimal.valueOf(cost))).red(").").sendTo(sender);
            return;
        }

        ColorHelper.aqua("Increasing your home count from ").gold("%d", currentHomeCount).aqua(" to ").gold("%d", newHomeCount).aqua(" will cost you ").gold(this.plugin.getSaneEconomy().getEconomyManager().getCurrency().formatAmount(BigDecimal.valueOf(cost))).aqua(".").sendTo(sender);
        ColorHelper.aqua("If you are sure you want to do this, type ").gold("/buyhome confirm").aqua(".").sendTo(sender);
        this.pendingConfirmations.add(sender.getUniqueId());
    }

    @CommandHelp(help = "Confirm your pending purchase of an additional home point.")
    @HandlesCommand(subCommand = "confirm")
    public void handleComfirm(Player sender) {
        if (!this.pendingConfirmations.contains(sender.getUniqueId())) {
            ColorHelper.red("You have no pending home purchase.").sendTo(sender);
            return;
        }

        int currentHomeCount = PoniArcade_Homes.getNumberOfAllowedHomes(sender);
        int newHomeCount = currentHomeCount + 1;
        double cost = BuyHomeCommand.HOME_COSTS.get(newHomeCount);
        EconomyManager ecoMan = this.plugin.getSaneEconomy().getEconomyManager();
        TransactionResult result = ecoMan.transact(new Transaction(ecoMan.getCurrency(), Economable.wrap(sender), Economable.PLUGIN, BigDecimal.valueOf(cost), TransactionReason.PLUGIN_TAKE));

        if (result.getStatus() != TransactionResult.Status.SUCCESS) {
            ColorHelper.red("You do not have enough money for that transaction!").sendTo(sender);
            return;
        }

        this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), "pex user " + sender.getName() + " remove poniarcade.homes.home." + currentHomeCount);
        this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), "pex user " + sender.getName() + " add poniarcade.homes.home." + newHomeCount);

        ColorHelper.aqua("You have increased your maximum home count to ").gold("%d", newHomeCount).aqua(" for ").gold(this.plugin.getSaneEconomy().getEconomyManager().getCurrency().formatAmount(BigDecimal.valueOf(cost))).aqua(".").sendTo(sender);
    }
}
