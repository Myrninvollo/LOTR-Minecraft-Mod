package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenDwarvenMineEntrance extends LOTRWorldGenStructureBase
{
	private Block plankBlock;
	private int plankMeta;
	
	private static int[][] offsetsForRotation = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
	private static int[] rotationOpposites = {2, 3, 0, 1};
	
	public LOTRWorldGenDwarvenMineEntrance(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		return generateWithDwarvenForgeDirection(world, random, i, j, k, -1);
	}
	
	public boolean generateWithDwarvenForgeDirection(World world, Random random, int i, int j, int k, int dwarvenForgeDirection)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass)
			{
				return false;
			}
		}
		
		j--;
		
		int rotation = -1;
		
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
			switch (rotation)
			{
				case 0:
					k += 5;
					break;
				case 1:
					i -= 5;
					break;
				case 2:
					k -= 5;
					break;
				case 3:
					i += 5;
					break;
			}
		}
		else
		{
			for (int l = 0; l < 4; l++)
			{
				int i1 = offsetsForRotation[l][0] * 5;
				int k1 = offsetsForRotation[l][1] * 5;
				if (LOTRMod.isOpaque(world, i + i1, j, k + k1) && world.isAirBlock(i + i1, j + 1, k + k1) && world.isAirBlock(i + i1, j + 2, k + k1))
				{
					rotation = l;
					break;
				}
			}
			
			if (rotation == -1)
			{
				for (int l = 0; l < 4; l++)
				{
					int i1 = offsetsForRotation[l][0] * 5;
					int k1 = offsetsForRotation[l][1] * 5;
					if (world.isAirBlock(i + i1, j + 1, k + k1) && world.isAirBlock(i + i1, j + 2, k + k1))
					{
						rotation = l;
						break;
					}
				}
			}
			
			if (rotation == -1)
			{
				rotation = random.nextInt(4);
			}
		}
		
		plankBlock = Blocks.planks;
		plankMeta = 1;
		
		int depth = 30;
		if (restrictions && j - depth <= 5)
		{
			return false;
		}
		
		if (usingPlayer == null)
		{
			depth = j - 40;
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				for (int j1 = j + 1; j1 <= j + 3; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j, k1, plankBlock, plankMeta);
				
				if (i1 == i - 4 || i1 == i + 4 || k1 == k - 4 || k1 == k + 4)
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.fence, 0);
					
					if (i1 == i - 1 || i1 == i + 1 || k1 == k - 1 || k1 == k + 1)
					{
						setBlockAndNotifyAdequately(world, i1, j + 2, k1, Blocks.torch, 5);
					}
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j1, k - 4, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i - 4, j1, k + 4, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i + 4, j1, k - 4, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i + 4, j1, k + 4, LOTRMod.pillar, 0);
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, plankBlock, plankMeta);
			}
		}
		
		for (int j1 = j - 1; j1 > j - depth && j1 >= 0; j1--)
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					if (i1 == i - 4 || i1 == i + 4 || k1 == k - 4 || k1 == k + 4)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 6);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i - 3, j1, k - 3, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i - 3, j1, k + 3, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i + 3, j1, k - 3, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i + 3, j1, k + 3, LOTRMod.pillar, 0);
			
			if ((depth - (j - j1)) % 6 == 3)
			{
				setBlockAndNotifyAdequately(world, i - 3, j1, k - 3, Blocks.glowstone, 0);
				setBlockAndNotifyAdequately(world, i - 3, j1, k + 3, Blocks.glowstone, 0);
				setBlockAndNotifyAdequately(world, i + 3, j1, k - 3, Blocks.glowstone, 0);
				setBlockAndNotifyAdequately(world, i + 3, j1, k + 3, Blocks.glowstone, 0);
			}
		}
		
		if (j - depth >= 0)
		{
			for (int i1 = i - 3; i1 <= i + 3; i1++)
			{
				for (int k1 = k - 3; k1 <= k + 3; k1++)
				{
					if (!LOTRMod.isOpaque(world, i1, j - depth, k1))
					{
						setBlockAndNotifyAdequately(world, i1, j - depth, k1, Blocks.stone, 0);
					}
				}
			}
			
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j - depth, k1, LOTRMod.pillar, 0);
				}
			}
		}
		
		int ladderX = 0;
		int ladderZ = 0;
		int ladderMeta = 0;
		switch (rotation)
		{
			case 0:
				ladderZ = 1;
				ladderMeta = 3;
				setBlockAndNotifyAdequately(world, i, j + 1, k - 4, Blocks.fence_gate, 0);
				break;
			case 1:
				ladderX = -1;
				ladderMeta = 4;
				setBlockAndNotifyAdequately(world, i + 4, j + 1, k, Blocks.fence_gate, 1);
				break;
			case 2:
				ladderZ = -1;
				ladderMeta = 2;
				setBlockAndNotifyAdequately(world, i, j + 1, k + 4, Blocks.fence_gate, 2);
				break;
			case 3:
				ladderX = 1;
				ladderMeta = 5;
				setBlockAndNotifyAdequately(world, i - 4, j + 1, k, Blocks.fence_gate, 3);
				break;
		}
		
		for (int j1 = j; j1 > j - depth && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i, j1, k, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i + ladderX, j1, k + ladderZ, Blocks.ladder, ladderMeta);
		}
		
		for (int j1 = j - depth + 1; j1 <= j - depth + 3; j1++)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 4, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j1, k + 4, Blocks.air, 0);
			}
			
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 4, j1, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i + 4, j1, k1, Blocks.air, 0);
			}
		}
		
		if (dwarvenForgeDirection == - 1)
		{
			dwarvenForgeDirection = rotation;
		}
		
		for (int l = 0; l < 4; l++)
		{
			if (l == rotationOpposites[dwarvenForgeDirection])
			{
				continue;
			}
			
			int i1 = i + offsetsForRotation[l][0] * 4;
			int k1 = k + offsetsForRotation[l][1] * 4;
			setBlockAndNotifyAdequately(world, i1, j - depth + 1, k1, LOTRMod.dwarvenForge, 0);
			
			if (i1 == i)
			{
				setBlockAndNotifyAdequately(world, i - 1, j - depth + 1, k1, LOTRMod.slabSingle, 15);
				setBlockAndNotifyAdequately(world, i + 1, j - depth + 1, k1, LOTRMod.slabSingle, 15);
			}
			
			if (k1 == k)
			{
				setBlockAndNotifyAdequately(world, i1, j - depth + 1, k - 1, LOTRMod.slabSingle, 15);
				setBlockAndNotifyAdequately(world, i1, j - depth + 1, k + 1, LOTRMod.slabSingle, 15);
			}
		}
		
		return true;
	}
}
