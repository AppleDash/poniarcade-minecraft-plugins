package com.poniarcade.warps.commands;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.warps.PoniArcade_Warps;
import com.poniarcade.warps.TeleportLocation;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by appledash on 5/16/16.
 * Blackjack is still best pony.
 */
public class TeleportLocationCommand extends BaseCommand {
    private final PoniArcade_Warps plugin;
    private final TeleportLocation.Type type;

    protected TeleportLocationCommand(PoniArcade_Warps owning_plugin, TeleportLocation.Type type) {
        super(owning_plugin);
        this.plugin = owning_plugin;
        this.type = type;
    }

    @HandlesCommand
    @CommandHelp(help = "List the available teleport points.")
    public void handleList(Player player) {
        String[] locationNames = this.listLocations();

        if (locationNames.length == 0) {
            player.sendMessage(ColorHelper.aqua("There are no %s.", this.type.getPlural()).toString());
            return;
        }

        this.sendLocationList(player);
    }

    @HandlesCommand(params = String.class)
    @CommandHelp(usage = "<name>", help = "Teleport to the given teleport point.")
    public void handleTeleportSelf(Player player, String locationName) {
        Optional<TeleportLocation> warp = this.plugin.getTeleportManager().getTeleportLocation(this.type, locationName);

        if (warp.isEmpty()) {
            player.sendMessage(ColorHelper.aqua("No %s named ", this.type.getSingular()).gold(locationName).aqua(" exists!").toString());
            return;
        }

        this.teleportPlayer(player, player, warp.get());
    }

    protected void teleportPlayer(Player cause, Player target, TeleportLocation teleportLocation) {
        if (cause == target) {
            cause.sendMessage(ColorHelper.aqua("Teleporting you to %s ", this.type.getSingular()).gold(teleportLocation.getName()).aqua("...").toString());
        } else {
            target.sendMessage(ColorHelper.gold(cause.getDisplayName()).aqua(" is teleporting you to ").gold(teleportLocation.getName()).aqua("...").toString());
            cause.sendMessage(ColorHelper.aqua("Teleporting ").gold(target.getDisplayName()).aqua(" to %s ", this.type.getSingular()).gold(teleportLocation.getName()).aqua("...").toString());
        }
        target.teleport(teleportLocation.getLocation());
    }

    private String[] listLocations() {
        return this.plugin.getTeleportManager().getAllLocations(this.type).stream().map(TeleportLocation::getName).toArray(String[]::new);
    }

    private void sendLocationList(Player player) {
        // {"text":"Click","clickEvent":{"action":"run_command","value":"/say Must be OP'd to run this command"}}
        StringBuilder json = new StringBuilder(String.format("{\"text\": \"%s\", \"extra\": [", ColorHelper.aqua("Available %s: ", this.type.getPlural())));

        String[] warpNames = this.listLocations();
        for (int i = 0; i < warpNames.length; i++) {
            String warpName = warpNames[i];
            String textElem = String.format("{\"text\": \"%s\", \"clickEvent\": {\"action\": \"run_command\", \"value\": \"/%s %s\"}, \"hoverEvent\": {\"action\": \"show_text\", \"value\": {\"text\": \"Click to warp!\"}}}", ColorHelper.aqua("%s", warpName) + (i == warpNames.length - 1 ? "" : ", "), this.type.getSingular(), warpName);
            json.append(textElem).append(", ");
        }

        json = new StringBuilder(json.substring(0, json.length() - 2));
        json.append("]}");

        String command = String.format("tellraw %s %s", player.getName(), json);
        this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), command); // FIXME: There has to be a better way.
    }
}
