package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockOre;
import lotr.common.entity.LOTREntities;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.feature.LOTRWorldGenDatePalm;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.gen.feature.WorldGenTallGrass;

public class LOTRWorldGenNearHaradVillage extends LOTRWorldGenStructureBase2
{
	private static int HEIGHT_TOLERANCE = 5;
	private static int BOUNDARY = HEIGHT_TOLERANCE;
	private static int VILLAGE_SIZE = 27;
	private static int VILLAGE_TOTAL_SIZE = VILLAGE_SIZE + BOUNDARY;
	private static int AIR_SET = 16;
	
	public LOTRWorldGenNearHaradVillage(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation)
	{
		j--;
		
		setRotationMode(rotation);
		if (!restrictions && usingPlayer != null)
		{
			switch (getRotationMode())
			{
				case 0:
					k += VILLAGE_SIZE + BOUNDARY + 1;
					break;
				case 1:
					i -= VILLAGE_SIZE + BOUNDARY + 1;
					break;
				case 2:
					k -= VILLAGE_SIZE + BOUNDARY + 1;
					break;
				case 3:
					i += VILLAGE_SIZE + BOUNDARY + 1;
					break;
			}
		}
		
		setOrigin(i, j, k);
	
		if (restrictions)
		{
			int minHeight = getTopBlock(world, 0, 0);
			int maxHeight = getTopBlock(world, 0, 0);
			
			for (int i1 = -VILLAGE_TOTAL_SIZE; i1 <= VILLAGE_TOTAL_SIZE; i1++)
			{
				for (int k1 = -VILLAGE_TOTAL_SIZE; k1 <= VILLAGE_TOTAL_SIZE; k1++)
				{
					int i2 = getX(i1, k1);
					int k2 = getZ(i1, k1);

					int j1 = getTopBlock(world, i1, k1);
					Block block = getBlock(world, i1, j1 - 1, k1);
					
					int j2 = getY(j1 - 1);
					if (block != Blocks.dirt && block != Blocks.grass && block != Blocks.sand && block != Blocks.stone && block != Blocks.clay && block != Blocks.gravel && !(block instanceof BlockOre) && !(block instanceof LOTRBlockOre) && !block.isWood(world, i2, j2, k2) && !block.isLeaves(world, i2, j2, k2) && block != Blocks.pumpkin)
					{
						return false;
					}
					
					int i3 = Math.abs(i1);
					int k3 = Math.abs(k1);
					if (i3 == VILLAGE_TOTAL_SIZE || k3 == VILLAGE_TOTAL_SIZE)
					{
						if (j1 < minHeight)
						{
							minHeight = j1;
						}
						if (j1 > maxHeight)
						{
							maxHeight = j1;
						}
					}
					else if (j1 - originY > AIR_SET)
					{
						maxHeight = j1;
					}
				}
			}
			
			if (maxHeight - minHeight > HEIGHT_TOLERANCE)
			{
				return false;
			}
		}
		
		for (int i1 = -VILLAGE_TOTAL_SIZE; i1 <= VILLAGE_TOTAL_SIZE; i1++)
		{
			for (int k1 = -VILLAGE_TOTAL_SIZE; k1 <= VILLAGE_TOTAL_SIZE; k1++)
			{
				setBlockAndMetadata(world, i1, 0, k1, Blocks.grass, 0);
				
				for (int j1 = 1; j1 <= AIR_SET; j1++)
				{
					setAir(world, i1, j1, k1);
				}
				
				for (int j1 = -1; !isOpaque(world, i1, j1, k1) && getY(j1) >= 0; j1--)
				{
					setBlockAndMetadata(world, i1, j1, k1, Blocks.dirt, 0);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
			}
		}
		
		for (int i1 = -VILLAGE_TOTAL_SIZE; i1 <= VILLAGE_TOTAL_SIZE; i1++)
		{
			for (int k1 = -VILLAGE_TOTAL_SIZE; k1 <= VILLAGE_TOTAL_SIZE; k1++)
			{
				if (i1 == -VILLAGE_TOTAL_SIZE)
				{
					terraformGround(world, i1, k1, -1, 0);
				}
				
				if (i1 == VILLAGE_TOTAL_SIZE)
				{
					terraformGround(world, i1, k1, 1, 0);
				}
				
				if (k1 == -VILLAGE_TOTAL_SIZE)
				{
					terraformGround(world, i1, k1, 0, -1);
				}
				
				if (k1 == VILLAGE_TOTAL_SIZE)
				{
					terraformGround(world, i1, k1, 0, 1);
				}
			}
		}
		
		for (int i1 = -3; i1 <= 3; i1++)
		{
			for (int k1 = -3; k1 <= 3; k1++)
			{
				setBlockAndMetadata(world, i1, -1, k1, Blocks.sandstone, 2);
				setBlockAndMetadata(world, i1, 0, k1, Blocks.sandstone, 2);
			}
		}
		
		for (int i1 = -1; i1 <= 1; i1++)
		{
			for (int k1 = -1; k1 <= 1; k1++)
			{
				setBlockAndMetadata(world, i1, 0, k1, Blocks.water, 0);
			}
		}
		
		for (int j1 = 1; j1 <= 4; j1++)
		{
			setBlockAndMetadata(world, -2, j1, -2, Blocks.sandstone, 2);
			setBlockAndMetadata(world, -2, j1, 2, Blocks.sandstone, 2);
			setBlockAndMetadata(world, 2, j1, -2, Blocks.sandstone, 2);
			setBlockAndMetadata(world, 2, j1, 2, Blocks.sandstone, 2);
		}
		
		for (int i1 = -2; i1 <= 2; i1++)
		{
			for (int k1 = -2; k1 <= 2; k1++)
			{
				setBlockAndMetadata(world, i1, 5, k1, LOTRMod.brick, 15);
				
				if (Math.abs(i1) == 2 || Math.abs(k1) == 2)
				{
					setBlockAndMetadata(world, i1, 6, k1, LOTRMod.wall, 15);
				}
			}
		}
		
		for (int i1 = -2; i1 <= 2; i1 += 4)
		{
			setBlockAndMetadata(world, i1, 4, -1, LOTRMod.stairsNearHaradBrick, 7);
			setBlockAndMetadata(world, i1, 4, 0, LOTRMod.slabSingle4, 8);
			setBlockAndMetadata(world, i1, 4, 1, LOTRMod.stairsNearHaradBrick, 6);
		}
		
		for (int k1 = -2; k1 <= 2; k1 += 4)
		{
			setBlockAndMetadata(world, -1, 4, k1, LOTRMod.stairsNearHaradBrick, 4);
			setBlockAndMetadata(world, 0, 4, k1, LOTRMod.slabSingle4, 8);
			setBlockAndMetadata(world, 1, 4, k1, LOTRMod.stairsNearHaradBrick, 5);
		}
		
		for (int i1 = -1; i1 <= 1; i1++)
		{
			setBlockAndMetadata(world, i1, 5, -1, LOTRMod.stairsNearHaradBrick, 7);
			setBlockAndMetadata(world, i1, 5, 1, LOTRMod.stairsNearHaradBrick, 6);
		}
		
		setBlockAndMetadata(world, -1, 5, 0, LOTRMod.stairsNearHaradBrick, 4);
		setBlockAndMetadata(world, 1, 5, 0, LOTRMod.stairsNearHaradBrick, 5);
		
		setBlockAndMetadata(world, 0, 5, 0, LOTRMod.slabSingle4, 8);
		
		setBlockAndMetadata(world, 0, 5, 0, Blocks.dirt, 0);
		
		for (int l = 0; l < 20; l++)
		{
			if (new LOTRWorldGenDatePalm(notifyChanges).generate(world, random, getX(0, 0), getY(6), getZ(0, 0)))
			{
				break;
			}
		}
		
		setBlockAndMetadata(world, 0, 5, 0, LOTRMod.slabSingle4, 8);
		
		placeWallBanner(world, 0, 5, -2, LOTRFaction.NEAR_HARAD, 2);
		placeWallBanner(world, 2, 5, 0, LOTRFaction.NEAR_HARAD, 1);
		placeWallBanner(world, 0, 5, 2, LOTRFaction.NEAR_HARAD, 0);
		placeWallBanner(world, -2, 5, 0, LOTRFaction.NEAR_HARAD, 3);
		
		setBlockAndMetadata(world, -2, 2, -3, Blocks.torch, 4);
		setBlockAndMetadata(world, 2, 2, -3, Blocks.torch, 4);
		
		setBlockAndMetadata(world, -2, 2, 3, Blocks.torch, 3);
		setBlockAndMetadata(world, 2, 2, 3, Blocks.torch, 3);
		
		setBlockAndMetadata(world, -3, 2, -2, Blocks.torch, 1);
		setBlockAndMetadata(world, -3, 2, 2, Blocks.torch, 1);
		
		setBlockAndMetadata(world, 3, 2, -2, Blocks.torch, 2);
		setBlockAndMetadata(world, 3, 2, 2, Blocks.torch, 2);
		
		for (int i1 = 4; i1 <= 27; i1++)
		{
			setBlockAndMetadata(world, i1, 0, -1, LOTRMod.brick, 15);
			setBlockAndMetadata(world, i1, 0, 0, Blocks.stained_hardened_clay, 14);
			setBlockAndMetadata(world, i1, 0, 1, LOTRMod.brick, 15);
			
			if (i1 == 13 || i1 == 21)
			{
				getRandomHouseGen(random).generateWithSetRotation(world, random, getX(i1, 2), getY(1), getZ(i1, 2), (getRotationMode() + 0) % 4);
				getRandomHouseGen(random).generateWithSetRotation(world, random, getX(i1, -2), getY(1), getZ(i1, -2), (getRotationMode() + 2) % 4);
			}
		}
		
		for (int k1 = -4; k1 >= -27; k1--)
		{
			setBlockAndMetadata(world, -1, 0, k1, LOTRMod.brick, 15);
			setBlockAndMetadata(world, 0, 0, k1, Blocks.stained_hardened_clay, 4);
			setBlockAndMetadata(world, 1, 0, k1, LOTRMod.brick, 15);
			
			if (k1 == -13 || k1 == -21)
			{
				getRandomHouseGen(random).generateWithSetRotation(world, random, getX(2, k1), getY(1), getZ(2, k1), (getRotationMode() + 1) % 4);
				getRandomHouseGen(random).generateWithSetRotation(world, random, getX(-2, k1), getY(1), getZ(-2, k1), (getRotationMode() + 3) % 4);
			}
		}
		
		for (int i1 = -4; i1 >= -27; i1--)
		{
			setBlockAndMetadata(world, i1, 0, -1, LOTRMod.brick, 15);
			setBlockAndMetadata(world, i1, 0, 0, Blocks.stained_hardened_clay, 13);
			setBlockAndMetadata(world, i1, 0, 1, LOTRMod.brick, 15);
			
			if (i1 == -13 || i1 == -21)
			{
				getRandomHouseGen(random).generateWithSetRotation(world, random, getX(i1, 2), getY(1), getZ(i1, 2), (getRotationMode() + 0) % 4);
				getRandomHouseGen(random).generateWithSetRotation(world, random, getX(i1, -2), getY(1), getZ(i1, -2), (getRotationMode() + 2) % 4);
			}
		}
		
		for (int k1 = 4; k1 <= 27; k1++)
		{
			setBlockAndMetadata(world, -1, 0, k1, LOTRMod.brick, 15);
			setBlockAndMetadata(world, 0, 0, k1, Blocks.stained_hardened_clay, 3);
			setBlockAndMetadata(world, 1, 0, k1, LOTRMod.brick, 15);
			
			if (k1 == 13 || k1 == 21)
			{
				getRandomHouseGen(random).generateWithSetRotation(world, random, getX(2, k1), getY(1), getZ(2, k1), (getRotationMode() + 1) % 4);
				getRandomHouseGen(random).generateWithSetRotation(world, random, getX(-2, k1), getY(1), getZ(-2, k1), (getRotationMode() + 3) % 4);
			}
		}
		
		for (int i1 = -8; i1 <= -3; i1++)
		{
			setBlockAndMetadata(world, i1, 1, -8, LOTRMod.fence, 14);
		}
		
		for (int k1 = -7; k1 <= -3; k1++)
		{
			setBlockAndMetadata(world, -8, 1, k1, LOTRMod.fence, 14);
		}
		
		setBlockAndMetadata(world, -7, 1, -3, Blocks.fence_gate, 0);
		setBlockAndMetadata(world, -6, 1, -3, Blocks.fence_gate, 0);
		setBlockAndMetadata(world, -5, 1, -3, LOTRMod.fence, 14);
		setBlockAndMetadata(world, -5, 1, -4, LOTRMod.fence, 14);
		setBlockAndMetadata(world, -5, 1, -5, LOTRMod.fence, 14);
		setBlockAndMetadata(world, -4, 1, -5, LOTRMod.fence, 14);
		setBlockAndMetadata(world, -3, 1, -5, LOTRMod.fence, 14);
		setBlockAndMetadata(world, -3, 1, -6, Blocks.fence_gate, 1);
		setBlockAndMetadata(world, -3, 1, -7, Blocks.fence_gate, 1);
		
		int animals = 1 + random.nextInt(3);
		for (int l = 0; l < animals; l++)
		{
			spawnNPCAndSetHome(getRandomHaradAnimal(world, random), world, -6, 1, -6, 16);
		}
		
		for (int i1 = 3; i1 <= 8; i1++)
		{
			setBlockAndMetadata(world, i1, 1, -8, LOTRMod.fence, 14);
		}
		
		for (int k1 = -7; k1 <= -3; k1++)
		{
			setBlockAndMetadata(world, 8, 1, k1, LOTRMod.fence, 14);
		}
		
		setBlockAndMetadata(world, 7, 1, -3, Blocks.fence_gate, 0);
		setBlockAndMetadata(world, 6, 1, -3, Blocks.fence_gate, 0);
		setBlockAndMetadata(world, 5, 1, -3, LOTRMod.fence, 14);
		setBlockAndMetadata(world, 5, 1, -4, LOTRMod.fence, 14);
		setBlockAndMetadata(world, 5, 1, -5, LOTRMod.fence, 14);
		setBlockAndMetadata(world, 4, 1, -5, LOTRMod.fence, 14);
		setBlockAndMetadata(world, 3, 1, -5, LOTRMod.fence, 14);
		setBlockAndMetadata(world, 3, 1, -6, Blocks.fence_gate, 1);
		setBlockAndMetadata(world, 3, 1, -7, Blocks.fence_gate, 1);
		
		animals = 1 + random.nextInt(3);
		for (int l = 0; l < animals; l++)
		{
			spawnNPCAndSetHome(getRandomHaradAnimal(world, random), world, 6, 1, -6, 16);
		}
		
		for (int i1 = -8; i1 <= -3; i1++)
		{
			setBlockAndMetadata(world, i1, 1, 8, Blocks.sandstone, 2);
		}
		
		for (int k1 = 3; k1 <= 7; k1++)
		{
			setBlockAndMetadata(world, -8, 1, k1, Blocks.sandstone, 2);
		}
		
		setBlockAndMetadata(world, -7, 1, 3, Blocks.sandstone, 2);
		setBlockAndMetadata(world, -6, 1, 3, Blocks.sandstone, 2);
		setBlockAndMetadata(world, -5, 1, 3, Blocks.sandstone, 2);
		setBlockAndMetadata(world, -5, 1, 4, Blocks.sandstone, 2);
		setBlockAndMetadata(world, -5, 1, 5, Blocks.sandstone, 2);
		setBlockAndMetadata(world, -4, 1, 5, Blocks.sandstone, 2);
		setBlockAndMetadata(world, -3, 1, 5, Blocks.sandstone, 2);
		setBlockAndMetadata(world, -3, 1, 6, Blocks.sandstone, 2);
		setBlockAndMetadata(world, -3, 1, 7, Blocks.sandstone, 2);
		
		Block crop = getRandomCropBlock(random);
		
		for (int k1 = 4; k1 <= 7; k1++)
		{
			for (int i1 = -7; i1 <= -6; i1++)
			{
				setBlockAndMetadata(world, i1, 1, k1, Blocks.farmland, 7);
				setBlockAndMetadata(world, i1, 2, k1, crop, 7);
			}
		}
		
		for (int k1 = 6; k1 <= 7; k1++)
		{
			for (int i1 = -5; i1 <= -4; i1++)
			{
				setBlockAndMetadata(world, i1, 1, k1, Blocks.farmland, 7);
				setBlockAndMetadata(world, i1, 2, k1, crop, 7);
			}
		}

		setBlockAndMetadata(world, -7, 2, 7, LOTRMod.slabSingle4, 0);
		setBlockAndMetadata(world, -7, 1, 7, Blocks.water, 0);
		setBlockAndMetadata(world, -8, 2, 7, LOTRMod.slabSingle4, 0);
		setBlockAndMetadata(world, -8, 2, 8, LOTRMod.slabSingle4, 0);
		setBlockAndMetadata(world, -7, 2, 8, LOTRMod.slabSingle4, 0);

		for (int i1 = 3; i1 <= 8; i1++)
		{
			setBlockAndMetadata(world, i1, 1, 8, Blocks.sandstone, 2);
		}
		
		for (int k1 = 3; k1 <= 7; k1++)
		{
			setBlockAndMetadata(world, 8, 1, k1, Blocks.sandstone, 2);
		}
		
		setBlockAndMetadata(world, 7, 1, 3, Blocks.sandstone, 2);
		setBlockAndMetadata(world, 6, 1, 3, Blocks.sandstone, 2);
		setBlockAndMetadata(world, 5, 1, 3, Blocks.sandstone, 2);
		setBlockAndMetadata(world, 5, 1, 4, Blocks.sandstone, 2);
		setBlockAndMetadata(world, 5, 1, 5, Blocks.sandstone, 2);
		setBlockAndMetadata(world, 4, 1, 5, Blocks.sandstone, 2);
		setBlockAndMetadata(world, 3, 1, 5, Blocks.sandstone, 2);
		setBlockAndMetadata(world, 3, 1, 6, Blocks.sandstone, 2);
		setBlockAndMetadata(world, 3, 1, 7, Blocks.sandstone, 2);
		
		crop = getRandomCropBlock(random);
		
		for (int k1 = 4; k1 <= 7; k1++)
		{
			for (int i1 = 6; i1 <= 7; i1++)
			{
				setBlockAndMetadata(world, i1, 1, k1, Blocks.farmland, 7);
				setBlockAndMetadata(world, i1, 2, k1, crop, 7);
			}
		}
		
		for (int k1 = 6; k1 <= 7; k1++)
		{
			for (int i1 = 4; i1 <= 5; i1++)
			{
				setBlockAndMetadata(world, i1, 1, k1, Blocks.farmland, 7);
				setBlockAndMetadata(world, i1, 2, k1, crop, 7);
			}
		}
		
		setBlockAndMetadata(world, 7, 2, 7, LOTRMod.slabSingle4, 0);
		setBlockAndMetadata(world, 7, 1, 7, Blocks.water, 0);
		setBlockAndMetadata(world, 8, 2, 7, LOTRMod.slabSingle4, 0);
		setBlockAndMetadata(world, 8, 2, 8, LOTRMod.slabSingle4, 0);
		setBlockAndMetadata(world, 7, 2, 8, LOTRMod.slabSingle4, 0);
		
		getRandomCornerStructure(random).generateWithSetRotation(world, random, getX(-18, -25), getY(1), getZ(-18, -25), (getRotationMode() + 0) % 4);
		getRandomCornerStructure(random).generateWithSetRotation(world, random, getX(18, -25), getY(1), getZ(18, -25), (getRotationMode() + 0) % 4);
		getRandomCornerStructure(random).generateWithSetRotation(world, random, getX(-18, 25), getY(1), getZ(-18, 25), (getRotationMode() + 2) % 4);
		getRandomCornerStructure(random).generateWithSetRotation(world, random, getX(18, 25), getY(1), getZ(18, 25), (getRotationMode() + 2) % 4);
		
		return true;
	}
	
	private void terraformGround(World world, int i, int k, int xDirection, int zDirection)
	{
		int height = getTopBlock(world, i + xDirection, k + zDirection) - 1;
		int actualHeight = getY(height);
		
		if (actualHeight > originY)
		{
			int difference = actualHeight - originY;
			difference = Math.min(difference, HEIGHT_TOLERANCE);
			for (int l = 0; l < HEIGHT_TOLERANCE; l++)
			{
				int maxElevation = Math.min(l + 1, difference);
				int i1 = i - xDirection * (BOUNDARY - 1 - l);
				int k1 = k - zDirection * (BOUNDARY - 1 - l);
				for (int j1 = 1; j1 <= maxElevation; j1++)
				{
					if (j1 == maxElevation)
					{
						setBlockAndMetadata(world, i1, j1, k1, Blocks.grass, 0);
					}
					else
					{
						setBlockAndMetadata(world, i1, j1, k1, Blocks.dirt, 0);
					}
				}
				setGrassToDirt(world, i1, 0, k1);
			}
		}
		
		if (actualHeight < originY)
		{
			int difference = Math.abs(originY - actualHeight);
			difference = Math.min(difference, HEIGHT_TOLERANCE);
			for (int l = 0; l < HEIGHT_TOLERANCE; l++)
			{
				int minElevation = Math.max(-l - 1, -difference);
				int i1 = i - xDirection * (BOUNDARY - 1 - l);
				int k1 = k - zDirection * (BOUNDARY - 1 - l);
				for (int j1 = 0; j1 >= minElevation; j1--)
				{
					if (j1 == minElevation && isOpaque(world, i1, j1, k1))
					{
						setBlockAndMetadata(world, i1, j1, k1, Blocks.grass, 0);
					}
					else
					{
						setAir(world, i1, j1, k1);
					}
				}
			}
		}
	}
	
	private LOTRWorldGenStructureBase2 getRandomHouseGen(Random random)
	{
		LOTRWorldGenStructureBase2 houseGen = new LOTRWorldGenNearHaradHouse(notifyChanges);
		houseGen.restrictions = false;
		houseGen.usingPlayer = usingPlayer;
		return houseGen;
	}
	
	private LOTRWorldGenStructureBase2 getRandomCornerStructure(Random random)
	{
		LOTRWorldGenStructureBase2 structure = null;
		if (random.nextBoolean())
		{
			if (random.nextBoolean())
			{
				structure = new LOTRWorldGenNearHaradTower(notifyChanges);
			}
			else
			{
				structure = new LOTRWorldGenNearHaradBazaar(notifyChanges);
			}
		}
		else
		{
			structure = new LOTRWorldGenNearHaradLargeHouse(notifyChanges);
		}
		structure.restrictions = false;
		structure.usingPlayer = usingPlayer;
		return structure;
	}
	
	private EntityCreature getRandomHaradAnimal(World world, Random random)
	{
		Class animalClass = ((SpawnListEntry)WeightedRandom.getRandomItem(random, LOTRBiome.nearHaradFertile.getSpawnableList(EnumCreatureType.creature))).entityClass;
		EntityCreature animal = (EntityCreature)LOTREntities.createEntityByClass(animalClass, world);
		return animal;
	}
	
	private Block getRandomCropBlock(Random random)
	{
		int l = random.nextInt(4);
		if (l == 0)
		{
			return Blocks.wheat;
		}
		if (l == 1)
		{
			return Blocks.carrots;
		}
		if (l == 2)
		{
			return Blocks.potatoes;
		}
		else
		{
			return LOTRMod.lettuceCrop;
		}
	}
}