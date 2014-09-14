package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.feature.LOTRWorldGenDesertTrees;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class LOTRBiomeGenNearHaradSemiDesert extends LOTRBiomeGenNearHarad
{
	private WorldGenerator dirtPatch = new WorldGenMinable(Blocks.dirt, 1, 50, Blocks.sand);
	
	public LOTRBiomeGenNearHaradSemiDesert(int i)
	{
		super(i);
		
		decorator.grassPerChunk = 5;
	}

	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 2, dirtPatch, 60, 90);
		
        super.decorate(world, random, i, k);
		
		if (random.nextInt(20) == 0)
		{
			int i1 = i + random.nextInt(16) + 8;
			int k1 = k + random.nextInt(16) + 8;
			int j1 = world.getHeightValue(i1, k1) - 1;
			if (world.getBlock(i1, j1, k1) == Blocks.sand)
			{
				world.setBlock(i1, j1, k1, Blocks.dirt);
				WorldGenerator tree = random.nextBoolean() ? new LOTRWorldGenDesertTrees() : new WorldGenSavannaTree(false);
				if (!tree.generate(world, random, i1, j1 + 1, k1))
				{
					world.setBlock(i1, j1, k1, Blocks.sand);
				}
			}
		}
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.05F;
	}
	
	@Override
    public WorldGenerator getRandomWorldGenForGrass(Random random)
    {
        return new WorldGenTallGrass(LOTRMod.aridGrass, 0);
    }
}
