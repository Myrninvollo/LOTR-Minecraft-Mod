package lotr.common.entity.item;

import java.util.List;

import lotr.common.LOTRMod;
import lotr.common.world.LOTRTeleporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class LOTREntityPortal extends Entity
{
	public static int MAX_SCALE = 120;
	
	private float prevPortalRotation;
	private float portalRotation;
	
	public LOTREntityPortal(World world)
	{
		super(world);
		setSize(3F, 1.5F);
	}
	
	@Override
    protected void entityInit()
	{
		dataWatcher.addObject(10, Integer.valueOf(0));
	}
	
	public int getScale()
	{
		return dataWatcher.getWatchableObjectInt(10);
	}
	
	public void setScale(int i)
	{
		dataWatcher.updateObject(10, Integer.valueOf(i));
	}

	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Scale", getScale());
	}

	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
	{
		setScale(nbt.getInteger("Scale"));
	}
	
	public float getPortalRotation(float f)
	{
		return prevPortalRotation + (portalRotation - prevPortalRotation) * f;
	}
	
	@Override
	public void onUpdate()
	{
		prevPortalRotation = portalRotation;
		portalRotation += 4F;
		while (portalRotation - prevPortalRotation < -180F)
		{
			prevPortalRotation -= 360F;
		}
		while (portalRotation - prevPortalRotation >= 180F)
		{
			prevPortalRotation += 360F;
		}
			
		if (!worldObj.isRemote && dimension != 0 && dimension != LOTRMod.idDimension)
		{
			setDead();
		}
		
		if (!worldObj.isRemote && getScale() < MAX_SCALE)
		{
			setScale(getScale() + 1);
		}
		
        if (getScale() >= MAX_SCALE)
        {
			List players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, boundingBox.expand(8D, 8D, 8D));
			for (int i = 0; i < players.size(); i++)
			{
				EntityPlayer entityplayer = (EntityPlayer)players.get(i);
				if (boundingBox.intersectsWith(entityplayer.boundingBox) && entityplayer.ridingEntity == null && entityplayer.riddenByEntity == null)
				{
					LOTRMod.proxy.setInPortal(entityplayer);
				}
			}
			
			List entities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(8D, 8D, 8D));
			for (int i = 0; i < entities.size(); i++)
			{
				Entity entity = (Entity)entities.get(i);
				if (!(entity instanceof EntityPlayer) && boundingBox.intersectsWith(entity.boundingBox) && entity.ridingEntity == null && entity.riddenByEntity == null && entity.timeUntilPortal == 0)
				{
					transferEntity(entity);
				}
			}
			
			if (rand.nextInt(50) == 0)
			{
				worldObj.playSoundAtEntity(this, "portal.portal", 0.5F, rand.nextFloat() * 0.4F + 0.8F);
			}
			
			for (int i = 0; i < 2; i++)
			{
				double d = posX - 3D + (double)(rand.nextFloat() * 6F);
				double d1 = posY - 0.75D + (double)(rand.nextFloat() * 3F);
				double d2 = posZ - 3D + (double)(rand.nextFloat() * 6F);
				double d3 = (posX - d) / 8D;
				double d4 = ((posY + 1.5D) - d1) / 8D;
				double d5 = (posZ - d2) / 8D;
				double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
				double d7 = 1.0D - d6;
				double d8 = 0D;
				double d9 = 0D;
				double d10 = 0D;
				if (d7 > 0.0D)
				{
					d7 *= d7;
					d8 += d3 / d6 * d7 * 0.2D;
					d9 += d4 / d6 * d7 * 0.2D;
					d10 += d5 / d6 * d7 * 0.2D;
				}
				worldObj.spawnParticle("flame", d, d1, d2, d8, d9, d10);
			}
        }
	}
	
	@Override
    protected boolean canTriggerWalking()
    {
        return false;
    }
	
	@Override
    public boolean isEntityInvulnerable()
    {
        return true;
    }
	
	@Override
    public boolean canBePushed()
    {
        return false;
    }
	
	@Override
    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }
	
	private void transferEntity(Entity entity)
	{
		if (!worldObj.isRemote)
		{
			int dimension = 0;
			if (entity.dimension == 0)
			{
				dimension = LOTRMod.idDimension;
			}
			else if (entity.dimension == LOTRMod.idDimension)
			{
				dimension = 0;
			}
			
			LOTRMod.transferEntityToDimension(entity, dimension, new LOTRTeleporter(DimensionManager.getWorld(dimension)));
		}
	}
}
