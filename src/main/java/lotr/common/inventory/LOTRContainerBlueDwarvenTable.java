package lotr.common.inventory;

import lotr.common.LOTRMod;
import lotr.common.recipe.LOTRRecipes;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRContainerBlueDwarvenTable extends LOTRContainerCraftingTable
{
	public LOTRContainerBlueDwarvenTable(InventoryPlayer inv, World world, int i, int j, int k)
	{
		super(inv, world, i, j, k, LOTRRecipes.blueMountainsRecipes, LOTRMod.blueDwarvenTable);
	}
}