package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityMirkwoodSpider;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.feature.LOTRWorldGenWebOfUngoliant;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenRuinedWoodElfTower extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenRuinedWoodElfTower(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass || world.getBiomeGenForCoords(i, k) != LOTRBiome.mirkwoodCorrupted)
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
							placeRandomStoneBrick(world, random, i1, j1, k1);
						}
						else
						{
							placeDungeonBlock(world, random, i1, j1, k1);
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
								placeRandomStoneBrick(world, random, i1, j1, k1);
								if (l == sections - 1 && j1 == sectionBase + sectionHeight)
								{
									placeRandomStoneBrick(world, random, i1, j1 + 1, k1);
									placeRandomStoneSlab(world, random, i1, j1 + 2, k1, false);
								}
							}
							else
							{
								if (j1 == sectionBase + sectionHeight && (Math.abs(i2) > 2 || Math.abs(k2) > 2))
								{
									placeDungeonBlock(world, random, i1, j1, k1);
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
				
				placeDungeonBlock(world, random, i, j1, k);
			}
			
			for (int l1 = 0; l1 < 2; l1++)
			{
				int stairBase = sectionBase + l1 * 4;
				
				placeRandomStoneSlab(world, random, i, stairBase + 1, k + 1, false);
				placeRandomStoneSlab(world, random, i, stairBase + 1, k + 2, false);
				
				placeRandomStoneSlab(world, random, i + 1, stairBase + 2, k, false);
				placeRandomStoneSlab(world, random, i + 2, stairBase + 2, k, false);
				
				placeRandomStoneSlab(world, random, i, stairBase + 3, k - 1, false);
				placeRandomStoneSlab(world, random, i, stairBase + 3, k - 2, false);
				
				placeRandomStoneSlab(world, random, i - 1, stairBase + 4, k, false);
				placeRandomStoneSlab(world, random, i - 2, stairBase + 4, k, false);
				
				for (int i1 = 0; i1 <= 1; i1++)
				{
					for (int k1 = 0; k1 <= 1; k1++)
					{
						placeRandomStoneSlab(world, random, i + 1 + i1, stairBase + 1, k + 1 + k1, true);
						
						placeRandomStoneSlab(world, random, i + 1 + i1, stairBase + 2, k - 2 + k1, true);
						
						placeRandomStoneSlab(world, random, i - 2 + i1, stairBase + 3, k - 2 + k1, true);
						
						placeRandomStoneSlab(world, random, i - 2 + i1, stairBase + 4, k + 1 + k1, true);
					}
				}
			}
			
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
				
				placeRandomStoneStairs(world, random, i - 1, sectionBase + 4, k - 6, 5);
				placeRandomStoneStairs(world, random, i + 1, sectionBase + 4, k - 6, 4);
				
				placeRandomStoneStairs(world, random, i - 1, sectionBase + 4, k + 6, 5);
				placeRandomStoneStairs(world, random, i + 1, sectionBase + 4, k + 6, 4);
				
				placeRandomStoneStairs(world, random, i - 6, sectionBase + 4, k - 1, 7);
				placeRandomStoneStairs(world, random, i - 6, sectionBase + 4, k + 1, 6);
				
				placeRandomStoneStairs(world, random, i + 6, sectionBase + 4, k - 1, 7);
				placeRandomStoneStairs(world, random, i + 6, sectionBase + 4, k + 1, 6);
			}
		}
		
		for (int j1 = topHeight + 2; j1 <= topHeight + 3; j1++)
		{
			placeRandomStoneBrick(world, random, i + 6, j1, k - 3);
			placeRandomStoneBrick(world, random, i + 6, j1, k);
			placeRandomStoneBrick(world, random, i + 6, j1, k + 3);
			
			placeRandomStoneBrick(world, random, i - 3, j1, k + 6);
			placeRandomStoneBrick(world, random, i, j1, k + 6);
			placeRandomStoneBrick(world, random, i + 3, j1, k + 6);
			
			placeRandomStoneBrick(world, random, i - 6, j1, k - 3);
			placeRandomStoneBrick(world, random, i - 6, j1, k);
			placeRandomStoneBrick(world, random, i - 6, j1, k + 3);
			
			placeRandomStoneBrick(world, random, i - 3, j1, k - 6);
			placeRandomStoneBrick(world, random, i, j1, k - 6);
			placeRandomStoneBrick(world, random, i + 3, j1, k - 6);
		}
		
		placeRandomStoneBrick(world, random, i + 6, topHeight + 2, k - 2);
		placeRandomStoneBrick(world, random, i + 6, topHeight + 2, k + 2);
		
		placeRandomStoneBrick(world, random, i - 2, topHeight + 2, k + 6);
		placeRandomStoneBrick(world, random, i + 2, topHeight + 2, k + 6);
		
		placeRandomStoneBrick(world, random, i - 6, topHeight + 2, k - 2);
		placeRandomStoneBrick(world, random, i - 6, topHeight + 2, k + 2);
		
		placeRandomStoneBrick(world, random, i - 2, topHeight + 2, k - 6);
		placeRandomStoneBrick(world, random, i + 2, topHeight + 2, k - 6);
		
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
				if (random.nextInt(4) == 0)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 4, Blocks.iron_bars, 0);
				}
				if (random.nextInt(4) == 0)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k + 4, Blocks.iron_bars, 0);
				}
			}
			
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				if (random.nextInt(4) == 0)
				{
					setBlockAndNotifyAdequately(world, i + 3, j1, k1, Blocks.iron_bars, 0);
				}
			}
		}
		
		placeSkull(world, random, i - 2, j - sectionHeight - 5, k - 5, 3, 1);
		placeSkull(world, random, i + 2, j - sectionHeight - 5, k + 5, 3, 1);
		placeSkull(world, random, i + 4, j - sectionHeight - 5, k - 1, 1, 3);
		
		placeMobSpawner(world, i - 1, j - sectionHeight - 5, k, LOTREntityMirkwoodSpider.class);
		
		new LOTRWorldGenWebOfUngoliant(notifyChanges, 24).generate(world, random, i - 1, j - sectionHeight - 5, k);
		
		placeDungeonBlock(world, random, i + 4, j - sectionHeight - 5, k);
		setBlockAndNotifyAdequately(world, i + 4, j - sectionHeight - 5, k - 1, Blocks.chest, 0);
		LOTRChestContents.fillChest(world, random, i + 4, j - sectionHeight - 5, k - 1, LOTRChestContents.MIRKWOOD_LOOT);
		setBlockAndNotifyAdequately(world, i + 4, j - sectionHeight - 5, k + 1, Blocks.chest, 0);
		LOTRChestContents.fillChest(world, random, i + 4, j - sectionHeight - 5, k + 1, LOTRChestContents.MIRKWOOD_LOOT);
		
		setBlockAndNotifyAdequately(world, i - 5, j - sectionHeight - 1, k, Blocks.air, 0);
		setBlockAndNotifyAdequately(world, i - 5, j - sectionHeight, k, Blocks.air, 0);
		
		int i1;
		int j1;
		int k1;
		switch (rotation)
		{
			case 0:
				k1 = k - radius;
				setBlockAndNotifyAdequately(world, i, j + 1, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i, j + 2, k1, Blocks.air, 0);
				break;
			case 1:
				i1 = i + radius;
				setBlockAndNotifyAdequately(world, i1, j + 1, k, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j + 2, k, Blocks.air, 0);
				break;
			case 2:
				k1 = k + radius;
				setBlockAndNotifyAdequately(world, i, j + 1, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i, j + 2, k1, Blocks.air, 0);
				break;
			case 3:
				i1 = i - radius;
				setBlockAndNotifyAdequately(world, i1, j + 1, k, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j + 2, k, Blocks.air, 0);
				break;
		}
		
		return true;
	}
	
	private void placeRandomStoneBrick(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(20) == 0)
		{
			return;
		}
		
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
	
	private void placeRandomStoneSlab(World world, Random random, int i, int j, int k, boolean inverted)
	{
		if (random.nextInt(8) == 0)
		{
			return;
		}

		setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle2, inverted ? 11 : 3);
	}
	
	private void placeRandomStoneStairs(World world, Random random, int i, int j, int k, int meta)
	{
		if (random.nextInt(8) == 0)
		{
			return;
		}
		
		int l = random.nextInt(3);
		switch (l)
		{
			case 0:
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.stairsElvenBrick, meta);
				break;
			case 1:
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.stairsElvenBrickMossy, meta);
				break;
			case 2:
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.stairsElvenBrickCracked, meta);
				break;
		}
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
	
	private void placeSkull(World world, Random random, int i, int j, int k, int xRange, int zRange)
	{
		i += random.nextInt(xRange);
		k += random.nextInt(zRange);
		
		if (random.nextBoolean())
		{
			placeSkull(world, random, i, j, k);
		}
	}
}
