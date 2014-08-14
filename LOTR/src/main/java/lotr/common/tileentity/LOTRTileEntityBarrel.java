package lotr.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import lotr.common.item.LOTRItemMugBrewable;
import lotr.common.recipe.LOTRBrewingRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

public class LOTRTileEntityBarrel extends TileEntity implements IInventory
{
	public static int EMPTY = 0;
	public static int BREWING = 1;
	public static int FULL = 2;
	
    private ItemStack[] inventory = new ItemStack[10];
	public int barrelMode;
	public int brewingTime;
	public int brewingAnimationTick;
	private String specialBarrelName;
	public List players = new ArrayList();
	
	public ItemStack getMugRefill()
	{
		if (barrelMode == FULL && inventory[9] != null)
		{
			ItemStack itemstack = inventory[9].copy();
			itemstack.stackSize = 1;
			return itemstack;
		}
		return null;
	}
	
	public void consumeMugRefill()
	{
		if (barrelMode == FULL && inventory[9] != null)
		{
			inventory[9].stackSize--;
			if (inventory[9].stackSize <= 0)
			{
				inventory[9] = null;
				barrelMode = EMPTY;
			}
		}
	}
	
	private void updateBrewingRecipe()
	{
		if (barrelMode == EMPTY)
		{
			inventory[9] = LOTRBrewingRecipes.findMatchingRecipe(this);
		}
	}
	
