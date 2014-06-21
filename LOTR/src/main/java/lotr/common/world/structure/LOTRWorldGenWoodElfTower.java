package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityDwarf;
import lotr.common.entity.npc.LOTREntityGundabadOrc;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTREntityWoodElf;
import lotr.common.entity.npc.LOTREntityWoodElfCaptain;
import lotr.common.entity.npc.LOTREntityWoodElfScout;
import lotr.common.entity.npc.LOTREntityWoodElfWarrior;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.feature.LOTRWorldGenMirkOak;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenWoodElfTower extends LOTRWorldGenStructureBase
{
	private WorldGenerator treeGen = new LOTRWorldGenMirkOak(true, 10, 1, 0, 4).disableDecay().disableRestrictions();
	
	public LOTRWorldGenWoodElfTower(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass || world.getBiomeGenForCoords(i, k) != LOTRBiome.mirkwood)
			{
				return false;
			}
		}
		
		j--;
		
		int rotation = random.nextInt(4);
		int radius = 6;
		int radiusPlusOne = radius + 1;
		
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
		}
		
		switch (rotation)
		{
			case 0:
				k += radiusPlusOne;
				break;
			case 1:
				i -= radiusPlusOne;
				break;
			case 2:
				k -= radiusPlusOne;
				break;
			case 3:
				i += radiusPlusOne;
				break;
		}
		
		if (restrictions)
		{
			int minHeight = j;
			int maxHeight = j;
			
			for (int i1 = i - radiusPlusOne; i1 <= i + radiusPlusOne; i1++)
			{
				for (int k1 = k - radiusPlusOne; k1 <= k + radiusPlusOne; k1++)
				{
					int i2 = i1 - i;
					int k2 = k1 - k;
					if (i2 * i2 + k2 * k2 > radiusPlusOne * radiusPlusOne)
					{
						continue;
					}
					
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					Block block = world.getBlock(i1, j1, k1);
					if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone && !block.isWood(world, i1, j1, k1) && !block.isLeaves(world, i1, j1, k1))
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
					
					if (maxHeight - minHeight > 8)
					{
						return false;
					}
				}
			}
		}
		
		int sections = 3 + random.nextInt(3);
		int sectionHeight = 8;
		int topHeight = j + sections * sectionHeight;
		
		int wallThresholdMin = radius * radius;
		int wallThresholdMax = radiusPlusOne * radiusPlusOne;
		
		for (int i1 = i - radius; i1 <= i + radius; i1++)
		{
			for (int k1 = k - radius; k1 <= k + radius; k1++)
			{
				int i2 = i1 - i;
				int k2 = k1 - k;
				int distSq = i2 * i2 + k2 * k2;
				if (distSq >= wallThresholdMax)
				{
					continue;
				}
				else
				{
					int start = j - sectionHeight;
					for (int j1 = start; (j1 == start || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
					{
						if (j1 != start || distSq >= wallThresholdMin)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 11);
						}
						else
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.planks, 2);
						}

						if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
						{
							setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
						}
					}
				}
			}
		}
		
		for (int l = -1; l < sections; l++)
		{
			int sectionBase = j + l * sectionHeight;
			for (int j1 = sectionBase + 1; j1 <= sectionBase + sectionHeight; j1++)
			{
				for (int i1 = i - radius; i1 <= i + radius; i1++)
				{
					for (int k1 = k - radius; k1 <= k + radius; k1++)
					{
						int i2 = i1 - i;
						int k2 = k1 - k;
						int distSq = i2 * i2 + k2 * k2;
						if (distSq >= wallThresholdMax)
						{
							continue;
						}
						else
						{
							if (distSq >= wallThresholdMin)
							{
								setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 11);
								if (l == sections - 1 && j1 == sectionBase + sectionHeight)
								{
									setBlockAndNotifyAdequately(world, i1, j1 + 1, k1, LOTRMod.brick, 11);
									setBlockAndNotifyAdequately(world, i1, j1 + 2, k1, LOTRMod.slabSingle2, 3);
								}
							}
							else
							{
								if (j1 == sectionBase + sectionHeight && (Math.abs(i2) > 2 || Math.abs(k2) > 2))
								{
									setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.planks, 2);
								}
								else
								{
									setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
								}
							}
							
							if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
							{
								setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
							}
						}
					}
				}
				
				setBlockAndNotifyAdequately(world, i, j1, k, LOTRMod.wood, 2);
			}
			
			for (int l1 = 0; l1 < 2; l1++)
			{
				int stairBase = sectionBase + l1 * 4;

				setBlockAndNotifyAdequately(world, i - 4, sectionBase + 2, k - 4, LOTRMod.fence, 2);
				
				setBlockAndNotifyAdequately(world, i, stairBase + 1, k + 1, LOTRMod.slabSingle2, 3);
				setBlockAndNotifyAdequately(world, i, stairBase + 1, k + 2, LOTRMod.slabSingle2, 3);
				
				setBlockAndNotifyAdequately(world, i + 1, stairBase + 2, k, LOTRMod.slabSingle2, 3);
				setBlockAndNotifyAdequately(world, i + 2, stairBase + 2, k, LOTRMod.slabSingle2, 3);
				
				setBlockAndNotifyAdequately(world, i, stairBase + 3, k - 1, LOTRMod.slabSingle2, 3);
				setBlockAndNotifyAdequately(world, i, stairBase + 3, k - 2, LOTRMod.slabSingle2, 3);
				
				setBlockAndNotifyAdequately(world, i - 1, stairBase + 4, k, LOTRMod.slabSingle2, 3);
				setBlockAndNotifyAdequately(world, i - 2, stairBase + 4, k, LOTRMod.slabSingle2, 3);
				
				for (int i1 = 0; i1 <= 1; i1++)
				{
					for (int k1 = 0; k1 <= 1; k1++)
					{
						setBlockAndNotifyAdequately(world, i + 1 + i1, stairBase + 1, k + 1 + k1, LOTRMod.slabSingle2, 11);
						
						setBlockAndNotifyAdequately(world, i + 1 + i1, stairBase + 2, k - 2 + k1, LOTRMod.slabSingle2, 11);
						
						setBlockAndNotifyAdequately(world, i - 2 + i1, stairBase + 3, k - 2 + k1, LOTRMod.slabSingle2, 11);
						
						setBlockAndNotifyAdequately(world, i - 2 + i1, stairBase + 4, k + 1 + k1, LOTRMod.slabSingle2, 11);
					}
				}
				
				setBlockAndNotifyAdequately(world, i - 1, stairBase + 2, k, LOTRMod.woodElvenTorch, 2);
				setBlockAndNotifyAdequately(world, i + 1, stairBase + 4, k, LOTRMod.woodElvenTorch, 1);
			}
			
			setBlockAndNotifyAdequately(world, i - 4, sectionBase + 2, k - 4, LOTRMod.fence, 2);
			setBlockAndNotifyAdequately(world, i - 4, sectionBase + 3, k - 4, LOTRMod.woodElvenTorch, 5);
			
			setBlockAndNotifyAdequately(world, i - 4, sectionBase + 2, k + 4, LOTRMod.fence, 2);
			setBlockAndNotifyAdequately(world, i - 4, sectionBase + 3, k + 4, LOTRMod.woodElvenTorch, 5);
			
			setBlockAndNotifyAdequately(world, i + 4, sectionBase + 2, k - 4, LOTRMod.fence, 2);
			setBlockAndNotifyAdequately(world, i + 4, sectionBase + 3, k - 4, LOTRMod.woodElvenTorch, 5);
			
			setBlockAndNotifyAdequately(world, i + 4, sectionBase + 2, k + 4, LOTRMod.fence, 2);
			setBlockAndNotifyAdequately(world, i + 4, sectionBase + 3, k + 4, LOTRMod.woodElvenTorch, 5);
			
			if (l > 0)
			{
				for (int j1 = sectionBase + 1; j1 <= sectionBase + 4; j1++)
				{
					for (int i1 = i - 1; i1 <= i + 1; i1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k - 6, Blocks.air, 0);
						setBlockAndNotifyAdequately(world, i1, j1, k + 6, Blocks.air, 0);
					}
					
					for (int k1 = k - 1; k1 <= k + 1; k1++)
					{
						setBlockAndNotifyAdequately(world, i - 6, j1, k1, Blocks.air, 0);
						setBlockAndNotifyAdequately(world, i + 6, j1, k1, Blocks.air, 0);
					}
				}
				
				setBlockAndNotifyAdequately(world, i - 1, sectionBase + 4, k - 6, LOTRMod.stairsElvenBrick, 5);
				setBlockAndNotifyAdequately(world, i + 1, sectionBase + 4, k - 6, LOTRMod.stairsElvenBrick, 4);
				
				setBlockAndNotifyAdequately(world, i - 1, sectionBase + 4, k + 6, LOTRMod.stairsElvenBrick, 5);
				setBlockAndNotifyAdequately(world, i + 1, sectionBase + 4, k + 6, LOTRMod.stairsElvenBrick, 4);
				
				setBlockAndNotifyAdequately(world, i - 6, sectionBase + 4, k - 1, LOTRMod.stairsElvenBrick, 7);
				setBlockAndNotifyAdequately(world, i - 6, sectionBase + 4, k + 1, LOTRMod.stairsElvenBrick, 6);
				
				setBlockAndNotifyAdequately(world, i + 6, sectionBase + 4, k - 1, LOTRMod.stairsElvenBrick, 7);
				setBlockAndNotifyAdequately(world, i + 6, sectionBase + 4, k + 1, LOTRMod.stairsElvenBrick, 6);
				
				for (int i1 = i - 2; i1 <= i + 2; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, sectionBase, k - 8, LOTRMod.stairsMirkOak, 6);
					setBlockAndNotifyAdequately(world, i1, sectionBase + 1, k - 8, LOTRMod.fence, 2);
					
					setBlockAndNotifyAdequately(world, i1, sectionBase, k + 8, LOTRMod.stairsMirkOak, 7);
					setBlockAndNotifyAdequately(world, i1, sectionBase + 1, k + 8, LOTRMod.fence, 2);
				}
				
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i - 8, sectionBase, k1, LOTRMod.stairsMirkOak, 4);
					setBlockAndNotifyAdequately(world, i - 8, sectionBase + 1, k1, LOTRMod.fence, 2);
					
					setBlockAndNotifyAdequately(world, i + 8, sectionBase, k1, LOTRMod.stairsMirkOak, 5);
					setBlockAndNotifyAdequately(world, i + 8, sectionBase + 1, k1, LOTRMod.fence, 2);
				}
				
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, sectionBase, k - 7, LOTRMod.planks, 2);
					setBlockAndNotifyAdequately(world, i1, sectionBase, k + 7, LOTRMod.planks, 2);
				}
				
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i - 7, sectionBase, k1, LOTRMod.planks, 2);
					setBlockAndNotifyAdequately(world, i + 7, sectionBase, k1, LOTRMod.planks, 2);
				}
				
				setBlockAndNotifyAdequately(world, i - 7, sectionBase, k - 2, LOTRMod.stairsMirkOak, 6);
				setBlockAndNotifyAdequately(world, i - 7, sectionBase + 1, k - 2, LOTRMod.fence, 2);
				setBlockAndNotifyAdequately(world, i - 8, sectionBase + 2, k - 2, LOTRMod.woodElvenTorch, 5);
				setBlockAndNotifyAdequately(world, i - 7, sectionBase, k + 2, LOTRMod.stairsMirkOak, 7);
				setBlockAndNotifyAdequately(world, i - 7, sectionBase + 1, k + 2, LOTRMod.fence, 2);
				setBlockAndNotifyAdequately(world, i - 8, sectionBase + 2, k + 2, LOTRMod.woodElvenTorch, 5);
				
				setBlockAndNotifyAdequately(world, i + 7, sectionBase, k - 2, LOTRMod.stairsMirkOak, 6);
				setBlockAndNotifyAdequately(world, i + 7, sectionBase + 1, k - 2, LOTRMod.fence, 2);
				setBlockAndNotifyAdequately(world, i + 8, sectionBase + 2, k - 2, LOTRMod.woodElvenTorch, 5);
				setBlockAndNotifyAdequately(world, i + 7, sectionBase, k + 2, LOTRMod.stairsMirkOak, 7);
				setBlockAndNotifyAdequately(world, i + 7, sectionBase + 1, k + 2, LOTRMod.fence, 2);
				setBlockAndNotifyAdequately(world, i + 8, sectionBase + 2, k + 2, LOTRMod.woodElvenTorch, 5);
				
				setBlockAndNotifyAdequately(world, i - 2, sectionBase, k - 7, LOTRMod.stairsMirkOak, 4);
				setBlockAndNotifyAdequately(world, i - 2, sectionBase + 1, k - 7, LOTRMod.fence, 2);
				setBlockAndNotifyAdequately(world, i - 2, sectionBase + 2, k - 8, LOTRMod.woodElvenTorch, 5);
				setBlockAndNotifyAdequately(world, i + 2, sectionBase, k - 7, LOTRMod.stairsMirkOak, 5);
				setBlockAndNotifyAdequately(world, i + 2, sectionBase + 1, k - 7, LOTRMod.fence, 2);
				setBlockAndNotifyAdequately(world, i + 2, sectionBase + 2, k - 8, LOTRMod.woodElvenTorch, 5);
				
				setBlockAndNotifyAdequately(world, i - 2, sectionBase, k + 7, LOTRMod.stairsMirkOak, 4);
				setBlockAndNotifyAdequately(world, i - 2, sectionBase + 1, k + 7, LOTRMod.fence, 2);
				setBlockAndNotifyAdequately(world, i - 2, sectionBase + 2, k + 8, LOTRMod.woodElvenTorch, 5);
				setBlockAndNotifyAdequately(world, i + 2, sectionBase, k + 7, LOTRMod.stairsMirkOak, 5);
				setBlockAndNotifyAdequately(world, i + 2, sectionBase + 1, k + 7, LOTRMod.fence, 2);
				setBlockAndNotifyAdequately(world, i + 2, sectionBase + 2, k + 8, LOTRMod.woodElvenTorch, 5);
			}
			
			LOTREntityWoodElf woodElf = random.nextInt(3) == 0 ? new LOTREntityWoodElfScout(world) : new LOTREntityWoodElfWarrior(world);
			woodElf.setLocationAndAngles(i - 3 + 0.5D, sectionBase + 1, k - 3 + 0.5D, world.rand.nextFloat() * 360F, 0F);
			woodElf.onSpawnWithEgg(null);
			woodElf.setHomeArea(i, sectionBase + 1, k, 12);
			woodElf.isNPCPersistent = true;
			world.spawnEntityInWorld(woodElf);
		}
		
		treeGen.generate(world, random, i, topHeight, k);
		
		for (int j1 = topHeight + 2; j1 <= topHeight + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 6, j1, k - 3, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i + 6, j1, k, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i + 6, j1, k + 3, LOTRMod.brick, 11);
			
			setBlockAndNotifyAdequately(world, i - 3, j1, k + 6, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i, j1, k + 6, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i + 3, j1, k + 6, LOTRMod.brick, 11);
			
			setBlockAndNotifyAdequately(world, i - 6, j1, k - 3, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i - 6, j1, k, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i - 6, j1, k + 3, LOTRMod.brick, 11);
			
			setBlockAndNotifyAdequately(world, i - 3, j1, k - 6, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i, j1, k - 6, LOTRMod.brick, 11);
			setBlockAndNotifyAdequately(world, i + 3, j1, k - 6, LOTRMod.brick, 11);
		}
		
		setBlockAndNotifyAdequately(world, i + 6, topHeight + 2, k - 2, LOTRMod.brick, 11);
		setBlockAndNotifyAdequately(world, i + 6, topHeight + 2, k + 2, LOTRMod.brick, 11);
		
		setBlockAndNotifyAdequately(world, i - 2, topHeight + 2, k + 6, LOTRMod.brick, 11);
		setBlockAndNotifyAdequately(world, i + 2, topHeight + 2, k + 6, LOTRMod.brick, 11);
		
		setBlockAndNotifyAdequately(world, i - 6, topHeight + 2, k - 2, LOTRMod.brick, 11);
		setBlockAndNotifyAdequately(world, i - 6, topHeight + 2, k + 2, LOTRMod.brick, 11);
		
		setBlockAndNotifyAdequately(world, i - 2, topHeight + 2, k - 6, LOTRMod.brick, 11);
		setBlockAndNotifyAdequately(world, i + 2, topHeight + 2, k - 6, LOTRMod.brick, 11);
		
		ItemStack bow = new ItemStack(LOTRMod.mirkwoodBow);
		ItemStack arrow = new ItemStack(Items.arrow);
		ItemStack[] armor = new ItemStack[] {new ItemStack(LOTRMod.helmetWoodElvenScout), new ItemStack(LOTRMod.bodyWoodElvenScout), new ItemStack(LOTRMod.legsWoodElvenScout), new ItemStack(LOTRMod.bootsWoodElvenScout)};
		switch (rotation)
		{
			case 0:
				placeArmorStand(world, i, topHeight + 1, k + 5, 0, armor);
				spawnItemFrame(world, i + 6, topHeight + 2, k, 1, arrow);
				spawnItemFrame(world, i, topHeight + 2, k - 6, 0, bow);
				spawnItemFrame(world, i - 6, topHeight + 2, k, 3, arrow);
				break;
			case 1:
				spawnItemFrame(world, i, topHeight + 2, k + 6, 2, arrow);
				spawnItemFrame(world, i + 6, topHeight + 2, k, 1, bow);
				spawnItemFrame(world, i, topHeight + 2, k - 6, 0, arrow);
				placeArmorStand(world, i - 5, topHeight + 1, k, 1, armor);
				break;
			case 2:
				spawnItemFrame(world, i, topHeight + 2, k + 6, 2, bow);
				spawnItemFrame(world, i + 6, topHeight + 2, k, 1, arrow);
				placeArmorStand(world, i, topHeight + 1, k - 5, 2, armor);
				spawnItemFrame(world, i - 6, topHeight + 2, k, 3, arrow);
				break;
			case 3:
				spawnItemFrame(world, i, topHeight + 2, k + 6, 2, arrow);
				placeArmorStand(world, i + 5, topHeight + 1, k, 3, armor);
				spawnItemFrame(world, i, topHeight + 2, k - 6, 0, arrow);
				spawnItemFrame(world, i - 6, topHeight + 2, k, 3, bow);
				break;
		}
		
		placeWallBanner(world, i, topHeight + 1, k + 6, 0, 4);
		placeWallBanner(world, i - 6, topHeight + 1, k, 1, 4);
		placeWallBanner(world, i, topHeight + 1, k - 6, 2, 4);
		placeWallBanner(world, i + 6, topHeight + 1, k, 3, 4);
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - sectionHeight + 1, k - 5, Blocks.wooden_slab, 8);
			setBlockAndNotifyAdequately(world, i1, j - sectionHeight + 1, k + 5, Blocks.wooden_slab, 8);
			
			if (random.nextBoolean())
			{
				setBlockAndNotifyAdequately(world, i1, j - sectionHeight + 2, k + 5, LOTRMod.mugBlock, 0);
			}
			
			if (Math.abs(i1 - i) > 1)
			{
				placeBarrel(world, random, i1, j - sectionHeight + 2, k - 5, 3, LOTRMod.mugRedWine);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j - sectionHeight + 1, k - 5, LOTRMod.woodElvenTable, 0);
		setBlockAndNotifyAdequately(world, i + 1, j - sectionHeight + 1, k - 5, LOTRMod.woodElvenTable, 0);
		setBlockAndNotifyAdequately(world, i, j - sectionHeight + 1, k - 5, Blocks.chest, 0);
		LOTRChestContents.fillChest(world, random, i, j - sectionHeight + 1, k - 5, LOTRChestContents.WOOD_ELF_HOUSE);
		
		for (int i1 = i + 4; i1 <= i + 5; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - sectionHeight + 1, k - 3, Blocks.oak_stairs, 3);
			setBlockAndNotifyAdequately(world, i1, j - sectionHeight + 1, k - 1, Blocks.planks, 0);
			placeMug(world, random, i1, j - sectionHeight + 2, k - 1, 0, LOTRMod.mugRedWine);
			setBlockAndNotifyAdequately(world, i1, j - sectionHeight + 1, k, Blocks.wooden_slab, 8);
			placePlateWithCertainty(world, i1, j - sectionHeight + 2, k, random, LOTRFoods.ELF);
			setBlockAndNotifyAdequately(world, i1, j - sectionHeight + 1, k + 1, Blocks.planks, 0);
			placeMug(world, random, i1, j - sectionHeight + 2, k + 1, 2, LOTRMod.mugRedWine);
			setBlockAndNotifyAdequately(world, i1, j - sectionHeight + 1, k + 3, Blocks.oak_stairs, 2);
		}
		
		setBlockAndNotifyAdequately(world, i + 4, j - sectionHeight + 1, k - 4, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 4, j - sectionHeight + 1, k + 4, Blocks.planks, 0);
		
		for (int j1 = j - sectionHeight - 6; j1 <= j - sectionHeight - 1; j1++)
		{
			placeDungeonBlock(world, random, i - 6, j1, k);
			
			placeDungeonBlock(world, random, i - 5, j1, k - 2);
			placeDungeonBlock(world, random, i - 5, j1, k - 1);
			placeDungeonBlock(world, random, i - 5, j1, k + 1);
			placeDungeonBlock(world, random, i - 5, j1, k + 2);
			
			placeDungeonBlock(world, random, i - 4, j1, k - 3);
			placeDungeonBlock(world, random, i - 4, j1, k + 3);
			
			placeDungeonBlock(world, random, i - 3, j1, k - 5);
			placeDungeonBlock(world, random, i - 3, j1, k - 4);
			placeDungeonBlock(world, random, i - 3, j1, k + 4);
			placeDungeonBlock(world, random, i - 3, j1, k + 5);
			
			placeDungeonBlock(world, random, i - 2, j1, k - 6);
			placeDungeonBlock(world, random, i - 2, j1, k + 6);
			
			placeDungeonBlock(world, random, i - 1, j1, k - 6);
			placeDungeonBlock(world, random, i - 1, j1, k + 6);
			
			placeDungeonBlock(world, random, i, j1, k - 6);
			placeDungeonBlock(world, random, i, j1, k + 6);
			
			placeDungeonBlock(world, random, i + 1, j1, k - 5);
			placeDungeonBlock(world, random, i + 1, j1, k - 4);
			placeDungeonBlock(world, random, i + 1, j1, k + 4);
			placeDungeonBlock(world, random, i + 1, j1, k + 5);
			
			placeDungeonBlock(world, random, i + 2, j1, k - 3);
			placeDungeonBlock(world, random, i + 2, j1, k + 3);
			
			placeDungeonBlock(world, random, i + 3, j1, k - 2);
			placeDungeonBlock(world, random, i + 3, j1, k + 2);
			
			placeDungeonBlock(world, random, i + 4, j1, k - 2);
			placeDungeonBlock(world, random, i + 4, j1, k + 2);
			
			placeDungeonBlock(world, random, i + 5, j1, k - 1);
			placeDungeonBlock(world, random, i + 5, j1, k);
			placeDungeonBlock(world, random, i + 5, j1, k + 1);
			
			if (j1 == j - sectionHeight - 6 || j1 == j - sectionHeight - 1)
			{
				placeDungeonBlock(world, random, i - 5, j1, k);
				
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					placeDungeonBlock(world, random, i - 4, j1, k1);
				}
				
				for (int k1 = k - 3; k1 <= k + 3; k1++)
				{
					placeDungeonBlock(world, random, i - 3, j1, k1);
				}
				
				for (int k1 = k - 5; k1 <= k + 5; k1++)
				{
					placeDungeonBlock(world, random, i - 2, j1, k1);
					placeDungeonBlock(world, random, i - 1, j1, k1);
					placeDungeonBlock(world, random, i, j1, k1);
				}
				
				for (int k1 = k - 3; k1 <= k + 3; k1++)
				{
					placeDungeonBlock(world, random, i + 1, j1, k1);
				}
				
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					placeDungeonBlock(world, random, i + 2, j1, k1);
				}
				
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					placeDungeonBlock(world, random, i + 3, j1, k1);
					placeDungeonBlock(world, random, i + 4, j1, k1);
				}
			}
			else
			{
				setBlockAndNotifyAdequately(world, i - 5, j1, k, Blocks.air, 0);
				
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i - 4, j1, k1, Blocks.air, 0);
				}
				
				for (int k1 = k - 3; k1 <= k + 3; k1++)
				{
					setBlockAndNotifyAdequately(world, i - 3, j1, k1, Blocks.air, 0);
				}
				
				for (int k1 = k - 5; k1 <= k + 5; k1++)
				{
					setBlockAndNotifyAdequately(world, i - 2, j1, k1, Blocks.air, 0);
					setBlockAndNotifyAdequately(world, i - 1, j1, k1, Blocks.air, 0);
					setBlockAndNotifyAdequately(world, i, j1, k1, Blocks.air, 0);
				}
				
				for (int k1 = k - 3; k1 <= k + 3; k1++)
				{
					setBlockAndNotifyAdequately(world, i + 1, j1, k1, Blocks.air, 0);
				}
				
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i + 2, j1, k1, Blocks.air, 0);
				}
				
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i + 3, j1, k1, Blocks.air, 0);
					setBlockAndNotifyAdequately(world, i + 4, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i - 2; i1 <= i; i1++)
		{
			placeDungeonBlock(world, random, i1, j - sectionHeight - 2, k - 5);
			placeDungeonBlock(world, random, i1, j - sectionHeight - 2, k - 4);
			placeDungeonBlock(world, random, i1, j - sectionHeight - 2, k + 4);
			placeDungeonBlock(world, random, i1, j - sectionHeight - 2, k + 5);
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			placeDungeonBlock(world, random, i + 3, j - sectionHeight - 2, k1);
			placeDungeonBlock(world, random, i + 4, j - sectionHeight - 2, k1);
		}
		
		for (int j1 = j - sectionHeight - 5; j1 <= j - sectionHeight - 3; j1++)
		{
			for (int i1 = i - 2; i1 <= i; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 4, Blocks.iron_bars, 0);
				setBlockAndNotifyAdequately(world, i1, j1, k + 4, Blocks.iron_bars, 0);
			}
			
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 3, j1, k1, Blocks.iron_bars, 0);
			}
		}
		
		placePrisoner(world, random, i - 2, j - sectionHeight - 5, k - 5, 3, 1);
		placePrisoner(world, random, i - 2, j - sectionHeight - 5, k + 5, 3, 1);
		placePrisoner(world, random, i + 4, j - sectionHeight - 5, k - 1, 1, 3);
		
		setBlockAndNotifyAdequately(world, i - 4, j - sectionHeight - 3, k - 1, LOTRMod.woodElvenTorch, 1);
		setBlockAndNotifyAdequately(world, i - 4, j - sectionHeight - 3, k + 1, LOTRMod.woodElvenTorch, 1);
		
		for (int j1 = j - sectionHeight - 5; j1 <= j - sectionHeight; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j1, k, Blocks.ladder, 5);
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j - sectionHeight + 1, k, Blocks.trapdoor, 3);
		
		int i1;
		int j1;
		int k1;
		switch (rotation)
		{
			case 0:
				k1 = k - radius;
				for (i1 = i - 1; i1 <= i + 1; i1++)
				{
					for (j1 = j + 1; j1 <= j + 3; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.planks, 2);
					}
				}
				setBlockAndNotifyAdequately(world, i, j + 1, k1, Blocks.wooden_door, 1);
				setBlockAndNotifyAdequately(world, i, j + 2, k1, Blocks.wooden_door, 8);
				placeWallBanner(world, i, j + 6, k1, 2, 4);
				break;
			case 1:
				i1 = i + radius;
				for (k1 = k - 1; k1 <= k + 1; k1++)
				{
					for (j1 = j + 1; j1 <= j + 3; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.planks, 2);
					}
				}
				setBlockAndNotifyAdequately(world, i1, j + 1, k, Blocks.wooden_door, 2);
				setBlockAndNotifyAdequately(world, i1, j + 2, k, Blocks.wooden_door, 8);
				placeWallBanner(world, i1, j + 6, k, 3, 4);
				break;
			case 2:
				k1 = k + radius;
				for (i1 = i - 1; i1 <= i + 1; i1++)
				{
					for (j1 = j + 1; j1 <= j + 3; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.planks, 2);
					}
				}
				setBlockAndNotifyAdequately(world, i, j + 1, k1, Blocks.wooden_door, 3);
				setBlockAndNotifyAdequately(world, i, j + 2, k1, Blocks.wooden_door, 8);
				placeWallBanner(world, i, j + 6, k1, 0, 4);
				break;
			case 3:
				i1 = i - radius;
				for (k1 = k - 1; k1 <= k + 1; k1++)
				{
					for (j1 = j + 1; j1 <= j + 3; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.planks, 2);
					}
				}
				setBlockAndNotifyAdequately(world, i1, j + 1, k, Blocks.wooden_door, 0);
				setBlockAndNotifyAdequately(world, i1, j + 2, k, Blocks.wooden_door, 8);
				placeWallBanner(world, i1, j + 6, k, 1, 4);
				break;
		}
		
		LOTREntityWoodElfCaptain woodElfCaptain = new LOTREntityWoodElfCaptain(world);
		woodElfCaptain.setLocationAndAngles(i - 3 + 0.5D, topHeight + 1, k - 3 + 0.5D, world.rand.nextFloat() * 360F, 0F);
		woodElfCaptain.onSpawnWithEgg(null);
		woodElfCaptain.setHomeArea(i, topHeight, k, 16);
		world.spawnEntityInWorld(woodElfCaptain);
		
		if (usingPlayer == null)
		{
			LOTRLevelData.woodElvenTowerLocations.add(new ChunkCoordinates(i, j, k));
		}
		
		return true;
	}
	
	private void placeDungeonBlock(World world, Random random, int i, int j, int k)
	{
		int l = random.nextInt(3);
		switch (l)
		{
			case 0:
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 11);
				break;
			case 1:
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 12);
				break;
			case 2:
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 13);
				break;
		}
	}
	
	private void placePrisoner(World world, Random random, int i, int j, int k, int xRange, int zRange)
	{
		i += random.nextInt(xRange);
		k += random.nextInt(zRange);
		
		if (random.nextBoolean())
		{
			if (random.nextInt(3) == 0)
			{
				LOTREntityNPC npc;
				
				if (random.nextInt(50) == 0)
				{
					npc = new LOTREntityDwarf(world);
				}
				else
				{
					npc = new LOTREntityGundabadOrc(world);
				}
				
				npc.setLocationAndAngles(i + 0.5D, j, k + 0.5D, 0F, 0F);
				npc.spawnRidingHorse = false;
				npc.onSpawnWithEgg(null);
				for (int l = 0; l < 5; l++)
				{
					npc.setCurrentItemOrArmor(l, null);
				}
				npc.isNPCPersistent = true;
				world.spawnEntityInWorld(npc);
			}
			else
			{
				placeSkull(world, random, i, j, k);
			}
		}
	}
}
