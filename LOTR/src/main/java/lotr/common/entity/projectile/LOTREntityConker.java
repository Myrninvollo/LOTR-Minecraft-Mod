package lotr.common.entity.projectile;

import lotr.common.LOTRMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityConker extends EntityThrowable
{
	public LOTREntityConker(World world)
	{
		super(world);
	}
	
	public LOTREntityConker(World world, EntityLivingBase entity)
	{
		super(world, entity);
	}
	
	public LOTREntityConker(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
	}
	
	@Override
	protected void onImpact(MovingObjectPosition m)
	{
        if (m.entityHit != null)
        {
            m.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 1F);
        }

        if (!worldObj.isRemote)
        {
            entityDropItem(new ItemStack(LOTRMod.chestnut), 0F);
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
