package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockFangornRiverweed extends BlockLilyPad
{
	public LOTRBlockFangornRiverweed()
	{
		super();
		setCreativeTab(LOTRCreativeTabs.tabDeco);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
		return 0xFFFFFF;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta)
    {
        return 0xFFFFFF;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int i, int j, int k)
    {
    	return 0xFFFFFF;
    }
}
