package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIDrink;
import lotr.common.entity.ai.LOTREntityAIEat;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtByTarget;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtTarget;
import lotr.common.world.biome.LOTRBiomeGenRohan;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityRohirrim extends LOTREntityRohanMan
{
	public LOTREntityRohirrim(World world)
	{
		super(world);
        targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
        targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        addTargetTasks(4);
		spawnRidingHorse = true;
		spawnCountValue = 3;
		npcShield = LOTRShields.ALIGNMENT_ROHAN;
	}
	
	@Override
	public EntityAIBase createRohanAttackAI()
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
		if (rand.nextInt(3) == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearRohan));
		}
		else if (rand.nextInt(3) == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.battleaxeRohan));
		}
		else
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.swordRohan));
		}
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsRohan));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsRohan));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyRohan));
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetRohan));
		
		return data;
    }
	
	@Override
	public String getCommandSenderName()
	{
		return StatCollector.translateToLocalFormatted("entity.lotr.Rohirrim.entityName", new Object[] {getNPCName()});
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killRohirrim;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.ROHIRRIM;
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
				return biome instanceof LOTRBiomeGenRohan && j > 62 && worldObj.getBlock(i, j - 1, k) == Blocks.grass;
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
				return "rohirrim_hired";
			}
			return "rohirrim_friendly";
		}
		else
		{
			return "rohirrim_hostile";
		}
	}
}
