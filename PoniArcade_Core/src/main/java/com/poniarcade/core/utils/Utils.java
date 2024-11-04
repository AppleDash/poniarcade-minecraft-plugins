package com.poniarcade.core.utils;

import com.google.common.collect.ImmutableSet;
import com.poniarcade.core.utils.nms.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Utils Class to help with logging and other various tasks
 */
public final class Utils {
    public static final Set<Material> TRANSPARENT_BLOCKS = new HashSet<>(Material.values().length);
    public static final List<Material> SOLID_BLOCKS = new ArrayList<>(Material.values().length);
    private static final Set<Material> BED_MATERIALS = ImmutableSet.<Material>builder()
            .add(Material.BLACK_BED)
            .add(Material.BLUE_BED)
            .add(Material.BROWN_BED)
            .add(Material.CYAN_BED)
            .add(Material.GRAY_BED)
            .add(Material.GREEN_BED)
            .add(Material.LIGHT_BLUE_BED)
            .add(Material.LIGHT_GRAY_BED)
            .add(Material.MAGENTA_BED)
            .add(Material.ORANGE_BED)
            .add(Material.PINK_BED)
            .add(Material.PURPLE_BED)
            .add(Material.RED_BED)
            .add(Material.WHITE_BED)
            .add(Material.YELLOW_BED)
            .build();
    private static final Tag<Material> LAVA_TAG = Bukkit.getTag("items", NamespacedKey.minecraft("lava"), Material.class);
    private static final Tag<Material> WATER_TAG = Bukkit.getTag("items", NamespacedKey.minecraft("water"), Material.class);


    static {
        for (Material material : Material.values()) {
            //noinspection deprecation
            if (material.isBlock() && material.isTransparent()) {
                Utils.TRANSPARENT_BLOCKS.add(material);
            } else if (material.isBlock() && material.isSolid()) {
                Utils.SOLID_BLOCKS.add(material);
            }
        }
    }

    private Utils() {
    }

    /**
     * Returns true if player has a negative potion effect applied.
     * Otherwise, returns false.
     */
    public static boolean hasNegativePotionEffect(Player player) {
        return player.hasPotionEffect(PotionEffectType.BLINDNESS) ||
               player.hasPotionEffect(PotionEffectType.CONFUSION) ||
               player.hasPotionEffect(PotionEffectType.HARM) ||
               player.hasPotionEffect(PotionEffectType.HUNGER) ||
               player.hasPotionEffect(PotionEffectType.POISON) ||
               player.hasPotionEffect(PotionEffectType.SLOW) ||
               player.hasPotionEffect(PotionEffectType.SLOW_DIGGING) ||
               player.hasPotionEffect(PotionEffectType.WEAKNESS);
    }

    /**
     * Check if a player is vanished with VanishNoPacket.
     * @param player Player to check.
     * @return True if vanished, false otherwise.
     */
    public static boolean isVanished(OfflinePlayer player) {
        if (!player.isOnline()) {
            return false;
        }

        Player onlinePlayer = (Player)player;

        return onlinePlayer.hasMetadata("vanished") && onlinePlayer.getMetadata("vanished").get(0).asBoolean();
    }


    public static boolean isUnsafe(Material block) {
        return Utils.LAVA_TAG.isTagged(block) ||
               block == Material.FIRE ||
            Utils.BED_MATERIALS.contains(block);
    }

    public static Set<Material> getTransparentBlocks() {
        Set<Material> blocks = EnumSet.noneOf(Material.class);
        for (Material material : Material.values()) {
            //noinspection deprecation (it's OK, it's not actually deprecated, just discouraged)
            if (material.isTransparent()) {
                blocks.add(material);
            }
        }
        return blocks;
    }

    public static boolean growGrowable(Block block) {
        return Utils.growCrop(block) || Utils.growTree(block);

    }

    public static boolean growCrop(Block block) {
        BlockState blockState = block.getState();
        BlockData materialData = blockState.getBlockData();

        if (!(materialData instanceof Ageable crops)) {
            return false;
        }


        if (crops.getAge() != crops.getMaximumAge()) {
            crops.setAge(crops.getMaximumAge());
            blockState.update(true);
            return true;
        }

        return false;
    }

