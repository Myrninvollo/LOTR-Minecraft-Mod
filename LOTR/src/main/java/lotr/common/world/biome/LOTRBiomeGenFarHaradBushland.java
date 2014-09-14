package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenForest;

public class LOTRBiomeGenFarHaradBushland extends LOTRBiomeGenFarHarad
{
	public LOTRBiomeGenFarHaradBushland(int i)
	{
		super(i);
		
		decorator.treesPerChunk = 0;
		decorator.setTreeCluster(3, 3);
		decorator.logsPerChunk = 1;
		decorator.grassPerChunk = 16;
		decorator.doubleGrassPerChunk = 10;
		
		biomeColors.setGrass(0xCCB257);
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		super.decorate(world, random, i, k);
		
		if (random.nextInt(16) == 0)
		{
			int termites = 1 + random.nextInt(4);
			for (int l = 0; l < termites; l++)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				int j1 = world.getHeightValue(i1, k1);
				new LOTRWorldGenBoulder(LOTRMod.termiteMound, 0, 1, 4).generate(world, random, i1, j1, k1);
				for (int x = 0; x < 5; x++)
				{
					int i2 = i1 - random.nextInt(5) + random.nextInt(5);
					int k2 = k1 - random.nextInt(5) + random.nextInt(5);
					int j2 = world.getHeightValue(i2, k2);
					if (world.getBlock(i2, j2 - 1, k1).isOpaqueCube())
					{
						int j3 = j2 + random.nextInt(4);
						for (int j4 = j2; j4 <= j3; j4++)
						{
							world.setBlock(i2, j4, k2, LOTRMod.termiteMound);
							if (world.getBlock(i2, j4 - 1, k2) == Blocks.grass)
							{
								world.setBlock(i2, j4 - 1, k2, Blocks.dirt);
							}
						}
					}
				}
			}
		}
    }
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(4) == 0)
		{
			return new WorldGenForest(false, random.nextBoolean());
		}
		return super.func_150567_a(random);
    }
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.05F;
	}
}
