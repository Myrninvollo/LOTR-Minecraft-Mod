package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRFaction;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import lotr.common.world.feature.LOTRWorldGenSimpleTrees;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class LOTRBiomeGenLothlorienEdge extends LOTRBiomeGenLothlorien
{
	public LOTRBiomeGenLothlorienEdge(int i)
	{
		super(i);

		decorator.treesPerChunk = 3;
		decorator.flowersPerChunk = 2;
		decorator.grassPerChunk = 5;
		decorator.doubleGrassPerChunk = 1;
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(8) != 0)
		{
			return random.nextInt(6) == 0 ? new WorldGenBigTree(false) : new WorldGenTrees(false);
		}
		else
		{
			return LOTRWorldGenSimpleTrees.newMallorn(false);
		}
    }
	
	@Override
	public int spawnCountMultiplier()
	{
		return 4;
	}
}
