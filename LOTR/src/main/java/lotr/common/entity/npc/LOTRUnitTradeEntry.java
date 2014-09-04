package lotr.common.entity.npc;

import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.animal.*;
import lotr.common.entity.npc.LOTRHiredNPCInfo.Task;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;

public class LOTRUnitTradeEntry
{
	public static LOTRUnitTradeEntry[] MORDOR_ORC_MERCENARY_CAPTAIN;
	
	public static LOTRUnitTradeEntry[] GONDORIAN_CAPTAIN;
	
	public static LOTRUnitTradeEntry[] DWARF_COMMANDER;
	
	public static LOTRUnitTradeEntry[] URUK_HAI_MERCENARY_CAPTAIN;
	
	public static LOTRUnitTradeEntry[] ELF_LORD;
	
	public static LOTRUnitTradeEntry[] ROHIRRIM_MARSHAL;
	
	public static LOTRUnitTradeEntry[] HOBBIT_SHIRRIFF_CHIEF;
	
	public static LOTRUnitTradeEntry[] DUNLENDING_WARLORD;
	
	public static LOTRUnitTradeEntry[] WOOD_ELF_CAPTAIN;
	
	public static LOTRUnitTradeEntry[] ANGMAR_ORC_MERCENARY_CAPTAIN;
	
	public static LOTRUnitTradeEntry[] MORDOR_ORC_SLAVER;
	
	public static LOTRUnitTradeEntry[] MORDOR_ORC_SPIDER_KEEPER;
	
	public static LOTRUnitTradeEntry[] GUNDABAD_ORC_MERCENARY_CAPTAIN;
	
	public static LOTRUnitTradeEntry[] RANGER_NORTH_CAPTAIN;
	
	public static LOTRUnitTradeEntry[] HOBBIT_FARMER;
	
	public static LOTRUnitTradeEntry[] HIGH_ELF_LORD;
	
	public static LOTRUnitTradeEntry[] NEAR_HARADRIM_WARLORD;
	
	public static LOTRUnitTradeEntry[] BLUE_DWARF_COMMANDER;
	
	public static LOTRUnitTradeEntry[] DOL_GULDUR_CAPTAIN;
	
	public Class entityClass;
	public Class mountClass;
	private Item mountArmor;
	private float mountArmorChance;
	private String name;
	private int initialCost;
	public int alignmentRequired;
	public Task task = Task.WARRIOR;
	
	public LOTRUnitTradeEntry(Class c, int cost, int alignment)
	{
		entityClass = c;
		initialCost = cost;
		alignmentRequired = alignment;
	}
	
	public LOTRUnitTradeEntry(Class c, Class c1, String s, int cost, int alignment)
	{
		this(c, cost, alignment);
		mountClass = c1;
		name = s;
	}
	
	public LOTRUnitTradeEntry setTask(Task t)
	{
		task = t;
		return this;
	}
	
	public LOTRUnitTradeEntry setMountArmor(Item item)
	{
		return setMountArmor(item, 1F);
	}
	
	public LOTRUnitTradeEntry setMountArmor(Item item, float chance)
	{
		mountArmor = item;
		mountArmorChance = chance;
		return this;
	}
	
	public int getCost(EntityPlayer entityplayer, LOTRUnitTradeable trader)
	{
		int cost = initialCost;
		int costThreshold = initialCost / 2;
		int alignment = LOTRLevelData.getData(entityplayer).getAlignment(((LOTREntityNPC)trader).getFaction());
		int d = alignment - alignmentRequired;
		d = Math.max(0, d);
		float f = (float)d / 3000F;
		int i = (int)((float)cost * f);
		cost -= i;
		cost = Math.max(cost, costThreshold);
		return cost;
	}
	
	public boolean hasRequiredCostAndAlignment(EntityPlayer entityplayer, LOTRUnitTradeable trader)
	{
		int coins = 0;
		for (ItemStack itemstack : entityplayer.inventory.mainInventory)
		{
			if (itemstack != null && itemstack.getItem() == LOTRMod.silverCoin)
			{
				coins += itemstack.stackSize;
			}
		}
		if (coins < getCost(entityplayer, trader))
		{
			return false;
		}
				
		int alignment = LOTRLevelData.getData(entityplayer).getAlignment(((LOTREntityNPC)trader).getFaction());
		if (alignmentRequired < 0)
		{
			return alignment <= alignmentRequired;
		}
		else
		{
			return alignment >= alignmentRequired;
		}
	}
	
	public String getUnitTradeName()
	{
		if (mountClass == null)
		{
			String s = String.format("%s.%s", LOTRMod.getModID(), LOTREntities.getStringFromClass(entityClass));
			return StatCollector.translateToLocal("entity." + s + ".name");
		}
		else
		{
			return StatCollector.translateToLocal("lotr.unit." + name);
		}
	}
	
