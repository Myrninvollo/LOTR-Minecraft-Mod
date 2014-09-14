package lotr.common.block;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockHaradFlower extends LOTRBlockFlower
{
	@SideOnly(Side.CLIENT)
	private IIcon[] flowerIcons;
	private static String[] flowerNames = {"red", "yellow", "daisy", "pink"};
	
	public LOTRBlockHaradFlower()
	{
		super();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		if (j >= flowerNames.length)
		{
			j = 0;
		}
		
		return flowerIcons[j];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		flowerIcons = new IIcon[flowerNames.length];
        for (int i = 0; i < flowerNames.length; i++)
        {
        	flowerIcons[i] = iconregister.registerIcon(getTextureName() + "_" + flowerNames[i]);
        }
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
		for (int j = 0; j < flowerNames.length; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
}
