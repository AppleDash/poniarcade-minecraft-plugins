package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

@Command(name = "gamemode", description = "Change the game mode of yourself of another player.", basePermission = "core.gamemode", aliases = {"gm", "gmode"})
public class GameModeCommand extends BaseCommand {

    public GameModeCommand(Plugin owning_plugin) {
        super(owning_plugin);
    }

    @HandlesCommand(params = {Player.class, String.class})
    @CommandHelp(usage = "<player> <game mode>", help = "Change the given player to the given game mode.")
    public void handleOther(CommandSender sender, Player target, String gameModeStr) {
        Optional<GameMode> gameMode = this.parseGameMode(gameModeStr);

        if (gameMode.isEmpty()) {
            sender.sendMessage(ColorHelper.red("There is no game mode called '%s'.", gameModeStr).toString());
            return;
        }

        target.setGameMode(gameMode.get());

        if (sender != target) {
            sender.sendMessage(ColorHelper.aqua("Game mode for ").gold(target.getDisplayName()).aqua(" has been changed to ").gold(target.getGameMode().toString()).aqua(".").toString());
        }

        target.sendMessage(ColorHelper.aqua("Your game mode has been changed to ").gold(target.getGameMode().toString()).aqua(".").toString());
    }

    @HandlesCommand(params = String.class)
    @CommandHelp(usage = "<game mode>", help = "Change your game mode to the given game mode.")
    public void handleSelf(Player player, String gameModeStr) {
	    this.handleOther(player, player, gameModeStr);
    }

    private Optional<GameMode> parseGameMode(String raw) {
        return switch (raw.toLowerCase()) {
            case "0", "survival", "s" -> Optional.of(GameMode.SURVIVAL);
            case "1", "creative", "c" -> Optional.of(GameMode.CREATIVE);
            case "2", "adventure", "a" -> Optional.of(GameMode.ADVENTURE);
            case "3", "spectator", "sp" -> Optional.of(GameMode.SPECTATOR);
            default -> Optional.empty();
        };

    }
}
