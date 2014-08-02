package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityBlueDwarf;
import lotr.common.entity.npc.LOTREntityBlueDwarfAxeThrower;
import lotr.common.entity.npc.LOTREntityBlueDwarfCommander;
import lotr.common.entity.npc.LOTREntityBlueDwarfWarrior;
import lotr.common.entity.npc.LOTREntityDwarf;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class LOTRWorldGenBlueMountainsStronghold extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenBlueMountainsStronghold(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			Block block = world.getBlock(i, j - 1, k);
			if (block != Blocks.stone && block != Blocks.dirt && block != LOTRMod.rock && block != Blocks.snow)
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
				k += 8;
				break;
			case 1:
				i -= 8;
				break;
			case 2:
				k -= 8;
				break;
			case 3:
				i += 8;
				break;
		}
		
		if (restrictions)
		{
			int minHeight = j;
			int maxHeight = j;
			
			for (int i1 = i - 6; i1 <= i + 6; i1++)
			{
				for (int k1 = k - 6; k1 <= k + 6; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					Block block = world.getBlock(i1, j1, k1);
					if (block != Blocks.stone && block != Blocks.dirt && block != LOTRMod.rock && block != Blocks.snow)
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
			
			if (maxHeight - minHeight > 10)
			{
				return false;
			}
		}
	
		for (int k1 = k - 6; k1 <= k + 6; k1++)
		{
			for (int i1 = i - 6; i1 <= i + 6; i1++)
			{
				boolean flag = Math.abs(k1 - k) == 6 && Math.abs(i1 - i) == 6;
				
				for (int j1 = j + 7; (j1 >= j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
				{
					if (flag)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.pillar, 3);
					}
					else
					{
						if (Math.abs(i1 - i) < 6 && Math.abs(k1 - k) < 6)
						{
							if (j1 >= j + 1 && j1 <= j + 3)
							{
								setAir(world, i1, j1, k1);
								continue;
							}
							else if (j1 >= j + 4 && j1 <= j + 7)
							{
								setAir(world, i1, j1, k1);
								continue;
							}
							else if (j1 == j)
							{
								setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 1);
								continue;
							}
						}

						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 14);
					}
					
					if (j1 <= j)
					{
						setGrassToDirt(world, i1, j1 - 1, k1);
					}
				}
			}
		}
		
		for (int i1 = i - 6; i1 <= i + 6; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 8, k - 6, LOTRMod.stairsDwarvenBrick, 2);
			setBlockAndNotifyAdequately(world, i1, j + 8, k + 6, LOTRMod.stairsDwarvenBrick, 3);
		}
	
		for (int k1 = k - 6; k1 <= k + 6; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j + 8, k1, LOTRMod.stairsDwarvenBrick, 0);
			setBlockAndNotifyAdequately(world, i + 6, j + 8, k1, LOTRMod.stairsDwarvenBrick, 1);
		}
		
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			for (int i1 = i - 5; i1 <= i + 5; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, LOTRMod.slabDouble3, 0);
				setBlockAndNotifyAdequately(world, i1, j + 8, k1, LOTRMod.slabDouble3, 0);
				
				int i2 = Math.abs(i1 - i);
				int k2 = Math.abs(k1 - k);
				
				int l = -1;
				if (i2 == 5)
				{
					l = k2 % 2;
				}
				else if (k2 == 5)
				{
					l = i2 % 2;
				}
				
				if (l > -1)
				{
					if (l == 1)
					{
						for (int j1 = j + 9; j1 <= j + 11; j1++)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.pillar, 3);
						}
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j + 9, k1, LOTRMod.wall, 14);
					}
				}
			}
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 12, k - 5, LOTRMod.stairsDwarvenBrick, 2);
			setBlockAndNotifyAdequately(world, i1, j + 12, k + 5, LOTRMod.stairsDwarvenBrick, 3);
		}
	
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j + 12, k1, LOTRMod.stairsDwarvenBrick, 0);
			setBlockAndNotifyAdequately(world, i + 5, j + 12, k1, LOTRMod.stairsDwarvenBrick, 1);
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 12, k1, LOTRMod.slabSingle, 15);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 7, k, LOTRMod.chandelier, 11);
		setBlockAndNotifyAdequately(world, i, j + 11, k, LOTRMod.chandelier, 11);
		setBlockAndNotifyAdequately(world, i, j + 12, k, LOTRMod.brick, 6);

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
		
		spawnDwarfCommander(world, i, j + 9, k);
		
		for (int l = 0; l < 4; l++)
		{
			spawnDwarf(world, i, j + 5, k);
		}
		
		if (usingPlayer == null)
		{
			LOTRLevelData.blueMountainsStrongholdLocations.add(new ChunkCoordinates(i, j, k));
		}

		return true;
	}
	
	private void generateFacingSouth(World world, Random random, int i, int j, int k)
	{
		for (int i1 = i - 6; i1 <= i + 6; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 7, LOTRMod.slabSingle3, 0);
			setGrassToDirt(world, i1, j, k - 7);
			
			for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k - 7) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 7, LOTRMod.pillar, 3);
				setGrassToDirt(world, i1, j1 - 1, k - 7);
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setAir(world, i, j1, k - 6);
			setBlockAndNotifyAdequately(world, i - 1, j1, k - 7, LOTRMod.pillar, 3);
			setBlockAndNotifyAdequately(world, i + 1, j1, k - 7, LOTRMod.pillar, 3);
		}
		
		setBlockAndNotifyAdequately(world, i, j, k - 7, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i, j, k - 6, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 7, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 7, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i - 1, j + 3, k - 7, LOTRMod.stairsDwarvenBrick, 0);
		setBlockAndNotifyAdequately(world, i, j + 3, k - 7, Blocks.glowstone, 0);
		setBlockAndNotifyAdequately(world, i + 1, j + 3, k - 7, LOTRMod.stairsDwarvenBrick, 1);
		setBlockAndNotifyAdequately(world, i, j + 4, k - 7, LOTRMod.slabSingle, 7);
		
		placeWallBanner(world, i, j + 6, k - 6, 2, 11);
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int k1 = k - 4; k1 <= k - 1; k1++)
			{
				for (int i1 = i - 5; i1 <= i - 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 14);
				}
				
				for (int i1 = i + 1; i1 <= i + 5; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 14);
				}
			}
			
			for (int k1 = k - 2; k1 <= k + 5; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 1, j1, k1, LOTRMod.brick, 14);
				setBlockAndNotifyAdequately(world, i + 1, j1, k1, LOTRMod.brick, 14);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 3, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 3, Blocks.wooden_door, 8);
		
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k + 3, Blocks.wooden_door, 2);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k + 3, Blocks.wooden_door, 8);
		
		for (int i1 = i - 5; i1 <= i + 2; i1 += 7)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 1, LOTRMod.dwarvenBed, 2);
			setBlockAndNotifyAdequately(world, i1, j + 1, k, LOTRMod.dwarvenBed, 10);
			
			setBlockAndNotifyAdequately(world, i1 + 3, j + 1, k + 1, LOTRMod.dwarvenBed, 2);
			setBlockAndNotifyAdequately(world, i1 + 3, j + 1, k, LOTRMod.dwarvenBed, 10);
			
			setBlockAndNotifyAdequately(world, i1 + 1, j + 1, k, Blocks.chest, 0);
			setBlockAndNotifyAdequately(world, i1 + 2, j + 1, k, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i1 + 1, j + 1, k, LOTRChestContents.DWARF_HOUSE_LARDER);
			LOTRChestContents.fillChest(world, random, i1 + 2, j + 1, k, LOTRChestContents.BLUE_MOUNTAINS_STRONGHOLD);
			
			setBlockAndNotifyAdequately(world, i1 + 1, j + 3, k + 3, LOTRMod.chandelier, 11);
			
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 5, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1 + 3, j + 1, k + 5, Blocks.planks, 1);
			
			placeBarrel(world, random, i1, j + 2, k + 5, 2, LOTRFoods.DWARF_DRINK.getRandomFood(random).getItem());
			placeBarrel(world, random, i1 + 3, j + 2, k + 5, 2, LOTRFoods.DWARF_DRINK.getRandomFood(random).getItem());
			
			setBlockAndNotifyAdequately(world, i1 + 1, j + 1, k + 5, Blocks.furnace, 0);
			setBlockMetadata(world, i1 + 1, j + 1, k + 5, 2);
			setBlockAndNotifyAdequately(world, i1 + 2, j + 1, k + 5, Blocks.furnace, 0);
			setBlockMetadata(world, i1 + 2, j + 1, k + 5, 2);
		}
		
		setAir(world, i, j + 4, k - 5);
		int stairX = 1;
		for (int j1 = j + 1; j1 <= j + 4; j1++)
		{
			setAir(world, i - stairX, j + 4, k - 5);
			setAir(world, i + stairX, j + 4, k - 5);
			
			setBlockAndNotifyAdequately(world, i - stairX, j1, k - 5, LOTRMod.stairsDwarvenBrick, 1);
			setBlockAndNotifyAdequately(world, i + stairX, j1, k - 5, LOTRMod.stairsDwarvenBrick, 0);
			
			for (int j2 = j1 - 1; j2 > j; j2--)
			{
				setBlockAndNotifyAdequately(world, i - stairX, j2, k - 5, LOTRMod.brick, 6);
				setBlockAndNotifyAdequately(world, i + stairX, j2, k - 5, LOTRMod.brick, 6);
			}
			
			stairX++;
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - stairX, j1, k - 5, LOTRMod.brick, 6);
			setBlockAndNotifyAdequately(world, i + stairX, j1, k - 5, LOTRMod.brick, 6);
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1 += 10)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 2, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j + 6, k - 2, Blocks.wooden_slab, 1);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 2, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j + 6, k + 2, Blocks.wooden_slab, 1);
			setBlockAndNotifyAdequately(world, i1, j + 5, k, LOTRMod.blueDwarvenTable, 0);
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 1, LOTRMod.dwarvenForge, 0);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 1, LOTRMod.dwarvenForge, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 6, k + 6, Blocks.glowstone, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 6, k + 6, Blocks.glowstone, 0);
		
		stairX = 4;
		for (int j1 = j + 5; j1 <= j + 8; j1++)
		{
			setAir(world, i - stairX, j + 8, k - 4);
			setAir(world, i + stairX, j + 8, k - 4);
			
			setBlockAndNotifyAdequately(world, i - stairX, j1, k - 4, LOTRMod.stairsDwarvenBrick, 0);
			setBlockAndNotifyAdequately(world, i + stairX, j1, k - 4, LOTRMod.stairsDwarvenBrick, 1);
			
			for (int j2 = j1 - 1; j2 > j + 4; j2--)
			{
				setBlockAndNotifyAdequately(world, i - stairX, j2, k - 4, LOTRMod.brick, 6);
				setBlockAndNotifyAdequately(world, i + stairX, j2, k - 4, LOTRMod.brick, 6);
			}
			
			stairX--;
		}
		
		for (int j1 = j + 5; j1 <= j + 7; j1++)
		{
			setBlockAndNotifyAdequately(world, i, j1, k - 4, LOTRMod.brick, 6);
		}
		
		setBlockAndNotifyAdequately(world, i, j + 6, k - 4, Blocks.glowstone, 0);
		
		for (int k1 = k + 7; k1 <= k + 8; k1++)
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				placeBalconySection(world, i1, j, k1, false, false);
			}
			placeBalconySection(world, i - 5, j, k1, true, false);
			placeBalconySection(world, i + 5, j, k1, true, false);
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			placeBalconySection(world, i1, j, k + 9, false, false);
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			if (Math.abs(i1 - i) < 3)
			{
				continue;
			}
			placeBalconySection(world, i1, j, k + 9, true, false);
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			placeBalconySection(world, i1, j, k + 10, false, false);
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			if (Math.abs(i1 - i) < 2)
			{
				continue;
			}
			placeBalconySection(world, i1, j, k + 10, true, false);
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			if (Math.abs(i1 - i) == 0)
			{
				placeBalconySection(world, i1, j, k + 11, true, true);
			}
			else
			{
				placeBalconySection(world, i1, j, k + 11, true, false);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 4, k + 6, LOTRMod.slabDouble3, 0);
		setAir(world, i, j + 5, k + 6);
		setAir(world, i, j + 6, k + 6);
		
		setBlockAndNotifyAdequately(world, i, j, k + 6, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 6, Blocks.wooden_door, 3);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 6, Blocks.wooden_door, 8);
		
		setBlockAndNotifyAdequately(world, i, j + 3, k + 8, LOTRMod.chandelier, 11);
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			for (int k1 = k + 7; k1 <= k + 8; k1++)
			{
				placeRandomOre(world, random, i - 4, j1, k1);
				placeRandomOre(world, random, i - 3, j1, k1);
				placeRandomOre(world, random, i + 3, j1, k1);
				placeRandomOre(world, random, i + 4, j1, k1);
			}
			
			placeRandomOre(world, random, i - 2, j1, k + 9);
			placeRandomOre(world, random, i + 2, j1, k + 9);
			
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				placeRandomOre(world, random, i1, j1, k + 10);
			}
		}
	}
	
	private void generateFacingWest(World world, Random random, int i, int j, int k)
	{
		for (int k1 = k - 6; k1 <= k + 6; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 7, j + 1, k1, LOTRMod.slabSingle3, 0);
			setGrassToDirt(world, i + 7, j, k1);
			
			for (int j1 = j; !LOTRMod.isOpaque(world, i + 7, j1, k1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i + 7, j1, k1, LOTRMod.pillar, 3);
				setGrassToDirt(world, i + 7, j1 - 1, k1);
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setAir(world, i + 6, j1, k);
			setBlockAndNotifyAdequately(world, i + 7, j1, k - 1, LOTRMod.pillar, 3);
			setBlockAndNotifyAdequately(world, i + 7, j1, k + 1, LOTRMod.pillar, 3);
		}
		
		setBlockAndNotifyAdequately(world, i + 7, j, k, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 6, j, k, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k, Blocks.wooden_door, 2);
		setBlockAndNotifyAdequately(world, i + 7, j + 2, k, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i + 7, j + 3, k - 1, LOTRMod.stairsDwarvenBrick, 2);
		setBlockAndNotifyAdequately(world, i + 7, j + 3, k, Blocks.glowstone, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 3, k + 1, LOTRMod.stairsDwarvenBrick, 3);
		setBlockAndNotifyAdequately(world, i + 7, j + 4, k, LOTRMod.slabSingle, 7);
		
		placeWallBanner(world, i + 6, j + 6, k, 3, 11);
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int i1 = i + 4; i1 >= i + 1; i1--)
			{
				for (int k1 = k - 5; k1 <= k - 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 14);
				}
				
				for (int k1 = k + 1; k1 <= k + 5; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 14);
				}
			}
			
			for (int i1 = i + 2; i1 >= i - 5; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 1, LOTRMod.brick, 14);
				setBlockAndNotifyAdequately(world, i1, j1, k + 1, LOTRMod.brick, 14);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 1, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 1, Blocks.wooden_door, 8);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 1, Blocks.wooden_door, 3);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 1, Blocks.wooden_door, 8);
		
		for (int k1 = k - 5; k1 <= k + 2; k1 += 7)
		{
			setBlockAndNotifyAdequately(world, i - 1, j + 1, k1, LOTRMod.dwarvenBed, 3);
			setBlockAndNotifyAdequately(world, i, j + 1, k1, LOTRMod.dwarvenBed, 11);
			
			setBlockAndNotifyAdequately(world, i - 1, j + 1, k1 + 3, LOTRMod.dwarvenBed, 3);
			setBlockAndNotifyAdequately(world, i, j + 1, k1 + 3, LOTRMod.dwarvenBed, 11);
			
			setBlockAndNotifyAdequately(world, i, j + 1, k1 + 1, Blocks.chest, 0);
			setBlockAndNotifyAdequately(world, i, j + 1, k1 + 2, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i, j + 1, k1 + 1, LOTRChestContents.DWARF_HOUSE_LARDER);
			LOTRChestContents.fillChest(world, random, i, j + 1, k1 + 2, LOTRChestContents.BLUE_MOUNTAINS_STRONGHOLD);
			
			setBlockAndNotifyAdequately(world, i - 3, j + 3, k1 + 1, LOTRMod.chandelier, 11);
			
			setBlockAndNotifyAdequately(world, i - 5, j + 1, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 5, j + 1, k1 + 3, Blocks.planks, 1);
			
			placeBarrel(world, random, i - 5, j + 2, k1, 5, LOTRFoods.DWARF_DRINK.getRandomFood(random).getItem());
			placeBarrel(world, random, i - 5, j + 2, k1 + 3, 5, LOTRFoods.DWARF_DRINK.getRandomFood(random).getItem());
			
			setBlockAndNotifyAdequately(world, i - 5, j + 1, k1 + 1, Blocks.furnace, 0);
			setBlockMetadata(world, i - 5, j + 1, k1 + 1, 5);
			setBlockAndNotifyAdequately(world, i - 5, j + 1, k1 + 2, Blocks.furnace, 0);
			setBlockMetadata(world, i - 5, j + 1, k1 + 2, 5);
		}
		
		setAir(world, i + 5, j + 4, k);
		int stairX = 1;
		for (int j1 = j + 1; j1 <= j + 4; j1++)
		{
			setAir(world, i + 5, j + 4, k - stairX);
			setAir(world, i + 5, j + 4, k + stairX);
			
			setBlockAndNotifyAdequately(world, i + 5, j1, k - stairX, LOTRMod.stairsDwarvenBrick, 3);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + stairX, LOTRMod.stairsDwarvenBrick, 2);
			
			for (int j2 = j1 - 1; j2 > j; j2--)
			{
				setBlockAndNotifyAdequately(world, i + 5, j2, k - stairX, LOTRMod.brick, 6);
				setBlockAndNotifyAdequately(world, i + 5, j2, k + stairX, LOTRMod.brick, 6);
			}
			
			stairX++;
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 5, j1, k - stairX, LOTRMod.brick, 6);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + stairX, LOTRMod.brick, 6);
		}
		
		for (int k1 = k - 5; k1 <= k + 5; k1 += 10)
		{
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k1, Blocks.wooden_slab, 1);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 2, j + 6, k1, Blocks.wooden_slab, 1);
			setBlockAndNotifyAdequately(world, i, j + 5, k1, LOTRMod.blueDwarvenTable, 0);
			setBlockAndNotifyAdequately(world, i - 1, j + 5, k1, LOTRMod.dwarvenForge, 0);
			setBlockAndNotifyAdequately(world, i + 1, j + 5, k1, LOTRMod.dwarvenForge, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 6, j + 6, k - 3, Blocks.glowstone, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 6, k + 3, Blocks.glowstone, 0);
		
		stairX = 4;
		for (int j1 = j + 5; j1 <= j + 8; j1++)
		{
			setAir(world, i + 4, j + 8, k - stairX);
			setAir(world, i + 4, j + 8, k + stairX);
			
			setBlockAndNotifyAdequately(world, i + 4, j1, k - stairX, LOTRMod.stairsDwarvenBrick, 2);
			setBlockAndNotifyAdequately(world, i + 4, j1, k + stairX, LOTRMod.stairsDwarvenBrick, 3);
			
			for (int j2 = j1 - 1; j2 > j + 4; j2--)
			{
				setBlockAndNotifyAdequately(world, i + 4, j2, k - stairX, LOTRMod.brick, 6);
				setBlockAndNotifyAdequately(world, i + 4, j2, k + stairX, LOTRMod.brick, 6);
			}
			
			stairX--;
		}
		
		for (int j1 = j + 5; j1 <= j + 7; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 4, j1, k, LOTRMod.brick, 6);
		}
		
		setBlockAndNotifyAdequately(world, i + 4, j + 6, k, Blocks.glowstone, 0);
		
		for (int i1 = i - 7; i1 >= i - 8; i1--)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				placeBalconySection(world, i1, j, k1, false, false);
			}
			placeBalconySection(world, i1, j, k - 5, true, false);
			placeBalconySection(world, i1, j, k + 5, true, false);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			placeBalconySection(world, i - 9, j, k1, false, false);
		}
		
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			if (Math.abs(k1 - k) < 3)
			{
				continue;
			}
			placeBalconySection(world, i - 9, j, k1, true, false);
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			placeBalconySection(world, i - 10, j, k1, false, false);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			if (Math.abs(k1 - k) < 2)
			{
				continue;
			}
			placeBalconySection(world, i - 10, j, k1, true, false);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			if (Math.abs(k1 - k) == 0)
			{
				placeBalconySection(world, i - 11, j, k1, true, true);
			}
			else
			{
				placeBalconySection(world, i - 11, j, k1, true, false);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 6, j + 4, k, LOTRMod.slabDouble3, 0);
		setAir(world, i - 6, j + 5, k);
		setAir(world, i - 6, j + 6, k);
		
		setBlockAndNotifyAdequately(world, i - 6, j, k, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k, Blocks.wooden_door, 8);
		
		setBlockAndNotifyAdequately(world, i - 8, j + 3, k, LOTRMod.chandelier, 11);
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			for (int i1 = i - 7; i1 >= i - 8; i1--)
			{
				placeRandomOre(world, random, i1, j1, k - 4);
				placeRandomOre(world, random, i1, j1, k - 3);
				placeRandomOre(world, random, i1, j1, k + 3);
				placeRandomOre(world, random, i1, j1, k + 4);
			}
			
			placeRandomOre(world, random, i - 9, j1, k - 2);
			placeRandomOre(world, random, i - 9, j1, k + 2);
			
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				placeRandomOre(world, random, i - 10, j1, k1);
			}
		}
	}
	
	private void generateFacingNorth(World world, Random random, int i, int j, int k)
	{
		for (int i1 = i - 6; i1 <= i + 6; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 7, LOTRMod.slabSingle3, 0);
			setGrassToDirt(world, i1, j, k + 7);
			
			for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k + 7) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 7, LOTRMod.pillar, 3);
				setGrassToDirt(world, i1, j1 - 1, k + 7);
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setAir(world, i, j1, k + 6);
			setBlockAndNotifyAdequately(world, i - 1, j1, k + 7, LOTRMod.pillar, 3);
			setBlockAndNotifyAdequately(world, i + 1, j1, k + 7, LOTRMod.pillar, 3);
		}
		
		setBlockAndNotifyAdequately(world, i, j, k + 7, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i, j, k + 6, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 7, Blocks.wooden_door, 3);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 7, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i - 1, j + 3, k + 7, LOTRMod.stairsDwarvenBrick, 0);
		setBlockAndNotifyAdequately(world, i, j + 3, k + 7, Blocks.glowstone, 0);
		setBlockAndNotifyAdequately(world, i + 1, j + 3, k + 7, LOTRMod.stairsDwarvenBrick, 1);
		setBlockAndNotifyAdequately(world, i, j + 4, k + 7, LOTRMod.slabSingle, 7);
		
		placeWallBanner(world, i, j + 6, k + 6, 0, 11);
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int k1 = k + 4; k1 >= k + 1; k1--)
			{
				for (int i1 = i - 5; i1 <= i - 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 14);
				}
				
				for (int i1 = i + 1; i1 <= i + 5; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 14);
				}
			}
			
			for (int k1 = k + 2; k1 >= k - 5; k1--)
			{
				setBlockAndNotifyAdequately(world, i - 1, j1, k1, LOTRMod.brick, 14);
				setBlockAndNotifyAdequately(world, i + 1, j1, k1, LOTRMod.brick, 14);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 3, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k - 3, Blocks.wooden_door, 8);
		
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 3, Blocks.wooden_door, 2);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k - 3, Blocks.wooden_door, 8);
		
		for (int i1 = i - 5; i1 <= i + 2; i1 += 7)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 1, LOTRMod.dwarvenBed, 0);
			setBlockAndNotifyAdequately(world, i1, j + 1, k, LOTRMod.dwarvenBed, 8);
			
			setBlockAndNotifyAdequately(world, i1 + 3, j + 1, k - 1, LOTRMod.dwarvenBed, 0);
			setBlockAndNotifyAdequately(world, i1 + 3, j + 1, k, LOTRMod.dwarvenBed, 8);
			
			setBlockAndNotifyAdequately(world, i1 + 1, j + 1, k, Blocks.chest, 0);
			setBlockAndNotifyAdequately(world, i1 + 2, j + 1, k, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i1 + 1, j + 1, k, LOTRChestContents.DWARF_HOUSE_LARDER);
			LOTRChestContents.fillChest(world, random, i1 + 2, j + 1, k, LOTRChestContents.BLUE_MOUNTAINS_STRONGHOLD);
			
			setBlockAndNotifyAdequately(world, i1 + 1, j + 3, k - 3, LOTRMod.chandelier, 11);
			
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 5, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1 + 3, j + 1, k - 5, Blocks.planks, 1);
			
			placeBarrel(world, random, i1, j + 2, k - 5, 3, LOTRFoods.DWARF_DRINK.getRandomFood(random).getItem());
			placeBarrel(world, random, i1 + 3, j + 2, k - 5, 3, LOTRFoods.DWARF_DRINK.getRandomFood(random).getItem());
			
			setBlockAndNotifyAdequately(world, i1 + 1, j + 1, k - 5, Blocks.furnace, 0);
			setBlockMetadata(world, i1 + 1, j + 1, k - 5, 3);
			setBlockAndNotifyAdequately(world, i1 + 2, j + 1, k - 5, Blocks.furnace, 0);
			setBlockMetadata(world, i1 + 2, j + 1, k - 5, 3);
		}
		
		setAir(world, i, j + 4, k + 5);
		int stairX = 1;
		for (int j1 = j + 1; j1 <= j + 4; j1++)
		{
			setAir(world, i - stairX, j + 4, k + 5);
			setAir(world, i + stairX, j + 4, k + 5);
			
			setBlockAndNotifyAdequately(world, i - stairX, j1, k + 5, LOTRMod.stairsDwarvenBrick, 1);
			setBlockAndNotifyAdequately(world, i + stairX, j1, k + 5, LOTRMod.stairsDwarvenBrick, 0);
			
			for (int j2 = j1 - 1; j2 > j; j2--)
			{
				setBlockAndNotifyAdequately(world, i - stairX, j2, k + 5, LOTRMod.brick, 6);
				setBlockAndNotifyAdequately(world, i + stairX, j2, k + 5, LOTRMod.brick, 6);
			}
			
			stairX++;
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - stairX, j1, k + 5, LOTRMod.brick, 6);
			setBlockAndNotifyAdequately(world, i + stairX, j1, k + 5, LOTRMod.brick, 6);
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1 += 10)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 2, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j + 6, k + 2, Blocks.wooden_slab, 1);
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 2, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j + 6, k - 2, Blocks.wooden_slab, 1);
			setBlockAndNotifyAdequately(world, i1, j + 5, k, LOTRMod.blueDwarvenTable, 0);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 1, LOTRMod.dwarvenForge, 0);
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 1, LOTRMod.dwarvenForge, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 6, k - 6, Blocks.glowstone, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 6, k - 6, Blocks.glowstone, 0);
		
		stairX = 4;
		for (int j1 = j + 5; j1 <= j + 8; j1++)
		{
			setAir(world, i - stairX, j + 8, k + 4);
			setAir(world, i + stairX, j + 8, k + 4);
			
			setBlockAndNotifyAdequately(world, i - stairX, j1, k + 4, LOTRMod.stairsDwarvenBrick, 0);
			setBlockAndNotifyAdequately(world, i + stairX, j1, k + 4, LOTRMod.stairsDwarvenBrick, 1);
			
			for (int j2 = j1 - 1; j2 > j + 4; j2--)
			{
				setBlockAndNotifyAdequately(world, i - stairX, j2, k + 4, LOTRMod.brick, 6);
				setBlockAndNotifyAdequately(world, i + stairX, j2, k + 4, LOTRMod.brick, 6);
			}
			
			stairX--;
		}
		
		for (int j1 = j + 5; j1 <= j + 7; j1++)
		{
			setBlockAndNotifyAdequately(world, i, j1, k + 4, LOTRMod.brick, 6);
		}
		
		setBlockAndNotifyAdequately(world, i, j + 6, k + 4, Blocks.glowstone, 0);
		
		for (int k1 = k - 7; k1 >= k - 8; k1--)
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				placeBalconySection(world, i1, j, k1, false, false);
			}
			placeBalconySection(world, i - 5, j, k1, true, false);
			placeBalconySection(world, i + 5, j, k1, true, false);
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			placeBalconySection(world, i1, j, k - 9, false, false);
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			if (Math.abs(i1 - i) < 3)
			{
				continue;
			}
			placeBalconySection(world, i1, j, k - 9, true, false);
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			placeBalconySection(world, i1, j, k - 10, false, false);
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			if (Math.abs(i1 - i) < 2)
			{
				continue;
			}
			placeBalconySection(world, i1, j, k - 10, true, false);
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			if (Math.abs(i1 - i) == 0)
			{
				placeBalconySection(world, i1, j, k - 11, true, true);
			}
			else
			{
				placeBalconySection(world, i1, j, k - 11, true, false);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 4, k - 6, LOTRMod.slabDouble3, 0);
		setAir(world, i, j + 5, k - 6);
		setAir(world, i, j + 6, k - 6);
		
		setBlockAndNotifyAdequately(world, i, j, k - 6, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 6, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 6, Blocks.wooden_door, 8);
		
		setBlockAndNotifyAdequately(world, i, j + 3, k - 8, LOTRMod.chandelier, 11);
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			for (int k1 = k - 7; k1 >= k - 8; k1--)
			{
				placeRandomOre(world, random, i - 4, j1, k1);
				placeRandomOre(world, random, i - 3, j1, k1);
				placeRandomOre(world, random, i + 3, j1, k1);
				placeRandomOre(world, random, i + 4, j1, k1);
			}
			
			placeRandomOre(world, random, i - 2, j1, k - 9);
			placeRandomOre(world, random, i + 2, j1, k - 9);
			
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				placeRandomOre(world, random, i1, j1, k - 10);
			}
		}
	}

	private void generateFacingEast(World world, Random random, int i, int j, int k)
	{
		for (int k1 = k - 6; k1 <= k + 6; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 7, j + 1, k1, LOTRMod.slabSingle3, 0);
			setGrassToDirt(world, i - 7, j, k1);
			
			for (int j1 = j; !LOTRMod.isOpaque(world, i - 7, j1, k1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i - 7, j1, k1, LOTRMod.pillar, 3);
				setGrassToDirt(world, i - 7, j1 - 1, k1);
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setAir(world, i - 6, j1, k);
			setBlockAndNotifyAdequately(world, i - 7, j1, k - 1, LOTRMod.pillar, 3);
			setBlockAndNotifyAdequately(world, i - 7, j1, k + 1, LOTRMod.pillar, 3);
		}
		
		setBlockAndNotifyAdequately(world, i - 7, j, k, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i - 6, j, k, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i - 7, j + 1, k, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 2, k, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i - 7, j + 3, k - 1, LOTRMod.stairsDwarvenBrick, 2);
		setBlockAndNotifyAdequately(world, i - 7, j + 3, k, Blocks.glowstone, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 3, k + 1, LOTRMod.stairsDwarvenBrick, 3);
		setBlockAndNotifyAdequately(world, i - 7, j + 4, k, LOTRMod.slabSingle, 7);
		
		placeWallBanner(world, i - 6, j + 6, k, 1, 11);
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int i1 = i - 4; i1 <= i - 1; i1++)
			{
				for (int k1 = k - 5; k1 <= k - 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 14);
				}
				
				for (int k1 = k + 1; k1 <= k + 5; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 14);
				}
			}
			
			for (int i1 = i - 2; i1 <= i + 5; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 1, LOTRMod.brick, 14);
				setBlockAndNotifyAdequately(world, i1, j1, k + 1, LOTRMod.brick, 14);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 1, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 1, Blocks.wooden_door, 8);
		
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 1, Blocks.wooden_door, 3);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 1, Blocks.wooden_door, 8);
		
		for (int k1 = k - 5; k1 <= k + 2; k1 += 7)
		{
			setBlockAndNotifyAdequately(world, i + 1, j + 1, k1, LOTRMod.dwarvenBed, 1);
			setBlockAndNotifyAdequately(world, i, j + 1, k1, LOTRMod.dwarvenBed, 9);
			
			setBlockAndNotifyAdequately(world, i + 1, j + 1, k1 + 3, LOTRMod.dwarvenBed, 1);
			setBlockAndNotifyAdequately(world, i, j + 1, k1 + 3, LOTRMod.dwarvenBed, 9);
			
			setBlockAndNotifyAdequately(world, i, j + 1, k1 + 1, Blocks.chest, 0);
			setBlockAndNotifyAdequately(world, i, j + 1, k1 + 2, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i, j + 1, k1 + 1, LOTRChestContents.DWARF_HOUSE_LARDER);
			LOTRChestContents.fillChest(world, random, i, j + 1, k1 + 2, LOTRChestContents.BLUE_MOUNTAINS_STRONGHOLD);
			
			setBlockAndNotifyAdequately(world, i + 3, j + 3, k1 + 1, LOTRMod.chandelier, 11);
			
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k1 + 3, Blocks.planks, 1);
			
			placeBarrel(world, random, i + 5, j + 2, k1, 4, LOTRFoods.DWARF_DRINK.getRandomFood(random).getItem());
			placeBarrel(world, random, i + 5, j + 2, k1 + 3, 4, LOTRFoods.DWARF_DRINK.getRandomFood(random).getItem());
			
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k1 + 1, Blocks.furnace, 0);
			setBlockMetadata(world, i + 5, j + 1, k1 + 1, 4);
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k1 + 2, Blocks.furnace, 0);
			setBlockMetadata(world, i + 5, j + 1, k1 + 2, 4);
		}
		
		setAir(world, i - 5, j + 4, k);
		int stairX = 1;
		for (int j1 = j + 1; j1 <= j + 4; j1++)
		{
			setAir(world, i - 5, j + 4, k - stairX);
			setAir(world, i - 5, j + 4, k + stairX);
			
			setBlockAndNotifyAdequately(world, i - 5, j1, k - stairX, LOTRMod.stairsDwarvenBrick, 3);
			setBlockAndNotifyAdequately(world, i - 5, j1, k + stairX, LOTRMod.stairsDwarvenBrick, 2);
			
			for (int j2 = j1 - 1; j2 > j; j2--)
			{
				setBlockAndNotifyAdequately(world, i - 5, j2, k - stairX, LOTRMod.brick, 6);
				setBlockAndNotifyAdequately(world, i - 5, j2, k + stairX, LOTRMod.brick, 6);
			}
			
			stairX++;
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j1, k - stairX, LOTRMod.brick, 6);
			setBlockAndNotifyAdequately(world, i - 5, j1, k + stairX, LOTRMod.brick, 6);
		}
		
		for (int k1 = k - 5; k1 <= k + 5; k1 += 10)
		{
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k1, Blocks.wooden_slab, 1);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 2, j + 6, k1, Blocks.wooden_slab, 1);
			setBlockAndNotifyAdequately(world, i, j + 5, k1, LOTRMod.blueDwarvenTable, 0);
			setBlockAndNotifyAdequately(world, i - 1, j + 5, k1, LOTRMod.dwarvenForge, 0);
			setBlockAndNotifyAdequately(world, i + 1, j + 5, k1, LOTRMod.dwarvenForge, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j + 6, k - 3, Blocks.glowstone, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 6, k + 3, Blocks.glowstone, 0);
		
		stairX = 4;
		for (int j1 = j + 5; j1 <= j + 8; j1++)
		{
			setAir(world, i - 4, j + 8, k - stairX);
			setAir(world, i - 4, j + 8, k + stairX);
			
			setBlockAndNotifyAdequately(world, i - 4, j1, k - stairX, LOTRMod.stairsDwarvenBrick, 2);
			setBlockAndNotifyAdequately(world, i - 4, j1, k + stairX, LOTRMod.stairsDwarvenBrick, 3);
			
			for (int j2 = j1 - 1; j2 > j + 4; j2--)
			{
				setBlockAndNotifyAdequately(world, i - 4, j2, k - stairX, LOTRMod.brick, 6);
				setBlockAndNotifyAdequately(world, i - 4, j2, k + stairX, LOTRMod.brick, 6);
			}
			
			stairX--;
		}
		
		for (int j1 = j + 5; j1 <= j + 7; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j1, k, LOTRMod.brick, 6);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 6, k, Blocks.glowstone, 0);
		
		for (int i1 = i + 7; i1 <= i + 8; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				placeBalconySection(world, i1, j, k1, false, false);
			}
			placeBalconySection(world, i1, j, k - 5, true, false);
			placeBalconySection(world, i1, j, k + 5, true, false);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			placeBalconySection(world, i + 9, j, k1, false, false);
		}
		
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			if (Math.abs(k1 - k) < 3)
			{
				continue;
			}
			placeBalconySection(world, i + 9, j, k1, true, false);
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			placeBalconySection(world, i + 10, j, k1, false, false);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			if (Math.abs(k1 - k) < 2)
			{
				continue;
			}
			placeBalconySection(world, i + 10, j, k1, true, false);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			if (Math.abs(k1 - k) == 0)
			{
				placeBalconySection(world, i + 11, j, k1, true, true);
			}
			else
			{
				placeBalconySection(world, i + 11, j, k1, true, false);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j + 4, k, LOTRMod.slabDouble3, 0);
		setAir(world, i + 6, j + 5, k);
		setAir(world, i + 6, j + 6, k);
		
		setBlockAndNotifyAdequately(world, i + 6, j, k, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k, Blocks.wooden_door, 2);
		setBlockAndNotifyAdequately(world, i + 6, j + 2, k, Blocks.wooden_door, 8);
		
		setBlockAndNotifyAdequately(world, i + 8, j + 3, k, LOTRMod.chandelier, 11);
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			for (int i1 = i + 7; i1 <= i + 8; i1++)
			{
				placeRandomOre(world, random, i1, j1, k - 4);
				placeRandomOre(world, random, i1, j1, k - 3);
				placeRandomOre(world, random, i1, j1, k + 3);
				placeRandomOre(world, random, i1, j1, k + 4);
			}
			
			placeRandomOre(world, random, i + 9, j1, k - 2);
			placeRandomOre(world, random, i + 9, j1, k + 2);
			
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				placeRandomOre(world, random, i + 10, j1, k1);
			}
		}
	}
	
	private void spawnDwarfCommander(World world, int i, int j, int k)
	{
		LOTREntityDwarf dwarf = new LOTREntityBlueDwarfCommander(world);
		dwarf.setLocationAndAngles(i + 0.5D, j, k + 0.5D, 0F, 0F);
		dwarf.onSpawnWithEgg(null);
		dwarf.setHomeArea(i, j, k, 16);
		world.spawnEntityInWorld(dwarf);
	}
	
	private void spawnDwarf(World world, int i, int j, int k)
	{
		LOTREntityBlueDwarf dwarf = world.rand.nextInt(3) == 0 ? new LOTREntityBlueDwarfAxeThrower(world) : new LOTREntityBlueDwarfWarrior(world);
		dwarf.setLocationAndAngles(i + 0.5D, j, k + 0.5D, 0F, 0F);
		dwarf.onSpawnWithEgg(null);
		dwarf.isNPCPersistent = true;
		dwarf.setHomeArea(i, j, k, 16);
		world.spawnEntityInWorld(dwarf);
	}
	
	private void placeBalconySection(World world, int i, int j, int k, boolean isEdge, boolean isPillar)
	{
		if (isEdge)
		{
			for (int j1 = j + 4; (j1 >= j || !LOTRMod.isOpaque(world, i, j1, k)) && j1 >= 0; j1--)
			{
				if (isPillar)
				{
					setBlockAndNotifyAdequately(world, i, j1, k, LOTRMod.pillar, 3);
				}
				else
				{
					setBlockAndNotifyAdequately(world, i, j1, k, LOTRMod.brick, 14);
				}
				setGrassToDirt(world, i, j1 - 1, k);
			}
			
			if (isPillar)
			{
				setBlockAndNotifyAdequately(world, i, j + 4, k, Blocks.glowstone, 0);
			}
			
			setBlockAndNotifyAdequately(world, i, j + 5, k, LOTRMod.brick, 6);
			setBlockAndNotifyAdequately(world, i, j + 6, k, LOTRMod.wall, 14);
		}
		else
		{
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i, j1, k) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i, j1, k, LOTRMod.brick, 14);
				setGrassToDirt(world, i, j1 - 1, k);
			}
			
			setBlockAndNotifyAdequately(world, i, j, k, Blocks.planks, 1);
			
			for (int j1 = j + 1; j1 <= j + 6; j1++)
			{
				setAir(world, i, j1, k);
			}
			
			setBlockAndNotifyAdequately(world, i, j + 4, k, LOTRMod.slabDouble3, 0);
		}
	}
	
	private void placeRandomOre(World world, Random random, int i, int j, int k)
	{
		if (!LOTRMod.isOpaque(world, i, j - 1, k) || !random.nextBoolean())
		{
			return;
		}
		
		int l = random.nextInt(5);
		Block block = null;
		switch(l)
		{
			case 0:
				block = Blocks.iron_ore;
				break;
			case 1:
				block = Blocks.gold_ore;
				break;
			case 2:
				block = LOTRMod.oreCopper;
				break;
			case 3:
				block = LOTRMod.oreTin;
				break;
			case 4:
				block = LOTRMod.oreSilver;
				break;
		}
		
		setBlockAndNotifyAdequately(world, i, j, k, block, 0);
	}
}

