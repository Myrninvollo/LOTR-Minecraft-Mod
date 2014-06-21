package lotr.common.world.biome;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenShrub;

public class LOTRBiomeGenHarondorShrubland extends LOTRBiomeGenHarondor
{
	public LOTRBiomeGenHarondorShrubland(int i)
	{
		super(i);

		decorator.treesPerChunk = 8;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 6, harondorDirtPatchGen, 60, 80);
		
		super.decorate(world, random, i, k);
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(20) == 0)
		{
			return super.func_150567_a(random);
		}
		return new WorldGenShrub(0, 0);
    }
}
