package lotr.common.recipe;

import static lotr.common.LOTRMod.*;
import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPELESS;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemBone;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class LOTRRecipes
{
	public static List woodenSlabRecipes = new ArrayList();
	
    public static List morgulRecipes = new ArrayList();
	public static List elvenRecipes = new ArrayList();
	public static List dwarvenRecipes = new ArrayList();
	public static List urukRecipes = new ArrayList();
	public static List woodElvenRecipes = new ArrayList();
	public static List gondorianRecipes = new ArrayList();
	public static List rohirricRecipes = new ArrayList();
	public static List dunlendingRecipes = new ArrayList();
	public static List angmarRecipes = new ArrayList();
	public static List nearHaradRecipes = new ArrayList();
	public static List highElvenRecipes = new ArrayList();
	public static List blueMountainsRecipes = new ArrayList();
	public static List rangerRecipes = new ArrayList();
	
	private static List[] commonOrcRecipes = new List[] {morgulRecipes, urukRecipes, angmarRecipes};
	private static List[] morgulAndAngmarRecipes = new List[] {morgulRecipes, angmarRecipes};
	private static List[] commonElfRecipes = new List[] {elvenRecipes, woodElvenRecipes, highElvenRecipes};
	private static List[] commonDwarfRecipes = new List[] {dwarvenRecipes, blueMountainsRecipes};
	private static List[] commonDunedainRecipes = new List[] {gondorianRecipes, rangerRecipes};
	
	public static void createAllRecipes()
	{
		registerOres();
		
		RecipeSorter.register("lotr:hobbitPipe", LOTRRecipeHobbitPipe.class, SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register("lotr:pouch", LOTRPouchRecipe.class, SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register("lotr:leatherHatDye", LOTRRecipeLeatherHatDye.class, SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register("lotr:featherDye", LOTRRecipeFeatherDye.class, SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register("lotr:leatherHatFeather", LOTRRecipeLeatherHatFeather.class, SHAPELESS, "after:minecraft:shapeless");
		
		createStandardRecipes();
		
		createWoodenSlabRecipes();
		CraftingManager.getInstance().getRecipeList().addAll(0, woodenSlabRecipes);
		
		createSmeltingRecipes();
		
		createCommonOrcRecipes();
		createMorgulAndAngmarRecipes();
		createCommonElfRecipes();
		createCommonDwarfRecipes();
		createCommonDunedainRecipes();
		
		createMorgulRecipes();
		createElvenRecipes();
		createDwarvenRecipes();
		createUrukRecipes();
		createWoodElvenRecipes();
		createGondorianRecipes();
		createRohirricRecipes();
		createDunlendingRecipes();
		createAngmarRecipes();
		createNearHaradRecipes();
		createHighElvenRecipes();
		createBlueMountainsRecipes();
		createRangerRecipes();
	}

	private static void registerOres()
	{
		OreDictionary.registerOre("plankWood", new ItemStack(planks, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("slabWood", new ItemStack(woodSlabSingle, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("slabWood", new ItemStack(woodSlabSingle2, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("stickWood", mallornStick);
		
		OreDictionary.registerOre("logWood", new ItemStack(wood, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("logWood", new ItemStack(fruitWood, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("logWood", new ItemStack(wood2, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("logWood", new ItemStack(wood3, 1, OreDictionary.WILDCARD_VALUE));
		
		OreDictionary.registerOre("treeLeaves", new ItemStack(leaves, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(fruitLeaves, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(leaves2, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(leaves3, 1, OreDictionary.WILDCARD_VALUE));
		
		OreDictionary.registerOre("oreCopper", oreCopper);
		OreDictionary.registerOre("oreTin", oreTin);
		OreDictionary.registerOre("oreSilver", oreSilver);
		OreDictionary.registerOre("oreSulfur", oreSulfur);
		OreDictionary.registerOre("oreSaltpeter", oreSaltpeter);
		
		OreDictionary.registerOre("ingotCopper", copper);
		OreDictionary.registerOre("ingotTin", tin);
		OreDictionary.registerOre("ingotBronze", bronze);
		OreDictionary.registerOre("ingotSilver", silver);
		OreDictionary.registerOre("nuggetSilver", silverNugget);
		OreDictionary.registerOre("sulfur", sulfur);
		OreDictionary.registerOre("saltpeter", saltpeter);
		
		OreDictionary.registerOre("dyeYellow", new ItemStack(dye, 1, 0));
		OreDictionary.registerOre("dyeWhite", new ItemStack(dye, 1, 1));
		OreDictionary.registerOre("dyeBlue", new ItemStack(dye, 1, 2));
		
		OreDictionary.registerOre("apple", Items.apple);
		OreDictionary.registerOre("apple", appleGreen);
		
		OreDictionary.registerOre("bone", Items.bone);
		LOTRItemBone.registerAllBones("bone");
	}
	
	private static void createStandardRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(goldRing), new Object[]
		{
			"XXX", "X X", "XXX", 'X', Items.gold_nugget
		});
		GameRegistry.addRecipe(new ItemStack(Items.saddle), new Object[]
		{
			"XXX", "Y Y", 'X', Items.leather, 'Y', Items.iron_ingot
		});
		GameRegistry.addShapelessRecipe(new ItemStack(bronze, 2), new Object[]
		{
			copper, tin
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shovelBronze), new Object[]
		{
			"X", "Y", "Y", 'X', bronze, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pickaxeBronze), new Object[]
		{
			"XXX", " Y ", " Y ", 'X', bronze, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axeBronze), new Object[]
		{
			"XX", "XY", " Y", 'X', bronze, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(swordBronze), new Object[]
		{
			"X", "X", "Y", 'X', bronze, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoeBronze), new Object[]
		{
			"XX", " Y", " Y", 'X', bronze, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ItemStack(helmetBronze), new Object[]
		{
			"XXX", "X X", 'X', bronze
		});
		GameRegistry.addRecipe(new ItemStack(bodyBronze), new Object[]
		{
			"X X", "XXX", "XXX", 'X', bronze
		});
		GameRegistry.addRecipe(new ItemStack(legsBronze), new Object[]
		{
			"XXX", "X X", "X X", 'X', bronze
		});
		GameRegistry.addRecipe(new ItemStack(bootsBronze), new Object[]
		{
			"X X", "X X", 'X', bronze
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 0), new Object[]
		{
			new ItemStack(wood, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(stairsShirePine, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 0)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(silverNugget, 9), new Object[]
		{
			silver
		});
		GameRegistry.addRecipe(new ItemStack(silver), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', silverNugget
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(silverRing), new Object[]
		{
			"XXX", "X X", "XXX", 'X', "nuggetSilver"
		}));
		GameRegistry.addShapelessRecipe(new ItemStack(mithrilNugget, 9), new Object[]
		{
			mithril
		});
		GameRegistry.addRecipe(new ItemStack(mithril), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', mithrilNugget
		});
		GameRegistry.addRecipe(new ItemStack(mithrilRing), new Object[]
		{
			"XXX", "X X", "XXX", 'X', mithrilNugget
		});
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 13), new Object[]
		{
			shireHeather
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(clayMug, 2), new Object[]
		{
			"X", "Y", "X", 'X', "ingotTin", 'Y', Items.clay_ball
		}));
		GameRegistry.addShapelessRecipe(new ItemStack(mugChocolate), new Object[]
		{
			mugMilk, Items.sugar, new ItemStack(Items.dye, 1, 3)
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(appleCrumbleItem), new Object[]
		{
			"AAA", "BCB", "DDD", 'A', Items.milk_bucket, 'B', "apple", 'C', Items.sugar, 'D', Items.wheat
		}));
		GameRegistry.addShapelessRecipe(new ItemStack(copper, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 0), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', copper
		});
		GameRegistry.addShapelessRecipe(new ItemStack(tin, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 1), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', tin
		});
		GameRegistry.addShapelessRecipe(new ItemStack(bronze, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 2)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 2), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', bronze
		});
		GameRegistry.addShapelessRecipe(new ItemStack(silver, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 3), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', silver
		});
		GameRegistry.addShapelessRecipe(new ItemStack(mithril, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 4)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 4), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', mithril
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chandelier, 2, 0), new Object[]
		{
			" X ", "YZY", 'X', "stickWood", 'Y', Blocks.torch, 'Z', "ingotBronze"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chandelier, 2, 1), new Object[]
		{
			" X ", "YZY", 'X', "stickWood", 'Y', Blocks.torch, 'Z', Items.iron_ingot
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chandelier, 2, 2), new Object[]
		{
			" X ", "YZY", 'X', "stickWood", 'Y', Blocks.torch, 'Z', "ingotSilver"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chandelier, 2, 3), new Object[]
		{
			" X ", "YZY", 'X', "stickWood", 'Y', Blocks.torch, 'Z', Items.gold_ingot
		}));
		GameRegistry.addShapelessRecipe(new ItemStack(pipeweedSeeds), new Object[]
		{
			pipeweedPlant
		});
		GameRegistry.addShapelessRecipe(new ItemStack(pipeweedSeeds), new Object[]
		{
			pipeweedLeaf
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shovelMithril), new Object[]
		{
			"X", "Y", "Y", 'X', mithril, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pickaxeMithril), new Object[]
		{
			"XXX", " Y ", " Y ", 'X', mithril, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axeMithril), new Object[]
		{
			"XX", "XY", " Y", 'X', mithril, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(swordMithril), new Object[]
		{
			"X", "X", "Y", 'X', mithril, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoeMithril), new Object[]
		{
			"XX", " Y", " Y", 'X', mithril, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ItemStack(helmetMithril), new Object[]
		{
			"XXX", "X X", 'X', mithril
		});
		GameRegistry.addRecipe(new ItemStack(bodyMithril), new Object[]
		{
			"X X", "XXX", "XXX", 'X', mithril
		});
		GameRegistry.addRecipe(new ItemStack(legsMithril), new Object[]
		{
			"XXX", "X X", "X X", 'X', mithril
		});
		GameRegistry.addRecipe(new ItemStack(bootsMithril), new Object[]
		{
			"X X", "X X", 'X', mithril
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(spearBronze), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', bronze, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(spearIron), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(spearMithril), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', mithril, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(silverCoin), new Object[]
		{
			"XX", "XX", 'X', "nuggetSilver"
		}));
		GameRegistry.addRecipe(new ItemStack(clayPlate), new Object[]
		{
			"XXX", 'X', Items.clay_ball
		});
		GameRegistry.addRecipe(new ItemStack(helmetWarg), new Object[]
		{
			"XXX", "X X", 'X', wargFur
		});
		GameRegistry.addRecipe(new ItemStack(bodyWarg), new Object[]
		{
			"X X", "XXX", "XXX", 'X', wargFur
		});
		GameRegistry.addRecipe(new ItemStack(legsWarg), new Object[]
		{
			"XXX", "X X", "X X", 'X', wargFur
		});
		GameRegistry.addRecipe(new ItemStack(bootsWarg), new Object[]
		{
			"X X", "X X", 'X', wargFur
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chandelier, 2, 4), new Object[]
		{
			" X ", "YZY", 'X', "stickWood", 'Y', Blocks.torch, 'Z', mithril
		}));
		for (int i = 0; i <= 15; i++)
		{
			ItemStack dye = new ItemStack(Items.dye, 1, BlockColored.func_150031_c(i));
			String dyeName = OreDictionary.getOreName(OreDictionary.getOreID(dye));
			GameRegistry.addRecipe(new LOTRRecipeHobbitPipe(i, new Object[]
			{
				new ItemStack(hobbitPipe, 1, OreDictionary.WILDCARD_VALUE), dyeName
			}));
			
			/*ItemStack dye = new ItemStack(Items.dye, 1, BlockColored.func_150031_c(i));
			String dyeName = "";
			int[] ids = OreDictionary.getOreIDs(dye);
			for (int l : ids)
			{
				String name = OreDictionary.getOreName(l);
				if (name.startsWith("dye") && name.length() > "dye".length())
				{
					dyeName = name;
					break;
				}
			}
			System.out.println(dye.getDisplayName() + " : " + dyeName);
			
			GameRegistry.addRecipe(new LOTRRecipeHobbitPipe(i, new Object[]
			{
				new ItemStack(hobbitPipe, 1, OreDictionary.WILDCARD_VALUE), dyeName
			}));*/
		}
		GameRegistry.addRecipe(new LOTRRecipeHobbitPipe(16, new Object[]
		{
			new ItemStack(hobbitPipe, 1, OreDictionary.WILDCARD_VALUE), Items.diamond
		}));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 4, 15), new Object[]
		{
			wargBone
		});
		GameRegistry.addRecipe(new ItemStack(Items.golden_apple, 1, 0), new Object[]
		{
			"XXX", "XYX", "XXX", 'X', Items.gold_ingot, 'Y', appleGreen
		});
		GameRegistry.addRecipe(new ItemStack(Items.golden_apple, 1, 1), new Object[]
		{
			"XXX", "XYX", "XXX", 'X', Blocks.gold_block, 'Y', appleGreen
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 4), new Object[]
		{
			new ItemStack(fruitWood, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(stairsApple, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 4)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 5), new Object[]
		{
			new ItemStack(fruitWood, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(stairsPear, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 5)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 6), new Object[]
		{
			new ItemStack(fruitWood, 1, 2)
		});
		GameRegistry.addRecipe(new ItemStack(stairsCherry, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 6)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(dye, 2, 2), new Object[]
		{
			bluebell
		});
		GameRegistry.addRecipe(new ItemStack(hearth, 3), new Object[]
		{
			"XXX", "YYY", 'X', Items.coal, 'Y', Items.brick
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(daggerBronze), new Object[]
		{
			"X", "Y", 'X', bronze, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(daggerIron), new Object[]
		{
			"X", "Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(daggerMithril), new Object[]
		{
			"X", "Y", 'X', mithril, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(battleaxeMithril), new Object[]
		{
			"XXX", "XYX", " Y ", 'X', mithril, 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hammerMithril), new Object[]
		{
			"XYX", "XYX", " Y ", 'X', mithril, 'Y', "stickWood"
		}));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 3, 15), new Object[]
		{
			orcBone
		});
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 3, 15), new Object[]
		{
			elfBone
		});
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 15), new Object[]
		{
			dwarfBone
		});
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 15), new Object[]
		{
			hobbitBone
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(commandHorn), new Object[]
		{
			"X ", "XX", 'X', "ingotBronze"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(crossbowBolt, 4), new Object[]
		{
			"X", "Y", "Z", 'X', Items.iron_ingot, 'Y', "stickWood", 'Z', Items.feather
		}));
		GameRegistry.addRecipe(new ItemStack(cherryPieItem), new Object[]
		{
			"AAA", "BCB", "DDD", 'A', Items.milk_bucket, 'B', cherry, 'C', Items.sugar, 'D', Items.wheat
		});
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 6, 15), new Object[]
		{
			trollBone
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ironCrossbow), new Object[]
		{
			"XXY", "ZYX", "YZX", 'X', Items.iron_ingot, 'Y', "stickWood", 'Z', Items.string
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(mithrilCrossbow), new Object[]
		{
			"XXY", "ZYX", "YZX", 'X', mithril, 'Y', "stickWood", 'Z', Items.string
		}));
		GameRegistry.addRecipe(new ItemStack(stairsMirkOak, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 2)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 2), new Object[]
		{
			new ItemStack(wood, 1, 2)
		});
		GameRegistry.addRecipe(new ItemStack(horseArmorIron), new Object[]
		{
			"X  ", "XYX", "XXX", 'X', Items.iron_ingot, 'Y', Items.leather
		});
		GameRegistry.addRecipe(new ItemStack(horseArmorGold), new Object[]
		{
			"X  ", "XYX", "XXX", 'X', Items.gold_ingot, 'Y', Items.leather
		});
		GameRegistry.addRecipe(new ItemStack(horseArmorDiamond), new Object[]
		{
			"X  ", "XYX", "XXX", 'X', Items.diamond, 'Y', Items.leather
		});
		GameRegistry.addRecipe(new LOTRPouchRecipe(new ItemStack(pouch, 1, 1), new Object[]
		{
			new ItemStack(pouch, 1, 0), new ItemStack(pouch, 1, 0)
		}));
		GameRegistry.addRecipe(new LOTRPouchRecipe(new ItemStack(pouch, 1, 2), new Object[]
		{
			new ItemStack(pouch, 1, 0), new ItemStack(pouch, 1, 0), new ItemStack(pouch, 1, 0)
		}));
		GameRegistry.addRecipe(new LOTRPouchRecipe(new ItemStack(pouch, 1, 2), new Object[]
		{
			new ItemStack(pouch, 1, 0), new ItemStack(pouch, 1, 1)
		}));
		GameRegistry.addRecipe(new ItemStack(ancientItem, 1, 0), new Object[]
		{
			"X", "Y", "Z", 'X', new ItemStack(ancientItemParts, 1, 0), 'Y', new ItemStack(ancientItemParts, 1, 1), 'Z', new ItemStack(ancientItemParts, 1, 2)
		});
		GameRegistry.addRecipe(new ItemStack(ancientItem, 1, 1), new Object[]
		{
			"X", "Y", 'X', new ItemStack(ancientItemParts, 1, 0), 'Y', new ItemStack(ancientItemParts, 1, 2)
		});
		GameRegistry.addRecipe(new ItemStack(ancientItem, 1, 2), new Object[]
		{
			"XXX", "X X", 'X', new ItemStack(ancientItemParts, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(ancientItem, 1, 3), new Object[]
		{
			"X X", "XXX", "XXX", 'X', new ItemStack(ancientItemParts, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(ancientItem, 1, 4), new Object[]
		{
			"XXX", "X X", "X X", 'X', new ItemStack(ancientItemParts, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(ancientItem, 1, 5), new Object[]
		{
			"X X", "X X", 'X', new ItemStack(ancientItemParts, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(stairsCharred, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 3)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 3), new Object[]
		{
			new ItemStack(wood, 1, 3)
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(barrel), new Object[]
		{
			"XXX", "YZY", "XXX", 'X', "plankWood", 'Y', Items.iron_ingot, 'Z', Items.bucket
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(armorStandItem), new Object[]
		{
			" X ", " X ", "YYY", 'X', "stickWood", 'Y', Blocks.stone
		}));
		GameRegistry.addShapelessRecipe(new ItemStack(pebble, 4), new Object[]
		{
			Blocks.gravel
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sling), new Object[]
		{
			"XYX", "XZX", " X ", 'X', "stickWood", 'Y', Items.leather, 'Z', Items.string
		}));
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 8), new Object[]
		{
			new ItemStack(wood2, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(stairsLebethron, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 8)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(dye, 2, 1), new Object[]
		{
			asphodel
		});
		GameRegistry.addShapelessRecipe(new ItemStack(orcSteel, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 5)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 5), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', orcSteel
		});
		GameRegistry.addRecipe(new ItemStack(pressurePlateMordorRock), new Object[]
		{
			"XX", 'X', new ItemStack(rock, 1, 0)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(buttonMordorRock), new Object[]
		{
			new ItemStack(rock, 1, 0)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(nauriteGem, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 10)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 10), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', nauriteGem
		});
		GameRegistry.addShapelessRecipe(new ItemStack(guldurilCrystal, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 11)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 11), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', guldurilCrystal
		});
		GameRegistry.addShapelessRecipe(new ItemStack(dye, 2, 0), new Object[]
		{
			elanor
		});
		GameRegistry.addShapelessRecipe(new ItemStack(dye, 2, 1), new Object[]
		{
			niphredil
		});
		GameRegistry.addShapelessRecipe(new ItemStack(quenditeCrystal, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 6)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 6), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', quenditeCrystal
		});
		GameRegistry.addShapelessRecipe(new ItemStack(galvorn, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 8)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 8), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', galvorn
		});
		GameRegistry.addShapelessRecipe(new ItemStack(dwarfSteel, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 7)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 7), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', dwarfSteel
		});
		GameRegistry.addShapelessRecipe(new ItemStack(urukSteel, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 9)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 9), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', urukSteel
		});
		GameRegistry.addRecipe(new ItemStack(pressurePlateGondorRock), new Object[]
		{
			"XX", 'X', new ItemStack(rock, 1, 1)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(buttonGondorRock), new Object[]
		{
			new ItemStack(rock, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(pressurePlateRohanRock), new Object[]
		{
			"XX", 'X', new ItemStack(rock, 1, 2)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(buttonRohanRock), new Object[]
		{
			new ItemStack(rock, 1, 2)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 9), new Object[]
		{
			new ItemStack(wood2, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(stairsBeech, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 9)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(morgulSteel, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 12)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 12), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', morgulSteel
		});
		GameRegistry.addRecipe(new ItemStack(leatherHat), new Object[]
		{
			" X ", "XXX", 'X', Items.leather
		});
		GameRegistry.addRecipe(new LOTRRecipeLeatherHatDye());
		GameRegistry.addRecipe(new LOTRRecipeFeatherDye());
		GameRegistry.addRecipe(new LOTRRecipeLeatherHatFeather());
		GameRegistry.addRecipe(new ItemStack(pressurePlateBlueRock), new Object[]
		{
			"XX", 'X', new ItemStack(rock, 1, 3)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(buttonBlueRock), new Object[]
		{
			new ItemStack(rock, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(slabSingle3, 6, 0), new Object[]
		{
			"XXX", 'X', new ItemStack(rock, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(brick, 4, 14), new Object[]
		{
			"XX", "XX", 'X', new ItemStack(rock, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(wall, 6, 13), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(rock, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(slabSingle3, 6, 1), new Object[]
		{
			"XXX", 'X', new ItemStack(brick, 1, 14)
		});
		GameRegistry.addRecipe(new ItemStack(stairsBlueRockBrick, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick, 1, 14)
		});
		GameRegistry.addRecipe(new ItemStack(wall, 6, 14), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick, 1, 14)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 10), new Object[]
		{
			new ItemStack(wood2, 1, 2)
		});
		GameRegistry.addRecipe(new ItemStack(stairsHolly, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 10)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(rabbitStew), new Object[]
		{
			Items.bowl, rabbitCooked, Items.potato, Items.potato
		});
		GameRegistry.addRecipe(new ItemStack(Blocks.fence, 4), new Object[]
		{
			"XYX", "XYX", 'X', Blocks.planks, 'Y', Items.stick
		});
		for (int i = 0; i <= 14; i++)
		{
			if (i == 1)
			{
				continue;
			}
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fence, 4, i), new Object[]
			{
				"XYX", "XYX", 'X', new ItemStack(planks, 1, i), 'Y', "stickWood"
			}));
		}
		GameRegistry.addShapelessRecipe(new ItemStack(dye, 2, 0), new Object[]
		{
			new ItemStack(doubleFlower, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(pillar, 3, 3), new Object[]
		{
			"X", "X", "X", 'X', new ItemStack(rock, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(slabSingle3, 6, 2), new Object[]
		{
			"XXX", 'X', new ItemStack(pillar, 1, 3)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(Items.gunpowder, 2), new Object[]
		{
			sulfur, saltpeter, new ItemStack(Items.coal, 1, 1)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 15), new Object[]
		{
			saltpeter, Blocks.dirt
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(commandSword), new Object[]
		{
			"X", "Y", "Z", 'X', Items.iron_ingot, 'Y', "ingotBronze", 'Z', "stickWood"
		}));
		GameRegistry.addShapelessRecipe(new ItemStack(daggerOrcPoisoned), new Object[]
		{
			daggerOrc, bottlePoison
		});
		GameRegistry.addShapelessRecipe(new ItemStack(daggerUrukPoisoned), new Object[]
		{
			daggerUruk, bottlePoison
		});
		GameRegistry.addShapelessRecipe(new ItemStack(daggerBronzePoisoned), new Object[]
		{
			daggerBronze, bottlePoison
		});
		GameRegistry.addShapelessRecipe(new ItemStack(daggerIronPoisoned), new Object[]
		{
			daggerIron, bottlePoison
		});
		GameRegistry.addShapelessRecipe(new ItemStack(daggerMithrilPoisoned), new Object[]
		{
			daggerMithril, bottlePoison
		});
		GameRegistry.addShapelessRecipe(new ItemStack(daggerGondorPoisoned), new Object[]
		{
			daggerGondor, bottlePoison
		});
		GameRegistry.addShapelessRecipe(new ItemStack(daggerElvenPoisoned), new Object[]
		{
			daggerElven, bottlePoison
		});
		GameRegistry.addShapelessRecipe(new ItemStack(daggerDwarvenPoisoned), new Object[]
		{
			daggerDwarven, bottlePoison
		});
		GameRegistry.addShapelessRecipe(new ItemStack(daggerRohanPoisoned), new Object[]
		{
			daggerRohan, bottlePoison
		});
		GameRegistry.addShapelessRecipe(new ItemStack(daggerWoodElvenPoisoned), new Object[]
		{
			daggerWoodElven, bottlePoison
		});
		GameRegistry.addShapelessRecipe(new ItemStack(daggerAngmarPoisoned), new Object[]
		{
			daggerAngmar, bottlePoison
		});
		GameRegistry.addShapelessRecipe(new ItemStack(daggerHighElvenPoisoned), new Object[]
		{
			daggerHighElven, bottlePoison
		});
		GameRegistry.addShapelessRecipe(new ItemStack(daggerNearHaradPoisoned), new Object[]
		{
			daggerNearHarad, bottlePoison
		});
		GameRegistry.addShapelessRecipe(new ItemStack(daggerBlueDwarvenPoisoned), new Object[]
		{
			daggerBlueDwarven, bottlePoison
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sulfurMatch, 4), new Object[]
		{
			"X", "Y", 'X', "sulfur", 'Y', "stickWood"
		}));
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 13), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', sulfur
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 14), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', saltpeter
		});
		GameRegistry.addShapelessRecipe(new ItemStack(sulfur, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 13)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(saltpeter, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 14)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 7), new Object[]
		{
			new ItemStack(fruitWood, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(stairsMango, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 7)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(mugMangoJuice), new Object[]
		{
			mug, mango, mango
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 11), new Object[]
		{
			new ItemStack(wood2, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(stairsBanana, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 11)
		});
		GameRegistry.addRecipe(new ItemStack(bananaCakeItem), new Object[]
		{
			"AAA", "BCB", "DDD", 'A', Items.milk_bucket, 'B', banana, 'C', Items.egg, 'D', Items.wheat
		});
		GameRegistry.addRecipe(new ItemStack(bananaBread), new Object[]
		{
			"XYX", 'X', Items.wheat, 'Y', banana
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(lionBedItem), new Object[]
		{
			"XXX", "YYY", 'X', lionFur, 'Y', "plankWood"
		}));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 13), new Object[]
		{
			new ItemStack(doubleFlower, 1, 2)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 1), new Object[]
		{
			new ItemStack(doubleFlower, 1, 3)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 12), new Object[]
		{
			new ItemStack(wood3, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(stairsMaple, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 12)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(mapleSyrup), new Object[]
		{
			new ItemStack(wood3, 1, 0), Items.bowl
		});
		for (int i = 1; i <= 8; i++)
		{
			Object[] ingredients = new Object[i + 1];
			ingredients[0] = mapleSyrup;
			for (int j = 1; j < ingredients.length; j++)
			{
				ingredients[j] = hobbitPancake;
			}
			GameRegistry.addShapelessRecipe(new ItemStack(hobbitPancakeMapleSyrup, i), ingredients);
		}
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 13), new Object[]
		{
			new ItemStack(wood3, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(stairsLarch, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 13)
		});
		GameRegistry.addRecipe(new ItemStack(helmetGemsbok), new Object[]
		{
			"XXX", "X X", 'X', gemsbokHide
		});
		GameRegistry.addRecipe(new ItemStack(bodyGemsbok), new Object[]
		{
			"X X", "XXX", "XXX", 'X', gemsbokHide
		});
		GameRegistry.addRecipe(new ItemStack(legsGemsbok), new Object[]
		{
			"XXX", "X X", "X X", 'X', gemsbokHide
		});
		GameRegistry.addRecipe(new ItemStack(bootsGemsbok), new Object[]
		{
			"X X", "X X", 'X', gemsbokHide
		});
		GameRegistry.addRecipe(new ItemStack(pressurePlateRedRock), new Object[]
		{
			"XX", 'X', new ItemStack(rock, 1, 4)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(buttonRedRock), new Object[]
		{
			new ItemStack(rock, 1, 4)
		});
		GameRegistry.addRecipe(new ItemStack(slabSingle3, 6, 5), new Object[]
		{
			"XXX", 'X', new ItemStack(rock, 1, 4)
		});
		GameRegistry.addRecipe(new ItemStack(brick2, 4, 2), new Object[]
		{
			"XX", "XX", 'X', new ItemStack(rock, 1, 4)
		});
		GameRegistry.addRecipe(new ItemStack(wall2, 6, 2), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(rock, 1, 4)
		});
		GameRegistry.addRecipe(new ItemStack(slabSingle3, 6, 6), new Object[]
		{
			"XXX", 'X', new ItemStack(brick2, 1, 2)
		});
		GameRegistry.addRecipe(new ItemStack(stairsRedRockBrick, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick2, 1, 2)
		});
		GameRegistry.addRecipe(new ItemStack(wall2, 6, 3), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick2, 1, 2)
		});
		GameRegistry.addRecipe(new ItemStack(pillar, 3, 4), new Object[]
		{
			"X", "X", "X", 'X', new ItemStack(rock, 1, 4)
		});
		GameRegistry.addRecipe(new ItemStack(slabSingle3, 6, 7), new Object[]
		{
			"XXX", 'X', new ItemStack(pillar, 1, 4)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 14), new Object[]
		{
			new ItemStack(wood3, 1, 2)
		});
		GameRegistry.addRecipe(new ItemStack(stairsDatePalm, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 14)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(blueDwarfSteel, 9), new Object[]
		{
			new ItemStack(blockOreStorage, 1, 15)
		});
		GameRegistry.addRecipe(new ItemStack(blockOreStorage, 1, 15), new Object[]
		{
			"XXX", "XXX", "XXX", 'X', blueDwarfSteel
		});
		GameRegistry.addRecipe(new ItemStack(thatch, 6), new Object[]
		{
			"XYX", "YXY", "XYX", 'X', Items.wheat, 'Y', Blocks.dirt
		});
		GameRegistry.addRecipe(new ItemStack(slabSingleThatch, 6), new Object[]
		{
			"XXX", 'X', thatch
		});
		GameRegistry.addRecipe(new ItemStack(stairsThatch, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', thatch
		});
		GameRegistry.addRecipe(new ItemStack(horseArmorMithril), new Object[]
		{
			"X  ", "XYX", "XXX", 'X', mithril, 'Y', Items.leather
		});
	}
	
	private static void createWoodenSlabRecipes()
	{
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle, 6, 0), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 0)
		}));
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle, 6, 2), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 2)
		}));
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle, 6, 3), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 3)
		}));
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle, 6, 4), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 4)
		}));
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle, 6, 5), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 5)
		}));
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle, 6, 6), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 6)
		}));
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle, 6, 7), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 7)
		}));
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle2, 6, 0), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 8)
		}));
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle2, 6, 1), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 9)
		}));
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle2, 6, 2), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 10)
		}));
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle2, 6, 3), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 11)
		}));
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle2, 6, 4), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 12)
		}));
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle2, 6, 5), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 13)
		}));
		woodenSlabRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle2, 6, 6), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 14)
		}));
	}
	
	private static void createSmeltingRecipes()
	{
		GameRegistry.addSmelting(wood, new ItemStack(Items.coal, 1, 1), 0.15F);
		GameRegistry.addSmelting(fruitWood, new ItemStack(Items.coal, 1, 1), 0.15F);
		GameRegistry.addSmelting(wood2, new ItemStack(Items.coal, 1, 1), 0.15F);
		GameRegistry.addSmelting(wood3, new ItemStack(Items.coal, 1, 1), 0.15F);
		
		GameRegistry.addSmelting(oreCopper, new ItemStack(copper), 0.35F);
		GameRegistry.addSmelting(oreTin, new ItemStack(tin), 0.35F);
		GameRegistry.addSmelting(oreSilver, new ItemStack(silver), 0.8F);
		GameRegistry.addSmelting(oreNaurite, new ItemStack(nauriteGem), 1F);
		GameRegistry.addSmelting(oreQuendite, new ItemStack(quenditeCrystal), 1F);
		GameRegistry.addSmelting(oreGlowstone, new ItemStack(Items.glowstone_dust), 1F);
		GameRegistry.addSmelting(oreGulduril, new ItemStack(guldurilCrystal), 1F);
		
		GameRegistry.addSmelting(clayMug, new ItemStack(mug), 0.4F);
		GameRegistry.addSmelting(clayPlate, new ItemStack(plate), 0.2F);
		
		GameRegistry.addSmelting(pipeweedLeaf, new ItemStack(pipeweed), 0.25F);
		GameRegistry.addSmelting(rabbitRaw, new ItemStack(rabbitCooked), 0.35F);
		GameRegistry.addSmelting(lionRaw, new ItemStack(lionCooked), 0.35F);
		GameRegistry.addSmelting(zebraRaw, new ItemStack(zebraCooked), 0.35F);
		GameRegistry.addSmelting(rhinoRaw, new ItemStack(rhinoCooked), 0.35F);
		
		addSmeltingXPForItem(bronze, 0.7F);
		addSmeltingXPForItem(mithril, 1F);
		addSmeltingXPForItem(orcSteel, 0.7F);
		addSmeltingXPForItem(dwarfSteel, 0.7F);
		addSmeltingXPForItem(galvorn, 0.8F);
		addSmeltingXPForItem(urukSteel, 0.7F);
		addSmeltingXPForItem(morgulSteel, 0.8F);
		addSmeltingXPForItem(blueDwarfSteel, 0.7F);
	}

	private static void addSmeltingXPForItem(Item item, float xp)
	{
		try
		{
			Field field = FurnaceRecipes.class.getDeclaredFields()[2];
			field.setAccessible(true);
			HashMap map = (HashMap)field.get(FurnaceRecipes.smelting());
			map.put(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), Float.valueOf(xp));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void addRecipeTo(List[] recipeLists, IRecipe recipe)
	{
		for (List list : recipeLists)
		{
			list.add(recipe);
		}
	}
	
	private static void createCommonOrcRecipes()
	{
		addRecipeTo(commonOrcRecipes, new ShapedOreRecipe(new ItemStack(orcBedItem), new Object[]
		{
			"XXX", "YYY", 'X', Blocks.wool, 'Y', "plankWood"
		}));
		addRecipeTo(commonOrcRecipes, new ShapedOreRecipe(new ItemStack(maggotyBread), new Object[]
		{
			"XXX", 'X', Items.wheat
		}));
		for (int i = 0; i <= 2; i++)
		{
			addRecipeTo(commonOrcRecipes, new ShapelessOreRecipe(new ItemStack(orcBomb, 1, i + 8), new Object[]
			{
				new ItemStack(orcBomb, 1, i), Items.lava_bucket
			}));
		}
	}
		
	private static void createMorgulAndAngmarRecipes()
	{
		addRecipeTo(morgulAndAngmarRecipes, new ShapedOreRecipe(new ItemStack(morgulBlade), new Object[]
		{
			"X", "X", "Y", 'X', morgulSteel, 'Y', "stickWood"
		}));
		addRecipeTo(morgulAndAngmarRecipes, new ShapedOreRecipe(new ItemStack(helmetMorgul), new Object[]
		{
			"XXX", "X X", 'X', morgulSteel
		}));
		addRecipeTo(morgulAndAngmarRecipes, new ShapedOreRecipe(new ItemStack(bodyMorgul), new Object[]
		{
			"X X", "XXX", "XXX", 'X', morgulSteel
		}));
		addRecipeTo(morgulAndAngmarRecipes, new ShapedOreRecipe(new ItemStack(legsMorgul), new Object[]
		{
			"XXX", "X X", "X X", 'X', morgulSteel
		}));
		addRecipeTo(morgulAndAngmarRecipes, new ShapedOreRecipe(new ItemStack(bootsMorgul), new Object[]
		{
			"X X", "X X", 'X', morgulSteel
		}));
		addRecipeTo(morgulAndAngmarRecipes, new ShapedOreRecipe(new ItemStack(morgulTorch, 4), new Object[]
		{
			"X", "Y", 'X', guldurilCrystal, 'Y', "stickWood"
		}));
		addRecipeTo(morgulAndAngmarRecipes, new ShapedOreRecipe(new ItemStack(horseArmorMorgul), new Object[]
		{
			"X  ", "XYX", "XXX", 'X', morgulSteel, 'Y', Items.leather
		}));
	}
	
	private static void createCommonElfRecipes()
	{
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(helmetGalvorn), new Object[]
		{
			"XXX", "X X", 'X', galvorn
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(bodyGalvorn), new Object[]
		{
			"X X", "XXX", "XXX", 'X', galvorn
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(legsGalvorn), new Object[]
		{
			"XXX", "X X", "X X", 'X', galvorn
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(bootsGalvorn), new Object[]
		{
			"X X", "X X", 'X', galvorn
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(brick, 4, 11), new Object[]
		{
			"XX", "XX", 'X', Blocks.stone
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(slabSingle2, 6, 3), new Object[]
		{
			"XXX", 'X', new ItemStack(brick, 4, 11)
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(slabSingle2, 6, 4), new Object[]
		{
			"XXX", 'X', new ItemStack(brick, 4, 12)
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(slabSingle2, 6, 5), new Object[]
		{
			"XXX", 'X', new ItemStack(brick, 4, 13)
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(stairsElvenBrick, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick, 1, 11)
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(stairsElvenBrickMossy, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick, 1, 12)
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(stairsElvenBrickCracked, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick, 1, 13)
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(wall, 6, 10), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick, 1, 11)
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(wall, 6, 11), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick, 1, 12)
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(wall, 6, 12), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick, 1, 13)
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(pillar, 3, 1), new Object[]
		{
			"X", "X", "X", 'X', Blocks.stone
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(slabSingle2, 6, 6), new Object[]
		{
			"XXX", 'X', new ItemStack(pillar, 1, 1)
		}));
		addRecipeTo(commonElfRecipes, new ShapedOreRecipe(new ItemStack(slabSingle2, 6, 7), new Object[]
		{
			"XXX", 'X', new ItemStack(pillar, 1, 2)
		}));
	}
	
	private static void createCommonDwarfRecipes()
	{
		addRecipeTo(commonDwarfRecipes, new ShapedOreRecipe(new ItemStack(brick, 4, 6), new Object[]
		{
			"XX", "XX", 'X', Blocks.stone
		}));
		addRecipeTo(commonDwarfRecipes, new ShapedOreRecipe(new ItemStack(stairsDwarvenBrick, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick, 1, 6)
		}));
		addRecipeTo(commonDwarfRecipes, new ShapedOreRecipe(new ItemStack(slabSingle, 6, 7), new Object[]
		{
			"XXX", 'X', new ItemStack(brick, 1, 6)
		}));
		addRecipeTo(commonDwarfRecipes, new ShapedOreRecipe(new ItemStack(wall, 6, 7), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick, 1, 6)
		}));
		addRecipeTo(commonDwarfRecipes, new ShapedOreRecipe(new ItemStack(pillar, 3, 0), new Object[]
		{
			"X", "X", "X", 'X', Blocks.stone
		}));
		addRecipeTo(commonDwarfRecipes, new ShapedOreRecipe(new ItemStack(dwarvenForge), new Object[]
		{
			"XXX", "X X", "XXX", 'X', new ItemStack(brick, 1, 6)
		}));
		addRecipeTo(commonDwarfRecipes, new ShapedOreRecipe(new ItemStack(slabSingle2, 6, 0), new Object[]
		{
			"XXX", 'X', new ItemStack(pillar, 1, 0)
		}));
		addRecipeTo(commonDwarfRecipes, new ShapedOreRecipe(new ItemStack(dwarvenDoorItem), new Object[]
		{
			"XX", "XX", "XX", 'X', Blocks.stone
		}));
		addRecipeTo(commonDwarfRecipes, new ShapedOreRecipe(new ItemStack(dwarvenBedItem), new Object[]
		{
			"XXX", "YYY", 'X', Blocks.wool, 'Y', "plankWood"
		}));
		addRecipeTo(commonDwarfRecipes, new ShapedOreRecipe(new ItemStack(brick, 4, 8), new Object[]
		{
			" X ", "XYX", " X ", 'X', new ItemStack(brick, 1, 6), 'Y', "ingotSilver"
		}));
		addRecipeTo(commonDwarfRecipes, new ShapedOreRecipe(new ItemStack(brick, 4, 9), new Object[]
		{
			" X ", "XYX", " X ", 'X', new ItemStack(brick, 1, 6), 'Y', Items.gold_ingot
		}));
		addRecipeTo(commonDwarfRecipes, new ShapedOreRecipe(new ItemStack(brick, 4, 10), new Object[]
		{
			" X ", "XYX", " X ", 'X', new ItemStack(brick, 1, 6), 'Y', mithril
		}));
	}
	
	private static void createCommonDunedainRecipes()
	{
		addRecipeTo(commonDunedainRecipes, new ShapedOreRecipe(new ItemStack(helmetRanger), new Object[]
		{
			"XXX", "X X", 'X', Items.leather
		}));
		addRecipeTo(commonDunedainRecipes, new ShapedOreRecipe(new ItemStack(bodyRanger), new Object[]
		{
			"X X", "XXX", "XXX", 'X', Items.leather
		}));
		addRecipeTo(commonDunedainRecipes, new ShapedOreRecipe(new ItemStack(legsRanger), new Object[]
		{
			"XXX", "X X", "X X", 'X', Items.leather
		}));
		addRecipeTo(commonDunedainRecipes, new ShapedOreRecipe(new ItemStack(bootsRanger), new Object[]
		{
			"X X", "X X", 'X', Items.leather
		}));
	}
	
    private static void createMorgulRecipes()
    {
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(brick, 4, 0), new Object[]
		{
			"XX", "XX", 'X', new ItemStack(rock, 1, 0)
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(morgulTable), new Object[]
		{
			"XX", "YY", 'X', "plankWood", 'Y', new ItemStack(rock, 1, 0)
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(scimitarOrc), new Object[]
		{
			"X", "X", "Y", 'X', orcSteel, 'Y', "stickWood"
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(battleaxeOrc), new Object[]
		{
			"XXX", "XYX", " Y ", 'X', orcSteel, 'Y', "stickWood"
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(daggerOrc), new Object[]
		{
			"X", "Y", 'X', orcSteel, 'Y', "stickWood"
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(helmetOrc), new Object[]
		{
			"XXX", "X X", 'X', orcSteel
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(bodyOrc), new Object[]
		{
			"X X", "XXX", "XXX", 'X', orcSteel
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(legsOrc), new Object[]
		{
			"XXX", "X X", "X X", 'X', orcSteel
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(bootsOrc), new Object[]
		{
			"X X", "X X", 'X', orcSteel
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle, 6, 0), new Object[]
		{
			"XXX", 'X', new ItemStack(rock, 1, 0)
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle, 6, 1), new Object[]
		{
			"XXX", 'X', new ItemStack(brick, 1, 0)
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(stairsMordorBrick, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick, 1, 0)
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(wall, 6, 0), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(rock, 1, 0)
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(wall, 6, 1), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick, 1, 0)
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(orcBomb, 4), new Object[]
		{
			"XYX", "YXY", "XYX", 'X', Items.gunpowder, 'Y', orcSteel
		}));
		morgulRecipes.add(new ShapelessOreRecipe(new ItemStack(orcBomb, 1, 1), new Object[]
		{
			new ItemStack(orcBomb, 1, 0), Items.gunpowder, orcSteel
		}));
		morgulRecipes.add(new ShapelessOreRecipe(new ItemStack(orcBomb, 1, 2), new Object[]
		{
			new ItemStack(orcBomb, 1, 1), Items.gunpowder, orcSteel
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(orcTorchItem, 2), new Object[]
		{
			"X", "Y", "Y", 'X', nauriteGem, 'Y', "stickWood"
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(spearOrc), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', orcSteel, 'Y', "stickWood"
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(orcSteelBars, 16), new Object[]
		{
			"XXX", "XXX", 'X', orcSteel
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(LOTRMod.orcBow), new Object[]
		{
			" XY", "X Y", " XY", 'X', orcSteel, 'Y', Items.string
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(shovelOrc), new Object[]
		{
			"X", "Y", "Y", 'X', orcSteel, 'Y', "stickWood"
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(pickaxeOrc), new Object[]
		{
			"XXX", " Y ", " Y ", 'X', orcSteel, 'Y', "stickWood"
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(axeOrc), new Object[]
		{
			"XX", "XY", " Y", 'X', orcSteel, 'Y', "stickWood"
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(hoeOrc), new Object[]
		{
			"XX", " Y", " Y", 'X', orcSteel, 'Y', "stickWood"
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(hammerOrc), new Object[]
		{
			"XYX", "XYX", " Y ", 'X', orcSteel, 'Y', "stickWood"
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(chandelier, 2, 7), new Object[]
		{
			" X ", "YZY", 'X', "stickWood", 'Y', orcTorchItem, 'Z', orcSteel
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle2, 6, 2), new Object[]
		{
			"XXX", 'X', new ItemStack(brick, 1, 7)
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(stairsMordorBrickCracked, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick, 1, 7)
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(wall, 6, 9), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick, 1, 7)
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(orcForge), new Object[]
		{
			"XXX", "X X", "XXX", 'X', new ItemStack(brick, 1, 0)
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(banner, 1, 2), new Object[]
		{
			"X", "Y", "Z", 'X', Blocks.wool, 'Y', "stickWood", 'Z', "plankWood"
		}));
		morgulRecipes.add(new ShapedOreRecipe(new ItemStack(wargArmorMordor), new Object[]
		{
			"X  ", "XYX", "XXX", 'X', orcSteel, 'Y', Items.leather
		}));
    }
	
	private static void createElvenRecipes()
	{
		elvenRecipes.add(new ShapelessOreRecipe(new ItemStack(planks, 4, 1), new Object[]
		{
			new ItemStack(wood, 1, 1)
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(woodSlabSingle, 6, 1), new Object[]
		{
			"XXX", 'X', new ItemStack(planks, 1, 1)
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(stairsMallorn, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 1)
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(elvenTable), new Object[]
		{
			"XX", "XX", 'X', new ItemStack(planks, 1, 1)
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(mallornStick, 4), new Object[]
		{
			"X", "X", 'X', new ItemStack(planks, 1, 1)
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(shovelMallorn), new Object[]
		{
			"X", "Y", "Y", 'X', new ItemStack(planks, 1, 1), 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(pickaxeMallorn), new Object[]
		{
			"XXX", " Y ", " Y ", 'X', new ItemStack(planks, 1, 1), 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(axeMallorn), new Object[]
		{
			"XX", "XY", " Y", 'X', new ItemStack(planks, 1, 1), 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(swordMallorn), new Object[]
		{
			"X", "X", "Y", 'X', new ItemStack(planks, 1, 1), 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(hoeMallorn), new Object[]
		{
			"XX", " Y", " Y", 'X', new ItemStack(planks, 1, 1), 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(shovelElven), new Object[]
		{
			"X", "Y", "Y", 'X', Items.iron_ingot, 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(pickaxeElven), new Object[]
		{
			"XXX", " Y ", " Y ", 'X', Items.iron_ingot, 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(axeElven), new Object[]
		{
			"XX", "XY", " Y", 'X', Items.iron_ingot, 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(swordElven), new Object[]
		{
			"X", "X", "Y", 'X', Items.iron_ingot, 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(hoeElven), new Object[]
		{
			"XX", " Y", " Y", 'X', Items.iron_ingot, 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(spearElven), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', Items.iron_ingot, 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(mallornBow), new Object[]
		{
			" XY", "X Y", " XY", 'X', mallornStick, 'Y', Items.string
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(helmetElven), new Object[]
		{
			"XXX", "X X", 'X', Items.iron_ingot
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(bodyElven), new Object[]
		{
			"X X", "XXX", "XXX", 'X', Items.iron_ingot
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(legsElven), new Object[]
		{
			"XXX", "X X", "X X", 'X', Items.iron_ingot
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(bootsElven), new Object[]
		{
			"X X", "X X", 'X', Items.iron_ingot
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(mallornLadder, 3), new Object[]
		{
			"X X", "XXX", "X X", 'X', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(elvenBow), new Object[]
		{
			" XY", "X Y", " XY", 'X', Items.iron_ingot, 'Y', Items.string
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(mallornTorch, 4), new Object[]
		{
			"X", "Y", 'X', quenditeCrystal, 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(daggerElven), new Object[]
		{
			"X", "Y", 'X', Items.iron_ingot, 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(elvenBedItem), new Object[]
		{
			"XXX", "YYY", 'X', Blocks.wool, 'Y', new ItemStack(planks, 1, 1)
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(chandelier, 2, 5), new Object[]
		{
			" X ", "YZY", 'X', mallornStick, 'Y', mallornTorch, 'Z', Items.iron_ingot
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(fence, 4, 1), new Object[]
		{
			"XYX", "XYX", 'X', new ItemStack(planks, 1, 1), 'Y', mallornStick
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(banner, 1, 3), new Object[]
		{
			"X", "Y", "Z", 'X', Blocks.wool, 'Y', mallornStick, 'Z', new ItemStack(planks, 1, 1)
		}));
		elvenRecipes.add(new ShapedOreRecipe(new ItemStack(horseArmorGaladhrim), new Object[]
		{
			"X  ", "XYX", "XXX", 'X', Items.iron_ingot, 'Y', Items.leather
		}));
	}
	
	private static void createDwarvenRecipes()
	{
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(dwarvenTable), new Object[]
		{
			"XX", "YY", 'X', "plankWood", 'Y', new ItemStack(brick, 1, 6)
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(shovelDwarven), new Object[]
		{
			"X", "Y", "Y", 'X', dwarfSteel, 'Y', "stickWood"
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(pickaxeDwarven), new Object[]
		{
			"XXX", " Y ", " Y ", 'X', dwarfSteel, 'Y', "stickWood"
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(axeDwarven), new Object[]
		{
			"XX", "XY", " Y", 'X', dwarfSteel, 'Y', "stickWood"
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(swordDwarven), new Object[]
		{
			"X", "X", "Y", 'X', dwarfSteel, 'Y', "stickWood"
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(hoeDwarven), new Object[]
		{
			"XX", " Y", " Y", 'X', dwarfSteel, 'Y', "stickWood"
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(daggerDwarven), new Object[]
		{
			"X", "Y", 'X', dwarfSteel, 'Y', "stickWood"
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(battleaxeDwarven), new Object[]
		{
			"XXX", "XYX", " Y ", 'X', dwarfSteel, 'Y', "stickWood"
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(hammerDwarven), new Object[]
		{
			"XYX", "XYX", " Y ", 'X', dwarfSteel, 'Y', "stickWood"
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(helmetDwarven), new Object[]
		{
			"XXX", "X X", 'X', dwarfSteel
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(bodyDwarven), new Object[]
		{
			"X X", "XXX", "XXX", 'X', dwarfSteel
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(legsDwarven), new Object[]
		{
			"XXX", "X X", "X X", 'X', dwarfSteel
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(bootsDwarven), new Object[]
		{
			"X X", "X X", 'X', dwarfSteel
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(throwingAxeDwarven), new Object[]
		{
			" X ", " YX", "Y  ", 'X', dwarfSteel, 'Y', "stickWood"
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(chandelier, 2, 8), new Object[]
		{
			" X ", "YZY", 'X', "stickWood", 'Y', Blocks.torch, 'Z', dwarfSteel
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(mattockDwarven), new Object[]
		{
			"XXX", "XY ", " Y ", 'X', dwarfSteel, 'Y', "stickWood"
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(banner, 1, 7), new Object[]
		{
			"X", "Y", "Z", 'X', Blocks.wool, 'Y', "stickWood", 'Z', "plankWood"
		}));
		dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(spearDwarven), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', dwarfSteel, 'Y', "stickWood"
		}));
	}
	
	private static void createUrukRecipes()
	{
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(urukTable), new Object[]
		{
			"XX", "YY", 'X', "plankWood", 'Y', urukSteel
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(orcTorchItem, 2), new Object[]
		{
			"X", "Y", "Y", 'X', Items.coal, 'Y', "stickWood"
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(shovelUruk), new Object[]
		{
			"X", "Y", "Y", 'X', urukSteel, 'Y', "stickWood"
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(pickaxeUruk), new Object[]
		{
			"XXX", " Y ", " Y ", 'X', urukSteel, 'Y', "stickWood"
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(axeUruk), new Object[]
		{
			"XX", "XY", " Y", 'X', urukSteel, 'Y', "stickWood"
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(scimitarUruk), new Object[]
		{
			"X", "X", "Y", 'X', urukSteel, 'Y', "stickWood"
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(hoeUruk), new Object[]
		{
			"XX", " Y", " Y", 'X', urukSteel, 'Y', "stickWood"
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(daggerUruk), new Object[]
		{
			"X", "Y", 'X', urukSteel, 'Y', "stickWood"
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(battleaxeUruk), new Object[]
		{
			"XXX", "XYX", " Y ", 'X', urukSteel, 'Y', "stickWood"
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(hammerUruk), new Object[]
		{
			"XYX", "XYX", " Y ", 'X', urukSteel, 'Y', "stickWood"
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(spearUruk), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', urukSteel, 'Y', "stickWood"
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(helmetUruk), new Object[]
		{
			"XXX", "X X", 'X', urukSteel
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(bodyUruk), new Object[]
		{
			"X X", "XXX", "XXX", 'X', urukSteel
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(legsUruk), new Object[]
		{
			"XXX", "X X", "X X", 'X', urukSteel
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(bootsUruk), new Object[]
		{
			"X X", "X X", 'X', urukSteel
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(urukCrossbow), new Object[]
		{
			"XXY", "ZYX", "YZX", 'X', urukSteel, 'Y', "stickWood", 'Z', Items.string
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(chandelier, 2, 9), new Object[]
		{
			" X ", "YZY", 'X', "stickWood", 'Y', orcTorchItem, 'Z', urukSteel
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(orcForge), new Object[]
		{
			"XXX", "X X", "XXX", 'X', Blocks.cobblestone
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(orcBomb, 4), new Object[]
		{
			"XYX", "YXY", "XYX", 'X', Items.gunpowder, 'Y', urukSteel
		}));
		urukRecipes.add(new ShapelessOreRecipe(new ItemStack(orcBomb, 1, 1), new Object[]
		{
			new ItemStack(orcBomb, 1, 0), Items.gunpowder, urukSteel
		}));
		urukRecipes.add(new ShapelessOreRecipe(new ItemStack(orcBomb, 1, 2), new Object[]
		{
			new ItemStack(orcBomb, 1, 1), Items.gunpowder, urukSteel
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(banner, 1, 6), new Object[]
		{
			"X", "Y", "Z", 'X', Blocks.wool, 'Y', "stickWood", 'Z', "plankWood"
		}));
		urukRecipes.add(new ShapedOreRecipe(new ItemStack(wargArmorUruk), new Object[]
		{
			"X  ", "XYX", "XXX", 'X', urukSteel, 'Y', Items.leather
		}));
	}
	
	private static void createWoodElvenRecipes()
	{
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(woodElvenTable), new Object[]
		{
			"XX", "XX", 'X', new ItemStack(LOTRMod.planks, 1, 2)
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(woodElvenBedItem), new Object[]
		{
			"XXX", "YYY", 'X', Blocks.wool, 'Y', new ItemStack(planks, 1, 2)
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(helmetWoodElvenScout), new Object[]
		{
			"XXX", "X X", 'X', Items.leather
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(bodyWoodElvenScout), new Object[]
		{
			"X X", "XXX", "XXX", 'X', Items.leather
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(legsWoodElvenScout), new Object[]
		{
			"XXX", "X X", "X X", 'X', Items.leather
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(bootsWoodElvenScout), new Object[]
		{
			"X X", "X X", 'X', Items.leather
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(mirkwoodBow), new Object[]
		{
			" XY", "X Y", " XY", 'X', "stickWood", 'Y', Items.string
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(woodElvenTorch, 4), new Object[]
		{
			"X", "Y", "Z", 'X', new ItemStack(leaves, 1, 3), 'Y', Items.coal, 'Z', "stickWood" 
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(chandelier, 2, 6), new Object[]
		{
			" X ", "YZY", 'X', "stickWood", 'Y', woodElvenTorch, 'Z', new ItemStack(planks, 1, 2)
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(shovelWoodElven), new Object[]
		{
			"X", "Y", "Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(pickaxeWoodElven), new Object[]
		{
			"XXX", " Y ", " Y ", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(axeWoodElven), new Object[]
		{
			"XX", "XY", " Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(swordWoodElven), new Object[]
		{
			"X", "X", "Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(hoeWoodElven), new Object[]
		{
			"XX", " Y", " Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(daggerWoodElven), new Object[]
		{
			"X", "Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(spearWoodElven), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(helmetWoodElven), new Object[]
		{
			"XXX", "X X", 'X', Items.iron_ingot
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(bodyWoodElven), new Object[]
		{
			"X X", "XXX", "XXX", 'X', Items.iron_ingot
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(legsWoodElven), new Object[]
		{
			"XXX", "X X", "X X", 'X', Items.iron_ingot
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(bootsWoodElven), new Object[]
		{
			"X X", "X X", 'X', Items.iron_ingot
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(banner, 1, 4), new Object[]
		{
			"X", "Y", "Z", 'X', Blocks.wool, 'Y', "stickWood", 'Z', "plankWood"
		}));
		woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(elkArmorWoodElven), new Object[]
		{
			"X  ", "XYX", "XXX", 'X', Items.iron_ingot, 'Y', Items.leather
		}));
	}
	
	private static void createGondorianRecipes()
	{
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(gondorianTable), new Object[]
		{
			"XX", "YY", 'X', "plankWood", 'Y', new ItemStack(LOTRMod.rock, 1, 1)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(beacon), new Object[]
		{
			"XXX", "XXX", "YYY", 'X', "logWood", 'Y', Blocks.cobblestone
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle, 6, 2), new Object[]
		{
			"XXX", 'X', new ItemStack(rock, 1, 1)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(brick, 4, 1), new Object[]
		{
			"XX", "XX", 'X', new ItemStack(rock, 1, 1)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle, 6, 3), new Object[]
		{
			"XXX", 'X', new ItemStack(brick, 1, 1)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(stairsGondorBrick, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick, 1, 1)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(wall, 6, 2), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(rock, 1, 1)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(wall, 6, 3), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick, 1, 1)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle, 6, 4), new Object[]
		{
			"XXX", 'X', new ItemStack(brick, 1, 2)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(stairsGondorBrickMossy, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick, 1, 2)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(wall, 6, 4), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick, 1, 2)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle, 6, 5), new Object[]
		{
			"XXX", 'X', new ItemStack(brick, 1, 3)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(stairsGondorBrickCracked, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick, 1, 3)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(wall, 6, 5), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick, 1, 3)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(brick, 1, 5), new Object[]
		{
			"XX", "XX", 'X', new ItemStack(brick, 1, 1)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(helmetGondor), new Object[]
		{
			"XXX", "X X", 'X', Items.iron_ingot
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(bodyGondor), new Object[]
		{
			"X X", "XXX", "XXX", 'X', Items.iron_ingot
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(legsGondor), new Object[]
		{
			"XXX", "X X", "X X", 'X', Items.iron_ingot
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(bootsGondor), new Object[]
		{
			"X X", "X X", 'X', Items.iron_ingot
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(swordGondor), new Object[]
		{
			"X", "X", "Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(spearGondor), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(daggerGondor), new Object[]
		{
			"X", "Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(hammerGondor), new Object[]
		{
			"XYX", "XYX", " Y ", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(helmetGondorWinged), new Object[]
		{
			"XYX", 'X', Items.feather, 'Y', new ItemStack(helmetGondor, 1, 0)
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(banner, 1, 0), new Object[]
		{
			"X", "Y", "Z", 'X', Blocks.wool, 'Y', "stickWood", 'Z', "plankWood"
		}));
		gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(horseArmorGondor), new Object[]
		{
			"X  ", "XYX", "XXX", 'X', Items.iron_ingot, 'Y', Items.leather
		}));
	}
	
	private static void createRohirricRecipes()
	{
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(rohirricTable), new Object[]
		{
			"XX", "XX", 'X', "plankWood"
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle2, 6, 1), new Object[]
		{
			"XXX", 'X', new ItemStack(rock, 1, 2)
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(brick, 4, 4), new Object[]
		{
			"XX", "XX", 'X', new ItemStack(rock, 1, 2)
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle, 6, 6), new Object[]
		{
			"XXX", 'X', new ItemStack(brick, 1, 4)
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(stairsRohanBrick, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick, 1, 4)
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(wall, 6, 8), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(rock, 1, 2)
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(wall, 6, 6), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick, 1, 4)
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(swordRohan), new Object[]
		{
			"X", "X", "Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(daggerRohan), new Object[]
		{
			"X", "Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(spearRohan), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(helmetRohan), new Object[]
		{
			"YYY", "X X", 'X', Items.leather, 'Y', Items.iron_ingot
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(bodyRohan), new Object[]
		{
			"X X", "YXY", "YYY", 'X', Items.leather, 'Y', Items.iron_ingot
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(legsRohan), new Object[]
		{
			"XXX", "Y Y", "Y Y", 'X', Items.leather, 'Y', Items.iron_ingot
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(bootsRohan), new Object[]
		{
			"Y Y", "X X", 'X', Items.leather, 'Y', Items.iron_ingot
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(banner, 1, 1), new Object[]
		{
			"X", "Y", "Z", 'X', Blocks.wool, 'Y', "stickWood", 'Z', "plankWood"
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(battleaxeRohan), new Object[]
		{
			"XXX", "XYX", " Y ", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(horseArmorRohan), new Object[]
		{
			"X  ", "XYX", "XXX", 'X', Items.iron_ingot, 'Y', Items.leather
		}));
	}
	
	private static void createDunlendingRecipes()
	{
		dunlendingRecipes.add(new ShapedOreRecipe(new ItemStack(dunlendingTable), new Object[]
		{
			"XX", "YY", 'X', "plankWood", 'Y', Blocks.cobblestone
		}));
		dunlendingRecipes.add(new ShapedOreRecipe(new ItemStack(helmetDunlending), new Object[]
		{
			"YYY", "X X", 'X', Items.leather, 'Y', Items.iron_ingot
		}));
		dunlendingRecipes.add(new ShapedOreRecipe(new ItemStack(bodyDunlending), new Object[]
		{
			"X X", "YXY", "YYY", 'X', Items.leather, 'Y', Items.iron_ingot
		}));
		dunlendingRecipes.add(new ShapedOreRecipe(new ItemStack(legsDunlending), new Object[]
		{
			"XXX", "Y Y", "Y Y", 'X', Items.leather, 'Y', Items.iron_ingot
		}));
		dunlendingRecipes.add(new ShapedOreRecipe(new ItemStack(bootsDunlending), new Object[]
		{
			"Y Y", "X X", 'X', Items.leather, 'Y', Items.iron_ingot
		}));
		dunlendingRecipes.add(new ShapedOreRecipe(new ItemStack(dunlendingClub), new Object[]
		{
			"X", "X", "X", 'X', "plankWood"
		}));
		dunlendingRecipes.add(new ShapedOreRecipe(new ItemStack(dunlendingTrident), new Object[]
		{
			" XX", " YX", "Y  ", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		dunlendingRecipes.add(new ShapedOreRecipe(new ItemStack(banner, 1, 5), new Object[]
		{
			"X", "Y", "Z", 'X', Blocks.wool, 'Y', "stickWood", 'Z', "plankWood"
		}));
	}
	
    private static void createAngmarRecipes()
    {
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(brick2, 4, 0), new Object[]
		{
			"XX", "XX", 'X', Blocks.stone
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(angmarTable), new Object[]
		{
			"XX", "YY", 'X', "plankWood", 'Y', orcSteel
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(swordAngmar), new Object[]
		{
			"X", "X", "Y", 'X', orcSteel, 'Y', "stickWood"
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(battleaxeAngmar), new Object[]
		{
			"XXX", "XYX", " Y ", 'X', orcSteel, 'Y', "stickWood"
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(daggerAngmar), new Object[]
		{
			"X", "Y", 'X', orcSteel, 'Y', "stickWood"
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(helmetAngmar), new Object[]
		{
			"XXX", "X X", 'X', orcSteel
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(bodyAngmar), new Object[]
		{
			"X X", "XXX", "XXX", 'X', orcSteel
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(legsAngmar), new Object[]
		{
			"XXX", "X X", "X X", 'X', orcSteel
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(bootsAngmar), new Object[]
		{
			"X X", "X X", 'X', orcSteel
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle3, 6, 3), new Object[]
		{
			"XXX", 'X', new ItemStack(brick2, 1, 0)
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(stairsAngmarBrick, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick2, 1, 0)
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(wall2, 6, 0), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick2, 1, 0)
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(orcBomb, 4), new Object[]
		{
			"XYX", "YXY", "XYX", 'X', Items.gunpowder, 'Y', orcSteel
		}));
		angmarRecipes.add(new ShapelessOreRecipe(new ItemStack(orcBomb, 1, 1), new Object[]
		{
			new ItemStack(orcBomb, 1, 0), Items.gunpowder, orcSteel
		}));
		angmarRecipes.add(new ShapelessOreRecipe(new ItemStack(orcBomb, 1, 2), new Object[]
		{
			new ItemStack(orcBomb, 1, 1), Items.gunpowder, orcSteel
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(orcTorchItem, 2), new Object[]
		{
			"X", "Y", "Y", 'X', Items.coal, 'Y', "stickWood"
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(spearAngmar), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', orcSteel, 'Y', "stickWood"
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(orcSteelBars, 16), new Object[]
		{
			"XXX", "XXX", 'X', orcSteel
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(LOTRMod.orcBow), new Object[]
		{
			" XY", "X Y", " XY", 'X', orcSteel, 'Y', Items.string
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(shovelAngmar), new Object[]
		{
			"X", "Y", "Y", 'X', orcSteel, 'Y', "stickWood"
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(pickaxeAngmar), new Object[]
		{
			"XXX", " Y ", " Y ", 'X', orcSteel, 'Y', "stickWood"
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(axeAngmar), new Object[]
		{
			"XX", "XY", " Y", 'X', orcSteel, 'Y', "stickWood"
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(hoeAngmar), new Object[]
		{
			"XX", " Y", " Y", 'X', orcSteel, 'Y', "stickWood"
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(hammerAngmar), new Object[]
		{
			"XYX", "XYX", " Y ", 'X', orcSteel, 'Y', "stickWood"
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(chandelier, 2, 7), new Object[]
		{
			" X ", "YZY", 'X', "stickWood", 'Y', orcTorchItem, 'Z', orcSteel
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle3, 6, 4), new Object[]
		{
			"XXX", 'X', new ItemStack(brick2, 1, 1)
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(stairsAngmarBrickCracked, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick2, 1, 1)
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(wall2, 6, 1), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick2, 1, 1)
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(orcForge), new Object[]
		{
			"XXX", "X X", "XXX", 'X', new ItemStack(brick2, 1, 0)
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(banner, 1, 8), new Object[]
		{
			"X", "Y", "Z", 'X', Blocks.wool, 'Y', "stickWood", 'Z', "plankWood"
		}));
		angmarRecipes.add(new ShapedOreRecipe(new ItemStack(wargArmorAngmar), new Object[]
		{
			"X  ", "XYX", "XXX", 'X', orcSteel, 'Y', Items.leather
		}));
    }
    
    private static void createNearHaradRecipes()
    {
    	nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(nearHaradTable), new Object[]
		{
			"XX", "YY", 'X', "plankWood", 'Y', new ItemStack(Blocks.sandstone, 1, 0)
		}));
    	nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(banner, 1, 9), new Object[]
		{
			"X", "Y", "Z", 'X', Blocks.wool, 'Y', "stickWood", 'Z', "plankWood"
		}));
    	nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(scimitarNearHarad), new Object[]
		{
			"X", "X", "Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(helmetNearHarad), new Object[]
		{
			"XXX", "X X", 'X', Items.iron_ingot
		}));
		nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(bodyNearHarad), new Object[]
		{
			"X X", "XXX", "XXX", 'X', Items.iron_ingot
		}));
		nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(legsNearHarad), new Object[]
		{
			"XXX", "X X", "X X", 'X', Items.iron_ingot
		}));
		nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(bootsNearHarad), new Object[]
		{
			"X X", "X X", 'X', Items.iron_ingot
		}));
		nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(daggerNearHarad), new Object[]
		{
			"X", "Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(spearNearHarad), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(nearHaradBow), new Object[]
		{
			" XY", "X Y", " XY", 'X', "stickWood", 'Y', Items.string
		}));
		nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(brick, 4, 15), new Object[]
		{
			"XX", "XX", 'X', new ItemStack(Blocks.sandstone, 1, 0)
		}));
		nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle4, 6, 0), new Object[]
		{
			"XXX", 'X', new ItemStack(LOTRMod.brick, 1, 15)
		}));
		nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(stairsNearHaradBrick, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick, 1, 15)
		}));
		nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(wall, 6, 15), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(LOTRMod.brick, 1, 15)
		}));
    }
    
    private static void createHighElvenRecipes()
	{
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(highElvenTable), new Object[]
		{
			"XX", "XX", 'X', "plankWood"
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(elvenBow), new Object[]
		{
			" XY", "X Y", " XY", 'X', Items.iron_ingot, 'Y', Items.string
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(shovelHighElven), new Object[]
		{
			"X", "Y", "Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(pickaxeHighElven), new Object[]
		{
			"XXX", " Y ", " Y ", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(axeHighElven), new Object[]
		{
			"XX", "XY", " Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(swordHighElven), new Object[]
		{
			"X", "X", "Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(hoeHighElven), new Object[]
		{
			"XX", " Y", " Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(daggerHighElven), new Object[]
		{
			"X", "Y", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(spearHighElven), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', Items.iron_ingot, 'Y', "stickWood"
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(helmetHighElven), new Object[]
		{
			"XXX", "X X", 'X', Items.iron_ingot
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(bodyHighElven), new Object[]
		{
			"X X", "XXX", "XXX", 'X', Items.iron_ingot
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(legsHighElven), new Object[]
		{
			"XXX", "X X", "X X", 'X', Items.iron_ingot
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(bootsHighElven), new Object[]
		{
			"X X", "X X", 'X', Items.iron_ingot
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(banner, 1, 10), new Object[]
		{
			"X", "Y", "Z", 'X', Blocks.wool, 'Y', "stickWood", 'Z', "plankWood"
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(highElvenTorch, 4), new Object[]
		{
			"X", "Y", 'X', LOTRMod.quenditeCrystal, 'Y', "stickWood"
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(chandelier, 2, 10), new Object[]
		{
			" X ", "YZY", 'X', "stickWood", 'Y', highElvenTorch, 'Z', Items.iron_ingot
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(highElvenBedItem), new Object[]
		{
			"XXX", "YYY", 'X', Blocks.wool, 'Y', "plankWood"
		}));
		highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(horseArmorHighElven), new Object[]
		{
			"X  ", "XYX", "XXX", 'X', Items.iron_ingot, 'Y', Items.leather
		}));
	}
    
	private static void createBlueMountainsRecipes()
	{
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(blueDwarvenTable), new Object[]
		{
			"XX", "YY", 'X', "plankWood", 'Y', new ItemStack(brick, 1, 14)
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(shovelBlueDwarven), new Object[]
		{
			"X", "Y", "Y", 'X', blueDwarfSteel, 'Y', "stickWood"
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(pickaxeBlueDwarven), new Object[]
		{
			"XXX", " Y ", " Y ", 'X', blueDwarfSteel, 'Y', "stickWood"
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(axeBlueDwarven), new Object[]
		{
			"XX", "XY", " Y", 'X', blueDwarfSteel, 'Y', "stickWood"
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(swordBlueDwarven), new Object[]
		{
			"X", "X", "Y", 'X', blueDwarfSteel, 'Y', "stickWood"
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(hoeBlueDwarven), new Object[]
		{
			"XX", " Y", " Y", 'X', blueDwarfSteel, 'Y', "stickWood"
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(daggerBlueDwarven), new Object[]
		{
			"X", "Y", 'X', blueDwarfSteel, 'Y', "stickWood"
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(battleaxeBlueDwarven), new Object[]
		{
			"XXX", "XYX", " Y ", 'X', blueDwarfSteel, 'Y', "stickWood"
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(hammerBlueDwarven), new Object[]
		{
			"XYX", "XYX", " Y ", 'X', blueDwarfSteel, 'Y', "stickWood"
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(helmetBlueDwarven), new Object[]
		{
			"XXX", "X X", 'X', blueDwarfSteel
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(bodyBlueDwarven), new Object[]
		{
			"X X", "XXX", "XXX", 'X', blueDwarfSteel
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(legsBlueDwarven), new Object[]
		{
			"XXX", "X X", "X X", 'X', blueDwarfSteel
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(bootsBlueDwarven), new Object[]
		{
			"X X", "X X", 'X', blueDwarfSteel
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(throwingAxeBlueDwarven), new Object[]
		{
			" X ", " YX", "Y  ", 'X', blueDwarfSteel, 'Y', "stickWood"
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(chandelier, 2, 11), new Object[]
		{
			" X ", "YZY", 'X', "stickWood", 'Y', Blocks.torch, 'Z', blueDwarfSteel
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(mattockBlueDwarven), new Object[]
		{
			"XXX", "XY ", " Y ", 'X', blueDwarfSteel, 'Y', "stickWood"
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(banner, 1, 11), new Object[]
		{
			"X", "Y", "Z", 'X', Blocks.wool, 'Y', "stickWood", 'Z', "plankWood"
		}));
		blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(spearBlueDwarven), new Object[]
		{
			"  X", " Y ", "Y  ", 'X', blueDwarfSteel, 'Y', "stickWood"
		}));
	}
	
	private static void createRangerRecipes()
	{
		rangerRecipes.add(new ShapedOreRecipe(new ItemStack(rangerTable), new Object[]
		{
			"XX", "XX", 'X', "plankWood"
		}));
		rangerRecipes.add(new ShapedOreRecipe(new ItemStack(banner, 1, 12), new Object[]
		{
			"X", "Y", "Z", 'X', Blocks.wool, 'Y', "stickWood", 'Z', "plankWood"
		}));
		rangerRecipes.add(new ShapedOreRecipe(new ItemStack(brick2, 4, 3), new Object[]
		{
			"XX", "XX", 'X', Blocks.stone
		}));
		rangerRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle4, 6, 1), new Object[]
		{
			"XXX", 'X', new ItemStack(brick2, 1, 3)
		}));
		rangerRecipes.add(new ShapedOreRecipe(new ItemStack(stairsArnorBrick, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick2, 1, 3)
		}));
		rangerRecipes.add(new ShapedOreRecipe(new ItemStack(wall2, 6, 4), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick2, 1, 3)
		}));
		rangerRecipes.add(new ShapedOreRecipe(new ItemStack(brick2, 1, 6), new Object[]
		{
			"XX", "XX", 'X', new ItemStack(brick2, 1, 3)
		}));
		rangerRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle4, 6, 2), new Object[]
		{
			"XXX", 'X', new ItemStack(brick2, 1, 4)
		}));
		rangerRecipes.add(new ShapedOreRecipe(new ItemStack(stairsArnorBrickMossy, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick2, 1, 4)
		}));
		rangerRecipes.add(new ShapedOreRecipe(new ItemStack(wall2, 6, 5), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick2, 1, 4)
		}));
		rangerRecipes.add(new ShapedOreRecipe(new ItemStack(slabSingle4, 6, 3), new Object[]
		{
			"XXX", 'X', new ItemStack(brick2, 1, 5)
		}));
		rangerRecipes.add(new ShapedOreRecipe(new ItemStack(stairsArnorBrickCracked, 4), new Object[]
		{
			"X  ", "XX ", "XXX", 'X', new ItemStack(brick2, 1, 5)
		}));
		rangerRecipes.add(new ShapedOreRecipe(new ItemStack(wall2, 6, 6), new Object[]
		{
			"XXX", "XXX", 'X', new ItemStack(brick2, 1, 5)
		}));
	}

    public static ItemStack findMatchingRecipe(List recipeList, InventoryCrafting inv, World world)
    {
		for (int i = 0; i < recipeList.size(); i++)
		{
			IRecipe recipe = (IRecipe)recipeList.get(i);

			if (recipe.matches(inv, world))
			{
				return recipe.getCraftingResult(inv);
			}
		}

		return null;
    }
    
    public static boolean checkItemEquals(ItemStack target, ItemStack input)
    {
        return target.getItem().equals(input.getItem()) && (target.getItemDamage() == OreDictionary.WILDCARD_VALUE || target.getItemDamage() == input.getItemDamage());
    }
}
