package lotr.common.entity.animal;

import lotr.common.LOTRMod;
import lotr.common.LOTRReflection;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.LOTRMountFunctions;
import lotr.common.entity.ai.LOTREntityAIHiredHorseRemainStill;
import lotr.common.entity.ai.LOTREntityAIHorseFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHorseMoveToRiderTarget;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRNPCMount;
import lotr.common.world.biome.LOTRBiomeGenRohan;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityHorse extends EntityHorse implements LOTRNPCMount
{
	private boolean isMoving;
	
    public LOTREntityHorse(World world)
    {
        super(world);
		tasks.addTask(0, new LOTREntityAIHiredHorseRemainStill(this));
		tasks.addTask(0, new LOTREntityAIHorseMoveToRiderTarget(this));
		tasks.addTask(0, new LOTREntityAIHorseFollowHiringPlayer(this));
    }
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(25, Byte.valueOf((byte)0));
		dataWatcher.addObject(26, Byte.valueOf((byte)1));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
    }

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		if (!worldObj.isRemote)
		{
			data = super.onSpawnWithEgg(data);
			int i = MathHelper.floor_double(posX);
			int k = MathHelper.floor_double(posZ);
			BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
			
			if (biome instanceof LOTRBiomeGenRohan)
			{
				double maxHealth = getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
				maxHealth = (double)(maxHealth * (1F + rand.nextFloat() * 0.5F));
				getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
				
				setHealth(getMaxHealth());

				double movementSpeed = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
				movementSpeed = (double)(movementSpeed * (1F + rand.nextFloat() * 0.5F));
				getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(movementSpeed);

				double jumpStrength = getEntityAttribute(LOTRReflection.getHorseJumpStrength()).getAttributeValue();
				jumpStrength = (double)(jumpStrength * (1F + rand.nextFloat() * 0.5F));
				getEntityAttribute(LOTRReflection.getHorseJumpStrength()).setBaseValue(jumpStrength);
			}
			
			return data;
		}
		else
		{
			int i;
			int j = rand.nextInt(7);
			int k = rand.nextInt(5);
			i = j | k << 8;
			setHorseVariant(i);
			return data;
		}
	}
	
	public boolean getBelongsToNPC()
	{
		return dataWatcher.getWatchableObjectByte(25) == (byte)1;
	}
	
	public void setBelongsToNPC(boolean flag)
	{
		dataWatcher.updateObject(25, Byte.valueOf(flag ? (byte)1 : (byte)0));
		if (flag)
		{
			setHorseTamed(true);
			setHorseSaddled(true);
			if (getGrowingAge() < 0)
			{
				setGrowingAge(0);
			}
			if (getHorseType() != 0 && !(this instanceof LOTREntityShirePony))
			{
				setHorseType(0);
			}
		}
	}
	
	public boolean getMountable()
	{
		return dataWatcher.getWatchableObjectByte(26) == (byte)1;
	}
	
	public void setMountable(boolean flag)
	{
		dataWatcher.updateObject(26, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}
	
	@Override
	public boolean isMountSaddled()
	{
		return isHorseSaddled();
	}
	
	@Override
	public boolean isHorseSaddled()
	{
		return (!isMoving || !getBelongsToNPC()) && super.isHorseSaddled();
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!worldObj.isRemote && riddenByEntity instanceof EntityPlayer && isInWater())
		{
			if (rand.nextFloat() < 0.6F)
	        {
				motionY = 0.2D;
				isAirBorne = true;
	        }
		}
	}
	
	@Override
    protected boolean isMovementBlocked()
    {
        isMoving = true;
		boolean flag = super.isMovementBlocked();
		isMoving = false;
		return flag;
    }
	
	@Override
	public void moveEntityWithHeading(float f, float f1)
	{
		isMoving = true;
		super.moveEntityWithHeading(f, f1);
		isMoving = false;
	}

	@Override
    public void super_moveEntityWithHeading(float strafe, float forward)
    {
		super.moveEntityWithHeading(strafe, forward);
    }
	
	@Override
	public float getBlockPathWeight(int i, int j, int k)
	{
		if (getBelongsToNPC() && riddenByEntity instanceof LOTREntityNPC)
		{
			return ((LOTREntityNPC)riddenByEntity).getBlockPathWeight(i, j, k);
		}
		return super.getBlockPathWeight(i, j, k);
	}
	
	@Override
    public double getMountedYOffset()
    {
		if (riddenByEntity instanceof LOTREntityNPC)
		{
			return (double)height * 0.5D;
		}
		return super.getMountedYOffset();
    }
	
	@Override
    public EntityAgeable createChild(EntityAgeable entityageable)
	{
		EntityHorse superHorse = (EntityHorse)super.createChild(entityageable);
		LOTREntityHorse horse = (LOTREntityHorse)LOTREntities.createEntityByClass(getClass(), worldObj);
		horse.setHorseType(superHorse.getHorseType());
		horse.setHorseVariant(superHorse.getHorseVariant());
        double maxHealth = superHorse.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue();
        horse.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
        double jumpStrength = superHorse.getEntityAttribute(LOTRReflection.getHorseJumpStrength()).getBaseValue();
        horse.getEntityAttribute(LOTRReflection.getHorseJumpStrength()).setBaseValue(jumpStrength);
		double moveSpeed = superHorse.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue();
        horse.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(moveSpeed);
        return horse;
    }
	
	@Override
	public boolean interact(EntityPlayer entityplayer)
	{
		if (!getMountable())
		{
			return false;
		}
		if (getBelongsToNPC())
		{
			if (riddenByEntity == null)
			{
				if (!worldObj.isRemote)
				{
					entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.mountOwnedByNPC"));
				}
				return true;
			}
			return false;
		}
		return super.interact(entityplayer);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("BelongsToNPC", getBelongsToNPC());
		nbt.setBoolean("Mountable", getMountable());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setBelongsToNPC(nbt.getBoolean("BelongsToNPC"));
		if (nbt.hasKey("Mountable"))
		{
			setMountable(nbt.getBoolean("Mountable"));
		}
	}
	
	@Override
	public boolean canDespawn()
	{
		return getBelongsToNPC() && riddenByEntity == null;
	}

	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		int id = LOTREntities.getEntityID(this);
		if (id > 0 && LOTREntities.creatures.containsKey(id))
		{
			return new ItemStack(LOTRMod.spawnEgg, 1, id);
		}
		return null;
    }
	
	@Override
	public boolean allowLeashing()
	{
		if (getBelongsToNPC())
		{
			return false;
		}
		return super.allowLeashing();
	}
	
	@Override
	public boolean shouldDismountInWater(Entity rider)
	{
		return false;
    }
}
