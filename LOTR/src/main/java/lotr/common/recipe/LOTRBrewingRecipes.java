package lotr.common.recipe;

import java.util.ArrayList;
import java.util.Iterator;

import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityBarrel;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class LOTRBrewingRecipes
{
	private static ArrayList<ShapelessOreRecipe> recipes = new ArrayList<ShapelessOreRecipe>();
	public static int BARREL_CAPACITY = 16;
	
	public static void createBrewingRecipes()
	{
		addBrewingRecipe(new ItemStack(LOTRMod.mugAle, BARREL_CAPACITY), new Object[]
		{
			Items.wheat, Items.wheat, Items.wheat, Items.wheat, Items.wheat, Items.wheat
		});
		
		addBrewingRecipe(new ItemStack(LOTRMod.mugMiruvor, BARREL_CAPACITY), new Object[]
		{
			LOTRMod.mallornNut, LOTRMod.mallornNut, LOTRMod.mallornNut, LOTRMod.elanor, LOTRMod.niphredil, Items.sugar
		});
		
		addBrewingRecipe(new ItemStack(LOTRMod.mugOrcDraught, BARREL_CAPACITY), new Object[]
		{
			LOTRMod.morgulShroom, LOTRMod.morgulShroom, LOTRMod.morgulShroom, "bone", "bone", "bone"
		});
		
		addBrewingRecipe(new ItemStack(LOTRMod.mugMead, BARREL_CAPACITY), new Object[]
		{
			Items.sugar, Items.sugar, Items.sugar, Items.sugar, Items.sugar, Items.sugar
		});
		
		addBrewingRecipe(new ItemStack(LOTRMod.mugCider, BARREL_CAPACITY), new Object[]
		{
			"apple", "apple", "apple", "apple", "apple", "apple"
		});
		
		addBrewingRecipe(new ItemStack(LOTRMod.mugPerry, BARREL_CAPACITY), new Object[]
		{
			LOTRMod.pear, LOTRMod.pear, LOTRMod.pear, LOTRMod.pear, LOTRMod.pear, LOTRMod.pear
		});
		
		addBrewingRecipe(new ItemStack(LOTRMod.mugCherryLiqueur, BARREL_CAPACITY), new Object[]
		{
			LOTRMod.cherry, LOTRMod.cherry, LOTRMod.cherry, LOTRMod.cherry, LOTRMod.cherry, LOTRMod.cherry
		});
		
		addBrewingRecipe(new ItemStack(LOTRMod.mugRum, BARREL_CAPACITY), new Object[]
		{
			Items.reeds, Items.reeds, Items.reeds, Items.reeds, Items.reeds, Items.reeds
		});
		
		addBrewingRecipe(new ItemStack(LOTRMod.mugAthelasBrew, BARREL_CAPACITY), new Object[]
		{
			LOTRMod.athelas, LOTRMod.athelas, LOTRMod.athelas, LOTRMod.athelas, LOTRMod.athelas, LOTRMod.athelas
		});
		
		addBrewingRecipe(new ItemStack(LOTRMod.mugDwarvenTonic, BARREL_CAPACITY), new Object[]
		{
			Items.wheat, Items.wheat, Items.wheat, LOTRMod.dwarfHerb, LOTRMod.dwarfHerb, LOTRMod.mithrilNugget
		});
		
		addBrewingRecipe(new ItemStack(LOTRMod.mugDwarvenAle, BARREL_CAPACITY), new Object[]
		{
			Items.wheat, Items.wheat, Items.wheat, Items.wheat, LOTRMod.dwarfHerb, LOTRMod.dwarfHerb
		});
		
		addBrewingRecipe(new ItemStack(LOTRMod.mugVodka, BARREL_CAPACITY), new Object[]
		{
			Items.potato, Items.potato, Items.potato, Items.potato, Items.potato, Items.potato
		});
		
		addBrewingRecipe(new ItemStack(LOTRMod.mugMapleBeer, BARREL_CAPACITY), new Object[]
		{
			Items.wheat, Items.wheat, Items.wheat, Items.wheat, LOTRMod.mapleSyrup, LOTRMod.mapleSyrup
		});
		
		addBrewingRecipe(new ItemStack(LOTRMod.mugAraq, BARREL_CAPACITY), new Object[]
		{
			LOTRMod.date, LOTRMod.date, LOTRMod.date, LOTRMod.date, LOTRMod.date, LOTRMod.date
		});
	}
	
	private static void addBrewingRecipe(ItemStack result, Object... ingredients)
	{
		if (ingredients.length != 6)
		{
			throw new IllegalArgumentException("Brewing recipes must contain exactly 6 items");
		}
		recipes.add(new ShapelessOreRecipe(result, ingredients));
	}
	
	public static ItemStack findMatchingRecipe(LOTRTileEntityBarrel barrel)
	{
		for (int i = 6; i < 9; i++)
		{
			if (barrel.getStackInSlot(i) == null || barrel.getStackInSlot(i).getItem() != Items.water_bucket)
			{
				return null;
			}
		}
		
		recipeLoop:
		for (ShapelessOreRecipe recipe : recipes)
		{
			ArrayList ingredients = new ArrayList(recipe.getInput());
			for (int i = 0; i < 6; i++)
			{
				ItemStack itemstack = barrel.getStackInSlot(i);
				if (itemstack != null)
				{
					boolean inRecipe = false;
					Iterator it = ingredients.iterator();

					while (it.hasNext())
					{
						boolean match = false;

						Object next = it.next();

						if (next instanceof ItemStack)
						{
							match = LOTRRecipes.checkItemEquals((ItemStack)next, itemstack);
						}
						else if (next instanceof ArrayList)
						{
							for (ItemStack item : (ArrayList<ItemStack>)next)
							{
								match = match || LOTRRecipes.checkItemEquals(item, itemstack);
							}
						}

						if (match)
						{
							inRecipe = true;
							ingredients.remove(next);
							break;
						}
					}

					if (!inRecipe)
					{
						continue recipeLoop;
					}
				}
			}
			
			if (ingredients.isEmpty())
			{
				return recipe.getRecipeOutput().copy();
			}
		}
		
		return null;
	}
}
