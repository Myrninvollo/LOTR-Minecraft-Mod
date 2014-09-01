package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.quest.LOTRMiniQuest;
import lotr.common.quest.LOTRMiniQuestFactory;
import lotr.common.world.biome.LOTRBiomeGenLindon;
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
		return LOTRAlignmentValues.Bonuses.HIGH_ELF;
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		if (flag)
		{
			int dropChance = 20 - i * 4;
			dropChance = Math.max(dropChance, 1);
			if (rand.nextInt(dropChance) == 0)
			{
				entityDropItem(new ItemStack(LOTRMod.mugMiruvor, 1, 1 + rand.nextInt(3)), 0F);
			}
		}
	}
	
	@Override
	public boolean canElfSpawnHere()
	{
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(boundingBox.minY);
		int k = MathHelper.floor_double(posZ);
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		return biome instanceof LOTRBiomeGenLindon && j > 62 && worldObj.getBlock(i, j - 1, k) == Blocks.grass;
	}
	
	@Override
	public float getBlockPathWeight(int i, int j, int k)
	{
		float f = 0F;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome instanceof LOTRBiomeGenLindon)
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
	
	@Override
	public LOTRMiniQuest createMiniQuest(EntityPlayer entityplayer)
	{
		return LOTRMiniQuestFactory.HIGH_ELF.createQuest(entityplayer);
	}
}
