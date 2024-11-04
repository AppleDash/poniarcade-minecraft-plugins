package com.poniarcade.classesng;

import com.poniarcade.classesng.classes.ClassManager;
import com.poniarcade.classesng.classes.ClassRecipe;
import com.poniarcade.classesng.commands.ClassCommand;
import com.poniarcade.classesng.listeners.ClassListener;
import com.poniarcade.classesng.listeners.CraftListener;
import com.poniarcade.classesng.listeners.InteractListener;
import com.poniarcade.classesng.listeners.JoinQuitListener;
import com.poniarcade.classesng.listeners.abilities.*;
import com.poniarcade.classesng.listeners.powers.SaddlePowerActivateListener;
import com.poniarcade.classesng.listeners.powers.SaddlePowerMoveListener;
import com.poniarcade.core.PoniArcadePlugin;
import com.poniarcade.core.handlers.CommandHandler;
import com.poniarcade.classesng.compat.WorldGuardHook;
import org.appledash.saneeconomy.ISaneEconomy;
import org.appledash.saneeconomy.economy.EconomyManager;

/**
 * Created by appledash on 7/18/16.
 * Blackjack is still best pony.
 *
 * The next-generation classes plugin for PoniArcade.
 */
public class PoniArcade_ClassesNG extends PoniArcadePlugin {
    private static PoniArcade_ClassesNG instance;

    private ClassManager classManager;
    private WorldGuardHook worldGuardHook;
    private ISaneEconomy saneEconomy;

    public PoniArcade_ClassesNG() {
        PoniArcade_ClassesNG.instance = this;
    }

    @Override
    public void onLoad() {
	    this.worldGuardHook = new WorldGuardHook();
    }

    @Override
    public void onEnable() {
        ClassRecipe.init(this);
        this.saneEconomy = (ISaneEconomy) this.getServer().getPluginManager().getPlugin("SaneEconomy");

        this.registerEvents(
            new ClassListener(),
            new CraftListener(this),
            new InteractListener(this),
            new JoinQuitListener(this)
        );

        this.registerEvents(
            new SaddlePowerActivateListener(this),
            new SaddlePowerMoveListener(this)
        );

        this.registerEvents(
            new AbilityArrowSplashListener(this),
            new AbilityDamageReductionListener(this),
            new AbilityDisguiseListener(this),
            new AbilityFlightListener(this),
            new AbilityFoodStealListener(this),
            new AbilityIncreaseMeleeDamageListener(this),
            new AbilityIncreaseRangedDamageListener(this),
            new AbilityIncreaseXPListener(this),
            new AbilityLifeStealListener(this),
            new AbilityMovementSpeedListener(this),
            new AbilityNightCriticalListener(this),
            new AbilityNoNaturalRegenListener(this),
            new AbilitySetUnarmedDamageListener(this),
            new AbilitySnowballDamageListener(this),
            new AbilitySwimListener(this),
            new AbilityThornsListener(this),
            new AbilityUnarmedAreaOfEffectListener(this),
            new FireDamageListener(this)
        );

        CommandHandler.addCommands(this, new ClassCommand(this));
        this.classManager = new ClassManager(this);
    }

    @Override
    public void onDisable() {
	    this.classManager.getClassStorage().syncPlayerData();
    }

    /**
     * Get the active ClassManager. Manages Classes for players.
     * @return Active ClassManager
     */
    public ClassManager getClassManager() {
        return this.classManager;
    }

    /**
     * Get the EconomyManager from our economy plugin, SaneEconomy.
     * @return SaneEconomy EconomyManager
     */
    public EconomyManager getSaneEconomyManager() {
        return this.saneEconomy.getEconomyManager();
    }

    /**
     * Get the current loaded plugin instance statically.
     * @return PoniArcade_ClassesNG instance
     */
    public static PoniArcade_ClassesNG instance() {
        return PoniArcade_ClassesNG.instance;
    }

    public WorldGuardHook getWorldGuardHook() {
        return this.worldGuardHook;
    }
}
