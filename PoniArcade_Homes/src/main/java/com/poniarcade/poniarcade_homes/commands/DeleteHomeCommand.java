package com.poniarcade.poniarcade_homes.commands;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.poniarcade_homes.Home;
import com.poniarcade.poniarcade_homes.PoniArcade_Homes;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by appledash on 7/16/17.
 * Blackjack is best pony.
 */
@Command(name = "deletehome", description = "Delete a home.", basePermission = "poniarcade.homes.delete", aliases = "delhome")
public class DeleteHomeCommand extends BaseCommand {
    private final PoniArcade_Homes plugin;

    public DeleteHomeCommand(PoniArcade_Homes plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @CommandHelp(usage = "<name>", help = "Delete your home point with the given name.")
    @HandlesCommand(params = String.class)
    public void handleDeleteHome(Player sender, String homeName) {
        Optional<Home> homeOptional = this.plugin.getHomeDatabase().getHome(sender, homeName);

        if (homeOptional.isEmpty()) {
            ColorHelper.red("That home does not exist!").sendTo(sender);
            return;
        }

        Home home = homeOptional.get();
        this.plugin.getHomeDatabase().deleteHome(sender, home);
        ColorHelper.aqua("Home ").gold(home.name()).aqua(" deleted!").sendTo(sender);
    }
}
