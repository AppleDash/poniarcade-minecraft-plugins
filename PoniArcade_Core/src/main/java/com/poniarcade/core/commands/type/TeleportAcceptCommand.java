package com.poniarcade.core.commands.type;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.handlers.PlayerFlags;
import com.poniarcade.core.structs.TeleportRequest;
import org.bukkit.entity.Player;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

/**
 * Created by appledash on 7/16/17.
 * Blackjack is best pony.
 */
@Command(name = "TeleportAccept", description = "Accept a pending teleport request.", basePermission = "poniarcade.core.teleportrequest", aliases = "tpaccept")
public class TeleportAcceptCommand extends BaseCommand {
    private final PoniArcade_Core plugin;

    public TeleportAcceptCommand(PoniArcade_Core plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand
    public void handleTpAccept(Player sender) {
        TeleportRequest teleportRequest = this.plugin.getPlayerFlags().getFlag(sender, PlayerFlags.FlagType.INCOMING_TELEPORT_REQUEST);

        if (teleportRequest == null) {
            messageTo(sender).red("You have no pending teleport request.").send();
            return;
        }

        Player player = this.plugin.getServer().getPlayer(teleportRequest.otherPlayerId());

        if (player == null) {
            messageTo(sender).red("The player who last requested to teleport to you is no longer online.").send();
            return;
        }

        messageTo(sender).aqua("Teleport request from ").gold(player.getDisplayName()).aqua(" accepted.").send();
        messageTo(player).gold(sender.getDisplayName()).aqua(" has accepted your teleport request.").send();

        if (teleportRequest.direction() == TeleportRequest.Direction.OTHER_TO_THEM) {
            player.teleport(sender);
        } else {
            sender.teleport(player);
        }

        this.plugin.getPlayerFlags().clearFlag(sender, PlayerFlags.FlagType.INCOMING_TELEPORT_REQUEST);
    }
}
