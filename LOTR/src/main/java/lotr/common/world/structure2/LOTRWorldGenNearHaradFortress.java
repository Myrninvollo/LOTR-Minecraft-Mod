package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.*;
import lotr.common.entity.npc.LOTREntityNearHaradrim;
import lotr.common.entity.npc.LOTREntityNearHaradrimArcher;
import lotr.common.entity.npc.LOTREntityNearHaradrimWarlord;
import lotr.common.entity.npc.LOTREntityNearHaradrimWarrior;
import lotr.common.world.feature.LOTRWorldGenDatePalm;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class LOTRWorldGenNearHaradFortress extends LOTRWorldGenStructureBase2
{
	public LOTRWorldGenNearHaradFortress(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation)
	{
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
			for (int i1 = 0; i1 <= 19; i1++)
			{
				for (int k1 = 0; k1 <= 7; k1++)
				{
					int j1 = getTopBlock(world, i1, k1);
					Block block = getBlock(world, i1, j1 - 1, k1);
					if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.sand)
					{
						return false;
					}
				}
			}
			
			for (int i1 = 2; i1 <= 12; i1++)
			{
				int j1 = getTopBlock(world, i1, -1) - 1;
				if (j1 != 0)
				{
					return false;
				}
			}
		}
		
		for (int i1 = 0; i1 <= 19; i1++)
		{
			for (int k1 = 0; k1 <= 7; k1++)
			{
				for (int j1 = 0; (j1 == 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; j1--)
				{
					setBlockAndMetadata(world, i1, j1, k1, LOTRMod.brick, 15);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
				
				for (int j1 = 1; j1 <= 10; j1++)
				{
					setAir(world, i1, j1, k1);
				}
			}
		}
		
		for (int i1 = 0; i1 <= 14; i1++)
		{
			for (int k1 = 0; k1 <= 7; k1 += 7)
			{
				for (int j1 = 1; j1 <= 5; j1++)
				{
					setBlockAndMetadata(world, i1, j1, k1, LOTRMod.brick, 15);
				}
				setBlockAndMetadata(world, i1, 6, k1, LOTRMod.wall, 15);
			}
		}
		
		for (int k1 = 0; k1 <= 7; k1++)
		{
			for (int i1 = 0; i1 <= 14; i1 += 14)
			{
				for (int j1 = 1; j1 <= 5; j1++)
				{
					setBlockAndMetadata(world, i1, j1, k1, LOTRMod.brick, 15);
				}
				setBlockAndMetadata(world, i1, 6, k1, LOTRMod.wall, 15);
			}
		}
		
		for (int i1 = 1; i1 <= 13; i1++)
		{
			for (int k1 = 1; k1 <= 6; k1++)
			{
				setBlockAndMetadata(world, i1, 0, k1, LOTRMod.planks2, 2);
				setBlockAndMetadata(world, i1, 5, k1, LOTRMod.brick, 15);
			}
		}
		
		for (int i1 = 2; i1 <= 11; i1 += 3)
		{
			for (int i2 = i1; i2 <= i1 + 1; i2++)
			{
				setBlockAndMetadata(world, i2, 0, 0, LOTRMod.planks2, 2);
				setAir(world, i2, 1, 0);
				setAir(world, i2, 2, 0);
			}
			setBlockAndMetadata(world, i1, 3, 0, LOTRMod.stairsNearHaradBrick, 4);
			setBlockAndMetadata(world, i1 + 1, 3, 0, LOTRMod.stairsNearHaradBrick, 5);
		}
		
		for (int i2 = 4; i2 <= 10; i2 += 3)
		{
			placeWallBanner(world, i2, 5, 0, LOTRFaction.NEAR_HARAD, 2);
			
			setBlockAndMetadata(world, i2, 4, 1, Blocks.torch, 3);
		}
	
		for (int j1 = 1; j1 <= 10; j1++)
		{
			for (int k1 = 3; k1 <= 6; k1++)
			{
				for (int i1 = 14; i1 <= 19; i1 += 5)
				{
					setBlockAndMetadata(world, i1, j1, k1, LOTRMod.brick, 15);
				}
			}
			
			for (int i1 = 15; i1 <= 18; i1++)
			{
				for (int k1 = 2; k1 <= 7; k1 += 5)
				{
					setBlockAndMetadata(world, i1, j1, k1, LOTRMod.brick, 15);
				}
			}
		}
		
		for (int i1 = 15; i1 <= 18; i1++)
		{
			for (int k1 = 3; k1 <= 6; k1++)
			{
				setBlockAndMetadata(world, i1, 0, k1, LOTRMod.planks2, 2);
				setBlockAndMetadata(world, i1, 5, k1, LOTRMod.brick, 15);
				setBlockAndMetadata(world, i1, 9, k1, Blocks.sandstone, 2);
			}
		}
		
		for (int i1 = 15; i1 <= 19; i1++)
		{
			for (int k1 = 0; k1 <= 1; k1++)
			{
				setBlockAndMetadata(world, i1, 1, k1, LOTRMod.slabSingle4, 0);
			}
		}
		
		setBlockAndMetadata(world, 19, 1, 2, LOTRMod.slabSingle4, 0);
		setBlockAndMetadata(world, 19, 1, 7, LOTRMod.slabSingle4, 0);
		
		for (int i1 = 0; i1 <= 6; i1++)
		{
			for (int k1 = 3; k1 <= 7; k1 += 4)
			{
				for (int j1 = 6; j1 <= 9; j1++)
				{
					setBlockAndMetadata(world, i1, j1, k1, LOTRMod.brick, 15);
				}
				setBlockAndMetadata(world, i1, 10, k1, LOTRMod.wall, 15);
			}
		}
		
		for (int k1 = 3; k1 <= 7; k1++)
		{
			for (int i1 = 0; i1 <= 6; i1 += 6)
			{
				for (int j1 = 6; j1 <= 9; j1++)
				{
					setBlockAndMetadata(world, i1, j1, k1, LOTRMod.brick, 15);
				}
				setBlockAndMetadata(world, i1, 10, k1, LOTRMod.wall, 15);
			}
		}
		
		for (int i1 = 1; i1 <= 5; i1++)
		{
			for (int k1 = 4; k1 <= 6; k1++)
			{
				setBlockAndMetadata(world, i1, 9, k1, LOTRMod.brick, 15);
			}
		}
		
		setBlockAndMetadata(world, 3, 9, 5, Blocks.dirt, 0);
		
		for (int l = 0; l < 20; l++)
		{
			if (new LOTRWorldGenDatePalm(notifyChanges).generate(world, random, getX(3, 5), getY(10), getZ(3, 5)))
			{
				break;
			}
		}
		
		setBlockAndMetadata(world, 3, 9, 5, LOTRMod.brick, 15);
		
		placeWindow(world, 16, 7, 2);
		placeWindow(world, 16, 7, 7);
		
		setAir(world, 19, 7, 4);
		setAir(world, 19, 7, 5);
		setBlockAndMetadata(world, 19, 8, 4, LOTRMod.stairsNearHaradBrick, 7);
		setBlockAndMetadata(world, 19, 8, 5, LOTRMod.stairsNearHaradBrick, 6);
		
		placeWindow(world, 2, 2, 7);
		placeWindow(world, 11, 2, 7);
		placeWindow(world, 16, 2, 7);
		
		setBlockAndMetadata(world, 5, 3, 6, Blocks.torch, 4);
		setBlockAndMetadata(world, 9, 3, 6, Blocks.torch, 4);
		
		placeArmorStand(world, 1, 1, 2, 3, new ItemStack[4]);
		placeArmorStand(world, 1, 1, 5, 3, new ItemStack[4]);
		
		setBlockAndMetadata(world, 5, 1, 6, Blocks.furnace, 2);
		for (int i1 = 6; i1 <= 7; i1++)
		{
			placeChest(world, random, i1, 1, 6, 2, LOTRChestContents.NEAR_HARAD_HOUSE);
		}
		setBlockAndMetadata(world, 8, 1, 6, Blocks.crafting_table, 0);
		setBlockAndMetadata(world, 9, 1, 6, LOTRMod.nearHaradTable, 0);
		
		setBlockAndMetadata(world, 13, 1, 1, Blocks.planks, 0);
		placePlateWithCertainty(world, 13, 2, 1, random, LOTRFoods.NEAR_HARAD);
		setBlockAndMetadata(world, 13, 1, 2, Blocks.planks, 0);
		placeBarrel(world, random, 13, 2, 2, 5, LOTRMod.mugAraq);
		setBlockAndMetadata(world, 13, 1, 3, Blocks.planks, 0);
		placeMug(world, random, 13, 2, 3, 1, LOTRMod.mugAraq);
		
		for (int k1 = 5; k1 <= 6; k1++)
		{
			placeChest(world, random, 18, 1, k1, 5, LOTRChestContents.NEAR_HARAD_TOWER);
			setBlockAndMetadata(world, 18, 3, k1, LOTRMod.brick, 15);
		}
		
		setBlockAndMetadata(world, 14, 0, 4, LOTRMod.planks2, 2);
		setBlockAndMetadata(world, 14, 0, 5, LOTRMod.planks2, 2);
		
		for (int j1 = 1; j1 <= 6; j1 += 5)
		{
			for (int k1 = 4; k1 <= 5; k1++)
			{
				setAir(world, 14, j1, k1);
				setAir(world, 14, j1 + 1, k1);
			}
			setBlockAndMetadata(world, 14, j1 + 2, 4, LOTRMod.stairsNearHaradBrick, 7);
			setBlockAndMetadata(world, 14, j1 + 2, 5, LOTRMod.stairsNearHaradBrick, 6);
		}
		
		setBlockAndMetadata(world, 16, 1, 3, LOTRMod.stairsNearHaradBrick, 1);
		setAir(world, 16, 5, 3);
		setBlockAndMetadata(world, 17, 1, 3, LOTRMod.brick, 15);
		setBlockAndMetadata(world, 17, 2, 3, LOTRMod.stairsNearHaradBrick, 1);
		setAir(world, 17, 5, 3);
		setBlockAndMetadata(world, 18, 1, 3, LOTRMod.brick, 15);
		setBlockAndMetadata(world, 18, 2, 3, LOTRMod.brick, 15);
		setAir(world, 18, 5, 3);
		setBlockAndMetadata(world, 18, 1, 4, LOTRMod.brick, 15);
		setBlockAndMetadata(world, 18, 2, 4, LOTRMod.brick, 15);
		setBlockAndMetadata(world, 18, 3, 4, LOTRMod.stairsNearHaradBrick, 2);
		setAir(world, 18, 5, 4);
		setBlockAndMetadata(world, 18, 4, 5, LOTRMod.stairsNearHaradBrick, 2);
		setAir(world, 18, 5, 5);
		setBlockAndMetadata(world, 18, 4, 6, LOTRMod.brick, 15);
		setBlockAndMetadata(world, 18, 5, 6, LOTRMod.stairsNearHaradBrick, 2);
		
		setBlockAndMetadata(world, 6, 6, 5, Blocks.wooden_door, 0);
		setBlockAndMetadata(world, 6, 7, 5, Blocks.wooden_door, 8);
		
		for (int i1 = 1; i1 <= 4; i1++)
		{
			for (int k1 = 4; k1 <= 6; k1++)
			{
				setBlockAndMetadata(world, i1, 6, k1, Blocks.carpet, 14);
			}
		}
		
		setBlockAndMetadata(world, 0, 6, 5, Blocks.sandstone, 2);
		setAir(world, 0, 7, 5);
		setBlockAndMetadata(world, 0, 8, 5, LOTRMod.slabSingle4, 8);
		
		int warriors = 2 + random.nextInt(4);
		for (int l = 0; l < warriors; l++)
		{
			LOTREntityNearHaradrim warrior = random.nextInt(3) == 0 ? new LOTREntityNearHaradrimArcher(world) : new LOTREntityNearHaradrimWarrior(world);
			warrior.spawnRidingHorse = false;
			spawnNPCAndSetHome(warrior, world, 7, 1, 3, 16);
		}
		
		spawnNPCAndSetHome(new LOTREntityNearHaradrimWarlord(world), world, 7, 6, 3, 16);
		
		if (usingPlayer == null)
		{
			LOTRLevelData.nearHaradFortressLocations.add(new ChunkCoordinates(getX(7, 3), getY(0), getZ(7, 3)));
		}
		
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
