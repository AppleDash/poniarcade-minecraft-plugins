package com.poniarcade.punishmentsng;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.poniarcade.core.PoniArcadePlugin;
import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.handlers.CommandHandler;
import com.poniarcade.punishmentsng.command.BanCommand;
import com.poniarcade.punishmentsng.command.FreezeCommand;
import com.poniarcade.punishmentsng.command.KickCommand;
import com.poniarcade.punishmentsng.command.MuteCommand;
import com.poniarcade.punishmentsng.command.UnbanCommand;
import com.poniarcade.punishmentsng.command.UnfreezeCommand;
import com.poniarcade.punishmentsng.command.UnmuteCommand;
import com.poniarcade.punishmentsng.listeners.PlayerChatListener;
import com.poniarcade.punishmentsng.listeners.PlayerLoginListener;
import com.poniarcade.punishmentsng.listeners.PlayerMovementListener;
import com.poniarcade.punishmentsng.punishments.PunishmentManager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by appledash on 7/23/17.
 * Blackjack is best pony.
 */
public class PoniArcade_PunishmentsNG extends PoniArcadePlugin {
    private final PunishmentManager punishmentManager = new PunishmentManager(this);
    private final AtomicBoolean joinLock = new AtomicBoolean(false);

    @Override
    public void onEnable() {
        CommandHandler.addCommands(
            this,
            new BanCommand(this),
            new FreezeCommand(this),
            new KickCommand(this),
            new MuteCommand(this),
            new UnbanCommand(this),
            new UnfreezeCommand(this),
            new UnmuteCommand(this)
        );

        this.registerEvents(
            new PlayerLoginListener(this),
            new PlayerChatListener(this),
            new PlayerMovementListener(this)
        );

        this.punishmentManager.reloadData();

        /* make sure that PoniArcade_Core knows about us so the data cen be reloaded once in awhile */
        PoniArcade_Core.getInstance().addReloadable(this.punishmentManager);

        // TODO: WTF is the new name of this channel?
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", (channel, player, data) -> {
            if (!channel.equals("BungeeCord")) {
                return;
            }

            ByteArrayDataInput badi = ByteStreams.newDataInput(data);

            if (!badi.readUTF().equals("PArcPunishments")) {
                return;
            }

            String tag = badi.readUTF();

            if (tag.equals("reload")) {
                if (this.joinLock.get()) {
                    this.getLogger().warning("Tried to reload punishments data when we were already reloading - that should not happen.");
                    return;
                }

                this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
                    this.punishmentManager.reloadData();
                    this.joinLock.set(false);
                });
            }
        });

    }

    public PunishmentManager getPunishmentManager() {
        return this.punishmentManager;
    }

    public AtomicBoolean getJoinLock() {
        return this.joinLock;
    }
}
