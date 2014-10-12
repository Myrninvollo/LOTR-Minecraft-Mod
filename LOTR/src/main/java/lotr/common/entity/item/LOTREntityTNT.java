package lotr.common.entity.item;

import lotr.common.LOTRBannerProtection;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class LOTREntityTNT extends EntityTNTPrimed
{
	public LOTREntityTNT(World world)
	{
		this(world, 0D, 0D, 0D, null);
	}
	
	public LOTREntityTNT(World world, double d, double d1, double d2, EntityLivingBase entity)
	{
		super(world, d, d1, d2, entity);
	}
	
    @Override
    public boolean func_145774_a(Explosion explosion, World world, int i, int j, int k, Block block, float strength)
    {
        if (LOTRBannerProtection.isProtectedByBanner(worldObj, i, j, k, LOTRBannerProtection.forTNT(this), false))
        {
        	return false;
        }
        return super.func_145774_a(explosion, world, i, j, k, block, strength);
    }
}
