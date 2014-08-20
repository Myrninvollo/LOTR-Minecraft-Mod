package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtByTarget;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtTarget;
import lotr.common.world.biome.LOTRBiomeGenGondor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityGondorSoldier extends LOTREntityGondorMan
{
	public LOTREntityGondorSoldier(World world)
	{
		super(world);
        targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
        targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        addTargetTasks(4);
		spawnRidingHorse = rand.nextInt(6) == 0;
		spawnCountValue = 3;
		npcShield = LOTRShields.ALIGNMENT_GONDOR;
	}
	
	@Override
	public EntityAIBase createGondorAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.45D, false);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(4);
		if (i == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearGondor));
		}
		else if (i == 1)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.hammerGondor));
		}
		else
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.swordGondor));
		}
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsGondor));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsGondor));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyGondor));
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetGondor));
		return data;
    }
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killGondorSoldier;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.GONDOR_SOLDIER;
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		if (super.getCanSpawnHere())
		{
			if (liftSpawnRestrictions)
			{
				return true;
			}
			else
			{
				int i = MathHelper.floor_double(posX);
				int j = MathHelper.floor_double(boundingBox.minY);
				int k = MathHelper.floor_double(posZ);
				BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
				return biome instanceof LOTRBiomeGenGondor && j > 62 && worldObj.getBlock(i, j - 1, k) == Blocks.grass;
			}
		}
		return false;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				return "gondorSoldier_hired";
			}
			return "gondorSoldier_friendly";
		}
		else
		{
			return "gondorSoldier_hostile";
		}
	}
}
