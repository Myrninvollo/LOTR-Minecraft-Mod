package lotr.client.gui;

import lotr.common.inventory.LOTRContainerWoodElvenTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiWoodElvenTable extends LOTRGuiCraftingTable
{
    public LOTRGuiWoodElvenTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerWoodElvenTable(inv, world, i, j, k), "woodElvenCrafting");
    }
}
