package lotr.common.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRSlotArmorStand extends Slot
{
    private int armorType;

    public LOTRSlotArmorStand(IInventory inv, int i, int j, int k)
    {
        super(inv, i, j, k);
        armorType = i;
    }

	@Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack)
    {
		return itemstack.getItem() instanceof ItemArmor;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getBackgroundIconIndex()
    {
        return ItemArmor.func_94602_b(armorType);
    }
}
