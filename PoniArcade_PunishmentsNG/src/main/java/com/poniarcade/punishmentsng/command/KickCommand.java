package com.poniarcade.punishmentsng.command;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.StringUtil;
import com.poniarcade.punishmentsng.PoniArcade_PunishmentsNG;
import com.poniarcade.punishmentsng.punishments.ActionType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

/**
 * Created by appledash on 7/23/17.
 * Blackjack is best pony.
 */
@Command(name = "kick", description = "Kick a player from the server.", aliases = "k", basePermission = "poniarcade.punishments.kick")
public class KickCommand extends BaseCommand {
    private final PoniArcade_PunishmentsNG plugin;

    public KickCommand(PoniArcade_PunishmentsNG plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @CommandHelp(usage = "<player> <reason>", help = "Kick a player from the server.")
    @HandlesCommand(params = { Player.class, String[].class })
    public void handleKick(CommandSender sender, Player target, String[] reasonParts) {
        String reason = StringUtil.combine(reasonParts, " ");
        this.plugin.getPunishmentManager().punishUser(target, sender, ActionType.KICK, reason);
        target.kickPlayer(reason);
        messageTo(sender).gold("%s", target.getDisplayName()).aqua(" has been kicked.").send();
    }
}
