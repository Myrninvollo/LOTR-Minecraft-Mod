package lotr.common.world.biome;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenHolly;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRBiomeGenEregionForest extends LOTRBiomeGenEregion
{
	public LOTRBiomeGenEregionForest(int i)
	{
		super(i);
		
		hasPodzol = true;
		decorator.treesPerChunk = 8;
        decorator.flowersPerChunk = 4;
		decorator.doubleFlowersPerChunk = 2;
        decorator.grassPerChunk = 6;
		decorator.doubleGrassPerChunk = 3;
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(6) == 0)
		{
			return new LOTRWorldGenHolly(false).setLarge();
		}
		return super.func_150567_a(random);
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.5F;
	}
}
