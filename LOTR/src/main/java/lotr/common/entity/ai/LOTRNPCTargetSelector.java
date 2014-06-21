package lotr.common.entity.ai;

import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class LOTRNPCTargetSelector implements IEntitySelector
{
	private LOTRFaction ownerFaction;
	
	public LOTRNPCTargetSelector(EntityLiving entity)
	{
		ownerFaction = LOTRMod.getNPCFaction(entity);
	}
	
	@Override
	public boolean isEntityApplicable(Entity entity)
	{
		return LOTRMod.getNPCFaction(entity).isEnemy(ownerFaction) && entity.isEntityAlive();
	}
}
