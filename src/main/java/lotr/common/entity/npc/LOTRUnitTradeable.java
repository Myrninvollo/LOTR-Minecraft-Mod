package lotr.common.entity.npc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface LOTRUnitTradeable
{
	public LOTRUnitTradeEntry[] getUnits();
	
	public boolean canTradeWith(EntityPlayer entityplayer);
	
	public String getNPCName();
	
	public void onUnitTrade(EntityPlayer entityplayer);
	
	public boolean shouldTraderRespawn();
	
	public ItemStack createAlignmentReward();
}
