package lotr.common.block;

import lotr.common.LOTRMod;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockSlab5 extends LOTRBlockSlabBase
{
    public LOTRBlockSlab5(boolean flag)
    {
        super(flag, Material.rock, 3);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		j &= 7;
		if (j == 1)
		{
			return LOTRMod.pillar.getIcon(i, 7);
		}
		if (j == 2)
		{
			return LOTRMod.pillar.getIcon(i, 8);
		}
		return LOTRMod.pillar.getIcon(i, 6);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {}
}
