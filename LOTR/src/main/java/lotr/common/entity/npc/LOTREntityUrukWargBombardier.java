package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import net.minecraft.world.World;

public class LOTREntityUrukWargBombardier extends LOTREntityWargBombardier
{
	public LOTREntityUrukWargBombardier(World world)
	{
		super(world);
	}

	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.URUK_HAI;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.URUK_WARG;
	}
}
