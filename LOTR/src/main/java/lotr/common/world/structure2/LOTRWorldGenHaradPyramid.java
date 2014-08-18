package lotr.common.world.structure2;

import java.util.*;

import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityDesertScorpion;
import lotr.common.entity.npc.LOTREntityHaradPyramidWraith;
import lotr.common.tileentity.LOTRTileEntityMobSpawner;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenHaradPyramid extends LOTRWorldGenStructureBase2
{
	public LOTRWorldGenHaradPyramid(boolean flag)
	{
		super(flag);
	}

	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation)
	{
		if (restrictions)
		{
			if (world.getBiomeGenForCoords(i, k) != LOTRBiome.nearHarad)
			{
				return false;
			}
		}
		
		int pyramidRadius = 25;
		
		j -= 2 + random.nextInt(3);
		
		setRotationMode(rotation);
		
		if (!restrictions)
		{
			switch (getRotationMode())
			{
				case 0:
					k += pyramidRadius;
					break;
				case 1:
					i -= pyramidRadius;
					break;
				case 2:
					k -= pyramidRadius;
					break;
				case 3:
					i += pyramidRadius;
					break;
			}
		}
			
		setOrigin(i, j, k);
		
		if (restrictions)
		{
			for (int i1 = -pyramidRadius; i1 <= pyramidRadius; i1++)
			{
				for (int k1 = -pyramidRadius; k1 <= pyramidRadius; k1++)
				{
					int j1 = getTopBlock(world, i1, k1);
					Block block = getBlock(world, i1, j1 - 1, k1);
					if (block != Blocks.sand && block != Blocks.sandstone)
					{
						return false;
					}
				}
			}
		}
		
		for (int i1 = -pyramidRadius; i1 <= pyramidRadius; i1++)
		{
			for (int k1 = -pyramidRadius; k1 <= pyramidRadius; k1++)
			{
				for (int j1 = 0; (getY(j1) >= originY || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; j1--)
				{
					setBlockAndMetadata(world, i1, j1, k1, LOTRMod.brick, 15);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
			}
		}
		
		int steps = (pyramidRadius - 5) / 2;
		int topRadius = pyramidRadius - steps * 2;
		int topHeight = steps * 2;
		
		for (int step = 0; step < steps; step++)
		{
			for (int j1 = step * 2; j1 <= step * 2 + 1; j1++)
			{
				int r = pyramidRadius - step * 2;
				for (int i1 = -r; i1 <= r; i1++)
				{
					for (int k1 = -r; k1 <= r; k1++)
					{
						setBlockAndMetadata(world, i1, j1, k1, LOTRMod.brick, 15);
						
						if ((Math.abs(i1) == r - 1 || Math.abs(k1) == r - 1) && random.nextInt(3) == 0)
						{
							setBlockAndMetadata(world, i1, j1 + 1, k1, LOTRMod.brick, 15);
						}
					}
				}
			}
		}
		
		for (int i1 = -topRadius; i1 <= topRadius; i1++)
		{
			for (int k1 = -topRadius; k1 <= topRadius; k1++)
			{
				if (random.nextInt(6) == 0)
				{
					setBlockAndMetadata(world, i1, topHeight, k1, Blocks.hardened_clay, 0);
				}
				else
				{
					setBlockAndMetadata(world, i1, topHeight, k1, Blocks.stained_hardened_clay, 14);
				}
				
				if (Math.abs(i1) == topRadius || Math.abs(k1) == topRadius)
				{
					setBlockAndMetadata(world, i1, topHeight + 1, k1, LOTRMod.wall, 15);
				}
			}
		}
		
		for (int i1 = -1; i1 <= 1; i1++)
		{
			for (int k1 = -1; k1 <= 1; k1++)
			{
				for (int j1 = topHeight + 1; j1 <= topHeight + 5; j1++)
				{
					setBlockAndMetadata(world, i1, j1, k1, LOTRMod.brick, 15);
				}
			}
		}
		
		int randomHeight;
		randomHeight = 2 + random.nextInt(5);
		for (int j1 = topHeight + 1; j1 <= topHeight + randomHeight; j1++)
		{
			setBlockAndMetadata(world, -2, j1, -2, LOTRMod.wall, 15);
		}
		randomHeight = 2 + random.nextInt(5);
		for (int j1 = topHeight + 1; j1 <= topHeight + randomHeight; j1++)
		{
			setBlockAndMetadata(world, -2, j1, 2, LOTRMod.wall, 15);
		}
		randomHeight = 2 + random.nextInt(5);
		for (int j1 = topHeight + 1; j1 <= topHeight + randomHeight; j1++)
		{
			setBlockAndMetadata(world, 2, j1, -2, LOTRMod.wall, 15);
		}
		randomHeight = 2 + random.nextInt(5);
		for (int j1 = topHeight + 1; j1 <= topHeight + randomHeight; j1++)
		{
			setBlockAndMetadata(world, 2, j1, 2, LOTRMod.wall, 15);
		}
		
		setBlockAndMetadata(world, 0, topHeight + 5, 2, LOTRMod.stairsNearHaradBrick, 7);
		setBlockAndMetadata(world, 0, topHeight + 6, 2, LOTRMod.stairsNearHaradBrick, 2);
		
		setBlockAndMetadata(world, 0, topHeight + 5, -2, LOTRMod.stairsNearHaradBrick, 6);
		setBlockAndMetadata(world, 0, topHeight + 6, -2, LOTRMod.stairsNearHaradBrick, 3);
		
		setBlockAndMetadata(world, -2, topHeight + 5, 0, LOTRMod.stairsNearHaradBrick, 5);
		setBlockAndMetadata(world, -2, topHeight + 6, 0, LOTRMod.stairsNearHaradBrick, 0);
		
		setBlockAndMetadata(world, 2, topHeight + 5, 0, LOTRMod.stairsNearHaradBrick, 4);
		setBlockAndMetadata(world, 2, topHeight + 6, 0, LOTRMod.stairsNearHaradBrick, 1);
		
		for (int j1 = topHeight + 6; j1 <= topHeight + 7; j1++)
		{
			setBlockAndMetadata(world, 0, j1, 0, LOTRMod.brick, 15);
		}
		
		for (int i1 = -1; i1 <= 1; i1++)
		{
			setBlockAndMetadata(world, i1, topHeight + 8, 0, LOTRMod.brick, 15);
			setBlockAndMetadata(world, i1, topHeight + 12, 0, LOTRMod.brick, 15);
		}
		
		setBlockAndMetadata(world, -2, topHeight + 8, 0, LOTRMod.stairsNearHaradBrick, 4);
		setBlockAndMetadata(world, -2, topHeight + 9, 0, LOTRMod.stairsNearHaradBrick, 0);
		setBlockAndMetadata(world, -3, topHeight + 9, 0, LOTRMod.stairsNearHaradBrick, 4);
		
		setBlockAndMetadata(world, 2, topHeight + 8, 0, LOTRMod.stairsNearHaradBrick, 5);
		setBlockAndMetadata(world, 2, topHeight + 9, 0, LOTRMod.stairsNearHaradBrick, 1);
		setBlockAndMetadata(world, 3, topHeight + 9, 0, LOTRMod.stairsNearHaradBrick, 5);
		
		setBlockAndMetadata(world, -3, topHeight + 10, 0, LOTRMod.brick, 15);
		setBlockAndMetadata(world, 3, topHeight + 10, 0, LOTRMod.brick, 15);
		
		setBlockAndMetadata(world, -3, topHeight + 11, 0, LOTRMod.stairsNearHaradBrick, 0);
		setBlockAndMetadata(world, -2, topHeight + 11, 0, LOTRMod.stairsNearHaradBrick, 4);
		setBlockAndMetadata(world, -2, topHeight + 12, 0, LOTRMod.stairsNearHaradBrick, 0);
		
		setBlockAndMetadata(world, 3, topHeight + 11, 0, LOTRMod.stairsNearHaradBrick, 1);
		setBlockAndMetadata(world, 2, topHeight + 11, 0, LOTRMod.stairsNearHaradBrick, 5);
		setBlockAndMetadata(world, 2, topHeight + 12, 0, LOTRMod.stairsNearHaradBrick, 1);
		
		setBlockAndMetadata(world, 0, topHeight + 9, 0, LOTRMod.wall, 15);
		setBlockAndMetadata(world, 0, topHeight + 10, 0, Blocks.gold_block, 0);
		setBlockAndMetadata(world, 0, topHeight + 11, 0, LOTRMod.wall, 15);
		
		for (int i1 = -1; i1 <= 1; i1++)
		{
			for (int j1 = 1; j1 <= 3; j1++)
			{
				for (int k1 = -pyramidRadius; k1 <= -pyramidRadius + 6; k1++)
				{
					setAir(world, i1, j1, k1);
				}
			}
		}

		List<Chamber> chambers = new ArrayList();
		chambers.add(Chamber.TOMB);
		chambers.add(Chamber.SCORPIONS);
		chambers.add(Chamber.SCORPIONS);
		chambers.add(Chamber.SCORPIONS);
		chambers.add(Chamber.SCORPIONS);
		Collections.shuffle(chambers);
		
		spawnChamber(world, random, 0, 12, 0, chambers.get(0));
		
		spawnChamber(world, random, -12, 2, -12, chambers.get(1));
		spawnChamber(world, random, -12, 2, 12, chambers.get(2));
		spawnChamber(world, random, 12, 2, -12, chambers.get(3));
		spawnChamber(world, random, 12, 2, 12, chambers.get(4));
		
		return true;
	}
	
	private static enum Chamber
	{
		TOMB,
		SCORPIONS,
	}
	
	private void spawnChamber(World world, Random random, int i, int j, int k, Chamber chamber)
	{
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			for (int k1 = k - 5; k1 <= k + 5; k1++)
			{
				for (int j1 = j + 1; j1 <= j + 4; j1++)
				{
					setAir(world, i1, j1, k1);
				}
				
				if (Math.abs(i1 - i) == 5 || Math.abs(k1 - k) == 5)
				{
					setBlockAndMetadata(world, i1, j + 1, k1, LOTRMod.brick, 15);
				}
			}
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			setBlockAndMetadata(world, i1, j + 1, k - 4, LOTRMod.stairsNearHaradBrick, 3);
			setBlockAndMetadata(world, i1, j + 1, k + 4, LOTRMod.stairsNearHaradBrick, 2);
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			setBlockAndMetadata(world, i - 4, j + 1, k1, LOTRMod.stairsNearHaradBrick, 0);
			setBlockAndMetadata(world, i + 4, j + 1, k1, LOTRMod.stairsNearHaradBrick, 1);
		}
		
		if (chamber == Chamber.TOMB)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndMetadata(world, i1, j + 1, k1, Blocks.stained_hardened_clay, 14);
				}
			}
			
			placeSpawnerChest(world, i, j + 1, k, 0, LOTREntityHaradPyramidWraith.class);
			fillChest(world, random, i, j + 1, k, LOTRChestContents.NEAR_HARAD_PYRAMID);
			
			setBlockAndMetadata(world, i, j + 2, k, Blocks.stained_hardened_clay, 14);
		
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				for (int k1 = k - 5; k1 <= k + 5; k1 += 10)
				{
					setBlockAndMetadata(world, i1, j, k1, LOTRMod.hearth, 0);
					setBlockAndMetadata(world, i1, j + 1, k1, Blocks.fire, 0);
					setAir(world, i1, j + 2, k1);
				}
			}
			
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				for (int i1 = i - 5; i1 <= i + 5; i1 += 10)
				{
					setBlockAndMetadata(world, i1, j, k1, LOTRMod.hearth, 0);
					setBlockAndMetadata(world, i1, j + 1, k1, Blocks.fire, 0);
					setAir(world, i1, j + 2, k1);
				}
			}
		}
		
		if (chamber == Chamber.SCORPIONS)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndMetadata(world, i1, j + 1, k1, Blocks.stained_hardened_clay, 14);
				}
			}
			
			placeMobSpawner(world, i, j + 1, k, LOTREntityDesertScorpion.class);
			LOTRTileEntityMobSpawner spawner = (LOTRTileEntityMobSpawner)getTileEntity(world, i, j + 1, k);
			if (spawner != null)
			{
				spawner.minSpawnDelay = 0;
				spawner.maxSpawnDelay = 20;
				spawner.requiredPlayerRange = 4;
				spawner.nearbyMobLimit = 4;
				spawner.nearbyMobCheckRange = 8;
				spawner.maxSpawnRange = 6;
				spawner.maxSpawnRangeVertical = 2;
			}
			
			setBlockAndMetadata(world, i, j + 2, k, Blocks.stained_hardened_clay, 14);
		}
	}
}
