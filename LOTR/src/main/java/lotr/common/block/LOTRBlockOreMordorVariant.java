package lotr.common.block;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockOreMordorVariant extends LOTRBlockOre
{
	@SideOnly(Side.CLIENT)
	private IIcon mordorIcon;
	
	public LOTRBlockOreMordorVariant()
	{
		super();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		if (j == 1)
		{
			return mordorIcon;
		}
		return super.getIcon(i, j);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		super.registerBlockIcons(iconregister);
		mordorIcon = iconregister.registerIcon(getTextureName() + "_mordor");
    }
	
	@Override
    public int damageDropped(int i)
    {
        return i;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int j = 0; j <= 1; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
}
