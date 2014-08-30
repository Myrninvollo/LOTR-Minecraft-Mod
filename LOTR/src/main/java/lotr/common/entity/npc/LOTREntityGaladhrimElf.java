package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.npc.LOTREntityNPC.AttackMode;
import lotr.common.quest.LOTRMiniQuest;
import lotr.common.quest.LOTRMiniQuestFactory;
import lotr.common.world.biome.LOTRBiomeGenLothlorien;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityGaladhrimElf extends LOTREntityElf
{
	public LOTREntityGaladhrimElf(World world)
	{
		super(world);
	}
	
	@Override
	public LOTRNPCMount createMountToRide()
	{
		LOTREntityHorse horse = (LOTREntityHorse)super.createMountToRide();
		horse.setMountArmor(new ItemStack(LOTRMod.horseArmorGaladhrim));
		return horse;
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.GALADHRIM;
	}
	
	@Override
	public Item getElfSwordId()
	{
		return LOTRMod.swordMallorn;
	}
	
	@Override
	public Item getElfBowId()
	{
		return LOTRMod.mallornBow;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killElf;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.GALADHRIM;
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
		if (j > 62 && worldObj.getBlock(i, j - 1, k) == Blocks.grass)
		{
			BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
			return biome instanceof LOTRBiomeGenLothlorien;
		}
		return false;
	}
	
	@Override
	public float getBlockPathWeight(int i, int j, int k)
	{
		float f = 0F;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome instanceof LOTRBiomeGenLothlorien)
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
				return "galadhrim_hired";
			}
			return "galadhrim_friendly";
		}
		else
		{
			return "galadhrim_hostile";
		}
	}
	
	@Override
	public LOTRMiniQuest createMiniQuest(EntityPlayer entityplayer)
	{
		return LOTRMiniQuestFactory.GALADHRIM.createQuest(entityplayer);
	}
}
