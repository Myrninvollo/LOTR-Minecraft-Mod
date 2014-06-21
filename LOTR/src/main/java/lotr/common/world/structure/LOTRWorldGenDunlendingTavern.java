package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityDunlending;
import lotr.common.entity.npc.LOTREntityDunlendingBartender;
import lotr.common.entity.npc.LOTREntityDunlendingDrunkard;
import lotr.common.entity.npc.LOTRNames;
import lotr.common.world.biome.LOTRBiomeGenDunland;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class LOTRWorldGenDunlendingTavern extends LOTRWorldGenStructureBase
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
	
	private Item drink;
	
	public LOTRWorldGenDunlendingTavern(boolean flag)
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
		
		int xRange = 0;
		int zRange = 0;
		switch (rotation)
		{
			case 0:
				k += 11;
				xRange = 6;
				zRange = 10;
				break;
			case 1:
				i -= 11;
				xRange = 10;
				zRange = 6;
				break;
			case 2:
				k -= 11;
				xRange = 6;
				zRange = 10;
				break;
			case 3:
				i += 11;
				xRange = 10;
				zRange = 6;
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
		
		Object[] obj = LOTRWorldGenDunlendingHouse.getRandomDunlandFloorBlock(random);
		floorBlock = (Block)obj[0];
		floorMeta = (Integer)obj[1];
		
		drink = LOTRFoods.DUNLENDING_DRINK.getRandomFood(random).getItem();
		
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
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.hearth, 0);

				for (int j1 = j + 1; j1 <= j + 5; j1++)
				{
					if (Math.abs(i1 - i) == 1 && Math.abs(k1 - k) == 1)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, floorBlock, floorMeta);
					}
					else if (i1 != i || k1 != k)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.iron_bars, 0);
					}
				}
				
				for (int j1 = j + 6; j1 <= j + 9; j1++)
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
		
		setBlockAndNotifyAdequately(world, i, j + 1, k, Blocks.fire, 0);
		
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
		
		return true;
	}
	
	private void generateFacingSouth(World world, Random random, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i, j, k - 10, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 10, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 10, Blocks.wooden_door, 8);
		
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k - 11, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k - 11, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i, j + 3, k - 11, Blocks.wall_sign, 2);
		String[] name = LOTRNames.getRandomDunlendingTavernName(random);
		TileEntity tileentity = world.getTileEntity(i, j + 3, k - 11);
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[1] = name[0];
			sign.signText[2] = name[1];
		}
		
		for (int i1 = i - 4; i1 <= i + 2; i1 += 6)
		{
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 10, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 10, stairBlock, 5);
			setBlockAndNotifyAdequately(world, i1 + 2, j + 2, k - 10, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1 + 2, j + 3, k - 10, stairBlock, 4);
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k - 9, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k - 9, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 9, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 9, Blocks.torch, 2);
		
		for (int k1 = k - 8; k1 <= k - 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j + 1, k1, stairBlock, 1);
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, stairBlock, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 5; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 3, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 3, woodBlock, woodMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 3, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 3, Blocks.torch, 2);
		
		for (int k1 = 0; k1 <= 2; k1++)
		{
			for (int i1 = 0; i1 <= 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i - 3 + i1, j + 1, k - 7 + k1, Blocks.planks, 0);
				placePlate(world, i - 3 + i1, j + 2, k - 7 + k1, random, LOTRFoods.DUNLENDING);
				setBlockAndNotifyAdequately(world, i + 2 + i1, j + 1, k - 7 + k1, Blocks.planks, 0);
				placePlate(world, i + 2 + i1, j + 2, k - 7 + k1, random, LOTRFoods.DUNLENDING);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 3; k1 += 2)
		{
			placeItemFrame(world, i - 6, j + 2, k1, 3, random);
			placeItemFrame(world, i + 6, j + 2, k1, 1, random);
		}
		
		for (int j1 = j + 1; j1 <= j + 5; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 5, woodBlock, woodMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 5, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 5, Blocks.torch, 2);
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 6, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 6, slabBlock, slabMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 6, woodBlock, woodMeta | 4);
			
			if (Math.abs(i1 - i) <= 4)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k + 9, slabBlock, slabMeta | 8);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 6, Blocks.fence_gate, 0);
		
		for (int i1 = i - 3; i1 <= i + 1; i1 += 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 4, Blocks.fence, 0);
		}
		
		for (int i1 = i - 3; i1 <= i + 1; i1++)
		{
			if (random.nextInt(3) != 0)
			{
				placeMug(world, random, i1, j + 2, k + 6, 0, drink);
			}
		}
		
		placeBarrel(world, random, i - 5, j + 1, k + 7, 5, drink);
		placeBarrel(world, random, i - 5, j + 1, k + 8, 5, drink);
		
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 9, Blocks.furnace, 0);
		setBlockMetadata(world, i - 1, j + 1, k + 9, 2);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 9, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k + 9, Blocks.furnace, 0);
		setBlockMetadata(world, i + 1, j + 1, k + 9, 2);
		
		placeFlowerPot(world, i - 3, j + 2, k + 9, getRandomPlant(random));
		placeFlowerPot(world, i + 3, j + 2, k + 9, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 9, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 9, Blocks.torch, 2);
		
		spawnBartender(world, i, j + 1, k + 8, name);
		
		int men = 3 + random.nextInt(6);
		for (int l = 0; l < men; l++)
		{
			spawnDunlending(world, i, j + 1, k + 3, i, j, k);
		}
		
		spawnDrunkard(world, i, j + 1, k + 3, i, j, k);
	}
	
	private void generateFacingWest(World world, Random random, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i + 10, j, k, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i + 10, j + 1, k, Blocks.wooden_door, 2);
		setBlockAndNotifyAdequately(world, i + 10, j + 2, k, Blocks.wooden_door, 8);
		
		setBlockAndNotifyAdequately(world, i + 11, j + 2, k - 1, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 11, j + 2, k + 1, Blocks.torch, 1);
		
		setBlockAndNotifyAdequately(world, i + 11, j + 3, k, Blocks.wall_sign, 5);
		String[] name = LOTRNames.getRandomDunlendingTavernName(random);
		TileEntity tileentity = world.getTileEntity(i + 11, j + 3, k);
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[1] = name[0];
			sign.signText[2] = name[1];
		}
		
		for (int k1 = k - 4; k1 <= k + 2; k1 += 6)
		{
			setBlockAndNotifyAdequately(world, i + 10, j + 2, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i + 10, j + 3, k1, stairBlock, 7);
			setBlockAndNotifyAdequately(world, i + 10, j + 2, k1 + 2, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i + 10, j + 3, k1 + 2, stairBlock, 6);
		}
		
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k - 1, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k + 1, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k + 4, Blocks.torch, 4);
		
		for (int i1 = i + 8; i1 >= i + 4; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 5, stairBlock, 3);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 5, stairBlock, 2);
		}
		
		for (int j1 = j + 1; j1 <= j + 5; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 3, j1, k - 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 3, j1, k + 5, woodBlock, woodMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 4, Blocks.torch, 4);
		
		for (int i1 = 0; i1 <= 2; i1++)
		{
			for (int k1 = 0; k1 <= 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 7 - i1, j + 1, k - 3 + k1, Blocks.planks, 0);
				placePlate(world, i + 7 - i1, j + 2, k - 3 + k1, random, LOTRFoods.DUNLENDING);
				setBlockAndNotifyAdequately(world, i + 7 - i1, j + 1, k + 2 + k1, Blocks.planks, 0);
				placePlate(world, i + 7 - i1, j + 2, k + 2 + k1, random, LOTRFoods.DUNLENDING);
			}
		}
		
		for (int i1 = i + 1; i1 >= i - 3; i1 -= 2)
		{
			placeItemFrame(world, i1, j + 2, k - 6, 0, random);
			placeItemFrame(world, i1, j + 2, k + 6, 2, random);
		}
		
		for (int j1 = j + 1; j1 <= j + 5; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 5, woodBlock, woodMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 4, Blocks.torch, 4);
		
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k1, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i - 6, j + 3, k1, slabBlock, slabMeta | 8);
			setBlockAndNotifyAdequately(world, i - 6, j + 4, k1, woodBlock, woodMeta | 8);
			
			if (Math.abs(k1 - k) <= 4)
			{
				setBlockAndNotifyAdequately(world, i - 9, j + 1, k1, slabBlock, slabMeta | 8);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 3, Blocks.fence_gate, 1);
		
		for (int k1 = k - 3; k1 <= k + 1; k1 += 2)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 1, k1, Blocks.fence, 0);
		}
		
		for (int k1 = k - 3; k1 <= k + 1; k1++)
		{
			if (random.nextInt(3) != 0)
			{
				placeMug(world, random, i - 6, j + 2, k1, 1, drink);
			}
		}
		
		placeBarrel(world, random, i - 7, j + 1, k - 5, 3, drink);
		placeBarrel(world, random, i - 8, j + 1, k - 5, 3, drink);
		
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 1, Blocks.furnace, 0);
		setBlockMetadata(world, i - 9, j + 1, k - 1, 5);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k + 1, Blocks.furnace, 0);
		setBlockMetadata(world, i - 9, j + 1, k + 1, 5);
		
		placeFlowerPot(world, i - 9, j + 2, k - 3, getRandomPlant(random));
		placeFlowerPot(world, i - 9, j + 2, k + 3, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k + 4, Blocks.torch, 4);
		
		spawnBartender(world, i - 8, j + 1, k, name);
		
		int men = 3 + random.nextInt(6);
		for (int l = 0; l < men; l++)
		{
			spawnDunlending(world, i - 3, j + 1, k, i, j, k);
		}
		
		spawnDrunkard(world, i - 3, j + 1, k, i, j, k);
	}
	
	private void generateFacingNorth(World world, Random random, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i, j, k + 10, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 10, Blocks.wooden_door, 3);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 10, Blocks.wooden_door, 8);
		
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 11, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k + 11, Blocks.torch, 3);
		
		setBlockAndNotifyAdequately(world, i, j + 3, k + 11, Blocks.wall_sign, 3);
		String[] name = LOTRNames.getRandomDunlendingTavernName(random);
		TileEntity tileentity = world.getTileEntity(i, j + 3, k + 11);
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[1] = name[0];
			sign.signText[2] = name[1];
		}
		
		for (int i1 = i - 4; i1 <= i + 2; i1 += 6)
		{
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 10, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 10, stairBlock, 5);
			setBlockAndNotifyAdequately(world, i1 + 2, j + 2, k + 10, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1 + 2, j + 3, k + 10, stairBlock, 4);
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 9, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k + 9, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 9, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 9, Blocks.torch, 2);
		
		for (int k1 = k + 8; k1 >= k + 4; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 5, j + 1, k1, stairBlock, 1);
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, stairBlock, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 5; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 3, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 3, woodBlock, woodMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 3, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 3, Blocks.torch, 2);
		
		for (int k1 = 0; k1 <= 2; k1++)
		{
			for (int i1 = 0; i1 <= 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i - 3 + i1, j + 1, k + 7 - k1, Blocks.planks, 0);
				placePlate(world, i - 3 + i1, j + 2, k + 7 - k1, random, LOTRFoods.DUNLENDING);
				setBlockAndNotifyAdequately(world, i + 2 + i1, j + 1, k + 7 - k1, Blocks.planks, 0);
				placePlate(world, i + 2 + i1, j + 2, k + 7 - k1, random, LOTRFoods.DUNLENDING);
			}
		}
		
		for (int k1 = k + 1; k1 >= k - 3; k1 -= 2)
		{
			placeItemFrame(world, i - 6, j + 2, k1, 3, random);
			placeItemFrame(world, i + 6, j + 2, k1, 1, random);
		}
		
		for (int j1 = j + 1; j1 <= j + 5; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 5, woodBlock, woodMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 5, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 5, Blocks.torch, 2);
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 6, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 6, slabBlock, slabMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 6, woodBlock, woodMeta | 4);
			
			if (Math.abs(i1 - i) <= 4)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k - 9, slabBlock, slabMeta | 8);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 6, Blocks.fence_gate, 2);
		
		for (int i1 = i - 3; i1 <= i + 1; i1 += 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 4, Blocks.fence, 0);
		}
		
		for (int i1 = i - 3; i1 <= i + 1; i1++)
		{
			if (random.nextInt(3) != 0)
			{
				placeMug(world, random, i1, j + 2, k - 6, 2, drink);
			}
		}
		
		placeBarrel(world, random, i - 5, j + 1, k - 7, 5, drink);
		placeBarrel(world, random, i - 5, j + 1, k - 8, 5, drink);
		
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 9, Blocks.furnace, 0);
		setBlockMetadata(world, i - 1, j + 1, k - 9, 3);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 9, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 9, Blocks.furnace, 0);
		setBlockMetadata(world, i + 1, j + 1, k - 9, 3);
		
		placeFlowerPot(world, i - 3, j + 2, k - 9, getRandomPlant(random));
		placeFlowerPot(world, i + 3, j + 2, k - 9, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 9, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 9, Blocks.torch, 2);
		
		spawnBartender(world, i, j + 1, k - 8, name);
		
		int men = 3 + random.nextInt(6);
		for (int l = 0; l < men; l++)
		{
			spawnDunlending(world, i, j + 1, k - 3, i, j, k);
		}
		
		spawnDrunkard(world, i, j + 1, k - 3, i, j, k);
	}
	
	private void generateFacingEast(World world, Random random, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i - 10, j, k, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i - 10, j + 1, k, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i - 10, j + 2, k, Blocks.wooden_door, 8);
		
		setBlockAndNotifyAdequately(world, i - 11, j + 2, k - 1, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 11, j + 2, k + 1, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i - 11, j + 3, k, Blocks.wall_sign, 4);
		String[] name = LOTRNames.getRandomDunlendingTavernName(random);
		TileEntity tileentity = world.getTileEntity(i - 11, j + 3, k);
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[1] = name[0];
			sign.signText[2] = name[1];
		}
		
		for (int k1 = k - 4; k1 <= k + 2; k1 += 6)
		{
			setBlockAndNotifyAdequately(world, i - 10, j + 2, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i - 10, j + 3, k1, stairBlock, 7);
			setBlockAndNotifyAdequately(world, i - 10, j + 2, k1 + 2, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i - 10, j + 3, k1 + 2, stairBlock, 6);
		}
		
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 1, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k + 1, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k + 4, Blocks.torch, 4);
		
		for (int i1 = i - 8; i1 <= i - 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 5, stairBlock, 3);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 5, stairBlock, 2);
		}
		
		for (int j1 = j + 1; j1 <= j + 5; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k - 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i - 3, j1, k + 5, woodBlock, woodMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 4, Blocks.torch, 4);
		
		for (int i1 = 0; i1 <= 2; i1++)
		{
			for (int k1 = 0; k1 <= 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 7 + i1, j + 1, k - 3 + k1, Blocks.planks, 0);
				placePlate(world, i - 7 + i1, j + 2, k - 3 + k1, random, LOTRFoods.DUNLENDING);
				setBlockAndNotifyAdequately(world, i - 7 + i1, j + 1, k + 2 + k1, Blocks.planks, 0);
				placePlate(world, i - 7 + i1, j + 2, k + 2 + k1, random, LOTRFoods.DUNLENDING);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 3; i1 += 2)
		{
			placeItemFrame(world, i1, j + 2, k - 6, 0, random);
			placeItemFrame(world, i1, j + 2, k + 6, 2, random);
		}
		
		for (int j1 = j + 1; j1 <= j + 5; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 5, woodBlock, woodMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 4, Blocks.torch, 4);
		
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k1, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i + 6, j + 3, k1, slabBlock, slabMeta | 8);
			setBlockAndNotifyAdequately(world, i + 6, j + 4, k1, woodBlock, woodMeta | 8);
			
			if (Math.abs(k1 - k) <= 4)
			{
				setBlockAndNotifyAdequately(world, i + 9, j + 1, k1, slabBlock, slabMeta | 8);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 3, Blocks.fence_gate, 3);
		
		for (int k1 = k - 3; k1 <= k + 1; k1 += 2)
		{
			setBlockAndNotifyAdequately(world, i + 4, j + 1, k1, Blocks.fence, 0);
		}
		
		for (int k1 = k - 3; k1 <= k + 1; k1++)
		{
			if (random.nextInt(3) != 0)
			{
				placeMug(world, random, i + 6, j + 2, k1, 3, drink);
			}
		}
		
		placeBarrel(world, random, i + 7, j + 1, k - 5, 3, drink);
		placeBarrel(world, random, i + 8, j + 1, k - 5, 3, drink);
		
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k - 1, Blocks.furnace, 0);
		setBlockMetadata(world, i + 9, j + 1, k - 1, 4);
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k + 1, Blocks.furnace, 0);
		setBlockMetadata(world, i + 9, j + 1, k + 1, 4);
		
		placeFlowerPot(world, i + 9, j + 2, k - 3, getRandomPlant(random));
		placeFlowerPot(world, i + 9, j + 2, k + 3, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k + 4, Blocks.torch, 4);
		
		spawnBartender(world, i + 8, j + 1, k, name);
		
		int men = 3 + random.nextInt(6);
		for (int l = 0; l < men; l++)
		{
			spawnDunlending(world, i + 3, j + 1, k, i, j, k);
		}
		
		spawnDrunkard(world, i + 3, j + 1, k, i, j, k);
	}
	
	private void spawnBartender(World world, int i, int j, int k, String[] tavernName)
	{
		LOTREntityDunlendingBartender bartender = new LOTREntityDunlendingBartender(world);
		bartender.setLocationAndAngles(i + 0.5D, j, k + 0.5D, 0F, 0F);
		bartender.setSpecificLocationName(tavernName[0] + " " + tavernName[1]);
		bartender.onSpawnWithEgg(null);
		world.spawnEntityInWorld(bartender);
		bartender.setHomeArea(i, j, k, 2);
	}
	
	private void spawnDunlending(World world, int i, int j, int k, int originX, int originY, int originZ)
	{
		LOTREntityDunlending dunlending = new LOTREntityDunlending(world);
		dunlending.setLocationAndAngles(i + 0.5D, j, k + 0.5D, 0F, 0F);
		dunlending.onSpawnWithEgg(null);
		dunlending.isNPCPersistent = true;
		world.spawnEntityInWorld(dunlending);
		dunlending.setHomeArea(originX, originY, originZ, 16);
	}
	
	private void spawnDrunkard(World world, int i, int j, int k, int originX, int originY, int originZ)
	{
		LOTREntityDunlendingDrunkard drunkard = new LOTREntityDunlendingDrunkard(world);
		drunkard.setLocationAndAngles(i + 0.5D, j, k + 0.5D, 0F, 0F);
		drunkard.onSpawnWithEgg(null);
		drunkard.isNPCPersistent = true;
		world.spawnEntityInWorld(drunkard);
		drunkard.setHomeArea(originX, originY, originZ, 16);
		drunkard.setCurrentItemOrArmor(0, new ItemStack(drink, 1, 3 + world.rand.nextInt(2)));
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
		spawnItemFrame(world, i, j, k, direction, item);
	}
}
