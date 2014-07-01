package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityHobbit;
import lotr.common.entity.npc.LOTREntityHobbitBartender;
import lotr.common.entity.npc.LOTREntityHobbitDrunkard;
import lotr.common.entity.npc.LOTREntityHobbitShirriffChief;
import lotr.common.entity.npc.LOTRNames;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class LOTRWorldGenHobbitTavern extends LOTRWorldGenStructureBase
{
	private Block stairBlock;
	
	private Block pillarBlock;
	private int pillarMeta;
	
	private Block wallBlock;
	private int wallMeta;
	
	private Block floorBlock;
	private int floorMeta;
	
	private Item drink;
	
	public LOTRWorldGenHobbitTavern(boolean flag)
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
			if (world.getBiomeGenForCoords(i, k) != LOTRBiome.shire)
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

		int minHeight = j;
		int maxHeight = j;
		
		switch (rotation)
		{
			case 0:
				k += 8;
				i += 6;
				if (restrictions)
				{
					for (int k1 = k - 7; k1 <= k + 7; k1++)
					{
						for (int i1 = i - 10; i1 <= i + 10; i1++)
						{
							int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
							Block block = world.getBlock(i1, j1, k1);
							if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone && block != Blocks.sand)
							{
								return false;
							}
							
							if (j1 < minHeight)
							{
								minHeight = j1;
							}
							if (j1 > maxHeight)
							{
								maxHeight = j1;
							}
						}
					}
				}
				break;
			case 1:
				i -= 8;
				k += 6;
				if (restrictions)
				{
					for (int i1 = i - 7; i1 <= i + 7; i1++)
					{
						for (int k1 = k - 10; k1 <= k + 10; k1++)
						{
							int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
							Block block= world.getBlock(i1, j1, k1);
							if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone && block != Blocks.sand)
							{
								return false;
							}
							
							if (j1 < minHeight)
							{
								minHeight = j1;
							}
							if (j1 > maxHeight)
							{
								maxHeight = j1;
							}
						}
					}
				}
				break;
			case 2:
				k -= 8;
				i += 6;
				if (restrictions)
				{
					for (int k1 = k - 7; k1 <= k + 7; k1++)
					{
						for (int i1 = i - 10; i1 <= i + 10; i1++)
						{
							int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
							Block block = world.getBlock(i1, j1, k1);
							if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone && block != Blocks.sand)
							{
								return false;
							}
							
							if (j1 < minHeight)
							{
								minHeight = j1;
							}
							if (j1 > maxHeight)
							{
								maxHeight = j1;
							}
						}
					}
				}
				break;
			case 3:
				i += 8;
				k += 6;
				if (restrictions)
				{
					for (int i1 = i - 7; i1 <= i + 7; i1++)
					{
						for (int k1 = k - 10; k1 <= k + 10; k1++)
						{
							int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
							Block block = world.getBlock(i1, j1, k1);
							if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone && block != Blocks.sand)
							{
								return false;
							}
							
							if (j1 < minHeight)
							{
								minHeight = j1;
							}
							if (j1 > maxHeight)
							{
								maxHeight = j1;
							}
						}
					}
				}
				break;
		}
	
		if (restrictions && maxHeight - minHeight > 5)
		{
			return false;
		}

		stairBlock = Blocks.oak_stairs;
		
		int randomWood = random.nextInt(4);
		switch (randomWood)
		{
			case 0:
				pillarBlock = Blocks.log;
				pillarMeta = 0;
				
				wallBlock = Blocks.planks;
				wallMeta = 0;

				stairBlock = Blocks.oak_stairs;
				break;
			case 1:
				pillarBlock = Blocks.log;
				pillarMeta = 1;
				
				wallBlock = Blocks.planks;
				wallMeta = 1;
				
				stairBlock = Blocks.spruce_stairs;
				break;
			case 2:
				pillarBlock = Blocks.log;
				pillarMeta = 2;
				
				wallBlock = Blocks.planks;
				wallMeta = 2;
				
				stairBlock = Blocks.birch_stairs;
				break;
			case 3:
				pillarBlock = LOTRMod.wood;
				pillarMeta = 0;
				
				wallBlock = LOTRMod.planks;
				wallMeta = 0;
				
				stairBlock = LOTRMod.stairsShirePine;
				break;
		}
		
		int randomFloor = random.nextInt(3);
		switch (randomFloor)
		{
			case 0:
				floorBlock = Blocks.cobblestone;
				floorMeta = 0;
				break;
			case 1:
				floorBlock = Blocks.stonebrick;
				floorMeta = 0;
				break;
			case 2:
				floorBlock = Blocks.brick_block;
				floorMeta = 0;
				break;
		}
		
		drink = LOTRFoods.HOBBIT_DRINK.getRandomFood(random).getItem();
		
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
		
		int hobbits = 3 + random.nextInt(5);
		for (int l = 0; l < hobbits; l++)
		{
			LOTREntityHobbit hobbit = new LOTREntityHobbit(world);
			hobbit.setLocationAndAngles(i, j + 1, k, 0F, 0F);
			hobbit.setHomeArea(i, j + 1, k, 16);
			hobbit.onSpawnWithEgg(null);
			hobbit.isNPCPersistent = true;
			world.spawnEntityInWorld(hobbit);
		}
		
		LOTREntityHobbitDrunkard drunkard = new LOTREntityHobbitDrunkard(world);
		drunkard.setLocationAndAngles(i, j + 1, k, 0F, 0F);
		drunkard.setHomeArea(i, j + 1, k, 7);
		drunkard.onSpawnWithEgg(null);
		drunkard.isNPCPersistent = true;
		world.spawnEntityInWorld(drunkard);
		drunkard.setCurrentItemOrArmor(0, new ItemStack(drink, 1, 3 + random.nextInt(2)));
		
		if (random.nextBoolean())
		{
			LOTREntityHobbitShirriffChief shirriff = new LOTREntityHobbitShirriffChief(world);
			shirriff.setLocationAndAngles(i, j + 1, k, 0F, 0F);
			shirriff.setHomeArea(i, j + 1, k, 7);
			shirriff.spawnRidingHorse = false;
			shirriff.onSpawnWithEgg(null);
			world.spawnEntityInWorld(shirriff);
		}
		
		return true;
	}
	
	private void generateFacingSouth(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1D, j + 1D, k + 1D).expand(11D, 6D, 8D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int k1 = k - 7; k1 <= k + 7; k1++)
		{
			for (int i1 = i - 10; i1 <= i + 10; i1++)
			{
				for (int j1 = j; (j1 == j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
				{
					if (Math.abs(i1 - i) == 10 && Math.abs(k1 - k) == 7)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, pillarBlock, pillarMeta);
					}
					else if (Math.abs(i1 - i) == 10 || Math.abs(k1 - k) == 7)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.dirt, 0);
					}
					
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int k1 = k - 6; k1 <= k + 6; k1++)
		{
			for (int i1 = i - 9; i1 <= i + 9; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorBlock, floorMeta);
			}
		}
		
		for (int k1 = k - 7; k1 <= k + 7; k1++)
		{
			for (int i1 = i - 10; i1 <= i + 10; i1++)
			{
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					if (Math.abs(i1 - i) == 10 && Math.abs(k1 - k) == 7)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, pillarBlock, pillarMeta);
					}
					else if (Math.abs(i1 - i) == 10 || Math.abs(k1 - k) == 7)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
					}
				}
			}
		}
		
		for (int k1 = k - 7; k1 <= k + 7; k1++)
		{
			for (int i1 = i - 10; i1 <= i + 10; i1++)
			{
				for (int j1 = j + 4; j1 <= j + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
				}
			}
		}
		
		for (int l = 1; l >= 0; l--)
		{
			for (int k1 = k - 7 - l; k1 <= k + 7 + l; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 10 - l, j + 4 + (1 - l), k1, stairBlock, 0);
				setBlockAndNotifyAdequately(world, i + 10 + l, j + 4 + (1 - l), k1, stairBlock, 1);
			}
			
			for (int i1 = i - 9 - l; i1 <= i + 9 + l; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4 + (1 - l), k - 7 - l, stairBlock, 2);
				setBlockAndNotifyAdequately(world, i1, j + 4 + (1 - l), k + 7 + l, stairBlock, 3);
			}
		}
		
		for (int k1 = k - 6; k1 <= k + 6; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int i1 = i - 9; i1 <= (j1 == j + 3 ? i - 8 : i - 9); i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
				}
			}
			setBlockAndNotifyAdequately(world, i - 8, j + 2, k1, Blocks.tripwire_hook, 3);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 7; j1++)
			{
				for (int i1 = i - 9; i1 <= i - 7; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.brick_block, 0);
				}
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 6; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 8, j1, k1, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i - 8, j, k1, LOTRMod.hearth, 0);
			setBlockAndNotifyAdequately(world, i - 8, j + 1, k1, Blocks.fire, 0);
			setBlockAndNotifyAdequately(world, i - 8, j + 8, k1, Blocks.flower_pot, 0);
			
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 7, j1, k1, Blocks.iron_bars, 0);
			}
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 9, j + 7, k1, Blocks.brick_stairs, 0);
			setBlockAndNotifyAdequately(world, i - 7, j + 7, k1, Blocks.brick_stairs, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 8, j + 7, k - 2, Blocks.brick_stairs, 2);
		setBlockAndNotifyAdequately(world, i - 8, j + 7, k + 2, Blocks.brick_stairs, 3);
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k - 6, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 6, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 9, j1, k - 2, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 9, j1, k + 1, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 6, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 6, pillarBlock, pillarMeta);
		}
		
		for (int i1 = i; i1 <= i + 2; i1++)
		{
			for (int j1 = j + 2; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 7, Blocks.glass_pane, 0);
			}
		}
		
		for (int j1 = j + 2; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 1, j1, k - 7, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 3, j1, k - 7, pillarBlock, pillarMeta);
		}
		
		for (int i1 = i - 2; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 6, Blocks.oak_stairs, 3);
		}
		
		for (int i1 = i + 6; i1 <= i + 9; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 6, Blocks.oak_stairs, 3);
		}
		
		for (int k1 = k - 5; k1 <= k - 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 9, j + 1, k1, Blocks.oak_stairs, 0);
		}
		
		for (int k1 = k + 2; k1 <= k + 6; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 9, j + 1, k1, Blocks.oak_stairs, 0);
		}
		
		for (int i1 = i + 6; i1 <= i + 8; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 6, Blocks.oak_stairs, 2);
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k - 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 3, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k - 3, Blocks.planks, 0);
		
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k + 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 3, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k + 3, Blocks.planks, 0);
		
		placePlate(world, i + 6, j + 2, k - 4, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 7, j + 2, k - 4, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 6, j + 2, k - 3, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 7, j + 2, k - 3, random, LOTRFoods.HOBBIT);
		
		placePlate(world, i + 6, j + 2, k + 4, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 7, j + 2, k + 4, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 6, j + 2, k + 3, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 7, j + 2, k + 3, random, LOTRFoods.HOBBIT);
		
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 5, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 5, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k + 1, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k - 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 5, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 5, Blocks.torch, 3);
		
		for (int i1 = i - 3; i1 <= i + 3; i1 += 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k, Blocks.fence, 0);
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 2, wallBlock, wallMeta);
		}
		
		for (int k1 = k + 3; k1 <= k + 6; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k1, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 4, Blocks.fence_gate, 1);
		
		placeBarrel(world, random, i - 2, j + 2, k + 2, 3, drink);
		placeBarrel(world, random, i + 2, j + 2, k + 2, 3, drink);
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			placeMug(world, random, i1, j + 2, k + 2, 0, drink);
		}

		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 6, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 6, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 6, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k + 6, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 6, wallBlock, wallMeta);
		placeFlowerPot(world, i, j + 2, k + 6, getRandomPlant(random));
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 6, LOTRMod.hobbitOven, 2);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k + 6, LOTRMod.hobbitOven, 2);
		
		setBlockAndNotifyAdequately(world, i - 5, j + 3, k + 3, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i, j + 3, k + 4, LOTRMod.chandelier, 3);
		setBlockAndNotifyAdequately(world, i + 5, j + 3, k + 3, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 3, k - 3, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i, j + 3, k - 3, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i + 5, j + 3, k - 3, LOTRMod.chandelier, 0);
		
		setBlockAndNotifyAdequately(world, i - 6, j, k - 7, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 7, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 7, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i - 7, j + 2, k - 8, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 8, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i - 6, j + 3, k - 8, Blocks.wall_sign, 2);
		String[] name = LOTRNames.getRandomHobbitTavernName(random);
		TileEntity tileentity = world.getTileEntity(i - 6, j + 3, k - 8);
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[1] = name[0];
			sign.signText[2] = name[1];
		}
		
		spawnBartender(world, i, j + 1, k + 4, name);
	}
	
	private void generateFacingWest(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1D, j + 1D, k + 1D).expand(8D, 6D, 11D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int i1 = i + 7; i1 >= i - 7; i1--)
		{
			for (int k1 = k - 10; k1 <= k + 10; k1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					if (Math.abs(i1 - i) == 7 && Math.abs(k1 - k) == 10)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, pillarBlock, pillarMeta);
					}
					else if (Math.abs(i1 - i) == 7 || Math.abs(k1 - k) == 10)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.dirt, 0);
					}
					
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i + 6; i1 >= i - 6; i1--)
		{
			for (int k1 = k - 9; k1 <= k + 9; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorBlock, floorMeta);
			}
		}
		
		for (int i1 = i - 7; i1 <= i + 7; i1++)
		{
			for (int k1 = k - 10; k1 <= k + 10; k1++)
			{
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					if (Math.abs(i1 - i) == 7 && Math.abs(k1 - k) == 10)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, pillarBlock, pillarMeta);
					}
					else if (Math.abs(i1 - i) == 7 || Math.abs(k1 - k) == 10)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
					}
				}
			}
		}
		
		for (int i1 = i + 7; i1 >= i - 7; i1--)
		{
			for (int k1 = k - 10; k1 <= k + 10; k1++)
			{
				for (int j1 = j + 4; j1 <= j + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
				}
			}
		}
		
		for (int l = 1; l >= 0; l--)
		{
			for (int i1 = i + 7 + l; i1 >= i - 7 - l; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4 + (1 - l), k - 10 - l, stairBlock, 2);
				setBlockAndNotifyAdequately(world, i1, j + 4 + (1 - l), k + 10 + l, stairBlock, 3);
			}
			
			for (int k1 = k - 9 - l; k1 <= k + 9 + l; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 7 + l, j + 4 + (1 - l), k1, stairBlock, 1);
				setBlockAndNotifyAdequately(world, i - 7 - l, j + 4 + (1 - l), k1, stairBlock, 0);
			}
		}
		
		for (int i1 = i + 6; i1 >= i - 6; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int k1 = k - 9; k1 <= (j1 == j + 3 ? k - 8 : k - 9); k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
				}
			}
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 8, Blocks.tripwire_hook, 0);
		}
		
		for (int i1 = i + 2; i1 >= i - 2; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 7; j1++)
			{
				for (int k1 = k - 9; k1 <= k - 7; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.brick_block, 0);
				}
			}
		}
		
		for (int i1 = i + 1; i1 >= i - 1; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 6; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 8, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i1, j, k - 8, LOTRMod.hearth, 0);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 8, Blocks.fire, 0);
			setBlockAndNotifyAdequately(world, i1, j + 8, k - 8, Blocks.flower_pot, 0);
			
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 7, Blocks.iron_bars, 0);
			}
		}
		
		for (int i1 = i + 2; i1 >= i - 2; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 7, k - 9, Blocks.brick_stairs, 2);
			setBlockAndNotifyAdequately(world, i1, j + 7, k - 7, Blocks.brick_stairs, 3);
		}
		
		setBlockAndNotifyAdequately(world, i + 2, j + 7, k - 8, Blocks.brick_stairs, 1);
		setBlockAndNotifyAdequately(world, i - 2, j + 7, k - 8, Blocks.brick_stairs, 0);
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 6, j1, k - 3, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 6, j1, k + 5, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 2, j1, k + 9, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 1, j1, k + 9, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 6, j1, k + 5, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 6, j1, k - 5, pillarBlock, pillarMeta);
		}
		
		for (int k1 = k; k1 <= k + 2; k1++)
		{
			for (int j1 = j + 2; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 7, j1, k1, Blocks.glass_pane, 0);
			}
		}
		
		for (int j1 = j + 2; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 7, j1, k - 1, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 7, j1, k + 3, pillarBlock, pillarMeta);
		}
		
		for (int k1 = k - 2; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k1, Blocks.oak_stairs, 0);
		}
		
		for (int k1 = k + 6; k1 <= k + 9; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k1, Blocks.oak_stairs, 0);
		}
		
		for (int i1 = i + 5; i1 >= i + 3; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 9, Blocks.oak_stairs, 2);
		}
		
		for (int i1 = i - 2; i1 >= i - 6; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 9, Blocks.oak_stairs, 2);
		}
		
		for (int k1 = k + 6; k1 <= k + 8; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k1, Blocks.oak_stairs, 1);
		}
		
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k + 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k + 7, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 7, Blocks.planks, 0);
		
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k + 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k + 7, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 7, Blocks.planks, 0);
		
		placePlate(world, i + 4, j + 2, k + 6, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 4, j + 2, k + 7, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 3, j + 2, k + 6, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 3, j + 2, k + 7, random, LOTRFoods.HOBBIT);
		
		placePlate(world, i - 4, j + 2, k + 6, random, LOTRFoods.HOBBIT);
		placePlate(world, i - 4, j + 2, k + 7, random, LOTRFoods.HOBBIT);
		placePlate(world, i - 3, j + 2, k + 6, random, LOTRFoods.HOBBIT);
		placePlate(world, i - 3, j + 2, k + 7, random, LOTRFoods.HOBBIT);
		
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 5, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 5, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 8, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k + 8, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 5, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 3, Blocks.torch, 2);
		
		for (int k1 = k - 3; k1 <= k + 3; k1 += 2)
		{
			setBlockAndNotifyAdequately(world, i, j + 1, k1, Blocks.fence, 0);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k1, wallBlock, wallMeta);
		}
		
		for (int i1 = i - 3; i1 >= i - 6; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 3, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 3, Blocks.fence_gate, 0);
		
		placeBarrel(world, random, i - 2, j + 2, k - 2, 4, drink);
		placeBarrel(world, random, i - 2, j + 2, k + 2, 4, drink);
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			placeMug(world, random, i - 2, j + 2, k1, 1, drink);
		}

		setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 3, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k + 3, Blocks.torch, 1);
		
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 2, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 2, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k, wallBlock, wallMeta);
		placeFlowerPot(world, i - 6, j + 2, k, getRandomPlant(random));
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 1, LOTRMod.hobbitOven, 5);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 1, LOTRMod.hobbitOven, 5);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k - 5, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 3, k, LOTRMod.chandelier, 3);
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k + 5, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 3, k - 5, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 3, k, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 3, k + 5, LOTRMod.chandelier, 0);
		
		setBlockAndNotifyAdequately(world, i + 7, j, k - 6, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k - 6, Blocks.wooden_door, 2);
		setBlockAndNotifyAdequately(world, i + 7, j + 2, k - 6, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k - 7, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k - 5, Blocks.torch, 1);
		
		setBlockAndNotifyAdequately(world, i + 8, j + 3, k - 6, Blocks.wall_sign, 5);
		String[] name = LOTRNames.getRandomHobbitTavernName(random);
		TileEntity tileentity = world.getTileEntity(i + 8, j + 3, k - 6);
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[1] = name[0];
			sign.signText[2] = name[1];
		}
		
		spawnBartender(world, i - 4, j + 1, k, name);
	}
	
	private void generateFacingNorth(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1D, j + 1D, k + 1D).expand(11D, 6D, 8D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int k1 = k + 7; k1 >= k - 7; k1--)
		{
			for (int i1 = i - 10; i1 <= i + 10; i1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					if (Math.abs(i1 - i) == 10 && Math.abs(k1 - k) == 7)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, pillarBlock, pillarMeta);
					}
					else if (Math.abs(i1 - i) == 10 || Math.abs(k1 - k) == 7)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.dirt, 0);
					}
					
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int k1 = k + 6; k1 >= k - 6; k1--)
		{
			for (int i1 = i - 9; i1 <= i + 9; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorBlock, floorMeta);
			}
		}
		
		for (int k1 = k - 7; k1 <= k + 7; k1++)
		{
			for (int i1 = i - 10; i1 <= i + 10; i1++)
			{
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					if (Math.abs(i1 - i) == 10 && Math.abs(k1 - k) == 7)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, pillarBlock, pillarMeta);
					}
					else if (Math.abs(i1 - i) == 10 || Math.abs(k1 - k) == 7)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
					}
				}
			}
		}
		
		for (int k1 = k + 7; k1 >= k - 7; k1--)
		{
			for (int i1 = i - 10; i1 <= i + 10; i1++)
			{
				for (int j1 = j + 4; j1 <= j + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
				}
			}
		}
		
		for (int l = 1; l >= 0; l--)
		{
			for (int k1 = k + 7 + l; k1 >= k - 7 - l; k1--)
			{
				setBlockAndNotifyAdequately(world, i - 10 - l, j + 4 + (1 - l), k1, stairBlock, 0);
				setBlockAndNotifyAdequately(world, i + 10 + l, j + 4 + (1 - l), k1, stairBlock, 1);
			}
			
			for (int i1 = i - 9 - l; i1 <= i + 9 + l; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4 + (1 - l), k + 7 + l, stairBlock, 3);
				setBlockAndNotifyAdequately(world, i1, j + 4 + (1 - l), k - 7 - l, stairBlock, 2);
			}
		}
		
		for (int k1 = k + 6; k1 >= k - 6; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int i1 = i - 9; i1 <= (j1 == j + 3 ? i - 8 : i - 9); i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
				}
			}
			setBlockAndNotifyAdequately(world, i - 8, j + 2, k1, Blocks.tripwire_hook, 3);
		}
		
		for (int k1 = k + 2; k1 >= k - 2; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 7; j1++)
			{
				for (int i1 = i - 9; i1 <= i - 7; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.brick_block, 0);
				}
			}
		}
		
		for (int k1 = k + 1; k1 >= k - 1; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 6; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 8, j1, k1, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i - 8, j, k1, LOTRMod.hearth, 0);
			setBlockAndNotifyAdequately(world, i - 8, j + 1, k1, Blocks.fire, 0);
			setBlockAndNotifyAdequately(world, i - 8, j + 8, k1, Blocks.flower_pot, 0);
			
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 7, j1, k1, Blocks.iron_bars, 0);
			}
		}
		
		for (int k1 = k + 2; k1 >= k - 2; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 9, j + 7, k1, Blocks.brick_stairs, 0);
			setBlockAndNotifyAdequately(world, i - 7, j + 7, k1, Blocks.brick_stairs, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 8, j + 7, k + 2, Blocks.brick_stairs, 3);
		setBlockAndNotifyAdequately(world, i - 8, j + 7, k - 2, Blocks.brick_stairs, 2);
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k + 6, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 6, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 9, j1, k + 2, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 9, j1, k - 1, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 6, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 6, pillarBlock, pillarMeta);
		}
		
		for (int i1 = i; i1 <= i + 2; i1++)
		{
			for (int j1 = j + 2; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 7, Blocks.glass_pane, 0);
			}
		}
		
		for (int j1 = j + 2; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 1, j1, k + 7, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 3, j1, k + 7, pillarBlock, pillarMeta);
		}
		
		for (int i1 = i - 2; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 6, Blocks.oak_stairs, 2);
		}
		
		for (int i1 = i + 6; i1 <= i + 9; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 6, Blocks.oak_stairs, 2);
		}
		
		for (int k1 = k + 5; k1 >= k + 3; k1--)
		{
			setBlockAndNotifyAdequately(world, i + 9, j + 1, k1, Blocks.oak_stairs, 0);
		}
		
		for (int k1 = k - 2; k1 >= k - 6; k1--)
		{
			setBlockAndNotifyAdequately(world, i + 9, j + 1, k1, Blocks.oak_stairs, 0);
		}
		
		for (int i1 = i + 6; i1 <= i + 8; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 6, Blocks.oak_stairs, 3);
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k + 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 3, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k + 3, Blocks.planks, 0);
		
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k - 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 3, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k - 3, Blocks.planks, 0);
		
		placePlate(world, i + 6, j + 2, k + 4, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 7, j + 2, k + 4, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 6, j + 2, k + 3, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 7, j + 2, k + 3, random, LOTRFoods.HOBBIT);
		
		placePlate(world, i + 6, j + 2, k - 4, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 7, j + 2, k - 4, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 6, j + 2, k - 3, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 7, j + 2, k - 3, random, LOTRFoods.HOBBIT);
		
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 5, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 5, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k - 1, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k + 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 5, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 5, Blocks.torch, 4);
		
		for (int i1 = i - 3; i1 <= i + 3; i1 += 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k, Blocks.fence, 0);
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 2, wallBlock, wallMeta);
		}
		
		for (int k1 = k - 3; k1 >= k - 6; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k1, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 4, Blocks.fence_gate, 1);
		
		placeBarrel(world, random, i - 2, j + 2, k - 2, 2, drink);
		placeBarrel(world, random, i + 2, j + 2, k - 2, 2, drink);
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			placeMug(world, random, i1, j + 2, k - 2, 2, drink);
		}

		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 6, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 6, Blocks.torch, 3);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 6, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 6, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 6, wallBlock, wallMeta);
		placeFlowerPot(world, i, j + 2, k - 6, getRandomPlant(random));
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 6, LOTRMod.hobbitOven, 3);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 6, LOTRMod.hobbitOven, 3);
		
		setBlockAndNotifyAdequately(world, i - 5, j + 3, k - 3, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i, j + 3, k - 4, LOTRMod.chandelier, 3);
		setBlockAndNotifyAdequately(world, i + 5, j + 3, k - 3, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 3, k + 3, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i, j + 3, k + 3, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i + 5, j + 3, k + 3, LOTRMod.chandelier, 0);
		
		setBlockAndNotifyAdequately(world, i - 6, j, k + 7, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 7, Blocks.wooden_door, 3);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k + 7, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i - 7, j + 2, k + 8, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 8, Blocks.torch, 3);
		
		setBlockAndNotifyAdequately(world, i - 6, j + 3, k + 8, Blocks.wall_sign, 3);
		String[] name = LOTRNames.getRandomHobbitTavernName(random);
		TileEntity tileentity = world.getTileEntity(i - 6, j + 3, k + 8);
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[1] = name[0];
			sign.signText[2] = name[1];
		}
		
		spawnBartender(world, i, j + 1, k - 4, name);
	}
	
	private void generateFacingEast(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1D, j + 1D, k + 1D).expand(8D, 6D, 11D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int i1 = i - 7; i1 <= i + 7; i1++)
		{
			for (int k1 = k - 10; k1 <= k + 10; k1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					if (Math.abs(i1 - i) == 7 && Math.abs(k1 - k) == 10)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, pillarBlock, pillarMeta);
					}
					else if (Math.abs(i1 - i) == 7 || Math.abs(k1 - k) == 10)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.dirt, 0);
					}
					
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i - 6; i1 <= i + 6; i1++)
		{
			for (int k1 = k - 9; k1 <= k + 9; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorBlock, floorMeta);
			}
		}
		
		for (int i1 = i - 7; i1 <= i + 7; i1++)
		{
			for (int k1 = k - 10; k1 <= k + 10; k1++)
			{
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					if (Math.abs(i1 - i) == 7 && Math.abs(k1 - k) == 10)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, pillarBlock, pillarMeta);
					}
					else if (Math.abs(i1 - i) == 7 || Math.abs(k1 - k) == 10)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
					}
				}
			}
		}
		
		for (int i1 = i - 7; i1 <= i + 7; i1++)
		{
			for (int k1 = k - 10; k1 <= k + 10; k1++)
			{
				for (int j1 = j + 4; j1 <= j + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
				}
			}
		}
		
		for (int l = 1; l >= 0; l--)
		{
			for (int i1 = i - 7 - l; i1 <= i + 7 + l; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4 + (1 - l), k - 10 - l, stairBlock, 2);
				setBlockAndNotifyAdequately(world, i1, j + 4 + (1 - l), k + 10 + l, stairBlock, 3);
			}
			
			for (int k1 = k - 9 - l; k1 <= k + 9 + l; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 7 - l, j + 4 + (1 - l), k1, stairBlock, 0);
				setBlockAndNotifyAdequately(world, i + 7 + l, j + 4 + (1 - l), k1, stairBlock, 1);
			}
		}
		
		for (int i1 = i - 6; i1 <= i + 6; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int k1 = k - 9; k1 <= (j1 == j + 3 ? k - 8 : k - 9); k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
				}
			}
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 8, Blocks.tripwire_hook, 0);
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 7; j1++)
			{
				for (int k1 = k - 9; k1 <= k - 7; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.brick_block, 0);
				}
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 6; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 8, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i1, j, k - 8, LOTRMod.hearth, 0);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 8, Blocks.fire, 0);
			setBlockAndNotifyAdequately(world, i1, j + 8, k - 8, Blocks.flower_pot, 0);
			
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 7, Blocks.iron_bars, 0);
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 7, k - 9, Blocks.brick_stairs, 2);
			setBlockAndNotifyAdequately(world, i1, j + 7, k - 7, Blocks.brick_stairs, 3);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 7, k - 8, Blocks.brick_stairs, 0);
		setBlockAndNotifyAdequately(world, i + 2, j + 7, k - 8, Blocks.brick_stairs, 1);
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j1, k - 3, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 6, j1, k + 5, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 2, j1, k + 9, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 1, j1, k + 9, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 6, j1, k + 5, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 6, j1, k - 5, pillarBlock, pillarMeta);
		}
		
		for (int k1 = k; k1 <= k + 2; k1++)
		{
			for (int j1 = j + 2; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 7, j1, k1, Blocks.glass_pane, 0);
			}
		}
		
		for (int j1 = j + 2; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 7, j1, k - 1, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 7, j1, k + 3, pillarBlock, pillarMeta);
		}
		
		for (int k1 = k - 2; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k1, Blocks.oak_stairs, 1);
		}
		
		for (int k1 = k + 6; k1 <= k + 9; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k1, Blocks.oak_stairs, 1);
		}
		
		for (int i1 = i - 5; i1 <= i - 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 9, Blocks.oak_stairs, 2);
		}
		
		for (int i1 = i + 2; i1 <= i + 6; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 9, Blocks.oak_stairs, 2);
		}
		
		for (int k1 = k + 6; k1 <= k + 8; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k1, Blocks.oak_stairs, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k + 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k + 7, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 7, Blocks.planks, 0);
		
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k + 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k + 7, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 7, Blocks.planks, 0);
		
		placePlate(world, i - 4, j + 2, k + 6, random, LOTRFoods.HOBBIT);
		placePlate(world, i - 4, j + 2, k + 7, random, LOTRFoods.HOBBIT);
		placePlate(world, i - 3, j + 2, k + 6, random, LOTRFoods.HOBBIT);
		placePlate(world, i - 3, j + 2, k + 7, random, LOTRFoods.HOBBIT);
		
		placePlate(world, i + 4, j + 2, k + 6, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 4, j + 2, k + 7, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 3, j + 2, k + 6, random, LOTRFoods.HOBBIT);
		placePlate(world, i + 3, j + 2, k + 7, random, LOTRFoods.HOBBIT);
		
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 5, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 5, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k + 8, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 8, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 5, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 3, Blocks.torch, 1);
		
		for (int k1 = k - 3; k1 <= k + 3; k1 += 2)
		{
			setBlockAndNotifyAdequately(world, i, j + 1, k1, Blocks.fence, 0);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 2, j + 1, k1, wallBlock, wallMeta);
		}
		
		for (int i1 = i + 3; i1 <= i + 6; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 3, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 3, Blocks.fence_gate, 0);
		
		placeBarrel(world, random, i + 2, j + 2, k - 2, 5, drink);
		placeBarrel(world, random, i + 2, j + 2, k + 2, 5, drink);
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			placeMug(world, random, i + 2, j + 2, k1, 3, drink);
		}

		setBlockAndNotifyAdequately(world, i + 6, j + 2, k - 3, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 6, j + 2, k + 3, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 2, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 2, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k, wallBlock, wallMeta);
		placeFlowerPot(world, i + 6, j + 2, k, getRandomPlant(random));
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 1, LOTRMod.hobbitOven, 4);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 1, LOTRMod.hobbitOven, 4);
		
		setBlockAndNotifyAdequately(world, i + 3, j + 3, k - 5, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 3, k, LOTRMod.chandelier, 3);
		setBlockAndNotifyAdequately(world, i + 3, j + 3, k + 5, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k - 5, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k, LOTRMod.chandelier, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k + 5, LOTRMod.chandelier, 0);
		
		setBlockAndNotifyAdequately(world, i - 7, j, k - 6, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i - 7, j + 1, k - 6, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 2, k - 6, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i - 8, j + 2, k - 7, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 8, j + 2, k - 5, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i - 8, j + 3, k - 6, Blocks.wall_sign, 4);
		String[] name = LOTRNames.getRandomHobbitTavernName(random);
		TileEntity tileentity = world.getTileEntity(i - 8, j + 3, k - 6);
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[1] = name[0];
			sign.signText[2] = name[1];
		}
		
		spawnBartender(world, i + 4, j + 1, k, name);
	}
	
	private void spawnBartender(World world, int i, int j, int k, String[] tavernName)
	{
		LOTREntityHobbitBartender bartender = new LOTREntityHobbitBartender(world);
		bartender.setLocationAndAngles(i + 0.5D, j, k + 0.5D, 0F, 0F);
		bartender.setSpecificLocationName(tavernName[0] + " " + tavernName[1]);
		bartender.onSpawnWithEgg(null);
		world.spawnEntityInWorld(bartender);
		bartender.setHomeArea(i, j, k, 2);
	}
	
	private ItemStack getRandomPlant(Random random)
	{
		int i = random.nextInt(4);
		switch (i)
		{
			case 0:
				return new ItemStack(Blocks.red_flower);
			case 1:
				return new ItemStack(Blocks.yellow_flower);
			case 2:
				return new ItemStack(LOTRMod.bluebell);
			case 3:
				return new ItemStack(LOTRMod.shireHeather);
		}
		return null;
	}
}
