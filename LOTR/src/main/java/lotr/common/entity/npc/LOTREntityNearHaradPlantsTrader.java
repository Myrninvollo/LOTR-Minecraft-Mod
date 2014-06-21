package lotr.common.entity.npc;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class LOTREntityNearHaradPlantsTrader extends LOTREntityNearHaradBazaarTrader
{
	public LOTREntityNearHaradPlantsTrader(World world)
	{
		super(world, LOTRTradeEntry.NEAR_HARAD_PLANTS_TRADER_BUY, LOTRTradeEntry.NEAR_HARAD_PLANTS_TRADER_SELL, Item.getItemFromBlock(Blocks.sapling));
	}
}
