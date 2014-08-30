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
	public LOTRContainerMountInventory(IInventory playerInv, final IInventory horseInv, final LOTREntityHorse horse)
    {
		super(playerInv, horseInv, horse);
		
		List<Slot> slots = new ArrayList(inventorySlots);
		inventorySlots.clear();
		inventoryItemStacks.clear();

		addSlotToContainer(slots.get(0));
		
		Slot armorSlot = slots.get(1);
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
		
		for (int i = 2; i < slots.size(); i++)
		{
			addSlotToContainer(slots.get(i));
		}
    }
}
