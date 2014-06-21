package lotr.client.gui;

import lotr.common.inventory.LOTRContainerNearHaradTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiNearHaradTable extends LOTRGuiCraftingTable
{
    public LOTRGuiNearHaradTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerNearHaradTable(inv, world, i, j, k), "nearHaradCrafting");
    }
}
