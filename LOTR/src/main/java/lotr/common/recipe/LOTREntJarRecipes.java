package lotr.common.recipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lotr.common.LOTRMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class LOTREntJarRecipes
{
	private static Map<ItemStack, ItemStack> recipes = new HashMap<ItemStack, ItemStack>();
	
	public static void createDraughtRecipes()
	{
		addDraughtRecipe(new ItemStack(LOTRMod.fangornPlant, 1, 0), new ItemStack(LOTRMod.entDraught, 1, 0));
		addDraughtRecipe(new ItemStack(LOTRMod.fangornPlant, 1, 1), new ItemStack(LOTRMod.entDraught, 1, 1));
		addDraughtRecipe(new ItemStack(LOTRMod.fangornPlant, 1, 2), new ItemStack(LOTRMod.entDraught, 1, 2));
		addDraughtRecipe(new ItemStack(LOTRMod.fangornPlant, 1, 3), new ItemStack(LOTRMod.entDraught, 1, 3));
		addDraughtRecipe(new ItemStack(LOTRMod.fangornPlant, 1, 4), new ItemStack(LOTRMod.entDraught, 1, 4));
		addDraughtRecipe(new ItemStack(LOTRMod.fangornPlant, 1, 5), new ItemStack(LOTRMod.entDraught, 1, 5));
		addDraughtRecipe(new ItemStack(LOTRMod.fangornRiverweed), new ItemStack(LOTRMod.entDraught, 1, 6));
	}
	
	private static void addDraughtRecipe(ItemStack ingredient, ItemStack result)
	{
		recipes.put(ingredient, result);
	}
	
	public static ItemStack findMatchingRecipe(ItemStack input)
	{
		if (input == null)
		{
			return null;
		}
		
		Iterator<ItemStack> it = recipes.keySet().iterator();
		while (it.hasNext())
		{
			ItemStack recipeInput = it.next();
			if (LOTRRecipes.checkItemEquals(recipeInput, input))
			{
				return recipes.get(recipeInput).copy();
			}
		}
		
		return null;
	}
}
