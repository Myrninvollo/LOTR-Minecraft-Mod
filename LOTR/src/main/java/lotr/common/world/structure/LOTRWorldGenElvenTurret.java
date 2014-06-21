package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenElvenTurret extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenElvenTurret(boolean flag)
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
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 11);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
				
				for (int j1 = j + 1; j1 <= j + 7; j1++)
				{
					if (Math.abs(i1 - i) == 4 || Math.abs(k1 - k) == 4)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 11);
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
					setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.pillar, 1);
				}
				else
				{
					setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.double_stone_slab, 0);
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 7; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k - 3, LOTRMod.pillar, 1);
			setBlockAndNotifyAdequately(world, i - 3, j1, k + 3, LOTRMod.pillar, 1);
			setBlockAndNotifyAdequately(world, i + 3, j1, k - 3, LOTRMod.pillar, 1);
			setBlockAndNotifyAdequately(world, i + 3, j1, k + 3, LOTRMod.pillar, 1);
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 7, k - 4, LOTRMod.stairsElvenBrick, 2);
			setBlockAndNotifyAdequately(world, i1, j + 7, k + 4, LOTRMod.stairsElvenBrick, 3);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 7, k1, LOTRMod.stairsElvenBrick, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + 7, k1, LOTRMod.stairsElvenBrick, 1);
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
							setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 11);
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
							setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 11);
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
			setBlockAndNotifyAdequately(world, i1, j + 16, k - 4, LOTRMod.stairsElvenBrick, 6);
			setBlockAndNotifyAdequately(world, i1, j + 16, k + 4, LOTRMod.stairsElvenBrick, 7);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 16, k1, LOTRMod.stairsElvenBrick, 4);
			setBlockAndNotifyAdequately(world, i + 4, j + 16, k1, LOTRMod.stairsElvenBrick, 5);
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
				setBlockAndNotifyAdequately(world, i - 1, j1, k - 5, LOTRMod.brick, 11);
				setBlockAndNotifyAdequately(world, i, j1, k - 5, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i, j1, k - 4, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i + 1, j1, k - 5, LOTRMod.brick, 11);
			}
			
			setBlockAndNotifyAdequately(world, i - 1, j + 3, k - 5, LOTRMod.stairsElvenBrick, 0);
			setBlockAndNotifyAdequately(world, i, j + 3, k - 5, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i + 1, j + 3, k - 5, LOTRMod.stairsElvenBrick, 1);
			
			for (int i1 = i + 1; i1 <= i + 2; i1++)
			{
				for (int j1 = j + 1; j1 <= j + 7; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k + 3, LOTRMod.brick, 11);
				}
				for (int j1 = j + 1; j1 <= j + 16; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k + 2, Blocks.ladder, 2);
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
				setBlockAndNotifyAdequately(world, i + 5, j1, k - 1, LOTRMod.brick, 11);
				setBlockAndNotifyAdequately(world, i + 5, j1, k, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i + 4, j1, k, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i + 5, j1, k + 1, LOTRMod.brick, 11);
			}
			
			setBlockAndNotifyAdequately(world, i + 5, j + 3, k - 1, LOTRMod.stairsElvenBrick, 2);
			setBlockAndNotifyAdequately(world, i + 5, j + 3, k, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i + 5, j + 3, k + 1, LOTRMod.stairsElvenBrick, 3);
			
			for (int k1 = k - 1; k1 >= k - 2; k1--)
			{
				for (int j1 = j + 1; j1 <= j + 7; j1++)
				{
					setBlockAndNotifyAdequately(world, i - 3, j1, k1, LOTRMod.brick, 11);
				}
				for (int j1 = j + 1; j1 <= j + 16; j1++)
				{
					setBlockAndNotifyAdequately(world, i - 2, j1, k1, Blocks.ladder, 5);
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
				setBlockAndNotifyAdequately(world, i - 1, j1, k + 5, LOTRMod.brick, 11);
				setBlockAndNotifyAdequately(world, i, j1, k + 5, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i, j1, k + 4, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i + 1, j1, k + 5, LOTRMod.brick, 11);
			}
			
			setBlockAndNotifyAdequately(world, i - 1, j + 3, k + 5, LOTRMod.stairsElvenBrick, 0);
			setBlockAndNotifyAdequately(world, i, j + 3, k + 5, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i + 1, j + 3, k + 5, LOTRMod.stairsElvenBrick, 1);
			
			for (int i1 = i - 1; i1 >= i - 2; i1--)
			{
				for (int j1 = j + 1; j1 <= j + 7; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 3, LOTRMod.brick, 11);
				}
				for (int j1 = j + 1; j1 <= j + 16; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 2, Blocks.ladder, 3);
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
				setBlockAndNotifyAdequately(world, i - 5, j1, k - 1, LOTRMod.brick, 11);
				setBlockAndNotifyAdequately(world, i - 5, j1, k, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i - 4, j1, k, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i - 5, j1, k + 1, LOTRMod.brick, 11);
			}
			
			setBlockAndNotifyAdequately(world, i - 5, j + 3, k - 1, LOTRMod.stairsElvenBrick, 2);
			setBlockAndNotifyAdequately(world, i - 5, j + 3, k, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i - 5, j + 3, k + 1, LOTRMod.stairsElvenBrick, 3);
			
			for (int k1 = k + 1; k1 <= k + 2; k1++)
			{
				for (int j1 = j + 1; j1 <= j + 7; j1++)
				{
					setBlockAndNotifyAdequately(world, i + 3, j1, k1, LOTRMod.brick, 11);
				}
				for (int j1 = j + 1; j1 <= j + 16; j1++)
				{
					setBlockAndNotifyAdequately(world, i + 2, j1, k1, Blocks.ladder, 4);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 3, j + 3, k, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i, j + 3, k - 3, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i, j + 3, k + 3, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 18, k, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 3, j + 18, k, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i, j + 18, k - 3, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i, j + 18, k + 3, Blocks.torch, 4);
		
		return true;
	}
}
