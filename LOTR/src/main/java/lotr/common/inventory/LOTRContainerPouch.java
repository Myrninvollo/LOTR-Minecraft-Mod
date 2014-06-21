package lotr.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class LOTRContainerPouch extends Container
{
	private ItemStack thePouchItem;
	private LOTRInventoryPouch pouchInventory;
	public int capacity;
	
    public LOTRContainerPouch(EntityPlayer entityplayer)
    {
		thePouchItem = entityplayer.inventory.getCurrentItem();
		pouchInventory = new LOTRInventoryPouch(entityplayer, this);
		capacity = pouchInventory.getSizeInventory();

		int rows = capacity / 9;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new LOTRSlotPouch(pouchInventory, j + i * 9, 8 + j * 18, 30 + i * 18));
            }
        }

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(entityplayer.inventory, j + i * 9 + 9, 8 + j * 18, 98 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(entityplayer.inventory, i, 8 + i * 18, 156));
        }
    }
	
	public String getDisplayName()
	{
		return pouchInventory.getInventoryName();
	}
	
	public void renamePouch(String name)
	{
		pouchInventory.getPouchItem().setStackDisplayName(name);
		syncPouchItem(pouchInventory.getPouchItem());
	}
	
	public void syncPouchItem(ItemStack itemstack)
	{
		thePouchItem = itemstack;
		detectAndSendChanges();
	}
	
	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
		return ItemStack.areItemStacksEqual(thePouchItem, pouchInventory.getPouchItem());
    }
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
		Slot aPouchSlot = (Slot)inventorySlots.get(0);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < capacity)
            {
                if (!mergeItemStack(itemstack1, capacity, capacity + 36, true))
                {
                    return null;
                }
            }
            else if (!aPouchSlot.isItemValid(itemstack1) || !mergeItemStack(itemstack1, 0, capacity, false))
            {
                return null;
            }
            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
			else
            {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize != itemstack.stackSize)
            {
                slot.onPickupFromSlot(entityplayer, itemstack1);
            }
			else
            {
                return null;
            }
        }
        return itemstack;
    }
}
