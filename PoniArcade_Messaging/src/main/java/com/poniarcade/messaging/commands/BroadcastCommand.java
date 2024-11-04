package com.poniarcade.messaging.commands;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.StringUtil;
import com.poniarcade.messaging.PoniArcade_Messaging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Command(name = "broadcast", description = "Broadcast a message to every server.", basePermission = "core.broadcast", aliases = "bcast")
public class BroadcastCommand extends BaseCommand {

    public BroadcastCommand(Plugin plugin) {
        super(plugin);
    }

    @CommandHelp(usage = "<message>", help = "Broadcast the given message to every server.")
    @HandlesCommand(params = String[].class)
    public void exec(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ColorHelper.red("You must supply a message!").toString());
            return;
        }

        this.broadcastMessage(ChatColor.AQUA + "[Broadcast]: " + ChatColor.GREEN + ChatColor.translateAlternateColorCodes('&', StringUtil.combine(args, " ")));
    }

    private void broadcastMessage(String message) {
        this.plugin.getServer().broadcastMessage(message);
        Player player;

        if ((player = Iterables.getFirst(this.plugin.getServer().getOnlinePlayers(), null)) != null) {
            ByteArrayDataOutput bado = ByteStreams.newDataOutput();
            bado.writeUTF("broadcast");
            bado.writeUTF(message);
            player.sendPluginMessage(PoniArcade_Core.getInstance(), PoniArcade_Messaging.PARC_MESSAGING_CHANNEL, bado.toByteArray());
        }
    }
}
