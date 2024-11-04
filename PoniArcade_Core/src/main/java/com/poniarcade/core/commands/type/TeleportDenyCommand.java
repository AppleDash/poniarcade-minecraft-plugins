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
@Command(name = "TeleportDeny", description = "Deny a pending teleport request.", basePermission = "poniarcade.teleport.teleportrequest", aliases = "tpdeny")
public class TeleportDenyCommand extends BaseCommand {
    private final PoniArcade_Core plugin;

    public TeleportDenyCommand(PoniArcade_Core plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand
    public void handleTeleportDeny(Player sender) {
        TeleportRequest teleportRequest = this.plugin.getPlayerFlags().getFlag(sender, PlayerFlags.FlagType.INCOMING_TELEPORT_REQUEST);

        if (teleportRequest == null) {
            messageTo(sender).red("You have no pending teleport request.").send();
            return;
        }

        this.plugin.getPlayerFlags().clearFlag(sender, PlayerFlags.FlagType.INCOMING_TELEPORT_REQUEST);

        messageTo(sender).aqua("Teleport request denied.").send();

        if (this.plugin.getServer().getPlayer(teleportRequest.otherPlayerId()) != null) {
            messageTo(this.plugin.getServer().getPlayer(teleportRequest.otherPlayerId())).gold(sender.getDisplayName()).aqua(" has denied your teleport request.").send();
        }
    }
}
