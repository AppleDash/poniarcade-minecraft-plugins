package com.poniarcade.core.listeners;

import com.google.common.collect.ImmutableMap;
import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.handlers.CommandHandler;
import com.poniarcade.core.handlers.PlayerFlags;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.StringUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {
    private final PoniArcade_Core plugin;

    public ChatListener(PoniArcade_Core plugin) {
        this.plugin = plugin;
    }

    public static final Pattern URL_REGEX = Pattern.compile(
            "(?:(?:https?|ftp|file)://|www\\.|ftp\\.)[-A-Z0-9+&@#/%=~_|$?!:,.]*[A-Z0-9+&@#/%=~_|$]",
            Pattern.CASE_INSENSITIVE);

    private static final String[] whitelist = {
        "poniarcade.com",
        "poniverse.net",
        "pony.fm",
        "mlpforums.com",
        "ponyvillelive.com",
        "ponyroleplay.com",
        "equestria.tv",
        "pvlive.me",
        "poni.me",
        "equestriadaily.com",
        "imgur.com",
        "youtube.com"
    };

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleLinkFilter(AsyncPlayerChatEvent event) {
        event.setMessage(ChatListener.filterText(event.getPlayer(), event.getMessage()));
    }

    private static String filterText(CommandSender user, String message) {
        //TODO: Check for color characters
        if (user.hasPermission("chat.all")) {
            return message;
        }

        Matcher matcher = ChatListener.URL_REGEX.matcher(message);
        String url;
        boolean replace;

        while (matcher.find()) {
            try {
                url = new URL(matcher.group().toLowerCase()).getHost();
                replace = true;

                for (String domain : ChatListener.whitelist) {
                    if (url.contains(domain)) {
                        replace = false;
                        break;
                    }
                }

                if (replace) {
                    message = message.replaceFirst(matcher.group(), "**");
                }
            } catch (MalformedURLException e) {
                /* Skip */
            }
        }

        return message;
    }

    private final Map<String, String> overriddenCommands = ImmutableMap.of(
            "msg", "m",
            "tell", "m",
            "whisper", "m",
            "rn", "realname",
            "r", "reply",
            "tp", "teleport"
    );

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleCommandStuff(PlayerCommandPreprocessEvent evt) {
        String message = evt.getMessage();

        if (message.startsWith("/")) {
            message = message.substring(1);
        }

        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (player.hasPermission("poniarcade.core.commandspy") && this.plugin.getPlayerFlags().getFlag(player, PlayerFlags.FlagType.COMMAND_SPY)) {
                player.sendMessage(ColorHelper.yellow("[CS]: [").reset(evt.getPlayer().getDisplayName()).yellow("]: ").reset("/%s", message).toString());
            }
        }

        String[] split = message.split(" ");
        String command = split[0];

        if (command.contains(":")) {
            String[] split2 = command.split(":");
            if (split.length > 1) {
                command = split2[split2.length - 1];
            }
        }

        String[] args = new String[0];

        if (split.length > 1) {
            args = StringUtil.arraySubset(split, 1);
        }

        if (command.equalsIgnoreCase("help") && args.length > 0) {
            if (this.plugin.getCommandHelpGenerator().hasCommand(args[0])) {
                evt.getPlayer().sendMessage(this.plugin.getCommandHelpGenerator().getHelpForCommand(evt.getPlayer(), args[0]));
                evt.setCancelled(true);
                return;
            }
        }

        if (this.overriddenCommands.containsKey(command.toLowerCase())) {
            CommandHandler.getBukkitCommandMap().getCommand(this.overriddenCommands.get(command.toLowerCase())).execute(evt.getPlayer(), command, args);
            evt.setCancelled(true);
        }

        if (command.equalsIgnoreCase("op")) {
            if (evt.getPlayer().isOp()) {
                evt.getPlayer().sendMessage(ColorHelper.aqua("You have op, but you may not give it to others!").toString());
                evt.setCancelled(true);
            }
        }

        if (StringUtil.matchAny(command, "pl", "plugins", "ver", "version")) {
            this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                evt.getPlayer().sendMessage(ColorHelper.aqua(
                    "Surprised that worked? We try to be transparent and non-secretive about the software we run, " +
                        "just like in the good old days - we know you can't hack us just by knowing what plugins we run :-) Any questions? Just ask staff!"
                ).toString());
            }, 10);
        }
    }
}
