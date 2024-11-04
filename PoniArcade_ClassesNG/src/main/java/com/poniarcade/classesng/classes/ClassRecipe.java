package com.poniarcade.classesng.classes;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.core.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Arrays;

/**
 * Created by appledash on 8/13/16.
 * Blackjack is still best pony.
 * <p>
 * Note from DashKetchum about adding recipes:
 * Spawn eggs CANNOT be ingredients for class recipes.
 * All spawn eggs are currently considered unequal by Bukkit for
 * the purposes of comparing recipes, and the result of this
 * is that any recipe with a spawn egg as an ingredient will be
 * considered "not a class recipe" and thus will be allowed
 * for ALL classes. Consider yourself warned.
 */
public abstract class ClassRecipe {
	public static ClassRecipe BATPONY;
	public static ClassRecipe CHANGELING;
	public static ClassRecipe DRAGON;
	public static ClassRecipe EARTH_PONY;
	public static ClassRecipe GRYPHON;
	public static Pegasus PEGASUS;
	public static ClassRecipe SEAPONY;
	public static ClassRecipe UNICORN;
	public static ClassRecipe ABYSSAL_SEAPONY;
	public static ClassRecipe ALICORN;
	public static ClassRecipe ARCH_UNICORN;
	public static ClassRecipe ELDER_DRAGON;
	public static ClassRecipe ELITE_CHANGELING;
	public static ClassRecipe GRAND_EARTH_PONY;

	public static void init(PoniArcade_ClassesNG plugin) {
		BATPONY = new Batpony(plugin);
		CHANGELING = new Changeling(plugin);
		DRAGON = new Dragon(plugin);
		EARTH_PONY = new EarthPony(plugin);
		GRYPHON = new Gryphon(plugin);
		PEGASUS = new Pegasus(plugin);
		SEAPONY = new Seapony(plugin);
		UNICORN = new Unicorn(plugin);
		ABYSSAL_SEAPONY = new AbyssalSeapony(plugin);
		ALICORN = new Alicorn(plugin);
		ARCH_UNICORN = new ArchUnicorn(plugin);
		ELDER_DRAGON = new ElderDragon(plugin);
		ELITE_CHANGELING = new EliteChangeling(plugin);
		GRAND_EARTH_PONY = new GrandEarthPony(plugin);
	}

	private static class Batpony extends ClassRecipe {
		protected final ShapedRecipe RECIPE_BAT_EGG;

