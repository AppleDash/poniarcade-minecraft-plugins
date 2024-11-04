package com.poniarcade.core.commands.type;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.handlers.PlayerFlags;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.entity.Player;

@Command(name = "cinematicflight", description = "Toggle cinematic-mode flight.", aliases = {"cinematicfly", "cflight", "cfly"}, basePermission = "core.flight.cinematic")
public class CinematicFlightCommand extends BaseCommand {
    private final PoniArcade_Core plugin;

    public CinematicFlightCommand(PoniArcade_Core plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @CommandHelp(help = "Check your current cinematic flight status.")
    @HandlesCommand
    public void handleStatus(Player sender) {
        boolean isCinematic = this.plugin.getPlayerFlags().getFlag(sender, PlayerFlags.FlagType.FLIGHT_CINEMATIC);
        sender.sendMessage(ColorHelper.aqua("Cinematic flight: ").yellow("%s", isCinematic ? "on" : "off").toString());
    }

    @CommandHelp(usage = "<on/off>", help = "Change your current cinematic flight status.")
    @HandlesCommand(params = Boolean.class)
    public void handleChange(Player sender, boolean enabled) {
        this.plugin.getPlayerFlags().setFlag(sender, PlayerFlags.FlagType.FLIGHT_CINEMATIC, enabled);
        this.handleStatus(sender);
    }
}
