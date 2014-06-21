package lotr.common.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityPebble extends EntityThrowable
{
	public LOTREntityPebble(World world)
	{
		super(world);
	}
	
	public LOTREntityPebble(World world, EntityLivingBase entityliving)
	{
		super(world, entityliving);
	}
	
	public LOTREntityPebble(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
	}
	
	@Override
	protected void onImpact(MovingObjectPosition m)
	{
        if (m.entityHit != null)
        {
            m.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 2F);
        }

        if (!worldObj.isRemote)
        {
            setDead();
        }
	}
	
	@Override
    protected float func_70182_d()
    {
        return 1F;
    }
	
	@Override
    protected float getGravityVelocity()
    {
        return 0.04F;
    }
}
