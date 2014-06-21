package lotr.common.inventory;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class LOTRInventoryHiredNPC extends InventoryBasic
{
	private LOTREntityNPC theEntity;
	
	public LOTRInventoryHiredNPC(LOTREntityNPC npc, int i)
	{
		super("hiredNPC", true, i);
		theEntity = npc;
	}
	
    public void readFromNBT(NBTTagCompound nbt)
    {
        NBTTagList items = nbt.getTagList("HiredInventory", new NBTTagCompound().getId());
		
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

        nbt.setTag("HiredInventory", items);
    }
	
    public void dropAllItems()
    {
        for (int i = 0; i < getSizeInventory(); i++)
        {
			ItemStack itemstack = getStackInSlot(i);
            if (itemstack != null)
            {
                theEntity.entityDropItem(itemstack, 0F);
                setInventorySlotContents(i, null);
            }
        }
    }
}
