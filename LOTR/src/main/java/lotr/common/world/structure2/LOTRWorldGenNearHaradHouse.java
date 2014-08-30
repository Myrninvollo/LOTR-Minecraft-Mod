package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNearHaradrim;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenNearHaradHouse extends LOTRWorldGenStructureBase2
{
	public LOTRWorldGenNearHaradHouse(boolean flag)
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
		
		setOrigin(i, j, k);
		
		if (restrictions)
		{
			for (int i1 = -4; i1 <= 4; i1++)
			{
				for (int k1 = -4; k1 <= 4; k1++)
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
		
		for (int i1 = -3; i1 <= 3; i1++)
		{
			for (int k1 = -3; k1 <= 3; k1++)
			{
				for (int j1 = -1; !isOpaque(world, i1, j1, k1) && getY(j1) >= 0; j1--)
				{
					setBlockAndMetadata(world, i1, j1, k1, Blocks.sandstone, 2);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
				
				int i2 = Math.abs(i1);
				int k2 = Math.abs(k1);
				
				for (int j1 = 1; j1 <= 7; j1++)
				{
					setAir(world, i1, j1, k1);
				}
				
				if (i2 < 3 && k2 < 3)
				{
					setBlockAndMetadata(world, i1, 0, k1, Blocks.stained_hardened_clay, 0);
				}
				
				if (i2 == 3 && k2 == 3)
				{
					for (int j1 = 0; j1 <= 4; j1++)
					{
						setBlockAndMetadata(world, i1, j1, k1, Blocks.hardened_clay, 0);
					}
				}
				else if (i2 == 3 || k2 == 3)
				{
					setBlockAndMetadata(world, i1, 0, k1, Blocks.sandstone, 2);
					setBlockAndMetadata(world, i1, 1, k1, Blocks.sandstone, 2);
					setBlockAndMetadata(world, i1, 2, k1, LOTRMod.brick, 15);
					setBlockAndMetadata(world, i1, 3, k1, Blocks.sandstone, 2);
					setBlockAndMetadata(world, i1, 4, k1, LOTRMod.brick, 15);
				}
				
				setBlockAndMetadata(world, i1, 5, k1, Blocks.sandstone, 2);
				
				if (i2 == 3 || k2 == 3)
				{
					setBlockAndMetadata(world, i1, 6, k1, LOTRMod.wall, 15);
				}
				
				if (i2 == 2 && k2 == 2)
				{
					setBlockAndMetadata(world, i1, 5, k1, Blocks.grass, 0);
					setBlockAndMetadata(world, i1, 6, k1, LOTRMod.doubleFlower, doubleFlowerMeta);
					setBlockAndMetadata(world, i1, 7, k1, LOTRMod.doubleFlower, 8);
				}
			}
		}
		
		setBlockAndMetadata(world, 0, 0, -3, Blocks.stained_hardened_clay, 0);
		setBlockAndMetadata(world, 0, 1, -3, Blocks.wooden_door, 1);
		setBlockAndMetadata(world, 0, 2, -3, Blocks.wooden_door, 8);
		
		setAir(world, -3, 3, -1);
		setAir(world, -3, 3, 1);
		setAir(world, 3, 3, -1);
		setAir(world, 3, 3, 1);
		
		for (int j1 = 1; j1 <= 5; j1++)
		{
			setBlockAndMetadata(world, 0, j1, 2, Blocks.ladder, 2);
		}
		
		for (int i1 = -1; i1 <= 1; i1 += 2)
		{
			setBlockAndMetadata(world, i1, 6, 0, Blocks.bed, 2);
			setBlockAndMetadata(world, i1, 6, -1, Blocks.bed, 10);
		}
		
		for (int k1 = -2; k1 <= 2; k1++)
		{
			setBlockAndMetadata(world, -2, 4, k1, LOTRMod.stairsNearHaradBrick, 4);
			setBlockAndMetadata(world, 2, 4, k1, LOTRMod.stairsNearHaradBrick, 5);
		}
		
		for (int i1 = -2; i1 <= 2; i1 += 4)
		{
			setBlockAndMetadata(world, i1, 1, -2, LOTRMod.slabSingle4, 8);
			setBlockAndMetadata(world, i1, 2, -2, LOTRMod.wall, 15);
			setBlockAndMetadata(world, i1, 3, -2, Blocks.torch, 5);
			
			setBlockAndMetadata(world, i1, 1, 0, LOTRMod.brick, 15);
			
			setBlockAndMetadata(world, i1, 1, 2, LOTRMod.slabSingle4, 8);
			setBlockAndMetadata(world, i1, 2, 2, LOTRMod.wall, 15);
			setBlockAndMetadata(world, i1, 3, 2, Blocks.torch, 5);
		}
		
		setBlockAndMetadata(world, -2, 1, -1, Blocks.crafting_table, 0);
		setBlockAndMetadata(world, -2, 1, 1, LOTRMod.nearHaradTable, 0);
		setBlockAndMetadata(world, 2, 1, -1, Blocks.furnace, 5);
		placeChest(world, random, 2, 1, 1, 5, LOTRChestContents.NEAR_HARAD_HOUSE);
		
		placeMug(world, random, -2, 2, 0, 3, LOTRMod.mugAraq);
		placePlateWithCertainty(world, 2, 2, 0, random, LOTRFoods.NEAR_HARAD);
		
		LOTREntityNearHaradrim haradrim = new LOTREntityNearHaradrim(world);
		spawnNPCAndSetHome(haradrim, world, 0, 1, 0, 8);
		
		return true;
	}
}
