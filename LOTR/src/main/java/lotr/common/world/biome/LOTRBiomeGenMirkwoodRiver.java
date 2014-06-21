package lotr.common.world.biome;

import lotr.common.LOTRAchievement;

public class LOTRBiomeGenMirkwoodRiver extends LOTRBiomeGenMirkwood
{
	public LOTRBiomeGenMirkwoodRiver(int i)
	{
		super(i, true);
		
		spawnableCreatureList.clear();
		
		spawnableGoodList.clear();
		spawnableEvilList.clear();
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return null;
	}
}
