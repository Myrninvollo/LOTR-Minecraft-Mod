package lotr.common.world.biome;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.LOTRWaypoint;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import lotr.common.world.structure.LOTRWorldGenBeaconTower;

public class LOTRBiomeGenWhiteMountains extends LOTRBiomeGenGondor
{
	public LOTRBiomeGenWhiteMountains(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		
		spawnableGoodList.clear();
		
		decorator.treesPerChunk = 1;
		decorator.flowersPerChunk = 2;
		decorator.grassPerChunk = 4;
		decorator.doubleGrassPerChunk = 2;
		decorator.generateWater = false;
		
		decorator.clearRandomStructures();
		decorator.addRandomStructure(new LOTRWorldGenBeaconTower(false), 50);
		
		setBanditChance(LOTRBanditSpawner.RARE);
		
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.MORDOR, LOTRInvasionSpawner.RARE));
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterWhiteMountains;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return null;
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return 2;
	}
}
