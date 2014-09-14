package lotr.common.world.biome;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class LOTRBiomeGenTaiga extends LOTRBiomeGenTundra
{
	public LOTRBiomeGenTaiga(int i)
	{
		super(i);

		decorator.treesPerChunk = 2;
		decorator.flowersPerChunk = 2;
		decorator.grassPerChunk = 8;
		decorator.doubleGrassPerChunk = 2;
		
		registerTaigaFlowers();
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(10) == 0)
		{
			return LOTRWorldGenDeadTrees.newSpruce();
		}
        return random.nextInt(5) == 0 ? new WorldGenTaiga1() : new WorldGenTaiga2(false);
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.25F;
	}
}
