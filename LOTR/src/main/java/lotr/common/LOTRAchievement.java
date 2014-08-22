package lotr.common;

import static lotr.common.LOTRAchievement.Category.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lotr.common.entity.animal.LOTRAmbientCreature;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.EnumHelper;

public class LOTRAchievement implements Comparable
{
	public enum Category
	{
		GENERAL,
		SHIRE,
		BLUE_MOUNTAINS,
		LINDON,
		ERIADOR,
		BREE_LAND,
		ANGMAR,
		EREGION,
		ENEDWAITH,
		MISTY_MOUNTAINS,
		FORODWAITH,
		RHOVANION,
		MIRKWOOD,
		IRON_HILLS,
		LOTHLORIEN,
		ROHAN,
		DUNLAND,
		FANGORN,
		GONDOR,
		NINDALF,
		MORDOR,
		NEAR_HARAD,
		UMBAR,
		FAR_HARAD,
		PERTOROGWAITH,
		RHUN,
		OROCARNI;
		
		private String displayName;
		public List<LOTRAchievement> list = new ArrayList();
		
		private Category()
		{
			displayName = name();
		}
		
		public String getDisplayName()
		{
			return StatCollector.translateToLocal("lotr.achievement.category." + displayName);
		}
	}

	public Category category;
	public int ID;
	public ItemStack icon;
	private String name;
	public boolean isBiomeAchievement;
	public boolean isSpecial;
	public LOTRFaction enemyFaction;
	public LOTRFaction allyFaction;
	
	public LOTRAchievement(Category c, int i, Block block, String s)
	{
		this(c, i, new ItemStack(block), s);
	}
	
	public LOTRAchievement(Category c, int i, Item item, String s)
	{
		this(c, i, new ItemStack(item), s);
	}
	
	public LOTRAchievement(Category c, int i, ItemStack itemstack, String s)
	{
		category = c;
		ID = i;
		icon = itemstack;
		name = s;
		
		for (LOTRAchievement achievement : category.list)
		{
			if (achievement.ID == ID)
			{
				throw new IllegalArgumentException("Duplicate ID " + ID + " for LOTR achievement category " + category.name());
			}
		}
		
		category.list.add(this);
		allAchievements.add(this);
	}
	
	public LOTRAchievement setBiomeAchievement()
	{
		isBiomeAchievement = true;
		return this;
	}
	
	public LOTRAchievement setSpecial()
	{
		isSpecial = true;
		return this;
	}
	
	public LOTRAchievement setRequiresEnemy(LOTRFaction f)
	{
		enemyFaction = f;
		return this;
	}
	
	public LOTRAchievement setRequiresAlly(LOTRFaction f)
	{
		allyFaction = f;
		return this;
	}
	
	public String getTitle()
	{
		return StatCollector.translateToLocal("lotr.achievement." + name + ".title");
	}
	
	public String getDescription()
	{
		return StatCollector.translateToLocal("lotr.achievement." + name + ".desc");
	}
	
