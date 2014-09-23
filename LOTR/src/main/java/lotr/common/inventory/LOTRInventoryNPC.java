package lotr.common.inventory;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class LOTRInventoryNPC extends InventoryBasic
{
	private LOTREntityNPC theEntity;
	private String nbtName;
	
	public LOTRInventoryNPC(String s, LOTREntityNPC npc, int i)
	{
		super(s, true, i);
		theEntity = npc;
		nbtName = s;
	}
	
    public void readFromNBT(NBTTagCompound nbt)
    {
        NBTTagList items = nbt.getTagList(nbtName, Constants.NBT.TAG_COMPOUND);
		
        for (int i = 0; i < items.tagCount(); i++)
        {
            NBTTagCompound itemData = (NBTTagCompound)items.getCompoundTagAt(i);
            byte slot = itemData.getByte("Slot");

            if (slot >= 0 && slot < getSizeInventory())
            {
                setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(itemData));
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList items = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); i++)
        {
			ItemStack itemstack = getStackInSlot(i);
            if (itemstack != null)
            {
                NBTTagCompound itemData = new NBTTagCompound();
                itemData.setByte("Slot", (byte)i);
                itemstack.writeToNBT(itemData);
                items.appendTag(itemData);
            }
        }

        nbt.setTag(nbtName, items);
    }
	
    public void dropAllItems()
    {
        for (int i = 0; i < getSizeInventory(); i++)
        {
			ItemStack itemstack = getStackInSlot(i);
            if (itemstack != null)
            {
                theEntity.npcDropItem(itemstack, 0F, false);
                setInventorySlotContents(i, null);
            }
        }
    }
    
    public boolean isEmpty()
    {
    	for (int i = 0; i < getSizeInventory(); i++)
    	{
    		if (getStackInSlot(i) != null)
    		{
    			return false;
    		}
    	}
    	return true;
    }
}
