package lotr.common.tileentity;

import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockOrcForge;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class LOTRTileEntityOrcForge extends LOTRTileEntityAlloyForge
{
	@Override
    public String getForgeName()
    {
        return StatCollector.translateToLocal("container.lotr.orcForge");
    }
	
	@Override
	public void setForgeActive()
	{
		LOTRBlockOrcForge.setForgeActive(worldObj, xCoord, yCoord, zCoord);
	}

	@Override
	public ItemStack getSmeltingResult(ItemStack itemstack)
	{
		if (isWood(itemstack))
		{
			return new ItemStack(LOTRMod.wood, 1, 3);
		}
		
		if (itemstack.getItem() == Item.getItemFromBlock(LOTRMod.oreMorgulIron))
		{
			return new ItemStack(LOTRMod.orcSteel);
		}
		
		return super.getSmeltingResult(itemstack);
	}
	
	@Override
	protected ItemStack getAlloySmeltingResult(ItemStack itemstack, ItemStack alloyItem)
	{
		if (isIron(itemstack) && alloyItem.getItem() == Items.coal)
		{
			return new ItemStack(LOTRMod.urukSteel);
		}
		
		if (isOrcSteel(itemstack) && alloyItem.getItem() == LOTRMod.guldurilCrystal)
		{
			return new ItemStack(LOTRMod.morgulSteel);
		}
		
		return super.getAlloySmeltingResult(itemstack, alloyItem);
	}
}
