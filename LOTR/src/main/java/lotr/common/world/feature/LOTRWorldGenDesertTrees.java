package lotr.common.world.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRWorldGenDesertTrees extends WorldGenAbstractTree
{
	public LOTRWorldGenDesertTrees()
	{
		super(false);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		Block id = world.getBlock(i, j - 1, k);
		if (id != Blocks.grass && id != Blocks.dirt && id != Blocks.sand && id != Blocks.stone)
		{
			return false;
		}

		if (id == Blocks.grass || id == Blocks.dirt)
		{
			setBlockAndNotifyAdequately(world, i, j - 1, k, Blocks.dirt, 0);
		}
		
		int height = 2 + random.nextInt(4);
		for (int j1 = j; j1 < j + height; j1++)
		{
			setBlockAndNotifyAdequately(world, i, j1, k, Blocks.log, 0);
		}
		
		for (int branch = 0; branch < 4; branch++)
		{
			int branchLength = 1 + random.nextInt(4);
			int i1 = i;
			int j1 = j + height - random.nextInt(2);
			int k1 = k;
			
			for (int l = 0; l < branchLength; l++)
			{
				if (random.nextInt(3) != 0)
				{
					j1++;
				}
				
				if (random.nextInt(3) != 0)
				{
					switch (branch)
					{
						case 0:
							i1--;
							break;
						case 1:
							k1++;
							break;
						case 2:
							i1++;
							break;
						case 3:
							k1--;
							break;
					}
				}
				
				Block block = world.getBlock(i1, j1, k1);
				if (block == null || world.isAirBlock(i1, j1, k1) || block.isWood(world, i1, j1, k1) || block.isLeaves(world, i1, j1, k1))
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.log, 0);
				}
				else
				{
					break;
				}
			}
			
			byte leafStart = 1;
			byte leafRangeMin = 0;
			for (int j2 = j1 - leafStart; j2 <= j1 + 1; j2++)
			{
				int j3 = j2 - j1;
				int leafRange = leafRangeMin + 1 - j3 / 2;
				for (int i2 = i1 - leafRange; i2 <= i1 + leafRange; i2++)
				{
					int i3 = i2 - i1;
					for (int k2 = k1 - leafRange; k2 <= k1 + leafRange; k2++)
					{
						int k3 = k2 - k1;
						Block block = world.getBlock(i2, j2, k2);
						if ((Math.abs(i3) != leafRange || Math.abs(k3) != leafRange || random.nextInt(2) != 0 && j3 != 0) && block.canBeReplacedByLeaves(world, i2, j2, k2))
						{
							setBlockAndNotifyAdequately(world, i2, j2, k2, Blocks.leaves, 0);
						}
					}
				}
			}
		}
		
		return true;
	}
}
