package lotr.client.gui;

import lotr.common.inventory.LOTRContainerGondorianTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiGondorianTable extends LOTRGuiCraftingTable
{
    public LOTRGuiGondorianTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerGondorianTable(inv, world, i, j, k), "gondorianCrafting");
    }
}
