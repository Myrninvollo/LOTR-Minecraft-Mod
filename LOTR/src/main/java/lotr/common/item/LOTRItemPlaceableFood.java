package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.block.LOTRBlockPlaceableFood;
import net.minecraft.block.Block;
import net.minecraft.item.ItemReed;

public class LOTRItemPlaceableFood extends ItemReed
{
	public LOTRItemPlaceableFood(Block block)
	{
		super(block);
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabFood);
		((LOTRBlockPlaceableFood)block).foodItem = this;
	}
}
