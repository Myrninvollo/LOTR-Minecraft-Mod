package lotr.common.inventory;

import lotr.common.entity.npc.LOTREntityGollum;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class LOTRInventoryGollum implements IInventory
{
    private ItemStack[] inventory = new ItemStack[9];
	private LOTREntityGollum theGollum;
	
	public LOTRInventoryGollum(LOTREntityGollum gollum)
	{
		theGollum = gollum;
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
                markDirty();
                return itemstack;
            }
            else
            {
                itemstack = inventory[i].splitStack(j);

                if (inventory[i].stackSize == 0)
                {
                    inventory[i] = null;
                }

                markDirty();
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

        markDirty();
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public String getInventoryName()
    {
        return "Gollum";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }
	
	@Override
	public void markDirty() {}

	@Override
    public void openInventory() {}

	@Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return true;
    }
	
    public void readFromNBT(NBTTagCompound nbt)
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
    }

    public void writeToNBT(NBTTagCompound nbt)
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
    }
	
    public void dropAllItems()
    {
        for (int i = 0; i < inventory.length; i++)
        {
            if (inventory[i] != null)
            {
                theGollum.entityDropItem(inventory[i], 0F);
                inventory[i] = null;
            }
        }
    }
}
