package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTREntityNearHaradDrinksTrader;
import lotr.common.entity.npc.LOTREntityNearHaradFoodTrader;
import lotr.common.entity.npc.LOTREntityNearHaradMineralsTrader;
import lotr.common.entity.npc.LOTREntityNearHaradPlantsTrader;
import lotr.common.entity.npc.LOTRTradeEntry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRWorldGenNearHaradBazaar extends LOTRWorldGenStructureBase2
{
	public LOTRWorldGenNearHaradBazaar(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation)
	{
		j--;
		
		setRotationMode(rotation);
		
		switch (getRotationMode())
		{
			case 0:
				k += 1;
				i += 6;
				break;
			case 1:
				i -= 1;
				k += 6;
				break;
			case 2:
				k -= 1;
				i -= 6;
				break;
			case 3:
				i += 1;
				k -= 6;
				break;
		}
		
		setOrigin(i, j, k);
		
		if (restrictions)
		{
			for (int i1 = 0; i1 <= 12; i1++)
			{
				for (int k1 = 0; k1 <= 12; k1++)
				{
					int j1 = getTopBlock(world, i1, k1);
					Block block = getBlock(world, i1, j1 - 1, k1);
					if (block != Blocks.sand && block != Blocks.dirt && block != Blocks.grass)
					{
						return false;
					}
				}
			}
		}
		
		for (int i1 = 0; i1 <= 12; i1++)
		{
			for (int k1 = 0; k1 <= 12; k1++)
			{
				for (int j1 = 0; (j1 == 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; j1--)
				{
					setBlockAndMetadata(world, i1, j1, k1, Blocks.sandstone, 0);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
				
				for (int j1 = 1; j1 <= 6; j1++)
				{
					setAir(world, i1, j1, k1);
				}
			}
		}

		for (int i1 = 2; i1 <= 10; i1 += 8)
		{
			for (int k1 = 2; k1 <= 10; k1 += 8)
			{
				for (int i2 = -2; i2 <= 2; i2++)
				{
					for (int k2 = -2; k2 <= 2; k2++)
					{
						int i3 = Math.abs(i2);
						int k3 = Math.abs(k2);
						
						if (i3 == 2 && k3 == 2)
						{
							setBlockAndMetadata(world, i1 + i2, 1, k1 + k2, LOTRMod.wood3, 2);
							setBlockAndMetadata(world, i1 + i2, 2, k1 + k2, LOTRMod.fence, 14);
							setBlockAndMetadata(world, i1 + i2, 3, k1 + k2, LOTRMod.wood3, 2);
							setBlockAndMetadata(world, i1 + i2, 5, k1 + k2, LOTRMod.fence, 14);
							setBlockAndMetadata(world, i1 + i2, 6, k1 + k2, Blocks.torch, 5);
						}
						else if (i3 == 2 || k3 == 2)
						{
							setBlockAndMetadata(world, i1 + i2, 1, k1 + k2, LOTRMod.woodSlabSingle2, 14);
							setBlockAndMetadata(world, i1 + i2, 3, k1 + k2, LOTRMod.woodSlabSingle2, 14);
						}
					}
				}
			}
		}
		
		for (int i1 = 0; i1 <= 4; i1++)
		{
			setBlockAndMetadata(world, i1, 4, 0, Blocks.wool, 1);
			setBlockAndMetadata(world, i1, 4, 1, Blocks.wool, 12);
			setBlockAndMetadata(world, i1, 4, 2, Blocks.wool, 1);
			setBlockAndMetadata(world, i1, 4, 3, Blocks.wool, 12);
			setBlockAndMetadata(world, i1, 4, 4, Blocks.wool, 1);
		}
		
		for (int i1 = -2; i1 <= 2; i1++)
		{
			for (int k1 = -2; k1 <= 2; k1++)
			{
				int i2 = Math.abs(i1);
				int k2 = Math.abs(k1);
				
				if (i2 == 2 || k2 == 2)
				{
					setBlockAndMetadata(world, 10 + i1, 4, 2 + k1, Blocks.wool, 14);
				}
				else if (i2 == 1 || k2 == 1)
				{
					setBlockAndMetadata(world, 10 + i1, 4, 2 + k1, Blocks.wool, 15);
				}
				else
				{
					setBlockAndMetadata(world, 10 + i1, 4, 2 + k1, Blocks.wool, 14);
				}
			}
		}
		
		for (int i1 = 0; i1 <= 2; i1++)
		{
			setBlockAndMetadata(world, i1, 4, 8, Blocks.wool, 11);
			setBlockAndMetadata(world, i1, 4, 9, Blocks.wool, 11);
		}
		
		for (int i1 = 2; i1 <= 4; i1++)
		{
			setBlockAndMetadata(world, i1, 4, 11, Blocks.wool, 11);
			setBlockAndMetadata(world, i1, 4, 12, Blocks.wool, 11);
		}
		
		for (int k1 = 10; k1 <= 12; k1++)
		{
			setBlockAndMetadata(world, 0, 4, k1, Blocks.wool, 10);
			setBlockAndMetadata(world, 1, 4, k1, Blocks.wool, 10);
		}
		
		for (int k1 = 8; k1 <= 10; k1++)
		{
			setBlockAndMetadata(world, 3, 4, k1, Blocks.wool, 10);
			setBlockAndMetadata(world, 4, 4, k1, Blocks.wool, 10);
		}
		
		setBlockAndMetadata(world, 2, 4, 10, Blocks.wool, 2);
		
		for (int i1 = -2; i1 <= 2; i1++)
		{
			for (int k1 = -2; k1 <= 2; k1++)
			{
				if ((i1 % 2) == (k1 % 2))
				{
					setBlockAndMetadata(world, 10 + i1, 4, 10 + k1, Blocks.wool, 4);
				}
				else
				{
					setBlockAndMetadata(world, 10 + i1, 4, 10 + k1, Blocks.wool, 13);
				}
			}
		}
		
		setBlockAndMetadata(world, 4, 1, 2, Blocks.fence_gate, 1);
		setBlockAndMetadata(world, 4, 1, 10, Blocks.fence_gate, 1);
		setBlockAndMetadata(world, 8, 1, 2, Blocks.fence_gate, 3);
		setBlockAndMetadata(world, 8, 1, 10, Blocks.fence_gate, 3);
		
		placeBarrel(world, random, 3, 1, 1, 5, getRandomDrink(random));
		placeBarrel(world, random, 3, 1, 3, 5, getRandomDrink(random));
		
		placeMug_Harad(world, random, 3, 2, 0, random.nextInt(4), getRandomDrink(random));
		placeMug_Harad(world, random, 2, 2, 0, random.nextInt(4), getRandomDrink(random));
		placeMug_Harad(world, random, 1, 2, 0, random.nextInt(4), getRandomDrink(random));
		placeMug_Harad(world, random, 0, 2, 1, random.nextInt(4), getRandomDrink(random));
		placeMug_Harad(world, random, 0, 2, 2, random.nextInt(4), getRandomDrink(random));
		placeMug_Harad(world, random, 0, 2, 3, random.nextInt(4), getRandomDrink(random));
		placeMug_Harad(world, random, 1, 2, 4, random.nextInt(4), getRandomDrink(random));
		placeMug_Harad(world, random, 2, 2, 4, random.nextInt(4), getRandomDrink(random));
		placeMug_Harad(world, random, 3, 2, 4, random.nextInt(4), getRandomDrink(random));
		
		LOTREntityNPC drinksTrader = new LOTREntityNearHaradDrinksTrader(world);
		spawnNPCAndSetHome(drinksTrader, world, 2, 1, 2, 4);
		
		setBlockAndMetadata(world, 11, 1, 3, Blocks.furnace, 5);
		setBlockAndMetadata(world, 11, 1, 1, Blocks.anvil, 0);
		setBlockAndMetadata(world, 9, 1, 3, LOTRMod.oreCopper, 0);
		setBlockAndMetadata(world, 9, 1, 1, LOTRMod.oreTin, 0);
		
		LOTREntityNPC mineralsTrader = new LOTREntityNearHaradMineralsTrader(world);
		spawnNPCAndSetHome(mineralsTrader, world, 10, 1, 2, 4);
		
		placeFlowerPot_Harad(world, random, 3, 2, 8, getRandomPlant(random));
		placeFlowerPot_Harad(world, random, 2, 2, 8, getRandomPlant(random));
		placeFlowerPot_Harad(world, random, 1, 2, 8, getRandomPlant(random));
		placeFlowerPot_Harad(world, random, 0, 2, 9, getRandomPlant(random));
		placeFlowerPot_Harad(world, random, 0, 2, 10, getRandomPlant(random));
		placeFlowerPot_Harad(world, random, 0, 2, 11, getRandomPlant(random));
		placeFlowerPot_Harad(world, random, 1, 2, 12, getRandomPlant(random));
		placeFlowerPot_Harad(world, random, 2, 2, 12, getRandomPlant(random));
		placeFlowerPot_Harad(world, random, 3, 2, 12, getRandomPlant(random));
		
		LOTREntityNPC plantsTrader = new LOTREntityNearHaradPlantsTrader(world);
		spawnNPCAndSetHome(plantsTrader, world, 2, 1, 10, 4);
		
		placePlateWithCertainty_Harad(world, 11, 2, 8, random, LOTRFoods.NEAR_HARAD);
		placePlateWithCertainty_Harad(world, 10, 2, 8, random, LOTRFoods.NEAR_HARAD);
		placePlateWithCertainty_Harad(world, 9, 2, 8, random, LOTRFoods.NEAR_HARAD);
		placePlateWithCertainty_Harad(world, 12, 2, 9, random, LOTRFoods.NEAR_HARAD);
		placePlateWithCertainty_Harad(world, 12, 2, 10, random, LOTRFoods.NEAR_HARAD);
		placePlateWithCertainty_Harad(world, 12, 2, 11, random, LOTRFoods.NEAR_HARAD);
		placePlateWithCertainty_Harad(world, 9, 2, 12, random, LOTRFoods.NEAR_HARAD);
		placePlateWithCertainty_Harad(world, 10, 2, 12, random, LOTRFoods.NEAR_HARAD);
		placePlateWithCertainty_Harad(world, 11, 2, 12, random, LOTRFoods.NEAR_HARAD);
		
		LOTREntityNPC foodTrader = new LOTREntityNearHaradFoodTrader(world);
		spawnNPCAndSetHome(foodTrader, world, 10, 1, 10, 4);
		
		return true;
	}
	
	private void placeMug_Harad(World world, Random random, int i, int j, int k, int direction, Item drink)
	{
		if (random.nextInt(4) == 0)
		{
			placeMug(world, random, i, j, k, direction, drink);
		}
	}
	
	protected void placeFlowerPot_Harad(World world, Random random, int i, int j, int k, ItemStack itemstack)
	{
		if (random.nextInt(4) == 0)
		{
			placeFlowerPot(world, i, j, k, itemstack);
		}
	}
	
	private void placePlateWithCertainty_Harad(World world, int i, int j, int k, Random random, LOTRFoods foodList)
	{
		if (random.nextInt(4) == 0)
		{
			placePlateWithCertainty(world, i, j, k, random, foodList);
		}
	}
	
	private Item getRandomDrink(Random random)
	{
		return LOTRTradeEntry.NEAR_HARAD_DRINKS_TRADER_BUY[random.nextInt(LOTRTradeEntry.NEAR_HARAD_DRINKS_TRADER_BUY.length)].item.getItem();
	}
	
	private ItemStack getRandomPlant(Random random)
	{
		return LOTRTradeEntry.NEAR_HARAD_PLANTS_TRADER_BUY[random.nextInt(LOTRTradeEntry.NEAR_HARAD_PLANTS_TRADER_BUY.length)].item.copy();
	}
}
