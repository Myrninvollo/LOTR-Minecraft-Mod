package lotr.common.entity.animal;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

public class LOTREntityBird extends EntityLiving implements LOTRAmbientCreature
{
	public int flapTime = 0;
	
	public enum BirdType
	{
		BLUE,
		RED,
		WHITE,
		BROWN,
		BLACK;
		
		public ResourceLocation texture;
		
		private BirdType()
		{
			texture = new ResourceLocation("lotr:mob/bird/" + name().toLowerCase() + ".png");
		}
	}
	
    private ChunkCoordinates currentFlightTarget;

    public LOTREntityBird(World world)
    {
        super(world);
        setSize(0.5F, 0.5F);
        tasks.addTask(0, new EntityAIWatchClosest(this, EntityPlayer.class, 12F, 0.05F));
        tasks.addTask(1, new EntityAIWatchClosest(this, EntityLiving.class, 12F, 0.1F));
        tasks.addTask(2, new EntityAILookIdle(this));
    }

	@Override
    public void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, Integer.valueOf(0));
        dataWatcher.addObject(17, Byte.valueOf((byte)1));
    }
	
	public BirdType getBirdType()
	{
		int i = dataWatcher.getWatchableObjectInt(16);
		if (i < 0 || i >= BirdType.values().length)
		{
			i = 0;
		}
		return BirdType.values()[i];
	}
	
	public void setBirdType(BirdType type)
	{
		setBirdType(type.ordinal());
	}
	
	public void setBirdType(int i)
	{
		dataWatcher.updateObject(16, Integer.valueOf(i));
	}
	
	public boolean isBirdStill()
	{
		return dataWatcher.getWatchableObjectByte(17) == (byte)1;
	}
	
	public void setBirdStill(boolean flag)
	{
		dataWatcher.updateObject(17, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4D);
    }
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		
		int l = rand.nextInt(5);
		if (l == 0)
		{
			setBirdType(BirdType.BLUE);
		}
		else if (l == 1)
		{
			setBirdType(BirdType.RED);
		}
		else if (l == 2)
		{
			setBirdType(BirdType.WHITE);
		}
		else if (l == 3)
		{
			setBirdType(BirdType.BROWN);
		}
		else if (l == 4)
		{
			setBirdType(BirdType.BLACK);
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

        if (isBirdStill())
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
        }
    }

	@Override
    protected void updateAITasks()
    {
        super.updateAITasks();

        if (isBirdStill())
        {
        	int i = MathHelper.floor_double(posX);
        	int j = (int)posY - 1;
        	int k = MathHelper.floor_double(posZ);
            if (!worldObj.getBlock(i, j, k).isSideSolid(worldObj, i, j, k, ForgeDirection.UP))
            {
                setBirdStill(false);
            }
            else
            {
                if (rand.nextInt(400) == 0 || worldObj.getClosestPlayerToEntity(this, 6D) != null)
                {
                    setBirdStill(false);
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
                currentFlightTarget = new ChunkCoordinates((int)posX + rand.nextInt(16) - rand.nextInt(16), (int)posY + rand.nextInt(6) - 2, (int)posZ + rand.nextInt(16) - rand.nextInt(16));
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

            if (rand.nextInt(200) == 0 && worldObj.getBlock(MathHelper.floor_double(posX), (int)posY - 1, MathHelper.floor_double(posZ)).isNormalCube())
            {
                setBirdStill(true);
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
		if (flag && !worldObj.isRemote && isBirdStill())
		{
			setBirdStill(false);
		}
		return flag;
    }
    
    @Override
    protected void dropFewItems(boolean flag, int i)
    {
		int k = rand.nextInt(3) + rand.nextInt(i + 1);
		for (int j = 0; j < k; j++)
		{
			dropItem(Items.feather, 1);
		}
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
		setBirdType(nbt.getInteger("BirdType"));
		setBirdStill(nbt.getBoolean("BirdStill"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("BirdType", getBirdType().ordinal());
        nbt.setBoolean("BirdStill", isBirdStill());
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
			return light < 8 ? false : super.getCanSpawnHere();
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
	public int getTalkInterval()
    {
    	return 40;
    }
    
    @Override
    protected String getLivingSound()
    {
        return "lotr:bird.say";
    }

   @Override
    protected String getHurtSound()
    {
        return "lotr:bird.hurt";
    }

    @Override
    protected String getDeathSound()
    {
        return "lotr:bird.hurt";
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
    }
}
