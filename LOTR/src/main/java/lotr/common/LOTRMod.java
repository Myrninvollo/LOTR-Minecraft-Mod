package lotr.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lotr.common.block.*;
import lotr.common.command.*;
import lotr.common.dispenser.*;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.LOTREntityRegistry;
import lotr.common.entity.LOTREntityRegistry.RegistryInfo;
import lotr.common.entity.animal.*;
import lotr.common.entity.item.*;
import lotr.common.entity.npc.*;
import lotr.common.entity.projectile.*;
import lotr.common.item.*;
import lotr.common.recipe.LOTRBrewingRecipes;
import lotr.common.recipe.LOTREntJarRecipes;
import lotr.common.recipe.LOTRRecipes;
import lotr.common.tileentity.*;
import lotr.common.world.LOTRWorldProvider;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.structure.LOTRStructures;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.*;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "lotr", name = "The Lord of the Rings Mod", version = "Beta v19.0 for Minecraft 1.7.10")
public class LOTRMod
{
	@SidedProxy(clientSide = "lotr.client.LOTRClientProxy", serverSide = "lotr.common.LOTRCommonProxy")
	public static LOTRCommonProxy proxy;
	
	@Mod.Instance("lotr")
	public static LOTRMod instance;
	
	public static ToolMaterial toolBronze = EnumHelper.addToolMaterial("LOTR_BRONZE", 2, 220, 5F, 1.5F, 10);
	public static ToolMaterial toolMithril = EnumHelper.addToolMaterial("LOTR_MITHRIL", 4, 2478, 9F, 5F, 12);
	public static ToolMaterial toolOrc = EnumHelper.addToolMaterial("LOTR_ORC", 2, 280, 6F, 2.5F, 7);
	public static ToolMaterial toolGondor = EnumHelper.addToolMaterial("LOTR_GONDOR", 2, 280, 6F, 2.5F, 10);
	public static ToolMaterial toolMallorn = EnumHelper.addToolMaterial("LOTR_MALLORN", 1, 120, 4F, 1F, 15);
	public static ToolMaterial toolElven = EnumHelper.addToolMaterial("LOTR_ELVEN", 2, 320, 7F, 3F, 14);
	public static ToolMaterial toolDwarven = EnumHelper.addToolMaterial("LOTR_DWARVEN", 3, 500, 5F, 3F, 8);
	public static ToolMaterial toolUruk = EnumHelper.addToolMaterial("LOTR_URUK", 2, 390, 6F, 3F, 6);
	public static ToolMaterial toolRohan = EnumHelper.addToolMaterial("LOTR_ROHAN", 2, 260, 6F, 2.5F, 12);
	public static ToolMaterial toolMorgul = EnumHelper.addToolMaterial("LOTR_MORGUL", 2, 400, 6F, 3F, 10);
	public static ToolMaterial toolWoodElven = EnumHelper.addToolMaterial("LOTR_WOOD_ELVEN", 2, 310, 7F, 3F, 15);
	public static ToolMaterial toolAngmar = EnumHelper.addToolMaterial("LOTR_ANGMAR", 2, 270, 6F, 2.5F, 11);
	public static ToolMaterial toolNearHarad = EnumHelper.addToolMaterial("LOTR_NEAR_HARAD", 2, 280, 6F, 2.5F, 16);
	public static ToolMaterial toolHighElven = EnumHelper.addToolMaterial("LOTR_HIGH_ELVEN", 2, 400, 7F, 3F, 15);
	public static ToolMaterial toolBlueDwarven = EnumHelper.addToolMaterial("LOTR_BLUE_DWARVEN", 3, 500, 5F, 3F, 8);
	
	public static ArmorMaterial armorBronze = EnumHelper.addArmorMaterial("LOTR_BRONZE", 12, new int[]{2, 5, 4, 2}, 10);
	public static ArmorMaterial armorMithril = EnumHelper.addArmorMaterial("LOTR_MITHRIL", 120, new int[]{3, 8, 6, 3}, 12);
	public static ArmorMaterial armorOrc = EnumHelper.addArmorMaterial("LOTR_ORC", 17, new int[]{2, 6, 5, 2}, 7);
	public static ArmorMaterial armorGondor = EnumHelper.addArmorMaterial("LOTR_GONDOR", 18, new int[]{2, 6, 5, 2}, 10);
	public static ArmorMaterial armorElven = EnumHelper.addArmorMaterial("LOTR_ELVEN", 20, new int[]{2, 6, 5, 2}, 14);
	public static ArmorMaterial armorWarg = EnumHelper.addArmorMaterial("LOTR_WARG", 10, new int[]{1, 4, 3, 1}, 8);
	public static ArmorMaterial armorDwarven = EnumHelper.addArmorMaterial("LOTR_DWARVEN", 24, new int[]{2, 6, 5, 2}, 8);
	public static ArmorMaterial armorGalvorn = EnumHelper.addArmorMaterial("LOTR_GALVORN", 19, new int[]{2, 6, 5, 2}, 15);
	public static ArmorMaterial armorUruk = EnumHelper.addArmorMaterial("LOTR_URUK", 21, new int[]{2, 6, 5, 2}, 6);
	public static ArmorMaterial armorWoodElvenScout = EnumHelper.addArmorMaterial("LOTR_WOOD_ELVEN_SCOUT", 12, new int[]{1, 4, 3, 1}, 10);
	public static ArmorMaterial armorRohan = EnumHelper.addArmorMaterial("LOTR_ROHAN", 14, new int[]{2, 5, 4, 2}, 12);
	public static ArmorMaterial armorRanger = EnumHelper.addArmorMaterial("LOTR_RANGER", 16, new int[]{1, 4, 3, 1}, 6);
	public static ArmorMaterial armorDunlending = EnumHelper.addArmorMaterial("LOTR_DUNLENDING", 14, new int[]{2, 5, 4, 2}, 12);
	public static ArmorMaterial armorMorgul = EnumHelper.addArmorMaterial("LOTR_MORGUL", 20, new int[]{2, 6, 5, 2}, 10);
	public static ArmorMaterial armorWoodElven = EnumHelper.addArmorMaterial("LOTR_WOOD_ELVEN", 19, new int[]{2, 6, 5, 2}, 15);
	public static ArmorMaterial armorAngmar = EnumHelper.addArmorMaterial("LOTR_ANGMAR", 17, new int[]{2, 6, 5, 2}, 11);
	public static ArmorMaterial armorNearHarad = EnumHelper.addArmorMaterial("LOTR_NEAR_HARAD", 18, new int[]{2, 6, 5, 2}, 16);
	public static ArmorMaterial armorGemsbok = EnumHelper.addArmorMaterial("LOTR_GEMSBOK", 10, new int[]{1, 4, 3, 1}, 6);
	public static ArmorMaterial armorHighElven = EnumHelper.addArmorMaterial("LOTR_HIGH_ELVEN", 22, new int[]{2, 6, 5, 2}, 15);
	public static ArmorMaterial armorBlueDwarven = EnumHelper.addArmorMaterial("LOTR_BLUE_DWARVEN", 24, new int[]{2, 6, 5, 2}, 8);
	
	public static ArmorMaterial materialWargArmor = EnumHelper.addArmorMaterial("LOTR_WARG_ARMOR", 18, new int[]{3, 8, 5, 0}, 0);
	public static ArmorMaterial materialLeatherHat = EnumHelper.addArmorMaterial("LOTR_LEATHER_HAT", 0, new int[]{0, 0, 0, 0}, 0);
	
	public static Block rock;
	public static Block oreCopper;
	public static Block oreTin;
	public static Block oreSilver;
	public static Block oreMithril;
	public static Block beacon;
	public static Block simbelmyne;
	public static Block wood;
	public static Block leaves;
	public static Block planks;
	public static Block sapling;
	public static Block woodSlabSingle;
	public static Block woodSlabDouble;
	public static Block stairsShirePine;
	public static Block shireHeather;
	public static Block brick;
	public static Block appleCrumble;
	public static Block hobbitOven;
	public static Block blockOreStorage;
	public static Block oreNaurite;
	public static Block oreMorgulIron;
	public static Block morgulTable;
	public static Block chandelier;
	public static Block pipeweedPlant;
	public static Block pipeweedCrop;
	public static Block slabSingle;
	public static Block slabDouble;
	public static Block stairsMordorBrick;
	public static Block stairsGondorBrick;
	public static Block wall;
	public static Block barrel;
	public static Block lettuceCrop;
	public static Block orcBomb;
	public static Block orcTorch;
	public static Block elanor;
	public static Block niphredil;
	public static Block stairsMallorn;
	public static Block elvenTable;
	public static Block mobSpawner;
	public static Block mallornLadder;
	public static Block plateBlock;
	public static Block orcSteelBars;
	public static Block stairsGondorBrickMossy;
	public static Block stairsGondorBrickCracked;
	public static Block athelas;
	public static Block stalactite;
	public static Block stairsRohanBrick;
	public static Block oreQuendite;
	public static Block mallornTorch;
	public static Block spawnerChest;
	public static Block quenditeGrass;
	public static Block pressurePlateMordorRock;
	public static Block pressurePlateGondorRock;
	public static Block buttonMordorRock;
	public static Block buttonGondorRock;
	public static Block elvenPortal;
	public static Block flowerPot;
	public static Block stairsDwarvenBrick;
	public static Block elvenBed;
	public static Block pillar;
	public static Block oreGlowstone;
	public static Block fruitWood;
	public static Block fruitLeaves;
	public static Block fruitSapling;
	public static Block stairsApple;
	public static Block stairsPear;
	public static Block stairsCherry;
	public static Block dwarvenTable;
	public static Block bluebell;
	public static Block dwarvenForge;
	public static Block hearth;
	public static Block morgulShroom;
	public static Block urukTable;
	public static Block cherryPie;
	public static Block clover;
	public static Block slabSingle2;
	public static Block slabDouble2;
	public static Block stairsMirkOak;
	public static Block webUngoliant;
	public static Block woodElvenTable;
	public static Block woodElvenBed;
	public static Block gondorianTable;
	public static Block woodElvenTorch;
	public static Block marshLights;
	public static Block rohirricTable;
	public static Block pressurePlateRohanRock;
	public static Block remains;
	public static Block deadPlant;
	public static Block oreGulduril;
	public static Block guldurilBrick;
	public static Block dwarvenDoor;
	public static Block stairsCharred;
	public static Block dwarvenBed;
	public static Block morgulPortal;
	public static Block armorStand;
	public static Block buttonRohanRock;
	public static Block asphodel;
	public static Block wood2;
	public static Block leaves2;
	public static Block sapling2;
	public static Block stairsLebethron;
	public static Block woodSlabSingle2;
	public static Block woodSlabDouble2;
	public static Block dwarfHerb;
	public static Block mugBlock;
	public static Block dunlendingTable;
	public static Block stairsBeech;
	public static Block entJar;
	public static Block mordorThorn;
	public static Block mordorMoss;
	public static Block stairsMordorBrickCracked;
	public static Block orcForge;
	public static Block trollTotem;
	public static Block orcBed;
	public static Block stairsElvenBrick;
	public static Block stairsElvenBrickMossy;
	public static Block stairsElvenBrickCracked;
	public static Block stairsHolly;
	public static Block pressurePlateBlueRock;
	public static Block buttonBlueRock;
	public static Block slabSingle3;
	public static Block slabDouble3;
	public static Block stairsBlueRockBrick;
	public static Block fence;
	public static Block doubleFlower;
	public static Block oreSulfur;
	public static Block oreSaltpeter;
	public static Block quagmire;
	public static Block angmarTable;
	public static Block brick2;
	public static Block wall2;
	public static Block stairsAngmarBrick;
	public static Block stairsAngmarBrickCracked;
	public static Block stairsMango;
	public static Block stairsBanana;
	public static Block bananaBlock;
	public static Block bananaCake;
	public static Block lionBed;
	public static Block wood3;
	public static Block leaves3;
	public static Block sapling3;
	public static Block stairsMaple;
	public static Block stairsLarch;
	public static Block nearHaradTable;
	public static Block highElvenTable;
	public static Block highElvenTorch;
	public static Block highElvenBed;
	public static Block pressurePlateRedRock;
	public static Block buttonRedRock;
	public static Block stairsRedRockBrick;
	public static Block slabSingle4;
	public static Block slabDouble4;
	public static Block stairsNearHaradBrick;
	public static Block stairsDatePalm;
	public static Block dateBlock;
	public static Block blueDwarvenTable;
	public static Block goran;
	public static Block thatch;
	public static Block slabSingleThatch;
	public static Block slabDoubleThatch;
	public static Block stairsThatch;
	public static Block fangornPlant;
	public static Block fangornRiverweed;

	public static Item goldRing;
	public static Item pouch;
	public static Item copper;
	public static Item tin;
	public static Item bronze;
	public static Item silver;
	public static Item mithril;
	public static Item shovelBronze;
	public static Item pickaxeBronze;
	public static Item axeBronze;
	public static Item swordBronze;
	public static Item hoeBronze;
	public static Item helmetBronze;
	public static Item bodyBronze;
	public static Item legsBronze;
	public static Item bootsBronze;
	public static Item silverNugget;
	public static Item silverRing;
	public static Item mithrilNugget;
	public static Item mithrilRing;
	public static Item hobbitPipe;
	public static Item pipeweed;
	public static Item clayMug;
	public static Item mug;
	public static Item mugWater;
	public static Item mugMilk;
	public static Item mugAle;
	public static Item mugChocolate;
	public static Item appleCrumbleItem;
	public static Item mugMiruvor;
	public static Item mugOrcDraught;
	public static Item scimitarOrc;
	public static Item helmetOrc;
	public static Item bodyOrc;
	public static Item legsOrc;
	public static Item bootsOrc;
	public static Item orcSteel;
	public static Item battleaxeOrc;
	public static Item lembas;
	public static Item nauriteGem;
	public static Item daggerOrc;
	public static Item daggerOrcPoisoned;
	public static Item sting;
	public static Item spawnEgg;
	public static Item pipeweedLeaf;
	public static Item pipeweedSeeds;
	public static Item structureSpawner;
	public static Item lettuce;
	public static Item shovelMithril;
	public static Item pickaxeMithril;
	public static Item axeMithril;
	public static Item swordMithril;
	public static Item hoeMithril;
	public static Item orcTorchItem;
	public static Item sauronMace;
	public static Item gandalfStaffWhite;
	public static Item swordGondor;
	public static Item helmetGondor;
	public static Item bodyGondor;
	public static Item legsGondor;
	public static Item bootsGondor;
	public static Item helmetMithril;
	public static Item bodyMithril;
	public static Item legsMithril;
	public static Item bootsMithril;
	public static Item spearGondor;
	public static Item spearOrc;
	public static Item spearBronze;
	public static Item spearIron;
	public static Item spearMithril;
	public static Item anduril;
	public static Item dye;
	public static Item mallornStick;
	public static Item shovelMallorn;
	public static Item pickaxeMallorn;
	public static Item axeMallorn;
	public static Item swordMallorn;
	public static Item hoeMallorn;
	public static Item shovelElven;
	public static Item pickaxeElven;
	public static Item axeElven;
	public static Item swordElven;
	public static Item hoeElven;
	public static Item spearElven;
	public static Item mallornBow;
	public static Item helmetElven;
	public static Item bodyElven;
	public static Item legsElven;
	public static Item bootsElven;
	public static Item silverCoin;
	public static Item gammon;
	public static Item clayPlate;
	public static Item plate;
	public static Item elvenBow;
	public static Item wargFur;
	public static Item helmetWarg;
	public static Item bodyWarg;
	public static Item legsWarg;
	public static Item bootsWarg;
	public static Item orcBow;
	public static Item mugMead;
	public static Item wargskinRug;
	public static Item quenditeCrystal;
	public static Item blacksmithHammer;
	public static Item daggerGondor;
	public static Item daggerElven;
	public static Item hobbitRing;
	public static Item elvenBedItem;
	public static Item wargBone;
	public static Item appleGreen;
	public static Item pear;
	public static Item cherry;
	public static Item dwarfSteel;
	public static Item shovelDwarven;
	public static Item pickaxeDwarven;
	public static Item axeDwarven;
	public static Item swordDwarven;
	public static Item hoeDwarven;
	public static Item daggerDwarven;
	public static Item battleaxeDwarven;
	public static Item hammerDwarven;
	public static Item shovelOrc;
	public static Item pickaxeOrc;
	public static Item axeOrc;
	public static Item hoeOrc;
	public static Item hammerOrc;
	public static Item helmetDwarven;
	public static Item bodyDwarven;
	public static Item legsDwarven;
	public static Item bootsDwarven;
	public static Item galvorn;
	public static Item helmetGalvorn;
	public static Item bodyGalvorn;
	public static Item legsGalvorn;
	public static Item bootsGalvorn;
	public static Item daggerBronze;
	public static Item daggerIron;
	public static Item daggerMithril;
	public static Item battleaxeMithril;
	public static Item hammerMithril;
	public static Item hammerGondor;
	public static Item orcBone;
	public static Item elfBone;
	public static Item dwarfBone;
	public static Item hobbitBone;
	public static Item commandHorn;
	public static Item throwingAxeDwarven;
	public static Item urukSteel;
	public static Item shovelUruk;
	public static Item pickaxeUruk;
	public static Item axeUruk;
	public static Item scimitarUruk;
	public static Item hoeUruk;
	public static Item daggerUruk;
	public static Item daggerUrukPoisoned;
	public static Item battleaxeUruk;
	public static Item hammerUruk;
	public static Item spearUruk;
	public static Item helmetUruk;
	public static Item bodyUruk;
	public static Item legsUruk;
	public static Item bootsUruk;
	public static Item crossbowBolt;
	public static Item urukCrossbow;
	public static Item cherryPieItem;
	public static Item trollBone;
	public static Item trollStatue;
	public static Item wargHelmet;
	public static Item wargChestplate;
	public static Item wargHindplate;
	public static Item ironCrossbow;
	public static Item mithrilCrossbow;
	public static Item woodElvenBedItem;
	public static Item helmetWoodElvenScout;
	public static Item bodyWoodElvenScout;
	public static Item legsWoodElvenScout;
	public static Item bootsWoodElvenScout;
	public static Item mirkwoodBow;
	public static Item mugRedWine;
	public static Item ancientItemParts;
	public static Item ancientItem;
	public static Item swordRohan;
	public static Item daggerRohan;
	public static Item spearRohan;
	public static Item helmetRohan;
	public static Item bodyRohan;
	public static Item legsRohan;
	public static Item bootsRohan;
	public static Item helmetGondorWinged;
	public static Item guldurilCrystal;
	public static Item dwarvenDoorItem;
	public static Item mallornNut;
	public static Item dwarvenBedItem;
	public static Item mugCider;
	public static Item mugPerry;
	public static Item mugCherryLiqueur;
	public static Item mugRum;
	public static Item mugAthelasBrew;
	public static Item armorStandItem;
	public static Item pebble;
	public static Item sling;
	public static Item mysteryWeb;
	public static Item mugDwarvenTonic;
	public static Item helmetRanger;
	public static Item bodyRanger;
	public static Item legsRanger;
	public static Item bootsRanger;
	public static Item helmetDunlending;
	public static Item bodyDunlending;
	public static Item legsDunlending;
	public static Item bootsDunlending;
	public static Item dunlendingClub;
	public static Item dunlendingTrident;
	public static Item entDraught;
	public static Item mugDwarvenAle;
	public static Item maggotyBread;
	public static Item morgulSteel;
	public static Item morgulBlade;
	public static Item helmetMorgul;
	public static Item bodyMorgul;
	public static Item legsMorgul;
	public static Item bootsMorgul;
	public static Item leatherHat;
	public static Item featherDyed;
	public static Item mattockDwarven;
	public static Item orcBedItem;
	public static Item shovelWoodElven;
	public static Item pickaxeWoodElven;
	public static Item axeWoodElven;
	public static Item swordWoodElven;
	public static Item hoeWoodElven;
	public static Item daggerWoodElven;
	public static Item spearWoodElven;
	public static Item helmetWoodElven;
	public static Item bodyWoodElven;
	public static Item legsWoodElven;
	public static Item bootsWoodElven;
	public static Item rabbitRaw;
	public static Item rabbitCooked;
	public static Item rabbitStew;
	public static Item mugVodka;
	public static Item sulfur;
	public static Item saltpeter;
	public static Item commandSword;
	public static Item hobbitPancake;
	public static Item bottlePoison;
	public static Item daggerBronzePoisoned;
	public static Item daggerIronPoisoned;
	public static Item daggerMithrilPoisoned;
	public static Item daggerGondorPoisoned;
	public static Item daggerElvenPoisoned;
	public static Item daggerDwarvenPoisoned;
	public static Item daggerRohanPoisoned;
	public static Item daggerWoodElvenPoisoned;
	public static Item banner;
	public static Item sulfurMatch;
	public static Item swordAngmar;
	public static Item daggerAngmar;
	public static Item daggerAngmarPoisoned;
	public static Item battleaxeAngmar;
	public static Item hammerAngmar;
	public static Item spearAngmar;
	public static Item shovelAngmar;
	public static Item pickaxeAngmar;
	public static Item axeAngmar;
	public static Item hoeAngmar;
	public static Item helmetAngmar;
	public static Item bodyAngmar;
	public static Item legsAngmar;
	public static Item bootsAngmar;
	public static Item mango;
	public static Item mugMangoJuice;
	public static Item banana;
	public static Item bananaBread;
	public static Item bananaCakeItem;
	public static Item lionFur;
	public static Item lionRaw;
	public static Item lionCooked;
	public static Item zebraRaw;
	public static Item zebraCooked;
	public static Item rhinoRaw;
	public static Item rhinoCooked;
	public static Item rhinoHorn;
	public static Item battleaxeRohan;
	public static Item lionBedItem;
	public static Item scimitarNearHarad;
	public static Item helmetNearHarad;
	public static Item bodyNearHarad;
	public static Item legsNearHarad;
	public static Item bootsNearHarad;
	public static Item gemsbokHide;
	public static Item gemsbokHorn;
	public static Item helmetGemsbok;
	public static Item bodyGemsbok;
	public static Item legsGemsbok;
	public static Item bootsGemsbok;
	public static Item mapleSyrup;
	public static Item hobbitPancakeMapleSyrup;
	public static Item mugMapleBeer;
	public static Item helmetHighElven;
	public static Item bodyHighElven;
	public static Item legsHighElven;
	public static Item bootsHighElven;
	public static Item shovelHighElven;
	public static Item pickaxeHighElven;
	public static Item axeHighElven;
	public static Item swordHighElven;
	public static Item hoeHighElven;
	public static Item daggerHighElven;
	public static Item daggerHighElvenPoisoned;
	public static Item spearHighElven;
	public static Item highElvenBedItem;
	public static Item daggerNearHarad;
	public static Item daggerNearHaradPoisoned;
	public static Item spearNearHarad;
	public static Item nearHaradBow;
	public static Item date;
	public static Item mugAraq;
	public static Item blueDwarfSteel;
	public static Item shovelBlueDwarven;
	public static Item pickaxeBlueDwarven;
	public static Item axeBlueDwarven;
	public static Item swordBlueDwarven;
	public static Item hoeBlueDwarven;
	public static Item daggerBlueDwarven;
	public static Item daggerBlueDwarvenPoisoned;
	public static Item battleaxeBlueDwarven;
	public static Item hammerBlueDwarven;
	public static Item mattockBlueDwarven;
	public static Item throwingAxeBlueDwarven;
	public static Item helmetBlueDwarven;
	public static Item bodyBlueDwarven;
	public static Item legsBlueDwarven;
	public static Item bootsBlueDwarven;
	public static Item dwarvenRing;
	
