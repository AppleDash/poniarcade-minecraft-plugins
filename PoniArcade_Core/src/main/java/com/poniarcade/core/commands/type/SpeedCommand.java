package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Command(name = "speed", description = "Set a player's speed.", basePermission = "core.speed", aliases = {"setspeed", "speedset"})
public class SpeedCommand extends BaseCommand {

    public SpeedCommand(Plugin owning_plugin) {
        super(owning_plugin);
    }


    @HandlesCommand(subCommand = "fly", params = {Player.class, Float.class})
    @CommandHelp(usage = "fly <player> <speed>", help = "Set another player's fly speed.")
    public void handleFlyOther(Player sender, Player other, float speed) {
	    this.applySpeed(sender, other, "fly", speed);
    }

    @HandlesCommand(subCommand = "walk", params = {Player.class, Float.class})
    @CommandHelp(usage = "walk <player> <speed>", help = "Set another player's walk speed.")
    public void handleWalkOther(Player sender, Player other, float speed) {
	    this.applySpeed(sender, other, "walk", speed);
    }

    @HandlesCommand(subCommand = "fly", params = Float.class)
    @CommandHelp(usage = "fly <speed>", help = "Set your own fly speed.")
    public void handleFlySelf(Player sender, float speed) {
	    this.applySpeed(sender, sender, "fly", speed);
    }

    @HandlesCommand(subCommand = "walk", params = Float.class)
    @CommandHelp(usage = "walk <speed>", help = "Set your own walk speed.")
    public void handleWalkSelf(Player sender, float speed) {
	    this.applySpeed(sender, sender, "walk", speed);
    }

    private void applySpeed(Player sender, Player target, String speedType, float value) {
        switch (speedType) {
            case "walk" -> target.setWalkSpeed(value);
            case "fly" -> target.setFlySpeed(value);
            default -> throw new IllegalStateException("Speed must be fly or walk.");
        }

        target.sendMessage(ColorHelper.aqua("Your ").gold("%s ", speedType).aqua("speed has been set to ").gold("%.2f", value).aqua(".").toString());
        if (sender != target) {
            sender.sendMessage(ColorHelper.aqua("%s ", target.toString()).gold("%s ", speedType).aqua("speed has been set to ").gold("%.2f", value).aqua(".").toString());
        }
    }
}
