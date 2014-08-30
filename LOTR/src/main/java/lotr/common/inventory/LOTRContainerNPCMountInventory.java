package lotr.common.inventory;

import lotr.common.entity.npc.LOTREntityNPCRideable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class LOTRContainerNPCMountInventory extends Container
{
    private IInventory theMountInv;
    private LOTREntityNPCRideable theMount;

    public LOTRContainerNPCMountInventory(IInventory playerInv, final IInventory mountInv, final LOTREntityNPCRideable mount)
    {
    	theMountInv = mountInv;
        theMount = mount;
        mountInv.openInventory();
        
        addSlotToContainer(new Slot(mountInv, 0, 8, 18)
        {
        	@Override
            public boolean isItemValid(ItemStack itemstack)
            {
                return super.isItemValid(itemstack) && itemstack.getItem() == Items.saddle && !getHasStack();
            }
        });
        
        addSlotToContainer(new Slot(mountInv, 1, 8, 36)
        {
            @Override
            public boolean isItemValid(ItemStack itemstack)
            {
                return super.isItemValid(itemstack) && mount.isMountArmorValid(itemstack);
            }
        });
        
        int chestRows = 3;
        int yOffset = (chestRows - 4) * 18;

        for (int j = 0; j < 3; j++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(playerInv, k + j * 9 + 9, 8 + k * 18, 102 + j * 18 + yOffset));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(playerInv, j, 8 + j * 18, 160 + yOffset));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return theMountInv.isUseableByPlayer(entityplayer) && theMount.isEntityAlive() && theMount.getDistanceToEntity(entityplayer) < 8F;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int slotIndex)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex < theMountInv.getSizeInventory())
            {
                if (!mergeItemStack(itemstack1, theMountInv.getSizeInventory(), inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (getSlot(1).isItemValid(itemstack1) && !getSlot(1).getHasStack())
            {
                if (!mergeItemStack(itemstack1, 1, 2, false))
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
            else if (theMountInv.getSizeInventory() <= 2 || !mergeItemStack(itemstack1, 2, theMountInv.getSizeInventory(), false))
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
        theMountInv.closeInventory();
    }
}