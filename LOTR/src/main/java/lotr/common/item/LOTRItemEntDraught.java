package lotr.common.item;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRLevelData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRItemEntDraught extends LOTRItemBowlDrink
{
	public LOTRItemEntDraught()
	{
		super();
	}
	
	@Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		if (!world.isRemote && entityplayer.getCurrentEquippedItem() == itemstack)
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.drinkEntDraught);
		}
		return super.onEaten(itemstack, world, entityplayer);
	}
}
