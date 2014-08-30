package lotr.common.entity.projectile;

import lotr.common.LOTREventHandler;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityPlate extends EntityThrowable
{
	private int plateSpin;
	
	public LOTREntityPlate(World world)
	{
		super(world);
		setSize(0.5F, 0.5F);
	}
	
	public LOTREntityPlate(World world, EntityLivingBase entityliving)
	{
		super(world, entityliving);
		setSize(0.5F, 0.5F);
	}
	
	public LOTREntityPlate(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
		setSize(0.5F, 0.5F);
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		plateSpin++;
		if (plateSpin > 9)
		{
			plateSpin = 0;
		}
		rotationYaw = ((float)plateSpin / 9F) * 360F;
	}
	
	@Override
	protected void onImpact(MovingObjectPosition m)
	{
		if (m.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
		{
			if (m.entityHit == getThrower())
			{
				return;
			}
			else
			{
				m.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 1F);
			}
		}
		else if (m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
		{
			if (!worldObj.isRemote)
			{
				int i = m.blockX;
				int j = m.blockY;
				int k = m.blockZ;

				if (breakGlass(i, j, k))
				{
					int range = 2;
					for (int i1 = i - range; i1 <= i + range; i1++)
					{
						for (int j1 = j - range; j1 <= j + range; j1++)
						{
							for (int k1 = k - range; k1 <= k + range; k1++)
							{
								if (rand.nextInt(4) != 0)
								{
									breakGlass(i1, j1, k1);
								}
							}
						}
					}
					return;
				}
				
			}
		}
		
		for (int i = 0; i < 8; i++)
		{
			double d = posX - 0.25D + (double)(rand.nextFloat() * 0.5F);
			double d1 = posY - 0.25D + (double)(rand.nextFloat() * 0.5F);
			double d2 = posZ - 0.25D + (double)(rand.nextFloat() * 0.5F);
			worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(LOTRMod.plateBlock) + "_0", d, d1, d2, 0D, 0D, 0D);
		}
		
		if (!worldObj.isRemote)
		{
			worldObj.playSoundAtEntity(this, "lotr:item.plate_break", 1F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			setDead();
		}
	}
	
	private boolean breakGlass(int i, int j, int k)
	{
		Block block = worldObj.getBlock(i, j, k);
		if (block.getMaterial() == Material.glass && getThrower() != null && !LOTREventHandler.isProtectedByBanner(worldObj, i, j, k, getThrower(), true))
		{
			worldObj.playAuxSFX(2001, i, j, k, Block.getIdFromBlock(block) + (worldObj.getBlockMetadata(i, j, k) << 12));
			worldObj.setBlockToAir(i, j, k);
			return true;
		}
		return false;
	}
	
	@Override
    protected float func_70182_d()
    {
		return 1.5F;
    }
	
	@Override
    protected float getGravityVelocity()
    {
        return 0.02F;
    }
}
