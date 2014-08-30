package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiomeGenDolGuldur;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenDolGuldurAltar extends LOTRWorldGenStructureBase2
{
	public LOTRWorldGenDolGuldurAltar(boolean flag)
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
		
		setRotationMode(rotation);
		switch (getRotationMode())
		{
			case 0:
				k += 6;
				break;
			case 1:
				i -= 6;
				break;
			case 2:
				k -= 6;
				break;
			case 3:
				i += 6;
				break;
		}
		
		setOrigin(i, j, k);
		
		if (restrictions)
		{
			for (int i1 = -5; i1 <= 5; i1++)
			{
				for (int k1 = -5; k1 <= 5; k1++)
				{
					int j1 = getTopBlock(world, i1, k1);
					Block block = getBlock(world, i1, j1 - 1, k1);
					if (block != Blocks.stone && block != Blocks.dirt && block != Blocks.grass)
					{
						return false;
					}
				}
			}
		}
		
		for (int i1 = -5; i1 <= 5; i1++)
		{
			for (int k1 = -5; k1 <= 5; k1++)
			{
				for (int j1 = 0; !isOpaque(world, i1, j1, k1) && getY(j1) >= 0; j1--)
				{
					placeRandomBrick(world, random, i1, j1, k1);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
				
				for (int j1 = 1; j1 <= 8; j1++)
				{
					setAir(world, i1, j1, k1);
				}
				
				int i2 = Math.abs(i1);
				int k2 = Math.abs(k1);
				
				if (i2 <= 4 && k2 <= 4)
				{
					placeRandomBrick(world, random, i1, 1, k1);
				}
				
				if (i2 <= 3 && k2 <= 3)
				{
					if (random.nextInt(10) == 0)
					{
						setBlockAndMetadata(world, i1, 2, k1, LOTRMod.guldurilBrick, 4);
					}
					else
					{
						placeRandomBrick(world, random, i1, 2, k1);
					}
				}
			}
		}
		
		for (int i1 = -5; i1 <= 5; i1++)
		{
			placeRandomStairs(world, random, i1, 1, -5, 2);
			placeRandomStairs(world, random, i1, 1, 5, 3);
		}
		
		for (int k1 = -5; k1 <= 5; k1++)
		{
			placeRandomStairs(world, random, -5, 1, k1, 1);
			placeRandomStairs(world, random, 5, 1, k1, 0);
		}
		
		for (int i1 = -4; i1 <= 4; i1++)
		{
			placeRandomStairs(world, random, i1, 2, -4, 2);
			placeRandomStairs(world, random, i1, 2, 4, 3);
		}
		
		for (int k1 = -4; k1 <= 4; k1++)
		{
			placeRandomStairs(world, random, -4, 2, k1, 1);
			placeRandomStairs(world, random, 4, 2, k1, 0);
		}
		
		for (int i1 = -1; i1 <= 1; i1++)
		{
			placeRandomStairs(world, random, i1, 3, -1, 2);
			placeRandomStairs(world, random, i1, 3, 1, 3);
		}
		
		for (int k1 = -1; k1 <= 1; k1++)
		{
			placeRandomStairs(world, random, -1, 3, k1, 1);
			placeRandomStairs(world, random, 1, 3, k1, 0);
		}
		
		placeRandomBrick(world, random, 0, 3, 0);
		setBlockAndMetadata(world, 0, 4, 0, LOTRMod.dolGuldurTable, 0);
		
		for (int x = -4; x <= 3; x += 7)
		{
			for (int z = -4; z <= 3; z += 7)
			{
				for (int i1 = x; i1 <= x + 1; i1++)
				{
					for (int k1 = z; k1 <= z + 1; k1++)
					{
						for (int j1 = 2; j1 <= 5; j1++)
						{
							placeRandomBrick(world, random, i1, j1, k1);
						}
					}
				}
			}
		}
		
		setBlockAndMetadata(world, -4, 6, -4, LOTRMod.wall2, 8);
		setBlockAndMetadata(world, -4, 7, -4, LOTRMod.morgulTorch, 5);
		
		setBlockAndMetadata(world, 4, 6, -4, LOTRMod.wall2, 8);
		setBlockAndMetadata(world, 4, 7, -4, LOTRMod.morgulTorch, 5);
		
		setBlockAndMetadata(world, -4, 6, 4, LOTRMod.wall2, 8);
		setBlockAndMetadata(world, -4, 7, 4, LOTRMod.morgulTorch, 5);
		
		setBlockAndMetadata(world, 4, 6, 4, LOTRMod.wall2, 8);
		setBlockAndMetadata(world, 4, 7, 4, LOTRMod.morgulTorch, 5);
		
		for (int i1 = -4; i1 <= 4; i1 += 8)
		{
			placeRandomStairs(world, random, i1, 6, -3, 2);
			placeRandomStairs(world, random, i1, 6, -2, 7);
			setBlockAndMetadata(world, i1, 7, -2, LOTRMod.guldurilBrick, 4);
			placeRandomStairs(world, random, i1, 8, -2, 2);
			placeRandomStairs(world, random, i1, 8, -1, 7);
			
			placeRandomStairs(world, random, i1, 6, 3, 3);
			placeRandomStairs(world, random, i1, 6, 2, 6);
			setBlockAndMetadata(world, i1, 7, 2, LOTRMod.guldurilBrick, 4);
			placeRandomStairs(world, random, i1, 8, 2, 3);
			placeRandomStairs(world, random, i1, 8, 1, 6);
		}
		
		for (int k1 = -4; k1 <= 4; k1 += 8)
		{
			placeRandomStairs(world, random, -3, 6, k1, 1);
			placeRandomStairs(world, random, -2, 6, k1, 4);
			setBlockAndMetadata(world, -2, 7, k1, LOTRMod.guldurilBrick, 4);
			placeRandomStairs(world, random, -2, 8, k1, 1);
			placeRandomStairs(world, random, -1, 8, k1, 4);
			
			placeRandomStairs(world, random, 3, 6, k1, 0);
			placeRandomStairs(world, random, 2, 6, k1, 5);
			setBlockAndMetadata(world, 2, 7, k1, LOTRMod.guldurilBrick, 4);
			placeRandomStairs(world, random, 2, 8, k1, 0);
			placeRandomStairs(world, random, 1, 8, k1, 5);
		}
		
		return true;
	}
	
	private void placeRandomBrick(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(4) == 0)
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
		if (random.nextInt(4) == 0)
		{
			setBlockAndMetadata(world, i, j, k, LOTRMod.stairsDolGuldurBrickCracked, meta);
		}
		else
		{
			setBlockAndMetadata(world, i, j, k, LOTRMod.stairsDolGuldurBrick, meta);
		}
	}
}
