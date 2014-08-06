package lotr.client.gui;

import lotr.common.inventory.LOTRContainerRangerTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiRangerTable extends LOTRGuiCraftingTable
{
    public LOTRGuiRangerTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerRangerTable(inv, world, i, j, k), "rangerCrafting");
    }
}
