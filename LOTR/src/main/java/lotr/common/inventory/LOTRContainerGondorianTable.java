package lotr.common.inventory;

import lotr.common.LOTRMod;
import lotr.common.recipe.LOTRRecipes;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRContainerGondorianTable extends LOTRContainerCraftingTable
{
	public LOTRContainerGondorianTable(InventoryPlayer inv, World world, int i, int j, int k)
	{
		super(inv, world, i, j, k, LOTRRecipes.gondorianRecipes, LOTRMod.gondorianTable);
	}
}