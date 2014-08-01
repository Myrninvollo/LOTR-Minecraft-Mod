package lotr.common.world.biome;

import lotr.common.LOTRAchievement;
import lotr.common.world.LOTRBanditSpawner;

public class LOTRBiomeGenMirkwoodRiver extends LOTRBiomeGenMirkwood
{
	public LOTRBiomeGenMirkwoodRiver(int i)
	{
		super(i, true);
		
		spawnableCreatureList.clear();
		
		spawnableGoodList.clear();
		spawnableEvilList.clear();
		
		setBanditChance(LOTRBanditSpawner.NEVER);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return null;
	}
}
