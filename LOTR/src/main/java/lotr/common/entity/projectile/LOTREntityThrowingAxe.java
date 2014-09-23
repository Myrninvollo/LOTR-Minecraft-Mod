package lotr.common.entity.projectile;

import lotr.common.item.LOTRItemThrowingAxe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityThrowingAxe extends LOTREntityProjectileBase
{
	private int axeRotation;
	
    public LOTREntityThrowingAxe(World world)
    {
        super(world);
    }
	
    public LOTREntityThrowingAxe(World world, ItemStack item, double d, double d1, double d2)
    {
        super(world, item, d, d1, d2);
    }

    public LOTREntityThrowingAxe(World world, EntityLivingBase entityliving, ItemStack item, float charge)
    {
        super(world, entityliving, item, charge);
    }
	
    public LOTREntityThrowingAxe(World world, EntityLivingBase entityliving, EntityLivingBase target, ItemStack item, float charge, float inaccuracy)
    {
        super(world, entityliving, target, item, charge, inaccuracy);
    }
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (!inGround)
		{
			axeRotation++;
			if (axeRotation > 9)
			{
				axeRotation = 0;
			}
			rotationPitch = ((float)axeRotation / 9F) * 360F;
		}
	}
	
	@Override
	public boolean isDamageable()
	{
		return true;
	}
	
	@Override
	public float getDamageVsEntity(Entity entity)
	{
		Item item = getItem().getItem();
		return ((LOTRItemThrowingAxe)item).getAxeMaterial().getDamageVsEntity() + 4F;
	}
}
