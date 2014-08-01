package lotr.common.entity;

import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;

public class LOTRNPCSelectByFaction implements IEntitySelector
{
	private LOTRFaction faction;
	
	public LOTRNPCSelectByFaction(LOTRFaction f)
	{
		faction = f;
	}
	
	@Override
	public boolean isEntityApplicable(Entity entity)
	{
		return entity.isEntityAlive() && LOTRMod.getNPCFaction(entity) == faction;
	}
}
