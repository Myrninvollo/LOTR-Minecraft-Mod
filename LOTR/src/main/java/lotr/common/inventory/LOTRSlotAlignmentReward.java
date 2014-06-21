package lotr.common.inventory;

import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRSpeech;
import lotr.common.entity.npc.LOTRUnitTradeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;

public class LOTRSlotAlignmentReward extends LOTRSlotProtected
{
	public static int ALIGNMENT_REQUIRED = 1500;
	public static int REBUY_COST = 2000;
	
	private LOTRContainerUnitTrade theContainer;
	private LOTRUnitTradeable theTrader;
	
    public LOTRSlotAlignmentReward(LOTRContainerUnitTrade container, IInventory inv, int i, int j, int k, LOTRUnitTradeable entity)
    {
        super(inv, i, j, k);
		theContainer = container;
		theTrader = entity;
    }

	@Override
    public void onPickupFromSlot(EntityPlayer entityplayer, ItemStack itemstack)
    {
		LOTRFaction faction = ((LOTREntityNPC)theTrader).getFaction();
		boolean prevTaken = LOTRLevelData.hasTakenAlignmentRewardItem(entityplayer, faction);
		if (prevTaken)
		{
			for (int i = 0; i < REBUY_COST; i++)
			{
				entityplayer.inventory.consumeInventoryItem(LOTRMod.silverCoin);
			}
		}
		else
		{
			if (!entityplayer.worldObj.isRemote)
			{
				LOTRLevelData.setTakenAlignmentRewardItem(entityplayer, faction, true);
			}
		}
		
		super.onPickupFromSlot(entityplayer, itemstack);
		
		if (!entityplayer.worldObj.isRemote)
		{
			ItemStack reward = theTrader.createAlignmentReward();
			putStack(reward);
			((EntityPlayerMP)entityplayer).sendContainerToPlayer(theContainer);
			
			if (!prevTaken)
			{
				entityplayer.addChatMessage(LOTRSpeech.getNamedSpeechForPlayer((LOTREntityNPC)theTrader, "claimAlignmentReward", entityplayer));
			}
		}
    }
}
