package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.LOTRMountFunctions;
import lotr.common.entity.ai.*;
import lotr.common.entity.animal.LOTREntityRabbit;
import lotr.common.item.LOTRItemMountArmor;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class LOTREntityWarg extends LOTREntityNPCRideable implements IInvBasic
{
	private int eatingTick;
	private AnimalChest wargInventory;
	
	public LOTREntityWarg(World world)
	{
		super(world);
		setSize(1.6F, 1.4F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(2, getWargAttackAI());
		tasks.addTask(3, new LOTREntityAIUntamedPanic(this, 1.2D));
		tasks.addTask(4, new LOTREntityAIFollowHiringPlayer(this));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest2(this, EntityPlayer.class, 12F, 0.05F));
        tasks.addTask(6, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 8F, 0.05F));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityLiving.class, 12F, 0.02F));
        tasks.addTask(8, new EntityAILookIdle(this));

		int target = addTargetTasks(true);
		targetTasks.addTask(target + 1, new LOTREntityAINearestAttackableTargetBasic(this, LOTREntityRabbit.class, 500, false));
		
		isImmuneToFrost = true;
		spawnsInDarkness = true;
		setupWargInventory();
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
		dataWatcher.addObject(19, Integer.valueOf(0));
		
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
		return dataWatcher.getWatchableObjectByte(17) == (byte)1;
    }

    public void setWargSaddled(boolean flag)
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
	
	public ItemStack getWargArmorWatched()
	{
		int ID = dataWatcher.getWatchableObjectInt(19);
		return new ItemStack(Item.getItemById(ID));
	}
	
	@Override
	public String getMountArmorTexture()
	{
		ItemStack armor = getWargArmorWatched();
		if (armor != null && armor.getItem() instanceof LOTRItemMountArmor)
		{
			return ((LOTRItemMountArmor)armor.getItem()).getArmorTexture();
		}
		return null;
	}
	
	private void setWargArmorWatched(ItemStack itemstack)
	{
		if (itemstack == null)
		{
			dataWatcher.updateObject(19, Integer.valueOf(0));
		}
		else
		{
			dataWatcher.updateObject(19, Integer.valueOf(Item.getIdFromItem(itemstack.getItem())));
		}
	}
	
	@Override
	public IInventory getMountInventory()
	{
		return wargInventory;
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
	
	private void setupWargInventory()
	{
        AnimalChest prevInv = wargInventory;
        wargInventory = new AnimalChest("WargInv", 2);
        wargInventory.func_110133_a(getCommandSenderName());

        if (prevInv != null)
        {
        	prevInv.func_110132_b(this);
            int invSize = Math.min(prevInv.getSizeInventory(), wargInventory.getSizeInventory());
            for (int slot = 0; slot < invSize; slot++)
            {
                ItemStack itemstack = prevInv.getStackInSlot(slot);
                if (itemstack != null)
                {
                	wargInventory.setInventorySlotContents(slot, itemstack.copy());
                }
            }

            prevInv = null;
        }

        wargInventory.func_110134_a(this);
        checkWargInventory();
	}
	
    private void checkWargInventory()
    {
        if (!worldObj.isRemote)
        {
            setWargSaddled(wargInventory.getStackInSlot(0) != null);
            setWargArmorWatched(getWargArmor());
        }
    }
    
    @Override
    public void onInventoryChanged(InventoryBasic inv)
    {
        boolean prevSaddled = isMountSaddled();
        ItemStack prevArmor = getWargArmorWatched();
        
        checkWargInventory();
        
        ItemStack wargArmor = getWargArmorWatched();

        if (ticksExisted > 20)
        {
            if (!prevSaddled && isMountSaddled())
            {
                playSound("mob.horse.leather", 0.5F, 1F);
            }
            
            if (!ItemStack.areItemStacksEqual(prevArmor, wargArmor))
            {
                playSound("mob.horse.armor", 0.5F, 1F);
            }
        }
    }
    
    public void setWargArmor(ItemStack itemstack)
    {
    	wargInventory.setInventorySlotContents(1, itemstack);
    	setupWargInventory();
    	setWargArmorWatched(getWargArmor());
    }
    
    public ItemStack getWargArmor()
    {
    	return wargInventory.getStackInSlot(1);
    }
    
    @Override
    public int getTotalArmorValue()
    {
        ItemStack itemstack = getWargArmor();
        if (itemstack != null && itemstack.getItem() instanceof LOTRItemMountArmor)
        {
        	LOTRItemMountArmor armor = (LOTRItemMountArmor)itemstack.getItem();
        	return armor.getDamageReduceAmount();
        }
        return 0;
    }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setByte("WargType", (byte)getWargType());
        
        if (wargInventory.getStackInSlot(0) != null)
        {
        	nbt.setTag("WargSaddleItem", wargInventory.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
        }
        
        if (getWargArmor() != null)
        {
        	nbt.setTag("WargArmorItem", getWargArmor().writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
		setWargType(nbt.getByte("WargType"));
		
        if (nbt.hasKey("WargSaddleItem"))
        {
            ItemStack saddle = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("WargSaddleItem"));
            if (saddle != null && saddle.getItem() == Items.saddle)
            {
            	wargInventory.setInventorySlotContents(0, saddle);
            }
        }
        else if (nbt.getBoolean("Saddled"))
        {
        	wargInventory.setInventorySlotContents(0, new ItemStack(Items.saddle));
        }
		
        if (nbt.hasKey("WargArmorItem"))
        {
            ItemStack wargArmor = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("WargArmorItem"));
            if (wargArmor != null && isMountArmorValid(wargArmor))
            {
            	wargInventory.setInventorySlotContents(1, wargArmor);
            }
        }

        checkWargInventory();
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
        if (!worldObj.isRemote)
        {
			if (riddenByEntity instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer)riddenByEntity;
				if (LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) < LOTRAlignmentValues.Levels.WARG_RIDE)
				{
					entityplayer.mountEntity(null);
				}
				else if (isNPCTamed() && isMountSaddled())
				{
					LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.rideWarg);
				}
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
		return super.canDespawn() && !isNPCTamed();
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
			boolean hasRequiredAlignment = LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.Levels.WARG_RIDE;
			boolean notifyNotEnoughAlignment = false;
			
			ItemStack itemstack = entityplayer.inventory.getCurrentItem();
			
			if (!notifyNotEnoughAlignment && isNPCTamed() && entityplayer.isSneaking())
	        {
				if (hasRequiredAlignment)
				{
		            openGUI(entityplayer);
		            return true;
				}
				else
				{
					notifyNotEnoughAlignment = true;
				}
	        }
			
			if (!notifyNotEnoughAlignment && isNPCTamed() && itemstack != null && itemstack.getItem() instanceof ItemFood && ((ItemFood)itemstack.getItem()).isWolfsFavoriteMeat() && getHealth() < getMaxHealth())
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
					notifyNotEnoughAlignment = true;
				}
			}
			
			if (!notifyNotEnoughAlignment && isNPCTamed() && !isMountSaddled() && canWargBeRidden() && riddenByEntity == null && itemstack != null && itemstack.getItem() == Items.saddle)
			{
				if (hasRequiredAlignment)
				{
					openGUI(entityplayer);
					return true;
				}
				else
				{
					notifyNotEnoughAlignment = true;
				}
			}
			
			if (!notifyNotEnoughAlignment && !isChild() && canWargBeRidden() && riddenByEntity == null)
			{
				if (hasRequiredAlignment)
				{
					entityplayer.mountEntity(this);
					setAttackTarget(null);
					getNavigator().clearPathEntity();
					return true;
				}
				else
				{
					notifyNotEnoughAlignment = true;
				}
			}
			
			if (notifyNotEnoughAlignment)
			{
				LOTRAlignmentValues.notifyAlignmentNotHighEnough(entityplayer, LOTRAlignmentValues.Levels.WARG_RIDE, getFaction());
				return true;
			}
		}
		
        return super.interact(entityplayer);
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
    		if (getBelongsToNPC())
    		{
    			wargInventory.setInventorySlotContents(0, null);
    			wargInventory.setInventorySlotContents(1, null);
    		}
    		
			if (isNPCTamed())
			{
				setWargSaddled(false);
				dropItem(Items.saddle, 1);
				
				ItemStack wargArmor = getWargArmor();
				if (wargArmor != null)
				{
					entityDropItem(wargArmor, 0F);
					setWargArmor(null);
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
		return isNPCTamed();
	}
}
