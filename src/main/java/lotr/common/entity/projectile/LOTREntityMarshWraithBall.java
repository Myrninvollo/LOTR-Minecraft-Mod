package lotr.common.entity.projectile;

import lotr.common.entity.npc.LOTREntityMarshWraith;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityMarshWraithBall extends EntityThrowable
{
	public int animationTick;
	public Entity attackTarget;
	
	public LOTREntityMarshWraithBall(World world)
	{
		super(world);
	}
	
	public LOTREntityMarshWraithBall(World world, EntityLivingBase entityliving)
	{
		super(world, entityliving);
	}
	
	public LOTREntityMarshWraithBall(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
	}
	
	public LOTREntityMarshWraithBall(World world, EntityLivingBase entityliving, EntityLivingBase target)
	{
		super(world, entityliving);
		attackTarget = target;

        posY = entityliving.posY + LOTREntityMarshWraith.VERTICAL_OFFSET + (double)entityliving.getEyeHeight() - 0.10000000149011612D;
        double d = target.posX - entityliving.posX;
        double d1 = target.boundingBox.minY + (double)(target.height / 3.0F) - posY;
        double d2 = target.posZ - entityliving.posZ;
        double d3 = (double)MathHelper.sqrt_double(d * d + d2 * d2);

        if (d3 >= 1.0E-7D)
        {
            float f2 = (float)(Math.atan2(d2, d) * 180.0D / Math.PI) - 90.0F;
            float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
            double d4 = d / d3;
            double d5 = d2 / d3;
            setLocationAndAngles(entityliving.posX + d4, posY, entityliving.posZ + d5, f2, f3);
            float f4 = (float)d3 * 0.2F;
			setThrowableHeading(d, d1 + (double)f4, d2, func_70182_d(), 1.0F);
        }
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, Integer.valueOf(0));
	}
	
	public int getBallAge()
	{
		return dataWatcher.getWatchableObjectInt(16);
	}
	
	public void setBallAge(int age)
	{
		dataWatcher.updateObject(16, Integer.valueOf(age));
	}
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
		nbt.setInteger("BallAge", getBallAge());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        setBallAge(nbt.getInteger("BallAge"));
    }
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (worldObj.getWorldTime() % 2L == 0)
		{
			animationTick++;
			if (animationTick >= 16)
			{
				animationTick = 0;
			}
		}
		
		if (!worldObj.isRemote)
		{
			setBallAge(getBallAge() + 1);
			if (getBallAge() >= 200)
			{
				setDead();
			}
		}
	}
	
	@Override
	protected void onImpact(MovingObjectPosition m)
	{
		if (m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
		{
			if (!worldObj.isRemote)
			{
				setDead();
			}
		}
		else if (m.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
		{
			Entity entity = m.entityHit;
			if (attackTarget != null && entity == attackTarget)
			{
				entity.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 5F);
				if (!worldObj.isRemote)
				{
					setDead();
				}
			}
		}
	}
	
	@Override
    protected float func_70182_d()
    {
        return 0.5F;
    }
	
	@Override
    protected float getGravityVelocity()
    {
        return 0F;
    }
}
