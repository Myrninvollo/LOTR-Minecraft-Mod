package lotr.common.inventory;

import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRUnitTradeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRContainerUnitTrade extends Container
{
	public LOTRUnitTradeable theUnitTrader;
	private LOTRFaction faction;
	private World theWorld;
	private IInventory alignmentRewardInv;
	public int alignmentRewardSlots;
	
    public LOTRContainerUnitTrade(EntityPlayer entityplayer, LOTRUnitTradeable trader, World world)
    {
		theUnitTrader = trader;
		faction = ((LOTREntityNPC)theUnitTrader).getFaction();
		theWorld = world;
		
		ItemStack reward = theUnitTrader.createAlignmentReward();
		boolean hasReward = reward != null;
		alignmentRewardSlots = hasReward ? 1 : 0;
		alignmentRewardInv = new InventoryBasic("specialItem", false, alignmentRewardSlots);

		if (hasReward)
		{
			addSlotToContainer(new LOTRSlotAlignmentReward(this, alignmentRewardInv, 0, 174, 78, theUnitTrader));
			
			if (!world.isRemote && LOTRLevelData.getAlignment(entityplayer, faction) >= LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED)
			{
				alignmentRewardInv.setInventorySlotContents(0, reward);
			}
		}
		
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(entityplayer.inventory, j + i * 9 + 9, 30 + j * 18, 156 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(entityplayer.inventory, i, 30 + i * 18, 214));
        }
    }
	
	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        EntityLiving livingUnitTrader = (EntityLiving)theUnitTrader;
		return livingUnitTrader != null && entityplayer.getDistanceToEntity(livingUnitTrader) <= 12D && livingUnitTrader.isEntityAlive() && livingUnitTrader.getAttackTarget() == null && theUnitTrader.canTradeWith(entityplayer);
    }
	
	@Override
    public ItemStack slotClick(int i, int j, int k, EntityPlayer entityplayer)
    {
		if (i >= 0)
		{
			Slot slot = (Slot)inventorySlots.get(i);
			if (slot instanceof LOTRSlotAlignmentReward)
			{
				if (LOTRLevelData.getAlignment(entityplayer, faction) < LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED)
				{
					return null;
				}
				
				if (slot.getHasStack() && LOTRLevelData.hasTakenAlignmentRewardItem(entityplayer, faction))
				{
					int coins = 0;
					for (ItemStack itemstack : entityplayer.inventory.mainInventory)
					{
						if (itemstack != null && itemstack.getItem() == LOTRMod.silverCoin)
						{
							coins += itemstack.stackSize;
						}
					}
					if (coins < LOTRSlotAlignmentReward.REBUY_COST)
					{
						return null;
					}
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
            if (i >= 0 && i < alignmentRewardSlots)
            {
                if (!mergeItemStack(itemstack1, alignmentRewardSlots, 36 + alignmentRewardSlots, true))
                {
                    return null;
                }
            }
            else if (i >= alignmentRewardSlots && i < 27 + alignmentRewardSlots)
            {
                if (!mergeItemStack(itemstack1, 27 + alignmentRewardSlots, 36 + alignmentRewardSlots, false))
                {
                    return null;
                }
            }
            else if (i >= 27 + alignmentRewardSlots && i < 36 + alignmentRewardSlots)
            {
                if (!mergeItemStack(itemstack1, alignmentRewardSlots, 27 + alignmentRewardSlots, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, alignmentRewardSlots, 27 + alignmentRewardSlots, false))
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
