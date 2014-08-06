package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class LOTREntityRangerNorth extends LOTREntityRanger
{
	public LOTREntityRangerNorth(World world)
	{
		super(world);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.RANGER_NORTH;
	}
	
	@Override
	public Item getRangerSwordId()
	{
		return LOTRMod.daggerIron;
	}
	
	@Override
	public Item getRangerBowId()
	{
		return Items.bow;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killRangerNorth;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.RANGER_NORTH_BONUS;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				return "rangerNorth_hired";
			}
			return "rangerNorth_friendly";
		}
		else
		{
			return "rangerNorth_hostile";
		}
	}
}
