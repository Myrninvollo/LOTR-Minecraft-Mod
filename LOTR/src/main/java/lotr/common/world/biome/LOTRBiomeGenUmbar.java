package lotr.common.world.biome;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.world.LOTRBanditSpawner;

public class LOTRBiomeGenUmbar extends LOTRBiome
{
	public LOTRBiomeGenUmbar(int i)
	{
		super(i);
		
		spawnableEvilList.clear();
		
		setGoodEvilWeight(0, 100);
		
		decorator.grassPerChunk = 3;
		decorator.doubleGrassPerChunk = 1;
		decorator.flowersPerChunk = 1;
		
		setBanditChance(LOTRBanditSpawner.RARE);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterUmbar;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.UMBAR;
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.15F;
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.05F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0.2F;
	}
}
