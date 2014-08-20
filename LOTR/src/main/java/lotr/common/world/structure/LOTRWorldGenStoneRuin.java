package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class LOTRWorldGenStoneRuin extends LOTRWorldGenStructureBase
{
	private int minWidth;
	private int maxWidth;
	
	private LOTRWorldGenStoneRuin(int i, int j)
	{
		super(false);
		minWidth = i;
		maxWidth = j;
	}
	
	public static class ANGMAR extends LOTRWorldGenStoneRuin
	{
		public ANGMAR(int i, int j)
		{
			super(i, j);
		}
		
		@Override
		protected void placeRandomBrick(World world, Random random, int i, int j, int k)
		{
			int l = random.nextInt(2);
			switch (l)
			{
				case 0:
					setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick2, 0);
					break;
				case 1:
					setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick2, 1);
					break;
			}
		}
		
		@Override
		protected void placeRandomSlab(World world, Random random, int i, int j, int k)
		{
			if (random.nextInt(4) == 0)
			{
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle3, 4);
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle3, 3);
			}
		}
	}
	
	public static class STONE extends LOTRWorldGenStoneRuin
	{
		public STONE(int i, int j)
		{
			super(i, j);
		}
		
		@Override
		protected void placeRandomBrick(World world, Random random, int i, int j, int k)
		{
			int l = random.nextInt(3);
			switch (l)
			{
				case 0:
					setBlockAndNotifyAdequately(world, i, j, k, Blocks.stonebrick, 0);
					break;
				case 1:
					setBlockAndNotifyAdequately(world, i, j, k, Blocks.stonebrick, 1);
					break;
				case 2:
					setBlockAndNotifyAdequately(world, i, j, k, Blocks.stonebrick, 2);
					break;
			}
		}
		
		@Override
		protected void placeRandomSlab(World world, Random random, int i, int j, int k)
		{
			if (random.nextInt(4) == 0)
			{
				setBlockAndNotifyAdequately(world, i, j, k, Blocks.stone_slab, 0);
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j, k, Blocks.stone_slab, 5);
			}
		}
	}
	
	public static class ARNOR extends LOTRWorldGenStoneRuin
	{
		public ARNOR(int i, int j)
		{
			super(i, j);
		}
		
		@Override
		protected void placeRandomBrick(World world, Random random, int i, int j, int k)
		{
			int l = random.nextInt(3);
			switch (l)
			{
				case 0:
					setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick2, 3);
					break;
				case 1:
					setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick2, 4);
					break;
				case 2:
					setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick2, 5);
					break;
			}
		}
		
		@Override
		protected void placeRandomSlab(World world, Random random, int i, int j, int k)
		{
			if (random.nextInt(4) == 0)
			{
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle4, 2 + random.nextInt(2));
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle4, 1);
			}
		}
	}
	
	public static class ELVEN extends LOTRWorldGenStoneRuin
	{
		public ELVEN(int i, int j)
		{
			super(i, j);
		}
		
		@Override
		protected void placeRandomBrick(World world, Random random, int i, int j, int k)
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
		
		@Override
		protected void placeRandomSlab(World world, Random random, int i, int j, int k)
		{
			if (random.nextInt(4) == 0)
			{
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle2, 6 + random.nextInt(2));
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle2, 3 + random.nextInt(3));
			}
		}
	}
	
	public static class DOL_GULDUR extends LOTRWorldGenStoneRuin
	{
		public DOL_GULDUR(int i, int j)
		{
			super(i, j);
		}
		
		@Override
		protected void placeRandomBrick(World world, Random random, int i, int j, int k)
		{
			int l = random.nextInt(2);
			switch (l)
			{
				case 0:
					setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick2, 8);
					break;
				case 1:
					setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick2, 9);
					break;
			}
		}
		
		@Override
		protected void placeRandomSlab(World world, Random random, int i, int j, int k)
		{
			if (random.nextInt(4) == 0)
			{
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle4, 6);
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle4, 5);
			}
		}
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			Block block = world.getBlock(i, j - 1, k);
			if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone)
			{
				return false;
			}
		}
		
		int width = MathHelper.getRandomIntegerInRange(random, minWidth, maxWidth);
		boolean generateColumn = random.nextInt(3) > 0;
		if (generateColumn)
		{
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
				int baseHeight = 4 + random.nextInt(4) + random.nextInt(width * 3);
				for (int i1 = i; i1 <= i + width; i1++)
				{
					for (int k1 = k; k1 <= k + width; k1++)
					{
						int height = (int)((float)baseHeight * (1F + random.nextFloat()));
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
		
		int radius = width * 2;
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
						placeRandomSlab(world, random, i1, j1, k1);
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
							placeRandomBrick(world, random, i1, j2, k1);
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
	
	protected abstract void placeRandomBrick(World world, Random random, int i, int j, int k);
	
	protected abstract void placeRandomSlab(World world, Random random, int i, int j, int k);
}
