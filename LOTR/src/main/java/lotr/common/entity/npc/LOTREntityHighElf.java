package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityHighElf extends LOTREntityElf
{
	public LOTREntityHighElf(World world)
	{
		super(world);
	}
	
	@Override
	public LOTRNPCMount createMountToRide()
	{
		LOTREntityHorse horse = (LOTREntityHorse)super.createMountToRide();
		horse.setMountArmor(new ItemStack(LOTRMod.horseArmorHighElven));
		return horse;
	}

	@Override
	public Item getElfSwordId()
	{
		return LOTRMod.daggerHighElven;
	}
	
	@Override
	public Item getElfBowId()
	{
		return LOTRMod.elvenBow;
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.HIGH_ELF;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killHighElf;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.HIGH_ELF_BONUS;
	}
	
	@Override
	public boolean canElfSpawnHere()
	{
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(boundingBox.minY);
		int k = MathHelper.floor_double(posZ);
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		return biome == LOTRBiome.lindon && j > 62 && worldObj.getBlock(i, j - 1, k) == Blocks.grass;
	}
	
	@Override
	public float getBlockPathWeight(int i, int j, int k)
	{
		float f = 0F;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome == LOTRBiome.lindon)
		{
			f += 20F;
		}
		return f;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				return "highElf_hired";
			}
			else
			{
				return "highElf_friendly";
			}
		}
		else
		{
			return "highElf_hostile";
		}
	}
}
