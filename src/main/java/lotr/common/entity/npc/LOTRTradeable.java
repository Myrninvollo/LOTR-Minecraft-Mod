package lotr.common.entity.npc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface LOTRTradeable
{
	public boolean canTradeWith(EntityPlayer entityplayer);
	
	public String getNPCName();
	
	public void onPlayerBuyItem(EntityPlayer entityplayer, ItemStack itemstack);
	
	public void onPlayerSellItem(EntityPlayer entityplayer, ItemStack itemstack);
	
	public boolean shouldTraderRespawn();
}