	public LOTREntityNPC createHiredNPC(World world)
	{
		LOTREntityNPC entity = (LOTREntityNPC)LOTREntities.createEntityByClass(entityClass, world);
		entity.initCreatureForHire(null);
		return entity;
	}
	
	public EntityLiving createHiredMount(World world)
	{
		if (mountClass == null)
		{
			return null;
		}
		
		EntityLiving entity = (EntityLiving)LOTREntities.createEntityByClass(mountClass, world);
		if (entity instanceof LOTREntityNPC)
		{
			((LOTREntityNPC)entity).initCreatureForHire(null);
		}
		else
		{
			entity.onSpawnWithEgg(null);
		}
		
		if (mountArmor != null && world.rand.nextFloat() < mountArmorChance)
		{
			if (entity instanceof LOTREntityHorse)
			{
				((LOTREntityHorse)entity).setMountArmor(new ItemStack(mountArmor));
			}
			else if (entity instanceof LOTREntityWarg)
			{
				((LOTREntityWarg)entity).setWargArmor(new ItemStack(mountArmor));
			}
		}
		
		return entity;
	}
	
	static
	{
		MORDOR_ORC_MERCENARY_CAPTAIN = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityMordorOrc.class, 10, 150),
			new LOTRUnitTradeEntry(LOTREntityMordorOrcArcher.class, 20, 200),
			new LOTRUnitTradeEntry(LOTREntityMordorOrcBombardier.class, 25, 250),
			new LOTRUnitTradeEntry(LOTREntityMordorWarg.class, 10, 150),
			new LOTRUnitTradeEntry(LOTREntityMordorOrc.class, LOTREntityMordorWarg.class, "MordorOrc_Warg", 20, 250).setMountArmor(LOTRMod.wargArmorMordor, 0.5F),
			new LOTRUnitTradeEntry(LOTREntityMordorOrcArcher.class, LOTREntityMordorWarg.class, "MordorOrcArcher_Warg", 30, 300).setMountArmor(LOTRMod.wargArmorMordor, 0.5F),
			new LOTRUnitTradeEntry(LOTREntityMordorWargBombardier.class, 25, 400),
			new LOTRUnitTradeEntry(LOTREntityOlogHai.class, 60, 500),
			new LOTRUnitTradeEntry(LOTREntityMordorBannerBearer.class, 40, 300)
		};
		
		GONDORIAN_CAPTAIN = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityGondorSoldier.class, 15, 100),
			new LOTRUnitTradeEntry(LOTREntityGondorArcher.class, 25, 150),
			new LOTRUnitTradeEntry(LOTREntityGondorSoldier.class, LOTREntityHorse.class, "GondorSoldier_Horse", 25, 200).setMountArmor(LOTRMod.horseArmorGondor),
			new LOTRUnitTradeEntry(LOTREntityGondorArcher.class, LOTREntityHorse.class, "GondorArcher_Horse", 35, 250).setMountArmor(LOTRMod.horseArmorGondor),
			new LOTRUnitTradeEntry(LOTREntityGondorTowerGuard.class, 25, 300),
			new LOTRUnitTradeEntry(LOTREntityGondorBannerBearer.class, 40, 250)
		};
		
		DWARF_COMMANDER = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityDwarf.class, 10, 200),
			new LOTRUnitTradeEntry(LOTREntityDwarfWarrior.class, 15, 250),
			new LOTRUnitTradeEntry(LOTREntityDwarfAxeThrower.class, 25, 300),
			new LOTRUnitTradeEntry(LOTREntityDwarfWarrior.class, LOTREntityWildBoar.class, "DwarfWarrior_Boar", 25, 350),
			new LOTRUnitTradeEntry(LOTREntityDwarfAxeThrower.class, LOTREntityWildBoar.class, "DwarfAxeThrower_Boar", 35, 400),
			new LOTRUnitTradeEntry(LOTREntityDwarfBannerBearer.class, 40, 400)
		};
		
		URUK_HAI_MERCENARY_CAPTAIN = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityUrukHai.class, 15, 150),
			new LOTRUnitTradeEntry(LOTREntityUrukHaiCrossbower.class, 25, 200),
			new LOTRUnitTradeEntry(LOTREntityUrukHaiBerserker.class, 30, 250),
			new LOTRUnitTradeEntry(LOTREntityUrukWarg.class, 10, 150),
			new LOTRUnitTradeEntry(LOTREntityUrukHai.class, LOTREntityUrukWarg.class, "UrukHai_Warg", 25, 250).setMountArmor(LOTRMod.wargArmorUruk, 0.5F),
			new LOTRUnitTradeEntry(LOTREntityUrukHaiCrossbower.class, LOTREntityUrukWarg.class, "UrukHaiCrossbower_Warg", 35, 300).setMountArmor(LOTRMod.wargArmorUruk, 0.5F),
			new LOTRUnitTradeEntry(LOTREntityUrukWargBombardier.class, 25, 400),
			new LOTRUnitTradeEntry(LOTREntityUrukHaiBannerBearer.class, 40, 300)
		};

		ELF_LORD = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityGaladhrimElf.class, 15, 200),
			new LOTRUnitTradeEntry(LOTREntityGaladhrimWarrior.class, 30, 300),
			new LOTRUnitTradeEntry(LOTREntityGaladhrimWarrior.class, LOTREntityHorse.class, "GaladhrimWarrior_Horse", 35, 400).setMountArmor(LOTRMod.horseArmorGaladhrim),
			new LOTRUnitTradeEntry(LOTREntityGaladhrimBannerBearer.class, 40, 450)
		};
		
		ROHIRRIM_MARSHAL = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityRohirrim.class, 15, 100),
			new LOTRUnitTradeEntry(LOTREntityRohirrimArcher.class, 25, 150),
			new LOTRUnitTradeEntry(LOTREntityRohirrim.class, LOTREntityHorse.class, "Rohirrim_Horse", 25, 200).setMountArmor(LOTRMod.horseArmorRohan),
			new LOTRUnitTradeEntry(LOTREntityRohirrimArcher.class, LOTREntityHorse.class, "RohirrimArcher_Horse", 35, 250).setMountArmor(LOTRMod.horseArmorRohan),
			new LOTRUnitTradeEntry(LOTREntityRohanBannerBearer.class, 40, 250)
		};
		
		HOBBIT_SHIRRIFF_CHIEF = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityHobbitShirriff.class, 10, 50),
			new LOTRUnitTradeEntry(LOTREntityHobbitShirriff.class, LOTREntityShirePony.class, "HobbitShirriff_Pony", 20, 100)
		};
		
		DUNLENDING_WARLORD = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityDunlending.class, 8, 100),
			new LOTRUnitTradeEntry(LOTREntityDunlendingWarrior.class, 15, 150),
			new LOTRUnitTradeEntry(LOTREntityDunlendingArcher.class, 25, 200),
			new LOTRUnitTradeEntry(LOTREntityDunlendingBannerBearer.class, 40, 300)
		};
		
		WOOD_ELF_CAPTAIN = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityWoodElf.class, 15, 200),
			new LOTRUnitTradeEntry(LOTREntityWoodElfScout.class, 25, 250),
			new LOTRUnitTradeEntry(LOTREntityWoodElfWarrior.class, 30, 300),
			new LOTRUnitTradeEntry(LOTREntityWoodElfWarrior.class, LOTREntityElk.class, "WoodElfWarrior_Elk", 35, 400).setMountArmor(LOTRMod.elkArmorWoodElven),
			new LOTRUnitTradeEntry(LOTREntityWoodElfBannerBearer.class, 40, 450)
		};
		
		ANGMAR_ORC_MERCENARY_CAPTAIN = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityAngmarOrc.class, 10, 150),
			new LOTRUnitTradeEntry(LOTREntityAngmarOrcArcher.class, 20, 200),
			new LOTRUnitTradeEntry(LOTREntityAngmarOrcBombardier.class, 25, 250),
			new LOTRUnitTradeEntry(LOTREntityAngmarOrcWarrior.class, 20, 300),
			new LOTRUnitTradeEntry(LOTREntityAngmarWarg.class, 10, 150),
			new LOTRUnitTradeEntry(LOTREntityAngmarOrc.class, LOTREntityAngmarWarg.class, "AngmarOrc_Warg", 20, 250).setMountArmor(LOTRMod.wargArmorAngmar, 0.5F),
			new LOTRUnitTradeEntry(LOTREntityAngmarOrcArcher.class, LOTREntityAngmarWarg.class, "AngmarOrcArcher_Warg", 30, 300).setMountArmor(LOTRMod.wargArmorAngmar, 0.5F),
			new LOTRUnitTradeEntry(LOTREntityAngmarOrcWarrior.class, LOTREntityAngmarWarg.class, "AngmarOrcWarrior_Warg", 30, 350).setMountArmor(LOTRMod.wargArmorAngmar),
			new LOTRUnitTradeEntry(LOTREntityAngmarWargBombardier.class, 25, 400),
			new LOTRUnitTradeEntry(LOTREntityTroll.class, 50, 400),
			new LOTRUnitTradeEntry(LOTREntityMountainTroll.class, 60, 500),
			new LOTRUnitTradeEntry(LOTREntityAngmarBannerBearer.class, 40, 300)
		};
		
		MORDOR_ORC_SLAVER = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityNurnSlave.class, 40, 1).setTask(Task.FARMER)
		};
		
		MORDOR_ORC_SPIDER_KEEPER = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityMordorSpider.class, 15, 250),
			new LOTRUnitTradeEntry(LOTREntityMordorOrc.class, LOTREntityMordorSpider.class, "MordorOrc_Spider", 25, 300),
			new LOTRUnitTradeEntry(LOTREntityMordorOrcArcher.class, LOTREntityMordorSpider.class, "MordorOrcArcher_Spider", 35, 350)
		};
		
		GUNDABAD_ORC_MERCENARY_CAPTAIN = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityGundabadOrc.class, 10, 100),
			new LOTRUnitTradeEntry(LOTREntityGundabadOrcArcher.class, 20, 150),
			new LOTRUnitTradeEntry(LOTREntityGundabadWarg.class, 10, 150),
			new LOTRUnitTradeEntry(LOTREntityGundabadOrc.class, LOTREntityGundabadWarg.class, "GundabadOrc_Warg", 20, 200),
			new LOTRUnitTradeEntry(LOTREntityGundabadOrcArcher.class, LOTREntityGundabadWarg.class, "GundabadOrcArcher_Warg", 30, 250),
			new LOTRUnitTradeEntry(LOTREntityGundabadBannerBearer.class, 40, 250)
		};
		
		RANGER_NORTH_CAPTAIN = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityRangerNorth.class, 30, 300),
			new LOTRUnitTradeEntry(LOTREntityRangerNorthBannerBearer.class, 40, 450)
		};
		
		HOBBIT_FARMER = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityHobbitFarmhand.class, 40, 50).setTask(Task.FARMER)
		};
		
		HIGH_ELF_LORD = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityHighElf.class, 15, 250),
			new LOTRUnitTradeEntry(LOTREntityHighElfWarrior.class, 30, 350),
			new LOTRUnitTradeEntry(LOTREntityHighElfWarrior.class, LOTREntityHorse.class, "HighElfWarrior_Horse", 35, 450).setMountArmor(LOTRMod.horseArmorHighElven),
			new LOTRUnitTradeEntry(LOTREntityHighElfBannerBearer.class, 40, 500)
		};
		
		NEAR_HARADRIM_WARLORD = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityNearHaradrimWarrior.class, 15, 100),
			new LOTRUnitTradeEntry(LOTREntityNearHaradrimArcher.class, 25, 150),
			new LOTRUnitTradeEntry(LOTREntityNearHaradrimWarrior.class, LOTREntityCamel.class, "NearHaradrimWarrior_Camel", 25, 200),
			new LOTRUnitTradeEntry(LOTREntityNearHaradrimArcher.class, LOTREntityCamel.class, "NearHaradrimArcher_Camel", 35, 250),
			new LOTRUnitTradeEntry(LOTREntityNearHaradBannerBearer.class, 40, 250)
		};
		
		BLUE_DWARF_COMMANDER = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityBlueDwarf.class, 10, 200),
			new LOTRUnitTradeEntry(LOTREntityBlueDwarfWarrior.class, 15, 250),
			new LOTRUnitTradeEntry(LOTREntityBlueDwarfAxeThrower.class, 25, 300),
			new LOTRUnitTradeEntry(LOTREntityBlueDwarfWarrior.class, LOTREntityWildBoar.class, "BlueDwarfWarrior_Boar", 25, 350),
			new LOTRUnitTradeEntry(LOTREntityBlueDwarfAxeThrower.class, LOTREntityWildBoar.class, "BlueDwarfAxeThrower_Boar", 35, 400),
			new LOTRUnitTradeEntry(LOTREntityBlueDwarfBannerBearer.class, 40, 400)
		};
		
		DOL_GULDUR_CAPTAIN = new LOTRUnitTradeEntry[]
		{
			new LOTRUnitTradeEntry(LOTREntityDolGuldurOrc.class, 10, 150),
			new LOTRUnitTradeEntry(LOTREntityDolGuldurOrcArcher.class, 20, 200),
			new LOTRUnitTradeEntry(LOTREntityMirkwoodSpider.class, 10, 150),
			new LOTRUnitTradeEntry(LOTREntityDolGuldurOrc.class, LOTREntityMirkwoodSpider.class, "DolGuldurOrc_Spider", 20, 250),
			new LOTRUnitTradeEntry(LOTREntityDolGuldurOrcArcher.class, LOTREntityMirkwoodSpider.class, "DolGuldurOrcArcher_Spider", 30, 300),
			new LOTRUnitTradeEntry(LOTREntityMirkTroll.class, 50, 400),
			new LOTRUnitTradeEntry(LOTREntityDolGuldurBannerBearer.class, 40, 300)
		};
	}
}
