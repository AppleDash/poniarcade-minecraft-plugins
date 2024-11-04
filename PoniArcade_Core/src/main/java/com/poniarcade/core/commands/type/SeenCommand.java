package com.poniarcade.core.commands.type;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.PlayerHelper;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Optional;

/**
 * Created by appledash on 11/18/16.
 * Blackjack is still best pony.
 */
@Command(name = "seen", description = "Check when a player was last online.", basePermission = "core.seen")
public class SeenCommand extends BaseCommand {
    public SeenCommand(PoniArcade_Core plugin) {
        super(plugin);
    }

    @HandlesCommand(params = String.class)
    @CommandHelp(usage = "<player>", help = "Check when a player was last online.")
    public void handleSeen(CommandSender sender, String playerName) {
        Optional<OfflinePlayer> optionalPlayer = PlayerHelper.getOfflinePlayer(playerName);
        if (optionalPlayer.isEmpty()) {
            sender.sendMessage(ColorHelper.gold(playerName).aqua(" has never played here before.").toString());
            return;
        }

        OfflinePlayer op = optionalPlayer.get();

        playerName = op.getName();

        if (op.isOnline()) {
            sender.sendMessage(ColorHelper.gold(playerName).aqua(" is currently online!").toString());
            return;
        }

        String lastPlayed = LocalDateTime.ofEpochSecond(op.getLastPlayed(), 0, ZoneOffset.UTC)
            .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));

        sender.sendMessage(ColorHelper.gold(playerName).aqua(" was last seen at").gold(" %s", lastPlayed).aqua(".").toString());

        if (op.isBanned()) {
            sender.sendMessage(ColorHelper.gold(playerName).aqua(" has been banned!").toString());
        }
    }
}
