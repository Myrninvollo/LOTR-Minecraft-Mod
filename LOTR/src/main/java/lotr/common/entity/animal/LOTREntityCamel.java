package lotr.common.entity.animal;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.LOTRMountFunctions;
import lotr.common.entity.ai.LOTREntityAIHiredHorseRemainStill;
import lotr.common.entity.ai.LOTREntityAIHorseFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHorseMoveToRiderTarget;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRNPCMount;
import lotr.common.world.biome.LOTRBiomeGenNearHarad.ImmuneToHeat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityCamel extends EntityAnimal implements LOTRNPCMount, ImmuneToHeat, IInvBasic
{
	public AnimalChest camelInv;
	
	public LOTREntityCamel(World world)
	{
		super(world);
		setSize(1.6F, 1.8F);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(0, new LOTREntityAIHiredHorseRemainStill(this));
		tasks.addTask(0, new LOTREntityAIHorseMoveToRiderTarget(this));
		tasks.addTask(0, new LOTREntityAIHorseFollowHiringPlayer(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.5D));
        tasks.addTask(2, new EntityAIMate(this, 1D));
        tasks.addTask(3, new EntityAITempt(this, 1.2D, Items.wheat, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6F));
        tasks.addTask(7, new EntityAILookIdle(this));
        setupInventory();
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte)0));
		dataWatcher.addObject(17, Byte.valueOf((byte)0));
		dataWatcher.addObject(18, Byte.valueOf((byte)0));
	}
	
	@Override
	public boolean isMountSaddled()
    {
		if (getBelongsToNPC())
		{
			return true;
		}
        return dataWatcher.getWatchableObjectByte(16) == (byte)1;
    }

    private void setSaddled(boolean flag)
    {
    	dataWatcher.updateObject(16, Byte.valueOf(flag ? (byte)1 : (byte)0));
    }
    
    public void saddleCamel()
    {
    	camelInv.setInventorySlotContents(0, new ItemStack(Items.saddle));
    	setupInventory();
    }
	
	@Override
	public boolean getBelongsToNPC()
	{
		return dataWatcher.getWatchableObjectByte(17) == (byte)1;
	}
	
	@Override
	public void setBelongsToNPC(boolean flag)
	{
		dataWatcher.updateObject(17, Byte.valueOf(flag ? (byte)1 : (byte)0));
		if (flag)
		{
			setSaddled(true);
			if (getGrowingAge() < 0)
			{
				setGrowingAge(0);
			}
		}
	}
	
	public boolean hasChest()
	{
		return dataWatcher.getWatchableObjectByte(18) == (byte)1;
	}
	
	public void setHasChest(boolean flag)
	{
		dataWatcher.updateObject(18, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(24D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.24D);
	}
	
	@Override
	public boolean isAIEnabled()
	{
		return true;
	}
	
	@Override
    public EntityAnimal createChild(EntityAgeable entity)
    {
        return new LOTREntityCamel(worldObj);
    }
	
	@Override
	public boolean isBreedingItem(ItemStack itemstack)
    {
        return itemstack != null && itemstack.getItem() == Items.wheat;
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		LOTRMountFunctions.update(this);
		
        if (!worldObj.isRemote)
        {
        	if (riddenByEntity instanceof EntityPlayer && isMountSaddled())
			{
				LOTRLevelData.addAchievement((EntityPlayer)riddenByEntity, LOTRAchievement.rideCamel);
			}
		}
	}
	
	public void setupInventory()
    {
        AnimalChest prevInv = camelInv;
        
        camelInv = new AnimalChest("camelInv", hasChest() ? 16 : 1);
        camelInv.func_110133_a(getCommandSenderName());

        if (prevInv != null && !worldObj.isRemote)
        {
        	prevInv.func_110132_b(this);
            int invSize = Math.min(prevInv.getSizeInventory(), camelInv.getSizeInventory());

            for (int i = 0; i < invSize; i++)
            {
                ItemStack itemstack = prevInv.getStackInSlot(i);
                if (itemstack != null)
                {
                    camelInv.setInventorySlotContents(i, itemstack.copy());
                }
            }

            prevInv = null;
        }

        camelInv.func_110134_a(this);
        checkInventory();
    }
	
	private void checkInventory()
    {
        if (!worldObj.isRemote)
        {
            setSaddled(camelInv.getStackInSlot(0) != null);
        }
    }
	
	@Override
	public void onInventoryChanged(InventoryBasic par1InventoryBasic)
    {
        boolean saddled = isMountSaddled();
        
        checkInventory();

        if (ticksExisted > 20)
        {
            if (!saddled && isMountSaddled())
            {
                playSound("mob.horse.leather", 0.5F, 1F);
            }
        }
    }
	
	@Override
	public boolean interact(EntityPlayer entityplayer)
	{
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		
		if (LOTRMountFunctions.interact(this, entityplayer))
		{
			return true;
		}
		
		if (!isChild() && entityplayer.isSneaking())
        {
            openGUI(entityplayer);
            return true;
        }
		else if (super.interact(entityplayer))
        {
            return true;
        }
		else if (!isChild() && !hasChest() && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.chest))
        {
			if (!entityplayer.capabilities.isCreativeMode)
			{
				itemstack.stackSize--;
				if (itemstack.stackSize <= 0)
				{
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
				}
			}
            setHasChest(true);
            playSound("mob.horse.leather", 0.5F, 1F);
            setupInventory();
           	return true;
        }
        else if (!isChild() && !worldObj.isRemote && (riddenByEntity == null || riddenByEntity == entityplayer))
        {
            entityplayer.mountEntity(this);
            return true;
        }
        else
        {
            return false;
        }
	}
	
	public void openGUI(EntityPlayer entityplayer)
    {
        if (!worldObj.isRemote && (riddenByEntity == null || riddenByEntity == entityplayer) && !getBelongsToNPC())
        {
            camelInv.func_110133_a(getCommandSenderName());
            entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_CAMEL, worldObj, getEntityId(), 0, 0);
        }
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("BelongsToNPC", getBelongsToNPC());
		nbt.setBoolean("HasChest", hasChest());
		
		if (camelInv != null)
        {
            NBTTagList items = new NBTTagList();

            for (int i = 0; i < camelInv.getSizeInventory(); i++)
            {
                ItemStack itemstack = camelInv.getStackInSlot(i);
                if (itemstack != null)
                {
                    NBTTagCompound itemData = new NBTTagCompound();
                    itemData.setByte("Slot", (byte)i);
                    itemstack.writeToNBT(itemData);
                    items.appendTag(itemData);
                }
            }

            nbt.setTag("Items", items);
        }
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setBelongsToNPC(nbt.getBoolean("BelongsToNPC"));
		setHasChest(nbt.getBoolean("HasChest"));

		setupInventory();
		
        NBTTagList items = nbt.getTagList("Items", new NBTTagCompound().getId());
        for (int i = 0; i < items.tagCount(); i++)
        {
            NBTTagCompound itemData = items.getCompoundTagAt(i);
            int slot = itemData.getByte("Slot");
            if (slot >= 0 && slot < camelInv.getSizeInventory())
            {
            	camelInv.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(itemData));
            }
        }

		checkInventory();
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
	public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        
        if (!worldObj.isRemote && isMountSaddled() && !getBelongsToNPC())
        {
			dropItem(Items.saddle, 1);
			setSaddled(false);
        }

        if (!worldObj.isRemote && hasChest())
        {
            for (int i = 0; i < camelInv.getSizeInventory(); ++i)
            {
                ItemStack itemstack = camelInv.getStackInSlot(i);
                if (itemstack != null)
                {
                    entityDropItem(itemstack, 0F);
                }
            }
        	
        	dropItem(Item.getItemFromBlock(Blocks.chest), 1);
        	setHasChest(false);
        }
    }

	@Override
    protected void dropFewItems(boolean flag, int i)
    {
        int j = rand.nextInt(3) + 1 + rand.nextInt(1 + i);

        for (int k = 0; k < j; k++)
        {
            dropItem(Items.leather, 1);
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
