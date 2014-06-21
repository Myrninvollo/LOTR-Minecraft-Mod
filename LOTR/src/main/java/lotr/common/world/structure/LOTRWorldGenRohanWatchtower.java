package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityRohirrim;
import lotr.common.entity.npc.LOTREntityRohirrimArcher;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenRohanWatchtower extends LOTRWorldGenStructureBase
{
	private Block plankBlock = Blocks.planks;
	private int plankMeta = 0;
	
	private Block woodBlock = Blocks.log;
	private int woodMeta = 0;
	
	private Block stairBlock = Blocks.oak_stairs;
	
	public LOTRWorldGenRohanWatchtower(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass || world.getBiomeGenForCoords(i, k) != LOTRBiome.rohan)
			{
				return false;
			}
		}
		
		int height = 5 + random.nextInt(4);
		j += height;
		
		if (restrictions)
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				for (int j1 = j - 3; j1 <= j + 4; j1++)
				{
					for (int k1 = k - 4; k1 <= k + 4; k1++)
					{
						if (!world.isAirBlock(i1, j1, k1))
						{
							return false;
						}
					}
				}
			}
		}
		
		generateBasicStructure(world, random, i, j, k);
		
		int soldiers = 1 + random.nextInt(3);
		for (int l = 0; l < soldiers; l++)
		{
			LOTREntityRohirrim rohirrim = random.nextInt(3) == 0 ? new LOTREntityRohirrimArcher(world) : new LOTREntityRohirrim(world);
			rohirrim.isNPCPersistent = true;
			rohirrim.spawnRidingHorse = false;
			rohirrim.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, world.rand.nextFloat(), 0F);
			rohirrim.onSpawnWithEgg(null);
			world.spawnEntityInWorld(rohirrim);
			rohirrim.setHomeArea(i, j, k, 4);
		}
		
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
		
		return true;
	}
	
	private void generateBasicStructure(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j + 3; !LOTRMod.isOpaque(world, i - 3, j1, k - 3) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k - 3, plankBlock, plankMeta);
		}
		for (int j1 = j + 3; !LOTRMod.isOpaque(world, i - 3, j1, k + 3) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k + 3, plankBlock, plankMeta);
		}
		for (int j1 = j + 3; !LOTRMod.isOpaque(world, i + 3, j1, k - 3) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i + 3, j1, k - 3, plankBlock, plankMeta);
		}
		for (int j1 = j + 3; !LOTRMod.isOpaque(world, i + 3, j1, k + 3) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i + 3, j1, k + 3, plankBlock, plankMeta);
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, plankBlock, plankMeta);
			}
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, plankBlock, plankMeta);
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 3, woodBlock, woodMeta | 4);
			setBlockAndNotifyAdequately(world, i1, j, k + 3, woodBlock, woodMeta | 4);
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 3, woodBlock, woodMeta | 4);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 3, woodBlock, woodMeta | 4);
			
			setBlockAndNotifyAdequately(world, i1, j, k - 4, stairBlock, 6);
			setBlockAndNotifyAdequately(world, i1, j, k + 4, stairBlock, 7);
			
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 4, Blocks.fence, 0);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 4, Blocks.fence, 0);
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 3, Blocks.fence, 0);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 3, Blocks.fence, 0);
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j, k1, woodBlock, woodMeta | 8);
			setBlockAndNotifyAdequately(world, i + 3, j, k1, woodBlock, woodMeta | 8);
			setBlockAndNotifyAdequately(world, i - 3, j + 4, k1, woodBlock, woodMeta | 8);
			setBlockAndNotifyAdequately(world, i + 3, j + 4, k1, woodBlock, woodMeta | 8);
			
			setBlockAndNotifyAdequately(world, i - 4, j, k1, stairBlock, 4);
			setBlockAndNotifyAdequately(world, i + 4, j, k1, stairBlock, 5);
			
			setBlockAndNotifyAdequately(world, i - 4, j + 1, k1, Blocks.fence, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + 1, k1, Blocks.fence, 0);
			
			setBlockAndNotifyAdequately(world, i - 3, j + 3, k1, Blocks.fence, 0);
			setBlockAndNotifyAdequately(world, i + 3, j + 3, k1, Blocks.fence, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 2, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 2, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 2, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 2, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 3, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 3, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 3, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k + 3, Blocks.fence, 0);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 2, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 2, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 2, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 2, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 3, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 3, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 3, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k + 3, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i - 1, j + 4, k, woodBlock, woodMeta | 8);
		setBlockAndNotifyAdequately(world, i + 1, j + 4, k, woodBlock, woodMeta | 8);
		setBlockAndNotifyAdequately(world, i, j + 4, k - 1, woodBlock, woodMeta | 4);
		setBlockAndNotifyAdequately(world, i, j + 4, k + 1, woodBlock, woodMeta | 4);
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 4, stairBlock, 2);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 4, stairBlock, 3);
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 4, k1, stairBlock, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + 4, k1, stairBlock, 1);
		}
	}
	
	private boolean generateFacingSouth(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i, j1, k + 3, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i, j1, k + 2, Blocks.ladder, 2);
		}
		
		for (int j1 = j - 1; !LOTRMod.isOpaque(world, i, j1, k + 3) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i, j1, k + 3, plankBlock, plankMeta);
		}
		
		for (int j1 = j - 1; !LOTRMod.isOpaque(world, i, j1, k + 2) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i, j1, k + 2, Blocks.ladder, 2);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 2, Blocks.chest, 3);
		LOTRChestContents.fillChest(world, random, i - 2, j + 1, k + 2, LOTRChestContents.ROHAN_WATCHTOWER);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k + 2, LOTRMod.rohirricTable, 0);
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			int k2 = Math.abs(k - k1);
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i - 3, j1, k1) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((k2 == 2 && j2 % 4 == 1) || (k2 == 1 && j2 % 2 == 0) || (k2 == 0 && j2 % 4 == 3))
				{
					setBlockAndNotifyAdequately(world, i - 3, j1, k1, woodBlock, woodMeta);
					if (k2 == 0)
					{
						setBlockAndNotifyAdequately(world, i - 2, j1, k1, Blocks.torch, 1);
					}
				}
			}
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i + 3, j1, k1) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((k2 == 2 && j2 % 4 == 1) || (k2 == 1 && j2 % 2 == 0) || (k2 == 0 && j2 % 4 == 3))
				{
					setBlockAndNotifyAdequately(world, i + 3, j1, k1, woodBlock, woodMeta);
					if (k2 == 0)
					{
						setBlockAndNotifyAdequately(world, i + 2, j1, k1, Blocks.torch, 2);
					}
				}
			}
		}
		
		int j1;
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i + 2, j1, k + 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i + 2, j1 + 1, k + 2, Blocks.chest, 4);
		LOTRChestContents.fillChest(world, random, i + 2, j1 + 1, k + 2, LOTRChestContents.ROHAN_WATCHTOWER);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i + 2, j1, k) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i + 2, j1 + 1, k, plankBlock, plankMeta);
		placeBarrel(world, random, i + 2, j1 + 2, k, 4, LOTRMod.mugMead);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i - 2, j1, k + 1) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i - 2, j1 + 1, k + 1, Blocks.hay_block, 0);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i - 2, j1, k + 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i - 2, j1 + 1, k + 2, Blocks.hay_block, 0);
		setBlockAndNotifyAdequately(world, i - 2, j1 + 2, k + 2, Blocks.hay_block, 0);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i - 1, j1, k + 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i - 1, j1 + 1, k + 2, Blocks.hay_block, 0);
		
		return true;
	}
	
	private boolean generateFacingWest(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i - 2, j1, k, Blocks.ladder, 5);
		}
		
		for (int j1 = j - 1; !LOTRMod.isOpaque(world, i - 3, j1, k) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i - 3, j1, k, plankBlock, plankMeta);
		}
		
		for (int j1 = j - 1; !LOTRMod.isOpaque(world, i - 2, j1, k) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i - 2, j1, k, Blocks.ladder, 5);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 2, Blocks.chest, 4);
		LOTRChestContents.fillChest(world, random, i - 2, j + 1, k - 2, LOTRChestContents.ROHAN_WATCHTOWER);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 2, LOTRMod.rohirricTable, 0);
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			int i2 = Math.abs(i - i1);
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k - 3) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((i2 == 2 && j2 % 4 == 1) || (i2 == 1 && j2 % 2 == 0) || (i2 == 0 && j2 % 4 == 3))
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 3, woodBlock, woodMeta);
					if (i2 == 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k - 2, Blocks.torch, 3);
					}
				}
			}
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k + 3) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((i2 == 2 && j2 % 4 == 1) || (i2 == 1 && j2 % 2 == 0) || (i2 == 0 && j2 % 4 == 3))
				{
					setBlockAndNotifyAdequately(world, i1, j1, k + 3, woodBlock, woodMeta);
					if (i2 == 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k + 2, Blocks.torch, 4);
					}
				}
			}
		}
		
		int j1;
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i - 2, j1, k + 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i - 2, j1 + 1, k + 2, Blocks.chest, 2);
		LOTRChestContents.fillChest(world, random, i - 2, j1 + 1, k + 2, LOTRChestContents.ROHAN_WATCHTOWER);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i, j1, k + 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i, j1 + 1, k + 2, plankBlock, plankMeta);
		placeBarrel(world, random, i, j1 + 2, k + 2, 2, LOTRMod.mugMead);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i - 1, j1, k - 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i - 1, j1 + 1, k - 2, Blocks.hay_block, 1);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i - 2, j1, k - 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i - 2, j1 + 1, k - 2, Blocks.hay_block, 1);
		setBlockAndNotifyAdequately(world, i - 2, j1 + 2, k - 2, Blocks.hay_block, 1);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i - 2, j1, k - 1) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i - 2, j1 + 1, k - 1, Blocks.hay_block, 1);
		
		return true;
	}
	
	private boolean generateFacingNorth(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i, j1, k - 3, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i, j1, k - 2, Blocks.ladder, 3);
		}
		
		for (int j1 = j - 1; !LOTRMod.isOpaque(world, i, j1, k - 3) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i, j1, k - 3, plankBlock, plankMeta);
		}
		
		for (int j1 = j - 1; !LOTRMod.isOpaque(world, i, j1, k - 2) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i, j1, k - 2, Blocks.ladder, 3);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 2, Blocks.chest, 3);
		LOTRChestContents.fillChest(world, random, i - 2, j + 1, k - 2, LOTRChestContents.ROHAN_WATCHTOWER);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 2, LOTRMod.rohirricTable, 0);
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			int k2 = Math.abs(k - k1);
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i - 3, j1, k1) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((k2 == 2 && j2 % 4 == 1) || (k2 == 1 && j2 % 2 == 0) || (k2 == 0 && j2 % 4 == 3))
				{
					setBlockAndNotifyAdequately(world, i - 3, j1, k1, woodBlock, woodMeta);
					if (k2 == 0)
					{
						setBlockAndNotifyAdequately(world, i - 2, j1, k1, Blocks.torch, 1);
					}
				}
			}
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i + 3, j1, k1) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((k2 == 2 && j2 % 4 == 1) || (k2 == 1 && j2 % 2 == 0) || (k2 == 0 && j2 % 4 == 3))
				{
					setBlockAndNotifyAdequately(world, i + 3, j1, k1, woodBlock, woodMeta);
					if (k2 == 0)
					{
						setBlockAndNotifyAdequately(world, i + 2, j1, k1, Blocks.torch, 2);
					}
				}
			}
		}
		
		int j1;
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i + 2, j1, k - 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i + 2, j1 + 1, k - 2, Blocks.chest, 4);
		LOTRChestContents.fillChest(world, random, i + 2, j1 + 1, k - 2, LOTRChestContents.ROHAN_WATCHTOWER);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i + 2, j1, k) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i + 2, j1 + 1, k, plankBlock, plankMeta);
		placeBarrel(world, random, i + 2, j1 + 2, k, 4, LOTRMod.mugMead);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i - 2, j1, k - 1) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i - 2, j1 + 1, k - 1, Blocks.hay_block, 2);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i - 2, j1, k - 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i - 2, j1 + 1, k - 2, Blocks.hay_block, 2);
		setBlockAndNotifyAdequately(world, i - 2, j1 + 2, k - 2, Blocks.hay_block, 2);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i - 1, j1, k - 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i - 1, j1 + 1, k - 2, Blocks.hay_block, 2);
		
		return true;
	}
	
	private boolean generateFacingEast(World world, Random random, int i, int j, int k)
	{
		for (int j1 = j; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 3, j1, k, plankBlock, plankMeta);
			setBlockAndNotifyAdequately(world, i + 2, j1, k, Blocks.ladder, 4);
		}
		
		for (int j1 = j - 1; !LOTRMod.isOpaque(world, i + 3, j1, k) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i + 3, j1, k, plankBlock, plankMeta);
		}
		
		for (int j1 = j - 1; !LOTRMod.isOpaque(world, i + 2, j1, k) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i + 2, j1, k, Blocks.ladder, 4);
		}
		
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 2, Blocks.chest, 4);
		LOTRChestContents.fillChest(world, random, i + 2, j + 1, k - 2, LOTRChestContents.ROHAN_WATCHTOWER);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k + 2, LOTRMod.rohirricTable, 0);
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			int i2 = Math.abs(i - i1);
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k - 3) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((i2 == 2 && j2 % 4 == 1) || (i2 == 1 && j2 % 2 == 0) || (i2 == 0 && j2 % 4 == 3))
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 3, woodBlock, woodMeta);
					if (i2 == 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k - 2, Blocks.torch, 3);
					}
				}
			}
			
			for (int j1 = j - 1; !LOTRMod.isOpaque(world, i1, j1, k + 3) && j1 >= 0; j1--)
			{
				int j2 = j - j1;
				if ((i2 == 2 && j2 % 4 == 1) || (i2 == 1 && j2 % 2 == 0) || (i2 == 0 && j2 % 4 == 3))
				{
					setBlockAndNotifyAdequately(world, i1, j1, k + 3, woodBlock, woodMeta);
					if (i2 == 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k + 2, Blocks.torch, 4);
					}
				}
			}
		}
		
		int j1;
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i + 2, j1, k + 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i + 2, j1 + 1, k + 2, Blocks.chest, 2);
		LOTRChestContents.fillChest(world, random, i + 2, j1 + 1, k + 2, LOTRChestContents.ROHAN_WATCHTOWER);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i, j1, k + 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i, j1 + 1, k + 2, plankBlock, plankMeta);
		placeBarrel(world, random, i, j1 + 2, k + 2, 2, LOTRMod.mugMead);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i + 1, j1, k - 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i + 1, j1 + 1, k - 2, Blocks.hay_block, 3);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i + 2, j1, k - 2) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i + 2, j1 + 1, k - 2, Blocks.hay_block, 3);
		setBlockAndNotifyAdequately(world, i + 2, j1 + 2, k - 2, Blocks.hay_block, 3);
		
		for (j1 = j - 1; !LOTRMod.isOpaque(world, i + 2, j1, k - 1) && j1 >= 0; j1--) {}
		setBlockAndNotifyAdequately(world, i + 2, j1 + 1, k - 1, Blocks.hay_block, 3);
		
		return true;
	}
}
