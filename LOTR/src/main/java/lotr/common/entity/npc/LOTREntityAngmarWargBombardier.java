package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import net.minecraft.world.World;

public class LOTREntityAngmarWargBombardier extends LOTREntityWargBombardier
{
	public LOTREntityAngmarWargBombardier(World world)
	{
		super(world);
	}

	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.ANGMAR;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.ANGMAR_WARG_BONUS;
	}
}
