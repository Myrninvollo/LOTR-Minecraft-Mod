package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenRuinedHouse extends LOTRWorldGenStructureBase2
{
	public LOTRWorldGenRuinedHouse(boolean flag)
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
		
		int width = 4 + random.nextInt(3);
		
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
		
		if (restrictions)
		{
			int minHeight = 1;
			int maxHeight = 1;
			
			for (int i1 = -width; i1 <= width; i1++)
			{
				for (int k1 = -width; k1 <= width; k1++)
				{
					int j1 = getTopBlock(world, i1, k1);
					Block block = getBlock(world, i1, j1 - 1, k1);
					if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone)
					{
						return false;
					}
					
					if (j1 > maxHeight)
					{
						maxHeight = j1;
					}
					if (j1 < minHeight)
					{
						minHeight = j1;
					}
				}
			}
			
			if (Math.abs(maxHeight - minHeight) > 5)
			{
				return false;
			}
		}
		
		for (int i1 = -width; i1 <= width; i1++)
		{
			for (int k1 = -width; k1 <= width; k1++)
			{
				for (int j1 = 0; j1 <= 5; j1++)
				{
					setAir(world, i1, j1, k1);
				}
				
				for (int j1 = 0; !isOpaque(world, i1, j1, k1) && getY(j1) >= 0; j1--)
				{
					placeRandomGroundBlock(world, random, i1, j1, k1);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
			}
		}
		
		for (int k1 = -width; k1 <= width; k1++)
		{
			placeRandomWallOrStone(world, random, -width, 1, -k1);
			placeRandomWallOrStone(world, random, width, 1, k1);
			
			placeRandomWall(world, random, -width, 2, k1, true);
			placeRandomWall(world, random, width, 2, k1, true);
			
			placeRandomWall(world, random, -width, 3, k1, true);
			placeRandomWall(world, random, width, 3, k1, true);
		}
		
		for (int i1 = -width; i1 <= width; i1++)
		{
			placeRandomWallOrStone(world, random, i1, 1, width);
			
			if (random.nextInt(3) == 0)
			{
				placeRandomWallOrStone(world, random, i1, 2, width - 1);
			}
			
			placeRandomWall(world, random, i1, 2, width, false);
			
			placeRandomWall(world, random, i1, 3, width, false);
		}
		
		for (int i1 = -width + 1; i1 <= -1; i1++)
		{
			if (random.nextInt(4) == 0)
			{
				break;
			}
			placeRandomWallOrStone(world, random, i1, 1, -width);
		}
		
		for (int i1 = width - 1; i1 >= 1; i1--)
		{
			if (random.nextInt(4) == 0)
			{
				break;
			}
			placeRandomWallOrStone(world, random, i1, 1, -width);
		}
		
		setBlockAndMetadata(world, -width + 1, 2, -width, Blocks.fence, 0);
		setBlockAndMetadata(world, width - 1, 2, -width, Blocks.fence, 0);
		
		placeWoodPillar(world, random, -width, 1, -width);
		placeWoodPillar(world, random, width, 1, -width);
		placeWoodPillar(world, random, -width, 1, width);
		placeWoodPillar(world, random, width, 1, width);
		
		if (random.nextBoolean())
		{
			setBlockAndMetadata(world, width - 1, 1, -width + 1, Blocks.cobblestone, 0);
			setBlockAndMetadata(world, width - 1, 1, -width + 2, Blocks.furnace, 0);
		}
		else
		{
			setBlockAndMetadata(world, -width + 1, 1, -width + 1, Blocks.cobblestone, 0);
			setBlockAndMetadata(world, -width + 1, 1, -width + 2, Blocks.furnace, 0);
		}
		
		if (random.nextBoolean())
		{
			placeChest(world, random, width - 1, 1, width - 2, 0, LOTRChestContents.RUINED_HOUSE);
		}
		else
		{
			placeChest(world, random, -width + 1, 1, width - 2, 0, LOTRChestContents.RUINED_HOUSE);
		}
		
		return true;
	}
	
	private void placeRandomGroundBlock(World world, Random random, int i, int j, int k)
	{
		int l = random.nextInt(4);
		if (l == 0)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.dirt, 1);
		}
		else if (l == 1)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.gravel, 0);
		}
		else if (l == 2)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.cobblestone, 0);
		}
		else if (l == 3)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.mossy_cobblestone, 0);
		}
	}
	
	private void placeRandomWallOrStone(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(12) == 0)
		{
			return;
		}
		
		if (isAir(world, i, j - 1, k))
		{
			return;
		}
		
		int l = random.nextInt(4);
		if (l == 0)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.fence, 0);
		}
		else if (l == 1)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.planks, 0);
		}
		else if (l == 2)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.cobblestone, 0);
		}
		else if (l == 3)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.mossy_cobblestone, 0);
		}
	}
	
	private void placeRandomWall(World world, Random random, int i, int j, int k, boolean northToSouth)
	{
		if (random.nextInt(12) == 0)
		{
			return;
		}
		
		if (isAir(world, i, j - 1, k))
		{
			return;
		}
		
		int l = random.nextInt(4);
		if (l == 0)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.fence, 0);
		}
		else if (l == 1)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.planks, 0);
		}
		else if (l == 2)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.log, northToSouth ? 8 : 4);
		}
		else if (l == 3)
		{
			int upsideDown = random.nextBoolean() ? 4 : 0;
			if (northToSouth)
			{
				setBlockAndMetadata(world, i, j, k, Blocks.oak_stairs, (random.nextInt(2)) | upsideDown);
			}
			else
			{
				setBlockAndMetadata(world, i, j, k, Blocks.oak_stairs, (2 + random.nextInt(2)) | upsideDown);
			}
		}
	}
	
	private void placeWoodPillar(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j; j1 <= j + 4; j1++)
		{
			setBlockAndMetadata(world, i, j1, k, Blocks.log, 0);
			
			if (random.nextInt(4) == 0 && j1 >= j + 2)
			{
				break;
			}
		}
	}
}
