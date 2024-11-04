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
 * Created by appledash on 7/16/17.
 * Blackjack is best pony.
 */
@Command(name = "TPA", description = "Request to teleport to another player.", basePermission = "poniarcade.core.teleportrequest")
public class TeleportRequestCommand extends BaseCommand {
    private final PoniArcade_Core plugin;

    public TeleportRequestCommand(PoniArcade_Core plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand(params = Player.class)
    public void handleTeleportRequest(Player sender, Player target) {
        if (sender == target) {
            messageTo(sender).red("You cannot request to teleport to yourself.").send();
            return;
        }

        TeleportRequest teleportRequest = new TeleportRequest(sender.getUniqueId(), TeleportRequest.Direction.OTHER_TO_THEM);
        messageTo(sender).aqua("Requesting to teleport to ").gold(target.getDisplayName()).aqua("...").send();
        this.plugin.getPlayerFlags().setFlag(target, PlayerFlags.FlagType.INCOMING_TELEPORT_REQUEST, teleportRequest);
        messageTo(target).gold(sender.getDisplayName()).aqua(" has requested to teleport to you. Use ").gold("/tpaccept").aqua(" to accept, or ").gold("/tpdeny").aqua(" to deny.").send();
    }
}
