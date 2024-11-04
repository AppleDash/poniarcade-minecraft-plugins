package com.poniarcade.core.commands.type;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.structs.TeleportRequest;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.handlers.PlayerFlags;
import org.bukkit.entity.Player;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

/**
 * Created by appledash on 7/17/17.
 * Blackjack is best pony.
 */
@Command(name = "TPAHere", description = "Rqquest another player to teleport to you.", basePermission = "poniarcade.core.teleportrequest")
public class TeleportRequestHereCommand extends BaseCommand {
    private final PoniArcade_Core plugin;

    protected TeleportRequestHereCommand(PoniArcade_Core plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand(params = Player.class)
    public void handleTpaHere(Player sender, Player target) {
        if (sender == target) {
            messageTo(sender).red("You cannot request to teleport to yourself.").send();
            return;
        }

        TeleportRequest teleportRequest = new TeleportRequest(sender.getUniqueId(), TeleportRequest.Direction.THEM_TO_OTHER);
        this.plugin.getPlayerFlags().setFlag(target, PlayerFlags.FlagType.INCOMING_TELEPORT_REQUEST, teleportRequest);
        messageTo(sender).aqua("You have requested for ").gold(target.getDisplayName()).aqua(" to teleport to you.").send();
        messageTo(target).gold(sender.getDisplayName()).aqua(" is requesting for you to teleport to them. Type ").gold("/tpaccept").aqua(" to accept.").send();
    }
}
