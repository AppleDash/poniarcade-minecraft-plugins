package com.poniarcade.core;

import com.poniarcade.core.commands.CommandHelpGenerator2;
import com.poniarcade.core.commands.type.BackCommand;
import com.poniarcade.core.commands.type.BookCommand;
import com.poniarcade.core.commands.type.CinematicFlightCommand;
import com.poniarcade.core.commands.type.ClearInventoryCommand;
import com.poniarcade.core.commands.type.CommandBlockCommand;
import com.poniarcade.core.commands.type.CommandSpyCommand;
import com.poniarcade.core.commands.type.EnchantCommand;
import com.poniarcade.core.commands.type.EnderChestCommand;
import com.poniarcade.core.commands.type.GameModeCommand;
import com.poniarcade.core.commands.type.GiveCommand;
import com.poniarcade.core.commands.type.GodModeCommand;
import com.poniarcade.core.commands.type.HatCommand;
import com.poniarcade.core.commands.type.HealCommand;
import com.poniarcade.core.commands.type.InventorySeeCommand;
import com.poniarcade.core.commands.type.ItemInfoCommand;
import com.poniarcade.core.commands.type.ItemLoreCommand;
import com.poniarcade.core.commands.type.ListCommand;
import com.poniarcade.core.commands.type.MailCommand;
import com.poniarcade.core.commands.type.MobCommand;
import com.poniarcade.core.commands.type.MoreCommand;
import com.poniarcade.core.commands.type.NearCommand;
import com.poniarcade.core.commands.type.PowertoolCommand;
import com.poniarcade.core.commands.type.RepairCommand;
import com.poniarcade.core.commands.type.SeenCommand;
import com.poniarcade.core.commands.type.SpawnerCommand;
import com.poniarcade.core.commands.type.SpeedCommand;
import com.poniarcade.core.commands.type.StatusCommand;
import com.poniarcade.core.commands.type.SudoCommand;
import com.poniarcade.core.commands.type.SuggestCommand;
import com.poniarcade.core.commands.type.TeleportAcceptCommand;
import com.poniarcade.core.commands.type.TeleportCommand;
import com.poniarcade.core.commands.type.TeleportDenyCommand;
import com.poniarcade.core.commands.type.TeleportHereCommand;
import com.poniarcade.core.commands.type.TeleportRequestCommand;
import com.poniarcade.core.commands.type.TopCommand;
import com.poniarcade.core.commands.type.WeatherCommand;
import com.poniarcade.core.commands.type.WhitelistCommand;
import com.poniarcade.core.commands.type.WhoisCommand;
import com.poniarcade.core.commands.type.WorkbenchCommand;
import com.poniarcade.core.handlers.*;
import com.poniarcade.core.listeners.*;
import com.poniarcade.core.structs.Reloadable;
import com.poniarcade.core.utils.ItemDatabase;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PoniArcade_Core extends PoniArcadePlugin {
    private static PoniArcade_Core INSTANCE;
    private boolean whitelistEnabled;
    private final PlayerFlags playerFlags = new PlayerFlags(); // TODO: Move this to some kind of player manager?

    private SuggestionHandler suggestionHandler = new SuggestionHandler();
    private final CommandHelpGenerator2 commandHelpGenerator = new CommandHelpGenerator2();
    private final List<Reloadable> reloadables = new ArrayList<>();
    private final PlayerManager playerManager = new PlayerManager();
    private final MailManager mailManager = new MailManager();

    @Override
    public void onEnable() {
        PoniArcade_Core.INSTANCE = this;

        ItemDatabase.initItemDB();
        CommandHandler.addCommands(
            this,
            new BackCommand(this),
            new BookCommand(this),
            new CinematicFlightCommand(this),
            new ClearInventoryCommand(this),
            new CommandBlockCommand(this),
            new CommandSpyCommand(this),
            new EnchantCommand(this),
            new EnderChestCommand(this),
            new GameModeCommand(this),
            new GiveCommand(this),
            new GodModeCommand(this),
            new HatCommand(this),
            new HealCommand(this),
            new InventorySeeCommand(this),
            new ItemInfoCommand(this),
            new ItemLoreCommand(this),
            new ListCommand(this),
            new MailCommand(this),
            new MobCommand(this),
            new MoreCommand(this),
            new NearCommand(this),
            new PowertoolCommand(this),
            new RepairCommand(this),
            new SeenCommand(this),
            new SpawnerCommand(this),
            new SpeedCommand(this),
            new StatusCommand(this),
            new SudoCommand(this),
            new SuggestCommand(this),
            new TeleportAcceptCommand(this),
            new TeleportCommand(this),
            new TeleportDenyCommand(this),
            new TeleportHereCommand(this),
            new TeleportRequestCommand(this),
            new TopCommand(this),
            new WeatherCommand(this),
            new WhitelistCommand(this),
            new WhoisCommand(this),
            new WorkbenchCommand(this)
        );

        // PacketListener packetListener = new PacketListener();
        // ProtocolLibrary.getProtocolManager().addPacketListener(packetListener);

        this.registerEvents(
            new ChatListener(this),
            new FlightListener(),
            new JoinQuitListener(this),
            new MobHatListener(),
            new PlayerListener(this),
            // new SpawnListener(),
            new SuggestionBookListener(this),
            new TeleportListener(this),
            new WhitelistListener()
        );


        // Load whitelist status
        File configFile = new File(this.getDataFolder(), "whitelist.yaml");
        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(configFile);
        this.whitelistEnabled = yamlConfig.getBoolean("enabled", false);


        this.getServer().addRecipe(
            new ShapedRecipe(
                new NamespacedKey(this, "nametag"),
                new ItemStack(Material.NAME_TAG, 1)
            )
            .shape("   ", "spp", "   ")
            .setIngredient('s', Material.STRING)
            .setIngredient('p', Material.PAPER)
        );

        this.suggestionHandler = new SuggestionHandler();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "poniarcade:parcmessaging");

        this.reloadables.add(this.playerManager);
        this.reloadables.add(this.suggestionHandler);
        this.reloadables.add(() -> {
            this.getServer().getScheduler().runTask(this, () -> {
                this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex reload");
            });
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                PoniArcade_Core.this.reloadables.forEach(Reloadable::reloadData);
            }
        } .runTaskTimerAsynchronously(this, 0, 20 * 30);
    }


    public static PoniArcade_Core getInstance() {
        return PoniArcade_Core.INSTANCE;
    }

    public String getServerName() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("server.properties", StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty("server-name");
    }

    public void setWhitelistEnabled(boolean enabled) {
        this.whitelistEnabled = enabled;

        File configFile = new File(this.getDataFolder(), "whitelist.yaml");
        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(configFile);
        yamlConfig.set("enabled", enabled);

        try {
            yamlConfig.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save whitelist config!", e);
        }
    }

    public boolean isWhitelistEnabled() {
        return this.whitelistEnabled;
    }

    public PlayerFlags getPlayerFlags() {
        return this.playerFlags;
    }

    public SuggestionHandler getSuggestionHandler() {
        return this.suggestionHandler;
    }

    public CommandHelpGenerator2 getCommandHelpGenerator() {
        return this.commandHelpGenerator;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public MailManager getMailManager() {
        return this.mailManager;
    }

    public void addReloadable(Reloadable reloadable) {
        this.reloadables.add(reloadable);
    }
}
