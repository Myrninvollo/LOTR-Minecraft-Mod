package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityDwarf;
import lotr.common.entity.npc.LOTRNames;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class LOTRWorldGenDwarfHouse extends LOTRWorldGenStructureBase
{
	protected Block brickBlock;
	protected int brickMeta;
	
	protected Block brick2Block;
	protected int brick2Meta;
	
	protected Block pillarBlock;
	protected int pillarMeta;
	
	protected Block stairBlock;
	
	protected Block chandelierBlock;
	protected int chandelierMeta;
	
	protected Block tableBlock;
	
	public LOTRWorldGenDwarfHouse(boolean flag)
	{
		super(flag);
		
		brickBlock = LOTRMod.brick;
		brickMeta = 6;
		brick2Block = Blocks.stonebrick;
		brick2Meta = 0;
		pillarBlock = LOTRMod.pillar;
		pillarMeta = 0;
		stairBlock = LOTRMod.stairsDwarvenBrick;
		chandelierBlock = LOTRMod.chandelier;
		chandelierMeta = 8;
		tableBlock = LOTRMod.dwarvenTable;
	}
	
	protected LOTREntityDwarf createDwarf(World world)
	{
		return new LOTREntityDwarf(world);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		j--;
		
		if (restrictions)
		{
			for (int i1 = i - 6; i1 <= i + 6; i1++)
			{
				for (int j1 = j - 4; j1 <= j + 4; j1++)
				{
					for (int k1 = k - 6; k1 <= k + 6; k1++)
					{
						if (world.isAirBlock(i1, j1, k1))
						{
							return false;
						}
					}
				}
			}
		}
		
		int rotation = random.nextInt(4);
		
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
			switch (rotation)
			{
				case 0:
					k += 8;
					break;
				case 1:
					i -= 8;
					break;
				case 2:
					k -= 8;
					break;
				case 3:
					i += 8;
					break;
			}
		}
		
		if (restrictions)
		{
			if (rotation == 0)
			{
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					for (int j1 = j + 1; j1 <= j + 2; j1++)
					{
						boolean foundAir = false;
						for (int k1 = k - 8; k1 >= k - 14; k1--)
						{
							if (world.isAirBlock(i1, j1, k1))
							{
								foundAir = true;
								break;
							}
						}
						
						if (!foundAir)
						{
							return false;
						}
					}
				}
				
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					for (int j1 = j + 1; j1 <= j + 2; j1++)
					{
						airSearchLoop:
						for (int k1 = k - 8; k1 >= k - 14; k1--)
						{
							if (world.isAirBlock(i1, j1, k1))
							{
								break airSearchLoop;
							}
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
							if (j1 == j + 1)
							{
								setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.stone, 0);
							}
						}
					}
				}
			}
			
			if (rotation == 1)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					for (int j1 = j + 1; j1 <= j + 2; j1++)
					{
						boolean foundAir = false;
						for (int i1 = i + 8; i1 <= i + 14; i1++)
						{
							if (world.isAirBlock(i1, j1, k1))
							{
								foundAir = true;
								break;
							}
						}
						
						if (!foundAir)
						{
							return false;
						}
					}
				}
				
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					for (int j1 = j + 1; j1 <= j + 2; j1++)
					{
						airSearchLoop:
						for (int i1 = i + 8; i1 <= i + 14; i1++)
						{
							if (world.isAirBlock(i1, j1, k1))
							{
								break airSearchLoop;
							}
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
							if (j1 == j + 1)
							{
								setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.stone, 0);
							}
						}
					}
				}
			}
			
			if (rotation == 2)
			{
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					for (int j1 = j + 1; j1 <= j + 2; j1++)
					{
						boolean foundAir = false;
						for (int k1 = k + 8; k1 <= k + 14; k1++)
						{
							if (world.isAirBlock(i1, j1, k1))
							{
								foundAir = true;
								break;
							}
						}
						
						if (!foundAir)
						{
							return false;
						}
					}
				}
				
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					for (int j1 = j + 1; j1 <= j + 2; j1++)
					{
						airSearchLoop:
						for (int k1 = k + 8; k1 <= k + 14; k1++)
						{
							if (world.isAirBlock(i1, j1, k1))
							{
								break airSearchLoop;
							}
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
							if (j1 == j + 1)
							{
								setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.stone, 0);
							}
						}
					}
				}
			}
			
			if (rotation == 3)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					for (int j1 = j + 1; j1 <= j + 2; j1++)
					{
						boolean foundAir = false;
						for (int i1 = i - 8; i1 >= i - 14; i1--)
						{
							if (world.isAirBlock(i1, j1, k1))
							{
								foundAir = true;
								break;
							}
						}
						
						if (!foundAir)
						{
							return false;
						}
					}
				}
				
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					for (int j1 = j + 1; j1 <= j + 2; j1++)
					{
						airSearchLoop:
						for (int i1 = i - 8; i1 >= i - 14; i1--)
						{
							if (world.isAirBlock(i1, j1, k1))
							{
								break airSearchLoop;
							}
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
							if (j1 == j + 1)
							{
								setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.stone, 0);
							}
						}
					}
				}
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1D, j + 1D, k + 1D).expand(7D, 5D, 7D));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int i1 = i - 7; i1 <= i + 7; i1++)
		{
			for (int k1 = k - 7; k1 <= k + 7; k1++)
			{
				for (int j1 = j + 5; (j1 >= j - 5 || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
				{
					if (!LOTRMod.isOpaque(world, i1, j1, k1))
					{
						Block block = null;
						if (j1 >= j + 2)
						{
							if (world.getBlock(i1, j1 + 1, k1).isOpaqueCube())
							{
								block = Blocks.dirt;
							}
							else
							{
								block = Blocks.grass;
							}
						}
						else
						{
							block = Blocks.stone;
						}
						if (block != null)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, block, 0);
							if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
							{
								setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
							}
						}
					}
				}
			}
		}
		
		for (int i1 = i - 6; i1 <= i + 6; i1++)
		{
			for (int k1 = k - 6; k1 <= k + 6; k1++)
			{
				for (int j1 = j - 4; j1 <= j + 4; j1++)
				{
					if (Math.abs(i1 - i) == 6 || Math.abs(k1 - k) == 6)
					{
						if (j1 == j + 2)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 1);
						}
						else
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, brick2Block, brick2Meta);
						}
					}
					else
					{
						if (j1 == j || Math.abs(j1 - j) == 4)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, brick2Block, brick2Meta);
						}
						else
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
						}
					}
				}
			}
		}
		
		for (int j1 = j - 3; j1 <= j + 3; j1++)
		{
			if (j1 == j)
			{
				continue;
			}
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 5, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 5, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 5, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 5, pillarBlock, pillarMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 5, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 5, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 4, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 5, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 5, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 4, Blocks.torch, 4);
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 5, stairBlock, 7);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 5, stairBlock, 6);
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j + 3, k1, stairBlock, 5);
			setBlockAndNotifyAdequately(world, i + 5, j + 3, k1, stairBlock, 4);
		}
		
		int carpet = 0;
		int randomCarpet = random.nextInt(3);
		switch (randomCarpet)
		{
			case 0:
				carpet = 7;
				break;
			case 1:
				carpet = 12;
				break;
			case 2:
				carpet = 15;
				break;
		}

		switch (rotation)
		{
			case 0:
				generateFacingSouth(world, random, i, j, k, carpet);
				break;
			case 1:
				generateFacingWest(world, random, i, j, k, carpet);
				break;
			case 2:
				generateFacingNorth(world, random, i, j, k, carpet);
				break;
			case 3:
				generateFacingEast(world, random, i, j, k, carpet);
				break;
		}
		
		LOTREntityDwarf dwarfMale = createDwarf(world);
		dwarfMale.isNPCPersistent = true;
		dwarfMale.setLocationAndAngles(i + 0.5D, j + 2, k + 0.5D, 0F, 0F);
		dwarfMale.familyInfo.setNPCMale(true);
		dwarfMale.setDwarfName(LOTRNames.getRandomDwarfName(true, random));
		dwarfMale.setHomeArea(i, j + 1, k, 8);
		dwarfMale.onSpawnWithEgg(null);
		world.spawnEntityInWorld(dwarfMale);
		
		LOTREntityDwarf dwarfFemale = createDwarf(world);
		dwarfFemale.isNPCPersistent = true;
		dwarfFemale.setLocationAndAngles(i + 0.5D, j + 2, k + 0.5D, 0F, 0F);
		dwarfFemale.familyInfo.setNPCMale(false);
		dwarfFemale.setDwarfName(LOTRNames.getRandomDwarfName(false, random));
		dwarfFemale.setHomeArea(i, j + 1, k, 8);
		dwarfFemale.onSpawnWithEgg(null);
		world.spawnEntityInWorld(dwarfFemale);
		
		int maxChildren = dwarfMale.familyInfo.getRandomMaxChildren();
		
		dwarfMale.setCurrentItemOrArmor(4, new ItemStack(LOTRMod.dwarvenRing));
		dwarfMale.familyInfo.spouseUniqueID = dwarfFemale.getPersistentID();
		dwarfMale.familyInfo.setMaxBreedingDelay();
		dwarfMale.familyInfo.maxChildren = maxChildren;
		
		dwarfFemale.setCurrentItemOrArmor(4, new ItemStack(LOTRMod.dwarvenRing));
		dwarfFemale.familyInfo.spouseUniqueID = dwarfMale.getPersistentID();
		dwarfFemale.familyInfo.setMaxBreedingDelay();
		dwarfFemale.familyInfo.maxChildren = maxChildren;
		
		return true;
	}
	
	private void generateFacingSouth(World world, Random random, int i, int j, int k, int carpet)
	{
		for (int j1 = j + 2; j1 <= j + 3; j1++)
		{
			int i2 = 5 - (j1 - j);
			for (int i1 = i - i2; i1 <= i + i2; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 7, Blocks.stone, 0);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 7, Blocks.stone, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 1, j1, k - 6, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i, j1, k - 6, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i + 1, j1, k - 6, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 1, j1, k - 7, Blocks.stone, 0);
			setBlockAndNotifyAdequately(world, i, j1, k - 7, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i + 1, j1, k - 7, Blocks.stone, 0);
		}
		
		setBlockAndNotifyAdequately(world, i, j + 1, k - 7, LOTRMod.dwarvenDoor, 1);
		setBlockAndNotifyAdequately(world, i, j + 2, k - 7, LOTRMod.dwarvenDoor, 8);
		
		for (int k1 = k - 4; k1 <= k - 3; k1++)
		{
			for (int i1 = i - 3; i1 <= i + 3; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.carpet, carpet);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 3; k1++)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				if (Math.abs(i1 - i) == 1 && (k1 == k - 1 || k1 == k + 3))
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.planks, 1);
				}
				else
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.wooden_slab, 9);
				}
				placePlate(world, i1, j + 2, k1, random, LOTRFoods.DWARF);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 3, k, chandelierBlock, chandelierMeta);
		setBlockAndNotifyAdequately(world, i, j + 3, k + 2, chandelierBlock, chandelierMeta);
		
		for (int k1 = k; k1 <= k + 2; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k1, Blocks.spruce_stairs, 1);
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, Blocks.spruce_stairs, 0);
		}
		
		for (int k1 = k + 4; k1 <= k + 6; k1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				for (int i1 = i - 2; i1 <= i + 2; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, brickBlock, brickMeta);
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j1, k + 4, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 2, j1, k + 4, pillarBlock, pillarMeta);
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 4, Blocks.iron_bars, 0);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 4, Blocks.iron_bars, 0);
			
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 5, LOTRMod.hearth, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k + 5, Blocks.fire, 0);
			setBlockAndNotifyAdequately(world, i1, j + 3, k + 5, Blocks.air, 0);
		}
		
		for (int k1 = k - 2; k1 <= k + 1; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i + 5, j, k1, Blocks.air, 0);
			int height = 1 - (k1 - k);
			for (int j1 = j - 3; j1 < j - 3 + height; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 5, j1, k1, brickBlock, brickMeta);
				setBlockAndNotifyAdequately(world, i + 5, j1, k1, brickBlock, brickMeta);
			}
			setBlockAndNotifyAdequately(world, i - 5, j - 3 + height, k1, stairBlock, 3);
			setBlockAndNotifyAdequately(world, i + 5, j - 3 + height, k1, stairBlock, 3);
		}
		
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			for (int j1 = j - 3; j1 <= j - 1; j1++)
			{
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 1);
				}
			}
		}
		
		for (int j1 = j - 3; j1 <= j - 1; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j1, k - 5, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 2, j1, k + 5, pillarBlock, pillarMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j - 2, k + 4, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 2, j - 2, k + 4, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i - 2, j - 2, k - 4, Blocks.torch, 3);
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 2, j - 1, k1, stairBlock, 4);
		}
		
		for (int i1 = i - 4; i1 <= i - 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - 3, k - 5, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 2, k - 6, Blocks.planks, 1);
			placeBarrel(world, random, i1, j - 2, k - 5, 3, LOTRFoods.DWARF_DRINK.getRandomFood(random).getItem());
			setBlockAndNotifyAdequately(world, i1, j - 1, k - 5, stairBlock, 7);
			
			setBlockAndNotifyAdequately(world, i1, j - 3, k + 5, Blocks.furnace, 0);
			world.setBlockMetadataWithNotify(i1, j - 3, k + 5, 2, 3);
			setBlockAndNotifyAdequately(world, i1, j - 2, k + 6, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 1, k + 5, stairBlock, 6);
		}
		
		for (int k1 = k - 4; k1 <= k - 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 6, j - 2, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 5, j - 2, k1, Blocks.chest, 5);
			LOTRChestContents.fillChest(world, random, i - 5, j - 2, k1, LOTRChestContents.DWARF_HOUSE_LARDER);
			setBlockAndNotifyAdequately(world, i - 5, j - 1, k1, stairBlock, 5);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j - 3, k - 1, Blocks.cauldron, 3);
		setBlockAndNotifyAdequately(world, i - 2, j - 3, k, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 2, j - 3, k + 1, tableBlock, 0);
		
		for (int j1 = j - 3; j1 <= j - 1; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 2, j1, k - 5, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 2, j1, k + 5, pillarBlock, pillarMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 5, j - 2, k + 4, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 2, j - 2, k + 4, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 5, j - 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 2, j - 2, k - 4, Blocks.torch, 3);
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 2, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 2, j - 1, k1, stairBlock, 5);
		}
		
		for (int i1 = i + 3; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - 3, k - 5, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 2, k - 6, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 1, k - 5, stairBlock, 7);
			
			setBlockAndNotifyAdequately(world, i1, j - 3, k + 5, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 2, k + 6, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 1, k + 5, stairBlock, 6);
			
			for (int k1 = k - 2; k1 <= k + 4; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j - 3, k1, Blocks.carpet, carpet);
			}
			
			setBlockAndNotifyAdequately(world, i1, j - 3, k - 3, LOTRMod.dwarvenBed, 2);
			setBlockAndNotifyAdequately(world, i1, j - 3, k - 4, LOTRMod.dwarvenBed, 10);
		}
		
		for (int k1 = k - 4; k1 <= k - 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 5, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 6, j - 2, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 5, j - 1, k1, stairBlock, 4);
		}
	}
	
	private void generateFacingWest(World world, Random random, int i, int j, int k, int carpet)
	{
		for (int j1 = j + 2; j1 <= j + 3; j1++)
		{
			int k2 = 5 - (j1 - j);
			for (int k1 = k - k2; k1 <= k + k2; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 7, j1, k1, Blocks.stone, 0);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 7, j, k1, Blocks.stone, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 6, j1, k - 1, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 6, j1, k, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i + 6, j1, k + 1, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 7, j1, k - 1, Blocks.stone, 0);
			setBlockAndNotifyAdequately(world, i + 7, j1, k, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i + 7, j1, k + 1, Blocks.stone, 0);
		}
		
		setBlockAndNotifyAdequately(world, i + 7, j + 1, k, LOTRMod.dwarvenDoor, 2);
		setBlockAndNotifyAdequately(world, i + 7, j + 2, k, LOTRMod.dwarvenDoor, 8);
		
		for (int i1 = i + 4; i1 >= i + 3; i1--)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.carpet, carpet);
			}
		}
		
		for (int i1 = i + 1; i1 >= i - 3; i1--)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				if (Math.abs(k1 - k) == 1 && (i1 == i + 1 || i1 == i - 3))
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.planks, 1);
				}
				else
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.wooden_slab, 9);
				}
				placePlate(world, i1, j + 2, k1, random, LOTRFoods.DWARF);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 3, k, chandelierBlock, chandelierMeta);
		setBlockAndNotifyAdequately(world, i - 2, j + 3, k, chandelierBlock, chandelierMeta);
		
		for (int i1 = i; i1 >= i - 2; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 3, Blocks.spruce_stairs, 3);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, Blocks.spruce_stairs, 2);
		}
		
		for (int i1 = i - 4; i1 >= i - 6; i1--)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, brickBlock, brickMeta);
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j1, k - 2, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 4, j1, k + 2, pillarBlock, pillarMeta);
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 4, j + 2, k1, Blocks.iron_bars, 0);
			setBlockAndNotifyAdequately(world, i - 4, j + 3, k1, Blocks.iron_bars, 0);
			
			setBlockAndNotifyAdequately(world, i - 5, j + 1, k1, LOTRMod.hearth, 0);
			setBlockAndNotifyAdequately(world, i - 5, j + 2, k1, Blocks.fire, 0);
			setBlockAndNotifyAdequately(world, i - 5, j + 3, k1, Blocks.air, 0);
		}
		
		for (int i1 = i + 2; i1 >= i - 1; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 5, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j, k + 5, Blocks.air, 0);
			int height = 1 + (i1 - i);
			for (int j1 = j - 3; j1 < j - 3 + height; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 5, brickBlock, brickMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k + 5, brickBlock, brickMeta);
			}
			setBlockAndNotifyAdequately(world, i1, j - 3 + height, k - 5, stairBlock, 0);
			setBlockAndNotifyAdequately(world, i1, j - 3 + height, k + 5, stairBlock, 0);
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			for (int j1 = j - 3; j1 <= j - 1; j1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 1);
				}
			}
		}
		
		for (int j1 = j - 3; j1 <= j - 1; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 2, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 2, pillarBlock, pillarMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 4, j - 2, k - 5, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 4, j - 2, k - 2, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + 4, j - 2, k - 2, Blocks.torch, 2);
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - 3, k - 2, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 1, k - 2, stairBlock, 6);
		}
		
		for (int k1 = k - 4; k1 <= k - 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 5, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 6, j - 2, k1, Blocks.planks, 1);
			placeBarrel(world, random, i + 5, j - 2, k1, 4, LOTRFoods.DWARF_DRINK.getRandomFood(random).getItem());
			setBlockAndNotifyAdequately(world, i + 5, j - 1, k1, stairBlock, 4);
			
			setBlockAndNotifyAdequately(world, i - 5, j - 3, k1, Blocks.furnace, 0);
			world.setBlockMetadataWithNotify(i + 5, j - 3, k1, 5, 3);
			setBlockAndNotifyAdequately(world, i - 6, j - 2, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 5, j - 1, k1, stairBlock, 5);
		}
		
		for (int i1 = i + 4; i1 >= i + 3; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j - 3, k - 5, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 2, k - 6, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 2, k - 5, Blocks.chest, 3);
			LOTRChestContents.fillChest(world, random, i1, j - 2, k - 5, LOTRChestContents.DWARF_HOUSE_LARDER);
			setBlockAndNotifyAdequately(world, i1, j - 1, k - 5, stairBlock, 7);
		}
		
		setBlockAndNotifyAdequately(world, i + 1, j - 3, k - 2, Blocks.cauldron, 3);
		setBlockAndNotifyAdequately(world, i, j - 3, k - 2, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 1, j - 3, k - 2, tableBlock, 0);
		
		for (int j1 = j - 3; j1 <= j - 1; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 2, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 2, pillarBlock, pillarMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 4, j - 2, k + 5, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 4, j - 2, k + 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 4, j - 2, k + 5, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 4, j - 2, k + 2, Blocks.torch, 1);
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - 3, k + 2, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 1, k + 2, stairBlock, 7);
		}
		
		for (int k1 = k + 3; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 6, j - 2, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 5, j - 1, k1, stairBlock, 5);
			
			setBlockAndNotifyAdequately(world, i + 5, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 6, j - 2, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 5, j - 1, k1, stairBlock, 4);
			
			for (int i1 = i + 2; i1 >= i - 4; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j - 3, k1, Blocks.carpet, carpet);
			}
			
			setBlockAndNotifyAdequately(world, i + 3, j - 3, k1, LOTRMod.dwarvenBed, 3);
			setBlockAndNotifyAdequately(world, i + 4, j - 3, k1, LOTRMod.dwarvenBed, 11);
		}
		
		for (int i1 = i + 4; i1 >= i + 3; i1--)
		{
			setBlockAndNotifyAdequately(world, i1, j - 3, k + 5, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 2, k + 6, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 1, k + 5, stairBlock, 6);
		}
	}
	
	private void generateFacingNorth(World world, Random random, int i, int j, int k, int carpet)
	{
		for (int j1 = j + 2; j1 <= j + 3; j1++)
		{
			int i2 = 5 - (j1 - j);
			for (int i1 = i - i2; i1 <= i + i2; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k + 7, Blocks.stone, 0);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k + 7, Blocks.stone, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 1, j1, k + 6, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i, j1, k + 6, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i + 1, j1, k + 6, pillarBlock, pillarMeta);
		}
		
		setBlockAndNotifyAdequately(world, i, j + 1, k + 7, LOTRMod.dwarvenDoor, 3);
		setBlockAndNotifyAdequately(world, i, j + 2, k + 7, LOTRMod.dwarvenDoor, 8);
		
		for (int k1 = k + 4; k1 >= k + 3; k1--)
		{
			for (int i1 = i - 3; i1 <= i + 3; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.carpet, carpet);
			}
		}
		
		for (int k1 = k + 1; k1 >= k - 3; k1--)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				if (Math.abs(i1 - i) == 1 && (k1 == k + 1 || k1 == k - 3))
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.planks, 1);
				}
				else
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.wooden_slab, 9);
				}
				placePlate(world, i1, j + 2, k1, random, LOTRFoods.DWARF);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 3, k, chandelierBlock, chandelierMeta);
		setBlockAndNotifyAdequately(world, i, j + 3, k - 2, chandelierBlock, chandelierMeta);
		
		for (int k1 = k; k1 >= k - 2; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k1, Blocks.spruce_stairs, 1);
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, Blocks.spruce_stairs, 0);
		}
		
		for (int k1 = k - 4; k1 >= k - 6; k1--)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				for (int i1 = i - 2; i1 <= i + 2; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, brickBlock, brickMeta);
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j1, k - 4, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 2, j1, k - 4, pillarBlock, pillarMeta);
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 4, Blocks.iron_bars, 0);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 4, Blocks.iron_bars, 0);
			
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 5, LOTRMod.hearth, 0);
			setBlockAndNotifyAdequately(world, i1, j + 2, k - 5, Blocks.fire, 0);
			setBlockAndNotifyAdequately(world, i1, j + 3, k - 5, Blocks.air, 0);
		}
		
		for (int k1 = k + 2; k1 >= k - 1; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 5, j, k1, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i + 5, j, k1, Blocks.air, 0);
			int height = 1 + (k1 - k);
			for (int j1 = j - 3; j1 < j - 3 + height; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 5, j1, k1, brickBlock, brickMeta);
				setBlockAndNotifyAdequately(world, i + 5, j1, k1, brickBlock, brickMeta);
			}
			setBlockAndNotifyAdequately(world, i - 5, j - 3 + height, k1, stairBlock, 2);
			setBlockAndNotifyAdequately(world, i + 5, j - 3 + height, k1, stairBlock, 2);
		}
		
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			for (int j1 = j - 3; j1 <= j - 1; j1++)
			{
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 1);
				}
			}
		}
		
		for (int j1 = j - 3; j1 <= j - 1; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j1, k - 5, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 2, j1, k + 5, pillarBlock, pillarMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j - 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 2, j - 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i - 2, j - 2, k + 4, Blocks.torch, 4);
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 2, j - 1, k1, stairBlock, 4);
		}
		
		for (int i1 = i - 4; i1 <= i - 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - 3, k + 5, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 2, k + 6, Blocks.planks, 1);
			placeBarrel(world, random, i1, j - 2, k + 5, 2, LOTRFoods.DWARF_DRINK.getRandomFood(random).getItem());
			setBlockAndNotifyAdequately(world, i1, j - 1, k + 5, stairBlock, 6);
			
			setBlockAndNotifyAdequately(world, i1, j - 3, k - 5, Blocks.furnace, 0);
			world.setBlockMetadataWithNotify(i1, j - 3, k - 5, 3, 3);
			setBlockAndNotifyAdequately(world, i1, j - 2, k - 6, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 1, k - 5, stairBlock, 7);
		}
		
		for (int k1 = k + 4; k1 >= k + 3; k1--)
		{
			setBlockAndNotifyAdequately(world, i - 5, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 6, j - 2, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 5, j - 2, k1, Blocks.chest, 5);
			LOTRChestContents.fillChest(world, random, i - 5, j - 2, k1, LOTRChestContents.DWARF_HOUSE_LARDER);
			setBlockAndNotifyAdequately(world, i - 5, j - 1, k1, stairBlock, 5);
		}
		
		setBlockAndNotifyAdequately(world, i - 2, j - 3, k + 1, Blocks.cauldron, 3);
		setBlockAndNotifyAdequately(world, i - 2, j - 3, k, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i - 2, j - 3, k - 1, tableBlock, 0);
		
		for (int j1 = j - 3; j1 <= j - 1; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 2, j1, k - 5, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 2, j1, k + 5, pillarBlock, pillarMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 5, j - 2, k + 4, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 2, j - 2, k + 4, Blocks.torch, 4);
		setBlockAndNotifyAdequately(world, i + 5, j - 2, k - 4, Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i + 2, j - 2, k - 4, Blocks.torch, 3);
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 2, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 2, j - 1, k1, stairBlock, 5);
		}
		
		for (int i1 = i + 3; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - 3, k - 5, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 2, k - 6, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 1, k - 5, stairBlock, 7);
			
			setBlockAndNotifyAdequately(world, i1, j - 3, k + 5, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 2, k + 6, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 1, k + 5, stairBlock, 6);
			
			for (int k1 = k + 2; k1 >= k - 4; k1--)
			{
				setBlockAndNotifyAdequately(world, i1, j - 3, k1, Blocks.carpet, carpet);
			}
			
			setBlockAndNotifyAdequately(world, i1, j - 3, k + 3, LOTRMod.dwarvenBed, 0);
			setBlockAndNotifyAdequately(world, i1, j - 3, k + 4, LOTRMod.dwarvenBed, 8);
		}
		
		for (int k1 = k + 4; k1 >= k + 3; k1--)
		{
			setBlockAndNotifyAdequately(world, i + 5, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 6, j - 2, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 5, j - 1, k1, stairBlock, 4);
		}
	}
	
	private void generateFacingEast(World world, Random random, int i, int j, int k, int carpet)
	{
		for (int j1 = j + 2; j1 <= j + 3; j1++)
		{
			int k2 = 5 - (j1 - j);
			for (int k1 = k - k2; k1 <= k + k2; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 7, j1, k1, Blocks.stone, 0);
			}
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 7, j, k1, Blocks.stone, 0);
		}
		
		for (int j1 = j + 1; j1 <= j + 2; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 6, j1, k - 1, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i - 6, j1, k, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i - 6, j1, k + 1, pillarBlock, pillarMeta);
		}
		
		setBlockAndNotifyAdequately(world, i - 7, j + 1, k, LOTRMod.dwarvenDoor, 0);
		setBlockAndNotifyAdequately(world, i - 7, j + 2, k, LOTRMod.dwarvenDoor, 8);
		
		for (int i1 = i - 4; i1 <= i - 3; i1++)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.carpet, carpet);
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 3; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				if (Math.abs(k1 - k) == 1 && (i1 == i - 1 || i1 == i + 3))
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.planks, 1);
				}
				else
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k1, Blocks.wooden_slab, 9);
				}
				placePlate(world, i1, j + 2, k1, random, LOTRFoods.DWARF);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 3, k, chandelierBlock, chandelierMeta);
		setBlockAndNotifyAdequately(world, i + 2, j + 3, k, chandelierBlock, chandelierMeta);
		
		for (int i1 = i; i1 <= i + 2; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j + 1, k - 3, Blocks.spruce_stairs, 3);
			setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, Blocks.spruce_stairs, 2);
		}
		
		for (int i1 = i + 4; i1 <= i + 6; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 4; j1++)
			{
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, brickBlock, brickMeta);
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= j + 3; j1++)
		{
			setBlockAndNotifyAdequately(world, i + 4, j1, k - 2, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 4, j1, k + 2, pillarBlock, pillarMeta);
		}
		
		for (int k1 = k - 1; k1 <= k + 1; k1++)
		{
			setBlockAndNotifyAdequately(world, i + 4, j + 2, k1, Blocks.iron_bars, 0);
			setBlockAndNotifyAdequately(world, i + 4, j + 3, k1, Blocks.iron_bars, 0);
			
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, LOTRMod.hearth, 0);
			setBlockAndNotifyAdequately(world, i + 5, j + 2, k1, Blocks.fire, 0);
			setBlockAndNotifyAdequately(world, i + 5, j + 3, k1, Blocks.air, 0);
		}
		
		for (int i1 = i - 2; i1 <= i + 1; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j, k - 5, Blocks.air, 0);
			setBlockAndNotifyAdequately(world, i1, j, k + 5, Blocks.air, 0);
			int height = 1 - (i1 - i);
			for (int j1 = j - 3; j1 < j - 3 + height; j1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 5, brickBlock, brickMeta);
				setBlockAndNotifyAdequately(world, i1, j1, k + 5, brickBlock, brickMeta);
			}
			setBlockAndNotifyAdequately(world, i1, j - 3 + height, k - 5, stairBlock, 1);
			setBlockAndNotifyAdequately(world, i1, j - 3 + height, k + 5, stairBlock, 1);
		}
		
		for (int i1 = i - 5; i1 <= i + 5; i1++)
		{
			for (int j1 = j - 3; j1 <= j - 1; j1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 1);
				}
			}
		}
		
		for (int j1 = j - 3; j1 <= j - 1; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 2, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 2, pillarBlock, pillarMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 4, j - 2, k - 5, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 4, j - 2, k - 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 4, j - 2, k - 2, Blocks.torch, 1);
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - 3, k - 2, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 1, k - 2, stairBlock, 6);
		}
		
		for (int k1 = k - 4; k1 <= k - 3; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 6, j - 2, k1, Blocks.planks, 1);
			placeBarrel(world, random, i - 5, j - 2, k1, 5, LOTRFoods.DWARF_DRINK.getRandomFood(random).getItem());
			setBlockAndNotifyAdequately(world, i - 5, j - 1, k1, stairBlock, 5);
			
			setBlockAndNotifyAdequately(world, i + 5, j - 3, k1, Blocks.furnace, 0);
			world.setBlockMetadataWithNotify(i + 5, j - 3, k1, 4, 3);
			setBlockAndNotifyAdequately(world, i + 6, j - 2, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 5, j - 1, k1, stairBlock, 4);
		}
		
		for (int i1 = i - 4; i1 <= i - 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - 3, k - 5, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 2, k - 6, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 2, k - 5, Blocks.chest, 3);
			LOTRChestContents.fillChest(world, random, i1, j - 2, k - 5, LOTRChestContents.DWARF_HOUSE_LARDER);
			setBlockAndNotifyAdequately(world, i1, j - 1, k - 5, stairBlock, 7);
		}
		
		setBlockAndNotifyAdequately(world, i - 1, j - 3, k - 2, Blocks.cauldron, 3);
		setBlockAndNotifyAdequately(world, i, j - 3, k - 2, Blocks.crafting_table, 0);
		setBlockAndNotifyAdequately(world, i + 1, j - 3, k - 2, tableBlock, 0);
		
		for (int j1 = j - 3; j1 <= j - 1; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 2, pillarBlock, pillarMeta);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 2, pillarBlock, pillarMeta);
		}
		
		setBlockAndNotifyAdequately(world, i + 4, j - 2, k + 5, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i + 4, j - 2, k + 2, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i - 4, j - 2, k + 5, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i - 4, j - 2, k + 2, Blocks.torch, 1);
		
		for (int i1 = i - 4; i1 <= i + 4; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - 3, k + 2, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 1, k + 2, stairBlock, 7);
		}
		
		for (int k1 = k + 3; k1 <= k + 4; k1++)
		{
			setBlockAndNotifyAdequately(world, i - 5, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 6, j - 2, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i - 5, j - 1, k1, stairBlock, 5);
			
			setBlockAndNotifyAdequately(world, i + 5, j - 3, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 6, j - 2, k1, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i + 5, j - 1, k1, stairBlock, 4);
			
			for (int i1 = i - 2; i1 <= i + 4; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j - 3, k1, Blocks.carpet, carpet);
			}
			
			setBlockAndNotifyAdequately(world, i - 3, j - 3, k1, LOTRMod.dwarvenBed, 1);
			setBlockAndNotifyAdequately(world, i - 4, j - 3, k1, LOTRMod.dwarvenBed, 9);
		}
		
		for (int i1 = i - 4; i1 <= i - 3; i1++)
		{
			setBlockAndNotifyAdequately(world, i1, j - 3, k + 5, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 2, k + 6, Blocks.planks, 1);
			setBlockAndNotifyAdequately(world, i1, j - 1, k + 5, stairBlock, 6);
		}
	}
}
