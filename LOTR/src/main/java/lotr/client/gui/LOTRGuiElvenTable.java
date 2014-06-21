package lotr.client.gui;

import lotr.common.inventory.LOTRContainerElvenTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiElvenTable extends LOTRGuiCraftingTable
{
    public LOTRGuiElvenTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerElvenTable(inv, world, i, j, k), "elvenCrafting");
    }
}
