package com.poniarcade.core.commands.type;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.command.CommandSender;

@Command(name = "Whitelist", description = "Enable or disable the permission-based server whitelist.", basePermission = "core.whitelist.modify")
public class WhitelistCommand extends BaseCommand {
    private final PoniArcade_Core plugin;

    public WhitelistCommand(PoniArcade_Core plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand(subCommand = "status")
    @CommandHelp(usage = "status", help = "Check the current status of the whitelist.")
    public void handleStatus(CommandSender sender) {
        sender.sendMessage(ColorHelper.aqua("Whitelist is currently %s.", this.plugin.isWhitelistEnabled() ? "enabled" : "disabled").toString());
    }

    @HandlesCommand(params = Boolean.class, priority = 1)
    @CommandHelp(usage = "on", help = "Enable the whitelist.")
    @CommandHelp(usage = "off", help = "Disable the whitelist.")
    public void handleWhitelist(CommandSender sender, boolean enabled) {
        this.plugin.setWhitelistEnabled(enabled);
        sender.sendMessage(ColorHelper.aqua("Whitelist %s.", enabled ? "enabled" : "disabled").toString());
    }
}
