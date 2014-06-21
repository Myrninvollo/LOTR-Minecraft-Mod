package lotr.common.world.biome;

import java.util.Random;

import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityRabbit;
import lotr.common.world.feature.LOTRWorldGenHugeTrees;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;

public class LOTRBiomeGenRhunForest extends LOTRBiomeGenRhun
{
	public LOTRBiomeGenRhunForest(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 16, 4, 8));
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityRabbit.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 12, 4, 4));

		decorator.treesPerChunk = 8;
		decorator.logsPerChunk = 1;
		decorator.flowersPerChunk = 4;
		decorator.doubleFlowersPerChunk = 1;
		decorator.doubleGrassPerChunk = 2;
		
		registerForestFlowers();
		
		decorator.treesPerChunk = 9;
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(30) == 0)
		{
			return LOTRWorldGenHugeTrees.newOak();
		}
		else if (random.nextInt(3) > 0)
		{
			return new WorldGenBigTree(false);
		}
		else
		{
			return super.func_150567_a(random);
		}
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.5F;
	}
}
