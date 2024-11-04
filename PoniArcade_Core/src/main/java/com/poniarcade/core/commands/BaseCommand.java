package com.poniarcade.core.commands;

import com.google.common.base.Strings;
import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.utils.BooleanUtil;
import com.poniarcade.core.utils.PlayerHelper;
import com.poniarcade.core.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

/**
 * Represents an executable command.
 */
public abstract class BaseCommand extends org.bukkit.command.Command implements PluginIdentifiableCommand {
    protected final Plugin plugin;
    private final String[] NO_USAGE = new String[0];

    protected BaseCommand(Plugin plugin) {
        super(plugin.getClass().getSimpleName() + ":tmp");
        this.plugin = plugin;
        if (!this.getClass().isAnnotationPresent(Command.class)) {
            throw new IllegalStateException("Cannot register a Command that isn't annotated with @Command!");
        }

        Command annot = this.getClass().getAnnotation(Command.class);
        this.setName(annot.name());
        this.setDescription(annot.description());
        this.setPermission(annot.basePermission());
        this.setAliases(Arrays.asList(annot.aliases()));
        PoniArcade_Core.getInstance().getCommandHelpGenerator().putCommand(this);
    }

    @Override
    public @NotNull String getUsage() {
        return ChatColor.RED + "You shouldn't see this! Report this to staff, please.";
    }

    @Override
    public final boolean execute(@NotNull CommandSender sender, @NotNull String command, String[] args) {
        if (!this.testPermission(sender)) {
            if (Strings.isNullOrEmpty(this.getPermissionMessage())) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
            } else {
                for (String line : this.getPermissionMessage().replace("<permission>", this.getPermission()).split("\n")) {
                    sender.sendMessage(ChatColor.RED + line);
                }
            }
            return false;
        }

