package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenBigTrees;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;

public class LOTRWorldGenNumenorRuin extends LOTRWorldGenStructureBase2
{
	public LOTRWorldGenNumenorRuin(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass)
			{
				return false;
			}
		}
		
		j--;
		
		int width = 3 + random.nextInt(3);
		
		setRotationMode(rotation);
		
		switch (getRotationMode())
		{
			case 0:
				k += width + 1;
				break;
			case 1:
				i -= width + 1;
				break;
			case 2:
				k -= width + 1;
				break;
			case 3:
				i += width + 1;
				break;
		}
		
		setOrigin(i, j, k);
		
		for (int i1 = -width; i1 <= width; i1++)
		{
			for (int k1 = -width; k1 <= width; k1++)
			{
				if (Math.abs(i1) == width || Math.abs(k1) == width)
				{
					for (int j1 = 0; !isOpaque(world, i1, j1, k1) && getY(j1) >= 0; j1--)
					{
						placeRandomBrick(world, random, i1, j1, k1);
						setGrassToDirt(world, i1, j1 - 1, k1);
					}
				}
				else
				{
					setBlockAndMetadata(world, i1, 0, k1, Blocks.grass, 0);
					for (int j1 = -1; !isOpaque(world, i1, j1, k1) && getY(j1) >= 0; j1--)
					{
						setBlockAndMetadata(world, i1, j1, k1, Blocks.dirt, 0);
						setGrassToDirt(world, i1, j1 - 1, k1);
					}
				}
			}
		}
		
		if (random.nextBoolean())
		{
			new WorldGenBigTree(notifyChanges).generate(world, random, originX, originY + 1, originZ);
		}
		else
		{
			LOTRWorldGenBigTrees.newBeech(notifyChanges).generate(world, random, originX, originY + 1, originZ);
		}
		
		for (int i1 = -width; i1 <= width; i1++)
		{
			for (int k1 = -width; k1 <= width; k1++)
			{
				if (Math.abs(i1) == width || Math.abs(k1) == width)
				{
					int height = width * 2 + random.nextInt(8);
					for (int j1 = 1; j1 < height; j1++)
					{
						placeRandomBrick(world, random, i1, j1, k1);
					}
				}
			}
		}
		
		setAir(world, 0, 1, -width);
		setAir(world, 0, 2, -width);
		
		int ruins = 10 + random.nextInt(20);
		for (int l = 0; l < ruins; l++)
		{
			int i1 = -width * 2 + random.nextInt(width * 2 + 1);
			int k1 = -width * 2 + random.nextInt(width * 2 + 1);
			int j1 = getTopBlock(world, i1, k1);
			Block block = getBlock(world, i1, j1 - 1, k1);
			if (block == Blocks.grass || block == Blocks.dirt || block == Blocks.stone)
			{
				int l1 = random.nextInt(3);
				if (l1 == 0)
				{
					setBlockAndMetadata(world, i1, j1 - 1, k1, Blocks.gravel, 0);
				}
				else if (l1 == 1)
				{
					placeRandomBrick(world, random, i1, j1 - 1, k1);
				}
				else if (l1 == 2)
				{
					int height = 1 + random.nextInt(3);
					for (int j2 = j1; j2 < j1 + height; j2++)
					{
						if (isOpaque(world, i1, j2, k1))
						{
							break;
						}
						
						placeRandomBrick(world, random, i1, j2, k1);
					}
				}
			}
		}
		
		return true;
	}
	
	private void placeRandomBrick(World world, Random random, int i, int j, int k)
	{
		int l = random.nextInt(5);
		if (l == 0)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.stonebrick, 0);
		}
		else if (l == 1)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.stonebrick, 1);
		}
		else if (l == 2)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.stonebrick, 2);
		}
		else if (l == 3)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.cobblestone, 0);
		}
		else if (l == 4)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.mossy_cobblestone, 0);
		}
	}
}
