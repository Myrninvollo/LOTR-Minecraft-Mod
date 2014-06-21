package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRWorldGenCharredTrees extends WorldGenAbstractTree
{
	public LOTRWorldGenCharredTrees()
	{
		super(false);
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		Block id = world.getBlock(i, j - 1, k);
		int meta = world.getBlockMetadata(i, j - 1, k);
		if (!(id == LOTRMod.rock && meta == 0) && id != Blocks.grass && id != Blocks.dirt && id != Blocks.stone && id != Blocks.sand && id != Blocks.gravel)
		{
			return false;
		}

		if (id == Blocks.grass)
		{
			world.setBlock(i, j - 1, k, Blocks.dirt, 0, 2);
		}

		int height = 2 + random.nextInt(5);
		for (int j1 = j; j1 < j + height; j1++)
		{
			world.setBlock(i, j1, k, LOTRMod.wood, 3, 2);
		}
		
		if (height >= 4)
		{
			for (int branch = 0; branch < 4; branch++)
			{
				int branchLength = 2 + random.nextInt(4);
				int branchHorizontalPos = 0;
				int branchVerticalPos = j + height - random.nextInt(2);
				for (int l = 0; l < branchLength; l++)
				{
					if (random.nextInt(4) == 0)
					{
						branchHorizontalPos++;
					}
					if (random.nextInt(3) != 0)
					{
						branchVerticalPos++;
					}
					
					switch (branch)
					{
						case 0:
							world.setBlock(i - branchHorizontalPos, branchVerticalPos, k, LOTRMod.wood, 3 | 12, 2);
							break;
						case 1:
							world.setBlock(i, branchVerticalPos, k + branchHorizontalPos, LOTRMod.wood, 3 | 12, 2);
							break;
						case 2:
							world.setBlock(i + branchHorizontalPos, branchVerticalPos, k, LOTRMod.wood, 3 | 12, 2);
							break;
						case 3:
							world.setBlock(i, branchVerticalPos, k - branchHorizontalPos, LOTRMod.wood, 3 | 12, 2);
							break;
					}
				}
			}
		}
		
		return true;
	}
}
