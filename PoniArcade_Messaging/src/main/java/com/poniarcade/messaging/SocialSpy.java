package com.poniarcade.messaging;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by appledash on 12/14/16.
 * Blackjack is best pony.
 */
public class SocialSpy {
    private static final String SOCIAL_SPY_PERMISSION = "core.socialspy";
    private final PoniArcade_Messaging plugin;

    public SocialSpy(PoniArcade_Messaging plugin) {
        this.plugin = plugin;
    }

    public void spyMessage(Player sender, Player target, String body) {
        Collection<? extends Player> onlinePlayers = this.plugin.getServer().getOnlinePlayers();

        this.printSpyMessage(sender.getDisplayName(), target.getDisplayName(), body);

        if (!onlinePlayers.isEmpty()) {
            ByteArrayDataOutput bado = ByteStreams.newDataOutput();
            bado.writeUTF("socialspy");
            bado.writeUTF(sender.getDisplayName());
            bado.writeUTF(target.getDisplayName());
            bado.writeUTF(body);

            ImmutableList.copyOf(onlinePlayers).get(0).sendPluginMessage(
                this.plugin,
                "poniarcade:parcmessaging",
                bado.toByteArray()
            );
        }
    }

    public void printSpyMessage(String senderName, String targetName, String body) {
        Collection<? extends Player> onlinePlayers = this.plugin.getServer().getOnlinePlayers();

        for (Player player : onlinePlayers) {
            if (player.hasPermission(SocialSpy.SOCIAL_SPY_PERMISSION)) {
                player.sendMessage(ColorHelper.yellow("[").reset(senderName).yellow("] -> [").reset(targetName).yellow("]: ").reset(body).toString());
            }
        }
    }
}
