package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.LOTRMountFunctions;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtByTarget;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtTarget;
import lotr.common.entity.ai.LOTREntityAINearestAttackableTargetWarg;
import lotr.common.entity.animal.LOTREntityRabbit;
import lotr.common.item.LOTRItemWargArmor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class LOTREntityWarg extends LOTREntityNPC implements LOTRNPCMount
{
	private int eatingTick;
	
	public LOTREntityWarg(World world)
	{
		super(world);
		setSize(1.6F, 1.4F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(2, getWargAttackAI());
		tasks.addTask(3, new LOTREntityAIFollowHiringPlayer(this));
        tasks.addTask(4, new EntityAIWander(this, 1D));
        tasks.addTask(5, new EntityAIWatchClosest2(this, EntityPlayer.class, 12F, 0.05F));
        tasks.addTask(5, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 8F, 0.05F));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityLiving.class, 12F, 0.02F));
        tasks.addTask(7, new EntityAILookIdle(this));
        targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
        targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
		addTargetTasks(4, LOTREntityAINearestAttackableTargetWarg.class);
		targetTasks.addTask(5, new LOTREntityAINearestAttackableTargetWarg(this, LOTREntityRabbit.class, 500, false));
		isImmuneToFrost = true;
		spawnsInDarkness = true;
	}
	
	public EntityAIBase getWargAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.8D, false, 0.7F);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(17, Byte.valueOf((byte)0));
		dataWatcher.addObject(18, Byte.valueOf((byte)0));
		
		if (rand.nextInt(500) == 0)
		{
			setWargType(3);
		}
		else if (rand.nextInt(20) == 0)
		{
			setWargType(2);
		}
		else if (rand.nextInt(3) == 0)
		{
			setWargType(1);
		}
		else
		{
			setWargType(0);
		}
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)(18 + rand.nextInt(13)));
        getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(32D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.22D);
        getEntityAttribute(npcAttackDamage).setBaseValue(3D + (double)rand.nextInt(3));
    }
	
	@Override
    public boolean isMountSaddled()
    {
        return (dataWatcher.getWatchableObjectByte(17) & 1) != 0;
    }

    public void setSaddled(boolean flag)
    {
    	dataWatcher.updateObject(17, Byte.valueOf(flag ? (byte)1 : (byte)0));
    }
	
	public int getWargType()
	{
		return dataWatcher.getWatchableObjectByte(18);
	}
	
	public void setWargType(int i)
	{
		dataWatcher.updateObject(18, Byte.valueOf((byte)i));
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public IEntityLivingData initCreatureForHire(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		return data;
	}
	
	public abstract LOTREntityNPC createWargRider();
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		if (!worldObj.isRemote && canWargBeRidden() && rand.nextInt(3) == 0)
		{
			LOTREntityNPC rider = createWargRider();
			rider.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0F);
			rider.onSpawnWithEgg(null);
			rider.isNPCPersistent = isNPCPersistent;
			worldObj.spawnEntityInWorld(rider);
			rider.mountEntity(this);
		}
		return data;
	}
	
	public boolean canWargBeRidden()
	{
		return true;
	}
	
	@Override
    public double getMountedYOffset()
    {
		if (riddenByEntity instanceof EntityPlayer)
		{
			return (double)height * 0.95D;
		}
		else if (riddenByEntity instanceof LOTREntityNPC)
		{
			return (double)height * 0.6D;
		}
		return super.getMountedYOffset();
    }
	
	@Override
	public boolean getBelongsToNPC()
	{
		return false;
	}
	
	@Override
	public void setBelongsToNPC(boolean flag) {}
	
	@Override
    public void moveEntityWithHeading(float strafe, float forward)
    {
		LOTRMountFunctions.move(this, strafe, forward);
    }
	
	@Override
    public void super_moveEntityWithHeading(float strafe, float forward)
    {
		super.moveEntityWithHeading(strafe, forward);
    }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Saddled", isMountSaddled());
        nbt.setByte("WargType", (byte)getWargType());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        setSaddled(nbt.getBoolean("Saddled"));
		setWargType(nbt.getByte("WargType"));
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		LOTRMountFunctions.update(this);
		
        if (!worldObj.isRemote)
        {
			if (riddenByEntity instanceof EntityPlayer && LOTRLevelData.getData((EntityPlayer)riddenByEntity).getAlignment(getFaction()) < LOTRAlignmentValues.WARG_RIDE)
			{
				riddenByEntity.mountEntity(null);
			}
		}
		
		if (eatingTick > 0)
		{
			if (eatingTick % 4 == 0)
			{
				worldObj.playSoundAtEntity(this, "random.eat", 0.5F + 0.5F * (float)rand.nextInt(2), 0.4F + (rand.nextFloat() * 0.2F));
			}
			eatingTick--;
		}
	}
	
	@Override
	public boolean canDespawn()
	{
		return !isMountSaddled();
	}
	
	@Override
    public boolean interact(EntityPlayer entityplayer)
    {
		if (worldObj.isRemote || hiredNPCInfo.isActive)
		{
			return false;
		}
		
		if (LOTRMountFunctions.interact(this, entityplayer))
		{
			return true;
		}
		
		if (getAttackTarget() != entityplayer)
		{
			boolean flag = false;
			boolean hasRequiredAlignment = LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.WARG_RIDE;
			
			ItemStack itemstack = entityplayer.inventory.getCurrentItem();
			if (!flag && itemstack != null && itemstack.getItem() instanceof ItemFood && ((ItemFood)itemstack.getItem()).isWolfsFavoriteMeat() && isMountSaddled() && getHealth() < getMaxHealth())
			{
				if (hasRequiredAlignment)
				{
					if (!entityplayer.capabilities.isCreativeMode)
					{
						itemstack.stackSize--;
						if (itemstack.stackSize == 0)
						{
							entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
						}
					}
					heal(((ItemFood)itemstack.getItem()).func_150905_g(itemstack));
					eatingTick = 20;
					return true;
				}
				else
				{
					flag = true;
				}
			}
			
			if (!flag && itemstack != null && itemstack.getItem() instanceof LOTRItemWargArmor && isMountSaddled())
			{
				int slot = 4 - ((LOTRItemWargArmor)itemstack.getItem()).armorType;
				if (getEquipmentInSlot(slot) == null)
				{
					if (hasRequiredAlignment)
					{
						setCurrentItemOrArmor(slot, itemstack.copy());
						if (!entityplayer.capabilities.isCreativeMode)
						{
							entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
						}
						playSound("mob.horse.armor", 0.5F, 1F);
						return true;
					}
					else
					{
						flag = true;
					}
				}
			}
			
			if (!flag && !isMountSaddled() && canWargBeRidden() && riddenByEntity == null && itemstack != null && itemstack.getItem() == Items.saddle)
			{
				if (hasRequiredAlignment)
				{
					if (!entityplayer.capabilities.isCreativeMode)
					{
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
					}
					setSaddled(true);
					playSound("mob.horse.leather", 0.5F, 1F);
					setAttackTarget(null);
					getNavigator().clearPathEntity();
					return true;
				}
				else
				{
					flag = true;
				}
			}
			
			if (!flag && isMountSaddled() && riddenByEntity == null)
			{
				if (hasRequiredAlignment)
				{
					entityplayer.mountEntity(this);
					LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.rideWarg);
					return true;
				}
				else
				{
					flag = true;
				}
			}
			
			if (flag)
			{
				LOTRAlignmentValues.notifyAlignmentNotHighEnough(entityplayer, LOTRAlignmentValues.WARG_RIDE, getFaction());
				return true;
			}
		}
        return false;
    }
	
	@Override
    public int getTotalArmorValue()
    {
        int i = 0;
        ItemStack[] lastActiveItems = getLastActiveItems();
        int j = lastActiveItems.length;
        for (int k = 0; k < j; k++)
        {
            ItemStack itemstack = lastActiveItems[k];
            if (itemstack != null && itemstack.getItem() instanceof LOTRItemWargArmor)
            {
                int l = ((LOTRItemWargArmor)itemstack.getItem()).damageReduceAmount;
                i += l;
            }
        }
        return i;
    }
	
	@Override
	protected void damageArmor(float f)
	{
        f /= 4F;
        if (f < 1F)
        {
            f = 1F;
        }

        for (int j = 4; j >= 1; j--)
        {
            if (getEquipmentInSlot(j) != null && getEquipmentInSlot(j).getItem() instanceof LOTRItemWargArmor)
            {
                getEquipmentInSlot(j).damageItem((int)f, this);
                if (getEquipmentInSlot(j).stackSize == 0)
                {
                    setCurrentItemOrArmor(j, null);
                }
            }
        }
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		int j = 1 + rand.nextInt(3) + rand.nextInt(i + 1);
		for (int k = 0; k < j; k++)
		{
			dropItem(LOTRMod.wargFur, 1);
		}
		
		j = rand.nextInt(2) + rand.nextInt(i + 1);
		for (int k = 0; k < j; k++)
		{
			dropItem(LOTRMod.wargBone, 1);
		}
		
		if (flag && rand.nextInt(50) == 0)
		{
			entityDropItem(new ItemStack(LOTRMod.wargskinRug, 1, getWargType()), 0F);
		}
	}
	
	@Override
	public boolean canDropPouch()
	{
		return false;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killWarg;
	}
	
	@Override
	public String getLivingSound()
	{
		return "lotr:warg.growl";
	}
	
	@Override
	public String getHurtSound()
	{
		return "lotr:warg.hurt";
	}
	
	@Override
	public String getDeathSound()
	{
		return "lotr:warg.death";
	}
	
	@Override
    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        
        if (!worldObj.isRemote)
        {
			if (isMountSaddled())
			{
				setSaddled(false);
				dropItem(Items.saddle, 1);
			}

			for (int i = 0; i < getLastActiveItems().length; i++)
			{
				if (getEquipmentInSlot(i) != null)
				{
					entityDropItem(getEquipmentInSlot(i), 0F);
					setCurrentItemOrArmor(i, null);
				}
			}
		}
    }
	
	public float getTailRotation()
	{
		float f = (getMaxHealth() - getHealth()) / getMaxHealth();
		return f * -1.2F;
	}
	
	@Override
	public boolean allowLeashing()
	{
		return isMountSaddled();
	}
}
