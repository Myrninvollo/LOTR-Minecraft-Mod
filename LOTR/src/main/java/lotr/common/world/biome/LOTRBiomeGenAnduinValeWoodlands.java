package lotr.common.world.biome;

import lotr.common.entity.animal.*;

public class LOTRBiomeGenAnduinValeWoodlands extends LOTRBiomeGenAnduin
{
	public LOTRBiomeGenAnduinValeWoodlands(int i)
	{
		super(i);
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityButterfly.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityRabbit.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 12, 4, 4));

		decorator.treesPerChunk = 6;
		decorator.flowersPerChunk = 6;
		decorator.doubleFlowersPerChunk = 4;
		decorator.grassPerChunk = 12;
		decorator.doubleGrassPerChunk = 6;
		
		registerForestFlowers();
	}
}
