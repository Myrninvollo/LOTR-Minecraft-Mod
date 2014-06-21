package lotr.common.world.biome;


import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityButterfly;
import lotr.common.entity.animal.LOTREntityRabbit;

public class LOTRBiomeGenLindonWoodlands extends LOTRBiomeGenLindon
{
	public LOTRBiomeGenLindonWoodlands(int i)
	{
		super(i);
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityButterfly.class, 10, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityRabbit.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 12, 4, 4));
		
		decorator.treesPerChunk = 6;
		decorator.flowersPerChunk = 4;
		decorator.doubleFlowersPerChunk = 1;
		decorator.grassPerChunk = 5;
		decorator.doubleGrassPerChunk = 3;
		
		registerForestFlowers();
	}
}
