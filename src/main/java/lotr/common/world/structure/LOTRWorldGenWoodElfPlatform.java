package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenWoodElfPlatform extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenWoodElfPlatform(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		int rotation = -1;
		
		if (restrictions)
		{
			rotation = random.nextInt(4);
			switch (rotation)
			{
				case 0:
					k -= 3;
					break;
				case 1:
					i += 3;
					break;
				case 2:
					k += 3;
					break;
				case 3:
					i -= 3;
					break;
			}
		}
		else if (usingPlayer != null)
		{
			rotation = usingPlayerRotation();
		}
		
		switch (rotation)
		{
			case 0:
				return generateFacingSouth(world, random, i, j, k);
			case 1:
				return generateFacingWest(world, random, i, j, k);
			case 2:
				return generateFacingNorth(world, random, i, j, k);
			case 3:
				return generateFacingEast(world, random, i, j, k);
		}
		
		return false;
	}
	
	private boolean generateFacingSouth(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				for (int j1 = j; j1 <= j + 4; j1++)
				{
					if (world.getBlock(i1, j1, k + 1) != LOTRMod.wood || world.getBlockMetadata(i1, j1, k + 1) != 2)
					{
						return false;
					}
					
					for (int k1 = k; k1 >= k - 3; k1--)
					{
						if (!world.isAirBlock(i1, j1, k1))
						{
							return false;
						}
					}
				}
			}
		}
		else
		{
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				for (int j1 = j; j1 <= j + 4; j1++)
				{
					for (int k1 = k; k1 >= k - 3; k1--)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k; k1 >= k - 2; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.planks, 2);
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 3, LOTRMod.stairsMirkOak, 6);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 3, LOTRMod.fence, 2);
		}
		
		for (int k1 = k; k1 >= k - 2; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 2, j, k1, LOTRMod.stairsMirkOak, 4);
			setBlockAndNotifyAdequately(world, i + 2, j, k1, LOTRMod.stairsMirkOak, 5);
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k1, LOTRMod.fence, 2);
			setBlockAndNotifyAdequately(world, i + 2, j + 1, k1, LOTRMod.fence, 2);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i - 2, j + 3, k, LOTRMod.woodElvenTorch, 5);
		
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i + 2, j + 3, k, LOTRMod.woodElvenTorch, 5);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 3, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i - 2, j + 3, k - 3, LOTRMod.woodElvenTorch, 5);
		
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 3, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i + 2, j + 3, k - 3, LOTRMod.woodElvenTorch, 5);
		
		for (int j1 = j; j1 >= 0; j1--)
		{
			if (!LOTRMod.isOpaque(world, i, j1, k + 1) || (j1 < j && LOTRMod.isOpaque(world, i, j1, k)))
			{
				break;
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j1, k, Blocks.ladder, 2);
			}
		}
		
		return true;
	}
	
	private boolean generateFacingWest(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				for (int j1 = j; j1 <= j + 4; j1++)
				{
					if (world.getBlock(i - 1, j1, k1) != LOTRMod.wood || world.getBlockMetadata(i - 1, j1, k1) != 2)
					{
						return false;
					}
					
					for (int i1 = i; i1 <= i + 3; i1++)
					{
						if (!world.isAirBlock(i1, j1, k1))
						{
							return false;
						}
					}
				}
			}
		}
		else
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				for (int j1 = j; j1 <= j + 4; j1++)
				{
					for (int i1 = i; i1 <= i + 3; i1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int i1 = i; i1 <= i + 2; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.planks, 2);
			}
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 3, j, k1, LOTRMod.stairsMirkOak, 5);
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, LOTRMod.fence, 2);
		}
		
		for (int i1 = i; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 2, LOTRMod.stairsMirkOak, 6);
			setBlockAndNotifyAdequately(world, i1, j, k + 2, LOTRMod.stairsMirkOak, 7);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 2, LOTRMod.fence, 2);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 2, LOTRMod.fence, 2);
		}
		
		setBlockAndNotifyAdequately(world, i, j + 2, k - 2, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i, j + 3, k - 2, LOTRMod.woodElvenTorch, 5);
		
		setBlockAndNotifyAdequately(world, i, j + 2, k + 2, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i, j + 3, k + 2, LOTRMod.woodElvenTorch, 5);
		
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 2, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i + 3, j + 3, k - 2, LOTRMod.woodElvenTorch, 5);
		
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 2, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i + 3, j + 3, k + 2, LOTRMod.woodElvenTorch, 5);
		
		for (int j1 = j; j1 >= 0; j1--)
		{
			if (!LOTRMod.isOpaque(world, i - 1, j1, k) || (j1 < j && LOTRMod.isOpaque(world, i, j1, k)))
			{
				break;
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j1, k, Blocks.ladder, 5);
			}
		}
		
		return true;
	}
	
	private boolean generateFacingNorth(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				for (int j1 = j; j1 <= j + 4; j1++)
				{
					if (world.getBlock(i1, j1, k - 1) != LOTRMod.wood || world.getBlockMetadata(i1, j1, k - 1) != 2)
					{
						return false;
					}
					
					for (int k1 = k; k1 <= k + 3; k1++)
					{
						if (!world.isAirBlock(i1, j1, k1))
						{
							return false;
						}
					}
				}
			}
		}
		else
		{
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				for (int j1 = j; j1 <= j + 4; j1++)
				{
					for (int k1 = k; k1 <= k + 3; k1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k; k1 <= k + 2; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.planks, 2);
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k + 3, LOTRMod.stairsMirkOak, 7);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, LOTRMod.fence, 2);
		}
		
		for (int k1 = k; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j, k1, LOTRMod.stairsMirkOak, 4);
			setBlockAndNotifyAdequately(world, i + 2, j, k1, LOTRMod.stairsMirkOak, 5);
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k1, LOTRMod.fence, 2);
			setBlockAndNotifyAdequately(world, i + 2, j + 1, k1, LOTRMod.fence, 2);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i - 2, j + 3, k, LOTRMod.woodElvenTorch, 5);
		
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i + 2, j + 3, k, LOTRMod.woodElvenTorch, 5);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 3, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i - 2, j + 3, k + 3, LOTRMod.woodElvenTorch, 5);
		
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k + 3, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i + 2, j + 3, k + 3, LOTRMod.woodElvenTorch, 5);
		
		for (int j1 = j; j1 >= 0; j1--)
		{
			if (!LOTRMod.isOpaque(world, i, j1, k - 1) || (j1 < j && LOTRMod.isOpaque(world, i, j1, k)))
			{
				break;
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j1, k, Blocks.ladder, 3);
			}
		}
		
		return true;
	}
	
	private boolean generateFacingEast(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				for (int j1 = j; j1 <= j + 4; j1++)
				{
					if (world.getBlock(i + 1, j1, k1) != LOTRMod.wood || world.getBlockMetadata(i + 1, j1, k1) != 2)
					{
						return false;
					}
					
					for (int i1 = i; i1 >= i - 3; i1--)
					{
						if (!world.isAirBlock(i1, j1, k1))
						{
							return false;
						}
					}
				}
			}
		}
		else
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				for (int j1 = j; j1 <= j + 4; j1++)
				{
					for (int i1 = i; i1 >= i - 3; i1--)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int i1 = i; i1 >= i - 2; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.planks, 2);
			}
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j, k1, LOTRMod.stairsMirkOak, 4);
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k1, LOTRMod.fence, 2);
		}
		
		for (int i1 = i; i1 >= i - 2; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 2, LOTRMod.stairsMirkOak, 6);
			setBlockAndNotifyAdequately(world, i1, j, k + 2, LOTRMod.stairsMirkOak, 7);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 2, LOTRMod.fence, 2);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 2, LOTRMod.fence, 2);
		}
		
		setBlockAndNotifyAdequately(world, i, j + 2, k - 2, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i, j + 3, k - 2, LOTRMod.woodElvenTorch, 5);
		
		setBlockAndNotifyAdequately(world, i, j + 2, k + 2, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i, j + 3, k + 2, LOTRMod.woodElvenTorch, 5);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 2, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k - 2, LOTRMod.woodElvenTorch, 5);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 2, LOTRMod.fence, 2);
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k + 2, LOTRMod.woodElvenTorch, 5);
		
		for (int j1 = j; j1 >= 0; j1--)
		{
			if (!LOTRMod.isOpaque(world, i + 1, j1, k) || (j1 < j && LOTRMod.isOpaque(world, i, j1, k)))
			{
				break;
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j1, k, Blocks.ladder, 4);
			}
		}
		
		return true;
	}
}
