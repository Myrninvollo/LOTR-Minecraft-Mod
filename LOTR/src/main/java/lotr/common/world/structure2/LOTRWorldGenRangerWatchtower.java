package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityRangerNorth;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class LOTRWorldGenRangerWatchtower extends LOTRWorldGenStructureBase2
{
	private Block woodBlock;
	private int woodMeta;
	
	private Block plankBlock;
	private int plankMeta;
	
	private Block fenceBlock;
	private int fenceMeta;
	
	private Block stairBlock;
	
	public LOTRWorldGenRangerWatchtower(boolean flag)
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
		
		setRotationMode(rotation);
		
		setOrigin(i, j, k);
		
		if (restrictions)
		{
			for (int i1 = -4; i1 <= 4; i1++)
			{
				for (int k1 = -4; k1 <= 4; k1++)
				{
					int j1 = getTopBlock(world, i1, k1);
					Block block = getBlock(world, i1, j1 - 1, k1);
					if (block != Blocks.grass)
					{
						return false;
					}
				}
			}
		}
		
		int randomWood = random.nextInt(4);
		if (randomWood == 0)
		{
			woodBlock = Blocks.log;
			woodMeta = 0;
			
			plankBlock = Blocks.planks;
			plankMeta = 0;
			
			fenceBlock = Blocks.fence;
			fenceMeta = 0;
			
			stairBlock = Blocks.oak_stairs;
		}
		else if (randomWood == 1)
		{
			woodBlock = Blocks.log;
			woodMeta = 1;
			
			plankBlock = Blocks.planks;
			plankMeta = 1;
			
			fenceBlock = Blocks.fence;
			fenceMeta = 0;
			
			stairBlock = Blocks.spruce_stairs;
		}
		else if (randomWood == 2)
		{
			woodBlock = LOTRMod.wood2;
			woodMeta = 1;
			
			plankBlock = LOTRMod.planks;
			plankMeta = 9;
			
			fenceBlock = LOTRMod.fence;
			fenceMeta = 9;
			
			stairBlock = LOTRMod.stairsBeech;
		}
		else if (randomWood == 3)
		{
			woodBlock = LOTRMod.wood3;
			woodMeta = 0;
			
			plankBlock = LOTRMod.planks;
			plankMeta = 12;
			
			fenceBlock = LOTRMod.fence;
			fenceMeta = 12;
			
			stairBlock = LOTRMod.stairsMaple;
		}
		
		generateSupportPillar(world, -3, 4, -3);
		generateSupportPillar(world, -3, 4, 3);
		generateSupportPillar(world, 3, 4, -3);
		generateSupportPillar(world, 3, 4, 3);
		
		for (int i1 = -2; i1 <= 2; i1++)
		{
			for (int k1 = -2; k1 <= 2; k1++)
			{
				for (int j1 = 5; j1 <= 19; j1++)
				{
					setAir(world, i1, j1, k1);
				}
			}
		}

		for (int j1 = 6; j1 <= 19; j1++)
		{
			setBlockAndMetadata(world, -2, j1, -2, woodBlock, woodMeta);
			setBlockAndMetadata(world, -2, j1, 2, woodBlock, woodMeta);
			setBlockAndMetadata(world, 2, j1, -2, woodBlock, woodMeta);
			setBlockAndMetadata(world, 2, j1, 2, woodBlock, woodMeta);
		}
		
		for (int j1 = 5; j1 <= 10; j1 += 5)
		{
			for (int i1 = -3; i1 <= 3; i1++)
			{
				for (int k1 = -3; k1 <= 3; k1++)
				{
					setBlockAndMetadata(world, i1, j1, k1, plankBlock, plankMeta);
				}
			}
			
			for (int i1 = -4; i1 <= 4; i1++)
			{
				setBlockAndMetadata(world, i1, j1, -4, stairBlock, 2);
				setBlockAndMetadata(world, i1, j1, 4, stairBlock, 3);
			}
			
			for (int k1 = -3; k1 <= 3; k1++)
			{
				setBlockAndMetadata(world, -4, j1, k1, stairBlock, 1);
				setBlockAndMetadata(world, 4, j1, k1, stairBlock, 0);
			}
			
			for (int i1 = -2; i1 <= 2; i1++)
			{
				setBlockAndMetadata(world, i1, j1 + 1, -3, fenceBlock, fenceMeta);
				setBlockAndMetadata(world, i1, j1 + 1, 3, fenceBlock, fenceMeta);
			}
			
			for (int k1 = -2; k1 <= 2; k1++)
			{
				setBlockAndMetadata(world, -3, j1 + 1, k1, fenceBlock, fenceMeta);
				setBlockAndMetadata(world, 3, j1 + 1, k1, fenceBlock, fenceMeta);
			}
			
			setBlockAndMetadata(world, 0, j1 + 2, -3, Blocks.torch, 5);
			setBlockAndMetadata(world, 0, j1 + 2, 3, Blocks.torch, 5);
			setBlockAndMetadata(world, -3, j1 + 2, 0, Blocks.torch, 5);
			setBlockAndMetadata(world, 3, j1 + 2, 0, Blocks.torch, 5);
			
			spawnNPCAndSetHome(new LOTREntityRangerNorth(world), world, -1, j1 + 1, 0, 8);
		}
		
		for (int i1 = -2; i1 <= 2; i1++)
		{
			for (int k1 = -2; k1 <= 2; k1++)
			{
				int i2 = Math.abs(i1);
				int k2 = Math.abs(k1);
				
				if (i2 < 2 || k2 < 2)
				{
					setBlockAndMetadata(world, i1, 15, k1, plankBlock, plankMeta);
					
					if ((i2 < 2 && k2 == 2) || (i2 == 2 && k2 < 2))
					{
						setBlockAndMetadata(world, i1, 16, k1, fenceBlock, fenceMeta);
					}
				}
			}
		}
		
		setGrassToDirt(world, 0, 0, 0);
		for (int j1 = 1; j1 <= 22; j1++)
		{
			setBlockAndMetadata(world, 0, j1, 0, woodBlock, woodMeta);
			
			if (j1 <= 15)
			{
				setBlockAndMetadata(world, 0, j1, -1, Blocks.ladder, 2);
			}
		}
		
		setBlockAndMetadata(world, 0, 6, -1, Blocks.trapdoor, 0);
		setBlockAndMetadata(world, 0, 11, -1, Blocks.trapdoor, 0);
		
		setBlockAndMetadata(world, 0, 17, -2, Blocks.torch, 5);
		setBlockAndMetadata(world, 0, 17, 2, Blocks.torch, 5);
		setBlockAndMetadata(world, -2, 17, 0, Blocks.torch, 5);
		setBlockAndMetadata(world, 2, 17, 0, Blocks.torch, 5);
		
		setBlockAndMetadata(world, 0, 16, 1, Blocks.chest, 0);
		for (int l = 0; l < 4; l++)
		{
			fillChest(world, random, 0, 16, 1, LOTRChestContents.RANGER_TENT);
		}
		
		for (int j1 = 17; j1 <= 18; j1++)
		{
			setBlockAndMetadata(world, -2, j1, -2, fenceBlock, fenceMeta);
			setBlockAndMetadata(world, -2, j1, 2, fenceBlock, fenceMeta);
			setBlockAndMetadata(world, 2, j1, -2, fenceBlock, fenceMeta);
			setBlockAndMetadata(world, 2, j1, 2, fenceBlock, fenceMeta);
		}
		
		for (int step = 0; step <= 1; step++)
		{
			for (int i1 = -2 + step; i1 <= 2 - step; i1++)
			{
				setBlockAndMetadata(world, i1, 20 + step, -2 + step, stairBlock, 2);
				setBlockAndMetadata(world, i1, 20 + step, 2 - step, stairBlock, 3);
			}
			
			for (int k1 = -1 + step; k1 <= 1 - step; k1++)
			{
				setBlockAndMetadata(world, -2 + step, 20 + step, k1, stairBlock, 1);
				setBlockAndMetadata(world, 2 - step, 20 + step, k1, stairBlock, 0);
			}
		}
	
		setBlockAndMetadata(world, 0, 22, -1, Blocks.torch, 0);
		setBlockAndMetadata(world, 0, 22, 1, Blocks.torch, 0);
		setBlockAndMetadata(world, -1, 22, 0, Blocks.torch, 0);
		setBlockAndMetadata(world, 1, 22, 0, Blocks.torch, 0);
		
		for (int j1 = 23; j1 <= 26; j1++)
		{
			setBlockAndMetadata(world, 0, j1, 0, fenceBlock, fenceMeta);
		}
		
		for (int j1 = 25; j1 <= 26; j1++)
		{
			for (int i1 = 0; i1 >= -1; i1--)
			{
				setBlockAndMetadata(world, -1 + i1, j1, 0, Blocks.wool, 13);
				setBlockAndMetadata(world, -3 + i1, j1, 1, Blocks.wool, 13);
				setBlockAndMetadata(world, -5 + i1, j1, 2, Blocks.wool, 13);
			}
		}
		
		if (usingPlayer == null)
		{
			LOTRLevelData.rangerWatchtowerLocations.add(new ChunkCoordinates(i, j, k));
		}
		
		return true;
	}
	
	private void generateSupportPillar(World world, int i, int j, int k)
	{
		for (int j1 = j; !isOpaque(world, i, j1, k) && getY(j1) >= 0; j1--)
		{
			setBlockAndMetadata(world, i, j1, k, woodBlock, woodMeta);
			setGrassToDirt(world, i, j1 - 1, i);
		}
	}
}
