package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityHobbit;
import lotr.common.entity.npc.LOTRNames;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenHobbitHole extends LOTRWorldGenStructureBase
{
	private Block floorBlock;
	private int floorMeta;
	
	private Block floorStoneBlock;
	private int floorStoneMeta;
	
	private Block wallBlock;
	private int wallMeta;
	
	private Block wallStairBlock;
	
	private Block wallHalfBlock;
	private int wallHalfMeta;
	
	private Block rugBlock;
	private int rugMeta;
	
	private int chandelierMeta;
	
	private Block fenceBlock;
	private int fenceMeta;
	
	private boolean isWealthy;
	
	public LOTRWorldGenHobbitHole(boolean flag)
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
		
		switch (rotation)
		{
			case 0:
				k += 16;
				break;
			case 1:
				i -= 16;
				break;
			case 2:
				k -= 16;
				break;
			case 3:
				i += 16;
				break;
		}
		
		int radius = 16;
		int height = 7;
		int extraRadius = 2;
		
		if (restrictions)
		{
			int minHeight = j;
			int maxHeight = j;
			
			for (int i1 = i - radius; i1 <= i + radius; i1++)
			{
				for (int k1 = k - radius; k1 <= k + radius; k1++)
				{
					int i2 = i1 - i;
					int k2 = k1 - k;
					if (i2 * i2 + k2 * k2 > radius * radius)
					{
						continue;
					}
					
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
			
			if (maxHeight - minHeight > 8)
			{
				return false;
			}
		}
		
		if (restrictions)
		{
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1D, j + 1D, k + 1D).expand(radius + 1, height + 1, radius + 1));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int i1 = i - radius; i1 <= i + radius; i1++)
		{
			for (int j1 = j + height; j1 >= j; j1--)
			{
				for (int k1 = k - radius; k1 <= k + radius; k1++)
				{
					int i2 = i1 - i;
					int j2 = j1 - j + (radius - height);
					int k2 = k1 - k;
					if (i2 * i2 + j2 * j2 + k2 * k2 > (radius + extraRadius) * (radius + extraRadius))
					{
						continue;
					}
					boolean grass = !LOTRMod.isOpaque(world, i1, j1 + 1, k1);
					setBlockAndNotifyAdequately(world, i1, j1, k1, grass ? Blocks.grass : Blocks.dirt, 0);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		for (int i1 = i - radius; i1 <= i + radius; i1++)
		{
			for (int k1 = k - radius; k1 <= k + radius; k1++)
			{
				for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					int i2 = i1 - i;
					int k2 = k1 - k;
					if (i2 * i2 + k2 * k2 > radius * radius)
					{
						continue;
					}
					boolean grass = !LOTRMod.isOpaque(world, i1, j1 + 1, k1);
					setBlockAndNotifyAdequately(world, i1, j1, k1, grass ? Blocks.grass : Blocks.dirt, 0);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 8, k, Blocks.brick_block, 0);
		setBlockAndNotifyAdequately(world, i, j + 9, k, Blocks.brick_block, 0);
		setBlockAndNotifyAdequately(world, i, j + 10, k, Blocks.flower_pot, 0);
		
		floorBlock = LOTRMod.planks;
		floorMeta = 0;
		
		floorStoneBlock = random.nextBoolean() ? Blocks.brick_block : (random.nextBoolean() ? Blocks.cobblestone : Blocks.stonebrick);
		floorStoneMeta = 0;
		
		wallBlock = Blocks.planks;
		wallMeta = random.nextInt(3);
		
		wallHalfBlock = Blocks.wooden_slab;
		wallHalfMeta = wallMeta;

		rugBlock = Blocks.carpet;
		rugMeta = random.nextInt(16);

		chandelierMeta = 1;
		
		if (random.nextBoolean())
		{
			if (random.nextBoolean())
			{
				fenceBlock = Blocks.fence;
				fenceMeta = 0;
			}
			else
			{
				fenceBlock = LOTRMod.fence;
				fenceMeta = 0;
			}
		}
		else
		{
			fenceBlock = Blocks.cobblestone_wall;
			fenceMeta = 0;
		}
		
		isWealthy = false;
		
		if (random.nextInt(5) == 0)
		{
			wallBlock = LOTRMod.planks;
			wallMeta = 4 + random.nextInt(3);
			
			wallHalfBlock = LOTRMod.woodSlabSingle;
			wallHalfMeta = wallMeta;
			
			chandelierMeta = random.nextInt(4) == 0 ? 3 : 2;
			
			isWealthy = true;
		}
		
		if (wallBlock == Blocks.planks)
		{
			switch (wallMeta)
			{
				case 0:
					wallStairBlock = Blocks.oak_stairs;
					break;
				case 1:
					wallStairBlock = Blocks.spruce_stairs;
					break;
				case 2:
					wallStairBlock = Blocks.birch_stairs;
					break;
			}
		}
		else if (wallBlock == LOTRMod.planks)
		{
			switch (wallMeta)
			{
				case 4:
					wallStairBlock = LOTRMod.stairsApple;
					break;
				case 5:
					wallStairBlock = LOTRMod.stairsPear;
					break;
				case 6:
					wallStairBlock = LOTRMod.stairsCherry;
					break;
			}
		}
		
		boolean generated = false;
		switch (rotation)
		{
			case 0:
				generated = generateFacingSouth(world, i, j, k, random);
				break;
			case 1:
				generated = generateFacingWest(world, i, j, k, random);
				break;
			case 2:
				generated = generateFacingNorth(world, i, j, k, random);
				break;
			case 3:
				generated = generateFacingEast(world, i, j, k, random);
				break;
		}
		
		if (generated)
		{
			int decorationRadius = radius - 4;

			int grass = 1 + random.nextInt(3);
			for (int l = 0; l < grass; l++)
			{
				int i1 = i - decorationRadius + random.nextInt(decorationRadius * 2);
				int k1 = k - decorationRadius + random.nextInt(decorationRadius * 2);
				LOTRBiome.shire.getRandomWorldGenForGrass(random).generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
			
			int flowers = 1 + random.nextInt(3);
			for (int l = 0; l < flowers; l++)
			{
				int i1 = i - decorationRadius + random.nextInt(decorationRadius * 2);
				int k1 = k - decorationRadius + random.nextInt(decorationRadius * 2);
				LOTRBiome.shire.plantFlower(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
			
			if (random.nextInt(4) == 0)
			{
				int i1 = i - decorationRadius + random.nextInt(decorationRadius * 2);
				int k1 = k - decorationRadius + random.nextInt(decorationRadius * 2);
				WorldGenerator treeGen = LOTRBiome.shire.func_150567_a(random);
				treeGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
			
			String[] names = LOTRNames.getRandomHobbitCoupleNames(random);
			
			LOTREntityHobbit hobbitMale = new LOTREntityHobbit(world);
			hobbitMale.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0F, 0F);
			hobbitMale.setHobbitName(names[0]);
			hobbitMale.familyInfo.setNPCMale(true);
			hobbitMale.setHomeArea(i, j + 1, k, MathHelper.floor_double(radius * 1.5D));
			hobbitMale.onSpawnWithEgg(null);
			hobbitMale.isNPCPersistent = true;
			world.spawnEntityInWorld(hobbitMale);
			
			LOTREntityHobbit hobbitFemale = new LOTREntityHobbit(world);
			hobbitFemale.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0F, 0F);
			hobbitFemale.setHobbitName(names[1]);
			hobbitFemale.familyInfo.setNPCMale(false);
			hobbitFemale.setHomeArea(i, j + 1, k, MathHelper.floor_double(radius * 1.5D));
			hobbitFemale.onSpawnWithEgg(null);
			hobbitFemale.isNPCPersistent = true;
			world.spawnEntityInWorld(hobbitFemale);
			
			int maxChildren = hobbitMale.familyInfo.getRandomMaxChildren();
			
			hobbitMale.setCurrentItemOrArmor(4, new ItemStack(LOTRMod.hobbitRing));
			hobbitMale.familyInfo.spouseUniqueID = hobbitFemale.getPersistentID();
			hobbitMale.familyInfo.setMaxBreedingDelay();
			hobbitMale.familyInfo.maxChildren = maxChildren;
			
			hobbitFemale.setCurrentItemOrArmor(4, new ItemStack(LOTRMod.hobbitRing));
			hobbitFemale.familyInfo.spouseUniqueID = hobbitMale.getPersistentID();
			hobbitFemale.familyInfo.setMaxBreedingDelay();
			hobbitFemale.familyInfo.maxChildren = maxChildren;
		}
		
		return generated;
	}
	
	private boolean generateFacingSouth(World world, int i, int j, int k, Random random)
	{
		for (int k1 = k - 12; k1 >= k - 16; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				for (int i1 = i - 3; i1 <= i + 3; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int k1 = k - 12; k1 >= k - 16; k1--)
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				for (int j1 = j; j1 == j || (!LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0); j1--)
				{
					boolean grass = j1 == j;
					setBlockAndNotifyAdequately(world, i1, j1, k1, grass ? Blocks.grass : Blocks.dirt, 0);
				}
			}
		}
		
		for (int k1 = k - 12; k1 >= k - 15; k1--)
		{
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, fenceBlock, fenceMeta);
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k1, fenceBlock, fenceMeta);
			setBlockAndNotifyAdequately(world, i, j, k1, Blocks.gravel, 0);
		}
		setBlockAndNotifyAdequately(world, i, j, k - 11, Blocks.gravel, 0);
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 15, fenceBlock, fenceMeta);
		}
		
		setBlockAndNotifyAdequately(world, i, j + 1, k - 15, Blocks.fence_gate, 0);
		
		for (int k1 = 0; k1 <= 2; k1++)
		{
			Block flower = k1 == 1 ? Blocks.red_flower : Blocks.yellow_flower;
			setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 12 - k1, flower, 0);
			setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 12 - k1, flower, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				if (j1 < j + 3 || Math.abs(i1 - i) < 2)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 11, wallBlock, wallMeta);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 11, wallStairBlock, 1);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 11, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 11, wallStairBlock, 0);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k - 11, wallStairBlock, 5);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 11, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k - 11, wallStairBlock, 4);
		
		for (int k1 = k - 10; k1 <= k + 1; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int i1 = i - 2; i1 <= i + 2; i1++)
				{
					if (k1 == k - 10)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i + 1, j, k1, floorStoneBlock, floorStoneMeta);
			setBlockAndNotifyAdequately(world, i, j, k1, floorBlock, floorMeta);
			setBlockAndNotifyAdequately(world, i - 1, j, k1, floorStoneBlock, floorStoneMeta);
			
			if (k1 == k - 10)
			{
				continue;
			}
			
			setBlockAndNotifyAdequately(world, i + 2, j + 1, k1, wallHalfBlock, wallHalfMeta);
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k1, wallHalfBlock, wallHalfMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 3, j1, k1, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i - 3, j1, k1, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i + 2, j + 3, k1, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i - 2, j + 3, k1, wallBlock, wallMeta);
			
			setBlockAndNotifyAdequately(world, i + 1, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i - 1, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
			
			setBlockAndNotifyAdequately(world, i, j + 4, k1, wallBlock, wallMeta);
		}
		
        setBlockAndNotifyAdequately(world, i, j + 1, k - 10, Blocks.wooden_door, 1);
        setBlockAndNotifyAdequately(world, i, j + 2, k - 10, Blocks.wooden_door, 8);
		
		for (int k1 = k - 8; k1 <= k; k1 += 4)
		{
			setBlockAndNotifyAdequately(world, i, j + 3, k1, LOTRMod.chandelier, chandelierMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 2, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j, k + 2, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 2, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 2, Blocks.air, 0);
		
		for (int k1 = k + 3; k1 <= k + 9; k1++)
		{
			for (int i1 = i - 3; i1 <= i + 3; i1++)
			{
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
				
				setBlockAndNotifyAdequately(world, i1, j, k1, floorBlock, floorMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 3, k + 6, LOTRMod.chandelier, chandelierMeta);
		
		for (int k1 = k + 5; k1 <= k + 7; k1++)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, rugBlock, rugMeta);
			}
		}
		
		if (isWealthy && random.nextInt(4) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k + 6, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i, j, k + 6, LOTRChestContents.HOBBIT_HOLE_TREASURE);
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 10, wallBlock, wallMeta);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int j1 = j + 2; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 10, Blocks.glass_pane, 0);
				setBlockAndNotifyAdequately(world, i1, j1, k + 10, Blocks.glass_pane, 0);
				
				for (int k1 = k + 11; k1 <= k + 14; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.dirt)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.grass, 0);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 10, wallBlock, wallMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k + 3, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i + 3, j1, k + 3, wallBlock, wallMeta);
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 3, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int k1 = k + 4; k1 <= k + 9; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i + 3, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 9, Blocks.oak_stairs, 2);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 8, Blocks.oak_stairs, 1);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 8, Blocks.oak_stairs, 0);
		
		for (int k1 = k + 4; k1 <= k + 9; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 4, j1, k1, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i + 4, j1, k1, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k + 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k + 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 6, Blocks.air, 0);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 9, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 9, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 2, j, k - 6, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i + 3, j, k - 6, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 6, Blocks.wooden_door, 2);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 6, Blocks.wooden_door, 8);
		
		for (int k1 = k - 8; k1 <= k - 3; k1++)
		{
			for (int i1 = i + 4; i1 <= i + 8; i1++)
			{
				if (i1 == i + 8 && k1 == k - 8)
				{
					continue;
				}
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
				
				if (i1 >= i + 7 && k1 <= k - 7)
				{
					continue;
				}
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
		}
		
		for (int i1 = i + 4; i1 <= i + 7; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 2, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k - 8, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 7, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 3, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int k1 = k - 7; k1 <= k - 3; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 8, j1, k1, wallBlock, wallMeta);
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			for (int i1 = i + 5; i1 <= i + 6; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 8, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j1, k - 9, Blocks.bookshelf, 0);
			}
			
			for (int k1 = k - 6; k1 <= k - 4; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 8, j1, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i + 9, j1, k1, Blocks.bookshelf, 0);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j + 3, k - 5, LOTRMod.chandelier, chandelierMeta);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 5, Blocks.oak_stairs, 3);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 3, Blocks.wooden_slab, 8);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k - 3, Blocks.chest, 2);
		
		LOTRChestContents.fillChest(world, random, i + 7, j + 1, k - 3, LOTRChestContents.HOBBIT_HOLE_STUDY);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 2, j, k - 6, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i - 3, j, k - 6, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 6, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 6, Blocks.wooden_door, 8);
		
		for (int k1 = k - 7; k1 <= k - 4; k1++)
		{
			for (int i1 = i - 4; i1 >= i - 7; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
		}
		
		for (int i1 = i - 4; i1 >= i - 7; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 8, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k - 3, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 7, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 4, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int k1 = k - 7; k1 <= k - 3; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 8, j1, k1, wallBlock, wallMeta);
			}
		}
		
		for (int k1 = k - 7; k1 <= k - 6; k1++)
		{
			for (int i1 = i - 5; i1 >= i - 6; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, rugBlock, rugMeta);
			}
		}
		
		for (int i1 = i - 5; i1 >= i - 6; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 8, floorStoneBlock, floorStoneMeta);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 8, Blocks.wooden_slab, 8);
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 8, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 9, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 1, k - 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 4, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i - 7, j + 2, k - 4, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 5, Blocks.bed, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 4, Blocks.bed, 8);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 5, Blocks.bed, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 4, Blocks.bed, 8);
		
		setBlockAndNotifyAdequately(world, i + 4, j, k + 6, floorBlock, floorMeta);
		
		for (int i1 = i + 5; i1 <= i + 6; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k + 6, floorStoneBlock, floorStoneMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 6, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 6, wallBlock, wallMeta);
		}
		
		for (int i1 = i + 5; i1 <= i + 7; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k + 5, floorStoneBlock, floorStoneMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 5, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 5, wallBlock, wallMeta);
		}
		
		for (int i1 = i + 5; i1 <= i + 8; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k + 4, floorStoneBlock, floorStoneMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 4, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 4, wallBlock, wallMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 7, j1, k + 6, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i + 8, j1, k + 5, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i + 9, j1, k + 4, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 7, j + 2, k + 5, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k + 4, Blocks.torch, 2);
		
		for (int k1 = k + 3; k1 >= k - 1; k1--)
		{
			for (int i1 = i + 4; i1 <= i + 9; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 3, j1, k1, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i + 10, j1, k1, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i + 4, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i + 9, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int k1 = k + 2; k1 >= k; k1--)
		{
			setBlockAndNotifyAdequately(world, i + 4, j + 1, k1, Blocks.oak_stairs, 1);
			setBlockAndNotifyAdequately(world, i + 9, j + 1, k1, Blocks.oak_stairs, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j + 3, k + 1, LOTRMod.chandelier, chandelierMeta);
		setBlockAndNotifyAdequately(world, i + 7, j + 3, k + 1, LOTRMod.chandelier, chandelierMeta);
		
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 2, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k + 2, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 1, Blocks.wooden_slab, 9);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k + 1, Blocks.wooden_slab, 9);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k, Blocks.planks, 1);
		
		for (int i1 = i + 6; i1 <= i + 7; i1++)
		{
			for (int k1 = k + 2; k1 >= k; k1--)
			{
				placePlate(world, i1, j + 2, k1, random, LOTRFoods.HOBBIT);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 5, j + 3, k + 6, wallHalfBlock, wallHalfMeta | 8);
		setBlockAndNotifyAdequately(world, i + 6, j + 3, k + 6, wallHalfBlock, wallHalfMeta | 8);
		setBlockAndNotifyAdequately(world, i + 7, j + 3, k + 5, wallHalfBlock, wallHalfMeta | 8);
		setBlockAndNotifyAdequately(world, i + 8, j + 3, k + 4, wallHalfBlock, wallHalfMeta | 8);
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int i1 = i + 5; i1 <= i + 6; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 7, wallBlock, wallMeta);
			}
			
			for (int i1 = i + 8; i1 <= i + 9; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 2, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j, k + 6, floorBlock, floorMeta);
		
		for (int k1 = k + 7; k1 >= k + 3; k1--)
		{
			for (int i1 = i - 5; i1 >= i - 7; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.double_stone_slab, 0);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
		}
		
		for (int k1 = k + 6; k1 >= k + 3; k1--)
		{
			for (int i1 = i - 5; i1 >= i - 6; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 8, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 8, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 8, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k + 8, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 9, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k + 9, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i - 5, j + 3, k + 8, Blocks.double_stone_slab, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 3, k + 8, Blocks.double_stone_slab, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 3, k + 8, Blocks.double_stone_slab, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 1, k + 8, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i - 7, j + 2, k + 8, wallBlock, wallMeta);
		
		for (int k1 = k + 6; k1 <= k + 7; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 8, j1, k1, wallBlock, wallMeta);
			}
		}
		
		for (int k1 = k + 3; k1 <= k + 5; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 8, j, k1, Blocks.double_stone_slab, 0);
			setBlockAndNotifyAdequately(world, i - 8, j + 2, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i - 9, j + 2, k1, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i - 8, j + 3, k1, Blocks.double_stone_slab, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k + 4, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k + 5, LOTRMod.hobbitOven, 4);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k + 3, LOTRMod.hobbitOven, 4);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k + 4, Blocks.cauldron, 3);
		
		setBlockAndNotifyAdequately(world, i - 6, j + 3, k + 5, LOTRMod.chandelier, chandelierMeta);
		
		for (int i1 = i - 4; i1 >= i - 9; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 2, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 6, j, k + 2, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 2, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k + 2, Blocks.air, 0);
		
		for (int k1 = k + 1; k1 >= k - 2; k1--)
		{
			for (int i1 = i - 4; i1 >= i - 9; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorBlock, floorMeta);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, Blocks.double_stone_slab, 0);
			}
			
			setBlockAndNotifyAdequately(world, i - 4, j + 1, k1, Blocks.wooden_slab, 8);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 10, j1, k1, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 2, Blocks.torch, 3);
		
		int pastries = 1 + random.nextInt(5);
		for (int l = 0; l < pastries; l++)
		{
			Block block = getRandomFoodBlock(random);
			setBlockAndNotifyAdequately(world, i - 4, j + 2, (k - 2) + random.nextInt(4), block, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 9, j1, k - 3, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i - 4, j1, k + 3, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 2, Blocks.chest, 3);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 2, Blocks.chest, 3);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k + 1, Blocks.chest, 2);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k + 1, Blocks.chest, 2);
		
		LOTRChestContents.fillChest(world, random, i - 8, j + 1, k - 2, LOTRChestContents.HOBBIT_HOLE_LARDER);
		LOTRChestContents.fillChest(world, random, i - 9, j + 1, k - 2, LOTRChestContents.HOBBIT_HOLE_LARDER);
		LOTRChestContents.fillChest(world, random, i - 8, j + 1, k + 1, LOTRChestContents.HOBBIT_HOLE_LARDER);
		LOTRChestContents.fillChest(world, random, i - 9, j + 1, k + 1, LOTRChestContents.HOBBIT_HOLE_LARDER);
		
		return true;
	}
	
	private boolean generateFacingWest(World world, int i, int j, int k, Random random)
	{
		for (int i1 = i + 12; i1 <= i + 16; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				for (int k1 = k - 3; k1 <= k + 3; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i + 12; i1 <= i + 16; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				for (int j1 = j; j1 == j || (!LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0); j1--)
				{
					boolean grass = j1 == j;
					setBlockAndNotifyAdequately(world, i1, j1, k1, grass ? Blocks.grass : Blocks.dirt, 0);
				}
			}
		}
		
		for (int i1 = i + 12; i1 <= i + 15; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, fenceBlock, fenceMeta);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 3, fenceBlock, fenceMeta);
			setBlockAndNotifyAdequately(world, i1, j, k, Blocks.gravel, 0);
		}
		setBlockAndNotifyAdequately(world, i + 11, j, k, Blocks.gravel, 0);
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 15, j + 1, k1, fenceBlock, fenceMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 15, j + 1, k, Blocks.fence_gate, 1);
		
		for (int i1 = 0; i1 <= 2; i1++)
		{
			Block flower = i1 == 1 ? Blocks.red_flower : Blocks.yellow_flower;
			setBlockAndNotifyAdequately(world, i + 12 + i1, j + 1, k + 1, flower, 0);
			setBlockAndNotifyAdequately(world, i + 12 + i1, j + 1, k - 1, flower, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				if (j1 < j + 3 || Math.abs(k1 - k) < 2)
				{
					setBlockAndNotifyAdequately(world, i + 11, j1, k1, wallBlock, wallMeta);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 11, j + 1, k - 1, wallStairBlock, 3);
		setBlockAndNotifyAdequately(world, i + 11, j + 1, k, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 11, j + 1, k + 1, wallStairBlock, 2);
		setBlockAndNotifyAdequately(world, i + 11, j + 2, k - 1, wallStairBlock, 7);
		setBlockAndNotifyAdequately(world, i + 11, j + 2, k, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 11, j + 2, k + 1, wallStairBlock, 6);
		
		for (int i1 = i + 10; i1 >= i - 1; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					if (i1 == i + 10)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i1, j, k + 1, floorStoneBlock, floorStoneMeta);
			setBlockAndNotifyAdequately(world, i1, j, k, floorBlock, floorMeta);
			setBlockAndNotifyAdequately(world, i1, j, k - 1, floorStoneBlock, floorStoneMeta);
			
			if (i1 == i + 10)
			{
				continue;
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 2, wallHalfBlock, wallHalfMeta);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 2, wallHalfBlock, wallHalfMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 3, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k - 3, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 2, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 2, wallBlock, wallMeta);
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 1, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 1, wallHalfBlock, wallHalfMeta | 8);
			
			setBlockAndNotifyAdequately(world, i1, j + 4, k, wallBlock, wallMeta);
		}
		
        setBlockAndNotifyAdequately(world, i + 10, j + 1, k, Blocks.wooden_door, 2);
        setBlockAndNotifyAdequately(world, i + 10, j + 2, k, Blocks.wooden_door, 8);
		
		for (int i1 = i + 8; i1 >= i; i1 -= 4)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k, LOTRMod.chandelier, chandelierMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 2, j1, k1, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j, k, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k, Blocks.air, 0);
		
		for (int i1 = i - 3; i1 >= i - 9; i1--)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
				
				setBlockAndNotifyAdequately(world, i1, j, k1, floorBlock, floorMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 6, j + 3, k, LOTRMod.chandelier, chandelierMeta);
		
		for (int i1 = i - 5; i1 >= i - 7; i1--)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, rugBlock, rugMeta);
			}
		}
		
		if (isWealthy && random.nextInt(4) == 0)
		{
			setBlockAndNotifyAdequately(world, i - 6, j, k, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i - 6, j, k, LOTRChestContents.HOBBIT_HOLE_TREASURE);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 10, j1, k1, wallBlock, wallMeta);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int j1 = j + 2; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 10, j1, k1, Blocks.glass_pane, 0);
				setBlockAndNotifyAdequately(world, i - 10, j1, k1, Blocks.glass_pane, 0);
				
				for (int i1 = i - 11; i1 >= i - 14; i1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.dirt)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.grass, 0);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i - 10, j + 4, k1, wallBlock, wallMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k - 3, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i - 3, j1, k + 3, wallBlock, wallMeta);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int i1 = i - 4; i1 >= i - 9; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 3, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 3, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 9, j + 1, k1, Blocks.oak_stairs, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 3, Blocks.oak_stairs, 3);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k + 3, Blocks.oak_stairs, 2);
		
		for (int i1 = i - 4; i1 >= i - 9; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 4, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k + 4, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 4, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 4, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 4, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k + 4, Blocks.air, 0);
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 3, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 3, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 3, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k + 3, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 2, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 6, j, k + 2, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i + 6, j, k + 3, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 3, Blocks.wooden_door, 3);
		setBlockAndNotifyAdequately(world, i + 6, j + 2, k + 3, Blocks.wooden_door, 8);
		
		for (int i1 = i + 8; i1 >= i + 3; i1--)
		{
			for (int k1 = k + 4; k1 <= k + 8; k1++)
			{
				if (i1 == i + 8 && k1 == k + 8)
				{
					continue;
				}
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
				
				if (i1 >= i + 7 && k1 >= k + 7)
				{
					continue;
				}
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
		}
		
		for (int k1 = k + 4; k1 <= k + 7; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 2, j1, k1, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i + 8, j1, k1, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i + 7, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i + 3, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int i1 = i + 7; i1 >= i + 3; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 8, wallBlock, wallMeta);
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			for (int k1 = k + 5; k1 <= k + 6; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 8, j1, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i + 9, j1, k1, Blocks.bookshelf, 0);
			}
			
			for (int i1 = i + 6; i1 >= i + 4; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 8, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j1, k + 9, Blocks.bookshelf, 0);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 5, j + 3, k + 6, LOTRMod.chandelier, chandelierMeta);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 5, Blocks.oak_stairs, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 5, Blocks.wooden_slab, 8);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 7, Blocks.chest, 5);
		
		LOTRChestContents.fillChest(world, random, i + 3, j + 1, k + 7, LOTRChestContents.HOBBIT_HOLE_STUDY);
		
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 2, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 6, j, k - 2, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i + 6, j, k - 3, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 3, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i + 6, j + 2, k - 3, Blocks.wooden_door, 8);
		
		for (int i1 = i + 7; i1 >= i + 4; i1--)
		{
			for (int k1 = k - 4; k1 >= k - 7; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
		}
		
		for (int k1 = k - 4; k1 >= k - 7; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 8, j1, k1, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i + 3, j1, k1, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i + 7, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i + 4, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int i1 = i + 7; i1 >= i + 3; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 8, wallBlock, wallMeta);
			}
		}
		
		for (int i1 = i + 7; i1 >= i + 6; i1--)
		{
			for (int k1 = k - 5; k1 >= k - 6; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, rugBlock, rugMeta);
			}
		}
		
		for (int k1 = k - 5; k1 >= k - 6; k1--)
		{
			setBlockAndNotifyAdequately(world, i + 8, j, k1, floorStoneBlock, floorStoneMeta);
			setBlockAndNotifyAdequately(world, i + 8, j + 1, k1, Blocks.wooden_slab, 8);
			setBlockAndNotifyAdequately(world, i + 8, j + 2, k1, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i + 9, j + 1, k1, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 7, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 4, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 7, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 5, Blocks.bed, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 5, Blocks.bed, 9);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 6, Blocks.bed, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 6, Blocks.bed, 9);
		
		setBlockAndNotifyAdequately(world, i - 6, j, k + 4, floorBlock, floorMeta);
		
		for (int k1 = k + 5; k1 <= k + 6; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j, k1, floorStoneBlock, floorStoneMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 6, j1, k1, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i - 6, j + 4, k1, wallBlock, wallMeta);
		}
		
		for (int k1 = k + 5; k1 <= k + 7; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j, k1, floorStoneBlock, floorStoneMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 5, j1, k1, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i - 5, j + 4, k1, wallBlock, wallMeta);
		}
		
		for (int k1 = k + 5; k1 <= k + 8; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j, k1, floorStoneBlock, floorStoneMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 4, j1, k1, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i - 4, j + 4, k1, wallBlock, wallMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j1, k + 7, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 8, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i - 4, j1, k + 9, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 7, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 8, Blocks.torch, 4);
		
		for (int i1 = i - 3; i1 <= i + 1; i1++)
		{
			for (int k1 = k + 4; k1 <= k + 9; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 3, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k + 10, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 4, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 9, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int i1 = i - 2; i1 <= i; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 4, Blocks.oak_stairs, 3);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 9, Blocks.oak_stairs, 2);
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j + 3, k + 6, LOTRMod.chandelier, chandelierMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 3, k + 7, LOTRMod.chandelier, chandelierMeta);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 6, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 7, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 6, Blocks.wooden_slab, 9);
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 7, Blocks.wooden_slab, 9);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 6, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 7, Blocks.planks, 1);
		
		for (int i1 = i - 2; i1 <= i; i1++)
		{
			for (int k1 = k + 6; k1 <= k + 7; k1++)
			{
				placePlate(world, i1, j + 2, k1, random, LOTRFoods.HOBBIT);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 6, j + 3, k + 5, wallHalfBlock, wallHalfMeta | 8);
		setBlockAndNotifyAdequately(world, i - 6, j + 3, k + 6, wallHalfBlock, wallHalfMeta | 8);
		setBlockAndNotifyAdequately(world, i - 5, j + 3, k + 7, wallHalfBlock, wallHalfMeta | 8);
		setBlockAndNotifyAdequately(world, i - 4, j + 3, k + 8, wallHalfBlock, wallHalfMeta | 8);
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int k1 = k + 5; k1 <= k + 6; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 7, j1, k1, wallBlock, wallMeta);
			}
			
			for (int k1 = k + 8; k1 <= k + 9; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 2, j1, k1, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 6, j, k - 4, floorBlock, floorMeta);
		
		for (int i1 = i - 7; i1 <= i - 3; i1++)
		{
			for (int k1 = k - 5; k1 >= k - 7; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.double_stone_slab, 0);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
		}
		
		for (int i1 = i - 6; i1 <= i - 3; i1++)
		{
			for (int k1 = k - 5; k1 >= k - 6; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 5, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 6, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 8, j + 2, k - 5, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 8, j + 2, k - 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 5, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 6, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i - 8, j + 3, k - 5, Blocks.double_stone_slab, 0);
		setBlockAndNotifyAdequately(world, i - 8, j + 3, k - 6, Blocks.double_stone_slab, 0);
		setBlockAndNotifyAdequately(world, i - 8, j + 3, k - 7, Blocks.double_stone_slab, 0);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 7, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i - 8, j + 2, k - 7, wallBlock, wallMeta);
		
		for (int i1 = i - 6; i1 >= i - 7; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 8, wallBlock, wallMeta);
			}
		}
		
		for (int i1 = i - 3; i1 >= i - 5; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 8, Blocks.double_stone_slab, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 8, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 9, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 8, Blocks.double_stone_slab, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 8, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 8, LOTRMod.hobbitOven, 3);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 8, LOTRMod.hobbitOven, 3);
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 8, Blocks.cauldron, 3);
		
		setBlockAndNotifyAdequately(world, i - 5, j + 3, k - 6, LOTRMod.chandelier, chandelierMeta);
		
		for (int k1 = k - 4; k1 >= k - 9; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 2, j1, k1, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j, k - 6, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 6, Blocks.air, 0);
		
		for (int i1 = i - 1; i1 <= i + 2; i1++)
		{
			for (int k1 = k - 4; k1 >= k - 9; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorBlock, floorMeta);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, Blocks.double_stone_slab, 0);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 4, Blocks.wooden_slab, 8);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 10, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 6, Blocks.torch, 2);
		
		int pastries = 1 + random.nextInt(5);
		for (int l = 0; l < pastries; l++)
		{
			Block block = getRandomFoodBlock(random);
			setBlockAndNotifyAdequately(world, (i + 2) - random.nextInt(4), j + 2, k - 4, block, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 3, j1, k - 9, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i - 3, j1, k - 4, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 8, Blocks.chest, 4);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 9, Blocks.chest, 4);
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 8, Blocks.chest, 5);
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 9, Blocks.chest, 5);
		
		LOTRChestContents.fillChest(world, random, i + 2, j + 1, k - 8, LOTRChestContents.HOBBIT_HOLE_LARDER);
		LOTRChestContents.fillChest(world, random, i + 2, j + 1, k - 9, LOTRChestContents.HOBBIT_HOLE_LARDER);
		LOTRChestContents.fillChest(world, random, i - 1, j + 1, k - 8, LOTRChestContents.HOBBIT_HOLE_LARDER);
		LOTRChestContents.fillChest(world, random, i - 1, j + 1, k - 9, LOTRChestContents.HOBBIT_HOLE_LARDER);
		
		return true;
	}
	
	private boolean generateFacingNorth(World world, int i, int j, int k, Random random)
	{
		for (int k1 = k + 12; k1 <= k + 16; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				for (int i1 = i - 3; i1 <= i + 3; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int k1 = k + 12; k1 <= k + 16; k1++)
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				for (int j1 = j; j1 == j || (!LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0); j1--)
				{
					boolean grass = j1 == j;
					setBlockAndNotifyAdequately(world, i1, j1, k1, grass ? Blocks.grass : Blocks.dirt, 0);
				}
			}
		}
		
		for (int k1 = k + 12; k1 <= k + 15; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, fenceBlock, fenceMeta);
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k1, fenceBlock, fenceMeta);
			setBlockAndNotifyAdequately(world, i, j, k1, Blocks.gravel, 0);
		}
		setBlockAndNotifyAdequately(world, i, j, k + 11, Blocks.gravel, 0);
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 15, fenceBlock, fenceMeta);
		}
		
		setBlockAndNotifyAdequately(world, i, j + 1, k + 15, Blocks.fence_gate, 0);
		
		for (int k1 = 0; k1 <= 2; k1++)
		{
			Block flower = k1 == 1 ? Blocks.red_flower : Blocks.yellow_flower;
			setBlockAndNotifyAdequately(world, i + 1, j + 1, k + 12 + k1, flower, 0);
			setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 12 + k1, flower, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				if (j1 < j + 3 || Math.abs(i1 - i) < 2)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k + 11, wallBlock, wallMeta);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 11, wallStairBlock, 1);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 11, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k + 11, wallStairBlock, 0);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 11, wallStairBlock, 5);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 11, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k + 11, wallStairBlock, 4);
		
		for (int k1 = k + 10; k1 >= k - 1; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int i1 = i - 2; i1 <= i + 2; i1++)
				{
					if (k1 == k + 10)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i + 1, j, k1, floorStoneBlock, floorStoneMeta);
			setBlockAndNotifyAdequately(world, i, j, k1, floorBlock, floorMeta);
			setBlockAndNotifyAdequately(world, i - 1, j, k1, floorStoneBlock, floorStoneMeta);
			
			if (k1 == k + 10)
			{
				continue;
			}
			
			setBlockAndNotifyAdequately(world, i + 2, j + 1, k1, wallHalfBlock, wallHalfMeta);
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k1, wallHalfBlock, wallHalfMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 3, j1, k1, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i - 3, j1, k1, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i + 2, j + 3, k1, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i - 2, j + 3, k1, wallBlock, wallMeta);
			
			setBlockAndNotifyAdequately(world, i + 1, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i - 1, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
			
			setBlockAndNotifyAdequately(world, i, j + 4, k1, wallBlock, wallMeta);
		}
		
        setBlockAndNotifyAdequately(world, i, j + 1, k + 10, Blocks.wooden_door, 3);
        setBlockAndNotifyAdequately(world, i, j + 2, k + 10, Blocks.wooden_door, 8);
		
		for (int k1 = k + 8; k1 >= k; k1 -= 4)
		{
			setBlockAndNotifyAdequately(world, i, j + 3, k1, LOTRMod.chandelier, chandelierMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 2, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j, k - 2, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 2, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 2, Blocks.air, 0);
		
		for (int k1 = k - 3; k1 >= k - 9; k1--)
		{
			for (int i1 = i - 3; i1 <= i + 3; i1++)
			{
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
				
				setBlockAndNotifyAdequately(world, i1, j, k1, floorBlock, floorMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 3, k - 6, LOTRMod.chandelier, chandelierMeta);
		
		for (int k1 = k - 5; k1 >= k - 7; k1--)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, rugBlock, rugMeta);
			}
		}
		
		if (isWealthy && random.nextInt(4) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k - 6, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i, j, k - 6, LOTRChestContents.HOBBIT_HOLE_TREASURE);
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 10, wallBlock, wallMeta);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int j1 = j + 2; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 10, Blocks.glass_pane, 0);
				setBlockAndNotifyAdequately(world, i1, j1, k - 10, Blocks.glass_pane, 0);
				
				for (int k1 = k - 11; k1 >= k - 14; k1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.dirt)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.grass, 0);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 10, wallBlock, wallMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k - 3, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i + 3, j1, k - 3, wallBlock, wallMeta);
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 3, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int k1 = k - 4; k1 >= k - 9; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i + 3, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 9, Blocks.oak_stairs, 3);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 8, Blocks.oak_stairs, 1);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 8, Blocks.oak_stairs, 0);
		
		for (int k1 = k - 4; k1 >= k - 9; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 4, j1, k1, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i + 4, j1, k1, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 6, Blocks.air, 0);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 4, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 4, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 9, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 9, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k + 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 2, j, k + 6, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i + 3, j, k + 6, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 6, Blocks.wooden_door, 2);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 6, Blocks.wooden_door, 8);
		
		for (int k1 = k + 8; k1 >= k + 3; k1--)
		{
			for (int i1 = i + 4; i1 <= i + 8; i1++)
			{
				if (i1 == i + 8 && k1 == k + 8)
				{
					continue;
				}
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
				
				if (i1 >= i + 7 && k1 >= k + 7)
				{
					continue;
				}
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
		}
		
		for (int i1 = i + 4; i1 <= i + 7; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 2, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k + 8, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 7, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 3, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int k1 = k + 7; k1 >= k + 3; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 8, j1, k1, wallBlock, wallMeta);
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			for (int i1 = i + 5; i1 <= i + 6; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 8, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j1, k + 9, Blocks.bookshelf, 0);
			}
			
			for (int k1 = k + 6; k1 >= k + 4; k1--)
			{
				setBlockAndNotifyAdequately(world, i + 8, j1, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i + 9, j1, k1, Blocks.bookshelf, 0);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j + 3, k + 5, LOTRMod.chandelier, chandelierMeta);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 5, Blocks.oak_stairs, 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 3, Blocks.wooden_slab, 8);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k + 3, Blocks.chest, 3);
		
		LOTRChestContents.fillChest(world, random, i + 7, j + 1, k + 3, LOTRChestContents.HOBBIT_HOLE_STUDY);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 2, j, k + 6, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i - 3, j, k + 6, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 6, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 6, Blocks.wooden_door, 8);
		
		for (int k1 = k + 7; k1 >= k + 4; k1--)
		{
			for (int i1 = i - 4; i1 >= i - 7; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
		}
		
		for (int i1 = i - 4; i1 >= i - 7; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 8, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k + 3, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 7, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 4, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int k1 = k + 7; k1 >= k + 3; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 8, j1, k1, wallBlock, wallMeta);
			}
		}
		
		for (int k1 = k + 7; k1 >= k + 6; k1--)
		{
			for (int i1 = i - 5; i1 >= i - 6; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, rugBlock, rugMeta);
			}
		}
		
		for (int i1 = i - 5; i1 >= i - 6; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j, k + 8, floorStoneBlock, floorStoneMeta);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 8, Blocks.wooden_slab, 8);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 8, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 9, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k + 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 1, k + 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 4, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i - 7, j + 2, k + 4, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 5, Blocks.bed, 2);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 4, Blocks.bed, 10);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 5, Blocks.bed, 2);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 4, Blocks.bed, 10);
		
		setBlockAndNotifyAdequately(world, i + 4, j, k - 6, floorBlock, floorMeta);
		
		for (int i1 = i + 5; i1 <= i + 6; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 6, floorStoneBlock, floorStoneMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 6, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 6, wallBlock, wallMeta);
		}
		
		for (int i1 = i + 5; i1 <= i + 7; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 5, floorStoneBlock, floorStoneMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 5, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 5, wallBlock, wallMeta);
		}
		
		for (int i1 = i + 5; i1 <= i + 8; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 4, floorStoneBlock, floorStoneMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 4, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 4, wallBlock, wallMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 7, j1, k - 6, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i + 8, j1, k - 5, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i + 9, j1, k - 4, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 7, j + 2, k - 5, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k - 4, Blocks.torch, 0);
		
		for (int k1 = k - 3; k1 <= k + 1; k1++)
		{
			for (int i1 = i + 4; i1 <= i + 9; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 3, j1, k1, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i + 10, j1, k1, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i + 4, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i + 9, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int k1 = k - 2; k1 <= k; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 4, j + 1, k1, Blocks.oak_stairs, 1);
			setBlockAndNotifyAdequately(world, i + 9, j + 1, k1, Blocks.oak_stairs, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j + 3, k - 1, LOTRMod.chandelier, chandelierMeta);
		setBlockAndNotifyAdequately(world, i + 7, j + 3, k - 1, LOTRMod.chandelier, chandelierMeta);
		
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 2, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k - 2, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 1, Blocks.wooden_slab, 9);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k - 1, Blocks.wooden_slab, 9);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k, Blocks.planks, 1);
		
		for (int i1 = i + 6; i1 <= i + 7; i1++)
		{
			for (int k1 = k - 2; k1 <= k; k1++)
			{
				placePlate(world, i1, j + 2, k1, random, LOTRFoods.HOBBIT);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 5, j + 3, k - 6, wallHalfBlock, wallHalfMeta | 8);
		setBlockAndNotifyAdequately(world, i + 6, j + 3, k - 6, wallHalfBlock, wallHalfMeta | 8);
		setBlockAndNotifyAdequately(world, i + 7, j + 3, k - 5, wallHalfBlock, wallHalfMeta | 8);
		setBlockAndNotifyAdequately(world, i + 8, j + 3, k - 4, wallHalfBlock, wallHalfMeta | 8);
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int i1 = i + 5; i1 <= i + 6; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 7, wallBlock, wallMeta);
			}
			
			for (int i1 = i + 8; i1 <= i + 9; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 2, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j, k - 6, floorBlock, floorMeta);
		
		for (int k1 = k - 7; k1 <= k - 3; k1++)
		{
			for (int i1 = i - 5; i1 >= i - 7; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.double_stone_slab, 0);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
		}
		
		for (int k1 = k - 6; k1 <= k - 3; k1++)
		{
			for (int i1 = i - 5; i1 >= i - 6; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 8, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 8, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 8, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 8, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 9, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 9, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i - 5, j + 3, k - 8, Blocks.double_stone_slab, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 3, k - 8, Blocks.double_stone_slab, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 3, k - 8, Blocks.double_stone_slab, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 1, k - 8, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i - 7, j + 2, k - 8, wallBlock, wallMeta);
		
		for (int k1 = k - 6; k1 >= k - 7; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 8, j1, k1, wallBlock, wallMeta);
			}
		}
		
		for (int k1 = k - 3; k1 >= k - 5; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 8, j, k1, Blocks.double_stone_slab, 0);
			setBlockAndNotifyAdequately(world, i - 8, j + 2, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i - 9, j + 2, k1, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i - 8, j + 3, k1, Blocks.double_stone_slab, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 4, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 5, LOTRMod.hobbitOven, 4);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 3, LOTRMod.hobbitOven, 4);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 4, Blocks.cauldron, 3);
		
		setBlockAndNotifyAdequately(world, i - 6, j + 3, k - 5, LOTRMod.chandelier, chandelierMeta);
		
		for (int i1 = i - 4; i1 >= i - 9; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 2, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 6, j, k - 2, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 2, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 2, Blocks.air, 0);
		
		for (int k1 = k - 1; k1 <= k + 2; k1++)
		{
			for (int i1 = i - 4; i1 >= i - 9; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorBlock, floorMeta);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, Blocks.double_stone_slab, 0);
			}
			
			setBlockAndNotifyAdequately(world, i - 4, j + 1, k1, Blocks.wooden_slab, 8);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 10, j1, k1, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k + 2, Blocks.torch, 4);
		
		int pastries = 1 + random.nextInt(5);
		for (int l = 0; l < pastries; l++)
		{
			Block block = getRandomFoodBlock(random);
			setBlockAndNotifyAdequately(world, i - 4, j + 2, (k + 2) - random.nextInt(4), block, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 9, j1, k + 3, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i - 4, j1, k - 3, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k + 2, Blocks.chest, 2);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k + 2, Blocks.chest, 2);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 1, Blocks.chest, 3);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 1, Blocks.chest, 3);
		
		LOTRChestContents.fillChest(world, random, i - 8, j + 1, k + 2, LOTRChestContents.HOBBIT_HOLE_LARDER);
		LOTRChestContents.fillChest(world, random, i - 9, j + 1, k + 2, LOTRChestContents.HOBBIT_HOLE_LARDER);
		LOTRChestContents.fillChest(world, random, i - 8, j + 1, k - 1, LOTRChestContents.HOBBIT_HOLE_LARDER);
		LOTRChestContents.fillChest(world, random, i - 9, j + 1, k - 1, LOTRChestContents.HOBBIT_HOLE_LARDER);
		
		return true;
	}
	
	private boolean generateFacingEast(World world, int i, int j, int k, Random random)
	{
		for (int i1 = i - 12; i1 >= i - 16; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				for (int k1 = k - 3; k1 <= k + 3; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i - 12; i1 >= i - 16; i1--)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				for (int j1 = j; j1 == j || (!LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0); j1--)
				{
					boolean grass = j1 == j;
					setBlockAndNotifyAdequately(world, i1, j1, k1, grass ? Blocks.grass : Blocks.dirt, 0);
				}
			}
		}
		
		for (int i1 = i - 12; i1 >= i - 15; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, fenceBlock, fenceMeta);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 3, fenceBlock, fenceMeta);
			setBlockAndNotifyAdequately(world, i1, j, k, Blocks.gravel, 0);
		}
		setBlockAndNotifyAdequately(world, i - 11, j, k, Blocks.gravel, 0);
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 15, j + 1, k1, fenceBlock, fenceMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 15, j + 1, k, Blocks.fence_gate, 1);
		
		for (int i1 = 0; i1 <= 2; i1++)
		{
			Block flower = i1 == 1 ? Blocks.red_flower : Blocks.yellow_flower;
			setBlockAndNotifyAdequately(world, i - 12 - i1, j + 1, k + 1, flower, 0);
			setBlockAndNotifyAdequately(world, i - 12 - i1, j + 1, k - 1, flower, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				if (j1 < j + 3 || Math.abs(k1 - k) < 2)
				{
					setBlockAndNotifyAdequately(world, i - 11, j1, k1, wallBlock, wallMeta);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 11, j + 1, k - 1, wallStairBlock, 3);
		setBlockAndNotifyAdequately(world, i - 11, j + 1, k, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 11, j + 1, k + 1, wallStairBlock, 2);
		setBlockAndNotifyAdequately(world, i - 11, j + 2, k - 1, wallStairBlock, 7);
		setBlockAndNotifyAdequately(world, i - 11, j + 2, k, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 11, j + 2, k + 1, wallStairBlock, 6);
		
		for (int i1 = i - 10; i1 <= i + 1; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					if (i1 == i - 10)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, wallBlock, wallMeta);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i1, j, k + 1, floorStoneBlock, floorStoneMeta);
			setBlockAndNotifyAdequately(world, i1, j, k, floorBlock, floorMeta);
			setBlockAndNotifyAdequately(world, i1, j, k - 1, floorStoneBlock, floorStoneMeta);
			
			if (i1 == i - 10)
			{
				continue;
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 2, wallHalfBlock, wallHalfMeta);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 2, wallHalfBlock, wallHalfMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 3, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k - 3, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 2, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 2, wallBlock, wallMeta);
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 1, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 1, wallHalfBlock, wallHalfMeta | 8);
			
			setBlockAndNotifyAdequately(world, i1, j + 4, k, wallBlock, wallMeta);
		}
		
        setBlockAndNotifyAdequately(world, i - 10, j + 1, k, Blocks.wooden_door, 0);
        setBlockAndNotifyAdequately(world, i - 10, j + 2, k, Blocks.wooden_door, 8);
		
		for (int i1 = i - 8; i1 <= i; i1 += 4)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k, LOTRMod.chandelier, chandelierMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 2, j1, k1, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 2, j, k, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k, Blocks.air, 0);
		
		for (int i1 = i + 3; i1 <= i + 9; i1++)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
				
				setBlockAndNotifyAdequately(world, i1, j, k1, floorBlock, floorMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j + 3, k, LOTRMod.chandelier, chandelierMeta);
		
		for (int i1 = i + 5; i1 <= i + 7; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, rugBlock, rugMeta);
			}
		}
		
		if (isWealthy && random.nextInt(4) == 0)
		{
			setBlockAndNotifyAdequately(world, i + 6, j, k, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i + 6, j, k, LOTRChestContents.HOBBIT_HOLE_TREASURE);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 10, j1, k1, wallBlock, wallMeta);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int j1 = j + 2; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 10, j1, k1, Blocks.glass_pane, 0);
				setBlockAndNotifyAdequately(world, i + 10, j1, k1, Blocks.glass_pane, 0);
				
				for (int i1 = i + 11; i1 <= i + 14; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.dirt)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.grass, 0);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i + 10, j + 4, k1, wallBlock, wallMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 3, j1, k - 3, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i + 3, j1, k + 3, wallBlock, wallMeta);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 3, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int i1 = i + 4; i1 <= i + 9; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 3, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 3, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 9, j + 1, k1, Blocks.oak_stairs, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k - 3, Blocks.oak_stairs, 3);
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k + 3, Blocks.oak_stairs, 2);
		
		for (int i1 = i + 4; i1 <= i + 9; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 4, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k + 4, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 4, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 2, k - 4, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 4, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 2, k + 4, Blocks.air, 0);
		
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 3, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 3, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k - 3, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k + 3, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 2, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 6, j, k + 2, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i - 6, j, k + 3, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 3, Blocks.wooden_door, 3);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k + 3, Blocks.wooden_door, 8);
		
		for (int i1 = i - 8; i1 <= i - 3; i1++)
		{
			for (int k1 = k + 4; k1 <= k + 8; k1++)
			{
				if (i1 == i - 8 && k1 == k + 8)
				{
					continue;
				}
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
				
				if (i1 <= i - 7 && k1 >= k + 7)
				{
					continue;
				}
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
		}
		
		for (int k1 = k + 4; k1 <= k + 7; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 2, j1, k1, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i - 8, j1, k1, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i - 7, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i - 3, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int i1 = i - 7; i1 <= i - 3; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 8, wallBlock, wallMeta);
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			for (int k1 = k + 5; k1 <= k + 6; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 8, j1, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i - 9, j1, k1, Blocks.bookshelf, 0);
			}
			
			for (int i1 = i - 6; i1 <= i - 4; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 8, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j1, k + 9, Blocks.bookshelf, 0);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 3, k + 6, LOTRMod.chandelier, chandelierMeta);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 5, Blocks.oak_stairs, 1);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 5, Blocks.wooden_slab, 8);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 7, Blocks.chest, 4);
		
		LOTRChestContents.fillChest(world, random, i - 3, j + 1, k + 7, LOTRChestContents.HOBBIT_HOLE_STUDY);
		
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 2, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 6, j, k - 2, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i - 6, j, k - 3, floorStoneBlock, floorStoneMeta);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 3, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 3, Blocks.wooden_door, 8);
		
		for (int i1 = i - 7; i1 <= i - 4; i1++)
		{
			for (int k1 = k - 4; k1 >= k - 7; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
		}
		
		for (int k1 = k - 4; k1 >= k - 7; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 8, j1, k1, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i - 3, j1, k1, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i - 7, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i - 4, j + 3, k1, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int i1 = i - 7; i1 <= i - 3; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 8, wallBlock, wallMeta);
			}
		}
		
		for (int i1 = i - 7; i1 <= i - 6; i1++)
		{
			for (int k1 = k - 5; k1 >= k - 6; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, rugBlock, rugMeta);
			}
		}
		
		for (int k1 = k - 5; k1 >= k - 6; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 8, j, k1, floorStoneBlock, floorStoneMeta);
			setBlockAndNotifyAdequately(world, i - 8, j + 1, k1, Blocks.wooden_slab, 8);
			setBlockAndNotifyAdequately(world, i - 8, j + 2, k1, Blocks.bookshelf, 0);
			setBlockAndNotifyAdequately(world, i - 9, j + 1, k1, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 7, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 4, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 7, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 5, Blocks.bed, 3);
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 5, Blocks.bed, 11);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 6, Blocks.bed, 3);
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 6, Blocks.bed, 11);
		
		setBlockAndNotifyAdequately(world, i + 6, j, k + 4, floorBlock, floorMeta);
		
		for (int k1 = k + 5; k1 <= k + 6; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 6, j, k1, floorStoneBlock, floorStoneMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 6, j1, k1, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i + 6, j + 4, k1, wallBlock, wallMeta);
		}
		
		for (int k1 = k + 5; k1 <= k + 7; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 5, j, k1, floorStoneBlock, floorStoneMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 5, j1, k1, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i + 5, j + 4, k1, wallBlock, wallMeta);
		}
		
		for (int k1 = k + 5; k1 <= k + 8; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 4, j, k1, floorStoneBlock, floorStoneMeta);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 4, j1, k1, Blocks.air, 0);
			}
			
			setBlockAndNotifyAdequately(world, i + 4, j + 4, k1, wallBlock, wallMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 6, j1, k + 7, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 8, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i + 4, j1, k + 9, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 7, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 8, Blocks.torch, 4);
		
		for (int i1 = i + 3; i1 >= i - 1; i1--)
		{
			for (int k1 = k + 4; k1 <= k + 9; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 3, wallBlock, wallMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k + 10, wallBlock, wallMeta);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 4, wallHalfBlock, wallHalfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 9, wallHalfBlock, wallHalfMeta | 8);
		}
		
		for (int i1 = i + 2; i1 >= i; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 4, Blocks.oak_stairs, 3);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 9, Blocks.oak_stairs, 2);
		}
		
		setBlockAndNotifyAdequately(world, i + 1, j + 3, k + 6, LOTRMod.chandelier, chandelierMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 3, k + 7, LOTRMod.chandelier, chandelierMeta);
		
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k + 6, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k + 7, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k + 6, Blocks.wooden_slab, 9);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k + 7, Blocks.wooden_slab, 9);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 6, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 7, Blocks.planks, 1);
		
		for (int i1 = i + 2; i1 >= i; i1--)
		{
			for (int k1 = k + 6; k1 <= k + 7; k1++)
			{
				placePlate(world, i1, j + 2, k1, random, LOTRFoods.HOBBIT);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j + 3, k + 5, wallHalfBlock, wallHalfMeta | 8);
		setBlockAndNotifyAdequately(world, i + 6, j + 3, k + 6, wallHalfBlock, wallHalfMeta | 8);
		setBlockAndNotifyAdequately(world, i + 5, j + 3, k + 7, wallHalfBlock, wallHalfMeta | 8);
		setBlockAndNotifyAdequately(world, i + 4, j + 3, k + 8, wallHalfBlock, wallHalfMeta | 8);
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int k1 = k + 5; k1 <= k + 6; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 7, j1, k1, wallBlock, wallMeta);
			}
			
			for (int k1 = k + 8; k1 <= k + 9; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 2, j1, k1, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j, k - 4, floorBlock, floorMeta);
		
		for (int i1 = i + 7; i1 >= i + 3; i1--)
		{
			for (int k1 = k - 5; k1 >= k - 7; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.double_stone_slab, 0);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, wallBlock, wallMeta);
			}
		}
		
		for (int i1 = i + 6; i1 >= i + 3; i1--)
		{
			for (int k1 = k - 5; k1 >= k - 6; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorStoneBlock, floorStoneMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k - 5, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k - 6, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k - 5, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k - 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k - 5, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k - 6, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i + 8, j + 3, k - 5, Blocks.double_stone_slab, 0);
		setBlockAndNotifyAdequately(world, i + 8, j + 3, k - 6, Blocks.double_stone_slab, 0);
		setBlockAndNotifyAdequately(world, i + 8, j + 3, k - 7, Blocks.double_stone_slab, 0);
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k - 7, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k - 7, wallBlock, wallMeta);
		
		for (int i1 = i + 6; i1 <= i + 7; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 8, wallBlock, wallMeta);
			}
		}
		
		for (int i1 = i + 3; i1 <= i + 5; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 8, Blocks.double_stone_slab, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 8, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 9, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 8, Blocks.double_stone_slab, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 8, wallBlock, wallMeta);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 8, LOTRMod.hobbitOven, 3);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 8, LOTRMod.hobbitOven, 3);
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 8, Blocks.cauldron, 3);
		
		setBlockAndNotifyAdequately(world, i + 5, j + 3, k - 6, LOTRMod.chandelier, chandelierMeta);
		
		for (int k1 = k - 4; k1 >= k - 9; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 2, j1, k1, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 2, j, k - 6, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 6, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 6, Blocks.air, 0);
		
		for (int i1 = i + 1; i1 >= i - 2; i1--)
		{
			for (int k1 = k - 4; k1 >= k - 9; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, floorBlock, floorMeta);
				
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, Blocks.double_stone_slab, 0);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 4, Blocks.wooden_slab, 8);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 10, wallBlock, wallMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 6, Blocks.torch, 1);
		
		int pastries = 1 + random.nextInt(5);
		for (int l = 0; l < pastries; l++)
		{
			Block block = getRandomFoodBlock(random);
			setBlockAndNotifyAdequately(world, (i - 2) + random.nextInt(4), j + 2, k - 4, block, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k - 9, wallBlock, wallMeta);
			setBlockAndNotifyAdequately(world, i + 3, j1, k - 4, wallBlock, wallMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 8, Blocks.chest, 5);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 9, Blocks.chest, 5);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 8, Blocks.chest, 4);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 9, Blocks.chest, 4);
		
		LOTRChestContents.fillChest(world, random, i - 2, j + 1, k - 8, LOTRChestContents.HOBBIT_HOLE_LARDER);
		LOTRChestContents.fillChest(world, random, i - 2, j + 1, k - 9, LOTRChestContents.HOBBIT_HOLE_LARDER);
		LOTRChestContents.fillChest(world, random, i + 1, j + 1, k - 8, LOTRChestContents.HOBBIT_HOLE_LARDER);
		LOTRChestContents.fillChest(world, random, i + 1, j + 1, k - 9, LOTRChestContents.HOBBIT_HOLE_LARDER);
		
		return true;
	}
	
	public static Block getRandomFoodBlock(Random random)
	{
		int i = random.nextInt(3);
		if (i == 0)
		{
			return Blocks.cake;
		}
		else if (i == 1)
		{
			return LOTRMod.appleCrumble;
		}
		else if (i == 2)
		{
			return LOTRMod.cherryPie;
		}
		return Blocks.cake;
	}
}
