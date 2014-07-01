package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityGondorArcher;
import lotr.common.entity.npc.LOTREntityGondorSoldier;
import lotr.common.entity.npc.LOTREntityGondorianCaptain;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class LOTRWorldGenGondorFortress extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenGondorFortress(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass)
			{
				return false;
			}
			if (world.getBiomeGenForCoords(i, k) != LOTRBiome.gondor)
			{
				return false;
			}
		}
		
		j--;
		
		int rotation = random.nextInt(4);
		
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
			switch (rotation)
			{
				case 0:
					k += 9;
					break;
				case 1:
					i -= 9;
					break;
				case 2:
					k -= 9;
					break;
				case 3:
					i += 9;
					break;
			}
		}
		
		if (restrictions)
		{
			List soldiers = world.getEntitiesWithinAABB(LOTREntityGondorSoldier.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1D, j + 1D, k + 1D).offset(0D, 10D, 0D).expand(34D, 12D, 34D));
			if (!soldiers.isEmpty())
			{
				return false;
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1D, j + 1D, k + 1D).offset(0D, 10D, 0D).expand(34D, 12D, 34D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		generateBasicStructure(world, random, i, j, k);
		
		switch (rotation)
		{
			case 0:
				return generateFacingSouth(world, random, i, j, k);
			case 1:
				return generateFacingWest(world, random, i, j, k);
			case 2:
				return generateFacingNorth(world, random, i, j, k);
			case 3:
				return generateFacingEast(world, random, i, j, k);
		}
		
		return true;
	}
	
	private void generateBasicStructure(World world, Random random, int i, int j, int k)
	{
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 11; j1++)
			{
				for (int k1 = k - 5; k1 <= k + 5; k1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
			}
		}
		
		for (int i1 = i - 6; i1 <= i + 6; i1++)
		{
			placeRandomStairs(world, random, i1, j + 1, k - 6, 2);
			placeRandomStairs(world, random, i1, j + 1, k + 6, 3);
		}
		
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			placeRandomStairs(world, random, i - 6, j + 1, k1, 0);
			placeRandomStairs(world, random, i + 6, j + 1, k1, 1);
		}
		
		for (int i1 = i - 6; i1 <= i + 6; i1++)
		{
			for (int k1 = k - 6; k1 <= k + 6; k1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
			}
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				for (int j1 = j + 2; j1 <= j + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				for (int j1 = j + 7; j1 <= j + 10; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int j1 = j + 4; j1 <= j + 9; j1 += 5)
		{
			setBlockAndNotifyAdequately(world, i - 4, j1, k - 2, Blocks.torch, 1);
			setBlockAndNotifyAdequately(world, i - 4, j1, k + 2, Blocks.torch, 1);
			setBlockAndNotifyAdequately(world, i + 4, j1, k - 2, Blocks.torch, 2);
			setBlockAndNotifyAdequately(world, i + 4, j1, k + 2, Blocks.torch, 2);
			setBlockAndNotifyAdequately(world, i - 2, j1, k - 4, Blocks.torch, 3);
			setBlockAndNotifyAdequately(world, i + 2, j1, k - 4, Blocks.torch, 3);
			setBlockAndNotifyAdequately(world, i - 2, j1, k + 4, Blocks.torch, 4);
			setBlockAndNotifyAdequately(world, i + 2, j1, k + 4, Blocks.torch, 4);
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int j1 = j + 12; j1 <= j + 16; j1++)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
			}
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			placeRandomStairs(world, random, i1, j + 12, k - 5, 2);
			placeRandomStairs(world, random, i1, j + 12, k + 5, 3);
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			placeRandomStairs(world, random, i - 5, j + 12, k1, 0);
			placeRandomStairs(world, random, i + 5, j + 12, k1, 1);
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				for (int j1 = j + 12; j1 <= j + 15; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 14, k - 2, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 3, j + 14, k + 2, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 3, j + 14, k - 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 3, j + 14, k + 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 2, j + 14, k - 3, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 2, j + 14, k - 3, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 2, j + 14, k + 3, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 2, j + 14, k + 3, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 17, k - 2, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i - 2, j + 17, k + 2, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i + 2, j + 17, k - 2, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i + 2, j + 17, k + 2, Blocks.torch, 5);
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			placeRandomWall(world, random, i1, j + 17, k - 4);
			placeRandomWall(world, random, i1, j + 17, k + 4);
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			placeRandomWall(world, random, i - 4, j + 17, k1);
			placeRandomWall(world, random, i + 4, j + 17, k1);
		}
		
		for (int j1 = j + 17; j1 <= j + 20; j1++)
		{
			placeRandomWall(world, random, i - 4, j1, k - 4);
			placeRandomWall(world, random, i - 4, j1, k + 4);
			placeRandomWall(world, random, i + 4, j1, k - 4);
			placeRandomWall(world, random, i + 4, j1, k + 4);
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				placeRandomBrick(world, random, i1, j + 21, k1);
				if (i1 >= i - 1 && i1 <= i + 1 && k1 >= k - 1 && k1 <= k + 1)
				{
					placeRandomBrick(world, random, i1, j + 22, k1);
				}
				else
				{
					placeRandomSlab(world, random, i1, j + 22, k1, false);
				}
			}
		}
		
		placeRandomSlab(world, random, i - 4, j + 21, k - 4, false);
		placeRandomSlab(world, random, i - 4, j + 21, k - 3, false);
		placeRandomStairs(world, random, i - 4, j + 21, k - 2, 0);
		placeRandomStairs(world, random, i - 4, j + 21, k - 1, 3);
		placeRandomSlab(world, random, i - 4, j + 21, k, false);
		placeRandomStairs(world, random, i - 4, j + 21, k + 1, 2);
		placeRandomStairs(world, random, i - 4, j + 21, k + 2, 0);
		placeRandomSlab(world, random, i - 4, j + 21, k + 3, false);
		placeRandomSlab(world, random, i - 4, j + 21, k + 4, false);
		
		placeRandomSlab(world, random, i - 3, j + 21, k - 4, false);
		placeRandomBrick(world, random, i - 3, j + 21, k - 3);
		placeRandomBrick(world, random, i - 3, j + 21, k - 2);
		placeRandomStairs(world, random, i - 3, j + 21, k - 1, 0);
		placeRandomStairs(world, random, i - 3, j + 21, k, 0);
		placeRandomStairs(world, random, i - 3, j + 21, k + 1, 0);
		placeRandomBrick(world, random, i - 3, j + 21, k + 2);
		placeRandomBrick(world, random, i - 3, j + 21, k + 3);
		placeRandomSlab(world, random, i - 3, j + 21, k + 4, false);
		
		placeRandomSlab(world, random, i + 4, j + 21, k - 4, false);
		placeRandomSlab(world, random, i + 4, j + 21, k - 3, false);
		placeRandomStairs(world, random, i + 4, j + 21, k - 2, 1);
		placeRandomStairs(world, random, i + 4, j + 21, k - 1, 3);
		placeRandomSlab(world, random, i + 4, j + 21, k, false);
		placeRandomStairs(world, random, i + 4, j + 21, k + 1, 2);
		placeRandomStairs(world, random, i + 4, j + 21, k + 2, 1);
		placeRandomSlab(world, random, i + 4, j + 21, k + 3, false);
		placeRandomSlab(world, random, i + 4, j + 21, k + 4, false);
		
		placeRandomSlab(world, random, i + 3, j + 21, k - 4, false);
		placeRandomBrick(world, random, i + 3, j + 21, k - 3);
		placeRandomBrick(world, random, i + 3, j + 21, k - 2);
		placeRandomStairs(world, random, i + 3, j + 21, k - 1, 1);
		placeRandomStairs(world, random, i + 3, j + 21, k, 1);
		placeRandomStairs(world, random, i + 3, j + 21, k + 1, 1);
		placeRandomBrick(world, random, i + 3, j + 21, k + 2);
		placeRandomBrick(world, random, i + 3, j + 21, k + 3);
		placeRandomSlab(world, random, i + 3, j + 21, k + 4, false);
		
		placeRandomStairs(world, random, i - 2, j + 21, k + 4, 3);
		placeRandomStairs(world, random, i - 1, j + 21, k + 4, 1);
		placeRandomSlab(world, random, i, j + 21, k + 4, false);
		placeRandomStairs(world, random, i + 1, j + 21, k + 4, 0);
		placeRandomStairs(world, random, i + 2, j + 21, k + 4, 3);
		
		placeRandomBrick(world, random, i - 2, j + 21, k + 3);
		placeRandomStairs(world, random, i - 1, j + 21, k + 3, 3);
		placeRandomStairs(world, random, i, j + 21, k + 3, 3);
		placeRandomStairs(world, random, i + 1, j + 21, k + 3, 3);
		placeRandomBrick(world, random, i + 2, j + 21, k + 3);
		
		placeRandomStairs(world, random, i - 2, j + 21, k - 4, 2);
		placeRandomStairs(world, random, i - 1, j + 21, k - 4, 1);
		placeRandomSlab(world, random, i, j + 21, k - 4, false);
		placeRandomStairs(world, random, i + 1, j + 21, k - 4, 0);
		placeRandomStairs(world, random, i + 2, j + 21, k - 4, 2);
		
		placeRandomBrick(world, random, i - 2, j + 21, k - 3);
		placeRandomStairs(world, random, i - 1, j + 21, k - 3, 2);
		placeRandomStairs(world, random, i, j + 21, k - 3, 2);
		placeRandomStairs(world, random, i + 1, j + 21, k - 3, 2);
		placeRandomBrick(world, random, i + 2, j + 21, k - 3);
		
		placeBarredWindowOnZ(world, i - 5, j + 3, k);
		placeBarredWindowOnZ(world, i + 5, j + 3, k);
		placeBarredWindowOnX(world, i, j + 3, k - 5);
		placeBarredWindowOnX(world, i, j + 3, k + 5);
		
		placeBarredWindowOnZ(world, i - 5, j + 8, k);
		placeBarredWindowOnZ(world, i + 5, j + 8, k);
		placeBarredWindowOnX(world, i, j + 8, k - 5);
		placeBarredWindowOnX(world, i, j + 8, k + 5);
		
		placeBarredWindowOnZ(world, i - 4, j + 13, k);
		placeBarredWindowOnZ(world, i + 4, j + 13, k);
		placeBarredWindowOnX(world, i, j + 13, k - 4);
		placeBarredWindowOnX(world, i, j + 13, k + 4);
		
		if (usingPlayer == null)
		{
			LOTRLevelData.gondorFortressLocations.add(new ChunkCoordinates(i, j, k));
		}
	}
	
	private boolean generateFacingSouth(World world, Random random, int i, int j, int k)
	{
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int k1 = k - 7; k1 >= k - 8; k1--)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				placeRandomBrick(world, random, i1, j1, k - 6);
			}
			
			if (Math.abs(i1 - i) == 2)
			{
				placeRandomBrick(world, random, i1, j + 4, k - 6);
			}
			else
			{
				placeRandomSlab(world, random, i1, j + 4, k - 6, false);
			}
			
			placeRandomStairs(world, random, i1, j + 1, k - 8, 2);
		}
		
		placeWallBanner(world, i, j + 6, k - 5, 2, 0);
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k - 6; k1 >= k - 7; k1--)
			{
				placeRandomBrick(world, random, i1, j + 1, k1);
			}
			
			placeRandomBrick(world, random, i1, j + 1, k - 5);
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 5, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 5, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 5, Blocks.air, 0);
			placeRandomSlab(world, random, i1, j + 4, k - 5, true);
		}
		
		placeRandomStairs(world, random, i - 2, j + 1, k - 7, 0);
		placeRandomStairs(world, random, i + 2, j + 1, k - 7, 1);
		
		setBlockAndNotifyAdequately(world, i, j + 2, k - 6, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i, j + 3, k - 6, Blocks.wooden_door, 8);
		
		placeRandomSlab(world, random, i - 4, j + 2, k - 4, true);
		placeBarrel(world, random, i - 4, j + 3, k - 4, 5, LOTRMod.mugAle);
		placeRandomSlab(world, random, i - 4, j + 2, k - 3, true);
		placeBarrel(world, random, i - 4, j + 3, k - 3, 5, LOTRMod.mugAle);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 2, Blocks.chest, 5);
		LOTRChestContents.fillChest(world, random, i - 4, j + 2, k - 2, LOTRChestContents.GONDOR_FORTRESS_DRINKS);
		
		placeRandomSlab(world, random, i + 4, j + 2, k - 4, true);
		placeBarrel(world, random, i + 4, j + 3, k - 4, 4, LOTRMod.mugAle);
		placeRandomSlab(world, random, i + 4, j + 2, k - 3, true);
		placeBarrel(world, random, i + 4, j + 3, k - 3, 4, LOTRMod.mugAle);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 2, Blocks.chest, 4);
		LOTRChestContents.fillChest(world, random, i + 4, j + 2, k - 2, LOTRChestContents.GONDOR_FORTRESS_DRINKS);
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 4, LOTRMod.gondorianTable, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 4, LOTRMod.gondorianTable, 0);
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 2; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 6, k1, Blocks.air, 0);
				
				for (int j1 = j + 2; j1 < j + 2 + k1 - k + 1; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				placeRandomStairs(world, random, i1, j + 2 + k1 - k + 1, k1, 2);
			}
			
			placeRandomStairs(world, random, i1, j + 6, k + 3, 2);
		}
		
		setBlockAndNotifyAdequately(world, i, j + 7, k - 4, Blocks.chest, 3);
		LOTRChestContents.fillChest(world, random, i, j + 7, k - 4, LOTRChestContents.GONDOR_FORTRESS_SUPPLIES);
		
		for (int i1 = i - 3; i1 <= i + 3; i1 += 6)
		{
			for (int k1 = k + 2; k1 >= k - 2; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j + 11, k1, Blocks.air, 0);
				
				for (int j1 = j + 7; j1 < j + 7 - k1 + k + 2; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				placeRandomStairs(world, random, i1, j + 7 - k1 + k + 2, k1, 3);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k - 2; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 16, k1, Blocks.air, 0);
				
				for (int j1 = j + 12; j1 < j + 12 + k1 - k + 2; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				placeRandomStairs(world, random, i1, j + 12 + k1 - k + 2, k1, 2);
			}
			
			placeRandomStairs(world, random, i1, j + 16, k + 2, 2);
		}
		
		for (int k1 = k + 5; k1 <= k + 28; k1++)
		{
			for (int j1 = j + 12; j1 <= j + 15; j1++)
			{
				for (int i1 = i - 2; i1 <= i + 2; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			placeRandomBrick(world, random, i1, j + 13, k + 4);
			placeRandomBrick(world, random, i1, j + 14, k + 4);
		}
		
		placeRandomBrick(world, random, i - 2, j + 12, k + 5);
		placeRandomBrick(world, random, i + 2, j + 12, k + 5);
		placeRandomBrick(world, random, i - 2, j + 13, k + 5);
		placeRandomBrick(world, random, i + 2, j + 13, k + 5);
		setBlockAndNotifyAdequately(world, i - 2, j + 14, k + 5, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 2, j + 14, k + 5, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i, j + 12, k + 4, Blocks.wooden_door, 3);
		setBlockAndNotifyAdequately(world, i, j + 13, k + 4, Blocks.wooden_door, 8);
		
		for (int k1 = k + 6; k1 <= k + 28; k1++)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				placeRandomBrick(world, random, i1, j + 11, k1);
			}
			
			placeRandomWall(world, random, i - 2, j + 12, k1);
			placeRandomWall(world, random, i + 2, j + 12, k1);
			placeRandomStairs(world, random, i - 2, j + 11, k1, 4);
			placeRandomStairs(world, random, i + 2, j + 11, k1, 5);
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			placeRandomStairs(world, random, i1, j + 10, k + 6, 7);
			placeRandomStairs(world, random, i1, j + 10, k + 16, 6);
			placeRandomStairs(world, random, i1, j + 10, k + 18, 7);
			placeRandomStairs(world, random, i1, j + 10, k + 28, 6);
			
			for (int j1 = j + 10; !LOTRMod.isOpaque(world, i1, j1, k + 17) && j1 >= 0; j1--)
			{
				placeRandomBrick(world, random, i1, j1, k + 17);
			}
		}
		
		for (int j1 = j + 12; j1 <= j + 13; j1++)
		{
			placeRandomBrick(world, random, i - 2, j1, k + 11);
			placeRandomBrick(world, random, i + 2, j1, k + 11);
			placeRandomBrick(world, random, i - 2, j1, k + 23);
			placeRandomBrick(world, random, i + 2, j1, k + 23);
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j + 13, k + 11, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 1, j + 13, k + 11, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 1, j + 13, k + 23, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 1, j + 13, k + 23, Blocks.torch, 2);
		
		placeBanner(world, i - 2, j + 14, k + 11, 3, 0);
		placeBanner(world, i + 2, j + 14, k + 11, 1, 0);
		placeBanner(world, i - 2, j + 14, k + 23, 3, 0);
		placeBanner(world, i + 2, j + 14, k + 23, 1, 0);
		
		for (int j1 = j + 12; j1 <= j + 15; j1++)
		{
			placeRandomBrick(world, random, i - 2, j1, k + 17);
			placeRandomBrick(world, random, i + 2, j1, k + 17);
		}
		
		placeRandomStairs(world, random, i - 1, j + 15, k + 17, 5);
		placeRandomStairs(world, random, i + 1, j + 15, k + 17, 4);
		placeRandomSlab(world, random, i, j + 15, k + 17, true);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 14, k + 16, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 2, j + 14, k + 16, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 2, j + 14, k + 18, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 2, j + 14, k + 18, Blocks.torch, 3);
		
		LOTRWorldGenBeaconTower beaconTower = new LOTRWorldGenBeaconTower(notifyChanges);
		beaconTower.restrictions = false;
		beaconTower.generateWithSetHeightAndRotation(world, random, i, j + 11, k + 31, 0, 2);
		
		spawnGondorSoldier(world, i, j + 1, k - 3, i, j, k);
		spawnGondorSoldier(world, i, j + 1, k + 3, i, j, k);
		spawnGondorSoldier(world, i, j + 6, k - 3, i, j, k);
		spawnGondorSoldier(world, i, j + 6, k + 3, i, j, k);
		spawnGondorSoldier(world, i, j + 11, k - 3, i, j, k);
		spawnGondorSoldier(world, i, j + 11, k + 3, i, j, k);
		spawnGondorSoldier(world, i, j + 11, k + 11, i, j, k);
		spawnGondorSoldier(world, i, j + 11, k + 23, i, j, k);
		
		LOTREntityGondorianCaptain captain = new LOTREntityGondorianCaptain(world);
		captain.setLocationAndAngles(i + 0.5D, j + 15, k + 0.5D, 0F, 0F);
		captain.spawnRidingHorse = false;
		captain.onSpawnWithEgg(null);
		world.spawnEntityInWorld(captain);
		captain.setHomeArea(i, j + 15, k, 8);
		
		return true;
	}
	
	private boolean generateFacingWest(World world, Random random, int i, int j, int k)
	{
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			for (int i1 = i + 7; i1 <= i + 8; i1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
			}
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				placeRandomBrick(world, random, i + 6, j1, k1);
			}
			
			if (Math.abs(k1 - k) == 2)
			{
				placeRandomBrick(world, random, i + 6, j + 4, k1);
			}
			else
			{
				placeRandomSlab(world, random, i + 6, j + 4, k1, false);
			}
			
			placeRandomStairs(world, random, i + 8, j + 1, k1, 1);
		}
		
		placeWallBanner(world, i + 5, j + 6, k, 3, 0);
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int i1 = i + 6; i1 <= i + 7; i1++)
			{
				placeRandomBrick(world, random, i1, j + 1, k1);
			}
			
			placeRandomBrick(world, random, i + 5, j + 1, k1);
			setBlockAndNotifyAdequately(world, i + 5, j + 2, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i + 5, j + 3, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i + 5, j + 4, k1, Blocks.air, 0);
			placeRandomSlab(world, random, i + 5, j + 4, k1, true);
		}
		
		placeRandomStairs(world, random, i + 7, j + 1, k - 2, 2);
		placeRandomStairs(world, random, i + 7, j + 1, k + 2, 3);
		
		setBlockAndNotifyAdequately(world, i + 6, j + 2, k, Blocks.wooden_door, 2);
		setBlockAndNotifyAdequately(world, i + 6, j + 3, k, Blocks.wooden_door, 8);
		
		placeRandomSlab(world, random, i + 4, j + 2, k - 4, true);
		placeBarrel(world, random, i + 4, j + 3, k - 4, 3, LOTRMod.mugAle);
		placeRandomSlab(world, random, i + 3, j + 2, k - 4, true);
		placeBarrel(world, random, i + 3, j + 3, k - 4, 3, LOTRMod.mugAle);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 4, Blocks.chest, 3);
		LOTRChestContents.fillChest(world, random, i + 2, j + 2, k - 4, LOTRChestContents.GONDOR_FORTRESS_DRINKS);
		
		placeRandomSlab(world, random, i + 4, j + 2, k + 4, true);
		placeBarrel(world, random, i + 4, j + 3, k + 4, 2, LOTRMod.mugAle);
		placeRandomSlab(world, random, i + 3, j + 2, k + 4, true);
		placeBarrel(world, random, i + 3, j + 3, k + 4, 2, LOTRMod.mugAle);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k + 4, Blocks.chest, 2);
		LOTRChestContents.fillChest(world, random, i + 2, j + 2, k + 4, LOTRChestContents.GONDOR_FORTRESS_DRINKS);
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 4, LOTRMod.gondorianTable, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 4, LOTRMod.gondorianTable, 0);
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int i1 = i + 1; i1 >= i - 2; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j + 6, k1, Blocks.air, 0);
				
				for (int j1 = j + 2; j1 < j + 2 - i1 + i + 1; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				placeRandomStairs(world, random, i1, j + 2 - i1 + i + 1, k1, 1);
			}
			
			placeRandomStairs(world, random, i - 3, j + 6, k1, 1);
		}
		
		setBlockAndNotifyAdequately(world, i + 4, j + 7, k, Blocks.chest, 5);
		LOTRChestContents.fillChest(world, random, i + 4, j + 7, k, LOTRChestContents.GONDOR_FORTRESS_SUPPLIES);
		
		for (int k1 = k - 3; k1 <= k + 3; k1 += 6)
		{
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 11, k1, Blocks.air, 0);
				
				for (int j1 = j + 7; j1 < j + 7 + i1 - i + 2; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				placeRandomStairs(world, random, i1, j + 7 + i1 - i + 2, k1, 0);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int i1 = i + 2; i1 >= i - 1; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j + 16, k1, Blocks.air, 0);
				
				for (int j1 = j + 12; j1 < j + 12 - i1 + i + 2; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				placeRandomStairs(world, random, i1, j + 12 - i1 + i + 2, k1, 1);
			}
			
			placeRandomStairs(world, random, i - 2, j + 16, k1, 1);
		}
		
		for (int i1 = i - 5; i1 >= i - 28; i1--)
		{
			for (int j1 = j + 12; j1 <= j + 15; j1++)
			{
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			placeRandomBrick(world, random, i - 4, j + 13, k1);
			placeRandomBrick(world, random, i - 4, j + 14, k1);
		}
		
		placeRandomBrick(world, random, i - 5, j + 12, k - 2);
		placeRandomBrick(world, random, i - 5, j + 12, k + 2);
		placeRandomBrick(world, random, i - 5, j + 13, k - 2);
		placeRandomBrick(world, random, i - 5, j + 13, k + 2);
		setBlockAndNotifyAdequately(world, i - 5, j + 14, k - 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 5, j + 14, k + 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 4, j + 12, k, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 13, k, Blocks.wooden_door, 8);
		
		for (int i1 = i - 6; i1 >= i - 28; i1--)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				placeRandomBrick(world, random, i1, j + 11, k1);
			}
			
			placeRandomWall(world, random, i1, j + 12, k - 2);
			placeRandomWall(world, random, i1, j + 12, k + 2);
			placeRandomStairs(world, random, i1, j + 11, k - 2, 6);
			placeRandomStairs(world, random, i1, j + 11, k + 2, 7);
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			placeRandomStairs(world, random, i - 6, j + 10, k1, 4);
			placeRandomStairs(world, random, i - 16, j + 10, k1, 5);
			placeRandomStairs(world, random, i - 18, j + 10, k1, 4);
			placeRandomStairs(world, random, i - 28, j + 10, k1, 5);
			
			for (int j1 = j + 10; !LOTRMod.isOpaque(world, i - 17, j1, k1) && j1 >= 0; j1--)
			{
				placeRandomBrick(world, random, i - 17, j1, k1);
			}
		}
		
		for (int j1 = j + 12; j1 <= j + 13; j1++)
		{
			placeRandomBrick(world, random, i - 11, j1, k - 2);
			placeRandomBrick(world, random, i - 11, j1, k + 2);
			placeRandomBrick(world, random, i - 23, j1, k - 2);
			placeRandomBrick(world, random, i - 23, j1, k + 2);
		}
		
		setBlockAndNotifyAdequately(world, i - 11, j + 13, k - 1, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 11, j + 13, k + 1, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 23, j + 13, k - 1, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 23, j + 13, k + 1, Blocks.torch, 4);
		
		placeBanner(world, i - 11, j + 14, k - 2, 2, 0);
		placeBanner(world, i - 11, j + 14, k + 2, 0, 0);
		placeBanner(world, i - 23, j + 14, k - 2, 2, 0);
		placeBanner(world, i - 23, j + 14, k + 2, 0, 0);
		
		for (int j1 = j + 12; j1 <= j + 15; j1++)
		{
			placeRandomBrick(world, random, i - 17, j1, k - 2);
			placeRandomBrick(world, random, i - 17, j1, k + 2);
		}
		
		placeRandomStairs(world, random, i - 17, j + 15, k - 1, 7);
		placeRandomStairs(world, random, i - 17, j + 15, k + 1, 6);
		placeRandomSlab(world, random, i - 17, j + 15, k, true);
		
		setBlockAndNotifyAdequately(world, i - 16, j + 14, k - 2, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 16, j + 14, k + 2, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 18, j + 14, k - 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 18, j + 14, k + 2, Blocks.torch, 2);
		
		LOTRWorldGenBeaconTower beaconTower = new LOTRWorldGenBeaconTower(notifyChanges);
		beaconTower.restrictions = false;
		beaconTower.generateWithSetHeightAndRotation(world, random, i - 31, j + 11, k, 0, 3);
		
		spawnGondorSoldier(world, i + 3, j + 1, k, i, j, k);
		spawnGondorSoldier(world, i - 3, j + 1, k, i, j, k);
		spawnGondorSoldier(world, i + 3, j + 6, k, i, j, k);
		spawnGondorSoldier(world, i - 3, j + 6, k, i, j, k);
		spawnGondorSoldier(world, i + 3, j + 11, k, i, j, k);
		spawnGondorSoldier(world, i - 3, j + 11, k, i, j, k);
		spawnGondorSoldier(world, i - 11, j + 11, k, i, j, k);
		spawnGondorSoldier(world, i - 23, j + 11, k, i, j, k);
		
		LOTREntityGondorianCaptain captain = new LOTREntityGondorianCaptain(world);
		captain.setLocationAndAngles(i + 0.5D, j + 15, k + 0.5D, 0F, 0F);
		captain.onSpawnWithEgg(null);
		world.spawnEntityInWorld(captain);
		captain.setHomeArea(i, j + 15, k, 8);
		
		return true;
	}
	
	private boolean generateFacingNorth(World world, Random random, int i, int j, int k)
	{
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int k1 = k + 7; k1 <= k + 8; k1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				placeRandomBrick(world, random, i1, j1, k + 6);
			}
			
			if (Math.abs(i1 - i) == 2)
			{
				placeRandomBrick(world, random, i1, j + 4, k + 6);
			}
			else
			{
				placeRandomSlab(world, random, i1, j + 4, k + 6, false);
			}
			
			placeRandomStairs(world, random, i1, j + 1, k + 8, 3);
		}
		
		placeWallBanner(world, i, j + 6, k + 5, 0, 0);
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k + 6; k1 <= k + 7; k1++)
			{
				placeRandomBrick(world, random, i1, j + 1, k1);
			}
			
			placeRandomBrick(world, random, i1, j + 1, k + 5);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 5, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 5, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 5, Blocks.air, 0);
			placeRandomSlab(world, random, i1, j + 4, k + 5, true);
		}
		
		placeRandomStairs(world, random, i - 2, j + 1, k + 7, 0);
		placeRandomStairs(world, random, i + 2, j + 1, k + 7, 1);
		
		setBlockAndNotifyAdequately(world, i, j + 2, k + 6, Blocks.wooden_door, 3);
		setBlockAndNotifyAdequately(world, i, j + 3, k + 6, Blocks.wooden_door, 8);
		
		placeRandomSlab(world, random, i - 4, j + 2, k + 4, true);
		placeBarrel(world, random, i - 4, j + 3, k + 4, 5, LOTRMod.mugAle);
		placeRandomSlab(world, random, i - 4, j + 2, k + 3, true);
		placeBarrel(world, random, i - 4, j + 3, k + 3, 5, LOTRMod.mugAle);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 2, Blocks.chest, 5);
		LOTRChestContents.fillChest(world, random, i - 4, j + 2, k + 2, LOTRChestContents.GONDOR_FORTRESS_DRINKS);
		
		placeRandomSlab(world, random, i + 4, j + 2, k + 4, true);
		placeBarrel(world, random, i + 4, j + 3, k + 4, 4, LOTRMod.mugAle);
		placeRandomSlab(world, random, i + 4, j + 2, k + 3, true);
		placeBarrel(world, random, i + 4, j + 3, k + 3, 4, LOTRMod.mugAle);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 2, Blocks.chest, 4);
		LOTRChestContents.fillChest(world, random, i + 4, j + 2, k + 2, LOTRChestContents.GONDOR_FORTRESS_DRINKS);
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 4, LOTRMod.gondorianTable, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 4, LOTRMod.gondorianTable, 0);
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k + 1; k1 >= k - 2; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j + 6, k1, Blocks.air, 0);
				
				for (int j1 = j + 2; j1 < j + 2 - k1 + k + 1; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				placeRandomStairs(world, random, i1, j + 2 - k1 + k + 1, k1, 3);
			}
			
			placeRandomStairs(world, random, i1, j + 6, k - 3, 3);
		}
		
		setBlockAndNotifyAdequately(world, i, j + 7, k + 4, Blocks.chest, 2);
		LOTRChestContents.fillChest(world, random, i, j + 7, k + 4, LOTRChestContents.GONDOR_FORTRESS_SUPPLIES);
		
		for (int i1 = i - 3; i1 <= i + 3; i1 += 6)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 11, k1, Blocks.air, 0);
				
				for (int j1 = j + 7; j1 < j + 7 + k1 - k + 2; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				placeRandomStairs(world, random, i1, j + 7 + k1 - k + 2, k1, 2);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k + 2; k1 >= k - 1; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j + 16, k1, Blocks.air, 0);
				
				for (int j1 = j + 12; j1 < j + 12 - k1 + k + 2; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				placeRandomStairs(world, random, i1, j + 12 - k1 + k + 2, k1, 3);
			}
			
			placeRandomStairs(world, random, i1, j + 16, k - 2, 3);
		}
		
		for (int k1 = k - 5; k1 >= k - 28; k1--)
		{
			for (int j1 = j + 12; j1 <= j + 15; j1++)
			{
				for (int i1 = i - 2; i1 <= i + 2; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			placeRandomBrick(world, random, i1, j + 13, k - 4);
			placeRandomBrick(world, random, i1, j + 14, k - 4);
		}
		
		placeRandomBrick(world, random, i - 2, j + 12, k - 5);
		placeRandomBrick(world, random, i + 2, j + 12, k - 5);
		placeRandomBrick(world, random, i - 2, j + 13, k - 5);
		placeRandomBrick(world, random, i + 2, j + 13, k - 5);
		setBlockAndNotifyAdequately(world, i - 2, j + 14, k - 5, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 2, j + 14, k - 5, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i, j + 12, k - 4, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i, j + 13, k - 4, Blocks.wooden_door, 8);
		
		for (int k1 = k - 6; k1 >= k - 28; k1--)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				placeRandomBrick(world, random, i1, j + 11, k1);
			}
			
			placeRandomWall(world, random, i - 2, j + 12, k1);
			placeRandomWall(world, random, i + 2, j + 12, k1);
			placeRandomStairs(world, random, i - 2, j + 11, k1, 4);
			placeRandomStairs(world, random, i + 2, j + 11, k1, 5);
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			placeRandomStairs(world, random, i1, j + 10, k - 6, 6);
			placeRandomStairs(world, random, i1, j + 10, k - 16, 7);
			placeRandomStairs(world, random, i1, j + 10, k - 18, 6);
			placeRandomStairs(world, random, i1, j + 10, k - 28, 7);
			
			for (int j1 = j + 10; !LOTRMod.isOpaque(world, i1, j1, k - 17) && j1 >= 0; j1--)
			{
				placeRandomBrick(world, random, i1, j1, k - 17);
			}
		}
		
		for (int j1 = j + 12; j1 <= j + 13; j1++)
		{
			placeRandomBrick(world, random, i - 2, j1, k - 11);
			placeRandomBrick(world, random, i + 2, j1, k - 11);
			placeRandomBrick(world, random, i - 2, j1, k - 23);
			placeRandomBrick(world, random, i + 2, j1, k - 23);
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j + 13, k - 11, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 1, j + 13, k - 11, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 1, j + 13, k - 23, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 1, j + 13, k - 23, Blocks.torch, 2);
		
		placeBanner(world, i - 2, j + 14, k - 11, 3, 0);
		placeBanner(world, i + 2, j + 14, k - 11, 1, 0);
		placeBanner(world, i - 2, j + 14, k - 23, 3, 0);
		placeBanner(world, i + 2, j + 14, k - 23, 1, 0);
		
		for (int j1 = j + 12; j1 <= j + 15; j1++)
		{
			placeRandomBrick(world, random, i - 2, j1, k - 17);
			placeRandomBrick(world, random, i + 2, j1, k - 17);
		}
		
		placeRandomStairs(world, random, i - 1, j + 15, k - 17, 5);
		placeRandomStairs(world, random, i + 1, j + 15, k - 17, 4);
		placeRandomSlab(world, random, i, j + 15, k - 17, true);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 14, k - 16, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 2, j + 14, k - 16, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 2, j + 14, k - 18, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 2, j + 14, k - 18, Blocks.torch, 4);
		
		LOTRWorldGenBeaconTower beaconTower = new LOTRWorldGenBeaconTower(notifyChanges);
		beaconTower.restrictions = false;
		beaconTower.generateWithSetHeightAndRotation(world, random, i, j + 11, k - 31, 0, 0);
		
		spawnGondorSoldier(world, i, j + 1, k + 3, i, j, k);
		spawnGondorSoldier(world, i, j + 1, k - 3, i, j, k);
		spawnGondorSoldier(world, i, j + 6, k + 3, i, j, k);
		spawnGondorSoldier(world, i, j + 6, k - 3, i, j, k);
		spawnGondorSoldier(world, i, j + 11, k + 3, i, j, k);
		spawnGondorSoldier(world, i, j + 11, k - 3, i, j, k);
		spawnGondorSoldier(world, i, j + 11, k - 11, i, j, k);
		spawnGondorSoldier(world, i, j + 11, k - 23, i, j, k);
		
		LOTREntityGondorianCaptain captain = new LOTREntityGondorianCaptain(world);
		captain.setLocationAndAngles(i + 0.5D, j + 15, k + 0.5D, 0F, 0F);
		captain.onSpawnWithEgg(null);
		world.spawnEntityInWorld(captain);
		captain.setHomeArea(i, j + 15, k, 8);
		
		return true;
	}
	
	private boolean generateFacingEast(World world, Random random, int i, int j, int k)
	{
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			for (int i1 = i - 7; i1 >= i - 8; i1--)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
			}
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				placeRandomBrick(world, random, i - 6, j1, k1);
			}
			
			if (Math.abs(k1 - k) == 2)
			{
				placeRandomBrick(world, random, i - 6, j + 4, k1);
			}
			else
			{
				placeRandomSlab(world, random, i - 6, j + 4, k1, false);
			}
			
			placeRandomStairs(world, random, i - 8, j + 1, k1, 0);
		}
		
		placeWallBanner(world, i - 5, j + 6, k, 1, 0);
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int i1 = i - 6; i1 >= i - 7; i1--)
			{
				placeRandomBrick(world, random, i1, j + 1, k1);
			}
			
			placeRandomBrick(world, random, i - 5, j + 1, k1);
			setBlockAndNotifyAdequately(world, i - 5, j + 2, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i - 5, j + 3, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i - 5, j + 4, k1, Blocks.air, 0);
			placeRandomSlab(world, random, i - 5, j + 4, k1, true);
		}
		
		placeRandomStairs(world, random, i - 7, j + 1, k - 2, 2);
		placeRandomStairs(world, random, i - 7, j + 1, k + 2, 3);
		
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 3, k, Blocks.wooden_door, 8);
		
		placeRandomSlab(world, random, i - 4, j + 2, k - 4, true);
		placeBarrel(world, random, i - 4, j + 3, k - 4, 3, LOTRMod.mugAle);
		placeRandomSlab(world, random, i - 3, j + 2, k - 4, true);
		placeBarrel(world, random, i - 3, j + 3, k - 4, 3, LOTRMod.mugAle);
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 4, Blocks.chest, 3);
		LOTRChestContents.fillChest(world, random, i - 2, j + 2, k - 4, LOTRChestContents.GONDOR_FORTRESS_DRINKS);
		
		placeRandomSlab(world, random, i - 4, j + 2, k + 4, true);
		placeBarrel(world, random, i - 4, j + 3, k + 4, 2, LOTRMod.mugAle);
		placeRandomSlab(world, random, i - 3, j + 2, k + 4, true);
		placeBarrel(world, random, i - 3, j + 3, k + 4, 2, LOTRMod.mugAle);
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 4, Blocks.chest, 2);
		LOTRChestContents.fillChest(world, random, i - 2, j + 2, k + 4, LOTRChestContents.GONDOR_FORTRESS_DRINKS);
		
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 4, LOTRMod.gondorianTable, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 4, LOTRMod.gondorianTable, 0);
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int i1 = i - 1; i1 <= i + 2; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 6, k1, Blocks.air, 0);
				
				for (int j1 = j + 2; j1 < j + 2 + i1 - i + 1; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				placeRandomStairs(world, random, i1, j + 2 + i1 - i + 1, k1, 0);
			}
			
			placeRandomStairs(world, random, i + 3, j + 6, k1, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 7, k, Blocks.chest, 5);
		LOTRChestContents.fillChest(world, random, i - 4, j + 7, k, LOTRChestContents.GONDOR_FORTRESS_SUPPLIES);
		
		for (int k1 = k - 3; k1 <= k + 3; k1 += 6)
		{
			for (int i1 = i + 2; i1 >= i - 2; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j + 11, k1, Blocks.air, 0);
				
				for (int j1 = j + 7; j1 < j + 7 - i1 + i + 2; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				placeRandomStairs(world, random, i1, j + 7 - i1 + i + 2, k1, 1);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int i1 = i - 2; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 16, k1, Blocks.air, 0);
				
				for (int j1 = j + 12; j1 < j + 12 + i1 - i + 2; j1++)
				{
					placeRandomBrick(world, random, i1, j1, k1);
				}
				placeRandomStairs(world, random, i1, j + 12 + i1 - i + 2, k1, 0);
			}
			
			placeRandomStairs(world, random, i + 2, j + 16, k1, 0);
		}
		
		for (int i1 = i + 5; i1 <= i + 28; i1++)
		{
			for (int j1 = j + 12; j1 <= j + 15; j1++)
			{
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			placeRandomBrick(world, random, i + 4, j + 13, k1);
			placeRandomBrick(world, random, i + 4, j + 14, k1);
		}
		
		placeRandomBrick(world, random, i + 5, j + 12, k - 2);
		placeRandomBrick(world, random, i + 5, j + 12, k + 2);
		placeRandomBrick(world, random, i + 5, j + 13, k - 2);
		placeRandomBrick(world, random, i + 5, j + 13, k + 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 14, k - 2, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 5, j + 14, k + 2, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 12, k, Blocks.wooden_door, 2);
		setBlockAndNotifyAdequately(world, i + 4, j + 13, k, Blocks.wooden_door, 8);
		
		for (int i1 = i + 6; i1 <= i + 28; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				placeRandomBrick(world, random, i1, j + 11, k1);
			}
			
			placeRandomWall(world, random, i1, j + 12, k - 2);
			placeRandomWall(world, random, i1, j + 12, k + 2);
			placeRandomStairs(world, random, i1, j + 11, k - 2, 6);
			placeRandomStairs(world, random, i1, j + 11, k + 2, 7);
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			placeRandomStairs(world, random, i + 6, j + 10, k1, 5);
			placeRandomStairs(world, random, i + 16, j + 10, k1, 4);
			placeRandomStairs(world, random, i + 18, j + 10, k1, 5);
			placeRandomStairs(world, random, i + 28, j + 10, k1, 4);
			
			for (int j1 = j + 10; !LOTRMod.isOpaque(world, i + 17, j1, k1) && j1 >= 0; j1--)
			{
				placeRandomBrick(world, random, i + 17, j1, k1);
			}
		}
		
		for (int j1 = j + 12; j1 <= j + 13; j1++)
		{
			placeRandomBrick(world, random, i + 11, j1, k - 2);
			placeRandomBrick(world, random, i + 11, j1, k + 2);
			placeRandomBrick(world, random, i + 23, j1, k - 2);
			placeRandomBrick(world, random, i + 23, j1, k + 2);
		}
		
		setBlockAndNotifyAdequately(world, i + 11, j + 13, k - 1, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 11, j + 13, k + 1, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 23, j + 13, k - 1, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 23, j + 13, k + 1, Blocks.torch, 4);
		
		placeBanner(world, i + 11, j + 14, k - 2, 2, 0);
		placeBanner(world, i + 11, j + 14, k + 2, 0, 0);
		placeBanner(world, i + 23, j + 14, k - 2, 2, 0);
		placeBanner(world, i + 23, j + 14, k + 2, 0, 0);
		
		for (int j1 = j + 12; j1 <= j + 15; j1++)
		{
			placeRandomBrick(world, random, i + 17, j1, k - 2);
			placeRandomBrick(world, random, i + 17, j1, k + 2);
		}
		
		placeRandomStairs(world, random, i + 17, j + 15, k - 1, 7);
		placeRandomStairs(world, random, i + 17, j + 15, k + 1, 6);
		placeRandomSlab(world, random, i + 17, j + 15, k, true);
		
		setBlockAndNotifyAdequately(world, i + 16, j + 14, k - 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 16, j + 14, k + 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 18, j + 14, k - 2, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 18, j + 14, k + 2, Blocks.torch, 1);
		
		LOTRWorldGenBeaconTower beaconTower = new LOTRWorldGenBeaconTower(notifyChanges);
		beaconTower.restrictions = false;
		beaconTower.generateWithSetHeightAndRotation(world, random, i + 31, j + 11, k, 0, 1);
		
		spawnGondorSoldier(world, i - 3, j + 1, k, i, j, k);
		spawnGondorSoldier(world, i + 3, j + 1, k, i, j, k);
		spawnGondorSoldier(world, i - 3, j + 6, k, i, j, k);
		spawnGondorSoldier(world, i + 3, j + 6, k, i, j, k);
		spawnGondorSoldier(world, i - 3, j + 11, k, i, j, k);
		spawnGondorSoldier(world, i + 3, j + 11, k, i, j, k);
		spawnGondorSoldier(world, i + 11, j + 11, k, i, j, k);
		spawnGondorSoldier(world, i + 23, j + 11, k, i, j, k);
		
		LOTREntityGondorianCaptain captain = new LOTREntityGondorianCaptain(world);
		captain.setLocationAndAngles(i + 0.5D, j + 15, k + 0.5D, 0F, 0F);
		captain.onSpawnWithEgg(null);
		world.spawnEntityInWorld(captain);
		captain.setHomeArea(i, j + 15, k, 8);
		
		return true;
	}
	
	private void placeRandomBrick(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(10) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 2 + random.nextInt(2));
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.brick, 1);
		}
	}
	
	private void placeRandomSlab(World world, Random random, int i, int j, int k, boolean inverted)
	{
		if (random.nextInt(10) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle, 4 + random.nextInt(2) + (inverted ? 8 : 0));
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.slabSingle, 3 + (inverted ? 8 : 0));
		}
	}
	
	private void placeRandomStairs(World world, Random random, int i, int j, int k, int metadata)
	{
		if (random.nextInt(10) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, random.nextBoolean() ? LOTRMod.stairsGondorBrickMossy : LOTRMod.stairsGondorBrickCracked, metadata);
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.stairsGondorBrick, metadata);
		}
	}
	
	private void placeRandomWall(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(10) == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.wall, 4 + random.nextInt(2));
		}
		else
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.wall, 3);
		}
	}
	
	private void placeBarredWindowOnX(World world, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i - 1, j, k, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i, j, k, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i + 1, j, k, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i, j + 1, k, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k, Blocks.iron_bars, 0);
	}
	
	private void placeBarredWindowOnZ(World world, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i, j, k - 1, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i, j, k, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i, j, k + 1, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 1, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i, j + 1, k, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 1, Blocks.iron_bars, 0);
	}
	
	private void spawnGondorSoldier(World world, int i, int j, int k, int i1, int j1, int k1)
	{
		LOTREntityGondorSoldier soldier = world.rand.nextInt(3) == 0 ? new LOTREntityGondorArcher(world) : new LOTREntityGondorSoldier(world);
		soldier.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0F, 0F);
		soldier.spawnRidingHorse = false;
		soldier.onSpawnWithEgg(null);
		soldier.isNPCPersistent = true;
		world.spawnEntityInWorld(soldier);
		soldier.setHomeArea(i1, j1, k1, 32);
	}
}
