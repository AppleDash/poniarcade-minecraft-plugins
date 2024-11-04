package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;


@Command(name = "whois", description = "Retrieve various pieces of information about a player.", basePermission = "core.playerinfo", aliases = {"player", "who", "info", "playerinfo"})
public class WhoisCommand extends BaseCommand {

    public WhoisCommand(Plugin plugin) {
        super(plugin);
    }

    @HandlesCommand(params = OfflinePlayer.class)
    @CommandHelp(usage = "<player>", help = "Retrieve various pieces of information about the given player.")
    public void handleWhois(CommandSender sender, OfflinePlayer player) {
        if (player.isOnline()) {
            Player online = (Player) player;
            sender.sendMessage(
                ColorHelper.aqua("Name: ").gold(online.getName()).newLine()
                .aqua("Display Name: ").gold(online.getDisplayName()).newLine()
                .aqua("UUID: ").gold(online.getUniqueId().toString()).newLine()
                .aqua("Game mode: ").gold(online.getGameMode().toString()).newLine()
                .aqua("Health: ").gold("%.2f/%.2f", online.getHealth(), online.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()).newLine()
                .aqua("Food: ").gold("%d/20", online.getFoodLevel()).newLine()
                .aqua("Punishments: ").gold("http://poniarcade.com/punishments/player/%s", online.getName())
                .toString()
            );
            return;
        }

        String lastPlayed = LocalDateTime.ofEpochSecond(player.getLastPlayed(), 0, ZoneOffset.UTC)
            .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));

        sender.sendMessage(
            ColorHelper.aqua("Name: ").gold(player.getName()).newLine()
            // TODO: .aqua("Display Name: ").gold(player.getName()).newLine()
            .aqua("UUID: ").gold(player.getUniqueId().toString()).newLine()
            .aqua("Punishments: ").gold("http://poniarcade.com/punishments/player/%s", player.getName()).newLine()
            .aqua("Last online: ").gold(lastPlayed)
            .toString()
        );
    }
}
