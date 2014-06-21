package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class LOTREntityBlueDwarf extends LOTREntityDwarf
{
	public LOTREntityBlueDwarf(World world)
	{
		super(world);
		
		familyInfo.marriageEntityClass = LOTREntityBlueDwarf.class;
		familyInfo.marriageAchievement = LOTRAchievement.marryBlueDwarf;
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.BLUE_MOUNTAINS;
	}
	
	@Override
	protected Item getDwarfDagger()
	{
		return LOTRMod.daggerBlueDwarven;
	}
	
	@Override
	protected Item getDwarfSteelDrop()
	{
		return LOTRMod.blueDwarfSteel;
	}

	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killBlueDwarf;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.BLUE_DWARF_BONUS;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				return "blueDwarf_hired";
			}
			return isChild() ? "blueDwarfChild_friendly" : "blueDwarf_friendly";
		}
		else
		{
			return isChild() ? "blueDwarfChild_unfriendly" : "blueDwarf_hostile";
		}
	}
}
