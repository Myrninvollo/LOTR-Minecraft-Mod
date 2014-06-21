package lotr.common.entity.npc;

import net.minecraft.init.Items;
import net.minecraft.world.World;

public class LOTREntityNearHaradFoodTrader extends LOTREntityNearHaradBazaarTrader
{
	public LOTREntityNearHaradFoodTrader(World world)
	{
		super(world, LOTRTradeEntry.NEAR_HARAD_FOOD_TRADER_BUY, LOTRTradeEntry.NEAR_HARAD_FOOD_TRADER_SELL, Items.apple);
	}
}
