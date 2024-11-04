package com.poniarcade.poniarcade_homes.commands;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.poniarcade_homes.Home;
import com.poniarcade.poniarcade_homes.PoniArcade_Homes;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.util.List;
import java.util.Optional;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

/**
 * Created by appledash on 7/15/17.
 * Blackjack is best pony.
 */
@Command(name = "home", description = "Teleport to one of your homes.", basePermission = "poniarcade.homes.home")
public class HomeCommand extends BaseCommand {
    private final PoniArcade_Homes plugin;

    public HomeCommand(PoniArcade_Homes plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @CommandHelp(help = "List your homes.")
    @HandlesCommand
    public void handleHome(Player sender) {
        this.sendHomeList(sender, sender);
    }

    @CommandHelp(usage = "<home>", help = "Teleport to your home point with the given name.")
    @HandlesCommand(params = String.class)
    public void handleHome(Player sender, String homeName) {
        Optional<Home> homeOptional = this.plugin.getHomeDatabase().getHome(sender, homeName);
        if (homeOptional.isEmpty()) {
            ColorHelper.red("That home does not exist!").sendTo(sender);
            return;
        }

        Home home = homeOptional.get();

        sender.teleport(home.location());
        ColorHelper.aqua("Sending you to home ").gold(home.name()).aqua("...").sendTo(sender);
    }

    @CommandHelp(usage = "<player> <home>", help = "Teleport to a player's home point with the given name.")
    @HandlesCommand(params = { OfflinePlayer.class, String.class }, permission = "poniarcade.homes.home.other")
    public void handleHome(Player sender, OfflinePlayer target, String name) {
        if (name.equalsIgnoreCase("list")) {
            this.sendHomeList(sender, target);
            return;
        }

        Optional<Home> homeOptional = this.plugin.getHomeDatabase().getHome(target, name);

        if (homeOptional.isEmpty()) {
            messageTo(sender).red("That home does not exist!").send();
            return;
        }

        Home home = homeOptional.get();

        sender.teleport(home.location());
        ColorHelper.aqua("Sending you to ").gold(target.isOnline() ? ((Player)target).getDisplayName() : target.getName()).aqua("'s home ").gold(home.name()).aqua("...").sendTo(sender);
    }

    private void sendHomeList(Player target, OfflinePlayer homeDude) {
        List<Home> homes = this.plugin.getHomeDatabase().getAllHomes(homeDude);
        String numberOfAllowedHomes = homeDude.isOnline() ? String.valueOf(PoniArcade_Homes.getNumberOfAllowedHomes((Permissible) homeDude)) : "?";

        if (homes.isEmpty()) {
            if (target == homeDude) {
                ColorHelper.aqua("You have no homes! (Limit: ").gold(numberOfAllowedHomes).aqua(")").sendTo(target);
            } else {
                ColorHelper.gold(homeDude.isOnline() ? ((Player)homeDude).getDisplayName() : homeDude.getName()).aqua(" has no homes! (Limit: ").gold(numberOfAllowedHomes).aqua(")").sendTo(target);
            }

            return;
        }


        ColorHelper.Builder builder = ColorHelper.aqua("Homes: ");
        int accum = 0;

        for (int i = 0; i < homes.size(); i++) {
            Home home = homes.get(i);

            builder.gold(home.name());

            if (i == homes.size() - 1) {
                builder.aqua(".");
            } else {
                builder.aqua(", ");
            }

            accum++;
        }

        builder.aqua(" (Used: ").gold("%d", accum).aqua("/").gold(numberOfAllowedHomes).aqua(")");
        builder.sendTo(target);
    }
}
