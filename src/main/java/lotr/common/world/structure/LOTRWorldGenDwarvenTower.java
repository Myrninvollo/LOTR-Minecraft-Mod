package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityDwarf;
import lotr.common.entity.npc.LOTREntityDwarfAxeThrower;
import lotr.common.entity.npc.LOTREntityDwarfCommander;
import lotr.common.entity.npc.LOTREntityDwarfWarrior;
import lotr.common.tileentity.LOTRTileEntityDwarvenForge;
import lotr.common.world.biome.LOTRBiomeGenIronHills;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class LOTRWorldGenDwarvenTower extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenDwarvenTower(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (!(world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenIronHills) || world.getBlock(i, j - 1, k) != Blocks.grass)
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
				k += 6;
				break;
			case 1:
				i -= 6;
				break;
			case 2:
				k -= 6;
				break;
			case 3:
				i += 6;
				break;
		}
		
		int sections = 5 + random.nextInt(3);
	
		if (restrictions)
		{
			for (int i1 = i - 6; i1 <= i + 6; i1++)
			{
				for (int k1 = k - 6; k1 <= k + 6; k1++)
				{
					int j1 = world.getHeightValue(i1, k1) - 1;
					if (world.getBlock(i1, j1, k1) != Blocks.grass)
					{
						return false;
					}
				}
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getAABBPool().getAABB(i - 6, j, k - 6, i + 6, j + ((sections + 1) * 5) + 5, k + 6));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		int lowestHeight = j + 1;
		
		for (int i1 = i - 6; i1 <= i + 6; i1++)
		{
			for (int k1 = k - 6; k1 <= k + 6; k1++)
			{
				int j1 = world.getHeightValue(i1, k1);
				if (j1 < lowestHeight)
				{
					lowestHeight = j1;
				}
			}
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			for (int k1 = k - 5; k1 <= k + 5; k1++)
			{
				for (int j1 = j; j1 >= lowestHeight; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 6);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.planks, 1);
			}
		}
		
		for (int l = 0; l <= sections; l++)
		{
			int l1 = (l * 5);
			
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				for (int j1 = j + l1 + 1; j1 <= j + l1 + 5; j1++)
				{
					for (int k1 = k - 4; k1 <= k + 4; k1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
					}
				}
			}
			
			for (int i1 = i - 5; i1 <= i + 5; i1++)
			{
				for (int j1 = j + l1 + 1; j1 <= j + l1 + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 5, LOTRMod.brick, 6);
					setBlockAndNotifyAdequately(world, i1, j1, k + 5, LOTRMod.brick, 6);
				}
			}
			
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				for (int j1 = j + l1 + 1; j1 <= j + l1 + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i - 5, j1, k1, LOTRMod.brick, 6);
					setBlockAndNotifyAdequately(world, i + 5, j1, k1, LOTRMod.brick, 6);
				}
			}
			
			setBlockAndNotifyAdequately(world, i - 4, j + l1 + 1, k - 4, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i - 4, j + l1 + 2, k - 4, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i - 4, j + l1 + 3, k - 4, Blocks.glowstone, 0);
			setBlockAndNotifyAdequately(world, i - 4, j + l1 + 4, k - 4, LOTRMod.pillar, 0);
			
			setBlockAndNotifyAdequately(world, i - 4, j + l1 + 1, k + 4, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i - 4, j + l1 + 2, k + 4, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i - 4, j + l1 + 3, k + 4, Blocks.glowstone, 0);
			setBlockAndNotifyAdequately(world, i - 4, j + l1 + 4, k + 4, LOTRMod.pillar, 0);
			
			setBlockAndNotifyAdequately(world, i + 4, j + l1 + 1, k - 4, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + l1 + 2, k - 4, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + l1 + 3, k - 4, Blocks.glowstone, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + l1 + 4, k - 4, LOTRMod.pillar, 0);
			
			setBlockAndNotifyAdequately(world, i + 4, j + l1 + 1, k + 4, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + l1 + 2, k + 4, LOTRMod.pillar, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + l1 + 3, k + 4, Blocks.glowstone, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + l1 + 4, k + 4, LOTRMod.pillar, 0);
		
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j + l1 + 5, k1, Blocks.planks, 1);
				}
			}
			
			switch (rotation)
			{
				case 0: case 2:
					for (int k1 = k - 2; k1 <= k + 2; k1++)
					{
						for (int j1 = j + l1 + 1; j1 <= j + l1 + 4; j1++)
						{
							if (Math.abs(k1 - k) < 2 && (j1 == j + l1 + 2 || j1 == j + l1 + 3))
							{
								setBlockAndNotifyAdequately(world, i - 5, j1, k1, Blocks.iron_bars, 0);
								setBlockAndNotifyAdequately(world, i + 5, j1, k1, Blocks.iron_bars, 0);
							}
							else
							{
								setBlockAndNotifyAdequately(world, i - 5, j1, k1, LOTRMod.pillar, 0);
								setBlockAndNotifyAdequately(world, i + 5, j1, k1, LOTRMod.pillar, 0);
							}
						}
					}
					break;
				case 1: case 3:
					for (int i1 = i - 2; i1 <= i + 2; i1++)
					{
						for (int j1 = j + l1 + 1; j1 <= j + l1 + 4; j1++)
						{
							if (Math.abs(i1 - i) < 2 && (j1 == j + l1 + 2 || j1 == j + l1 + 3))
							{
								setBlockAndNotifyAdequately(world, i1, j1, k - 5, Blocks.iron_bars, 0);
								setBlockAndNotifyAdequately(world, i1, j1, k + 5, Blocks.iron_bars, 0);
							}
							else
							{
								setBlockAndNotifyAdequately(world, i1, j1, k - 5, LOTRMod.pillar, 0);
								setBlockAndNotifyAdequately(world, i1, j1, k + 5, LOTRMod.pillar, 0);
							}
						}
					}
					break;
			}
			
			int stairDirection = rotation;
			if (l % 2 == 1)
			{
				stairDirection = (stairDirection + 2) & 3;
			}
			int randomFeature = random.nextInt(5);
			switch (stairDirection)
			{
				case 0:
					for (int k1 = k - 1; k1 <= k + 4; k1++)
					{
						for (int i1 = i + 1; i1 <= i + 2; i1++)
						{
							setBlockAndNotifyAdequately(world, i1, j + l1 + 5, k1, Blocks.air, 0);
							int k2 = k1 - (k - 1);
							for (int j1 = j + l1 + 1; j1 <= j + l1 + k2; j1++)
							{
								setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 6);
							}
							if (k2 < 5)
							{
								setBlockAndNotifyAdequately(world, i1, j + l1 + k2 + 1, k1, LOTRMod.stairsDwarvenBrick, 2);
							}
						}
					}
					placeRandomFeature(world, random, i - 2, j + l1 + 1, k + 4, randomFeature);
					placeRandomFeature(world, random, i - 1, j + l1 + 1, k + 4, randomFeature);
					setBlockAndNotifyAdequately(world, i, j + l1 + 1, k + 4, Blocks.planks, 1);
					setBlockAndNotifyAdequately(world, i - 3, j + l1 + 1, k + 4, Blocks.planks, 1);
					setBlockAndNotifyAdequately(world, i, j + l1 + 2, k + 4, Blocks.wooden_slab, 1);
					setBlockAndNotifyAdequately(world, i - 3, j + l1 + 2, k + 4, Blocks.wooden_slab, 1);
					break;
				case 1:
					for (int i1 = i - 4; i1 <= i + 1; i1++)
					{
						for (int k1 = k - 2; k1 <= k - 1; k1++)
						{
							setBlockAndNotifyAdequately(world, i1, j + l1 + 5, k1, Blocks.air, 0);
							int i2 = 5 - (i1 - (i - 4));
							for (int j1 = j + l1 + 1; j1 <= j + l1 + i2; j1++)
							{
								setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 6);
							}
							if (i2 < 5)
							{
								setBlockAndNotifyAdequately(world, i1, j + l1 + i2 + 1, k1, LOTRMod.stairsDwarvenBrick, 1);
							}
						}
					}
					placeRandomFeature(world, random, i - 4, j + l1 + 1, k + 1, randomFeature);
					placeRandomFeature(world, random, i - 4, j + l1 + 1, k + 2, randomFeature);
					setBlockAndNotifyAdequately(world, i - 4, j + l1 + 1, k, Blocks.planks, 1);
					setBlockAndNotifyAdequately(world, i - 4, j + l1 + 1, k + 3, Blocks.planks, 1);
					setBlockAndNotifyAdequately(world, i - 4, j + l1 + 2, k, Blocks.wooden_slab, 1);
					setBlockAndNotifyAdequately(world, i - 4, j + l1 + 2, k + 3, Blocks.wooden_slab, 1);
					break;
				case 2:
					for (int k1 = k - 4; k1 <= k + 1; k1++)
					{
						for (int i1 = i - 2; i1 <= i - 1; i1++)
						{
							setBlockAndNotifyAdequately(world, i1, j + l1 + 5, k1, Blocks.air, 0);
							int k2 = 5 - (k1 - (k - 4));
							for (int j1 = j + l1 + 1; j1 <= j + l1 + k2; j1++)
							{
								setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 6);
							}
							if (k2 < 5)
							{
								setBlockAndNotifyAdequately(world, i1, j + l1 + k2 + 1, k1, LOTRMod.stairsDwarvenBrick, 3);
							}
						}
					}
					placeRandomFeature(world, random, i + 2, j + l1 + 1, k - 4, randomFeature);
					placeRandomFeature(world, random, i + 1, j + l1 + 1, k - 4, randomFeature);
					setBlockAndNotifyAdequately(world, i, j + l1 + 1, k - 4, Blocks.planks, 1);
					setBlockAndNotifyAdequately(world, i + 3, j + l1 + 1, k - 4, Blocks.planks, 1);
					setBlockAndNotifyAdequately(world, i, j + l1 + 2, k - 4, Blocks.wooden_slab, 1);
					setBlockAndNotifyAdequately(world, i + 3, j + l1 + 2, k - 4, Blocks.wooden_slab, 1);
					break;
				case 3:
					for (int i1 = i - 1; i1 <= i + 4; i1++)
					{
						for (int k1 = k + 1; k1 <= k + 2; k1++)
						{
							setBlockAndNotifyAdequately(world, i1, j + l1 + 5, k1, Blocks.air, 0);
							int i2 = i1 - (i - 1);
							for (int j1 = j + l1 + 1; j1 <= j + l1 + i2; j1++)
							{
								setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 6);
							}
							if (i2 < 5)
							{
								setBlockAndNotifyAdequately(world, i1, j + l1 + i2 + 1, k1, LOTRMod.stairsDwarvenBrick, 0);
							}
						}
					}
					placeRandomFeature(world, random, i + 4, j + l1 + 1, k - 1, randomFeature);
					placeRandomFeature(world, random, i + 4, j + l1 + 1, k - 2, randomFeature);
					setBlockAndNotifyAdequately(world, i + 4, j + l1 + 1, k, Blocks.planks, 1);
					setBlockAndNotifyAdequately(world, i + 4, j + l1 + 1, k - 3, Blocks.planks, 1);
					setBlockAndNotifyAdequately(world, i + 4, j + l1 + 2, k, Blocks.wooden_slab, 1);
					setBlockAndNotifyAdequately(world, i + 4, j + l1 + 2, k - 3, Blocks.wooden_slab, 1);
					break;
			}
			
			LOTREntityDwarf dwarf = random.nextInt(3) == 0 ? new LOTREntityDwarfAxeThrower(world) : new LOTREntityDwarfWarrior(world);
			dwarf.setLocationAndAngles(i + 0.5D, j + l1 + 1, k + 0.5D, world.rand.nextFloat() * 360F, 0F);
			dwarf.onSpawnWithEgg(null);
			dwarf.setHomeArea(i, j + l1 + 1, k, 12);
			dwarf.isNPCPersistent = true;
			world.spawnEntityInWorld(dwarf);
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int j1 = 1; j1 <= 2; j1++)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j + (sections + 1) * 5 + j1, k1, Blocks.air, 0);
					setBlockAndNotifyAdequately(world, i1, j + (sections + 1) * 5 + j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + (sections + 1) * 5 + 1, k - 5, LOTRMod.wall, 7);
			setBlockAndNotifyAdequately(world, i1, j + (sections + 1) * 5 + 1, k + 5, LOTRMod.wall, 7);
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j + (sections + 1) * 5 + 1, k1, LOTRMod.wall, 7);
			setBlockAndNotifyAdequately(world, i + 5, j + (sections + 1) * 5 + 1, k1, LOTRMod.wall, 7);
		}
		
		generatePillar(world, i - 5, lowestHeight, j + (sections + 1) * 5 + 4, k - 5);
		generatePillar(world, i - 5, lowestHeight, j + (sections + 1) * 5 + 4, k + 6);
		generatePillar(world, i + 6, lowestHeight, j + (sections + 1) * 5 + 4, k - 5);
		generatePillar(world, i + 6, lowestHeight, j + (sections + 1) * 5 + 4, k + 6);
		
		switch (rotation)
		{
			case 0:
				setBlockAndNotifyAdequately(world, i, j, k - 5, LOTRMod.pillar, 0);
				setBlockAndNotifyAdequately(world, i, j + 1, k - 5, Blocks.wooden_door, 1);
				setBlockAndNotifyAdequately(world, i, j + 2, k - 5, Blocks.wooden_door, 8);
				
				for (int j1 = j + 3; !LOTRMod.isOpaque(world, i - 2, j1, k - 6) && j1 >= 0; j1--)
				{
					if (j1 == j + 2)
					{
						setBlockAndNotifyAdequately(world, i - 2, j1, k - 6, Blocks.glowstone, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i - 2, j1, k - 6, LOTRMod.pillar, 0);
					}
					if (world.getBlock(i - 2, j1 - 1, k - 6) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i - 2, j1 - 1, k - 6, Blocks.dirt, 0);
					}
				}
				
				for (int j1 = j + 3; !LOTRMod.isOpaque(world, i + 2, j1, k - 6) && j1 >= 0; j1--)
				{
					if (j1 == j + 2)
					{
						setBlockAndNotifyAdequately(world, i + 2, j1, k - 6, Blocks.glowstone, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i + 2, j1, k - 6, LOTRMod.pillar, 0);
					}
					if (world.getBlock(i + 2, j1 - 1, k - 6) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i + 2, j1 - 1, k - 6, Blocks.dirt, 0);
					}
				}
				
				for (int i1 = i - 2; i1 <= i + 2; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j + 4, k - 6, LOTRMod.pillar, 0);
				}
				
				placeBanner(world, i - 2, j + 5, k - 6, 0, 7);
				placeBanner(world, i + 2, j + 5, k - 6, 0, 7);
				
				break;
			case 1:
				setBlockAndNotifyAdequately(world, i + 5, j, k, LOTRMod.pillar, 0);
				setBlockAndNotifyAdequately(world, i + 5, j + 1, k, Blocks.wooden_door, 2);
				setBlockAndNotifyAdequately(world, i + 5, j + 2, k, Blocks.wooden_door, 8);
				
				for (int j1 = j + 3; !LOTRMod.isOpaque(world, i + 6, j1, k - 2) && j1 >= 0; j1--)
				{
					if (j1 == j + 2)
					{
						setBlockAndNotifyAdequately(world, i + 6, j1, k - 2, Blocks.glowstone, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i + 6, j1, k - 2, LOTRMod.pillar, 0);
					}
					if (world.getBlock(i + 6, j1 - 1, k - 2) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i + 6, j1 - 1, k - 2, Blocks.dirt, 0);
					}
				}
				
				for (int j1 = j + 3; !LOTRMod.isOpaque(world, i + 6, j1, k + 2) && j1 >= 0; j1--)
				{
					if (j1 == j + 2)
					{
						setBlockAndNotifyAdequately(world, i + 6, j1, k + 2, Blocks.glowstone, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i + 6, j1, k + 2, LOTRMod.pillar, 0);
					}
					if (world.getBlock(i + 6, j1 - 1, k + 2) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i + 6, j1 - 1, k + 2, Blocks.dirt, 0);
					}
				}
				
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i + 6, j + 4, k1, LOTRMod.pillar, 0);
				}
				
				placeBanner(world, i + 6, j + 5, k - 2, 1, 7);
				placeBanner(world, i + 6, j + 5, k + 2, 1, 7);
				
				break;
			case 2:
				setBlockAndNotifyAdequately(world, i, j, k + 5, LOTRMod.pillar, 0);
				setBlockAndNotifyAdequately(world, i, j + 1, k + 5, Blocks.wooden_door, 3);
				setBlockAndNotifyAdequately(world, i, j + 2, k + 5, Blocks.wooden_door, 8);
				
				for (int j1 = j + 3; !LOTRMod.isOpaque(world, i - 2, j1, k + 6) && j1 >= 0; j1--)
				{
					if (j1 == j + 2)
					{
						setBlockAndNotifyAdequately(world, i - 2, j1, k + 6, Blocks.glowstone, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i - 2, j1, k + 6, LOTRMod.pillar, 0);
					}
					if (world.getBlock(i - 2, j1 - 1, k + 6) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i - 2, j1 - 1, k + 6, Blocks.dirt, 0);
					}
				}
				
				for (int j1 = j + 3; !LOTRMod.isOpaque(world, i + 2, j1, k + 6) && j1 >= 0; j1--)
				{
					if (j1 == j + 2)
					{
						setBlockAndNotifyAdequately(world, i + 2, j1, k + 6, Blocks.glowstone, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i + 2, j1, k + 6, LOTRMod.pillar, 0);
					}
					if (world.getBlock(i + 2, j1 - 1, k + 6) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i + 2, j1 - 1, k + 6, Blocks.dirt, 0);
					}
				}
				
				for (int i1 = i - 2; i1 <= i + 2; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j + 4, k + 6, LOTRMod.pillar, 0);
				}
				
				placeBanner(world, i - 2, j + 5, k + 6, 2, 7);
				placeBanner(world, i + 2, j + 5, k + 6, 2, 7);
				
				break;
			case 3:
				setBlockAndNotifyAdequately(world, i - 5, j, k, LOTRMod.pillar, 0);
				setBlockAndNotifyAdequately(world, i - 5, j + 1, k, Blocks.wooden_door, 0);
				setBlockAndNotifyAdequately(world, i - 5, j + 2, k, Blocks.wooden_door, 8);
				
				for (int j1 = j + 3; !LOTRMod.isOpaque(world, i - 6, j1, k - 2) && j1 >= 0; j1--)
				{
					if (j1 == j + 2)
					{
						setBlockAndNotifyAdequately(world, i - 6, j1, k - 2, Blocks.glowstone, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i - 6, j1, k - 2, LOTRMod.pillar, 0);
					}
					if (world.getBlock(i - 6, j1 - 1, k - 2) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i - 6, j1 - 1, k - 2, Blocks.dirt, 0);
					}
				}
				
				for (int j1 = j + 3; !LOTRMod.isOpaque(world, i - 6, j1, k + 2) && j1 >= 0; j1--)
				{
					if (j1 == j + 2)
					{
						setBlockAndNotifyAdequately(world, i - 6, j1, k + 2, Blocks.glowstone, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i - 6, j1, k + 2, LOTRMod.pillar, 0);
					}
					if (world.getBlock(i - 6, j1 - 1, k + 2) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i - 6, j1 - 1, k + 2, Blocks.dirt, 0);
					}
				}
				
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i - 6, j + 4, k1, LOTRMod.pillar, 0);
				}
				
				placeBanner(world, i - 6, j + 5, k - 2, 3, 7);
				placeBanner(world, i - 6, j + 5, k + 2, 3, 7);
				
				break;
		}
		
		LOTREntityDwarfCommander dwarfCommander = new LOTREntityDwarfCommander(world);
		dwarfCommander.setLocationAndAngles(i + 0.5D, j + (sections + 1) * 5 + 1, k + 0.5D, world.rand.nextFloat() * 360F, 0F);
		dwarfCommander.onSpawnWithEgg(null);
		dwarfCommander.setHomeArea(i, j + (sections + 1) * 5 + 1, k, 16);
		world.spawnEntityInWorld(dwarfCommander);
		
		if (rotation == 0 || rotation == 2)
		{
			placeBanner(world, i - 4, j + (sections + 1) * 5 + 1, k, 1, 7);
			placeBanner(world, i + 4, j + (sections + 1) * 5 + 1, k, 3, 7);
		}
		else
		{
			placeBanner(world, i, j + (sections + 1) * 5 + 1, k - 4, 0, 7);
			placeBanner(world, i, j + (sections + 1) * 5 + 1, k + 4, 2, 7);
		}
		
		if (usingPlayer == null)
		{
			LOTRLevelData.dwarvenTowerLocations.add(new ChunkCoordinates(i, j, k));
		}
			
		return true;
	}
	
	private void generatePillar(World world, int i, int minY, int maxY, int k)
	{
		for (int i1 = i - 1; i1 <= i; i1++)
		{
			for (int k1 = k - 1; k1 <= k; k1++)
			{
				for (int j = minY; j <= maxY; j++)
				{
					if (j == maxY - 1)
					{
						setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.glowstone, 0);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.pillar, 0);
						if (world.getBlock(i1, j - 1, k1) == Blocks.grass)
						{
							setBlockAndNotifyAdequately(world, i1, j - 1, k1, Blocks.dirt, 0);
						}
					}
				}
			}
		}
	}
	
	private void placeRandomFeature(World world, Random random, int i, int j, int k, int randomFeature)
	{
		if (randomFeature == 0)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.dwarvenTable, 0);
		}
		else if (randomFeature == 1)
		{
			setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.dwarvenForge, 0);
			TileEntity tileentity = world.getTileEntity(i, j, k);
			if (tileentity != null && tileentity instanceof LOTRTileEntityDwarvenForge)
			{
				((LOTRTileEntityDwarvenForge)tileentity).setInventorySlotContents(12, new ItemStack(Items.coal, 1 + random.nextInt(4)));
			}
		}
		else if (randomFeature == 2)
		{
			setBlockAndNotifyAdequately(world, i, j, k, Blocks.wooden_slab, 9);
			setBlockAndNotifyAdequately(world, i, j + 1, k, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i, j + 1, k, LOTRChestContents.DWARVEN_TOWER);
		}
		else if (randomFeature == 3)
		{
			setBlockAndNotifyAdequately(world, i, j, k, Blocks.wooden_slab, 9);
			placePlateWithCertainty(world, i, j + 1, k, random, LOTRFoods.DWARF);
		}
		else if (randomFeature == 4)
		{
			setBlockAndNotifyAdequately(world, i, j, k, Blocks.wooden_slab, 9);
            Block l = world.getBlock(i, j + 1, k - 1);
            Block i1 = world.getBlock(i, j + 1, k + 1);
            Block j1 = world.getBlock(i - 1, j + 1, k);
            Block k1 = world.getBlock(i + 1, j + 1, k);
            byte meta = 3;
            if (l.isOpaqueCube() && !i1.isOpaqueCube())
            {
                meta = 3;
            }
            if (i1.isOpaqueCube() && !l.isOpaqueCube())
            {
                meta = 2;
            }
            if (j1.isOpaqueCube() && !k1.isOpaqueCube())
            {
                meta = 5;
            }
            if (k1.isOpaqueCube() && !j1.isOpaqueCube())
            {
                meta = 4;
            }
			placeBarrel(world, random, i, j + 1, k, meta, LOTRFoods.DWARF_DRINK.getRandomFood(random).getItem());
		}
	}
}
