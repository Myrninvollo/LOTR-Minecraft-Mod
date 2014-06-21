package lotr.client.gui;

import lotr.common.inventory.LOTRContainerMorgulTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiMorgulTable extends LOTRGuiCraftingTable
{
    public LOTRGuiMorgulTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerMorgulTable(inv, world, i, j, k), "morgulCrafting");
    }
}
