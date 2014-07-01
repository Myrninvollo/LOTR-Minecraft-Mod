package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityAngmarOrcMercenaryCaptain;
import lotr.common.world.biome.LOTRBiomeGenAngmar;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class LOTRWorldGenAngmarTower extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenAngmarTower(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (!(world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenAngmar))
			{
				return false;
			}
			Block l = world.getBlock(i, j - 1, k);
			if (l != Blocks.grass && l != Blocks.dirt && l != Blocks.stone)
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
				k += 7;
				break;
			case 1:
				i -= 7;
				break;
			case 2:
				k -= 7;
				break;
			case 3:
				i += 7;
				break;
		}
		
		int sections = 2 + random.nextInt(3);
		
		if (restrictions)
		{
			for (int i1 = i - 7; i1 <= i + 7; i1++)
			{
				for (int k1 = k - 7; k1 <= k + 7; k1++)
				{
					int j1 = world.getHeightValue(i1, k1) - 1;
					Block block = world.getBlock(i1, j1, k1);
					if (block != Blocks.grass && block != Blocks.stone && block != Blocks.dirt && !block.isWood(world, i1, j1, k1) && !block.isLeaves(world, i1, j1, k1))
					{
						return false;
					}
				}
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(i - 7, j, k - 7, i + 7, j + ((sections + 1) * 8) + 10, k + 7));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			for (int j1 = j; !LOTRMod.isOpaque(world, i - 6, j1, k1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i - 6, j1, k1, LOTRMod.brick2, 0);
				if (world.getBlock(i - 6, j1 - 1, k1) == Blocks.grass)
				{
					setBlockAndNotifyAdequately(world, i - 6, j1 - 1, k1, Blocks.dirt, 0);
				}
			}
			for (int j1 = j; !LOTRMod.isOpaque(world, i + 6, j1, k1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i + 6, j1, k1, LOTRMod.brick2, 0);
				if (world.getBlock(i + 6, j1 - 1, k1) == Blocks.grass)
				{
					setBlockAndNotifyAdequately(world, i + 6, j1 - 1, k1, Blocks.dirt, 0);
				}
			}
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			for (int j1 = j; !LOTRMod.isOpaque(world, i - 5, j1, k1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i - 5, j1, k1, LOTRMod.brick2, 0);
				if (world.getBlock(i - 5, j1 - 1, k1) == Blocks.grass)
				{
					setBlockAndNotifyAdequately(world, i - 5, j1 - 1, k1, Blocks.dirt, 0);
				}
			}
			for (int j1 = j; !LOTRMod.isOpaque(world, i + 5, j1, k1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i + 5, j1, k1, LOTRMod.brick2, 0);
				if (world.getBlock(i + 5, j1 - 1, k1) == Blocks.grass)
				{
					setBlockAndNotifyAdequately(world, i + 5, j1 - 1, k1, Blocks.dirt, 0);
				}
			}
		}
		
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			for (int i1 = i - 4; i1 <= i - 3; i1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick2, 0);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
			
			for (int i1 = i + 3; i1 <= i + 4; i1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick2, 0);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		for (int k1 = k - 6; k1 <= k + 6; k1++)
		{
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick2, 0);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		for (int l = 0; l <= sections; l++)
		{
			generateTowerSection(world, random, i, j, k, l, false);
		}
		
		generateTowerSection(world, random, i, j, k, sections + 1, true);
		
		LOTREntityAngmarOrcMercenaryCaptain trader = new LOTREntityAngmarOrcMercenaryCaptain(world);
		trader.setLocationAndAngles(i - 2 + 0.5D, j + (sections + 1) * 8 + 1, k + 0.5D, world.rand.nextFloat() * 360F, 0F);
		trader.onSpawnWithEgg(null);
		trader.setHomeArea(i, j + (sections + 1) * 8, k, 24);
		world.spawnEntityInWorld(trader);
		
		switch (rotation)
		{
			case 0:
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j, k - 6, Blocks.stonebrick, 0);
					for (int j1 = j + 1; j1 <= j + 4; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k - 6, Blocks.air, 0);
					}
				}
				setBlockAndNotifyAdequately(world, i, j + 7, k - 6, LOTRMod.brick2, 0);
				placeWallBanner(world, i, j + 7, k - 6, 2, 8);
				break;
			case 1:
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i + 6, j, k1, Blocks.stonebrick, 0);
					for (int j1 = j + 1; j1 <= j + 4; j1++)
					{
						setBlockAndNotifyAdequately(world, i + 6, j1, k1, Blocks.air, 0);
					}
				}
				setBlockAndNotifyAdequately(world, i + 6, j + 7, k, LOTRMod.brick2, 0);
				placeWallBanner(world, i + 6, j + 7, k, 3, 8);
				break;
			case 2:
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j, k + 6, Blocks.stonebrick, 0);
					for (int j1 = j + 1; j1 <= j + 4; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k + 6, Blocks.air, 0);
					}
				}
				setBlockAndNotifyAdequately(world, i, j + 7, k + 6, LOTRMod.brick2, 0);
				placeWallBanner(world, i, j + 7, k + 6, 0, 8);
				break;
			case 3:
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i - 6, j, k1, Blocks.stonebrick, 0);
					for (int j1 = j + 1; j1 <= j + 4; j1++)
					{
						setBlockAndNotifyAdequately(world, i - 6, j1, k1, Blocks.air, 0);
					}
				}
				setBlockAndNotifyAdequately(world, i - 6, j + 7, k, LOTRMod.brick2, 0);
				placeWallBanner(world, i - 6, j + 7, k, 1, 8);
				break;
		}
		
		int radius = 6;
		for (int l = 0; l < 16; l++)
		{
			int i1 = i - random.nextInt(radius * 2) + random.nextInt(radius * 2);
			int k1 = k - random.nextInt(radius * 2) + random.nextInt(radius * 2);
			int j1 = world.getHeightValue(i1, k1);
			Block id = world.getBlock(i1, j1 - 1, k1);
			if (id == Blocks.grass || id == Blocks.dirt || id == Blocks.stone)
			{
				int randomFeature = random.nextInt(4);
				boolean flag = true;
				if (randomFeature == 0)
				{
					if (!LOTRMod.isOpaque(world, i1, j1, k1))
					{
						if (random.nextInt(3) == 0)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.slabSingle3, 4);
						}
						else
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.slabSingle3, 3);
						}
					}
				}
				else
				{
					for (int j2 = j1; j2 < j1 + randomFeature && flag; j2++)
					{
						flag = !LOTRMod.isOpaque(world, i1, j2, k1);
					}
					if (flag)
					{
						for (int j2 = j1; j2 < j1 + randomFeature; j2++)
						{
							if (random.nextBoolean())
							{
								setBlockAndNotifyAdequately(world, i1, j2, k1, LOTRMod.brick2, 0);
							}
							else
							{
								setBlockAndNotifyAdequately(world, i1, j2, k1, LOTRMod.brick2, 1);
							}
						}
					}
				}
				
				if (world.getBlock(i1, j1 - 1, k1) == Blocks.dirt)
				{
					setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
				}
			}
		}
		
		return true;
	}
	
	private void generateTowerSection(World world, Random random, int i, int j, int k, int section, boolean isTop)
	{
		j += section * 8;
		
		for (int j1 = (section == 0 ? j : j + 1); j1 <= (isTop ? j + 10 : j + 8); j1++)
		{
			Block fillBlock = Blocks.air;
			int fillMeta = 0;
			
			if (j1 == j)
			{
				fillBlock = Blocks.stonebrick;
				fillMeta = 0;
			}
			else
			{
				fillBlock = Blocks.air;
				fillMeta = 0;
			}
			
			boolean hasCeiling = j1 == j + 8 && !isTop;
			
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 5, j1, k1, fillBlock, fillMeta);
				setBlockAndNotifyAdequately(world, i + 5, j1, k1, fillBlock, fillMeta);
				
				if (hasCeiling && random.nextInt(20) != 0)
				{
					setBlockAndNotifyAdequately(world, i - 5, j1, k1, LOTRMod.slabSingle3, 11);
				}
				if (hasCeiling && random.nextInt(20) != 0)
				{
					setBlockAndNotifyAdequately(world, i + 5, j1, k1, LOTRMod.slabSingle3, 11);
				}
			}
			
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				for (int i1 = i - 4; i1 <= i - 3; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, fillBlock, fillMeta);
					
					if (hasCeiling && random.nextInt(20) != 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.slabSingle3, 11);
					}
				}
				for (int i1 = i + 3; i1 <= i + 4; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, fillBlock, fillMeta);
					
					if (hasCeiling && random.nextInt(20) != 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.slabSingle3, 11);
					}
				}
			}
			
			for (int k1 = k - 5; k1 <= k + 5; k1++)
			{
				for (int i1 = i - 2; i1 <= i + 2; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, fillBlock, fillMeta);
					
					if (hasCeiling && random.nextInt(20) != 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.slabSingle3, 11);
					}
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= (isTop ? j + 1 : j + 8); j1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				placeRandomBrick(world, random, i - 6, j1, k1);
				placeRandomBrick(world, random, i + 6, j1, k1);
			}
			
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				placeRandomBrick(world, random, i1, j1, k - 6);
				placeRandomBrick(world, random, i1, j1, k + 6);
			}
			
			placeRandomBrick(world, random, i - 5, j1, k - 4);
			placeRandomBrick(world, random, i - 5, j1, k - 3);
			placeRandomBrick(world, random, i - 5, j1, k + 3);
			placeRandomBrick(world, random, i - 5, j1, k + 4);
			placeRandomBrick(world, random, i - 4, j1, k - 5);
			placeRandomBrick(world, random, i - 4, j1, k + 5);
			placeRandomBrick(world, random, i - 3, j1, k - 5);
			placeRandomBrick(world, random, i - 3, j1, k + 5);
			placeRandomBrick(world, random, i + 3, j1, k - 5);
			placeRandomBrick(world, random, i + 3, j1, k + 5);
			placeRandomBrick(world, random, i + 4, j1, k - 5);
			placeRandomBrick(world, random, i + 4, j1, k + 5);
			placeRandomBrick(world, random, i + 5, j1, k - 4);
			placeRandomBrick(world, random, i + 5, j1, k - 3);
			placeRandomBrick(world, random, i + 5, j1, k + 3);
			placeRandomBrick(world, random, i + 5, j1, k + 4);
		}
		
		if (!isTop)
		{
			for (int j1 = j + 2; j1 <= j + 4; j1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					if (random.nextInt(3) != 0)
					{
						setBlockAndNotifyAdequately(world, i - 6, j1, k1, LOTRMod.orcSteelBars, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i - 6, j1, k1, Blocks.air, 0);
					}
					
					if (random.nextInt(3) != 0)
					{
						setBlockAndNotifyAdequately(world, i + 6, j1, k1, LOTRMod.orcSteelBars, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i + 6, j1, k1, Blocks.air, 0);
					}
				}
				
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					if (random.nextInt(3) != 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k - 6, LOTRMod.orcSteelBars, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k - 6, Blocks.air, 0);
					}
					
					if (random.nextInt(3) != 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k + 6, LOTRMod.orcSteelBars, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k + 6, Blocks.air, 0);
					}
				}
			}

		
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j + 8, k1, Blocks.air, 0);
				}
			}
		
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 1, LOTRMod.slabSingle3, 3);
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 2, LOTRMod.slabSingle3, 11);
			setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 2, LOTRMod.slabSingle3, 3);
			setBlockAndNotifyAdequately(world, i, j + 2, k + 2, LOTRMod.slabSingle3, 11);
			setBlockAndNotifyAdequately(world, i + 1, j + 3, k + 2, LOTRMod.slabSingle3, 3);
			setBlockAndNotifyAdequately(world, i + 2, j + 3, k + 2, LOTRMod.slabSingle3, 11);
			setBlockAndNotifyAdequately(world, i + 2, j + 4, k + 1, LOTRMod.slabSingle3, 3);
			setBlockAndNotifyAdequately(world, i + 2, j + 4, k, LOTRMod.slabSingle3, 11);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 1, LOTRMod.slabSingle3, 3);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 2, LOTRMod.slabSingle3, 11);
			setBlockAndNotifyAdequately(world, i + 1, j + 6, k - 2, LOTRMod.slabSingle3, 3);
			setBlockAndNotifyAdequately(world, i, j + 6, k - 2, LOTRMod.slabSingle3, 11);
			setBlockAndNotifyAdequately(world, i - 1, j + 7, k - 2, LOTRMod.slabSingle3, 3);
			setBlockAndNotifyAdequately(world, i - 2, j + 7, k - 2, LOTRMod.slabSingle3, 11);
			setBlockAndNotifyAdequately(world, i - 2, j + 8, k - 1, LOTRMod.slabSingle3, 3);
			setBlockAndNotifyAdequately(world, i - 2, j + 8, k, LOTRMod.slabSingle3, 11);
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				for (int j1 = j + 1; j1 <= (isTop ? j + 3: j + 8); j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
			}
		}

		if (isTop)
		{
			int top = 4 + random.nextInt(5);
			for (int j1 = j + 1; j1 <= j + top; j1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					placeRandomBrick(world, random, i - 7, j1, k1);
					placeRandomBrick(world, random, i + 7, j1, k1);
				}
				
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					placeRandomBrick(world, random, i1, j1, k - 7);
					placeRandomBrick(world, random, i1, j1, k + 7);
				}
			}
			
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				placeRandomStairs(world, random, i - 7, j, k1, 4);
				placeRandomStairs(world, random, i - 6, j + 2, k1, 1);
				
				placeRandomStairs(world, random, i + 7, j, k1, 5);
				placeRandomStairs(world, random, i + 6, j + 2, k1, 0);
			}
			
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				placeRandomStairs(world, random, i1, j, k - 7, 6);
				placeRandomStairs(world, random, i1, j + 2, k - 6, 3);
				
				placeRandomStairs(world, random, i1, j, k + 7, 7);
				placeRandomStairs(world, random, i1, j + 2, k + 6, 2);
			}
			
			for (int j1 = j; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 5, j1, k - 5, LOTRMod.brick2, 0);
				setBlockAndNotifyAdequately(world, i - 5, j1, k + 5, LOTRMod.brick2, 0);
				setBlockAndNotifyAdequately(world, i + 5, j1, k - 5, LOTRMod.brick2, 0);
				setBlockAndNotifyAdequately(world, i + 5, j1, k + 5, LOTRMod.brick2, 0);
			}
			
			placeBanner(world, i - 5, j + 5, k - 5, 0, 8);
			placeBanner(world, i - 5, j + 5, k + 5, 0, 8);
			placeBanner(world, i + 5, j + 5, k - 5, 0, 8);
			placeBanner(world, i + 5, j + 5, k + 5, 0, 8);
			
			placeRandomStairs(world, random, i - 5, j + 2, k - 4, 3);
			placeRandomStairs(world, random, i - 4, j + 2, k - 5, 1);
			
			placeRandomStairs(world, random, i - 5, j + 2, k + 4, 2);
			placeRandomStairs(world, random, i - 4, j + 2, k + 5, 1);
			
			placeRandomStairs(world, random, i + 5, j + 2, k - 4, 3);
			placeRandomStairs(world, random, i + 4, j + 2, k - 5, 0);
			
			placeRandomStairs(world, random, i + 5, j + 2, k + 4, 2);
			placeRandomStairs(world, random, i + 4, j + 2, k + 5, 0);
		}
	}
	
	private void placeRandomBrick(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(20) == 0)
		{
			return;
		}
		
		if (random.nextInt(3) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick2, 1);
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick2, 0);
		}
	}
	
	private void placeRandomStairs(World world, Random random, int i, int j, int k, int meta)
	{
		if (random.nextInt(10) == 0)
		{
			return;
		}
		
		if (random.nextInt(3) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.stairsAngmarBrickCracked, meta);
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.stairsAngmarBrick, meta);
		}
	}
}
