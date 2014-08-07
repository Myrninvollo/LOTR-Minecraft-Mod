package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockPlate;
import lotr.common.entity.npc.*;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class LOTRWorldGenMeadHall extends LOTRWorldGenStructureBase2
{
	private Block plankBlock;
	private int plankMeta;
	
	private Block woodBlock;
	private int woodMeta;
	
	private Block stairBlock;
	
	private Block halfBlock;
	private int halfMeta;
	
	private Block floorBlock;
	private int floorMeta;
	
	private String[] meadHallName;
	
	public LOTRWorldGenMeadHall(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation)
	{
		if (restrictions)
		{
			if (world.getBiomeGenForCoords(i, k) != LOTRBiome.rohan)
			{
				return false;
			}
			if (world.getBlock(i, j - 1, k) != Blocks.grass)
			{
				return false;
			}
		}
		
		j--;
		
		setOrigin(i, j, k);
		setRotationMode(rotation);
		
		int randomWood = random.nextInt(2);
		
		switch (randomWood)
		{
			case 0:
				plankBlock = Blocks.planks;
				plankMeta = 0;
				
				woodBlock = Blocks.log;
				woodMeta = 0;
				
				stairBlock = Blocks.oak_stairs;
				
				halfBlock = Blocks.wooden_slab;
				halfMeta = 0;
				
				break;
			case 1:
				plankBlock = Blocks.planks;
				plankMeta = 1;
				
				woodBlock = Blocks.log;
				woodMeta = 1;
				
				stairBlock = Blocks.spruce_stairs;
				
				halfBlock = Blocks.wooden_slab;
				halfMeta = 1;
				
				break;
		}
		
		int randomFloor = random.nextInt(3);
		switch (randomFloor)
		{
			case 0:
				floorBlock = Blocks.stonebrick;
				floorMeta = 0;
				break;
			case 1:
				floorBlock = Blocks.brick_block;
				floorMeta = 0;
				break;
			case 2:
				floorBlock = LOTRMod.brick;
				floorMeta = 4;
				break;
		}
		
		meadHallName = LOTRNames.getRandomRohanMeadHallName(random);
		
		if (restrictions)
		{
			for (int k1 = 1; k1 <= 24; k1++)
			{
				for (int i1 = -5; i1 <= 5; i1++)
				{
					int j1 = getTopBlock(world, i1, k1) - 1;
					if (getBlock(world, i1, j1, k1) != Blocks.grass)
					{
						return false;
					}
				}
			}
		}
		
		for (int k1 = 1; k1 <= 24; k1++)
		{
			for (int i1 = -5; i1 <= 5; i1++)
			{
				for (int j1 = 0; (j1 == 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; j1--)
				{
					if (Math.abs(i1) == 5 && (k1 == 1 || k1 == 23))
					{
						setBlockAndMetadata(world, i1, j1, k1, woodBlock, woodMeta);
					}
					else if (Math.abs(i) == 5 || k1 == 1 || k1 == 24)
					{
						setBlockAndMetadata(world, i1, j1, k1, plankBlock, plankMeta);
					}
					else
					{
						if (j1 == 0)
						{
							setBlockAndMetadata(world, i1, j1, k1, floorBlock, floorMeta);
						}
						else
						{
							setBlockAndMetadata(world, i1, j1, k1, Blocks.dirt, 0);
						}
					}
					
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
				
				for (int j1 = 1; j1 <= 5; j1++)
				{
					setAir(world, i1, j1, k1);
				}
			}
		}
		
		for (int k1 = 2; k1 <= 22; k1++)
		{
			for (int j1 = 1; j1 <= 3; j1++)
			{
				setBlockAndMetadata(world, -5, j1, k1, plankBlock, plankMeta);
				setBlockAndMetadata(world, 5, j1, k1, plankBlock, plankMeta);
			}
		}
		
		for (int k1 = 0; k1 <= 24; k1++)
		{
			setBlockAndMetadata(world, -5, 4, k1, woodBlock, woodMeta | 8);
			setBlockAndMetadata(world, 5, 4, k1, woodBlock, woodMeta | 8);
			setBlockAndMetadata(world, 0, 6, k1, woodBlock, woodMeta | 8);
		}
		
		for (int i1 = -4; i1 <= 4; i1++)
		{
			for (int j1 = 1; j1 <= 4; j1++)
			{
				setBlockAndMetadata(world, i1, j1, 1, plankBlock, plankMeta);
				setBlockAndMetadata(world, i1, j1, 23, plankBlock, plankMeta);
			}
		}
		
		for (int j1 = 1; j1 <= 3; j1++)
		{
			setBlockAndMetadata(world, -5, j1, 1, woodBlock, woodMeta);
			setBlockAndMetadata(world, -5, j1, 23, woodBlock, woodMeta);
			setBlockAndMetadata(world, 5, j1, 1, woodBlock, woodMeta);
			setBlockAndMetadata(world, 5, j1, 23, woodBlock, woodMeta);
		}
		
		for (int k1 = 1; k1 <= 23; k1++)
		{
			setBlockAndMetadata(world, -4, 5, k1, LOTRMod.slabSingleThatch, 0);
			setBlockAndMetadata(world, -3, 5, k1, LOTRMod.slabSingleThatch, 8);
			setBlockAndMetadata(world, -2, 6, k1, LOTRMod.slabSingleThatch, 0);
			setBlockAndMetadata(world, -1, 6, k1, LOTRMod.slabSingleThatch, 8);
			setBlockAndMetadata(world, 0, 7, k1, LOTRMod.slabSingleThatch, 0);
			setBlockAndMetadata(world, 1, 6, k1, LOTRMod.slabSingleThatch, 8);
			setBlockAndMetadata(world, 2, 6, k1, LOTRMod.slabSingleThatch, 0);
			setBlockAndMetadata(world, 3, 5, k1, LOTRMod.slabSingleThatch, 8);
			setBlockAndMetadata(world, 4, 5, k1, LOTRMod.slabSingleThatch, 0);
		}

		for (int i1 = -3; i1 <= 3; i1++)
		{
			setBlockAndMetadata(world, i1, 5, 23, plankBlock, plankMeta);
		}
		
		for (int j1 = 5; j1 >= 1; j1--)
		{
			setBlockAndMetadata(world, -(j - j1) - 5, j1, 23, woodBlock, woodMeta);
			setBlockAndMetadata(world, (j - j1) + 5, j1, 23, woodBlock, woodMeta);
		}
		
		for (int k1 = 1; k1 <= 23; k1 += 22)
		{
			setBlockAndMetadata(world, -3, 4, k1, woodBlock, woodMeta);
			setBlockAndMetadata(world, -3, 5, k1, LOTRMod.thatch, 0);
			setBlockAndMetadata(world, -2, 5, k1, plankBlock, plankMeta);
			setBlockAndMetadata(world, -1, 5, k1, woodBlock, woodMeta);
			setBlockAndMetadata(world, -1, 6, k1, LOTRMod.thatch, 0);
			setBlockAndMetadata(world, 0, 5, k1, plankBlock, plankMeta);
			setBlockAndMetadata(world, 1, 6, k1, LOTRMod.thatch, 0);
			setBlockAndMetadata(world, 1, 5, k1, woodBlock, woodMeta);
			setBlockAndMetadata(world, 2, 5, k1, plankBlock, plankMeta);
			setBlockAndMetadata(world, 3, 5, k1, LOTRMod.thatch, 0);
			setBlockAndMetadata(world, 3, 4, k1, woodBlock, woodMeta);
		}
		
		for (int i1 = -5; i1 <= 5; i1++)
		{
			setBlockAndMetadata(world, i1, 1, 24, halfBlock, halfMeta);
		}
		
		for (int j1 = 3; j1 >= 1; j1--)
		{
			for (int i1 = j1 - 3; i1 <= -j1 + 3; i1++)
			{
				setBlockAndMetadata(world, i1, j1, 24, plankBlock, plankMeta);
			}
		}
		
		setBlockAndMetadata(world, 0, 0, 1, floorBlock, floorMeta);
		setBlockAndMetadata(world, 0, 1, 1, Blocks.wooden_door, 1);
		setBlockAndMetadata(world, 0, 2, 1, Blocks.wooden_door, 8);
		setBlockAndMetadata(world, -1, 2, 0, Blocks.torch, 4);
		setBlockAndMetadata(world, 1, 2, 0, Blocks.torch, 4);
		
		setBlockAndMetadata(world, 0, 5, 5, LOTRMod.chandelier, 1);
		setBlockAndMetadata(world, 0, 5, 11, LOTRMod.chandelier, 1);
		setBlockAndMetadata(world, 0, 5, 17, LOTRMod.chandelier, 1);
		
		setBlockAndMetadata(world, -3, 3, 2, Blocks.torch, 3);
		setBlockAndMetadata(world, 0, 3, 2, Blocks.torch, 3);
		setBlockAndMetadata(world, 3, 3, 2, Blocks.torch, 3);
		
		setBlockAndMetadata(world, -4, 1, 2, plankBlock, plankMeta);
		setBlockAndMetadata(world, -3, 1, 2, halfBlock, halfMeta | 8);
		setBlockAndMetadata(world, -2, 1, 2, plankBlock, plankMeta);
		placeBarrel(world, random, -4, 2, 2, 3, LOTRMod.mugMead);
		placeBarrel(world, random, -3, 2, 2, 3, LOTRMod.mugMead);
		placeMug(world, random, -2, 2, 2, 2, LOTRMod.mugMead);
		
		setBlockAndMetadata(world, 4, 1, 2, plankBlock, plankMeta);
		setBlockAndMetadata(world, 3, 1, 2, halfBlock, halfMeta | 8);
		setBlockAndMetadata(world, 2, 1, 2, plankBlock, plankMeta);
		placeBarrel(world, random, 4, 2, 2, 3, LOTRMod.mugMead);
		placeBarrel(world, random, 3, 2, 2, 3, LOTRMod.mugMead);
		placeMug(world, random, 2, 2, 2, 2, LOTRMod.mugMead);
		
		for (int k1 = 6; k1 <= 16; k1 += 2)
		{
			setBlockAndMetadata(world, -3, 1, k1, stairBlock, 0);
			setBlockAndMetadata(world, 3, 1, k1, stairBlock, 1);
		}
		
		for (int k1 = 4; k1 <= 20; k1 += 2)
		{
			setBlockAndMetadata(world, -4, 3, k1, Blocks.torch, 2);
			setBlockAndMetadata(world, 4, 3, k1, Blocks.torch, 1);
		}
		
		for (int k1 = 5; k1 <= 21; k1 += 4)
		{
			for (int j1 = 1; j1 <= 4; j1++)
			{
				setBlockAndMetadata(world, -4, j1, k1, woodBlock, woodMeta);
				setBlockAndMetadata(world, 4, j1, k1, woodBlock, woodMeta);
			}
		}
		
		for (int k1 = 7; k1 <= 15; k1 += 4)
		{
			spawnItemFrame(world, -5, 2, k1, 1, random);
			spawnItemFrame(world, 5, 2, k1, 3, random);
		}
		
		for (int k1 = 5; k1 <= 17; k1++)
		{
			for (int i1 = -1; i1 <= 1; i1++)
			{
				setBlockAndMetadata(world, i1, 1, k1, Blocks.wooden_slab, 10);
			}
			
			if ((k1 - 3) % 4 == 0)
			{
				setBlockAndMetadata(world, 0, 2, k1, Blocks.torch, 5);
			}
			
			if ((k1 - 2) % 3 == 0)
			{
				setBlockAndMetadata(world, -1, 1, k1, Blocks.planks, 2);
				setBlockAndMetadata(world, 1, 1, k1, Blocks.planks, 2);
			}
		}
		
		for (int k1 = 5; k1 <= 17; k1++)
		{
			for (int i1 = -1; i1 <= 1; i1++)
			{
				if (getBlock(world, i1, 2, k1).getMaterial() == Material.air)
				{
					placePlate(world, i1, 2, k1, random, LOTRFoods.ROHAN);
					
					if (getBlock(world, i1, 2, k1).getMaterial() == Material.air || (getBlock(world, i1, 2, k1) == LOTRMod.plateBlock && LOTRBlockPlate.getFoodItem(world, i1, 2, k1) == null))
					{
						placePlate(world, i1, 2, k1, random, LOTRFoods.ROHAN);
					}
				}
			}
		}
		
		for (int i1 = -2; i1 <= 2; i1++)
		{
			for (int j1 = 1; j1 <= 5; j1++)
			{
				setBlockAndMetadata(world, i1, j1, 21, floorBlock, floorMeta);
			}
		}
		
		for (int i1 = -4; i1 <= 4; i1++)
		{
			for (int j1 = 1; j1 <= 4; j1++)
			{
				setBlockAndMetadata(world, i1, j1, 22, floorBlock, floorMeta);
			}
		}
		
		for (int i1 = -2; i1 <= 2; i1++)
		{
			setBlockAndMetadata(world, i1, 5, 22, floorBlock, floorMeta);
		}
		
		for (int i1 = -1; i1 <= 1; i1++)
		{
			for (int j1 = 1; j1 <= 2; j1++)
			{
				setBlockAndMetadata(world, i1, j1, 23, floorBlock, floorMeta);
			}
		}
		
		for (int i1 = -1; i1 <= 1; i1++)
		{
			setBlockAndMetadata(world, i1, 1, 21, Blocks.iron_bars, 0);
			setBlockAndMetadata(world, i1, 2, 21, Blocks.iron_bars, 0);
			setBlockAndMetadata(world, i1, 0, 22, LOTRMod.hearth, 0);
			setBlockAndMetadata(world, i1, 1, 22, Blocks.fire, 0);
			setBlockAndMetadata(world, i1, 2, 22, Blocks.air, 0);
		}
		
		setBlockAndMetadata(world, -3, 3, 21, Blocks.torch, 4);
		setBlockAndMetadata(world, 3, 3, 21, Blocks.torch, 4);
		
		setBlockAndMetadata(world, -3, 5, 22, LOTRMod.thatch, 0);
		setBlockAndMetadata(world, -1, 6, 21, LOTRMod.thatch, 0);
		setBlockAndMetadata(world, 1, 6, 21, LOTRMod.thatch, 0);
		setBlockAndMetadata(world, 3, 5, 22, LOTRMod.thatch, 0);
		
		setBlockAndMetadata(world, 0, 3, 0, Blocks.wall_sign, 2);
		TileEntity tileentity = getTileEntity(world, 0, 3, 0);
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[1] = meadHallName[0];
			sign.signText[2] = meadHallName[1];
		}

		LOTREntityRohanMeadhost meadHost = new LOTREntityRohanMeadhost(world);
		spawnNPCAndSetHome(meadHost, world, 0, 3, 11, 4);
		meadHost.setSpecificLocationName(meadHallName[0] + " " + meadHallName[1]);
		
		int men = 3 + random.nextInt(5);
		for (int l = 0; l < men; l++)
		{
			LOTREntityRohirrim man = new LOTREntityRohirrim(world);
			man.spawnRidingHorse = false;
			spawnNPCAndSetHome(man, world, 0, 3, 11, 16);
			if (random.nextInt(3) == 0)
			{
				man.setCurrentItemOrArmor(0, new ItemStack(LOTRMod.mug));
			}
			else
			{
				man.setCurrentItemOrArmor(0, new ItemStack(LOTRMod.mugMead, 1, 1 + random.nextInt(3)));
			}
			man.setCurrentItemOrArmor(4, null);
		}
		
		return true;
	}
	
	private void spawnItemFrame(World world, int i, int j, int k, int direction, Random random)
	{
		ItemStack item = null;
		int l = random.nextInt(6);
		switch (l)
		{
			case 0: case 1: case 2:
				item = new ItemStack(LOTRMod.mug);
				break;
			case 3:
				item = new ItemStack(Items.wooden_sword);
				break;
			case 4:
				item = new ItemStack(Items.bow);
				break;
			case 5:
				item = new ItemStack(Items.arrow);
				break;
		}
		super.spawnItemFrame(world, i, j, k, direction, item);
	}
}
