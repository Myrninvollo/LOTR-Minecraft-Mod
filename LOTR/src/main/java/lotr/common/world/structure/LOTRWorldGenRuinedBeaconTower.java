package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiomeGenIthilien;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenRuinedBeaconTower extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenRuinedBeaconTower(boolean flag)
	{
		super(flag);
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass || !(world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenIthilien))
			{
				return false;
			}
		}
		
		int height = 4 + random.nextInt(4);
		j += height;
		
		if (!restrictions && usingPlayer != null)
		{
			int rotation = usingPlayerRotation();
			switch (rotation)
			{
				case 0:
					k += 3;
					break;
				case 1:
					i -= 3;
					break;
				case 2:
					k -= 3;
					break;
				case 3:
					i += 3;
					break;
			}
		}
		
		if (restrictions)
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					for (int j1 = j; j1 <= j + 5; j1++)
					{
						if (!world.isAirBlock(i1, j1, k1))
						{
							return false;
						}
					}
				}
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				int j1 = j;
				if ((Math.abs(i1 - i) == 2 && Math.abs(k1 - k) < 2) || (Math.abs(k1 - k) == 2 && Math.abs(i1 - i) < 2))
				{
					j1 -= random.nextInt(4);
				}
				for (; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					placeRandomBrick(world, random, i1, j1, k1);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.slabDouble, 2);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 1, k, LOTRMod.rock, 1);
		
		for (int i1 = i - 2; i1 <= i + 2; i1 += 4)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1 += 4)
			{
				int j1 = j + 1 + random.nextInt(5);
				for (int j2 = j + 1; j2 <= j1; j2++)
				{
					placeRandomBrick(world, random, i1, j2, k1);
				}
			}
		}
		
		return true;
	}
	
	private void placeRandomBrick(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(5) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 2 + random.nextInt(2));
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 1);
		}
	}
	
	private void placeRandomSlab(World world, Random random, int i, int j, int k, boolean inverted)
	{
		if (random.nextInt(5) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle, 4 + random.nextInt(2) + (inverted ? 8 : 0));
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle, 3 + (inverted ? 8 : 0));
		}
	}
	
	private void placeRandomStairs(World world, Random random, int i, int j, int k, int metadata)
	{
		if (random.nextInt(5) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, random.nextBoolean() ? LOTRMod.stairsGondorBrickMossy : LOTRMod.stairsGondorBrickCracked, metadata);
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.stairsGondorBrick, metadata);
		}
	}
}