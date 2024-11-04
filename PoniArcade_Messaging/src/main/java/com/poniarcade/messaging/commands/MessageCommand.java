package com.poniarcade.messaging.commands;

import com.google.common.base.Joiner;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.Utils;
import com.poniarcade.messaging.PoniArcade_Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by appledash on 7/1/17.
 * Blackjack is best pony.
 */
@Command(name = "message", description = "Send a message to another player.", basePermission = "poniarcade.messaging.message", aliases = {"tell", "msg", "m", "t", "whisper"})
public class MessageCommand extends BaseCommand {
    private final PoniArcade_Messaging plugin;

    public MessageCommand(PoniArcade_Messaging plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand(params = { Player.class, String[].class })
    @CommandHelp(usage = "<player> <message>", help = "Send the given message to the given player.")
    public void handleMsg(CommandSender sender, Player target, String[] message) {
        String messageJoined = Joiner.on(" ").join(message);

        if (Utils.isVanished(target) && !sender.hasPermission("poniarcade.messaging.message.vanished")) {
            sender.sendMessage(ColorHelper.red(ExecutionStatus.FAIL_PLAYER_NOT_FOUND.getMessage()).toString());
            return;
        }

        if (sender == target) {
            sender.sendMessage(ColorHelper.red("You can't message yourself!").toString());
            return;
        }

        sender.sendMessage(ColorHelper.blue("[me] -> [").reset(target.getDisplayName()).blue("]: ").reset(messageJoined).toString());
        target.sendMessage(ColorHelper.blue("[").reset(sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName()).blue("] -> [me]: ").reset(messageJoined).toString());
        this.plugin.getServer().getLogger().info(String.format("[%s] -> [%s]: %s", sender.getName(), target.getName(), messageJoined));

        if (sender instanceof Player player) {
            this.plugin.getSocialSpy().spyMessage(player, target, messageJoined);
            this.plugin.putLastTarget(player.getUniqueId(), target.getUniqueId());
            this.plugin.putLastTarget(target.getUniqueId(), player.getUniqueId());
        }

    }
}
