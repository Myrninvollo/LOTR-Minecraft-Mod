package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityDunlending;
import lotr.common.entity.npc.LOTREntityDunlendingArcher;
import lotr.common.entity.npc.LOTREntityDunlendingWarlord;
import lotr.common.entity.npc.LOTREntityDunlendingWarrior;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class LOTRWorldGenDunlandHillFort extends LOTRWorldGenStructureBase
{
	private Block plankBlock;
	private int plankMeta;
	
	private Block woodBlock;
	private int woodMeta;
	
	private Block slabBlock;
	private int slabMeta;
	
	private Block stairBlock;
	
	private Block floorBlock;
	private int floorMeta;
	
	public LOTRWorldGenDunlandHillFort(boolean flag)
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
		
		if (restrictions)
		{
			for (int i1 = i - 7; i1 <= i + 7; i1++)
			{
				for (int k1 = k - 7; k1 <= k + 7; k1++)
				{
					int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
					Block block = world.getBlock(i1, j1 - 1, k1);
					if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone)
					{
						return false;
					}
				}
			}
		}
		
		if (random.nextBoolean())
		{
			plankBlock = Blocks.planks;
			plankMeta = 0;
			woodBlock = Blocks.log;
			woodMeta = 0;
			slabBlock = Blocks.wooden_slab;
			slabMeta = 0;
			stairBlock = Blocks.oak_stairs;
		}
		else
		{
			plankBlock = Blocks.planks;
			plankMeta = 1;
			woodBlock = Blocks.log;
			woodMeta = 1;
			slabBlock = Blocks.wooden_slab;
			slabMeta = 1;
			stairBlock = Blocks.spruce_stairs;
		}
		
		Object[] obj = LOTRWorldGenDunlendingHouse.getRandomDunlandFloorBlock(random);
		floorBlock = (Block)obj[0];
		floorMeta = (Integer)obj[1];
		
		for (int i1 = i - 7; i1 <= i + 7; i1++)
		{
			for (int k1 = k - 7; k1 <= k + 7; k1++)
			{
				int i2 = Math.abs(i1 - i);
				int k2 = Math.abs(k1 - k);
				
				if (i2 == 7 && k2 == 7)
				{
					for (int j1 = j + 8; (j1 >= j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta);
						setGrassToDirt(world, i1, j1 - 1, k1);
					}
				}
				else if (i2 == 7 || k2 == 7)
				{
					for (int j1 = j + 5; (j1 >= j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, plankBlock, plankMeta);
						setGrassToDirt(world, i1, j1 - 1, k1);
					}
					
					if ((i2 == 7 && k2 % 2 == 1) || (k2 == 7 && i2 % 2 == 1))
					{
						setBlockAndNotifyAdequately(world, i1, j + 6, k1, plankBlock, plankMeta);
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j + 6, k1, slabBlock, slabMeta);
					}
				}
				else
				{
					for (int j1 = j + 6; j1 >= j + 1; j1--)
					{
						setAir(world, i1, j1, k1);
					}
					
					for (int j1 = j; (j1 >= j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, floorBlock, floorMeta);
						setGrassToDirt(world, i1, j1 - 1, k1);
					}
				}
				
				if ((i2 >= 5 && i2 <= 6) || (k2 >= 5 && k2 <= 6))
				{
					setBlockAndNotifyAdequately(world, i1, j + 4, k1, plankBlock, plankMeta);
				}
				
				if ((i2 == 5 && k2 <= 5) || (k2 == 5 && i2 <= 5))
				{
					setBlockAndNotifyAdequately(world, i1, j + 5, k1, Blocks.fence, 0);
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i - 5, j + 6, k - 5, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i - 5, j + 6, k + 5, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i + 5, j + 6, k - 5, Blocks.torch, 5);
		setBlockAndNotifyAdequately(world, i + 5, j + 6, k + 5, Blocks.torch, 5);
		
		placeBanner(world, i - 7, j + 9, k - 7, rotation, 5);
		placeBanner(world, i - 7, j + 9, k + 7, rotation, 5);
		placeBanner(world, i + 7, j + 9, k - 7, rotation, 5);
		placeBanner(world, i + 7, j + 9, k + 7, rotation, 5);
		
		if (rotation == 0)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k - 7, floorBlock, floorMeta);
				setAir(world, i1, j + 1, k - 7);
				setBlockAndNotifyAdequately(world, i1, j + 2, k - 7, Blocks.fence_gate, 0);
				setAir(world, i1, j + 3, k - 7);
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k - 7, slabBlock, slabMeta | 8);
				setBlockAndNotifyAdequately(world, i1, j + 4, k - 6, slabBlock, slabMeta | 8);
				
				setBlockAndNotifyAdequately(world, i1, j + 6, k - 7, woodBlock, woodMeta);
			}
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 2, j1, k - 7, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i + 2, j1, k - 7, woodBlock, woodMeta);
				
				setBlockAndNotifyAdequately(world, i - 3, j1 + 2, k - 7, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i + 3, j1 + 2, k - 7, woodBlock, woodMeta);
			}
			
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k - 7, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 7, woodBlock, woodMeta);
			
			setBlockAndNotifyAdequately(world, i, j + 5, k - 7, slabBlock, slabMeta | 8);
			
			setBlockAndNotifyAdequately(world, i, j + 6, k - 8, Blocks.skull, 2);
			setBlockAndNotifyAdequately(world, i, j + 6, k - 6, Blocks.torch, 3);
			
			for (int i1 = i - 3; i1 <= i - 2; i1++)
			{
				setAir(world, i1, j + 5, k - 5);
				
				int stairY = j + 4;
				for (int k1 = k - 4; k1 <= k - 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, stairY, k1, stairBlock, 3);
					stairY--;
				}
			}
			
			for (int k1 = k - 4; k1 <= k - 1; k1++)
			{
				for (int i1 = i + 1; i1 <= i + 4; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.grass, 0);
				}
			}
			
			setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 6, Blocks.torch, 3);
			setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 6, Blocks.torch, 3);
			
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 6, LOTRMod.dunlendingTable, 0);
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 6, Blocks.crafting_table, 0);
			
			setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 6, Blocks.furnace, 0);
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 6, Blocks.furnace, 0);
			
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 3, Blocks.anvil, 0);
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 2, Blocks.anvil, 0);
			
			placeArmorStand(world, i + 2, j + 1, k + 6, 0, new ItemStack[] {new ItemStack(LOTRMod.helmetWarg), new ItemStack(LOTRMod.bodyWarg), new ItemStack(LOTRMod.legsWarg), new ItemStack(LOTRMod.bootsWarg)});
			
			setBlockAndNotifyAdequately(world, i + 4, j + 1, k + 6, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i + 4, j + 1, k + 6, LOTRChestContents.DUNLENDING_HOUSE);
			
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 5, j1, k + 4, Blocks.fence, 0);
				setBlockAndNotifyAdequately(world, i + 5, j1, k + 2, Blocks.fence, 0);
				
				setBlockAndNotifyAdequately(world, i + 4, j1, k + 1, Blocks.fence, 0);
				setBlockAndNotifyAdequately(world, i + 2, j1, k + 1, Blocks.fence, 0);
			}
			
			setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 3, Blocks.wool, 12);
			setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 1, Blocks.wool, 12);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int i1 = i - 6; i1 <= i - 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k + 2, plankBlock, plankMeta);
				}
				
				for (int k1 = k + 3; k1 <= k + 6; k1++)
				{
					setBlockAndNotifyAdequately(world, i - 1, j1, k1, plankBlock, plankMeta);
				}
			}
			
			for (int i1 = i - 4; i1 <= i - 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4, k + 2, slabBlock, slabMeta);
			}
			
			for (int k1 = k + 3; k1 <= k + 4; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 1, j + 4, k1, slabBlock, slabMeta);
			}
			
			for (int k1 = k + 3; k1 <= k + 5; k1++)
			{
				for (int i1 = i - 5; i1 <= i - 2; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j + 4, k1, plankBlock, plankMeta);
					if (k1 == k + 3 || i1 == i - 2)
					{
						setBlockAndNotifyAdequately(world, i1, j + 5, k1, Blocks.fence, 0);
					}
					else
					{
						setAir(world, i1, j + 6, k1);
						setAir(world, i1, j + 5, k1);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i - 5, j + 6, k + 3, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k + 3, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k + 5, Blocks.torch, 5);
			
			setBlockAndNotifyAdequately(world, i - 4, j + 1, k + 2, Blocks.wooden_door, 1);
			setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 2, Blocks.wooden_door, 8);
			setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 1, Blocks.torch, 4);
			
			for (int i1 = i - 6; i1 <= i - 2; i1 += 4)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k + 4, Blocks.bed, 2);
				setBlockAndNotifyAdequately(world, i1, j + 1, k + 3, Blocks.bed, 10);
				
				setBlockAndNotifyAdequately(world, i1, j + 1, k + 5, Blocks.bed, 0);
				setBlockAndNotifyAdequately(world, i1, j + 1, k + 6, Blocks.bed, 8);
			}
			
			setBlockAndNotifyAdequately(world, i - 4, j + 1, k + 6, plankBlock, plankMeta);
			placeBarrel(world, random, i - 4, j + 2, k + 6, 2, LOTRFoods.DUNLENDING_DRINK.getRandomFood(random).getItem());
			
			setBlockAndNotifyAdequately(world, i - 6, j + 3, k + 3, Blocks.torch, 3);
			setBlockAndNotifyAdequately(world, i - 2, j + 3, k + 3, Blocks.torch, 3);
			setBlockAndNotifyAdequately(world, i - 6, j + 3, k + 6, Blocks.torch, 4);
			setBlockAndNotifyAdequately(world, i - 2, j + 3, k + 6, Blocks.torch, 4);
		}
		else if (rotation == 1)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 7, j, k1, floorBlock, floorMeta);
				setAir(world, i + 7, j + 1, k1);
				setBlockAndNotifyAdequately(world, i + 7, j + 2, k1, Blocks.fence_gate, 1);
				setAir(world, i + 7, j + 3, k1);
				
				setBlockAndNotifyAdequately(world, i + 7, j + 4, k1, slabBlock, slabMeta | 8);
				setBlockAndNotifyAdequately(world, i + 6, j + 4, k1, slabBlock, slabMeta | 8);
				
				setBlockAndNotifyAdequately(world, i + 7, j + 6, k1, woodBlock, woodMeta);
			}
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 7, j1, k - 2, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i + 7, j1, k + 2, woodBlock, woodMeta);
				
				setBlockAndNotifyAdequately(world, i + 7, j1 + 2, k - 3, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i + 7, j1 + 2, k + 3, woodBlock, woodMeta);
			}
			
			setBlockAndNotifyAdequately(world, i + 7, j + 5, k - 2, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 7, j + 5, k + 2, woodBlock, woodMeta);
			
			setBlockAndNotifyAdequately(world, i + 7, j + 5, k, slabBlock, slabMeta | 8);
			
			setBlockAndNotifyAdequately(world, i + 8, j + 6, k, Blocks.skull, 5);
			setBlockAndNotifyAdequately(world, i + 6, j + 6, k, Blocks.torch, 2);
			
			for (int k1 = k - 3; k1 <= k - 2; k1++)
			{
				setAir(world, i + 5, j + 5, k1);
				
				int stairY = j + 4;
				for (int i1 = i + 4; i1 >= i + 1; i1--)
				{
					setBlockAndNotifyAdequately(world, i1, stairY, k1, stairBlock, 0);
					stairY--;
				}
			}
			
			for (int i1 = i + 4; i1 >= i + 1; i1--)
			{
				for (int k1 = k + 1; k1 <= k + 4; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.grass, 0);
				}
			}
			
			setBlockAndNotifyAdequately(world, i + 6, j + 2, k - 2, Blocks.torch, 2);
			setBlockAndNotifyAdequately(world, i + 6, j + 2, k + 2, Blocks.torch, 2);
			
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 6, LOTRMod.dunlendingTable, 0);
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 5, Blocks.crafting_table, 0);
			
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 5, Blocks.furnace, 0);
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 6, Blocks.furnace, 0);
			
			setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 6, Blocks.anvil, 3);
			setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 6, Blocks.anvil, 3);
			
			placeArmorStand(world, i - 6, j + 1, k + 2, 1, new ItemStack[] {new ItemStack(LOTRMod.helmetWarg), new ItemStack(LOTRMod.bodyWarg), new ItemStack(LOTRMod.legsWarg), new ItemStack(LOTRMod.bootsWarg)});
			
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 4, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i - 6, j + 1, k + 4, LOTRChestContents.DUNLENDING_HOUSE);
			
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 4, j1, k + 5, Blocks.fence, 0);
				setBlockAndNotifyAdequately(world, i - 2, j1, k + 5, Blocks.fence, 0);
				
				setBlockAndNotifyAdequately(world, i - 1, j1, k + 4, Blocks.fence, 0);
				setBlockAndNotifyAdequately(world, i - 1, j1, k + 2, Blocks.fence, 0);
			}
			
			setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 5, Blocks.wool, 12);
			setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 3, Blocks.wool, 12);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int k1 = k - 6; k1 <= k - 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i - 2, j1, k1, plankBlock, plankMeta);
				}
				
				for (int i1 = i - 3; i1 >= i - 6; i1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 1, plankBlock, plankMeta);
				}
			}
			
			for (int k1 = k - 4; k1 <= k - 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 2, j + 4, k1, slabBlock, slabMeta);
			}
			
			for (int i1 = i - 3; i1 >= i - 4; i1--)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4, k - 1, slabBlock, slabMeta);
			}
			
			for (int i1 = i - 3; i1 >= i - 5; i1--)
			{
				for (int k1 = k - 5; k1 <= k - 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j + 4, k1, plankBlock, plankMeta);
					if (i1 == i - 3 || k1 == k - 2)
					{
						setBlockAndNotifyAdequately(world, i1, j + 5, k1, Blocks.fence, 0);
					}
					else
					{
						setAir(world, i1, j + 6, k1);
						setAir(world, i1, j + 5, k1);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i - 3, j + 6, k - 5, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i - 3, j + 6, k - 2, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i - 5, j + 6, k - 2, Blocks.torch, 5);
			
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 4, Blocks.wooden_door, 2);
			setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 4, Blocks.wooden_door, 8);
			setBlockAndNotifyAdequately(world, i - 1, j + 2, k - 2, Blocks.torch, 1);
			
			for (int k1 = k - 6; k1 <= k - 2; k1 += 4)
			{
				setBlockAndNotifyAdequately(world, i - 4, j + 1, k1, Blocks.bed, 3);
				setBlockAndNotifyAdequately(world, i - 3, j + 1, k1, Blocks.bed, 11);
				
				setBlockAndNotifyAdequately(world, i - 5, j + 1, k1, Blocks.bed, 1);
				setBlockAndNotifyAdequately(world, i - 6, j + 1, k1, Blocks.bed, 9);
			}
			
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 4, plankBlock, plankMeta);
			placeBarrel(world, random, i - 6, j + 2, k - 4, 5, LOTRFoods.DUNLENDING_DRINK.getRandomFood(random).getItem());
			
			setBlockAndNotifyAdequately(world, i - 3, j + 3, k - 6, Blocks.torch, 2);
			setBlockAndNotifyAdequately(world, i - 3, j + 3, k - 2, Blocks.torch, 2);
			setBlockAndNotifyAdequately(world, i - 6, j + 3, k - 6, Blocks.torch, 1);
			setBlockAndNotifyAdequately(world, i - 6, j + 3, k - 2, Blocks.torch, 1);
		}
		else if (rotation == 2)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k + 7, floorBlock, floorMeta);
				setAir(world, i1, j + 1, k + 7);
				setBlockAndNotifyAdequately(world, i1, j + 2, k + 7, Blocks.fence_gate, 2);
				setAir(world, i1, j + 3, k + 7);
				
				setBlockAndNotifyAdequately(world, i1, j + 4, k + 7, slabBlock, slabMeta | 8);
				setBlockAndNotifyAdequately(world, i1, j + 4, k + 6, slabBlock, slabMeta | 8);
				
				setBlockAndNotifyAdequately(world, i1, j + 6, k + 7, woodBlock, woodMeta);
			}
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 2, j1, k + 7, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i + 2, j1, k + 7, woodBlock, woodMeta);
				
				setBlockAndNotifyAdequately(world, i - 3, j1 + 2, k + 7, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i + 3, j1 + 2, k + 7, woodBlock, woodMeta);
			}
			
			setBlockAndNotifyAdequately(world, i - 2, j + 5, k + 7, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k + 7, woodBlock, woodMeta);
			
			setBlockAndNotifyAdequately(world, i, j + 5, k + 7, slabBlock, slabMeta | 8);
			
			setBlockAndNotifyAdequately(world, i, j + 6, k + 8, Blocks.skull, 3);
			setBlockAndNotifyAdequately(world, i, j + 6, k + 6, Blocks.torch, 4);
			
			for (int i1 = i - 3; i1 <= i - 2; i1++)
			{
				setAir(world, i1, j + 5, k + 5);
				
				int stairY = j + 4;
				for (int k1 = k + 4; k1 >= k + 1; k1--)
				{
					setBlockAndNotifyAdequately(world, i1, stairY, k1, stairBlock, 2);
					stairY--;
				}
			}
			
			for (int k1 = k + 4; k1 >= k + 1; k1--)
			{
				for (int i1 = i + 1; i1 <= i + 4; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.grass, 0);
				}
			}
			
			setBlockAndNotifyAdequately(world, i - 2, j + 2, k + 6, Blocks.torch, 4);
			setBlockAndNotifyAdequately(world, i + 2, j + 2, k + 6, Blocks.torch, 4);
			
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 6, LOTRMod.dunlendingTable, 0);
			setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 6, Blocks.crafting_table, 0);
			
			setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 6, Blocks.furnace, 0);
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 6, Blocks.furnace, 0);
			
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 3, Blocks.anvil, 0);
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 2, Blocks.anvil, 0);
			
			placeArmorStand(world, i + 2, j + 1, k - 6, 2, new ItemStack[] {new ItemStack(LOTRMod.helmetWarg), new ItemStack(LOTRMod.bodyWarg), new ItemStack(LOTRMod.legsWarg), new ItemStack(LOTRMod.bootsWarg)});
			
			setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 6, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i + 4, j + 1, k - 6, LOTRChestContents.DUNLENDING_HOUSE);
			
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 5, j1, k - 4, Blocks.fence, 0);
				setBlockAndNotifyAdequately(world, i + 5, j1, k - 2, Blocks.fence, 0);
				
				setBlockAndNotifyAdequately(world, i + 4, j1, k - 1, Blocks.fence, 0);
				setBlockAndNotifyAdequately(world, i + 2, j1, k - 1, Blocks.fence, 0);
			}
			
			setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 3, Blocks.wool, 12);
			setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 1, Blocks.wool, 12);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int i1 = i - 6; i1 <= i - 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 2, plankBlock, plankMeta);
				}
				
				for (int k1 = k - 3; k1 >= k - 6; k1--)
				{
					setBlockAndNotifyAdequately(world, i - 1, j1, k1, plankBlock, plankMeta);
				}
			}
			
			for (int i1 = i - 4; i1 <= i - 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4, k - 2, slabBlock, slabMeta);
			}
			
			for (int k1 = k - 3; k1 >= k - 4; k1--)
			{
				setBlockAndNotifyAdequately(world, i - 1, j + 4, k1, slabBlock, slabMeta);
			}
			
			for (int k1 = k - 3; k1 >= k - 5; k1--)
			{
				for (int i1 = i - 5; i1 <= i - 2; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j + 4, k1, plankBlock, plankMeta);
					if (k1 == k - 3 || i1 == i - 2)
					{
						setBlockAndNotifyAdequately(world, i1, j + 5, k1, Blocks.fence, 0);
					}
					else
					{
						setAir(world, i1, j + 6, k1);
						setAir(world, i1, j + 5, k1);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i - 5, j + 6, k - 3, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k - 3, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i - 2, j + 6, k - 5, Blocks.torch, 5);
			
			setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 2, Blocks.wooden_door, 3);
			setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 2, Blocks.wooden_door, 8);
			setBlockAndNotifyAdequately(world, i - 2, j + 2, k - 1, Blocks.torch, 3);
			
			for (int i1 = i - 6; i1 <= i - 2; i1 += 4)
			{
				setBlockAndNotifyAdequately(world, i1, j + 1, k - 4, Blocks.bed, 0);
				setBlockAndNotifyAdequately(world, i1, j + 1, k - 3, Blocks.bed, 8);
				
				setBlockAndNotifyAdequately(world, i1, j + 1, k - 5, Blocks.bed, 2);
				setBlockAndNotifyAdequately(world, i1, j + 1, k - 6, Blocks.bed, 10);
			}
			
			setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 6, plankBlock, plankMeta);
			placeBarrel(world, random, i - 4, j + 2, k - 6, 3, LOTRFoods.DUNLENDING_DRINK.getRandomFood(random).getItem());
			
			setBlockAndNotifyAdequately(world, i - 6, j + 3, k - 3, Blocks.torch, 4);
			setBlockAndNotifyAdequately(world, i - 2, j + 3, k - 3, Blocks.torch, 4);
			setBlockAndNotifyAdequately(world, i - 6, j + 3, k - 6, Blocks.torch, 3);
			setBlockAndNotifyAdequately(world, i - 2, j + 3, k - 6, Blocks.torch, 3);
		}
		else if (rotation == 3)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 7, j, k1, floorBlock, floorMeta);
				setAir(world, i - 7, j + 1, k1);
				setBlockAndNotifyAdequately(world, i - 7, j + 2, k1, Blocks.fence_gate, 3);
				setAir(world, i - 7, j + 3, k1);
				
				setBlockAndNotifyAdequately(world, i - 7, j + 4, k1, slabBlock, slabMeta | 8);
				setBlockAndNotifyAdequately(world, i - 6, j + 4, k1, slabBlock, slabMeta | 8);
				
				setBlockAndNotifyAdequately(world, i - 7, j + 6, k1, woodBlock, woodMeta);
			}
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 7, j1, k - 2, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i - 7, j1, k + 2, woodBlock, woodMeta);
				
				setBlockAndNotifyAdequately(world, i - 7, j1 + 2, k - 3, woodBlock, woodMeta);
				setBlockAndNotifyAdequately(world, i - 7, j1 + 2, k + 3, woodBlock, woodMeta);
			}
			
			setBlockAndNotifyAdequately(world, i - 7, j + 5, k - 2, woodBlock, woodMeta);
			setBlockAndNotifyAdequately(world, i - 7, j + 5, k + 2, woodBlock, woodMeta);
			
			setBlockAndNotifyAdequately(world, i - 7, j + 5, k, slabBlock, slabMeta | 8);
			
			setBlockAndNotifyAdequately(world, i - 8, j + 6, k, Blocks.skull, 4);
			setBlockAndNotifyAdequately(world, i - 6, j + 6, k, Blocks.torch, 1);
			
			for (int k1 = k - 3; k1 <= k - 2; k1++)
			{
				setAir(world, i - 5, j + 5, k1);
				
				int stairY = j + 4;
				for (int i1 = i - 4; i1 <= i - 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, stairY, k1, stairBlock, 1);
					stairY--;
				}
			}
			
			for (int i1 = i - 4; i1 <= i - 1; i1++)
			{
				for (int k1 = k + 1; k1 <= k + 4; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.grass, 0);
				}
			}
			
			setBlockAndNotifyAdequately(world, i - 6, j + 2, k - 2, Blocks.torch, 1);
			setBlockAndNotifyAdequately(world, i - 6, j + 2, k + 2, Blocks.torch, 1);
			
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 6, LOTRMod.dunlendingTable, 0);
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k + 5, Blocks.crafting_table, 0);
			
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 5, Blocks.furnace, 0);
			setBlockAndNotifyAdequately(world, i - 6, j + 1, k - 6, Blocks.furnace, 0);
			
			setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 6, Blocks.anvil, 3);
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k - 6, Blocks.anvil, 3);
			
			placeArmorStand(world, i + 6, j + 1, k + 2, 3, new ItemStack[] {new ItemStack(LOTRMod.helmetWarg), new ItemStack(LOTRMod.bodyWarg), new ItemStack(LOTRMod.legsWarg), new ItemStack(LOTRMod.bootsWarg)});
			
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k + 4, Blocks.chest, 0);
			LOTRChestContents.fillChest(world, random, i + 6, j + 1, k + 4, LOTRChestContents.DUNLENDING_HOUSE);
			
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				setBlockAndNotifyAdequately(world, i + 4, j1, k + 5, Blocks.fence, 0);
				setBlockAndNotifyAdequately(world, i + 2, j1, k + 5, Blocks.fence, 0);
				
				setBlockAndNotifyAdequately(world, i + 1, j1, k + 4, Blocks.fence, 0);
				setBlockAndNotifyAdequately(world, i + 1, j1, k + 2, Blocks.fence, 0);
			}
			
			setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 5, Blocks.wool, 12);
			setBlockAndNotifyAdequately(world, i + 1, j + 2, k + 3, Blocks.wool, 12);
			
			for (int j1 = j + 1; j1 <= j + 3; j1++)
			{
				for (int k1 = k - 6; k1 <= k - 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i + 2, j1, k1, plankBlock, plankMeta);
				}
				
				for (int i1 = i + 3; i1 <= i + 6; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 1, plankBlock, plankMeta);
				}
			}
			
			for (int k1 = k - 4; k1 <= k - 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i + 2, j + 4, k1, slabBlock, slabMeta);
			}
			
			for (int i1 = i + 3; i1 <= i + 4; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j + 4, k - 1, slabBlock, slabMeta);
			}
			
			for (int i1 = i + 3; i1 <= i + 5; i1++)
			{
				for (int k1 = k - 5; k1 <= k - 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j + 4, k1, plankBlock, plankMeta);
					if (i1 == i + 3 || k1 == k - 2)
					{
						setBlockAndNotifyAdequately(world, i1, j + 5, k1, Blocks.fence, 0);
					}
					else
					{
						setAir(world, i1, j + 6, k1);
						setAir(world, i1, j + 5, k1);
					}
				}
			}
			
			setBlockAndNotifyAdequately(world, i + 3, j + 6, k - 5, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i + 3, j + 6, k - 2, Blocks.torch, 5);
			setBlockAndNotifyAdequately(world, i + 5, j + 6, k - 2, Blocks.torch, 5);
			
			setBlockAndNotifyAdequately(world, i + 2, j + 1, k - 4, Blocks.wooden_door, 0);
			setBlockAndNotifyAdequately(world, i + 2, j + 2, k - 4, Blocks.wooden_door, 8);
			setBlockAndNotifyAdequately(world, i + 1, j + 2, k - 2, Blocks.torch, 2);
			
			for (int k1 = k - 6; k1 <= k - 2; k1 += 4)
			{
				setBlockAndNotifyAdequately(world, i + 4, j + 1, k1, Blocks.bed, 1);
				setBlockAndNotifyAdequately(world, i + 3, j + 1, k1, Blocks.bed, 9);
				
				setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, Blocks.bed, 3);
				setBlockAndNotifyAdequately(world, i + 6, j + 1, k1, Blocks.bed, 11);
			}
			
			setBlockAndNotifyAdequately(world, i + 6, j + 1, k - 4, plankBlock, plankMeta);
			placeBarrel(world, random, i + 6, j + 2, k - 4, 4, LOTRFoods.DUNLENDING_DRINK.getRandomFood(random).getItem());
			
			setBlockAndNotifyAdequately(world, i + 3, j + 3, k - 6, Blocks.torch, 1);
			setBlockAndNotifyAdequately(world, i + 3, j + 3, k - 2, Blocks.torch, 1);
			setBlockAndNotifyAdequately(world, i + 6, j + 3, k - 6, Blocks.torch, 2);
			setBlockAndNotifyAdequately(world, i + 6, j + 3, k - 2, Blocks.torch, 2);
		}
		
		LOTREntityDunlendingWarlord warlord = new LOTREntityDunlendingWarlord(world);
		warlord.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0F, 0F);
		warlord.onSpawnWithEgg(null);
		warlord.setHomeArea(i, j, k, 8);
		world.spawnEntityInWorld(warlord);
		
		for (int l = 0; l < 8; l++)
		{
			LOTREntityDunlending dunlending = world.rand.nextInt(3) == 0 ? new LOTREntityDunlendingArcher(world) : new LOTREntityDunlendingWarrior(world);
			dunlending.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0F, 0F);
			dunlending.onSpawnWithEgg(null);
			dunlending.setHomeArea(i, j, k, 16);
			dunlending.isNPCPersistent = true;
			world.spawnEntityInWorld(dunlending);
		}
		
		if (usingPlayer == null)
		{
			LOTRLevelData.dunlandHillFortLocations.add(new ChunkCoordinates(i, j, k));
		}
		
		return true;
	}
}

