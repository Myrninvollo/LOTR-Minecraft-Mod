package lotr.common.entity.projectile;

import lotr.common.LOTRMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTREntityCrossbowBolt extends LOTREntityProjectileBase
{
	public double boltDamageFactor = 2D;
	
    public LOTREntityCrossbowBolt(World world)
    {
        super(world);
    }
	
    public LOTREntityCrossbowBolt(World world, double d, double d1, double d2)
    {
        super(world, LOTRMod.crossbowBolt, 0, d, d1, d2);
    }

    public LOTREntityCrossbowBolt(World world, EntityLivingBase entityliving, float charge)
    {
        super(world, entityliving, LOTRMod.crossbowBolt, 0, charge);
    }
	
    public LOTREntityCrossbowBolt(World world, EntityLivingBase entityliving, EntityLivingBase target, float charge, float inaccuracy)
    {
        super(world, entityliving, target, LOTRMod.crossbowBolt, 0, charge, inaccuracy);
    }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
		nbt.setDouble("boltDamageFactor", boltDamageFactor);
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("boltDamageFactor"))
        {
            boltDamageFactor = nbt.getDouble("boltDamageFactor");
        }
    }
	
	@Override
	public boolean isDamageable()
	{
		return false;
	}
	
	@Override
	public float getDamageVsEntity(Entity entity)
	{
		float momentum = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
		return momentum * 2F * (float)boltDamageFactor;
	}
	
	@Override
	public int maxTicksInGround()
	{
		return 1200;
	}
}