	public void handleBrewingButtonPress()
	{
		if (barrelMode == EMPTY && inventory[9] != null)
		{
			barrelMode = BREWING;
			for (int i = 0; i < 9; i++)
			{
				if (inventory[i] != null)
				{
					ItemStack containerItem = null;
					if (inventory[i].getItem().hasContainerItem(inventory[i]))
					{
						containerItem = inventory[i].getItem().getContainerItem(inventory[i]);
						if (containerItem.isItemStackDamageable() && containerItem.getItemDamage() > containerItem.getMaxDamage())
						{
							containerItem = null;
						}
					}
					
					inventory[i].stackSize--;
					if (inventory[i].stackSize <= 0)
					{
						inventory[i] = null;
						if (containerItem != null)
						{
							inventory[i] = containerItem;
						}
					}
				}
			}
			
			if (!worldObj.isRemote)
			{
				for (int i = 0; i < players.size(); i++)
				{
					EntityPlayerMP entityplayer = (EntityPlayerMP)players.get(i);
					entityplayer.openContainer.detectAndSendChanges();
					entityplayer.sendContainerToPlayer(entityplayer.openContainer);
				}
			}
		}
		
		else if (barrelMode == BREWING && inventory[9] != null && inventory[9].getItemDamage() > 0)
		{
			barrelMode = FULL;
			brewingTime = 0;
			ItemStack itemstack = inventory[9].copy();
			itemstack.setItemDamage(itemstack.getItemDamage() - 1);
			inventory[9] = itemstack;
		}
	}

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (inventory[i] != null)
        {
            ItemStack itemstack;

            if (inventory[i].stackSize <= j)
            {
                itemstack = inventory[i];
                inventory[i] = null;
				if (i != 9)
				{
					updateBrewingRecipe();
				}
                return itemstack;
            }
            else
            {
                itemstack = inventory[i].splitStack(j);

                if (inventory[i].stackSize == 0)
                {
                    inventory[i] = null;
                }

				if (i != 9)
				{
					updateBrewingRecipe();
				}
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (inventory[i] != null)
        {
            ItemStack itemstack = inventory[i];
            inventory[i] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
		
		if (i != 9)
		{
			updateBrewingRecipe();
		}
    }

	@Override
    public String getInventoryName()
    {
        return hasCustomInventoryName() ? specialBarrelName : StatCollector.translateToLocal("container.lotr.barrel");
    }
	
	public String getInvSubtitle()
	{
		ItemStack brewingItem = getStackInSlot(9);
		if (barrelMode == EMPTY)
		{
			return StatCollector.translateToLocal("container.lotr.barrel.empty");
		}
		else if (barrelMode == BREWING && brewingItem != null)
		{
			return StatCollector.translateToLocalFormatted("container.lotr.barrel.brewing", new Object[] {brewingItem.getDisplayName(), LOTRItemMugBrewable.getStrengthSubtitle(brewingItem)});
		}
		else if (barrelMode == FULL && brewingItem != null)
		{
			return StatCollector.translateToLocalFormatted("container.lotr.barrel.full", new Object[] {brewingItem.getDisplayName(), LOTRItemMugBrewable.getStrengthSubtitle(brewingItem), brewingItem.stackSize});
		}
		return "";
	}

	@Override
    public boolean hasCustomInventoryName()
    {
        return specialBarrelName != null && specialBarrelName.length() > 0;
    }

    public void setBarrelName(String s)
    {
        specialBarrelName = s;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        readBarrelFromNBT(nbt);
    }
    
    public void readBarrelFromNBT(NBTTagCompound nbt)
    {
        NBTTagList items = nbt.getTagList("Items", new NBTTagCompound().getId());
        inventory = new ItemStack[getSizeInventory()];
        for (int i = 0; i < items.tagCount(); i++)
        {
            NBTTagCompound itemData = (NBTTagCompound)items.getCompoundTagAt(i);
            byte byte0 = itemData.getByte("Slot");

            if (byte0 >= 0 && byte0 < inventory.length)
            {
                inventory[byte0] = ItemStack.loadItemStackFromNBT(itemData);
            }
        }
		
		barrelMode = nbt.getByte("BarrelMode");
		brewingTime = nbt.getInteger("BrewingTime");
		
        if (nbt.hasKey("CustomName"))
        {
            specialBarrelName = nbt.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        writeBarrelToNBT(nbt);
    } 
        
    public void writeBarrelToNBT(NBTTagCompound nbt)
    {
        NBTTagList items = new NBTTagList();
        for (int i = 0; i < inventory.length; i++)
        {
            if (inventory[i] != null)
            {
                NBTTagCompound itemData = new NBTTagCompound();
                itemData.setByte("Slot", (byte)i);
                inventory[i].writeToNBT(itemData);
                items.appendTag(itemData);
            }
        }
        nbt.setTag("Items", items);
		
		nbt.setByte("BarrelMode", (byte)barrelMode);
		nbt.setInteger("BrewingTime", brewingTime);
		
        if (hasCustomInventoryName())
        {
            nbt.setString("CustomName", specialBarrelName);
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }
	
	public int getBrewProgressScaled(int i)
	{
		return brewingTime * i / 12000;
	}
	
	public int getBrewAnimationProgressScaled(int i)
	{
		return (brewingAnimationTick + 1) * i / 20;
	}
	
	public int getBarrelFullAmountScaled(int i)
	{
		return inventory[9] == null ? 0 : inventory[9].stackSize * i / LOTRBrewingRecipes.BARREL_CAPACITY;
	}

    @Override
    public void updateEntity()
    {
		boolean needUpdate = false;
		
        if (!worldObj.isRemote)
        {
			if (barrelMode == BREWING)
			{
				if (inventory[9] != null)
				{
					brewingTime++;
					if (brewingTime >= 12000)
					{
						brewingTime = 0;
						if (inventory[9].getItemDamage() < 4)
						{
							inventory[9].setItemDamage(inventory[9].getItemDamage() + 1);
							needUpdate = true;
						}
						else
						{
							barrelMode = FULL;
						}
					}
				}
				else
				{
					barrelMode = EMPTY;
				}
			}
			else
			{
				brewingTime = 0;
			}
			if (barrelMode == FULL && inventory[9] == null)
			{
				barrelMode = EMPTY;
			}
		}
		else
		{
			if (barrelMode == BREWING)
			{
				brewingAnimationTick++;
				if (brewingAnimationTick >= 20)
				{
					brewingAnimationTick = 0;
				}
			}
			else
			{
				brewingAnimationTick = 0;
			}
		}
		
		if (needUpdate)
		{
			markDirty();
		}
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
    }

	@Override
    public void openInventory() {}

	@Override
    public void closeInventory() {}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		return false;
	}
	
	@Override
    public Packet getDescriptionPacket()
    {
		NBTTagCompound data = new NBTTagCompound();
		writeBarrelToNBT(data);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, data);
    }
	
	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet)
	{
		NBTTagCompound data = packet.func_148857_g();
		readBarrelFromNBT(data);
	}
}
