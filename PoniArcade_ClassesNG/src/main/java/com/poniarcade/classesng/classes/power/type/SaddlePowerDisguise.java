package com.poniarcade.classesng.classes.power.type;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.power.SaddlePower;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.Utils;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.*;

/**
 * Created by appledash on 8/5/16.
 * Blackjack is still best pony.
 */
public class SaddlePowerDisguise extends SaddlePower {
    private float exhaustionMultiplier = 1.0f;

    public SaddlePowerDisguise() {
        super("Disguise", "Allows you to disguise yourself as a mob, using food energy to maintain the disguise.");
        this.setActivatable();
    }

    public SaddlePowerDisguise(float exhaustionMultiplier) {
        this();
        this.exhaustionMultiplier = exhaustionMultiplier;
    }

    @Override
    public boolean activatePrimaryAtEntity(Player player, Entity rightClicked) {
        if (rightClicked instanceof Player) {
            if (PoniArcade_ClassesNG.instance().getClassManager().isSaddlePowerActive(player)) {
                PoniArcade_ClassesNG.instance().getClassManager().deactivateSaddlePower(player);
                return true;
            }
        } else if (rightClicked instanceof LivingEntity) {
            if (player.getFoodLevel() < 10) {
                return false;
            }

            if (PoniArcade_ClassesNG.instance().getClassManager().isSaddlePowerActive(player)) {
                PoniArcade_ClassesNG.instance().getClassManager().deactivateSaddlePower(player);
            }

            if (rightClicked instanceof Pig pig) {
                // Note: Cannot disguise as a pig if they have a saddle
                if (!player.isSneaking() && !pig.hasSaddle()) {
                    // Disguise as pig, remove old pig and spawn new one to fix visual glitch
                    pig.getWorld().spawnEntity(pig.getLocation(), EntityType.PIG);
                    pig.remove();
                    player.updateInventory(); // Fix for visual inventory glitch (Blame Bukkit)
                    DisguiseAPI.disguiseToAll(player, new MobDisguise(DisguiseType.getType(rightClicked.getType()), true));
                    player.sendMessage(ColorHelper.aqua("You are now disguised as a ").gold("%s", rightClicked.getType().name()).aqua(".").toString());
                    this.changelingEffect(player);
                }

                return true;
            }

            DisguiseAPI.disguiseToAll(player, new MobDisguise(DisguiseType.getType(rightClicked.getType()), true));
            player.sendMessage(ColorHelper.aqua("You are now disguised as a ").gold("%s", rightClicked.getType().name()).aqua(".").toString());
            this.changelingEffect(player);
            PoniArcade_ClassesNG.instance().getClassManager().setSaddlePowerActive(player, this, Integer.MAX_VALUE);
            return true;
        }

        return false;
    }

    @Override
    public void activatePrimary(Player player) {
        if (PoniArcade_ClassesNG.instance().getClassManager().isSaddlePowerActive(player)) {
            PoniArcade_ClassesNG.instance().getClassManager().deactivateSaddlePower(player);
        }
    }

    @Override
    public void doSaddlePowerOnMove(Player player) {
        if ((player.getFoodLevel() < 10) || Utils.hasNegativePotionEffect(player)) {
            PoniArcade_ClassesNG.instance().getClassManager().deactivateSaddlePower(player);
        } else if (player.getGameMode() != GameMode.CREATIVE) {
            player.setExhaustion(player.getExhaustion() + (0.1f * this.exhaustionMultiplier));
        }
    }

    @Override
    public void deactivateSaddlePower(Player player) {
        DisguiseAPI.undisguiseToAll(player);
        player.sendMessage(ColorHelper.aqua("You are no longer disguised!").toString());
        this.changelingEffect(player);
    }

    private void changelingEffect(Player player) {
        if (!player.hasMetadata("vanished") || !player.getMetadata("vanished").get(0).asBoolean()) {
            /*
            player.getWorld().playEffect(player.getEyeLocation(), Effect.HAPPY_VILLAGER, 30);
            player.getWorld().playEffect(player.getEyeLocation().subtract(0, 0.5, 0), Effect.HAPPY_VILLAGER, 30);
            player.getWorld().playEffect(player.getEyeLocation().subtract(1, 0.5, 0), Effect.HAPPY_VILLAGER, 30);
            player.getWorld().playEffect(player.getEyeLocation().subtract(-1, 0.5, 0), Effect.HAPPY_VILLAGER, 30);
            player.getWorld().playEffect(player.getEyeLocation().subtract(0, 0.5, 1), Effect.HAPPY_VILLAGER, 30);
            player.getWorld().playEffect(player.getEyeLocation().subtract(0, 0.5, -1), Effect.HAPPY_VILLAGER, 30);
            player.getWorld().playEffect(player.getEyeLocation(), Effect.HAPPY_VILLAGER, 30);
            TODO WTF
               */
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SILVERFISH_HURT, 1, 1);
        }
    }
}
