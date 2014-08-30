package lotr.common.entity.npc;

import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtByTarget;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class LOTREntityHuornBase extends LOTREntityTree
{
	private float origWidth;
	private float origHeight;
	private boolean resizedForRender;
	
	public LOTREntityHuornBase(World world)
	{
		super(world);
		setSize(1.5F, 4F);
		origWidth = width;
		origHeight = height;
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(2, new LOTREntityAIAttackOnCollide(this, 1.5D, false));
		tasks.addTask(3, new LOTREntityAIFollowHiringPlayer(this));
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(17, Byte.valueOf((byte)0));
	}
	
	public boolean isHuornActive()
	{
		return dataWatcher.getWatchableObjectByte(17) == (byte)1;
	}
	
	public void setHuornActive(boolean flag)
	{
		dataWatcher.updateObject(17, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}

	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(60D);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        getEntityAttribute(npcAttackDamage).setBaseValue(4D);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRender3d(double d, double d1, double d2)
    {
		boolean flag = super.isInRangeToRender3d(d, d1, d2);
		if (flag)
		{
			resizeForRender(true);
		}
		return flag;
    }
	
	public boolean isResizedForRender()
	{
		return resizedForRender;
	}
	
	public void resizeForRender(boolean flag)
	{
		if (flag)
		{
			setSize(5F, 7F);
			resizedForRender = true;
		}
		else
		{
			setSize(origWidth, origHeight);
			resizedForRender = false;
		}
	}
	
	@Override
	public void onUpdate()
	{
		if (worldObj.isRemote && isResizedForRender())
		{
			resizeForRender(false);
		}
		
		super.onUpdate();
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!worldObj.isRemote)
		{
			boolean active = !getNavigator().noPath();
			if (active != isHuornActive())
			{
				setHuornActive(active);
			}
		}
	}
	
    @Override
    protected int decreaseAirSupply(int i)
    {
        return i;
    }
	
	@Override
    public void applyEntityCollision(Entity entity)
    {
        if (isHuornActive())
		{
			super.applyEntityCollision(entity);
		}
		else
		{
			double x = motionX;
			double y = motionY;
			double z = motionZ;
			super.applyEntityCollision(entity);
			motionX = x;
			motionY = y;
			motionZ = z;
		}
	}
	
	@Override
    public void collideWithEntity(Entity entity)
    {
        if (isHuornActive())
		{
			super.collideWithEntity(entity);
		}
		else
		{
			double x = motionX;
			double y = motionY;
			double z = motionZ;
			super.collideWithEntity(entity);
			motionX = x;
			motionY = y;
			motionZ = z;
		}
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f)
	{
		boolean flag = super.attackEntityFrom(damagesource, f);
		if (flag && !worldObj.isRemote && !isHuornActive())
		{
			setHuornActive(true);
		}
		return flag;
	}
	
	@Override
    protected String getHurtSound()
    {
        return Blocks.log.stepSound.getBreakSound();
    }

    @Override
    protected String getDeathSound()
    {
        return Blocks.log.stepSound.getBreakSound();
    }
}
