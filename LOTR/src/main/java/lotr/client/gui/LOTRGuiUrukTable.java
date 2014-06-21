package lotr.client.gui;

import lotr.common.inventory.LOTRContainerUrukTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class LOTRGuiUrukTable extends LOTRGuiCraftingTable
{
    public LOTRGuiUrukTable(InventoryPlayer inv, World world, int i, int j, int k)
    {
        super(new LOTRContainerUrukTable(inv, world, i, j, k), "urukCrafting");
    }
}
