package lotr.common.world.biome;

import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityRabbit;
import net.minecraft.entity.passive.EntityWolf;

public class LOTRBiomeGenEnedwaithWoodlands extends LOTRBiomeGenEnedwaith
{
	public LOTRBiomeGenEnedwaithWoodlands(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 12, 4, 8));
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityRabbit.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 10, 4, 4));

		decorator.treesPerChunk = 8;
		decorator.flowersPerChunk = 4;
		decorator.doubleFlowersPerChunk = 1;
		decorator.doubleGrassPerChunk = 2;
		
		registerForestFlowers();
	}
}
