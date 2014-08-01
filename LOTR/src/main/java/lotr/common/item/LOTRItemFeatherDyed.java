package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemFeatherDyed extends Item
{
	public LOTRItemFeatherDyed()
	{
		super();
		setMaxStackSize(1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconregister) {}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int i)
	{
		return Items.feather.getIconFromDamage(i);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemstack, int pass)
    {
        return getFeatherColor(itemstack);
    }
	
	public static int getFeatherColor(ItemStack itemstack)
	{
		int i = LOTRItemLeatherHat.FEATHER_WHITE;
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("FeatherColor"))
		{
			i = itemstack.getTagCompound().getInteger("FeatherColor");
		}
		return i;
	}
	
	public static void setFeatherColor(ItemStack itemstack, int i)
	{
		if (itemstack.getTagCompound() == null)
		{
			itemstack.setTagCompound(new NBTTagCompound());
		}
		itemstack.getTagCompound().setInteger("FeatherColor", i);
	}
}
