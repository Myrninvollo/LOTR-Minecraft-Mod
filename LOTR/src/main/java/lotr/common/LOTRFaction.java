package lotr.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public enum LOTRFaction
{
	HOBBIT(0x59CE4E),
	RANGER_NORTH(0x44A839),
	BLUE_MOUNTAINS(0x648DBC),
	HIGH_ELF(0x49C2FF),
	GUNDABAD(0x966C54),
	ANGMAR(0x779177),
	WOOD_ELF(0x39964E),
	DOL_GULDUR(0x3B775C),
	DWARF(0x3E506B),
	GALADHRIM(0xE5D180),
	DUNLAND(0xA8948F),
	FANGORN(0x49B752),
	ROHAN(0xCC986C),
	URUK_HAI(0x738970),
	GONDOR(0x00D8FF),
	MORDOR(0xE2291F),
	NEAR_HARAD(0xEAAB5D),
	FAR_HARAD(0x2E6342),
	HALF_TROLL(0x9E8373),
	HOSTILE(true),
	UNALIGNED(false);
	
	public static int totalPlayerFactions;
	
	private Set enemies = new HashSet();
	public Set killPositives = new HashSet();
	public Set killNegatives = new HashSet();
	public boolean allowPlayer;
	public boolean allowEntityRegistry;
	private String factionName;
	public float[] factionColors;
	
	public static Set playersTakenRewardItem = new HashSet();
	
	private LOTRFaction(int i)
	{
		this(true, true, i);
	}
	
	private LOTRFaction(boolean flag)
	{
		this(false, flag, 0);
	}
	
	private LOTRFaction(boolean flag, boolean flag1, int i)
	{
		allowPlayer = flag;
		allowEntityRegistry = flag1;
		
		factionColors = new float[3];
		factionColors[0] = (float)((i >> 16) & 255) / 255F;
		factionColors[1] = (float)((i >> 8) & 255) / 255F;
		factionColors[2] = (float)(i & 255) / 255F;
	}

	static
	{
		for (LOTRFaction f : values())
		{
			if (f.allowPlayer)
			{
				totalPlayerFactions++;
			}
		}
		
		HOBBIT.addEnemy(GUNDABAD);
		HOBBIT.addEnemy(ANGMAR);
		HOBBIT.addEnemy(DOL_GULDUR);
		HOBBIT.addEnemy(URUK_HAI);
		HOBBIT.addEnemy(MORDOR);
		HOBBIT.addEnemy(HALF_TROLL);
		
		HOBBIT.addKillNegative(HOBBIT);
		HOBBIT.addKillNegative(RANGER_NORTH);
		
		HOBBIT.addKillPositive(GUNDABAD);
		
		RANGER_NORTH.addEnemy(GUNDABAD);
		RANGER_NORTH.addEnemy(ANGMAR);
		RANGER_NORTH.addEnemy(DOL_GULDUR);
		RANGER_NORTH.addEnemy(DUNLAND);
		RANGER_NORTH.addEnemy(URUK_HAI);
		RANGER_NORTH.addEnemy(MORDOR);
		RANGER_NORTH.addEnemy(NEAR_HARAD);
		RANGER_NORTH.addEnemy(FAR_HARAD);
		RANGER_NORTH.addEnemy(HALF_TROLL);
		
		RANGER_NORTH.addKillNegative(RANGER_NORTH);
		RANGER_NORTH.addKillNegative(HIGH_ELF);
		
		RANGER_NORTH.addKillPositive(GUNDABAD);
		RANGER_NORTH.addKillPositive(ANGMAR);
		
		BLUE_MOUNTAINS.addEnemy(GUNDABAD);
		BLUE_MOUNTAINS.addEnemy(ANGMAR);
		BLUE_MOUNTAINS.addEnemy(DOL_GULDUR);
		BLUE_MOUNTAINS.addEnemy(URUK_HAI);
		BLUE_MOUNTAINS.addEnemy(MORDOR);
		BLUE_MOUNTAINS.addEnemy(HALF_TROLL);
		
		BLUE_MOUNTAINS.addKillNegative(BLUE_MOUNTAINS);
		BLUE_MOUNTAINS.addKillNegative(DWARF);
		
		BLUE_MOUNTAINS.addKillPositive(GUNDABAD);
		
		HIGH_ELF.addEnemy(GUNDABAD);
		HIGH_ELF.addEnemy(ANGMAR);
		HIGH_ELF.addEnemy(DOL_GULDUR);
		HIGH_ELF.addEnemy(DUNLAND);
		HIGH_ELF.addEnemy(URUK_HAI);
		HIGH_ELF.addEnemy(MORDOR);
		HIGH_ELF.addEnemy(NEAR_HARAD);
		HIGH_ELF.addEnemy(FAR_HARAD);
		HIGH_ELF.addEnemy(HALF_TROLL);
		
		HIGH_ELF.addKillNegative(HIGH_ELF);
		HIGH_ELF.addKillNegative(RANGER_NORTH);
		HIGH_ELF.addKillNegative(GALADHRIM);
		
		HIGH_ELF.addKillPositive(GUNDABAD);
		HIGH_ELF.addKillPositive(ANGMAR);
		
		GUNDABAD.addEnemy(HOBBIT);
		GUNDABAD.addEnemy(RANGER_NORTH);
		GUNDABAD.addEnemy(BLUE_MOUNTAINS);
		GUNDABAD.addEnemy(HIGH_ELF);
		GUNDABAD.addEnemy(WOOD_ELF);
		GUNDABAD.addEnemy(DWARF);
		GUNDABAD.addEnemy(GALADHRIM);
		GUNDABAD.addEnemy(DUNLAND);
		GUNDABAD.addEnemy(FANGORN);
		GUNDABAD.addEnemy(ROHAN);
		GUNDABAD.addEnemy(GONDOR);
		
		GUNDABAD.addKillNegative(GUNDABAD);
		GUNDABAD.addKillNegative(ANGMAR);
		GUNDABAD.addKillNegative(DOL_GULDUR);
		
		GUNDABAD.addKillPositive(HOBBIT);
		GUNDABAD.addKillPositive(RANGER_NORTH);
		GUNDABAD.addKillPositive(BLUE_MOUNTAINS);
		GUNDABAD.addKillPositive(HIGH_ELF);
		GUNDABAD.addKillPositive(WOOD_ELF);
		GUNDABAD.addKillPositive(DWARF);
		GUNDABAD.addKillPositive(GALADHRIM);
		GUNDABAD.addKillPositive(DUNLAND);
		GUNDABAD.addKillPositive(FANGORN);
		
		ANGMAR.addEnemy(HOBBIT);
		ANGMAR.addEnemy(RANGER_NORTH);
		ANGMAR.addEnemy(BLUE_MOUNTAINS);
		ANGMAR.addEnemy(HIGH_ELF);
		ANGMAR.addEnemy(WOOD_ELF);
		ANGMAR.addEnemy(DWARF);
		ANGMAR.addEnemy(GALADHRIM);
		ANGMAR.addEnemy(DUNLAND);
		ANGMAR.addEnemy(FANGORN);
		ANGMAR.addEnemy(ROHAN);
		ANGMAR.addEnemy(GONDOR);
		
		ANGMAR.addKillNegative(ANGMAR);
		ANGMAR.addKillNegative(GUNDABAD);
		
		ANGMAR.addKillPositive(RANGER_NORTH);
		ANGMAR.addKillPositive(HIGH_ELF);
		
		WOOD_ELF.addEnemy(GUNDABAD);
		WOOD_ELF.addEnemy(ANGMAR);
		WOOD_ELF.addEnemy(DOL_GULDUR);
		WOOD_ELF.addEnemy(DUNLAND);
		WOOD_ELF.addEnemy(URUK_HAI);
		WOOD_ELF.addEnemy(MORDOR);
		WOOD_ELF.addEnemy(NEAR_HARAD);
		WOOD_ELF.addEnemy(FAR_HARAD);
		WOOD_ELF.addEnemy(HALF_TROLL);
		
		WOOD_ELF.addKillNegative(WOOD_ELF);
		WOOD_ELF.addKillNegative(HIGH_ELF);
		WOOD_ELF.addKillNegative(GALADHRIM);
		
		WOOD_ELF.addKillPositive(GUNDABAD);
		WOOD_ELF.addKillPositive(DOL_GULDUR);
		
		DOL_GULDUR.addEnemy(HOBBIT);
		DOL_GULDUR.addEnemy(RANGER_NORTH);
		DOL_GULDUR.addEnemy(BLUE_MOUNTAINS);
		DOL_GULDUR.addEnemy(HIGH_ELF);
		DOL_GULDUR.addEnemy(WOOD_ELF);
		DOL_GULDUR.addEnemy(DWARF);
		DOL_GULDUR.addEnemy(GALADHRIM);
		DOL_GULDUR.addEnemy(DUNLAND);
		DOL_GULDUR.addEnemy(FANGORN);
		DOL_GULDUR.addEnemy(ROHAN);
		DOL_GULDUR.addEnemy(GONDOR);
		
		DOL_GULDUR.addKillNegative(DOL_GULDUR);
		DOL_GULDUR.addKillNegative(GUNDABAD);
		
		DOL_GULDUR.addKillPositive(WOOD_ELF);
		DOL_GULDUR.addKillPositive(GALADHRIM);
		
		DWARF.addEnemy(GUNDABAD);
		DWARF.addEnemy(ANGMAR);
		DWARF.addEnemy(DOL_GULDUR);
		DWARF.addEnemy(URUK_HAI);
		DWARF.addEnemy(MORDOR);
		DWARF.addEnemy(HALF_TROLL);
		
		DWARF.addKillNegative(DWARF);
		DWARF.addKillNegative(BLUE_MOUNTAINS);
		
		DWARF.addKillPositive(GUNDABAD);
		
		GALADHRIM.addEnemy(GUNDABAD);
		GALADHRIM.addEnemy(ANGMAR);
		GALADHRIM.addEnemy(DOL_GULDUR);
		GALADHRIM.addEnemy(DUNLAND);
		GALADHRIM.addEnemy(URUK_HAI);
		GALADHRIM.addEnemy(MORDOR);
		GALADHRIM.addEnemy(NEAR_HARAD);
		GALADHRIM.addEnemy(FAR_HARAD);
		GALADHRIM.addEnemy(HALF_TROLL);
		
		GALADHRIM.addKillNegative(GALADHRIM);
		GALADHRIM.addKillNegative(HIGH_ELF);
		GALADHRIM.addKillNegative(WOOD_ELF);
		
		GALADHRIM.addKillPositive(GUNDABAD);
		GALADHRIM.addKillPositive(DOL_GULDUR);
		
		DUNLAND.addEnemy(RANGER_NORTH);
		DUNLAND.addEnemy(HIGH_ELF);
		DUNLAND.addEnemy(GUNDABAD);
		DUNLAND.addEnemy(ANGMAR);
		DUNLAND.addEnemy(WOOD_ELF);
		DUNLAND.addEnemy(DOL_GULDUR);
		DUNLAND.addEnemy(GALADHRIM);
		DUNLAND.addEnemy(ROHAN);
		DUNLAND.addEnemy(GONDOR);
		DUNLAND.addEnemy(MORDOR);
		DUNLAND.addEnemy(HALF_TROLL);
		
		DUNLAND.addKillNegative(DUNLAND);
		
		DUNLAND.addKillPositive(ROHAN);
		DUNLAND.addKillPositive(GONDOR);
		
		FANGORN.addEnemy(GUNDABAD);
		FANGORN.addEnemy(ANGMAR);
		FANGORN.addEnemy(DOL_GULDUR);
		FANGORN.addEnemy(URUK_HAI);
		FANGORN.addEnemy(MORDOR);
		FANGORN.addEnemy(HALF_TROLL);
		
		FANGORN.addKillNegative(FANGORN);

		FANGORN.addKillPositive(GUNDABAD);
		FANGORN.addKillPositive(URUK_HAI);
		
		ROHAN.addEnemy(GUNDABAD);
		ROHAN.addEnemy(ANGMAR);
		ROHAN.addEnemy(DOL_GULDUR);
		ROHAN.addEnemy(DUNLAND);
		ROHAN.addEnemy(URUK_HAI);
		ROHAN.addEnemy(MORDOR);
		ROHAN.addEnemy(NEAR_HARAD);
		ROHAN.addEnemy(FAR_HARAD);
		ROHAN.addEnemy(HALF_TROLL);
		
		ROHAN.addKillNegative(ROHAN);
		ROHAN.addKillNegative(GONDOR);
		
		ROHAN.addKillPositive(DUNLAND);
		ROHAN.addKillPositive(URUK_HAI);
		ROHAN.addKillPositive(MORDOR);
		
		URUK_HAI.addEnemy(HOBBIT);
		URUK_HAI.addEnemy(RANGER_NORTH);
		URUK_HAI.addEnemy(BLUE_MOUNTAINS);
		URUK_HAI.addEnemy(HIGH_ELF);
		URUK_HAI.addEnemy(WOOD_ELF);
		URUK_HAI.addEnemy(DWARF);
		URUK_HAI.addEnemy(GALADHRIM);
		URUK_HAI.addEnemy(FANGORN);
		URUK_HAI.addEnemy(ROHAN);
		URUK_HAI.addEnemy(GONDOR);
		
		URUK_HAI.addKillNegative(URUK_HAI);
		
		URUK_HAI.addKillPositive(ROHAN);
		URUK_HAI.addKillPositive(FANGORN);
		
		GONDOR.addEnemy(GUNDABAD);
		GONDOR.addEnemy(ANGMAR);
		GONDOR.addEnemy(DOL_GULDUR);
		GONDOR.addEnemy(DUNLAND);
		GONDOR.addEnemy(URUK_HAI);
		GONDOR.addEnemy(MORDOR);
		GONDOR.addEnemy(NEAR_HARAD);
		GONDOR.addEnemy(FAR_HARAD);
		GONDOR.addEnemy(HALF_TROLL);
		
		GONDOR.addKillNegative(GONDOR);
		GONDOR.addKillNegative(RANGER_NORTH);
		GONDOR.addKillNegative(ROHAN);
		
		GONDOR.addKillPositive(MORDOR);
		GONDOR.addKillPositive(NEAR_HARAD);
		GONDOR.addKillPositive(FAR_HARAD);
		GONDOR.addKillPositive(HALF_TROLL);
		
		MORDOR.addEnemy(HOBBIT);
		MORDOR.addEnemy(RANGER_NORTH);
		MORDOR.addEnemy(BLUE_MOUNTAINS);
		MORDOR.addEnemy(HIGH_ELF);
		MORDOR.addEnemy(WOOD_ELF);
		MORDOR.addEnemy(DWARF);
		MORDOR.addEnemy(GALADHRIM);
		MORDOR.addEnemy(DUNLAND);
		MORDOR.addEnemy(FANGORN);
		MORDOR.addEnemy(ROHAN);
		MORDOR.addEnemy(GONDOR);
		
		MORDOR.addKillNegative(MORDOR);
		MORDOR.addKillNegative(NEAR_HARAD);
		MORDOR.addKillNegative(FAR_HARAD);
		MORDOR.addKillNegative(HALF_TROLL);
		
		MORDOR.addKillPositive(ROHAN);
		MORDOR.addKillPositive(GONDOR);
		
		NEAR_HARAD.addEnemy(RANGER_NORTH);
		NEAR_HARAD.addEnemy(HIGH_ELF);
		NEAR_HARAD.addEnemy(WOOD_ELF);
		NEAR_HARAD.addEnemy(GALADHRIM);
		NEAR_HARAD.addEnemy(ROHAN);
		NEAR_HARAD.addEnemy(GONDOR);
		
		NEAR_HARAD.addKillNegative(NEAR_HARAD);
		NEAR_HARAD.addKillNegative(MORDOR);
		
		NEAR_HARAD.addKillPositive(GONDOR);
		
		FAR_HARAD.addEnemy(RANGER_NORTH);
		FAR_HARAD.addEnemy(HIGH_ELF);
		FAR_HARAD.addEnemy(WOOD_ELF);
		FAR_HARAD.addEnemy(GALADHRIM);
		FAR_HARAD.addEnemy(ROHAN);
		FAR_HARAD.addEnemy(GONDOR);
		
		FAR_HARAD.addKillNegative(FAR_HARAD);
		FAR_HARAD.addKillNegative(MORDOR);
		
		FAR_HARAD.addKillPositive(GONDOR);
		
		HALF_TROLL.addEnemy(HOBBIT);
		HALF_TROLL.addEnemy(RANGER_NORTH);
		HALF_TROLL.addEnemy(BLUE_MOUNTAINS);
		HALF_TROLL.addEnemy(HIGH_ELF);
		HALF_TROLL.addEnemy(WOOD_ELF);
		HALF_TROLL.addEnemy(DWARF);
		HALF_TROLL.addEnemy(GALADHRIM);
		HALF_TROLL.addEnemy(DUNLAND);
		HALF_TROLL.addEnemy(FANGORN);
		HALF_TROLL.addEnemy(ROHAN);
		HALF_TROLL.addEnemy(GONDOR);
		
		HALF_TROLL.addKillNegative(HALF_TROLL);
		HALF_TROLL.addKillNegative(MORDOR);
		
		HALF_TROLL.addKillPositive(GONDOR);
	}
	
	public void addEnemy(LOTRFaction f)
	{
		enemies.add(f);
	}
	
	public void addKillPositive(LOTRFaction f)
	{
		killPositives.add(f);
	}
	
	public void addKillNegative(LOTRFaction f)
	{
		killNegatives.add(f);
	}
	
	public boolean isEnemy(LOTRFaction f)
	{
		if (this == HOSTILE || f == HOSTILE)
		{
			return true;
		}
		if (this == UNALIGNED || f == UNALIGNED)
		{
			return false;
		}
		return enemies.contains(f);
	}
	
	public String factionName()
	{
		return StatCollector.translateToLocal("lotr.faction." + name() + ".name");
	}
	
	public boolean isAllied(LOTRFaction f)
	{
		if (this == UNALIGNED || this == HOSTILE)
		{
			return false;
		}
		if (f == UNALIGNED || f == HOSTILE)
		{
			return false;
		}
		if (this == f)
		{
			return true;
		}
		return !isEnemy(f);
	}
	
	public ItemStack createAlignmentReward()
	{
		if (this == WOOD_ELF)
		{
			return new ItemStack(LOTRMod.anduril);
		}
		return null;
	}
	
	public static LOTRFaction forName(String factionName)
	{
		for (LOTRFaction f : values())
		{
			if (f.name().equals(factionName))
			{
				return f;
			}
		}
		return null;
	}
	
	public static LOTRFaction forID(int ID)
	{
		for (LOTRFaction f : values())
		{
			if (f.ordinal() == ID)
			{
				return f;
			}
		}
		return null;
	}
	
	public static List getFactionNameList()
	{
		ArrayList list = new ArrayList();
		for (LOTRFaction f : values())
		{
			if (!f.allowPlayer)
			{
				continue;
			}
			list.add(f.name());
		}
		return list;
	}
}