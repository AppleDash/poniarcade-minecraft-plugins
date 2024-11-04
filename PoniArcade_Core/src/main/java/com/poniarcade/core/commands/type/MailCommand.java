package com.poniarcade.core.commands.type;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.structs.MailMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

@Command(name = "mail", description = "Manage your mail messages.", basePermission = "poniarcade.core.mail")
public class MailCommand extends BaseCommand {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final PoniArcade_Core plugin;

    public MailCommand(PoniArcade_Core plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand(subCommand = "send", params = { OfflinePlayer.class, String[].class })
    @CommandHelp(usage = "<player> <message>", help = "Send the given message to the given player.")
    public void handleSend(Player sender, OfflinePlayer target, String message) {
        this.runAsync(() -> {
            this.plugin.getMailManager().storeMailMessage(sender, target, message);
            this.runSync(() -> {
                messageTo(sender).aqua("Message saved!").send();
            });
        });
    }

    @HandlesCommand(subCommand = "read")
    @CommandHelp(help = "Display a list of your unread messages.")
    public void handleRead(Player player) {
        this.runAsync(() -> {
            List<MailMessage> messages = this.plugin.getMailManager().getUnreadMessages(player);

            this.runSync(() -> {
                if (messages.isEmpty()) {
                    messageTo(player).aqua("You have no unread messages. Try ").gold("/mail archive ").aqua("to see your read messages.").send();
                } else {
                    messages.forEach(message -> {
                        messageTo(player).gold(message.time().format(MailCommand.FORMATTER)).aqua("|").yellow(message.senderName()).aqua(": ").yellow(message.message()).send();
                    });
                }
            });
        });
    }

    @HandlesCommand(subCommand = "clear")
    public void handleClear(Player sender) {
        this.runAsync(() -> {
            this.plugin.getMailManager().markAllIncomingMessagesAsRead(sender);
            this.runSync(() -> {
                messageTo(sender).aqua("All mail messages marked as read.").send();
            });
        });
    }

    @HandlesCommand(subCommand = "archive")
    @CommandHelp(help = "View the first page of archived messages.")
    public void handleArchive(Player sender) {
        this.handleArchive(sender, "", 1);
    }

    @HandlesCommand(subCommand = "archive", params = Integer.class)
    @CommandHelp(usage = "<page>", help = "View the given page of archived messages.")
    public void handleArchive(Player sender, int page) {
        this.handleArchive(sender, "", page);
    }

    @HandlesCommand(subCommand = "archive", params = { String.class, Integer.class })
    @CommandHelp(usage = "<search> <page>", help = "View the given page of archived messages, searching for the given word in them.")
    public void handleArchive(Player sender, String search, int page) {
        this.runAsync(() -> {
            List<MailMessage> messages = this.plugin.getMailManager().findArchivedMessages(sender, search, page);

            this.runSync(() -> {
                if (messages.isEmpty()) {
                    messageTo(sender).aqua("No messages matched your search.").send();
                } else {
                    messages.forEach(message -> {
                        messageTo(sender).gold(message.time().format(MailCommand.FORMATTER)).aqua("|").yellow(message.senderName()).aqua(": ").yellow(message.message()).send();
                    });
                }
            });
        });
    }

    private void runAsync(Runnable r) {
        new RunnableRunner(r).runTaskAsynchronously(this.plugin);
    }

    private void runSync(Runnable r) {
        this.plugin.getServer().getScheduler().runTask(this.plugin, r);
    }

    private static final class RunnableRunner extends BukkitRunnable {
        private final Runnable runnable;

        private RunnableRunner(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            this.runnable.run();
        }
    }

}
