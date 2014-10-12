package lotr.common.entity.animal;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.world.biome.LOTRBiomeGenLothlorien;
import lotr.common.world.biome.LOTRBiomeGenMirkwood;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTREntityButterfly extends EntityLiving implements LOTRAmbientCreature
{
	public int flapTime = 0;
	
	public enum ButterflyType
	{
		MIRKWOOD,
		LORIEN,
		COMMON_WHITE,
		COMMON_RED,
		COMMON_ORANGE,
		COMMON_YELLOW,
		COMMON_BLUE;
		
		public ResourceLocation texture;
		
		private ButterflyType()
		{
			texture = new ResourceLocation("lotr:mob/butterfly/" + name().toLowerCase() + ".png");
		}
	}
	
    private ChunkCoordinates currentFlightTarget;

    public LOTREntityButterfly(World world)
    {
        super(world);
        setSize(0.5F, 0.5F);
    }

	@Override
    public void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, Integer.valueOf(0));
        dataWatcher.addObject(17, Byte.valueOf((byte)0));
    }
	
	public ButterflyType getButterflyType()
	{
		int i = dataWatcher.getWatchableObjectInt(16);
		if (i < 0 || i >= ButterflyType.values().length)
		{
			i = 0;
		}
		return ButterflyType.values()[i];
	}
	
	public void setButterflyType(ButterflyType type)
	{
		setButterflyType(type.ordinal());
	}
	
	public void setButterflyType(int i)
	{
		dataWatcher.updateObject(16, Integer.valueOf(i));
	}
	
	public boolean isButterflyStill()
	{
		return dataWatcher.getWatchableObjectByte(17) == (byte)1;
	}
	
	public void setButterflyStill(boolean flag)
	{
		dataWatcher.updateObject(17, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(2D);
    }
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		
		if (biome instanceof LOTRBiomeGenMirkwood)
		{
			setButterflyType(ButterflyType.MIRKWOOD);
		}
		else if (biome instanceof LOTRBiomeGenLothlorien)
		{
			setButterflyType(ButterflyType.LORIEN);
		}
		else
		{
			int l = rand.nextInt(5);
			if (l == 0)
			{
				setButterflyType(ButterflyType.COMMON_WHITE);
			}
			else if (l == 1)
			{
				setButterflyType(ButterflyType.COMMON_RED);
			}
			else if (l == 2)
			{
				setButterflyType(ButterflyType.COMMON_ORANGE);
			}
			else if (l == 3)
			{
				setButterflyType(ButterflyType.COMMON_YELLOW);
			}
			else if (l == 4)
			{
				setButterflyType(ButterflyType.COMMON_BLUE);
			}
		}
		
		return data;
	}

	@Override
    public boolean canBePushed()
    {
        return false;
    }

	@Override
    protected void collideWithEntity(Entity entity) {}

	@Override
    protected void collideWithNearbyEntities() {}

	@Override
    protected boolean isAIEnabled()
    {
        return true;
    }

	@Override
    public void onUpdate()
    {
        super.onUpdate();

        if (isButterflyStill())
        {
            motionX = motionY = motionZ = 0;
			posY = (double)MathHelper.floor_double(posY);
			
			if (worldObj.isRemote)
			{
				if (rand.nextInt(200) == 0)
				{
					flapTime = 40;
				}
				if (flapTime > 0)
				{
					flapTime--;
				}
			}
        }
        else
        {
            motionY *= 0.6D;
			
			if (worldObj.isRemote)
			{
				flapTime = 0;
			}
			
			if (getButterflyType() == ButterflyType.LORIEN && rand.nextInt(4) == 0)
			{
				double d = posX;
				double d1 = posY;
				double d2 = posZ;
				double d3 = motionX * -0.2D;
				double d4 = motionY * -0.2D;
				double d5 = motionZ * -0.2D;
				LOTRMod.proxy.spawnParticle("lorienButterfly", d, d1, d2, d3, d4, d5);
			}
        }
    }

	@Override
    protected void updateAITasks()
    {
        super.updateAITasks();

        if (isButterflyStill())
        {
        	int i = MathHelper.floor_double(posX);
        	int j = (int)posY - 1;
        	int k = MathHelper.floor_double(posZ);
            if (!worldObj.getBlock(i, j, k).isSideSolid(worldObj, i, j, k, ForgeDirection.UP))
            {
                setButterflyStill(false);
            }
            else
            {
                if (rand.nextInt(400) == 0 || worldObj.getClosestPlayerToEntity(this, 3D) != null)
                {
                    setButterflyStill(false);
                }
            }
        }
        else
        {
            if (currentFlightTarget != null && (!worldObj.isAirBlock(currentFlightTarget.posX, currentFlightTarget.posY, currentFlightTarget.posZ) || currentFlightTarget.posY < 1))
            {
                currentFlightTarget = null;
            }

            if (currentFlightTarget == null || rand.nextInt(30) == 0 || currentFlightTarget.getDistanceSquared((int)posX, (int)posY, (int)posZ) < 4F)
            {
                currentFlightTarget = new ChunkCoordinates((int)posX + rand.nextInt(7) - rand.nextInt(7), (int)posY + rand.nextInt(6) - 2, (int)posZ + rand.nextInt(7) - rand.nextInt(7));
            }

            double d0 = (double)currentFlightTarget.posX + 0.5D - posX;
            double d1 = (double)currentFlightTarget.posY + 0.1D - posY;
            double d2 = (double)currentFlightTarget.posZ + 0.5D - posZ;
            motionX += (Math.signum(d0) * 0.5D - motionX) * 0.1D;
            motionY += (Math.signum(d1) * 0.7D - motionY) * 0.1D;
            motionZ += (Math.signum(d2) * 0.5D - motionZ) * 0.1D;
            float f = (float)(Math.atan2(motionZ, motionX) * 180D / Math.PI) - 90F;
            float f1 = MathHelper.wrapAngleTo180_float(f - rotationYaw);
            moveForward = 0.5F;
            rotationYaw += f1;

            if (rand.nextInt(150) == 0 && worldObj.getBlock(MathHelper.floor_double(posX), (int)posY - 1, MathHelper.floor_double(posZ)).isNormalCube())
            {
                setButterflyStill(true);
            }
        }
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected void fall(float f) {}

    @Override
    protected void updateFallState(double d, boolean flag) {}

    @Override
    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float f)
    {
        boolean flag = super.attackEntityFrom(damagesource, f);
		if (flag && !worldObj.isRemote && isButterflyStill())
		{
			setButterflyStill(false);
		}
		return flag;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
		setButterflyType(nbt.getInteger("ButterflyType"));
		setButterflyStill(nbt.getBoolean("ButterflyStill"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("ButterflyType", getButterflyType().ordinal());
        nbt.setBoolean("ButterflyStill", isButterflyStill());
    }
    
    @Override
    protected boolean canDespawn()
    {
        return true;
    }

    @Override
    public boolean getCanSpawnHere()
    {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);

        if (j < 62)
        {
            return false;
        }
		Block l = worldObj.getBlock(i, j - 1, k);
		if (l == Blocks.grass)
		{
            int light = worldObj.getBlockLightValue(i, j, k);
			return light < rand.nextInt(8) ? false : super.getCanSpawnHere();
        }
		return false;
    }
	
	@Override
    public boolean allowLeashing()
    {
        return false;
    }

    @Override
    protected boolean interact(EntityPlayer entityplayer)
    {
        return false;
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
    }
}
