package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiomeGenEregion;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenEregionRuin extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenEregionRuin()
	{
		super(false);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (!(world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenEregion))
			{
				return false;
			}
			Block block = world.getBlock(i, j - 1, k);
			if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone)
			{
				return false;
			}
		}
		
		boolean generateColumn = random.nextBoolean();
		if (generateColumn)
		{
			int width = 1 + random.nextInt(3);
			int minHeight = j;
			int maxHeight = j;
			
			for (int i1 = i; i1 <= i + width && generateColumn; i1++)
			{
				for (int k1 = k; k1 <= k + width && generateColumn; k1++)
				{
					int j1 = world.getHeightValue(i1, k1);
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
						generateColumn = false;
					}
					
					Block block = world.getBlock(i1, j1 - 1, k1);
					if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone)
					{
						generateColumn = false;
					}
				}
			}
		
			if (generateColumn)
			{
				int baseHeight = 3 + random.nextInt(4) + random.nextInt(width * 2);
				for (int i1 = i; i1 <= i + width; i1++)
				{
					for (int k1 = k; k1 <= k + width; k1++)
					{
						int height = baseHeight + random.nextInt(6);
						for (int j1 = j + height; j1 >= minHeight; j1--)
						{
							placeRandomBrick(world, random, i1, j1, k1);
							if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
							{
								setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
							}
						}
					}
				}
			}
		}
		
		int radius = 6;
		int ruinParts = 2 + random.nextInt(6);
		for (int l = 0; l < ruinParts; l++)
		{
			int i1 = i - random.nextInt(radius * 2) + random.nextInt(radius * 2);
			int k1 = k - random.nextInt(radius * 2) + random.nextInt(radius * 2);
			int j1 = world.getHeightValue(i1, k1);
			Block block = world.getBlock(i1, j1 - 1, k1);
			if (block == Blocks.grass || block == Blocks.dirt || block == Blocks.stone)
			{
				int randomFeature = random.nextInt(4);
				boolean flag = true;
				if (randomFeature == 0)
				{
					if (!LOTRMod.isOpaque(world, i1, j1, k1))
					{
						if (random.nextInt(4) == 0)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.slabSingle2, 6 + random.nextInt(2));
						}
						else
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.slabSingle2, 3 + random.nextInt(3));
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
						if (random.nextInt(4) == 0)
						{
							for (int j2 = j1; j2 < j1 + randomFeature; j2++)
							{
								placeRandomPillar(world, random, i1, j2, k1);
							}
						}
						else
						{
							for (int j2 = j1; j2 < j1 + randomFeature; j2++)
							{
								placeRandomBrick(world, random, i1, j2, k1);
							}
						}
					}
				}
				
				if (flag && world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
				{
					setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
				}
			}
		}
		
		return true;
	}
	
	private void placeRandomBrick(World world, Random random, int i, int j, int k)
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
	
	private void placeRandomPillar(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(3) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.pillar, 2);
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.pillar, 1);
		}
	}
}
