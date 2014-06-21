package lotr.client.gui;

import lotr.common.inventory.LOTRContainerAngmarTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiAngmarTable extends LOTRGuiCraftingTable
{
    public LOTRGuiAngmarTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerAngmarTable(inv, world, i, j, k), "angmarCrafting");
    }
}
