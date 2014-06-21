package lotr.common.inventory;

import lotr.common.tileentity.LOTRTileEntityBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRContainerBarrel extends Container
{
    public LOTRTileEntityBarrel theBarrel;
	private int barrelMode = 0;
	private int brewingTime = 0;

    public LOTRContainerBarrel(InventoryPlayer inv, LOTRTileEntityBarrel barrel)
    {
        theBarrel = barrel;
		
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				addSlotToContainer(new LOTRSlotBarrel(barrel, j + i * 3, 14 + j * 18, 34 + i * 18));
			}
		}
        
        addSlotToContainer(new LOTRSlotBarrelResult(barrel, 9, 108, 52));

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inv, j + i * 9 + 9, 25 + j * 18, 139 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inv, i, 25 + i * 18, 197));
		}
		
		if (!barrel.getWorldObj().isRemote && inv.player instanceof EntityPlayerMP)
		{
			barrel.players.add(inv.player);
		}
    }

	@Override
    public void addCraftingToCrafters(ICrafting crafting)
    {
        super.addCraftingToCrafters(crafting);
		
		crafting.sendProgressBarUpdate(this, 0, theBarrel.barrelMode);
		crafting.sendProgressBarUpdate(this, 1, theBarrel.brewingTime);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
		
        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting crafting = (ICrafting)crafters.get(i);

            if (barrelMode != theBarrel.barrelMode)
            {
				crafting.sendProgressBarUpdate(this, 0, theBarrel.barrelMode);
            }
			
            if (brewingTime != theBarrel.brewingTime)
            {
				crafting.sendProgressBarUpdate(this, 1, theBarrel.brewingTime);
            }
        }

        barrelMode = theBarrel.barrelMode;
		brewingTime = theBarrel.brewingTime;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int i, int j)
    {
        if (i == 0)
        {
            theBarrel.barrelMode = j;
        }

        if (i == 1)
        {
            theBarrel.brewingTime = j;
        }
    }

	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return theBarrel.isUseableByPlayer(entityplayer);
    }

	@Override
    public ItemStack slotClick(int i, int j, int k, EntityPlayer entityplayer)
    {
		if (i >= 0)
		{
			Slot slot = (Slot)inventorySlots.get(i);
			if (slot instanceof LOTRSlotBarrelResult)
			{
				return null;
			}
		}
		return super.slotClick(i, j, k, entityplayer);
    }
	
	@Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        if (!theBarrel.getWorldObj().isRemote && entityplayer instanceof EntityPlayerMP)
        {
			theBarrel.players.remove(entityplayer);
		}
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
                if (!mergeItemStack(itemstack1, 10, 46, true))
                {
                    return null;
                }
            }
            else if (i >= 10 && i < 37)
            {
                if (!mergeItemStack(itemstack1, 37, 46, false))
                {
                    return null;
                }
            }
            else if (i >= 37 && i < 46)
            {
                if (!mergeItemStack(itemstack1, 10, 37, false))
                {
                    return null;
                }
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
