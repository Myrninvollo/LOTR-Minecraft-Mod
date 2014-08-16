package lotr.common.block;

import lotr.common.LOTRMod;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockSlab4 extends LOTRBlockSlabBase
{
    public LOTRBlockSlab4(boolean flag)
    {
        super(flag, Material.rock, 4);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		j &= 7;
		if (j == 1)
		{
			return LOTRMod.brick2.getIcon(i, 3);
		}
		if (j == 2)
		{
			return LOTRMod.brick2.getIcon(i, 4);
		}
		if (j == 3)
		{
			return LOTRMod.brick2.getIcon(i, 5);
		}
		if (j == 4)
		{
			return LOTRMod.brick2.getIcon(i, 7);
		}
		return LOTRMod.brick.getIcon(i, 15);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {}
}