        try {
            ExecutionStatus executionStatus = ExecutionStatus.NOTHING;

            for (Method meth : this.getCommandHandlerMethods()) {
                HandlesCommand hc = meth.getAnnotation(HandlesCommand.class);
                if (!hc.subCommand().isEmpty()) { // We have a subcommand.
                    if (args.length == 0 || !args[0].equalsIgnoreCase(hc.subCommand())) {
                        // We need a sub command, but we don't have the right one, or have none at all.
                        continue;
                    }

                    // 2 or more params on the @HandlesCommand and the last one is an array
                    if (hc.params().length >= 2 && hc.params()[hc.params().length - 1].isArray()) {
                        // Command args are less than the number of params we have.
                        if (args.length - 1 < hc.params().length) {
                            // We have the wrong number of arguments.
                            continue;
                        }

                        String[] argsSub = new String[args.length - 1];
                        System.arraycopy(args, 1, argsSub, 0, argsSub.length);
                        executionStatus = this.parseArgumentsAndInvoke(meth, sender, hc.params(), argsSub);
                        break;
                    }

                    if (hc.params().length != args.length - 1) {
                        // We have the wrong number of arguments.
                        continue;
                    }

                    if (!hc.permission().isEmpty() && !sender.hasPermission(hc.permission())) {
                        executionStatus = ExecutionStatus.FAIL_NO_PERMISSIONS;
                        break;
                    }

                    // Remove the subcommand from the args array
                    String[] argsSub = new String[args.length - 1];
                    System.arraycopy(args, 1, argsSub, 0, argsSub.length);
                    executionStatus = this.parseArgumentsAndInvoke(meth, sender, hc.params(), argsSub);
                } else { // We have no subcommand
                    // This is something like @HandlesCommand(params = { String[] }) void handleCommand(String... args), if we need all the args for some reason.
                    if ((hc.params().length == 1) && hc.params()[0].isArray()) { // FIXME: This doesn't check for Player
                        executionStatus = this.invokeHandler(meth, new Object[] {sender, args});
                        break;
                    }

                    // 2 or more params on the @HandlesCommand and the last one is an array
                    if (hc.params().length >= 2 && hc.params()[hc.params().length - 1].isArray()) {
                        // Command args are less than the number of params we have.
                        if (args.length < hc.params().length) {
                            // We have the wrong number of arguments.
                            continue;
                        }

                        executionStatus = this.parseArgumentsAndInvoke(meth, sender, hc.params(), args);
                        break;
                    }

                    if (hc.params().length != args.length) {
                        // We have the wrong number of arguments.
                        continue;
                    }

                    if (!hc.permission().isEmpty() && !sender.hasPermission(hc.permission())) {
                        executionStatus = ExecutionStatus.FAIL_NO_PERMISSIONS;
                        break;
                    }

                    executionStatus = this.parseArgumentsAndInvoke(meth, sender, hc.params(), args);
                }

                break;
            }

            // At this point, we may or may not have invoked a method. Let's find out.
            if (executionStatus == ExecutionStatus.NOTHING) {
                // Nothing at all happened. No methods matched.
                sender.sendMessage(ChatColor.RED + "There is no syntax for that command that matches what you have entered.");
                sender.sendMessage(ChatColor.AQUA + String.format("Perhaps try /help %s for more information?", this.getName()));
            } else if (executionStatus != ExecutionStatus.SUCCESS) {
                // Something bad happened :(
                sender.sendMessage(ChatColor.RED + executionStatus.getMessage());
                sender.sendMessage(ChatColor.AQUA + String.format("Perhaps try /help %s for more information?", this.getName()));
            }

        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "An internal error has occurred while attempting to execute that command. Contact a staff member for help.");
            this.plugin.getLogger().log(Level.WARNING, "Error processing command \"" + command + " " + String.join(" ", args), e);
        }

        return true;
    }

    private List<Method> getCommandHandlerMethods() {
        List<Method> methods = new ArrayList<>();

        for (Method meth : this.getClass().getMethods()) {
            if (meth.isAnnotationPresent(HandlesCommand.class)) {
                methods.add(meth);
            }
        }

        // Sort by @HandlesCommand priority. This is so that more specific handlers are considered before capture-all 'String... args' methods.
        // Smallest to biggest
        methods.sort(Comparator.comparingInt(left -> left.getAnnotation(HandlesCommand.class).priority()));

        return Collections.unmodifiableList(methods);
    }

    private ExecutionStatus parseArgumentsAndInvoke(Method meth, CommandSender sender, Class<?>[] paramTypes, String[] params) {
        ExecutionStatus executionStatus = ExecutionStatus.SUCCESS;

        Object[] objects = new Object[paramTypes.length + 1];
        objects[0] = sender;

        /*
         * If we have a method that looks something like void handle(Player sender, String argument)
         * we assume that they want the Sender to be a Player. If the Sender isn't a Player, fail the command.
         */
        if (!Player.class.isAssignableFrom(sender.getClass()) && meth.getParameters()[0].getType() == Player.class) {
            return ExecutionStatus.FAIL_NEED_TO_BE_A_PLAYER;
        }

        for (int i = 0; i < objects.length - 1; i++) {
            String stringRep = params[i];

            // If it's an array, assume that every param after this is part of the array
            if (paramTypes[i].isArray()) {
                int parsedParams = i;
                int remainingParams = params.length - parsedParams;
                Object[] newObjects = new Object[parsedParams + 2]; // Room for sender, already parsed, and new array
                System.arraycopy(objects, 0, newObjects, 0, parsedParams + 1); // Copy the sender and the already parsed
                String[] array = new String[remainingParams];
                System.arraycopy(params, i, array, 0, remainingParams);

                // Prevent arrays of only nothing.
                if (array.length == 0 || Strings.isNullOrEmpty(StringUtil.combine(array, " "))) {
                    return ExecutionStatus.FAIL_ARGUMENT_PARSE_ERROR;
                }

                // This handles the case of an @HandlesCommand(params = { String.class }) but a String in the actual
                // method declaration, indicating we want to accept multiple arguments but have them combined into one.
                if (meth.getParameters()[i + 1].getType().isAssignableFrom(String.class)) {
                    newObjects[newObjects.length - 1] = StringUtil.combine(array, " ");
                } else {
                    newObjects[newObjects.length - 1] = array;
                }

                objects = newObjects;
                break;
            }

            if (paramTypes[i] == String.class) { // We want a string - no conversion required.
                objects[i + 1] = stringRep;
                continue;
            }

            if (paramTypes[i] == Integer.class) {
                try {
                    objects[i + 1] = Integer.valueOf(stringRep);
                    continue;
                } catch (NumberFormatException e) {
                    executionStatus = ExecutionStatus.FAIL_ARGUMENT_PARSE_ERROR;
                    break;
                }
            }

            if (paramTypes[i] == Double.class) {
                try {
                    objects[i + 1] = Double.valueOf(stringRep);
                    continue;
                } catch (NumberFormatException e) {
                    executionStatus = ExecutionStatus.FAIL_ARGUMENT_PARSE_ERROR;
                    break;
                }
            }

            if (paramTypes[i] == Float.class) {
                try {
                    objects[i + 1] = Float.valueOf(stringRep);
                    continue;
                } catch (NumberFormatException e) {
                    executionStatus = ExecutionStatus.FAIL_ARGUMENT_PARSE_ERROR;
                    break;
                }
            }

            if (paramTypes[i] == OfflinePlayer.class) {
                Optional<Player> online = PlayerHelper.yank(stringRep);
                OfflinePlayer player;

                if (online.isPresent()) {
                    player = online.get();
                } else {
                    //noinspection deprecation
                    player = Bukkit.getServer().getOfflinePlayer(stringRep);
                }

                if (player == null) {
                    executionStatus = ExecutionStatus.FAIL_PLAYER_NOT_FOUND;
                    break;
                }
                objects[i + 1] = player;
                continue;
            }

            if (paramTypes[i] == Player.class) {
                Optional<Player> player = PlayerHelper.yank(stringRep);
                if (player.isEmpty()) {
                    executionStatus = ExecutionStatus.FAIL_PLAYER_NOT_FOUND;
                    break;
                }
                objects[i + 1] = player.get();
                continue;
            }

            if (paramTypes[i] == Boolean.class) {
                BooleanUtil.AlmostBoolean bool = BooleanUtil.parseHard(stringRep);

                if (bool == BooleanUtil.AlmostBoolean.NULL) {
                    executionStatus = ExecutionStatus.FAIL_ARGUMENT_PARSE_ERROR;
                    break;
                }

                objects[i + 1] = bool.getBool();
            }
        }

        if (executionStatus != ExecutionStatus.SUCCESS) {
            return executionStatus;
        }

        // Done parsing all the objects...
        return this.invokeHandler(meth, objects);
    }

    private ExecutionStatus invokeHandler(Method meth, Object[] objects) {
        try {
            meth.invoke(this, objects);
            return ExecutionStatus.SUCCESS;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            this.plugin.getLogger().log(Level.WARNING, "Failed to invoke command handler", e);
            return ExecutionStatus.FAIL_INTERNAL_ERROR;
        }
    }

    @Override
    public final @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    public enum ExecutionStatus {
        NOTHING("Nothing interesting happened."), // No command handler was called.
        SUCCESS("Success."), // Command successfully executed
        FAIL_NO_MATCH("There is no syntax for that command that matches what you have entered."), // No command handler matched the syntax given
        FAIL_ARGUMENT_PARSE_ERROR("The wrong argument types were given. Did you use a string where you meant to use a number?"), // Failed to convert an argument, for example failed to parse String to Integer.
        FAIL_INTERNAL_ERROR("An internal server error occurred while attempting to execute that command."), // Exception was thrown by command handler
        FAIL_NO_PERMISSIONS("You do not have permission to use that command in that way."), // Command handler matched, but the player does not have its required permission.
        FAIL_PLAYER_NOT_FOUND("The player provided was not found."), // A Player type argument was not found on the server.
        FAIL_NEED_TO_BE_A_PLAYER("You need to be a player in order to run that command in that form.");

        private final String message;

        ExecutionStatus(String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }
    }

}
