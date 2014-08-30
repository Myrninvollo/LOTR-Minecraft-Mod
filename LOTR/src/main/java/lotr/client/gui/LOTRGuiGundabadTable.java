package lotr.client.gui;

import lotr.common.inventory.LOTRContainerGundabadTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiGundabadTable extends LOTRGuiCraftingTable
{
    public LOTRGuiGundabadTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerGundabadTable(inv, world, i, j, k), "gundabadCrafting");
    }
}
