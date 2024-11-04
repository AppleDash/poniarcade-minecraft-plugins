package com.poniarcade.core.commands;

import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.permissions.Permissible;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by appledash on 7/18/17.
 * Blackjack is best pony.
 */
public class CommandHelpGenerator2 {
    private final List<CommandHelpInfo> commandHelps = new ArrayList<>();

    public void putCommand(BaseCommand command) {
        for (Method m : command.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(HandlesCommand.class)) {
                HandlesCommand handlesCommand = m.getAnnotation(HandlesCommand.class);
                String permission = handlesCommand.permission().isEmpty() ? command.getPermission() : handlesCommand.permission();
                for (CommandHelp commandHelp : m.getAnnotationsByType(CommandHelp.class)) {
                    this.commandHelps.add(new CommandHelpInfo(command, permission, handlesCommand.subCommand() + " " + commandHelp.usage(), commandHelp.help()));
                }
            }
        }
    }

    public boolean hasCommand(String command) {
        return this.commandHelps.stream().anyMatch(commandHelpInfo -> commandHelpInfo.hasAlias(command));
    }

    public String getHelpForCommand(Permissible permissible, String command) {
        List<CommandHelpInfo> infos = this.commandHelps.stream().filter(chi -> chi.hasAlias(command)).collect(Collectors.toList());

        if (infos.isEmpty()) {
            throw new IllegalArgumentException("Cannot get help for command we do not know about!");
        }

        infos = infos.stream().filter(chi -> permissible.hasPermission(chi.permission)).collect(Collectors.toList());

        if (infos.isEmpty()) {
            return ColorHelper.red("You do not have permission to use any variant of that command.").toString();
        }

        ColorHelper.Builder builder = ColorHelper.empty();

        CommandHelpInfo theDude = infos.get(0);

        builder.gold("Command: ").aqua(theDude.command.getName()).newLine();
        builder.gold("Description: ").aqua(theDude.command.getDescription()).newLine();

        infos.forEach(info -> {
            builder.gold("Usage: ").aqua("/%s %s- %s", command, info.usage.isEmpty() ? "" : info.usage + " ", info.help).newLine();
        });

        return builder.toString();
    }

    private record CommandHelpInfo(BaseCommand command, String permission, String usage, String help) {
        public boolean hasAlias(String alias) {
            return this.command.getName().equalsIgnoreCase(alias) || this.command.getAliases().stream().anyMatch(s -> s.equalsIgnoreCase(alias));
        }
    }
}
