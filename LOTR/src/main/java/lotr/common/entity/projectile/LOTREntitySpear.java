package lotr.common.entity.projectile;

import lotr.common.item.LOTRItemSpear;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTREntitySpear extends LOTREntityProjectileBase
{
    public LOTREntitySpear(World world)
    {
        super(world);
    }
	
    public LOTREntitySpear(World world, ItemStack item, double d, double d1, double d2)
    {
        super(world, item, d, d1, d2);
    }

    public LOTREntitySpear(World world, EntityLivingBase entityliving, ItemStack item, float charge)
    {
        super(world, entityliving, item, charge);
    }
	
    public LOTREntitySpear(World world, EntityLivingBase entityliving, EntityLivingBase target, ItemStack item, float charge, float inaccuracy)
    {
        super(world, entityliving, target, item, charge, inaccuracy);
    }
	
	@Override
	public boolean isDamageable()
	{
		return true;
	}
	
	@Override
	public float getDamageVsEntity(Entity entity)
	{
		float momentum = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
		float damage = ((LOTRItemSpear)getItem().getItem()).getLOTRWeaponDamage();
		return momentum * damage * 0.7F;
	}
}
