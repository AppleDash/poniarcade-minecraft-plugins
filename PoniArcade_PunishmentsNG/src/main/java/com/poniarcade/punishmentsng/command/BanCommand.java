package com.poniarcade.punishmentsng.command;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.StringUtil;
import com.poniarcade.punishmentsng.PoniArcade_PunishmentsNG;
import com.poniarcade.punishmentsng.punishments.ActionType;
import com.poniarcade.punishmentsng.punishments.PunishmentManager.PunishResult;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

/**
 * Created by appledash on 2/26/18.
 * Blackjack is still best pony.
 */
@Command(name = "ban", description = "Ban a user from the server.", basePermission = "poniarcade.punishments.ban")
public class BanCommand extends BaseCommand {
    private final PoniArcade_PunishmentsNG plugin;

    public BanCommand(PoniArcade_PunishmentsNG plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @CommandHelp(usage = "<player> <reason>", help = "Ban the given player for the given reason.")
    @HandlesCommand(params = { OfflinePlayer.class, String[].class })
    public void handleBan(CommandSender sender, OfflinePlayer target, String[] reasonParts) {
        String reason = StringUtil.combine(reasonParts, " ");
        PunishResult result = this.plugin.getPunishmentManager().tryPunishUser(target, sender, ActionType.BAN, reason);

        String targetName = target.getName();

        if (result == PunishResult.SUCCESS) {
            if (target.isOnline()) {
                ((Player) target).kickPlayer(reason);
                targetName = ((Player) target).getDisplayName();
            }

            messageTo(sender).gold("%s", targetName).aqua(" has been banned.").send();
            this.getPlugin().getServer().broadcastMessage(ColorHelper.red("%s has been sent to the moon!", targetName).toString());
        } else {
            messageTo(sender).red(result.toString()).send();
        }
    }
}