		protected Batpony(PoniArcade_ClassesNG plugin) {
            this.RECIPE_BAT_EGG =
				new ShapedRecipe(
					new NamespacedKey(plugin, "bat_egg"),
					Utils.createSpawnEgg(EntityType.BAT))
					.shape("L L", " P ", "   ")
					.setIngredient('L', Material.LEATHER)
					.setIngredient('P', Material.PORKCHOP);
		}

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.RECIPE_BAT_EGG
			};
		}
	}

	private static class Changeling extends ClassRecipe {
		private final ShapedRecipe recipeSlimeEgg;

		private final ShapedRecipe recipeCobweb;

		protected Changeling(PoniArcade_ClassesNG plugin) {
            this.recipeSlimeEgg =
				new ShapedRecipe(
					new NamespacedKey(plugin, "slime_egg"),
					Utils.createSpawnEgg(EntityType.SLIME))
					.shape("SSS", "SGS", "SSS")
					.setIngredient('S', Material.SLIME_BALL)
					.setIngredient('G', Material.GOLD_INGOT);

            this.recipeCobweb =
				new ShapedRecipe(
					new NamespacedKey(plugin, "cobweb"),
					new ItemStack(Material.COBWEB, 4))
					.shape("TLT", "LTL", "TLT")
					.setIngredient('T', Material.STRING)
					.setIngredient('L', Material.SLIME_BALL);

		}

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.recipeSlimeEgg,
				this.recipeCobweb
			};
		}
	}

	@SuppressWarnings("WeakerAccess")
	private static class Dragon extends ClassRecipe {
		public final ShapelessRecipe recipeIron;
		public final ShapelessRecipe recipeGold;
		public final ShapelessRecipe recipeStone;
		public final ShapelessRecipe recipeCharcoal;
		public final ShapelessRecipe recipeGlass;
		public final ShapelessRecipe recipePork;
		public final ShapelessRecipe recipeBeef;
		public final ShapelessRecipe recipeChicken;
		public final ShapelessRecipe recipeFish;
		public final ShapelessRecipe recipeSalmon;
		public final ShapelessRecipe recipePotato;
		public final ShapelessRecipe recipeBrick;
		public final ShapelessRecipe recipeHardenedClay;
		public final ShapelessRecipe recipeMutton;
		public final ShapelessRecipe recipeRabbit;
		public final ShapelessRecipe recipeNetherBrick;
		public final ShapelessRecipe recipeCrackedStoneBrick;

		protected Dragon(PoniArcade_ClassesNG plugin) {
			this.recipeIron =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_iron"),
					new ItemStack(Material.IRON_INGOT, 1))
					.addIngredient(Material.IRON_ORE)
					.addIngredient(Material.COAL);

			this.recipeGold =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_gold"),
					new ItemStack(Material.GOLD_INGOT, 1))
					.addIngredient(Material.GOLD_ORE)
					.addIngredient(Material.COAL);

			this.recipeStone =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_stone"),
					new ItemStack(Material.STONE, 1))
					.addIngredient(Material.COBBLESTONE)
					.addIngredient(Material.COAL);

			this.recipeCharcoal =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_charcoal"),
					new ItemStack(Material.CHARCOAL))
					.addIngredient(Material.OAK_LOG)
					.addIngredient(Material.COAL);

			this.recipeGlass =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_glass"),
					new ItemStack(Material.GLASS, 1))
					.addIngredient(Material.SAND)
					.addIngredient(Material.COAL);

			this.recipePork =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_pork"),
					new ItemStack(Material.COOKED_PORKCHOP, 1))
					.addIngredient(Material.PORKCHOP)
					.addIngredient(Material.COAL);

			this.recipeBeef =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_beef"),
					new ItemStack(Material.COOKED_BEEF, 1))
					.addIngredient(Material.BEEF)
					.addIngredient(Material.COAL);

			this.recipeChicken =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_chicken"),
					new ItemStack(Material.COOKED_CHICKEN, 1))
					.addIngredient(Material.CHICKEN)
					.addIngredient(Material.COAL);

			this.recipeFish =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_fish"),
					new ItemStack(Material.COOKED_COD))
					.addIngredient(Material.COD)
					.addIngredient(Material.COAL);

			this.recipeSalmon =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_salmon"),
					new ItemStack(Material.COOKED_SALMON, 1))
					.addIngredient(Material.SALMON)
					.addIngredient(Material.COAL);

			this.recipePotato =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_potato"),
					new ItemStack(Material.BAKED_POTATO, 1))
					.addIngredient(Material.POTATO)
					.addIngredient(Material.COAL);

			this.recipeBrick =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_brick"),
					new ItemStack(Material.BRICK))
					.addIngredient(Material.CLAY_BALL)
					.addIngredient(Material.COAL);

			this.recipeHardenedClay =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_hardened_clay"),
					new ItemStack(Material.TERRACOTTA, 1))
					.addIngredient(Material.CLAY)
					.addIngredient(Material.COAL);


			this.recipeMutton =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_mutton"),
					new ItemStack(Material.COOKED_MUTTON, 1))
					.addIngredient(Material.MUTTON)
					.addIngredient(Material.COAL);

			this.recipeRabbit =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_rabbit"),
					new ItemStack(Material.COOKED_RABBIT, 1))
					.addIngredient(Material.RABBIT)
					.addIngredient(Material.COAL);

			this.recipeNetherBrick =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_nether_brick"),
					new ItemStack(Material.NETHER_BRICK_STAIRS, 1))
					.addIngredient(Material.NETHERRACK)
					.addIngredient(Material.COAL);

			this.recipeCrackedStoneBrick =
				new ShapelessRecipe(new NamespacedKey(plugin, "dragon_cracked_stone_brick"),
					new ItemStack(Material.CRACKED_STONE_BRICKS, 1))
					.addIngredient(Material.STONE_BRICKS)
					.addIngredient(Material.COAL);
		}

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.recipeIron,
				this.recipeGold,
				this.recipeStone,
				this.recipeCharcoal,
				this.recipeGlass,
				this.recipePork,
				this.recipeBeef,
				this.recipeChicken,
				this.recipeFish,
				this.recipePotato,
				this.recipeBrick,
				this.recipeHardenedClay,
				this.recipeSalmon,
				this.recipeMutton,
				this.recipeRabbit,
				this.recipeNetherBrick,
				this.recipeCrackedStoneBrick
			};
		}
	}

	private static class EarthPony extends ClassRecipe {
		private final ShapedRecipe recipeChickenEgg;
		private final ShapedRecipe recipeCowEgg;
		private final ShapedRecipe recipePigEgg;
		private final ShapedRecipe recipeSheepEgg;
		private final ShapedRecipe recipeHorseEgg;
		private final ShapedRecipe recipeRabbitEgg;
		private final ShapedRecipe recipeWolfEgg;
		private final ShapedRecipe recipeZombieEgg;
		private final ShapedRecipe recipeSkeletonEgg;
		private final ShapedRecipe recipeSpiderEgg;
		private final ShapedRecipe recipeCreeperEgg;
		private final ShapedRecipe recipeSaddle;

		protected EarthPony(PoniArcade_ClassesNG plugin) {
			this.recipeChickenEgg =
				new ShapedRecipe(new NamespacedKey(plugin, "earth_pony_chicken_egg"),
					Utils.createSpawnEgg(EntityType.CHICKEN))
					.shape(" S ", "FCF", "   ")
					.setIngredient('S', Material.WHEAT_SEEDS)
					.setIngredient('F', Material.FEATHER)
					.setIngredient('C', Material.CHICKEN);

			this.recipeCowEgg =
				new ShapedRecipe(new NamespacedKey(plugin, "earth_pony_cow_egg"),
					Utils.createSpawnEgg(EntityType.COW))
					.shape("W  ", "BBB", "L L")
					.setIngredient('W', Material.WHEAT)
					.setIngredient('B', Material.BEEF)
					.setIngredient('L', Material.LEATHER);

			this.recipePigEgg =
				new ShapedRecipe(new NamespacedKey(plugin, "earth_pony_pig_egg"),
					Utils.createSpawnEgg(EntityType.PIG))
					.shape("P  ", "RRR", "L L")
					.setIngredient('P', Material.POTATO)
					.setIngredient('R', Material.PORKCHOP)
					.setIngredient('L', Material.LEATHER);

			this.recipeSheepEgg =
				new ShapedRecipe(new NamespacedKey(plugin, "earth_pony_sheep_egg"),
					Utils.createSpawnEgg(EntityType.SHEEP))
					.shape("W  ", "OMO", "L L")
					.setIngredient('W', Material.WHEAT)
					.setIngredient('O', Material.WHITE_WOOL)
					.setIngredient('M', Material.MUTTON)
					.setIngredient('L', Material.LEATHER);

			this.recipeHorseEgg =
				new ShapedRecipe(new NamespacedKey(plugin, "earth_pony_horse_egg"),
					Utils.createSpawnEgg(EntityType.HORSE))
					.shape("A  ", "LSL", "L L")
					.setIngredient('A', Material.APPLE)
					.setIngredient('S', Material.SADDLE)
					.setIngredient('L', Material.LEATHER);

			this.recipeRabbitEgg =
				new ShapedRecipe(new NamespacedKey(plugin, "earth_pony_rabbit_egg"),
					Utils.createSpawnEgg(EntityType.RABBIT))
					.shape(" C ", "HRH", "   ")
					.setIngredient('C', Material.CARROT)
					.setIngredient('H', Material.RABBIT_HIDE)
					.setIngredient('R', Material.RABBIT);

			this.recipeWolfEgg =
				new ShapedRecipe(new NamespacedKey(plugin, "earth_pony_wolf_egg"),
					Utils.createSpawnEgg(EntityType.WOLF))
					.shape("B  ", "LML", "B B")
					.setIngredient('B', Material.BONE)
					.setIngredient('L', Material.LEATHER)
					.setIngredient('M', Material.MUTTON);

			this.recipeZombieEgg =
				new ShapedRecipe(new NamespacedKey(plugin, "earth_pony_zombie_egg"),
					Utils.createSpawnEgg(EntityType.ZOMBIE))
					.shape(" G ", "RRR", "R R")
					.setIngredient('G', Material.GOLD_INGOT)
					.setIngredient('R', Material.ROTTEN_FLESH);

			this.recipeSkeletonEgg =
				new ShapedRecipe(new NamespacedKey(plugin, "earth_pony_skeleton_egg"),
					Utils.createSpawnEgg(EntityType.SKELETON))
					.shape(" G ", "WBA", "B B")
					.setIngredient('W', Material.BOW)
					.setIngredient('B', Material.BONE)
					.setIngredient('A', Material.ARROW)
					.setIngredient('G', Material.GOLD_INGOT);

			this.recipeSpiderEgg =
				new ShapedRecipe(new NamespacedKey(plugin, "earth_pony_spider_egg"),
					Utils.createSpawnEgg(EntityType.SPIDER))
					.shape("   ", "EGE", "S S")
					.setIngredient('G', Material.GOLD_INGOT)
					.setIngredient('E', Material.SPIDER_EYE)
					.setIngredient('S', Material.STRING);

			this.recipeCreeperEgg =
				new ShapedRecipe(new NamespacedKey(plugin, "earth_pony_creeper_egg"),
					Utils.createSpawnEgg(EntityType.CREEPER))
					.shape(" G ", " T ", "STS")
					.setIngredient('G', Material.GOLD_INGOT)
					.setIngredient('T', Material.TNT)
					.setIngredient('S', Material.GUNPOWDER);

			this.recipeSaddle =
				new ShapedRecipe(new NamespacedKey(plugin, "earth_pony_saddle"),
					new ItemStack(Material.SADDLE))
					.shape(" L ", "L L", "   ")
					.setIngredient('L', Material.LEATHER);
		}

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.recipeChickenEgg,
				this.recipeCowEgg,
				this.recipePigEgg,
				this.recipeSheepEgg,
				this.recipeHorseEgg,
				this.recipeRabbitEgg,
				this.recipeWolfEgg,
				this.recipeZombieEgg,
				this.recipeSkeletonEgg,
				this.recipeSpiderEgg,
				this.recipeCreeperEgg,
				this.recipeSaddle
			};
		}
	}

	private static class Gryphon extends ClassRecipe {
		private final ShapedRecipe recipeOcelotEgg;

		protected Gryphon(PoniArcade_ClassesNG plugin) {
			this.recipeOcelotEgg =
				new ShapedRecipe(
					new NamespacedKey(plugin, "gryphon_ocelot_egg"),
					Utils.createSpawnEgg(EntityType.OCELOT))
					.shape("M  ", "FFF", "B B")
					.setIngredient('M', Material.MILK_BUCKET)
					.setIngredient('F', Material.COD)
					.setIngredient('B', Material.BONE);
		}

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.recipeOcelotEgg
			};
		}
	}

	@SuppressWarnings("ConstantConditions")
    public static class Pegasus extends ClassRecipe {
		public static final ItemStack RAINWATER_ITEM = new ItemStack(Material.POTION, 1);
		public static final ItemStack CLEAR_SKIES_ITEM = new ItemStack(Material.POTION, 1);
		public static final ItemStack CHARGED_RAINWATER_ITEM = new ItemStack(Material.POTION, 1);
		public static final ItemStack ATMOSPHERIC_BALANCE_ITEM = new ItemStack(Material.POTION, 1);

		private final ShapelessRecipe recipeRainwater;
		private final ShapelessRecipe recipeClearSkies;
		private final ShapelessRecipe recipeAtmosphericBalanceRain;
		private final ShapelessRecipe recipeChargedRainwater;
		private final ShapelessRecipe recipeAtmosphericBalanceClear;

		protected static final String WEATHER_ITEM_NAME = ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Bottled Weather";

		protected Pegasus(PoniArcade_ClassesNG plugin) {
            // Set rainwater meta
            PotionMeta meta = (PotionMeta) Pegasus.RAINWATER_ITEM.getItemMeta();
            meta.setDisplayName(Pegasus.WEATHER_ITEM_NAME);
            meta.setLore(Arrays.asList(ChatColor.BLUE + "Rainwater", "It's rather wet"));
            Pegasus.RAINWATER_ITEM.setItemMeta(meta);

            // Set atmospheric balance meta
            meta = (PotionMeta) Pegasus.ATMOSPHERIC_BALANCE_ITEM.getItemMeta();
            meta.setDisplayName(Pegasus.WEATHER_ITEM_NAME);
            meta.setLore(Arrays.asList(ChatColor.GREEN + "Atmospheric Balance", "Returns the weather to its natural state"));
            Pegasus.ATMOSPHERIC_BALANCE_ITEM.setItemMeta(meta);

            // Set charged rainwater meta
            meta = (PotionMeta) Pegasus.CHARGED_RAINWATER_ITEM.getItemMeta();
            meta.setDisplayName(Pegasus.WEATHER_ITEM_NAME);
            meta.setLore(Arrays.asList(ChatColor.LIGHT_PURPLE + "Charged Rainwater", "Shocking!"));
            Pegasus.CHARGED_RAINWATER_ITEM.setItemMeta(meta);

            // Set clear skies meta
            meta = (PotionMeta) Pegasus.CLEAR_SKIES_ITEM.getItemMeta();
            meta.setDisplayName(Pegasus.WEATHER_ITEM_NAME);
            meta.setLore(Arrays.asList(ChatColor.GOLD + "Clear Skies", "Lovely weather for a picnic"));
            Pegasus.CLEAR_SKIES_ITEM.setItemMeta(meta);

            this.recipeRainwater =
                new ShapelessRecipe(
                    new NamespacedKey(plugin, "pegasus_rainwater"),
                    Pegasus.RAINWATER_ITEM)
                    .addIngredient(3, Material.FEATHER)
                    .addIngredient(Material.POTION);

            this.recipeClearSkies =
                new ShapelessRecipe(
                    new NamespacedKey(plugin, "pegasus_clear_skies"),
                    Pegasus.CLEAR_SKIES_ITEM)
                    .addIngredient(3, Material.BLAZE_POWDER)
                    .addIngredient(Pegasus.RAINWATER_ITEM.getData());

            this.recipeChargedRainwater =
                new ShapelessRecipe(
                    new NamespacedKey(plugin, "pegasus_charged_rainwater"),
                    Pegasus.CHARGED_RAINWATER_ITEM)
                    .addIngredient(3, Material.REDSTONE)
                    .addIngredient(Pegasus.RAINWATER_ITEM.getData());

            this.recipeAtmosphericBalanceRain =
                new ShapelessRecipe(
                    new NamespacedKey(plugin, "pegasus_atmospheric_balance"),
                    Pegasus.ATMOSPHERIC_BALANCE_ITEM)
                    .addIngredient(3, Material.FEATHER)
                    .addIngredient(Pegasus.RAINWATER_ITEM.getData());

            this.recipeAtmosphericBalanceClear =
                new ShapelessRecipe(
                    new NamespacedKey(plugin, "pegasus_atmospheric_balance_clear"),
                    Pegasus.ATMOSPHERIC_BALANCE_ITEM)
                    .addIngredient(3, Material.FEATHER)
                    .addIngredient(Pegasus.CLEAR_SKIES_ITEM.getData());
		}

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.recipeRainwater,
                this.recipeClearSkies,
                this.recipeAtmosphericBalanceRain,
                this.recipeChargedRainwater,
                this.recipeAtmosphericBalanceClear
			};
		}
	}

	private static class Seapony extends ClassRecipe {
		private final ShapedRecipe recipeSquidEgg;
		private final ShapedRecipe recipeIce;

		protected Seapony(PoniArcade_ClassesNG plugin) {
			this.recipeSquidEgg =
				new ShapedRecipe(
					new NamespacedKey(plugin, "seapony_squid_egg"),
					Utils.createSpawnEgg(EntityType.SQUID))
					.shape(" I ", "SIS", " I ")
					.setIngredient('I', Material.INK_SAC)
					.setIngredient('S', Material.SLIME_BALL);
			this.recipeIce =
				new ShapedRecipe(
					new NamespacedKey(plugin, "seapony_ice"),
					new ItemStack(Material.ICE, 3))
					.shape("SSS", "SWS", "SSS")
					.setIngredient('S', Material.SNOWBALL)
					.setIngredient('W', Material.WATER_BUCKET);
		}

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.recipeIce,
				this.recipeSquidEgg
			};
		}
	}

	private static class Unicorn extends ClassRecipe {
		private final ShapedRecipe recipeIronHorseArmor;

		private final ShapedRecipe recipeGoldHorseArmor;

		private final ShapedRecipe recipeDiamondHorseArmor;

		protected Unicorn(PoniArcade_ClassesNG plugin) {
            this.recipeIronHorseArmor =
                new ShapedRecipe(
                    new NamespacedKey(plugin, "unicorn_iron_horse_armor"),
                    new ItemStack(Material.IRON_HORSE_ARMOR))
                    .shape("I  ", "IWI", "III")
                    .setIngredient('I', Material.IRON_INGOT)
                    .setIngredient('W', Material.WHITE_WOOL);
            this.recipeGoldHorseArmor =
                new ShapedRecipe(
                    new NamespacedKey(plugin, "unicorn_golden_horse_armor"),
                    new ItemStack(Material.GOLDEN_HORSE_ARMOR))
                    .shape("G  ", "GWG", "GGG")
                    .setIngredient('G', Material.GOLD_INGOT)
                    .setIngredient('W', Material.WHITE_WOOL);
            this.recipeDiamondHorseArmor =
                new ShapedRecipe(
                    new NamespacedKey(plugin, "unicorn_diamond_horse_armor"),
                    new ItemStack(Material.DIAMOND_HORSE_ARMOR))
                    .shape("D  ", "DWD", "DDD")
                    .setIngredient('D', Material.DIAMOND)
                    .setIngredient('W', Material.WHITE_WOOL);
		}

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.recipeIronHorseArmor,
				this.recipeGoldHorseArmor,
				this.recipeDiamondHorseArmor
			};
		}
	}

	private static class AbyssalSeapony extends ClassRecipe {
		private final ShapedRecipe recipeGuardianEgg;
		protected AbyssalSeapony(PoniArcade_ClassesNG plugin) {
            this.recipeGuardianEgg =
                new ShapedRecipe(
                    new NamespacedKey(plugin, "abyssal_seapony_guardian_egg"),
                    Utils.createSpawnEgg(EntityType.GUARDIAN))
                    .shape(" G ", "SCS", "SSS")
                    .setIngredient('G', Material.GOLD_INGOT)
                    .setIngredient('S', Material.PRISMARINE_SHARD)
                    .setIngredient('C', Material.PRISMARINE_CRYSTALS);
        }

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.recipeGuardianEgg
			};
		}
	}

	private static class Alicorn extends ClassRecipe {
		private final ShapedRecipe recipeVillagerEgg;

		protected Alicorn(PoniArcade_ClassesNG plugin) {
            this.recipeVillagerEgg =
                new ShapedRecipe(
                    new NamespacedKey(plugin, "alicorn_villager_egg"),
                    Utils.createSpawnEgg(EntityType.VILLAGER))
                    .shape("BEB", " P ", "B B")
                    .setIngredient('B', Material.BONE)
                    .setIngredient('E', Material.EMERALD)
                    .setIngredient('P', Material.PORKCHOP);
		}

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.recipeVillagerEgg
			};
		}
	}

	private static class ArchUnicorn extends ClassRecipe {
		private final ShapedRecipe recipeSkeletonHorseEgg;

		protected ArchUnicorn(PoniArcade_ClassesNG plugin) {
            this.recipeSkeletonHorseEgg =
                new ShapedRecipe(
                    new NamespacedKey(plugin, "arch_unicorn_horse_egg"),
                    Utils.createSpawnEgg(EntityType.SKELETON_HORSE))
                    .shape("B  ", "BSB", "B B")
                    .setIngredient('B', Material.BONE)
                    .setIngredient('S', Material.SADDLE);
		}

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.recipeSkeletonHorseEgg
			};
		}
	}

	private static class ElderDragon extends ClassRecipe {
		private final ShapedRecipe recipeBlazeEgg;

		private final ShapedRecipe recipeDragonsBreath;

		protected ElderDragon(PoniArcade_ClassesNG plugin) {
            this.recipeBlazeEgg =
                new ShapedRecipe(
                    new NamespacedKey(plugin, "elder_dragon_blaze_egg"),
                    Utils.createSpawnEgg(EntityType.BLAZE))
                    .shape(" G ", "RPR", "R R")
                    .setIngredient('G', Material.GOLD_INGOT)
                    .setIngredient('R', Material.BLAZE_ROD)
                    .setIngredient('P', Material.BLAZE_POWDER);
            this.recipeDragonsBreath =
                new ShapedRecipe(
                    new NamespacedKey(plugin, "elder_dragon_dragons_breath"),
                    new ItemStack(Material.DRAGON_BREATH, 1))
                    .shape("PGP", "FLF", "PPP")
                    .setIngredient('P', Material.BLAZE_POWDER)
                    .setIngredient('G', Material.GUNPOWDER)
                    .setIngredient('F', Material.FIRE_CHARGE)
                    .setIngredient('L', Material.GLASS_BOTTLE);
		}

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.recipeBlazeEgg,
				this.recipeDragonsBreath
			};
		}
	}

	private static class EliteChangeling extends ClassRecipe {
		private final ShapedRecipe recipeSilverfishEgg;

		protected EliteChangeling(PoniArcade_ClassesNG plugin) {
            this.recipeSilverfishEgg =
                new ShapedRecipe(
                    new NamespacedKey(plugin, "elite_changeling_silverfish_egg"),
                    Utils.createSpawnEgg(EntityType.SILVERFISH))
                    .shape(" G ", "SES", "   ")
                    .setIngredient('G', Material.GOLD_INGOT)
                    .setIngredient('S', Material.STONE)
                    .setIngredient('E', Material.SPIDER_EYE);
		}

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.recipeSilverfishEgg
			};
		}
	}

	private static class GrandEarthPony extends ClassRecipe {
		private final ShapedRecipe recipeLlamaEgg;

		protected GrandEarthPony(PoniArcade_ClassesNG plugin) {
            this.recipeLlamaEgg =
                new ShapedRecipe(
                    new NamespacedKey(plugin, "grand_earth_pony_llama_egg"),
                    Utils.createSpawnEgg(EntityType.LLAMA))
                    .shape("B  ", "LLL", "L L")
                    .setIngredient('B', Material.BEETROOT)
                    .setIngredient('L', Material.LEATHER);
		}

		@Override
		public Recipe[] values() {
			return new Recipe[]{
				this.recipeLlamaEgg
			};
		}
	}

	public abstract Recipe[] values();
}
