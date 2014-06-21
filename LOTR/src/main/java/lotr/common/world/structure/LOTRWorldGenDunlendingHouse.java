package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityDunlending;
import lotr.common.world.biome.LOTRBiomeGenDunland;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class LOTRWorldGenDunlendingHouse extends LOTRWorldGenStructureBase
{
	private Block plankBlock;
	private int plankMeta;
	
	private Block woodBlock;
	private int woodMeta;
	
	private Block slabBlock;
	private int slabMeta;
	
	private Block stairBlock;
	
	private Block floorBlock;
	private int floorMeta;
	
	public LOTRWorldGenDunlendingHouse(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass || !(world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenDunland))
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
		
		int originX = i;
		int originZ = k;
		int xRange = 0;
		int zRange = 0;
		switch (rotation)
		{
			case 0:
				k += 7;
				xRange = 4;
				zRange = 6;
				break;
			case 1:
				i -= 7;
				xRange = 6;
				zRange = 4;
				break;
			case 2:
				k -= 7;
				xRange = 4;
				zRange = 6;
				break;
			case 3:
				i += 7;
				xRange = 6;
				zRange = 4;
				break;
		}
		
		if (restrictions)
		{
			for (int i1 = i - xRange; i1 <= i + xRange; i1++)
			{
				for (int k1 = k - zRange; k1 <= k + zRange; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					if (Math.abs(j1 - j) > 5)
					{
						return false;
					}
					Block l = world.getBlock(i1, j1, k1);
					if (l == Blocks.grass)
					{
						continue;
					}
					return false;
				}
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1D, j + 1D, k + 1D).offset(0D, 4D, 0D).expand(xRange, 4D, zRange));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		
			for (int i1 = i - xRange; i1 <= i + xRange; i1++)
			{
				for (int k1 = k - zRange; k1 <= k + zRange; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					if (Math.abs(j1 - j) > 5)
					{
						return false;
					}
					Block l = world.getBlock(i1, j1, k1);
					if (l != Blocks.grass)
					{
						return false;
					}
				}
			}
		}
		
		if (random.nextBoolean())
		{
			plankBlock = Blocks.planks;
			plankMeta = 0;
			woodBlock = Blocks.log;
			woodMeta = 0;
			slabBlock = Blocks.wooden_slab;
			slabMeta = 0;
			stairBlock = Blocks.oak_stairs;
		}
		else
		{
			plankBlock = Blocks.planks;
			plankMeta = 1;
			woodBlock = Blocks.log;
			woodMeta = 1;
			slabBlock = Blocks.wooden_slab;
			slabMeta = 1;
			stairBlock = Blocks.spruce_stairs;
		}
		
		Object[] obj = getRandomDunlandFloorBlock(random);
		floorBlock = (Block)obj[0];
		floorMeta = (Integer)obj[1];

		for (int i1 = i - xRange; i1 <= i + xRange; i1++)
		{
			for (int k1 = k - zRange; k1 <= k + zRange; k1++)
			{
				for (int j1 = j + 5; (j1 >= j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
				{
					if (Math.abs(i1 - i) == xRange && Math.abs(k1 - k) == zRange)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta);
					}
					else if (Math.abs(i1 - i) == xRange || Math.abs(k1 - k) == zRange)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, plankBlock, plankMeta);
					}
					else
					{
						if (j1 > j)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
						}
						else if (j1 == j)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, floorBlock, floorMeta);
						}
						else
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.dirt, 0);
						}
					}
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		for (int i1 = i - xRange + 1; i1 <= i + xRange - 1; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 4, k - zRange, woodBlock, woodMeta | 4);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + zRange, woodBlock, woodMeta | 4);
		}
		
		for (int k1 = k - zRange + 1; k1 <= k + zRange - 1; k1++)
		{
			setBlockAndNotifyAdequately(world, i - xRange, j + 4, k1, woodBlock, woodMeta | 8);
			setBlockAndNotifyAdequately(world, i + xRange, j + 4, k1, woodBlock, woodMeta | 8);
		}
		
		for (int j1 = j + 1; j1 <= j + 5; j1++)
		{
			setBlockAndNotifyAdequately(world, i - xRange + 1, j1, k - zRange + 1, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + xRange - 1, j1, k - zRange + 1, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i - xRange + 1, j1, k + zRange - 1, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + xRange - 1, j1, k + zRange - 1, woodBlock, woodMeta);
		}
		
		if (xRange > zRange)
		{
			for (int i1 = i - xRange - 1; i1 <= i + xRange + 1; i1++)
			{
				for (int stair = 0; stair < 3; stair++)
				{
					setBlockAndNotifyAdequately(world, i1, j + 4 + stair, k - zRange - 1 + stair, stairBlock, 2);
					setBlockAndNotifyAdequately(world, i1, j + 4 + stair, k + zRange + 1 - stair, stairBlock, 3);
				}
			}
			
			for (int k1 = k - zRange + 2; k1 <= k + zRange - 2; k1++)
			{
				setBlockAndNotifyAdequately(world, i - xRange - 1, j + 6, k1, slabBlock, slabMeta | 8);
				setBlockAndNotifyAdequately(world, i + xRange + 1, j + 6, k1, slabBlock, slabMeta | 8);
			}
			
			zRange -= 2;
		}
		else
		{
			for (int k1 = k - zRange - 1; k1 <= k + zRange + 1; k1++)
			{
				for (int stair = 0; stair < 3; stair++)
				{
					setBlockAndNotifyAdequately(world, i - xRange - 1 + stair, j + 4 + stair, k1, stairBlock, 0);
					setBlockAndNotifyAdequately(world, i + xRange + 1 - stair, j + 4 + stair, k1, stairBlock, 1);
				}
			}
			
			for (int i1 = i - xRange + 2; i1 <= i + xRange - 2; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 6, k - zRange - 1, slabBlock, slabMeta | 8);
				setBlockAndNotifyAdequately(world, i1, j + 6, k + zRange + 1, slabBlock, slabMeta | 8);
			}
			
			xRange -= 2;
		}
		
		for (int i1 = i - xRange; i1 <= i + xRange; i1++)
		{
			for (int k1 = k - zRange; k1 <= k + zRange; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 6, k1, plankBlock, plankMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j - 2, k, LOTRMod.hearth, 0);
		setBlockAndNotifyAdequately(world, i, j - 1, k, Blocks.fire, 0);
		setBlockAndNotifyAdequately(world, i - 1, j - 1, k, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i + 1, j - 1, k, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i, j - 1, k - 1, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i, j - 1, k + 1, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i, j, k, Blocks.iron_bars, 0);
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				for (int j1 = j + 6; j1 <= j + 8; j1++)
				{
					if (i1 == i && k1 == k)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, plankBlock, plankMeta);
					}
				}
			}
		}
		
		switch (rotation)
		{
			case 0:
				generateFacingSouth(world, random, i, j, k);
				break;
			case 1:
				generateFacingWest(world, random, i, j, k);
				break;
			case 2:
				generateFacingNorth(world, random, i, j, k);
				break;
			case 3:
				generateFacingEast(world, random, i, j, k);
				break;
		}
		
		LOTREntityDunlending dunlending = new LOTREntityDunlending(world);
		dunlending.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0F, 0F);
		dunlending.onSpawnWithEgg(null);
		dunlending.setHomeArea(i, j, k, 12);
		dunlending.isNPCPersistent = true;
		world.spawnEntityInWorld(dunlending);
		
		return true;
	}
	
	private void generateFacingSouth(World world, Random random, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i, j, k - 6, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 6, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 6, Blocks.wooden_door, 8);
		
		for (int k1 = k - 2; k1 <= k + 2; k1 += 2)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 2, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + 2, k1, Blocks.air, 0);
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k1, slabBlock, slabMeta | 8);
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, slabBlock, slabMeta | 8);
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 5, slabBlock, slabMeta | 8);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 2, Blocks.furnace, 0);
		setBlockMetadata(world, i - 3, j + 1, k - 2, 5);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k, Blocks.chest, 5);
		LOTRChestContents.fillChest(world, random, i - 3, j + 1, k, LOTRChestContents.DUNLENDING_HOUSE);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 2, plankBlock, plankMeta);
		placeFlowerPot(world, i - 3, j + 2, k + 2, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 2, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k, LOTRMod.dunlendingTable, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 2, plankBlock, plankMeta);
		placeFlowerPot(world, i + 3, j + 2, k + 2, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i, j + 1, k + 4, Blocks.bed, 0);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 5, Blocks.bed, 8);
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 5, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k + 5, plankBlock, plankMeta);
		
		placeItemFrame(world, i, j + 3, k + 6, 2, random);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 5, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 5, Blocks.torch, 3);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 4, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 4, Blocks.torch, 1);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 5, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 5, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k + 5, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 4, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 4, Blocks.torch, 2);
	}
	
	private void generateFacingWest(World world, Random random, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i + 6, j, k, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k, Blocks.wooden_door, 2);
		setBlockAndNotifyAdequately(world, i + 6, j + 2, k, Blocks.wooden_door, 8);
		
		for (int i1 = i - 2; i1 <= i + 2; i1 += 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 4, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 4, Blocks.air, 0);
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 3, slabBlock, slabMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, slabBlock, slabMeta | 8);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j + 1, k1, slabBlock, slabMeta | 8);
		}
		
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 3, Blocks.furnace, 0);
		setBlockMetadata(world, i + 2, j + 1, k - 3, 3);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 3, Blocks.chest, 3);
		LOTRChestContents.fillChest(world, random, i, j + 1, k - 3, LOTRChestContents.DUNLENDING_HOUSE);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 3, plankBlock, plankMeta);
		placeFlowerPot(world, i - 2, j + 2, k - 3, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k + 3, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 3, LOTRMod.dunlendingTable, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 3, plankBlock, plankMeta);
		placeFlowerPot(world, i - 2, j + 2, k + 3, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k, Blocks.bed, 1);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k, Blocks.bed, 9);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 1, plankBlock, plankMeta);
		
		placeItemFrame(world, i - 6, j + 3, k, 3, random);
		
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 2, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 3, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 3, Blocks.torch, 3);
		
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 2, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 2, Blocks.torch, 1);
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 3, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 3, Blocks.torch, 4);
	}
	
	private void generateFacingNorth(World world, Random random, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i, j, k + 6, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 6, Blocks.wooden_door, 3);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 6, Blocks.wooden_door, 8);
		
		for (int k1 = k - 2; k1 <= k + 2; k1 += 2)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 2, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + 2, k1, Blocks.air, 0);
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k1, slabBlock, slabMeta | 8);
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, slabBlock, slabMeta | 8);
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 5, slabBlock, slabMeta | 8);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 2, Blocks.furnace, 0);
		setBlockMetadata(world, i - 3, j + 1, k + 2, 5);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k, Blocks.chest, 5);
		LOTRChestContents.fillChest(world, random, i - 3, j + 1, k, LOTRChestContents.DUNLENDING_HOUSE);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 2, plankBlock, plankMeta);
		placeFlowerPot(world, i - 3, j + 2, k - 2, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 2, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k, LOTRMod.dunlendingTable, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 2, plankBlock, plankMeta);
		placeFlowerPot(world, i + 3, j + 2, k - 2, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i, j + 1, k - 4, Blocks.bed, 2);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 5, Blocks.bed, 10);
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 5, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 5, plankBlock, plankMeta);
		
		placeItemFrame(world, i, j + 3, k - 6, 0, random);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 5, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k + 5, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 4, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 4, Blocks.torch, 1);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 5, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 5, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 5, Blocks.torch, 3);
		
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 4, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 4, Blocks.torch, 2);
	}
	
	private void generateFacingEast(World world, Random random, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i - 6, j, k, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k, Blocks.wooden_door, 8);
		
		for (int i1 = i - 2; i1 <= i + 2; i1 += 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 4, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 4, Blocks.air, 0);
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 3, slabBlock, slabMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, slabBlock, slabMeta | 8);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, slabBlock, slabMeta | 8);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 3, Blocks.furnace, 0);
		setBlockMetadata(world, i - 2, j + 1, k - 3, 3);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 3, Blocks.chest, 3);
		LOTRChestContents.fillChest(world, random, i, j + 1, k - 3, LOTRChestContents.DUNLENDING_HOUSE);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 3, plankBlock, plankMeta);
		placeFlowerPot(world, i + 2, j + 2, k - 3, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 3, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 3, LOTRMod.dunlendingTable, 0);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k + 3, plankBlock, plankMeta);
		placeFlowerPot(world, i + 2, j + 2, k + 3, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k, Blocks.bed, 3);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k, Blocks.bed, 11);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 1, plankBlock, plankMeta);
		
		placeItemFrame(world, i + 6, j + 3, k, 1, random);
		
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 2, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 2, Blocks.torch, 1);
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 3, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 3, Blocks.torch, 3);
		
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 2, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 3, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 3, Blocks.torch, 4);
	}
	
	private ItemStack getRandomPlant(Random random)
	{
		return random.nextBoolean() ? new ItemStack(Blocks.red_flower) : new ItemStack(Blocks.yellow_flower);
	}
	
	private void placeItemFrame(World world, int i, int j, int k, int direction, Random random)
	{
		ItemStack item = null;
		int l = random.nextInt(6);
		switch (l)
		{
			case 0:
				item = new ItemStack(Items.bone);
				break;
			case 1:
				item = new ItemStack(Items.iron_sword);
				break;
			case 2:
				item = new ItemStack(Items.bow);
				break;
			case 3:
				item = new ItemStack(Items.arrow);
				break;
			case 4:
				item = new ItemStack(LOTRMod.mug);
				break;
			case 5:
				item = new ItemStack(Items.flint);
				break;
		}
		spawnItemFrame(world, i, j, k, direction, item);
	}
	
	public static Object[] getRandomDunlandFloorBlock(Random random)
	{
		Object[] block = new Object[2];
		int randomFloor = random.nextInt(6);
		switch (randomFloor)
		{
			case 0:
				block[0] = Blocks.cobblestone;
				block[1] = 0;
				break;
			case 1:
				block[0] = Blocks.stonebrick;
				block[1] = 0;
				break;
			case 2:
				block[0] = Blocks.hardened_clay;
				block[1] = 0;
				break;
			case 3:
				block[0] = Blocks.stained_hardened_clay;
				block[1] = 7;
				break;
			case 4:
				block[0] = Blocks.stained_hardened_clay;
				block[1] = 12;
				break;
			case 5:
				block[0] = Blocks.stained_hardened_clay;
				block[1] = 15;
				break;
		}
		
		return block;
	}
}
