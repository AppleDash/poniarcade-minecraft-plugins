package com.poniarcade.messaging.commands;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.messaging.PoniArcade_Messaging;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by appledash on 7/1/17.
 * Blackjack is best pony.
 */
@Command(name = "reply", description = "Reply to a message,", basePermission = "poniarcade.messaging.message", aliases = "reply")
public class ReplyCommand extends BaseCommand {
    private final PoniArcade_Messaging plugin;

    public ReplyCommand(PoniArcade_Messaging plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand(params = String[].class)
    public void handleReply(Player sender, String[] message) {
        String messageJoined = Joiner.on(" ").join(message);

        UUID targetUuid = this.plugin.getReplyCandidate(sender.getUniqueId());

        if (targetUuid == null || this.plugin.getServer().getPlayer(targetUuid) == null) {
            sender.sendMessage(ColorHelper.red("You do not have anybody to reply to, or the last person you messaged is no longer online!").toString());
            return;
        }

        if (Strings.isNullOrEmpty(messageJoined)) {
            sender.sendMessage(ColorHelper.red("You may not send an empty message.").toString());
            return;
        }

        Player target = this.plugin.getServer().getPlayer(targetUuid);

        sender.sendMessage(ColorHelper.blue("[me] -> [").reset(target.getDisplayName()).blue("]: ").reset(messageJoined).toString());
        target.sendMessage(ColorHelper.blue("[").reset(sender.getDisplayName()).blue("] -> [me]: ").reset(messageJoined).toString());
        this.plugin.getServer().getLogger().info(String.format("[%s] -> [%s]: %s", sender.getName(), target.getName(), messageJoined));

        this.plugin.getSocialSpy().spyMessage(sender, target, messageJoined);

        this.plugin.putLastTarget(sender.getUniqueId(), targetUuid);
        this.plugin.putLastTarget(targetUuid, sender.getUniqueId());
    }
}
