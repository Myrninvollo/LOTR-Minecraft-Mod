package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAINearestAttackableTargetHuorn;
import net.minecraft.world.World;

public class LOTREntityHuorn extends LOTREntityHuornBase
{
	public LOTREntityHuorn(World world)
	{
		super(world);
		addTargetTasks(true, LOTREntityAINearestAttackableTargetHuorn.class);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.FANGORN;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killHuorn;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.HUORN;
	}
}
