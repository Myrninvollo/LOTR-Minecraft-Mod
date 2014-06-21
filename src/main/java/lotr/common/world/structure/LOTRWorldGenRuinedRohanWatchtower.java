package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenRuinedRohanWatchtower extends LOTRWorldGenStructureBase
{
	private Block plankBlock = LOTRMod.planks;
	private int plankMeta = 3;
	
	private Block woodBlock = LOTRMod.wood;
	private int woodMeta = 3;
	
	private Block stairBlock = LOTRMod.stairsCharred;
	
	public LOTRWorldGenRuinedRohanWatchtower(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass || world.getBiomeGenForCoords(i, k) != LOTRBiome.rohanUrukHighlands)
			{
				return false;
			}
		}
		
		int height = 5 + random.nextInt(4);
		j += height;
		
		if (restrictions)
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				for (int j1 = j - 3; j1 <= j + 4; j1++)
				{
					for (int k1 = k - 4; k1 <= k + 4; k1++)
					{
						if (!world.isAirBlock(i1, j1, k1))
						{
							return false;
						}
					}
				}
			}
		}
		
		generateBasicStructure(world, random, i, j, k);
		
		int rotation = random.nextInt(4);
		
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
		}
		
		switch (rotation)
		{
			case 0:
				return generateFacingSouth(world, random, i, j, k);
			case 1:
				return generateFacingWest(world, random, i, j, k);
			case 2:
				return generateFacingNorth(world, random, i, j, k);
			case 3:
				return generateFacingEast(world, random, i, j, k);
		}
		
		return true;
	}
	
	private void generateBasicStructure(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j + 3 - random.nextInt(8); !LOTRMod.isOpaque(world, i - 3, j1, k - 3) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k - 3, plankBlock, plankMeta);
		}
		for (int j1 = j + 3 - random.nextInt(8); !LOTRMod.isOpaque(world, i - 3, j1, k + 3) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k + 3, plankBlock, plankMeta);
		}
		for (int j1 = j + 3 - random.nextInt(8); !LOTRMod.isOpaque(world, i + 3, j1, k - 3) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i + 3, j1, k - 3, plankBlock, plankMeta);
		}
		for (int j1 = j + 3 - random.nextInt(8); !LOTRMod.isOpaque(world, i + 3, j1, k + 3) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i + 3, j1, k + 3, plankBlock, plankMeta);
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				if (random.nextInt(4) == 0)
				{
					continue;
				}
				setBlockAndNotifyAdequately(world, i1, j, k1, plankBlock, plankMeta);
			}
		}
		
		for (int i1 = i - 2 + random.nextInt(3); i1 <= i + 2 - random.nextInt(3); i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 3, woodBlock, woodMeta | 4);	
		}
		
		for (int i1 = i - 2 + random.nextInt(3); i1 <= i + 2 - random.nextInt(3); i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k + 3, woodBlock, woodMeta | 4);
		}
		
		for (int i1 = i - 2 + random.nextInt(3); i1 <= i + 2 - random.nextInt(3); i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 4, stairBlock, 6);
		}
		
		for (int i1 = i - 2 + random.nextInt(3); i1 <= i + 2 - random.nextInt(3); i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k + 4, stairBlock, 7);
		}
		
		for (int k1 = k - 2 + random.nextInt(3); k1 <= k + 2 - random.nextInt(3); k1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j, k1, woodBlock, woodMeta | 8);
		}
		
		for (int k1 = k - 2 + random.nextInt(3); k1 <= k + 2 - random.nextInt(3); k1++)
		{
			setBlockAndNotifyAdequately(world, i + 3, j, k1, woodBlock, woodMeta | 8);
		}
		
		for (int k1 = k - 2 + random.nextInt(3); k1 <= k + 2 - random.nextInt(3); k1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j, k1, stairBlock, 4);
		}
		
		for (int k1 = k - 2 + random.nextInt(3); k1 <= k + 2 - random.nextInt(3); k1++)
		{
			setBlockAndNotifyAdequately(world, i + 4, j, k1, stairBlock, 5);
		}
	}
	
	private boolean generateFacingSouth(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j - 1 - random.nextInt(4); !LOTRMod.isOpaque(world, i, j1, k + 3) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i, j1, k + 3, plankBlock, plankMeta);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			int k2 = Math.abs(k - k1);
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i - 3, j1, k1) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((k2 == 2 && j2 % 4 == 1) || (k2 == 1 && j2 % 2 == 0) || (k2 == 0 && j2 % 4 == 3) && random.nextInt(3) == 0)
				{
					setBlockAndNotifyAdequately(world, i - 3, j1, k1, woodBlock, woodMeta);
				}
			}
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i + 3, j1, k1) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((k2 == 2 && j2 % 4 == 1) || (k2 == 1 && j2 % 2 == 0) || (k2 == 0 && j2 % 4 == 3) && random.nextInt(3) == 0)
				{
					setBlockAndNotifyAdequately(world, i + 3, j1, k1, woodBlock, woodMeta);
				}
			}
		}

		return true;
	}
	
	private boolean generateFacingWest(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j - 1 - random.nextInt(4); !LOTRMod.isOpaque(world, i - 3, j1, k) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k, plankBlock, plankMeta);
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			int i2 = Math.abs(i - i1);
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k - 3) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((i2 == 2 && j2 % 4 == 1) || (i2 == 1 && j2 % 2 == 0) || (i2 == 0 && j2 % 4 == 3) && random.nextInt(3) == 0)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 3, woodBlock, woodMeta);
				}
			}
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k + 3) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((i2 == 2 && j2 % 4 == 1) || (i2 == 1 && j2 % 2 == 0) || (i2 == 0 && j2 % 4 == 3) && random.nextInt(3) == 0)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k + 3, woodBlock, woodMeta);
				}
			}
		}

		return true;
	}
	
	private boolean generateFacingNorth(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j - 1 - random.nextInt(4); !LOTRMod.isOpaque(world, i, j1, k - 3) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i, j1, k - 3, plankBlock, plankMeta);
		}

		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			int k2 = Math.abs(k - k1);
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i - 3, j1, k1) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((k2 == 2 && j2 % 4 == 1) || (k2 == 1 && j2 % 2 == 0) || (k2 == 0 && j2 % 4 == 3) && random.nextInt(3) == 0)
				{
					setBlockAndNotifyAdequately(world, i - 3, j1, k1, woodBlock, woodMeta);
				}
			}
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i + 3, j1, k1) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((k2 == 2 && j2 % 4 == 1) || (k2 == 1 && j2 % 2 == 0) || (k2 == 0 && j2 % 4 == 3) && random.nextInt(3) == 0)
				{
					setBlockAndNotifyAdequately(world, i + 3, j1, k1, woodBlock, woodMeta);
				}
			}
		}
		
		return true;
	}
	
	private boolean generateFacingEast(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j - 1 - random.nextInt(4); !LOTRMod.isOpaque(world, i + 3, j1, k) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i + 3, j1, k, plankBlock, plankMeta);
		}

		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			int i2 = Math.abs(i - i1);
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k - 3) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((i2 == 2 && j2 % 4 == 1) || (i2 == 1 && j2 % 2 == 0) || (i2 == 0 && j2 % 4 == 3) && random.nextInt(3) == 0)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 3, woodBlock, woodMeta);
				}
			}
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k + 3) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((i2 == 2 && j2 % 4 == 1) || (i2 == 1 && j2 % 2 == 0) || (i2 == 0 && j2 % 4 == 3) && random.nextInt(3) == 0)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k + 3, woodBlock, woodMeta);
				}
			}
		}

		return true;
	}
}
