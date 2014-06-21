package lotr.common.entity.npc;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class LOTRTradeEntry
{
	public static LOTRTradeEntry[] HOBBIT_BARTENDER_BUY;
	public static LOTRTradeEntry[] HOBBIT_BARTENDER_SELL;
	
	public static LOTRTradeEntry[] ORC_TRADER_BUY;
	public static LOTRTradeEntry[] ORC_TRADER_SELL;
	
	public static LOTRTradeEntry[] GONDOR_BLACKSMITH_BUY;
	public static LOTRTradeEntry[] GONDOR_BLACKSMITH_SELL;
	
	public static LOTRTradeEntry[] ELVEN_TRADER_BUY;
	public static LOTRTradeEntry[] ELVEN_TRADER_SELL;
	
	public static LOTRTradeEntry[] URUK_HAI_TRADER_BUY;
	public static LOTRTradeEntry[] URUK_HAI_TRADER_SELL;
	
	public static LOTRTradeEntry[] DWARF_MINER_BUY;
	public static LOTRTradeEntry[] DWARF_MINER_SELL;
	
	public static LOTRTradeEntry[] ROHAN_BLACKSMITH_BUY;
	public static LOTRTradeEntry[] ROHAN_BLACKSMITH_SELL;
	
	public static LOTRTradeEntry[] DUNLENDING_BARTENDER_BUY;
	public static LOTRTradeEntry[] DUNLENDING_BARTENDER_SELL;
	
	public static LOTRTradeEntry[] ROHAN_MEADHOST_BUY;
	public static LOTRTradeEntry[] ROHAN_MEADHOST_SELL;
	
	public static LOTRTradeEntry[] ANGMAR_ORC_SCAVENGER_BUY;
	public static LOTRTradeEntry[] ANGMAR_ORC_SCAVENGER_SELL;
	
	public static LOTRTradeEntry[] HOBBIT_ORCHARDER_BUY;
	public static LOTRTradeEntry[] HOBBIT_ORCHARDER_SELL;
	
	public static LOTRTradeEntry[] HOBBIT_FARMER_BUY;
	public static LOTRTradeEntry[] HOBBIT_FARMER_SELL;
	
	public static LOTRTradeEntry[] BLUE_DWARF_MINER_BUY;
	public static LOTRTradeEntry[] BLUE_DWARF_MINER_SELL;
	
	public static LOTRTradeEntry[] NEAR_HARAD_DRINKS_TRADER_BUY;
	public static LOTRTradeEntry[] NEAR_HARAD_DRINKS_TRADER_SELL;
	
	public static LOTRTradeEntry[] NEAR_HARAD_MINERALS_TRADER_BUY;
	public static LOTRTradeEntry[] NEAR_HARAD_MINERALS_TRADER_SELL;
	
	public static LOTRTradeEntry[] NEAR_HARAD_PLANTS_TRADER_BUY;
	public static LOTRTradeEntry[] NEAR_HARAD_PLANTS_TRADER_SELL;
	
	public static LOTRTradeEntry[] NEAR_HARAD_FOOD_TRADER_BUY;
	public static LOTRTradeEntry[] NEAR_HARAD_FOOD_TRADER_SELL;
	
	public static LOTRTradeEntry[] BLUE_DWARF_MERCHANT_BUY;
	public static LOTRTradeEntry[] BLUE_DWARF_MERCHANT_SELL;
	
	public ItemStack item;
	public int cost;
	
	public LOTRTradeEntry(ItemStack itemstack, int i)
	{
		item = itemstack;
		cost = i;
	}
	
	public static LOTRTradeEntry[] getRandomTrades(LOTRTradeEntry[] tradePool, Random random, boolean canEnchant)
	{
		int numberTrades = 3 + random.nextInt(3) + random.nextInt(3) + random.nextInt(3);
		
		if (numberTrades > tradePool.length)
		{
			numberTrades = tradePool.length;
		}
		
		LOTRTradeEntry[] tempTrades = new LOTRTradeEntry[tradePool.length];
		System.arraycopy(tradePool, 0, tempTrades, 0, tradePool.length);
		for	(int i = tempTrades.length - 1; i > 0; i--)
		{
			int rand = (int)(random.nextDouble() * i);
			LOTRTradeEntry tempTrade = tempTrades[i];
			tempTrades[i] = tempTrades[rand];
			tempTrades[rand] = tempTrade;
		}
		
		LOTRTradeEntry[] trades = new LOTRTradeEntry[numberTrades];
		for (int i = 0; i < trades.length; i++)
		{
			ItemStack tradeItem = tempTrades[i].item.copy();

			boolean enchanted = false;
			if (canEnchant && random.nextInt(3) == 0 && tradeItem.isItemEnchantable() && tradeItem.getItem().getItemEnchantability() > 0)
			{
				int enchantability = EnchantmentHelper.calcItemStackEnchantability(random, 1, 8, tradeItem);
				EnchantmentHelper.addRandomEnchantment(random, tradeItem, enchantability);
				enchanted = true;
			}
			
			int tradeCost = tempTrades[i].cost;
			tradeCost = Math.round(tradeCost * (0.75F + (random.nextFloat() * 0.5F)));
			if (tradeCost < 1)
			{
				tradeCost = 1;
			}
			if (enchanted)
			{
				tradeCost = Math.round((float)tradeCost * 1.5F);
			}
			
			trades[i] = new LOTRTradeEntry(tradeItem, tradeCost);
		}
		
		return trades;
	}
	
	public static int[] getSellPrice_TradeCount_SellAmount(ItemStack itemstack, LOTREntityNPC trader)
	{
		int[] ret = new int[3];
		
		for (LOTRTradeEntry trade : trader.traderNPCInfo.getSellTrades())
		{
			if (trade.item.getItem() == itemstack.getItem() && itemstack.stackSize >= trade.item.stackSize && (trade.item.getItemDamage() == OreDictionary.WILDCARD_VALUE || trade.item.getItemDamage() == itemstack.getItemDamage()))
			{
				ret[0] = trade.cost;
				ret[1] = itemstack.stackSize / trade.item.stackSize;
				ret[2] = ret[1] * trade.item.stackSize;
				break;
			}
		}
		
		return ret;
	}
	
	static
	{
		HOBBIT_BARTENDER_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.cooked_beef), 7),
			new LOTRTradeEntry(new ItemStack(Items.cooked_porkchop), 7),
			new LOTRTradeEntry(new ItemStack(Items.cooked_chicken), 6),
			new LOTRTradeEntry(new ItemStack(Items.bread), 5),
			new LOTRTradeEntry(new ItemStack(LOTRMod.pipeweed, 6), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.hobbitPipe), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mug), 2),
			new LOTRTradeEntry(new ItemStack(Items.baked_potato, 2), 7),
			new LOTRTradeEntry(new ItemStack(Items.cooked_fished), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugChocolate), 4),
			new LOTRTradeEntry(new ItemStack(LOTRMod.gammon), 7),
			new LOTRTradeEntry(new ItemStack(LOTRMod.rabbitCooked), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.rabbitStew), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.plate), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.hobbitRing), 14),
			new LOTRTradeEntry(new ItemStack(LOTRMod.hobbitOven), 20),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugAle, 1, 1), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCider, 1, 1), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugPerry, 1, 1), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.hobbitPancake), 10)
		};
		
		HOBBIT_BARTENDER_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.beef), 3),
			new LOTRTradeEntry(new ItemStack(Items.porkchop), 3),
			new LOTRTradeEntry(new ItemStack(Items.chicken), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.rabbitRaw), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.clayMug), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.clayPlate), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.pipeweedLeaf), 1),
			new LOTRTradeEntry(new ItemStack(Items.potato, 2), 1),
			new LOTRTradeEntry(new ItemStack(Items.fish), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.clover, 1, 1), 40),
			new LOTRTradeEntry(new ItemStack(Items.water_bucket), 4),
			new LOTRTradeEntry(new ItemStack(Items.coal, 2, OreDictionary.WILDCARD_VALUE), 1)
		};
		
		ORC_TRADER_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(LOTRMod.helmetOrc), 20),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bodyOrc), 30),
			new LOTRTradeEntry(new ItemStack(LOTRMod.legsOrc), 26),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bootsOrc), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.scimitarOrc), 14),
			new LOTRTradeEntry(new ItemStack(LOTRMod.spearOrc), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.daggerOrc), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.daggerOrcPoisoned), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.battleaxeOrc), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.orcBomb), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugOrcDraught, 1, 2), 7),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugOrcDraught, 1, 3), 14),
			new LOTRTradeEntry(new ItemStack(LOTRMod.wargFur), 4),
			new LOTRTradeEntry(new ItemStack(LOTRMod.orcBow), 16),
			new LOTRTradeEntry(new ItemStack(Items.arrow, 4), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.pickaxeOrc), 14),
			new LOTRTradeEntry(new ItemStack(LOTRMod.axeOrc), 14),
			new LOTRTradeEntry(new ItemStack(LOTRMod.hammerOrc), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.maggotyBread), 4),
			new LOTRTradeEntry(new ItemStack(LOTRMod.helmetWarg), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bodyWarg), 20),
			new LOTRTradeEntry(new ItemStack(LOTRMod.legsWarg), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bootsWarg), 12)
		};
		
		ORC_TRADER_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(LOTRMod.orcSteel), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.nauriteGem), 2),
			new LOTRTradeEntry(new ItemStack(Items.string), 2),
			new LOTRTradeEntry(new ItemStack(Items.feather), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mug), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.morgulShroom), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.elfBone), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.dwarfBone), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.hobbitBone), 1),
			new LOTRTradeEntry(new ItemStack(Items.bone), 1)
		};
		
		GONDOR_BLACKSMITH_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(LOTRMod.swordGondor), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.daggerGondor), 9),
			new LOTRTradeEntry(new ItemStack(LOTRMod.spearGondor), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.helmetGondor), 20),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bodyGondor), 32),
			new LOTRTradeEntry(new ItemStack(LOTRMod.legsGondor), 26),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bootsGondor), 17),
			new LOTRTradeEntry(new ItemStack(LOTRMod.blacksmithHammer), 18),
			new LOTRTradeEntry(new ItemStack(Blocks.iron_bars, 8), 20),
			new LOTRTradeEntry(new ItemStack(LOTRMod.hammerGondor), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.crossbowBolt, 4), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.ironCrossbow), 15)
		};
		
		GONDOR_BLACKSMITH_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.iron_ingot), 3),
			new LOTRTradeEntry(new ItemStack(Items.coal, 2, OreDictionary.WILDCARD_VALUE), 1),
			new LOTRTradeEntry(new ItemStack(Items.gold_ingot), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.copper), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.tin), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bronze), 3)
		};
		
		ELVEN_TRADER_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(LOTRMod.swordElven), 21),
			new LOTRTradeEntry(new ItemStack(LOTRMod.daggerElven), 13),
			new LOTRTradeEntry(new ItemStack(LOTRMod.spearElven), 21),
			new LOTRTradeEntry(new ItemStack(LOTRMod.axeElven), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.elvenBow), 21),
			new LOTRTradeEntry(new ItemStack(LOTRMod.swordMallorn), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mallornBow), 17),
			new LOTRTradeEntry(new ItemStack(LOTRMod.helmetElven), 25),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bodyElven), 36),
			new LOTRTradeEntry(new ItemStack(LOTRMod.legsElven), 30),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bootsElven), 22),
			new LOTRTradeEntry(new ItemStack(LOTRMod.planks, 4, 1), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.elanor), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.niphredil), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mallornTorch, 4), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.lembas), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugMiruvor, 1, 2), 7),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugMiruvor, 1, 3), 14),
			new LOTRTradeEntry(new ItemStack(Items.arrow, 4), 2)
		};
		
		ELVEN_TRADER_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.iron_ingot), 3),
			new LOTRTradeEntry(new ItemStack(Items.wheat, 3), 1),
			new LOTRTradeEntry(new ItemStack(Items.string), 2),
			new LOTRTradeEntry(new ItemStack(Items.feather), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mug), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.orcBone), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.wargBone), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.trollBone), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mithrilNugget), 8)
		};
		
		URUK_HAI_TRADER_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(LOTRMod.helmetUruk), 24),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bodyUruk), 36),
			new LOTRTradeEntry(new ItemStack(LOTRMod.legsUruk), 30),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bootsUruk), 22),
			new LOTRTradeEntry(new ItemStack(LOTRMod.scimitarUruk), 17),
			new LOTRTradeEntry(new ItemStack(LOTRMod.spearUruk), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.daggerUruk), 9),
			new LOTRTradeEntry(new ItemStack(LOTRMod.daggerUrukPoisoned), 11),
			new LOTRTradeEntry(new ItemStack(LOTRMod.battleaxeUruk), 20),
			new LOTRTradeEntry(new ItemStack(LOTRMod.orcBomb), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugOrcDraught, 1, 2), 7),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugOrcDraught, 1, 3), 14),
			new LOTRTradeEntry(new ItemStack(LOTRMod.wargFur), 4),
			new LOTRTradeEntry(new ItemStack(LOTRMod.urukCrossbow), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.crossbowBolt, 4), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.pickaxeUruk), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.axeUruk), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.hammerUruk), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.maggotyBread), 4),
			new LOTRTradeEntry(new ItemStack(LOTRMod.helmetWarg), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bodyWarg), 20),
			new LOTRTradeEntry(new ItemStack(LOTRMod.legsWarg), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bootsWarg), 12)
		};
		
		URUK_HAI_TRADER_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(LOTRMod.urukSteel), 3),
			new LOTRTradeEntry(new ItemStack(Items.iron_ingot), 3),
			new LOTRTradeEntry(new ItemStack(Items.coal, 2, OreDictionary.WILDCARD_VALUE), 1),
			new LOTRTradeEntry(new ItemStack(Items.leather), 2),
			new LOTRTradeEntry(new ItemStack(Items.string), 2),
			new LOTRTradeEntry(new ItemStack(Items.feather), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.elfBone), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.dwarfBone), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.hobbitBone), 1),
			new LOTRTradeEntry(new ItemStack(Items.bone), 1)
		};
		
		DWARF_MINER_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.coal, 2), 4),
			new LOTRTradeEntry(new ItemStack(Blocks.iron_ore), 7),
			new LOTRTradeEntry(new ItemStack(LOTRMod.oreCopper), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.oreTin), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.oreSilver), 12),
			new LOTRTradeEntry(new ItemStack(Blocks.gold_ore), 20),
			new LOTRTradeEntry(new ItemStack(Items.dye, 1, 4), 1),
			new LOTRTradeEntry(new ItemStack(Items.glowstone_dust, 4), 3),
			new LOTRTradeEntry(new ItemStack(Blocks.cobblestone, 8), 1),
			new LOTRTradeEntry(new ItemStack(Items.lava_bucket), 20),
			new LOTRTradeEntry(new ItemStack(Items.flint), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.sulfur), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.saltpeter), 6)
		};
		
		DWARF_MINER_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.cooked_beef), 3),
			new LOTRTradeEntry(new ItemStack(Items.cooked_porkchop), 3),
			new LOTRTradeEntry(new ItemStack(Items.cooked_chicken), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.gammon), 3),
			new LOTRTradeEntry(new ItemStack(Items.cooked_fished), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.rabbitCooked), 3),
			new LOTRTradeEntry(new ItemStack(Items.bread), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugAle, 1, OreDictionary.WILDCARD_VALUE), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugMead, 1, OreDictionary.WILDCARD_VALUE), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCider, 1, OreDictionary.WILDCARD_VALUE), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugPerry, 1, OreDictionary.WILDCARD_VALUE), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugDwarvenAle, 1, OreDictionary.WILDCARD_VALUE), 12),
			new LOTRTradeEntry(new ItemStack(LOTRMod.pickaxeDwarven), 10)
		};
		
		ROHAN_BLACKSMITH_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(LOTRMod.swordRohan), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.battleaxeRohan), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.daggerRohan), 9),
			new LOTRTradeEntry(new ItemStack(LOTRMod.spearRohan), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.helmetRohan), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bodyRohan), 28),
			new LOTRTradeEntry(new ItemStack(LOTRMod.legsRohan), 24),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bootsRohan), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.blacksmithHammer), 18),
			new LOTRTradeEntry(new ItemStack(Blocks.iron_bars, 8), 20),
			new LOTRTradeEntry(new ItemStack(Items.saddle), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.crossbowBolt, 4), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.ironCrossbow), 15)
		};
		
		ROHAN_BLACKSMITH_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.iron_ingot), 3),
			new LOTRTradeEntry(new ItemStack(Items.coal, 2, OreDictionary.WILDCARD_VALUE), 1),
			new LOTRTradeEntry(new ItemStack(Items.gold_ingot), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.copper), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.tin), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bronze), 3),
			new LOTRTradeEntry(new ItemStack(Items.leather), 2)
		};
		
		DUNLENDING_BARTENDER_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.cooked_beef), 7),
			new LOTRTradeEntry(new ItemStack(Items.cooked_porkchop), 7),
			new LOTRTradeEntry(new ItemStack(Items.cooked_chicken), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.rabbitCooked), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.rabbitStew), 10),
			new LOTRTradeEntry(new ItemStack(Items.bread), 5),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mug), 2),
			new LOTRTradeEntry(new ItemStack(Items.baked_potato, 2), 7),
			new LOTRTradeEntry(new ItemStack(Items.cooked_fished), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.gammon), 7),
			new LOTRTradeEntry(new ItemStack(LOTRMod.plate), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugAle, 1, 2), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugMead, 1, 2), 9),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCider, 1, 2), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugRum, 1, 2), 12)
		};
		
		DUNLENDING_BARTENDER_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.beef), 3),
			new LOTRTradeEntry(new ItemStack(Items.porkchop), 3),
			new LOTRTradeEntry(new ItemStack(Items.chicken), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.rabbitRaw), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.clayMug), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.clayPlate), 1),
			new LOTRTradeEntry(new ItemStack(Items.potato, 2), 1),
			new LOTRTradeEntry(new ItemStack(Items.fish), 2),
			new LOTRTradeEntry(new ItemStack(Items.wheat, 3), 1)
		};
		
		ROHAN_MEADHOST_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.cooked_beef), 7),
			new LOTRTradeEntry(new ItemStack(Items.cooked_porkchop), 7),
			new LOTRTradeEntry(new ItemStack(Items.cooked_chicken), 6),
			new LOTRTradeEntry(new ItemStack(Items.bread), 5),
			new LOTRTradeEntry(new ItemStack(Items.apple, 2), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.plate), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mug), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugWater), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugMead, 1, 0), 5),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugMead, 1, 1), 7),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugMead, 1, 2), 9),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugMead, 1, 3), 12),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugMead, 1, 4), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugMilk), 3)
		};
		
		ROHAN_MEADHOST_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.beef), 3),
			new LOTRTradeEntry(new ItemStack(Items.porkchop), 3),
			new LOTRTradeEntry(new ItemStack(Items.chicken), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.clayMug), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.clayPlate), 1),
			new LOTRTradeEntry(new ItemStack(Items.sugar, 2), 1),
			new LOTRTradeEntry(new ItemStack(Items.wheat, 3), 1),
			new LOTRTradeEntry(new ItemStack(Items.bucket), 3),
			new LOTRTradeEntry(new ItemStack(Items.water_bucket), 4),
			new LOTRTradeEntry(new ItemStack(Items.milk_bucket), 4)
		};
		
		HOBBIT_ORCHARDER_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.apple), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.appleGreen), 2),
			new LOTRTradeEntry(new ItemStack(Items.golden_apple, 1, 0), 25),
			new LOTRTradeEntry(new ItemStack(LOTRMod.pear), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.cherry), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.fruitWood, 4, 0), 5),
			new LOTRTradeEntry(new ItemStack(LOTRMod.fruitWood, 4, 1), 5),
			new LOTRTradeEntry(new ItemStack(LOTRMod.fruitWood, 4, 2), 5),
			new LOTRTradeEntry(new ItemStack(LOTRMod.fruitSapling, 4, 0), 4),
			new LOTRTradeEntry(new ItemStack(LOTRMod.fruitSapling, 4, 1), 4),
			new LOTRTradeEntry(new ItemStack(LOTRMod.fruitSapling, 4, 2), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCider, 1, 1), 5),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCider, 1, 2), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCider, 1, 3), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugPerry, 1, 1), 5),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugPerry, 1, 2), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugPerry, 1, 3), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCherryLiqueur, 1, 1), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCherryLiqueur, 1, 2), 12),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCherryLiqueur, 1, 3), 16)
		};
		
		HOBBIT_ORCHARDER_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.bucket), 3),
			new LOTRTradeEntry(new ItemStack(Items.water_bucket), 4),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mug), 1),
			new LOTRTradeEntry(new ItemStack(Items.iron_axe), 10),
			new LOTRTradeEntry(new ItemStack(Items.stone_axe), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.axeBronze), 8),
			new LOTRTradeEntry(new ItemStack(Items.dye, 6, 15), 1),
		};
		
		HOBBIT_FARMER_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.wheat), 2),
			new LOTRTradeEntry(new ItemStack(Items.wheat_seeds), 1),
			new LOTRTradeEntry(new ItemStack(Items.carrot), 3),
			new LOTRTradeEntry(new ItemStack(Items.potato), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.lettuce), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.pipeweedLeaf), 4),
			new LOTRTradeEntry(new ItemStack(LOTRMod.pipeweedSeeds), 3),
			new LOTRTradeEntry(new ItemStack(Blocks.wool), 2),
			new LOTRTradeEntry(new ItemStack(Items.milk_bucket), 8),
			new LOTRTradeEntry(new ItemStack(Items.lead), 8)
		};
		
		HOBBIT_FARMER_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.bucket), 3),
			new LOTRTradeEntry(new ItemStack(Items.water_bucket), 4),
			new LOTRTradeEntry(new ItemStack(Items.iron_hoe), 6),
			new LOTRTradeEntry(new ItemStack(Items.stone_hoe), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.hoeBronze), 4),
			new LOTRTradeEntry(new ItemStack(Items.dye, 6, 15), 1)
		};
		
		BLUE_DWARF_MINER_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.coal, 2), 4),
			new LOTRTradeEntry(new ItemStack(Blocks.iron_ore), 7),
			new LOTRTradeEntry(new ItemStack(LOTRMod.oreCopper), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.oreTin), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.oreSilver), 12),
			new LOTRTradeEntry(new ItemStack(Blocks.gold_ore), 20),
			new LOTRTradeEntry(new ItemStack(Items.dye, 1, 4), 1),
			new LOTRTradeEntry(new ItemStack(Items.glowstone_dust, 4), 3),
			new LOTRTradeEntry(new ItemStack(Blocks.cobblestone, 8), 1),
			new LOTRTradeEntry(new ItemStack(Items.lava_bucket), 20),
			new LOTRTradeEntry(new ItemStack(Items.flint), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.sulfur), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.saltpeter), 6)
		};
		
		BLUE_DWARF_MINER_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.cooked_beef), 3),
			new LOTRTradeEntry(new ItemStack(Items.cooked_porkchop), 3),
			new LOTRTradeEntry(new ItemStack(Items.cooked_chicken), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.gammon), 3),
			new LOTRTradeEntry(new ItemStack(Items.cooked_fished), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.rabbitCooked), 3),
			new LOTRTradeEntry(new ItemStack(Items.bread), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugAle, 1, OreDictionary.WILDCARD_VALUE), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugMead, 1, OreDictionary.WILDCARD_VALUE), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCider, 1, OreDictionary.WILDCARD_VALUE), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugPerry, 1, OreDictionary.WILDCARD_VALUE), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugDwarvenAle, 1, OreDictionary.WILDCARD_VALUE), 12),
			new LOTRTradeEntry(new ItemStack(LOTRMod.pickaxeBlueDwarven), 10)
		};
		
		NEAR_HARAD_DRINKS_TRADER_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugChocolate), 4),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugMilk), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugAle, 1, 1), 5),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugAle, 1, 2), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugAle, 1, 3), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugDwarvenAle, 1, 1), 5),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugDwarvenAle, 1, 2), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugDwarvenAle, 1, 3), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCider, 1, 1), 5),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCider, 1, 2), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCider, 1, 3), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugPerry, 1, 1), 5),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugPerry, 1, 2), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugPerry, 1, 3), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCherryLiqueur, 1, 1), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCherryLiqueur, 1, 2), 12),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugCherryLiqueur, 1, 3), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugRum, 1, 1), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugRum, 1, 2), 12),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugRum, 1, 3), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugAraq, 1, 1), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugAraq, 1, 2), 12),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugAraq, 1, 3), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugVodka, 1, 1), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugVodka, 1, 2), 12),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugVodka, 1, 3), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugOrcDraught, 1, 1), 12),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugOrcDraught, 1, 2), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugOrcDraught, 1, 3), 20),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugRedWine, 1, 1), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugRedWine, 1, 2), 12),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugRedWine, 1, 3), 16)
		};
		
		NEAR_HARAD_DRINKS_TRADER_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(LOTRMod.clayMug), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mug), 1),
			new LOTRTradeEntry(new ItemStack(Items.wheat, 3), 1),
			new LOTRTradeEntry(new ItemStack(Items.bucket), 3),
			new LOTRTradeEntry(new ItemStack(Items.water_bucket), 4),
			new LOTRTradeEntry(new ItemStack(Items.milk_bucket), 4),
			new LOTRTradeEntry(new ItemStack(Items.apple), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.appleGreen), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.pear), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.cherry), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.date), 2),
			new LOTRTradeEntry(new ItemStack(Items.potato, 2), 1),
			new LOTRTradeEntry(new ItemStack(Items.reeds, 2), 1)
		};
		
		NEAR_HARAD_MINERALS_TRADER_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.coal, 2), 4),
			new LOTRTradeEntry(new ItemStack(Blocks.iron_ore), 7),
			new LOTRTradeEntry(new ItemStack(LOTRMod.oreCopper), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.oreTin), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.oreSilver), 12),
			new LOTRTradeEntry(new ItemStack(Blocks.gold_ore), 20),
			new LOTRTradeEntry(new ItemStack(Items.dye, 1, 4), 1),
			new LOTRTradeEntry(new ItemStack(Items.glowstone_dust, 4), 3),
			new LOTRTradeEntry(new ItemStack(Items.lava_bucket), 20),
			new LOTRTradeEntry(new ItemStack(Items.flint), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.sulfur), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.saltpeter), 6)
		};
		
		NEAR_HARAD_MINERALS_TRADER_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.bucket), 3),
			new LOTRTradeEntry(new ItemStack(Items.iron_pickaxe), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.pickaxeBronze), 8),
			new LOTRTradeEntry(new ItemStack(Items.stone_pickaxe), 5),
			new LOTRTradeEntry(new ItemStack(Items.wooden_pickaxe), 1),
			new LOTRTradeEntry(new ItemStack(Blocks.torch, 16), 2)
		};
		
		NEAR_HARAD_PLANTS_TRADER_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Blocks.sapling, 1, 0), 3),
			new LOTRTradeEntry(new ItemStack(Blocks.sapling, 1, 1), 3),
			new LOTRTradeEntry(new ItemStack(Blocks.sapling, 1, 2), 3),
			new LOTRTradeEntry(new ItemStack(Blocks.sapling, 1, 3), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.sapling, 1, 0), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.sapling, 1, 2), 8),
			new LOTRTradeEntry(new ItemStack(LOTRMod.fruitSapling, 1, 0), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.fruitSapling, 1, 1), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.fruitSapling, 1, 2), 20),
			new LOTRTradeEntry(new ItemStack(LOTRMod.fruitSapling, 1, 2), 30),
			new LOTRTradeEntry(new ItemStack(LOTRMod.sapling2, 1, 1), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.sapling2, 1, 3), 30),
			new LOTRTradeEntry(new ItemStack(LOTRMod.sapling3, 1, 0), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.sapling3, 1, 1), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.sapling3, 1, 2), 3),
			new LOTRTradeEntry(new ItemStack(Blocks.red_flower, 1, 0), 1),
			new LOTRTradeEntry(new ItemStack(Blocks.yellow_flower, 1, 0), 1),
			new LOTRTradeEntry(new ItemStack(Blocks.red_mushroom, 1, 0), 3),
			new LOTRTradeEntry(new ItemStack(Blocks.brown_mushroom, 1, 0), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.morgulShroom, 1, 0), 20)
		};
		
		NEAR_HARAD_PLANTS_TRADER_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.bucket), 3),
			new LOTRTradeEntry(new ItemStack(Items.water_bucket), 4),
			new LOTRTradeEntry(new ItemStack(Items.iron_axe), 10),
			new LOTRTradeEntry(new ItemStack(Items.stone_axe), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.axeBronze), 8),
			new LOTRTradeEntry(new ItemStack(Items.dye, 6, 15), 1),
		};
		
		NEAR_HARAD_FOOD_TRADER_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.cooked_beef), 7),
			new LOTRTradeEntry(new ItemStack(Items.cooked_porkchop), 7),
			new LOTRTradeEntry(new ItemStack(Items.cooked_chicken), 6),
			new LOTRTradeEntry(new ItemStack(Items.bread), 5),
			new LOTRTradeEntry(new ItemStack(Items.baked_potato, 2), 7),
			new LOTRTradeEntry(new ItemStack(Items.cooked_fished), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.gammon), 7),
			new LOTRTradeEntry(new ItemStack(LOTRMod.rabbitCooked), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.date), 2),
			new LOTRTradeEntry(new ItemStack(Items.apple), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.appleGreen), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.pear), 2),
			new LOTRTradeEntry(new ItemStack(LOTRMod.cherry), 1),
			new LOTRTradeEntry(new ItemStack(LOTRMod.banana), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mango), 10),
			new LOTRTradeEntry(new ItemStack(Items.melon), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mallornNut), 25)
		};
		
		NEAR_HARAD_FOOD_TRADER_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.beef), 3),
			new LOTRTradeEntry(new ItemStack(Items.porkchop), 3),
			new LOTRTradeEntry(new ItemStack(Items.chicken), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.rabbitRaw), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.clayPlate), 1),
			new LOTRTradeEntry(new ItemStack(Items.potato, 2), 1),
			new LOTRTradeEntry(new ItemStack(Items.fish), 2),
			new LOTRTradeEntry(new ItemStack(Items.bucket), 3),
			new LOTRTradeEntry(new ItemStack(Items.water_bucket), 4),
			new LOTRTradeEntry(new ItemStack(Items.dye, 6, 15), 1)
		};
		
		BLUE_DWARF_MERCHANT_BUY = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(LOTRMod.dwarvenRing), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.swordBlueDwarven), 16),
			new LOTRTradeEntry(new ItemStack(LOTRMod.battleaxeBlueDwarven), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.hammerBlueDwarven), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.daggerBlueDwarven), 13),
			new LOTRTradeEntry(new ItemStack(LOTRMod.axeBlueDwarven), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.pickaxeBlueDwarven), 14),
			new LOTRTradeEntry(new ItemStack(LOTRMod.shovelBlueDwarven), 12),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mattockBlueDwarven), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.throwingAxeBlueDwarven), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.helmetBlueDwarven), 25),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bodyBlueDwarven), 36),
			new LOTRTradeEntry(new ItemStack(LOTRMod.legsBlueDwarven), 30),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bootsBlueDwarven), 22),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugDwarvenAle, 1, 1), 6),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugDwarvenAle, 1, 2), 9),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugDwarvenAle, 1, 3), 12),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugDwarvenTonic, 1, 1), 10),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugDwarvenTonic, 1, 2), 14),
			new LOTRTradeEntry(new ItemStack(LOTRMod.mugDwarvenTonic, 1, 3), 18),
			new LOTRTradeEntry(new ItemStack(LOTRMod.dwarfHerb), 10),
			new LOTRTradeEntry(new ItemStack(Items.glowstone_dust, 4), 2)
		};
		
		BLUE_DWARF_MERCHANT_SELL = new LOTRTradeEntry[]
		{
			new LOTRTradeEntry(new ItemStack(Items.iron_ingot), 3),
			new LOTRTradeEntry(new ItemStack(Items.coal, 2, OreDictionary.WILDCARD_VALUE), 1),
			new LOTRTradeEntry(new ItemStack(Items.gold_ingot), 15),
			new LOTRTradeEntry(new ItemStack(LOTRMod.copper), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.tin), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.bronze), 3),
			new LOTRTradeEntry(new ItemStack(Items.cooked_beef), 3),
			new LOTRTradeEntry(new ItemStack(Items.cooked_porkchop), 3),
			new LOTRTradeEntry(new ItemStack(Items.cooked_chicken), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.gammon), 3),
			new LOTRTradeEntry(new ItemStack(Items.cooked_fished), 3),
			new LOTRTradeEntry(new ItemStack(LOTRMod.rabbitCooked), 3),
			new LOTRTradeEntry(new ItemStack(Items.bread), 2)
		};
	}
}
