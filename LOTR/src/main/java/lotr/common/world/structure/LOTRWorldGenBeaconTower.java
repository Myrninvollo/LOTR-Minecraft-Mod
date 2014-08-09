package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityGondorSoldier;
import lotr.common.entity.npc.LOTREntityGondorTowerGuard;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class LOTRWorldGenBeaconTower extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenBeaconTower(boolean flag)
	{
		super(flag);
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		return generateWithSetHeightAndRotation(world, random, i, j, k, 7 + random.nextInt(4), random.nextInt(4));
	}
	
	public boolean generateWithSetHeightAndRotation(World world, Random random, int i, int j, int k, int height, int rotation)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != LOTRMod.rock || world.getBlockMetadata(i, j - 1, k) != 1)
			{
				return false;
			}
		}
		
		j += height;
		
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
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
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 1);
					setGrassToDirt(world, i1, j1 - 1, k1);
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
		setBlockAndNotifyAdequately(world, i, j + 2, k, LOTRMod.beacon, 0);
		
		for (int j1 = j + 1; j1 <= j + 5; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j1, k - 2, LOTRMod.brick, 1);
			setBlockAndNotifyAdequately(world, i - 2, j1, k + 2, LOTRMod.brick, 1);
			setBlockAndNotifyAdequately(world, i + 2, j1, k - 2, LOTRMod.brick, 1);
			setBlockAndNotifyAdequately(world, i + 2, j1, k + 2, LOTRMod.brick, 1);
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 5, k1, LOTRMod.slabSingle, 11);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 5, k - 2, LOTRMod.slabSingle, 11);
		setBlockAndNotifyAdequately(world, i, j + 5, k + 2, LOTRMod.slabSingle, 11);
		setBlockAndNotifyAdequately(world, i - 2, j + 5, k, LOTRMod.slabSingle, 11);
		setBlockAndNotifyAdequately(world, i + 2, j + 5, k, LOTRMod.slabSingle, 11);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 5, k - 1, LOTRMod.stairsGondorBrick, 7);
		setBlockAndNotifyAdequately(world, i - 2, j + 5, k + 1, LOTRMod.stairsGondorBrick, 6);
		setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 1, LOTRMod.stairsGondorBrick, 7);
		setBlockAndNotifyAdequately(world, i + 2, j + 5, k + 1, LOTRMod.stairsGondorBrick, 6);
		setBlockAndNotifyAdequately(world, i - 1, j + 5, k - 2, LOTRMod.stairsGondorBrick, 5);
		setBlockAndNotifyAdequately(world, i + 1, j + 5, k - 2, LOTRMod.stairsGondorBrick, 4);
		setBlockAndNotifyAdequately(world, i - 1, j + 5, k + 2, LOTRMod.stairsGondorBrick, 5);
		setBlockAndNotifyAdequately(world, i + 1, j + 5, k + 2, LOTRMod.stairsGondorBrick, 4);
		
		int soldiers = 1 + random.nextInt(2);
		for (int l = 0; l < soldiers; l++)
		{
			LOTREntityGondorSoldier soldier = new LOTREntityGondorTowerGuard(world);
			soldier.setLocationAndAngles(i - 1 + l * 2, j + 1, k, 0F, 0F);
			soldier.spawnRidingHorse = false;
			soldier.onSpawnWithEgg(null);
			soldier.isNPCPersistent = true;
			world.spawnEntityInWorld(soldier);
			soldier.setHomeArea(i, j, k, 16);
		}
		
		int ladderMetadata = 0;
		switch (rotation)
		{
			case 0:
				k -= 3;
				ladderMetadata = 2;
				break;
			case 1:
				i += 3;
				ladderMetadata = 5;
				break;
			case 2:
				k += 3;
				ladderMetadata = 3;
				break;
			case 3:
				i -= 3;
				ladderMetadata = 4;
				break;
		}
		
		for (int j1 = j; !LOTRMod.isOpaque(world, i, j1, k) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i, j1, k, Blocks.ladder, ladderMetadata);
		}
		
		if (usingPlayer == null)
		{
			LOTRLevelData.beaconTowerLocations.add(new ChunkCoordinates(i, j, k));
		}
		
		return true;
	}
}