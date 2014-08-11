package lotr.common.inventory;

import java.util.ArrayList;
import java.util.List;

import lotr.common.LOTRReflection;
import lotr.common.entity.animal.LOTREntityHorse;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRContainerMountInventory extends ContainerHorseInventory
{
	public LOTRContainerMountInventory(IInventory playerInv, final LOTREntityHorse horse)
    {
		super(playerInv, LOTRReflection.getHorseInv(horse), horse);
		
		List slots = new ArrayList(inventorySlots);
		inventorySlots.clear();
		
		Slot armorSlot = (Slot)slots.get(1);
		
		inventorySlots.add(slots.get(0));
		
		addSlotToContainer(new Slot(armorSlot.inventory, armorSlot.slotNumber, armorSlot.xDisplayPosition, armorSlot.yDisplayPosition)
        {
            @Override
            public boolean isItemValid(ItemStack itemstack)
            {
                return super.isItemValid(itemstack) && horse.func_110259_cr() && horse.isMountArmorValid(itemstack);
            }
            
            @Override
            @SideOnly(Side.CLIENT)
            public boolean func_111238_b()
            {
                return horse.func_110259_cr();
            }
        });
		
		for (int l = 0; l <= 1; l++)
		{
			slots.remove(0);
		}
		inventorySlots.addAll(slots);
    }
}