	public static int idDimension;
	public static boolean alwaysShowAlignment;
	public static int alignmentXOffset;
	public static int alignmentYOffset;
	public static boolean alignmentMoveWhenBoss;
	public static boolean displayAlignmentAboveHead;
	public static boolean enableLOTRSky;
	public static boolean enableMistyMountainsMist;
	
	public static List unnamedItems = new ArrayList();
	private static List woodenSlabRecipes = new ArrayList();
	private LOTRTickHandlerServer serverTickHandler = new LOTRTickHandlerServer();
	private LOTREventHandler modEventHandler = new LOTREventHandler();
	
	@Mod.EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		rock = new LOTRBlockRock().setHardness(1.5F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("lotr:rock");
		oreCopper = new LOTRBlockOre().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:oreCopper");
		oreTin = new LOTRBlockOre().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:oreTin");
		oreSilver = new LOTRBlockOre().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:oreSilver");
		oreMithril = new LOTRBlockOre().setHardness(4F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("lotr:oreMithril");
		beacon = new LOTRBlockBeacon().setHardness(0F).setResistance(5F).setStepSound(Block.soundTypeWood).setBlockName("lotr:beacon");
		simbelmyne = new LOTRBlockFlower().setStepSound(Block.soundTypeGrass).setBlockName("lotr:simbelmyne");
		wood = new LOTRBlockWood().setHardness(2F).setStepSound(Block.soundTypeWood).setBlockName("lotr:wood");
		leaves = new LOTRBlockLeaves().setStepSound(Block.soundTypeGrass).setBlockName("lotr:leaves");
		planks = new LOTRBlockPlanks().setHardness(2F).setResistance(5F).setStepSound(Block.soundTypeWood).setBlockName("lotr:planks");
		sapling = new LOTRBlockSapling().setStepSound(Block.soundTypeGrass).setBlockName("lotr:sapling");
		woodSlabSingle = new LOTRBlockWoodSlab(false).setHardness(2F).setResistance(5F).setStepSound(Block.soundTypeWood).setBlockName("lotr:woodSlabSingle");
		woodSlabDouble = new LOTRBlockWoodSlab(true).setHardness(2F).setResistance(5F).setStepSound(Block.soundTypeWood).setBlockName("lotr:woodSlabDouble");
		stairsShirePine = new LOTRBlockStairs(planks, 0).setBlockName("lotr:stairsPine");
		shireHeather = new LOTRBlockFlower().setFlowerBounds(0.1F, 0F, 0.1F, 0.9F, 0.7F, 0.9F).setHardness(0F).setStepSound(Block.soundTypeGrass).setBlockName("lotr:shireHeather");
		brick = new LOTRBlockBrick().setHardness(1.5F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("lotr:brick");
		appleCrumble = new LOTRBlockPlaceableFood().setHardness(0.5F).setStepSound(Block.soundTypeCloth).setBlockName("lotr:appleCrumble");
		hobbitOven = new LOTRBlockHobbitOven().setHardness(3.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:hobbitOven");
		blockOreStorage = new LOTRBlockOreStorage().setHardness(5F).setResistance(10F).setStepSound(Block.soundTypeMetal).setBlockName("lotr:oreStorage");
		oreNaurite = new LOTRBlockOre().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:oreNaurite");
		oreMorgulIron = new LOTRBlockOreMordorVariant().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:oreMorgulIron");
		morgulTable = new LOTRBlockMorgulTable().setHardness(2.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:morgulCraftingTable");
		chandelier = new LOTRBlockChandelier().setHardness(0F).setResistance(2F).setStepSound(Block.soundTypeStone).setBlockName("lotr:chandelier");
		pipeweedPlant = new LOTRBlockPipeweedPlant().setHardness(0F).setStepSound(Block.soundTypeGrass).setBlockName("lotr:pipeweedPlant");
		pipeweedCrop = new LOTRBlockPipeweedCrop().setBlockName("lotr:pipeweed");
		slabSingle = new LOTRBlockSlab(false).setHardness(2F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("lotr:slabSingle");
		slabDouble = new LOTRBlockSlab(true).setHardness(2F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("lotr:slabDouble");
		stairsMordorBrick = new LOTRBlockStairs(brick, 0).setBlockName("lotr:stairsMordorBrick");
		stairsGondorBrick = new LOTRBlockStairs(brick, 1).setBlockName("lotr:stairsGondorBrick");
		wall = new LOTRBlockWall().setBlockName("lotr:wallStone");
		barrel = new LOTRBlockBarrel().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeWood).setBlockName("lotr:barrel");
		lettuceCrop = new LOTRBlockLettuceCrop().setBlockName("lotr:lettuce");
		orcBomb = new LOTRBlockOrcBomb().setHardness(3F).setResistance(0F).setStepSound(Block.soundTypeMetal).setBlockName("lotr:orcBomb");
		orcTorch = new LOTRBlockOrcTorch().setHardness(0F).setStepSound(Block.soundTypeWood).setBlockName("lotr:orcTorch");
		elanor = new LOTRBlockFlower().setStepSound(Block.soundTypeGrass).setBlockName("lotr:elanor");
		niphredil = new LOTRBlockFlower().setStepSound(Block.soundTypeGrass).setBlockName("lotr:niphredil");
		stairsMallorn = new LOTRBlockStairs(planks, 1).setBlockName("lotr:stairsMallorn");
		elvenTable = new LOTRBlockElvenTable().setHardness(2.5F).setStepSound(Block.soundTypeWood).setBlockName("lotr:elvenCraftingTable");
		mobSpawner = new LOTRBlockMobSpawner().setHardness(5F).setStepSound(Block.soundTypeMetal).setBlockName("lotr:mobSpawner");
		mallornLadder = new LOTRBlockLadder().setHardness(0.4F).setStepSound(Block.soundTypeLadder).setBlockName("lotr:mallornLadder");
		plateBlock = new LOTRBlockPlate().setHardness(0F).setStepSound(Block.soundTypeStone).setBlockName("lotr:plate");
		orcSteelBars = new LOTRBlockPane("lotr:orcSteelBars", "lotr:orcSteelBars", Material.iron, true).setHardness(5F).setResistance(10F).setStepSound(Block.soundTypeMetal).setBlockName("lotr:orcSteelBars");
		stairsGondorBrickMossy = new LOTRBlockStairs(brick, 2).setBlockName("lotr:stairsGondorBrickMossy");
		stairsGondorBrickCracked = new LOTRBlockStairs(brick, 3).setBlockName("lotr:stairsGondorBrickCracked");
		athelas = new LOTRBlockFlower().setFlowerBounds(0.1F, 0F, 0.1F, 0.9F, 0.7F, 0.9F).setHardness(0F).setStepSound(Block.soundTypeGrass).setBlockName("lotr:athelas");
		stalactite = new LOTRBlockStalactite().setHardness(1.5F).setResistance(5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:stalactite");
		stairsRohanBrick = new LOTRBlockStairs(brick, 4).setBlockName("lotr:stairsRohanBrick");
		oreQuendite = new LOTRBlockOre().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:oreQuendite");
		mallornTorch = new LOTRBlockElvenTorch().setHardness(0F).setStepSound(Block.soundTypeWood).setLightLevel(0.875F).setBlockName("lotr:mallornTorch");
		spawnerChest = new LOTRBlockSpawnerChest().setHardness(2.5F).setStepSound(Block.soundTypeWood).setBlockName("lotr:spawnerChest");
		quenditeGrass = new LOTRBlockQuenditeGrass().setHardness(3F).setStepSound(Block.soundTypeGrass).setBlockName("lotr:quenditeGrass");
		pressurePlateMordorRock = new LOTRBlockPressurePlate("lotr:rock_mordor", Material.rock, BlockPressurePlate.Sensitivity.mobs).setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:pressurePlateMordorRock");
		pressurePlateGondorRock = new LOTRBlockPressurePlate("lotr:rock_gondor", Material.rock, BlockPressurePlate.Sensitivity.mobs).setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:pressurePlateGondorRock");
		buttonMordorRock = new LOTRBlockButton(false, "lotr:rock_mordor").setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:buttonMordorRock");
		buttonGondorRock = new LOTRBlockButton(false, "lotr:rock_gondor").setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:buttonGondorRock");
		elvenPortal = new LOTRBlockElvenPortal().setHardness(-1F).setStepSound(Block.soundTypeGlass).setLightLevel(0.875F).setBlockName("lotr:elvenPortal");
		flowerPot = new LOTRBlockFlowerPot().setHardness(0F).setStepSound(Block.soundTypeStone).setBlockName("lotr:flowerPot");
		stairsDwarvenBrick = new LOTRBlockStairs(brick, 6).setBlockName("lotr:stairsDwarvenBrick");
		elvenBed = new LOTRBlockBed(planks, 1).setHardness(0.2F).setStepSound(Block.soundTypeWood).setBlockName("lotr:elvenBed");
		pillar = new LOTRBlockPillar().setHardness(1.5F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("lotr:pillar");
		oreGlowstone = new LOTRBlockOre().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone).setLightLevel(0.75F).setBlockName("lotr:oreGlowstone");
		fruitWood = new LOTRBlockFruitWood().setHardness(2F).setStepSound(Block.soundTypeWood).setBlockName("lotr:fruitWood");
		fruitLeaves = new LOTRBlockFruitLeaves().setStepSound(Block.soundTypeGrass).setBlockName("lotr:fruitLeaves");
		fruitSapling = new LOTRBlockFruitSapling().setStepSound(Block.soundTypeGrass).setBlockName("lotr:fruitSapling");
		stairsApple = new LOTRBlockStairs(planks, 4).setBlockName("lotr:stairsApple");
		stairsPear = new LOTRBlockStairs(planks, 5).setBlockName("lotr:stairsPear");
		stairsCherry = new LOTRBlockStairs(planks, 6).setBlockName("lotr:stairsCherry");
		dwarvenTable = new LOTRBlockDwarvenTable().setHardness(2.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:dwarvenCraftingTable");
		bluebell = new LOTRBlockFlower().setStepSound(Block.soundTypeGrass).setBlockName("lotr:bluebell");
		dwarvenForge = new LOTRBlockDwarvenForge().setHardness(3.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:dwarvenForge");
		hearth = new LOTRBlockHearth().setHardness(1F).setResistance(8F).setStepSound(Block.soundTypeStone).setBlockName("lotr:hearth");
		morgulShroom = new LOTRBlockMorgulShroom().setStepSound(Block.soundTypeGrass).setBlockName("lotr:morgulShroom");
		urukTable = new LOTRBlockUrukTable().setHardness(2.5F).setStepSound(Block.soundTypeMetal).setBlockName("lotr:urukCraftingTable");
		cherryPie = new LOTRBlockPlaceableFood().setHardness(0.5F).setStepSound(Block.soundTypeCloth).setBlockName("lotr:cherryPie");
		clover = new LOTRBlockClover().setStepSound(Block.soundTypeGrass).setBlockName("lotr:clover");
		slabSingle2 = new LOTRBlockSlab2(false).setHardness(2F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("lotr:slabSingle2");
		slabDouble2 = new LOTRBlockSlab2(true).setHardness(2F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("lotr:slabDouble2");
		stairsMirkOak = new LOTRBlockStairs(planks, 2).setBlockName("lotr:stairsMirkOak");
		webUngoliant = new BlockWeb().setLightOpacity(2).setHardness(2F).setCreativeTab(LOTRCreativeTabs.tabDeco).setBlockName("lotr:webUngoliant");
		woodElvenTable = new LOTRBlockWoodElvenTable().setHardness(2.5F).setStepSound(Block.soundTypeWood).setBlockName("lotr:woodElvenCraftingTable");
		woodElvenBed = new LOTRBlockBed(planks, 2).setHardness(0.2F).setStepSound(Block.soundTypeWood).setBlockName("lotr:woodElvenBed");
		gondorianTable = new LOTRBlockGondorianTable().setHardness(2.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:gondorianCraftingTable");
		woodElvenTorch = new LOTRBlockWoodElvenTorch().setHardness(0F).setStepSound(Block.soundTypeWood).setLightLevel(0.9375F).setBlockName("lotr:woodElvenTorch");
		marshLights = new LOTRBlockMarshLights().setBlockName("lotr:marshLights");
		rohirricTable = new LOTRBlockRohirricTable().setHardness(2.5F).setStepSound(Block.soundTypeWood).setBlockName("lotr:rohirricCraftingTable");
		pressurePlateRohanRock = new LOTRBlockPressurePlate("lotr:rock_rohan", Material.rock, BlockPressurePlate.Sensitivity.mobs).setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:pressurePlateRohanRock");
		remains = new LOTRBlockRemains().setHardness(3F).setStepSound(Block.soundTypeGravel).setBlockName("lotr:remains");
		deadPlant = new LOTRBlockDeadPlant().setStepSound(Block.soundTypeGrass).setBlockName("lotr:deadMarshPlant");
		oreGulduril = new LOTRBlockOreMordorVariant().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone).setLightLevel(0.75F).setBlockName("lotr:oreGulduril");
		guldurilBrick = new LOTRBlockGuldurilBrick().setHardness(3F).setResistance(10F).setStepSound(Block.soundTypeStone).setLightLevel(0.75F).setBlockName("lotr:guldurilBrick");
		dwarvenDoor = new LOTRBlockDwarvenDoor().setHardness(4F).setStepSound(Block.soundTypeStone).setBlockName("lotr:dwarvenDoor");
		stairsCharred = new LOTRBlockStairs(planks, 3).setBlockName("lotr:stairsCharred");
		dwarvenBed = new LOTRBlockBed(Blocks.planks, 1).setHardness(0.2F).setStepSound(Block.soundTypeWood).setBlockName("lotr:dwarvenBed");
		morgulPortal = new LOTRBlockMorgulPortal().setHardness(-1F).setStepSound(Block.soundTypeGlass).setLightLevel(0.875F).setBlockName("lotr:morgulPortal");
		armorStand = new LOTRBlockArmorStand().setHardness(0F).setResistance(1F).setStepSound(Block.soundTypeStone).setBlockName("lotr:armorStand");
		buttonRohanRock = new LOTRBlockButton(false, "lotr:rock_rohan").setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:buttonRohanRock");
		asphodel = new LOTRBlockFlower().setStepSound(Block.soundTypeGrass).setBlockName("lotr:asphodel");
		wood2 = new LOTRBlockWood2().setHardness(2F).setStepSound(Block.soundTypeWood).setBlockName("lotr:wood2");
		leaves2 = new LOTRBlockLeaves2().setStepSound(Block.soundTypeGrass).setBlockName("lotr:leaves2");
		sapling2 = new LOTRBlockSapling2().setStepSound(Block.soundTypeGrass).setBlockName("lotr:sapling2");
		stairsLebethron = new LOTRBlockStairs(planks, 8).setBlockName("lotr:stairsLebethron");
		woodSlabSingle2 = new LOTRBlockWoodSlab2(false).setHardness(2F).setResistance(5F).setStepSound(Block.soundTypeWood).setBlockName("lotr:woodSlabSingle2");
		woodSlabDouble2 = new LOTRBlockWoodSlab2(true).setHardness(2F).setResistance(5F).setStepSound(Block.soundTypeWood).setBlockName("lotr:woodSlabDouble2");
		dwarfHerb = new LOTRBlockFlower().setStepSound(Block.soundTypeGrass).setBlockName("lotr:dwarfHerb");
		mugBlock = new LOTRBlockMug().setHardness(0F).setStepSound(Block.soundTypeStone).setBlockName("lotr:mug");
		dunlendingTable = new LOTRBlockDunlendingTable().setHardness(2.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:dunlendingCraftingTable");
		stairsBeech = new LOTRBlockStairs(planks, 9).setBlockName("lotr:stairsBeech");
		entJar = new LOTRBlockEntJar().setHardness(1F).setStepSound(Block.soundTypeGlass).setBlockName("lotr:entJar");
		mordorThorn = new LOTRBlockMordorThorn().setStepSound(Block.soundTypeGrass).setBlockName("lotr:mordorThorn");
		mordorMoss = new LOTRBlockMordorMoss().setHardness(0.2F).setStepSound(Block.soundTypeGrass).setBlockName("lotr:mordorMoss");
		stairsMordorBrickCracked = new LOTRBlockStairs(brick, 7).setBlockName("lotr:stairsMordorBrickCracked");
		orcForge = new LOTRBlockOrcForge().setHardness(3.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:orcForge");
		trollTotem = new LOTRBlockTrollTotem().setHardness(5F).setResistance(20F).setStepSound(Block.soundTypeStone).setBlockName("lotr:trollTotem");
		orcBed = new LOTRBlockBed(LOTRMod.planks, 3).setHardness(0.2F).setStepSound(Block.soundTypeWood).setBlockName("lotr:orcBed");
		stairsElvenBrick = new LOTRBlockStairs(brick, 11).setBlockName("lotr:stairsElvenBrick");
		stairsElvenBrickMossy = new LOTRBlockStairs(brick, 12).setBlockName("lotr:stairsElvenBrickMossy");
		stairsElvenBrickCracked = new LOTRBlockStairs(brick, 13).setBlockName("lotr:stairsElvenBrickCracked");
		stairsHolly = new LOTRBlockStairs(planks, 10).setBlockName("lotr:stairsHolly");
		pressurePlateBlueRock = new LOTRBlockPressurePlate("lotr:rock_blue", Material.rock, BlockPressurePlate.Sensitivity.mobs).setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:pressurePlateBlueRock");
		buttonBlueRock = new LOTRBlockButton(false, "lotr:rock_blue").setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:buttonBlueRock");
		slabSingle3 = new LOTRBlockSlab3(false).setHardness(2F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("lotr:slabSingle3");
		slabDouble3 = new LOTRBlockSlab3(true).setHardness(2F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("lotr:slabDouble3");
		stairsBlueRockBrick = new LOTRBlockStairs(brick, 14).setBlockName("lotr:stairsBlueRockBrick");
		fence = new LOTRBlockFence().setHardness(2F).setResistance(5F).setStepSound(Block.soundTypeWood).setBlockName("lotr:fence");
		doubleFlower = new LOTRBlockDoubleFlower().setBlockName("lotr:doubleFlower");
		oreSulfur = new LOTRBlockOre().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:oreSulfur");
		oreSaltpeter = new LOTRBlockOre().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:oreSaltpeter");
		quagmire = new LOTRBlockQuagmire().setHardness(1F).setStepSound(Block.soundTypeGravel).setBlockName("lotr:quagmire");
		angmarTable = new LOTRBlockAngmarTable().setHardness(2.5F).setStepSound(Block.soundTypeMetal).setBlockName("lotr:angmarCraftingTable");
		brick2 = new LOTRBlockBrick2().setHardness(1.5F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("lotr:brick2");
		wall2 = new LOTRBlockWall2().setBlockName("lotr:wallStone2");
		stairsAngmarBrick = new LOTRBlockStairs(brick2, 0).setBlockName("lotr:stairsAngmarBrick");
		stairsAngmarBrickCracked = new LOTRBlockStairs(brick2, 1).setBlockName("lotr:stairsAngmarBrickCracked");
		stairsMango = new LOTRBlockStairs(planks, 7).setBlockName("lotr:stairsMango");
		stairsBanana = new LOTRBlockStairs(planks, 11).setBlockName("lotr:stairsBanana");
		bananaBlock = new LOTRBlockBanana().setHardness(0F).setResistance(1F).setStepSound(Block.soundTypeWood).setBlockName("lotr:banana");
		bananaCake = new LOTRBlockPlaceableFood().setHardness(0.5F).setStepSound(Block.soundTypeCloth).setBlockName("lotr:bananaCake");
		lionBed = new LOTRBlockBed(Blocks.planks, 0).setHardness(0.2F).setStepSound(Block.soundTypeWood).setBlockName("lotr:lionBed");
		wood3 = new LOTRBlockWood3().setHardness(2F).setStepSound(Block.soundTypeWood).setBlockName("lotr:wood3");
		leaves3 = new LOTRBlockLeaves3().setStepSound(Block.soundTypeGrass).setBlockName("lotr:leaves3");
		sapling3 = new LOTRBlockSapling3().setStepSound(Block.soundTypeGrass).setBlockName("lotr:sapling3");
		stairsMaple = new LOTRBlockStairs(planks, 12).setBlockName("lotr:stairsMaple");
		stairsLarch = new LOTRBlockStairs(planks, 13).setBlockName("lotr:stairsLarch");
		nearHaradTable = new LOTRBlockNearHaradTable().setHardness(2.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:nearHaradCraftingTable");
		highElvenTable = new LOTRBlockHighElvenTable().setHardness(2.5F).setStepSound(Block.soundTypeWood).setBlockName("lotr:highElvenCraftingTable");
		highElvenTorch = new LOTRBlockElvenTorch().setHardness(0F).setStepSound(Block.soundTypeWood).setLightLevel(0.875F).setBlockName("lotr:highElvenTorch");
		highElvenBed = new LOTRBlockBed(Blocks.planks, 0).setHardness(0.2F).setStepSound(Block.soundTypeWood).setBlockName("lotr:highElvenBed");
		pressurePlateRedRock = new LOTRBlockPressurePlate("lotr:rock_red", Material.rock, BlockPressurePlate.Sensitivity.mobs).setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:pressurePlateRedRock");
		buttonRedRock = new LOTRBlockButton(false, "lotr:rock_red").setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:buttonRedRock");
		stairsRedRockBrick = new LOTRBlockStairs(brick2, 2).setBlockName("lotr:stairsRedRockBrick");
		slabSingle4 = new LOTRBlockSlab4(false).setHardness(2F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("lotr:slabSingle4");
		slabDouble4 = new LOTRBlockSlab4(true).setHardness(2F).setResistance(10F).setStepSound(Block.soundTypeStone).setBlockName("lotr:slabDouble4");
		stairsNearHaradBrick = new LOTRBlockStairs(brick, 15).setBlockName("lotr:stairsNearHaradBrick");
		stairsDatePalm = new LOTRBlockStairs(planks, 14).setBlockName("lotr:stairsDatePalm");
		dateBlock = new LOTRBlockDate().setHardness(0F).setResistance(1F).setStepSound(Block.soundTypeWood).setBlockName("lotr:date");
		blueDwarvenTable = new LOTRBlockBlueDwarvenTable().setHardness(2.5F).setStepSound(Block.soundTypeStone).setBlockName("lotr:blueDwarvenCraftingTable");
		goran = new LOTRBlockGoran().setHardness(0F).setStepSound(Block.soundTypeStone).setBlockName("lotr:goran");
		thatch = new LOTRBlockThatch().setHardness(0.5F).setStepSound(Block.soundTypeGrass).setCreativeTab(LOTRCreativeTabs.tabBlock).setBlockName("lotr:thatch");
		slabSingleThatch = new LOTRBlockSlabThatch(false).setHardness(0.5F).setStepSound(Block.soundTypeGrass).setBlockName("lotr:slabSingleThatch");
		slabDoubleThatch = new LOTRBlockSlabThatch(true).setHardness(0.5F).setStepSound(Block.soundTypeGrass).setBlockName("lotr:slabDoubleThatch");
		stairsThatch = new LOTRBlockStairs(thatch, 0).setBlockName("lotr:stairsThatch");
		fangornPlant = new LOTRBlockFangornPlant().setStepSound(Block.soundTypeGrass).setBlockName("lotr:fangornPlant");
		fangornRiverweed = new LOTRBlockFangornRiverweed().setStepSound(Block.soundTypeGrass).setBlockName("lotr:fangornRiverweed");
		
		goldRing = new Item().setCreativeTab(LOTRCreativeTabs.tabMagic).setUnlocalizedName("lotr:goldRing");
		pouch = new LOTRItemPouch().setUnlocalizedName("lotr:pouch");
		copper = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:copper");
		tin = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:tin");
		bronze = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:bronze");
		silver = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:silver");
		mithril = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:mithril");
		shovelBronze = new LOTRItemShovel(toolBronze).setUnlocalizedName("lotr:shovelBronze");
		pickaxeBronze = new LOTRItemPickaxe(toolBronze).setUnlocalizedName("lotr:pickaxeBronze");
		axeBronze = new LOTRItemAxe(toolBronze).setUnlocalizedName("lotr:axeBronze");
		swordBronze = new LOTRItemSword(toolBronze).setUnlocalizedName("lotr:swordBronze");
		hoeBronze = new LOTRItemHoe(toolBronze).setUnlocalizedName("lotr:hoeBronze");
		helmetBronze = new LOTRItemArmor(armorBronze, 0).setUnlocalizedName("lotr:helmetBronze");
		bodyBronze = new LOTRItemArmor(armorBronze, 1).setUnlocalizedName("lotr:bodyBronze");
		legsBronze = new LOTRItemArmor(armorBronze, 2).setUnlocalizedName("lotr:legsBronze");
		bootsBronze = new LOTRItemArmor(armorBronze, 3).setUnlocalizedName("lotr:bootsBronze");
		silverNugget = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:silverNugget");
		silverRing = new Item().setCreativeTab(LOTRCreativeTabs.tabMagic).setUnlocalizedName("lotr:silverRing");
		mithrilNugget = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:mithrilNugget");
		mithrilRing = new Item().setCreativeTab(LOTRCreativeTabs.tabMagic).setUnlocalizedName("lotr:mithrilRing");
		hobbitPipe = new LOTRItemHobbitPipe().setUnlocalizedName("lotr:hobbitPipe");
		pipeweed = new Item().setCreativeTab(LOTRCreativeTabs.tabMisc).setUnlocalizedName("lotr:pipeweed");
		clayMug = new Item().setCreativeTab(LOTRCreativeTabs.tabFood).setUnlocalizedName("lotr:clayMug");
		mug = new LOTRItemMug(false).setMaxStackSize(64).setUnlocalizedName("lotr:mug");
		mugWater = new LOTRItemMug(true).setUnlocalizedName("lotr:mugWater");
		mugMilk = new LOTRItemMug(true).setCuresEffects().setUnlocalizedName("lotr:mugMilk");
		mugAle = new LOTRItemMugBrewable(0.3F).setDrinkStats(3, 0.3F).setUnlocalizedName("lotr:mugAle");
		mugChocolate = new LOTRItemMug(false).setDrinkStats(6, 0.6F).setUnlocalizedName("lotr:mugChocolate");
		appleCrumbleItem = new ItemReed(appleCrumble).setMaxStackSize(1).setCreativeTab(LOTRCreativeTabs.tabFood).setUnlocalizedName("lotr:appleCrumble");
		mugMiruvor = new LOTRItemMugBrewable(0F).setDrinkStats(8, 0.8F).addPotionEffect(Potion.damageBoost.id, 40).addPotionEffect(Potion.moveSpeed.id, 40).setUnlocalizedName("lotr:mugMiruvor");
		mugOrcDraught = new LOTRItemMugBrewable(0F).setDrinkStats(6, 0.6F).addPotionEffect(Potion.damageBoost.id, 60).addPotionEffect(Potion.moveSpeed.id, 60).setDamageAmount(2).setUnlocalizedName("lotr:mugOrcDraught");
		scimitarOrc = new LOTRItemSword(toolOrc).setUnlocalizedName("lotr:scimitarOrc");
		helmetOrc = new LOTRItemArmor(armorOrc, 0).setUnlocalizedName("lotr:helmetOrc");
		bodyOrc = new LOTRItemArmor(armorOrc, 1).setUnlocalizedName("lotr:bodyOrc");
		legsOrc = new LOTRItemArmor(armorOrc, 2).setUnlocalizedName("lotr:legsOrc");
		bootsOrc = new LOTRItemArmor(armorOrc, 3).setUnlocalizedName("lotr:bootsOrc");
		orcSteel = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:orcSteel");
		battleaxeOrc = new LOTRItemBattleaxe(toolOrc).setUnlocalizedName("lotr:battleaxeOrc");
		lembas = new LOTRItemFood(20, 0.8F, false).setUnlocalizedName("lotr:lembas");
		nauriteGem = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:naurite");
		daggerOrc = new LOTRItemDagger(toolOrc, 0).setUnlocalizedName("lotr:daggerOrc");
		daggerOrcPoisoned = new LOTRItemDagger(toolOrc, 1).setUnlocalizedName("lotr:daggerOrcPoisoned");
		sting = new LOTRItemSting().setIsElvenBlade().setUnlocalizedName("lotr:sting");
		spawnEgg = new LOTRItemSpawnEgg().setUnlocalizedName("lotr:spawnEgg");
		pipeweedLeaf = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:pipeweedLeaf");
		pipeweedSeeds = new LOTRItemSeeds(pipeweedCrop, Blocks.farmland).setUnlocalizedName("lotr:pipeweedSeeds");
		structureSpawner = new LOTRItemStructureSpawner().setUnlocalizedName("lotr:structureSpawner");
		lettuce = new ItemSeedFood(3, 0.4F, lettuceCrop, Blocks.farmland).setCreativeTab(LOTRCreativeTabs.tabFood).setUnlocalizedName("lotr:lettuce");
		shovelMithril = new LOTRItemShovel(toolMithril).setUnlocalizedName("lotr:shovelMithril");
		pickaxeMithril = new LOTRItemPickaxe(toolMithril).setUnlocalizedName("lotr:pickaxeMithril");
		axeMithril = new LOTRItemAxe(toolMithril).setUnlocalizedName("lotr:axeMithril");
		swordMithril = new LOTRItemSword(toolMithril).setUnlocalizedName("lotr:swordMithril");
		hoeMithril = new LOTRItemHoe(toolMithril).setUnlocalizedName("lotr:hoeMithril");
		orcTorchItem = new LOTRItemOrcTorch().setCreativeTab(LOTRCreativeTabs.tabDeco).setUnlocalizedName("lotr:orcTorch");
		sauronMace = new LOTRItemSauronMace().setUnlocalizedName("lotr:sauronMace");
		gandalfStaffWhite = new LOTRItemGandalfStaffWhite().setUnlocalizedName("lotr:gandalfStaffWhite");
		swordGondor = new LOTRItemSword(toolGondor).setUnlocalizedName("lotr:swordGondor");
		helmetGondor = new LOTRItemArmor(armorGondor, 0).setUnlocalizedName("lotr:helmetGondor");
		bodyGondor = new LOTRItemArmor(armorGondor, 1).setUnlocalizedName("lotr:bodyGondor");
		legsGondor = new LOTRItemArmor(armorGondor, 2).setUnlocalizedName("lotr:legsGondor");
		bootsGondor = new LOTRItemArmor(armorGondor, 3).setUnlocalizedName("lotr:bootsGondor");
		helmetMithril = new LOTRItemArmor(armorMithril, 0).setUnlocalizedName("lotr:helmetMithril");
		bodyMithril = new LOTRItemArmor(armorMithril, 1).setUnlocalizedName("lotr:bodyMithril");
		legsMithril = new LOTRItemArmor(armorMithril, 2).setUnlocalizedName("lotr:legsMithril");
		bootsMithril = new LOTRItemArmor(armorMithril, 3).setUnlocalizedName("lotr:bootsMithril");
		spearGondor = new LOTRItemSpear(toolGondor).setUnlocalizedName("lotr:spearGondor");
		spearOrc = new LOTRItemSpear(toolOrc).setUnlocalizedName("lotr:spearOrc");
		spearBronze = new LOTRItemSpear(toolBronze).setUnlocalizedName("lotr:spearBronze");
		spearIron = new LOTRItemSpear(ToolMaterial.IRON).setUnlocalizedName("lotr:spearIron");
		spearMithril = new LOTRItemSpear(toolMithril).setUnlocalizedName("lotr:spearMithril");
		anduril = new LOTRItemAnduril().setUnlocalizedName("lotr:anduril");
		dye = new LOTRItemDye().setUnlocalizedName("lotr:dye");
		mallornStick = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setFull3D().setUnlocalizedName("lotr:mallornStick");
		shovelMallorn = new LOTRItemShovel(toolMallorn).setUnlocalizedName("lotr:shovelMallorn");
		pickaxeMallorn = new LOTRItemPickaxe(toolMallorn).setUnlocalizedName("lotr:pickaxeMallorn");
		axeMallorn = new LOTRItemAxe(toolMallorn).setUnlocalizedName("lotr:axeMallorn");
		swordMallorn = new LOTRItemSword(toolMallorn).setUnlocalizedName("lotr:swordMallorn");
		hoeMallorn = new LOTRItemHoe(toolMallorn).setUnlocalizedName("lotr:hoeMallorn");
		shovelElven = new LOTRItemShovel(toolElven).setUnlocalizedName("lotr:shovelElven");
		pickaxeElven = new LOTRItemPickaxe(toolElven).setUnlocalizedName("lotr:pickaxeElven");
		axeElven = new LOTRItemAxe(toolElven).setUnlocalizedName("lotr:axeElven");
		swordElven = new LOTRItemSword(toolElven).setIsElvenBlade().setUnlocalizedName("lotr:swordElven");
		hoeElven = new LOTRItemHoe(toolElven).setUnlocalizedName("lotr:hoeElven");
		spearElven = new LOTRItemSpear(toolElven).setUnlocalizedName("lotr:spearElven");
		mallornBow = new LOTRItemBow(416, 0, 0.15F, 3, 20).setUnlocalizedName("lotr:mallornBow");
		helmetElven = new LOTRItemArmor(armorElven, 0).setUnlocalizedName("lotr:helmetElven");
		bodyElven = new LOTRItemArmor(armorElven, 1).setUnlocalizedName("lotr:bodyElven");
		legsElven = new LOTRItemArmor(armorElven, 2).setUnlocalizedName("lotr:legsElven");
		bootsElven = new LOTRItemArmor(armorElven, 3).setUnlocalizedName("lotr:bootsElven");
		silverCoin = new Item().setCreativeTab(LOTRCreativeTabs.tabMisc).setUnlocalizedName("lotr:coin");
		gammon = new LOTRItemFood(8, 0.8F, true).setUnlocalizedName("lotr:gammon");
		clayPlate = new Item().setCreativeTab(LOTRCreativeTabs.tabFood).setUnlocalizedName("lotr:clayPlate");
		plate = new LOTRItemPlate().setUnlocalizedName("lotr:plate");
		elvenBow = new LOTRItemBow(484, 0.25D, 0.5F, 5, 20).setUnlocalizedName("lotr:elvenBow");
		wargFur = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:wargFur");
		helmetWarg = new LOTRItemArmor(armorWarg, 0).setUnlocalizedName("lotr:helmetWarg");
		bodyWarg = new LOTRItemArmor(armorWarg, 1).setUnlocalizedName("lotr:bodyWarg");
		legsWarg = new LOTRItemArmor(armorWarg, 2).setUnlocalizedName("lotr:legsWarg");
		bootsWarg = new LOTRItemArmor(armorWarg, 3).setUnlocalizedName("lotr:bootsWarg");
		orcBow = new LOTRItemBow(440, 0.25D, 0F, 1, 20).setUnlocalizedName("lotr:orcBow");
		mugMead = new LOTRItemMugBrewable(0.6F).setDrinkStats(4, 0.4F).setUnlocalizedName("lotr:mugMead");
		wargskinRug = new LOTRItemWargskinRug().setUnlocalizedName("lotr:wargskinRug");
		quenditeCrystal = new LOTRItemQuenditeCrystal().setUnlocalizedName("lotr:quenditeCrystal");
		blacksmithHammer = new LOTRItemHammer(toolGondor).setUnlocalizedName("lotr:blacksmithHammer");
		daggerGondor = new LOTRItemDagger(toolGondor, 0).setUnlocalizedName("lotr:daggerGondor");
		daggerElven = new LOTRItemDagger(toolElven, 0).setIsElvenBlade().setUnlocalizedName("lotr:daggerElven");
		hobbitRing = new Item().setCreativeTab(LOTRCreativeTabs.tabMisc).setUnlocalizedName("lotr:hobbitRing");
		elvenBedItem = new LOTRItemBed(elvenBed).setUnlocalizedName("lotr:elvenBed");
		wargBone = new LOTRItemBone().setUnlocalizedName("lotr:wargBone");
		appleGreen = new LOTRItemFood(4, 0.3F, false).setUnlocalizedName("lotr:appleGreen");
		pear = new LOTRItemFood(4, 0.3F, false).setUnlocalizedName("lotr:pear");
		cherry = new LOTRItemFood(2, 0.2F, false).setUnlocalizedName("lotr:cherry");
		dwarfSteel = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:dwarfSteel");
		shovelDwarven = new LOTRItemShovel(toolDwarven).setUnlocalizedName("lotr:shovelDwarven");
		pickaxeDwarven = new LOTRItemPickaxe(toolDwarven).setUnlocalizedName("lotr:pickaxeDwarven");
		axeDwarven = new LOTRItemAxe(toolDwarven).setUnlocalizedName("lotr:axeDwarven");
		swordDwarven = new LOTRItemSword(toolDwarven).setUnlocalizedName("lotr:swordDwarven");
		hoeDwarven = new LOTRItemHoe(toolDwarven).setUnlocalizedName("lotr:hoeDwarven");
		daggerDwarven = new LOTRItemDagger(toolDwarven, 0).setUnlocalizedName("lotr:daggerDwarven");
		battleaxeDwarven = new LOTRItemBattleaxe(toolDwarven).setUnlocalizedName("lotr:battleaxeDwarven");
		hammerDwarven = new LOTRItemHammer(toolDwarven).setUnlocalizedName("lotr:hammerDwarven");
		shovelOrc = new LOTRItemShovel(toolOrc).setUnlocalizedName("lotr:shovelOrc");
		pickaxeOrc = new LOTRItemPickaxe(toolOrc).setUnlocalizedName("lotr:pickaxeOrc");
		axeOrc = new LOTRItemAxe(toolOrc).setUnlocalizedName("lotr:axeOrc");
		hoeOrc = new LOTRItemHoe(toolOrc).setUnlocalizedName("lotr:hoeOrc");
		hammerOrc = new LOTRItemHammer(toolOrc).setUnlocalizedName("lotr:hammerOrc");
		helmetDwarven = new LOTRItemArmor(armorDwarven, 0).setUnlocalizedName("lotr:helmetDwarven");
		bodyDwarven = new LOTRItemArmor(armorDwarven, 1).setUnlocalizedName("lotr:bodyDwarven");
		legsDwarven = new LOTRItemArmor(armorDwarven, 2).setUnlocalizedName("lotr:legsDwarven");
		bootsDwarven = new LOTRItemArmor(armorDwarven, 3).setUnlocalizedName("lotr:bootsDwarven");
		galvorn = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:galvorn");
		helmetGalvorn = new LOTRItemArmor(armorGalvorn, 0).setUnlocalizedName("lotr:helmetGalvorn");
		bodyGalvorn = new LOTRItemArmor(armorGalvorn, 1).setUnlocalizedName("lotr:bodyGalvorn");
		legsGalvorn = new LOTRItemArmor(armorGalvorn, 2).setUnlocalizedName("lotr:legsGalvorn");
		bootsGalvorn = new LOTRItemArmor(armorGalvorn, 3).setUnlocalizedName("lotr:bootsGalvorn");
		daggerBronze = new LOTRItemDagger(toolBronze, 0).setUnlocalizedName("lotr:daggerBronze");
		daggerIron = new LOTRItemDagger(ToolMaterial.IRON, 0).setUnlocalizedName("lotr:daggerIron");
		daggerMithril = new LOTRItemDagger(toolMithril, 0).setUnlocalizedName("lotr:daggerMithril");
		battleaxeMithril = new LOTRItemBattleaxe(toolMithril).setUnlocalizedName("lotr:battleaxeMithril");
		hammerMithril = new LOTRItemHammer(toolMithril).setUnlocalizedName("lotr:hammerMithril");
		hammerGondor = new LOTRItemHammer(toolGondor).setUnlocalizedName("lotr:hammerGondor");
		orcBone = new LOTRItemBone().setUnlocalizedName("lotr:orcBone");
		elfBone = new LOTRItemBone().setUnlocalizedName("lotr:elfBone");
		dwarfBone = new LOTRItemBone().setUnlocalizedName("lotr:dwarfBone");
		hobbitBone = new LOTRItemBone().setUnlocalizedName("lotr:hobbitBone");
		commandHorn = new LOTRItemCommandHorn().setUnlocalizedName("lotr:commandHorn");
		throwingAxeDwarven = new LOTRItemThrowingAxe(toolDwarven).setUnlocalizedName("lotr:throwingAxeDwarven");
		urukSteel = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:urukSteel");
		shovelUruk = new LOTRItemShovel(toolUruk).setUnlocalizedName("lotr:shovelUruk");
		pickaxeUruk = new LOTRItemPickaxe(toolUruk).setUnlocalizedName("lotr:pickaxeUruk");
		axeUruk = new LOTRItemAxe(toolUruk).setUnlocalizedName("lotr:axeUruk");
		scimitarUruk = new LOTRItemSword(toolUruk).setUnlocalizedName("lotr:scimitarUruk");
		hoeUruk = new LOTRItemHoe(toolUruk).setUnlocalizedName("lotr:hoeUruk");
		daggerUruk = new LOTRItemDagger(toolUruk, 0).setUnlocalizedName("lotr:daggerUruk");
		daggerUrukPoisoned = new LOTRItemDagger(toolUruk, 1).setUnlocalizedName("lotr:daggerUrukPoisoned");
		battleaxeUruk = new LOTRItemBattleaxe(toolUruk).setUnlocalizedName("lotr:battleaxeUruk");
		hammerUruk = new LOTRItemHammer(toolUruk).setUnlocalizedName("lotr:hammerUruk");
		spearUruk = new LOTRItemSpear(toolUruk).setUnlocalizedName("lotr:spearUruk");
		helmetUruk = new LOTRItemArmor(armorUruk, 0).setUnlocalizedName("lotr:helmetUruk");
		bodyUruk = new LOTRItemArmor(armorUruk, 1).setUnlocalizedName("lotr:bodyUruk");
		legsUruk = new LOTRItemArmor(armorUruk, 2).setUnlocalizedName("lotr:legsUruk");
		bootsUruk = new LOTRItemArmor(armorUruk, 3).setUnlocalizedName("lotr:bootsUruk");
		crossbowBolt = new Item().setCreativeTab(LOTRCreativeTabs.tabCombat).setUnlocalizedName("lotr:crossbowBolt");
		urukCrossbow = new LOTRItemCrossbow(470, 0.4D, toolUruk, 1).setUnlocalizedName("lotr:urukCrossbow");
		cherryPieItem = new ItemReed(cherryPie).setMaxStackSize(1).setCreativeTab(LOTRCreativeTabs.tabFood).setUnlocalizedName("lotr:cherryPie");
		trollBone = new LOTRItemBone().setUnlocalizedName("lotr:trollBone");
		trollStatue = new LOTRItemTrollStatue().setUnlocalizedName("lotr:trollStatue");
		wargHelmet = new LOTRItemWargArmor(materialWargArmor, 0).setUnlocalizedName("lotr:wargHelmet");
		wargChestplate = new LOTRItemWargArmor(materialWargArmor, 1).setUnlocalizedName("lotr:wargChestplate");
		wargHindplate = new LOTRItemWargArmor(materialWargArmor, 2).setUnlocalizedName("lotr:wargHindplate");
		ironCrossbow = new LOTRItemCrossbow(356, 0D, ToolMaterial.IRON, 3).setUnlocalizedName("lotr:ironCrossbow");
		mithrilCrossbow = new LOTRItemCrossbow(1760, 0.5D, toolMithril, 6).setUnlocalizedName("lotr:mithrilCrossbow");
		woodElvenBedItem = new LOTRItemBed(woodElvenBed).setUnlocalizedName("lotr:woodElvenBed");
		helmetWoodElvenScout = new LOTRItemArmor(armorWoodElvenScout, 0).setUnlocalizedName("lotr:helmetWoodElvenScout");
		bodyWoodElvenScout = new LOTRItemArmor(armorWoodElvenScout, 1).setUnlocalizedName("lotr:bodyWoodElvenScout");
		legsWoodElvenScout = new LOTRItemArmor(armorWoodElvenScout, 2).setUnlocalizedName("lotr:legsWoodElvenScout");
		bootsWoodElvenScout = new LOTRItemArmor(armorWoodElvenScout, 3).setUnlocalizedName("lotr:bootsWoodElvenScout");
		mirkwoodBow = new LOTRItemBow(416, 0D, 0.25F, 10, 15).setUnlocalizedName("lotr:mirkwoodBow");
		mugRedWine = new LOTRItemMugBrewable(0.9F).setDrinkStats(4, 0.4F).setUnlocalizedName("lotr:mugRedWine");
		ancientItemParts = new LOTRItemAncientItemParts().setUnlocalizedName("lotr:ancientParts");
		ancientItem = new LOTRItemAncientItem().setUnlocalizedName("lotr:ancient");
		swordRohan = new LOTRItemSword(toolRohan).setUnlocalizedName("lotr:swordRohan");
		daggerRohan = new LOTRItemDagger(toolRohan, 0).setUnlocalizedName("lotr:daggerRohan");
		spearRohan = new LOTRItemSpear(toolRohan).setUnlocalizedName("lotr:spearRohan");
		helmetRohan = new LOTRItemArmor(armorRohan, 0).setUnlocalizedName("lotr:helmetRohan");
		bodyRohan = new LOTRItemArmor(armorRohan, 1).setUnlocalizedName("lotr:bodyRohan");
		legsRohan = new LOTRItemArmor(armorRohan, 2).setUnlocalizedName("lotr:legsRohan");
		bootsRohan = new LOTRItemArmor(armorRohan, 3).setUnlocalizedName("lotr:bootsRohan");
		helmetGondorWinged = new LOTRItemArmor(armorGondor, "winged", 0).setUnlocalizedName("lotr:helmetGondorWinged");
		guldurilCrystal = new LOTRItemGuldurilCrystal().setUnlocalizedName("lotr:guldurilCrystal");
		dwarvenDoorItem = new LOTRItemDwarvenDoor().setUnlocalizedName("lotr:dwarvenDoor");
		mallornNut = new LOTRItemFood(2, 0.2F, false).setUnlocalizedName("lotr:mallornNut");
		dwarvenBedItem = new LOTRItemBed(dwarvenBed).setUnlocalizedName("lotr:dwarvenBed");
		mugCider = new LOTRItemMugBrewable(0.3F).setDrinkStats(4, 0.4F).setUnlocalizedName("lotr:mugCider");
		mugPerry = new LOTRItemMugBrewable(0.3F).setDrinkStats(4, 0.4F).setUnlocalizedName("lotr:mugPerry");
		mugCherryLiqueur = new LOTRItemMugBrewable(1F).setDrinkStats(3, 0.3F).setUnlocalizedName("lotr:mugCherryLiqueur");
		mugRum = new LOTRItemMugBrewable(1.5F).setDrinkStats(3, 0.3F).setUnlocalizedName("lotr:mugRum");
		mugAthelasBrew = new LOTRItemMugBrewable(0F).setDrinkStats(6, 0.6F).addPotionEffect(Potion.damageBoost.id, 120).addPotionEffect(Potion.regeneration.id, 60).setUnlocalizedName("lotr:mugAthelasBrew");
		armorStandItem = new LOTRItemArmorStand().setUnlocalizedName("lotr:armorStand");
		pebble = new Item().setCreativeTab(LOTRCreativeTabs.tabCombat).setUnlocalizedName("lotr:pebble");
		sling = new LOTRItemSling().setUnlocalizedName("lotr:sling");
		mysteryWeb = new LOTRItemMysteryWeb().setUnlocalizedName("lotr:mysteryWeb");
		mugDwarvenTonic = new LOTRItemMugBrewable(0.2F).setDrinkStats(4, 0.4F).addPotionEffect(Potion.nightVision.id, 240).setUnlocalizedName("lotr:mugDwarvenTonic");
		helmetRanger = new LOTRItemArmor(armorRanger, 0).setUnlocalizedName("lotr:helmetRanger");
		bodyRanger = new LOTRItemArmor(armorRanger, 1).setUnlocalizedName("lotr:bodyRanger");
		legsRanger = new LOTRItemArmor(armorRanger, 2).setUnlocalizedName("lotr:legsRanger");
		bootsRanger = new LOTRItemArmor(armorRanger, 3).setUnlocalizedName("lotr:bootsRanger");
		helmetDunlending = new LOTRItemArmor(armorDunlending, 0).setUnlocalizedName("lotr:helmetDunlending");
		bodyDunlending = new LOTRItemArmor(armorDunlending, 1).setUnlocalizedName("lotr:bodyDunlending");
		legsDunlending = new LOTRItemArmor(armorDunlending, 2).setUnlocalizedName("lotr:legsDunlending");
		bootsDunlending = new LOTRItemArmor(armorDunlending, 3).setUnlocalizedName("lotr:bootsDunlending");
		dunlendingClub = new LOTRItemHammer(ToolMaterial.WOOD).setUnlocalizedName("lotr:dunlendingClub");
		dunlendingTrident = new LOTRItemTrident(ToolMaterial.IRON).setUnlocalizedName("lotr:dunlendingTrident");
		entDraught = new LOTRItemEntDraught().setUnlocalizedName("lotr:entDraught");
		mugDwarvenAle = new LOTRItemMugBrewable(0.4F).setDrinkStats(3, 0.3F).setUnlocalizedName("lotr:mugDwarvenAle");
		maggotyBread = new LOTRItemFood(4, 0.5F, false).setPotionEffect(Potion.hunger.id, 20, 0, 0.4F).setUnlocalizedName("lotr:maggotyBread");
		morgulSteel = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:morgulSteel");
		morgulBlade = new LOTRItemSword(toolMorgul).setUnlocalizedName("lotr:morgulBlade");
		helmetMorgul = new LOTRItemArmor(armorMorgul, 0).setUnlocalizedName("lotr:helmetMorgul");
		bodyMorgul = new LOTRItemArmor(armorMorgul, 1).setUnlocalizedName("lotr:bodyMorgul");
		legsMorgul = new LOTRItemArmor(armorMorgul, 2).setUnlocalizedName("lotr:legsMorgul");
		bootsMorgul = new LOTRItemArmor(armorMorgul, 3).setUnlocalizedName("lotr:bootsMorgul");
		leatherHat = new LOTRItemLeatherHat().setUnlocalizedName("lotr:leatherHat");
		featherDyed = new LOTRItemFeatherDyed().setUnlocalizedName("lotr:featherDyed");
		mattockDwarven = new LOTRItemMattock(toolDwarven).setUnlocalizedName("lotr:mattockDwarven");
		orcBedItem = new LOTRItemBed(orcBed).setUnlocalizedName("lotr:orcBed");
		shovelWoodElven = new LOTRItemShovel(toolWoodElven).setUnlocalizedName("lotr:shovelWoodElven");
		pickaxeWoodElven = new LOTRItemPickaxe(toolWoodElven).setUnlocalizedName("lotr:pickaxeWoodElven");
		axeWoodElven = new LOTRItemAxe(toolWoodElven).setUnlocalizedName("lotr:axeWoodElven");
		swordWoodElven = new LOTRItemSword(toolWoodElven).setIsElvenBlade().setUnlocalizedName("lotr:swordWoodElven");
		hoeWoodElven = new LOTRItemHoe(toolWoodElven).setUnlocalizedName("lotr:hoeWoodElven");
		daggerWoodElven = new LOTRItemDagger(toolWoodElven, 0).setIsElvenBlade().setUnlocalizedName("lotr:daggerWoodElven");
		spearWoodElven = new LOTRItemSpear(toolWoodElven).setUnlocalizedName("lotr:spearWoodElven");
		helmetWoodElven = new LOTRItemArmor(armorWoodElven, 0).setUnlocalizedName("lotr:helmetWoodElven");
		bodyWoodElven = new LOTRItemArmor(armorWoodElven, 1).setUnlocalizedName("lotr:bodyWoodElven");
		legsWoodElven = new LOTRItemArmor(armorWoodElven, 2).setUnlocalizedName("lotr:legsWoodElven");
		bootsWoodElven = new LOTRItemArmor(armorWoodElven, 3).setUnlocalizedName("lotr:bootsWoodElven");
		rabbitRaw = new LOTRItemFood(2, 0.3F, true).setUnlocalizedName("lotr:rabbitRaw");
		rabbitCooked = new LOTRItemFood(6, 0.6F, true).setUnlocalizedName("lotr:rabbitCooked");
		rabbitStew = new LOTRItemStew(10, 0.8F, true).setUnlocalizedName("lotr:rabbitStew");
		mugVodka = new LOTRItemMugBrewable(1.75F).setDrinkStats(3, 0.3F).setUnlocalizedName("lotr:mugVodka");
		sulfur = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:sulfur");
		saltpeter = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:saltpeter");
		commandSword = new LOTRItemCommandSword().setUnlocalizedName("lotr:commandSword");
		hobbitPancake = new LOTRItemFood(4, 0.6F, false).setUnlocalizedName("lotr:hobbitPancake");
		bottlePoison = new LOTRItemBottlePoison().setUnlocalizedName("lotr:bottlePoison");
		daggerBronzePoisoned = new LOTRItemDagger(toolBronze, 1).setUnlocalizedName("lotr:daggerBronzePoisoned");
		daggerIronPoisoned = new LOTRItemDagger(ToolMaterial.IRON, 1).setUnlocalizedName("lotr:daggerIronPoisoned");
		daggerMithrilPoisoned = new LOTRItemDagger(toolMithril, 1).setUnlocalizedName("lotr:daggerMithrilPoisoned");
		daggerGondorPoisoned = new LOTRItemDagger(toolGondor, 1).setUnlocalizedName("lotr:daggerGondorPoisoned");
		daggerElvenPoisoned = new LOTRItemDagger(toolElven, 1).setIsElvenBlade().setUnlocalizedName("lotr:daggerElvenPoisoned");
		daggerDwarvenPoisoned = new LOTRItemDagger(toolDwarven, 1).setUnlocalizedName("lotr:daggerDwarvenPoisoned");
		daggerRohanPoisoned = new LOTRItemDagger(toolRohan, 1).setUnlocalizedName("lotr:daggerRohanPoisoned");
		daggerWoodElvenPoisoned = new LOTRItemDagger(toolWoodElven, 1).setIsElvenBlade().setUnlocalizedName("lotr:daggerWoodElvenPoisoned");
		banner = new LOTRItemBanner().setUnlocalizedName("lotr:banner");
		sulfurMatch = new LOTRItemMatch().setUnlocalizedName("lotr:sulfurMatch");
		swordAngmar = new LOTRItemSword(toolAngmar).setUnlocalizedName("lotr:swordAngmar");
		daggerAngmar = new LOTRItemDagger(toolAngmar, 0).setUnlocalizedName("lotr:daggerAngmar");
		daggerAngmarPoisoned = new LOTRItemDagger(toolAngmar, 1).setUnlocalizedName("lotr:daggerAngmarPoisoned");
		battleaxeAngmar = new LOTRItemBattleaxe(toolAngmar).setUnlocalizedName("lotr:battleaxeAngmar");
		hammerAngmar = new LOTRItemHammer(toolAngmar).setUnlocalizedName("lotr:hammerAngmar");
		spearAngmar = new LOTRItemSpear(toolAngmar).setUnlocalizedName("lotr:spearAngmar");
		shovelAngmar = new LOTRItemShovel(toolAngmar).setUnlocalizedName("lotr:shovelAngmar");
		pickaxeAngmar = new LOTRItemPickaxe(toolAngmar).setUnlocalizedName("lotr:pickaxeAngmar");
		axeAngmar = new LOTRItemAxe(toolAngmar).setUnlocalizedName("lotr:axeAngmar");
		hoeAngmar = new LOTRItemHoe(toolAngmar).setUnlocalizedName("lotr:hoeAngmar");
		helmetAngmar = new LOTRItemArmor(armorAngmar, 0).setUnlocalizedName("lotr:helmetAngmar");
		bodyAngmar = new LOTRItemArmor(armorAngmar, 1).setUnlocalizedName("lotr:bodyAngmar");
		legsAngmar = new LOTRItemArmor(armorAngmar, 2).setUnlocalizedName("lotr:legsAngmar");
		bootsAngmar = new LOTRItemArmor(armorAngmar, 3).setUnlocalizedName("lotr:bootsAngmar");
		mango = new LOTRItemFood(4, 0.3F, false).setUnlocalizedName("lotr:mango");
		mugMangoJuice = new LOTRItemMug(false).setDrinkStats(8, 0.6F).setUnlocalizedName("lotr:mugMangoJuice");
		banana = new LOTRItemHangingFruit(2, 0.5F, false, bananaBlock).setUnlocalizedName("lotr:banana");
		bananaBread = new LOTRItemFood(5, 0.5F, false).setUnlocalizedName("lotr:bananaBread");
		bananaCakeItem = new ItemReed(bananaCake).setMaxStackSize(1).setCreativeTab(LOTRCreativeTabs.tabFood).setUnlocalizedName("lotr:bananaCake");
		lionFur = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:lionFur");
		lionRaw = new LOTRItemFood(3, 0.3F, true).setUnlocalizedName("lotr:lionRaw");
		lionCooked = new LOTRItemFood(8, 0.8F, true).setUnlocalizedName("lotr:lionCooked");
		zebraRaw = new LOTRItemFood(2, 0.1F, true).setUnlocalizedName("lotr:zebraRaw");
		zebraCooked = new LOTRItemFood(6, 0.6F, true).setUnlocalizedName("lotr:zebraCooked");
		rhinoRaw = new LOTRItemFood(2, 0.1F, true).setUnlocalizedName("lotr:rhinoRaw");
		rhinoCooked = new LOTRItemFood(7, 0.4F, true).setUnlocalizedName("lotr:rhinoCooked");
		rhinoHorn = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:rhinoHorn");
		battleaxeRohan = new LOTRItemBattleaxe(toolRohan).setUnlocalizedName("lotr:battleaxeRohan");
		lionBedItem = new LOTRItemBed(lionBed).setUnlocalizedName("lotr:lionBed");
		scimitarNearHarad = new LOTRItemSword(toolNearHarad).setUnlocalizedName("lotr:scimitarNearHarad");
		helmetNearHarad = new LOTRItemArmor(armorNearHarad, 0).setUnlocalizedName("lotr:helmetNearHarad");
		bodyNearHarad = new LOTRItemArmor(armorNearHarad, 1).setUnlocalizedName("lotr:bodyNearHarad");
		legsNearHarad = new LOTRItemArmor(armorNearHarad, 2).setUnlocalizedName("lotr:legsNearHarad");
		bootsNearHarad = new LOTRItemArmor(armorNearHarad, 3).setUnlocalizedName("lotr:bootsNearHarad");
		gemsbokHide = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:gemsbokHide");
		gemsbokHorn = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:gemsbokHorn");
		helmetGemsbok = new LOTRItemArmor(armorGemsbok, 0).setUnlocalizedName("lotr:helmetGemsbok");
		bodyGemsbok = new LOTRItemArmor(armorGemsbok, 1).setUnlocalizedName("lotr:bodyGemsbok");
		legsGemsbok = new LOTRItemArmor(armorGemsbok, 2).setUnlocalizedName("lotr:legsGemsbok");
		bootsGemsbok = new LOTRItemArmor(armorGemsbok, 3).setUnlocalizedName("lotr:bootsGemsbok");
		mapleSyrup = new LOTRItemStew(2, 0.1F, false).setUnlocalizedName("lotr:mapleSyrup");
		hobbitPancakeMapleSyrup = new LOTRItemFood(5, 0.6F, false).setUnlocalizedName("lotr:hobbitPancakeMapleSyrup");
		mugMapleBeer = new LOTRItemMugBrewable(0.4F).setDrinkStats(4, 0.6F).setUnlocalizedName("lotr:mugMapleBeer");
		helmetHighElven = new LOTRItemArmor(armorHighElven, 0).setUnlocalizedName("lotr:helmetHighElven");
		bodyHighElven = new LOTRItemArmor(armorHighElven, 1).setUnlocalizedName("lotr:bodyHighElven");
		legsHighElven = new LOTRItemArmor(armorHighElven, 2).setUnlocalizedName("lotr:legsHighElven");
		bootsHighElven = new LOTRItemArmor(armorHighElven, 3).setUnlocalizedName("lotr:bootsHighElven");
		shovelHighElven = new LOTRItemShovel(toolHighElven).setUnlocalizedName("lotr:shovelHighElven");
		pickaxeHighElven = new LOTRItemPickaxe(toolHighElven).setUnlocalizedName("lotr:pickaxeHighElven");
		axeHighElven = new LOTRItemAxe(toolHighElven).setUnlocalizedName("lotr:axeHighElven");
		swordHighElven = new LOTRItemSword(toolHighElven).setIsElvenBlade().setUnlocalizedName("lotr:swordHighElven");
		hoeHighElven = new LOTRItemHoe(toolHighElven).setUnlocalizedName("lotr:hoeHighElven");
		daggerHighElven = new LOTRItemDagger(toolHighElven, 0).setIsElvenBlade().setUnlocalizedName("lotr:daggerHighElven");
		daggerHighElvenPoisoned = new LOTRItemDagger(toolHighElven, 1).setIsElvenBlade().setUnlocalizedName("lotr:daggerHighElvenPoisoned");
		spearHighElven = new LOTRItemSpear(toolHighElven).setUnlocalizedName("lotr:spearHighElven");
		highElvenBedItem = new LOTRItemBed(highElvenBed).setUnlocalizedName("lotr:highElvenBed");
		daggerNearHarad = new LOTRItemDagger(toolNearHarad, 0).setUnlocalizedName("lotr:daggerNearHarad");
		daggerNearHaradPoisoned = new LOTRItemDagger(toolNearHarad, 1).setUnlocalizedName("lotr:daggerNearHaradPoisoned");
		spearNearHarad = new LOTRItemSpear(toolNearHarad).setUnlocalizedName("lotr:spearNearHarad");
		nearHaradBow = new LOTRItemBow(484, 0.4D, 0.5F, 3, 30).setUnlocalizedName("lotr:nearHaradBow");
		date = new LOTRItemHangingFruit(2, 0.3F, false, dateBlock).setUnlocalizedName("lotr:date");
		mugAraq = new LOTRItemMugBrewable(1.4F).setDrinkStats(4, 0.4F).setUnlocalizedName("lotr:mugAraq");
		blueDwarfSteel = new Item().setCreativeTab(LOTRCreativeTabs.tabMaterials).setUnlocalizedName("lotr:blueDwarfSteel");
		shovelBlueDwarven = new LOTRItemShovel(toolBlueDwarven).setUnlocalizedName("lotr:shovelBlueDwarven");
		pickaxeBlueDwarven = new LOTRItemPickaxe(toolBlueDwarven).setUnlocalizedName("lotr:pickaxeBlueDwarven");
		axeBlueDwarven = new LOTRItemAxe(toolBlueDwarven).setUnlocalizedName("lotr:axeBlueDwarven");
		swordBlueDwarven = new LOTRItemSword(toolBlueDwarven).setUnlocalizedName("lotr:swordBlueDwarven");
		hoeBlueDwarven = new LOTRItemHoe(toolBlueDwarven).setUnlocalizedName("lotr:hoeBlueDwarven");
		daggerBlueDwarven = new LOTRItemDagger(toolBlueDwarven, 0).setUnlocalizedName("lotr:daggerBlueDwarven");
		daggerBlueDwarvenPoisoned = new LOTRItemDagger(toolBlueDwarven, 1).setUnlocalizedName("lotr:daggerBlueDwarvenPoisoned");
		battleaxeBlueDwarven = new LOTRItemBattleaxe(toolBlueDwarven).setUnlocalizedName("lotr:battleaxeBlueDwarven");
		hammerBlueDwarven = new LOTRItemHammer(toolBlueDwarven).setUnlocalizedName("lotr:hammerBlueDwarven");
		mattockBlueDwarven = new LOTRItemMattock(toolBlueDwarven).setUnlocalizedName("lotr:mattockBlueDwarven");
		throwingAxeBlueDwarven = new LOTRItemThrowingAxe(toolBlueDwarven).setUnlocalizedName("lotr:throwingAxeBlueDwarven");
		helmetBlueDwarven = new LOTRItemArmor(armorBlueDwarven, 0).setUnlocalizedName("lotr:helmetBlueDwarven");
		bodyBlueDwarven = new LOTRItemArmor(armorBlueDwarven, 1).setUnlocalizedName("lotr:bodyBlueDwarven");
		legsBlueDwarven = new LOTRItemArmor(armorBlueDwarven, 2).setUnlocalizedName("lotr:legsBlueDwarven");
		bootsBlueDwarven = new LOTRItemArmor(armorBlueDwarven, 3).setUnlocalizedName("lotr:bootsBlueDwarven");
		dwarvenRing = new Item().setCreativeTab(LOTRCreativeTabs.tabMisc).setUnlocalizedName("lotr:dwarvenRing");
		
		try
		{
			String prefix = "lotr:";
			for (Field field : LOTRMod.class.getFields())
			{
				if (field.get(null) instanceof Block)
				{
					Block block = (Block)field.get(null);
					String newName = block.getUnlocalizedName().substring(prefix.length());
					field.set(null, block.setBlockTextureName(newName));
				}
				
				if (field.get(null) instanceof Item)
				{
					Item item = (Item)field.get(null);
					String newName = item.getUnlocalizedName().substring(prefix.length());
					field.set(null, item.setTextureName(newName));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		LOTRBlockSlabBase.registerSlabs(woodSlabSingle, woodSlabDouble);
		LOTRBlockSlabBase.registerSlabs(slabSingle, slabDouble);
		LOTRBlockSlabBase.registerSlabs(slabSingle2, slabDouble2);
		LOTRBlockSlabBase.registerSlabs(woodSlabSingle2, woodSlabDouble2);
		LOTRBlockSlabBase.registerSlabs(slabSingle3, slabDouble3);
		LOTRBlockSlabBase.registerSlabs(slabSingle4, slabDouble4);
		LOTRBlockSlabBase.registerSlabs(slabSingleThatch, slabDoubleThatch);
		
		LOTRBlockBed.registerBedItem(elvenBed, elvenBedItem);
		LOTRBlockBed.registerBedItem(dwarvenBed, dwarvenBedItem);
		LOTRBlockBed.registerBedItem(woodElvenBed, woodElvenBedItem);
		LOTRBlockBed.registerBedItem(orcBed, orcBedItem);
		LOTRBlockBed.registerBedItem(lionBed, lionBedItem);
		LOTRBlockBed.registerBedItem(highElvenBed, highElvenBedItem);
		
		LOTRBlockPlaceableFood.registerFoodItem(appleCrumble, appleCrumbleItem);
		LOTRBlockPlaceableFood.registerFoodItem(cherryPie, cherryPieItem);
		LOTRBlockPlaceableFood.registerFoodItem(bananaCake, bananaCakeItem);
		
		registerBlock(rock, LOTRItemBlockMetadata.class);
		registerBlock(oreCopper);
		registerBlock(oreTin);
		registerBlock(oreSilver);
		registerBlock(oreMithril);
		registerBlock(beacon);
		registerBlock(simbelmyne);
		registerBlock(wood, LOTRItemBlockMetadata.class);
		registerBlock(leaves, LOTRItemLeaves.class);
		registerBlock(planks, LOTRItemBlockMetadata.class);
		registerBlock(sapling, LOTRItemBlockMetadata.class);
		registerBlock(woodSlabSingle, LOTRBlockSlabBase.SlabItems.WoodSlab1Single.class);
		registerBlock(woodSlabDouble, LOTRBlockSlabBase.SlabItems.WoodSlab1Double.class);
		registerBlock(stairsShirePine);
		registerBlock(shireHeather);
		registerBlock(brick, LOTRItemBlockMetadata.class);
		registerBlock(appleCrumble);
		registerBlock(hobbitOven);
		registerBlock(blockOreStorage, LOTRItemBlockMetadata.class);
		registerBlock(oreNaurite);
		registerBlock(oreMorgulIron, LOTRItemBlockMetadata.class);
		registerBlock(morgulTable);
		registerBlock(chandelier, LOTRItemBlockMetadata.class);
		registerBlock(pipeweedPlant);
		registerBlock(pipeweedCrop);
		registerBlock(slabSingle, LOTRBlockSlabBase.SlabItems.Slab1Single.class);
		registerBlock(slabDouble, LOTRBlockSlabBase.SlabItems.Slab1Double.class);
		registerBlock(stairsMordorBrick);
		registerBlock(stairsGondorBrick);
		registerBlock(wall, LOTRItemBlockMetadata.class);
		registerBlock(barrel, LOTRItemBarrel.class);
		registerBlock(lettuceCrop);
		registerBlock(orcBomb, LOTRItemOrcBomb.class);
		registerBlock(orcTorch);
		registerBlock(elanor);
		registerBlock(niphredil);
		registerBlock(stairsMallorn);
		registerBlock(elvenTable);
		registerBlock(mobSpawner, LOTRItemMobSpawner.class);
		registerBlock(mallornLadder);
		registerBlock(plateBlock);
		registerBlock(orcSteelBars);
		registerBlock(stairsGondorBrickMossy);
		registerBlock(stairsGondorBrickCracked);
		registerBlock(athelas);
		registerBlock(stalactite, LOTRItemBlockMetadata.class);
		registerBlock(stairsRohanBrick);
		registerBlock(oreQuendite);
		registerBlock(mallornTorch);
		registerBlock(spawnerChest);
		registerBlock(quenditeGrass);
		registerBlock(pressurePlateMordorRock);
		registerBlock(pressurePlateGondorRock);
		registerBlock(buttonMordorRock);
		registerBlock(buttonGondorRock);
		registerBlock(elvenPortal);
		registerBlock(flowerPot);
		registerBlock(stairsDwarvenBrick);
		registerBlock(elvenBed);
		registerBlock(pillar, LOTRItemBlockMetadata.class);
		registerBlock(oreGlowstone);
		registerBlock(fruitWood, LOTRItemBlockMetadata.class);
		registerBlock(fruitLeaves, LOTRItemLeaves.class);
		registerBlock(fruitSapling, LOTRItemBlockMetadata.class);
		registerBlock(stairsApple);
		registerBlock(stairsPear);
		registerBlock(stairsCherry);
		registerBlock(dwarvenTable);
		registerBlock(bluebell);
		registerBlock(dwarvenForge);
		registerBlock(hearth);
		registerBlock(morgulShroom, LOTRItemMorgulShroom.class);
		registerBlock(urukTable);
		registerBlock(cherryPie);
		registerBlock(clover, LOTRItemBlockMetadata.class);
		registerBlock(slabSingle2, LOTRBlockSlabBase.SlabItems.Slab2Single.class);
		registerBlock(slabDouble2, LOTRBlockSlabBase.SlabItems.Slab2Double.class);
		registerBlock(stairsMirkOak);
		registerBlock(webUngoliant);
		registerBlock(woodElvenTable);
		registerBlock(woodElvenBed);
		registerBlock(gondorianTable);
		registerBlock(woodElvenTorch);
		registerBlock(marshLights);
		registerBlock(rohirricTable);
		registerBlock(pressurePlateRohanRock);
		registerBlock(remains);
		registerBlock(deadPlant);
		registerBlock(oreGulduril, LOTRItemBlockMetadata.class);
		registerBlock(guldurilBrick, LOTRItemBlockMetadata.class);
		registerBlock(dwarvenDoor);
		registerBlock(stairsCharred);
		registerBlock(dwarvenBed);
		registerBlock(morgulPortal);
		registerBlock(armorStand);
		registerBlock(buttonRohanRock);
		registerBlock(asphodel);
		registerBlock(wood2, LOTRItemBlockMetadata.class);
		registerBlock(leaves2, LOTRItemLeaves.class);
		registerBlock(sapling2, LOTRItemBlockMetadata.class);
		registerBlock(stairsLebethron);
		registerBlock(woodSlabSingle2, LOTRBlockSlabBase.SlabItems.WoodSlab2Single.class);
		registerBlock(woodSlabDouble2, LOTRBlockSlabBase.SlabItems.WoodSlab2Double.class);
		registerBlock(dwarfHerb);
		registerBlock(mugBlock);
		registerBlock(dunlendingTable);
		registerBlock(stairsBeech);
		registerBlock(entJar);
		registerBlock(mordorThorn);
		registerBlock(mordorMoss);
		registerBlock(stairsMordorBrickCracked);
		registerBlock(orcForge);
		registerBlock(trollTotem, LOTRItemBlockMetadata.class);
		registerBlock(orcBed);
		registerBlock(stairsElvenBrick);
		registerBlock(stairsElvenBrickMossy);
		registerBlock(stairsElvenBrickCracked);
		registerBlock(stairsHolly);
		registerBlock(pressurePlateBlueRock);
		registerBlock(buttonBlueRock);
		registerBlock(slabSingle3, LOTRBlockSlabBase.SlabItems.Slab3Single.class);
		registerBlock(slabDouble3, LOTRBlockSlabBase.SlabItems.Slab3Double.class);
		registerBlock(stairsBlueRockBrick);
		registerBlock(fence, LOTRItemBlockMetadata.class);
		registerBlock(doubleFlower, LOTRItemDoubleFlower.class);
		registerBlock(oreSulfur);
		registerBlock(oreSaltpeter);
		registerBlock(quagmire);
		registerBlock(angmarTable);
		registerBlock(brick2, LOTRItemBlockMetadata.class);
		registerBlock(wall2, LOTRItemBlockMetadata.class);
		registerBlock(stairsAngmarBrick);
		registerBlock(stairsAngmarBrickCracked);
		registerBlock(stairsMango);
		registerBlock(stairsBanana);
		registerBlock(bananaBlock);
		registerBlock(bananaCake);
		registerBlock(lionBed);
		registerBlock(wood3, LOTRItemBlockMetadata.class);
		registerBlock(leaves3, LOTRItemLeaves.class);
		registerBlock(sapling3, LOTRItemBlockMetadata.class);
		registerBlock(stairsMaple);
		registerBlock(stairsLarch);
		registerBlock(nearHaradTable);
		registerBlock(highElvenTable);
		registerBlock(highElvenTorch);
		registerBlock(highElvenBed);
		registerBlock(pressurePlateRedRock);
		registerBlock(buttonRedRock);
		registerBlock(stairsRedRockBrick);
		registerBlock(slabSingle4, LOTRBlockSlabBase.SlabItems.Slab4Single.class);
		registerBlock(slabDouble4, LOTRBlockSlabBase.SlabItems.Slab4Double.class);
		registerBlock(stairsNearHaradBrick);
		registerBlock(stairsDatePalm);
		registerBlock(dateBlock);
		registerBlock(blueDwarvenTable);
		registerBlock(goran);
		registerBlock(thatch);
		registerBlock(slabSingleThatch, LOTRBlockSlabBase.SlabItems.ThatchSingle.class);
		registerBlock(slabDoubleThatch, LOTRBlockSlabBase.SlabItems.ThatchDouble.class);
		registerBlock(stairsThatch);
		registerBlock(fangornPlant, LOTRItemBlockMetadata.class);
		registerBlock(fangornRiverweed, LOTRItemRiverweed.class);
		
		registerItem(goldRing);
		registerItem(pouch);
		registerItem(copper);
		registerItem(tin);
		registerItem(bronze);
		registerItem(silver);
		registerItem(mithril);
		registerItem(shovelBronze);
		registerItem(pickaxeBronze);
		registerItem(axeBronze);
		registerItem(swordBronze);
		registerItem(hoeBronze);
		registerItem(helmetBronze);
		registerItem(bodyBronze);
		registerItem(legsBronze);
		registerItem(bootsBronze);
		registerItem(silverNugget);
		registerItem(silverRing);
		registerItem(mithrilNugget);
		registerItem(mithrilRing);
		registerItem(hobbitPipe);
		registerItem(pipeweed);
		registerItem(clayMug);
		registerItem(mug);
		registerItem(mugWater);
		registerItem(mugMilk);
		registerItem(mugAle);
		registerItem(mugChocolate);
		registerItem(appleCrumbleItem);
		registerItem(mugMiruvor);
		registerItem(mugOrcDraught);
		registerItem(scimitarOrc);
		registerItem(helmetOrc);
		registerItem(bodyOrc);
		registerItem(legsOrc);
		registerItem(bootsOrc);
		registerItem(orcSteel);
		registerItem(battleaxeOrc);
		registerItem(lembas);
		registerItem(nauriteGem);
		registerItem(daggerOrc);
		registerItem(daggerOrcPoisoned);
		registerItem(sting);
		registerItem(spawnEgg);
		registerItem(pipeweedLeaf);
		registerItem(pipeweedSeeds);
		registerItem(structureSpawner);
		registerItem(lettuce);
		registerItem(shovelMithril);
		registerItem(pickaxeMithril);
		registerItem(axeMithril);
		registerItem(swordMithril);
		registerItem(hoeMithril);
		registerItem(orcTorchItem);
		registerItem(sauronMace);
		registerItem(gandalfStaffWhite);
		registerItem(swordGondor);
		registerItem(helmetGondor);
		registerItem(bodyGondor);
		registerItem(legsGondor);
		registerItem(bootsGondor);
		registerItem(helmetMithril);
		registerItem(bodyMithril);
		registerItem(legsMithril);
		registerItem(bootsMithril);
		registerItem(spearGondor);
		registerItem(spearOrc);
		registerItem(spearBronze);
		registerItem(spearIron);
		registerItem(spearMithril);
		registerItem(anduril);
		registerItem(dye);
		registerItem(mallornStick);
		registerItem(shovelMallorn);
		registerItem(pickaxeMallorn);
		registerItem(axeMallorn);
		registerItem(swordMallorn);
		registerItem(hoeMallorn);
		registerItem(shovelElven);
		registerItem(pickaxeElven);
		registerItem(axeElven);
		registerItem(swordElven);
		registerItem(hoeElven);
		registerItem(spearElven);
		registerItem(mallornBow);
		registerItem(helmetElven);
		registerItem(bodyElven);
		registerItem(legsElven);
		registerItem(bootsElven);
		registerItem(silverCoin);
		registerItem(gammon);
		registerItem(clayPlate);
		registerItem(plate);
		registerItem(elvenBow);
		registerItem(wargFur);
		registerItem(helmetWarg);
		registerItem(bodyWarg);
		registerItem(legsWarg);
		registerItem(bootsWarg);
		registerItem(orcBow);
		registerItem(mugMead);
		registerItem(wargskinRug);
		registerItem(quenditeCrystal);
		registerItem(blacksmithHammer);
		registerItem(daggerGondor);
		registerItem(daggerElven);
		registerItem(hobbitRing);
		registerItem(elvenBedItem);
		registerItem(wargBone);
		registerItem(appleGreen);
		registerItem(pear);
		registerItem(cherry);
		registerItem(dwarfSteel);
		registerItem(shovelDwarven);
		registerItem(pickaxeDwarven);
		registerItem(axeDwarven);
		registerItem(swordDwarven);
		registerItem(hoeDwarven);
		registerItem(daggerDwarven);
		registerItem(battleaxeDwarven);
		registerItem(hammerDwarven);
		registerItem(shovelOrc);
		registerItem(pickaxeOrc);
		registerItem(axeOrc);
		registerItem(hoeOrc);
		registerItem(hammerOrc);
		registerItem(helmetDwarven);
		registerItem(bodyDwarven);
		registerItem(legsDwarven);
		registerItem(bootsDwarven);
		registerItem(galvorn);
		registerItem(helmetGalvorn);
		registerItem(bodyGalvorn);
		registerItem(legsGalvorn);
		registerItem(bootsGalvorn);
		registerItem(daggerBronze);
		registerItem(daggerIron);
		registerItem(daggerMithril);
		registerItem(battleaxeMithril);
		registerItem(hammerMithril);
		registerItem(hammerGondor);
		registerItem(orcBone);
		registerItem(elfBone);
		registerItem(dwarfBone);
		registerItem(hobbitBone);
		registerItem(commandHorn);
		registerItem(throwingAxeDwarven);
		registerItem(urukSteel);
		registerItem(shovelUruk);
		registerItem(pickaxeUruk);
		registerItem(axeUruk);
		registerItem(scimitarUruk);
		registerItem(hoeUruk);
		registerItem(daggerUruk);
		registerItem(daggerUrukPoisoned);
		registerItem(battleaxeUruk);
		registerItem(hammerUruk);
		registerItem(spearUruk);
		registerItem(helmetUruk);
		registerItem(bodyUruk);
		registerItem(legsUruk);
		registerItem(bootsUruk);
		registerItem(crossbowBolt);
		registerItem(urukCrossbow);
		registerItem(cherryPieItem);
		registerItem(trollBone);
		registerItem(trollStatue);
		registerItem(wargHelmet);
		registerItem(wargChestplate);
		registerItem(wargHindplate);
		registerItem(ironCrossbow);
		registerItem(mithrilCrossbow);
		registerItem(woodElvenBedItem);
		registerItem(helmetWoodElvenScout);
		registerItem(bodyWoodElvenScout);
		registerItem(legsWoodElvenScout);
		registerItem(bootsWoodElvenScout);
		registerItem(mirkwoodBow);
		registerItem(mugRedWine);
		registerItem(ancientItemParts);
		registerItem(ancientItem);
		registerItem(swordRohan);
		registerItem(daggerRohan);
		registerItem(spearRohan);
		registerItem(helmetRohan);
		registerItem(bodyRohan);
		registerItem(legsRohan);
		registerItem(bootsRohan);
		registerItem(helmetGondorWinged);
		registerItem(guldurilCrystal);
		registerItem(dwarvenDoorItem);
		registerItem(mallornNut);
		registerItem(dwarvenBedItem);
		registerItem(mugCider);
		registerItem(mugPerry);
		registerItem(mugCherryLiqueur);
		registerItem(mugRum);
		registerItem(mugAthelasBrew);
		registerItem(armorStandItem);
		registerItem(pebble);
		registerItem(sling);
		registerItem(mysteryWeb);
		registerItem(mugDwarvenTonic);
		registerItem(helmetRanger);
		registerItem(bodyRanger);
		registerItem(legsRanger);
		registerItem(bootsRanger);
		registerItem(helmetDunlending);
		registerItem(bodyDunlending);
		registerItem(legsDunlending);
		registerItem(bootsDunlending);
		registerItem(dunlendingClub);
		registerItem(dunlendingTrident);
		registerItem(entDraught);
		registerItem(mugDwarvenAle);
		registerItem(maggotyBread);
		registerItem(morgulSteel);
		registerItem(morgulBlade);
		registerItem(helmetMorgul);
		registerItem(bodyMorgul);
		registerItem(legsMorgul);
		registerItem(bootsMorgul);
		registerItem(leatherHat);
		registerItem(featherDyed);
		registerItem(mattockDwarven);
		registerItem(orcBedItem);
		registerItem(shovelWoodElven);
		registerItem(pickaxeWoodElven);
		registerItem(axeWoodElven);
		registerItem(swordWoodElven);
		registerItem(hoeWoodElven);
		registerItem(daggerWoodElven);
		registerItem(spearWoodElven);
		registerItem(helmetWoodElven);
		registerItem(bodyWoodElven);
		registerItem(legsWoodElven);
		registerItem(bootsWoodElven);
		registerItem(rabbitRaw);
		registerItem(rabbitCooked);
		registerItem(rabbitStew);
		registerItem(mugVodka);
		registerItem(sulfur);
		registerItem(saltpeter);
		registerItem(commandSword);
		registerItem(hobbitPancake);
		registerItem(bottlePoison);
		registerItem(daggerBronzePoisoned);
		registerItem(daggerIronPoisoned);
		registerItem(daggerMithrilPoisoned);
		registerItem(daggerGondorPoisoned);
		registerItem(daggerElvenPoisoned);
		registerItem(daggerDwarvenPoisoned);
		registerItem(daggerRohanPoisoned);
		registerItem(daggerWoodElvenPoisoned);
		registerItem(banner);
		registerItem(sulfurMatch);
		registerItem(swordAngmar);
		registerItem(daggerAngmar);
		registerItem(daggerAngmarPoisoned);
		registerItem(battleaxeAngmar);
		registerItem(hammerAngmar);
		registerItem(spearAngmar);
		registerItem(shovelAngmar);
		registerItem(pickaxeAngmar);
		registerItem(axeAngmar);
		registerItem(hoeAngmar);
		registerItem(helmetAngmar);
		registerItem(bodyAngmar);
		registerItem(legsAngmar);
		registerItem(bootsAngmar);
		registerItem(mango);
		registerItem(mugMangoJuice);
		registerItem(banana);
		registerItem(bananaBread);
		registerItem(bananaCakeItem);
		registerItem(lionFur);
		registerItem(lionRaw);
		registerItem(lionCooked);
		registerItem(zebraRaw);
		registerItem(zebraCooked);
		registerItem(rhinoRaw);
		registerItem(rhinoCooked);
		registerItem(rhinoHorn);
		registerItem(battleaxeRohan);
		registerItem(lionBedItem);
		registerItem(scimitarNearHarad);
		registerItem(helmetNearHarad);
		registerItem(bodyNearHarad);
		registerItem(legsNearHarad);
		registerItem(bootsNearHarad);
		registerItem(gemsbokHide);
		registerItem(gemsbokHorn);
		registerItem(helmetGemsbok);
		registerItem(bodyGemsbok);
		registerItem(legsGemsbok);
		registerItem(bootsGemsbok);
		registerItem(mapleSyrup);
		registerItem(hobbitPancakeMapleSyrup);
		registerItem(mugMapleBeer);
		registerItem(helmetHighElven);
		registerItem(bodyHighElven);
		registerItem(legsHighElven);
		registerItem(bootsHighElven);
		registerItem(shovelHighElven);
		registerItem(pickaxeHighElven);
		registerItem(axeHighElven);
		registerItem(swordHighElven);
		registerItem(hoeHighElven);
		registerItem(daggerHighElven);
		registerItem(daggerHighElvenPoisoned);
		registerItem(spearHighElven);
		registerItem(highElvenBedItem);
		registerItem(daggerNearHarad);
		registerItem(daggerNearHaradPoisoned);
		registerItem(spearNearHarad);
		registerItem(nearHaradBow);
		registerItem(date);
		registerItem(mugAraq);
		registerItem(blueDwarfSteel);
		registerItem(shovelBlueDwarven);
		registerItem(pickaxeBlueDwarven);
		registerItem(axeBlueDwarven);
		registerItem(swordBlueDwarven);
		registerItem(hoeBlueDwarven);
		registerItem(daggerBlueDwarven);
		registerItem(daggerBlueDwarvenPoisoned);
		registerItem(battleaxeBlueDwarven);
		registerItem(hammerBlueDwarven);
		registerItem(mattockBlueDwarven);
		registerItem(throwingAxeBlueDwarven);
		registerItem(helmetBlueDwarven);
		registerItem(bodyBlueDwarven);
		registerItem(legsBlueDwarven);
		registerItem(bootsBlueDwarven);
		registerItem(dwarvenRing);
		
		idDimension = config.get("general", "Dimension ID", 100).getInt();
		alwaysShowAlignment = config.get("general", "Always show alignment", false, "If set to false, the alignment bar will only be shown in Middle-earth. If set to true, it will be shown in all dimensions").getBoolean(false);
		alignmentXOffset = config.get("general", "Alignment X Offset", 0, "Configure the x-position of the alignment bar on-screen. Negative values move it left, positive values right.").getInt();
		alignmentYOffset = config.get("general", "Alignment Y Offset", 0, "Configure the y-position of the alignment bar on-screen. Negative values move it up, positive values down.").getInt();
		alignmentMoveWhenBoss = config.get("general", "Move alignment when boss", true, "If set to true, the alignment bar is moved down while the boss status bar is displayed").getBoolean(true);
		displayAlignmentAboveHead = config.get("general", "Display alignment above head", true, "Enable or disable the rendering of other players' alignment values above their heads").getBoolean(true);
		enableLOTRSky = config.get("general", "Enable LOTR sky", true, "Enable or disable the new Middle-earth sky").getBoolean(true);
		enableMistyMountainsMist = config.get("general", "Enable Mist", true, "Enable or disable mist in the Misty Mountains").getBoolean(true);

		if (config.hasChanged())
		{
			config.save();
		}
		
		proxy.onPreload();
		
		LOTRBiome.initBiomes();
		
		LOTREntityRegistry.loadRegisteredNPCs(event);
		
		LOTRCapes.forceClassLoad();
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.onLoad();
		
		LOTRCreativeTabs.setupIcons();
		
		toolBronze.customCraftingMaterial = bronze;
		toolMithril.customCraftingMaterial = mithril;
		toolOrc.customCraftingMaterial = orcSteel;
		toolGondor.customCraftingMaterial = Items.iron_ingot;
		toolElven.customCraftingMaterial = Items.iron_ingot;
		toolDwarven.customCraftingMaterial = dwarfSteel;
		toolUruk.customCraftingMaterial = urukSteel;
		toolRohan.customCraftingMaterial = Items.iron_ingot;
		toolMorgul.customCraftingMaterial = morgulSteel;
		toolWoodElven.customCraftingMaterial = Items.iron_ingot;
		toolAngmar.customCraftingMaterial = orcSteel;
		toolNearHarad.customCraftingMaterial = Items.iron_ingot;
		toolHighElven.customCraftingMaterial = Items.iron_ingot;
		toolBlueDwarven.customCraftingMaterial = blueDwarfSteel;
		
		armorBronze.customCraftingMaterial = bronze;
		armorMithril.customCraftingMaterial = mithril;
		armorOrc.customCraftingMaterial = orcSteel;
		armorGondor.customCraftingMaterial = Items.iron_ingot;
		armorElven.customCraftingMaterial = Items.iron_ingot;
		armorWarg.customCraftingMaterial = wargFur;
		armorDwarven.customCraftingMaterial = dwarfSteel;
		armorGalvorn.customCraftingMaterial = galvorn;
		armorUruk.customCraftingMaterial = urukSteel;
		armorWoodElvenScout.customCraftingMaterial = Items.leather;
		armorRohan.customCraftingMaterial = Items.iron_ingot;
		armorRanger.customCraftingMaterial = Items.leather;
		armorDunlending.customCraftingMaterial = Items.iron_ingot;
		armorMorgul.customCraftingMaterial = morgulSteel;
		armorWoodElven.customCraftingMaterial = Items.iron_ingot;
		armorAngmar.customCraftingMaterial = orcSteel;
		armorNearHarad.customCraftingMaterial = Items.iron_ingot;
		armorGemsbok.customCraftingMaterial = gemsbokHide;
		armorHighElven.customCraftingMaterial = Items.iron_ingot;
		armorBlueDwarven.customCraftingMaterial = blueDwarfSteel;
		
		materialWargArmor.customCraftingMaterial = urukSteel;

		LOTRRecipes.createAllRecipes();
		
		Blocks.fire.setFireInfo(wood, 5, 5);
		Blocks.fire.setFireInfo(fruitWood, 5, 5);
		Blocks.fire.setFireInfo(wood2, 5, 5);
		Blocks.fire.setFireInfo(wood3, 5, 5);
		
		Blocks.fire.setFireInfo(leaves, 30, 60);
		Blocks.fire.setFireInfo(fruitLeaves, 30, 60);
		Blocks.fire.setFireInfo(leaves2, 30, 60);
		Blocks.fire.setFireInfo(leaves3, 30, 60);
		
		Blocks.fire.setFireInfo(thatch, 20, 40);
		Blocks.fire.setFireInfo(slabSingleThatch, 20, 40);
		Blocks.fire.setFireInfo(slabDoubleThatch, 20, 40);
		Blocks.fire.setFireInfo(stairsThatch, 20, 40);
		
		Blocks.fire.setFireInfo(planks, 5, 20);
		Blocks.fire.setFireInfo(fence, 5, 20);
		
		Blocks.fire.setFireInfo(woodSlabSingle, 5, 20);
		Blocks.fire.setFireInfo(woodSlabDouble, 5, 20);
		Blocks.fire.setFireInfo(woodSlabSingle2, 5, 20);
		Blocks.fire.setFireInfo(woodSlabDouble2, 5, 20);
		
		Blocks.fire.setFireInfo(stairsShirePine, 5, 20);
		Blocks.fire.setFireInfo(stairsMallorn, 5, 20);
		Blocks.fire.setFireInfo(stairsMirkOak, 5, 20);
		Blocks.fire.setFireInfo(stairsCharred, 5, 20);
		Blocks.fire.setFireInfo(stairsApple, 5, 20);
		Blocks.fire.setFireInfo(stairsPear, 5, 20);
		Blocks.fire.setFireInfo(stairsCherry, 5, 20);
		Blocks.fire.setFireInfo(stairsMango, 5, 20);
		Blocks.fire.setFireInfo(stairsLebethron, 5, 20);
		Blocks.fire.setFireInfo(stairsBeech, 5, 20);
		Blocks.fire.setFireInfo(stairsHolly, 5, 20);
		Blocks.fire.setFireInfo(stairsBanana, 5, 20);
		Blocks.fire.setFireInfo(stairsMaple, 5, 20);
		Blocks.fire.setFireInfo(stairsLarch, 5, 20);
		
		Blocks.fire.setFireInfo(clover, 60, 100);
		Blocks.fire.setFireInfo(deadPlant, 60, 100);
		Blocks.fire.setFireInfo(mordorThorn, 60, 100);
		Blocks.fire.setFireInfo(mordorMoss, 60, 100);
		
		oreCopper.setHarvestLevel("pickaxe", 1);
		oreTin.setHarvestLevel("pickaxe", 1);
		oreSilver.setHarvestLevel("pickaxe", 2);
		oreMithril.setHarvestLevel("pickaxe", 3);
		blockOreStorage.setHarvestLevel("pickaxe", 1, 0);
		blockOreStorage.setHarvestLevel("pickaxe", 1, 1);
		blockOreStorage.setHarvestLevel("pickaxe", 1, 2);
		blockOreStorage.setHarvestLevel("pickaxe", 2, 3);
		blockOreStorage.setHarvestLevel("pickaxe", 3, 4);
		blockOreStorage.setHarvestLevel("pickaxe", 1, 5);
		blockOreStorage.setHarvestLevel("pickaxe", 2, 6);
		blockOreStorage.setHarvestLevel("pickaxe", 1, 7);
		blockOreStorage.setHarvestLevel("pickaxe", 2, 8);
		blockOreStorage.setHarvestLevel("pickaxe", 1, 9);
		blockOreStorage.setHarvestLevel("pickaxe", 2, 11);
		blockOreStorage.setHarvestLevel("pickaxe", 2, 12);
		oreMorgulIron.setHarvestLevel("pickaxe", 1);
		oreQuendite.setHarvestLevel("pickaxe", 2);
		quenditeGrass.setHarvestLevel("shovel", 0);
		oreGlowstone.setHarvestLevel("pickaxe", 1);
		remains.setHarvestLevel("shovel", 0);
		oreGulduril.setHarvestLevel("pickaxe", 2);
		quagmire.setHarvestLevel("shovel", 0);
		
		GameRegistry.registerTileEntity(LOTRTileEntityBeacon.class, "LOTRBeacon");
		GameRegistry.registerTileEntity(LOTRTileEntityHobbitOven.class, "LOTROven");
		GameRegistry.registerTileEntity(LOTRTileEntityMobSpawner.class, "LOTRMobSpawner");
		GameRegistry.registerTileEntity(LOTRTileEntityPlate.class, "LOTRPlate");
		GameRegistry.registerTileEntity(LOTRTileEntityElvenPortal.class, "LOTRElvenPortal");
		GameRegistry.registerTileEntity(LOTRTileEntityDwarvenForge.class, "LOTRForge");
		GameRegistry.registerTileEntity(LOTRTileEntityFlowerPot.class, "LOTRFlowerPot");
		GameRegistry.registerTileEntity(LOTRTileEntitySpawnerChest.class, "LOTRSpawnerChest");
		GameRegistry.registerTileEntity(LOTRTileEntityGulduril.class, "LOTRGulduril");
		GameRegistry.registerTileEntity(LOTRTileEntityDwarvenDoor.class, "LOTRDwarvenDoor");
		GameRegistry.registerTileEntity(LOTRTileEntityMorgulPortal.class, "LOTRMorgulPortal");
		GameRegistry.registerTileEntity(LOTRTileEntityBarrel.class, "LOTRBarrel");
		GameRegistry.registerTileEntity(LOTRTileEntityArmorStand.class, "LOTRArmorStand");
		GameRegistry.registerTileEntity(LOTRTileEntityMug.class, "LOTRMug");
		GameRegistry.registerTileEntity(LOTRTileEntityEntJar.class, "LOTREntJar");
		GameRegistry.registerTileEntity(LOTRTileEntityOrcForge.class, "LOTROrcForge");
		GameRegistry.registerTileEntity(LOTRTileEntityTrollTotem.class, "LOTRTrollTotem");
		
		LOTREntities.registerCreature(LOTREntityHorse.class, "Horse", 1, 0x834121, 0x3F1E0E);
		LOTREntities.registerCreature(LOTREntityHobbit.class, "Hobbit", 2, 0xFF9F7F, 0x7A3A23);
		LOTREntities.registerCreature(LOTREntityMordorOrc.class, "MordorOrc", 3, 0x332B22, 0x6B7567);
		LOTREntities.registerCreature(LOTREntityShirePony.class, "ShirePony", 4, 0x5D3B2A, 0xC6A38B);
		LOTREntities.registerCreature(LOTREntityMordorWarg.class, "MordorWarg", 5, 0x463329, 0x291D16);
		LOTREntities.registerCreature(LOTREntityGondorSoldier.class, "GondorSoldier", 6, 0x514C4C, 0xE5DADA);
		LOTREntities.registerCreature(LOTREntitySauron.class, "Sauron", 7);
		LOTREntities.registerCreature(LOTREntityElf.class, "GaladhrimElf", 8, 0x8E7961, 0xF2EDAB);
		LOTREntities.registerCreature(LOTREntityHobbitBartender.class, "HobbitBartender", 9, 0xFF9F7F, 0x7A3A23);
		LOTREntities.registerCreature(LOTREntityHobbitDrunkard.class, "HobbitDrunkard", 10, 0xFF9F7F, 0x7A3A23);
		LOTREntities.registerCreature(LOTREntityElfWarrior.class, "GaladhrimWarrior", 11, 0xC1BEBA, 0xEAB956);
		LOTREntities.registerCreature(LOTREntityMordorOrcBombardier.class, "MordorOrcBombardier", 12, 0x332B22, 0x6B7567);
		LOTREntities.registerCreature(LOTREntityMordorOrcTrader.class, "MordorOrcTrader", 13, 0x5B3D2C, 0xCCCCCC);
		LOTREntities.registerCreature(LOTREntityMordorOrcArcher.class, "MordorOrcArcher", 14, 0x332B22, 0x6B7567);
		LOTREntities.registerCreature(LOTREntityGondorRuinsWraith.class, "GondorRuinsWraith", 15, 0xC1C1C1, 0x494949);
		LOTREntities.registerCreature(LOTREntityGondorBlacksmith.class, "GondorBlacksmith", 16, 0xBCA286, 0x555454);
		LOTREntities.registerCreature(LOTREntityElvenTrader.class, "GaladhrimTrader", 17, 0x1F3F22, 0xFFC526);
		LOTREntities.registerCreature(LOTREntityDwarf.class, "Dwarf", 18, 0xF9876D, 0xEA5620);
		LOTREntities.registerCreature(LOTREntityDwarfWarrior.class, "DwarfWarrior", 19, 0x22282A, 0x6C787A);
		LOTREntities.registerCreature(LOTREntityDwarfMiner.class, "DwarfMiner", 20, 0xF9876D, 0xEA5620);
		LOTREntities.registerCreature(LOTREntityMarshWraith.class, "MarshWraith", 21, 0xA09584, 0x5E4B34);
		LOTREntities.registerCreature(LOTREntityMordorWargBombardier.class, "MordorWargBombardier", 22, 0x463329, 0x291D16);
		LOTREntities.registerCreature(LOTREntityMordorOrcMercenaryCaptain.class, "MordorOrcMercenaryCaptain", 23, 0x332B22, 0x6B7567);
		LOTREntities.registerCreature(LOTREntityGondorianCaptain.class, "GondorianCaptain", 24, 0x514C4C, 0xE5DADA);
		LOTREntities.registerCreature(LOTREntityDwarfCommander.class, "DwarfCommander", 25, 0x22282A, 0x6C787A);
		LOTREntities.registerCreature(LOTREntityDwarfAxeThrower.class, "DwarfAxeThrower", 26, 0x22282A, 0x6C787A);
		LOTREntities.registerCreature(LOTREntityGondorArcher.class, "GondorArcher", 27, 0x514C4C, 0xE5DADA);
		LOTREntities.registerCreature(LOTREntityUrukHai.class, "UrukHai", 28, 0x24261A, 0x58593F);
		LOTREntities.registerCreature(LOTREntityUrukHaiCrossbower.class, "UrukHaiCrossbower", 29, 0x24261A, 0x58593F);
		LOTREntities.registerCreature(LOTREntityUrukHaiBerserker.class, "UrukHaiBerserker", 30, 0x24261A, 0x58593F);
		LOTREntities.registerCreature(LOTREntityUrukHaiTrader.class, "UrukHaiTrader", 31, 0x5B3D2C, 0xCCCCCC);
		LOTREntities.registerCreature(LOTREntityUrukHaiMercenaryCaptain.class, "UrukHaiMercenaryCaptain", 32, 0x24261A, 0x58593F);
		LOTREntities.registerCreature(LOTREntityTroll.class, "Troll", 33, 0xA58752, 0x49311E);
		LOTREntities.registerCreature(LOTREntityOlogHai.class, "OlogHai", 34, 0x3F483C, 0x222322);
		LOTREntities.registerCreature(LOTREntityElfLord.class, "GaladhrimLord", 35, 0xC1BEBA, 0xEAB956);
		LOTREntities.registerCreature(LOTREntityGollum.class, "Gollum", 36);
		LOTREntities.registerCreature(LOTREntityMirkwoodSpider.class, "MirkwoodSpider", 37, 0x282521, 0x141110);
		LOTREntities.registerCreature(LOTREntityWoodElf.class, "WoodElf", 38, 0x235121, 0xFFCE9E);
		LOTREntities.registerCreature(LOTREntityWoodElfScout.class, "WoodElfScout", 39, 0x05210C, 0x3B6033);
		LOTREntities.registerCreature(LOTREntityRohanBarrowWraith.class, "RohanBarrowWraith", 40, 0xC1C1C1, 0x494949);
		LOTREntities.registerCreature(LOTREntityRohirrim.class, "Rohirrim", 41, 0x5E504C, 0xE2B678);
		LOTREntities.registerCreature(LOTREntityRohirrimArcher.class, "RohirrimArcher", 42, 0x5E504C, 0xE2B678);
		LOTREntities.registerCreature(LOTREntityRohirrimMarshal.class, "RohirrimMarshal", 43, 0x5E504C, 0xE2B678);
		LOTREntities.registerCreature(LOTREntityHobbitShirriff.class, "HobbitShirriff", 44, 0xFF9F7F, 0x7A3A23);
		LOTREntities.registerCreature(LOTREntityHobbitShirriffChief.class, "HobbitShirriffChief", 45, 0xFF9F7F, 0x7A3A23);
		LOTREntities.registerCreature(LOTREntityRohanBlacksmith.class, "RohanBlacksmith", 46, 0xBCA286, 0x555454);
		LOTREntities.registerCreature(LOTREntityRangerNorth.class, "RangerNorth", 47, 0x3D4425, 0x4B3322);
		LOTREntities.registerCreature(LOTREntityRangerIthilien.class, "RangerIthilien", 48, 0x3D4425, 0x15190D);
		LOTREntities.registerCreature(LOTREntityDunlendingWarrior.class, "DunlendingWarrior", 49, 0x4F3C31, 0x8E7C77);
		LOTREntities.registerCreature(LOTREntityDunlendingArcher.class, "DunlendingArcher", 50, 0x4F3C31, 0x8E7C77);
		LOTREntities.registerCreature(LOTREntityDunlendingWarlord.class, "DunlendingWarlord", 51, 0x4F3C31, 0x8E7C77);
		LOTREntities.registerCreature(LOTREntityDunlending.class, "Dunlending", 52, 0xF29472, 0x38241A);
		LOTREntities.registerCreature(LOTREntityDunlendingBartender.class, "DunlendingBartender", 53, 0xF29472, 0x38241A);
		LOTREntities.registerCreature(LOTREntityDunlendingDrunkard.class, "DunlendingDrunkard", 54, 0xF29472, 0x38241A);
		LOTREntities.registerCreature(LOTREntityEnt.class, "Ent", 55, 0x382B18, 0x67962A);
		LOTREntities.registerCreature(LOTREntityMountainTroll.class, "MountainTroll", 56, 0x987359, 0x563D29);
		LOTREntities.registerCreature(LOTREntityMountainTrollChieftain.class, "MountainTrollChieftain", 57);
		LOTREntities.registerCreature(LOTREntityHuorn.class, "Huorn", 58, 0x382B18, 0x67962A);
		LOTREntities.registerCreature(LOTREntityDarkHuorn.class, "DarkHuorn", 59, 0x261E13, 0x28561D);
		LOTREntities.registerCreature(LOTREntityWoodElfWarrior.class, "WoodElfWarrior", 60, 0xB1B391, 0x59673E);
		LOTREntities.registerCreature(LOTREntityWoodElfCaptain.class, "WoodElfCaptain", 61, 0xB1B391, 0x59673E);
		LOTREntities.registerCreature(LOTREntityRohanMeadhost.class, "RohanMeadhost", 62, 0x643526, 0xDB9C52);
		LOTREntities.registerCreature(LOTREntityButterfly.class, "Butterfly", 63, 0x26201D, 0xFF8C16);
		LOTREntities.registerCreature(LOTREntityMidges.class, "Midges", 64, 0x564230, 0x1E1812);
		LOTREntities.registerCreature(LOTREntityAngmarOrcMercenaryCaptain.class, "AngmarOrcMercenaryCaptain", 65, 0x313529, 0x557749);
		LOTREntities.registerCreature(LOTREntityAngmarOrcWarrior.class, "AngmarOrcWarrior", 66, 0x313529, 0x557749);
		LOTREntities.registerCreature(LOTREntityNurnSlave.class, "NurnSlave", 67, 0x4C342D, 0x3A231C);
		LOTREntities.registerCreature(LOTREntityRabbit.class, "Rabbit", 68, 0x967452, 0x543E2A);
		LOTREntities.registerCreature(LOTREntityWildBoar.class, "Boar", 69, 0x65402A, 0x3E1802);
		LOTREntities.registerCreature(LOTREntityHobbitOrcharder.class, "HobbitOrcharder", 70, 0xFF9F7F, 0x7A3A23);
		LOTREntities.registerCreature(LOTREntityMordorOrcSlaver.class, "MordorOrcSlaver", 71, 0x332B22, 0x6B7567);
		LOTREntities.registerCreature(LOTREntityMordorSpider.class, "MordorSpider", 72, 0x170F0D, 0xC51B1E);
		LOTREntities.registerCreature(LOTREntityMordorOrcSpiderKeeper.class, "MordorOrcSpiderKeeper", 73, 0x332B22, 0x6B7567);
		LOTREntities.registerCreature(LOTREntityAngmarOrc.class, "AngmarOrc", 74, 0x313529, 0x557749);
		LOTREntities.registerCreature(LOTREntityAngmarOrcArcher.class, "AngmarOrcArcher", 75, 0x313529, 0x557749);
		LOTREntities.registerCreature(LOTREntityAngmarOrcBombardier.class, "AngmarOrcBombardier", 76, 0x313529, 0x557749);
		LOTREntities.registerCreature(LOTREntityGundabadOrc.class, "GundabadOrc", 77, 0x33271A, 0x827053);
		LOTREntities.registerCreature(LOTREntityGundabadOrcArcher.class, "GundabadOrcArcher", 78, 0x33271A, 0x827053);
		LOTREntities.registerCreature(LOTREntityGundabadOrcMercenaryCaptain.class, "GundabadOrcMercenaryCaptain", 79, 0x33271A, 0x827053);
		LOTREntities.registerCreature(LOTREntityRangerNorthCaptain.class, "RangerNorthCaptain", 80, 0x3D4425, 0x4B3322);
		LOTREntities.registerCreature(LOTREntityGundabadWarg.class, "GundabadWarg", 81, 0x463329, 0x291D16);
		LOTREntities.registerCreature(LOTREntityAngmarWarg.class, "AngmarWarg", 82, 0x463329, 0x291D16);
		LOTREntities.registerCreature(LOTREntityAngmarWargBombardier.class, "AngmarWargBombardier", 83, 0x463329, 0x291D16);
		LOTREntities.registerCreature(LOTREntityUrukWarg.class, "UrukWarg", 84, 0x463329, 0x291D16);
		LOTREntities.registerCreature(LOTREntityUrukWargBombardier.class, "UrukWargBombardier", 85, 0x463329, 0x291D16);
		LOTREntities.registerCreature(LOTREntityLion.class, "Lion", 86, 0xCBA24A, 0xA56230);
		LOTREntities.registerCreature(LOTREntityLioness.class, "Lioness", 87, 0xCBA85C, 0xAB7C42);
		LOTREntities.registerCreature(LOTREntityGiraffe.class, "Giraffe", 88, 0xF2B438, 0x604006);
		LOTREntities.registerCreature(LOTREntityZebra.class, "Zebra", 89, 0xE4E4E4, 0x423934);
		LOTREntities.registerCreature(LOTREntityRhino.class, "Rhino", 90, 0x5D5C51, 0xB9B79D);
		LOTREntities.registerCreature(LOTREntityCrocodile.class, "Crocodile", 91, 0x2C3313, 0x0F0F06);
		LOTREntities.registerCreature(LOTREntityNearHaradrim.class, "NearHaradrim", 92, 0xCE967D, 0x917D77);
		LOTREntities.registerCreature(LOTREntityNearHaradrimWarrior.class, "NearHaradrimWarrior", 93, 0x212121, 0xB51B1B);
		LOTREntities.registerCreature(LOTREntityHighElf.class, "HighElf", 94, 0xFFC187, 0xEFE3AB);
		LOTREntities.registerCreature(LOTREntityGemsbok.class, "Gemsbok", 95, 0xB36F3F, 0xF2ECD7);
		LOTREntities.registerCreature(LOTREntityFlamingo.class, "Flamingo", 96, 0xF57B9E, 0xF9D9E3);
		LOTREntities.registerCreature(LOTREntityHobbitFarmer.class, "HobbitFarmer", 97, 0xFF9F7F, 0x7A3A23);
		LOTREntities.registerCreature(LOTREntityHobbitFarmhand.class, "HobbitFarmhand", 98, 0xFF9F7F, 0x7A3A23);
		LOTREntities.registerCreature(LOTREntityHighElfWarrior.class, "HighElfWarrior", 99, 0xFFDB93, 0x4F71A1);
		LOTREntities.registerCreature(LOTREntityHighElfLord.class, "HighElfLord", 100, 0xFFDB93, 0x4F71A1);
		LOTREntities.registerCreature(LOTREntityGondorBannerBearer.class, "GondorBannerBearer", 101, 0x514C4C, 0xE5DADA);
		LOTREntities.registerCreature(LOTREntityRohanBannerBearer.class, "RohanBannerBearer", 102, 0x5E504C, 0xE2B678);
		LOTREntities.registerCreature(LOTREntityMordorBannerBearer.class, "MordorBannerBearer", 103, 0x332B22, 0x6B7567);
		LOTREntities.registerCreature(LOTREntityGaladhrimBannerBearer.class, "GaladhrimBannerBearer", 104, 0xC1BEBA, 0xEAB956);
		LOTREntities.registerCreature(LOTREntityWoodElfBannerBearer.class, "WoodElfBannerBearer", 105, 0xB1B391, 0x59673E);
		LOTREntities.registerCreature(LOTREntityDunlendingBannerBearer.class, "DunlendingBannerBearer", 106, 0x4F3C31, 0x8E7C77);
		LOTREntities.registerCreature(LOTREntityUrukHaiBannerBearer.class, "UrukHaiBannerBearer", 107, 0x24261A, 0x58593F);
		LOTREntities.registerCreature(LOTREntityDwarfBannerBearer.class, "DwarfBannerBearer", 108, 0x22282A, 0x6C787A);
		LOTREntities.registerCreature(LOTREntityAngmarBannerBearer.class, "AngmarBannerBearer", 109, 0x313529, 0x557749);
		LOTREntities.registerCreature(LOTREntityNearHaradBannerBearer.class, "NearHaradBannerBearer", 110, 0x212121, 0xB51B1B);
		LOTREntities.registerCreature(LOTREntityHighElfBannerBearer.class, "HighElfBannerBearer", 111, 0xFFDB93, 0x4F71A1);
		LOTREntities.registerCreature(LOTREntityJungleScorpion.class, "JungleScorpion", 112, 0x282521, 0x141110);
		LOTREntities.registerCreature(LOTREntityDesertScorpion.class, "DesertScorpion", 113, 0xF9E3BC, 0xB3A381);
		LOTREntities.registerCreature(LOTREntityBird.class, "Bird", 114, 0x71B4E0, 0x71B4E0);
		LOTREntities.registerCreature(LOTREntityCrebain.class, "Crebain", 115, 0x252525, 0xA01200);
		LOTREntities.registerCreature(LOTREntityCamel.class, "Camel", 116, 0xC8A76D, 0x8C7038);
		LOTREntities.registerCreature(LOTREntityNearHaradrimArcher.class, "NearHaradrimArcher", 117, 0x212121, 0xB51B1B);
		LOTREntities.registerCreature(LOTREntityNearHaradrimWarlord.class, "NearHaradrimWarlord", 118, 0x212121, 0xB51B1B);
		LOTREntities.registerCreature(LOTREntityBlueDwarf.class, "BlueDwarf", 119, 0xF9876D, 0xEA5620);
		LOTREntities.registerCreature(LOTREntityBlueDwarfWarrior.class, "BlueDwarfWarrior", 120, 0x303E49, 0x5F7B8F);
		LOTREntities.registerCreature(LOTREntityBlueDwarfAxeThrower.class, "BlueDwarfAxeThrower", 121, 0x303E49, 0x5F7B8F);
		LOTREntities.registerCreature(LOTREntityBlueDwarfBannerBearer.class, "BlueDwarfBannerBearer", 122, 0x303E49, 0x5F7B8F);
		LOTREntities.registerCreature(LOTREntityBlueDwarfCommander.class, "BlueDwarfCommander", 123, 0x303E49, 0x5F7B8F);
		LOTREntities.registerCreature(LOTREntityBlueDwarfMiner.class, "BlueDwarfMiner", 124, 0xF9876D, 0xEA5620);
		LOTREntities.registerCreature(LOTREntityNearHaradDrinksTrader.class, "NearHaradDrinksTrader", 125, 0xCE967D, 0x917D77);
		LOTREntities.registerCreature(LOTREntityNearHaradMineralsTrader.class, "NearHaradMineralsTrader", 126, 0xCE967D, 0x917D77);
		LOTREntities.registerCreature(LOTREntityNearHaradPlantsTrader.class, "NearHaradPlantsTrader", 127, 0xCE967D, 0x917D77);
		LOTREntities.registerCreature(LOTREntityNearHaradFoodTrader.class, "NearHaradFoodTrader", 128, 0xCE967D, 0x917D77);
		LOTREntities.registerCreature(LOTREntityBlueDwarfMerchant.class, "BlueDwarfMerchant", 129, 0xF9876D, 0xEA5620);
		LOTREntities.registerCreature(LOTREntityBandit.class, "Bandit", 130, 0xF79574, 0x513B21);

		LOTREntities.registerEntity(LOTREntityPortal.class, "Portal", 200, 80, 3, true);
		LOTREntities.registerEntity(LOTREntitySmokeRing.class, "SmokeRing", 201, 64, 10, true);
		LOTREntities.registerEntity(LOTREntityOrcBomb.class, "OrcBomb", 202, 160, 10, true);
		LOTREntities.registerEntity(LOTREntityGandalfFireball.class, "GandalfFireball", 203, 64, 10, true);
		LOTREntities.registerEntity(LOTREntitySpear.class, "Spear", 204, 64, Integer.MAX_VALUE, false);
		LOTREntities.registerEntity(LOTREntityPlate.class, "Plate", 205, 64, 10, true);
		LOTREntities.registerEntity(LOTREntityWargskinRug.class, "WargRug", 206, 80, 3, true);
		LOTREntities.registerEntity(LOTREntityMarshWraithBall.class, "MarshWraithBall", 207, 64, 10, true);
		LOTREntities.registerEntity(LOTREntityThrowingAxe.class, "ThrowingAxe", 208, 64, Integer.MAX_VALUE, false);
		LOTREntities.registerEntity(LOTREntityCrossbowBolt.class, "CrossbowBolt", 209, 64, Integer.MAX_VALUE, false);
		LOTREntities.registerEntity(LOTREntityStoneTroll.class, "StoneTroll", 210, 80, 3, true);
		LOTREntities.registerEntity(LOTREntityPebble.class, "Pebble", 211, 64, 10, true);
		LOTREntities.registerEntity(LOTREntityMysteryWeb.class, "MysteryWeb", 212, 64, 10, true);
		LOTREntities.registerEntity(LOTREntityTraderRespawn.class, "TraderRespawn", 213, 80, 3, true);
		LOTREntities.registerEntity(LOTREntityThrownRock.class, "ThrownRock", 214, 64, 10, true);
		LOTREntities.registerEntity(LOTREntityBarrel.class, "Barrel", 215, 80, 3, true);
		LOTREntities.registerEntity(LOTREntityBanner.class, "Banner", 216, 160, 3, true);
		LOTREntities.registerEntity(LOTREntityBannerWall.class, "WallBanner", 217, 160, Integer.MAX_VALUE, false);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
		
		FMLEventChannel channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("lotr");
		channel.register(new LOTRPacketHandlerServer());
		
		DimensionManager.registerProviderType(idDimension, LOTRWorldProvider.class, true);
		DimensionManager.registerDimension(idDimension, idDimension);
		
		BlockDispenser.dispenseBehaviorRegistry.putObject(orcBomb, new LOTRDispenserBehaviorOrcBomb());
		BlockDispenser.dispenseBehaviorRegistry.putObject(spawnEgg, new LOTRDispenserBehaviorMobEgg());
		BlockDispenser.dispenseBehaviorRegistry.putObject(plate, new LOTRDispenserBehaviorPlate());
		BlockDispenser.dispenseBehaviorRegistry.putObject(crossbowBolt, new LOTRDispenserBehaviorCrossbowBolt());
		BlockDispenser.dispenseBehaviorRegistry.putObject(pebble, new LOTRDispenserBehaviorPebble());
		BlockDispenser.dispenseBehaviorRegistry.putObject(mysteryWeb, new LOTRDispenserBehaviorMysteryWeb());
		
		LOTRSpeech.loadAllSpeechBanks();
		LOTRNames.loadAllNameBanks();
		LOTRBrewingRecipes.createBrewingRecipes();
		LOTREntJarRecipes.createDraughtRecipes();
		LOTRAchievement.createAchievements();
		LOTRStructures.registerStructures();
		
		LOTRReflection.testAll();
	}
	
	@Mod.EventHandler
	public void postload(FMLPostInitializationEvent event) {}
	
	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		World world = DimensionManager.getWorld(0);
		
		//world.getGameRules().addGameRule("allowPVPBetweenSameAlignment", "true");
		world.getGameRules().addGameRule("enableOrcSkirmish", "true");
		world.getGameRules().addGameRule("enableMiddleEarthRespawning", "true");
		
		event.registerServerCommand(new LOTRCommandAlignment());
		event.registerServerCommand(new LOTRCommandFastTravelTimer());
		event.registerServerCommand(new LOTRCommandSummon());
		event.registerServerCommand(new LOTRCommandFastTravelCooldown());
		if (event.getServer().isDedicatedServer())
		{
			event.registerServerCommand(new LOTRCommandBanStructures());
			event.registerServerCommand(new LOTRCommandAllowStructures());
		}
	}
	
	private void registerBlock(Block block)
	{
		GameRegistry.registerBlock(block, block.getUnlocalizedName());
	}
	
	private void registerBlock(Block block, Class itemClass)
	{
		GameRegistry.registerBlock(block, itemClass, block.getUnlocalizedName());
	}
	
	private void registerItem(Item item)
	{
		GameRegistry.registerItem(item, item.getUnlocalizedName());
	}
	
	public static LOTRFaction getNPCFaction(Entity entity)
	{
		if (entity == null)
		{
			return LOTRFaction.UNALIGNED;
		}
		if (entity instanceof LOTREntityNPC)
		{
			return ((LOTREntityNPC)entity).getFaction();
		}
		
		String s = EntityList.getEntityString(entity);
		if (LOTREntityRegistry.registeredNPCs.get(s) != null)
		{
			RegistryInfo info = (RegistryInfo)LOTREntityRegistry.registeredNPCs.get(s);
			return info.alignmentFaction;
		}
		
		return LOTRFaction.UNALIGNED;
	}
	
	public static void transferEntityToDimension(Entity entity, int newDimension, Teleporter teleporter)
	{
		if (entity instanceof LOTREntityPortal)
		{
			return;
		}
		
        if (!entity.worldObj.isRemote && !entity.isDead)
        {
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            int oldDimension = entity.dimension;
            WorldServer oldWorld = minecraftserver.worldServerForDimension(oldDimension);
            WorldServer newWorld = minecraftserver.worldServerForDimension(newDimension);
            entity.dimension = newDimension;
            entity.worldObj.removeEntity(entity);
            entity.isDead = false;
            minecraftserver.getConfigurationManager().transferEntityToWorld(entity, oldDimension, oldWorld, newWorld, teleporter);
            Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), newWorld);

            if (newEntity != null)
            {
                newEntity.copyDataFrom(entity, true);
                newWorld.spawnEntityInWorld(newEntity);
            }

            entity.isDead = true;
            oldWorld.resetUpdateEntityTick();
            newWorld.resetUpdateEntityTick();
			
			if (newEntity != null)
			{
				newEntity.timeUntilPortal = newEntity.getPortalCooldown();
			}
        }
	}
	
	public static void dropContainerItems(IInventory container, World world, int i, int j, int k)
	{
		for (int l = 0; l < container.getSizeInventory(); l++)
		{
			ItemStack item = container.getStackInSlot(l);
			if (item != null)
			{
				float f = world.rand.nextFloat() * 0.8F + 0.1F;
				float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
				float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

				while (item.stackSize > 0)
				{
					int i1 = world.rand.nextInt(21) + 10;
					if (i1 > item.stackSize)
					{
						i1 = item.stackSize;
					}

					item.stackSize -= i1;
					EntityItem entityItem = new EntityItem(world, (double)((float)i + f), (double)((float)j + f1), (double)((float)k + f2), new ItemStack(item.getItem(), i1, item.getItemDamage()));

					if (item.hasTagCompound())
					{
						entityItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
					}

					entityItem.motionX = (double)((float)world.rand.nextGaussian() * 0.05F);
					entityItem.motionY = (double)((float)world.rand.nextGaussian() * 0.05F + 0.2F);
					entityItem.motionZ = (double)((float)world.rand.nextGaussian() * 0.05F);
					world.spawnEntityInWorld(entityItem);
				}
			}
		}
	}
	
	public static boolean isNewYearsDay()
	{
		Calendar calendar = Calendar.getInstance();
		return calendar.get(2) + 1 == 1 && calendar.get(5) == 1;
	}
	
	public static boolean isAprilFools()
	{
		Calendar calendar = Calendar.getInstance();
		return calendar.get(2) + 1 == 4 && calendar.get(5) == 1;
	}
	
	public static boolean isHalloween()
	{
		Calendar calendar = Calendar.getInstance();
		return calendar.get(2) + 1 == 10 && calendar.get(5) == 31;
	}
	
	public static boolean isChristmas()
	{
		Calendar calendar = Calendar.getInstance();
		return calendar.get(2) + 1 == 12 && (calendar.get(5) == 24 || calendar.get(5) == 25 || calendar.get(5) == 26);
	}
	
	public static EntityPlayer playerSourceOfDamage(DamageSource damagesource)
	{
		if (damagesource.getEntity() instanceof EntityPlayer)
		{
			return (EntityPlayer)damagesource.getEntity();
		}
		else if (damagesource.getEntity() instanceof LOTREntityNPC)
		{
			LOTREntityNPC npc = (LOTREntityNPC)damagesource.getEntity();
			if (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() != null)
			{
				return npc.hiredNPCInfo.getHiringPlayer();
			}
		}
		return null;
	}
	
	public static boolean canPlayerAttackEntity(EntityPlayer attacker, EntityLivingBase target, boolean warnFriendlyFire)
	{
		if (target == null || !target.isEntityAlive())
		{
			return false;
		}

		/*if (target instanceof EntityPlayer)
		{
			EntityPlayer targetPlayer = (EntityPlayer)target;
			int targetAlignment = LOTRLevelData.getAlignment(targetPlayer);

			if (!attacker.worldObj.getGameRules().getGameRuleBooleanValue("allowPVPBetweenSameAlignment") && attacker != targetPlayer)
			{
				if ((alignment > 0 && targetAlignment > 0) || (alignment < 0 && targetAlignment < 0))
				{
					return false;
				}
			}
		}*/
		
		Entity targetNPC = null;
		LOTRFaction targetNPCFaction = null;
		boolean flag = false;
		boolean friendlyFire = false;
		if (getNPCFaction(target) != LOTRFaction.UNALIGNED)
		{
			targetNPC = target;
		}
		else if (getNPCFaction(target.riddenByEntity) != LOTRFaction.UNALIGNED)
		{
			targetNPC = target.riddenByEntity;
		}
		
		if (targetNPC != null)
		{
			targetNPCFaction = getNPCFaction(targetNPC);
			if (targetNPC instanceof LOTREntityNPC && ((LOTREntityNPC)targetNPC).hiredNPCInfo.isActive)
			{
				if (((LOTREntityNPC)targetNPC).hiredNPCInfo.getHiringPlayer() == attacker)
				{
					return false;
				}
				else
				{
					flag = true;
				}
			}
			else if (targetNPC instanceof EntityLiving && ((EntityLiving)targetNPC).getAttackTarget() != attacker)
			{
				if (!LOTRLevelData.getFriendlyFire(attacker))
				{
					flag = true;
					friendlyFire = true;
				}
			}
		}
		
		if (flag)
		{
			if (LOTRLevelData.getAlignment(attacker, targetNPCFaction) > 0)
			{
				if (friendlyFire && warnFriendlyFire)
				{
					LOTRGuiMessageTypes.FRIENDLY_FIRE.sendMessageIfNotReceived(attacker);
				}
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean canNPCAttackEntity(EntityCreature attacker, EntityLivingBase target)
	{
		if (target == null || !target.isEntityAlive())
		{
			return false;
		}
		
		if (attacker instanceof LOTREntityNPC)
		{
			EntityPlayer hiringPlayer = ((LOTREntityNPC)attacker).hiredNPCInfo.getHiringPlayer();
			if (hiringPlayer != null)
			{
				if (target == hiringPlayer || target.riddenByEntity == hiringPlayer)
				{
					return false;
				}
			}
		}
		
		LOTRFaction attackerFaction = getNPCFaction(attacker);
		if (attackerFaction.allowEntityRegistry)
		{
			if (getNPCFaction(target).isAllied(attackerFaction) && attacker.getAttackTarget() != target)
			{
				return false;
			}
			else if (target.riddenByEntity != null && getNPCFaction(target.riddenByEntity).isAllied(attackerFaction) && attacker.getAttackTarget() != target.riddenByEntity)
			{
				return false;
			}
			else if (target instanceof EntityPlayer && LOTRLevelData.getAlignment((EntityPlayer)target, attackerFaction) >= 0 && attacker.getAttackTarget() != target)
			{
				return false;
			}
			else if (target.riddenByEntity instanceof EntityPlayer && LOTRLevelData.getAlignment((EntityPlayer)target.riddenByEntity, attackerFaction) >= 0 && attacker.getAttackTarget() != target.riddenByEntity)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isOpaque(World world, int i, int j, int k)
	{
		return world.getBlock(i, j, k).isOpaqueCube();
	}

	public static boolean isOreNameEqual(ItemStack itemstack, String name)
	{
		List<ItemStack> list = OreDictionary.getOres(name);
		for (ItemStack obj : list)
		{
			if (OreDictionary.itemMatches(obj, itemstack, false))
			{
				return true;
			}
		}
		return false;
	}
}
