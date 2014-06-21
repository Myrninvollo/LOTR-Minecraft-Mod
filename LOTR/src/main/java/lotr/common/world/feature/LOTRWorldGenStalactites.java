package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenStalactites extends WorldGenerator
{
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		for (int l = 0; l < 64; l++)
		{
			int i1 = i - random.nextInt(8) + random.nextInt(8);
			int j1 = j - random.nextInt(4) + random.nextInt(4);
			int k1 = k - random.nextInt(8) + random.nextInt(8);
			
			if (world.isAirBlock(i1, j1, k1))
			{
				if (world.getBlock(i1, j1 + 1, k1) == Blocks.stone)
				{
					world.setBlock(i1, j1, k1, LOTRMod.stalactite, 0, 2);
				}
				else if (world.getBlock(i1, j1 - 1, k1) == Blocks.stone)
				{
					world.setBlock(i1, j1, k1, LOTRMod.stalactite, 1, 2);
				}
			}
		}
		
		return true;
	}
}
