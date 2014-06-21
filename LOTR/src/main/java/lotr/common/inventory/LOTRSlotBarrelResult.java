package lotr.common.inventory;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemMugBrewable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRSlotBarrelResult extends Slot
{
    public LOTRSlotBarrelResult(IInventory inv, int i, int j, int k)
    {
        super(inv, i, j, k);
	}

	@Override
    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }
	
	@Override
    public boolean canTakeStack(EntityPlayer entityplayer)
    {
        return false;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getBackgroundIconIndex()
    {
        return getSlotIndex() > 5 ? ((LOTRItemMugBrewable)LOTRMod.mugAle).barrelGui_emptyMugSlotIcon : null;
    }
}
