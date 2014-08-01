package lotr.common.world.biome;

import lotr.common.world.LOTRBanditSpawner;

public class LOTRBiomeGenRiver extends LOTRBiome
{
	public LOTRBiomeGenRiver(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		
		spawnableEvilList.clear();
		
		setBanditChance(LOTRBanditSpawner.NEVER);
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0F;
	}
	
	@Override
	public float getChanceToSpawnLavaLakes()
	{
		return 0F;
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.5F;
	}
}
