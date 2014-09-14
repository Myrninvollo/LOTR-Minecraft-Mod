package lotr.common.world.biome;

import java.util.Random;

import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityButterfly;
import lotr.common.entity.animal.LOTREntityRabbit;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class LOTRBiomeGenMinhiriathWoodlands extends LOTRBiomeGenMinhiriath
{
	public LOTRBiomeGenMinhiriathWoodlands(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 10, 4, 8));
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityButterfly.class, 2, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityRabbit.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 12, 4, 4));

		decorator.treesPerChunk = 8;
		decorator.doubleFlowersPerChunk = 1;
		decorator.grassPerChunk = 10;
		decorator.doubleGrassPerChunk = 3;
		
		registerForestFlowers();
	}

	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(20) == 0)
		{
			return super.func_150567_a(random);
		}
		else if (random.nextInt(5) == 0)
		{
			return new WorldGenTaiga2(false);
		}
		return random.nextInt(10) == 0 ? new WorldGenBigTree(false) : new WorldGenTrees(false);
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.25F;
	}
}
