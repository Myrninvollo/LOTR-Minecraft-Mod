package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityGondorRuinsWraith;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.biome.LOTRBiomeGenIthilien;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTRWorldGenGondorRuins extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenGondorRuins()
	{
		super(false);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (world.getBlock(i, j - 1, k) != Blocks.grass)
		{
			return false;
		}
		else
		{
			BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
			if (biome != LOTRBiome.gondor && !(biome instanceof LOTRBiomeGenIthilien))
			{
				return false;
			}
		}
		
		int slabRuinParts = 3 + random.nextInt(4);
		for (int l = 0; l < slabRuinParts; l++)
		{
			int i1 = i - 5 + random.nextInt(10);
			int k1 = k - 5 + random.nextInt(10);
			if (i1 == i && k1 == k)
			{
				continue;
			}

			int j1 = world.getHeightValue(i1, k1);
			if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
			{
				placeRandomSlab(world, random, i1, j1, k1);
				world.setBlock(i1, j1 - 1, k1, Blocks.dirt, 0, 2);
			}
		}
		
		int smallRuinParts = 3 + random.nextInt(4);
		for (int l = 0; l < smallRuinParts; l++)
		{
			int i1 = i - 5 + random.nextInt(10);
			int k1 = k - 5 + random.nextInt(10);
			if (i1 == i && k1 == k)
			{
				continue;
			}

			int j1 = world.getHeightValue(i1, k1);
			if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
			{
				int height = 1 + random.nextInt(3);
				for (int j2 = 0; j2 < height; j2++)
				{
					placeRandomBrick(world, random, i1, j1 + j2, k1);
				}
				world.setBlock(i1, j1 - 1, k1, Blocks.dirt, 0, 2);
			}
		}

		int largeRuinParts = 3 + random.nextInt(5);
		for (int l = 0; l < largeRuinParts; l++)
		{
			int i1 = i - 5 + random.nextInt(10);
			int k1 = k - 5 + random.nextInt(10);
			if (i1 == i && k1 == k)
			{
				continue;
			}

			int j1 = world.getHeightValue(i1, k1);
			if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
			{
				int height = 4 + random.nextInt(7);
				for (int j2 = 0; j2 < height; j2++)
				{
					placeRandomBrick(world, random, i1, j1 + j2, k1);
				}
				world.setBlock(i1, j1 - 1, k1, Blocks.dirt, 0, 2);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int j1 = j - 2; j1 >= j - 5; j1--)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					if (!LOTRMod.isOpaque(world, i1, j1, k1))
					{
						return true;
					}
				}
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 8; i1++)
		{
			for (int j1 = j - 6; j1 >= j - 11; j1--)
			{
				for (int k1 = k - 3; k1 <= k + 3; k1++)
				{
					if (!LOTRMod.isOpaque(world, i1, j1, k1))
					{
						return true;
					}
				}
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 8; i1++)
		{
			for (int j1 = j - 6; j1 >= j - 11; j1--)
			{
				for (int k1 = k - 3; k1 <= k + 3; k1++)
				{
					if (j1 == j - 6 || j1 < j - 9)
					{
						world.setBlock(i1, j1, k1, LOTRMod.rock, 1, 2);
					}
					else
					{
						world.setBlock(i1, j1, k1, LOTRMod.brick, 1, 2);
					}
				}
			}
		}
		
		for (int i1 = i; i1 <= i + 7; i1++)
		{
			for (int j1 = j - 7; j1 >= j - 9; j1--)
			{
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					world.setBlock(i1, j1, k1, Blocks.air, 0, 2);
				}
			}
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			world.setBlock(i + 7, j - 9, k1, LOTRMod.brick, 1, 2);
			world.setBlock(i + 7, j - 7, k1, LOTRMod.slabSingle, 11, 2);
			world.setBlock(i, j - 7, k1, LOTRMod.slabSingle, 11, 2);
		}
		
		for (int i1 = i + 1; i1 <= i + 5; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				world.setBlock(i1, j - 10, k1, LOTRMod.slabDouble, 2, 2);
			}
		}
		
		world.setBlock(i + 2, j - 9, k, LOTRMod.slabSingle, 2, 2);
		world.setBlock(i + 3, j - 9, k, LOTRMod.slabSingle, 2, 2);
		world.setBlock(i + 4, j - 9, k, LOTRMod.slabSingle, 2, 2);
		
		placeSpawnerChest(world, i + 4, j - 10, k, 4, LOTREntityGondorRuinsWraith.class);
		LOTRChestContents.fillChest(world, random, i + 4, j - 10, k, LOTRChestContents.GONDOR_RUINS_TREASURE);
		world.setBlock(i + 2, j - 10, k, Blocks.chest, 4, 2);
		LOTRChestContents.fillChest(world, random, i + 2, j - 10, k, LOTRChestContents.GONDOR_RUINS_BONES);
		
		for (int j1 = j - 2; j1 >= j - 9; j1--)
		{
			world.setBlock(i, j1, k, Blocks.ladder, 5, 2);
		}
		
		world.setBlock(i, j - 1, k, LOTRMod.brick, 5, 2);
		
		return true;
	}

	private void placeRandomBrick(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(4) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 2 + random.nextInt(2));
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 1);
		}
	}
	
	private void placeRandomSlab(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(4) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle, 4 + random.nextInt(2));
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle, 3);
		}
	}
}