    public static boolean growTree(Block block) {
        BlockState blockState = block.getState();
        BlockData materialData = blockState.getBlockData();

        if (!(materialData instanceof Sapling sapling)) {
            return false;
        }

        if (sapling.getStage() != sapling.getMaximumStage()) {
            sapling.setStage(sapling.getMaximumStage());
            blockState.update(true);
            return true;
        }

        return false;
    }

    /**
     * @param milliseconds Number of milliseconds to be converted to DHMS format.
     * @return A string with time in Days, Hours, Minutes and Seconds.
     */
    public static String millisToDHMS(long milliseconds) {
        String message = "";

        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) - (TimeUnit.HOURS.toMinutes(hours) + TimeUnit.DAYS.toMinutes(days));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - (TimeUnit.HOURS.toSeconds(hours) + TimeUnit.DAYS.toSeconds(days) + TimeUnit.MINUTES.toSeconds(minutes));

        if (days > 0) {
            message += " " + days + "D ";
        }
        if (hours > 0) {
            message += " " + hours + "H ";
        }
        if (minutes > 0) {
            message += " " + minutes + "M ";
        }
        if (seconds > 0) {
            message += " " + seconds + "S ";
        }
        return message;
    }

    public static Location getSafeDestination(Location loc) {
        if (loc == null ) {
            return null;
        }

        if (!Utils.isUnsafe(loc.getBlock().getType())) {
            return loc;
        }

        List<Location> area = new ArrayList<>();
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                Location temp = new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY(), loc.getBlockZ() + z);
                area.add(temp);
            }
        }

        // Sorts by smallest area (blocks closest to starting point)
        area.sort(Comparator.comparingInt(a -> (a.getBlockX() * a.getBlockX() + a.getBlockZ() * a.getBlockZ())));

        for (Location safe_loc : area) {
            if (!Utils.isUnsafe(safe_loc.getBlock().getType())
                    && safe_loc.add(0, 1, 0).getBlock().getType() == Material.AIR
                    && safe_loc.add(0, 2, 0).getBlock().getType() == Material.AIR) {
                return safe_loc;
            }
        }

        // Sorry buddy, you're falling into lava
        return loc;
    }

    public static boolean canPlayerStandAt(Location loc) {
        World world = Objects.requireNonNull(loc.getWorld(), "world shall not be null");

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        return world.getBlockAt(x, y + 1, z).isEmpty()
            && world.getBlockAt(x, y + 2, z).isEmpty();
    }

    @SuppressWarnings("deprecation")
    public static EntityType getMob(String name) {
        name = name.replaceAll("[^a-z^A-Z0-9]", "");
        for (EntityType check : EntityType.values()) {
            if (check.name().replaceAll("[^a-z^bA-Z0-9]", "").equalsIgnoreCase(name)
                    || (check.getName() != null && check.getName().equalsIgnoreCase(name))
                    || (check.getEntityClass() != null && check.getEntityClass().getSimpleName().equalsIgnoreCase(name))) {
                return check;
            }
        }
        return null;
    }

    public static Block getPointedBlock(Entity entity, int distance, Collection<Material> transparent, boolean stop_on_solid) {
        Vector start_vector = entity.getLocation().toVector();
        Vector direction = entity.getLocation().getDirection();
        if (entity instanceof LivingEntity) {
            start_vector = ((LivingEntity) entity).getEyeLocation().toVector();
            direction = ((LivingEntity) entity).getEyeLocation().getDirection();
        }
        BlockIterator iterator = new BlockIterator(entity.getWorld(), start_vector, direction, 0, distance);
        Block pointed = null;
        while (iterator.hasNext()) {
            if (stop_on_solid && pointed != null && (transparent == null ? pointed.getType().isSolid() : !transparent.contains(pointed.getType()))) {
                break;
            }
            pointed = iterator.next();
        }
        return pointed;
    }

    public static Enchantment getEnchantment(String name) {
        switch (name.toLowerCase().replace("_", "")) {
            case "0":
            case "protect":
            case "protection":
            case "protectionenvironmental":
                return Enchantment.PROTECTION_ENVIRONMENTAL;
            case "1":
            case "fireprotect":
            case "fireprotection":
            case "protectionfire":
                return Enchantment.PROTECTION_FIRE;
            case "2":
            case "featherfall":
            case "featherfalling":
            case "fallprotect":
            case "fallprotection":
            case "protectionfall":
                return Enchantment.PROTECTION_FALL;
            case "3":
            case "blastprotect":
            case "blastprotection":
            case "explosionprotect":
            case "explosionprotection":
            case "protectionexplosions":
                return Enchantment.PROTECTION_EXPLOSIONS;
            case "4":
            case "projectileprotect":
            case "projectileprotection":
            case "arrowprotect":
            case "arrowprotection":
            case "protectionprojectile":
                return Enchantment.PROTECTION_PROJECTILE;
            case "5":
            case "breathing":
            case "respiration":
            case "oxygen":
                return Enchantment.OXYGEN;
            case "6":
            case "aquaaffinity":
            case "wateraffinity":
            case "waterworker":
                return Enchantment.WATER_WORKER;
            case "7":
            case "retaliate":
            case "thorns":
                return Enchantment.THORNS;
            case "8":
            case "depthstrider":
                return Enchantment.DEPTH_STRIDER;
            case "16":
            case "sharpness":
            case "damageall":
                return Enchantment.DAMAGE_ALL;
            case "17":
            case "smite":
            case "damageundead":
                return Enchantment.DAMAGE_UNDEAD;
            case "18":
            case "damagespiders":
            case "baneofarthropods":
            case "damagearthropods":
                return Enchantment.DAMAGE_ARTHROPODS;
            case "19":
            case "knockback":
                return Enchantment.KNOCKBACK;
            case "20":
            case "fire":
            case "fireaspect":
                return Enchantment.FIRE_ASPECT;
            case "21":
            case "looting":
            case "lootbonusmobs":
                return Enchantment.LOOT_BONUS_MOBS;
            case "32":
            case "efficiency":
            case "digspeed":
                return Enchantment.DIG_SPEED;
            case "33":
            case "silktouch":
                return Enchantment.SILK_TOUCH;
            case "34":
            case "unbreaking":
            case "durability":
                return Enchantment.DURABILITY;
            case "35":
            case "fortune":
            case "lootbonusblocks":
                return Enchantment.LOOT_BONUS_BLOCKS;
            case "48":
            case "power":
            case "arrowdamage":
                return Enchantment.ARROW_DAMAGE;
            case "49":
            case "punch":
            case "arrowknockback":
                return Enchantment.ARROW_KNOCKBACK;
            case "50":
            case "flame":
            case "arrowfire":
                return Enchantment.ARROW_FIRE;
            case "51":
            case "infinity":
            case "arrowinfinite":
                return Enchantment.ARROW_INFINITE;
            case "61":
            case "luckofthesea":
            case "luck":
                return Enchantment.LUCK;
            case "62":
            case "lure":
                return Enchantment.LURE;
            default:
                Enchantment enchantment = Enchantment.getByName(name.toUpperCase());
                if (enchantment == null) {
                    enchantment = Enchantment.getByKey(NamespacedKey.minecraft(name));
                }
                return enchantment;
        }
    }

    public static boolean isInWater(Location location) {
        return location.getBlock().getType() == Material.WATER;
    }

    public static boolean hasBetterEffect(Player player, PotionEffectType effect, int speedLevel) {
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            if ((potionEffect.getType().equals(effect)) && (potionEffect.getAmplifier() > speedLevel)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isHoldingBonemeal(PlayerInventory inventory) {
        return inventory.getItemInMainHand().getType() == Material.BONE_MEAL;
    }

    public static boolean isGrowable(Block block) {
        // TODO: Investigate if we can remove these first three branches?
        return (block.getType() == Material.WHEAT_SEEDS) ||
               (block.getType() == Material.CARROT) ||
               (block.getType() == Material.POTATO) ||
               (block.getBlockData() instanceof Sapling) ||
               (block.getBlockData() instanceof Ageable);
    }

    public static boolean isHoldingNothing(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        boolean mainEmpty = mainHand.getType() == Material.AIR || mainHand.getAmount() == 0;
        boolean offEmpty = offHand.getType() == Material.AIR || offHand.getAmount() == 0;

        return mainEmpty && offEmpty;
    }

    public static ItemStack addCustomNBTString(ItemStack itemStack, String key, String value) {
        ItemStack cloned = new ItemStack(itemStack);

        ItemMeta itemMeta = cloned.getItemMeta();

        try {
            // Find the NBTBase class by reflecting it out of a method parameter
            Class<?> nbtBaseClass = NMSUtil.getNMSClass("NBTBase");

            if (nbtBaseClass == null) {
                throw new IllegalStateException("Can't find nbtBaseClass!");
            }

            // We've got the NBTBase class, now get the createTag method.
            Method createTag = nbtBaseClass.getDeclaredMethod("createTag", byte.class);
            createTag.setAccessible(true);

            // Call it with param 8 to create an NBTTagString
            Object tagString = createTag.invoke(null, (byte)8);
            Field data = tagString.getClass().getDeclaredField("data");
            data.setAccessible(true);
            data.set(tagString, value);

            Field unhandledTagsField = itemMeta.getClass().getDeclaredField("unhandledTags");
            unhandledTagsField.setAccessible(true);
            //noinspection unchecked
            ((Map<String, Object>) unhandledTagsField.get(itemMeta)).put(key, tagString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cloned.setItemMeta(itemMeta);


        return cloned;
    }

    public static boolean hasCustomNBTString(ItemStack itemStack, String key) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        try {
            Field unhandledTagsField = itemMeta.getClass().getDeclaredField("unhandledTags");
            unhandledTagsField.setAccessible(true);
            return ((Map<?, ?>)unhandledTagsField.get(itemMeta)).containsKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Check if two Recipes are equal, by comparing their ingredients.
     * Merchant and Furnace recipes not supported at the moment.
     * @param first First recipe
     * @param second Recipe to compare first to
     * @return True if first is equal to second, false otherwise.
     */
    public static boolean areRecipesEqual(Recipe first, Recipe second) {
        // DO NOT compare results. Bukkit's deprecated spawn egg methods
        // cause all eggs to be unequal even if they spawn the same mob,
        // which would cause all spawn eggs to be craftable by all classes
        // if the results of the recipes were compared.

        if (first instanceof ShapedRecipe shapedFirst && second instanceof ShapedRecipe shapedSecond) {
            String[] secondShape = shapedSecond.getShape();
            String[] firstShape = shapedFirst.getShape();
            Map<Character, ItemStack> secondMap = shapedSecond.getIngredientMap();
            Map<Character, ItemStack> firstMap = shapedFirst.getIngredientMap();

            if (secondShape.length != firstShape.length) {
                return false;
            }

            int len = secondShape.length;

            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    /* FIXME
                    Caused by: java.lang.StringIndexOutOfBoundsException: String index out of range: 1
                    at java.lang.String.charAt(String.java:658) ~[?:1.8.0_111]
                    at com.poniarcade.core.utils.Utils.areRecipesEqual(Utils.java:725) ~[?:?]
                    at com.poniarcade.classesng.classes.Class.hasRecipe(Class.java:146) ~[?:?]
                    at com.poniarcade.classesng.classes.ClassManager.isClassRecipe(ClassManager.java:358) ~[?:?]
                     */
                    ItemStack firstItem = firstMap.get(firstShape[i].charAt(j));
                    ItemStack secondItem = secondMap.get(secondShape[i].charAt(j));
                    if ((secondItem == null || secondItem.isSimilar(new ItemStack(Material.AIR, 0))) &&
                            (firstItem == null || firstItem.isSimilar(new ItemStack(Material.AIR, 0)))) {
                        continue;
                    }

                    if (!secondItem.equals(firstItem)) {
                        return false;
                    }
                }
            }
            return true;
        } else if (first instanceof ShapelessRecipe shapelessFirst && second instanceof ShapelessRecipe shapelessSecond) {
            List<ItemStack> secondIngredients = shapelessSecond.getIngredientList();
            List<ItemStack> firstIngredients = shapelessFirst.getIngredientList();

            if (secondIngredients.size() != firstIngredients.size()) {
                return false;
            }

            for (ItemStack stack : secondIngredients) {
                for (int i = 0; i < firstIngredients.size(); i++) {
                    if (stack.equals(firstIngredients.get(i))) {
                        firstIngredients.remove(i);
                        break;
                    }
                }
            }
            return firstIngredients.isEmpty(); // everything matches
        }

        return false;
    }

    public static ItemStack createSpawnEgg(EntityType type) {
        //noinspection ConstantConditions
        return new ItemStack(Material.getMaterial(type.name() + "_SPAWN_EGG"));
    }

    public static boolean isStackEmpty(ItemStack stack) {
        return stack == null || stack.getType() == Material.AIR;
    }
}
