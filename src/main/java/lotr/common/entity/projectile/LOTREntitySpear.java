package lotr.common.entity.projectile;

import lotr.common.item.LOTRItemSpear;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTREntitySpear extends LOTREntityProjectileBase
{
    public LOTREntitySpear(World world)
    {
        super(world);
    }
	
    public LOTREntitySpear(World world, Item item, int damage, double d, double d1, double d2)
    {
        super(world, item, damage, d, d1, d2);
    }

    public LOTREntitySpear(World world, EntityLivingBase entityliving, Item item, int damage, float charge)
    {
        super(world, entityliving, item, damage, charge);
    }
	
    public LOTREntitySpear(World world, EntityLivingBase entityliving, EntityLivingBase target, Item item, int damage, float charge, float inaccuracy)
    {
        super(world, entityliving, target, item, damage, charge, inaccuracy);
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
		float damage = ((LOTRItemSpear)Item.getItemById(getItemID())).spearMaterial.getDamageVsEntity() + 5F;
		return momentum * damage * 2F / 3F;
	}
}
