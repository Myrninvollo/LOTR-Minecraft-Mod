package lotr.client.gui;

import lotr.common.inventory.LOTRContainerBlueDwarvenTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiBlueDwarvenTable extends LOTRGuiCraftingTable
{
    public LOTRGuiBlueDwarvenTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerBlueDwarvenTable(inv, world, i, j, k), "blueDwarvenCrafting");
    }
}
