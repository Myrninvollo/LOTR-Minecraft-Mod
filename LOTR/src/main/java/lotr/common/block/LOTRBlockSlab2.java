package lotr.common.block;

import lotr.common.LOTRMod;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockSlab2 extends LOTRBlockSlabBase
{
	@SideOnly(Side.CLIENT)
	private IIcon[] rohanRockSlabIcons;
	
    public LOTRBlockSlab2(boolean flag)
    {
        super(flag, Material.rock, 7);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		j &= 7;
		if (j == 0)
		{
			return LOTRMod.pillar.getIcon(i, 0);
		}
		if (j == 1)
		{
			return rohanRockSlabIcons[i < 2 ? 0 : 1];
		}
		if (j == 2)
		{
			return LOTRMod.brick.getIcon(i, 7);
		}
		if (j == 3)
		{
			return LOTRMod.brick.getIcon(i, 11);
		}
		if (j == 4)
		{
			return LOTRMod.brick.getIcon(i, 12);
		}
		if (j == 5)
		{
			return LOTRMod.brick.getIcon(i, 13);
		}
		if (j == 6)
		{
			return LOTRMod.pillar.getIcon(i, 1);
		}
		if (j == 7)
		{
			return LOTRMod.pillar.getIcon(i, 2);
		}
		return LOTRMod.pillar.getIcon(i, 0);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
	{
		rohanRockSlabIcons = new IIcon[2];
		rohanRockSlabIcons[0] = iconregister.registerIcon("lotr:slab_rohanRock_top");
		rohanRockSlabIcons[1] = iconregister.registerIcon("lotr:slab_rohanRock_side");
	}
}
