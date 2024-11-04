package com.poniarcade.messaging;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.poniarcade.core.PoniArcadePlugin;
import com.poniarcade.core.handlers.CommandHandler;
import com.poniarcade.messaging.commands.BroadcastCommand;
import com.poniarcade.messaging.commands.MessageCommand;
import com.poniarcade.messaging.commands.NickCommand;
import com.poniarcade.messaging.commands.RealnameCommand;
import com.poniarcade.messaging.commands.ReplyCommand;
import com.poniarcade.messaging.listeners.ChatListener;
import com.poniarcade.messaging.listeners.JoinListener;
import com.poniarcade.messaging.manager.NicknameManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by appledash on 7/1/17.
 * Blackjack is best pony.
 */
public class PoniArcade_Messaging extends PoniArcadePlugin {
    public static final String PARC_MESSAGING_CHANNEL = "poniarcade:parcmessaging";
    private SocialSpy socialSpy;
    private NicknameManager nicknameManager;
    private final Map<UUID, UUID> lastMessageTargets = new HashMap<>();

    @Override
    public void onEnable() {
        this.socialSpy = new SocialSpy(this);
        this.nicknameManager = new NicknameManager();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, PoniArcade_Messaging.PARC_MESSAGING_CHANNEL);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, PoniArcade_Messaging.PARC_MESSAGING_CHANNEL, (pluginChannel, player, bytes) -> {
            ByteArrayDataInput badi = ByteStreams.newDataInput(bytes);
            String eventTag = badi.readUTF();

            if (eventTag.equals("socialspy")) {
                String senderName = badi.readUTF();
                String targetName = badi.readUTF();
                String body = badi.readUTF();
                this.socialSpy.printSpyMessage(senderName, targetName, body);
            }
            if (eventTag.equals("broadcast")) {
                this.getServer().broadcastMessage(badi.readUTF());
            }

        });
        this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, this.nicknameManager::reloadNicknames, 0, 20 * 10);
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
        CommandHandler.addCommands(
            this,
            new BroadcastCommand(this),
            new MessageCommand(this),
            new NickCommand(this),
            new RealnameCommand(this),
            new ReplyCommand(this)
        );
    }

    public NicknameManager getNicknameManager() {
        return this.nicknameManager;
    }

    public SocialSpy getSocialSpy() {
        return this.socialSpy;
    }

    public void putLastTarget(UUID sender, UUID target) {
        this.lastMessageTargets.put(sender, target);
    }

    public UUID getReplyCandidate(UUID sender) {
        return this.lastMessageTargets.get(sender);
    }
}
