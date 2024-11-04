package com.poniarcade.poniarcade_homes.commands;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.poniarcade_homes.Home;
import com.poniarcade.poniarcade_homes.PoniArcade_Homes;
import org.bukkit.entity.Player;

/**
 * Created by appledash on 7/15/17.
 * Blackjack is best pony.
 */
@Command(name = "sethome", description = "Set a new home at your current location.", basePermission = "poniarcade.homes.set", aliases = "homeset")
public class SetHomeCommand extends BaseCommand {
    private final PoniArcade_Homes plugin;

    public SetHomeCommand(PoniArcade_Homes plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @CommandHelp(usage = "<home>", help = "Set a home at your current location. This will replace an old home with the same name.")
    @HandlesCommand(params = String.class)
    public void handleSetHome(Player sender, String name) {
        int allowedHomes = PoniArcade_Homes.getNumberOfAllowedHomes(sender);
        int currentCount = this.plugin.getHomeDatabase().getAllHomes(sender).size();
        boolean replacingHome = this.plugin.getHomeDatabase().getHome(sender, name).isPresent();

        if (!replacingHome && currentCount >= allowedHomes) {
            ColorHelper.red("Sorry, you have reached your maximum of ").gold("%d", allowedHomes).red(" homes!").sendTo(sender);
            return;
        }

        Home home = new Home(sender.getUniqueId(), name, sender.getLocation());
        this.plugin.getHomeDatabase().setHome(sender, home);

        ColorHelper.aqua("Home ").gold(home.name()).aqua(" set!").sendTo(sender);
    }

}
