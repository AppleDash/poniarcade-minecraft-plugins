package com.poniarcade.core.commands.type;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.handlers.PlayerFlags;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "godmode", description = "Toggle god mode for yourself or another player.", aliases = "god", basePermission = "core.god")
public class GodModeCommand extends BaseCommand {
    private final PoniArcade_Core plugin;

    public GodModeCommand(PoniArcade_Core plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand
    @CommandHelp(help = "Toggle god mode on yourself.")
    public void handleGodSelf(Player sender) {
        PlayerFlags playerFlags = this.plugin.getPlayerFlags();
        boolean newGod = playerFlags.toggleFlag(sender, PlayerFlags.FlagType.GOD_MODE);
        sender.sendMessage(ColorHelper.aqua("God mode ").gold(newGod ? "enabled" : "disabled").aqua(".").toString());
    }

    @HandlesCommand(params = Player.class, permission = "core.god.other")
    @CommandHelp(usage = "<player>", help = "Toggle god mode on another player.")
    public void handleGodOther(CommandSender sender, Player target) {
        this.handleGodSelf(target);
    }

}
