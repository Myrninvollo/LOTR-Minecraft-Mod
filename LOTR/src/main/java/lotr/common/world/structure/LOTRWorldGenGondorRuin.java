package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenGondorRuin extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenGondorRuin(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		int rotation = random.nextInt(4);
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
		}
		
		switch (rotation)
		{
			case 0:
				k += 8;
				break;
			case 1:
				i -= 8;
				break;
			case 2:
				k -= 8;
				break;
			case 3:
				i += 8;
				break;
		}
		
		j = world.getTopSolidOrLiquidBlock(i, k);
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass)
			{
				return false;
			}
		}
		
		for (int i1 = i - 7; i1 <= i + 7; i1++)
		{
			for (int k1 = k - 7; k1 <= k + 7; k1++)
			{
				int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
				Block block = world.getBlock(i1, j1 - 1, k1);
				if (block == Blocks.grass || block == Blocks.dirt || block == Blocks.stone)
				{
					if (random.nextInt(3) == 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, LOTRMod.rock, 1);
					}
					
					if (random.nextInt(3) == 0)
					{
						if (random.nextInt(3) == 0)
						{
							placeRandomSlab(world, random, i1, j1, k1);
							setGrassToDirt(world, i1, j1 - 1, k1);
						}
						else
						{
							placeRandomBrick(world, random, i1, j1, k1);
							setGrassToDirt(world, i1, j1 - 1, k1);
						}
					}
					
					if (LOTRMod.isOpaque(world, i1, j1, k1) && random.nextInt(4) == 0)
					{
						if (random.nextInt(5) == 0)
						{
							setBlockAndNotifyAdequately(world, i1, j1 + 1, k1, LOTRMod.wall, 3);
							placeSkull(world, random, i1, j1 + 2, k1);
						}
						else
						{
							if (random.nextInt(3) == 0)
							{
								placeRandomSlab(world, random, i1, j1 + 1, k1);
							}
							else
							{
								placeRandomBrick(world, random, i1, j1 + 1, k1);
							}
						}
					}
				}
			}
		}
		
		for (int i1 = i - 7; i1 <= i + 7; i1 += 7)
		{
			for (int k1 = k - 7; k1 <= k + 7; k1 += 7)
			{
				int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
				
				setGrassToDirt(world, i1, j1 - 1, k1);

				for (int j2 = j1; true; j2++)
				{
					placeRandomBrick(world, random, i1, j2, k1);
					
					if (random.nextInt(4) == 0 || j2 > j1 + 4)
					{
						if (i1 == i && k1 == k)
						{
							setBlockAndNotifyAdequately(world, i1, j2 + 1, k1, LOTRMod.beacon, 0);
						}
						else
						{
							setBlockAndNotifyAdequately(world, i1, j2 + 1, k1, LOTRMod.brick, 5);
						}
						break;
					}
				}
			}
		}
				
		return true;
	}
	
	private void placeRandomBrick(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(20) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 5);
		}
		else if (random.nextInt(4) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 2 + random.nextInt(2));
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 1);
		}
	}
	
	private void placeRandomSlab(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(5) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle, 2);
		}
		else
		{
			if (random.nextInt(4) == 0)
			{
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle, 4 + random.nextInt(2));
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle, 3);
			}
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
