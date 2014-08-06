package lotr.common.world.biome;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.world.structure.LOTRWorldGenMordorTower;

public class LOTRBiomeGenMordorMountains extends LOTRBiomeGenMordor
{
	public LOTRBiomeGenMordorMountains(int i)
	{
		super(i);
		
		decorator.clearRandomStructures();
		decorator.addRandomStructure(new LOTRWorldGenMordorTower(false), 400);
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return null;
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return null;
	}
}
