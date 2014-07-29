package lotr.common.entity.item;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityTraderRespawn extends Entity
{
	private static int MAX_SCALE = 40;
	
	private int timeUntilSpawn;
	private int prevBobbingTime;
	private int bobbingTime;
	
	private String traderClassID;
	private boolean traderHasHome;
	private int traderHomeX;
	private int traderHomeY;
	private int traderHomeZ;
	private float traderHomeRadius;
	private String traderLocationName;
	
	public LOTREntityTraderRespawn(World world)
	{
		super(world);
		setSize(0.75F, 0.75F);
	}
	
	@Override
    protected void entityInit()
	{
		dataWatcher.addObject(16, Integer.valueOf(0));
		dataWatcher.addObject(17, Byte.valueOf((byte)0));
		dataWatcher.addObject(18, "");
	}
	
	public int getScale()
	{
		return dataWatcher.getWatchableObjectInt(16);
	}
	
	public void setScale(int i)
	{
		dataWatcher.updateObject(16, Integer.valueOf(i));
	}
	
	public boolean isSpawnImminent()
	{
		return dataWatcher.getWatchableObjectByte(17) == (byte)1;
	}
	
	public void setSpawnImminent()
	{
		dataWatcher.updateObject(17, Byte.valueOf((byte)1));
	}
	
	public String getClientTraderString()
	{
		return dataWatcher.getWatchableObjectString(18);
	}
	
	public void setClientTraderString(String s)
	{
		dataWatcher.updateObject(18, s);
	}

	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Scale", getScale());
		nbt.setInteger("TimeUntilSpawn", timeUntilSpawn);
		
		nbt.setString("TraderClassID", traderClassID);
		nbt.setBoolean("TraderHasHome", traderHasHome);
		nbt.setInteger("TraderHomeX", traderHomeX);
		nbt.setInteger("TraderHomeY", traderHomeY);
		nbt.setInteger("TraderHomeZ", traderHomeZ);
		nbt.setFloat("TraderHomeRadius", traderHomeRadius);
		if (traderLocationName != null)
		{
			nbt.setString("TraderLocationName", traderLocationName);
		}
	}

	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
	{
		setScale(nbt.getInteger("Scale"));
		timeUntilSpawn = nbt.getInteger("TimeUntilSpawn");
		if (timeUntilSpawn <= 1200)
		{
			setSpawnImminent();
		}
		
		if (nbt.hasKey("TraderClassID", 3))
        {
        	int id = nbt.getInteger("TraderClassID");
        	traderClassID = LOTREntities.getStringFromID(id);
        }
		else
		{
			traderClassID = nbt.getString("TraderClassID");
		}
		
		traderHasHome = nbt.getBoolean("TraderHasHome");
		traderHomeX = nbt.getInteger("TraderHomeX");
		traderHomeY = nbt.getInteger("TraderHomeY");
		traderHomeZ = nbt.getInteger("TraderHomeZ");
		traderHomeRadius = nbt.getFloat("TraderHomeRadius");
		if (nbt.hasKey("TraderLocationName"))
		{
			traderLocationName = nbt.getString("TraderLocationName");
		}
	}
	
	public void copyTraderDataFrom(LOTREntityNPC entity)
	{
		traderClassID = LOTREntities.getStringFromClass(entity.getClass());
		traderHasHome = entity.hasHome();
		if (traderHasHome)
		{
			ChunkCoordinates home = entity.getHomePosition();
			traderHomeX = home.posX;
			traderHomeY = home.posY;
			traderHomeZ = home.posZ;
			traderHomeRadius = entity.func_110174_bM();
		}
		if (entity.getHasSpecificLocationName())
		{
			traderLocationName = entity.npcLocationName;
		}
	}
	
	public void onSpawn()
	{
		motionY = 0.25D;
		timeUntilSpawn = (15 + rand.nextInt(46)) * 1200;
	}
	
	public void onBreak()
	{
		worldObj.playSoundAtEntity(this, Blocks.glass.stepSound.getBreakSound(), (Blocks.glass.stepSound.getVolume() + 1.0F) / 2.0F, Blocks.glass.stepSound.getPitch() * 0.8F);
		worldObj.setEntityState(this, (byte)16);
		setDead();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte b)
	{
		if (b == (byte)16)
		{
			for (int l = 0; l < 16; l++)
			{
				worldObj.spawnParticle("iconcrack_" + Item.getIdFromItem(LOTRMod.silverCoin), posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0D, 0D, 0D);
			}
		}
		else
		{
			super.handleHealthUpdate(b);
		}
	}
	
	@Override
	public void onUpdate()
	{
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
		prevRotationYaw = rotationYaw;
		
		if (isSpawnImminent())
		{
			rotationYaw += 24F;
		}
		else
		{
			rotationYaw += 6F;
		}

		prevRotationYaw = MathHelper.wrapAngleTo180_float(prevRotationYaw);
		rotationYaw = MathHelper.wrapAngleTo180_float(rotationYaw);
		
		if (getScale() < MAX_SCALE)
		{
			if (!worldObj.isRemote)
			{
				setScale(getScale() + 1);
			}
			motionX = 0D;
			motionY *= 0.9D;
			motionZ = 0D;
		}
		else
		{
			motionX = 0D;
			motionY = 0D;
			motionZ = 0D;
		}
		
		moveEntity(motionX, motionY, motionZ);
		
		if (!worldObj.isRemote)
		{
			setClientTraderString(traderClassID);
			
			if (!isSpawnImminent() && timeUntilSpawn <= 1200)
			{
				setSpawnImminent();
			}
				
			if (timeUntilSpawn > 0)
			{
				timeUntilSpawn--;
			}
			else
			{
				boolean flag = false;
				Entity entity = EntityList.createEntityByName(LOTREntities.getFullEntityName(traderClassID), worldObj);
				if (entity != null && entity instanceof LOTREntityNPC)
				{
					LOTREntityNPC trader = (LOTREntityNPC)entity;
					trader.setLocationAndAngles(posX, posY, posZ, rand.nextFloat() * 360F, 0F);
					trader.spawnRidingHorse = false;
					trader.liftSpawnRestrictions = true;
					boundingBox.offset(0D, 100D, 0D);
					if (trader.getCanSpawnHere())
					{
						trader.liftSpawnRestrictions = false;
						trader.onSpawnWithEgg(null);
						if (traderHasHome)
						{
							trader.setHomeArea(traderHomeX, traderHomeY, traderHomeZ, Math.round(traderHomeRadius));
						}
						if (traderLocationName != null)
						{
							trader.setSpecificLocationName(traderLocationName);
						}
						flag = worldObj.spawnEntityInWorld(trader);
					}
					boundingBox.offset(0D, -100D, 0D);
				}
				
				if (flag)
				{
					playSound("random.pop", 1F, 0.5F + rand.nextFloat() * 0.5F);
					setDead();
				}
				else
				{
					timeUntilSpawn = 60;
					setLocationAndAngles(posX, posY + 1D, posZ, rotationYaw, rotationPitch);
				}
			}
		}
		else
		{
			if (isSpawnImminent())
			{
				prevBobbingTime = bobbingTime;
				bobbingTime++;
			}
		}
	}
	
	public float getScaleFloat(float tick)
	{
		float scale = (float)getScale();
		if (scale < (float)MAX_SCALE)
		{
			scale += tick;
		}
		return scale / (float)MAX_SCALE;
	}
	
	public float getBobbingOffset(float tick)
	{
		float f = bobbingTime - prevBobbingTime;
		f *= tick;
		return MathHelper.sin((prevBobbingTime + f) / 5F) * 0.25F;
	}
	
	@Override
	public boolean canBeCollidedWith()
    {
        return true;
    }
	
	@Override
	public void applyEntityCollision(Entity entity) {}
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		int entityID = LOTREntities.getIDFromString(getClientTraderString());
		if (entityID > 0)
		{
			return new ItemStack(LOTRMod.spawnEgg, 1, entityID);
		}
		return null;
    }
}
