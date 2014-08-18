package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemMugBrewable;
import lotr.common.item.LOTRItemPouch;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

public class LOTRChestContents
{
	public static LOTRChestContents HOBBIT_HOLE_STUDY;
	public static LOTRChestContents HOBBIT_HOLE_LARDER;
	public static LOTRChestContents HOBBIT_HOLE_TREASURE;
	
	public static LOTRChestContents BLUE_MOUNTAINS_STRONGHOLD;
	
	public static LOTRChestContents HIGH_ELVEN_HALL;
	
	public static LOTRChestContents TROLL_HOARD;
	public static LOTRChestContents TROLL_HOARD_ETTENMOORS;
	
	public static LOTRChestContents DUNEDAIN_TOWER;
	
	public static LOTRChestContents RUINED_HOUSE;
	
	public static LOTRChestContents RANGER_TENT;
	
	public static LOTRChestContents BARROW_DOWNS;
	
	public static LOTRChestContents UNDERWATER_ELVEN_RUIN;
	
	public static LOTRChestContents ORC_DUNGEON;
	
	public static LOTRChestContents GUNDABAD_TENT;
	
	public static LOTRChestContents WARG_PIT;
	
	public static LOTRChestContents WOOD_ELF_HOUSE;
	
	public static LOTRChestContents MIRKWOOD_LOOT;
	
	public static LOTRChestContents DWARVEN_MINE_CORRIDOR;
	
	public static LOTRChestContents DWARVEN_TOWER;
	
	public static LOTRChestContents DWARF_HOUSE_LARDER;
	
	public static LOTRChestContents ELF_HOUSE;
	
	public static LOTRChestContents ROHAN_WATCHTOWER;
	
	public static LOTRChestContents ROHAN_BARROWS;
	
	public static LOTRChestContents URUK_TENT;
	
	public static LOTRChestContents DUNLENDING_HOUSE;
	
	public static LOTRChestContents DUNLENDING_CAMPFIRE;

	public static LOTRChestContents GONDOR_FORTRESS_DRINKS;
	public static LOTRChestContents GONDOR_FORTRESS_SUPPLIES;
	
	public static LOTRChestContents GONDOR_RUINS_BONES;
	public static LOTRChestContents GONDOR_RUINS_TREASURE;

	public static LOTRChestContents ORC_TENT;
	
	public static LOTRChestContents NEAR_HARAD_HOUSE;
	
	public static LOTRChestContents NEAR_HARAD_TOWER;
	
	public static LOTRChestContents NEAR_HARAD_PYRAMID;
	
	public int minItems;
	public int maxItems;
	public WeightedRandomChestContent[] items;
	public boolean pouches = false;
	public boolean enchantedBooks = false;
	
	public LOTRChestContents(int i, int j, WeightedRandomChestContent[] w)
	{
		minItems = i;
		maxItems = j;
		items = w;
	}
	
	public LOTRChestContents enablePouches()
	{
		pouches = true;
		return this;
	}
	
	public LOTRChestContents enableEnchantedBooks()
	{
		enchantedBooks = true;
		return this;
	}
	
