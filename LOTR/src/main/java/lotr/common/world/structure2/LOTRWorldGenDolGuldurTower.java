package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityDolGuldurOrcChieftain;
import lotr.common.world.biome.LOTRBiomeGenDolGuldur;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenDolGuldurTower extends LOTRWorldGenStructureBase2
{
	public LOTRWorldGenDolGuldurTower(boolean flag)
	{
		super(flag);
	}

	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation)
	{
		if (restrictions)
		{
			if (!(world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenDolGuldur))
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
		
		int sections = 3 + random.nextInt(3);
		int sectionHeight = 6;
		int topHeight = sections * sectionHeight;
		
		double radiusD = (double)radius - 0.5D;
		double radiusDPlusOne = radiusD + 1D;
		int wallThresholdMin = (int)(radiusD * radiusD);
		int wallThresholdMax = (int)(radiusDPlusOne * radiusDPlusOne);
		
		if (restrictions)
		{
			int minHeight = 0;
			int maxHeight = 0;
			
			for (int i1 = -radiusPlusOne; i1 <= radiusPlusOne; i1++)
			{
				for (int k1 = -radiusPlusOne; k1 <=radiusPlusOne; k1++)
				{
					int distSq = i1 * i1 + k1 * k1;
					if (distSq >= wallThresholdMax)
					{
						continue;
					}
					else
					{
						int j1 = getTopBlock(world, i1, k1) - 1;
						Block block = getBlock(world, i1, j1, k1);
						if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone)
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
						
						if (maxHeight - minHeight > 16)
						{
							return false;
						}
					}
				}
			}
		}
		
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
						if (distSq >= wallThresholdMin)
						{
							placeRandomBrick(world, random, i1, j1, k1);
						}
						else
						{
							setBlockAndMetadata(world, i1, j1, k1, Blocks.stonebrick, 0);
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
								placeRandomBrick(world, random, i1, j1, k1);
							}
							else
							{
								if (j1 == sectionBase + sectionHeight)
								{
									setBlockAndMetadata(world, i1, j1, k1, Blocks.stonebrick, 0);
								}
								else
								{
									setAir(world, i1, j1, k1);
								}
							}
						}
					}
				}
			}
			
			for (int j1 = sectionBase + 2; j1 <= sectionBase + 3; j1++)
			{
				for (int k1 = -1; k1 <= 1; k1++)
				{
					setBlockAndMetadata(world, -radius, j1, k1, LOTRMod.orcSteelBars, 0);
					setBlockAndMetadata(world, radius, j1, k1, LOTRMod.orcSteelBars, 0);
				}
				
				for (int i1 = -1; i1 <= 1; i1++)
				{
					setBlockAndMetadata(world, i1, j1, -radius, LOTRMod.orcSteelBars, 0);
				}
			}
			
			if (l > 0)
			{
				setAir(world, 0, sectionBase, 0);
				
				for (int i1 = -1; i1 <= 1; i1++)
				{
					for (int k1 = -1; k1 <= 1; k1++)
					{
						int i2 = Math.abs(i1);
						int k2 = Math.abs(k1);
						
						if (i2 == 1 || k2 == 1)
						{
							setBlockAndMetadata(world, i1, sectionBase + 1, k1, LOTRMod.wall2, 8);
						}
						
						if (i2 == 1 && k2 == 1)
						{
							setBlockAndMetadata(world, i1, sectionBase + 2, k1, LOTRMod.wall2, 8);
							placeSkull(world, random, i1, sectionBase + 2, k1);
						}
					}
				}
			}
			else
			{
				for (int i1 = -1; i1 <= 1; i1++)
				{
					for (int j1 = sectionBase + 1; j1 <= sectionBase + 3; j1++)
					{
						setAir(world, i1, j1, -radius);
					}
					
					setBlockAndMetadata(world, i1, sectionBase, -radius, Blocks.stonebrick, 0);
				}
				
				placeRandomStairs(world, random, -1, sectionBase + 3, -radius, 4);
				placeRandomStairs(world, random, 1, sectionBase + 3, -radius, 5);
				
				placeWallBanner(world, 0, sectionBase + 6, -radius, LOTRFaction.DOL_GULDUR, 2);
				
				for (int i1 = -5; i1 <= 5; i1++)
				{
					setBlockAndMetadata(world, i1, sectionBase, 0, LOTRMod.guldurilBrick, 4);
				}
				
				for (int k1 = -6; k1 <= 3; k1++)
				{
					setBlockAndMetadata(world, 0, sectionBase, k1, LOTRMod.guldurilBrick, 4);
				}
				
				setBlockAndMetadata(world, 0, sectionBase + 1, 0, LOTRMod.guldurilBrick, 4);
				setBlockAndMetadata(world, 0, sectionBase + 2, 0, LOTRMod.wall2, 8);
				placeSkull(world, random, 0, sectionBase + 3, 0);
			}
			
			for (int j1 = sectionBase + 1; j1 <= sectionBase + 5; j1++)
			{
				setBlockAndMetadata(world, -2, j1, -5, LOTRMod.wood, 2);
				setBlockAndMetadata(world, 2, j1, -5, LOTRMod.wood, 2);
				
				setBlockAndMetadata(world, 5, j1, -2, LOTRMod.wood, 2);
				setBlockAndMetadata(world, 5, j1, 2, LOTRMod.wood, 2);
				
				setBlockAndMetadata(world, -3, j1, 4, LOTRMod.wood, 2);
				setBlockAndMetadata(world, 3, j1, 4, LOTRMod.wood, 2);
				
				setBlockAndMetadata(world, -5, j1, -2, LOTRMod.wood, 2);
				setBlockAndMetadata(world, -5, j1, 2, LOTRMod.wood, 2);
			}
			
			setBlockAndMetadata(world, -3, sectionBase + 4, 3, LOTRMod.morgulTorch, 4);
			setBlockAndMetadata(world, 3, sectionBase + 4, 3, LOTRMod.morgulTorch, 4);
			
			setBlockAndMetadata(world, 4, sectionBase + 4, -2, LOTRMod.morgulTorch, 1);
			setBlockAndMetadata(world, 4, sectionBase + 4, 2, LOTRMod.morgulTorch, 1);
			
			setBlockAndMetadata(world, -2, sectionBase + 4, -4, LOTRMod.morgulTorch, 3);
			setBlockAndMetadata(world, 2, sectionBase + 4, -4, LOTRMod.morgulTorch, 3);
			
			setBlockAndMetadata(world, -4, sectionBase + 4, -2, LOTRMod.morgulTorch, 2);
			setBlockAndMetadata(world, -4, sectionBase + 4, 2, LOTRMod.morgulTorch, 2);
			
			setBlockAndMetadata(world, -3, sectionBase + 5, 3, Blocks.stone_brick_stairs, 6);
			setBlockAndMetadata(world, 3, sectionBase + 5, 3, Blocks.stone_brick_stairs, 6);
			
			setBlockAndMetadata(world, 4, sectionBase + 5, -2, Blocks.stone_brick_stairs, 5);
			setBlockAndMetadata(world, 5, sectionBase + 5, -1, Blocks.stone_brick_stairs, 7);
			setBlockAndMetadata(world, 5, sectionBase + 5, 1, Blocks.stone_brick_stairs, 6);
			setBlockAndMetadata(world, 4, sectionBase + 5, 2, Blocks.stone_brick_stairs, 5);
			
			setBlockAndMetadata(world, -2, sectionBase + 5, -4, Blocks.stone_brick_stairs, 7);
			setBlockAndMetadata(world, -1, sectionBase + 5, -5, Blocks.stone_brick_stairs, 4);
			setBlockAndMetadata(world, 1, sectionBase + 5, -5, Blocks.stone_brick_stairs, 5);
			setBlockAndMetadata(world, 2, sectionBase + 5, -4, Blocks.stone_brick_stairs, 7);
			
			setBlockAndMetadata(world, -4, sectionBase + 5, -2, Blocks.stone_brick_stairs, 4);
			setBlockAndMetadata(world, -5, sectionBase + 5, -1, Blocks.stone_brick_stairs, 7);
			setBlockAndMetadata(world, -5, sectionBase + 5, 1, Blocks.stone_brick_stairs, 6);
			setBlockAndMetadata(world, -4, sectionBase + 5, 2, Blocks.stone_brick_stairs, 4);
			
			for (int step = 0; step <= 2; step++)
			{
				setBlockAndMetadata(world, 1 - step, sectionBase + 1 + step, 4, Blocks.stone_brick_stairs, 0);
				for (int j1 = sectionBase + 1; j1 <= sectionBase + step; j1++)
				{
					setBlockAndMetadata(world, 1 - step, j1, 4, Blocks.stonebrick, 0);
				}
			}
			
			for (int k1 = 4; k1 <= 5; k1++)
			{
				for (int j1 = sectionBase + 1; j1 <= sectionBase + 3; j1++)
				{
					setBlockAndMetadata(world, -2, j1, k1, Blocks.stonebrick, 0);
				}
			}
			
			for (int i1 = -2; i1 <= 0; i1++)
			{
				setAir(world, i1, sectionBase + sectionHeight, 5);
			}
			
			for (int step = 0; step <= 2; step++)
			{
				setBlockAndMetadata(world, -1 + step, sectionBase + 4 + step, 5, Blocks.stone_brick_stairs, 1);
				setBlockAndMetadata(world, -1 + step, sectionBase + 3 + step, 5, Blocks.stonebrick, 0);
				setBlockAndMetadata(world, -1 + step, sectionBase + 2 + step, 5, Blocks.stone_brick_stairs, 4);
			}
			
			setBlockAndMetadata(world, 2, sectionBase + 5, 5, Blocks.stone_brick_stairs, 4);
		}
		
		placeChest(world, random, -1, 1, 5, 0, LOTRChestContents.DOL_GULDUR_TOWER);
		
		for (int k1 = -3; k1 <= 3; k1 += 6)
		{
			for (int step = 0; step <= 3; step++)
			{
				placeBrickSupports(world, random, -9 + step, k1);
				placeBrickSupports(world, random, 9 - step, k1);
				
				placeRandomStairs(world, random, -9 + step, 1 + step * 2, k1, 1);
				placeRandomStairs(world, random, 9 - step, 1 + step * 2, k1, 0);
				
				for (int j1 = 1; j1 <= step * 2; j1++)
				{
					placeRandomBrick(world, random, -9 + step, j1, k1);
					placeRandomBrick(world, random, 9 - step, j1, k1);
				}
			}
		}
		
		for (int i1 = -3; i1 <= 3; i1 += 6)
		{
			for (int step = 0; step <= 3; step++)
			{
				placeBrickSupports(world, random, i1, -9 + step);
				placeBrickSupports(world, random, i1, 9 - step);
				
				placeRandomStairs(world, random, i1, 1 + step * 2, -9 + step, 2);
				placeRandomStairs(world, random, i1, 1 + step * 2, 9 - step, 3);
				
				for (int j1 = 1; j1 <= step * 2; j1++)
				{
					placeRandomBrick(world, random, i1, j1, -9 + step);
					placeRandomBrick(world, random, i1, j1, 9 - step);
				}
			}
		}
		
		for (int i1 = -radius; i1 <= radius; i1++)
		{
			for (int k1 = -radius; k1 <= radius; k1++)
			{
				int distSq = i1 * i1 + k1 * k1;
				if (distSq >= wallThresholdMax)
				{
					continue;
				}
				else if (distSq >= (int)Math.pow(radiusD - 0.25D, 2))
				{
					int i2 = Math.abs(i1);
					int k2 = Math.abs(k1);
					
					setBlockAndMetadata(world, i1, topHeight + 1, k1, LOTRMod.wall2, 8);
					
					if (i2 >= 3 && k2 >= 3)
					{
						setBlockAndMetadata(world, i1, topHeight + 2, k1, LOTRMod.wall2, 8);
						
						if (i2 == 4 && k2 == 4)
						{
							setBlockAndMetadata(world, i1, topHeight + 3, k1, LOTRMod.wall2, 8);
							setBlockAndMetadata(world, i1, topHeight + 4, k1, LOTRMod.wall2, 8);
							setBlockAndMetadata(world, i1, topHeight + 5, k1, LOTRMod.morgulTorch, 5);
						}
					}
				}
			}
		}
		
		setAir(world, -2, topHeight + 1, 5);
		
		for (int i1 = -2; i1 <= 2; i1 += 4)
		{
			for (int step = 0; step <= 4; step++)
			{
				int j1 = topHeight + 1 + step * 2;
				
				int k1 = -9 + step;
				
				placeRandomStairs(world, random, i1, j1 - 2, k1, 7);
				for (int j2 = j1 - 1; j2 <= j1 + 1; j2++)
				{
					placeRandomBrick(world, random, i1, j2, k1);
				}
				placeRandomStairs(world, random, i1, j1 + 2, k1, 2);
				
				k1 = 9 - step;
				
				placeRandomStairs(world, random, i1, j1 - 2, k1, 6);
				for (int j2 = j1 - 1; j2 <= j1 + 1; j2++)
				{
					placeRandomBrick(world, random, i1, j2, k1);
				}
				placeRandomStairs(world, random, i1, j1 + 2, k1, 3);
			}
			
			for (int j1 = topHeight - 4; j1 <= topHeight + 2; j1++)
			{
				for (int k1 = -9; k1 <= -8; k1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				
				for (int k1 = 8; k1 <= 9; k1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
			}
			
			placeRandomBrick(world, random, i1, topHeight - 1, -7);
			placeRandomBrick(world, random, i1, topHeight, -7);
			setBlockAndMetadata(world, i1, topHeight + 1, -7, LOTRMod.wall2, 8);
			
			placeRandomBrick(world, random, i1, topHeight - 1, 7);
			placeRandomBrick(world, random, i1, topHeight, 7);
			setBlockAndMetadata(world, i1, topHeight + 1, 7, LOTRMod.wall2, 8);
			
			placeRandomStairs(world, random, i1, topHeight - 4, -9, 6);
			placeRandomStairs(world, random, i1, topHeight - 5, -8, 6);
			
			placeRandomStairs(world, random, i1, topHeight - 4, 9, 7);
			placeRandomStairs(world, random, i1, topHeight - 5, 8, 7);
		}
		
		for (int k1 = -2; k1 <= 2; k1 += 4)
		{
			for (int step = 0; step <= 4; step++)
			{
				int j1 = topHeight + 1 + step * 2;
				
				int i1 = -9 + step;
				
				placeRandomStairs(world, random, i1, j1 - 2, k1, 4);
				for (int j2 = j1 - 1; j2 <= j1 + 1; j2++)
				{
					placeRandomBrick(world, random, i1, j2, k1);
				}
				placeRandomStairs(world, random, i1, j1 + 2, k1, 1);
				
				i1 = 9 - step;
				
				placeRandomStairs(world, random, i1, j1 - 2, k1, 5);
				for (int j2 = j1 - 1; j2 <= j1 + 1; j2++)
				{
					placeRandomBrick(world, random, i1, j2, k1);
				}
				placeRandomStairs(world, random, i1, j1 + 2, k1, 0);
			}
			
			for (int j1 = topHeight - 4; j1 <= topHeight + 2; j1++)
			{
				for (int i1 = -9; i1 <= -8; i1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				
				for (int i1 = 8; i1 <= 9; i1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
			}
			
			placeRandomBrick(world, random, -7, topHeight - 1, k1);
			placeRandomBrick(world, random, -7, topHeight, k1);
			setBlockAndMetadata(world, -7, topHeight + 1, k1, LOTRMod.wall2, 8);
			
			placeRandomBrick(world, random, 7, topHeight - 1, k1);
			placeRandomBrick(world, random, 7, topHeight, k1);
			setBlockAndMetadata(world, 7, topHeight + 1, k1, LOTRMod.wall2, 8);
			
			placeRandomStairs(world, random, -9, topHeight - 4, k1, 5);
			placeRandomStairs(world, random, -8, topHeight - 5, k1, 5);
			
			placeRandomStairs(world, random, 9, topHeight - 4, k1, 4);
			placeRandomStairs(world, random, 8, topHeight - 5, k1, 4);
		}
		
		spawnNPCAndSetHome(new LOTREntityDolGuldurOrcChieftain(world), world, 0, topHeight + 1, 0, 16);
		
		return true;
	}
	
	private void placeBrickSupports(World world, Random random, int i, int k)
	{
		for (int j = 0; !isOpaque(world, i, j, k) && getY(j) >= 0; j--)
		{
			placeRandomBrick(world, random, i, j, k);
			setGrassToDirt(world, i, j - 1, k);
		}
	}
	
	private void placeRandomBrick(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(6) == 0)
		{
			setBlockAndMetadata(world, i, j, k, LOTRMod.brick2, 9);
		}
		else
		{
			setBlockAndMetadata(world, i, j, k, LOTRMod.brick2, 8);
		}
	}
	
	private void placeRandomStairs(World world, Random random, int i, int j, int k, int meta)
	{
		if (random.nextInt(6) == 0)
		{
			setBlockAndMetadata(world, i, j, k, LOTRMod.stairsDolGuldurBrickCracked, meta);
		}
		else
		{
			setBlockAndMetadata(world, i, j, k, LOTRMod.stairsDolGuldurBrick, meta);
		}
	}
}
