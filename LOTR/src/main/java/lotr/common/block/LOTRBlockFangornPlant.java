package lotr.common.block;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockFangornPlant extends LOTRBlockFlower
{
	@SideOnly(Side.CLIENT)
	private IIcon[] plantIcons;
	private String[] plantNames = new String[] {"green", "brown", "gold", "yellow", "red", "silver"};
	
	public LOTRBlockFangornPlant()
	{
		super();
		setFlowerBounds(0.2F, 0F, 0.2F, 0.8F, 0.8F, 0.8F);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		if (j >= plantNames.length)
		{
			j = 0;
		}
		return plantIcons[j];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		plantIcons = new IIcon[plantNames.length];
        for (int i = 0; i < plantNames.length; i++)
        {
        	plantIcons[i] = iconregister.registerIcon(getTextureName() + "_" + plantNames[i]);
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
		for (int j = 0; j < plantNames.length; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
}
