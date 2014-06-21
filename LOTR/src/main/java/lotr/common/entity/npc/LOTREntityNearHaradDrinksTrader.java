package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import net.minecraft.world.World;

public class LOTREntityNearHaradDrinksTrader extends LOTREntityNearHaradBazaarTrader
{
	public LOTREntityNearHaradDrinksTrader(World world)
	{
		super(world, LOTRTradeEntry.NEAR_HARAD_DRINKS_TRADER_BUY, LOTRTradeEntry.NEAR_HARAD_DRINKS_TRADER_SELL, LOTRMod.mug);
	}
}
