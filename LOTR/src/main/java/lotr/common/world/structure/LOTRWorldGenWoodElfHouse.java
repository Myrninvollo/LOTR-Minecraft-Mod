package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityWoodElf;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class LOTRWorldGenWoodElfHouse extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenWoodElfHouse(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass || world.getBiomeGenForCoords(i, k) != LOTRBiome.mirkwood)
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
		
		if (restrictions)
		{
			int highestHeight = j;
			for (int i1 = i - 6; i1 <= i + 6; i1++)
			{
				for (int k1 = k - 6; k1 <= k + 6; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
					if (world.getBlock(i1, j1 - 1, k1) != Blocks.grass)
					{
						return false;
					}
					
					if (j1 - 1 > highestHeight)
					{
						highestHeight = j1 - 1;
					}
				}
			}
			
			for (int i1 = i - 6; i1 <= i + 6; i1++)
			{
				for (int j1 = j; j1 <= highestHeight; j1++)
				{
					for (int k1 = k - 6; k1 <= k + 6; k1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
		}
		else
		{
			for (int i1 = i - 6; i1 <= i + 6; i1++)
			{
				for (int j1 = j; j1 < j + 4; j1++)
				{
					for (int k1 = k - 6; k1 <= k + 6; k1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
		}
		
		for (int i1 = i - 6; i1 <= i + 6; i1++)
		{
			for (int k1 = k - 6; k1 <= k + 6; k1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					if (j1 == j)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.grass, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.brick, 11);
			}
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					if ((i1 == i - 4 || i1 == i + 4) && (k1 == k - 4 || k1 == k + 4))
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.wood, 2);
					}
					else if (i1 == i - 4 || i1 == i + 4 || k1 == k - 4 || k1 == k + 4)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.planks, 2);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
				
				if (i1 == i - 4 || i1 == i + 4 || k1 == k - 4 || k1 == k + 4)
				{
					setBlockAndNotifyAdequately(world, i1, j + 4, k1, LOTRMod.planks, 2);
				}
				else
				{
					setBlockAndNotifyAdequately(world, i1, j + 4, k1, LOTRMod.leaves, 2 | 4);
				}
			}
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 5, LOTRMod.stairsMirkOak, 2);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 5, LOTRMod.stairsMirkOak, 3);
			
			if (!world.getBlock(i1, j + 1, k - 5).isOpaqueCube())
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k - 5, LOTRMod.leaves, 2 | 4);
			}
			
			if (!world.getBlock(i1, j + 1, k + 5).isOpaqueCube())
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k + 5, LOTRMod.leaves, 2 | 4);
			}
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j + 4, k1, LOTRMod.stairsMirkOak, 0);
			setBlockAndNotifyAdequately(world, i + 5, j + 4, k1, LOTRMod.stairsMirkOak, 1);
			
			if (!world.getBlock(i - 5, j + 1, k1).isOpaqueCube())
			{
				setBlockAndNotifyAdequately(world, i - 5, j + 1, k1, LOTRMod.leaves, 2 | 4);
			}
			
			if (!world.getBlock(i + 5, j + 1, k1).isOpaqueCube())
			{
				setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, LOTRMod.leaves, 2 | 4);
			}
		}
		
		for (int j1 = j; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i, j1, k, LOTRMod.wood, 2);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 1, LOTRMod.woodElvenTorch, 1);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 1, LOTRMod.woodElvenTorch, 1);
		
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 1, LOTRMod.woodElvenTorch, 2);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 1, LOTRMod.woodElvenTorch, 2);
		
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k - 3, LOTRMod.woodElvenTorch, 3);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k - 3, LOTRMod.woodElvenTorch, 3);
		
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 3, LOTRMod.woodElvenTorch, 4);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k + 3, LOTRMod.woodElvenTorch, 4);
		
		int carpetType = 15 - random.nextInt(4);
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j - 5, k1, LOTRMod.brick, 11);
				
				for (int j1 = j - 4; j1 <= j - 1; j1++)
				{
					if (i1 == i - 4 || i1 == i + 4 || k1 == k - 4 || k1 == k + 4)
					{
						if (j1 == j - 3 || j1 == j - 2)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.stone, 0);
						}
						else
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.planks, 2);
						}
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
				
				if (i1 > i - 3 && i1 < i + 3 && k1 > k - 3 && k1 < k + 3)
				{
					setBlockAndNotifyAdequately(world, i1, j - 4, k1, Blocks.carpet, carpetType);
				}
			}
		}
		
		for (int j1 = j - 4; j1 <= j - 1; j1++)
		{
			setBlockAndNotifyAdequately(world, i, j1, k, LOTRMod.wood, 2);
		}
		
		LOTREntityWoodElf elf = new LOTREntityWoodElf(world);
		elf.setLocationAndAngles(i + 1.5D, j + 1, k + 1.5D, 0F, 0F);
		elf.onSpawnWithEgg(null);
		elf.setHomeArea(i, j, k, 8);
		elf.isNPCPersistent = true;
		world.spawnEntityInWorld(elf);
		
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
		
		return true;
	}
	
	private boolean generateFacingSouth(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 4, LOTRMod.wood, 2);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 1, k - 5, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 4, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 4, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k - 5, LOTRMod.woodElvenTorch, 4);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k - 5, LOTRMod.woodElvenTorch, 4);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 4, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 4, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 4, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 4, Blocks.glass_pane, 0);
		
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 3, LOTRMod.woodElvenTable, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 3, Blocks.chest, 0);
		LOTRChestContents.fillChest(world, random, i - 3, j + 1, k - 3, LOTRChestContents.WOOD_ELF_HOUSE);
		
		placeWoodElfItemFrame(world, i - 4, j + 2, k, 3, random);
		placeWoodElfItemFrame(world, i + 4, j + 2, k, 1, random);
		
		for (int k1 = k + 2; k1 <= k + 3; k1++)
		{
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.air, 0);
				int stairHeight = i1 - (i - 2);
				for (int j1 = j - 4; j1 < j - 4 + stairHeight; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 11);
				}
				setBlockAndNotifyAdequately(world, i1, j - 4 + stairHeight, k1, LOTRMod.stairsElvenBrick, 0);
			}
			
			for (int j1 = j - 4; j1 <= j - 1; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 3, j1, k1, LOTRMod.brick, 11);
			}
		}
	
		for (int j1 = j - 3; j1 <= j - 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j1, k - 4, LOTRMod.wood, 2);
			setBlockAndNotifyAdequately(world, i - 1, j1, k - 4, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i, j1, k - 4, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i + 1, j1, k - 4, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i + 1, j1, k - 4, LOTRMod.wood, 2);
		}
	
		setBlockAndNotifyAdequately(world, i - 3, j - 2, k - 3, LOTRMod.woodElvenTorch, 3);
		setBlockAndNotifyAdequately(world, i + 3, j - 2, k - 3, LOTRMod.woodElvenTorch, 3);
		setBlockAndNotifyAdequately(world, i - 3, j - 2, k + 3, LOTRMod.woodElvenTorch, 4);
		setBlockAndNotifyAdequately(world, i + 3, j - 2, k + 1, LOTRMod.woodElvenTorch, 4);
		
		setBlockAndNotifyAdequately(world, i + 3, j - 4, k, LOTRMod.woodElvenBed, 0);
		setBlockAndNotifyAdequately(world, i + 3, j - 4, k + 1, LOTRMod.woodElvenBed, 8);
		
		return true;
	}
	
	private boolean generateFacingWest(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 4, j1, k1, LOTRMod.wood, 2);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k, Blocks.wooden_door, 2);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 1, LOTRMod.woodElvenTorch, 1);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 1, LOTRMod.woodElvenTorch, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 3, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 2, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 2, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 3, Blocks.glass_pane, 0);
		
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 3, LOTRMod.woodElvenTable, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 3, Blocks.chest, 0);
		LOTRChestContents.fillChest(world, random, i + 3, j + 1, k - 3, LOTRChestContents.WOOD_ELF_HOUSE);
		
		placeWoodElfItemFrame(world, i, j + 2, k - 4, 0, random);
		placeWoodElfItemFrame(world, i, j + 2, k + 4, 2, random);
		
		for (int i1 = i - 2; i1 >= i - 3; i1--)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.air, 0);
				int stairHeight = k1 - (k - 2);
				for (int j1 = j - 4; j1 < j - 4 + stairHeight; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 11);
				}
				setBlockAndNotifyAdequately(world, i1, j - 4 + stairHeight, k1, LOTRMod.stairsElvenBrick, 2);
			}
			
			for (int j1 = j - 4; j1 <= j - 1; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 3, LOTRMod.brick, 11);
			}
		}
	
		for (int j1 = j - 3; j1 <= j - 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 4, j1, k - 2, LOTRMod.wood, 2);
			setBlockAndNotifyAdequately(world, i + 4, j1, k - 1, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i + 4, j1, k, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i + 4, j1, k + 1, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i + 4, j1, k + 2, LOTRMod.wood, 2);
		}
	
		setBlockAndNotifyAdequately(world, i + 3, j - 2, k - 3, LOTRMod.woodElvenTorch, 2);
		setBlockAndNotifyAdequately(world, i + 3, j - 2, k + 3, LOTRMod.woodElvenTorch, 2);
		setBlockAndNotifyAdequately(world, i - 3, j - 2, k - 3, LOTRMod.woodElvenTorch, 1);
		setBlockAndNotifyAdequately(world, i - 1, j - 2, k + 3, LOTRMod.woodElvenTorch, 1);
		
		setBlockAndNotifyAdequately(world, i, j - 4, k + 3, LOTRMod.woodElvenBed, 1);
		setBlockAndNotifyAdequately(world, i - 1, j - 4, k + 3, LOTRMod.woodElvenBed, 9);
		
		return true;
	}
	
	private boolean generateFacingNorth(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 4, LOTRMod.wood, 2);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 1, k + 5, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 4, Blocks.wooden_door, 3);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 4, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 5, LOTRMod.woodElvenTorch, 3);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k + 5, LOTRMod.woodElvenTorch, 3);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 4, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 4, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k + 4, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 4, Blocks.glass_pane, 0);
		
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 3, LOTRMod.woodElvenTable, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 3, Blocks.chest, 0);
		LOTRChestContents.fillChest(world, random, i - 3, j + 1, k + 3, LOTRChestContents.WOOD_ELF_HOUSE);
		
		placeWoodElfItemFrame(world, i - 4, j + 2, k, 3, random);
		placeWoodElfItemFrame(world, i + 4, j + 2, k, 1, random);
		
		for (int k1 = k - 2; k1 >= k - 3; k1--)
		{
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.air, 0);
				int stairHeight = i1 - (i - 2);
				for (int j1 = j - 4; j1 < j - 4 + stairHeight; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 11);
				}
				setBlockAndNotifyAdequately(world, i1, j - 4 + stairHeight, k1, LOTRMod.stairsElvenBrick, 0);
			}
			
			for (int j1 = j - 4; j1 <= j - 1; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 3, j1, k1, LOTRMod.brick, 11);
			}
		}
	
		for (int j1 = j - 3; j1 <= j - 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j1, k + 4, LOTRMod.wood, 2);
			setBlockAndNotifyAdequately(world, i - 1, j1, k + 4, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i, j1, k + 4, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i + 1, j1, k + 4, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i + 1, j1, k + 4, LOTRMod.wood, 2);
		}
	
		setBlockAndNotifyAdequately(world, i - 3, j - 2, k + 3, LOTRMod.woodElvenTorch, 4);
		setBlockAndNotifyAdequately(world, i + 3, j - 2, k + 3, LOTRMod.woodElvenTorch, 4);
		setBlockAndNotifyAdequately(world, i - 3, j - 2, k - 3, LOTRMod.woodElvenTorch, 3);
		setBlockAndNotifyAdequately(world, i + 3, j - 2, k - 1, LOTRMod.woodElvenTorch, 3);
		
		setBlockAndNotifyAdequately(world, i + 3, j - 4, k, LOTRMod.woodElvenBed, 2);
		setBlockAndNotifyAdequately(world, i + 3, j - 4, k - 1, LOTRMod.woodElvenBed, 10);
		
		return true;
	}
	
	private boolean generateFacingEast(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 4, j1, k1, LOTRMod.wood, 2);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 1, LOTRMod.woodElvenTorch, 2);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 1, LOTRMod.woodElvenTorch, 2);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 3, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 2, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 2, Blocks.glass_pane, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 3, Blocks.glass_pane, 0);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 3, LOTRMod.woodElvenTable, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 3, Blocks.chest, 0);
		LOTRChestContents.fillChest(world, random, i - 3, j + 1, k - 3, LOTRChestContents.WOOD_ELF_HOUSE);
		
		placeWoodElfItemFrame(world, i, j + 2, k - 4, 0, random);
		placeWoodElfItemFrame(world, i, j + 2, k + 4, 2, random);
		
		for (int i1 = i + 2; i1 <= i + 3; i1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.air, 0);
				int stairHeight = k1 - (k - 2);
				for (int j1 = j - 4; j1 < j - 4 + stairHeight; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 11);
				}
				setBlockAndNotifyAdequately(world, i1, j - 4 + stairHeight, k1, LOTRMod.stairsElvenBrick, 2);
			}
			
			for (int j1 = j - 4; j1 <= j - 1; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 3, LOTRMod.brick, 11);
			}
		}
	
		for (int j1 = j - 3; j1 <= j - 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j1, k - 2, LOTRMod.wood, 2);
			setBlockAndNotifyAdequately(world, i - 4, j1, k - 1, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i - 4, j1, k, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i - 4, j1, k + 1, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i - 4, j1, k + 2, LOTRMod.wood, 2);
		}
	
		setBlockAndNotifyAdequately(world, i - 3, j - 2, k - 3, LOTRMod.woodElvenTorch, 1);
		setBlockAndNotifyAdequately(world, i - 3, j - 2, k + 3, LOTRMod.woodElvenTorch, 1);
		setBlockAndNotifyAdequately(world, i + 3, j - 2, k - 3, LOTRMod.woodElvenTorch, 2);
		setBlockAndNotifyAdequately(world, i + 1, j - 2, k + 3, LOTRMod.woodElvenTorch, 2);
		
		setBlockAndNotifyAdequately(world, i, j - 4, k + 3, LOTRMod.woodElvenBed, 3);
		setBlockAndNotifyAdequately(world, i + 1, j - 4, k + 3, LOTRMod.woodElvenBed, 11);
		
		return true;
	}
	
	private void placeWoodElfItemFrame(World world, int i, int j, int k, int direction, Random random)
	{
		ItemStack item = null;
		int l = random.nextInt(3);
		switch (l)
		{
			case 0:
				item = new ItemStack(LOTRMod.mirkwoodBow);
				break;
			case 1:
				item = new ItemStack(Items.arrow);
				break;
			case 2:
				item = new ItemStack(LOTRMod.sapling, 1, 2);
				break;
			case 3:
				item = new ItemStack(Blocks.red_flower);
				break;
			case 4:
				item = new ItemStack(Blocks.yellow_flower);
				break;
			case 5:
				item = new ItemStack(Items.book);
				break;
		}
		spawnItemFrame(world, i, j, k, direction, item);
	}
}
