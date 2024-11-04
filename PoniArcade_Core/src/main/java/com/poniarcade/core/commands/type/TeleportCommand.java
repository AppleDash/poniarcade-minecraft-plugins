package com.poniarcade.core.commands.type;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

/**
 * Created by appledash on 7/16/17.
 * Blackjack is best pony.
 */
@Command(name = "teleport", description = "Teleport to a player or a location.", basePermission = "poniarcade.core.teleport", aliases = "tp")
public class TeleportCommand extends BaseCommand {
    public TeleportCommand(PoniArcade_Core plugin) {
        super(plugin);
    }

    @CommandHelp(usage = "<player>", help = "Teleport yourself to another player.")
    @HandlesCommand(params = Player.class)
    public void handleSelfToPlayer(Player sender, Player target) {
        if (sender == target) {
            messageTo(sender).red("You cannot teleport to yourself!").send();
            return;
        }

        sender.teleport(target);
        messageTo(sender).aqua("Teleporting to ").gold(target.getName()).aqua(".").send();
    }

    @CommandHelp(usage = "<player> <other player>", help = "Teleport a player to another player.")
    @HandlesCommand(params = { Player.class, Player.class })
    public void handlePlayerToPlayer(Player sender, Player from, Player to) {
        if (from == to) {
            messageTo(sender).red("You cannot teleport a player to themselves!").send();
            return;
        }

        from.teleport(to);
        messageTo(from).gold(sender.getDisplayName()).aqua(" is teleporting you to ").gold(to.getDisplayName()).aqua(".").send();
        messageTo(to).gold(sender.getDisplayName()).aqua(" has teleported ").gold(from.getDisplayName()).aqua(" to you.").send();
    }

    @CommandHelp(usage = "<x> <y> <z>", help = "Teleport yourself to the given coordinates.")
    @HandlesCommand(params = { Double.class, Double.class, Double.class })
    public void handleSelfToLocation(Player sender, double posX, double posY, double posZ) {
        Location target = new Location(sender.getWorld(), posX, posY, posZ, sender.getLocation().getYaw(), sender.getLocation().getPitch());
        sender.teleport(target);
        messageTo(sender).aqua("Teleporting to ").gold("%.2f", posX).aqua(", ").gold("%.2f", posY).aqua(", ").gold("%.2f", posZ).aqua(".").send();
    }

    @CommandHelp(usage = "<player> <x> <y> <z>", help = "Teleport a player to the given coordinates.")
    @HandlesCommand(params = { Player.class, Double.class, Double.class, Double.class })
    public void handlePlayerToLocation(Player sender, Player target, double posX, double posY, double posZ) {
        Location targetLoc = new Location(target.getWorld(), posX, posY, posZ, target.getLocation().getYaw(), target.getLocation().getPitch());
        target.teleport(targetLoc);
        messageTo(sender).aqua("Teleporting ").gold(target.getDisplayName()).aqua(" to ").gold("%.2f", posX).aqua(", ").gold("%.2f", posY).aqua(", ").gold("%.2f", posZ).aqua(".").send();
        messageTo(sender).gold(sender.getDisplayName()).aqua(" is teleporting you to ").gold("%.2f", posX).aqua(", ").gold("%.2f", posY).aqua(", ").gold("%.2f", posZ).aqua(".").send();
    }
}
