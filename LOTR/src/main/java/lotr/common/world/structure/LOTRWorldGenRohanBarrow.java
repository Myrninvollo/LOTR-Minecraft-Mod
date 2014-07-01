package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityRohanBarrowWraith;
import lotr.common.world.biome.LOTRBiomeGenRohan;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class LOTRWorldGenRohanBarrow extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenRohanBarrow(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass || !(world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenRohan))
			{
				return false;
			}
		}
		
		j--;
		
		int radius = 7;
		int height = 4;
		
		if (!restrictions && usingPlayer != null)
		{
			int playerRotation = usingPlayerRotation();
			switch (playerRotation)
			{
				case 0:
					k += radius;
					break;
				case 1:
					i -= radius;
					break;
				case 2:
					k -= radius;
					break;
				case 3:
					i += radius;
					break;
			}
		}
		
		if (restrictions)
		{
			int minHeight = j;
			int maxHeight = j;
			
			for (int i1 = i - radius; i1 <= i + radius; i1++)
			{
				for (int k1 = k - radius; k1 <= k + radius; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					if (world.getBlock(i1, j1, k1) != Blocks.grass)
					{
						return false;
					}
					
					if (j1 < minHeight)
					{
						minHeight = j1;
					}
					if (j1 > maxHeight)
					{
						maxHeight = j1;
					}
				}
			}
			
			if (maxHeight - minHeight > 3)
			{
				return false;
			}

			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1D, j + 1D, k + 1D).expand(radius + 1, height + 1, radius + 1));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int i1 = i - radius; i1 <= i + radius; i1++)
		{
			for (int j1 = j + height; j1 >= j; j1--)
			{
				for (int k1 = k - radius; k1 <= k + radius; k1++)
				{
					int i2 = i1 - i;
					int j2 = j1 - j;
					int k2 = k1 - k;
					if (i2 * i2 + j2 * j2 + k2 * k2 > radius * radius)
					{
						continue;
					}
					boolean grass = !LOTRMod.isOpaque(world, i1, j1 + 1, k1);
					setBlockAndNotifyAdequately(world, i1, j1, k1, grass ? Blocks.grass : Blocks.dirt, 0);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		for (int i1 = i - radius; i1 <= i + radius; i1++)
		{
			for (int k1 = k - radius; k1 <= k + radius; k1++)
			{
				for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					int i2 = i1 - i;
					int k2 = k1 - k;
					if (i2 * i2 + k2 * k2 > radius * radius)
					{
						continue;
					}
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.dirt, 0);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		for (int l = 0; l < 10; l++)
		{
			int i1 = i - random.nextInt(radius) + random.nextInt(radius);
			int k1 = k - random.nextInt(radius) + random.nextInt(radius);
			int j1 = world.getHeightValue(i1, k1);
			if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.simbelmyne, 0);
			}
		}
		
		j = j + height;
		
		for (int i1 = i - 1; i1 < i + 1; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j - 1, k1, Blocks.air, 0);
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				for (int j1 = j - 2; j1 >= j - 4; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				setBlockAndNotifyAdequately(world, i1, j - 5, k1, LOTRMod.slabDouble2, 1);
			}
		}
		
		for (int j1 = j - 3; j1 >= j - 4; j1--)
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					if (Math.abs(i1 - i) > 2)
					{
						setBlockAndNotifyAdequately(world, i1, j - 5, k1, LOTRMod.brick, 4);
					}
				}
			}
			
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					if (Math.abs(k1 - k) > 2)
					{
						setBlockAndNotifyAdequately(world, i1, j - 5, k1, LOTRMod.brick, 4);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i - 4, j1, k - 1, LOTRMod.rock, 2);
			setBlockAndNotifyAdequately(world, i - 5, j1, k, LOTRMod.rock, 2);
			setBlockAndNotifyAdequately(world, i - 4, j1, k + 1, LOTRMod.rock, 2);
			setBlockAndNotifyAdequately(world, i + 4, j1, k - 1, LOTRMod.rock, 2);
			setBlockAndNotifyAdequately(world, i + 5, j1, k, LOTRMod.rock, 2);
			setBlockAndNotifyAdequately(world, i + 4, j1, k + 1, LOTRMod.rock, 2);
			setBlockAndNotifyAdequately(world, i - 1, j1, k - 4, LOTRMod.rock, 2);
			setBlockAndNotifyAdequately(world, i, j1, k - 5, LOTRMod.rock, 2);
			setBlockAndNotifyAdequately(world, i + 1, j1, k - 4, LOTRMod.rock, 2);
			setBlockAndNotifyAdequately(world, i - 1, j1, k + 4, LOTRMod.rock, 2);
			setBlockAndNotifyAdequately(world, i, j1, k + 5, LOTRMod.rock, 2);
			setBlockAndNotifyAdequately(world, i + 1, j1, k + 4, LOTRMod.rock, 2);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j - 3, k, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 4, j - 4, k, LOTRMod.slabSingle2, 9);
		setBlockAndNotifyAdequately(world, i + 4, j - 3, k, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 4, j - 4, k, LOTRMod.slabSingle2, 9);
		setBlockAndNotifyAdequately(world, i, j - 3, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i, j - 4, k - 4, LOTRMod.slabSingle2, 9);
		setBlockAndNotifyAdequately(world, i, j - 3, k + 4, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i, j - 4, k + 4, LOTRMod.slabSingle2, 9);
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - 4, k - 1, LOTRMod.stairsRohanBrick, 2);
			setBlockAndNotifyAdequately(world, i1, j - 4, k + 1, LOTRMod.stairsRohanBrick, 3);
		}
		setBlockAndNotifyAdequately(world, i - 1, j - 4, k, LOTRMod.stairsRohanBrick, 0);
		setBlockAndNotifyAdequately(world, i + 1, j - 4, k, LOTRMod.stairsRohanBrick, 1);
		
		placeSpawnerChest(world, i, j - 5, k, 4, LOTREntityRohanBarrowWraith.class);
		LOTRChestContents.fillChest(world, random, i, j - 5, k, LOTRChestContents.ROHAN_BARROWS);
		setBlockAndNotifyAdequately(world, i, j - 3, k, LOTRMod.slabSingle2, 1);
		
		return true;
	}
}
