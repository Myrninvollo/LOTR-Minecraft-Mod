package lotr.common.world.biome;

import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityButterfly;
import lotr.common.entity.animal.LOTREntityRabbit;
import net.minecraft.entity.passive.EntityWolf;

public class LOTRBiomeGenRohanWoodlands extends LOTRBiomeGenRohan
{
	public LOTRBiomeGenRohanWoodlands(int i)
	{
		super(i, false);
		
		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 8, 4, 8));
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityButterfly.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityRabbit.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 12, 4, 4));
		
		decorator.treesPerChunk = 5;
		decorator.flowersPerChunk = 3;
		decorator.doubleFlowersPerChunk = 1;
		decorator.grassPerChunk = 10;
		decorator.doubleGrassPerChunk = 3;
		
		registerForestFlowers();
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.75F;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return super.spawnCountMultiplier() * 3;
	}
}
