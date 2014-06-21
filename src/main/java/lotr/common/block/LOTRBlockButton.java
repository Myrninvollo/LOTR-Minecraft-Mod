package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.BlockButton;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockButton extends BlockButton
{
	private String iconPath;
	
	public LOTRBlockButton(boolean flag, String s)
	{
		super(flag);
		iconPath = s;
		setCreativeTab(LOTRCreativeTabs.tabMisc);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister)
	{
		blockIcon = iconregister.registerIcon(iconPath);
	}
}