	public static void fillChest(World world, Random random, int i, int j, int k, LOTRChestContents itemPool)
	{
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity == null || !(tileentity instanceof TileEntityChest))
		{
			if (j >= 0 && j < 256)
			{
				System.out.println("Warning: LOTRChestContents attempted to fill a chest at " + i + ", " + j + ", " + k + " which does not exist");
			}
			return;
		}
		fillInventory((TileEntityChest)tileentity, random, itemPool, getRandomItemAmount(itemPool, random));
	}
	
	public static void fillInventory(IInventory inventory, Random random, LOTRChestContents itemPool, int amount)
	{
        for (int i = 0; i < amount; i++)
        {
            WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent)WeightedRandom.getRandomItem(random, itemPool.items);
            ItemStack[] stacks = ChestGenHooks.generateStacks(random, weightedrandomchestcontent.theItemId, weightedrandomchestcontent.theMinimumChanceToGenerateItem, weightedrandomchestcontent.theMaximumChanceToGenerateItem);
            for (ItemStack itemstack : stacks)
            {
				if (itemstack.isItemStackDamageable() && !itemstack.getHasSubtypes())
				{
					itemstack.setItemDamage(MathHelper.floor_double((float)itemstack.getMaxDamage() * (0.25F + random.nextFloat() * 0.5F)));
				}
				
				if (itemPool.pouches && random.nextInt(100) == 0)
				{
					itemstack = new ItemStack(LOTRMod.pouch, 1, LOTRItemPouch.getRandomPouchSize(random));
				}
				
				if (itemPool.enchantedBooks && random.nextInt(60) == 0)
				{
					itemstack = Items.enchanted_book.func_92114_b(random).theItemId;
				}
				
				if (itemstack.stackSize > itemstack.getMaxStackSize())
				{
					itemstack.stackSize = itemstack.getMaxStackSize();
				}
				
				if (itemstack.getItem() instanceof LOTRItemMugBrewable)
				{
					itemstack.setItemDamage(1 + random.nextInt(3));
				}
				
				if (random.nextInt(4) == 0 && itemstack.isItemEnchantable() && itemstack.getItem().getItemEnchantability() > 0)
				{
					int enchantability = EnchantmentHelper.calcItemStackEnchantability(random, 1, 8, itemstack);
					EnchantmentHelper.addRandomEnchantment(random, itemstack, enchantability);
				}
				
                inventory.setInventorySlotContents(random.nextInt(inventory.getSizeInventory()), itemstack);
            }
        }
	}
	
	public static int getRandomItemAmount(LOTRChestContents itemPool, Random random)
	{
		return MathHelper.getRandomIntegerInRange(random, itemPool.minItems, itemPool.maxItems);
	}

	static
	{
		HOBBIT_HOLE_STUDY = new LOTRChestContents(3, 5, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.paper), 2, 8, 100),
			new WeightedRandomChestContent(new ItemStack(Items.feather), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(Items.dye), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(Items.glass_bottle), 2, 4, 50),
			new WeightedRandomChestContent(new ItemStack(Items.book), 1, 4, 100),
			new WeightedRandomChestContent(new ItemStack(Items.string), 1, 5, 50),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.lead), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.sulfurMatch), 1, 4, 50)
		}).enablePouches().enableEnchantedBooks();
		
		HOBBIT_HOLE_LARDER = new LOTRChestContents(2, 5, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bread), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.potato), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.pipeweed), 3, 8, 100),
			new WeightedRandomChestContent(new ItemStack(Items.bowl), 2, 4, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.lettuce), 1, 4, 100),
			new WeightedRandomChestContent(new ItemStack(Items.apple), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.appleGreen), 1, 3, 75),
			new WeightedRandomChestContent(new ItemStack(Items.sugar), 2, 5, 100),
			new WeightedRandomChestContent(new ItemStack(Items.egg), 1, 4, 100),
			new WeightedRandomChestContent(new ItemStack(Items.pumpkin_pie), 1, 2, 100),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_porkchop), 1, 2, 75),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_beef), 1, 2, 75),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_chicken), 1, 3, 75),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_fished), 1, 3, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.rabbitCooked), 1, 3, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.gammon), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(Items.milk_bucket), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(Items.carrot), 2, 4, 100),
			new WeightedRandomChestContent(new ItemStack(Items.cookie), 2, 5, 75),
			new WeightedRandomChestContent(new ItemStack(Items.dye, 1, 3), 2, 6, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.pear), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.cherry), 2, 4, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mug), 1, 2, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.plate), 1, 2, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.hobbitPancake), 1, 3, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugAle), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugCider), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugPerry), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugCherryLiqueur), 1, 1, 25)
		});
		
		HOBBIT_HOLE_TREASURE = new LOTRChestContents(3, 6, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 4, 100),
			new WeightedRandomChestContent(new ItemStack(Items.gold_nugget), 1, 8, 100),
			new WeightedRandomChestContent(new ItemStack(Items.diamond), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silver), 1, 4, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverNugget), 1, 8, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverCoin), 1, 25, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mithrilNugget), 1, 3, 25)
		});
		
		BLUE_MOUNTAINS_STRONGHOLD = new LOTRChestContents(2, 4, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(LOTRMod.helmetBlueDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bodyBlueDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.legsBlueDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bootsBlueDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordBlueDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.spearBlueDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.battleaxeBlueDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerBlueDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.hammerBlueDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.pickaxeBlueDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mattockBlueDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.throwingAxeBlueDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.coal), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Blocks.torch), 2, 4, 100),
			new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.blueDwarfSteel), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mug), 1, 1, 150),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_porkchop), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_chicken), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_beef), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.gammon), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.dwarvenRing), 1, 1, 25)
		}).enablePouches();
		
		HIGH_ELVEN_HALL = new LOTRChestContents(3, 5, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.book), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mug), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugMiruvor), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 2, 6, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.elvenBow), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordHighElven), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerHighElven), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.spearHighElven), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.lembas), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.apple), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.appleGreen), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(Items.bread), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.pear), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.lettuce), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(Items.carrot), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverCoin), 1, 10, 10)
		}).enablePouches().enableEnchantedBooks();
		
		TROLL_HOARD = new LOTRChestContents(1, 4, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(Items.gold_nugget), 1, 4, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silver), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverNugget), 1, 4, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverCoin), 1, 30, 75),
			new WeightedRandomChestContent(new ItemStack(Items.diamond), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.goldRing), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverRing), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mithrilNugget), 1, 2, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordHighElven), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerHighElven), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.helmetHighElven), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bodyHighElven), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bootsHighElven), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.legsHighElven), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.scimitarOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.helmetOrc), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bodyOrc), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.legsOrc), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bootsOrc), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_sword), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerIron), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.iron_helmet), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_chestplate), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_leggings), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_boots), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.helmetRanger), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bodyRanger), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.legsRanger), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bootsRanger), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25)
		}).enablePouches().enableEnchantedBooks();
		
		TROLL_HOARD_ETTENMOORS = new LOTRChestContents(1, 1, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(LOTRMod.trollTotem, 1, 0), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.trollTotem, 1, 1), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.trollTotem, 1, 2), 1, 1, 100)
		});
		
		DUNEDAIN_TOWER = new LOTRChestContents(5, 6, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.skull), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 2, 5, 100),
			new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silver), 2, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.gold_nugget), 2, 9, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverNugget), 2, 9, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverCoin), 2, 20, 100),
			new WeightedRandomChestContent(new ItemStack(Items.iron_sword), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerIron), 1, 1, 50)
		}).enablePouches().enableEnchantedBooks();
		
		RUINED_HOUSE = new LOTRChestContents(2, 5, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 4, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.orcBone), 1, 4, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.wargBone), 1, 2, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 2, 4, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bronze), 2, 4, 50),
			new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silver), 2, 3, 25),
			new WeightedRandomChestContent(new ItemStack(Items.gold_nugget), 2, 9, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverNugget), 2, 9, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverCoin), 1, 40, 50),
			new WeightedRandomChestContent(new ItemStack(Items.diamond), 1, 2, 2),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mithrilNugget), 1, 4, 2),
			new WeightedRandomChestContent(new ItemStack(Items.iron_sword), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerIron), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_axe), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordBronze), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerBronze), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.axeBronze), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.stone_sword), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.stone_axe), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.leather), 1, 4, 25),
			new WeightedRandomChestContent(new ItemStack(Items.string), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(Items.book), 1, 2, 25),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 1, 5, 50),
			new WeightedRandomChestContent(new ItemStack(Items.bow), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.glass_bottle), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(Items.lead), 1, 2, 25),
			new WeightedRandomChestContent(new ItemStack(Items.saddle), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mug), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.goldRing), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverRing), 1, 1, 5)
		}).enablePouches().enableEnchantedBooks();
		
		RANGER_TENT = new LOTRChestContents(1, 2, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.stick), 8, 16, 100),
			new WeightedRandomChestContent(new ItemStack(Items.coal), 1, 4, 100),
			new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugAle), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 2, 8, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerIron), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_sword), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_pickaxe), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_axe), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.bread), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.apple), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.appleGreen), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.pear), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.helmetRanger), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bodyRanger), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.legsRanger), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bootsRanger), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.bow), 1, 1, 25)
		}).enablePouches();
		
		BARROW_DOWNS = new LOTRChestContents(3, 8, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverCoin), 1, 20, 200),
			new WeightedRandomChestContent(new ItemStack(Items.gold_nugget), 1, 5, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverNugget), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 4, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silver), 1, 4, 50),
			new WeightedRandomChestContent(new ItemStack(Items.diamond), 1, 4, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mithrilNugget), 1, 2, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.helmetBronze), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bodyBronze), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.legsBronze), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bootsBronze), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_helmet), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_chestplate), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_leggings), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_boots), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 1, 5, 25),
			new WeightedRandomChestContent(new ItemStack(Items.skull), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerBronze), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordBronze), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerIron), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.iron_sword), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerMithril), 1, 1, 1),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordMithril), 1, 1, 1),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.battleaxeMithril), 1, 1, 1),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.hammerMithril), 1, 1, 1),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.helmetMithril), 1, 1, 1),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bodyMithril), 1, 1, 1),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.legsMithril), 1, 1, 1),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bootsMithril), 1, 1, 1),
			new WeightedRandomChestContent(new ItemStack(Items.glass_bottle), 1, 3, 10),
			new WeightedRandomChestContent(new ItemStack(Items.book), 1, 2, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.goldRing), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverRing), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 50)
		}).enablePouches().enableEnchantedBooks();
		
		UNDERWATER_ELVEN_RUIN = new LOTRChestContents(6, 10, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 4, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.elfBone), 1, 4, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.orcBone), 1, 4, 25),
			new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 2, 4, 50),
			new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silver), 2, 3, 50),
			new WeightedRandomChestContent(new ItemStack(Items.gold_nugget), 2, 9, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverNugget), 2, 9, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverCoin), 2, 30, 50),
			new WeightedRandomChestContent(new ItemStack(Items.diamond), 1, 2, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mithrilNugget), 1, 4, 25),
			new WeightedRandomChestContent(new ItemStack(Items.iron_sword), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerIron), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordHighElven), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerHighElven), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordMithril), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerMithril), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 1, 5, 25),
			new WeightedRandomChestContent(new ItemStack(Items.glass_bottle), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.goldRing), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverRing), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mithrilRing), 1, 1, 5)
		}).enablePouches().enableEnchantedBooks();
		
		ORC_DUNGEON = new LOTRChestContents(6, 8, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(Items.skull), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 1, 4, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.orcSteel), 1, 4, 100),
			new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 2, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silver), 1, 2, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mithrilNugget), 1, 4, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.guldurilCrystal), 1, 4, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.orcBomb), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.orcTorchItem), 1, 4, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.helmetOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bodyOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.legsOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bootsOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.scimitarOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerOrcPoisoned), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.battleaxeOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.hammerOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.orcBow), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 2, 7, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.maggotyBread), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugOrcDraught), 1, 1, 200)
		}).enablePouches().enableEnchantedBooks();
		
		GUNDABAD_TENT = new LOTRChestContents(1, 2, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.rotten_flesh), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.stick), 8, 16, 100),
			new WeightedRandomChestContent(new ItemStack(Items.coal), 1, 4, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.orcSteel), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugOrcDraught), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 2, 8, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerOrc), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerOrcPoisoned), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.scimitarOrc), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.pickaxeOrc), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.axeOrc), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerAngmar), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerAngmarPoisoned), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordAngmar), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.pickaxeAngmar), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.axeAngmar), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerIron), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerIronPoisoned), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_sword), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_pickaxe), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_axe), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.saddle), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.maggotyBread), 1, 3, 200),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25)
		}).enablePouches();
		
		WARG_PIT = new LOTRChestContents(4, 6, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 4, 100),
			new WeightedRandomChestContent(new ItemStack(Items.rotten_flesh), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(Items.skull), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.lead), 1, 2, 75),
			new WeightedRandomChestContent(new ItemStack(Items.saddle), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.porkchop), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(Items.beef), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(Items.chicken), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.rabbitRaw), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.maggotyBread), 1, 3, 50)
		}).enablePouches();
		
		WOOD_ELF_HOUSE = new LOTRChestContents(4, 6, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mirkwoodBow), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordWoodElven), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerWoodElven), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.axeWoodElven), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 2, 6, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.sapling, 1, 2), 1, 3, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugRedWine), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.lembas), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mug), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.string), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25)
		}).enablePouches().enableEnchantedBooks();
		
		MIRKWOOD_LOOT = new LOTRChestContents(4, 6, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverCoin), 1, 10, 100),
			new WeightedRandomChestContent(new ItemStack(Items.gold_nugget), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverNugget), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 2, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silver), 1, 2, 25),
			new WeightedRandomChestContent(new ItemStack(Items.diamond), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mithrilNugget), 1, 2, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.leatherHat), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.leather_helmet), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.leather_chestplate), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.leather_leggings), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.leather_boots), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.helmetBronze), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bodyBronze), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.legsBronze), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bootsBronze), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_helmet), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_chestplate), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_leggings), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.iron_boots), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 1, 5, 25),
			new WeightedRandomChestContent(new ItemStack(Items.skull), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.elfBone), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.orcBone), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.dwarfBone), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(Items.rotten_flesh), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerBronze), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordBronze), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerIron), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.iron_sword), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerMithril), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(Items.glass_bottle), 1, 3, 10),
			new WeightedRandomChestContent(new ItemStack(Items.book), 1, 2, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.goldRing), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverRing), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 50)
		}).enablePouches().enableEnchantedBooks();
		
		DWARVEN_MINE_CORRIDOR = new LOTRChestContents(3, 6, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 1, 4, 75),
			new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silver), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(Items.diamond), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mithril), 1, 1, 10),
			new WeightedRandomChestContent(new ItemStack(Items.coal), 3, 8, 100),
			new WeightedRandomChestContent(new ItemStack(Items.bread), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.glowstone_dust), 2, 8, 75),
			new WeightedRandomChestContent(new ItemStack(Blocks.torch), 2, 6, 100)
		});
		
		DWARVEN_TOWER = new LOTRChestContents(2, 2, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(LOTRMod.helmetDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bodyDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.legsDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bootsDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.spearDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.battleaxeDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.hammerDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.pickaxeDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mattockDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.throwingAxeDwarven), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.coal), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Blocks.torch), 2, 4, 100),
			new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.dwarfSteel), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mug), 1, 1, 150),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_porkchop), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_chicken), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_beef), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.gammon), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.dwarvenRing), 1, 1, 25)
		}).enablePouches();
		
		DWARF_HOUSE_LARDER = new LOTRChestContents(2, 5, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bread), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_porkchop), 1, 2, 75),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_beef), 1, 2, 75),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_chicken), 1, 3, 75),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_fished), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.gammon), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mug), 1, 2, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.plate), 1, 2, 100)
		});
		
		ELF_HOUSE = new LOTRChestContents(4, 6, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mallornStick), 3, 8, 100),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 2, 8, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordMallorn), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mallornBow), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.elanor), 1, 3, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.niphredil), 1, 3, 75),
			new WeightedRandomChestContent(new ItemStack(Items.apple), 1, 3, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.lembas), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mallornNut), 1, 2, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugMiruvor), 1, 1, 30),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25)
		}).enablePouches().enableEnchantedBooks();
		
		ROHAN_WATCHTOWER = new LOTRChestContents(3, 5, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 2, 6, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordRohan), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(Items.bow), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(Items.bread), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mug), 1, 2, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugMead), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(Items.apple), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.saddle), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(Items.lead), 1, 2, 75),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25)
		}).enablePouches();
		
		ROHAN_BARROWS = new LOTRChestContents(6, 8, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(Items.skull), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 2, 5, 75),
			new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 3, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silver), 2, 3, 75),
			new WeightedRandomChestContent(new ItemStack(Items.gold_nugget), 2, 9, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverNugget), 2, 9, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverCoin), 2, 10, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordRohan), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.spearRohan), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerRohan), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.saddle), 1, 1, 75)
		}).enablePouches().enableEnchantedBooks();
		
		URUK_TENT = new LOTRChestContents(1, 2, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.rotten_flesh), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.stick), 8, 16, 100),
			new WeightedRandomChestContent(new ItemStack(Items.coal), 1, 4, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.urukSteel), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugOrcDraught), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.crossbowBolt), 2, 8, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerUruk), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerUrukPoisoned), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.scimitarUruk), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.pickaxeUruk), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.axeUruk), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.saddle), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.maggotyBread), 1, 3, 200),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25)
		}).enablePouches();
		
		DUNLENDING_HOUSE = new LOTRChestContents(3, 6, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 2, 100),
			new WeightedRandomChestContent(new ItemStack(Items.leather), 1, 2, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.wargFur), 1, 2, 100),
			new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 1, 3, 50),
			new WeightedRandomChestContent(new ItemStack(Items.string), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.wheat), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.bread), 1, 2, 100),
			new WeightedRandomChestContent(new ItemStack(Items.potato), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.carrot), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.apple), 1, 2, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.appleGreen), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.iron_sword), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.stone_sword), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.wooden_sword), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.stone_hoe), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.stone_axe), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.stone_shovel), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerIron), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.spearIron), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.bow), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 2, 5, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mug), 1, 3, 75),
			new WeightedRandomChestContent(new ItemStack(Items.coal), 1, 3, 75),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25)
		}).enablePouches();
		
		DUNLENDING_CAMPFIRE = new LOTRChestContents(1, 4, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.flint_and_steel), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(Items.coal, 1, 0), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(Items.coal, 1, 1), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(Items.stick), 1, 8, 100),
			new WeightedRandomChestContent(new ItemStack(Items.porkchop), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_porkchop), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.gammon), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(Items.beef), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_beef), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(Items.porkchop), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_porkchop), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(Items.fish), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(Items.cooked_fished), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.rabbitRaw), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.rabbitCooked), 1, 2, 50)
		});
		
		GONDOR_FORTRESS_DRINKS = new LOTRChestContents(2, 4, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mug), 1, 2, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugAle), 1, 1, 50)
		});
		
		GONDOR_FORTRESS_SUPPLIES = new LOTRChestContents(4, 6, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(LOTRMod.helmetGondor), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bodyGondor), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.legsGondor), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bootsGondor), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordGondor), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.spearGondor), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerGondor), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.hammerGondor), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.bow), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 4, 10, 100),
			new WeightedRandomChestContent(new ItemStack(Items.flint_and_steel), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverCoin), 2, 5, 75),
			new WeightedRandomChestContent(new ItemStack(Items.apple), 2, 5, 100),
			new WeightedRandomChestContent(new ItemStack(Items.bread), 2, 4, 100),
			new WeightedRandomChestContent(new ItemStack(Items.saddle), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.lead), 1, 2, 50),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25)
		}).enablePouches();
		
		GONDOR_RUINS_BONES = new LOTRChestContents(6, 6, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(Items.skull), 1, 1, 25)
		});
		
		GONDOR_RUINS_TREASURE = new LOTRChestContents(6, 6, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 2, 5, 100),
			new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silver), 2, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.gold_nugget), 2, 9, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverNugget), 2, 9, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverCoin), 2, 20, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.swordGondor), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.spearGondor), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerGondor), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.hammerGondor), 1, 1, 25)
		}).enablePouches().enableEnchantedBooks();
		
		ORC_TENT = new LOTRChestContents(1, 2, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.rotten_flesh), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.stick), 8, 16, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.nauriteGem), 1, 4, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.orcSteel), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugOrcDraught), 1, 1, 100),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 2, 8, 100),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerOrcPoisoned), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.scimitarOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.pickaxeOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.axeOrc), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.saddle), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.maggotyBread), 1, 3, 200),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25)
		}).enablePouches();
		
		NEAR_HARAD_HOUSE = new LOTRChestContents(4, 6, new WeightedRandomChestContent[]
		{
				new WeightedRandomChestContent(new ItemStack(Items.paper), 2, 8, 50),
				new WeightedRandomChestContent(new ItemStack(Items.bucket), 1, 3, 25),
				new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 4, 25),
				new WeightedRandomChestContent(new ItemStack(Items.feather), 1, 2, 50),
				new WeightedRandomChestContent(new ItemStack(Items.leather), 1, 2, 50),
				new WeightedRandomChestContent(new ItemStack(Items.glass_bottle), 1, 2, 50),
				new WeightedRandomChestContent(new ItemStack(Items.book), 1, 3, 50),
				new WeightedRandomChestContent(new ItemStack(Items.string), 1, 3, 50),
				new WeightedRandomChestContent(new ItemStack(Items.stick), 1, 8, 50),
				new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 50),
				new WeightedRandomChestContent(new ItemStack(Items.apple), 1, 3, 25),
				new WeightedRandomChestContent(new ItemStack(Items.bread), 1, 3, 25),
				new WeightedRandomChestContent(new ItemStack(LOTRMod.appleGreen), 1, 3, 25),
				new WeightedRandomChestContent(new ItemStack(LOTRMod.pear), 1, 3, 25),
				new WeightedRandomChestContent(new ItemStack(LOTRMod.date), 1, 3, 100),
				new WeightedRandomChestContent(new ItemStack(Items.carrot), 1, 3, 25),
				new WeightedRandomChestContent(new ItemStack(Items.potato), 1, 4, 25),
				new WeightedRandomChestContent(new ItemStack(Items.baked_potato), 1, 2, 25),
				new WeightedRandomChestContent(new ItemStack(Items.cooked_porkchop), 1, 2, 25),
				new WeightedRandomChestContent(new ItemStack(Items.cooked_beef), 1, 2, 25),
				new WeightedRandomChestContent(new ItemStack(Items.cooked_chicken), 1, 3, 25),
				new WeightedRandomChestContent(new ItemStack(Items.cooked_fished), 1, 3, 25),
				new WeightedRandomChestContent(new ItemStack(LOTRMod.lettuce), 1, 4, 25),
				new WeightedRandomChestContent(new ItemStack(Items.coal), 1, 4, 50),
				new WeightedRandomChestContent(new ItemStack(Items.wheat), 1, 5, 50),
				new WeightedRandomChestContent(new ItemStack(LOTRMod.plate), 1, 3, 50),
				new WeightedRandomChestContent(new ItemStack(LOTRMod.mug), 1, 3, 100),
				new WeightedRandomChestContent(new ItemStack(LOTRMod.mugAraq), 1, 1, 75)
		}).enablePouches().enableEnchantedBooks();
		
		NEAR_HARAD_TOWER = new LOTRChestContents(4, 6, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(LOTRMod.helmetNearHarad), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bodyNearHarad), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.legsNearHarad), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bootsNearHarad), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.scimitarNearHarad), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.daggerNearHarad), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.spearNearHarad), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.nearHaradBow), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.arrow), 1, 6, 75),
			new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 1, 1, 75),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mug), 1, 1, 150),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugAraq, 1, 1), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugAraq, 1, 2), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mugAraq, 1, 3), 1, 1, 50),
			new WeightedRandomChestContent(new ItemStack(Items.apple), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(Items.bread), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.appleGreen), 1, 3, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.date), 1, 3, 100),
			new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.saddle), 1, 1, 25)
		}).enablePouches();
		
		NEAR_HARAD_PYRAMID = new LOTRChestContents(8, 10, new WeightedRandomChestContent[]
		{
			new WeightedRandomChestContent(new ItemStack(LOTRMod.helmetNearHarad), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bodyNearHarad), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.legsNearHarad), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bootsNearHarad), 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.scimitarNearHarad), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.nearHaradBow), 1, 1, 25),
			new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 8, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silver), 1, 8, 50),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.bronze), 1, 8, 50),
			new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 1, 8, 50),
			new WeightedRandomChestContent(new ItemStack(Items.diamond), 1, 4, 25),
			new WeightedRandomChestContent(new ItemStack(Items.dye, 1, 4), 1, 8, 25),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.mithrilNugget), 1, 8, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.goldRing), 1, 2, 10),
			new WeightedRandomChestContent(new ItemStack(LOTRMod.silverRing), 1, 2, 10),
			new WeightedRandomChestContent(new ItemStack(Items.bone), 1, 3, 10),
			new WeightedRandomChestContent(new ItemStack(Items.rotten_flesh), 1, 3, 10)
		});
	}
}
