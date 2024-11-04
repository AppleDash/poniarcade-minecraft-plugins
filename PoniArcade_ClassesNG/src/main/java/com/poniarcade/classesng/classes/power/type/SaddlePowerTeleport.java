package com.poniarcade.classesng.classes.power.type;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.power.SaddlePower;
import com.poniarcade.core.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Objects;
import java.util.logging.Level;

/**
 * Created by appledash on 7/28/16.
 * Blackjack is still best pony.
 */
public class SaddlePowerTeleport extends SaddlePower {
    private final float foodMultiplier;

    public SaddlePowerTeleport(float foodMultiplier) {
        super("Teleport", "Teleport a short distance away in the direction you are looking.");
        this.foodMultiplier = foodMultiplier;
    }

    public SaddlePowerTeleport() {
        this(1.0f);
    }

    @Override
    public void activateSecondary(Player player, PlayerInteractEvent evt) {
        /* They should have enough food, but only if they are in Survival/Adventure. */
        if (player.getGameMode() != GameMode.CREATIVE && player.getFoodLevel() <= 10) {
            return;
        }

        Location originalLocation = player.getLocation();
        World world = player.getWorld();

        /*
         *            Warning
         *
         *   The clean code end here ^
         *   please don't scroll any down
         */
        try {
            Location targetLocation = player.getTargetBlock(Utils.getTransparentBlocks(), 55).getLocation();

            if (targetLocation.distance(originalLocation) >= 50.0) {
                return;
            }

            targetLocation.setPitch(player.getLocation().getPitch());
            targetLocation.setYaw(player.getLocation().getYaw());

            while (targetLocation.getBlock().getType() == Material.AIR) {
                targetLocation.setY(targetLocation.getY() - 1);
                if (targetLocation.getY() < world.getMinHeight()) { // Ensures no server lock due to endless loop if there is no floor.
                    return;
                }
            }

            boolean bedrockCeiling = false;

            while (!Utils.canPlayerStandAt(targetLocation)) {
                if (world.getBlockAt(targetLocation.getBlockX(), targetLocation.getBlockY() + 2, targetLocation.getBlockZ()).getType() == Material.BEDROCK) {
                    bedrockCeiling = true;
                    break;
                }

                targetLocation.setY(targetLocation.getY() + 1);
            }

            Block targetStandingBlock = targetLocation.getBlock().getRelative(BlockFace.DOWN, 1);


            if (bedrockCeiling || (targetStandingBlock.getType() == Material.LAVA) || (targetStandingBlock.getType() == Material.BEDROCK)) {
                player.sendMessage(ChatColor.AQUA + "You cannot teleport to that location.");
                return;
            }

            player.setVelocity(player.getVelocity().setY(0));

            if (!Utils.isVanished(player)) {
                this.teleportEffect(player.getLocation());
                player.teleport(targetLocation, TeleportCause.PLUGIN);
                player.setFallDistance(0);
                this.teleportEffect(player.getLocation());
            }

            float distanceTravelled = (int) originalLocation.distance(targetLocation);

            if (player.getGameMode() != GameMode.CREATIVE) {
                float foodPenalty;
                if (distanceTravelled < 20) {
                    foodPenalty = 2;
                } else if (distanceTravelled > 50) {
                    foodPenalty = 10;
                } else {
                    foodPenalty = distanceTravelled / 10;
                }

                foodPenalty *= this.foodMultiplier;
                foodPenalty = (float) Math.ceil(foodPenalty);

                player.setFoodLevel(player.getFoodLevel() - (int) foodPenalty);
            }
        } catch (Exception ex) {
            PoniArcade_ClassesNG.instance().getLogger().log(Level.WARNING, "Exception occurred with SaddlePowerTeleport", ex);
        }
    }

    private void teleportEffect(Location location) {
        World world = Objects.requireNonNull(location.getWorld());

        world.playEffect(location, Effect.PORTAL_TRAVEL, 30);
        world.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
    }
}
