package lotr.common.world.biome;

import java.util.Random;

import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityRabbit;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class LOTRBiomeGenWilderlandForest extends LOTRBiomeGenWilderland
{
	public LOTRBiomeGenWilderlandForest(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 5, 4, 8));
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityRabbit.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 10, 4, 4));
		
		decorator.treesPerChunk = 10;
		decorator.flowersPerChunk = 4;
		decorator.doubleFlowersPerChunk = 2;
		decorator.grassPerChunk = 12;
		decorator.doubleGrassPerChunk = 5;
		
		registerForestFlowers();
	}
	
	@Override
	public WorldGenAbstractTree func_150567_a(Random random)
	{
		if (random.nextInt(5) == 0)
		{
			return random.nextInt(10) == 0 ? new WorldGenBigTree(false) : new WorldGenTrees(false);
		}
		return new WorldGenTaiga2(false);
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.5F;
	}
}
