package lotr.common.inventory;

import java.util.List;

import lotr.common.recipe.LOTRRecipes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public abstract class LOTRContainerCraftingTable extends ContainerWorkbench
{
	private World theTableWorld;
	private int tablePosX;
	private int tablePosY;
	private int tablePosZ;
	private List recipes;
	private Block tableBlock;
	
	public LOTRContainerCraftingTable(InventoryPlayer inv, World world, int i, int j, int k, List list, Block block)
	{
		super(inv, world, i, j, k);
		theTableWorld = world;
		tablePosX = i;
		tablePosY = j;
		tablePosZ = k;
		recipes = list;
		tableBlock = block;
	}
	
	@Override
    public void onCraftMatrixChanged(IInventory inv)
    {
		if (recipes == null)
		{
			return;
		}
        craftResult.setInventorySlotContents(0, LOTRRecipes.findMatchingRecipe(recipes, craftMatrix, theTableWorld));
    }
	
	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return theTableWorld.getBlock(tablePosX, tablePosY, tablePosZ) != tableBlock ? false : entityplayer.getDistanceSq((double)tablePosX + 0.5D, (double)tablePosY + 0.5D, (double)tablePosZ + 0.5D) <= 64D;
    }
}
