package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public abstract class LOTRWorldGenTentBase extends LOTRWorldGenStructureBase
{
	protected Block tentBlock;
	protected int tentMeta;
	
	protected Block floorBlock;
	protected int floorMeta;
	
	protected Block supportsBlock;
	protected int supportsMeta;
	
	protected LOTRChestContents chestContents;
	protected boolean hasOrcForge;
	
	public LOTRWorldGenTentBase(boolean flag)
	{
		super(flag);
		if (isOrcTent())
		{
			supportsBlock = LOTRMod.fence;
			supportsMeta = 3;
		}
		else
		{
			supportsBlock = Blocks.fence;
			supportsMeta = 0;
		}
	}
	
	protected boolean isOrcTent()
	{
		return true;
	}
	
	protected abstract boolean canTentGenerateAt(World world, int i, int j, int k);
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		return generateWithSetRotation(world, random, i, j, k, -1);
	}
	
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation)
	{
		if (restrictions)
		{
			if (!canTentGenerateAt(world, i, j, k))
			{
				return false;
			}
		}
		
		j--;
		
		if (rotation == -1)
		{
			rotation = random.nextInt(4);
			
			if (!restrictions && usingPlayer != null)
			{
				rotation = usingPlayerRotation();
			}
		}
		
		switch (rotation)
		{
			case 0:
				k += 4;
				return generateNorthToSouth(world, random, i, j, k);
			case 1:
				i -= 4;
				return generateEastToWest(world, random, i, j, k);
			case 2:
				k -= 4;
				return generateNorthToSouth(world, random, i, j, k);
			case 3:
				i += 4;
				return generateEastToWest(world, random, i, j, k);
		}
		
		return false;
	}
	
	private boolean generateNorthToSouth(World world, Random random, int i, int j, int k)
	{
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				if (restrictions)
				{
					int j1 = world.getHeightValue(i1, k1);
					if (!canTentGenerateAt(world, i1, j1, k1))
					{
						return false;
					}
				}
				
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					if (floorBlock == Blocks.grass && j1 < j)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.dirt, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, floorBlock, floorMeta);
					}
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				int range = j1 == j + 3 ? 0 : 1;
				for (int i1 = i - range; i1 <= i + range; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k1, tentBlock, tentMeta);
			setBlockAndNotifyAdequately(world, i + 2, j + 1, k1, tentBlock, tentMeta);
			setBlockAndNotifyAdequately(world, i - 2, j + 2, k1, tentBlock, tentMeta);
			setBlockAndNotifyAdequately(world, i + 2, j + 2, k1, tentBlock, tentMeta);
			setBlockAndNotifyAdequately(world, i - 1, j + 3, k1, tentBlock, tentMeta);
			setBlockAndNotifyAdequately(world, i + 1, j + 3, k1, tentBlock, tentMeta);
			setBlockAndNotifyAdequately(world, i, j + 4, k1, tentBlock, tentMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i, j1, k - 3, supportsBlock, supportsMeta);
			setBlockAndNotifyAdequately(world, i, j1, k + 3, supportsBlock, supportsMeta);
		}
		
		if (isOrcTent())
		{
			placeOrcTorch(world, i - 1, j + 1, k - 3);
			placeOrcTorch(world, i + 1, j + 1, k - 3);
			placeOrcTorch(world, i - 1, j + 1, k + 3);
			placeOrcTorch(world, i + 1, j + 1, k + 3);
		}
		else
		{
			setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 3, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 3, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 3, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i + 1, j + 1, k + 3, Blocks.torch, 5);;
		}
		
		if (random.nextBoolean())
		{
			if (hasOrcForge)
			{
				setBlockAndNotifyAdequately(world, i - 1, j + 1, k, LOTRMod.orcForge, 0);
				setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 1, supportsBlock, supportsMeta);
				setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 1, supportsBlock, supportsMeta);
			}
			else
			{
				setBlockAndNotifyAdequately(world, i - 1, j + 1, k, Blocks.chest, 0);
				LOTRChestContents.fillChest(world, random, i - 1, j + 1, k, chestContents);
			}
		}
		else
		{
			if (hasOrcForge)
			{
				setBlockAndNotifyAdequately(world, i + 1, j + 1, k, LOTRMod.orcForge, 0);
				setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 1, supportsBlock, supportsMeta);
				setBlockAndNotifyAdequately(world, i + 1, j + 1, k + 1, supportsBlock, supportsMeta);
			}
			else
			{
				setBlockAndNotifyAdequately(world, i + 1, j + 1, k, Blocks.chest, 0);
				LOTRChestContents.fillChest(world, random, i + 1, j + 1, k, chestContents);
			}
		}
		
		return true;
	}
	
	private boolean generateEastToWest(World world, Random random, int i, int j, int k)
	{
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			for (int i1 = i - 3; i1 <= i + 3; i1++)
			{
				if (restrictions)
				{
					int j1 = world.getHeightValue(i1, k1);
					if (!canTentGenerateAt(world, i1, j1, k1))
					{
						return false;
					}
				}
				
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					if (floorBlock == Blocks.grass && j1 < j)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.dirt, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, floorBlock, floorMeta);
					}
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int i1 = i - 3; i1 <= i + 3; i1++)
			{
				int range = j1 == j + 3 ? 0 : 1;
				for (int k1 = k - range; k1 <= k + range; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 2, tentBlock, tentMeta);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 2, tentBlock, tentMeta);
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 2, tentBlock, tentMeta);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 2, tentBlock, tentMeta);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 1, tentBlock, tentMeta);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 1, tentBlock, tentMeta);
			setBlockAndNotifyAdequately(world, i1, j + 4, k, tentBlock, tentMeta);
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k, supportsBlock, supportsMeta);
			setBlockAndNotifyAdequately(world, i + 3, j1, k, supportsBlock, supportsMeta);
		}
		
		if (isOrcTent())
		{
			placeOrcTorch(world, i - 3, j + 1, k - 1);
			placeOrcTorch(world, i - 3, j + 1, k + 1);
			placeOrcTorch(world, i + 3, j + 1, k - 1);
			placeOrcTorch(world, i + 3, j + 1, k + 1);
		}
		else
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 1, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 1, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 1, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 1, Blocks.torch, 5);
		}
		
		if (random.nextBoolean())
		{
			if (hasOrcForge)
			{
				setBlockAndNotifyAdequately(world, i, j + 1, k - 1, LOTRMod.orcForge, 0);
				setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 1, supportsBlock, supportsMeta);
				setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 1, supportsBlock, supportsMeta);
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j + 1, k - 1, Blocks.chest, 0);
				LOTRChestContents.fillChest(world, random, i, j + 1, k - 1, chestContents);
			}
		}
		else
		{
			if (hasOrcForge)
			{
				setBlockAndNotifyAdequately(world, i, j + 1, k + 1, LOTRMod.orcForge, 0);
				setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 1, supportsBlock, supportsMeta);
				setBlockAndNotifyAdequately(world, i + 1, j + 1, k + 1, supportsBlock, supportsMeta);
			}
			else
			{
				setBlockAndNotifyAdequately(world, i, j + 1, k + 1, Blocks.chest, 0);
				LOTRChestContents.fillChest(world, random, i, j + 1, k + 1, chestContents);
			}
		}
		
		return true;
	}
}
