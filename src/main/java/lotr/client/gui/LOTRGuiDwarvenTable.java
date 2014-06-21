package lotr.client.gui;

import lotr.common.inventory.LOTRContainerDwarvenTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiDwarvenTable extends LOTRGuiCraftingTable
{
    public LOTRGuiDwarvenTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerDwarvenTable(inv, world, i, j, k), "dwarvenCrafting");
    }
}
