package com.poniarcade.classesng.classes;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.ability.Ability;
import com.poniarcade.classesng.classes.power.SaddlePower;
import com.poniarcade.classesng.classes.storage.ClassStorage;
import com.poniarcade.classesng.classes.type.ClassBatpony;
import com.poniarcade.classesng.classes.type.ClassChangeling;
import com.poniarcade.classesng.classes.type.ClassDragon;
import com.poniarcade.classesng.classes.type.ClassEarthPony;
import com.poniarcade.classesng.classes.type.ClassGryphon;
import com.poniarcade.classesng.classes.type.ClassPegasus;
import com.poniarcade.classesng.classes.type.ClassSeapony;
import com.poniarcade.classesng.classes.type.ClassUnicorn;
import com.poniarcade.classesng.classes.type.ClassZebra;
import com.poniarcade.classesng.classes.type.master.ClassAbyssalSeapony;
import com.poniarcade.classesng.classes.type.master.ClassAlicorn;
import com.poniarcade.classesng.classes.type.master.ClassArchUnicorn;
import com.poniarcade.classesng.classes.type.master.ClassDraconequus;
import com.poniarcade.classesng.classes.type.master.ClassElderDragon;
import com.poniarcade.classesng.classes.type.master.ClassEliteChangeling;
import com.poniarcade.classesng.classes.type.master.ClassGrandEarth;
import com.poniarcade.classesng.classes.type.master.ClassKnightBatpony;
import com.poniarcade.classesng.classes.type.master.ClassMasterPegasus;
import com.poniarcade.classesng.classes.type.master.ClassTribalZebra;
import com.poniarcade.classesng.classes.type.master.ClassVeteranGryphon;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by appledash on 7/18/16.
 * Blackjack is still best pony.
 */
public class ClassManager {
    private final ClassStorage classStorage;
    private final List<Class> registeredClasses;
    private final PoniArcade_ClassesNG plugin;
    private static final Logger LOGGER = Logger.getLogger("PoniArcade_ClassesNG");

    public ClassManager(PoniArcade_ClassesNG plugin) {
        this.plugin = plugin;
        this.classStorage = new ClassStorage(this);

        /* Load classes */
        this.registeredClasses = ImmutableList.<Class>builder()
            .add(new ClassAbyssalSeapony(), new ClassAlicorn(),
                new ClassArchUnicorn(), new ClassDraconequus(),
                new ClassElderDragon(), new ClassEliteChangeling(),
                new ClassGrandEarth(), new ClassKnightBatpony(),
                new ClassMasterPegasus(), new ClassTribalZebra(),
                new ClassVeteranGryphon(),
                new ClassBatpony(), new ClassChangeling(),
                new ClassDragon(), new ClassEarthPony(),
                new ClassGryphon(), new ClassPegasus(),
                new ClassSeapony(), new ClassUnicorn(),
                new ClassZebra()
            ).build();

        ClassManager.LOGGER.info("Loaded " + this.registeredClasses.size() + " classes.");
        Set<Recipe> registeredRecipes = new HashSet<>();
        int recipeCounter = 0;
        for (Class clazz : this.registeredClasses) {
            for (Recipe recipe : clazz.getRecipes()) {
                if (!registeredRecipes.contains(recipe)) {
                    registeredRecipes.add(recipe);
                    plugin.getServer().addRecipe(recipe);
                    recipeCounter++;
                }
            }
        }
        plugin.getLogger().info("Registered " + recipeCounter + " class recipes.");
        this.setupTasks();
    }

    private void setupTasks() {
        /* Reload player -> class mappings every so often from the DB */
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, this.classStorage::syncPlayerData, 0, 20 * 100); // Every 100 seconds

