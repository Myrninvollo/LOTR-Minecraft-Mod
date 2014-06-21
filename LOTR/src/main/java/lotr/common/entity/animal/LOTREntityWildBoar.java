package lotr.common.entity.animal;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIHiredHorseRemainStill;
import lotr.common.entity.ai.LOTREntityAIHorseFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHorseMoveToRiderTarget;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRNPCMount;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityWildBoar extends EntityPig implements LOTRNPCMount
{
	private EntityAIBase attackAI = new LOTREntityAIAttackOnCollide(this, 1.25D, true, 0.8F);
	private EntityAIBase panicAI = new EntityAIPanic(this, 1.5D);
	private boolean prevIsChild = true;
	
	public LOTREntityWildBoar(World world)
	{
		super(world);
		tasks.taskEntries.clear();
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(0, new LOTREntityAIHiredHorseRemainStill(this));
		tasks.addTask(0, new LOTREntityAIHorseMoveToRiderTarget(this));
		tasks.addTask(0, new LOTREntityAIHorseFollowHiringPlayer(this));
		tasks.addTask(1, panicAI);
        tasks.addTask(2, new EntityAIMate(this, 1D));
        tasks.addTask(3, new EntityAITempt(this, 1.2D, Items.carrot, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6F));
        tasks.addTask(7, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(20, Byte.valueOf((byte)0));
		dataWatcher.addObject(21, Byte.valueOf((byte)0));
	}
	
	@Override
	public boolean getBelongsToNPC()
	{
		return dataWatcher.getWatchableObjectByte(20) == (byte)1;
	}
	
	@Override
	public void setBelongsToNPC(boolean flag)
	{
		dataWatcher.updateObject(20, Byte.valueOf(flag ? (byte)1 : (byte)0));
		if (flag)
		{
			setSaddled(true);
			if (getGrowingAge() < 0)
			{
				setGrowingAge(0);
			}
		}
	}
	
	public boolean isBoarEnraged()
	{
		return dataWatcher.getWatchableObjectByte(21) == (byte)1;
	}
	
	public void setBoarEnraged(boolean flag)
	{
		dataWatcher.updateObject(21, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}
	
	@Override
	public void setNavigatorRangeFrom(LOTREntityNPC npc)
	{
		double d = npc.getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(d);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16D);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
        getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3D);
	}
	
	@Override
    public EntityPig createChild(EntityAgeable entity)
    {
        return new LOTREntityWildBoar(worldObj);
    }
	
	@Override
	public void onLivingUpdate()
	{
		if (!worldObj.isRemote)
		{
			boolean isChild = isChild();
			if (isChild != prevIsChild)
			{
				if (isChild)
				{
					tasks.removeTask(attackAI);
					tasks.addTask(1, panicAI);
				}
				else
				{
					tasks.removeTask(panicAI);
					tasks.addTask(1, attackAI);
				}
			}
		}

		super.onLivingUpdate();
		
        if (!worldObj.isRemote)
        {
            if (rand.nextInt(900) == 0 && isEntityAlive())
            {
                heal(1F);
            }

			if (getAttackTarget() != null)
			{
				EntityLivingBase entity = getAttackTarget();
				if (!entity.isEntityAlive() || (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode))
				{
					setAttackTarget(null);
				}
			}
			
			if (riddenByEntity instanceof EntityLiving)
			{
				EntityLivingBase target = ((EntityLiving)riddenByEntity).getAttackTarget();
				setAttackTarget(target);
			}
			else if (riddenByEntity instanceof EntityPlayer)
			{
				setAttackTarget(null);
			}
			
			setBoarEnraged(getAttackTarget() != null);
		}
		
		prevIsChild = isChild();
	}
	
	@Override
	public boolean interact(EntityPlayer entityplayer)
	{
		if (isBoarEnraged())
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
	public boolean getSaddled()
	{
		if (getBelongsToNPC())
		{
			return true;
		}
		return super.getSaddled();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("BelongsToNPC", getBelongsToNPC());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setBelongsToNPC(nbt.getBoolean("BelongsToNPC"));
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
    public void moveEntityWithHeading(float f, float f1)
    {
        if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer && getSaddled())
        {
            prevRotationYaw = rotationYaw = riddenByEntity.rotationYaw;
            rotationPitch = riddenByEntity.rotationPitch * 0.5F;
            setRotation(rotationYaw, rotationPitch);
            rotationYawHead = renderYawOffset = rotationYaw;
            f = ((EntityLivingBase)riddenByEntity).moveStrafing * 0.5F;
            f1 = ((EntityLivingBase)riddenByEntity).moveForward;

            if (f1 <= 0F)
            {
                f1 *= 0.25F;
            }

            stepHeight = 1F;
            jumpMovementFactor = getAIMoveSpeed() * 0.1F;

            if (!worldObj.isRemote)
            {
                setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() * 0.75F);
                super.moveEntityWithHeading(f, f1);
            }

            prevLimbSwingAmount = limbSwingAmount;
            double d0 = posX - prevPosX;
            double d1 = posZ - prevPosZ;
            float f4 = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4F;

            if (f4 > 1F)
            {
                f4 = 1F;
            }

            limbSwingAmount += (f4 - limbSwingAmount) * 0.4F;
            limbSwing += limbSwingAmount;
        }
        else
        {
            stepHeight = 0.5F;
            jumpMovementFactor = 0.02F;
            super.moveEntityWithHeading(f, f1);
        }
    }

	@Override
    public boolean attackEntityAsMob(Entity entity)
    {
        float f = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        return flag;
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f)
	{
		if (riddenByEntity != null && damagesource.getEntity() == riddenByEntity)
		{
			return false;
		}
		return super.attackEntityFrom(damagesource, f);
	}

	@Override
    protected void dropFewItems(boolean flag, int i)
    {
        int j = rand.nextInt(3) + 1 + rand.nextInt(1 + i);

        for (int k = 0; k < j; k++)
        {
            if (isBurning())
            {
                dropItem(Items.cooked_porkchop, 1);
            }
            else
            {
                dropItem(Items.porkchop, 1);
            }
        }
    }
	
	@Override
	public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        
        if (!worldObj.isRemote && getSaddled() && !getBelongsToNPC())
        {
			dropItem(Items.saddle, 1);
			setSaddled(false);
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
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
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
