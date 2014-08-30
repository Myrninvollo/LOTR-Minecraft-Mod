package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtByTarget;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtTarget;
import lotr.common.entity.ai.LOTREntityAINearestAttackableTargetWoodElf;
import lotr.common.entity.npc.LOTREntityNPC.AttackMode;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityWoodElf extends LOTREntityElf
{
	public LOTREntityWoodElf(World world)
	{
		super(world);
		tasks.addTask(2, rangedAttackAI);
		addTargetTasks(true, LOTREntityAINearestAttackableTargetWoodElf.class);
	}
	
	@Override
	public EntityAIBase createElfMeleeAttackAI()
	{
		return createElfRangedAttackAI();
	}
	
	@Override
	public EntityAIBase createElfRangedAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.25D, 30, 50, 16F);
	}
	
	@Override
	public Item getElfSwordId()
	{
		return getElfBowId();
	}
	
	@Override
	public Item getElfBowId()
	{
		return LOTRMod.mirkwoodBow;
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.WOOD_ELF;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killWoodElf;
	}

	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.WOOD_ELF;
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
				entityDropItem(new ItemStack(LOTRMod.mugRedWine, 1, 1 + rand.nextInt(3)), 0F);
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
		return biome == LOTRBiome.mirkwood && j > 62 && (worldObj.getBlock(i, j - 1, k) == Blocks.grass || (worldObj.getBlock(i, j - 1, k) == LOTRMod.planks && worldObj.getBlockMetadata(i, j - 1, k) == 2));
	}
	
	@Override
	public float getBlockPathWeight(int i, int j, int k)
	{
		float f = 0F;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome == LOTRBiome.mirkwood)
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
				return "woodElf_hired";
			}
			else
			{
				if (LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.Levels.WOOD_ELF_TRUST)
				{
					return "woodElf_friendly";
				}
				else
				{
					return "woodElf_neutral";
				}
			}
		}
		else
		{
			return "woodElf_hostile";
		}
	}
}
