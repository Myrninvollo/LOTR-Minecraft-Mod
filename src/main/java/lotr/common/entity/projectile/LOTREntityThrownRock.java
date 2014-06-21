package lotr.common.entity.projectile;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityMountainTroll;
import lotr.common.entity.npc.LOTREntityTroll;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityThrownRock extends EntityThrowable
{
	private int rockRotation;
	private float damage;
	
	public LOTREntityThrownRock(World world)
	{
		super(world);
		setSize(4F, 4F);
	}
	
	public LOTREntityThrownRock(World world, EntityLivingBase entityliving)
	{
		super(world, entityliving);
		setSize(4F, 4F);
	}
	
	public LOTREntityThrownRock(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
		setSize(4F, 4F);
	}
	
	public void setDamage(float f)
	{
		damage = f;
	}
	
	@Override
	public void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte)0));
	}
	
	public boolean getSpawnsTroll()
	{
		return dataWatcher.getWatchableObjectByte(16) == (byte)1;
	}
	
	public void setSpawnsTroll(boolean flag)
	{
		dataWatcher.updateObject(16, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (!inGround)
		{
			rockRotation++;
			if (rockRotation > 60)
			{
				rockRotation = 0;
			}
			rotationPitch = ((float)rockRotation / 60F) * 360F;
			while (rotationPitch - prevRotationPitch < -180F)
			{
				prevRotationPitch -= 360F;
			}
			while (rotationPitch - prevRotationPitch >= 180F)
			{
				prevRotationPitch += 360F;
			}
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setFloat("RockDamage", damage);
		nbt.setBoolean("Troll", getSpawnsTroll());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setDamage(nbt.getFloat("RockDamage"));
		setSpawnsTroll(nbt.getBoolean("Troll"));
	}
	
	@Override
	protected void onImpact(MovingObjectPosition m)
	{
		if (!worldObj.isRemote)
		{
			boolean flag = false;
			
			if (m.entityHit != null)
			{
				if (m.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), damage))
				{
					flag = true;
				}
			}
			
			if (m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
			{
				flag = true;
			}
			
			if (flag)
			{
				if (getSpawnsTroll())
				{
					LOTREntityTroll troll = new LOTREntityTroll(worldObj);
					if (rand.nextInt(3) == 0)
					{
						troll = new LOTREntityMountainTroll(worldObj);
					}
					troll.setLocationAndAngles(posX, posY, posZ, rand.nextFloat() * 360F, 0F);
					troll.onSpawnWithEgg(null);
					worldObj.spawnEntityInWorld(troll);
				}
				
				worldObj.setEntityState(this, (byte)15);				
				int drops = 1 + rand.nextInt(3);
				for (int l = 0; l < drops; l++)
				{
					dropItem(Item.getItemFromBlock(Blocks.cobblestone), 1);
				}
				playSound("lotr:troll.rockSmash", 2F, (1F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
				setDead();
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte b)
	{
		if (b == 15)
		{
			for (int l = 0; l < 32; l++)
			{
				LOTRMod.proxy.spawnParticle("largeStone", posX + rand.nextGaussian() * (double)width, posY + rand.nextDouble() * (double)height, posZ + rand.nextGaussian() * (double)width, 0D, 0D, 0D);
			}
		}
		else
		{
			super.handleHealthUpdate(b);
		}
	}
	
	@Override
    protected float func_70182_d()
    {
        return 0.75F;
    }
	
	@Override
    protected float getGravityVelocity()
    {
        return 0.1F;
    }
}
