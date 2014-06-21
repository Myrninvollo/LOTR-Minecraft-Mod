package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenGondorObelisk extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenGondorObelisk(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass)
			{
				return false;
			}
		}
		
		j += 2;

		int rotation = random.nextInt(4);
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
		}
		
		switch (rotation)
		{
			case 0:
				k += 4;
				break;
			case 1:
				i -= 4;
				break;
			case 2:
				k -= 4;
				break;
			case 3:
				i += 4;
				break;
		}
		
		if (restrictions)
		{
			for (int i1 = i - 3; i1 <= i + 3; i1++)
			{
				for (int k1 = k - 3; k1 <= k + 3; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
					Block block = world.getBlock(i1, j1 - 1, k1);
					if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone && block != null && !block.isWood(world, i1, j1 - 1, k1) && !block.isLeaves(world, i1, j1 - 1, k1))
					{
						return false;
					}
				}
			}
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					placeRandomBrick(world, random, i1, j1, k1);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				for (int j1 = j + 1; j1 <= j + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.rock, 1);
				}
			}
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			placeRandomStairs(world, random, i1, j + 1, k - 3, 2);
			placeRandomStairs(world, random, i1, j + 1, k + 3, 3);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			placeRandomStairs(world, random, i - 3, j + 1, k1, 0);
			placeRandomStairs(world, random, i + 3, j + 1, k1, 1);
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				for (int j1 = j + 6; j1 <= j + 11; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			placeRandomStairs(world, random, i1, j + 6, k - 2, 2);
			placeRandomStairs(world, random, i1, j + 6, k + 2, 3);
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			placeRandomStairs(world, random, i - 2, j + 6, k1, 0);
			placeRandomStairs(world, random, i + 2, j + 6, k1, 1);
		}
		
		for (int j1 = j + 12; j1 <= j + 15; j1++)
		{
			placeRandomBrick(world, random, i, j1, k);
			placeRandomBrick(world, random, i - 1, j1, k);
			placeRandomBrick(world, random, i + 1, j1, k);
			placeRandomBrick(world, random, i, j1, k - 1);
			placeRandomBrick(world, random, i, j1, k + 1);
		}
		
		placeRandomStairs(world, random, i - 1, j + 16, k, 0);
		placeRandomStairs(world, random, i + 1, j + 16, k, 1);
		placeRandomStairs(world, random, i, j + 16, k - 1, 2);
		placeRandomStairs(world, random, i, j + 16, k + 1, 3);
		
		placeRandomBrick(world, random, i, j + 16, k);
		setBlockAndNotifyAdequately(world, i, j + 17, k, LOTRMod.beacon, 0);
		
		return true;
	}
	
	private void placeRandomBrick(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(4) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 2 + random.nextInt(2));
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 1);
		}
	}
	
	private void placeRandomStairs(World world, Random random, int i, int j, int k, int metadata)
	{
		if (random.nextInt(4) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, random.nextBoolean() ? LOTRMod.stairsGondorBrickMossy : LOTRMod.stairsGondorBrickCracked, metadata);
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.stairsGondorBrick, metadata);
		}
	}
}
