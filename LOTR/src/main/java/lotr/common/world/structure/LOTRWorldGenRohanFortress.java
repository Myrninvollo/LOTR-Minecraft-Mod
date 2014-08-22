package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.npc.LOTREntityRohanBlacksmith;
import lotr.common.entity.npc.LOTREntityRohirrim;
import lotr.common.entity.npc.LOTREntityRohirrimArcher;
import lotr.common.entity.npc.LOTREntityRohirrimMarshal;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class LOTRWorldGenRohanFortress extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenRohanFortress(boolean flag)
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
		
		j--;
		
		int rotation = random.nextInt(4);
		
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
		}
		
		switch (rotation)
		{
			case 0:
				k += 13;
				break;
			case 1:
				i -= 13;
				break;
			case 2:
				k -= 13;
				break;
			case 3:
				i += 13;
				break;
		}
		
		if (restrictions)
		{
			for (int i1 = i - 12; i1 <= i + 12; i1++)
			{
				for (int k1 = k - 12; k1 <= k + 12; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					Block block = world.getBlock(i1, j1, k1);
					if (block == Blocks.grass || block == Blocks.dirt || block == Blocks.stone || block == LOTRMod.rock)
					{
						continue;
					}
					return false;
				}
			}
		}
		
		if (restrictions)
		{
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1D, j + 1D, k + 1D).offset(0D, 5D, 0D).expand(12D, 5D, 12D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int i1 = i - 12; i1 <= i + 12; i1++)
		{
			for (int k1 = k - 12; k1 <= k + 12; k1++)
			{
				for (int j1 = j + 1; j1 <= j + 9; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				for (int j1 = j; (j1 == j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
				{
					if (Math.abs(i1 - i) == 12 && Math.abs(k1 - k) == 12)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.log, 1);
					}
					else if (Math.abs(i1 - i) > 9 || Math.abs(k1 - k) > 9)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 0);
					}
					else
					{
						if (j1 == j)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.grass, 0);
						}
						else
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.dirt, 0);
						}
					}
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		for (int i1 = i - 12; i1 <= i + 12; i1++)
		{
			for (int k1 = k - 12; k1 <= k + 12; k1++)
			{
				int yBoost = 0;
				if (rotation == 0 && (k1 - k) < 8 && Math.abs(i1 - i) < 7)
				{
					yBoost = 1;
				}
				else if (rotation == 1 && (i1 - i) > 8 && Math.abs(k1 - k) < 7)
				{
					yBoost = 1;
				}
				else if (rotation == 2 && (k1 - k) > 8 && Math.abs(i1 - i) < 7)
				{
					yBoost = 1;
				}
				else if (rotation == 3 && (i1 - i) < 8 && Math.abs(k1 - k) < 7)
				{
					yBoost = 1;
				}
				
				if (Math.abs(i1 - i) == 12 && Math.abs(k1 - k) == 12)
				{
					for (int j1 = j + 1; j1 <= j + 8; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.log, 1);
					}
				}
				else if (Math.abs(i1 - i) == 12 || Math.abs(k1 - k) == 12)
				{
					for (int j1 = j + 1; j1 <= j + 5 + yBoost; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 0);
					}
					if (Math.abs(i1 - i) == 12)
					{
						if ((k1 - k) % 2 == 0)
						{
							setBlockAndNotifyAdequately(world, i1, j + 6 + yBoost, k1, Blocks.planks, 0);
						}
						else
						{
							setBlockAndNotifyAdequately(world, i1, j + 6 + yBoost, k1, Blocks.wooden_slab, 0);
						}
					}
					else if (Math.abs(k1 - k) == 12)
					{
						if ((i1 - i) % 2 == 0)
						{
							setBlockAndNotifyAdequately(world, i1, j + 6 + yBoost, k1, Blocks.planks, 0);
						}
						else
						{
							setBlockAndNotifyAdequately(world, i1, j + 6 + yBoost, k1, Blocks.wooden_slab, 0);
						}
					}
				}
				else if (Math.abs(i1 - i) > 9 || Math.abs(k1 - k) > 9)
				{
					for (int j1 = j + 1; j1 <= j + 4 + yBoost; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 0);
					}
				}
				else if (Math.abs(i1 - i) == 9 && Math.abs(k1 - k) == 9)
				{
					for (int j1 = j + 4; j1 <= j + 6 + yBoost; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.log, 1);
					}
					setBlockAndNotifyAdequately(world, i1, j + 7 + yBoost, k1, Blocks.torch, 5);
				}
				else if (Math.abs(i1 - i) == 9 || Math.abs(k1 - k) == 9)
				{
					setBlockAndNotifyAdequately(world, i1, j + 5 + yBoost, k1, Blocks.fence, 0);
					if ((Math.abs(i1 - i) == 9 && (k1 - k) % 3 == 0) || (Math.abs(k1 - k) == 9 && (i1 - i) % 3 == 0))
					{
						setBlockAndNotifyAdequately(world, i1, j + 6 + yBoost, k1, Blocks.torch, 5);
					}
					
					if (k1 == k - 9)
					{
						setBlockAndNotifyAdequately(world, i1, j + 4 + yBoost, k1, Blocks.oak_stairs, 7);
					}
					else if (k1 == k + 9)
					{
						setBlockAndNotifyAdequately(world, i1, j + 4 + yBoost, k1, Blocks.oak_stairs, 6);
					}
					else if (i1 == i - 9)
					{
						setBlockAndNotifyAdequately(world, i1, j + 4 + yBoost, k1, Blocks.oak_stairs, 5);
					}
					else if (i1 == i + 9)
					{
						setBlockAndNotifyAdequately(world, i1, j + 4 + yBoost, k1, Blocks.oak_stairs, 4);
					}
				}
			}
		}
		
		switch (rotation)
		{
			case 0:
				generateFacingSouth(world, random, i, j, k);
				break;
			case 1:
				generateFacingWest(world, random, i, j, k);
				break;
			case 2:
				generateFacingNorth(world, random, i, j, k);
				break;
			case 3:
				generateFacingEast(world, random, i, j, k);
				break;
		}
		
		LOTREntityRohirrimMarshal marshal = new LOTREntityRohirrimMarshal(world);
		marshal.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0F, 0F);
		marshal.spawnRidingHorse = false;
		marshal.onSpawnWithEgg(null);
		marshal.setHomeArea(i, j, k, 8);
		world.spawnEntityInWorld(marshal);
		
		
		for (int l = 0; l < 8; l++)
		{
			LOTREntityRohirrim rohirrim = world.rand.nextInt(3) == 0 ? new LOTREntityRohirrimArcher(world) : new LOTREntityRohirrim(world);
			rohirrim.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0F, 0F);
			rohirrim.spawnRidingHorse = false;
			rohirrim.onSpawnWithEgg(null);
			rohirrim.setHomeArea(i, j, k, 20);
			rohirrim.isNPCPersistent = true;
			world.spawnEntityInWorld(rohirrim);
		}
		
		if (usingPlayer == null)
		{
			LOTRLevelData.rohanFortressLocations.add(new ChunkCoordinates(i, j, k));
		}
		
		return true;
	}
	
	private void generateFacingSouth(World world, Random random, int i, int j, int k)
	{
		placeBanner(world, i - 12, j + 9, k - 12, 0, 1);
		placeBanner(world, i - 12, j + 9, k + 12, 0, 1);
		placeBanner(world, i + 12, j + 9, k - 12, 0, 1);
		placeBanner(world, i + 12, j + 9, k + 12, 0, 1);
		
		placeWallBanner(world, i, j + 6, k - 12, 2, 1);
		
		for (int k1 = k - 11; k1 <= k - 10; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j + 5, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i - 5, j + 5, k1, Blocks.oak_stairs, 0);
			setBlockAndNotifyAdequately(world, i + 5, j + 5, k1, Blocks.oak_stairs, 1);
			setBlockAndNotifyAdequately(world, i + 6, j + 5, k1, Blocks.air, 0);
		}
		
		for (int j1 = j + 4; j1 <= j + 7; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j1, k - 9, Blocks.log, 1);
			setBlockAndNotifyAdequately(world, i + 6, j1, k - 9, Blocks.log, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 6, j + 8, k - 9, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i + 6, j + 8, k - 9, Blocks.torch, 5);
		
		for (int k1 = k - 12; k1 <= k - 10; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i - 2, j1, k1, Blocks.log, 1);
				setBlockAndNotifyAdequately(world, i + 2, j1, k1, Blocks.log, 1);
			}
			
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, Blocks.log, 5);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 11, Blocks.fence_gate, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 13, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 13, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 9, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 9, Blocks.torch, 3);
		
		for (int k1 = k - 13; k1 <= k + 9; k1++)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.brick, 4);
			}
			
			if (k1 > k - 10)
			{
				setBlockAndNotifyAdequately(world, i - 2, j, k1, LOTRMod.slabDouble2, 1);
				setBlockAndNotifyAdequately(world, i + 2, j, k1, LOTRMod.slabDouble2, 1);
				
				if (Math.abs((k1 - k) % 4) == 2)
				{
					setBlockAndNotifyAdequately(world, i - 2, j + 1, k1, LOTRMod.wall, 6);
					setBlockAndNotifyAdequately(world, i - 2, j + 2, k1, Blocks.torch, 5);
					
					setBlockAndNotifyAdequately(world, i + 2, j + 1, k1, LOTRMod.wall, 6);
					setBlockAndNotifyAdequately(world, i + 2, j + 2, k1, Blocks.torch, 5);
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j1, k + 10, Blocks.log, 1);
			setBlockAndNotifyAdequately(world, i + 2, j1, k + 10, Blocks.log, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 9, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k + 9, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i - 7, j + 1, k, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 2, k, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 3, k, Blocks.torch, 5);
		
		for (int l = 0; l < 2; l++)
		{
			LOTREntityHorse horse = new LOTREntityHorse(world);
			horse.setLocationAndAngles(i - 4 + 0.5D, j + 1, k + 0.5D, 0F, 0F);
			horse.onSpawnWithEgg(null);
			horse.setHorseType(0);
			horse.setGrowingAge(0);
			horse.saddleMount();
			world.spawnEntityInWorld(horse);
			
			EntityLeashKnot leash = EntityLeashKnot.func_110129_a(world, i - 7, j + 2, k);
			horse.setLeashedToEntity(leash, true);
		}
		
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 2, k, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 3, k, Blocks.torch, 5);
		
		for (int l = 0; l < 2; l++)
		{
			LOTREntityHorse horse = new LOTREntityHorse(world);
			horse.setLocationAndAngles(i + 4 + 0.5D, j + 1, k + 0.5D, 0F, 0F);
			horse.onSpawnWithEgg(null);
			horse.setHorseType(0);
			horse.setGrowingAge(0);
			horse.saddleMount();
			world.spawnEntityInWorld(horse);
			
			EntityLeashKnot leash = EntityLeashKnot.func_110129_a(world, i + 7, j + 2, k);
			horse.setLeashedToEntity(leash, true);
		}
		
		for (int k1 = k - 9; k1 <= k - 5; k1++)
		{
			for (int i1 = i - 9; i1 <= i - 5; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 3, k1, Blocks.wooden_slab, 1);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 9, j + 3, k - 9, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i - 6, j + 3, k - 9, Blocks.planks, 1);
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 7, j1, k - 9, Blocks.furnace, 0);
			setBlockMetadata(world, i - 7, j1, k - 9, 3);
			setBlockAndNotifyAdequately(world, i - 8, j1, k - 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 9, j1, k - 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 9, j1, k - 8, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 9, j1, k - 7, Blocks.furnace, 0);
			setBlockMetadata(world, i - 9, j1, k - 7, 5);
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 5, Blocks.fence, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 9, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 9, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 6, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 5, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 5, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 6, Blocks.anvil, 0);
		
		spawnBlacksmith(world, i - 4, j + 1, k - 4);
		
		for (int k1 = k + 5; k1 <= k + 9; k1++)
		{
			for (int i1 = i - 9; i1 <= i - 5; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 3, k1, Blocks.wooden_slab, 1);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 9, j + 3, k + 9, Blocks.planks, 1);
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 9, j1, k + 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 5, Blocks.fence, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 9, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k + 9, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 7, j + 1, k + 9, Blocks.chest, 2);
		LOTRChestContents.fillChest(world, random, i - 7, j + 1, k + 9, LOTRChestContents.ROHAN_WATCHTOWER);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k + 9, Blocks.chest, 2);
		LOTRChestContents.fillChest(world, random, i - 8, j + 1, k + 9, LOTRChestContents.ROHAN_WATCHTOWER);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k + 8, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k + 7, LOTRMod.rohirricTable, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k + 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k + 6, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k + 5, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k + 5, Blocks.fence, 0);
		
		for (int k1 = k + 5; k1 <= k + 10; k1++)
		{
			for (int i1 = i + 5; i1 <= i + 10; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.planks, 1);
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j + 2, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j + 3, k1, Blocks.planks, 1);
			}
		}
		
		for (int k1 = k + 4; k1 <= k + 9; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 4, j + 3, k1, Blocks.spruce_stairs, 0);
		}
		
		for (int i1 = i + 5; i1 <= i + 9; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 4, Blocks.spruce_stairs, 2);
		}

		for (int k1 = k + 5; k1 <= k + 10; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i + 5, j + 2, k1, Blocks.planks, 0);
		}
		
		for (int i1 = i + 6; i1 <= i + 10; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 5, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 5, Blocks.planks, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 5, j, k + 8, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 8, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 8, Blocks.wooden_door, 8);
		
		for (int i1 = i + 6; i1 <= i + 10; i1 += 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 7, LOTRMod.strawBed, 2);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 6, LOTRMod.strawBed, 10);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 6, Blocks.torch, 3);
			
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 9, LOTRMod.strawBed, 0);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 10, LOTRMod.strawBed, 8);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 10, Blocks.torch, 4);
		}
		
		placeBarrel(world, random, i + 11, j + 2, k + 8, 4, LOTRMod.mugMead);
		
		for (int j1 = j + 1; j1 <= j + 4; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 6, j1, k - 9, Blocks.log, 1);
			setBlockAndNotifyAdequately(world, i + 7, j1, k - 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i + 8, j1, k - 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i + 9, j1, k - 9, Blocks.log, 1);
		}
		
		for (int k1 = k - 8; k1 <= k - 7; k1++)
		{
			for (int i1 = i + 6; i1 <= i + 9; i1++)
			{
				int stairHeight = i1 - (i + 5);
				for (int j1 = j; j1 < j + stairHeight; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 0);
				}
				setBlockAndNotifyAdequately(world, i1, j + stairHeight, k1, Blocks.oak_stairs, 0);
			}
			
			setBlockAndNotifyAdequately(world, i + 9, j + 5, k1, Blocks.air, 0);
		}
	}
	
	private void generateFacingWest(World world, Random random, int i, int j, int k)
	{
		placeBanner(world, i - 12, j + 9, k - 12, 1, 1);
		placeBanner(world, i - 12, j + 9, k + 12, 1, 1);
		placeBanner(world, i + 12, j + 9, k - 12, 1, 1);
		placeBanner(world, i + 12, j + 9, k + 12, 1, 1);
		
		placeWallBanner(world, i + 12, j + 6, k, 3, 1);
		
		for (int i1 = i + 11; i1 >= i + 10; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 6, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 5, Blocks.oak_stairs, 2);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 5, Blocks.oak_stairs, 3);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 6, Blocks.air, 0);
		}
		
		for (int j1 = j + 4; j1 <= j + 7; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 9, j1, k - 6, Blocks.log, 1);
			setBlockAndNotifyAdequately(world, i + 9, j1, k + 6, Blocks.log, 1);
		}
		
		setBlockAndNotifyAdequately(world, i + 9, j + 8, k - 6, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i + 9, j + 8, k + 6, Blocks.torch, 5);
		
		for (int i1 = i + 12; i1 >= i + 10; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j1, k - 2, Blocks.log, 1);
				setBlockAndNotifyAdequately(world, i1, j1, k + 2, Blocks.log, 1);
			}
			
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, Blocks.log, 9);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 11, j + 2, k1, Blocks.fence_gate, 1);
		}
		
		setBlockAndNotifyAdequately(world, i + 13, j + 2, k - 2, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 13, j + 2, k + 2, Blocks.torch, 1);
		
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k - 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k + 2, Blocks.torch, 2);
		
		for (int i1 = i + 13; i1 >= i - 9; i1--)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.brick, 4);
			}
			
			if (i1 < i + 10)
			{
				setBlockAndNotifyAdequately(world, i1, j, k - 2, LOTRMod.slabDouble2, 1);
				setBlockAndNotifyAdequately(world, i1, j, k + 2, LOTRMod.slabDouble2, 1);
				
				if (Math.abs((i1 - i) % 4) == 2)
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k - 2, LOTRMod.wall, 6);
					setBlockAndNotifyAdequately(world, i1, j + 2, k - 2, Blocks.torch, 5);
					
					setBlockAndNotifyAdequately(world, i1, j + 1, k + 2, LOTRMod.wall, 6);
					setBlockAndNotifyAdequately(world, i1, j + 2, k + 2, Blocks.torch, 5);
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 10, j1, k - 2, Blocks.log, 1);
			setBlockAndNotifyAdequately(world, i - 10, j1, k + 2, Blocks.log, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 2, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k + 2, Blocks.torch, 1);
		
		setBlockAndNotifyAdequately(world, i, j + 1, k - 7, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 7, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i, j + 3, k - 7, Blocks.torch, 5);
		
		for (int l = 0; l < 2; l++)
		{
			LOTREntityHorse horse = new LOTREntityHorse(world);
			horse.setLocationAndAngles(i + 0.5D, j + 1, k - 4 + 0.5D, 0F, 0F);
			horse.onSpawnWithEgg(null);
			horse.setHorseType(0);
			horse.setGrowingAge(0);
			horse.saddleMount();
			world.spawnEntityInWorld(horse);
			
			EntityLeashKnot leash = EntityLeashKnot.func_110129_a(world, i, j + 2, k - 7);
			horse.setLeashedToEntity(leash, true);
		}
		
		setBlockAndNotifyAdequately(world, i, j + 1, k + 7, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 7, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i, j + 3, k + 7, Blocks.torch, 5);
		
		for (int l = 0; l < 2; l++)
		{
			LOTREntityHorse horse = new LOTREntityHorse(world);
			horse.setLocationAndAngles(i + 0.5D, j + 1, k + 4 + 0.5D, 0F, 0F);
			horse.onSpawnWithEgg(null);
			horse.setHorseType(0);
			horse.setGrowingAge(0);
			horse.saddleMount();
			world.spawnEntityInWorld(horse);
			
			EntityLeashKnot leash = EntityLeashKnot.func_110129_a(world, i, j + 2, k + 7);
			horse.setLeashedToEntity(leash, true);
		}
		
		for (int i1 = i + 9; i1 >= i + 5; i1--)
		{
			for (int k1 = k - 9; k1 <= k - 5; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 3, k1, Blocks.wooden_slab, 1);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 9, j + 3, k - 9, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 9, j + 3, k - 6, Blocks.planks, 1);
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 9, j1, k - 7, Blocks.furnace, 0);
			setBlockMetadata(world, i + 9, j1, k - 7, 4);
			setBlockAndNotifyAdequately(world, i + 9, j1, k - 8, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i + 9, j1, k - 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i + 8, j1, k - 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i + 7, j1, k - 9, Blocks.furnace, 0);
			setBlockMetadata(world, i + 7, j1, k - 9, 3);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 5, Blocks.fence, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k - 5, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k - 5, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k - 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k - 6, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 2, k - 9, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 9, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 6, Blocks.anvil, 0);
		
		spawnBlacksmith(world, i + 4, j + 1, k - 4);
		
		for (int i1 = i - 5; i1 >= i - 9; i1--)
		{
			for (int k1 = k - 9; k1 <= k - 5; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 3, k1, Blocks.wooden_slab, 1);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 9, j + 3, k - 9, Blocks.planks, 1);
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 9, j1, k - 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 5, Blocks.fence, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 5, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 5, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 6, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 7, Blocks.chest, 5);
		LOTRChestContents.fillChest(world, random, i - 9, j + 1, k - 7, LOTRChestContents.ROHAN_WATCHTOWER);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 8, Blocks.chest, 5);
		LOTRChestContents.fillChest(world, random, i - 9, j + 1, k - 8, LOTRChestContents.ROHAN_WATCHTOWER);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 9, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 1, k - 9, LOTRMod.rohirricTable, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 9, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 9, Blocks.fence, 0);
		
		for (int i1 = i - 5; i1 >= i - 10; i1--)
		{
			for (int k1 = k + 5; k1 <= k + 10; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.planks, 1);
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j + 2, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j + 3, k1, Blocks.planks, 1);
			}
		}
		
		for (int i1 = i - 4; i1 >= i - 9; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 4, Blocks.spruce_stairs, 2);
		}
		
		for (int k1 = k + 5; k1 <= k + 9; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 3, k1, Blocks.spruce_stairs, 1);
		}

		for (int i1 = i - 5; i1 >= i - 10; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 5, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 5, Blocks.planks, 0);
		}
		
		for (int k1 = k + 6; k1 <= k + 10; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j + 1, k1, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 5, j + 2, k1, Blocks.planks, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 8, j, k + 5, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k + 5, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i - 8, j + 2, k + 5, Blocks.wooden_door, 8);
		
		for (int k1 = k + 6; k1 <= k + 10; k1 += 2)
		{
			setBlockAndNotifyAdequately(world, i - 7, j + 1, k1, LOTRMod.strawBed, 3);
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k1, LOTRMod.strawBed, 11);
			setBlockAndNotifyAdequately(world, i - 6, j + 2, k1, Blocks.torch, 2);
			
			setBlockAndNotifyAdequately(world, i - 9, j + 1, k1, LOTRMod.strawBed, 1);
			setBlockAndNotifyAdequately(world, i - 10, j + 1, k1, LOTRMod.strawBed, 9);
			setBlockAndNotifyAdequately(world, i - 10, j + 2, k1, Blocks.torch, 1);
		}
		
		placeBarrel(world, random, i - 8, j + 2, k + 11, 2, LOTRMod.mugMead);
		
		for (int j1 = j + 1; j1 <= j + 4; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 9, j1, k + 6, Blocks.log, 1);
			setBlockAndNotifyAdequately(world, i + 9, j1, k + 7, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i + 9, j1, k + 8, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i + 9, j1, k + 6, Blocks.log, 1);
		}
		
		for (int i1 = i + 8; i1 >= i + 7; i1--)
		{
			for (int k1 = k + 6; k1 <= k + 9; k1++)
			{
				int stairHeight = k1 - (k + 5);
				for (int j1 = j; j1 < j + stairHeight; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 0);
				}
				setBlockAndNotifyAdequately(world, i1, j + stairHeight, k1, Blocks.oak_stairs, 2);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 9, Blocks.air, 0);
		}
	}
	
	private void generateFacingNorth(World world, Random random, int i, int j, int k)
	{
		placeBanner(world, i - 12, j + 9, k - 12, 2, 1);
		placeBanner(world, i - 12, j + 9, k + 12, 2, 1);
		placeBanner(world, i + 12, j + 9, k - 12, 2, 1);
		placeBanner(world, i + 12, j + 9, k + 12, 2, 1);
		
		placeWallBanner(world, i, j + 6, k + 12, 0, 1);
		
		for (int k1 = k + 11; k1 >= k + 10; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 6, j + 5, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i - 5, j + 5, k1, Blocks.oak_stairs, 0);
			setBlockAndNotifyAdequately(world, i + 5, j + 5, k1, Blocks.oak_stairs, 1);
			setBlockAndNotifyAdequately(world, i + 6, j + 5, k1, Blocks.air, 0);
		}
		
		for (int j1 = j + 4; j1 <= j + 7; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j1, k + 9, Blocks.log, 1);
			setBlockAndNotifyAdequately(world, i + 6, j1, k + 9, Blocks.log, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 6, j + 8, k + 9, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i + 6, j + 8, k + 9, Blocks.torch, 5);
		
		for (int k1 = k + 12; k1 >= k + 10; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i - 2, j1, k1, Blocks.log, 1);
				setBlockAndNotifyAdequately(world, i + 2, j1, k1, Blocks.log, 1);
			}
			
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, Blocks.log, 5);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 11, Blocks.fence_gate, 2);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 13, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k + 13, Blocks.torch, 3);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 9, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k + 9, Blocks.torch, 4);
		
		for (int k1 = k + 13; k1 >= k - 9; k1--)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.brick, 4);
			}
			
			if (k1 < k + 10)
			{
				setBlockAndNotifyAdequately(world, i - 2, j, k1, LOTRMod.slabDouble2, 1);
				setBlockAndNotifyAdequately(world, i + 2, j, k1, LOTRMod.slabDouble2, 1);
				
				if (Math.abs((k1 - k) % 4) == 2)
				{
					setBlockAndNotifyAdequately(world, i - 2, j + 1, k1, LOTRMod.wall, 6);
					setBlockAndNotifyAdequately(world, i - 2, j + 2, k1, Blocks.torch, 5);
					
					setBlockAndNotifyAdequately(world, i + 2, j + 1, k1, LOTRMod.wall, 6);
					setBlockAndNotifyAdequately(world, i + 2, j + 2, k1, Blocks.torch, 5);
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j1, k - 10, Blocks.log, 1);
			setBlockAndNotifyAdequately(world, i + 2, j1, k - 10, Blocks.log, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 9, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 9, Blocks.torch, 3);
		
		setBlockAndNotifyAdequately(world, i - 7, j + 1, k, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 2, k, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 3, k, Blocks.torch, 5);
		
		for (int l = 0; l < 2; l++)
		{
			LOTREntityHorse horse = new LOTREntityHorse(world);
			horse.setLocationAndAngles(i - 4 + 0.5D, j + 1, k + 0.5D, 0F, 0F);
			horse.onSpawnWithEgg(null);
			horse.setHorseType(0);
			horse.setGrowingAge(0);
			horse.saddleMount();
			world.spawnEntityInWorld(horse);
			
			EntityLeashKnot leash = EntityLeashKnot.func_110129_a(world, i - 7, j + 2, k);
			horse.setLeashedToEntity(leash, true);
		}
		
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 2, k, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 3, k, Blocks.torch, 5);
		
		for (int l = 0; l < 2; l++)
		{
			LOTREntityHorse horse = new LOTREntityHorse(world);
			horse.setLocationAndAngles(i + 4 + 0.5D, j + 1, k + 0.5D, 0F, 0F);
			horse.onSpawnWithEgg(null);
			horse.setHorseType(0);
			horse.setGrowingAge(0);
			horse.saddleMount();
			world.spawnEntityInWorld(horse);
			
			EntityLeashKnot leash = EntityLeashKnot.func_110129_a(world, i + 7, j + 2, k);
			horse.setLeashedToEntity(leash, true);
		}
		
		for (int k1 = k + 9; k1 >= k + 5; k1--)
		{
			for (int i1 = i - 9; i1 <= i - 5; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 3, k1, Blocks.wooden_slab, 1);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 9, j + 3, k + 9, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i - 6, j + 3, k + 9, Blocks.planks, 1);
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 7, j1, k + 9, Blocks.furnace, 0);
			setBlockMetadata(world, i - 7, j1, k + 9, 2);
			setBlockAndNotifyAdequately(world, i - 8, j1, k + 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 9, j1, k + 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 9, j1, k + 8, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 9, j1, k + 7, Blocks.furnace, 0);
			setBlockMetadata(world, i - 9, j1, k + 7, 5);
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 5, Blocks.fence, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 9, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k + 9, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k + 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k + 6, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k + 5, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k + 5, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 6, Blocks.anvil, 0);
		
		spawnBlacksmith(world, i - 4, j + 1, k + 4);
		
		for (int k1 = k - 5; k1 >= k - 9; k1--)
		{
			for (int i1 = i - 9; i1 <= i - 5; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 3, k1, Blocks.wooden_slab, 1);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 9, j + 3, k - 9, Blocks.planks, 1);
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 9, j1, k - 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 5, Blocks.fence, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 9, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 9, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 7, j + 1, k - 9, Blocks.chest, 3);
		LOTRChestContents.fillChest(world, random, i - 7, j + 1, k - 9, LOTRChestContents.ROHAN_WATCHTOWER);
		setBlockAndNotifyAdequately(world, i - 8, j + 1, k - 9, Blocks.chest, 3);
		LOTRChestContents.fillChest(world, random, i - 8, j + 1, k - 9, LOTRChestContents.ROHAN_WATCHTOWER);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 8, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 7, LOTRMod.rohirricTable, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 6, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 5, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 5, Blocks.fence, 0);
		
		for (int k1 = k - 5; k1 >= k - 10; k1--)
		{
			for (int i1 = i + 5; i1 <= i + 10; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.planks, 1);
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j + 2, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j + 3, k1, Blocks.planks, 1);
			}
		}
		
		for (int k1 = k - 4; k1 >= k - 9; k1--)
		{
			setBlockAndNotifyAdequately(world, i + 4, j + 3, k1, Blocks.spruce_stairs, 0);
		}
		
		for (int i1 = i + 5; i1 <= i + 9; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 4, Blocks.spruce_stairs, 3);
		}

		for (int k1 = k - 5; k1 >= k - 10; k1--)
		{
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i + 5, j + 2, k1, Blocks.planks, 0);
		}
		
		for (int i1 = i + 6; i1 <= i + 10; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 5, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 5, Blocks.planks, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 5, j, k - 8, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 8, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 8, Blocks.wooden_door, 8);
		
		for (int i1 = i + 6; i1 <= i + 10; i1 += 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 7, LOTRMod.strawBed, 0);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 6, LOTRMod.strawBed, 8);
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 6, Blocks.torch, 4);
			
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 9, LOTRMod.strawBed, 2);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 10, LOTRMod.strawBed, 10);
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 10, Blocks.torch, 3);
		}
		
		placeBarrel(world, random, i + 11, j + 2, k - 8, 4, LOTRMod.mugMead);
		
		for (int j1 = j + 1; j1 <= j + 4; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 6, j1, k + 9, Blocks.log, 1);
			setBlockAndNotifyAdequately(world, i + 7, j1, k + 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i + 8, j1, k + 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i + 9, j1, k + 9, Blocks.log, 1);
		}
		
		for (int k1 = k + 8; k1 >= k + 7; k1--)
		{
			for (int i1 = i + 6; i1 <= i + 9; i1++)
			{
				int stairHeight = i1 - (i + 5);
				for (int j1 = j; j1 < j + stairHeight; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 0);
				}
				setBlockAndNotifyAdequately(world, i1, j + stairHeight, k1, Blocks.oak_stairs, 0);
			}
			
			setBlockAndNotifyAdequately(world, i + 9, j + 5, k1, Blocks.air, 0);
		}
	}
	
	private void generateFacingEast(World world, Random random, int i, int j, int k)
	{
		placeBanner(world, i - 12, j + 9, k - 12, 3, 1);
		placeBanner(world, i - 12, j + 9, k + 12, 3, 1);
		placeBanner(world, i + 12, j + 9, k - 12, 3, 1);
		placeBanner(world, i + 12, j + 9, k + 12, 3, 1);
		
		placeWallBanner(world, i - 12, j + 6, k, 1, 1);
		
		for (int i1 = i - 11; i1 <= i - 10; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 6, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 5, Blocks.oak_stairs, 2);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 5, Blocks.oak_stairs, 3);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 6, Blocks.air, 0);
		}
		
		for (int j1 = j + 4; j1 <= j + 7; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 9, j1, k - 6, Blocks.log, 1);
			setBlockAndNotifyAdequately(world, i - 9, j1, k + 6, Blocks.log, 1);
		}
		
		setBlockAndNotifyAdequately(world, i - 9, j + 8, k - 6, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i - 9, j + 8, k + 6, Blocks.torch, 5);
		
		for (int i1 = i - 12; i1 <= i - 10; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
				
				setBlockAndNotifyAdequately(world, i1, j1, k - 2, Blocks.log, 1);
				setBlockAndNotifyAdequately(world, i1, j1, k + 2, Blocks.log, 1);
			}
			
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4, k1, Blocks.log, 9);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 11, j + 2, k1, Blocks.fence_gate, 3);
		}
		
		setBlockAndNotifyAdequately(world, i - 13, j + 2, k - 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 13, j + 2, k + 2, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 2, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k + 2, Blocks.torch, 1);
		
		for (int i1 = i - 13; i1 <= i + 9; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.brick, 4);
			}
			
			if (i1 > i - 10)
			{
				setBlockAndNotifyAdequately(world, i1, j, k - 2, LOTRMod.slabDouble2, 1);
				setBlockAndNotifyAdequately(world, i1, j, k + 2, LOTRMod.slabDouble2, 1);
				
				if (Math.abs((i1 - i) % 4) == 2)
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k - 2, LOTRMod.wall, 6);
					setBlockAndNotifyAdequately(world, i1, j + 2, k - 2, Blocks.torch, 5);
					
					setBlockAndNotifyAdequately(world, i1, j + 1, k + 2, LOTRMod.wall, 6);
					setBlockAndNotifyAdequately(world, i1, j + 2, k + 2, Blocks.torch, 5);
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 10, j1, k - 2, Blocks.log, 1);
			setBlockAndNotifyAdequately(world, i + 10, j1, k + 2, Blocks.log, 1);
		}
		
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k - 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k + 2, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i, j + 1, k - 7, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 7, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i, j + 3, k - 7, Blocks.torch, 5);
		
		for (int l = 0; l < 2; l++)
		{
			LOTREntityHorse horse = new LOTREntityHorse(world);
			horse.setLocationAndAngles(i + 0.5D, j + 1, k - 4 + 0.5D, 0F, 0F);
			horse.onSpawnWithEgg(null);
			horse.setHorseType(0);
			horse.setGrowingAge(0);
			horse.saddleMount();
			world.spawnEntityInWorld(horse);
			
			EntityLeashKnot leash = EntityLeashKnot.func_110129_a(world, i, j + 2, k - 7);
			horse.setLeashedToEntity(leash, true);
		}
		
		setBlockAndNotifyAdequately(world, i, j + 1, k + 7, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 7, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i, j + 3, k + 7, Blocks.torch, 5);
		
		for (int l = 0; l < 2; l++)
		{
			LOTREntityHorse horse = new LOTREntityHorse(world);
			horse.setLocationAndAngles(i + 0.5D, j + 1, k + 4 + 0.5D, 0F, 0F);
			horse.onSpawnWithEgg(null);
			horse.setHorseType(0);
			horse.setGrowingAge(0);
			horse.saddleMount();
			world.spawnEntityInWorld(horse);
			
			EntityLeashKnot leash = EntityLeashKnot.func_110129_a(world, i, j + 2, k + 7);
			horse.setLeashedToEntity(leash, true);
		}
		
		for (int i1 = i - 9; i1 <= i - 5; i1++)
		{
			for (int k1 = k - 9; k1 <= k - 5; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 3, k1, Blocks.wooden_slab, 1);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 9, j + 3, k - 9, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i - 9, j + 3, k - 6, Blocks.planks, 1);
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 9, j1, k - 7, Blocks.furnace, 0);
			setBlockMetadata(world, i - 9, j1, k - 7, 5);
			setBlockAndNotifyAdequately(world, i - 9, j1, k - 8, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 9, j1, k - 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 8, j1, k - 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 7, j1, k - 9, Blocks.furnace, 0);
			setBlockMetadata(world, i - 7, j1, k - 9, 3);
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 5, Blocks.fence, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 5, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 5, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 1, k - 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 9, j + 2, k - 6, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 9, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 9, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 6, Blocks.anvil, 0);
		
		spawnBlacksmith(world, i - 4, j + 1, k - 4);
		
		for (int i1 = i + 5; i1 <= i + 9; i1++)
		{
			for (int k1 = k - 9; k1 <= k - 5; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 3, k1, Blocks.wooden_slab, 1);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 9, j + 3, k - 9, Blocks.planks, 1);
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 9, j1, k - 9, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 5, Blocks.fence, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k - 5, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k - 5, Blocks.fence, 0);
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k - 6, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 9, j + 2, k - 6, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k - 7, Blocks.chest, 4);
		LOTRChestContents.fillChest(world, random, i + 9, j + 1, k - 7, LOTRChestContents.ROHAN_WATCHTOWER);
		setBlockAndNotifyAdequately(world, i + 9, j + 1, k - 8, Blocks.chest, 4);
		LOTRChestContents.fillChest(world, random, i + 9, j + 1, k - 8, LOTRChestContents.ROHAN_WATCHTOWER);
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k - 9, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k - 9, LOTRMod.rohirricTable, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 6, j + 2, k - 9, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 9, Blocks.planks, 0);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 9, Blocks.fence, 0);
		
		for (int i1 = i + 5; i1 <= i + 10; i1++)
		{
			for (int k1 = k + 5; k1 <= k + 10; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.planks, 1);
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j + 2, k1, Blocks.air, 0);
				setBlockAndNotifyAdequately(world, i1, j + 3, k1, Blocks.planks, 1);
			}
		}
		
		for (int i1 = i + 4; i1 <= i + 9; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 4, Blocks.spruce_stairs, 2);
		}
		
		for (int k1 = k + 5; k1 <= k + 9; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 4, j + 3, k1, Blocks.spruce_stairs, 0);
		}

		for (int i1 = i + 5; i1 <= i + 10; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 5, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 5, Blocks.planks, 0);
		}
		
		for (int k1 = k + 6; k1 <= k + 10; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i + 5, j + 2, k1, Blocks.planks, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 8, j, k + 5, Blocks.planks, 1);
		setBlockAndNotifyAdequately(world, i + 8, j + 1, k + 5, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i + 8, j + 2, k + 5, Blocks.wooden_door, 8);
		
		for (int k1 = k + 6; k1 <= k + 10; k1 += 2)
		{
			setBlockAndNotifyAdequately(world, i + 7, j + 1, k1, LOTRMod.strawBed, 1);
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k1, LOTRMod.strawBed, 9);
			setBlockAndNotifyAdequately(world, i + 6, j + 2, k1, Blocks.torch, 1);
			
			setBlockAndNotifyAdequately(world, i + 9, j + 1, k1, LOTRMod.strawBed, 3);
			setBlockAndNotifyAdequately(world, i + 10, j + 1, k1, LOTRMod.strawBed, 11);
			setBlockAndNotifyAdequately(world, i + 10, j + 2, k1, Blocks.torch, 2);
		}
		
		placeBarrel(world, random, i + 8, j + 2, k + 11, 2, LOTRMod.mugMead);
		
		for (int j1 = j + 1; j1 <= j + 4; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 9, j1, k + 6, Blocks.log, 1);
			setBlockAndNotifyAdequately(world, i - 9, j1, k + 7, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 9, j1, k + 8, Blocks.planks, 0);
			setBlockAndNotifyAdequately(world, i - 9, j1, k + 6, Blocks.log, 1);
		}
		
		for (int i1 = i - 8; i1 <= i - 7; i1++)
		{
			for (int k1 = k + 6; k1 <= k + 9; k1++)
			{
				int stairHeight = k1 - (k + 5);
				for (int j1 = j; j1 < j + stairHeight; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 0);
				}
				setBlockAndNotifyAdequately(world, i1, j + stairHeight, k1, Blocks.oak_stairs, 2);
			}
			
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 9, Blocks.air, 0);
		}
	}
	
	private void spawnBlacksmith(World world, int i, int j, int k)
	{
		LOTREntityRohanBlacksmith blacksmith = new LOTREntityRohanBlacksmith(world);
		blacksmith.setLocationAndAngles(i + 0.5D, j, k + 0.5D, 0F, 0F);
		blacksmith.onSpawnWithEgg(null);
		blacksmith.setHomeArea(i, j, k, 8);
		world.spawnEntityInWorld(blacksmith);
	}
}
