package lotr.common.entity.animal;

import java.util.List;

import lotr.common.*;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.ai.*;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRNPCMount;
import lotr.common.item.LOTRItemMountArmor;
import lotr.common.world.biome.LOTRBiomeGenRohan;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.oredict.OreDictionary;

public class LOTREntityHorse extends EntityHorse implements LOTRNPCMount
{
	private boolean isMoving;
	private ItemStack prevMountArmor;
	private EntityAIBase attackAI;
	private EntityAIBase panicAI;
	private boolean prevIsChild = true;
	
    public LOTREntityHorse(World world)
    {
        super(world);
		tasks.addTask(0, new LOTREntityAIHiredHorseRemainStill(this));
		tasks.addTask(0, new LOTREntityAIHorseMoveToRiderTarget(this));
		tasks.addTask(0, new LOTREntityAIHorseFollowHiringPlayer(this));
		
		EntityAITaskEntry panic = removeTask(EntityAIPanic.class);
		tasks.addTask(panic.priority, panic.action);
		panicAI = panic.action;
		
		attackAI = createMountAttackAI();
		
		if (isMountHostile())
		{
			targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		}
    }
    
    protected EntityAIBase createMountAttackAI()
    {
    	return null;
    }
    
	protected boolean isMountHostile()
	{
		return false;
	}
    
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(25, Byte.valueOf((byte)0));
		dataWatcher.addObject(26, Byte.valueOf((byte)1));
		dataWatcher.addObject(27, Integer.valueOf(0));
		dataWatcher.addObject(28, Byte.valueOf((byte)0));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
		if (isMountHostile())
		{
			getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
		}
    }

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		if (!worldObj.isRemote)
		{
			data = super.onSpawnWithEgg(data);
			onLOTRHorseSpawn();
			setHealth(getMaxHealth());
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
	
	protected void onLOTRHorseSpawn()
	{
		int i = MathHelper.floor_double(posX);
		int k = MathHelper.floor_double(posZ);
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		
		if (biome instanceof LOTRBiomeGenRohan && getClass() == LOTREntityHorse.class)
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
			if (getClass() == LOTREntityHorse.class)
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
	
	public ItemStack getMountArmor()
	{
		int ID = dataWatcher.getWatchableObjectInt(27);
		return new ItemStack(Item.getItemById(ID));
	}
	
	@Override
	public String getMountArmorTexture()
	{
		ItemStack armor = getMountArmor();
		if (armor != null && armor.getItem() instanceof LOTRItemMountArmor)
		{
			return ((LOTRItemMountArmor)armor.getItem()).getArmorTexture();
		}
		return null;
	}
	
	private void setMountArmorWatched(ItemStack itemstack)
	{
		if (itemstack == null)
		{
			dataWatcher.updateObject(27, Integer.valueOf(0));
		}
		else
		{
			dataWatcher.updateObject(27, Integer.valueOf(Item.getIdFromItem(itemstack.getItem())));
		}
	}
	
	public boolean isMountEnraged()
	{
		return dataWatcher.getWatchableObjectByte(28) == (byte)1;
	}
	
	public void setMountEnraged(boolean flag)
	{
		dataWatcher.updateObject(28, Byte.valueOf(flag ? (byte)1 : (byte)0));
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
	
    public void saddleMountForWorldGen()
    {
    	setGrowingAge(0);
    	LOTRReflection.getHorseInv(this).setInventorySlotContents(0, new ItemStack(Items.saddle));
    	LOTRReflection.setupHorseInv(this);
    	setHorseTamed(true);
    }
    
    public void setChestedForWorldGen()
    {
    	setChested(true);
    	LOTRReflection.setupHorseInv(this);
    }
    
    public void setMountArmor(ItemStack itemstack)
    {
    	LOTRReflection.getHorseInv(this).setInventorySlotContents(1, itemstack);
    	LOTRReflection.setupHorseInv(this);
    	setMountArmorWatched(itemstack);
    }

    @Override
    public boolean isMountArmorValid(ItemStack itemstack)
    {
    	if (itemstack != null && itemstack.getItem() instanceof LOTRItemMountArmor)
        {
        	LOTRItemMountArmor armor = (LOTRItemMountArmor)itemstack.getItem();
        	return armor.isValid(this);
        }
    	return false;
    }
    
    @Override
    public int getTotalArmorValue()
    {
        ItemStack itemstack = LOTRReflection.getHorseInv(this).getStackInSlot(1);
        if (itemstack != null && itemstack.getItem() instanceof LOTRItemMountArmor)
        {
        	LOTRItemMountArmor armor = (LOTRItemMountArmor)itemstack.getItem();
        	return armor.getDamageReduceAmount();
        }
        return 0;
    }
	
	@Override
	public void onLivingUpdate()
	{
		if (!worldObj.isRemote)
		{
			ItemStack armor = LOTRReflection.getHorseInv(this).getStackInSlot(1);
			
	        if (ticksExisted > 20)
	        {
	            if (!ItemStack.areItemStacksEqual(prevMountArmor, armor))
	            {
	                playSound("mob.horse.armor", 0.5F, 1F);
	            }
	        }
				
			prevMountArmor = armor;
			setMountArmorWatched(armor);
		}

		super.onLivingUpdate();
		
		if (!worldObj.isRemote && riddenByEntity instanceof EntityPlayer && isInWater())
		{
			if (rand.nextFloat() < 0.6F)
	        {
				motionY = 0.2D;
				isAirBorne = true;
	        }
		}
		
		if (!worldObj.isRemote && isMountHostile())
		{
			boolean isChild = isChild();
			if (isChild != prevIsChild)
			{
				if (isChild)
				{
					EntityAITaskEntry taskEntry = removeTask(attackAI.getClass());
					tasks.addTask(taskEntry.priority, panicAI);
				}
				else
				{
					EntityAITaskEntry taskEntry = removeTask(panicAI.getClass());
					tasks.addTask(taskEntry.priority, attackAI);
				}
			}
			
			if (getAttackTarget() != null)
			{
				EntityLivingBase target = getAttackTarget();
				if (!target.isEntityAlive() || (target instanceof EntityPlayer && ((EntityPlayer)target).capabilities.isCreativeMode))
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
			
			setMountEnraged(getAttackTarget() != null);
		}
		
		prevIsChild = isChild();
	}
	
	private EntityAITaskEntry removeTask(Class c)
	{
		for (int i = 0; i < tasks.taskEntries.size(); i++)
		{
			EntityAITaskEntry taskEntry = (EntityAITaskEntry)tasks.taskEntries.get(i);
			if (c.isAssignableFrom(taskEntry.action.getClass()))
			{
				tasks.removeTask(taskEntry.action);
				return taskEntry;
			}
		}
		return null;
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
	public boolean isBreedingItem(ItemStack itemstack)
    {
        return itemstack != null && LOTRMod.isOreNameEqual(itemstack, "apple");
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
		if (isMountEnraged())
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
		else
		{
			ItemStack itemstack = entityplayer.getHeldItem();
	        if (itemstack != null && isBreedingItem(itemstack) && getGrowingAge() == 0 && !isInLove() && isTame())
	        {
	            if (!entityplayer.capabilities.isCreativeMode)
	            {
	                itemstack.stackSize--;
	                if (itemstack.stackSize <= 0)
	                {
	                	entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
	                }
	            }
	            func_146082_f(entityplayer);
	            return true;
	        }
		}
		
		boolean prevInLove = isInLove();
		boolean flag = super.interact(entityplayer);
		if (isInLove() && !prevInLove)
		{
			resetInLove();
		}
		return flag;
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
		boolean flag = super.attackEntityFrom(damagesource, f);
		if (flag && isChild() && isMountHostile())
		{
			Entity attacker = damagesource.getEntity();
			if (attacker instanceof EntityLivingBase)
			{
				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(12D, 12D, 12D));
	            for (int j = 0; j < list.size(); j++)
	            {
					Entity entity = (Entity)list.get(j);
					if (entity.getClass() == getClass())
					{
						LOTREntityHorse mount = (LOTREntityHorse)entity;
						if (!mount.isChild() && !mount.isTame())
						{
							mount.setAttackTarget((EntityLivingBase)attacker);
						}
					}
				}
			}
		}
        return flag;
    }
	
	@Override
	public void openGUI(EntityPlayer entityplayer)
    {
        if (!worldObj.isRemote && (riddenByEntity == null || riddenByEntity == entityplayer) && isTame())
        {
        	AnimalChest animalchest = LOTRReflection.getHorseInv(this);
        	animalchest.func_110133_a(getCommandSenderName());
            entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_MOUNT_INV, worldObj, getEntityId(), animalchest.getSizeInventory(), 0);
        }
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		
		nbt.setBoolean("BelongsToNPC", getBelongsToNPC());
		nbt.setBoolean("Mountable", getMountable());
		
		AnimalChest inv = LOTRReflection.getHorseInv(this);
        if (inv.getStackInSlot(1) != null)
        {
            nbt.setTag("LOTRMountArmorItem", inv.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
        }
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
		
		AnimalChest inv = LOTRReflection.getHorseInv(this);
		if (nbt.hasKey("LOTRMountArmorItem"))
        {
            ItemStack armor = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("LOTRMountArmorItem"));
            if (armor != null && isMountArmorValid(armor))
            {
            	inv.setInventorySlotContents(1, armor);
            }
        }
	}
	
	@Override
	public boolean canDespawn()
	{
		return getBelongsToNPC() && riddenByEntity == null;
	}
	
	@Override
	public void onDeath(DamageSource damagesource)
	{
		if (getBelongsToNPC())
		{
			AnimalChest inv = LOTRReflection.getHorseInv(this);
			inv.setInventorySlotContents(0, null);
			inv.setInventorySlotContents(1, null);
		}
		
		super.onDeath(damagesource);
	}
	
	@Override
    public String getCommandSenderName()
    {
		if (getClass() == LOTREntityHorse.class)
		{
			return super.getCommandSenderName();
		}
		else
		{
	        if (hasCustomNameTag())
	        {
	            return getCustomNameTag();
	        }
	        else
	        {
	        	String s = EntityList.getEntityString(this);
	            return StatCollector.translateToLocal("entity." + s + ".name");
	        }
		}
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
