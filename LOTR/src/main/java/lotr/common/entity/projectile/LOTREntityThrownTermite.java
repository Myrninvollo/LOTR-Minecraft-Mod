package lotr.common.entity.projectile;

import lotr.common.LOTRBannerProtection;
import lotr.common.entity.animal.LOTREntityTermite;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
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
	
    @Override
    public boolean func_145774_a(Explosion explosion, World world, int i, int j, int k, Block block, float strength)
    {
        if (LOTRBannerProtection.isProtectedByBanner(worldObj, i, j, k, LOTRBannerProtection.forThrown(this), false))
        {
        	return false;
        }
        return super.func_145774_a(explosion, world, i, j, k, block, strength);
    }
}