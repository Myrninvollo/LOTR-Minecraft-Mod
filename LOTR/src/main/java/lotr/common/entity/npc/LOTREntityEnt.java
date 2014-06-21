package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTREntityEnt extends LOTREntityTree
{
	public int eyesClosed;
	
	public LOTREntityEnt(World world)
	{
		super(world);
		setSize(1.4F, 4.6F);
		getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIAttackOnCollide(this, 2D, false));
        tasks.addTask(2, new EntityAIWander(this, 1D));
        tasks.addTask(3, new EntityAIWatchClosest2(this, EntityPlayer.class, 12F, 0.05F));
        tasks.addTask(3, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 8F, 0.02F));
        tasks.addTask(4, new EntityAIWatchClosest(this, EntityLiving.class, 10F, 0.02F));
        tasks.addTask(5, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        addTargetTasks(2);
		spawnCountValue = 2;
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(17, LOTRNames.getRandomEntName(rand));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80D);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
        getEntityAttribute(npcAttackDamage).setBaseValue(6D);
    }
	
	public String getEntName()
	{
		return dataWatcher.getWatchableObjectString(17);
	}
	
	public void setEntName(String s)
	{
		dataWatcher.updateObject(17, s);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.FANGORN;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setString("EntName", getEntName());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("EntName"))
		{
			setEntName(nbt.getString("EntName"));
		}
	}
	
	@Override
	public String getNPCName()
	{
		return getEntName();
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (worldObj.isRemote)
		{
			if (eyesClosed > 0)
			{
				eyesClosed--;
			}
			else if (rand.nextInt(400) == 0)
			{
				eyesClosed = 30;
			}
		}
	}
	
	@Override
    public boolean attackEntityAsMob(Entity entity)
	{
		if (super.attackEntityAsMob(entity))
		{
			float knockbackModifier = 1.5F;
			entity.addVelocity((double)(-MathHelper.sin(rotationYaw * (float)Math.PI / 180.0F) * knockbackModifier * 0.5F), 0.15D, (double)(MathHelper.cos(rotationYaw * (float)Math.PI / 180.0F) * knockbackModifier * 0.5F));
			return true;
		}
		return false;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killEnt;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.ENT_BONUS;
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		if (flag)
		{
			int dropChance = 20 - (i * 3);
			if (dropChance < 1)
			{
				dropChance = 1;
			}
			
			if (rand.nextInt(dropChance) == 0)
			{
				if (rand.nextBoolean())
				{
					dropItem(LOTRMod.entDraughtGreen, 1);
				}
				else
				{
					dropItem(LOTRMod.entDraughtBrown, 1);
				}
			}
		}
	}

	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 5 + rand.nextInt(6);
    }
	
	@Override
	protected float getSoundVolume()
	{
		return 1.5F;
	}
	
	@Override
	protected void func_145780_a(int i, int j, int k, Block block)
	{
		playSound("lotr:ent.step", 0.75F, getSoundPitch());
	}
	
	@Override
	protected LOTRAchievement getTalkAchievement()
	{
		return LOTRAchievement.talkEnt;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			return "ent_friendly";
		}
		else
		{
			return "ent_hostile";
		}
	}
}
