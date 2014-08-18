package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenRuinedGondorTower extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenRuinedGondorTower(boolean flag)
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
				for (int k1 = k + 3; k1 <= k + 3; k1++)
				{
					int j1 = world.getHeightValue(i1, k1);
					Block block = world.getBlock(i1, j1 - 1, k1);
					if (block != Blocks.grass && !block.isWood(world, i1, j1 - 1, k1) && !block.isLeaves(world, i1, j1 - 1, k1))
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
				if (Math.abs(i1 - i) == 3 || Math.abs(k1 - k) == 3)
				{
					for (int j1 = j + 8; (j1 >= j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
					{
						placeRandomBrick(world, random, i1, j1, k1);
						setGrassToDirt(world, i1, j1 - 1, k1);
					}
				}
				else
				{
					for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
					{
						placeRandomBrick(world, random, i1, j1, k1);
						setGrassToDirt(world, i1, j1 - 1, k1);
					}
					for (int j1 = j + 1; j1 <= j + 8; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
				
				if (Math.abs(i1 - i) < 3 && Math.abs(k1 - k) < 3 && random.nextInt(20) != 0)
				{
					setBlockAndNotifyAdequately(world, i1, j + 5, k1, Blocks.planks, 0);
				}
			}
		}
		

		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 2, Blocks.chest, 0);
		if (random.nextInt(3) == 0)
		{
			LOTRChestContents.fillChest(world, random, i - 2, j + 1, k - 2, LOTRChestContents.GONDOR_FORTRESS_SUPPLIES);
		}
		
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 2, LOTRMod.gondorianTable, 0);
		
		if (random.nextInt(3) != 0)
		{
			setBlockAndNotifyAdequately(world, i + 2, j + 6, k - 2, LOTRMod.strawBed, 10);
			setBlockAndNotifyAdequately(world, i + 2, j + 6, k - 1, LOTRMod.strawBed, 2);
		}
		
		if (random.nextBoolean())
		{
			setBlockAndNotifyAdequately(world, i + 2, j + 6, k + 2, Blocks.anvil, 8);
		}
		
		for (int k1 = k; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k1, LOTRMod.slabSingle, 10);
		}
		
		if (random.nextBoolean())
		{
			setBlockAndNotifyAdequately(world, i - 2, j + 7, k, LOTRMod.mugBlock, 1);
		}
		if (random.nextBoolean())
		{
			setBlockAndNotifyAdequately(world, i - 2, j + 7, k + 1, LOTRMod.plateBlock, 0);
		}
		if (random.nextBoolean())
		{
			setBlockAndNotifyAdequately(world, i - 2, j + 7, k + 2, LOTRMod.barrel, 5);
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				placeRandomBrick(world, random, i1, j + 9, k1);
				
				if ((Math.abs(i1 - i) == 4 && Math.abs(k1 - k) % 2 == 0) || (Math.abs(k1 - k) == 4 && Math.abs(i1 - i) % 2 == 0))
				{
					if (LOTRMod.isOpaque(world, i1, j + 9, k1))
					{
						placeRandomBrick(world, random, i1, j + 10, k1);
					}
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 9; j1++)
		{
			if (rotation == 2)
			{
				if (LOTRMod.isOpaque(world, i + 3, j1, k))
				{
					setBlockAndNotifyAdequately(world, i + 2, j1, k, Blocks.ladder, 4);
				}
			}
			else
			{
				if (LOTRMod.isOpaque(world, i, j1, k + 3))
				{
					setBlockAndNotifyAdequately(world, i, j1, k + 2, Blocks.ladder, 2);
				}
			}
		}
		
		if (rotation == 0)
		{
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 1, j1, k - 3, LOTRMod.brick, 5);
				setBlockAndNotifyAdequately(world, i, j1, k - 3, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i + 1, j1, k - 3, LOTRMod.brick, 5);
			}

			for (int k1 = k - 4; k1 >= k - 7; k1--)
			{
				for (int i1 = i - 2; i1 <= i + 2; i1 += 4)
				{
					int j1 = world.getHeightValue(i1, k1);
					Block id = world.getBlock(i1, j1 - 1, k1);
					if (id == Blocks.grass && random.nextInt(4) != 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.wall, 3 + random.nextInt(3));
					}
				}
			}
		}
		
		if (rotation == 1)
		{
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 3, j1, k - 1, LOTRMod.brick, 5);
				setBlockAndNotifyAdequately(world, i + 3, j1, k, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i + 3, j1, k + 1, LOTRMod.brick, 5);
			}

			for (int i1 = i + 4; i1 <= i + 7; i1++)
			{
				for (int k1 = k - 2; k1 <= k + 2; k1 += 4)
				{
					int j1 = world.getHeightValue(i1, k1);
					Block id = world.getBlock(i1, j1 - 1, k1);
					if (id == Blocks.grass && random.nextInt(4) != 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.wall, 3 + random.nextInt(3));
					}
				}
			}
		}
		
		if (rotation == 2)
		{
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 1, j1, k + 3, LOTRMod.brick, 5);
				setBlockAndNotifyAdequately(world, i, j1, k + 3, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i + 1, j1, k + 3, LOTRMod.brick, 5);
			}

			for (int k1 = k + 4; k1 <= k + 7; k1++)
			{
				for (int i1 = i - 2; i1 <= i + 2; i1 += 4)
				{
					int j1 = world.getHeightValue(i1, k1);
					Block id = world.getBlock(i1, j1 - 1, k1);
					if (id == Blocks.grass && random.nextInt(4) != 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.wall, 3 + random.nextInt(3));
					}
				}
			}
		}
		
		if (rotation == 3)
		{
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 3, j1, k - 1, LOTRMod.brick, 5);
				setBlockAndNotifyAdequately(world, i - 3, j1, k, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i - 3, j1, k + 1, LOTRMod.brick, 5);
			}

			for (int i1 = i - 4; i1 >= i - 7; i1--)
			{
				for (int k1 = k - 2; k1 <= k + 2; k1 += 4)
				{
					int j1 = world.getHeightValue(i1, k1);
					Block id = world.getBlock(i1, j1 - 1, k1);
					if (id == Blocks.grass && random.nextInt(4) != 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.wall, 3 + random.nextInt(3));
					}
				}
			}
		}
		
		int radius = 8;
		int ruinParts = 2 + random.nextInt(6);
		for (int l = 0; l < ruinParts; l++)
		{
			int i1 = i - random.nextInt(radius * 2) + random.nextInt(radius * 2);
			int k1 = k - random.nextInt(radius * 2) + random.nextInt(radius * 2);
			int j1 = world.getHeightValue(i1, k1);
			Block id = world.getBlock(i1, j1 - 1, k1);
			if (id == Blocks.grass)
			{
				int height = 1 + random.nextInt(3);
				boolean flag = true;
				for (int j2 = j1; j2 < j1 + height && flag; j2++)
				{
					flag = !LOTRMod.isOpaque(world, i1, j2, k1);
				}
				if (flag)
				{
					for (int j2 = j1; j2 < j1 + height; j2++)
					{
						setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 2 + random.nextInt(2));
					}
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
			}
		}
		
		return true;
	}
	
	private void placeRandomBrick(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(16) == 0)
		{
			return;
		}
		
		if (random.nextInt(4) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 2 + random.nextInt(2));
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 1);
		}
	}
}
