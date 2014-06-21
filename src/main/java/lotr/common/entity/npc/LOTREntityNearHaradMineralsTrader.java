package lotr.common.entity.npc;

import net.minecraft.init.Items;
import net.minecraft.world.World;

public class LOTREntityNearHaradMineralsTrader extends LOTREntityNearHaradBazaarTrader
{
	public LOTREntityNearHaradMineralsTrader(World world)
	{
		super(world, LOTRTradeEntry.NEAR_HARAD_MINERALS_TRADER_BUY, LOTRTradeEntry.NEAR_HARAD_MINERALS_TRADER_SELL, Items.iron_pickaxe);
	}
}
