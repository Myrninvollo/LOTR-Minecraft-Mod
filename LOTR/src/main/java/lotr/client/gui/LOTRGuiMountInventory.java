package lotr.client.gui;

import lotr.common.LOTRReflection;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.inventory.LOTRContainerMountInventory;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.inventory.IInventory;

public class LOTRGuiMountInventory extends GuiScreenHorseInventory
{
	public LOTRGuiMountInventory(IInventory playerInv, LOTREntityHorse horse)
    {
		super(playerInv, LOTRReflection.getHorseInv(horse), horse);
		inventorySlots = new LOTRContainerMountInventory(playerInv, horse);
    }
}
