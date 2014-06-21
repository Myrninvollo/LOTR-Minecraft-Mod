package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenUnderwaterElvenRuin extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenUnderwaterElvenRuin(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j, k).getMaterial() != Material.water)
			{
				return false;
			}
		}
		
		j--;
		
		int width = 3 + random.nextInt(3);
		
		int rotation = random.nextInt(4);
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
		}
		
		switch (rotation)
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
		
		if (restrictions)
		{
			int minHeight = j + 1;
			int maxHeight = j + 1;
			
			for (int i1 = i - width; i1 <= i + width; i1++)
			{
				for (int k1 = k - width; k1 <= k + width; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
					if (world.getBlock(i1, j1, k1).getMaterial() != Material.water)
					{
						return false;
					}
					
					Block block = world.getBlock(i1, j1 - 1, k1);
					if (block != Blocks.dirt && block != Blocks.sand && block != Blocks.clay)
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
	
		for (int i1 = i - width - 3; i1 <= i + width + 3; i1++)
		{
			for (int k1 = k - width - 3; k1 <= k + width + 3; k1++)
			{
				int i2 = Math.abs(i1 - i);
				int k2 = Math.abs(k1 - k);
				
				if ((i2 > width || k2 > width) && random.nextInt(Math.max(1, i2 + k2)) != 0)
				{
					continue;
				}
				
				int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
				placeRandomBrick(world, random, i1, j1, k1);
				setGrassToDirt(world, i1, j1 - 1, k1);
				
				if (random.nextInt(5) == 0)
				{
					placeRandomBrick(world, random, i1, j1 + 1, k1);
					
					if (random.nextInt(4) == 0)
					{
						placeRandomBrick(world, random, i1, j1 + 2, k1);
						
						if (random.nextInt(3) == 0)
						{
							placeRandomBrick(world, random, i1, j1 + 3, k1);
						}
					}
				}
				
				if ((i2 == width && k2 == width) || random.nextInt(20) == 0)
				{
					int height = 2 + random.nextInt(4);
					for (int j2 = j1; j2 < j1 + height; j2++)
					{
						placeRandomPillar(world, random, i1, j2, k1);
					}
				}
			}
		}
		
		int j1 = world.getTopSolidOrLiquidBlock(i, k);
		setBlockAndNotifyAdequately(world, i, j1, k, Blocks.glowstone, 0);
		setBlockAndNotifyAdequately(world, i, j1 + 1, k, Blocks.chest, 0);
		LOTRChestContents.fillChest(world, random, i, j1 + 1, k, LOTRChestContents.UNDERWATER_ELVEN_RUIN);
		
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
