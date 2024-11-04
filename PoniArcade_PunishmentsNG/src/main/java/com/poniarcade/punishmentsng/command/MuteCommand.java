package com.poniarcade.punishmentsng.command;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.StringUtil;
import com.poniarcade.punishmentsng.PoniArcade_PunishmentsNG;
import com.poniarcade.punishmentsng.punishments.ActionType;
import com.poniarcade.punishmentsng.punishments.PunishmentManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

/**
 * Created by appledash on 5/14/18.
 * Blackjack is best pony.
 */
@Command(name = "mute", description = "Mute a player", basePermission = "poniarcade.punishments.mute")
public class MuteCommand extends BaseCommand {
    private final PoniArcade_PunishmentsNG plugin;

    public MuteCommand(PoniArcade_PunishmentsNG plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @CommandHelp(usage = "<player> <reason>", help = "Mute the given player for the given reason.")
    @HandlesCommand(params = { OfflinePlayer.class, String[].class })
    public void handleMute(CommandSender sender, OfflinePlayer target, String[] reasonParts) {
        String reason = StringUtil.combine(reasonParts, " ");
        PunishmentManager.PunishResult result = this.plugin.getPunishmentManager().tryPunishUser(target, sender, ActionType.BAN, reason);
        String targetName = target.getName();

        if (result == PunishmentManager.PunishResult.SUCCESS) {
            if (target.isOnline()) {
                ((CommandSender) target).sendMessage(ColorHelper.red("You have been muted!").toString());
            }

            messageTo(sender).gold("%s", targetName).aqua(" has been muted.").send();
        } else {
            messageTo(sender).red(result.toString()).send();
        }
    }
}
