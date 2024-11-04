package com.poniarcade.core.listeners;

import com.dthielke.herochat.Channel;
import com.dthielke.herochat.ChannelManager;
import com.dthielke.herochat.ChatterManager;
import com.dthielke.herochat.Herochat;
import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.handlers.PlayerFlags;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private final PoniArcade_Core plugin;
    // private final Set<UUID> notifiedPlayerNames = new HashSet<>();
    private int onlinePlayers;

    public JoinQuitListener(PoniArcade_Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void login(PlayerLoginEvent evt) {
        boolean ignoreCap = evt.getPlayer().hasPermission("poniarcade.core.ignore_cap");

        if (!ignoreCap) {
            this.onlinePlayers++;
        }

        //Make bypass players not count toward cap.
        if (this.onlinePlayers < PoniArcade_Core.getInstance().getServer().getMaxPlayers()) {
            evt.allow();
        }

        //Allow players with perms to override full server.
        if (evt.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (!ignoreCap) {
                evt.disallow(PlayerLoginEvent.Result.KICK_FULL, "Server is full.");
            } else {
                evt.allow();
            }
        }

        if (evt.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            this.plugin.getPlayerManager().handlePlayerJoin(evt.getPlayer());
        }
    }

    @EventHandler
    public void applyData(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();

        //Set First-Join Message
        if (!player.hasPlayedBefore()) {
            PoniArcade_Core.broadcastMessage(ChatColor.GOLD + "Welcome newpony " + player.getDisplayName() + " to the server!");
        }

        //Set Join Message
        if (!Utils.isVanished(player)) {
            evt.setJoinMessage(ChatColor.AQUA + player.getDisplayName() + ChatColor.AQUA + " has joined the server.");// Ranked: #" + corePlayer.getRank());
        }
        /* TODO

        if (corePlayer.getUnreadMail() > 0) {
            player.sendMessage(ChatColor.AQUA + "You have " + corePlayer.getUnreadMail() + " new message(s)");
            player.sendMessage(ChatColor.AQUA + "Type \"/mail read\" to view your inbox");
        } else {
            player.sendMessage(ChatColor.AQUA + "You have no new messages");
        }*/

        PlayerFlags playerFlags = PoniArcade_Core.getInstance().getPlayerFlags();
        player.setGameMode(playerFlags.getFlag(player, PlayerFlags.FlagType.GAME_MODE));

        if (player.hasPermission("poniarcade.core.join-info")) {
            player.sendMessage(ColorHelper.aqua("Status: ")
                               .gold("GM[").aqua("%s", playerFlags.getFlag(player, PlayerFlags.FlagType.GAME_MODE)).gold("]").aqua(", ")
                               .gold("God[").aqua("%b", playerFlags.getFlag(player, PlayerFlags.FlagType.GOD_MODE)).gold("]").aqua(", ")
                               .gold("CSpy[").aqua("%b", playerFlags.getFlag(player, PlayerFlags.FlagType.COMMAND_SPY)).gold("]").aqua(", ")
                               .toString()
                              );
        }


        /*if (evt.getPlayer().hasPermission("saneeconomy.mobkills.use") && !notifiedPlayerNames.contains(corePlayer.getPlayer().getUniqueId())) {
            notifiedPlayerNames.add(corePlayer.getPlayer().getUniqueId());
            corePlayer.getPlayer().sendMessage(ColorHelper.red("Attention! There's some important news about a recent feature addition on the server that you should read!").toString());
            corePlayer.getPlayer().sendMessage(ColorHelper.aqua("Check it out here: ").gold("https://mlpforums.com/topic/166573-about-the-recent-changes/").toString());
        }*/

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent evt) {
        if (!evt.getPlayer().hasPermission("poniarcade.core.ignore_cap")) {
            this.onlinePlayers--;
        }

        evt.setQuitMessage(ColorHelper.aqua("%s", evt.getPlayer().getName()).aqua(" has left the server.").toString());

        if (Utils.isVanished(evt.getPlayer())) {
            evt.setQuitMessage("");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent evt) {
        if (evt.getPlayer().hasPermission("core.autojoin.staff")) {
            ChatterManager chatterManager = Herochat.getChatterManager();
            chatterManager.addChatter(evt.getPlayer());
            ChannelManager channelManager = Herochat.getChannelManager();
            Channel channel = channelManager.getChannel("Staff");

            if (channel != null) {
                channel.addMember(chatterManager.getChatter(evt.getPlayer()), true, true);
            }

            Channel local = channelManager.getChannel("Local");

            if (local != null) {
                local.addMember(chatterManager.getChatter(evt.getPlayer()), true, true);
            }
        }
    }
}
