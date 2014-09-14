package lotr.common.world.biome;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenHugeTrees;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;

public class LOTRBiomeGenDunlandForest extends LOTRBiomeGenDunland
{
	public LOTRBiomeGenDunlandForest(int i)
	{
		super(i);
		
		hasPodzol = true;
		decorator.treesPerChunk = 8;
		decorator.logsPerChunk = 1;
		decorator.flowersPerChunk = 4;
		decorator.grassPerChunk = 8;
		decorator.doubleGrassPerChunk = 2;
		decorator.mushroomsPerChunk = 1;
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(6) == 0)
		{
			if (random.nextBoolean())
			{
				return LOTRWorldGenHugeTrees.newOak();
			}
			else
			{
				if (random.nextInt(4) == 0)
				{
					return new WorldGenMegaPineTree(false, true);
				}
				return new WorldGenMegaPineTree(false, false);
			}
		}
        return super.func_150567_a(random);
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 1F;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return super.spawnCountMultiplier() * 2;
	}
}
