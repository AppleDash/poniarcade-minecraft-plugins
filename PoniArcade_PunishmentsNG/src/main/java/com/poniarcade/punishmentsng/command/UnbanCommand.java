package com.poniarcade.punishmentsng.command;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.StringUtil;
import com.poniarcade.punishmentsng.PoniArcade_PunishmentsNG;
import com.poniarcade.punishmentsng.punishments.ActionDirection;
import com.poniarcade.punishmentsng.punishments.ActionType;
import com.poniarcade.punishmentsng.punishments.PunishmentManager.PunishResult;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

/**
 * Created by appledash on 2/26/18.
 * Blackjack is still best pony.
 */
@Command(name = "unban", description = "Unban a user from the server.", basePermission = "poniarcade.punishments.ban", aliases = "pardon")
public class UnbanCommand extends BaseCommand {
    private final PoniArcade_PunishmentsNG plugin;

    public UnbanCommand(PoniArcade_PunishmentsNG plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @CommandHelp(usage = "<player> <reason>", help = "Unban the given player for the given reason.")
    @HandlesCommand(params = { OfflinePlayer.class, String[].class })
    public void handleUnban(CommandSender sender, OfflinePlayer target, String[] reasonParts) {
        String reason = StringUtil.combine(reasonParts, " ");
        PunishResult result = this.plugin.getPunishmentManager().tryPunishUser(target, sender, ActionType.BAN, ActionDirection.REVOKE_PUNISHMENT, reason);

        if (result == PunishResult.SUCCESS) {
            messageTo(sender).gold("%s", target.getName()).aqua(" has been unbanned.").send();
        } else {
            messageTo(sender).red(result.getMessage()).send();
        }
    }
}
