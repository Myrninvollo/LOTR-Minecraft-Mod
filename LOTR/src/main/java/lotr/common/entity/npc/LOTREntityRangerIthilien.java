package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiomeGenIthilien;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityRangerIthilien extends LOTREntityRanger
{
	public LOTREntityRangerIthilien(World world)
	{
		super(world);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.GONDOR;
	}
	
	@Override
	public Item getRangerSwordId()
	{
		return LOTRMod.swordGondor;
	}
	
	@Override
	public Item getRangerBowId()
	{
		return Items.bow;
	}
	
	@Override
	public float getBlockPathWeight(int i, int j, int k)
	{
		float f = super.getBlockPathWeight(i, j, k);
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome instanceof LOTRBiomeGenIthilien)
		{
			f += 20F;
		}
		return f;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killRangerIthilien;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.RANGER_ITHILIEN_BONUS;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			return "rangerIthilien_friendly";
		}
		else
		{
			return "rangerIthilien_hostile";
		}
	}
}
