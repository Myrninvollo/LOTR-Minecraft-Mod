package lotr.common.block;

import lotr.common.LOTRMod;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockSlab3 extends LOTRBlockSlabBase
{
	@SideOnly(Side.CLIENT)
	private IIcon[] blueRockSlabIcons;
	@SideOnly(Side.CLIENT)
	private IIcon[] redRockSlabIcons;
	
    public LOTRBlockSlab3(boolean flag)
    {
        super(flag, Material.rock, 8);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		j &= 7;
		if (j == 1)
		{
			return LOTRMod.brick.getIcon(i, 14);
		}
		if (j == 2)
		{
			return LOTRMod.pillar.getIcon(i, 3);
		}
		if (j == 3)
		{
			return LOTRMod.brick2.getIcon(i, 0);
		}
		if (j == 4)
		{
			return LOTRMod.brick2.getIcon(i, 1);
		}
		if (j == 5)
		{
			return redRockSlabIcons[i < 2 ? 0 : 1];
		}
		if (j == 6)
		{
			return LOTRMod.brick2.getIcon(i, 2);
		}
		if (j == 7)
		{
			return LOTRMod.pillar.getIcon(i, 4);
		}
		return blueRockSlabIcons[i < 2 ? 0 : 1];
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
	{
		blueRockSlabIcons = new IIcon[2];
		blueRockSlabIcons[0] = iconregister.registerIcon("lotr:slab_blueRock_top");
		blueRockSlabIcons[1] = iconregister.registerIcon("lotr:slab_blueRock_side");
		redRockSlabIcons = new IIcon[2];
		redRockSlabIcons[0] = iconregister.registerIcon("lotr:slab_redRock_top");
		redRockSlabIcons[1] = iconregister.registerIcon("lotr:slab_redRock_side");
	}
}
