package com.poniarcade.classesng.classes.power;

import com.poniarcade.classesng.classes.ability.Ability;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by appledash on 7/24/16.
 * Blackjack is still best pony.
 */
public abstract class SaddlePower {
    private final String name;
    private final String description;
    private boolean canActivate;
    private java.lang.Class<? extends Ability> modifiedAbility;

    protected SaddlePower(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void activatePrimary(Player player) { }

    public void activateSecondary(Player player, PlayerInteractEvent evt) { }

    public boolean activatePrimaryAtEntity(Player player, Entity rightClicked) {
        return false;
    }

    public void deactivateSaddlePower(Player player) { }

    public void doSaddlePowerOnMove(Player player) { }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    protected void setActivatable() {
        this.canActivate = true;
    }

    public boolean canActivate() {
        return this.canActivate;
    }

    protected void setModifiesAbility(java.lang.Class<? extends Ability> ability) {
        this.modifiedAbility = ability;
    }

    public boolean modifiesAbility(java.lang.Class<? extends Ability> abilityType) {
        return this.modifiedAbility == abilityType;
    }
}
