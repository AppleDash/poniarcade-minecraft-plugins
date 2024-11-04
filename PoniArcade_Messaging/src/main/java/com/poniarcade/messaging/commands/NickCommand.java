package com.poniarcade.messaging.commands;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.messaging.NicknameValidator;
import com.poniarcade.messaging.PoniArcade_Messaging;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by appledash on 7/1/17.
 * Blackjack is best pony.
 */
@Command(name = "nick", description = "Change your nickname.", basePermission = "poniarcade.messaging.nickname", aliases = {"nickname", "nn"})
public class NickCommand extends BaseCommand {
    private final PoniArcade_Messaging plugin;

    public NickCommand(PoniArcade_Messaging plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @CommandHelp(usage = "off", help = "Removes your current nickname.")
    @HandlesCommand(subCommand = "off", priority = -1)
    public void handleNickOff(Player sender) {
        Optional<String> nickOptional = this.plugin.getNicknameManager().getNickname(sender);

        if (nickOptional.isEmpty()) {
            sender.sendMessage(ColorHelper.aqua("You do not currently have a nickname.").toString());
            return;
        }

        this.plugin.getNicknameManager().removeNickname(sender);
        sender.sendMessage(ColorHelper.aqua("Your nickname has been removed.").toString());
        sender.setDisplayName(sender.getName());
        sender.setPlayerListName(sender.getName());
    }

    @CommandHelp(usage = "<nickname>", help = "Changes your nickname to the given nickname.")
    @HandlesCommand(params = String.class)
    public void handleNickSelf(Player sender, String nickname) {
        if (this.plugin.getNicknameManager().isNicknameTaken(nickname)) {
            sender.sendMessage(ColorHelper.aqua("That nickname is already taken!").toString());
            return;
        }

        // They nicked to themselves in an attempt to remove the nick.
        if (nickname.equalsIgnoreCase(sender.getName())) {
            this.handleNickOff(sender);
            return;
        }

        NicknameValidator.ValidationStatus status = NicknameValidator.validateNickname(sender, nickname);

        if (status != NicknameValidator.ValidationStatus.OK) {
            sender.sendMessage(ColorHelper.red(status.getText()).toString());
            return;
        }

        if (sender.hasPermission("poniarcade.messaging.nickname.color")) {
            nickname = ChatColor.translateAlternateColorCodes('&', nickname);
        }

        this.setNickname(sender, nickname);
    }

    @CommandHelp(usage = "<player> <nickname>", help = "Change another player's nickname to the given value.")
    @CommandHelp(usage = "<player> off", help = "Remove another player's nickname.")
    @HandlesCommand(params = {OfflinePlayer.class, String.class}, permission = "poniarcade.messaging.nickname.other")
    public void handleNickOther(Player sender, OfflinePlayer target, String nickname) {
        if (this.plugin.getNicknameManager().isNicknameTaken(nickname)) {
            sender.sendMessage(ColorHelper.aqua("That nickname is already taken!").toString());
            return;
        }

        if (nickname.equalsIgnoreCase("off") || nickname.equalsIgnoreCase(target.getName())) {
            this.plugin.getNicknameManager().removeNickname(target);
            if (target.isOnline()) {
                ((CommandSender) target).sendMessage(ColorHelper.aqua("Your nickname has been removed.").toString());
                ((Player) target).setDisplayName(target.getName());
                ((Player) target).setPlayerListName(target.getName());
            }

            sender.sendMessage(ColorHelper.gold(((Player) target).getDisplayName()).aqua("'s nickname has been removed.").toString());

            return;
        }

        if (sender.hasPermission("poniarcade.messaging.nickname.color")) {
            nickname = ChatColor.translateAlternateColorCodes('&', nickname);
        }

        this.setNickname(target, nickname);
        sender.sendMessage(ColorHelper.gold(target.getName()).aqua("'s nickname has been changed to ").gold(nickname).aqua(".").toString());
    }

    private void setNickname(OfflinePlayer target, String nickname) {
        this.plugin.getNicknameManager().setNickname(target, nickname);

        if (target instanceof Player onlineTarget) {
            onlineTarget.sendMessage(ColorHelper.aqua("Your nickname has been changed to ").gold(nickname).aqua(".").toString());
            onlineTarget.setDisplayName("~" + nickname + ChatColor.RESET);
            onlineTarget.setPlayerListName("~" + nickname + ChatColor.RESET);
        }
    }


}
