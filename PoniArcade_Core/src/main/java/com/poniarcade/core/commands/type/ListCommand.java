package com.poniarcade.core.commands.type;

import com.google.common.collect.ImmutableList;
import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Command(name = "list", description = "List online players", basePermission = "core.list")
public class ListCommand extends BaseCommand {

    private final PoniArcade_Core plugin;

    public ListCommand(PoniArcade_Core plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand
    @CommandHelp(help = "List online players.")
    public void handleList(CommandSender sender) {
        boolean seeVanished = sender.hasPermission("vanish.see"); // TODO: Use better permission?

        List<Player> visiblePlayers = seeVanished ? this.getAllOnlinePlayers() : this.getUnvanishedOnlinePlayers();

        ColorHelper.Builder cb = ColorHelper.empty();

        int onlinePlayers = 0;
        int ignoringCap = 0;

        for (Player player : visiblePlayers) {
            if (player.hasPermission("core.ignore_cap")) {
                ignoringCap++;
            } else {
                onlinePlayers++;
            }

            if (cb.length() > 0) {
                cb.aqua(", ");
            }

            cb.put(org.bukkit.ChatColor.RESET, player.getDisplayName());

            if (!player.getDisplayName().equals(player.getName())) {
                cb.aqua("[%s]", player.getName());
            }
        }

        String playerCount = ColorHelper.aqua("There are ")
                             .yellow("%d ", onlinePlayers)
                             .aqua("out of ")
                             .yellow("%d ", this.plugin.getServer().getMaxPlayers())
                             .aqua("online ")
                             .yellow("+%d ", ignoringCap)
                             .aqua("ignoring cap. (")
                             .yellow("%d ", visiblePlayers.size()).aqua("total.)").toString();

        sender.sendMessage(playerCount);
        sender.sendMessage(cb.toString());
    }

    private List<Player> getAllOnlinePlayers() {
        return ImmutableList.copyOf(this.plugin.getServer().getOnlinePlayers());
    }

    private List<Player> getUnvanishedOnlinePlayers() {
        return this.getAllOnlinePlayers().stream().filter(player -> !Utils.isVanished(player)).collect(ImmutableList.toImmutableList());
    }
}
