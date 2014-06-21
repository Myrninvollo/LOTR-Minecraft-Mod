package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNearHaradrim;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenNearHaradLargeHouse extends LOTRWorldGenStructureBase2
{
	public LOTRWorldGenNearHaradLargeHouse(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass)
			{
				return false;
			}
		}
		
		j--;
		
		setRotationMode(rotation);
		
		switch (getRotationMode())
		{
			case 0:
				k += 1;
				i += 5;
				break;
			case 1:
				i -= 1;
				k += 5;
				break;
			case 2:
				k -= 1;
				i -= 5;
				break;
			case 3:
				i += 1;
				k -= 5;
				break;
		}
		
		setOrigin(i, j, k);
		
		if (restrictions)
		{
			for (int i1 = 0; i1 <= 11; i1++)
			{
				for (int k1 = 0; k1 <= 8; k1++)
				{
					int j1 = getTopBlock(world, i1, k1);
					Block block = getBlock(world, i1, j1 - 1, k1);
					if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.sand)
					{
						return false;
					}
				}
			}
		}
		
		int doubleFlowerMeta = 0;
		if (random.nextBoolean())
		{
			doubleFlowerMeta = 2;
		}
		else
		{
			doubleFlowerMeta = 3;
		}
		
		for (int i1 = 0; i1 <= 11; i1++)
		{
			for (int k1 = 0; k1 <= 8; k1++)
			{
				for (int j1 = 0; (j1 == 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; j1--)
				{
					setBlockAndMetadata(world, i1, j1, k1, Blocks.sandstone, 2);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
				
				for (int j1 = 1; j1 <= 10; j1++)
				{
					setAir(world, i1, j1, k1);
				}
			}
		}
		
		for (int j1 = 1; j1 <= 9; j1++)
		{
			setBlockAndMetadata(world, 0, j1, 0, Blocks.hardened_clay, 0);
			setBlockAndMetadata(world, 0, j1, 8, Blocks.hardened_clay, 0);
			setBlockAndMetadata(world, 5, j1, 0, Blocks.hardened_clay, 0);
			setBlockAndMetadata(world, 5, j1, 8, Blocks.hardened_clay, 0);
		}
		
		for (int j1 = 1; j1 <= 4; j1++)
		{
			setBlockAndMetadata(world, 11, j1, 0, Blocks.hardened_clay, 0);
			setBlockAndMetadata(world, 11, j1, 8, Blocks.hardened_clay, 0);
		}
		
		for (int k1 = 0; k1 <= 8; k1 += 8)
		{
			for (int i1 = 1; i1 <= 10; i1++)
			{
				setBlockAndMetadata(world, i1, 1, k1, Blocks.sandstone, 2);
				setBlockAndMetadata(world, i1, 2, k1, LOTRMod.brick, 15);
				setBlockAndMetadata(world, i1, 3, k1, Blocks.sandstone, 2);
				setBlockAndMetadata(world, i1, 4, k1, LOTRMod.brick, 15);
			}
			
			for (int i1 = 1; i1 <= 4; i1++)
			{
				setBlockAndMetadata(world, i1, 5, k1, Blocks.sandstone, 2);
				setBlockAndMetadata(world, i1, 6, k1, LOTRMod.brick, 15);
				setBlockAndMetadata(world, i1, 7, k1, LOTRMod.brick, 15);
				setBlockAndMetadata(world, i1, 8, k1, LOTRMod.brick, 15);
				setBlockAndMetadata(world, i1, 9, k1, Blocks.sandstone, 2);
			}
		}
		
		for (int i1 = 0; i1 <= 11; i1 += 11)
		{
			for (int k1 = 1; k1 <= 7; k1++)
			{
				setBlockAndMetadata(world, i1, 1, k1, Blocks.sandstone, 2);
				setBlockAndMetadata(world, i1, 2, k1, LOTRMod.brick, 15);
				setBlockAndMetadata(world, i1, 3, k1, Blocks.sandstone, 2);
				setBlockAndMetadata(world, i1, 4, k1, LOTRMod.brick, 15);
			}
		}
		
		for (int i1 = 0; i1 <= 5; i1 += 5)
		{
			for (int k1 = 1; k1 <= 7; k1++)
			{
				setBlockAndMetadata(world, i1, 5, k1, Blocks.sandstone, 2);
				setBlockAndMetadata(world, i1, 6, k1, LOTRMod.brick, 15);
				setBlockAndMetadata(world, i1, 7, k1, LOTRMod.brick, 15);
				setBlockAndMetadata(world, i1, 8, k1, LOTRMod.brick, 15);
				setBlockAndMetadata(world, i1, 9, k1, Blocks.sandstone, 2);
			}
		}
		
		for (int i1 = 1; i1 <= 10; i1++)
		{
			for (int k1 = 1; k1 <= 7; k1++)
			{
				setBlockAndMetadata(world, i1, 4, k1, LOTRMod.brick, 15);
			}
		}
		
		for (int i1 = 1; i1 <= 4; i1++)
		{
			for (int k1 = 1; k1 <= 7; k1++)
			{
				setBlockAndMetadata(world, i1, 9, k1, LOTRMod.brick, 15);
			}
		}
		
		placeWindow(world, 2, 2, 0);
		placeWindow(world, 8, 2, 0);
		placeWindow(world, 2, 6, 0);
		placeWindow(world, 2, 2, 8);
		placeWindow(world, 8, 2, 8);
		placeWindow(world, 2, 6, 8);
		
		for (int i1 = 1; i1 <= 10; i1++)
		{
			for (int k1 = 1; k1 <= 7; k1++)
			{
				setBlockAndMetadata(world, i1, 0, k1, LOTRMod.planks, 14);
			}
		}
		
		setBlockAndMetadata(world, 5, 0, 0, LOTRMod.planks, 14);
		setBlockAndMetadata(world, 5, 1, 0, Blocks.wooden_door, 1);
		setBlockAndMetadata(world, 5, 2, 0, Blocks.wooden_door, 8);
		setBlockAndMetadata(world, 6, 0, 0, LOTRMod.planks, 14);
		setBlockAndMetadata(world, 6, 1, 0, Blocks.wooden_door, 1);
		setBlockAndMetadata(world, 6, 2, 0, Blocks.wooden_door, 9);
		
		for (int k1 = 1; k1 <= 7; k1++)
		{
			setBlockAndMetadata(world, 1, 3, k1, LOTRMod.stairsNearHaradBrick, 4);
			setBlockAndMetadata(world, 10, 3, k1, LOTRMod.stairsNearHaradBrick, 5);
		}
	
		for (int i1 = 4; i1 <= 7; i1++)
		{
			for (int k1 = 6; k1 <= 7; k1++)
			{
				setBlockAndMetadata(world, i1, 1, k1, Blocks.planks, 0);
				if (random.nextInt(3) == 0)
				{
					placeMug(world, random, i1, 2, k1, random.nextInt(4), LOTRMod.mugAraq);
				}
				else
				{
					placePlate(world, i1, 2, k1, random, LOTRFoods.NEAR_HARAD);
				}
			}
		}
		
		setBlockAndMetadata(world, 1, 1, 2, Blocks.planks, 0);
		placeBarrel(world, random, 1, 2, 2, 4, LOTRMod.mugAraq);
		setBlockAndMetadata(world, 1, 1, 3, Blocks.planks, 0);
		placeMug(world, random, 1, 2, 3, 3, LOTRMod.mugAraq);
		
		setBlockAndMetadata(world, 10, 1, 3, Blocks.furnace, 5);
		setBlockAndMetadata(world, 10, 1, 2, Blocks.furnace, 5);
		setBlockAndMetadata(world, 10, 1, 1, Blocks.chest, 5);
		fillChest(world, random, 10, 1, 1, LOTRChestContents.NEAR_HARAD_HOUSE);
		
		setBlockAndMetadata(world, 4, 3, 1, Blocks.torch, 3);
		setBlockAndMetadata(world, 7, 3, 1, Blocks.torch, 3);
		setBlockAndMetadata(world, 4, 3, 7, Blocks.torch, 4);
		setBlockAndMetadata(world, 7, 3, 7, Blocks.torch, 4);
		
		for (int j1 = 1; j1 <= 8; j1++)
		{
			setBlockAndMetadata(world, 1, j1, 7, Blocks.ladder, 4);
		}
		
		setBlockAndMetadata(world, 2, 5, 2, Blocks.bed, 2);
		setBlockAndMetadata(world, 2, 5, 1, Blocks.bed, 10);
		setBlockAndMetadata(world, 3, 5, 2, Blocks.bed, 2);
		setBlockAndMetadata(world, 3, 5, 1, Blocks.bed, 10);
		
		setBlockAndMetadata(world, 1, 8, 4, Blocks.torch, 2);
		
		setBlockAndMetadata(world, 5, 5, 4, Blocks.wooden_door, 0);
		setBlockAndMetadata(world, 5, 6, 4, Blocks.wooden_door, 8);
		
		for (int k1 = 1; k1 <= 7; k1 += 6)
		{
			setBlockAndMetadata(world, 10, 4, k1, Blocks.grass, 0);
			setBlockAndMetadata(world, 10, 5, k1, LOTRMod.doubleFlower, doubleFlowerMeta);
			setBlockAndMetadata(world, 10, 6, k1, LOTRMod.doubleFlower, 8);
		}
		
		for (int i1 = 6; i1 <= 11; i1++)
		{
			setBlockAndMetadata(world, i1, 5, 0, LOTRMod.wall, 15);
			setBlockAndMetadata(world, i1, 5, 8, LOTRMod.wall, 15);
		}
		
		for (int k1 = 1; k1 <= 7; k1++)
		{
			setBlockAndMetadata(world, 11, 5, k1, LOTRMod.wall, 15);
		}
		
		for (int i1 = 0; i1 <= 5; i1++)
		{
			setBlockAndMetadata(world, i1, 10, 0, LOTRMod.brick, 15);
			setBlockAndMetadata(world, i1, 10, 8, LOTRMod.brick, 15);
		}
		
		for (int k1 = 1; k1 <= 7; k1++)
		{
			setBlockAndMetadata(world, 0, 10, k1, LOTRMod.brick, 15);
			setBlockAndMetadata(world, 5, 10, k1, LOTRMod.brick, 15);
		}
		
		LOTREntityNearHaradrim haradrim = new LOTREntityNearHaradrim(world);
		spawnNPCAndSetHome(haradrim, world, 5, 1, 4, 16);
		
		return true;
	}
	
	private void placeWindow(World world, int i, int j, int k)
	{
		setAir(world, i, j, k);
		setAir(world, i + 1, j, k);
		setBlockAndMetadata(world, i, j + 1, k, LOTRMod.stairsNearHaradBrick, 4);
		setBlockAndMetadata(world, i + 1, j + 1, k, LOTRMod.stairsNearHaradBrick, 5);
	}
}
