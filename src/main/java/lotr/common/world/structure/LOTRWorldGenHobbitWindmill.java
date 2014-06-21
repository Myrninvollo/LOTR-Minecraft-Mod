package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityHobbit;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class LOTRWorldGenHobbitWindmill extends LOTRWorldGenStructureBase
{
	private Block plankBlock;
	private int plankMeta;
	
	private Block woodBlock;
	private int woodMeta;
	
	public LOTRWorldGenHobbitWindmill(boolean flag)
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
				k += 5;
				break;
			case 1:
				i -= 5;
				break;
			case 2:
				k -= 5;
				break;
			case 3:
				i += 5;
				break;
		}
		
		if (restrictions)
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
					Block block = world.getBlock(i1, j1 - 1, k1);
					if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone)
					{
						return false;
					}
				}
			}
		}
		
		if (random.nextBoolean())
		{
			woodBlock = Blocks.log;
			woodMeta = 0;
			
			plankBlock = Blocks.planks;
			plankMeta = 0;
		}
		else
		{
			woodBlock = LOTRMod.wood;
			woodMeta = 0;
			
			plankBlock = LOTRMod.planks;
			plankMeta = 0;
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				int i2 = Math.abs(i1 - i);
				int k2 = Math.abs(k1 - k);
				
				if ((i2 >= 3 && k2 > 3) || (k2 >= 3 && i2 > 3))
				{
					continue;
				}
				
				Block fillBlock = Blocks.air;
				int fillMeta = 0;
				
				if (i2 == 3 && k2 == 3)
				{
					fillBlock = plankBlock;
					fillMeta = plankMeta;
				}
				else if ((i2 == 4 && k2 == 2) || (i2 == 2 && k2 == 4))
				{
					fillBlock = woodBlock;
					fillMeta = woodMeta;
				}
				else if (i2 == 4 || k2 == 4)
				{
					fillBlock = plankBlock;
					fillMeta = plankMeta;
				}
				else
				{
					fillBlock = Blocks.air;
				}
				
				for (int j1 = j + 4; (j1 >= j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
				{
					if (fillBlock == Blocks.air)
					{
						if (j1 == j + 4 || j1 <= j)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, plankBlock, plankMeta);
						}
						else
						{
							setAir(world, i1, j1, k1);
						}
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, fillBlock, fillMeta);
						if (j1 <= j)
						{
							setGrassToDirt(world, i1, j1 - 1, k1);
						}
					}
				}
			}
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				int i2 = Math.abs(i1 - i);
				int k2 = Math.abs(k1 - k);
				
				if (i2 == 3 && k2 == 3)
				{
					continue;
				}
				
				Block fillBlock = Blocks.air;
				int fillMeta = 0;
				
				if (i2 == 3 && k2 == 3)
				{
					fillBlock = plankBlock;
					fillMeta = plankMeta;
				}
				else if ((i2 == 3 && k2 == 2) || (i2 == 2 && k2 == 3))
				{
					fillBlock = woodBlock;
					fillMeta = woodMeta;
				}
				else if (i2 == 3 || k2 == 3)
				{
					fillBlock = plankBlock;
					fillMeta = plankMeta;
				}
				else
				{
					fillBlock = Blocks.air;
				}
				
				for (int j1 = j + 5; j1 <= j + 8; j1++)
				{
					if (fillBlock == Blocks.air)
					{
						setAir(world, i1, j1, k1);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, fillBlock, fillMeta);
					}
				}
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				int i2 = Math.abs(i1 - i);
				int k2 = Math.abs(k1 - k);
				
				for (int j1 = j + 9; j1 <= j + 12; j1++)
				{
					if (i2 == 2 && k2 == 2)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta);
					}
					else if (i2 == 2 || k2 == 2)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, plankBlock, plankMeta);
					}
					else
					{
						setAir(world, i1, j1, k1);
					}
				}
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				for (int j1 = j + 11; j1 <= j + 12; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, plankBlock, plankMeta);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 10, k, LOTRMod.chandelier, 2);
		
		int originX = i;
		int originY = j + 13;
		int originZ = k;
		int radius = 4;
		
		for (int i1 = originX - radius; i1 <= originX + radius; i1++)
		{
			for (int j1 = originY - radius; j1 <= originY + radius; j1++)
			{
				for (int k1 = originZ - radius; k1 <= originZ + radius; k1++)
				{
					int i2 = i1 - originX;
					int j2 = j1 - originY;
					int k2 = k1 - originZ;
					int dist = i2 * i2 + j2 * j2 + k2 * k2;
					
					if (dist < radius * radius && j1 >= originY)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.stained_hardened_clay, 13);
					}
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 6, k, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 6, k, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i, j + 6, k - 3, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i, j + 6, k + 3, Blocks.glass_pane, 0);
		
		placeFenceTorch(world, i - 2, j + 2, k - 3);
		placeFenceTorch(world, i - 2, j + 2, k + 3);
		placeFenceTorch(world, i + 2, j + 2, k - 3);
		placeFenceTorch(world, i + 2, j + 2, k + 3);
		placeFenceTorch(world, i - 3, j + 2, k - 2);
		placeFenceTorch(world, i + 3, j + 2, k - 2);
		placeFenceTorch(world, i - 3, j + 2, k + 2);
		placeFenceTorch(world, i + 3, j + 2, k + 2);
		
		if (rotation == 0)
		{
			setBlockAndNotifyAdequately(world, i, j + 1, k - 4, Blocks.wooden_door, 1);
			setBlockAndNotifyAdequately(world, i, j + 2, k - 4, Blocks.wooden_door, 8);
			
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 1, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 1, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 1, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 1, Blocks.hay_block, 0);
			
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i, j1, k + 2, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i, j1, k + 1, Blocks.ladder, 2);
			}
			
			setBlockAndNotifyAdequately(world, i + 1, j + 5, k - 2, Blocks.bed, 3);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 2, Blocks.bed, 11);
			
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k - 2, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i - 1, j + 5, k - 2, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k - 2, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i - 1, j + 6, k - 2, Blocks.bookshelf, 0);
			
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k - 1, Blocks.crafting_table, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k + 1, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k + 2, plankBlock, plankMeta);
			
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k + 1, LOTRWorldGenHobbitHole.getRandomFoodBlock(random), 0);
			
			Item drink = LOTRFoods.HOBBIT_DRINK.getRandomFood(random).getItem();
			placeBarrel(world, random, i - 2, j + 6, k + 2, 5, drink);
			
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k + 1, LOTRMod.hobbitOven, 2);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k + 2, LOTRMod.hobbitOven, 2);
			
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i - 2, j + 5, k, LOTRChestContents.HOBBIT_HOLE_STUDY);
			
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i + 2, j + 5, k, LOTRChestContents.HOBBIT_HOLE_LARDER);
			
			setBlockAndNotifyAdequately(world, i, j + 10, k - 3, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i, j + 10, k - 4, Blocks.wool, 15);
			
			for (int j1 = j + 7; j1 <= j + 13; j1++)
			{
				for (int i1 = i - 3; i1 <= i + 3; i1++)
				{
					int j2 = Math.abs(j1 - (j + 10));
					int i2 = Math.abs(i1 - i);
					
					if (j2 == i2 && j2 != 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k - 4, Blocks.wool, 0);
					}
				}
			}
		}
		else if (rotation == 1)
		{
			setBlockAndNotifyAdequately(world, i + 4, j + 1, k, Blocks.wooden_door, 2);
			setBlockAndNotifyAdequately(world, i + 4, j + 2, k, Blocks.wooden_door, 8);
			
			setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 3, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i, j + 1, k - 3, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i, j + 1, k - 2, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 3, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 2, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i - 1, j + 2, k - 3, Blocks.hay_block, 0);
			
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 2, j1, k, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i - 1, j1, k, Blocks.ladder, 5);
			}
			
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k + 1, Blocks.bed, 0);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k + 2, Blocks.bed, 8);
			
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 2, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 1, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i + 2, j + 6, k - 2, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i + 2, j + 6, k - 1, Blocks.bookshelf, 0);
			
			setBlockAndNotifyAdequately(world, i + 1, j + 5, k - 2, Blocks.crafting_table, 0);
			setBlockAndNotifyAdequately(world, i - 1, j + 5, k - 2, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k - 2, plankBlock, plankMeta);
			
			setBlockAndNotifyAdequately(world, i - 1, j + 6, k - 2, LOTRWorldGenHobbitHole.getRandomFoodBlock(random), 0);
			
			Item drink = LOTRFoods.HOBBIT_DRINK.getRandomFood(random).getItem();
			placeBarrel(world, random, i - 2, j + 6, k - 2, 3, drink);
			
			setBlockAndNotifyAdequately(world, i - 1, j + 5, k + 2, LOTRMod.hobbitOven, 2);
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k + 2, LOTRMod.hobbitOven, 2);
			
			setBlockAndNotifyAdequately(world, i, j + 5, k - 2, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i, j + 5, k - 2, LOTRChestContents.HOBBIT_HOLE_STUDY);
			
			setBlockAndNotifyAdequately(world, i, j + 5, k + 2, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i, j + 5, k + 2, LOTRChestContents.HOBBIT_HOLE_LARDER);
			
			setBlockAndNotifyAdequately(world, i + 3, j + 10, k, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i + 4, j + 10, k, Blocks.wool, 15);
			
			for (int j1 = j + 7; j1 <= j + 13; j1++)
			{
				for (int k1 = k - 3; k1 <= k + 3; k1++)
				{
					int j2 = Math.abs(j1 - (j + 10));
					int k2 = Math.abs(k1 - k);
					
					if (j2 == k2 && j2 != 0)
					{
						setBlockAndNotifyAdequately(world, i + 4, j1, k1, Blocks.wool, 0);
					}
				}
			}
		}
		else if (rotation == 2)
		{
			setBlockAndNotifyAdequately(world, i, j + 1, k + 4, Blocks.wooden_door, 3);
			setBlockAndNotifyAdequately(world, i, j + 2, k + 4, Blocks.wooden_door, 8);
			
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 1, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 1, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 1, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 1, Blocks.hay_block, 0);
			
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i, j1, k - 2, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i, j1, k - 1, Blocks.ladder, 3);
			}
			
			setBlockAndNotifyAdequately(world, i + 1, j + 5, k + 2, Blocks.bed, 3);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k + 2, Blocks.bed, 11);
			
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k + 2, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i - 1, j + 5, k + 2, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k + 2, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i - 1, j + 6, k + 2, Blocks.bookshelf, 0);
			
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k + 1, Blocks.crafting_table, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k - 1, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k - 2, plankBlock, plankMeta);
			
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k - 1, LOTRWorldGenHobbitHole.getRandomFoodBlock(random), 0);
			
			Item drink = LOTRFoods.HOBBIT_DRINK.getRandomFood(random).getItem();
			placeBarrel(world, random, i - 2, j + 6, k - 2, 5, drink);
			
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 1, LOTRMod.hobbitOven, 2);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 2, LOTRMod.hobbitOven, 2);
			
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i - 2, j + 5, k, LOTRChestContents.HOBBIT_HOLE_STUDY);
			
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i + 2, j + 5, k, LOTRChestContents.HOBBIT_HOLE_LARDER);
			
			setBlockAndNotifyAdequately(world, i, j + 10, k + 3, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i, j + 10, k + 4, Blocks.wool, 15);
			
			for (int j1 = j + 7; j1 <= j + 13; j1++)
			{
				for (int i1 = i - 3; i1 <= i + 3; i1++)
				{
					int j2 = Math.abs(j1 - (j + 10));
					int i2 = Math.abs(i1 - i);
					
					if (j2 == i2 && j2 != 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k + 4, Blocks.wool, 0);
					}
				}
			}
		}
		else if (rotation == 3)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 1, k, Blocks.wooden_door, 0);
			setBlockAndNotifyAdequately(world, i - 4, j + 2, k, Blocks.wooden_door, 8);
			
			setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 3, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i, j + 1, k - 3, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i, j + 1, k - 2, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 3, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 2, Blocks.hay_block, 0);
			setBlockAndNotifyAdequately(world, i + 1, j + 2, k - 3, Blocks.hay_block, 0);
			
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 2, j1, k, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i + 1, j1, k, Blocks.ladder, 4);
			}
			
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k + 1, Blocks.bed, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k + 2, Blocks.bed, 8);
			
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k - 2, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k - 1, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k - 2, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k - 1, Blocks.bookshelf, 0);
			
			setBlockAndNotifyAdequately(world, i - 1, j + 5, k - 2, Blocks.crafting_table, 0);
			setBlockAndNotifyAdequately(world, i + 1, j + 5, k - 2, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 2, plankBlock, plankMeta);
			
			setBlockAndNotifyAdequately(world, i + 1, j + 6, k - 2, LOTRWorldGenHobbitHole.getRandomFoodBlock(random), 0);
			
			Item drink = LOTRFoods.HOBBIT_DRINK.getRandomFood(random).getItem();
			placeBarrel(world, random, i + 2, j + 6, k - 2, 3, drink);
			
			setBlockAndNotifyAdequately(world, i + 1, j + 5, k + 2, LOTRMod.hobbitOven, 2);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k + 2, LOTRMod.hobbitOven, 2);
			
			setBlockAndNotifyAdequately(world, i, j + 5, k - 2, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i, j + 5, k - 2, LOTRChestContents.HOBBIT_HOLE_STUDY);
			
			setBlockAndNotifyAdequately(world, i, j + 5, k + 2, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i, j + 5, k + 2, LOTRChestContents.HOBBIT_HOLE_LARDER);
			
			setBlockAndNotifyAdequately(world, i - 3, j + 10, k, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i - 4, j + 10, k, Blocks.wool, 15);
			
			for (int j1 = j + 7; j1 <= j + 13; j1++)
			{
				for (int k1 = k - 3; k1 <= k + 3; k1++)
				{
					int j2 = Math.abs(j1 - (j + 10));
					int k2 = Math.abs(k1 - k);
					
					if (j2 == k2 && j2 != 0)
					{
						setBlockAndNotifyAdequately(world, i - 4, j1, k1, Blocks.wool, 0);
					}
				}
			}
		}
		
		LOTREntityHobbit hobbit = new LOTREntityHobbit(world);
		hobbit.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0F, 0F);
		hobbit.setHomeArea(i, j + 1, k, 8);
		hobbit.onSpawnWithEgg(null);
		hobbit.isNPCPersistent = true;
		world.spawnEntityInWorld(hobbit);

		return true;
	}
	
	private void placeFenceTorch(World world, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i, j, k, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i, j + 1, k, Blocks.torch, 5);
	}
}
