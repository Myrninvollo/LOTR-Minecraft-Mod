package lotr.common.inventory;

import lotr.common.entity.animal.LOTREntityCamel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class LOTRContainerCamel extends Container
{
    private LOTREntityCamel theCamel;

    public LOTRContainerCamel(IInventory inv, LOTREntityCamel camel)
    {
    	theCamel = camel;
    	camel.setupInventory();
    	camel.camelInv.openInventory();

        addSlotToContainer(new LOTRSlotSaddle(camel.camelInv, 0, 8, 18));

        if (camel.hasChest())
        {
            for (int j = 0; j < 3; j++)
            {
                for (int k = 0; k < 5; k++)
                {
                    addSlotToContainer(new Slot(camel.camelInv, 1 + k + j * 5, 80 + k * 18, 18 + j * 18));
                }
            }
        }

        for (int j = 0; j < 3; j++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(inv, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(inv, j, 8 + j * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return !theCamel.getBelongsToNPC() && theCamel.camelInv.isUseableByPlayer(entityplayer) && theCamel.isEntityAlive() && theCamel.getDistanceToEntity(entityplayer) < 8F;
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

            if (i < theCamel.camelInv.getSizeInventory())
            {
                if (!mergeItemStack(itemstack1, theCamel.camelInv.getSizeInventory(), inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (getSlot(0).isItemValid(itemstack1))
            {
                if (!mergeItemStack(itemstack1, 0, 1, false))
                {
                    return null;
                }
            }
            else if (theCamel.camelInv.getSizeInventory() <= 1 || !mergeItemStack(itemstack1, 1, theCamel.camelInv.getSizeInventory(), false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        theCamel.camelInv.closeInventory();
    }
}