package lotr.common.inventory;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemMugBrewable;
import lotr.common.tileentity.LOTRTileEntityBarrel;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRSlotBarrel extends Slot
{
	private LOTRTileEntityBarrel theBarrel;
	
    public LOTRSlotBarrel(LOTRTileEntityBarrel inv, int i, int j, int k)
    {
        super(inv, i, j, k);
		theBarrel = inv;
	}
	
	@Override
    public boolean isItemValid(ItemStack itemstack)
    {
        return theBarrel.barrelMode == LOTRTileEntityBarrel.EMPTY;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getBackgroundIconIndex()
    {
        return getSlotIndex() > 5 ? ((LOTRItemMugBrewable)LOTRMod.mugAle).barrelGui_emptyBucketSlotIcon : null;
    }
}
