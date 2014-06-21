package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityGondorArcher;
import lotr.common.entity.npc.LOTREntityGondorSoldier;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenGondorTurret extends LOTRWorldGenStructureBase2
{
	public LOTRWorldGenGondorTurret(boolean flag)
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
		
		switch (getRotationMode())
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
		
		setOrigin(i, j, k);
		
		if (restrictions)
		{
			for (int i1 = -2; i1 <= 2; i1++)
			{
				for (int k1 = -2; k1 <= 2; k1++)
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
		
		for (int i1 = -2; i1 <= 2; i1++)
		{
			for (int k1 = -2; k1 <= 2; k1++)
			{
				setBlockAndMetadata(world, i1, 0, k1, LOTRMod.slabDouble, 2);
				for (int j1 = -1; !isOpaque(world, i1, j1, k1) && getY(j1) >= 0; j1--)
				{
					setBlockAndMetadata(world, i1, j1, k1, LOTRMod.slabDouble, 2);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
			}
		}
		
		for (int j1 = 1; j1 <= 4; j1++)
		{
			for (int i1 = -1; i1 <= 1; i1++)
			{
				for (int k1 = -1; k1 <= 1; k1++)
				{
					if (Math.abs(i1) == 1 && Math.abs(k1) == 1)
					{
						setBlockAndMetadata(world, i1, j1, k1, LOTRMod.brick, 5);
					}
					else
					{
						setBlockAndMetadata(world, i1, j1, k1, LOTRMod.rock, 1);
					}
				}
			}
		}
		
		setBlockAndMetadata(world, -2, 1, -2, LOTRMod.slabDouble, 2);
		setBlockAndMetadata(world, -2, 1, 2, LOTRMod.slabDouble, 2);
		setBlockAndMetadata(world, 2, 1, -2, LOTRMod.slabDouble, 2);
		setBlockAndMetadata(world, 2, 1, 2, LOTRMod.slabDouble, 2);
		
		for (int j1 = 2; j1 <= 4; j1++)
		{
			setBlockAndMetadata(world, -2, j1, -2, LOTRMod.wall, 2);
			setBlockAndMetadata(world, -2, j1, 2, LOTRMod.wall, 2);
			setBlockAndMetadata(world, 2, j1, -2, LOTRMod.wall, 2);
			setBlockAndMetadata(world, 2, j1, 2, LOTRMod.wall, 2);
		}
		
		setBlockAndMetadata(world, -2, 5, -2, Blocks.log, 0);
		setBlockAndMetadata(world, -2, 5, 2, Blocks.log, 0);
		setBlockAndMetadata(world, 2, 5, -2, Blocks.log, 0);
		setBlockAndMetadata(world, 2, 5, 2, Blocks.log, 0);
		
		for (int k1 = -1; k1 <= 1; k1++)
		{
			setBlockAndMetadata(world, -2, 1, k1, LOTRMod.stairsGondorBrick, 1);
			setBlockAndMetadata(world, 2, 1, k1, LOTRMod.stairsGondorBrick, 0);
			
			setBlockAndMetadata(world, -2, 3, k1, LOTRMod.slabSingle, 10);
			setBlockAndMetadata(world, -2, 4, k1, Blocks.log, 0);
			setBlockAndMetadata(world, -2, 5, k1, Blocks.log, 0);
			
			setBlockAndMetadata(world, 2, 3, k1, LOTRMod.slabSingle, 10);
			setBlockAndMetadata(world, 2, 4, k1, Blocks.log, 0);
			setBlockAndMetadata(world, 2, 5, k1, Blocks.log, 0);
		}
		
		for (int i1 = -1; i1 <= 1; i1++)
		{
			setBlockAndMetadata(world, i1, 1, 2, LOTRMod.stairsGondorBrick, 3);
			
			setBlockAndMetadata(world, i1, 3, -2, LOTRMod.slabSingle, 10);
			setBlockAndMetadata(world, i1, 4, -2, Blocks.log, 0);
			setBlockAndMetadata(world, i1, 5, -2, Blocks.log, 0);
			
			setBlockAndMetadata(world, i1, 3, 2, LOTRMod.slabSingle, 10);
			setBlockAndMetadata(world, i1, 4, 2, Blocks.log, 0);
			setBlockAndMetadata(world, i1, 5, 2, Blocks.log, 0);
		}
		
		for (int j1 = 1; j1 <= 4; j1++)
		{
			setBlockAndMetadata(world, 0, j1, 0, Blocks.ladder, 2);
		}
		
		setBlockAndMetadata(world, 0, 1, -1, Blocks.wooden_door, 1);
		setBlockAndMetadata(world, 0, 2, -1, Blocks.wooden_door, 8);
		
		setBlockAndMetadata(world, 0, 5, 0, Blocks.trapdoor, 0);
		
		setBlockAndMetadata(world, 0, 5, 1, LOTRMod.slabSingle, 2);
		
		setBlockAndMetadata(world, 1, 5, 1, Blocks.chest, 2);
		fillChest(world, random, 1, 5, 1, LOTRChestContents.GONDOR_FORTRESS_SUPPLIES);
		
		for (int i1 = -2; i1 <= 2; i1++)
		{
			for (int k1 = -2; k1 <= 2; k1++)
			{
				if (Math.abs(i1) == 2 || Math.abs(k1) ==2)
				{
					setBlockAndMetadata(world, i1, 6, k1, LOTRMod.wall, 2);
					if (Math.abs(i1) == 2 && Math.abs(k1) == 2)
					{
						setBlockAndMetadata(world, i1, 7, k1, Blocks.torch, 5);
					}
				}
			}
		}
		
		int soldiers = 1 + random.nextInt(2);
		for (int l = 0; l < soldiers; l++)
		{
			LOTREntityGondorSoldier soldier = random.nextBoolean() ? new LOTREntityGondorSoldier(world) : new LOTREntityGondorArcher(world);
			soldier.spawnRidingHorse = false;
			spawnNPCAndSetHome(soldier, world, 0, 6, 0, 8);
		}
		
		return true;
	}
}
