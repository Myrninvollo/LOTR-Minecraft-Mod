package lotr.client.gui;

import lotr.common.inventory.LOTRContainerRohirricTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiRohirricTable extends LOTRGuiCraftingTable
{
    public LOTRGuiRohirricTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerRohirricTable(inv, world, i, j, k), "rohirricCrafting");
    }
}
