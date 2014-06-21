package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityHighElf;
import lotr.common.entity.npc.LOTREntityHighElfLord;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRWorldGenHighElvenHall extends LOTRWorldGenStructureBase
{
	private Block plankBlock;
	private int plankMeta;
	
	private Block slabBlock;
	private int slabMeta;
	
	private Block stairBlock;
	
	public LOTRWorldGenHighElvenHall(boolean flag)
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
				i -= 8;
				k += 1;
				break;
			case 1:
				i -= 16;
				k -= 8;
				break;
			case 2:
				i -= 7;
				k -= 16;
				break;
			case 3:
				i += 1;
				k -= 7;
				break;
		}
		
		if (restrictions)
		{
			int minHeight = j + 1;
			int maxHeight = j + 1;
			
			for (int i1 = i - 1; i1 <= i + 16; i1++)
			{
				for (int k1 = k - 1; k1 <= k + 16; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
					Block block = world.getBlock(i1, j1 - 1, k1);
					if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone)
					{
						return false;
					}
					
					if (j1 > maxHeight)
					{
						maxHeight = j1;
					}
					if (j1 < minHeight)
					{
						minHeight = j1;
					}
				}
			}
			
			if (Math.abs(maxHeight - minHeight) > 5)
			{
				return false;
			}
			
			int height = j + 1;
			
			for (int i1 = i - 1; i1 <= i + 16; i1++)
			{
				for (int k1 = k - 1; k1 <= k + 16; k1++)
				{
					if ((i1 == i - 1 || i1 == i + 16) && (k1 == k - 1 || k1 == k + 16))
					{
						int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
						if (j1 > height)
						{
							height = j1;
						}
					}
				}
			}
			
			j = height - 1;
		}
		
		int randomWood = random.nextInt(3);
		switch (randomWood)
		{
			case 0:
				plankBlock = Blocks.planks;
				plankMeta = 0;
				slabBlock = Blocks.wooden_slab;
				slabMeta = 0;
				stairBlock = Blocks.oak_stairs;
				break;
			case 1:
				plankBlock = Blocks.planks;
				plankMeta = 2;
				slabBlock = Blocks.wooden_slab;
				slabMeta = 2;
				stairBlock = Blocks.birch_stairs;
				break;
			case 2:
				plankBlock = LOTRMod.planks;
				plankMeta = 9;
				slabBlock = LOTRMod.woodSlabSingle2;
				slabMeta = 1;
				stairBlock = LOTRMod.stairsBeech;
				break;
		}
		
		for (int i1 = i; i1 <= i + 15; i1++)
		{
			for (int k1 = k; k1 <= k + 15; k1++)
			{
				for (int j1 = j; (j1 == j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 11);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
				
				for (int j1 = j + 1; j1 <= j + 4; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				if ((i1 < i + 2 || i1 > i + 13) || (k1 < k + 2 || k1 > k + 13))
				{
					setBlockAndNotifyAdequately(world, i1, j + 5, k1, LOTRMod.brick, 11);
				}
				else
				{
					setBlockAndNotifyAdequately(world, i1, j + 5, k1, plankBlock, plankMeta);
				}
				
				for (int j1 = j + 6; j1 <= j + 9; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i + 1; i1 <= i + 14; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 6, k, LOTRMod.wall, 10);
			setBlockAndNotifyAdequately(world, i1, j + 6, k + 15, LOTRMod.wall, 10);
		}
		
		for (int k1 = k + 1; k1 <= k + 14; k1++)
		{
			setBlockAndNotifyAdequately(world, i, j + 6, k1, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i, j + 7, k1, LOTRMod.wall, 10);
			setBlockAndNotifyAdequately(world, i + 15, j + 6, k1, LOTRMod.wall, 10);
		}
		
		for (int j1 = j; j1 <= j + 5; j1 += 5)
		{
			for (int k1 = k; k1 <= k + 15; k1 += 15)
			{
				for (int i1 = i; i1 <= i + 15; i1 += 3)
				{
					for (int j2 = j1 + 1; j2 <= j1 + 4; j2++)
					{
						setBlockAndNotifyAdequately(world, i1, j2, k1, LOTRMod.pillar, 1);
					}
					setBlockAndNotifyAdequately(world, i1, j1 + 5, k1, LOTRMod.brick, 11);
				}
				
				for (int i1 = i + 1; i1 <= i + 13; i1 += 3)
				{
					setBlockAndNotifyAdequately(world, i1, j1 + 5, k1, LOTRMod.stairsElvenBrick, 5);
					setBlockAndNotifyAdequately(world, i1 + 1, j1 + 5, k1, LOTRMod.stairsElvenBrick, 4);
				}
			}
		
			for (int i1 = i; i1 <= i + 15; i1 += 15)
			{
				for (int k1 = k + 3; k1 <= k + 12; k1 += 3)
				{
					for (int j2 = j1 + 1; j2 <= j1 + 4; j2++)
					{
						setBlockAndNotifyAdequately(world, i1, j2, k1, LOTRMod.pillar, 1);
					}
					setBlockAndNotifyAdequately(world, i1, j1 + 5, k1, LOTRMod.brick, 11);
				}
				
				for (int k1 = k + 1; k1 <= k + 13; k1 += 3)
				{
					setBlockAndNotifyAdequately(world, i1, j1 + 5, k1, LOTRMod.stairsElvenBrick, 7);
					setBlockAndNotifyAdequately(world, i1, j1 + 5, k1 + 1, LOTRMod.stairsElvenBrick, 6);
				}
			}
			
			for (int i1 = i; i1 <= i + 15; i1 += 3)
			{
				setBlockAndNotifyAdequately(world, i1, j1 + 4, k + 1, LOTRMod.highElvenTorch, 3);
				setBlockAndNotifyAdequately(world, i1, j1 + 4, k + 14, LOTRMod.highElvenTorch, 4);
			}
			
			for (int k1 = k; k1 <= k + 15; k1 += 3)
			{
				setBlockAndNotifyAdequately(world, i + 1, j1 + 4, k1, LOTRMod.highElvenTorch, 1);
				setBlockAndNotifyAdequately(world, i + 14, j1 + 4, k1, LOTRMod.highElvenTorch, 2);
			}
		}
		
		int roofWidth = 18;
		int roofX = i - 1;
		int roofY = j + 11;
		int roofZ = k - 1;
		
		while (roofWidth > 2)
		{
			for (int i1 = roofX; i1 < roofX + roofWidth; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, roofY, roofZ, LOTRMod.stairsElvenBrick, 2);
				setBlockAndNotifyAdequately(world, i1, roofY, roofZ + roofWidth - 1, LOTRMod.stairsElvenBrick, 3);
			}
			
			for (int k1 = roofZ; k1 < roofZ + roofWidth; k1++)
			{
				setBlockAndNotifyAdequately(world, roofX, roofY, k1, LOTRMod.stairsElvenBrick, 0);
				setBlockAndNotifyAdequately(world, roofX + roofWidth - 1, roofY, k1, LOTRMod.stairsElvenBrick, 1);
			}
			
			for (int i1 = roofX + 1; i1 < roofX + roofWidth - 2; i1++)
			{
				for (int k1 = roofZ + 1; k1 < roofZ + roofWidth - 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, roofY, k1, Blocks.air, 0);
				}
			}
			
			for (int i1 = roofX + 1; i1 < roofX + roofWidth - 1; i1++)
			{
				if (roofWidth > 16)
				{
					setBlockAndNotifyAdequately(world, i1, roofY, roofZ + 1, LOTRMod.brick, 11);
					setBlockAndNotifyAdequately(world, i1, roofY, roofZ + roofWidth - 2, LOTRMod.brick, 11);
				}
				else
				{
					setBlockAndNotifyAdequately(world, i1, roofY, roofZ + 1, LOTRMod.stairsElvenBrick, 7);
					setBlockAndNotifyAdequately(world, i1, roofY, roofZ + roofWidth - 2, LOTRMod.stairsElvenBrick, 6);
				}
			}
			
			for (int k1 = roofZ + 1; k1 < roofZ + roofWidth - 1; k1++)
			{
				if (roofWidth > 16)
				{
					setBlockAndNotifyAdequately(world, roofX + 1, roofY, k1, LOTRMod.brick, 11);
					setBlockAndNotifyAdequately(world, roofX + roofWidth - 2, roofY, k1, LOTRMod.brick, 11);
				}
				else
				{
					setBlockAndNotifyAdequately(world, roofX + 1, roofY, k1, LOTRMod.stairsElvenBrick, 5);
					setBlockAndNotifyAdequately(world, roofX + roofWidth - 2, roofY, k1, LOTRMod.stairsElvenBrick, 4);
				}
			}
			
			roofWidth -= 2;
			roofX++;
			roofY++;
			roofZ++;
		}
		
		for (int i1 = roofX; i1 < roofX + roofWidth; i1++)
		{
			for (int k1 = roofZ; k1 < roofZ + roofWidth; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, roofY - 1, k1, Blocks.glass, 0);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 2, j + 6, k + 9, LOTRMod.highElvenBed, 1);
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 9, LOTRMod.highElvenBed, 9);
		
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 10, plankBlock, plankMeta);
		placeFlowerPot(world, i + 1, j + 7, k + 10, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 8, plankBlock, plankMeta);
		placeFlowerPot(world, i + 1, j + 7, k + 8, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 6, Blocks.bookshelf, 0);
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 5, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 4, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 3, Blocks.bookshelf, 0);
		
		setBlockAndNotifyAdequately(world, i + 3, j + 6, k + 4, stairBlock, 0);
		
		placeMug(world, random, i + 1, j + 7, k + 4, 1, LOTRMod.mugMiruvor);
		
		setBlockAndNotifyAdequately(world, i + 11, j + 6, k + 10, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 11, j + 6, k + 11, plankBlock, plankMeta);
		
		for (int k1 = k + 10; k1 <= k + 12; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 13, j + 6, k1, stairBlock, 0);
		}
		
		for (int i1 = i + 11; i1 <= i + 13; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 6, k + 13, stairBlock, 2);
		}
		
		for (int k1 = k + 5; k1 <= k + 9; k1++)
		{
			for (int i1 = i + 7; i1 <= i + 10; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 5, k1, Blocks.air, 0);
			}
		}
		
		for (int k1 = k + 5; k1 <= k + 6; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 7, j1, k1, LOTRMod.brick, 11);
			}
			
			setBlockAndNotifyAdequately(world, i + 7, j + 5, k1, LOTRMod.stairsElvenBrick, 1);
			
			for (int i1 = i + 8; i1 <= i + 10; i1++)
			{
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 11);
				}
			}
			
			setBlockAndNotifyAdequately(world, i + 8, j + 4, k1, LOTRMod.stairsElvenBrick, 1);
		}
		
		for (int i1 = i + 9; i1 <= i + 10; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 7, LOTRMod.brick, 11);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 7, LOTRMod.stairsElvenBrick, 3);
			
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 8, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 8, LOTRMod.stairsElvenBrick, 3);
			
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 9, LOTRMod.stairsElvenBrick, 3);
		}
		
		setBlockAndNotifyAdequately(world, i + 11, j + 1, k + 5, LOTRMod.pillar, 1);
		setBlockAndNotifyAdequately(world, i + 11, j + 1, k + 6, LOTRMod.highElvenTable, 1);
		setBlockAndNotifyAdequately(world, i + 11, j + 1, k + 7, LOTRMod.pillar, 1);
		
		placeFlowerPot(world, i + 11, j + 2, k + 5, getRandomPlant(random));
		placeFlowerPot(world, i + 11, j + 2, k + 7, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i + 11, j + 3, k + 6, LOTRMod.highElvenTorch, 1);
		setBlockAndNotifyAdequately(world, i + 6, j + 3, k + 6, LOTRMod.highElvenTorch, 2);
		setBlockAndNotifyAdequately(world, i + 8, j + 3, k + 7, LOTRMod.highElvenTorch, 3);
		
		setBlockAndNotifyAdequately(world, i + 10, j + 1, k + 4, LOTRMod.pillar, 1);
		placeBarrel(world, random, i + 10, j + 2, k + 4, 2, LOTRMod.mugMiruvor);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k + 4, LOTRMod.pillar, 1);
		placeBarrel(world, random, i + 7, j + 2, k + 4, 2, LOTRMod.mugMiruvor);
		
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k + 4, Blocks.chest, 0);
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k + 4, Blocks.chest, 0);
		LOTRChestContents.fillChest(world, random, i + 8, j + 1, k + 4, LOTRChestContents.HIGH_ELVEN_HALL);
		LOTRChestContents.fillChest(world, random, i + 9, j + 1, k + 4, LOTRChestContents.HIGH_ELVEN_HALL);
		
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k + 5, Blocks.furnace, 2);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k + 5, Blocks.furnace, 2);
		setBlockMetadata(world, i + 8, j + 2, k + 5, 2);
		setBlockMetadata(world, i + 9, j + 2, k + 5, 2);
		
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k + 7, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k + 7, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k + 8, plankBlock, plankMeta);
		
		placePlateWithCertainty(world, i + 7, j + 2, k + 7, random, LOTRFoods.ELF);
		placePlateWithCertainty(world, i + 8, j + 2, k + 7, random, LOTRFoods.ELF);
		placePlateWithCertainty(world, i + 8, j + 2, k + 8, random, LOTRFoods.ELF);
		
		for (int k1 = k + 6; k1 <= k + 12; k1++)
		{
			for (int i1 = i + 2; i1 <= i + 4; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, slabBlock, slabMeta | 8);
			}
		}
		
		for (int k1 = k + 6; k1 <= k + 12; k1 += 3)
		{
			setBlockAndNotifyAdequately(world, i + 2, j + 1, k1, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i + 4, j + 1, k1, plankBlock, plankMeta);
			
			setBlockAndNotifyAdequately(world, i + 1, j + 1, k1, stairBlock, 1);
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, stairBlock, 0);
		}

		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 13, stairBlock, 2);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 5, stairBlock, 3);
		
		for (int k1 = k + 6; k1 <= k + 12; k1 += 2)
		{
			placePlateWithCertainty(world, i + 2, j + 2, k1, random, LOTRFoods.ELF);
			placePlateWithCertainty(world, i + 4, j + 2, k1, random, LOTRFoods.ELF);
		}
		
		for (int k1 = k + 7; k1 <= k + 11; k1 += 2)
		{
			int l = random.nextInt(2);
			if (l == 0)
			{
				setBlockAndNotifyAdequately(world, i + 3, j + 2, k1, LOTRMod.appleCrumble, 0);
			}
			if (l == 1)
			{
				setBlockAndNotifyAdequately(world, i + 3, j + 2, k1, LOTRMod.cherryPie, 0);
			}
			
			placeMug(world, random, i + 2, j + 2, k1, 3, LOTRMod.mugMiruvor);
			placeMug(world, random, i + 4, j + 2, k1, 1, LOTRMod.mugMiruvor);
		}
		
		placeMug(world, random, i + 3, j + 2, k + 6, 0, LOTRMod.mugMiruvor);
		placeMug(world, random, i + 3, j + 2, k + 12, 2, LOTRMod.mugMiruvor);
		
		placeFlowerPot(world, i + 3, j + 2, k + 8, getRandomPlant(random));
		placeFlowerPot(world, i + 3, j + 2, k + 10, getRandomPlant(random));
		
		for (int j1 = j + 3; j1 <= j + 8; j1 += 5)
		{
			for (int i1 = i + 3; i1 <= i + 12; i1 += 3)
			{
				placeWallBanner(world, i1, j1, k, 0, 10);
				placeWallBanner(world, i1, j1, k + 15, 2, 10);
			}
			
			for (int k1 = k + 3; k1 <= k + 12; k1 += 3)
			{
				placeWallBanner(world, i, j1, k1, 3, 10);
				placeWallBanner(world, i + 15, j1, k1, 1, 10);
			}
		}
		
		int elves = 2 + random.nextInt(4);
		for (int l = 0; l < elves; l++)
		{
			LOTREntityHighElf elf = new LOTREntityHighElf(world);
			
			if (l == 0)
			{
				elf = new LOTREntityHighElfLord(world);
			}
			
			elf.setLocationAndAngles(i + 6, j + 6, k + 6, 0F, 0F);
			elf.spawnRidingHorse = false;
			elf.onSpawnWithEgg(null);
			elf.isNPCPersistent = true;
			world.spawnEntityInWorld(elf);
			elf.setHomeArea(i + 7, j + 3, k + 7, 24);
		}
		
		return true;
	}
	
	private ItemStack getRandomPlant(Random random)
	{
		int l = random.nextInt(5);
		switch (l)
		{
			case 0:
				return new ItemStack(Blocks.sapling, 1, 0);
			case 1:
				return new ItemStack(Blocks.sapling, 1, 2);
			case 2:
				return new ItemStack(Blocks.sapling, 1, 2);
			case 3:
				return new ItemStack(Blocks.red_flower);
			case 4:
				return new ItemStack(Blocks.yellow_flower);
		}
		return new ItemStack(Blocks.sapling, 1, 0);
	}
}
