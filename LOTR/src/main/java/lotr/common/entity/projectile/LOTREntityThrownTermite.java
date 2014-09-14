package lotr.common.entity.projectile;

import lotr.common.entity.animal.LOTREntityTermite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityThrownTermite extends EntityThrowable
{
    public LOTREntityThrownTermite(World world)
    {
        super(world);
    }

    public LOTREntityThrownTermite(World world, EntityLivingBase throwingEntity)
    {
        super(world, throwingEntity);
    }

    public LOTREntityThrownTermite(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
    }

	@Override
    protected void onImpact(MovingObjectPosition movingobjectposition)
    {
        if (!worldObj.isRemote)
        {
            worldObj.createExplosion(this, posX, posY, posZ, LOTREntityTermite.explosionSize, true);
            setDead();
        }
    }
}