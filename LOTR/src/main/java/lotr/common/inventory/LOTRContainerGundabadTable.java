package lotr.common.inventory;

import lotr.common.LOTRMod;
import lotr.common.recipe.LOTRRecipes;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRContainerGundabadTable extends LOTRContainerCraftingTable
{
	public LOTRContainerGundabadTable(InventoryPlayer inv, World world, int i, int j, int k)
	{
		super(inv, world, i, j, k, LOTRRecipes.gundabadRecipes, LOTRMod.gundabadTable);
	}
}