package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Command(name = "sudo", description = "Force another player to run a command.", basePermission = "core.sudo")
public class SudoCommand extends BaseCommand {

    public SudoCommand(Plugin plugin) {
        super(plugin);
    }


    @HandlesCommand(params = {Player.class, String[].class})
    @CommandHelp(usage = "<player> <command/args>", help = "Force the given player to run the given command with the given args.")
    @CommandHelp(usage = "<player> c:<message>", help = "Force the given player to send the given chat message.")
    public void handleSudo(Player sender, Player target, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ColorHelper.aqua("Something is wrong.").toString());
        }

        if (target.hasPermission("core.sudo.deny")) {
            sender.sendMessage(ColorHelper.aqua("You cannot sudo ").gold("%s", target.getDisplayName()).aqua(".").toString());
            return;
        }

        String message = StringUtil.combine(args, " ");

        if (message.startsWith("c:")) {
            target.chat(message.substring(2));
            sender.sendMessage(ColorHelper.aqua("Forcing ").gold("%s ", target.getDisplayName()).aqua("to say '").gold("%s", message.substring(2)).aqua("'.").toString());
        } else {
            if (!message.startsWith("/")) {
                message = "/" + message;
            }

            target.performCommand(message);
            sender.sendMessage(ColorHelper.aqua("Forcing ").gold("%s ", target.getDisplayName()).aqua("to run '").gold("%s", message).aqua("'.").toString());
        }
    }
}
