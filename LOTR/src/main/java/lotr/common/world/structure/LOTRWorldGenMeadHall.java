package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockPlate;
import lotr.common.entity.npc.LOTREntityRohanMeadhost;
import lotr.common.entity.npc.LOTREntityRohirrim;
import lotr.common.entity.npc.LOTRNames;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class LOTRWorldGenMeadHall extends LOTRWorldGenStructureBase
{
	private Block plankBlock;
	private int plankMeta;
	
	private Block woodBlock;
	private int woodMeta;
	
	private Block stairBlock;
	
	private Block halfBlock;
	private int halfMeta;
	
	private Block floorBlock;
	private int floorMeta;
	
	private String[] meadHallName;
	
	public LOTRWorldGenMeadHall(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBiomeGenForCoords(i, k) != LOTRBiome.rohan)
			{
				return false;
			}
			if (world.getBlock(i, j - 1, k) != Blocks.grass)
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
		
		int xOffset = 0;
		int zOffset = 0;
		switch (rotation)
		{
			case 0:
				zOffset = 11;
				break;
			case 1:
				xOffset = -11;
				break;
			case 2:
				zOffset = -11;
				break;
			case 3:
				xOffset = 11;
				break;
		}
		
		int randomWood = random.nextInt(2);
		
		switch (randomWood)
		{
			case 0:
				plankBlock = Blocks.planks;
				plankMeta = 0;
				
				woodBlock = Blocks.log;
				woodMeta = 0;
				
				stairBlock = Blocks.oak_stairs;
				
				halfBlock = Blocks.wooden_slab;
				halfMeta = 0;
				
				break;
			case 1:
				plankBlock = Blocks.planks;
				plankMeta = 1;
				
				woodBlock = Blocks.log;
				woodMeta = 1;
				
				stairBlock = Blocks.spruce_stairs;
				
				halfBlock = Blocks.wooden_slab;
				halfMeta = 1;
				
				break;
		}
		
		int randomFloor = random.nextInt(3);
		switch (randomFloor)
		{
			case 0:
				floorBlock = Blocks.stonebrick;
				floorMeta = 0;
				break;
			case 1:
				floorBlock = Blocks.brick_block;
				floorMeta = 0;
				break;
			case 2:
				floorBlock = LOTRMod.brick;
				floorMeta = 4;
				break;
		}
		
		meadHallName = LOTRNames.getRandomRohanMeadHallName(random);
		
		boolean flag = false;
		switch (rotation)
		{
			case 0:
				flag = generateFacingSouth(world, random, i, j, k);
				break;
			case 1:
				flag = generateFacingWest(world, random, i, j, k);
				break;
			case 2:
				flag = generateFacingNorth(world, random, i, j, k);
				break;
			case 3:
				flag = generateFacingEast(world, random, i, j, k);
				break;
		}
		
		if (flag)
		{
			LOTREntityRohanMeadhost meadHost = new LOTREntityRohanMeadhost(world);
			meadHost.setLocationAndAngles(i + xOffset + 0.5D, j + 3D, k + zOffset + 0.5D, 0F, 0F);
			meadHost.onSpawnWithEgg(null);
			meadHost.setHomeArea(i + xOffset, j + 1, k + zOffset, 4);
			meadHost.setSpecificLocationName(meadHallName[0] + " " + meadHallName[1]);
			world.spawnEntityInWorld(meadHost);
			
			int men = 3 + random.nextInt(5);
			for (int l = 0; l < men; l++)
			{
				LOTREntityRohirrim man = new LOTREntityRohirrim(world);
				man.setLocationAndAngles(i + xOffset + 0.5D, j + 3D, k + zOffset + 0.5D, 0F, 0F);
				man.spawnRidingHorse = false;
				man.onSpawnWithEgg(null);
				man.setCurrentItemOrArmor(4, null);
				if (random.nextInt(3) == 0)
				{
					man.setCurrentItemOrArmor(0, new ItemStack(LOTRMod.mug));
				}
				else
				{
					man.setCurrentItemOrArmor(0, new ItemStack(LOTRMod.mugMead, 1, 1 + random.nextInt(3)));
				}
				man.isNPCPersistent = true;
				man.setHomeArea(i + xOffset, j + 1, k + zOffset, 16);
				world.spawnEntityInWorld(man);
			}
		}
		
		return flag;
	}
	
	private boolean generateFacingSouth(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			for (int k1 = k + 1; k1 <= k + 24; k1++)
			{
				for (int i1 = i - 5; i1 <= i + 5; i1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					if (world.getBlock(i1, j1, k1) != Blocks.grass)
					{
						return false;
					}
				}
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1D, j + 1D, k + 1D).offset(0D, 3D, 12D).expand(5D, 3D, 12D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int k1 = k + 1; k1 <= k + 24; k1++)
		{
			for (int i1 = i - 5; i1 <= i + 5; i1++)
			{
				for (int j1 = j; (j1 == j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
				{
					if (Math.abs(i1 - i) == 5 && (k1 == k + 1 || k1 == k + 23))
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta);
					}
					else if (Math.abs(i1 - i) == 5 || k1 == k + 1 || k1 == k + 24)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, plankBlock, plankMeta);
					}
					else
					{
						if (j1 == j)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, floorBlock, floorMeta);
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
				
				for (int j1 = j + 1; j1 <= j + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int k1 = k + 2; k1 <= k + 22; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 5, j1, k1, plankBlock, plankMeta);
				setBlockAndNotifyAdequately(world, i + 5, j1, k1, plankBlock, plankMeta);
			}
		}
		
		for (int k1 = k; k1 <= k + 24; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j + 4, k1, woodBlock, woodMeta | 8);
			setBlockAndNotifyAdequately(world, i + 5, j + 4, k1, woodBlock, woodMeta | 8);
			setBlockAndNotifyAdequately(world, i, j + 6, k1, woodBlock, woodMeta | 8);
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 1, plankBlock, plankMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k + 23, plankBlock, plankMeta);
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 1, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 23, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 1, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 23, woodBlock, woodMeta);
		}
		
		for (int k1 = k + 1; k1 <= k + 23; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 5, k1, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i - 3, j + 5, k1, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k1, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i - 1, j + 6, k1, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i, j + 7, k1, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i + 1, j + 6, k1, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i + 2, j + 6, k1, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i + 3, j + 5, k1, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i + 4, j + 5, k1, halfBlock, halfMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 5, k + 1, stairBlock, 1);
		setBlockAndNotifyAdequately(world, i - 3, j + 4, k + 1, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i - 3, j + 5, k + 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 2, j + 5, k + 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 5, k + 1, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 6, k + 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i, j + 5, k + 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 5, k + 1, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i + 2, j + 5, k + 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 3, j + 5, k + 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 3, j + 4, k + 1, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i + 5, j + 5, k + 1, stairBlock, 0);
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 23, plankBlock, plankMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 5, k + 23, stairBlock, 1);
		setBlockAndNotifyAdequately(world, i - 1, j + 6, k + 23, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 23, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 5, j + 5, k + 23, stairBlock, 0);
		
		for (int j1 = j + 5; j1 >= j + 1; j1--)
		{
			setBlockAndNotifyAdequately(world, i - (j - j1) - 5, j1, k + 23, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + (j - j1) + 5, j1, k + 23, woodBlock, woodMeta);
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 24, halfBlock, halfMeta);
		}
		
		for (int j1 = j + 3; j1 >= j + 1; j1--)
		{
			for (int i1 = i - (j - j1) - 3; i1 <= i + (j - j1) + 3; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 24, plankBlock, plankMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j, k + 1, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i, j + 1, k + 1, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 1, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i, j + 5, k + 5, LOTRMod.chandelier, 1);
		setBlockAndNotifyAdequately(world, i, j + 5, k + 11, LOTRMod.chandelier, 1);
		setBlockAndNotifyAdequately(world, i, j + 5, k + 17, LOTRMod.chandelier, 1);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k + 2, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i, j + 3, k + 2, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 3, j + 3, k + 2, Blocks.torch, 3);
		
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k + 2, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 2, halfBlock, halfMeta | 8);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 2, plankBlock, plankMeta);
		placeBarrel(world, random, i - 4, j + 2, k + 2, 3, LOTRMod.mugMead);
		placeBarrel(world, random, i - 3, j + 2, k + 2, 3, LOTRMod.mugMead);
		placeMug(world, random, i - 2, j + 2, k + 2, 2, LOTRMod.mugMead);
		
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k + 2, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 2, halfBlock, halfMeta | 8);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k + 2, plankBlock, plankMeta);
		placeBarrel(world, random, i + 4, j + 2, k + 2, 3, LOTRMod.mugMead);
		placeBarrel(world, random, i + 3, j + 2, k + 2, 3, LOTRMod.mugMead);
		placeMug(world, random, i + 2, j + 2, k + 2, 2, LOTRMod.mugMead);
		
		for (int k1 = k + 6; k1 <= k + 16; k1 += 2)
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k1, stairBlock, 1);
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, stairBlock, 0);
		}
		
		for (int k1 = k + 4; k1 <= k + 20; k1 += 2)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 3, k1, Blocks.torch, 1);
			setBlockAndNotifyAdequately(world, i + 4, j + 3, k1, Blocks.torch, 2);
		}
		
		for (int k1 = k + 5; k1 <= k + 21; k1 += 4)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 4, j1, k1, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i + 4, j1, k1, woodBlock, woodMeta);
			}
		}
		
		for (int k1 = k + 7; k1 <= k + 15; k1 += 4)
		{
			placeItemFrame(world, i - 5, j + 2, k1, 3, random);
			placeItemFrame(world, i + 5, j + 2, k1, 1, random);
		}
		
		for (int k1 = k + 5; k1 <= k + 17; k1++)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.wooden_slab, 10);
			}
			
			if ((k1 - k + 3) % 4 == 0)
			{
				setBlockAndNotifyAdequately(world, i - 1, j + 1, k1, Blocks.planks, 2);
				setBlockAndNotifyAdequately(world, i + 1, j + 1, k1, Blocks.planks, 2);
				setBlockAndNotifyAdequately(world, i, j + 2, k1, Blocks.torch, 5);
			}
		}
		
		for (int k1 = k + 5; k1 <= k + 17; k1++)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				if (world.isAirBlock(i1, j + 2, k1))
				{
					placePlate(world, i1, j + 2, k1, random, LOTRFoods.ROHAN);
					
					if (world.isAirBlock(i1, j + 2, k1) || (world.getBlock(i1, j + 2, k1) == LOTRMod.plateBlock && LOTRBlockPlate.getFoodItem(world, i1, j + 2, k1) == null))
					{
						placePlate(world, i1, j + 2, k1, random, LOTRFoods.ROHAN);
					}
				}
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 5; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 21, floorBlock, floorMeta);
			}
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 22, floorBlock, floorMeta);
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 22, floorBlock, floorMeta);
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 23, floorBlock, floorMeta);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 21, Blocks.iron_bars, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 21, Blocks.iron_bars, 0);
			setBlockAndNotifyAdequately(world, i1, j, k + 22, LOTRMod.hearth, 0);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 22, Blocks.fire, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 22, Blocks.air, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k + 21, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 3, j + 3, k + 21, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 5, k + 22, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 6, k + 21, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 21, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 3, j + 5, k + 22, plankBlock, plankMeta);
		
		setBlockAndNotifyAdequately(world, i, j + 3, k, Blocks.wall_sign, 2);
		TileEntity tileentity = world.getTileEntity(i, j + 3, k);
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[1] = meadHallName[0];
			sign.signText[2] = meadHallName[1];
		}
		
		return true;
	}
	
	private boolean generateFacingWest(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			for (int i1 = i - 1; i1 >= i - 24; i1--)
			{
				for (int k1 = k - 5; k1 <= k + 5; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					if (world.getBlock(i1, j1, k1) != Blocks.grass)
					{
						return false;
					}
				}
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1D, j + 1D, k + 1D).offset(-12D, 3D, 0D).expand(12D, 3D, 5D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int i1 = i - 1; i1 >= i - 24; i1--)
		{
			for (int k1 = k - 5; k1 <= k + 5; k1++)
			{
				for (int j1 = j; (j1 == j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
				{
					if (Math.abs(k1 - k) == 5 && (i1 == i - 1 || i1 == i - 23))
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta);
					}
					else if (Math.abs(k1 - k) == 5 || i1 == i - 1 || i1 == i - 24)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, plankBlock, plankMeta);
					}
					else
					{
						if (j1 == j)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, floorBlock, floorMeta);
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
				
				for (int j1 = j + 1; j1 <= j + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i - 2; i1 >= i - 22; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 5, plankBlock, plankMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k + 5, plankBlock, plankMeta);
			}
		}
		
		for (int i1 = i; i1 >= i - 24; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 5, woodBlock, woodMeta | 4);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 5, woodBlock, woodMeta | 4);
			setBlockAndNotifyAdequately(world, i1, j + 6, k, woodBlock, woodMeta | 4);
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 1, j1, k1, plankBlock, plankMeta);
				setBlockAndNotifyAdequately(world, i - 23, j1, k1, plankBlock, plankMeta);
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 1, j1, k - 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i - 23, j1, k - 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i - 1, j1, k + 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i - 23, j1, k + 5, woodBlock, woodMeta);
		}
		
		for (int i1 = i - 1; i1 >= i - 23; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 4, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 3, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 6, k - 2, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i1, j + 6, k - 1, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 7, k, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i1, j + 6, k + 1, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 6, k + 2, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 3, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 4, halfBlock, halfMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j + 5, k - 5, stairBlock, 3);
		setBlockAndNotifyAdequately(world, i - 1, j + 4, k - 3, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 5, k - 3, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 5, k - 2, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 5, k - 1, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 6, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 5, k, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 6, k + 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 5, k + 1, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 5, k + 2, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 5, k + 3, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 4, k + 3, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 5, k + 5, stairBlock, 2);
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 23, j + 5, k1, plankBlock, plankMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 23, j + 5, k - 5, stairBlock, 3);
		setBlockAndNotifyAdequately(world, i - 23, j + 6, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 23, j + 6, k + 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 23, j + 5, k + 5, stairBlock, 2);
		
		for (int j1 = j + 5; j1 >= j + 1; j1--)
		{
			setBlockAndNotifyAdequately(world, i - 23, j1, k - (j - j1) - 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i - 23, j1, k + (j - j1) + 5, woodBlock, woodMeta);
		}
		
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 24, j + 1, k1, halfBlock, halfMeta);
		}
		
		for (int j1 = j + 3; j1 >= j + 1; j1--)
		{
			for (int k1 = k - (j - j1) - 3; k1 <= k + (j - j1) + 3; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 24, j1, k1, plankBlock, plankMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j, k, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 1, k, Blocks.wooden_door, 2);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 1, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 1, Blocks.torch, 1);
		
		setBlockAndNotifyAdequately(world, i - 5, j + 5, k, LOTRMod.chandelier, 1);
		setBlockAndNotifyAdequately(world, i - 11, j + 5, k, LOTRMod.chandelier, 1);
		setBlockAndNotifyAdequately(world, i - 17, j + 5, k, LOTRMod.chandelier, 1);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 3, k - 3, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 2, j + 3, k, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 2, j + 3, k + 3, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 4, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 3, halfBlock, halfMeta | 8);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 2, plankBlock, plankMeta);
		placeBarrel(world, random, i - 2, j + 2, k - 4, 4, LOTRMod.mugMead);
		placeBarrel(world, random, i - 2, j + 2, k - 3, 4, LOTRMod.mugMead);
		placeMug(world, random, i - 2, j + 2, k - 2, 3, LOTRMod.mugMead);
		
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 4, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 3, halfBlock, halfMeta | 8);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 2, plankBlock, plankMeta);
		placeBarrel(world, random, i - 2, j + 2, k + 4, 4, LOTRMod.mugMead);
		placeBarrel(world, random, i - 2, j + 2, k + 3, 4, LOTRMod.mugMead);
		placeMug(world, random, i - 2, j + 2, k + 2, 3, LOTRMod.mugMead);
		
		for (int i1 = i - 6; i1 >= i - 16; i1 -= 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 3, stairBlock, 3);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, stairBlock, 2);
		}
		
		for (int i1 = i - 4; i1 >= i - 20; i1 -= 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 4, Blocks.torch, 3);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 4, Blocks.torch, 4);
		}
		
		for (int i1 = i - 5; i1 >= i - 21; i1 -= 4)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 4, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k + 4, woodBlock, woodMeta);
			}
		}
		
		for (int i1 = i - 7; i1 >= i - 15; i1 -= 4)
		{
			placeItemFrame(world, i1, j + 2, k - 5, 0, random);
			placeItemFrame(world, i1, j + 2, k + 5, 2, random);
		}
		
		for (int i1 = i - 5; i1 >= i - 17; i1--)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.wooden_slab, 10);
			}
			
			if ((i1 - i - 3) % 4 == 0)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k - 1, Blocks.planks, 2);
				setBlockAndNotifyAdequately(world, i1, j + 1, k + 1, Blocks.planks, 2);
				setBlockAndNotifyAdequately(world, i1, j + 2, k, Blocks.torch, 5);
			}
		}
		
		for (int i1 = i - 5; i1 >= i - 17; i1--)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				if (world.isAirBlock(i1, j + 2, k1))
				{
					placePlate(world, i1, j + 2, k1, random, LOTRFoods.ROHAN);
					
					if (world.isAirBlock(i1, j + 2, k1) || (world.getBlock(i1, j + 2, k1) == LOTRMod.plateBlock && LOTRBlockPlate.getFoodItem(world, i1, j + 2, k1) == null))
					{
						placePlate(world, i1, j + 2, k1, random, LOTRFoods.ROHAN);
					}
				}
			}
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 5; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 21, j1, k1, floorBlock, floorMeta);
			}
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 22, j1, k1, floorBlock, floorMeta);
			}
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 22, j + 5, k1, floorBlock, floorMeta);
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 23, j1, k1, floorBlock, floorMeta);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 21, j + 1, k1, Blocks.iron_bars, 0);
			setBlockAndNotifyAdequately(world, i - 21, j + 2, k1, Blocks.iron_bars, 0);
			setBlockAndNotifyAdequately(world, i - 22, j, k1, LOTRMod.hearth, 0);
			setBlockAndNotifyAdequately(world, i - 22, j + 1, k1, Blocks.fire, 0);
			setBlockAndNotifyAdequately(world, i - 22, j + 2, k1, Blocks.air, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 21, j + 3, k - 3, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 21, j + 3, k + 3, Blocks.torch, 1);
		
		setBlockAndNotifyAdequately(world, i - 22, j + 5, k - 3, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 21, j + 6, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 21, j + 6, k + 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 22, j + 5, k + 3, plankBlock, plankMeta);
		
		setBlockAndNotifyAdequately(world, i, j + 3, k, Blocks.wall_sign, 5);
		TileEntity tileentity = world.getTileEntity(i, j + 3, k);
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[1] = meadHallName[0];
			sign.signText[2] = meadHallName[1];
		}
		
		return true;
	}
	
	private boolean generateFacingNorth(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			for (int k1 = k - 1; k1 >= k - 24; k1--)
			{
				for (int i1 = i - 5; i1 <= i + 5; i1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					if (world.getBlock(i1, j1, k1) != Blocks.grass)
					{
						return false;
					}
				}
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1D, j + 1D, k + 1D).offset(0D, 3D, -12D).expand(5D, 3D, 12D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int k1 = k - 1; k1 >= k - 24; k1--)
		{
			for (int i1 = i - 5; i1 <= i + 5; i1++)
			{
				for (int j1 = j; (j1 == j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
				{
					if (Math.abs(i1 - i) == 5 && (k1 == k - 1 || k1 == k - 23))
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta);
					}
					else if (Math.abs(i1 - i) == 5 || k1 == k - 1 || k1 == k - 24)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, plankBlock, plankMeta);
					}
					else
					{
						if (j1 == j)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, floorBlock, floorMeta);
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
				
				for (int j1 = j + 1; j1 <= j + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int k1 = k - 2; k1 >= k - 22; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 5, j1, k1, plankBlock, plankMeta);
				setBlockAndNotifyAdequately(world, i + 5, j1, k1, plankBlock, plankMeta);
			}
		}
		
		for (int k1 = k; k1 >= k - 24; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 5, j + 4, k1, woodBlock, woodMeta | 8);
			setBlockAndNotifyAdequately(world, i + 5, j + 4, k1, woodBlock, woodMeta | 8);
			setBlockAndNotifyAdequately(world, i, j + 6, k1, woodBlock, woodMeta | 8);
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 1, plankBlock, plankMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k - 23, plankBlock, plankMeta);
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 1, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 23, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 1, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 23, woodBlock, woodMeta);
		}
		
		for (int k1 = k - 1; k1 >= k - 23; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 5, k1, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i - 3, j + 5, k1, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k1, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i - 1, j + 6, k1, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i, j + 7, k1, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i + 1, j + 6, k1, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i + 2, j + 6, k1, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i + 3, j + 5, k1, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i + 4, j + 5, k1, halfBlock, halfMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 5, k - 1, stairBlock, 1);
		setBlockAndNotifyAdequately(world, i - 3, j + 4, k - 1, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i - 3, j + 5, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 2, j + 5, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 5, k - 1, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 6, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i, j + 5, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 5, k - 1, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 3, j + 5, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 3, j + 4, k - 1, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i + 5, j + 5, k - 1, stairBlock, 0);
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 23, plankBlock, plankMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 5, k - 23, stairBlock, 1);
		setBlockAndNotifyAdequately(world, i - 1, j + 6, k - 23, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k - 23, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 5, j + 5, k - 23, stairBlock, 0);
		
		for (int j1 = j + 5; j1 >= j + 1; j1--)
		{
			setBlockAndNotifyAdequately(world, i - (j - j1) - 5, j1, k - 23, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + (j - j1) + 5, j1, k - 23, woodBlock, woodMeta);
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 24, halfBlock, halfMeta);
		}
		
		for (int j1 = j + 3; j1 >= j + 1; j1--)
		{
			for (int i1 = i - (j - j1) - 3; i1 <= i + (j - j1) + 3; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 24, plankBlock, plankMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j, k - 1, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i, j + 1, k - 1, Blocks.wooden_door, 3);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 1, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i - 1, j + 2, k, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k, Blocks.torch, 3);
		
		setBlockAndNotifyAdequately(world, i, j + 5, k - 5, LOTRMod.chandelier, 1);
		setBlockAndNotifyAdequately(world, i, j + 5, k - 11, LOTRMod.chandelier, 1);
		setBlockAndNotifyAdequately(world, i, j + 5, k - 17, LOTRMod.chandelier, 1);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k - 2, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i, j + 3, k - 2, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 3, j + 3, k - 2, Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 2, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 2, halfBlock, halfMeta | 8);
		setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 2, plankBlock, plankMeta);
		placeBarrel(world, random, i - 4, j + 2, k - 2, 2, LOTRMod.mugMead);
		placeBarrel(world, random, i - 3, j + 2, k - 2, 2, LOTRMod.mugMead);
		placeMug(world, random, i - 2, j + 2, k - 2, 0, LOTRMod.mugMead);
		
		setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 2, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 2, halfBlock, halfMeta | 8);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 2, plankBlock, plankMeta);
		placeBarrel(world, random, i + 4, j + 2, k - 2, 2, LOTRMod.mugMead);
		placeBarrel(world, random, i + 3, j + 2, k - 2, 2, LOTRMod.mugMead);
		placeMug(world, random, i + 2, j + 2, k - 2, 0, LOTRMod.mugMead);
		
		for (int k1 = k - 6; k1 >= k - 16; k1 -= 2)
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k1, stairBlock, 1);
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, stairBlock, 0);
		}
		
		for (int k1 = k - 4; k1 >= k - 20; k1 -= 2)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 3, k1, Blocks.torch, 1);
			setBlockAndNotifyAdequately(world, i + 4, j + 3, k1, Blocks.torch, 2);
		}
		
		for (int k1 = k - 5; k1 >= k - 21; k1 -= 4)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 4, j1, k1, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i + 4, j1, k1, woodBlock, woodMeta);
			}
		}
		
		for (int k1 = k - 7; k1 >= k - 15; k1 -= 4)
		{
			placeItemFrame(world, i - 5, j + 2, k1, 3, random);
			placeItemFrame(world, i + 5, j + 2, k1, 1, random);
		}
		
		for (int k1 = k - 5; k1 >= k - 17; k1--)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.wooden_slab, 10);
			}
			
			if ((k1 - k - 3) % 4 == 0)
			{
				setBlockAndNotifyAdequately(world, i - 1, j + 1, k1, Blocks.planks, 2);
				setBlockAndNotifyAdequately(world, i + 1, j + 1, k1, Blocks.planks, 2);
				setBlockAndNotifyAdequately(world, i, j + 2, k1, Blocks.torch, 5);
			}
		}
		
		for (int k1 = k - 5; k1 >= k - 17; k1--)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				if (world.isAirBlock(i1, j + 2, k1))
				{
					placePlate(world, i1, j + 2, k1, random, LOTRFoods.ROHAN);
					
					if (world.isAirBlock(i1, j + 2, k1) || (world.getBlock(i1, j + 2, k1) == LOTRMod.plateBlock && LOTRBlockPlate.getFoodItem(world, i1, j + 2, k1) == null))
					{
						placePlate(world, i1, j + 2, k1, random, LOTRFoods.ROHAN);
					}
				}
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 5; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 21, floorBlock, floorMeta);
			}
		}
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 22, floorBlock, floorMeta);
			}
		}
		
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 22, floorBlock, floorMeta);
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 23, floorBlock, floorMeta);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 21, Blocks.iron_bars, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 21, Blocks.iron_bars, 0);
			setBlockAndNotifyAdequately(world, i1, j, k - 22, LOTRMod.hearth, 0);
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 22, Blocks.fire, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 22, Blocks.air, 0);
		}
		
		setBlockAndNotifyAdequately(world, i - 3, j + 3, k - 21, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 3, j + 3, k - 21, Blocks.torch, 3);
		
		setBlockAndNotifyAdequately(world, i - 3, j + 5, k - 22, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i - 1, j + 6, k - 21, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k - 21, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 3, j + 5, k - 22, plankBlock, plankMeta);
		
		setBlockAndNotifyAdequately(world, i, j + 3, k, Blocks.wall_sign, 3);
		TileEntity tileentity = world.getTileEntity(i, j + 3, k);
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[1] = meadHallName[0];
			sign.signText[2] = meadHallName[1];
		}
		
		return true;
	}
	
	private boolean generateFacingEast(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			for (int i1 = i + 1; i1 <= i + 24; i1++)
			{
				for (int k1 = k - 5; k1 <= k + 5; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1) - 1;
					if (world.getBlock(i1, j1, k1) != Blocks.grass)
					{
						return false;
					}
				}
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1D, j + 1D, k + 1D).offset(12D, 3D, 0D).expand(12D, 3D, 5D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int i1 = i + 1; i1 <= i + 24; i1++)
		{
			for (int k1 = k - 5; k1 <= k + 5; k1++)
			{
				for (int j1 = j; (j1 == j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
				{
					if (Math.abs(k1 - k) == 5 && (i1 == i + 1 || i1 == i + 23))
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta);
					}
					else if (Math.abs(k1 - k) == 5 || i1 == i + 1 || i1 == i + 24)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, plankBlock, plankMeta);
					}
					else
					{
						if (j1 == j)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, floorBlock, floorMeta);
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
				
				for (int j1 = j + 1; j1 <= j + 5; j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
				}
			}
		}
		
		for (int i1 = i + 2; i1 <= i + 22; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 5, plankBlock, plankMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k + 5, plankBlock, plankMeta);
			}
		}
		
		for (int i1 = i; i1 <= i + 24; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 4, k - 5, woodBlock, woodMeta | 4);
			setBlockAndNotifyAdequately(world, i1, j + 4, k + 5, woodBlock, woodMeta | 4);
			setBlockAndNotifyAdequately(world, i1, j + 6, k, woodBlock, woodMeta | 4);
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 1, j1, k1, plankBlock, plankMeta);
				setBlockAndNotifyAdequately(world, i + 23, j1, k1, plankBlock, plankMeta);
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 1, j1, k - 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 23, j1, k - 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 1, j1, k + 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 23, j1, k + 5, woodBlock, woodMeta);
		}
		
		for (int i1 = i + 1; i1 <= i + 23; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 4, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i1, j + 5, k - 3, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 6, k - 2, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i1, j + 6, k - 1, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 7, k, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i1, j + 6, k + 1, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 6, k + 2, halfBlock, halfMeta);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 3, halfBlock, halfMeta | 8);
			setBlockAndNotifyAdequately(world, i1, j + 5, k + 4, halfBlock, halfMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 1, j + 5, k - 5, stairBlock, 3);
		setBlockAndNotifyAdequately(world, i + 1, j + 4, k - 3, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 5, k - 3, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 5, k - 2, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 5, k - 1, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 5, k, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 5, k + 1, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 5, k + 2, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 5, k + 3, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 4, k + 3, woodBlock, woodMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 5, k + 5, stairBlock, 2);
		
		for (int k1 = k - 3; k1 <= k + 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 23, j + 5, k1, plankBlock, plankMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 23, j + 5, k - 5, stairBlock, 3);
		setBlockAndNotifyAdequately(world, i + 23, j + 6, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 23, j + 6, k + 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 23, j + 5, k + 5, stairBlock, 2);
		
		for (int j1 = j + 5; j1 >= j + 1; j1--)
		{
			setBlockAndNotifyAdequately(world, i + 23, j1, k - (j - j1) - 5, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 23, j1, k + (j - j1) + 5, woodBlock, woodMeta);
		}
		
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 24, j + 1, k1, halfBlock, halfMeta);
		}
		
		for (int j1 = j + 3; j1 >= j + 1; j1--)
		{
			for (int k1 = k - (j - j1) - 3; k1 <= k + (j - j1) + 3; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 24, j1, k1, plankBlock, plankMeta);
			}
		}
		
		setBlockAndNotifyAdequately(world, i + 1, j, k, floorBlock, floorMeta);
		setBlockAndNotifyAdequately(world, i + 1, j + 1, k, Blocks.wooden_door, 0);
		setBlockAndNotifyAdequately(world, i + 1, j + 2, k, Blocks.wooden_door, 8);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 1, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 1, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i + 5, j + 5, k, LOTRMod.chandelier, 1);
		setBlockAndNotifyAdequately(world, i + 11, j + 5, k, LOTRMod.chandelier, 1);
		setBlockAndNotifyAdequately(world, i + 17, j + 5, k, LOTRMod.chandelier, 1);
		
		setBlockAndNotifyAdequately(world, i + 2, j + 3, k - 3, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 2, j + 3, k, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 2, j + 3, k + 3, Blocks.torch, 1);
		
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 4, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 3, halfBlock, halfMeta | 8);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 2, plankBlock, plankMeta);
		placeBarrel(world, random, i + 2, j + 2, k - 4, 5, LOTRMod.mugMead);
		placeBarrel(world, random, i + 2, j + 2, k - 3, 5, LOTRMod.mugMead);
		placeMug(world, random, i + 2, j + 2, k - 2, 1, LOTRMod.mugMead);
		
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k + 4, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k + 3, halfBlock, halfMeta | 8);
		setBlockAndNotifyAdequately(world, i + 2, j + 1, k + 2, plankBlock, plankMeta);
		placeBarrel(world, random, i + 2, j + 2, k + 4, 5, LOTRMod.mugMead);;
		placeBarrel(world, random, i + 2, j + 2, k + 3, 5, LOTRMod.mugMead);
		placeMug(world, random, i + 2, j + 2, k + 2, 1, LOTRMod.mugMead);
		
		for (int i1 = i + 6; i1 <= i + 16; i1 += 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 3, stairBlock, 3);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, stairBlock, 2);
		}
		
		for (int i1 = i + 4; i1 <= i + 20; i1 += 2)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 4, Blocks.torch, 3);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 4, Blocks.torch, 4);
		}
		
		for (int i1 = i + 5; i1 <= i + 21; i1 += 4)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 4, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k + 4, woodBlock, woodMeta);
			}
		}
		
		for (int i1 = i + 7; i1 <= i + 15; i1 += 4)
		{
			placeItemFrame(world, i1, j + 2, k - 5, 0, random);
			placeItemFrame(world, i1, j + 2, k + 5, 2, random);
		}
		
		for (int i1 = i + 5; i1 <= i + 17; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.wooden_slab, 10);
			}
			
			if ((i1 - i + 3) % 4 == 0)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k - 1, Blocks.planks, 2);
				setBlockAndNotifyAdequately(world, i1, j + 1, k + 1, Blocks.planks, 2);
				setBlockAndNotifyAdequately(world, i1, j + 2, k, Blocks.torch, 5);
			}
		}
		
		for (int i1 = i + 5; i1 <= i + 17; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				if (world.isAirBlock(i1, j + 2, k1))
				{
					placePlate(world, i1, j + 2, k1, random, LOTRFoods.ROHAN);
					
					if (world.isAirBlock(i1, j + 2, k1) || (world.getBlock(i1, j + 2, k1) == LOTRMod.plateBlock && LOTRBlockPlate.getFoodItem(world, i1, j + 2, k1) == null))
					{
						placePlate(world, i1, j + 2, k1, random, LOTRFoods.ROHAN);
					}
				}
			}
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 5; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 21, j1, k1, floorBlock, floorMeta);
			}
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 22, j1, k1, floorBlock, floorMeta);
			}
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 22, j + 5, k1, floorBlock, floorMeta);
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 23, j1, k1, floorBlock, floorMeta);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 21, j + 1, k1, Blocks.iron_bars, 0);
			setBlockAndNotifyAdequately(world, i + 21, j + 2, k1, Blocks.iron_bars, 0);
			setBlockAndNotifyAdequately(world, i + 22, j, k1, LOTRMod.hearth, 0);
			setBlockAndNotifyAdequately(world, i + 22, j + 1, k1, Blocks.fire, 0);
			setBlockAndNotifyAdequately(world, i + 22, j + 2, k1, Blocks.air, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 21, j + 3, k - 3, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 21, j + 3, k + 3, Blocks.torch, 2);
		
		setBlockAndNotifyAdequately(world, i + 22, j + 5, k - 3, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 21, j + 6, k - 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 21, j + 6, k + 1, plankBlock, plankMeta);
		setBlockAndNotifyAdequately(world, i + 22, j + 5, k + 3, plankBlock, plankMeta);
		
		setBlockAndNotifyAdequately(world, i, j + 3, k, Blocks.wall_sign, 4);
		TileEntity tileentity = world.getTileEntity(i, j + 3, k);
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[1] = meadHallName[0];
			sign.signText[2] = meadHallName[1];
		}
		
		return true;
	}
	
	private void placeItemFrame(World world, int i, int j, int k, int direction, Random random)
	{
		ItemStack item = null;
		int l = random.nextInt(6);
		switch (l)
		{
			case 0: case 1: case 2:
				item = new ItemStack(LOTRMod.mug);
				break;
			case 3:
				item = new ItemStack(Items.wooden_sword);
				break;
			case 4:
				item = new ItemStack(Items.bow);
				break;
			case 5:
				item = new ItemStack(Items.arrow);
				break;
		}
		spawnItemFrame(world, i, j, k, direction, item);
	}
}
