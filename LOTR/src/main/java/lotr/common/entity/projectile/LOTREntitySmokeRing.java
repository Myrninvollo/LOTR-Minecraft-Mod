package lotr.common.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntitySmokeRing extends EntityThrowable
{
	public static int MAX_AGE = 300;
	
	public LOTREntitySmokeRing(World world)
	{
		super(world);
	}
	
	public LOTREntitySmokeRing(World world, EntityLivingBase entityliving)
	{
		super(world, entityliving);
	}
	
	public LOTREntitySmokeRing(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, Integer.valueOf(0));
		dataWatcher.addObject(17, Integer.valueOf(0));
	}
	
	public int getSmokeAge()
	{
		return dataWatcher.getWatchableObjectInt(16);
	}
	
	public void setSmokeAge(int age)
	{
		dataWatcher.updateObject(16, Integer.valueOf(age));
	}
	
	public int getSmokeColour()
	{
		return dataWatcher.getWatchableObjectInt(17);
	}
	
	public void setSmokeColour(int colour)
	{
		dataWatcher.updateObject(17, Integer.valueOf(colour));
	}
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
		nbt.setInteger("SmokeAge", getSmokeAge());
		nbt.setInteger("SmokeColour", getSmokeColour());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        setSmokeAge(nbt.getInteger("SmokeAge"));
		setSmokeColour(nbt.getInteger("SmokeColour"));
    }
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (isInWater() && !worldObj.isRemote)
		{
			setDead();
		}
		
		if (!worldObj.isRemote)
		{
			setSmokeAge(getSmokeAge() + 1);
			if (getSmokeAge() >= MAX_AGE)
			{
				setDead();
			}
		}
	}
	
	@Override
	protected void onImpact(MovingObjectPosition m)
	{
		if (m.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && m.entityHit == getThrower())
		{
			return;
		}
		
		if (!worldObj.isRemote)
		{
			setDead();
		}
	}
	
	@Override
    protected float func_70182_d()
    {
        return 0.1F;
    }
	
	@Override
    protected float getGravityVelocity()
    {
        return 0F;
    }
}
