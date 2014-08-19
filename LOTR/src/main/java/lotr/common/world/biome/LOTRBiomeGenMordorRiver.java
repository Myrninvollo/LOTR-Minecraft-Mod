package lotr.common.world.biome;

import lotr.common.LOTRAchievement;

public class LOTRBiomeGenMordorRiver extends LOTRBiomeGenMordor
{
	public LOTRBiomeGenMordorRiver(int i)
	{
		super(i);
		
		spawnableEvilList.clear();
		
		decorator.clearRandomStructures();
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return null;
	}
	
	@Override
	public boolean isGorgoroth()
	{
		return true;
	}
}
