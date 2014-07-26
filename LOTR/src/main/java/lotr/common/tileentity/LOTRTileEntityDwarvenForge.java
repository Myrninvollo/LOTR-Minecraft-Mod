package lotr.common.tileentity;

import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockDwarvenForge;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class LOTRTileEntityDwarvenForge extends LOTRTileEntityAlloyForge
{
	@Override
    public String getForgeName()
    {
		return StatCollector.translateToLocal("container.lotr.dwarvenForge");
    }
	
	@Override
	public void setForgeActive()
	{
		LOTRBlockDwarvenForge.setForgeActive(worldObj, xCoord, yCoord, zCoord);
	}

	@Override
	public ItemStack getSmeltingResult(ItemStack itemstack)
	{
		if (itemstack.getItem() == Item.getItemFromBlock(LOTRMod.oreMithril))
		{
			return new ItemStack(LOTRMod.mithril);
		}
		
		return super.getSmeltingResult(itemstack);
	}
	
	@Override
	protected ItemStack getAlloySmeltingResult(ItemStack itemstack, ItemStack alloyItem)
	{
		if (isIron(itemstack) && alloyItem.getItem() == Items.coal)
		{
			return new ItemStack(LOTRMod.dwarfSteel);
		}
		
		if (isIron(itemstack) && alloyItem.getItem() == LOTRMod.quenditeCrystal)
		{
			return new ItemStack(LOTRMod.galvorn);
		}
		
		if (isIron(itemstack) && alloyItem.getItem() == Item.getItemFromBlock(LOTRMod.rock) && alloyItem.getItemDamage() == 3)
		{
			return new ItemStack(LOTRMod.blueDwarfSteel);
		}
		
		return super.getAlloySmeltingResult(itemstack, alloyItem);
	}
}
