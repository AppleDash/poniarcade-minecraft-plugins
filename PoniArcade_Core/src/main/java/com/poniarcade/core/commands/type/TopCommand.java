package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Command(name = "top", description = "Teleport yourself to the highest point where you stand.", basePermission = "core.top")
public class TopCommand extends BaseCommand {

    public TopCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand
    @CommandHelp(help = "Teleport yourself to the highest point where you stand.")
    public void handleTop(Player sender) {
        Location location = sender.getLocation();
        location.setY(sender.getWorld().getHighestBlockYAt(sender.getLocation()));
        sender.teleport(location);
        sender.sendMessage(ColorHelper.aqua("Zoom!").toString());
    }
}