	public boolean canPlayerEarn(EntityPlayer entityplayer)
	{
		if (enemyFaction != null && LOTRLevelData.getData(entityplayer).getAlignment(enemyFaction) > 0)
		{
			return false;
		}
		
		if (allyFaction != null && LOTRLevelData.getData(entityplayer).getAlignment(allyFaction) < 0)
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public int compareTo(Object obj)
	{
		if (obj instanceof LOTRAchievement)
		{
			LOTRAchievement other = (LOTRAchievement)obj;
			
			if (isSpecial)
			{
				if (other.isSpecial)
				{
					if (other.ID < ID)
					{
						return 1;
					}
					else if (other.ID == ID)
					{
						return 0;
					}
					else if (other.ID > ID)
					{
						return -1;
					}
				}
				else
				{
					return -1;
				}
			}
			else
			{
				if (other.isSpecial)
				{
					return 1;
				}
			}
			
			if (isBiomeAchievement)
			{
				if (!other.isBiomeAchievement)
				{
					return -1;
				}
			}
			else
			{
				if (other.isBiomeAchievement)
				{
					return 1;
				}
			}
			
			return getTitle().compareTo(other.getTitle());
		}
		
		return 0;
	}
	
	public static List allAchievements = new ArrayList();
	
	public static LOTRAchievement enterMiddleEarth;
	// empty slots
	public static LOTRAchievement killOrc;
	public static LOTRAchievement mineMithril;
	public static LOTRAchievement rideWarg;
	public static LOTRAchievement killWarg;
	public static LOTRAchievement useSpearFromFar;
	public static LOTRAchievement wearFullMithril;
	public static LOTRAchievement gainHighAlcoholTolerance;
	public static LOTRAchievement craftSaddle;
	public static LOTRAchievement drinkOrcDraught;
	public static LOTRAchievement getPouch;
	public static LOTRAchievement craftBronze;
	public static LOTRAchievement killUsingOnlyPlates;
	public static LOTRAchievement wearFullWargFur;
	public static LOTRAchievement brewDrinkInBarrel;
	public static LOTRAchievement findAthelas;
	public static LOTRAchievement drinkAthelasBrew;
	public static LOTRAchievement killLargeMobWithSlingshot;
	public static LOTRAchievement eatMaggotyBread;
	public static LOTRAchievement killWhileDrunk;
	public static LOTRAchievement collectCraftingTables;
	public static LOTRAchievement hitByOrcSpear;
	public static LOTRAchievement killBombardier;
	public static LOTRAchievement earnManyCoins;
	public static LOTRAchievement craftAppleCrumble;
	public static LOTRAchievement killButterfly;
	public static LOTRAchievement enterOcean;
	public static LOTRAchievement useCrossbow;
	public static LOTRAchievement collectCrossbowBolts;
	public static LOTRAchievement travel10;
	public static LOTRAchievement travel20;
	public static LOTRAchievement travel30;
	public static LOTRAchievement attackRabbit;
	public static LOTRAchievement craftRabbitStew;
	public static LOTRAchievement hireFarmer;
	public static LOTRAchievement travel40;
	public static LOTRAchievement travel50;
	public static LOTRAchievement killThievingBandit;
	
	public static LOTRAchievement killHobbit;
	public static LOTRAchievement sellPipeweedLeaf;
	public static LOTRAchievement marryHobbit;
	public static LOTRAchievement findFourLeafClover;
	public static LOTRAchievement useMagicPipe;
	public static LOTRAchievement rideShirePony;
	public static LOTRAchievement tradeBartender;
	public static LOTRAchievement speakToDrunkard;
	public static LOTRAchievement tradeHobbitShirriffChief;
	public static LOTRAchievement killDarkHuorn;
	public static LOTRAchievement enterOldForest;
	public static LOTRAchievement buyOrcharderFood;
	public static LOTRAchievement alignmentGood10_HOBBIT;
	public static LOTRAchievement alignmentGood100_HOBBIT;
	public static LOTRAchievement alignmentGood1000_HOBBIT;
	public static LOTRAchievement rideGiraffeShire;
	public static LOTRAchievement buyPotatoHobbitFarmer;
	
	public static LOTRAchievement enterBlueMountains;
	public static LOTRAchievement alignmentGood10_BLUE_MOUNTAINS;
	public static LOTRAchievement alignmentGood100_BLUE_MOUNTAINS;
	public static LOTRAchievement alignmentGood1000_BLUE_MOUNTAINS;
	public static LOTRAchievement smeltBlueDwarfSteel;
	public static LOTRAchievement killBlueDwarf;
	public static LOTRAchievement wearFullBlueDwarven;
	public static LOTRAchievement useBlueDwarvenTable;
	public static LOTRAchievement tradeBlueDwarfMiner;
	public static LOTRAchievement tradeBlueDwarfCommander;
	public static LOTRAchievement tradeBlueDwarfMerchant;
	public static LOTRAchievement marryBlueDwarf;
	
	public static LOTRAchievement enterLindon;
	//EMPTY
	public static LOTRAchievement alignmentGood10_HIGH_ELF;
	public static LOTRAchievement alignmentGood100_HIGH_ELF;
	public static LOTRAchievement alignmentGood1000_HIGH_ELF;
	public static LOTRAchievement killHighElf;
	public static LOTRAchievement tradeHighElfLord;
	public static LOTRAchievement useHighElvenTable;
	public static LOTRAchievement wearFullHighElven;
	
	public static LOTRAchievement enterBreeland;
	public static LOTRAchievement enterChetwood;
	
	public static LOTRAchievement killRangerNorth;
	public static LOTRAchievement wearFullRanger;
	public static LOTRAchievement killTroll;
	public static LOTRAchievement getTrollStatue;
	public static LOTRAchievement makeTrollSneeze;
	public static LOTRAchievement killMountainTroll;
	public static LOTRAchievement killTrollFleeingSun;
	public static LOTRAchievement killMountainTrollChieftain;
	public static LOTRAchievement shootDownMidges;
	public static LOTRAchievement enterTrollshaws;
	public static LOTRAchievement enterMidgewater;
	public static LOTRAchievement enterLoneLands;
	public static LOTRAchievement enterEttenmoors;
	public static LOTRAchievement enterEriador;
	public static LOTRAchievement enterColdfells;
	public static LOTRAchievement enterSwanfleet;
	public static LOTRAchievement enterMinhiriath;
	public static LOTRAchievement tradeGundabadCaptain;
	public static LOTRAchievement tradeRangerNorthCaptain;
	public static LOTRAchievement alignmentGood10_RANGER_NORTH;
	public static LOTRAchievement alignmentGood100_RANGER_NORTH;
	public static LOTRAchievement alignmentGood1000_RANGER_NORTH;
	public static LOTRAchievement alignmentGood10_GUNDABAD;
	public static LOTRAchievement alignmentGood100_GUNDABAD;
	public static LOTRAchievement alignmentGood1000_GUNDABAD;
	public static LOTRAchievement enterBarrowDowns;
	public static LOTRAchievement useRangerTable;
	
	public static LOTRAchievement tradeAngmarCaptain;
	public static LOTRAchievement killAngmarOrc;
	public static LOTRAchievement enterAngmar;
	public static LOTRAchievement useAngmarTable;
	public static LOTRAchievement wearFullAngmar;
	public static LOTRAchievement alignmentGood10_ANGMAR;
	public static LOTRAchievement alignmentGood100_ANGMAR;
	public static LOTRAchievement alignmentGood1000_ANGMAR;
	
	public static LOTRAchievement enterEregion;
	
	public static LOTRAchievement enterEnedwaith;

	public static LOTRAchievement climbMistyMountains;
	public static LOTRAchievement enterMistyMountains;
	
	public static LOTRAchievement enterForodwaith;
	
	public static LOTRAchievement enterValesOfAnduin;
	public static LOTRAchievement enterGreyMountains;
	public static LOTRAchievement enterGladdenFields;
	public static LOTRAchievement enterEmynMuil;
	public static LOTRAchievement enterBrownLands;
	public static LOTRAchievement enterWilderland;
	public static LOTRAchievement enterDagorlad;
	
	public static LOTRAchievement killMirkwoodSpider;
	public static LOTRAchievement killWoodElf;
	public static LOTRAchievement useWoodElvenTable;
	public static LOTRAchievement drinkWine;
	public static LOTRAchievement wearFullWoodElvenScout;
	public static LOTRAchievement tradeWoodElfCaptain;
	public static LOTRAchievement rideBarrelMirkwood;
	public static LOTRAchievement enterMirkwoodCorrupted;
	public static LOTRAchievement enterMirkwood;
	public static LOTRAchievement wearFullWoodElven;
	public static LOTRAchievement killMirkwoodSpiderMordorSpider;
	public static LOTRAchievement alignmentGood10_WOOD_ELF;
	public static LOTRAchievement alignmentGood100_WOOD_ELF;
	public static LOTRAchievement alignmentGood1000_WOOD_ELF;
	public static LOTRAchievement alignmentGood10_DOL_GULDUR;
	public static LOTRAchievement alignmentGood100_DOL_GULDUR;
	public static LOTRAchievement alignmentGood1000_DOL_GULDUR;
	public static LOTRAchievement enterDolGuldur;
	public static LOTRAchievement killDolGuldurOrc;
	public static LOTRAchievement useDolGuldurTable;
	public static LOTRAchievement tradeDolGuldurCaptain;
	public static LOTRAchievement killMirkTroll;
	
	public static LOTRAchievement killDwarf;
	public static LOTRAchievement wearFullDwarven;
	public static LOTRAchievement useDwarvenThrowingAxe;
	public static LOTRAchievement useDwarvenTable;
	public static LOTRAchievement tradeDwarfMiner;
	public static LOTRAchievement tradeDwarfCommander;
	public static LOTRAchievement mineGlowstone;
	public static LOTRAchievement smeltDwarfSteel;
	public static LOTRAchievement drinkDwarvenTonic;
	public static LOTRAchievement craftMithrilDwarvenBrick;
	public static LOTRAchievement talkDwarfWoman;
	public static LOTRAchievement enterIronHills;
	public static LOTRAchievement useDwarvenDoor;
	public static LOTRAchievement alignmentGood10_DWARF;
	public static LOTRAchievement alignmentGood100_DWARF;
	public static LOTRAchievement alignmentGood1000_DWARF;
	public static LOTRAchievement marryDwarf;
	
	public static LOTRAchievement killElf;
	public static LOTRAchievement useElvenPortal;
	public static LOTRAchievement wearFullElven;
	public static LOTRAchievement useElvenTable;
	public static LOTRAchievement tradeElfLord;
	public static LOTRAchievement mineQuendite;
	public static LOTRAchievement takeMallornWood;
	public static LOTRAchievement wearFullGalvorn;
	public static LOTRAchievement enterLothlorien;
	public static LOTRAchievement alignmentGood10_GALADHRIM;
	public static LOTRAchievement alignmentGood100_GALADHRIM;
	public static LOTRAchievement alignmentGood1000_GALADHRIM;
	public static LOTRAchievement tradeElvenTrader;
	
	public static LOTRAchievement raidUrukCamp;
	public static LOTRAchievement useUrukTable;
	public static LOTRAchievement tradeUrukTrader;
	public static LOTRAchievement tradeUrukCaptain;
	public static LOTRAchievement useRohirricTable;
	public static LOTRAchievement smeltUrukSteel;
	public static LOTRAchievement wearFullUruk;
	public static LOTRAchievement hireWargBombardier;
	public static LOTRAchievement killRohirrim;
	public static LOTRAchievement tradeRohirrimMarshal;
	public static LOTRAchievement wearFullRohirric;
	public static LOTRAchievement tradeRohanBlacksmith;
	public static LOTRAchievement buyRohanMead;
	public static LOTRAchievement enterRohan;
	public static LOTRAchievement enterRohanUrukHighlands;
	public static LOTRAchievement alignmentGood10_ROHAN;
	public static LOTRAchievement alignmentGood100_ROHAN;
	public static LOTRAchievement alignmentGood1000_ROHAN;
	public static LOTRAchievement alignmentGood10_URUK_HAI;
	public static LOTRAchievement alignmentGood100_URUK_HAI;
	public static LOTRAchievement alignmentGood1000_URUK_HAI;
	
	public static LOTRAchievement killDunlending;
	public static LOTRAchievement wearFullDunlending;
	public static LOTRAchievement useDunlendingTable;
	public static LOTRAchievement tradeDunlendingWarlord;
	public static LOTRAchievement useDunlendingTrident;
	public static LOTRAchievement tradeDunlendingBartender;
	public static LOTRAchievement enterDunland;
	public static LOTRAchievement alignmentGood10_DUNLAND;
	public static LOTRAchievement alignmentGood100_DUNLAND;
	public static LOTRAchievement alignmentGood1000_DUNLAND;
	
	public static LOTRAchievement killEnt;
	public static LOTRAchievement drinkEntDraught;
	public static LOTRAchievement killHuorn;
	public static LOTRAchievement talkEnt;
	public static LOTRAchievement enterFangorn;
	public static LOTRAchievement alignmentGood10_FANGORN;
	public static LOTRAchievement alignmentGood100_FANGORN;
	public static LOTRAchievement alignmentGood1000_FANGORN;
	public static LOTRAchievement summonHuorn;
	
	public static LOTRAchievement killGondorSoldier;
	public static LOTRAchievement lightGondorBeacon;
	public static LOTRAchievement useGondorianTable;
	public static LOTRAchievement tradeGondorBlacksmith;
	public static LOTRAchievement tradeGondorianCaptain;
	public static LOTRAchievement wearFullGondorian;
	public static LOTRAchievement killRangerIthilien;
	public static LOTRAchievement enterGondor;
	public static LOTRAchievement enterIthilien;
	public static LOTRAchievement enterWhiteMountains;
	public static LOTRAchievement alignmentGood10_GONDOR;
	public static LOTRAchievement alignmentGood100_GONDOR;
	public static LOTRAchievement alignmentGood1000_GONDOR;
	public static LOTRAchievement enterTolfalas;
	public static LOTRAchievement enterLebennin;
	
	public static LOTRAchievement mineRemains;
	public static LOTRAchievement craftAncientItem;
	public static LOTRAchievement enterDeadMarshes;
	public static LOTRAchievement enterNindalf;
	
	public static LOTRAchievement killOlogHai;
	public static LOTRAchievement useMorgulTable;
	public static LOTRAchievement smeltOrcSteel;
	public static LOTRAchievement wearFullOrc;
	public static LOTRAchievement tradeOrcTrader;
	public static LOTRAchievement tradeOrcCaptain;
	public static LOTRAchievement mineNaurite;
	public static LOTRAchievement eatMorgulShroom;
	public static LOTRAchievement craftOrcBomb;
	public static LOTRAchievement hireOlogHai;
	public static LOTRAchievement mineGulduril;
	public static LOTRAchievement useMorgulPortal;
	public static LOTRAchievement wearFullMorgul;
	public static LOTRAchievement enterMordor;
	public static LOTRAchievement enterNurn;
	public static LOTRAchievement enterNanUngol;
	public static LOTRAchievement killMordorSpider;
	public static LOTRAchievement tradeOrcSpiderKeeper;
	public static LOTRAchievement killMordorOrc;
	public static LOTRAchievement alignmentGood10_MORDOR;
	public static LOTRAchievement alignmentGood100_MORDOR;
	public static LOTRAchievement alignmentGood1000_MORDOR;
	
	public static LOTRAchievement enterHarondor;
	public static LOTRAchievement enterNearHarad;
	public static LOTRAchievement killNearHaradrim;
	public static LOTRAchievement alignmentGood10_NEAR_HARAD;
	public static LOTRAchievement alignmentGood100_NEAR_HARAD;
	public static LOTRAchievement alignmentGood1000_NEAR_HARAD;
	public static LOTRAchievement useNearHaradTable;
	public static LOTRAchievement wearFullNearHarad;
	public static LOTRAchievement tradeNearHaradWarlord;
	public static LOTRAchievement rideCamel;
	public static LOTRAchievement tradeBazaarTrader;
	public static LOTRAchievement tradeNearHaradMerchant;
	
	public static LOTRAchievement enterUmbar;
	
	public static LOTRAchievement enterFarHarad;
	public static LOTRAchievement drinkMangoJuice;
	public static LOTRAchievement pickBanana;
	public static LOTRAchievement alignmentGood10_FAR_HARAD;
	public static LOTRAchievement alignmentGood100_FAR_HARAD;
	public static LOTRAchievement alignmentGood1000_FAR_HARAD;

	public static LOTRAchievement enterPertorogwaith;
	public static LOTRAchievement alignmentGood10_HALF_TROLL;
	public static LOTRAchievement alignmentGood100_HALF_TROLL;
	public static LOTRAchievement alignmentGood1000_HALF_TROLL;
	
	public static LOTRAchievement enterRhun;
	
	public static LOTRAchievement enterRedMountains;
	
	public static void createAchievements()
	{
		enterMiddleEarth = new LOTRAchievement(GENERAL, 1, Items.book, "enterMiddleEarth").setSpecial();
		// empty
		killOrc = new LOTRAchievement(GENERAL, 14, LOTRMod.orcBone, "killOrc");
		mineMithril = new LOTRAchievement(GENERAL, 15, LOTRMod.oreMithril, "mineMithril");
		rideWarg = new LOTRAchievement(GENERAL, 16, Items.saddle, "rideWarg");
		killWarg = new LOTRAchievement(GENERAL, 17, LOTRMod.wargBone, "killWarg");
		useSpearFromFar = new LOTRAchievement(GENERAL, 18, LOTRMod.spearIron, "useSpearFromFar");
		wearFullMithril = new LOTRAchievement(GENERAL, 19, LOTRMod.bodyMithril, "wearFullMithril");
		gainHighAlcoholTolerance = new LOTRAchievement(GENERAL, 20, LOTRMod.mugRum, "gainHighAlcoholTolerance");
		craftSaddle = new LOTRAchievement(GENERAL, 21, Items.saddle, "craftSaddle");
		craftBronze = new LOTRAchievement(GENERAL, 22, LOTRMod.bronze, "craftBronze");
		drinkOrcDraught = new LOTRAchievement(GENERAL, 23, LOTRMod.mugOrcDraught, "drinkOrcDraught");
		getPouch = new LOTRAchievement(GENERAL, 24, LOTRMod.pouch, "getPouch");
		killUsingOnlyPlates = new LOTRAchievement(GENERAL, 25, LOTRMod.plate, "killUsingOnlyPlates");
		wearFullWargFur = new LOTRAchievement(GENERAL, 26, LOTRMod.bodyWarg, "wearFullWargFur");
		brewDrinkInBarrel = new LOTRAchievement(GENERAL, 27, LOTRMod.barrel, "brewDrinkInBarrel");
		findAthelas = new LOTRAchievement(GENERAL, 28, LOTRMod.athelas, "findAthelas");
		drinkAthelasBrew = new LOTRAchievement(GENERAL, 29, LOTRMod.mugAthelasBrew, "drinkAthelasBrew");
		killLargeMobWithSlingshot = new LOTRAchievement(GENERAL, 30, LOTRMod.sling, "killLargeMobWithSlingshot");
		eatMaggotyBread = new LOTRAchievement(GENERAL, 31, LOTRMod.maggotyBread, "eatMaggotyBread");
		killWhileDrunk = new LOTRAchievement(GENERAL, 32, LOTRMod.mugAle, "killWhileDrunk");
		collectCraftingTables = new LOTRAchievement(GENERAL, 33, Blocks.crafting_table, "collectCraftingTables");
		hitByOrcSpear = new LOTRAchievement(GENERAL, 34, LOTRMod.spearOrc, "hitByOrcSpear");
		killBombardier = new LOTRAchievement(GENERAL, 35, LOTRMod.orcBomb, "killBombardier");
		earnManyCoins = new LOTRAchievement(GENERAL, 36, LOTRMod.silverCoin, "earnManyCoins");
		craftAppleCrumble = new LOTRAchievement(GENERAL, 37, LOTRMod.appleCrumbleItem, "craftAppleCrumble");
		killButterfly = new LOTRAchievement(GENERAL, 38, Items.iron_sword, "killButterfly");
		enterOcean = new LOTRAchievement(GENERAL, 39, Items.boat, "enterOcean");
		useCrossbow = new LOTRAchievement(GENERAL, 40, LOTRMod.ironCrossbow, "useCrossbow");
		collectCrossbowBolts = new LOTRAchievement(GENERAL, 41, LOTRMod.crossbowBolt, "collectCrossbowBolts");
		travel10 = new LOTRAchievement(GENERAL, 42, Items.leather_boots, "travel10").setSpecial();
		travel20 = new LOTRAchievement(GENERAL, 43, Items.compass, "travel20").setSpecial();
		travel30 = new LOTRAchievement(GENERAL, 44, Items.map, "travel30").setSpecial();
		attackRabbit = new LOTRAchievement(GENERAL, 45, Items.wheat_seeds, "attackRabbit");
		craftRabbitStew = new LOTRAchievement(GENERAL, 46, LOTRMod.rabbitStew, "craftRabbitStew");
		hireFarmer = new LOTRAchievement(GENERAL, 47, Items.iron_hoe, "hireFarmer");
		travel40 = new LOTRAchievement(GENERAL, 48, Items.map, "travel40").setSpecial();
		travel50 = new LOTRAchievement(GENERAL, 49, Items.map, "travel50").setSpecial();
		killThievingBandit = new LOTRAchievement(GENERAL, 50, LOTRMod.leatherHat, "killThievingBandit");

		killHobbit = new LOTRAchievement(SHIRE, 0, LOTRMod.hobbitBone, "killHobbit").setRequiresEnemy(LOTRFaction.HOBBIT);
		sellPipeweedLeaf = new LOTRAchievement(SHIRE, 1, LOTRMod.pipeweedLeaf, "sellPipeweedLeaf").setRequiresAlly(LOTRFaction.HOBBIT);
		marryHobbit = new LOTRAchievement(SHIRE, 2, LOTRMod.hobbitRing, "marryHobbit").setRequiresAlly(LOTRFaction.HOBBIT);
		findFourLeafClover = new LOTRAchievement(SHIRE, 3, new ItemStack(LOTRMod.clover, 1, 1), "findFourLeafClover");
		useMagicPipe = new LOTRAchievement(SHIRE, 4, LOTRMod.hobbitPipe, "useMagicPipe");
		rideShirePony = new LOTRAchievement(SHIRE, 5, Items.saddle, "rideShirePony");
		tradeBartender = new LOTRAchievement(SHIRE, 6, LOTRMod.silverCoin, "tradeBartender").setRequiresAlly(LOTRFaction.HOBBIT);
		speakToDrunkard = new LOTRAchievement(SHIRE, 7, LOTRMod.mugAle, "speakToDrunkard");
		tradeHobbitShirriffChief = new LOTRAchievement(SHIRE, 8, LOTRMod.silverCoin, "tradeHobbitShirriffChief").setRequiresAlly(LOTRFaction.HOBBIT);
		killDarkHuorn = new LOTRAchievement(SHIRE, 9, Blocks.log, "killDarkHuorn");
		enterOldForest = new LOTRAchievement(SHIRE, 10, Blocks.log, "enterOldForest").setBiomeAchievement();
		buyOrcharderFood = new LOTRAchievement(SHIRE, 11, Items.apple, "buyOrcharderFood").setRequiresAlly(LOTRFaction.HOBBIT);
		alignmentGood10_HOBBIT = new LOTRAchievement(SHIRE, 12, LOTRMod.goldRing, "alignmentGood10_HOBBIT").setRequiresAlly(LOTRFaction.HOBBIT).setSpecial();
		alignmentGood100_HOBBIT = new LOTRAchievement(SHIRE, 13, LOTRMod.goldRing, "alignmentGood100_HOBBIT").setRequiresAlly(LOTRFaction.HOBBIT).setSpecial();
		alignmentGood1000_HOBBIT = new LOTRAchievement(SHIRE, 14, LOTRMod.goldRing, "alignmentGood1000_HOBBIT").setRequiresAlly(LOTRFaction.HOBBIT).setSpecial();
		rideGiraffeShire = new LOTRAchievement(SHIRE, 15, Items.saddle, "rideGiraffeShire");
		buyPotatoHobbitFarmer = new LOTRAchievement(SHIRE, 16, Items.potato, "buyPotatoHobbitFarmer").setRequiresAlly(LOTRFaction.HOBBIT);
		
		enterBlueMountains = new LOTRAchievement(BLUE_MOUNTAINS, 0, new ItemStack(LOTRMod.rock, 1, 3), "enterBlueMountains").setBiomeAchievement();
		alignmentGood10_BLUE_MOUNTAINS = new LOTRAchievement(BLUE_MOUNTAINS, 1, LOTRMod.goldRing, "alignmentGood10_BLUE_MOUNTAINS").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS).setSpecial();
		alignmentGood100_BLUE_MOUNTAINS = new LOTRAchievement(BLUE_MOUNTAINS, 2, LOTRMod.goldRing, "alignmentGood100_BLUE_MOUNTAINS").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS).setSpecial();
		alignmentGood1000_BLUE_MOUNTAINS = new LOTRAchievement(BLUE_MOUNTAINS, 3, LOTRMod.goldRing, "alignmentGood1000_BLUE_MOUNTAINS").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS).setSpecial();
		smeltBlueDwarfSteel = new LOTRAchievement(BLUE_MOUNTAINS, 4, LOTRMod.blueDwarfSteel, "smeltBlueDwarfSteel");
		killBlueDwarf = new LOTRAchievement(BLUE_MOUNTAINS, 5, LOTRMod.dwarfBone, "killBlueDwarf").setRequiresEnemy(LOTRFaction.BLUE_MOUNTAINS);
		wearFullBlueDwarven = new LOTRAchievement(BLUE_MOUNTAINS, 6, LOTRMod.bodyBlueDwarven, "wearFullBlueDwarven");
		useBlueDwarvenTable = new LOTRAchievement(BLUE_MOUNTAINS, 7, LOTRMod.blueDwarvenTable, "useBlueDwarvenTable").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS);
		tradeBlueDwarfMiner = new LOTRAchievement(BLUE_MOUNTAINS, 8, LOTRMod.silverCoin, "tradeBlueDwarfMiner").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS);
		tradeBlueDwarfCommander = new LOTRAchievement(BLUE_MOUNTAINS, 9, LOTRMod.silverCoin, "tradeBlueDwarfCommander").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS);
		tradeBlueDwarfMerchant = new LOTRAchievement(BLUE_MOUNTAINS, 10, LOTRMod.silverCoin, "tradeBlueDwarfMerchant").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS);
		marryBlueDwarf = new LOTRAchievement(BLUE_MOUNTAINS, 11, LOTRMod.dwarvenRing, "marryBlueDwarf").setRequiresAlly(LOTRFaction.BLUE_MOUNTAINS);

		enterLindon = new LOTRAchievement(LINDON, 0, new ItemStack(LOTRMod.brick, 1, 11), "enterLindon").setBiomeAchievement();
		//EMPTY
		alignmentGood10_HIGH_ELF = new LOTRAchievement(LINDON, 2, LOTRMod.goldRing, "alignmentGood10_HIGH_ELF").setRequiresAlly(LOTRFaction.HIGH_ELF).setSpecial();
		alignmentGood100_HIGH_ELF = new LOTRAchievement(LINDON, 3, LOTRMod.goldRing, "alignmentGood100_HIGH_ELF").setRequiresAlly(LOTRFaction.HIGH_ELF).setSpecial();
		alignmentGood1000_HIGH_ELF = new LOTRAchievement(LINDON, 4, LOTRMod.goldRing, "alignmentGood1000_HIGH_ELF").setRequiresAlly(LOTRFaction.HIGH_ELF).setSpecial();
		killHighElf = new LOTRAchievement(LINDON, 5, LOTRMod.elfBone, "killHighElf").setRequiresEnemy(LOTRFaction.HIGH_ELF);
		tradeHighElfLord = new LOTRAchievement(LINDON, 6, LOTRMod.silverCoin, "tradeHighElfLord").setRequiresAlly(LOTRFaction.HIGH_ELF);
		useHighElvenTable = new LOTRAchievement(LINDON, 7, LOTRMod.highElvenTable, "useHighElvenTable").setRequiresAlly(LOTRFaction.HIGH_ELF);
		wearFullHighElven = new LOTRAchievement(LINDON, 8, LOTRMod.bodyHighElven, "wearFullHighElven").setRequiresAlly(LOTRFaction.HIGH_ELF);

		enterBreeland = new LOTRAchievement(BREE_LAND, 0, Blocks.grass, "enterBreeland").setBiomeAchievement();
		enterChetwood = new LOTRAchievement(BREE_LAND, 1, Blocks.sapling, "enterChetwood").setBiomeAchievement();

		killRangerNorth = new LOTRAchievement(ERIADOR, 0, Items.bow, "killRangerNorth").setRequiresEnemy(LOTRFaction.RANGER_NORTH);
		wearFullRanger = new LOTRAchievement(ERIADOR, 1, LOTRMod.bodyRanger, "wearFullRanger");
		killTroll = new LOTRAchievement(ERIADOR, 2, LOTRMod.trollBone, "killTroll").setRequiresEnemy(LOTRFaction.ANGMAR);
		getTrollStatue = new LOTRAchievement(ERIADOR, 3, LOTRMod.trollStatue, "getTrollStatue");
		makeTrollSneeze = new LOTRAchievement(ERIADOR, 4, Items.slime_ball, "makeTrollSneeze").setRequiresAlly(LOTRFaction.ANGMAR);
		killMountainTroll = new LOTRAchievement(ERIADOR, 5, LOTRMod.trollBone, "killMountainTroll").setRequiresEnemy(LOTRFaction.ANGMAR);
		killTrollFleeingSun = new LOTRAchievement(ERIADOR, 6, Items.feather, "killTrollFleeingSun").setRequiresEnemy(LOTRFaction.ANGMAR);
		killMountainTrollChieftain = new LOTRAchievement(ERIADOR, 7, LOTRMod.trollTotem, "killMountainTrollChieftain").setRequiresEnemy(LOTRFaction.ANGMAR);
		shootDownMidges = new LOTRAchievement(ERIADOR, 8, Items.arrow, "shootDownMidges");
		enterTrollshaws = new LOTRAchievement(ERIADOR, 9, Items.cooked_beef, "enterTrollshaws").setBiomeAchievement();
		enterMidgewater = new LOTRAchievement(ERIADOR, 10, LOTRMod.quagmire, "enterMidgewater").setBiomeAchievement();
		enterLoneLands = new LOTRAchievement(ERIADOR, 11, new ItemStack(Blocks.stonebrick, 1, 2), "enterLoneLands").setBiomeAchievement();
		enterEttenmoors = new LOTRAchievement(ERIADOR, 12, new ItemStack(Blocks.sapling, 1, 1), "enterEttenmoors").setBiomeAchievement();
		enterEriador = new LOTRAchievement(ERIADOR, 13, Blocks.grass, "enterEriador").setBiomeAchievement();
		enterColdfells = new LOTRAchievement(ERIADOR, 14, new ItemStack(Blocks.sapling, 1, 0), "enterColdfells").setBiomeAchievement();
		enterSwanfleet = new LOTRAchievement(ERIADOR, 15, Blocks.waterlily, "enterSwanfleet").setBiomeAchievement();
		enterMinhiriath = new LOTRAchievement(ERIADOR, 16, Blocks.grass, "enterMinhiriath").setBiomeAchievement();
		tradeGundabadCaptain =  new LOTRAchievement(ERIADOR, 17, LOTRMod.silverCoin, "tradeGundabadCaptain").setRequiresAlly(LOTRFaction.GUNDABAD);
		tradeRangerNorthCaptain = new LOTRAchievement(ERIADOR, 18, LOTRMod.silverCoin, "tradeRangerNorthCaptain").setRequiresAlly(LOTRFaction.RANGER_NORTH);
		alignmentGood10_RANGER_NORTH = new LOTRAchievement(ERIADOR, 19, LOTRMod.goldRing, "alignmentGood10_RANGER_NORTH").setRequiresAlly(LOTRFaction.RANGER_NORTH).setSpecial();
		alignmentGood100_RANGER_NORTH = new LOTRAchievement(ERIADOR, 20, LOTRMod.goldRing, "alignmentGood100_RANGER_NORTH").setRequiresAlly(LOTRFaction.RANGER_NORTH).setSpecial();
		alignmentGood1000_RANGER_NORTH = new LOTRAchievement(ERIADOR, 21, LOTRMod.goldRing, "alignmentGood1000_RANGER_NORTH").setRequiresAlly(LOTRFaction.RANGER_NORTH).setSpecial();
		alignmentGood10_GUNDABAD = new LOTRAchievement(ERIADOR, 22, LOTRMod.goldRing, "alignmentGood10_GUNDABAD").setRequiresAlly(LOTRFaction.GUNDABAD).setSpecial();
		alignmentGood100_GUNDABAD = new LOTRAchievement(ERIADOR, 23, LOTRMod.goldRing, "alignmentGood100_GUNDABAD").setRequiresAlly(LOTRFaction.GUNDABAD).setSpecial();
		alignmentGood1000_GUNDABAD = new LOTRAchievement(ERIADOR, 24, LOTRMod.goldRing, "alignmentGood1000_GUNDABAD").setRequiresAlly(LOTRFaction.GUNDABAD).setSpecial();
		enterBarrowDowns = new LOTRAchievement(ERIADOR, 25, Items.bone, "enterBarrowDowns").setBiomeAchievement();
		useRangerTable = new LOTRAchievement(ERIADOR, 26, LOTRMod.rangerTable, "useRangerTable").setRequiresAlly(LOTRFaction.RANGER_NORTH);

		tradeAngmarCaptain = new LOTRAchievement(ANGMAR, 0, LOTRMod.silverCoin, "tradeAngmarCaptain").setRequiresAlly(LOTRFaction.ANGMAR);
		killAngmarOrc = new LOTRAchievement(ANGMAR, 1, LOTRMod.orcBone, "killAngmarOrc").setRequiresEnemy(LOTRFaction.ANGMAR);
		enterAngmar = new LOTRAchievement(ANGMAR, 2, new ItemStack(LOTRMod.brick, 1, 7), "enterAngmar").setBiomeAchievement();
		useAngmarTable = new LOTRAchievement(ANGMAR, 3, LOTRMod.angmarTable, "useAngmarTable").setRequiresAlly(LOTRFaction.ANGMAR);
		wearFullAngmar = new LOTRAchievement(ANGMAR, 4, LOTRMod.bodyAngmar, "wearFullAngmar");
		alignmentGood10_ANGMAR = new LOTRAchievement(ANGMAR, 5, LOTRMod.goldRing, "alignmentGood10_ANGMAR").setRequiresAlly(LOTRFaction.ANGMAR).setSpecial();
		alignmentGood100_ANGMAR = new LOTRAchievement(ANGMAR, 6, LOTRMod.goldRing, "alignmentGood100_ANGMAR").setRequiresAlly(LOTRFaction.ANGMAR).setSpecial();
		alignmentGood1000_ANGMAR = new LOTRAchievement(ANGMAR, 7, LOTRMod.goldRing, "alignmentGood1000_ANGMAR").setRequiresAlly(LOTRFaction.ANGMAR).setSpecial();

		enterEregion = new LOTRAchievement(EREGION, 0, new ItemStack(LOTRMod.sapling2, 1, 2), "enterEregion").setBiomeAchievement();

		enterEnedwaith = new LOTRAchievement(ENEDWAITH, 0, Blocks.grass, "enterEnedwaith").setBiomeAchievement();

		climbMistyMountains = new LOTRAchievement(MISTY_MOUNTAINS, 0, Blocks.snow, "climbMistyMountains");
		enterMistyMountains = new LOTRAchievement(MISTY_MOUNTAINS, 1, Blocks.stone, "enterMistyMountains").setBiomeAchievement();

		enterForodwaith = new LOTRAchievement(FORODWAITH, 0, Blocks.ice, "enterForodwaith").setBiomeAchievement();

		enterValesOfAnduin = new LOTRAchievement(RHOVANION, 0, Blocks.sapling, "enterValesOfAnduin").setBiomeAchievement();
		enterGreyMountains = new LOTRAchievement(RHOVANION, 1, Blocks.stone, "enterGreyMountains").setBiomeAchievement();
		enterGladdenFields = new LOTRAchievement(RHOVANION, 2, new ItemStack(LOTRMod.doubleFlower, 1, 1), "enterGladdenFields").setBiomeAchievement();
		enterEmynMuil = new LOTRAchievement(RHOVANION, 3, Blocks.stone, "enterEmynMuil").setBiomeAchievement();
		enterBrownLands = new LOTRAchievement(RHOVANION, 4, Blocks.dirt, "enterBrownLands").setBiomeAchievement();
		enterWilderland = new LOTRAchievement(RHOVANION, 5, Blocks.grass, "enterWilderland").setBiomeAchievement();
		enterDagorlad = new LOTRAchievement(RHOVANION, 6, Blocks.gravel, "enterDagorlad").setBiomeAchievement();

		killMirkwoodSpider = new LOTRAchievement(MIRKWOOD, 0, Items.string, "killMirkwoodSpider").setRequiresEnemy(LOTRFaction.DOL_GULDUR);
		killWoodElf = new LOTRAchievement(MIRKWOOD, 1, LOTRMod.elfBone, "killWoodElf").setRequiresEnemy(LOTRFaction.WOOD_ELF);
		useWoodElvenTable = new LOTRAchievement(MIRKWOOD, 2, LOTRMod.woodElvenTable, "useWoodElvenTable").setRequiresAlly(LOTRFaction.WOOD_ELF);
		drinkWine = new LOTRAchievement(MIRKWOOD, 3, LOTRMod.mugRedWine, "drinkWine");
		wearFullWoodElvenScout = new LOTRAchievement(MIRKWOOD, 4, LOTRMod.bodyWoodElvenScout, "wearFullWoodElvenScout");
		tradeWoodElfCaptain = new LOTRAchievement(MIRKWOOD, 5, LOTRMod.silverCoin, "tradeWoodElfCaptain").setRequiresAlly(LOTRFaction.WOOD_ELF);
		rideBarrelMirkwood = new LOTRAchievement(MIRKWOOD, 6, LOTRMod.barrel, "rideBarrelMirkwood");
		enterMirkwoodCorrupted = new LOTRAchievement(MIRKWOOD, 7, LOTRMod.webUngoliant, "enterMirkwoodCorrupted").setBiomeAchievement();
		enterMirkwood = new LOTRAchievement(MIRKWOOD, 8, LOTRMod.mirkwoodBow, "enterMirkwood").setBiomeAchievement();
		wearFullWoodElven = new LOTRAchievement(MIRKWOOD, 9, LOTRMod.bodyWoodElven, "wearFullWoodElven");
		killMirkwoodSpiderMordorSpider = new LOTRAchievement(MIRKWOOD, 10, Items.spider_eye, "killMirkwoodSpiderMordorSpider").setRequiresAlly(LOTRFaction.MORDOR);
		alignmentGood10_WOOD_ELF = new LOTRAchievement(MIRKWOOD, 11, LOTRMod.goldRing, "alignmentGood10_WOOD_ELF").setRequiresAlly(LOTRFaction.WOOD_ELF).setSpecial();
		alignmentGood100_WOOD_ELF = new LOTRAchievement(MIRKWOOD, 12, LOTRMod.goldRing, "alignmentGood100_WOOD_ELF").setRequiresAlly(LOTRFaction.WOOD_ELF).setSpecial();
		alignmentGood1000_WOOD_ELF = new LOTRAchievement(MIRKWOOD, 13, LOTRMod.goldRing, "alignmentGood1000_WOOD_ELF").setRequiresAlly(LOTRFaction.WOOD_ELF).setSpecial();
		alignmentGood10_DOL_GULDUR = new LOTRAchievement(MIRKWOOD, 14, LOTRMod.goldRing, "alignmentGood10_DOL_GULDUR").setRequiresAlly(LOTRFaction.DOL_GULDUR).setSpecial();
		alignmentGood100_DOL_GULDUR = new LOTRAchievement(MIRKWOOD, 15, LOTRMod.goldRing, "alignmentGood100_DOL_GULDUR").setRequiresAlly(LOTRFaction.DOL_GULDUR).setSpecial();
		alignmentGood1000_DOL_GULDUR = new LOTRAchievement(MIRKWOOD, 16, LOTRMod.goldRing, "alignmentGood1000_DOL_GULDUR").setRequiresAlly(LOTRFaction.DOL_GULDUR).setSpecial();
		enterDolGuldur = new LOTRAchievement(MIRKWOOD, 17, new ItemStack(LOTRMod.brick2, 1, 8), "enterDolGuldur").setBiomeAchievement();
		killDolGuldurOrc = new LOTRAchievement(MIRKWOOD, 18, LOTRMod.orcBone, "killDolGuldurOrc").setRequiresEnemy(LOTRFaction.DOL_GULDUR);
		useDolGuldurTable = new LOTRAchievement(MIRKWOOD, 19, LOTRMod.dolGuldurTable, "useDolGuldurTable").setRequiresAlly(LOTRFaction.DOL_GULDUR);
		tradeDolGuldurCaptain = new LOTRAchievement(MIRKWOOD, 20, LOTRMod.silverCoin, "tradeDolGuldurCaptain").setRequiresAlly(LOTRFaction.DOL_GULDUR);
		killMirkTroll = new LOTRAchievement(MIRKWOOD, 21, LOTRMod.trollBone, "killMirkTroll").setRequiresEnemy(LOTRFaction.DOL_GULDUR);

		killDwarf = new LOTRAchievement(IRON_HILLS, 0, LOTRMod.dwarfBone, "killDwarf").setRequiresEnemy(LOTRFaction.DWARF);
		wearFullDwarven = new LOTRAchievement(IRON_HILLS, 1, LOTRMod.bodyDwarven, "wearFullDwarven");
		useDwarvenThrowingAxe = new LOTRAchievement(IRON_HILLS, 2, LOTRMod.throwingAxeDwarven, "useDwarvenThrowingAxe");
		useDwarvenTable = new LOTRAchievement(IRON_HILLS, 3, LOTRMod.dwarvenTable, "useDwarvenTable").setRequiresAlly(LOTRFaction.DWARF);
		tradeDwarfMiner = new LOTRAchievement(IRON_HILLS, 4, LOTRMod.silverCoin, "tradeDwarfMiner").setRequiresAlly(LOTRFaction.DWARF);
		tradeDwarfCommander = new LOTRAchievement(IRON_HILLS, 5, LOTRMod.silverCoin, "tradeDwarfCommander").setRequiresAlly(LOTRFaction.DWARF);
		mineGlowstone = new LOTRAchievement(IRON_HILLS, 6, LOTRMod.oreGlowstone, "mineGlowstone");
		smeltDwarfSteel = new LOTRAchievement(IRON_HILLS, 7, LOTRMod.dwarfSteel, "smeltDwarfSteel");
		drinkDwarvenTonic = new LOTRAchievement(IRON_HILLS, 8, LOTRMod.mugDwarvenTonic, "drinkDwarvenTonic");
		craftMithrilDwarvenBrick = new LOTRAchievement(IRON_HILLS, 9, new ItemStack(LOTRMod.brick, 1, 10), "craftMithrilDwarvenBrick");
		talkDwarfWoman = new LOTRAchievement(IRON_HILLS, 10, LOTRMod.mugDwarvenAle, "talkDwarfWoman");
		enterIronHills = new LOTRAchievement(IRON_HILLS, 11, LOTRMod.pickaxeDwarven, "enterIronHills").setBiomeAchievement();
		useDwarvenDoor = new LOTRAchievement(IRON_HILLS, 12, LOTRMod.dwarvenDoorItem, "useDwarvenDoor");
		alignmentGood10_DWARF = new LOTRAchievement(IRON_HILLS, 13, LOTRMod.goldRing, "alignmentGood10_DWARF").setRequiresAlly(LOTRFaction.DWARF).setSpecial();
		alignmentGood100_DWARF = new LOTRAchievement(IRON_HILLS, 14, LOTRMod.goldRing, "alignmentGood100_DWARF").setRequiresAlly(LOTRFaction.DWARF).setSpecial();
		alignmentGood1000_DWARF = new LOTRAchievement(IRON_HILLS, 15, LOTRMod.goldRing, "alignmentGood1000_DWARF").setRequiresAlly(LOTRFaction.DWARF).setSpecial();
		marryDwarf = new LOTRAchievement(IRON_HILLS, 16, LOTRMod.dwarvenRing, "marryDwarf").setRequiresAlly(LOTRFaction.DWARF);

		killElf = new LOTRAchievement(LOTHLORIEN, 0, LOTRMod.elfBone, "killElf").setRequiresEnemy(LOTRFaction.GALADHRIM);
		useElvenPortal = new LOTRAchievement(LOTHLORIEN, 1, LOTRMod.quenditeGrass, "useElvenPortal").setRequiresAlly(LOTRFaction.GALADHRIM);
		wearFullElven = new LOTRAchievement(LOTHLORIEN, 2, LOTRMod.bodyElven, "wearFullElven");
		useElvenTable = new LOTRAchievement(LOTHLORIEN, 3, LOTRMod.elvenTable, "useElvenTable").setRequiresAlly(LOTRFaction.GALADHRIM);
		tradeElfLord = new LOTRAchievement(LOTHLORIEN, 4, LOTRMod.silverCoin, "tradeElfLord").setRequiresAlly(LOTRFaction.GALADHRIM);
		mineQuendite = new LOTRAchievement(LOTHLORIEN, 5, LOTRMod.oreQuendite, "mineQuendite");
		takeMallornWood = new LOTRAchievement(LOTHLORIEN, 6, new ItemStack(LOTRMod.wood, 1, 1), "takeMallornWood").setRequiresEnemy(LOTRFaction.GALADHRIM);
		wearFullGalvorn = new LOTRAchievement(LOTHLORIEN, 7, LOTRMod.bodyGalvorn, "wearFullGalvorn").setRequiresAlly(LOTRFaction.GALADHRIM);
		enterLothlorien = new LOTRAchievement(LOTHLORIEN, 8, new ItemStack(LOTRMod.sapling, 1, 1), "enterLothlorien").setBiomeAchievement();
		alignmentGood10_GALADHRIM = new LOTRAchievement(LOTHLORIEN, 9, LOTRMod.goldRing, "alignmentGood10_GALADHRIM").setRequiresAlly(LOTRFaction.GALADHRIM).setSpecial();
		alignmentGood100_GALADHRIM = new LOTRAchievement(LOTHLORIEN, 10, LOTRMod.goldRing, "alignmentGood100_GALADHRIM").setRequiresAlly(LOTRFaction.GALADHRIM).setSpecial();
		alignmentGood1000_GALADHRIM = new LOTRAchievement(LOTHLORIEN, 11, LOTRMod.goldRing, "alignmentGood1000_GALADHRIM").setRequiresAlly(LOTRFaction.GALADHRIM).setSpecial();
		tradeElvenTrader = new LOTRAchievement(LOTHLORIEN, 12, LOTRMod.silverCoin, "tradeElvenTrader").setRequiresAlly(LOTRFaction.GALADHRIM);

		raidUrukCamp = new LOTRAchievement(ROHAN, 0, Items.skull, "raidUrukCamp").setRequiresEnemy(LOTRFaction.URUK_HAI);
		useUrukTable = new LOTRAchievement(ROHAN, 1, LOTRMod.urukTable, "useUrukTable").setRequiresAlly(LOTRFaction.URUK_HAI);
		tradeUrukTrader = new LOTRAchievement(ROHAN, 2, LOTRMod.silverCoin, "tradeUrukTrader").setRequiresAlly(LOTRFaction.URUK_HAI);
		tradeUrukCaptain = new LOTRAchievement(ROHAN, 3, LOTRMod.silverCoin, "tradeUrukCaptain").setRequiresAlly(LOTRFaction.URUK_HAI);
		useRohirricTable = new LOTRAchievement(ROHAN, 4, LOTRMod.rohirricTable, "useRohirricTable").setRequiresAlly(LOTRFaction.ROHAN);
		smeltUrukSteel = new LOTRAchievement(ROHAN, 5, LOTRMod.urukSteel, "smeltUrukSteel");
		wearFullUruk = new LOTRAchievement(ROHAN, 6, LOTRMod.bodyUruk, "wearFullUruk");
		hireWargBombardier = new LOTRAchievement(ROHAN, 7, LOTRMod.wargFur, "hireWargBombardier").setRequiresAlly(LOTRFaction.URUK_HAI);
		killRohirrim = new LOTRAchievement(ROHAN, 8, LOTRMod.swordRohan, "killRohirrim").setRequiresEnemy(LOTRFaction.ROHAN);
		tradeRohirrimMarshal = new LOTRAchievement(ROHAN, 9, LOTRMod.silverCoin, "tradeRohirrimMarshal").setRequiresAlly(LOTRFaction.ROHAN);
		wearFullRohirric = new LOTRAchievement(ROHAN, 10, LOTRMod.bodyRohan, "wearFullRohirric");
		tradeRohanBlacksmith = new LOTRAchievement(ROHAN, 11, LOTRMod.silverCoin, "tradeRohanBlacksmith").setRequiresAlly(LOTRFaction.ROHAN);
		buyRohanMead = new LOTRAchievement(ROHAN, 12, LOTRMod.mugMead, "buyRohanMead").setRequiresAlly(LOTRFaction.ROHAN);
		enterRohan = new LOTRAchievement(ROHAN, 13, LOTRMod.spearRohan, "enterRohan").setBiomeAchievement();
		enterRohanUrukHighlands = new LOTRAchievement(ROHAN, 14, LOTRMod.helmetUruk, "enterRohanUrukHighlands").setBiomeAchievement();
		alignmentGood10_ROHAN = new LOTRAchievement(ROHAN, 15, LOTRMod.goldRing, "alignmentGood10_ROHAN").setRequiresAlly(LOTRFaction.ROHAN).setSpecial();
		alignmentGood100_ROHAN = new LOTRAchievement(ROHAN, 16, LOTRMod.goldRing, "alignmentGood100_ROHAN").setRequiresAlly(LOTRFaction.ROHAN).setSpecial();
		alignmentGood1000_ROHAN = new LOTRAchievement(ROHAN, 17, LOTRMod.goldRing, "alignmentGood1000_ROHAN").setRequiresAlly(LOTRFaction.ROHAN).setSpecial();
		alignmentGood10_URUK_HAI = new LOTRAchievement(ROHAN, 18, LOTRMod.goldRing, "alignmentGood10_URUK_HAI").setRequiresAlly(LOTRFaction.URUK_HAI).setSpecial();
		alignmentGood100_URUK_HAI = new LOTRAchievement(ROHAN, 19, LOTRMod.goldRing, "alignmentGood100_URUK_HAI").setRequiresAlly(LOTRFaction.URUK_HAI).setSpecial();
		alignmentGood1000_URUK_HAI = new LOTRAchievement(ROHAN, 20, LOTRMod.goldRing, "alignmentGood1000_URUK_HAI").setRequiresAlly(LOTRFaction.URUK_HAI).setSpecial();

		killDunlending = new LOTRAchievement(DUNLAND, 0, LOTRMod.dunlendingClub, "killDunlending").setRequiresEnemy(LOTRFaction.DUNLAND);
		wearFullDunlending = new LOTRAchievement(DUNLAND, 1, LOTRMod.bodyDunlending, "wearFullDunlending");
		useDunlendingTable = new LOTRAchievement(DUNLAND, 2, LOTRMod.dunlendingTable, "useDunlendingTable").setRequiresAlly(LOTRFaction.DUNLAND);
		tradeDunlendingWarlord = new LOTRAchievement(DUNLAND, 3, LOTRMod.silverCoin, "tradeDunlendingWarlord").setRequiresAlly(LOTRFaction.DUNLAND);
		useDunlendingTrident = new LOTRAchievement(DUNLAND, 4, LOTRMod.dunlendingTrident, "useDunlendingTrident");
		tradeDunlendingBartender = new LOTRAchievement(DUNLAND, 5, LOTRMod.silverCoin, "tradeDunlendingBartender").setRequiresAlly(LOTRFaction.DUNLAND);
		enterDunland = new LOTRAchievement(DUNLAND, 6, Items.stone_sword, "enterDunland").setBiomeAchievement();
		alignmentGood10_DUNLAND = new LOTRAchievement(DUNLAND, 7, LOTRMod.goldRing, "alignmentGood10_DUNLAND").setRequiresAlly(LOTRFaction.DUNLAND).setSpecial();
		alignmentGood100_DUNLAND = new LOTRAchievement(DUNLAND, 8, LOTRMod.goldRing, "alignmentGood100_DUNLAND").setRequiresAlly(LOTRFaction.DUNLAND).setSpecial();
		alignmentGood1000_DUNLAND = new LOTRAchievement(DUNLAND, 9, LOTRMod.goldRing, "alignmentGood1000_DUNLAND").setRequiresAlly(LOTRFaction.DUNLAND).setSpecial();

		killEnt = new LOTRAchievement(FANGORN, 0, Blocks.log, "killEnt").setRequiresEnemy(LOTRFaction.FANGORN);
		drinkEntDraught = new LOTRAchievement(FANGORN, 1, LOTRMod.entDraught, "drinkEntDraught");
		killHuorn = new LOTRAchievement(FANGORN, 2, Blocks.log, "killHuorn").setRequiresEnemy(LOTRFaction.FANGORN);
		talkEnt = new LOTRAchievement(FANGORN, 3, Blocks.log, "talkEnt");
		enterFangorn = new LOTRAchievement(FANGORN, 4, Blocks.leaves, "enterFangorn").setBiomeAchievement();
		alignmentGood10_FANGORN = new LOTRAchievement(FANGORN, 5, LOTRMod.goldRing, "alignmentGood10_FANGORN").setRequiresAlly(LOTRFaction.FANGORN).setSpecial();
		alignmentGood100_FANGORN = new LOTRAchievement(FANGORN, 6, LOTRMod.goldRing, "alignmentGood100_FANGORN").setRequiresAlly(LOTRFaction.FANGORN).setSpecial();
		alignmentGood1000_FANGORN = new LOTRAchievement(FANGORN, 7, LOTRMod.goldRing, "alignmentGood1000_FANGORN").setRequiresAlly(LOTRFaction.FANGORN).setSpecial();
		summonHuorn = new LOTRAchievement(FANGORN, 8, new ItemStack(LOTRMod.entDraught, 1, 2), "summonHuorn").setRequiresAlly(LOTRFaction.FANGORN);

		killGondorSoldier = new LOTRAchievement(GONDOR, 0, LOTRMod.swordGondor, "killGondorSoldier").setRequiresEnemy(LOTRFaction.GONDOR);
		lightGondorBeacon = new LOTRAchievement(GONDOR, 1, LOTRMod.beacon, "lightGondorBeacon");
		useGondorianTable = new LOTRAchievement(GONDOR, 2, LOTRMod.gondorianTable, "useGondorianTable").setRequiresAlly(LOTRFaction.GONDOR);
		tradeGondorBlacksmith = new LOTRAchievement(GONDOR, 3, LOTRMod.silverCoin, "tradeGondorBlacksmith").setRequiresAlly(LOTRFaction.GONDOR);
		tradeGondorianCaptain = new LOTRAchievement(GONDOR, 4, LOTRMod.silverCoin, "tradeGondorianCaptain").setRequiresAlly(LOTRFaction.GONDOR);
		wearFullGondorian = new LOTRAchievement(GONDOR, 5, LOTRMod.bodyGondor, "wearFullGondorian");
		killRangerIthilien = new LOTRAchievement(GONDOR, 6, Items.bow, "killRangerIthilien").setRequiresEnemy(LOTRFaction.GONDOR);
		enterGondor = new LOTRAchievement(GONDOR, 7, LOTRMod.spearGondor, "enterGondor").setBiomeAchievement();
		enterIthilien = new LOTRAchievement(GONDOR, 8, Items.bow, "enterIthilien").setBiomeAchievement();
		enterWhiteMountains = new LOTRAchievement(GONDOR, 9, new ItemStack(LOTRMod.rock, 1, 1), "enterWhiteMountains").setBiomeAchievement();
		alignmentGood10_GONDOR = new LOTRAchievement(GONDOR, 10, LOTRMod.goldRing, "alignmentGood10_GONDOR").setRequiresAlly(LOTRFaction.GONDOR).setSpecial();
		alignmentGood100_GONDOR = new LOTRAchievement(GONDOR, 11, LOTRMod.goldRing, "alignmentGood100_GONDOR").setRequiresAlly(LOTRFaction.GONDOR).setSpecial();
		alignmentGood1000_GONDOR = new LOTRAchievement(GONDOR, 12, LOTRMod.goldRing, "alignmentGood1000_GONDOR").setRequiresAlly(LOTRFaction.GONDOR).setSpecial();
		enterTolfalas = new LOTRAchievement(GONDOR, 13, Blocks.stone, "enterTolfalas").setBiomeAchievement();
		enterLebennin = new LOTRAchievement(GONDOR, 14, Blocks.grass, "enterLebennin").setBiomeAchievement();

		mineRemains = new LOTRAchievement(NINDALF, 0, LOTRMod.remains, "mineRemains");
		craftAncientItem = new LOTRAchievement(NINDALF, 1, LOTRMod.ancientItem, "craftAncientItem");
		enterDeadMarshes = new LOTRAchievement(NINDALF, 2, LOTRMod.deadPlant, "enterDeadMarshes").setBiomeAchievement();
		enterNindalf = new LOTRAchievement(NINDALF, 3, new ItemStack(Blocks.tallgrass, 1, 2), "enterNindalf").setBiomeAchievement();

		killOlogHai = new LOTRAchievement(MORDOR, 0, LOTRMod.trollBone, "killOlogHai").setRequiresEnemy(LOTRFaction.MORDOR);
		useMorgulTable = new LOTRAchievement(MORDOR, 1, LOTRMod.morgulTable, "useMorgulTable").setRequiresAlly(LOTRFaction.MORDOR);
		smeltOrcSteel = new LOTRAchievement(MORDOR, 2, LOTRMod.orcSteel, "smeltOrcSteel");
		wearFullOrc = new LOTRAchievement(MORDOR, 3, LOTRMod.bodyOrc, "wearFullOrc");
		tradeOrcTrader = new LOTRAchievement(MORDOR, 4, LOTRMod.silverCoin, "tradeOrcTrader").setRequiresAlly(LOTRFaction.MORDOR);
		tradeOrcCaptain = new LOTRAchievement(MORDOR, 5, LOTRMod.silverCoin, "tradeOrcCaptain").setRequiresAlly(LOTRFaction.MORDOR);
		mineNaurite = new LOTRAchievement(MORDOR, 6, LOTRMod.oreNaurite, "mineNaurite");
		eatMorgulShroom = new LOTRAchievement(MORDOR, 7, LOTRMod.morgulShroom, "eatMorgulShroom");
		craftOrcBomb = new LOTRAchievement(MORDOR, 8, LOTRMod.orcBomb, "craftOrcBomb").setRequiresAlly(LOTRFaction.MORDOR);
		hireOlogHai = new LOTRAchievement(MORDOR, 9, LOTRMod.hammerOrc, "hireOlogHai").setRequiresAlly(LOTRFaction.MORDOR);
		mineGulduril = new LOTRAchievement(MORDOR, 10, new ItemStack(LOTRMod.oreGulduril, 1, 1), "mineGulduril");
		useMorgulPortal = new LOTRAchievement(MORDOR, 11, LOTRMod.guldurilCrystal, "useMorgulPortal").setRequiresAlly(LOTRFaction.MORDOR);
		wearFullMorgul = new LOTRAchievement(MORDOR, 12, LOTRMod.bodyMorgul, "wearFullMorgul").setRequiresAlly(LOTRFaction.MORDOR);
		enterMordor = new LOTRAchievement(MORDOR, 13, new ItemStack(LOTRMod.rock, 1, 0), "enterMordor").setBiomeAchievement();
		enterNurn = new LOTRAchievement(MORDOR, 14, LOTRMod.hoeOrc, "enterNurn").setBiomeAchievement();
		enterNanUngol = new LOTRAchievement(MORDOR, 15, LOTRMod.webUngoliant, "enterNanUngol").setBiomeAchievement();
		killMordorSpider = new LOTRAchievement(MORDOR, 16, Items.string, "killMordorSpider").setRequiresEnemy(LOTRFaction.MORDOR);
		tradeOrcSpiderKeeper = new LOTRAchievement(MORDOR, 17, LOTRMod.silverCoin, "tradeOrcSpiderKeeper").setRequiresAlly(LOTRFaction.MORDOR);
		killMordorOrc = new LOTRAchievement(MORDOR, 18, LOTRMod.orcBone, "killMordorOrc").setRequiresEnemy(LOTRFaction.MORDOR);
		alignmentGood10_MORDOR = new LOTRAchievement(MORDOR, 19, LOTRMod.goldRing, "alignmentGood10_MORDOR").setRequiresAlly(LOTRFaction.MORDOR).setSpecial();
		alignmentGood100_MORDOR = new LOTRAchievement(MORDOR, 20, LOTRMod.goldRing, "alignmentGood100_MORDOR").setRequiresAlly(LOTRFaction.MORDOR).setSpecial();
		alignmentGood1000_MORDOR = new LOTRAchievement(MORDOR, 21, LOTRMod.goldRing, "alignmentGood1000_MORDOR").setRequiresAlly(LOTRFaction.MORDOR).setSpecial();

		enterHarondor = new LOTRAchievement(NEAR_HARAD, 0, Blocks.dirt, "enterHarondor").setBiomeAchievement();
		enterNearHarad = new LOTRAchievement(NEAR_HARAD, 1, Blocks.sand, "enterNearHarad").setBiomeAchievement();
		killNearHaradrim = new LOTRAchievement(NEAR_HARAD, 2, Items.bone, "killNearHaradrim").setRequiresEnemy(LOTRFaction.NEAR_HARAD);
		alignmentGood10_NEAR_HARAD = new LOTRAchievement(NEAR_HARAD, 3, LOTRMod.goldRing, "alignmentGood10_NEAR_HARAD").setRequiresAlly(LOTRFaction.NEAR_HARAD).setSpecial();
		alignmentGood100_NEAR_HARAD = new LOTRAchievement(NEAR_HARAD, 4, LOTRMod.goldRing, "alignmentGood100_NEAR_HARAD").setRequiresAlly(LOTRFaction.NEAR_HARAD).setSpecial();
		alignmentGood1000_NEAR_HARAD = new LOTRAchievement(NEAR_HARAD, 5, LOTRMod.goldRing, "alignmentGood1000_NEAR_HARAD").setRequiresAlly(LOTRFaction.NEAR_HARAD).setSpecial();
		useNearHaradTable = new LOTRAchievement(NEAR_HARAD, 6, LOTRMod.nearHaradTable, "useNearHaradTable").setRequiresAlly(LOTRFaction.NEAR_HARAD);
		wearFullNearHarad = new LOTRAchievement(NEAR_HARAD, 7, LOTRMod.bodyNearHarad, "wearFullNearHarad").setRequiresAlly(LOTRFaction.NEAR_HARAD);
		tradeNearHaradWarlord = new LOTRAchievement(NEAR_HARAD, 8, LOTRMod.silverCoin, "tradeNearHaradWarlord").setRequiresAlly(LOTRFaction.NEAR_HARAD);
		rideCamel = new LOTRAchievement(NEAR_HARAD, 9, Items.saddle, "rideCamel");
		tradeBazaarTrader = new LOTRAchievement(NEAR_HARAD, 10, LOTRMod.silverCoin, "tradeBazaarTrader").setRequiresAlly(LOTRFaction.NEAR_HARAD);
		tradeNearHaradMerchant = new LOTRAchievement(NEAR_HARAD, 11, LOTRMod.silverCoin, "tradeNearHaradMerchant").setRequiresAlly(LOTRFaction.NEAR_HARAD);

		enterUmbar = new LOTRAchievement(UMBAR, 0, Blocks.grass, "enterUmbar").setBiomeAchievement();

		enterFarHarad = new LOTRAchievement(FAR_HARAD, 0, Blocks.grass, "enterFarHarad").setBiomeAchievement();
		drinkMangoJuice = new LOTRAchievement(FAR_HARAD, 1, LOTRMod.mugMangoJuice, "drinkMangoJuice");
		pickBanana = new LOTRAchievement(FAR_HARAD, 2, LOTRMod.banana, "pickBanana");
		alignmentGood10_FAR_HARAD = new LOTRAchievement(FAR_HARAD, 3, LOTRMod.goldRing, "alignmentGood10_FAR_HARAD").setRequiresAlly(LOTRFaction.FAR_HARAD).setSpecial();
		alignmentGood100_FAR_HARAD = new LOTRAchievement(FAR_HARAD, 4, LOTRMod.goldRing, "alignmentGood100_FAR_HARAD").setRequiresAlly(LOTRFaction.FAR_HARAD).setSpecial();
		alignmentGood1000_FAR_HARAD = new LOTRAchievement(FAR_HARAD, 5, LOTRMod.goldRing, "alignmentGood1000_FAR_HARAD").setRequiresAlly(LOTRFaction.FAR_HARAD).setSpecial();

		enterPertorogwaith = new LOTRAchievement(PERTOROGWAITH, 0, Blocks.stone, "enterPertorogwaith").setBiomeAchievement();
		alignmentGood10_HALF_TROLL = new LOTRAchievement(PERTOROGWAITH, 1, LOTRMod.goldRing, "alignmentGood10_HALF_TROLL").setRequiresAlly(LOTRFaction.HALF_TROLL).setSpecial();
		alignmentGood100_HALF_TROLL = new LOTRAchievement(PERTOROGWAITH, 2, LOTRMod.goldRing, "alignmentGood100_HALF_TROLL").setRequiresAlly(LOTRFaction.HALF_TROLL).setSpecial();
		alignmentGood1000_HALF_TROLL = new LOTRAchievement(PERTOROGWAITH, 3, LOTRMod.goldRing, "alignmentGood1000_HALF_TROLL").setRequiresAlly(LOTRFaction.HALF_TROLL).setSpecial();
		
		enterRhun = new LOTRAchievement(RHUN, 0, Blocks.grass, "enterRhun").setBiomeAchievement();
		
		enterRedMountains = new LOTRAchievement(OROCARNI, 0, new ItemStack(LOTRMod.rock, 1, 4), "enterRedMountains").setBiomeAchievement();
	}
	
	public static Category categoryForName(String name)
	{
		for (Category category : Category.values())
		{
			if (category.name().equals(name))
			{
				return category;
			}
		}
		return null;
	}
	
	public static LOTRAchievement achievementForCategoryAndID(Category category, int ID)
	{
		if (category == null)
		{
			return null;
		}
		
		Iterator i = category.list.iterator();
		while (i.hasNext())
		{
			LOTRAchievement achievement = (LOTRAchievement)i.next();
			if (achievement.ID == ID)
			{
				return achievement;
			}
		}
		return null;
	}
	
	private static Class[][] actionParams = {{HoverEvent.Action.class, String.class, boolean.class}};
	public static HoverEvent.Action SHOW_LOTR_ACHIEVEMENT = (HoverEvent.Action)EnumHelper.addEnum(actionParams, HoverEvent.Action.class, "SHOW_LOTR_ACHIEVEMENT", "show_lotr_achievement", true);
	static
	{
		LOTRReflection.getHoverEventMappings().put(SHOW_LOTR_ACHIEVEMENT.getCanonicalName(), SHOW_LOTR_ACHIEVEMENT);
	}
	
	public IChatComponent getAchievementChatComponent()
    {
        IChatComponent component = new ChatComponentTranslation(getTitle()).createCopy();
        component.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        component.getChatStyle().setChatHoverEvent(new HoverEvent(SHOW_LOTR_ACHIEVEMENT, new ChatComponentText(category.name() + "$" + ID)));
        return component;
    }

    public IChatComponent getChatComponentForEarn()
    {
        IChatComponent base = getAchievementChatComponent();
        IChatComponent component = (new ChatComponentText("[")).appendSibling(base).appendText("]");
        component.setChatStyle(base.getChatStyle());
        return component;
    }
}
