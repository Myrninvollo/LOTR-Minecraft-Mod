package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockBerryBush;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenBerryBush extends WorldGenerator
{
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		Block bush = LOTRMod.berryBush;
		int bushMeta = random.nextInt(LOTRBlockBerryBush.bushTypes.length);
		bushMeta = LOTRBlockBerryBush.setHasBerries(bushMeta, true);
		
		for (int l = 0; l < 12; l++)
		{
			int i1 = i - random.nextInt(4) + random.nextInt(4);
			int j1 = j - random.nextInt(2) + random.nextInt(2);
			int k1 = k - random.nextInt(4) + random.nextInt(4);
			
			Block below = world.getBlock(i1, j1 - 1, k1);
			Block block = world.getBlock(i1, j1, k1);
			
			if ((below == Blocks.grass || below == Blocks.dirt) && !block.getMaterial().isLiquid() && block.isReplaceable(world, i1, j1, k1))
			{
				world.setBlock(i1, j1, k1, bush, bushMeta, 2);
			}
		}
		
		return true;
	}
}
