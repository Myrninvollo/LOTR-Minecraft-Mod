package lotr.common.block;

import lotr.common.LOTRMod;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockWoodSlab3 extends LOTRBlockSlabBase
{
    public LOTRBlockWoodSlab3(boolean flag)
    {
        super(flag, Material.wood, 3);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		j &= 7;
		return LOTRMod.planks2.getIcon(i, j);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {}
}
