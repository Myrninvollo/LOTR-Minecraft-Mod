package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityElf;
import lotr.common.entity.npc.LOTREntityGaladhrimLord;
import lotr.common.world.feature.LOTRWorldGenMallornLarge;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRWorldGenElfLordHouse extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenElfLordHouse(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			for (int i1 = i - 14; i1 <= i + 14; i1++)
			{
				for (int j1 = j; j1 <= j + 7; j1++)
				{
					for (int k1 = k - 14; k1 <= k + 14; k1++)
					{
						if (Math.abs(i1 - i) <= 1 && Math.abs(k1 - k) <= 1)
						{
							continue;
						}
						else if (!world.isAirBlock(i1, j1, k1))
						{
							return false;
						}
					}
				}
			}
			
			int totalGrass = 0;
			int numGrass = 0;
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					if (Math.abs(i1 - i) <= 1 && Math.abs(k1 - k) <= 1)
					{
						continue;
					}
					
					for (int j1 = j; j1 >= 0; j1--)
					{
						if (world.getBlock(i1, j1, k1) == Blocks.grass)
						{
							totalGrass += j1;
							numGrass++;
							break;
						}
					}
				}
			}
			
			int lowestGrass = totalGrass / numGrass;
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					if (Math.abs(i1 - i) <= 1 && Math.abs(k1 - k) <= 1)
					{
						continue;
					}
					
					for (int j1 = j; j1 > lowestGrass; j1--)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
					setBlockAndNotifyAdequately(world, i1, lowestGrass, k1, Blocks.grass, 0);
				}
			}
		}
		else if (usingPlayer != null)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.wood, 1);
					}
				}
			}
			
			j--;
			
			LOTRWorldGenMallornLarge treeGen = new LOTRWorldGenMallornLarge(true);
			int j1 = treeGen.generateAndReturnHeight(world, random, i, j, k, true);
			j += MathHelper.floor_double(j1 * MathHelper.randomFloatClamp(random, LOTRWorldGenMallornLarge.HOUSE_HEIGHT_MIN, LOTRWorldGenMallornLarge.HOUSE_HEIGHT_MAX));
		}
		
		buildStaircase(world, i, j, k);
		
		for (int i1 = i - 14; i1 <= i + 14; i1++)
		{
			for (int j1 = j; j1 <= j + 6; j1++)
			{
				for (int k1 = k - 14; k1 <= k + 14; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int j1 = j; j1 <= j + 7; j1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.wood, 1);
				}
			}
		}
		
		for (int i1 = i - 12; i1 <= i + 12; i1++)
		{
			for (int k1 = k - 12; k1 <= k + 12; k1++)
			{
				int i2 = i1 - i;
				int k2 = k1 - k;
				if (Math.abs(i2) > 1 || Math.abs(k2) > 1)
				{
					int distSq = i2 * i2 + k2 * k2;
					if (distSq < 100)
					{
						setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.planks, 1);
					}
					else if (distSq < 169)
					{
						setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRMod.planks, 1);
						if (distSq > 132)
						{
							setBlockAndNotifyAdequately(world, i1, j + 2, k1, LOTRMod.fence, 1);
						}
					}
				}
			}
		}
		
		for (int i1 = i - 12; i1 <= i + 12; i1++)
		{
			for (int k1 = k - 12; k1 <= k + 12; k1++)
			{
				int i2 = i1 - i;
				int k2 = k1 - k;
				int distSq = i2 * i2 + k2 * k2;
				if ((Math.abs(i2) > 1 || Math.abs(k2) > 1) && distSq < 169)
				{
					setBlockAndNotifyAdequately(world, i1, j + 6, k1, LOTRMod.planks, 1);
					int i3 = i1;
					int k3 = k1;
					if (i3 > i)
					{
						i3--;
					}
					if (i3 < i)
					{
						i3++;
					}
					if (k3 > k)
					{
						k3--;
					}
					if (k3 < k)
					{
						k3++;
					}
					setBlockAndNotifyAdequately(world, i3, j + 7, k3, LOTRMod.planks, 1);
				}
			}
		}
		
		buildStairCircle(world, i, j, k, 10, true, false);
		buildStairCircle(world, i, j + 1, k, 9, false, true);
		buildStairCircle(world, i, j + 6, k, 13, false, false);
		buildStairCircle(world, i, j + 7, k, 12, false, false);
		
		setBlockAndNotifyAdequately(world, i + 2, j + 3, k, LOTRMod.mallornTorch, 1);
		setBlockAndNotifyAdequately(world, i - 2, j + 3, k, LOTRMod.mallornTorch, 2);
		setBlockAndNotifyAdequately(world, i, j + 3, k + 2, LOTRMod.mallornTorch, 3);
		setBlockAndNotifyAdequately(world, i, j + 3, k - 2, LOTRMod.mallornTorch, 4);
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 2, LOTRMod.stairsMallorn, 6);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 2, LOTRMod.stairsMallorn, 7);
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k1, LOTRMod.stairsMallorn, 4);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k1, LOTRMod.stairsMallorn, 5);
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1 += 8)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1 += 8)
			{
				for (int j1 = j + 1; j1 <= j + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.wood, 1);
				}
				
				setBlockAndNotifyAdequately(world, i1 + 1, j + 3, k1, LOTRMod.mallornTorch, 1);
				setBlockAndNotifyAdequately(world, i1 - 1, j + 3, k1, LOTRMod.mallornTorch, 2);
				setBlockAndNotifyAdequately(world, i1, j + 3, k1 + 1, LOTRMod.mallornTorch, 3);
				setBlockAndNotifyAdequately(world, i1, j + 3, k1 - 1, LOTRMod.mallornTorch, 4);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 5, LOTRMod.elvenTable, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 4, LOTRMod.stairsMallorn, 7);
		placeFlowerPot(world, i - 5, j + 2, k - 4, getRandomPlant(random));
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 5, LOTRMod.stairsMallorn, 5);
		placeFlowerPot(world, i - 4, j + 2, k - 5, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 5, LOTRMod.elvenTable, 0);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 4, LOTRMod.stairsMallorn, 7);
		placeFlowerPot(world, i + 5, j + 2, k - 4, getRandomPlant(random));
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 5, LOTRMod.stairsMallorn, 4);
		placeFlowerPot(world, i + 4, j + 2, k - 5, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 5, LOTRMod.elvenTable, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 4, LOTRMod.stairsMallorn, 6);
		placeFlowerPot(world, i - 5, j + 2, k + 4, getRandomPlant(random));
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k + 5, LOTRMod.stairsMallorn, 5);
		placeFlowerPot(world, i - 4, j + 2, k + 5, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 5, LOTRMod.elvenTable, 0);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 4, LOTRMod.stairsMallorn, 6);
		placeFlowerPot(world, i + 5, j + 2, k + 4, getRandomPlant(random));
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k + 5, LOTRMod.stairsMallorn, 4);
		placeFlowerPot(world, i + 4, j + 2, k + 5, getRandomPlant(random));
		
		setBlockAndNotifyAdequately(world, i - 8, j + 5, k, LOTRMod.chandelier, 5);
		setBlockAndNotifyAdequately(world, i + 8, j + 5, k, LOTRMod.chandelier, 5);
		setBlockAndNotifyAdequately(world, i, j + 5, k - 8, LOTRMod.chandelier, 5);
		setBlockAndNotifyAdequately(world, i, j + 5, k + 8, LOTRMod.chandelier, 5);
		
		for (int i1 = i - 8; i1 <= i + 8; i1 += 16)
		{
			for (int k1 = k - 8; k1 <= k + 8; k1 += 16)
			{
				for (int j1 = j + 2; j1 <= j + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.planks, 1);
				}
				
				for (int i2 = i1 - 1; i2 <= i1 + 1; i2++)
				{
					setBlockAndNotifyAdequately(world, i2, j + 5, k1 - 1, LOTRMod.stairsMallorn, 6);
					setBlockAndNotifyAdequately(world, i2, j + 5, k1 + 1, LOTRMod.stairsMallorn, 7);
				}
				
				setBlockAndNotifyAdequately(world, i1 - 1, j + 5, k1, LOTRMod.stairsMallorn, 4);
				setBlockAndNotifyAdequately(world, i1 + 1, j + 5, k1, LOTRMod.stairsMallorn, 5);
			}
		}
		
		for (int j1 = j + 2; j1 <= j + 5; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 12, j1, k - 4, LOTRMod.wood, 1);
			setBlockAndNotifyAdequately(world, i - 12, j1, k + 4, LOTRMod.wood, 1);
			
			setBlockAndNotifyAdequately(world, i + 12, j1, k - 4, LOTRMod.wood, 1);
			setBlockAndNotifyAdequately(world, i + 12, j1, k + 4, LOTRMod.wood, 1);
			
			setBlockAndNotifyAdequately(world, i - 4, j1, k - 12, LOTRMod.wood, 1);
			setBlockAndNotifyAdequately(world, i + 4, j1, k - 12, LOTRMod.wood, 1);
			
			setBlockAndNotifyAdequately(world, i - 4, j1, k + 12, LOTRMod.wood, 1);
			setBlockAndNotifyAdequately(world, i + 4, j1, k + 12, LOTRMod.wood, 1);
		}
		
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			if (Math.abs(k1 - k) <= 2)
			{
				setBlockAndNotifyAdequately(world, i - 12, j + 5, k1, LOTRMod.woodSlabSingle, 9);
				setBlockAndNotifyAdequately(world, i + 12, j + 5, k1, LOTRMod.woodSlabSingle, 9);
			}
			else
			{
				setBlockAndNotifyAdequately(world, i - 11, j + 5, k1, LOTRMod.stairsMallorn, 5);
				setBlockAndNotifyAdequately(world, i + 11, j + 5, k1, LOTRMod.stairsMallorn, 4);
			}
			
			if (k1 - k == -5 || k1 - k == 3)
			{
				setBlockAndNotifyAdequately(world, i - 12, j + 5, k1, LOTRMod.stairsMallorn, 6);
				setBlockAndNotifyAdequately(world, i + 12, j + 5, k1, LOTRMod.stairsMallorn, 6);
			}
			else if (k1 - k == -3 || k1 - k == 5)
			{
				setBlockAndNotifyAdequately(world, i - 12, j + 5, k1, LOTRMod.stairsMallorn, 7);
				setBlockAndNotifyAdequately(world, i + 12, j + 5, k1, LOTRMod.stairsMallorn, 7);
			}
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			if (Math.abs(i1 - i) <= 2)
			{
				setBlockAndNotifyAdequately(world, i1, j + 5, k - 12, LOTRMod.woodSlabSingle, 9);
				setBlockAndNotifyAdequately(world, i1, j + 5, k + 12, LOTRMod.woodSlabSingle, 9);
			}
			else
			{
				setBlockAndNotifyAdequately(world, i1, j + 5, k - 11, LOTRMod.stairsMallorn, 7);
				setBlockAndNotifyAdequately(world, i1, j + 5, k + 11, LOTRMod.stairsMallorn, 6);
			}
			
			if (i1 - i == -5 || i1 - i == 3)
			{
				setBlockAndNotifyAdequately(world, i1, j + 5, k - 12, LOTRMod.stairsMallorn, 4);
				setBlockAndNotifyAdequately(world, i1, j + 5, k + 12, LOTRMod.stairsMallorn, 4);
			}
			else if (i1 - i == -3 || i1 - i == 5)
			{
				setBlockAndNotifyAdequately(world, i1, j + 5, k - 12, LOTRMod.stairsMallorn, 5);
				setBlockAndNotifyAdequately(world, i1, j + 5, k + 12, LOTRMod.stairsMallorn, 5);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k, LOTRMod.elvenBed, 3);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k, LOTRMod.elvenBed, 11);
		
		placeBanner(world, i, j + 2, k - 11, 0, 3);
		placeBanner(world, i + 11, j + 2, k, 1, 3);
		placeBanner(world, i, j + 2, k + 11, 2, 3);
		placeBanner(world, i - 11, j + 2, k, 3, 3);
		
		tryPlaceLight(world, i - 12, j, k - 2, random);
		tryPlaceLight(world, i - 12, j, k + 2, random);
		tryPlaceLight(world, i - 9, j, k + 9, random);
		tryPlaceLight(world, i - 2, j, k + 12, random);
		tryPlaceLight(world, i + 2, j, k + 12, random);
		tryPlaceLight(world, i + 9, j, k + 9, random);
		tryPlaceLight(world, i + 12, j, k + 2, random);
		tryPlaceLight(world, i + 12, j, k - 2, random);
		tryPlaceLight(world, i + 9, j, k - 9, random);
		tryPlaceLight(world, i + 2, j, k - 12, random);
		tryPlaceLight(world, i - 2, j, k - 12, random);
		tryPlaceLight(world, i - 9, j, k - 9, random);
		
		for (int i1 = i - 3; i1 <= i - 2; i1++)
		{
			for (int k1 = k - 2; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.air, 0);
			}
			setBlockAndNotifyAdequately(world, i1, j, k - 2, LOTRMod.stairsMallorn, 3);
		}

		LOTREntityElf elfLord = new LOTREntityGaladhrimLord(world);
		elfLord.setLocationAndAngles(i + 0.5D, j + 1, k + 3.5D, 0F, 0F);
		elfLord.spawnRidingHorse = false;
		elfLord.onSpawnWithEgg(null);
		elfLord.setHomeArea(i, j, k, 8);
		world.spawnEntityInWorld(elfLord);
		
		return true;
	}
	
	private void buildStaircase(World world, int i, int j, int k)
	{
		int i1 = i - 2;
		int j1 = j - 1;
		int k1 = k - 1;
		
		for (int l = 0; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; l++)
		{
			int l1 = l % 16;
			if (l1 < 3)
			{
				for (int i2 = i1; i2 >= i1 - 2; i2--)
				{
					for (int j2 = j1 + 1; j2 <= j1 + 3; j2++)
					{
						setBlockAndNotifyAdequately(world, i2, j2, k1, Blocks.air, 0);
					}
				}
				setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.stairsMallorn, 3);
				setBlockAndNotifyAdequately(world, i1 - 1, j1, k1, LOTRMod.stairsMallorn, 3);
				setBlockAndNotifyAdequately(world, i1 - 2, j1, k1, LOTRMod.stairsMallorn, 4);
				setBlockAndNotifyAdequately(world, i1 - 2, j1 + 1, k1, LOTRMod.fence, 1);
				if (l1 > 0)
				{
					setBlockAndNotifyAdequately(world, i1 - 2, j1 + 2, k1, LOTRMod.fence, 1);
				}
				j1--;
				k1++;
			}
			else if (l1 == 3)
			{
				for (int i2 = i1; i2 >= i1 - 2; i2--)
				{
					for (int j2 = j1 + 1; j2 <= j1 + 3; j2++)
					{
						for (int k2 = k1; k2 <= k1 + 2; k2++)
						{
							setBlockAndNotifyAdequately(world, i2, j2, k2, Blocks.air, 0);
						}
					}
				}
				for (int i2 = i1; i2 >= i1 - 1; i2--)
				{
					for (int k2 = k1; k2 <= k1 + 1; k2++)
					{
						setBlockAndNotifyAdequately(world, i2, j1, k2, LOTRMod.planks, 1);
					}
				}
				for (int k2 = k1; k2 <= k1 + 2; k2++)
				{
					setBlockAndNotifyAdequately(world, i1 - 2, j1, k2, LOTRMod.stairsMallorn, 4);
					setBlockAndNotifyAdequately(world, i1 - 2, j1 + 1, k2, LOTRMod.fence, 1);
				}
				for (int i2 = i1; i2 >= i1 - 1; i2--)
				{
					setBlockAndNotifyAdequately(world, i2, j1, k1 + 2, LOTRMod.stairsMallorn, 7);
					setBlockAndNotifyAdequately(world, i2, j1 + 1, k1 + 2, LOTRMod.fence, 1);
				}
				setBlockAndNotifyAdequately(world, i1 - 2, j1 + 2, k1, LOTRMod.fence, 1);
				setBlockAndNotifyAdequately(world, i1 - 2, j1 + 2, k1 + 2, LOTRMod.mallornTorch, 5);
				i1++;
			}
			else if (l1 < 7)
			{
				for (int k2 = k1; k2 <= k1 + 2; k2++)
				{
					for (int j2 = j1 + 1; j2 <= j1 + 3; j2++)
					{
						setBlockAndNotifyAdequately(world, i1, j2, k2, Blocks.air, 0);
					}
				}
				setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.stairsMallorn, 1);
				setBlockAndNotifyAdequately(world, i1, j1, k1 + 1, LOTRMod.stairsMallorn, 1);
				setBlockAndNotifyAdequately(world, i1, j1, k1 + 2, LOTRMod.stairsMallorn, 7);
				setBlockAndNotifyAdequately(world, i1, j1 + 1, k1 + 2, LOTRMod.fence, 1);
				if (l1 > 4)
				{
					setBlockAndNotifyAdequately(world, i1, j1 + 2, k1 + 2, LOTRMod.fence, 1);
				}
				j1--;
				i1++;
			}
			else if (l1 == 7)
			{
				for (int i2 = i1; i2 <= i1 + 2; i2++)
				{
					for (int j2 = j1 + 1; j2 <= j1 + 3; j2++)
					{
						for (int k2 = k1; k2 <= k1 + 2; k2++)
						{
							setBlockAndNotifyAdequately(world, i2, j2, k2, Blocks.air, 0);
						}
					}
				}
				for (int i2 = i1; i2 <= i1 + 1; i2++)
				{
					for (int k2 = k1; k2 <= k1 + 1; k2++)
					{
						setBlockAndNotifyAdequately(world, i2, j1, k2, LOTRMod.planks, 1);
					}
				}
				for (int i2 = i1; i2 <= i1 + 2; i2++)
				{
					setBlockAndNotifyAdequately(world, i2, j1, k1 + 2, LOTRMod.stairsMallorn, 7);
					setBlockAndNotifyAdequately(world, i2, j1 + 1, k1 + 2, LOTRMod.fence, 1);
				}
				for (int k2 = k1; k2 <= k1 + 1; k2++)
				{
					setBlockAndNotifyAdequately(world, i1 + 2, j1, k2, LOTRMod.stairsMallorn, 5);
					setBlockAndNotifyAdequately(world, i1 + 2, j1 + 1, k2, LOTRMod.fence, 1);
				}
				setBlockAndNotifyAdequately(world, i1, j1 + 2, k1 + 2, LOTRMod.fence, 1);
				setBlockAndNotifyAdequately(world, i1 + 2, j1 + 2, k1 + 2, LOTRMod.mallornTorch, 5);
				k1--;
			}
			else if (l1 < 11)
			{
				for (int i2 = i1; i2 <= i1 + 2; i2++)
				{
					for (int j2 = j1 + 1; j2 <= j1 + 3; j2++)
					{
						setBlockAndNotifyAdequately(world, i2, j2, k1, Blocks.air, 0);
					}
				}
				setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.stairsMallorn, 2);
				setBlockAndNotifyAdequately(world, i1 + 1, j1, k1, LOTRMod.stairsMallorn, 2);
				setBlockAndNotifyAdequately(world, i1 + 2, j1, k1, LOTRMod.stairsMallorn, 5);
				setBlockAndNotifyAdequately(world, i1 + 2, j1 + 1, k1, LOTRMod.fence, 1);
				if (l1 > 8)
				{
					setBlockAndNotifyAdequately(world, i1 + 2, j1 + 2, k1, LOTRMod.fence, 1);
				}
				j1--;
				k1--;
			}
			else if (l1 == 11)
			{
				for (int i2 = i1; i2 <= i1 + 2; i2++)
				{
					for (int j2 = j1 + 1; j2 <= j1 + 3; j2++)
					{
						for (int k2 = k1; k2 >= k1 - 2; k2--)
						{
							setBlockAndNotifyAdequately(world, i2, j2, k2, Blocks.air, 0);
						}
					}
				}
				for (int i2 = i1; i2 <= i1 + 1; i2++)
				{
					for (int k2 = k1; k2 >= k1 - 1; k2--)
					{
						setBlockAndNotifyAdequately(world, i2, j1, k2, LOTRMod.planks, 1);
					}
				}
				for (int k2 = k1; k2 >= k1 - 2; k2--)
				{
					setBlockAndNotifyAdequately(world, i1 + 2, j1, k2, LOTRMod.stairsMallorn, 5);
					setBlockAndNotifyAdequately(world, i1 + 2, j1 + 1, k2, LOTRMod.fence, 1);
				}
				for (int i2 = i1; i2 <= i1 + 1; i2++)
				{
					setBlockAndNotifyAdequately(world, i2, j1, k1 - 2, LOTRMod.stairsMallorn, 6);
					setBlockAndNotifyAdequately(world, i2, j1 + 1, k1 - 2, LOTRMod.fence, 1);
				}
				setBlockAndNotifyAdequately(world, i1 + 2, j1 + 2, k1, LOTRMod.fence, 1);
				setBlockAndNotifyAdequately(world, i1 + 2, j1 + 2, k1 - 2, LOTRMod.mallornTorch, 5);
				i1--;
			}
			else if (l1 < 15)
			{
				for (int k2 = k1; k2 >= k1 - 2; k2--)
				{
					for (int j2 = j1 + 1; j2 <= j1 + 3; j2++)
					{
						setBlockAndNotifyAdequately(world, i1, j2, k2, Blocks.air, 0);
					}
				}
				setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.stairsMallorn, 0);
				setBlockAndNotifyAdequately(world, i1, j1, k1 - 1, LOTRMod.stairsMallorn, 0);
				setBlockAndNotifyAdequately(world, i1, j1, k1 - 2, LOTRMod.stairsMallorn, 6);
				setBlockAndNotifyAdequately(world, i1, j1 + 1, k1 - 2, LOTRMod.fence, 1);
				if (l1 > 12)
				{
					setBlockAndNotifyAdequately(world, i1, j1 + 2, k1 - 2, LOTRMod.fence, 1);
				}
				j1--;
				i1--;
			}
			else if (l1 == 15)
			{
				for (int i2 = i1; i2 >= i1 - 2; i2--)
				{
					for (int j2 = j1 + 1; j2 <= j1 + 3; j2++)
					{
						for (int k2 = k1; k2 >= k1 - 2; k2--)
						{
							setBlockAndNotifyAdequately(world, i2, j2, k2, Blocks.air, 0);
						}
					}
				}
				for (int i2 = i1; i2 >= i1 - 1; i2--)
				{
					for (int k2 = k1; k2 >= k1 - 1; k2--)
					{
						setBlockAndNotifyAdequately(world, i2, j1, k2, LOTRMod.planks, 1);
					}
				}
				for (int i2 = i1; i2 >= i1 - 2; i2--)
				{
					setBlockAndNotifyAdequately(world, i2, j1, k1 - 2, LOTRMod.stairsMallorn, 6);
					setBlockAndNotifyAdequately(world, i2, j1 + 1, k1 - 2, LOTRMod.fence, 1);
				}
				for (int k2 = k1; k2 >= k1 - 1; k2--)
				{
					setBlockAndNotifyAdequately(world, i1 - 2, j1, k2, LOTRMod.stairsMallorn, 4);
					setBlockAndNotifyAdequately(world, i1 - 2, j1 + 1, k2, LOTRMod.fence, 1);
				}
				setBlockAndNotifyAdequately(world, i1, j1 + 2, k1 - 2, LOTRMod.fence, 1);
				setBlockAndNotifyAdequately(world, i1 - 2, j1 + 2, k1 - 2, LOTRMod.mallornTorch, 5);
				k1++;
			}
		}
	}
	
	private void buildStairCircle(World world, int i, int j, int k, int range, boolean upsideDown, boolean insideOut)
	{
		for (int i1 = i - range; i1 <= i + range; i1++)
		{
			for (int k1 = k - range; k1 <= k + range; k1++)
			{
				if (world.isAirBlock(i1, j, k1))
				{
					int direction = -1;
					
					if (isMallornPlanks(world, i1 - 1, j, k1))
					{
						direction = 1;
					}
					else if (isMallornPlanks(world, i1 + 1, j, k1))
					{
						direction = 3;
					}
					else if (isMallornPlanks(world, i1, j, k1 - 1))
					{
						direction = 2;
					}
					else if (isMallornPlanks(world, i1, j, k1 + 1))
					{
						direction = 0;
					}
					else if (isMallornPlanks(world, i1 - 1, j, k1 - 1) || isMallornPlanks(world, i1 + 1, j, k1 - 1))
					{
						direction = 2;
					}
					else if (isMallornPlanks(world, i1 - 1, j, k1 + 1) || isMallornPlanks(world, i1 + 1, j, k1 + 1))
					{
						direction = 0;
					}
					
					if (direction != -1)
					{
						if (insideOut)
						{
							direction += 4;
							direction &= 3;
						}
						
						int meta = 0;
						switch (direction)
						{
							case 0:
								meta = 2;
								break;
							case 1:
								meta = 1;
								break;
							case 2:
								meta = 3;
								break;
							case 3:
								meta = 0;
								break;
						}
						
						if (upsideDown)
						{
							meta |= 4;
						}
						
						setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.stairsMallorn, meta);
					}
				}
			}
		}
	}
	
	private boolean isMallornPlanks(World world, int i, int j, int k)
	{
		return world.getBlock(i, j, k) == LOTRMod.planks && world.getBlockMetadata(i, j, k) == 1;
	}
	
	private void tryPlaceLight(World world, int i, int j, int k, Random random)
	{
		int height = 3 + random.nextInt(7);
		for (int j1 = j; j1 >= j - height; j1--)
		{
			if (restrictions)
			{
				if (!world.isAirBlock(i, j1, k))
				{
					return;
				}
				if (j1 == j - height)
				{
					if (!world.isAirBlock(i, j1, k - 1) || !world.isAirBlock(i, j1, k + 1) || !world.isAirBlock(i - 1, j1, k) || !world.isAirBlock(i + 1, j1, k))
					{
						return;
					}
				}
			}
		}
		
		for (int j1 = j; j1 >= j - height; j1--)
		{
			if (j1 == j - height)
			{
				setBlockAndNotifyAdequately(world, i, j1, k, LOTRMod.planks, 1);
				setBlockAndNotifyAdequately(world, i, j1, k - 1, LOTRMod.mallornTorch, 4);
				setBlockAndNotifyAdequately(world, i, j1, k + 1, LOTRMod.mallornTorch, 3);
				setBlockAndNotifyAdequately(world, i - 1, j1, k, LOTRMod.mallornTorch, 2);
				setBlockAndNotifyAdequately(world, i + 1, j1, k, LOTRMod.mallornTorch, 1);
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j1, k, LOTRMod.fence, 1);
			}
		}
	}
	
	private ItemStack getRandomPlant(Random random)
	{
		return random.nextBoolean() ? new ItemStack(LOTRMod.elanor) : new ItemStack(LOTRMod.niphredil);
	}
}
