package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityElf;
import lotr.common.entity.npc.LOTREntityGaladhrimElf;
import lotr.common.world.feature.LOTRWorldGenMallornLarge;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRWorldGenElfHouse extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenElfHouse(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		int rotation = random.nextInt(4);
		
		if (restrictions)
		{
			for (int i1 = i - 7; i1 <= i + 7; i1++)
			{
				for (int j1 = j - 3; j1 <= j + 6; j1++)
				{
					for (int k1 = k - 7; k1 <= k + 7; k1++)
					{
						if (Math.abs(i1 - i) <= 1 && Math.abs(k1 - k) <= 1)
						{
							continue;
						}
						else if (!world.isAirBlock(i1, j1, k1))
						{
							return false;
						}
					}
				}
			}
		}
		else if (usingPlayer != null)
		{
			rotation = usingPlayerRotation();
			switch (rotation)
			{
				case 0:
					k += 2;
					break;
				case 1:
					i -= 2;
					break;
				case 2:
					k -= 2;
					break;
				case 3:
					i += 2;
					break;
			}
			
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.wood, 1);
					}
				}
			}
			
			j--;
			
			LOTRWorldGenMallornLarge treeGen = new LOTRWorldGenMallornLarge(true);
			int j1 = treeGen.generateAndReturnHeight(world, random, i, j, k, true);
			j += MathHelper.floor_double(j1 * MathHelper.randomFloatClamp(random, LOTRWorldGenMallornLarge.HOUSE_HEIGHT_MIN, LOTRWorldGenMallornLarge.HOUSE_HEIGHT_MAX));
		}
		
		for (int i1 = i - 6; i1 <= i + 6; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				for (int k1 = k - 6; k1 <= k + 6; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int j1 = j - 1; j1 <= j + 5; j1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.wood, 1);
				}
			}
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			for (int k1 = k - 5; k1 <= k + 5; k1++)
			{
				if (i1 >= i - 1 && i1 <= i + 1 && k1 >= k - 1 && k1 <= k + 1)
				{
					continue;
				}
				if ((i1 == i - 5 || i1 == i + 5) && (k1 == k - 5 || k1 == k + 5))
				{
					continue;
				}
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.planks, 1);
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 6, LOTRMod.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j, k + 6, LOTRMod.planks, 1);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j, k1, LOTRMod.planks, 1);
			setBlockAndNotifyAdequately(world, i + 6, j, k1, LOTRMod.planks, 1);
		}
		
		for (int j1 = j + 1; j1 <= j + 4; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j1, k - 4, LOTRMod.planks, 1);
			setBlockAndNotifyAdequately(world, i + 4, j1, k - 4, LOTRMod.planks, 1);
			setBlockAndNotifyAdequately(world, i - 4, j1, k + 4, LOTRMod.planks, 1);
			setBlockAndNotifyAdequately(world, i + 4, j1, k + 4, LOTRMod.planks, 1);
			
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 2, LOTRMod.wood, 1);
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 2, LOTRMod.wood, 1);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 2, LOTRMod.wood, 1);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 2, LOTRMod.wood, 1);
			setBlockAndNotifyAdequately(world, i - 2, j1, k - 5, LOTRMod.wood, 1);
			setBlockAndNotifyAdequately(world, i - 2, j1, k + 5, LOTRMod.wood, 1);
			setBlockAndNotifyAdequately(world, i + 2, j1, k - 5, LOTRMod.wood, 1);
			setBlockAndNotifyAdequately(world, i + 2, j1, k + 5, LOTRMod.wood, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 4, LOTRMod.mallornTorch, 1);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 3, LOTRMod.mallornTorch, 3);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 4, LOTRMod.mallornTorch, 2);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 3, LOTRMod.mallornTorch, 3);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 4, LOTRMod.mallornTorch, 1);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 3, LOTRMod.mallornTorch, 4);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 4, LOTRMod.mallornTorch, 2);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 3, LOTRMod.mallornTorch, 4);
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 6, LOTRMod.fence, 1);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 6, LOTRMod.fence, 1);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k1, LOTRMod.fence, 1);
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k1, LOTRMod.fence, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 5, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 5, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 5, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k + 5, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 5, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 5, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 5, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k + 5, LOTRMod.fence, 1);
		
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 3, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 4, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 3, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 4, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 3, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 4, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 3, LOTRMod.fence, 1);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 4, LOTRMod.fence, 1);
		
		setBlockAndNotifyAdequately(world, i - 5, j + 4, k - 1, LOTRMod.stairsMallorn, 7);
		setBlockAndNotifyAdequately(world, i - 5, j + 4, k, LOTRMod.woodSlabSingle, 9);
		setBlockAndNotifyAdequately(world, i - 5, j + 4, k + 1, LOTRMod.stairsMallorn, 6);
		
		setBlockAndNotifyAdequately(world, i + 5, j + 4, k - 1, LOTRMod.stairsMallorn, 7);
		setBlockAndNotifyAdequately(world, i + 5, j + 4, k, LOTRMod.woodSlabSingle, 9);
		setBlockAndNotifyAdequately(world, i + 5, j + 4, k + 1, LOTRMod.stairsMallorn, 6);
		
		setBlockAndNotifyAdequately(world, i - 1, j + 4, k - 5, LOTRMod.stairsMallorn, 5);
		setBlockAndNotifyAdequately(world, i, j + 4, k - 5, LOTRMod.woodSlabSingle, 9);
		setBlockAndNotifyAdequately(world, i + 1, j + 4, k - 5, LOTRMod.stairsMallorn, 4);
		
		setBlockAndNotifyAdequately(world, i - 1, j + 4, k + 5, LOTRMod.stairsMallorn, 5);
		setBlockAndNotifyAdequately(world, i, j + 4, k + 5, LOTRMod.woodSlabSingle, 9);
		setBlockAndNotifyAdequately(world, i + 1, j + 4, k + 5, LOTRMod.stairsMallorn, 4);
		
		for (int i1 = i - 5; i1 <= i - 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 5, LOTRMod.stairsMallorn, 6);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 5, LOTRMod.stairsMallorn, 7);
		}
		
		for (int i1 = i + 5; i1 >= i + 3; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 5, LOTRMod.stairsMallorn, 6);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 5, LOTRMod.stairsMallorn, 7);
		}
		
		for (int k1 = k - 4; k1 <= k - 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j + 4, k1, LOTRMod.stairsMallorn, 4);
			setBlockAndNotifyAdequately(world, i + 5, j + 4, k1, LOTRMod.stairsMallorn, 5);
		}
		
		for (int k1 = k + 4; k1 >= k + 3; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 5, j + 4, k1, LOTRMod.stairsMallorn, 4);
			setBlockAndNotifyAdequately(world, i + 5, j + 4, k1, LOTRMod.stairsMallorn, 5);
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			if (i1 == i)
			{
				continue;
			}
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 4, LOTRMod.stairsMallorn, 7);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 4, LOTRMod.stairsMallorn, 6);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			if (k1 == k)
			{
				continue;
			}
			setBlockAndNotifyAdequately(world, i - 4, j + 4, k1, LOTRMod.stairsMallorn, 5);
			setBlockAndNotifyAdequately(world, i + 4, j + 4, k1, LOTRMod.stairsMallorn, 4);
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			for (int k1 = k - 5; k1 <= k + 5; k1++)
			{
				if (restrictions && i1 >= i - 1 && i1 <= i + 1 && k1 >= k - 1 && k1 <= k + 1)
				{
					continue;
				}
				if ((i1 == i - 5 || i1 == i + 5) && (k1 == k - 5 || k1 == k + 5))
				{
					continue;
				}
				setBlockAndNotifyAdequately(world, i1, j + 5, k1, LOTRMod.planks, 1);
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 6, LOTRMod.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 6, LOTRMod.planks, 1);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j + 5, k1, LOTRMod.planks, 1);
			setBlockAndNotifyAdequately(world, i + 6, j + 5, k1, LOTRMod.planks, 1);
		}
		
		for (int i1 = i - 7; i1 <= i + 7; i1++)
		{
			int k1;
			int i2 = Math.abs(i1 - i);
			if (i2 == 7)
			{
				k1 = 3;
			}
			else if (i2 == 6)
			{
				k1 = 5;
			}
			else if (i2 > 3)
			{
				k1 = 6;
			}
			else
			{
				k1 = 7;
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 5, k - k1, LOTRMod.stairsMallorn, 2);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + k1, LOTRMod.stairsMallorn, 3);
		}
		
		for (int k1 = k - 7; k1 <= k + 7; k1++)
		{
			int i1;
			int k2 = Math.abs(k1 - k);
			if (k2 == 7)
			{
				i1 = 3;
			}
			else if (k2 == 6)
			{
				i1 = 5;
			}
			else if (k2 > 3)
			{
				i1 = 6;
			}
			else
			{
				i1 = 7;
			}
			
			setBlockAndNotifyAdequately(world, i - i1, j + 5, k1, LOTRMod.stairsMallorn, 0);
			setBlockAndNotifyAdequately(world, i + i1, j + 5, k1, LOTRMod.stairsMallorn, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 5, k - 6, LOTRMod.stairsMallorn, 2);
		setBlockAndNotifyAdequately(world, i - 5, j + 5, k - 5, LOTRMod.stairsMallorn, 2);
		setBlockAndNotifyAdequately(world, i - 6, j + 5, k - 3, LOTRMod.stairsMallorn, 2);
		setBlockAndNotifyAdequately(world, i - 6, j + 5, k + 3, LOTRMod.stairsMallorn, 3);
		setBlockAndNotifyAdequately(world, i - 5, j + 5, k + 5, LOTRMod.stairsMallorn, 3);
		setBlockAndNotifyAdequately(world, i - 3, j + 5, k + 6, LOTRMod.stairsMallorn, 3);
		
		setBlockAndNotifyAdequately(world, i + 3, j + 5, k - 6, LOTRMod.stairsMallorn, 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 5, k - 5, LOTRMod.stairsMallorn, 2);
		setBlockAndNotifyAdequately(world, i + 6, j + 5, k - 3, LOTRMod.stairsMallorn, 2);
		setBlockAndNotifyAdequately(world, i + 6, j + 5, k + 3, LOTRMod.stairsMallorn, 3);
		setBlockAndNotifyAdequately(world, i + 5, j + 5, k + 5, LOTRMod.stairsMallorn, 3);
		setBlockAndNotifyAdequately(world, i + 3, j + 5, k + 6, LOTRMod.stairsMallorn, 3);
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				if (restrictions && i1 >= i - 1 && i1 <= i + 1 && k1 >= k - 1 && k1 <= k + 1)
				{
					continue;
				}
				if ((i1 == i - 4 || i1 == i + 4) && (k1 == k - 4 || k1 == k + 4))
				{
					continue;
				}
				setBlockAndNotifyAdequately(world, i1, j + 6, k1, LOTRMod.planks, 1);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 6, k - 5, LOTRMod.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j + 6, k + 5, LOTRMod.planks, 1);
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j + 6, k1, LOTRMod.planks, 1);
			setBlockAndNotifyAdequately(world, i + 5, j + 6, k1, LOTRMod.planks, 1);
		}
		
		for (int i1 = i - 6; i1 <= i + 6; i1++)
		{
			int k1;
			int i2 = Math.abs(i1 - i);
			if (i2 == 6)
			{
				k1 = 2;
			}
			else if (i2 == 5)
			{
				k1 = 4;
			}
			else if (i2 > 2)
			{
				k1 = 5;
			}
			else
			{
				k1 = 6;
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 6, k - k1, LOTRMod.stairsMallorn, 2);
			setBlockAndNotifyAdequately(world, i1, j + 6, k + k1, LOTRMod.stairsMallorn, 3);
		}
		
		for (int k1 = k - 6; k1 <= k + 6; k1++)
		{
			int i1;
			int k2 = Math.abs(k1 - k);
			if (k2 == 6)
			{
				i1 = 2;
			}
			else if (k2 == 5)
			{
				i1 = 4;
			}
			else if (k2 > 2)
			{
				i1 = 5;
			}
			else
			{
				i1 = 6;
			}
			
			setBlockAndNotifyAdequately(world, i - i1, j + 6, k1, LOTRMod.stairsMallorn, 0);
			setBlockAndNotifyAdequately(world, i + i1, j + 6, k1, LOTRMod.stairsMallorn, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 6, k - 5, LOTRMod.stairsMallorn, 2);
		setBlockAndNotifyAdequately(world, i - 4, j + 6, k - 4, LOTRMod.stairsMallorn, 2);
		setBlockAndNotifyAdequately(world, i - 5, j + 6, k - 2, LOTRMod.stairsMallorn, 2);
		setBlockAndNotifyAdequately(world, i - 5, j + 6, k + 2, LOTRMod.stairsMallorn, 3);
		setBlockAndNotifyAdequately(world, i - 4, j + 6, k + 4, LOTRMod.stairsMallorn, 3);
		setBlockAndNotifyAdequately(world, i - 2, j + 6, k + 5, LOTRMod.stairsMallorn, 3);
		
		setBlockAndNotifyAdequately(world, i + 2, j + 6, k - 5, LOTRMod.stairsMallorn, 2);
		setBlockAndNotifyAdequately(world, i + 4, j + 6, k - 4, LOTRMod.stairsMallorn, 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 6, k - 2, LOTRMod.stairsMallorn, 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 6, k + 2, LOTRMod.stairsMallorn, 3);
		setBlockAndNotifyAdequately(world, i + 4, j + 6, k + 4, LOTRMod.stairsMallorn, 3);
		setBlockAndNotifyAdequately(world, i + 2, j + 6, k + 5, LOTRMod.stairsMallorn, 3);
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 2, LOTRMod.stairsMallorn, 6);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 2, LOTRMod.stairsMallorn, 7);
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j + 4, k1, LOTRMod.stairsMallorn, 4);
			setBlockAndNotifyAdequately(world, i + 2, j + 4, k1, LOTRMod.stairsMallorn, 5);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j - 3, k, LOTRMod.wood, 5);
		setBlockAndNotifyAdequately(world, i - 2, j - 2, k - 1, LOTRMod.wood, 5);
		setBlockAndNotifyAdequately(world, i - 2, j - 2, k + 1, LOTRMod.wood, 5);
		for (int i1 = i - 2; i1 >= i - 3; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j - 2, k, LOTRMod.wood, 5);
			setBlockAndNotifyAdequately(world, i1, j - 1, k - 1, LOTRMod.wood, 5);
			setBlockAndNotifyAdequately(world, i1, j - 1, k + 1, LOTRMod.wood, 5);
		}
		for (int i1 = i - 2; i1 >= i - 5; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j - 1, k, LOTRMod.wood, 5);
		}
		
		setBlockAndNotifyAdequately(world, i + 2, j - 3, k, LOTRMod.wood, 5);
		setBlockAndNotifyAdequately(world, i + 2, j - 2, k - 1, LOTRMod.wood, 5);
		setBlockAndNotifyAdequately(world, i + 2, j - 2, k + 1, LOTRMod.wood, 5);
		for (int i1 = i + 2; i1 <= i + 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - 2, k, LOTRMod.wood, 5);
			setBlockAndNotifyAdequately(world, i1, j - 1, k - 1, LOTRMod.wood, 5);
			setBlockAndNotifyAdequately(world, i1, j - 1, k + 1, LOTRMod.wood, 5);
		}
		for (int i1 = i + 2; i1 <= i + 5; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - 1, k, LOTRMod.wood, 5);
		}
		
		setBlockAndNotifyAdequately(world, i, j - 3, k - 2, LOTRMod.wood, 9);
		setBlockAndNotifyAdequately(world, i - 1, j - 2, k - 2, LOTRMod.wood, 9);
		setBlockAndNotifyAdequately(world, i + 1, j - 2, k - 2, LOTRMod.wood, 9);
		for (int k1 = k - 2; k1 >= k - 3; k1--)
		{
			setBlockAndNotifyAdequately(world, i, j - 2, k1, LOTRMod.wood, 9);
			setBlockAndNotifyAdequately(world, i - 1, j - 1, k1, LOTRMod.wood, 9);
			setBlockAndNotifyAdequately(world, i + 1, j - 1, k1, LOTRMod.wood, 9);
		}
		for (int k1 = k - 2; k1 >= k - 5; k1--)
		{
			setBlockAndNotifyAdequately(world, i, j - 1, k1, LOTRMod.wood, 9);
		}
		
		setBlockAndNotifyAdequately(world, i, j - 3, k + 2, LOTRMod.wood, 9);
		setBlockAndNotifyAdequately(world, i - 1, j - 2, k + 2, LOTRMod.wood, 9);
		setBlockAndNotifyAdequately(world, i + 1, j - 2, k + 2, LOTRMod.wood, 9);
		for (int k1 = k + 2; k1 <= k + 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i, j - 2, k1, LOTRMod.wood, 9);
			setBlockAndNotifyAdequately(world, i - 1, j - 1, k1, LOTRMod.wood, 9);
			setBlockAndNotifyAdequately(world, i + 1, j - 1, k1, LOTRMod.wood, 9);
		}
		for (int k1 = k + 2; k1 <= k + 5; k1++)
		{
			setBlockAndNotifyAdequately(world, i, j - 1, k1, LOTRMod.wood, 9);
		}
		
		int ladderX = 0;
		int ladderZ = 0;
		int ladderMeta = 0;
		int table1X = 0;
		int table1Z = 0;
		int wood1Meta = 0;
		int table2X = 0;
		int table2Z = 0;
		int wood2Meta = 0;
		int chestX = 0;
		int chestZ = 0;
		int wood3Meta = 0;
		
		switch (rotation)
		{
			case 0:
				ladderZ = -2;
				ladderMeta = 2;
				table1X = -1;
				wood1Meta = 5;
				table2X = 1;
				wood2Meta = 5;
				chestZ = 1;
				wood3Meta = 9;
				break;
			case 1:
				ladderX = 2;
				ladderMeta = 5;
				table1Z = -1;
				wood1Meta = 9;
				table2Z = 1;
				wood2Meta = 9;
				chestX = -1;
				wood3Meta = 5;
				break;
			case 2:
				ladderZ = 2;
				ladderMeta = 3;
				table1X = -1;
				wood1Meta = 5;
				table2X = 1;
				wood2Meta = 5;
				chestZ = -1;
				wood3Meta = 9;
				break;
			case 3:
				ladderX = -2;
				ladderMeta = 4;
				table1Z = -1;
				wood1Meta = 9;
				table2Z = 1;
				wood2Meta = 9;
				chestX = 1;
				wood3Meta = 5;
				break;
		}
		
		for (int j1 = j + 3; j1 >= j - 3 || (!LOTRMod.isOpaque(world, i + ladderX, j1, k + ladderZ) && j1 >= 0); j1--)
		{
			setBlockAndNotifyAdequately(world, i + ladderX, j1, k + ladderZ, LOTRMod.mallornLadder, ladderMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + table1X, j + 1, k + table1Z, LOTRMod.elvenTable, 0);
		setBlockAndNotifyAdequately(world, i + table1X, j + 2, k + table1Z, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + table1X, j + 3, k + table1Z, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + table1X, j + 4, k + table1Z, LOTRMod.wood, wood1Meta);
		
		setBlockAndNotifyAdequately(world, i + table2X, j + 1, k + table2Z, LOTRMod.elvenTable, 0);
		setBlockAndNotifyAdequately(world, i + table2X, j + 2, k + table2Z, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + table2X, j + 3, k + table2Z, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + table2X, j + 4, k + table2Z, LOTRMod.wood, wood2Meta);
		
		setBlockAndNotifyAdequately(world, i + chestX, j + 1, k + chestZ, Blocks.chest, 0);
		setBlockAndNotifyAdequately(world, i + chestX, j + 2, k + chestZ, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + chestX, j + 3, k + chestZ, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + chestX, j + 4, k + chestZ, LOTRMod.wood, wood3Meta);
		
		LOTRChestContents.fillChest(world, random, i + chestX, j + 1, k + chestZ, LOTRChestContents.ELF_HOUSE);
		
		tryPlaceLight(world, i - 6, j - 1, k - 2, random);
		tryPlaceLight(world, i - 6, j - 1, k + 2, random);
		tryPlaceLight(world, i + 6, j - 1, k - 2, random);
		tryPlaceLight(world, i + 6, j - 1, k + 2, random);
		tryPlaceLight(world, i - 2, j - 1, k - 6, random);
		tryPlaceLight(world, i + 2, j - 1, k - 6, random);
		tryPlaceLight(world, i - 2, j - 1, k + 6, random);
		tryPlaceLight(world, i + 2, j - 1, k + 6, random);
		
		placeFlowerPot(world, i - 3, j + 1, k - 4, getRandomPlant(random));
		placeFlowerPot(world, i - 4, j + 1, k - 3, getRandomPlant(random));
		placeFlowerPot(world, i - 4, j + 1, k + 3, getRandomPlant(random));
		placeFlowerPot(world, i - 3, j + 1, k + 4, getRandomPlant(random));
		placeFlowerPot(world, i + 3, j + 1, k - 4, getRandomPlant(random));
		placeFlowerPot(world, i + 4, j + 1, k - 3, getRandomPlant(random));
		placeFlowerPot(world, i + 4, j + 1, k + 3, getRandomPlant(random));
		placeFlowerPot(world, i + 3, j + 1, k + 4, getRandomPlant(random));
		
		int bedRotation = random.nextInt(4);
		switch (bedRotation)
		{
			case 0:
				setBlockAndNotifyAdequately(world, i, j + 1, k - 5, LOTRMod.elvenBed, 1);
				setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 5, LOTRMod.elvenBed, 9);
				break;
			case 1:
				setBlockAndNotifyAdequately(world, i + 5, j + 1, k, LOTRMod.elvenBed, 2);
				setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 1, LOTRMod.elvenBed, 10);
				break;
			case 2:
				setBlockAndNotifyAdequately(world, i, j + 1, k + 5, LOTRMod.elvenBed, 3);
				setBlockAndNotifyAdequately(world, i + 1, j + 1, k + 5, LOTRMod.elvenBed, 11);
				break;
			case 3:
				setBlockAndNotifyAdequately(world, i - 5, j + 1, k, LOTRMod.elvenBed, 0);
				setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 1, LOTRMod.elvenBed, 8);
				break;
		}

		LOTREntityElf elf = new LOTREntityGaladhrimElf(world);
		elf.setLocationAndAngles(i + 0.5D, j + 1, k + 3.5D, 0F, 0F);
		elf.spawnRidingHorse = false;
		elf.onSpawnWithEgg(null);
		elf.setHomeArea(i, j, k, 8);
		elf.isNPCPersistent = true;
		world.spawnEntityInWorld(elf);
		
		return true;
	}
	
	private void tryPlaceLight(World world, int i, int j, int k, Random random)
	{
		int height = 2 + random.nextInt(6);
		for (int j1 = j; j1 >= j - height; j1--)
		{
			if (restrictions)
			{
				if (!world.isAirBlock(i, j1, k))
				{
					return;
				}
				if (j1 == j - height)
				{
					if (!world.isAirBlock(i, j1, k - 1) || !world.isAirBlock(i, j1, k + 1) || !world.isAirBlock(i - 1, j1, k) || !world.isAirBlock(i + 1, j1, k))
					{
						return;
					}
				}
			}
		}
		
		for (int j1 = j; j1 >= j - height; j1--)
		{
			if (j1 == j - height)
			{
				setBlockAndNotifyAdequately(world, i, j1, k, LOTRMod.planks, 1);
				setBlockAndNotifyAdequately(world, i, j1, k - 1, LOTRMod.mallornTorch, 4);
				setBlockAndNotifyAdequately(world, i, j1, k + 1, LOTRMod.mallornTorch, 3);
				setBlockAndNotifyAdequately(world, i - 1, j1, k, LOTRMod.mallornTorch, 2);
				setBlockAndNotifyAdequately(world, i + 1, j1, k, LOTRMod.mallornTorch, 1);
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j1, k, LOTRMod.fence, 1);
			}
		}
	}
	
	private ItemStack getRandomPlant(Random random)
	{
		return random.nextBoolean() ? new ItemStack(LOTRMod.elanor) : new ItemStack(LOTRMod.niphredil);
	}
}
