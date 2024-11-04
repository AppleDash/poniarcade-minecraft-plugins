package com.poniarcade.messaging;

import com.google.common.collect.ImmutableList;
import org.bukkit.permissions.Permissible;

import java.util.List;
import java.util.regex.Pattern;

import static com.poniarcade.messaging.NicknameValidator.ValidationStatus.*;

/**
 * Created by appledash on 7/2/17.
 * Blackjack is best pony.
 */
public final class NicknameValidator {
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("&[a-fA-F0-9]");
    private static final Pattern VALID_CHARACTERS_PATTERN = Pattern.compile("[a-zA-Z0-9_]+");
    private static final List<String> NICKNAME_BLACKLIST = ImmutableList.of(
                "applejack",
                "cadance",
                "celestia",
                "chrysalis",
                "fluttershy",
                "luna",
                "pinkie_pie",
                "rarity",
                "rainbowdash",
                "sombra",
                "twilightsparkle",
                "trixielulamoon"
    );

	private NicknameValidator() {
	}

	public static ValidationStatus validateNickname(Permissible nickSetter, String nickname) {
        if (NicknameValidator.COLOR_CODE_PATTERN.matcher(nickname).find() && !nickSetter.hasPermission("poniarcade.messaging.nickname.color")) {
            return COLORS_NOT_ALLOWED;
        }

        if (!NicknameValidator.VALID_CHARACTERS_PATTERN.matcher(nickname).matches() && !nickSetter.hasPermission("poniarcade.messaging.nickname.invalid")) {
            return INVALID_CHARACTERS;
        }

        if (NicknameValidator.isNicknameBlacklisted(nickname) && !nickSetter.hasPermission("poniarcade.messaging.nickname.blacklisted")) {
            return BLACKLISTED;
        }

        if (nickname.length() > 16 && !nickSetter.hasPermission("poniarcade.messaging.nickname.long")) {
            return TOO_LONG;
        }

        return OK;
    }

    private static String normalizeNickname(String nick) {
        return nick.toLowerCase()
               .replace("5", "s")
               .replace("1", "l")
               .replace("7", "t")
               .replace("3", "e")
               .replace("4", "a")
               .replace("0", "o")
               .replace("_", "");
    }

    private static boolean isNicknameBlacklisted(String nick) {
        nick = NicknameValidator.normalizeNickname(nick).replaceAll("(pr[iu]nc([ie]+)(s+)?)|k[ie]ng|qu([ie]+)n", "");
        for (String blacklist : NicknameValidator.NICKNAME_BLACKLIST) {
            if (nick.equalsIgnoreCase(blacklist)) {
                return true;
            }
        }
        return false;
    }

    public enum ValidationStatus {
        OK(null),
        BLACKLISTED("That nickname is blacklisted."),
        COLORS_NOT_ALLOWED("You may not use color in your nickname."),
        INVALID_CHARACTERS("That nickname contains disallowed characters."),
        TOO_LONG("That nickname is too long."),
        ;

        private final String text;

        ValidationStatus(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }
    }
}
