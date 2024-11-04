package com.poniarcade.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by appledash on 7/12/17.
 * Blackjack is best pony.
 */
public final class PlayerHelper {
	private PlayerHelper() {
	}

	public static Optional<Player> yank(String name) {
        for (Player candidate : Bukkit.getServer().getOnlinePlayers()) {
            if (name.charAt(0) == '!') {
                if (candidate.getName().equalsIgnoreCase(name) || candidate.getDisplayName().replace("~", "").equalsIgnoreCase(name)) {
                    return Optional.of(candidate);
                }
            } else {
                if (PlayerHelper.sanitizeName(candidate.getName()).contains(PlayerHelper.sanitizeName(name)) || PlayerHelper.sanitizeName(candidate.getDisplayName()).contains(PlayerHelper.sanitizeName(name))) {
                    return Optional.of(candidate);
                }
            }
        }

        return Optional.empty();
    }

    public static Optional<OfflinePlayer> getOfflinePlayer(String name) {
        @SuppressWarnings("deprecation")
        OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(name);

        if (!offlinePlayer.hasPlayedBefore()) {
            return Optional.empty();
        }

        return Optional.of(offlinePlayer);
    }

    private static String sanitizeName(String name) {
        return name.toLowerCase().replace("_", "").replace("~", "");
    }
}
