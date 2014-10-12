package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityElf;
import lotr.common.entity.npc.LOTREntityGaladhrimElf;
import lotr.common.world.feature.LOTRWorldGenMallornLarge;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRWorldGenElfHouse extends LOTRWorldGenStructureBase2
{
	public LOTRWorldGenElfHouse(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation)
	{
		if (!restrictions && usingPlayer != null)
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
			
			j--;
			
			LOTRWorldGenMallornLarge treeGen = new LOTRWorldGenMallornLarge(true);
			int j1 = treeGen.generateAndReturnHeight(world, random, i, j, k, true);
			j += MathHelper.floor_double(j1 * MathHelper.randomFloatClamp(random, LOTRWorldGenMallornLarge.HOUSE_HEIGHT_MIN, LOTRWorldGenMallornLarge.HOUSE_HEIGHT_MAX));
		}
		
		setOrigin(i, j, k);
		setRotationMode(rotation);
		
		if (restrictions)
		{
			for (int i1 = -8; i1 <= 8; i1++)
			{
				for (int j1 = -3; j1 <= 6; j1++)
				{
					for (int k1 = -8; k1 <= 8; k1++)
					{
						if (Math.abs(i1) <= 2 && Math.abs(k1) <= 2)
						{
							continue;
						}
						else if (!isAir(world, i1, j1, k1))
						{
							return false;
						}
					}
				}
			}
		}
		else if (usingPlayer != null)
		{
			for (int i1 = -2; i1 <= 2; i1++)
			{
				for (int k1 = -2; k1 <= 2; k1++)
				{
					for (int j1 = j; !isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
					{
						setBlockAndMetadata(world, i1, j1, k1, LOTRMod.wood, 1);
					}
				}
			}
		}
		
		for (int i1 = -7; i1 <= 7; i1++)
		{
			for (int j1 = 1; j1 <= 4; j1++)
			{
				for (int k1 = -7; k1 <= 7; k1++)
				{
					setBlockAndMetadata(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = -2; i1 <= 2; i1++)
		{
			for (int j1 = -1; j1 <= 5; j1++)
			{
				for (int k1 = -2; k1 <= 2; k1++)
				{
					setBlockAndMetadata(world, i1, j1, k1, LOTRMod.wood, 1);
					
					if (j1 >= 1 && j1 <= 2 && Math.abs(i1) == 2 && Math.abs(k1) == 2)
					{
						setBlockAndMetadata(world, i1, j1, k1, LOTRMod.fence, 1);
					}
				}
			}
		}
		
		for (int i1 = -6; i1 <= 6; i1++)
		{
			for (int k1 = -6; k1 <= 6; k1++)
			{
				if (Math.abs(i1) <= 2 && Math.abs(k1) <= 2)
				{
					continue;
				}
				if (Math.abs(i1) == 6 || Math.abs(k1) == 6)
				{
					continue;
				}
				setBlockAndMetadata(world, i1, 0, k1, LOTRMod.planks, 1);
			}
		}
		
		for (int i1 = -3; i1 <= 3; i1++)
		{
			setBlockAndMetadata(world, i1, 0, -7, LOTRMod.planks, 1);
			setBlockAndMetadata(world, i1, 0, 7, LOTRMod.planks, 1);
		}
		
		for (int k1 = -3; k1 <= 3; k1++)
		{
			setBlockAndMetadata(world, -7, 0, k1, LOTRMod.planks, 1);
			setBlockAndMetadata(world, 7, 0, k1, LOTRMod.planks, 1);
		}
		
		for (int j1 = 1; j1 <= 4; j1++)
		{
			setBlockAndMetadata(world, -5, j1, -5, LOTRMod.planks, 1);
			setBlockAndMetadata(world, 5, j1, -5, LOTRMod.planks, 1);
			setBlockAndMetadata(world, -5, j1, 5, LOTRMod.planks, 1);
			setBlockAndMetadata(world, 5, j1, 5, LOTRMod.planks, 1);
			
			setBlockAndMetadata(world, -6, j1, -3, LOTRMod.wood, 1);
			setBlockAndMetadata(world, -6, j1, 3, LOTRMod.wood, 1);
			setBlockAndMetadata(world, 6, j1, -3, LOTRMod.wood, 1);
			setBlockAndMetadata(world, 6, j1, 3, LOTRMod.wood, 1);
			setBlockAndMetadata(world, -3, j1, -6, LOTRMod.wood, 1);
			setBlockAndMetadata(world, -3, j1, 6, LOTRMod.wood, 1);
			setBlockAndMetadata(world, 3, j1, -6, LOTRMod.wood, 1);
			setBlockAndMetadata(world, 3, j1, 6, LOTRMod.wood, 1);
		}
		
		setBlockAndMetadata(world, -4, 2, -5, LOTRMod.mallornTorch, 2);
		setBlockAndMetadata(world, -5, 2, -4, LOTRMod.mallornTorch, 3);
		setBlockAndMetadata(world, 4, 2, -5, LOTRMod.mallornTorch, 1);
		setBlockAndMetadata(world, 5, 2, -4, LOTRMod.mallornTorch, 3);
		setBlockAndMetadata(world, -4, 2, 5, LOTRMod.mallornTorch, 2);
		setBlockAndMetadata(world, -5, 2, 4, LOTRMod.mallornTorch, 4);
		setBlockAndMetadata(world, 4, 2, 5, LOTRMod.mallornTorch, 1);
		setBlockAndMetadata(world, 5, 2, 4, LOTRMod.mallornTorch, 4);
		
		for (int i1 = -3; i1 <= 3; i1++)
		{
			setBlockAndMetadata(world, i1, 1, -7, LOTRMod.fence, 1);
			setBlockAndMetadata(world, i1, 1, 7, LOTRMod.fence, 1);
		}
		
		for (int k1 = -3; k1 <= 3; k1++)
		{
			setBlockAndMetadata(world, -7, 1, k1, LOTRMod.fence, 1);
			setBlockAndMetadata(world, 7, 1, k1, LOTRMod.fence, 1);
		}
		
		setBlockAndMetadata(world, -4, 1, -6, LOTRMod.fence, 1);
		setBlockAndMetadata(world, -5, 1, -6, LOTRMod.fence, 1);
		setBlockAndMetadata(world, -4, 1, 6, LOTRMod.fence, 1);
		setBlockAndMetadata(world, -5, 1, 6, LOTRMod.fence, 1);
		setBlockAndMetadata(world, 4, 1, -6, LOTRMod.fence, 1);
		setBlockAndMetadata(world, 5, 1, -6, LOTRMod.fence, 1);
		setBlockAndMetadata(world, 4, 1, 6, LOTRMod.fence, 1);
		setBlockAndMetadata(world, 5, 1, 6, LOTRMod.fence, 1);
		
		setBlockAndMetadata(world, -6, 1, -4, LOTRMod.fence, 1);
		setBlockAndMetadata(world, -6, 1, -5, LOTRMod.fence, 1);
		setBlockAndMetadata(world, 6, 1, -4, LOTRMod.fence, 1);
		setBlockAndMetadata(world, 6, 1, -5, LOTRMod.fence, 1);
		setBlockAndMetadata(world, -6, 1, 4, LOTRMod.fence, 1);
		setBlockAndMetadata(world, -6, 1, 5, LOTRMod.fence, 1);
		setBlockAndMetadata(world, 6, 1, 4, LOTRMod.fence, 1);
		setBlockAndMetadata(world, 6, 1, 5, LOTRMod.fence, 1);
		
		setBlockAndMetadata(world, -6, 4, -2, LOTRMod.stairsMallorn, 7);
		setBlockAndMetadata(world, -6, 4, -1, LOTRMod.woodSlabSingle, 9);
		setBlockAndMetadata(world, -6, 4, 0, LOTRMod.woodSlabSingle, 9);
		setBlockAndMetadata(world, -6, 4, 1, LOTRMod.woodSlabSingle, 9);
		setBlockAndMetadata(world, -6, 4, 2, LOTRMod.stairsMallorn, 6);
		
		setBlockAndMetadata(world, 6, 4, -2, LOTRMod.stairsMallorn, 7);
		setBlockAndMetadata(world, 6, 4, -1, LOTRMod.woodSlabSingle, 9);
		setBlockAndMetadata(world, 6, 4, 0, LOTRMod.woodSlabSingle, 9);
		setBlockAndMetadata(world, 6, 4, 1, LOTRMod.woodSlabSingle, 9);
		setBlockAndMetadata(world, 6, 4, 2, LOTRMod.stairsMallorn, 6);
		
		setBlockAndMetadata(world, -2, 4, -6, LOTRMod.stairsMallorn, 5);
		setBlockAndMetadata(world, -1, 4, -6, LOTRMod.woodSlabSingle, 9);
		setBlockAndMetadata(world, 0, 4, -6, LOTRMod.woodSlabSingle, 9);
		setBlockAndMetadata(world, 1, 4, -6, LOTRMod.woodSlabSingle, 9);
		setBlockAndMetadata(world, 2, 4, -6, LOTRMod.stairsMallorn, 4);
		
		setBlockAndMetadata(world, -2, 4, 6, LOTRMod.stairsMallorn, 5);
		setBlockAndMetadata(world, -1, 4, 6, LOTRMod.woodSlabSingle, 9);
		setBlockAndMetadata(world, 0, 4, 6, LOTRMod.woodSlabSingle, 9);
		setBlockAndMetadata(world, 1, 4, 6, LOTRMod.woodSlabSingle, 9);
		setBlockAndMetadata(world, 2, 4, 6, LOTRMod.stairsMallorn, 4);
		
		for (int i1 = -5; i1 <= -4; i1++)
		{
			setBlockAndMetadata(world, i1, 4, -6, LOTRMod.stairsMallorn, 6);
			setBlockAndMetadata(world, i1, 4, 6, LOTRMod.stairsMallorn, 7);
		}
		
		for (int i1 = 4; i1 <= 5; i1++)
		{
			setBlockAndMetadata(world, i1, 4, -6, LOTRMod.stairsMallorn, 6);
			setBlockAndMetadata(world, i1, 4, 6, LOTRMod.stairsMallorn, 7);
		}
		
		for (int k1 = -5; k1 <= -4; k1++)
		{
			setBlockAndMetadata(world, -5, 4, k1, LOTRMod.stairsMallorn, 4);
			setBlockAndMetadata(world, 5, 4, k1, LOTRMod.stairsMallorn, 5);
		}
		
		for (int k1 = 4; k1 <= 5; k1++)
		{
			setBlockAndMetadata(world, -6, 4, k1, LOTRMod.stairsMallorn, 4);
			setBlockAndMetadata(world, 6, 4, k1, LOTRMod.stairsMallorn, 5);
		}
		
		for (int i1 = -4; i1 <= 4; i1++)
		{
			if (Math.abs(i1) <= 1)
			{
				continue;
			}
			setBlockAndMetadata(world, i1, 4, -5, LOTRMod.stairsMallorn, 7);
			setBlockAndMetadata(world, i1, 4, 5, LOTRMod.stairsMallorn, 6);
		}
		
		for (int k1 = -4; k1 <= 4; k1++)
		{
			if (Math.abs(k1) <= 1)
			{
				continue;
			}
			setBlockAndMetadata(world, -5, 4, k1, LOTRMod.stairsMallorn, 5);
			setBlockAndMetadata(world, 5, 4, k1, LOTRMod.stairsMallorn, 4);
		}
		
		for (int i1 = -6; i1 <= 6; i1++)
		{
			for (int k1 = -6; k1 <= 6; k1++)
			{
				if (restrictions && i1 >= -2 && i1 <= 2 && k1 >= -2 && k1 <= 2)
				{
					continue;
				}
				if ((i1 == -6 || i1 == 6) && (k1 == -6 || k1 == 6))
				{
					continue;
				}
				setBlockAndMetadata(world, i1, 5, k1, LOTRMod.planks, 1);
			}
		}
		
		for (int i1 = -3; i1 <= 3; i1++)
		{
			setBlockAndMetadata(world, i1, 5, -7, LOTRMod.planks, 1);
			setBlockAndMetadata(world, i1, 5, 7, LOTRMod.planks, 1);
		}
		
		for (int k1 = -3; k1 <= 3; k1++)
		{
			setBlockAndMetadata(world, -7, 5, k1, LOTRMod.planks, 1);
			setBlockAndMetadata(world, 7, 5, k1, LOTRMod.planks, 1);
		}
		
		for (int i1 = -8; i1 <= 8; i1++)
		{
			int k1;
			int i2 = Math.abs(i1);
			if (i2 == 8)
			{
				k1 = 3;
			}
			else if (i2 == 7)
			{
				k1 = 5;
			}
			else if (i2 > 4)
			{
				k1 = 6;
			}
			else
			{
				k1 = 7;
			}
			
			setBlockAndMetadata(world, i1, 5, -k1, LOTRMod.stairsMallorn, 2);
			setBlockAndMetadata(world, i1, 5, k1, LOTRMod.stairsMallorn, 3);
		}
		
		for (int k1 = -8; k1 <= 8; k1++)
		{
			int i1;
			int k2 = Math.abs(k1);
			if (k2 == 8)
			{
				i1 = 3;
			}
			else if (k2 == 7)
			{
				i1 = 5;
			}
			else if (k2 > 4)
			{
				i1 = 6;
			}
			else
			{
				i1 = 7;
			}
			
			setBlockAndMetadata(world, -i1, 5, k1, LOTRMod.stairsMallorn, 0);
			setBlockAndMetadata(world, i1, 5, k1, LOTRMod.stairsMallorn, 1);
		}
		
		setBlockAndMetadata(world, -4, 5, -7, LOTRMod.stairsMallorn, 2);
		setBlockAndMetadata(world, -6, 5, -6, LOTRMod.stairsMallorn, 2);
		setBlockAndMetadata(world, -7, 5, -4, LOTRMod.stairsMallorn, 2);
		setBlockAndMetadata(world, -7, 5, 4, LOTRMod.stairsMallorn, 3);
		setBlockAndMetadata(world, -6, 5, 6, LOTRMod.stairsMallorn, 3);
		setBlockAndMetadata(world, -4, 5, 7, LOTRMod.stairsMallorn, 3);
		
		setBlockAndMetadata(world, 4, 5, -7, LOTRMod.stairsMallorn, 2);
		setBlockAndMetadata(world, 6, 5, -6, LOTRMod.stairsMallorn, 2);
		setBlockAndMetadata(world, 7, 5, -4, LOTRMod.stairsMallorn, 2);
		setBlockAndMetadata(world, 7, 5, 4, LOTRMod.stairsMallorn, 3);
		setBlockAndMetadata(world, 6, 5, 6, LOTRMod.stairsMallorn, 3);
		setBlockAndMetadata(world, 4, 5, 7, LOTRMod.stairsMallorn, 3);
		
		for (int i1 = -5; i1 <= 5; i1++)
		{
			for (int k1 = -5; k1 <= 5; k1++)
			{
				if (restrictions && i1 >= -2 && i1 <= 2 && k1 >= -2 && k1 <= 2)
				{
					continue;
				}
				if ((i1 == -5 || i1 == 5) && (k1 == -5 || k1 == 5))
				{
					continue;
				}
				setBlockAndMetadata(world, i1, 6, k1, LOTRMod.planks, 1);
			}
		}
		
		for (int i1 = -2; i1 <= 2; i1++)
		{
			setBlockAndMetadata(world, i1, 6, -6, LOTRMod.planks, 1);
			setBlockAndMetadata(world, i1, 6, 6, LOTRMod.planks, 1);
		}
		
		for (int k1 = -2; k1 <= 2; k1++)
		{
			setBlockAndMetadata(world, -6, 6, k1, LOTRMod.planks, 1);
			setBlockAndMetadata(world, 6, 6, k1, LOTRMod.planks, 1);
		}
		
		for (int i1 = -7; i1 <= 7; i1++)
		{
			int k1;
			int i2 = Math.abs(i1);
			if (i2 == 7)
			{
				k1 = 2;
			}
			else if (i2 == 6)
			{
				k1 = 4;
			}
			else if (i2 > 3)
			{
				k1 = 5;
			}
			else
			{
				k1 = 6;
			}
			
			setBlockAndMetadata(world, i1, 6, -k1, LOTRMod.stairsMallorn, 2);
			setBlockAndMetadata(world, i1, 6, k1, LOTRMod.stairsMallorn, 3);
		}
		
		for (int k1 = -7; k1 <= 7; k1++)
		{
			int i1;
			int k2 = Math.abs(k1);
			if (k2 == 7)
			{
				i1 = 2;
			}
			else if (k2 == 6)
			{
				i1 = 4;
			}
			else if (k2 > 3)
			{
				i1 = 5;
			}
			else
			{
				i1 = 6;
			}
			
			setBlockAndMetadata(world, -i1, 6, k1, LOTRMod.stairsMallorn, 0);
			setBlockAndMetadata(world, i1, 6, k1, LOTRMod.stairsMallorn, 1);
		}
		
		setBlockAndMetadata(world, -3, 6, -6, LOTRMod.stairsMallorn, 2);
		setBlockAndMetadata(world, -5, 6, -5, LOTRMod.stairsMallorn, 2);
		setBlockAndMetadata(world, -6, 6, -3, LOTRMod.stairsMallorn, 2);
		setBlockAndMetadata(world, -6, 6, 3, LOTRMod.stairsMallorn, 3);
		setBlockAndMetadata(world, -5, 6, 5, LOTRMod.stairsMallorn, 3);
		setBlockAndMetadata(world, -3, 6, 6, LOTRMod.stairsMallorn, 3);
		
		setBlockAndMetadata(world, 3, 6, -6, LOTRMod.stairsMallorn, 2);
		setBlockAndMetadata(world, 5, 6, -5, LOTRMod.stairsMallorn, 2);
		setBlockAndMetadata(world, 6, 6, -3, LOTRMod.stairsMallorn, 2);
		setBlockAndMetadata(world, 6, 6, 3, LOTRMod.stairsMallorn, 3);
		setBlockAndMetadata(world, 5, 6, 5, LOTRMod.stairsMallorn, 3);
		setBlockAndMetadata(world, 3, 6, 6, LOTRMod.stairsMallorn, 3);
		
		for (int i1 = -3; i1 <= 3; i1++)
		{
			setBlockAndMetadata(world, i1, 4, -3, LOTRMod.stairsMallorn, 6);
			setBlockAndMetadata(world, i1, 4, 3, LOTRMod.stairsMallorn, 7);
		}
		
		for (int k1 = -2; k1 <= 2; k1++)
		{
			setBlockAndMetadata(world, -3, 4, k1, LOTRMod.stairsMallorn, 4);
			setBlockAndMetadata(world, 3, 4, k1, LOTRMod.stairsMallorn, 5);
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
				ladderZ = -3;
				ladderMeta = 2;
				table1X = -2;
				wood1Meta = 5;
				table2X = 2;
				wood2Meta = 5;
				chestZ = 2;
				wood3Meta = 9;
				break;
			case 1:
				ladderX = 3;
				ladderMeta = 5;
				table1Z = -2;
				wood1Meta = 9;
				table2Z = 2;
				wood2Meta = 9;
				chestX = -2;
				wood3Meta = 5;
				break;
			case 2:
				ladderZ = 3;
				ladderMeta = 3;
				table1X = -2;
				wood1Meta = 5;
				table2X = 2;
				wood2Meta = 5;
				chestZ = -2;
				wood3Meta = 9;
				break;
			case 3:
				ladderX = -3;
				ladderMeta = 4;
				table1Z = -2;
				wood1Meta = 9;
				table2Z = 2;
				wood2Meta = 9;
				chestX = 2;
				wood3Meta = 5;
				break;
		}
		
		for (int j1 = 3; j1 >= -3 || (!isOpaque(world, ladderX, j1, ladderZ) && getY(j1) >= 0); j1--)
		{
			setBlockAndMetadata(world, ladderX, j1, ladderZ, LOTRMod.mallornLadder, ladderMeta);
		}
		
		setBlockAndMetadata(world, table1X, 1, table1Z, LOTRMod.elvenTable, 0);
		setBlockAndMetadata(world, table1X, 2, table1Z, Blocks.air, 0);
		setBlockAndMetadata(world, table1X, 3, table1Z, Blocks.air, 0);
		setBlockAndMetadata(world, table1X, 4, table1Z, LOTRMod.wood, wood1Meta);
		
		setBlockAndMetadata(world, table2X, 1, table2Z, LOTRMod.elvenTable, 0);
		setBlockAndMetadata(world, table2X, 2, table2Z, Blocks.air, 0);
		setBlockAndMetadata(world, table2X, 3, table2Z, Blocks.air, 0);
		setBlockAndMetadata(world, table2X, 4, table2Z, LOTRMod.wood, wood2Meta);
		
		setBlockAndMetadata(world, chestX, 1, chestZ, Blocks.chest, 0);
		setBlockAndMetadata(world, chestX, 2, chestZ, Blocks.air, 0);
		setBlockAndMetadata(world, chestX, 3, chestZ, Blocks.air, 0);
		setBlockAndMetadata(world, chestX, 4, chestZ, LOTRMod.wood, wood3Meta);
		
		LOTRChestContents.fillChest(world, random, chestX, 1, chestZ, LOTRChestContents.ELF_HOUSE);
		
		tryPlaceLight(world, -7, -1, -3, random);
		tryPlaceLight(world, -7, -1, 3, random);
		tryPlaceLight(world, 7, -1, -3, random);
		tryPlaceLight(world, 7, -1, 3, random);
		tryPlaceLight(world, -3, -1, -7, random);
		tryPlaceLight(world, 3, -1, -7, random);
		tryPlaceLight(world, -3, -1, 7, random);
		tryPlaceLight(world, 3, -1, 7, random);
		
		placeFlowerPot(world, -4, 1, -5, getRandomPlant(random));
		placeFlowerPot(world, -5, 1, -4, getRandomPlant(random));
		placeFlowerPot(world, -5, 1, 4, getRandomPlant(random));
		placeFlowerPot(world, -4, 1, 5, getRandomPlant(random));
		placeFlowerPot(world, 4, 1, -5, getRandomPlant(random));
		placeFlowerPot(world, 5, 1, -4, getRandomPlant(random));
		placeFlowerPot(world, 5, 1, 4, getRandomPlant(random));
		placeFlowerPot(world, 4, 1, 5, getRandomPlant(random));
		
		setBlockAndMetadata(world, -2, 1, 5, LOTRMod.elvenBed, 1);
		setBlockAndMetadata(world, -3, 1, 5, LOTRMod.elvenBed, 9);

		LOTREntityElf elf = new LOTREntityGaladhrimElf(world);
		elf.spawnRidingHorse = false;
		spawnNPCAndSetHome(elf, world, 0, 1, 4, 8);
		
		return true;
	}
	
	private void tryPlaceLight(World world, int i, int j, int k, Random random)
	{
		int height = 2 + random.nextInt(6);
		for (int j1 = j; j1 >= -height; j1--)
		{
			if (restrictions)
			{
				if (!isAir(world, i, j1, k))
				{
					return;
				}
				if (j1 == -height)
				{
					if (!isAir(world, i, j1, k - 1) || !isAir(world, i, j1, k + 1) || !isAir(world, i - 1, j1, k) || !isAir(world, i + 1, j1, k))
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
				setBlockAndMetadata(world, i, j1, k, LOTRMod.planks, 1);
				setBlockAndMetadata(world, i, j1, k - 1, LOTRMod.mallornTorch, 4);
				setBlockAndMetadata(world, i, j1, k + 1, LOTRMod.mallornTorch, 3);
				setBlockAndMetadata(world, i - 1, j1, k, LOTRMod.mallornTorch, 1);
				setBlockAndMetadata(world, i + 1, j1, k, LOTRMod.mallornTorch, 2);
			}
			else
			{
				setBlockAndMetadata(world, i, j1, k, LOTRMod.fence, 1);
			}
		}
	}
	
	private ItemStack getRandomPlant(Random random)
	{
		return random.nextBoolean() ? new ItemStack(LOTRMod.elanor) : new ItemStack(LOTRMod.niphredil);
	}
}
