package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenRuinedElvenTurret extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenRuinedElvenTurret(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			Block block = world.getBlock(i, j - 1, k);
			if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone)
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
				k += 6;
				break;
			case 1:
				i -= 6;
				break;
			case 2:
				k -= 6;
				break;
			case 3:
				i += 6;
				break;
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				for (int j1 = j; (j1 == j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
				{
					placeRandomBrick(world, random, i1, j1, k1);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
				
				for (int j1 = j + 1; j1 <= j + 7; j1++)
				{
					if (Math.abs(i1 - i) == 4 || Math.abs(k1 - k) == 4)
					{
						placeRandomBrick(world, random, i1, j1, k1);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				if (Math.abs(i1 - i) % 2 == Math.abs(k1 - k) % 2)
				{
					placeRandomPillar(world, random, i1, j, k1);
				}
				else
				{
					setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.double_stone_slab, 0);
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 7; j1++)
		{
			placeRandomPillar(world, random, i - 3, j1, k - 3);
			placeRandomPillar(world, random, i - 3, j1, k + 3);
			placeRandomPillar(world, random, i + 3, j1, k - 3);
			placeRandomPillar(world, random, i + 3, j1, k + 3);
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			placeRandomStairs(world, random, i1, j + 7, k - 4, 2);
			placeRandomStairs(world, random, i1, j + 7, k + 4, 3);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			placeRandomStairs(world, random, i - 4, j + 7, k1, 0);
			placeRandomStairs(world, random, i + 4, j + 7, k1, 1);
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				for (int j1 = j + 7; j1 <= j + 15; j1++)
				{
					if (Math.abs(i1 - i) == 3 || Math.abs(k1 - k) == 3)
					{
						if (j1 - j < 10 || j1 - j > 14 || Math.abs(i1 - i) < 3 || Math.abs(k1 - k) < 3)
						{
							placeRandomBrick(world, random, i1, j1, k1);
						}
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				for (int j1 = j + 16; j1 <= j + 18; j1++)
				{
					if (j1 - j == 16 || Math.abs(i1 - i) == 4 || Math.abs(k1 - k) == 4)
					{
						if (j1 - j == 18 && (Math.abs(i1 - i) % 2 == 1 || Math.abs(k1 - k) % 2 == 1))
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
						}
						else
						{
							placeRandomBrick(world, random, i1, j1, k1);
						}
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			placeRandomStairs(world, random, i1, j + 16, k - 4, 6);
			placeRandomStairs(world, random, i1, j + 16, k + 4, 7);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			placeRandomStairs(world, random, i - 4, j + 16, k1, 4);
			placeRandomStairs(world, random, i + 4, j + 16, k1, 5);
		}
		
		if (rotation == 0)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k - 5, Blocks.double_stone_slab, 0);
				setBlockAndNotifyAdequately(world, i1, j, k - 4, Blocks.double_stone_slab, 0);
			}
			
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				placeRandomBrick(world, random, i - 1, j1, k - 5);
				setBlockAndNotifyAdequately(world, i, j1, k - 5, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i, j1, k - 4, Blocks.air, 0);
				placeRandomBrick(world, random, i + 1, j1, k - 5);
			}
			
			placeRandomStairs(world, random, i - 1, j + 3, k - 5, 0);
			placeRandomBrick(world, random, i, j + 3, k - 5);
			placeRandomStairs(world, random, i + 1, j + 3, k - 5, 1);
			
			for (int i1 = i + 1; i1 <= i + 2; i1++)
			{
				for (int j1 = j + 1; j1 <= j + 7; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k + 3);
				}
			}
		}
		else if (rotation == 1)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 5, j, k1, Blocks.double_stone_slab, 0);
				setBlockAndNotifyAdequately(world, i + 4, j, k1, Blocks.double_stone_slab, 0);
			}
			
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				placeRandomBrick(world, random, i + 5, j1, k - 1);
				setBlockAndNotifyAdequately(world, i + 5, j1, k, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i + 4, j1, k, Blocks.air, 0);
				placeRandomBrick(world, random, i + 5, j1, k + 1);
			}
			
			placeRandomStairs(world, random, i + 5, j + 3, k - 1, 2);
			placeRandomBrick(world, random, i + 5, j + 3, k);
			placeRandomStairs(world, random, i + 5, j + 3, k + 1, 3);
			
			for (int k1 = k - 1; k1 >= k - 2; k1--)
			{
				for (int j1 = j + 1; j1 <= j + 7; j1++)
				{
					placeRandomBrick(world, random, i - 3, j1, k1);
				}
			}
		}
		else if (rotation == 2)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k + 5, Blocks.double_stone_slab, 0);
				setBlockAndNotifyAdequately(world, i1, j, k + 4, Blocks.double_stone_slab, 0);
			}
			
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				placeRandomBrick(world, random, i - 1, j1, k + 5);
				setBlockAndNotifyAdequately(world, i, j1, k + 5, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i, j1, k + 4, Blocks.air, 0);
				placeRandomBrick(world, random, i + 1, j1, k + 5);
			}
			
			placeRandomStairs(world, random, i - 1, j + 3, k + 5, 0);
			placeRandomBrick(world, random, i, j + 3, k + 5);
			placeRandomStairs(world, random, i + 1, j + 3, k + 5, 1);
			
			for (int i1 = i - 1; i1 >= i - 2; i1--)
			{
				for (int j1 = j + 1; j1 <= j + 7; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k - 3);
				}
			}
		}
		else if (rotation == 3)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 5, j, k1, Blocks.double_stone_slab, 0);
				setBlockAndNotifyAdequately(world, i - 4, j, k1, Blocks.double_stone_slab, 0);
			}
			
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				placeRandomBrick(world, random, i - 5, j1, k - 1);
				setBlockAndNotifyAdequately(world, i - 5, j1, k, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i - 4, j1, k, Blocks.air, 0);
				placeRandomBrick(world, random, i - 5, j1, k + 1);
			}
			
			placeRandomStairs(world, random, i - 5, j + 3, k - 1, 2);
			placeRandomBrick(world, random, i - 5, j + 3, k);
			placeRandomStairs(world, random, i - 5, j + 3, k + 1, 3);
			
			for (int k1 = k + 1; k1 <= k + 2; k1++)
			{
				for (int j1 = j + 1; j1 <= j + 7; j1++)
				{
					placeRandomBrick(world, random, i + 3, j1, k1);
				}
			}
		}
		
		return true;
	}
	
	private void placeRandomBrick(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(20) == 0)
		{
			return;
		}
		
		int l = random.nextInt(3);
		switch (l)
		{
			case 0:
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 11);
				break;
			case 1:
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 12);
				break;
			case 2:
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 13);
				break;
		}
	}
	
	private void placeRandomPillar(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(8) == 0)
		{
			return;
		}
		
		if (random.nextInt(3) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.pillar, 2);
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.pillar, 1);
		}
	}
	
	private void placeRandomStairs(World world, Random random, int i, int j, int k, int meta)
	{
		if (random.nextInt(8) == 0)
		{
			return;
		}
		
		int l = random.nextInt(3);
		switch (l)
		{
			case 0:
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.stairsElvenBrick, meta);
				break;
			case 1:
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.stairsElvenBrickMossy, meta);
				break;
			case 2:
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.stairsElvenBrickCracked, meta);
				break;
		}
	}
}
