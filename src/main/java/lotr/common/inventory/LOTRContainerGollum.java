package lotr.common.inventory;

import lotr.common.entity.npc.LOTREntityGollum;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class LOTRContainerGollum extends Container
{
	public LOTREntityGollum theGollum;
	
    public LOTRContainerGollum(InventoryPlayer inv, LOTREntityGollum gollum)
    {
		theGollum = gollum;

		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(gollum.inventory, i, 8 + i * 18, 18));
		}

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 50 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inv, i, 8 + i * 18, 108));
        }
    }
	
	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
		return theGollum != null && theGollum.getGollumOwner() == entityplayer && entityplayer.getDistanceSqToEntity(theGollum) <= 144D && theGollum.isEntityAlive();
    }
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < 9)
            {
                if (!mergeItemStack(itemstack1, 9, 45, true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, 9, false))
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
