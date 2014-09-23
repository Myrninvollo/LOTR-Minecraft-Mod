package lotr.common.world.biome;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenCedar;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRBiomeGenNearHaradFertileForest extends LOTRBiomeGenNearHaradFertile
{
	public LOTRBiomeGenNearHaradFertileForest(int i)
	{
		super(i);
		
		decorator.treesPerChunk = 6;
		
		decorator.clearRandomStructures();
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(5) > 0)
		{
			if (random.nextInt(6) == 0)
			{
				return new LOTRWorldGenCedar(false).setMinMaxHeight(15, 30);
			}
			else
			{
				return new LOTRWorldGenCedar(false);
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
