package com.poniarcade.classesng.compat;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.Ability;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.logging.Logger;

public class WorldGuardHook {
    private static final Logger LOGGER = Logger.getLogger("WorldGuardHook");

    // This flag takes a comma separated list of abilities. use "all" to block all abilities
    private static final SetFlag<String> ABILITY_FLAG = new SetFlag<>("blocked-abilities", RegionGroup.ALL, new StringFlag(null));
    private boolean hookingWorldguard;

    public WorldGuardHook() {
        if (PoniArcade_ClassesNG.instance().getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
	        this.hookingWorldguard = true;
            WorldGuard.getInstance().getFlagRegistry().register(WorldGuardHook.ABILITY_FLAG);
        }
    }

    public boolean hasAbilityInCurrentRegion(Player player, Ability ability) {
        if (!this.hookingWorldguard) {
            return true;
        }

        WorldGuard worldGuard = WorldGuard.getInstance();

        RegionManager regionManager = worldGuard.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
        boolean abilityBlocked = false;
        if (regionManager != null) {
            ApplicableRegionSet regions = regionManager.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()).toVector().toBlockPoint());
            Set<String> blockedAbilities = regions.queryValue(WorldGuardPlugin.inst().wrapPlayer(player), WorldGuardHook.ABILITY_FLAG);
            if (blockedAbilities != null) {
                for (String blockedability : blockedAbilities) {
                    if (blockedability.equalsIgnoreCase(ability.getRootAbilityClass().getName().substring(7) /* strlen("Ability") == 7 */) || blockedability.equalsIgnoreCase("all")) {
                        abilityBlocked = true;
                        break;
                    }
                }
            }
        }

        return !abilityBlocked;
    }
}