        /* Clear pending master class purchases from the cache when they time out */
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            this.classStorage.getAllClassDatas().forEach(this::ensureExpiries);
        }, 0, 20 * 60); // Every minute

        /* Clear active saddle powers when they time out */
        Bukkit.getServer().getScheduler().runTaskTimer(this.plugin, () -> {
            this.classStorage.getAllClassDatas().forEach((uuid, classData) -> {
                if (classData.isSaddlePowerExpired()) {
                    classData.getActiveSaddlePower().deactivateSaddlePower(Bukkit.getServer().getPlayer(uuid));
                    classData.deactivateSaddlePower();
                }
            });
        }, 0, 10); // Twice per second
    }

    public void ensureExpiries(UUID uuid, PlayerClassData classData) {
        if ((classData.getPendingPurchase() != null) && (classData.getTimeUntilPendingPurchase() <= 0)) {
            classData.removePendingPurchase();
        }

        if (classData.getSpoofedClassExpiry() <= System.currentTimeMillis()) {
            Optional<Class> spoofedClass = classData.getSpoofedClass();

            if (spoofedClass.isPresent()) {
                classData.removeSpoofedClass();
                Player player = Bukkit.getPlayer(uuid);

                if (player != null) {
                    player.sendMessage(ColorHelper.aqua("Your time as ").append(spoofedClass.get().getInflectedColoredName()).aqua(" is up! You're now a ").append(classData.getPlayerClass().get().getInflectedColoredName()).aqua(" again.").toString());
                }
            }
        }
    }

    /**
     * Get a list of the names of all classes.
     * @param type The Class type to get the class names of, or _ANY for all.
     * @return List of classes
     */
    public List<String> getAllClassNames(ClassType type) {
        return this.getAllClasses(type).stream().map(Class::getFriendlyName).collect(Collectors.toList());
    }

    /**
     * Get all the Classes with the given type
     * Use _ANY for all types.
     * @param type Class type
     * @return List of Classes with the given type
     */
    public List<Class> getAllClasses(ClassType type) {
        return this.registeredClasses.stream().filter((clazz) -> (type == ClassType._ANY) || (clazz.getType() == type)).collect(ImmutableList.toImmutableList());
    }

    /**
     * Get a PoniArcade Class by its name, case-insensitively.
     * @param className Class name. Cannot be empty.
     * @return Optional, present if Class exists
     */
    public Optional<Class> getClassByName(String className) {
        if (Strings.isNullOrEmpty(className)) {
            throw new IllegalArgumentException("Class name cannot be empty.");
        }

        for (Class clazz : this.registeredClasses) {
            if (clazz.getSanitizedName().equalsIgnoreCase(className.replace(" ", ""))) {
                return Optional.of(clazz);
            }
        }

        return Optional.empty();
    }

    /**
     * Get the "effective" Class object for a Player, that is - the class whose abilities should be applied.
     * This may not be the Player's real Class.
     * @param player Player
     * @return Optional, present if Player has a Class
     */
    public Optional<Class> getEffectiveClassForPlayer(Player player) {
        PlayerClassData data = this.getClassData(player);

        if (data.getSpoofedClass().isPresent()) {
            return data.getSpoofedClass();
        }

        return data.getPlayerClass();
    }

    /**
     * Get the real Class object for a player, if they have one.
     * @param player Player to get the Class for
     * @return Optional, present if Player has a Class
     */
    public Optional<Class> getRealClassForPlayer(Player player) {
        return this.classStorage.getClassData(player).getPlayerClass();
    }

    /**
     * Check if a Player has a Class, and that Class has the given Ability.
     * @param player Player to check
     * @param ability Ability class to check
     * @return True if their effective class has the ability, false otherwise.
     */
    public boolean hasEffectiveAbility(Player player, java.lang.Class<? extends Ability> ability) {
        return this.getEffectiveAbility(player, ability).isPresent();
    }

    /**
     * Get the effective Ability of a given type for a Player.
     * @param player Player to check
     * @param abilityClass Ability class to check
     * @param <T> T
     * @return Ability optional, present if they have a Class and their effective Class has the given Ability.
     */
    public <T extends Ability> Optional<T> getEffectiveAbility(Player player, java.lang.Class<T> abilityClass) {
        Optional<Class> playerClassOptional = this.getEffectiveClassForPlayer(player);

        if (playerClassOptional.isEmpty()) {
            return Optional.empty();
        }

        Class playerClass = playerClassOptional.get();
        Optional<T> abilityOptional = playerClass.getAbility(abilityClass);

        if (abilityOptional.isEmpty()) {
            return abilityOptional;
        }

        if (!this.plugin.getWorldGuardHook().hasAbilityInCurrentRegion(player, abilityOptional.get())) {
            return Optional.empty();
        }

        return abilityOptional;
    }

    /**
     * Get the effective SaddlePower for a Player.
     * @param player Player.
     * @return Optional, present if Player has Class and SaddlePower, empty otherwise.
     */
    public Optional<SaddlePower> getEffectiveSaddlePower(Player player) {
        Optional<Class> playerClass = this.getEffectiveClassForPlayer(player);

        return playerClass.isPresent() ? playerClass.get().getSaddlePower() : Optional.empty();
    }

    /**
     * Set a player's class
     * @param player Player
     * @param clazz Class
     */
    public void setClassForPlayer(Player player, Class clazz) {
        Optional<Class> previousClass = this.getRealClassForPlayer(player);
        this.classStorage.getClassData(player).setPlayerClass(clazz);
        this.plugin.getServer().getPluginManager().callEvent(new PlayerClassChangeEvent(player, previousClass.orElse(null), clazz));
    }

    /**
     * Add a pending class purchase for a player.
     * @param player Player to add pending purchase for
     * @param clazz Class they are pending purchase for
     */
    public void addPendingClassPurchase(Player player, Class clazz) {
        this.classStorage.getClassData(player).setPendingPurchase(clazz, System.currentTimeMillis() + (300 * 1000));
    }

    /**
     * Check if a player has a pending class purchase, and return it if necessary/
     * @param player Player to check
     * @return Class they are trying to purchase if present, Optional.empty() otherwise
     */
    public Optional<Class> getPendingClassPurchase(Player player) {
        return Optional.ofNullable(this.classStorage.getClassData(player).getPendingPurchase());
    }

    /**
     * Remove a player's pending Class purchase
     * @param player Player to remove confirmation for
     */
    public void removePendingClassPurchase(Player player) {
        this.classStorage.getClassData(player).removePendingPurchase();
    }

    /**
     * Get the remaining cooldown before a player can change classes.
     * @param player Player to check.
     * @return Their cooldown remaining, or 0 if expired.
     */
    public long getRemainingCooldown(OfflinePlayer player) {
        PlayerClassData classData = this.classStorage.getClassData(player);

        long until = classData.getNextClassChange() - System.currentTimeMillis();

        if (until <= 0) {
            classData.removeNextClassChange();
            return 0;
        }

        return until;
    }

    /**
     * Set the next class change time for a player to a given time.
     * @param player Player
     * @param nextClassChange UNIX time to set cooldown to
     */
    public void setNextClassChange(Player player, long nextClassChange) {
        this.classStorage.getClassData(player).setNextClassChange(nextClassChange);
    }

    /**
     * Set a saddle power active on a player.
     * @param player Player
     * @param saddlePower Saddle power type
     * @param duration Duration in milliseconds to activate saddle power
     */
    public void setSaddlePowerActive(Player player, SaddlePower saddlePower, long duration) {
        if (!saddlePower.canActivate()) {
            throw new IllegalArgumentException("Tried to activate a SaddlePower that cannot be active!");
        }

        if (this.isSaddlePowerActive(player)) {
            throw new IllegalStateException("Cannot activate a saddle power when we already have one active!");
        }

        Optional<Class> classOptional = this.getEffectiveClassForPlayer(player);

        if (classOptional.isEmpty() || classOptional.get().getSaddlePower().isEmpty()) {
            throw new IllegalStateException("Cannot set a SaddlePower active for a player that doesn't have a Class or SaddlePower!");
        }

        if (classOptional.get().getSaddlePower().get() != saddlePower) {
            throw new IllegalArgumentException("Cannot activate a SaddlePower for a player that doesn't have it!");
        }

        this.classStorage.getClassData(player).setActiveSaddlePower(saddlePower, System.currentTimeMillis() + duration);
    }

    /**
     * Check if there is currently a saddle power active for the given player.
     * @param player Player
     * @return True if active saddle power, false otherwise
     */
    public boolean isSaddlePowerActive(Player player) {
        return this.classStorage.getClassData(player).isSaddlePowerActive();
    }

    /**
     * Deactivate a player's currently activated saddle power.
     * Does nothing if there is none currently activated.
     * @param player Player to deactivate saddle power for
     */
    public void deactivateSaddlePower(Player player) {
        PlayerClassData classData = this.classStorage.getClassData(player);

        if (classData.isSaddlePowerActive()) {
            classData.getActiveSaddlePower().deactivateSaddlePower(player);
            classData.deactivateSaddlePower();
        }
    }

    public void removeClassForPlayer(Player player) {
        PlayerClassData classData = this.classStorage.getClassData(player);

        if (classData.getPlayerClass().isPresent()) {
            classData.removePlayerClass();
        }
    }

    public ClassStorage getClassStorage() {
        return this.classStorage;
    }

    /**
     * Get the ClassData for a given Player
     * @param player Player
     * @return ClassData, will never be null
     */
    public PlayerClassData getClassData(Player player) {
        return this.classStorage.getClassData(player);
    }

    /**
     * Check if a given Recipe is added by a Class.
     * @param recipe Recipe to check
     * @return True if Recipe is added by a Class, false otherwise.
     */
    public boolean isClassRecipe(Recipe recipe) {
        for (Class clazz : this.registeredClasses) {
            if (clazz.hasRecipe(recipe)) {
                return true;
            }
        }

        return false;
    }
}
