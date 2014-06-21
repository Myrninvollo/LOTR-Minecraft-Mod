package lotr.common.world.feature;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenLogs extends WorldGenerator
{
	public LOTRWorldGenLogs()
	{
		super(false);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (!isSuitablePositionForLog(world, i, j, k))
		{
			return false;
		}
		
		int logType = random.nextInt(5);
		if (logType == 0)
		{
			int length = 2 + random.nextInt(6);
			for (int i1 = i; i1 < i + length && isSuitablePositionForLog(world, i1, j, k); i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k, Blocks.log, 4);
				if (world.getBlock(i1, j - 1, k) == Blocks.grass)
				{
					setBlockAndNotifyAdequately(world, i1, j - 1, k, Blocks.dirt, 0);
				}
			}
			return true;
		}
		else if (logType == 1)
		{
			int length = 2 + random.nextInt(6);
			for (int k1 = k; k1 < k + length && isSuitablePositionForLog(world, i, j, k1); k1++)
			{
				setBlockAndNotifyAdequately(world, i, j, k1, Blocks.log, 8);
				if (world.getBlock(i, j - 1, k1) == Blocks.grass)
				{
					setBlockAndNotifyAdequately(world, i, j - 1, k1, Blocks.dirt, 0);
				}
			}
			return true;
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, Blocks.log, 0);
			if (world.getBlock(i, j - 1, k) == Blocks.grass)
			{
				setBlockAndNotifyAdequately(world, i, j - 1, k, Blocks.dirt, 0);
			}
			return true;
		}
	}
	
	private boolean isSuitablePositionForLog(World world, int i, int j, int k)
	{
		if (world.getBlock(i, j - 1, k) != Blocks.grass)
		{
			return false;
		}
		return world.isAirBlock(i, j, k) || world.getBlock(i, j, k) == Blocks.tallgrass;
	}
}
