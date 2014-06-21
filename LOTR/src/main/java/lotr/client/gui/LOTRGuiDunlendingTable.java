package lotr.client.gui;

import lotr.common.inventory.LOTRContainerDunlendingTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiDunlendingTable extends LOTRGuiCraftingTable
{
    public LOTRGuiDunlendingTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerDunlendingTable(inv, world, i, j, k), "dunlendingCrafting");
    }
}
