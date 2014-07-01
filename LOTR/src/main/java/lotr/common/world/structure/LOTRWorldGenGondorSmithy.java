package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityGondorBlacksmith;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class LOTRWorldGenGondorSmithy extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenGondorSmithy(boolean flag)
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
		}
		
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
		return false;
	}
	
	private boolean generateFacingSouth(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			for (int k1 = k + 1; k1 <= k + 11; k1++)
			{
				for (int i1 = i - 4; i1 <= i + 4; i1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					if (world.getBlock(i1, j1, k1) != Blocks.grass)
					{
						return false;
					}
				}
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1D, j + 1D, k + 1D).offset(0D, 3D, 6D).expand(5D, 3D, 6D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int k1 = k + 1; k1 <= k + 11; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 5; j1++)
			{
				for (int i1 = i - 4; i1 <= i + 4; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}		
		
		for (int k1 = k + 1; k1 <= k + 11; k1++)
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.slabDouble, 2);
				
				for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 1);
				}
			}
		}
		
		for (int k1 = k + 3; k1 <= k + 7; k1 += 4)
		{
			for (int i1 = i - 2; i1 <= i + 1; i1 += 3)
			{
				for (int k2 = k1; k2 <= k1 + 2; k2++)
				{
					for (int i2 = i1; i2 <= i1 + 1; i2++)
					{
						setBlockAndNotifyAdequately(world, i2, j, k2, LOTRMod.rock, 1);
					}
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int k1 = k + 1; k1 <= k + 11; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 4, j1, k1, LOTRMod.brick, 1);
				setBlockAndNotifyAdequately(world, i + 4, j1, k1, LOTRMod.brick, 1);
			}
			
			for (int i1 = i - 3; i1 <= i + 3; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 1, LOTRMod.brick, 1);
				setBlockAndNotifyAdequately(world, i1, j1, k + 11, LOTRMod.brick, 1);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 1, LOTRMod.rock, 1);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 1, k + 1, Blocks.fence_gate, 0);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 1, Blocks.air, 0);
		
		for (int k1 = k + 2; k1 <= k + 10; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 4, k1, LOTRMod.wall, 3);
			setBlockAndNotifyAdequately(world, i + 4, j + 4, k1, LOTRMod.wall, 3);
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 1, LOTRMod.wall, 3);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 11, LOTRMod.wall, 3);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 4, k + 1, LOTRMod.rock, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 4, k + 1, LOTRMod.rock, 1);
		setBlockAndNotifyAdequately(world, i - 4, j + 4, k + 11, LOTRMod.rock, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 4, k + 11, LOTRMod.rock, 1);
		
		for (int k1 = k + 2; k1 <= k + 10; k1++)
		{
			for (int i1 = i - 3; i1 <= i + 3; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 5, k1, LOTRMod.brick, 1);
			}
		}
		
		for (int k1 = k + 2; k1 <= k + 10; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 5, k1, LOTRMod.stairsGondorBrick, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + 5, k1, LOTRMod.stairsGondorBrick, 1);
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 1, LOTRMod.stairsGondorBrick, 2);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 11, LOTRMod.stairsGondorBrick, 3);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 2, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 3, LOTRMod.gondorianTable, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 4, LOTRMod.brick, 1);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 4, LOTRMod.wall, 3);
		
		for (int k1 = k + 2; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 3, k1, LOTRMod.stairsGondorBrick, 1);
		}
		
		for (int k1 = k + 2; k1 <= k + 6; k1 += 2)
		{
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, Blocks.anvil, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 8, Blocks.chest, 4);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 9, Blocks.chest, 4);
		
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 2, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k + 2, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 6, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 6, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 8, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 8, LOTRMod.slabDouble, 2);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 9, Blocks.lava, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 9, Blocks.lava, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 10, Blocks.lava, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 10, Blocks.lava, 0);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k + 8, LOTRMod.stairsGondorBrick, 2);
		setBlockAndNotifyAdequately(world, i - 2, j + 3, k + 8, LOTRMod.stairsGondorBrick, 2);
		setBlockAndNotifyAdequately(world, i - 1, j + 3, k + 8, LOTRMod.stairsGondorBrick, 2);
		setBlockAndNotifyAdequately(world, i - 1, j + 3, k + 9, LOTRMod.stairsGondorBrick, 1);
		setBlockAndNotifyAdequately(world, i - 1, j + 3, k + 10, LOTRMod.stairsGondorBrick, 1);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 8, Blocks.furnace, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 8, Blocks.furnace, 0);
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 9, Blocks.furnace, 0);
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 10, Blocks.furnace, 0);
		
		world.setBlockMetadataWithNotify(i - 3, j + 1, k + 8, 2, 3);
		world.setBlockMetadataWithNotify(i - 2, j + 1, k + 8, 2, 3);
		world.setBlockMetadataWithNotify(i - 1, j + 1, k + 9, 5, 3);
		world.setBlockMetadataWithNotify(i - 1, j + 1, k + 10, 5, 3);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 8, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 8, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 9, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 10, Blocks.iron_bars, 0);
		
		for (int i1 = -1; i1 <= 1; i1++)
		{
			for (int k1 = -1; k1 <= 1; k1++)
			{
				if (i1 != 0 || k1 != 0)
				{
					setBlockAndNotifyAdequately(world, i - 3 + i1, j + 4, k + 10 + k1, LOTRMod.slabDouble, 2);
					setBlockAndNotifyAdequately(world, i - 3 + i1, j + 5, k + 10 + k1, LOTRMod.slabDouble, 2);
					setBlockAndNotifyAdequately(world, i - 3 + i1, j + 6, k + 10 + k1, LOTRMod.slabSingle, 2);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 5, k + 10, Blocks.air, 0);
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 11, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i - 4, j + 3, k + 11, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k + 11, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i - 4, j + 3, k + 10, LOTRMod.slabDouble, 2);
		
		spawnBlacksmith(world, i, j + 1, k + 6);
		
		return true;
	}
	
	private boolean generateFacingWest(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			for (int i1 = i - 1; i1 >= i - 11; i1--)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					if (world.getBlock(i1, j1, k1) != Blocks.grass)
					{
						return false;
					}
				}
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1D, j + 1D, k + 1D).offset(-6D, 3D, 0D).expand(6D, 3D, 5D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int i1 = i - 1; i1 >= i - 11; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 5; j1++)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}		
		
		for (int i1 = i - 1; i1 >= i - 11; i1--)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.slabDouble, 2);
				
				for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 1);
				}
			}
		}
		
		for (int i1 = i - 3; i1 >= i - 7; i1 -= 4)
		{
			for (int k1 = k - 2; k1 <= k + 1; k1 += 3)
			{
				for (int i2 = i1; i2 >= i1 - 2; i2--)
				{
					for (int k2 = k1; k2 <= k1 + 1; k2++)
					{
						setBlockAndNotifyAdequately(world, i2, j, k2, LOTRMod.rock, 1);
					}
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int i1 = i - 1; i1 >= i - 11; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 4, LOTRMod.brick, 1);
				setBlockAndNotifyAdequately(world, i1, j1, k + 4, LOTRMod.brick, 1);
			}
			
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 1, j1, k1, LOTRMod.brick, 1);
				setBlockAndNotifyAdequately(world, i - 11, j1, k1, LOTRMod.brick, 1);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 1, j1, k1, LOTRMod.rock, 1);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k, Blocks.fence_gate, 1);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k, Blocks.air, 0);
		
		for (int i1 = i - 2; i1 >= i - 10; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 4, LOTRMod.wall, 3);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 4, LOTRMod.wall, 3);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 1, j + 4, k1, LOTRMod.wall, 3);
			setBlockAndNotifyAdequately(world, i - 11, j + 4, k1, LOTRMod.wall, 3);
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j + 4, k - 4, LOTRMod.rock, 1);
		setBlockAndNotifyAdequately(world, i - 1, j + 4, k + 4, LOTRMod.rock, 1);
		setBlockAndNotifyAdequately(world, i - 11, j + 4, k - 4, LOTRMod.rock, 1);
		setBlockAndNotifyAdequately(world, i - 11, j + 4, k + 4, LOTRMod.rock, 1);
		
		for (int i1 = i - 2; i1 >= i - 10; i1--)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 5, k1, LOTRMod.brick, 1);
			}
		}
		
		for (int i1 = i - 2; i1 >= i - 10; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 4, LOTRMod.stairsGondorBrick, 2);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 4, LOTRMod.stairsGondorBrick, 3);
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 1, j + 5, k1, LOTRMod.stairsGondorBrick, 1);
			setBlockAndNotifyAdequately(world, i - 11, j + 5, k1, LOTRMod.stairsGondorBrick, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 3, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 3, LOTRMod.gondorianTable, 0);
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 3, LOTRMod.brick, 1);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 3, LOTRMod.wall, 3);
		
		for (int i1 = i - 2; i1 >= i - 4; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 3, LOTRMod.stairsGondorBrick, 3);
		}
		
		for (int i1 = i - 2; i1 >= i - 6; i1 -= 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, Blocks.anvil, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k + 3, Blocks.chest, 2);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k + 3, Blocks.chest, 2);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 1, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 1, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 3, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k + 3, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 1, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i - 8, j + 2, k - 1, LOTRMod.slabDouble, 2);
		
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 3, Blocks.lava, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 2, Blocks.lava, 0);
		setBlockAndNotifyAdequately(world, i - 10, j + 1, k - 3, Blocks.lava, 0);
		setBlockAndNotifyAdequately(world, i - 10, j + 1, k - 2, Blocks.lava, 0);
		
		setBlockAndNotifyAdequately(world, i - 8, j + 3, k - 3, LOTRMod.stairsGondorBrick, 1);
		setBlockAndNotifyAdequately(world, i - 8, j + 3, k - 2, LOTRMod.stairsGondorBrick, 1);
		setBlockAndNotifyAdequately(world, i - 8, j + 3, k - 1, LOTRMod.stairsGondorBrick, 1);
		setBlockAndNotifyAdequately(world, i - 9, j + 3, k - 1, LOTRMod.stairsGondorBrick, 3);
		setBlockAndNotifyAdequately(world, i - 10, j + 3, k - 1, LOTRMod.stairsGondorBrick, 3);
		
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 3, Blocks.furnace, 0);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 2, Blocks.furnace, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 1, Blocks.furnace, 0);
		setBlockAndNotifyAdequately(world, i - 10, j + 1, k - 1, Blocks.furnace, 0);
		
		world.setBlockMetadataWithNotify(i - 8, j + 1, k - 3, 5, 3);
		world.setBlockMetadataWithNotify(i - 8, j + 1, k - 2, 5, 3);
		world.setBlockMetadataWithNotify(i - 9, j + 1, k - 1, 3, 3);
		world.setBlockMetadataWithNotify(i - 10, j + 1, k - 1, 3, 3);
		
		setBlockAndNotifyAdequately(world, i - 8, j + 2, k - 3, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i - 8, j + 2, k - 2, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 1, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i - 10, j + 2, k - 1, Blocks.iron_bars, 0);
		
		for (int i1 = -1; i1 <= 1; i1++)
		{
			for (int k1 = -1; k1 <= 1; k1++)
			{
				if (i1 != 0 || k1 != 0)
				{
					setBlockAndNotifyAdequately(world, i - 10 + i1, j + 4, k - 3 + k1, LOTRMod.slabDouble, 2);
					setBlockAndNotifyAdequately(world, i - 10 + i1, j + 5, k - 3 + k1, LOTRMod.slabDouble, 2);
					setBlockAndNotifyAdequately(world, i - 10 + i1, j + 6, k - 3 + k1, LOTRMod.slabSingle, 2);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 10, j + 5, k - 3, Blocks.air, 0);
		
		setBlockAndNotifyAdequately(world, i - 11, j + 2, k - 4, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i - 11, j + 3, k - 4, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i - 11, j + 3, k - 3, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i - 10, j + 3, k - 4, LOTRMod.slabDouble, 2);
		
		spawnBlacksmith(world, i - 6, j + 1, k);
		
		return true;
	}
	
	private boolean generateFacingNorth(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			for (int k1 = k - 1; k1 >= k - 11; k1--)
			{
				for (int i1 = i - 4; i1 <= i + 4; i1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					if (world.getBlock(i1, j1, k1) != Blocks.grass)
					{
						return false;
					}
				}
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1D, j + 1D, k + 1D).offset(0D, 3D, -6D).expand(5D, 3D, 6D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int k1 = k - 1; k1 >= k - 11; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 5; j1++)
			{
				for (int i1 = i - 4; i1 <= i + 4; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}		
		
		for (int k1 = k - 1; k1 >= k - 11; k1--)
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.slabDouble, 2);
				
				for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 1);
				}
			}
		}
		
		for (int k1 = k - 3; k1 >= k - 7; k1 -= 4)
		{
			for (int i1 = i - 2; i1 <= i + 1; i1 += 3)
			{
				for (int k2 = k1; k2 >= k1 - 2; k2--)
				{
					for (int i2 = i1; i2 <= i1 + 1; i2++)
					{
						setBlockAndNotifyAdequately(world, i2, j, k2, LOTRMod.rock, 1);
					}
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int k1 = k - 1; k1 >= k - 11; k1--)
			{
				setBlockAndNotifyAdequately(world, i - 4, j1, k1, LOTRMod.brick, 1);
				setBlockAndNotifyAdequately(world, i + 4, j1, k1, LOTRMod.brick, 1);
			}
			
			for (int i1 = i - 3; i1 <= i + 3; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 1, LOTRMod.brick, 1);
				setBlockAndNotifyAdequately(world, i1, j1, k - 11, LOTRMod.brick, 1);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 1, LOTRMod.rock, 1);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 1, k - 1, Blocks.fence_gate, 2);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 1, Blocks.air, 0);
		
		for (int k1 = k - 2; k1 >= k - 10; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 4, k1, LOTRMod.wall, 3);
			setBlockAndNotifyAdequately(world, i + 4, j + 4, k1, LOTRMod.wall, 3);
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 1, LOTRMod.wall, 3);
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 11, LOTRMod.wall, 3);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 4, k - 1, LOTRMod.rock, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 4, k - 1, LOTRMod.rock, 1);
		setBlockAndNotifyAdequately(world, i - 4, j + 4, k - 11, LOTRMod.rock, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 4, k - 11, LOTRMod.rock, 1);
		
		for (int k1 = k - 2; k1 >= k - 10; k1--)
		{
			for (int i1 = i - 3; i1 <= i + 3; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 5, k1, LOTRMod.brick, 1);
			}
		}
		
		for (int k1 = k - 2; k1 >= k - 10; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 5, k1, LOTRMod.stairsGondorBrick, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + 5, k1, LOTRMod.stairsGondorBrick, 1);
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 1, LOTRMod.stairsGondorBrick, 3);
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 11, LOTRMod.stairsGondorBrick, 2);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 2, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 3, LOTRMod.gondorianTable, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 4, LOTRMod.brick, 1);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 4, LOTRMod.wall, 3);
		
		for (int k1 = k - 2; k1 >= k - 4; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 3, k1, LOTRMod.stairsGondorBrick, 1);
		}
		
		for (int k1 = k - 2; k1 >= k - 6; k1 -= 2)
		{
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, Blocks.anvil, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 8, Blocks.chest, 4);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 9, Blocks.chest, 4);
		
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k - 2, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k - 2, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 6, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 6, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 8, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k - 8, LOTRMod.slabDouble, 2);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 9, Blocks.lava, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 9, Blocks.lava, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 10, Blocks.lava, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 10, Blocks.lava, 0);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k - 8, LOTRMod.stairsGondorBrick, 3);
		setBlockAndNotifyAdequately(world, i - 2, j + 3, k - 8, LOTRMod.stairsGondorBrick, 3);
		setBlockAndNotifyAdequately(world, i - 1, j + 3, k - 8, LOTRMod.stairsGondorBrick, 3);
		setBlockAndNotifyAdequately(world, i - 1, j + 3, k - 9, LOTRMod.stairsGondorBrick, 1);
		setBlockAndNotifyAdequately(world, i - 1, j + 3, k - 10, LOTRMod.stairsGondorBrick, 1);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 8, Blocks.furnace, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 8, Blocks.furnace, 0);
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 9, Blocks.furnace, 0);
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 10, Blocks.furnace, 0);
		
		world.setBlockMetadataWithNotify(i - 3, j + 1, k - 8, 3, 3);
		world.setBlockMetadataWithNotify(i - 2, j + 1, k - 8, 3, 3);
		world.setBlockMetadataWithNotify(i - 1, j + 1, k - 9, 5, 3);
		world.setBlockMetadataWithNotify(i - 1, j + 1, k - 10, 5, 3);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 8, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 8, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k - 9, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k - 10, Blocks.iron_bars, 0);
		
		for (int i1 = -1; i1 <= 1; i1++)
		{
			for (int k1 = -1; k1 <= 1; k1++)
			{
				if (i1 != 0 || k1 != 0)
				{
					setBlockAndNotifyAdequately(world, i - 3 + i1, j + 4, k - 10 + k1, LOTRMod.slabDouble, 2);
					setBlockAndNotifyAdequately(world, i - 3 + i1, j + 5, k - 10 + k1, LOTRMod.slabDouble, 2);
					setBlockAndNotifyAdequately(world, i - 3 + i1, j + 6, k - 10 + k1, LOTRMod.slabSingle, 2);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 5, k - 10, Blocks.air, 0);
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 11, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i - 4, j + 3, k - 11, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k - 11, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i - 4, j + 3, k - 10, LOTRMod.slabDouble, 2);
		
		spawnBlacksmith(world, i, j + 1, k - 6);
		
		return true;
	}
	
	private boolean generateFacingEast(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			for (int i1 = i + 1; i1 <= i + 11; i1++)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					if (world.getBlock(i1, j1, k1) != Blocks.grass)
					{
						return false;
					}
				}
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1D, j + 1D, k + 1D).offset(6D, 3D, 0D).expand(6D, 3D, 5D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int i1 = i + 1; i1 <= i + 11; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 5; j1++)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}		
		
		for (int i1 = i + 1; i1 <= i + 11; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.slabDouble, 2);
				
				for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 1);
				}
			}
		}
		
		for (int i1 = i + 3; i1 <= i + 7; i1 += 4)
		{
			for (int k1 = k - 2; k1 <= k + 1; k1 += 3)
			{
				for (int i2 = i1; i2 <= i1 + 2; i2++)
				{
					for (int k2 = k1; k2 <= k1 + 1; k2++)
					{
						setBlockAndNotifyAdequately(world, i2, j, k2, LOTRMod.rock, 1);
					}
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			for (int i1 = i + 1; i1 <= i + 11; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 4, LOTRMod.brick, 1);
				setBlockAndNotifyAdequately(world, i1, j1, k + 4, LOTRMod.brick, 1);
			}
			
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 1, j1, k1, LOTRMod.brick, 1);
				setBlockAndNotifyAdequately(world, i + 11, j1, k1, LOTRMod.brick, 1);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 1, j1, k1, LOTRMod.rock, 1);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k, Blocks.fence_gate, 3);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k, Blocks.air, 0);
		
		for (int i1 = i + 2; i1 <= i + 10; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 4, LOTRMod.wall, 3);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 4, LOTRMod.wall, 3);
		}
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 1, j + 4, k1, LOTRMod.wall, 3);
			setBlockAndNotifyAdequately(world, i + 11, j + 4, k1, LOTRMod.wall, 3);
		}
		
		setBlockAndNotifyAdequately(world, i + 1, j + 4, k - 4, LOTRMod.rock, 1);
		setBlockAndNotifyAdequately(world, i + 1, j + 4, k + 4, LOTRMod.rock, 1);
		setBlockAndNotifyAdequately(world, i + 11, j + 4, k - 4, LOTRMod.rock, 1);
		setBlockAndNotifyAdequately(world, i + 11, j + 4, k + 4, LOTRMod.rock, 1);
		
		for (int i1 = i + 2; i1 <= i + 10; i1++)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 5, k1, LOTRMod.brick, 1);
			}
		}
		
		for (int i1 = i + 2; i1 <= i + 10; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 4, LOTRMod.stairsGondorBrick, 2);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 4, LOTRMod.stairsGondorBrick, 3);
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 1, j + 5, k1, LOTRMod.stairsGondorBrick, 0);
			setBlockAndNotifyAdequately(world, i + 11, j + 5, k1, LOTRMod.stairsGondorBrick, 1);
		}
		
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 3, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 3, LOTRMod.gondorianTable, 0);
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 3, LOTRMod.brick, 1);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 3, LOTRMod.wall, 3);
		
		for (int i1 = i + 2; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 3, LOTRMod.stairsGondorBrick, 3);
		}
		
		for (int i1 = i + 2; i1 <= i + 6; i1 += 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, Blocks.anvil, 1);
		}
		
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k + 3, Blocks.chest, 2);
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k + 3, Blocks.chest, 2);
		
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 1, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k + 1, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 6, j + 2, k - 3, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 6, j + 2, k + 3, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k - 1, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k - 1, LOTRMod.slabDouble, 2);
		
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k - 3, Blocks.lava, 0);
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k - 2, Blocks.lava, 0);
		setBlockAndNotifyAdequately(world, i + 10, j + 1, k - 3, Blocks.lava, 0);
		setBlockAndNotifyAdequately(world, i + 10, j + 1, k - 2, Blocks.lava, 0);
		
		setBlockAndNotifyAdequately(world, i + 8, j + 3, k - 3, LOTRMod.stairsGondorBrick, 0);
		setBlockAndNotifyAdequately(world, i + 8, j + 3, k - 2, LOTRMod.stairsGondorBrick, 0);
		setBlockAndNotifyAdequately(world, i + 8, j + 3, k - 1, LOTRMod.stairsGondorBrick, 0);
		setBlockAndNotifyAdequately(world, i + 9, j + 3, k - 1, LOTRMod.stairsGondorBrick, 3);
		setBlockAndNotifyAdequately(world, i + 10, j + 3, k - 1, LOTRMod.stairsGondorBrick, 3);
		
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k - 3, Blocks.furnace, 0);
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k - 2, Blocks.furnace, 0);
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k - 1, Blocks.furnace, 0);
		setBlockAndNotifyAdequately(world, i + 10, j + 1, k - 1, Blocks.furnace, 0);
		
		world.setBlockMetadataWithNotify(i + 8, j + 1, k - 3, 4, 3);
		world.setBlockMetadataWithNotify(i + 8, j + 1, k - 2, 4, 3);
		world.setBlockMetadataWithNotify(i + 9, j + 1, k - 1, 3, 3);
		world.setBlockMetadataWithNotify(i + 10, j + 1, k - 1, 3, 3);
		
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k - 3, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k - 2, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k - 1, Blocks.iron_bars, 0);
		setBlockAndNotifyAdequately(world, i + 10, j + 2, k - 1, Blocks.iron_bars, 0);
		
		for (int i1 = -1; i1 <= 1; i1++)
		{
			for (int k1 = -1; k1 <= 1; k1++)
			{
				if (i1 != 0 || k1 != 0)
				{
					setBlockAndNotifyAdequately(world, i + 10 + i1, j + 4, k - 3 + k1, LOTRMod.slabDouble, 2);
					setBlockAndNotifyAdequately(world, i + 10 + i1, j + 5, k - 3 + k1, LOTRMod.slabDouble, 2);
					setBlockAndNotifyAdequately(world, i + 10 + i1, j + 6, k - 3 + k1, LOTRMod.slabSingle, 2);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 10, j + 5, k - 3, Blocks.air, 0);
		
		setBlockAndNotifyAdequately(world, i + 11, j + 2, k - 4, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i + 11, j + 3, k - 4, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i + 11, j + 3, k - 3, LOTRMod.slabDouble, 2);
		setBlockAndNotifyAdequately(world, i + 10, j + 3, k - 4, LOTRMod.slabDouble, 2);
		
		spawnBlacksmith(world, i + 6, j + 1, k);
		
		return true;
	}
	
	private void spawnBlacksmith(World world, int i, int j, int k)
	{
		LOTREntityGondorBlacksmith blacksmith = new LOTREntityGondorBlacksmith(world);
		blacksmith.setLocationAndAngles(i + 0.5D, j, k + 0.5D, 0F, 0F);
		blacksmith.onSpawnWithEgg(null);
		blacksmith.setHomeArea(i, j, k, 4);
		world.spawnEntityInWorld(blacksmith);
	}
}
