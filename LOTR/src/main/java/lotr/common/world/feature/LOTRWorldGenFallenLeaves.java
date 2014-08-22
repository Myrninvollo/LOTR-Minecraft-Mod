package lotr.common.world.feature;

import java.util.Random;

import lotr.common.block.LOTRBlockFallenLeaves;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenFallenLeaves extends WorldGenerator
{
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		Block fallenLeaf = null;
		int fallenMeta = 0;
		
		findLeaf:
		for (int l = 0; l < 32; l++)
		{
			int i1 = i - random.nextInt(4) + random.nextInt(4);
			int j1 = j + random.nextInt(8);
			int k1 = k - random.nextInt(4) + random.nextInt(4);
			
			Block block = world.getBlock(i1, j1, k1);
			if (block instanceof BlockLeavesBase)
			{
				int meta = world.getBlockMetadata(i1, j1, k1);
				Object[] obj = LOTRBlockFallenLeaves.forLeaf(block, meta);
				if (obj != null)
				{
					fallenLeaf = (Block)obj[0];
					fallenMeta = (Integer)obj[1];
					break findLeaf;
				}
			}
		}
		
		if (fallenLeaf == null)
		{
			return false;
		}
		else
		{
			
			for (int l = 0; l < 40; l++)
			{
				int i1 = i - random.nextInt(5) + random.nextInt(5);
				int j1 = j - random.nextInt(3) + random.nextInt(3);
				int k1 = k - random.nextInt(5) + random.nextInt(5);
				Block block = world.getBlock(i1, j1 - 1, k1);
				if ((block == Blocks.grass || block == Blocks.dirt) && world.getBlock(i1, j1, k1).isReplaceable(world, i1, j1, k1))
				{
					world.setBlock(i1, j1, k1, fallenLeaf, fallenMeta, 2);
				}
			}
			
			return true;
		}
	}
}
