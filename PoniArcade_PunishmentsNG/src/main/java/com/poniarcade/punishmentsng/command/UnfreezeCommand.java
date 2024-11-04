package com.poniarcade.punishmentsng.command;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.StringUtil;
import com.poniarcade.punishmentsng.PoniArcade_PunishmentsNG;
import com.poniarcade.punishmentsng.punishments.ActionDirection;
import com.poniarcade.punishmentsng.punishments.ActionType;
import com.poniarcade.punishmentsng.punishments.PunishmentManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

@Command(name = "unfreeze", description = "Unfreeze a user on the server.", basePermission = "poniarcade.punishments.freeze", aliases = "thaw")
public class UnfreezeCommand extends BaseCommand {
    private final PoniArcade_PunishmentsNG plugin;

    public UnfreezeCommand(PoniArcade_PunishmentsNG plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @CommandHelp(usage = "<player> <reason>", help = "Unfreeze the given player for the given reason.")
    @HandlesCommand(params = { OfflinePlayer.class, String[].class })
    public void handleUnfreeze(CommandSender sender, OfflinePlayer target, String[] reasonParts) {
        String reason = StringUtil.combine(reasonParts, " ");
        PunishmentManager.PunishResult result = this.plugin.getPunishmentManager().tryPunishUser(target, sender, ActionType.FREEZE, ActionDirection.REVOKE_PUNISHMENT, reason);

        if (result == PunishmentManager.PunishResult.SUCCESS) {
            messageTo(sender).gold("%s", target.getName()).aqua(" has been unfrozen.").send();

            if (target.isOnline()) {
                messageTo((CommandSender) target).aqua("You have been unfrozen.").send();
            }
        } else {
            messageTo(sender).red(result.getMessage()).send();
        }
    }
}
