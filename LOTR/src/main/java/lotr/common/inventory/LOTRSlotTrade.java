package lotr.common.inventory;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRTradeEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class LOTRSlotTrade extends LOTRSlotProtected
{
	private LOTRContainerTrade theContainer;
	private LOTREntityNPC theEntity;
	
    public LOTRSlotTrade(LOTRContainerTrade container, IInventory inv, int i, int j, int k, LOTREntityNPC entity)
    {
        super(inv, i, j, k);
		theContainer = container;
		theEntity = entity;
    }
	
	public int cost()
	{
		LOTRTradeEntry trade = theTrade();
		return trade == null ? 0 : trade.cost;
	}
	
	private LOTRTradeEntry theTrade()
	{
		if (theEntity.traderNPCInfo.getBuyTrades() == null)
		{
			return null;
		}
		
		int i = getSlotIndex();
		return (i < 0 || i >= theEntity.traderNPCInfo.getBuyTrades().length) ? null : theEntity.traderNPCInfo.getBuyTrades()[i];
	}

	@Override
    public void onPickupFromSlot(EntityPlayer entityplayer, ItemStack itemstack)
    {
		for (int i = 0; i < cost(); i++)
		{
			entityplayer.inventory.consumeInventoryItem(LOTRMod.silverCoin);
		}
		super.onPickupFromSlot(entityplayer, itemstack);
		if (!entityplayer.worldObj.isRemote && theTrade() != null)
		{
			putStack(theTrade().item.copy());
			((EntityPlayerMP)entityplayer).sendContainerToPlayer(theContainer);
			theContainer.theTrader.onPlayerBuyItem(entityplayer, theTrade().item.copy());
		}
    }
}
