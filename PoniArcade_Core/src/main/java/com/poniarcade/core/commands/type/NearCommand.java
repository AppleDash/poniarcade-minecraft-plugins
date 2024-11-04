package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Command(name = "near", description = "Get a list of players near you.", basePermission = "core.near")
public class NearCommand extends BaseCommand {

    public NearCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand
    @CommandHelp(help = "Get a list of players within 100 blocks of you.")
    public void handleNear(Player sender) {
        double maxDistance = 100.0D;
        Location senderLocation = sender.getLocation();
        List<Player> nearPlayers = new ArrayList<>();

        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (player != sender && player.getLocation().distance(senderLocation) <= maxDistance && !Utils.isVanished(player)) {
                nearPlayers.add(player);
            }
        }

        nearPlayers.sort(Comparator.comparingInt(player -> (int) Math.floor(player.getLocation().distance(senderLocation))));

        ColorHelper.Builder builder = ColorHelper.aqua("Nearby players (within 100 blocks): ");

        if (nearPlayers.isEmpty()) {
            builder.gold("Nobody.");
        } else {
            for (int i = 0; i < nearPlayers.size(); i++) {
                Player nearPlayer = nearPlayers.get(i);
                builder.gold(nearPlayer.getDisplayName()).aqua(" (%.2f blocks)", nearPlayer.getLocation().distance(senderLocation));

                if (i == nearPlayers.size() - 1) {
                    builder.aqua(".");
                } else {
                    builder.aqua(", ");
                }
            }
        }

        sender.sendMessage(builder.toString());
    }
}
