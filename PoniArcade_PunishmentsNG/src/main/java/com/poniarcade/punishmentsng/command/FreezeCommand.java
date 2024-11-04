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

@Command(name = "freeze", description = "Freeze a user on the server.", basePermission = "poniarcade.punishments.freeze")
public class FreezeCommand extends BaseCommand {
    private final PoniArcade_PunishmentsNG plugin;

    public FreezeCommand(PoniArcade_PunishmentsNG plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @CommandHelp(usage = "<player> <reason>", help = "Freeze the given player for the given reason.")
    @HandlesCommand(params = { OfflinePlayer.class, String[].class })
    public void handleFreeze(CommandSender sender, OfflinePlayer target, String[] reasonParts) {
        String reason = StringUtil.combine(reasonParts, " ");
        PunishmentManager.PunishResult result = this.plugin.getPunishmentManager().tryPunishUser(target, sender, ActionType.FREEZE, reason);

        if (result == PunishmentManager.PunishResult.SUCCESS) {
            messageTo(sender).gold("%s", target.getName()).aqua(" has been frozen.").send();
            if (target.isOnline()) {
                ((CommandSender) target).sendMessage(ColorHelper.red("You have been frozen!").toString());
            }
        } else {
            messageTo(sender).red(result.toString()).send();
        }
    }
}
