package com.poniarcade.core.handlers;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.poniarcade.core.utils.Pair;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by appledash on 7/25/16.
 * Blackjack is still best pony.
 */
public class CommandHelpGenerator {
    private static final int MAX_CHAT_WIDTH = 52;
    private final Map<String, List<Pair<String, String>>> commandMap = new HashMap<>();

    public void putCommand(String name, List<Pair<String, String>> commandHelps) {
        this.commandMap.put(name, commandHelps.stream().map((pair) -> Pair.of("/" + name + " " + pair.getLeft(), pair.getRight())).collect(Collectors.toList()));
    }

    private final Map<Integer, String> charList = ImmutableMap.of(
        -6, "§",
        2, "!.,:;i|",
        3, "'`l",
        4, " I[]t",
        5, "\"()*<>fk{}",
        7, "@~"
    );

    public List<String> generateSimpleHelpForCommand(String commandName) {
        if (this.commandMap.get(commandName.toLowerCase()) == null) {
            throw new IllegalArgumentException("Cannot generate help for a command we don't know about!");
        }

        List<String> strings = new ArrayList<>();

        this.commandMap.get(commandName).forEach((pair) -> {
            strings.add(String.format(ChatColor.AQUA + "%s - %s", pair.getLeft(), pair.getRight()));
        });

        return strings;
    }

    public List<String> generateHelpForCommand(String commandName) {
        int widestCommandLength = -1;
        int maxLineLength = -1;
        List<String> outLines = new ArrayList<>();

        List<Pair<String, String>> helps = this.commandMap.get(commandName);

        /* Find the longest command example. Example: "/ponies help" */
        for (Pair<String, String> help : helps) {
            if (help.getKey().length() > widestCommandLength) {
                widestCommandLength = help.getKey().length();
            }
        }

        /* Find the longest total length. Example: "/ponies help | Find help for ponies." */
        for (Pair<String, String> help : helps) {
            if (help.getValue().length() + widestCommandLength + 3 > maxLineLength) {
                maxLineLength = help.getValue().length() + widestCommandLength + 3; // Plus 3 for the " | "
            }
        }

        if (maxLineLength + 4 > CommandHelpGenerator.MAX_CHAT_WIDTH) { // Wrap it to the max chat width.
            maxLineLength = CommandHelpGenerator.MAX_CHAT_WIDTH - 4;
        }

        int headerFooterLength = maxLineLength + 4; // Plus four because there is a # and a space on each side of each command/info pair
        String headerTag = "[ Help for /" + commandName + " ]";

        // Rare, but it might happen. Plus two so we can still have one = on either side of the tag
        if (headerFooterLength < headerTag.length() + 2) {
            headerFooterLength = headerTag.length() + 2;
        }

        // Ensure our header tag and header/footer length are the same evenness/oddness
        if ((headerTag.length() % 2 == 0 && headerFooterLength % 2 != 0) || (headerTag.length() % 2 != 0 && headerFooterLength % 2 == 0)) {
            headerFooterLength++;
            maxLineLength++;
        }

        int headerPaddingWidth = (headerFooterLength - headerTag.length()) / 2; // This might have a problem with odd header/footer lengths, but we'll see.

        String header = ChatColor.AQUA + "=".repeat(headerPaddingWidth) + ChatColor.RESET + headerTag + ChatColor.AQUA + "=".repeat(headerPaddingWidth);
        String footer = ChatColor.AQUA + "=".repeat(headerFooterLength);

        outLines.add(header);

        for (Pair<String, String> help : helps) {
            String paddedCommand = this.padString(help.getKey(), widestCommandLength);
            String separator = ChatColor.AQUA + " | " + ChatColor.RESET;
            String helpText = help.getValue();

            if (paddedCommand.length() + separator.length() + helpText.length() > maxLineLength) { // If the current command plus help text is too long, we need to split it across lines
                int howWideCanTheHelpTextOnOneLineBe = maxLineLength - (paddedCommand.length() + ChatColor.stripColor(separator).length());
                boolean isFirst = true;
                do {
                    Pair<String, String> split = this.splitAtLastSpace(helpText, howWideCanTheHelpTextOnOneLineBe);

                    outLines.add(
                        this.padString(
                            ChatColor.AQUA + "# " + ChatColor.RESET +
                            (isFirst ? paddedCommand : " ".repeat(paddedCommand.length())) + separator +
                            split.getLeft(),

                            maxLineLength + 2
                        ) + ChatColor.AQUA + " #"
                    );
                    helpText = split.getRight();
                    isFirst = false;
                } while (!helpText.isEmpty());
            } else { // If it's not too long, it can just all be on one line.
                outLines.add(ChatColor.AQUA + "# " + ChatColor.RESET + this.padString(paddedCommand + separator + help.getValue(), maxLineLength) + ChatColor.AQUA + " #");
            }
        }

        outLines.add(footer);

        return outLines;
    }

    /**
     * returns character total width, considering format codes, internal use
     * @param ch the character to check
     * @param mono true for result in chars or false for result in pixels
     * @return character width depending on "mono"
     */
    private int pxLen(char ch, boolean mono) {
        if (mono) {
            return (ch == '§') ? -1 : 1;
        }
        // character list iteration, 6 = default
        int l = 6;
        for (int px : this.charList.keySet()) {
            if (this.charList.get(px).indexOf(ch) >= 0) {
                l = px;
                break;
            }
        }
        return l;
    }

    private Pair<String, String> splitAtLastSpace(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return Pair.of(str, "");
        }

        int spaceIndex = str.lastIndexOf(' ', maxLength - 1);

        if (spaceIndex == -1) {
            return Pair.of(str.substring(0, maxLength), str.substring(maxLength));
        }

        return Pair.of(str.substring(0, spaceIndex), str.substring(spaceIndex + 1));
    }

    private String padString(String str, int amount) {
        while (ChatColor.stripColor(str).length() < amount) {
            str += " ";
        }
        return str;
    }

    @SuppressWarnings("all")
    public static void main(String[] args) {
        CommandHelpGenerator commandHelpGenerator = new CommandHelpGenerator();
        commandHelpGenerator.putCommand("class", ImmutableList.of(
                                            Pair.of("", "Display this help message."),
                                            Pair.of("list", "Display a list of possible classes."),
                                            Pair.of("status", "Display your current class."),
                                            Pair.of("info <class>", "Display information about a class."),
                                            Pair.of("learn <class>", "Change to the given class."),
                                            Pair.of("buy <class>", "Purchase a master class with ingame currency."),
                                            Pair.of("confirm", "Confirm your class purchase."),
                                            Pair.of("decline", "Decline your class purchase.")
                                        ));

        System.out.println(ChatColor.stripColor(Joiner.on("\n").join(commandHelpGenerator.generateHelpForCommand("class"))));
    }
}
