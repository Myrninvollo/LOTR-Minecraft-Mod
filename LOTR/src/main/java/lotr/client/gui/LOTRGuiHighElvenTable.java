package lotr.client.gui;

import lotr.common.inventory.LOTRContainerHighElvenTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiHighElvenTable extends LOTRGuiCraftingTable
{
    public LOTRGuiHighElvenTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerHighElvenTable(inv, world, i, j, k), "highElvenCrafting");
    }
}
