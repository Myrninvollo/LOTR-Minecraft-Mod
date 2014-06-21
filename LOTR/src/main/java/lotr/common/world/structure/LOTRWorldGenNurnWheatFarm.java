package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenNurnWheatFarm extends LOTRWorldGenNurnFarmBase
{
	public LOTRWorldGenNurnWheatFarm(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public void generateCrops(World world, Random random, int i, int j, int k)
	{
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				if (Math.abs(i1 - i) == 4 && Math.abs(k1 - k) == 4)
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRMod.brick, 0);
					placeOrcTorch(world, i1, j + 2, k1);
				}
				else if (Math.abs(i1 - i) <= 1 && Math.abs(k1 - k) <= 1)
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRMod.brick, 0);
					
					if (Math.abs(i1 - i) != 0 && Math.abs(k1 - k) != 0)
					{
						placeOrcTorch(world, i1, j + 2, k1);
					}
				}
				else if (i1 == i || k1 == k)
				{
					if (Math.abs(i1 - i) <= 3 && Math.abs(k1 - k) <= 3)
					{
						setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.flowing_water, 0);
					}
				}
				else
				{
					setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.farmland, 7);
					setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.wheat, 7);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 1, k, LOTRMod.morgulTable, 0);
	}
}
