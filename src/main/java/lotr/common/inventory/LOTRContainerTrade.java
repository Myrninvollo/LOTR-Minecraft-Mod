package lotr.common.inventory;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRTradeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRContainerTrade extends Container
{
	private IInventory tradeInventory = new InventoryBasic("trade", false, 18);
	public LOTRTradeable theTrader;
	public LOTREntityNPC theEntity;
	private World theWorld;
	
    public LOTRContainerTrade(InventoryPlayer inv, LOTRTradeable trader, World world)
    {
		theTrader = trader;
		theEntity = (LOTREntityNPC)trader;
		theWorld = world;
		
		if (!world.isRemote)
		{
			for (int i = 0; i < theEntity.traderNPCInfo.getBuyTrades().length; i++)
			{
				tradeInventory.setInventorySlotContents(i, theEntity.traderNPCInfo.getBuyTrades()[i].item.copy());
			}
		}
		
		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new LOTRSlotTrade(this, tradeInventory, i, 8 + i * 18, 44, theEntity));
		}
		
		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(tradeInventory, i + 9, 8 + i * 18, 97));
		}

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 156 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inv, i, 8 + i * 18, 214));
        }
    }
	
	@Override
    public void addCraftingToCrafters(ICrafting crafting)
    {
        super.addCraftingToCrafters(crafting);
		theEntity.traderNPCInfo.sendClientPacket((EntityPlayer)crafting);
    }
	
	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
		return theEntity != null && entityplayer.getDistanceToEntity(theEntity) <= 12D && theEntity.isEntityAlive() && theEntity.getAttackTarget() == null && theTrader.canTradeWith(entityplayer);
    }
	
	@Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        if (!theWorld.isRemote)
        {
            for (int i = 9; i < 18; i++)
            {
                ItemStack itemstack = tradeInventory.getStackInSlotOnClosing(i);
                if (itemstack != null)
                {
                    entityplayer.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }
	
	@Override
    public ItemStack slotClick(int i, int j, int k, EntityPlayer entityplayer)
    {
		if (i >= 0)
		{
			Slot slotForTrading = (Slot)inventorySlots.get(i);
			if (slotForTrading instanceof LOTRSlotTrade && ((LOTRSlotTrade)slotForTrading).cost() > 0)
			{
				int coins = 0;
				for (ItemStack itemstack : entityplayer.inventory.mainInventory)
				{
					if (itemstack != null && itemstack.getItem() == LOTRMod.silverCoin)
					{
						coins += itemstack.stackSize;
					}
				}
				if (coins < ((LOTRSlotTrade)slotForTrading).cost())
				{
					return null;
				}
			}
		}
		return super.slotClick(i, j, k, entityplayer);
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
                if (!mergeItemStack(itemstack1, 18, 54, true))
                {
                    return null;
                }
            }
            else if (i >= 18 && i < 45)
            {
                if (!mergeItemStack(itemstack1, 45, 54, false))
                {
                    return null;
                }
            }
            else if (i >= 45 && i < 54)
            {
                if (!mergeItemStack(itemstack1, 18, 45, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 18, 45, false))
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
