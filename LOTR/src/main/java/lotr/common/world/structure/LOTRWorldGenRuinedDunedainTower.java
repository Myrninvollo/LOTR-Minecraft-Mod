package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenRuinedDunedainTower extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenRuinedDunedainTower(boolean flag)
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
		
		j--;
		
		int rotation = random.nextInt(4);
		int radius = 4 + random.nextInt(2);
		
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
			switch (rotation)
			{
				case 0:
					k += radius;
					break;
				case 1:
					i -= radius;
					break;
				case 2:
					k -= radius;
					break;
				case 3:
					i += radius;
					break;
			}
		}
		
		int sections = 4 + random.nextInt(3);
		int sectionHeight = 4 + random.nextInt(4);
		int maxHeight = (sections - 1) * sectionHeight;
		
		int wallThresholdMin = radius;
		wallThresholdMin *= wallThresholdMin;
		int wallThresholdMax = radius + 1;
		wallThresholdMax *= wallThresholdMax;
		
		for (int i1 = i - radius; i1 <= i + radius; i1++)
		{
			for (int k1 = k - radius; k1 <= k + radius; k1++)
			{
				int i2 = i1 - i;
				int k2 = k1 - k;
				int distSq = i2 * i2 + k2 * k2;
				if (distSq < wallThresholdMax)
				{
					if (distSq >= wallThresholdMin)
					{
						for (int j1 = j - 1; j1 >= 0; j1--)
						{
							Block block = world.getBlock(i1, j1, k1);
							placeRandomBrick(world, random, i1, j1, k1);
							if (block == Blocks.grass || block == Blocks.dirt || block == Blocks.stone)
							{
								break;
							}
							if (!restrictions && block.isOpaqueCube())
							{
								break;
							}
						}
						
						int j2 = j + maxHeight;
						for (int j1 = j; j1 <= j2; j1++)
						{
							if (random.nextInt(20) != 0)
							{
								placeRandomBrick(world, random, i1, j1, k1);
							}
						}
						
						int j3 = j2 + 1 + random.nextInt(3);
						for (int j1 = j2; j1 <= j3; j1++)
						{
							placeRandomBrick(world, random, i1, j1, k1);
						}
					}
					else
					{
						for (int j1 = j + sectionHeight; j1 <= j + maxHeight; j1 += sectionHeight)
						{
							if (random.nextInt(6) != 0)
							{
								setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.stone_slab, 8);
							}
						}
					}
				}
			}
		}
		
		for (int j1 = j + sectionHeight; j1 < j + maxHeight; j1 += sectionHeight)
		{
			for (int j2 = j1 + 2; j2 <= j1 + 3; j2++)
			{
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					placeIronBars(world, random, i1, j2, k - radius);
					placeIronBars(world, random, i1, j2, k + radius);
				}
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					placeIronBars(world, random, i - radius, j2, k1);
					placeIronBars(world, random, i + radius, j2, k1);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + maxHeight, k, Blocks.stone_slab, 8);
		setBlockAndNotifyAdequately(world, i, j + maxHeight + 1, k, Blocks.chest, rotation + 2);
		LOTRChestContents.fillChest(world, random, i, j + maxHeight + 1, k, LOTRChestContents.DUNEDAIN_TOWER);
		
		switch (rotation)
		{
			case 0:
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					int height = j + 1 + random.nextInt(3);
					for (int j1 = j; j1 <= height; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k - radius, Blocks.air, 0);
					}
				}
				break;
			case 1:
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					int height = j + 1 + random.nextInt(3);
					for (int j1 = j; j1 <= height; j1++)
					{
						setBlockAndNotifyAdequately(world, i + radius, j1, k1, Blocks.air, 0);
					}
				}
				break;
			case 2:
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					int height = j + 1 + random.nextInt(3);
					for (int j1 = j; j1 <= height; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k + radius, Blocks.air, 0);
					}
				}
				break;
			case 3:
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					int height = j + 1 + random.nextInt(3);
					for (int j1 = j; j1 <= height; j1++)
					{
						setBlockAndNotifyAdequately(world, i - radius, j1, k1, Blocks.air, 0);
					}
				}
				break;
		}
		
		for (int l = 0; l < 16; l++)
		{
			int i1 = i - random.nextInt(radius * 2) + random.nextInt(radius * 2);
			int k1 = k - random.nextInt(radius * 2) + random.nextInt(radius * 2);
			int j1 = world.getHeightValue(i1, k1);
			if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
			{
				int randomFeature = random.nextInt(4);
				boolean flag = true;
				if (randomFeature == 0)
				{
					if (!LOTRMod.isOpaque(world, i1, j1, k1))
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.stone_slab, random.nextBoolean() ? 0 : 5);
					}
				}
				else
				{
					for (int j2 = j1; j2 < j1 + randomFeature && flag; j2++)
					{
						flag = !LOTRMod.isOpaque(world, i1, j2, k1);
					}
					if (flag)
					{
						for (int j2 = j1; j2 < j1 + randomFeature; j2++)
						{
							placeRandomBrick(world, random, i1, j2, k1);
						}
					}
				}
				
				if (flag)
				{
					setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
				}
			}
		}
		
		return true;
	}
	
	private void placeRandomBrick(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(5) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, Blocks.stonebrick, 1 + random.nextInt(2));
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, Blocks.stonebrick, 0);
		}
	}
	
	private void placeIronBars(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(4) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, Blocks.air, 0);
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, Blocks.iron_bars, 0);
		}
	}
}
