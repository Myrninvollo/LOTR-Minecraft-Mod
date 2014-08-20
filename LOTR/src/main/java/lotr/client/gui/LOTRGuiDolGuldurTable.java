package lotr.client.gui;

import lotr.common.inventory.LOTRContainerDolGuldurTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiDolGuldurTable extends LOTRGuiCraftingTable
{
    public LOTRGuiDolGuldurTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerDolGuldurTable(inv, world, i, j, k), "dolGuldurCrafting");
    }
}
