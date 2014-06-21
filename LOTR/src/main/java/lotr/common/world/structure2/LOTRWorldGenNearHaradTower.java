package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNearHaradrim;
import lotr.common.entity.npc.LOTREntityNearHaradrimArcher;
import lotr.common.entity.npc.LOTREntityNearHaradrimWarlord;
import lotr.common.entity.npc.LOTREntityNearHaradrimWarrior;
import lotr.common.entity.npc.LOTREntityWoodElf;
import lotr.common.entity.npc.LOTREntityWoodElfCaptain;
import lotr.common.entity.npc.LOTREntityWoodElfScout;
import lotr.common.entity.npc.LOTREntityWoodElfWarrior;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class LOTRWorldGenNearHaradTower extends LOTRWorldGenStructureBase2
{
	public LOTRWorldGenNearHaradTower(boolean flag)
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
		
		int radius = 6;
		int radiusPlusOne = radius + 1;
		
		setRotationMode(rotation);
		
		switch (getRotationMode())
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
		
		setOrigin(i, j, k);
		
		if (restrictions)
		{
			int minHeight = j;
			int maxHeight = j;
			
			for (int i1 = -radiusPlusOne; i1 <= radiusPlusOne; i1++)
			{
				for (int k1 = -radiusPlusOne; k1 <=radiusPlusOne; k1++)
				{
					if (i1 * i1 + k1 * k1 > radiusPlusOne * radiusPlusOne)
					{
						continue;
					}
					
					int j1 = getTopBlock(world, i1, k1) - 1;
					Block block = getBlock(world, i1, j1, k1);
					if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.sand)
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
		
		int sections = 3 + random.nextInt(2);
		int sectionHeight = 8;
		int topHeight = sections * sectionHeight;
		
		int wallThresholdMin = radius * radius;
		int wallThresholdMax = radiusPlusOne * radiusPlusOne;
		
		for (int i1 = -radius; i1 <= radius; i1++)
		{
			for (int k1 = -radius; k1 <= radius; k1++)
			{
				int distSq = i1 * i1 + k1 * k1;
				if (distSq >= wallThresholdMax)
				{
					continue;
				}
				else
				{
					for (int j1 = 0; (j1 == 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; j1--)
					{
						if (j1 > 0 || distSq >= wallThresholdMin)
						{
							setBlockAndMetadata(world, i1, j1, k1, LOTRMod.brick, 15);
						}
						else
						{
							setBlockAndMetadata(world, i1, j1, k1, Blocks.sandstone, 2);
						}

						setGrassToDirt(world, i1, j1 - 1, k1);
					}
				}
			}
		}
		
		for (int l = 0; l < sections; l++)
		{
			int sectionBase = l * sectionHeight;
			for (int j1 = sectionBase + 1; j1 <= sectionBase + sectionHeight; j1++)
			{
				for (int i1 = -radius; i1 <= radius; i1++)
				{
					for (int k1 = -radius; k1 <= radius; k1++)
					{
						int distSq = i1 * i1 + k1 * k1;
						if (distSq >= wallThresholdMax)
						{
							continue;
						}
						else
						{
							if (distSq >= wallThresholdMin)
							{
								int i2 = Math.abs(i1);
								int k2 = Math.abs(k1);
								if ((i2 == 3 && k2 == 6) || (k2 == 3 && i2 == 6))
								{
									setBlockAndMetadata(world, i1, j1, k1, Blocks.hardened_clay, 0);
								}
								else if ((j1 - sectionBase) % (sectionHeight / 2) == 0)
								{
									if (j1 != sectionBase + sectionHeight && ((i2 == 1 && k2 == 6) || (k2 == 1 && i2 == 6)))
									{
										setBlockAndMetadata(world, i1, j1, k1, LOTRMod.slabSingle4, 8);
										if (l > 0)
										{
											setAir(world, i1, j1 - 1, k1);
											setBlockAndMetadata(world, i1, j1 - 2, k1, Blocks.sandstone, 2);
										}
									}
									else
									{
										setBlockAndMetadata(world, i1, j1, k1, Blocks.sandstone, 2);
									}
								}	
								else
								{
									setBlockAndMetadata(world, i1, j1, k1, LOTRMod.brick, 15);
								}
								
								if (l == sections - 1 && j1 == sectionBase + sectionHeight)
								{
									setBlockAndMetadata(world, i1, j1 + 1, k1, LOTRMod.brick, 15);
									setBlockAndMetadata(world, i1, j1 + 2, k1, LOTRMod.wall, 15);
									if ((i2 == 3 && k2 == 6) || (k2 == 3 && i2 == 6))
									{
										setBlockAndMetadata(world, i1, j1 + 3, k1, LOTRMod.wall, 15);
										setBlockAndMetadata(world, i1, j1 + 4, k1, LOTRMod.wall, 15);
										setBlockAndMetadata(world, i1 - 1, j1 + 4, k1, LOTRMod.wall, 15);
										setBlockAndMetadata(world, i1 + 1, j1 + 4, k1, LOTRMod.wall, 15);
										setBlockAndMetadata(world, i1, j1 + 4, k1 - 1, LOTRMod.wall, 15);
										setBlockAndMetadata(world, i1, j1 + 4, k1 + 1, LOTRMod.wall, 15);
										setBlockAndMetadata(world, i1, j1 + 5, k1, LOTRMod.hearth, 0);
										setBlockAndMetadata(world, i1, j1 + 6, k1, Blocks.fire, 0);
									}
								}
							}
							else
							{
								if (j1 == sectionBase + sectionHeight && (Math.abs(i1) > 2 || Math.abs(k1) > 2))
								{
									setBlockAndMetadata(world, i1, j1, k1, Blocks.sandstone, 2);
								}
								else
								{
									setAir(world, i1, j1, k1);
								}
							}
							
							setGrassToDirt(world, i1, j1 - 1, k1);
						}
					}
				}
				
				setBlockAndMetadata(world, 0, j1, 0, Blocks.hardened_clay, 0);
			}
			
			for (int l1 = 0; l1 < 2; l1++)
			{
				int stairBase = sectionBase + l1 * 4;
				
				setBlockAndMetadata(world, 0, stairBase + 1, 1, LOTRMod.slabSingle4, 0);
				setBlockAndMetadata(world, 0, stairBase + 1, 2, LOTRMod.slabSingle4, 0);
				
				setBlockAndMetadata(world, 1, stairBase + 2, 0, LOTRMod.slabSingle4, 0);
				setBlockAndMetadata(world, 2, stairBase + 2, 0, LOTRMod.slabSingle4, 0);
				
				setBlockAndMetadata(world, 0, stairBase + 3, -1, LOTRMod.slabSingle4, 0);
				setBlockAndMetadata(world, 0, stairBase + 3, -2, LOTRMod.slabSingle4, 0);
				
				setBlockAndMetadata(world, -1, stairBase + 4, 0, LOTRMod.slabSingle4, 0);
				setBlockAndMetadata(world, -2, stairBase + 4, 0, LOTRMod.slabSingle4, 0);
				
				for (int i1 = 0; i1 <= 1; i1++)
				{
					for (int k1 = 0; k1 <= 1; k1++)
					{
						setBlockAndMetadata(world, 1 + i1, stairBase + 1, 1 + k1, LOTRMod.slabSingle4, 8);
						
						setBlockAndMetadata(world, 1 + i1, stairBase + 2, -2 + k1, LOTRMod.slabSingle4, 8);
						
						setBlockAndMetadata(world, -2 + i1, stairBase + 3, -2 + k1, LOTRMod.slabSingle4, 8);
						
						setBlockAndMetadata(world, -2 + i1, stairBase + 4, 1 + k1, LOTRMod.slabSingle4, 8);
					}
				}
				
				setBlockAndMetadata(world, -1, stairBase + 2, 0, Blocks.torch, 1);
				setBlockAndMetadata(world, 1, stairBase + 4, 0, Blocks.torch, 2);
			}
			
			setBlockAndMetadata(world, -4, sectionBase + 4, -4, LOTRMod.wall, 15);
			setBlockAndMetadata(world, -4, sectionBase + 5, -4, Blocks.torch, 5);
			
			setBlockAndMetadata(world, -4, sectionBase + 4, 4, LOTRMod.wall, 15);
			setBlockAndMetadata(world, -4, sectionBase + 5, 4, Blocks.torch, 5);
			
			setBlockAndMetadata(world, 4, sectionBase + 4, -4, LOTRMod.wall, 15);
			setBlockAndMetadata(world, 4, sectionBase + 5, -4, Blocks.torch, 5);
			
			setBlockAndMetadata(world, 4, sectionBase + 4, 4, LOTRMod.wall, 15);
			setBlockAndMetadata(world, 4, sectionBase + 5, 4, Blocks.torch, 5);
			
			placeRandomFeature(world, random, 0, sectionBase + 1, 5, 0);
			
			placeRandomFeature(world, random, -5, sectionBase + 1, 0, 1);
			
			if (l > 0)
			{
				placeRandomFeature(world, random, 0, sectionBase + 1, -5, 2);
			}
			
			placeRandomFeature(world, random, 5, sectionBase + 1, 0, 3);
			
			LOTREntityNearHaradrim warrior = random.nextInt(3) == 0 ? new LOTREntityNearHaradrimArcher(world) : new LOTREntityNearHaradrimWarrior(world);
			warrior.spawnRidingHorse = false;
			spawnNPCAndSetHome(warrior, world, -3, sectionBase + 1, -3, 12);
		}
		
		setBlockAndMetadata(world, 0, topHeight + 1, -5, LOTRMod.slabSingle4, 8);
		placeBanner(world, 0, topHeight + 2, -5, 0, 9);
		
		setBlockAndMetadata(world, 5, topHeight + 1, 0, LOTRMod.slabSingle4, 8);
		placeBanner(world, 5, topHeight + 2, 0, 1, 9);
		
		setBlockAndMetadata(world, 0, topHeight + 1, 5, LOTRMod.slabSingle4, 8);
		placeBanner(world, 0, topHeight + 2, 5, 2, 9);
		
		setBlockAndMetadata(world, -5, topHeight + 1, 0, LOTRMod.slabSingle4, 8);
		placeBanner(world, -5, topHeight + 2, 0, 3, 9);
		
		setBlockAndMetadata(world, 0, 1, -6, Blocks.wooden_door, 1);
		setBlockAndMetadata(world, 0, 2, -6, Blocks.wooden_door, 8);
		placeWallBanner(world, 0, 7, -6, 2, 9);
		
		if (usingPlayer == null)
		{
			LOTRLevelData.nearHaradTowerLocations.add(new ChunkCoordinates(i, j, k));
		}
		
		return true;
	}
	
	private void placeRandomFeature(World world, Random random, int i, int j, int k, int direction)
	{
		int l = random.nextInt(7);
		if (l == 0 || l == 1)
		{
			setBlockAndMetadata(world, i, j, k, LOTRMod.brick, 15);
			if (random.nextInt(4) == 0)
			{
				placeArmorStand(world, i, j + 1, k, direction, new ItemStack[] {new ItemStack(LOTRMod.helmetNearHarad), new ItemStack(LOTRMod.bodyNearHarad), new ItemStack(LOTRMod.legsNearHarad), new ItemStack(LOTRMod.bootsNearHarad)});
			}
		}
		if (l == 2)
		{
			setBlockAndMetadata(world, i, j, k, LOTRMod.nearHaradTable, 0);
		}
		if (l == 3)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.crafting_table, 0);
		}
		if (l == 4)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.chest, 0);
			fillChest(world, random, i, j, k, LOTRChestContents.NEAR_HARAD_TOWER);
		}
		if (l == 5)
		{
			setBlockAndMetadata(world, i, j, k, LOTRMod.brick, 15);
			placePlateWithCertainty(world, i, j + 1, k, random, LOTRFoods.NEAR_HARAD);
		}
		if (l == 6)
		{
			setBlockAndMetadata(world, i, j, k, LOTRMod.brick, 15);
			placeMug(world, random, i, j + 1, k, random.nextInt(4), LOTRMod.mugAraq);
		}
		
		if (direction == 0 || direction == 2)
		{
			setBlockAndMetadata(world, i - 1, j, k, LOTRMod.wall, 15);
			setBlockAndMetadata(world, i + 1, j, k, LOTRMod.wall, 15);
		}
		else
		{
			setBlockAndMetadata(world, i, j, k - 1, LOTRMod.wall, 15);
			setBlockAndMetadata(world, i, j, k + 1, LOTRMod.wall, 15);
		}
	}
}
