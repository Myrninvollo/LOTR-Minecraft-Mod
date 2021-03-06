package lotr.common.block;

import lotr.common.LOTRMod;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockSlab extends LOTRBlockSlabBase
{
	@SideOnly(Side.CLIENT)
	private IIcon[] mordorRockSlabIcons;
	@SideOnly(Side.CLIENT)
	private IIcon[] gondorRockSlabIcons;
	
    public LOTRBlockSlab(boolean flag)
    {
        super(flag, Material.rock, 8);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		j &= 7;
		if (j == 0)
		{
			return mordorRockSlabIcons[i < 2 ? 0 : 1];
		}
		if (j == 1)
		{
			return LOTRMod.brick.getIcon(i, 0);
		}
		if (j == 2)
		{
			return gondorRockSlabIcons[i < 2 ? 0 : 1];
		}
		if (j == 3)
		{
			return LOTRMod.brick.getIcon(i, 1);
		}
		if (j == 4)
		{
			return LOTRMod.brick.getIcon(i, 2);
		}
		if (j == 5)
		{
			return LOTRMod.brick.getIcon(i, 3);
		}
		if (j == 6)
		{
			return LOTRMod.brick.getIcon(i, 4);
		}
		if (j == 7)
		{
			return LOTRMod.brick.getIcon(i, 6);
		}
		return gondorRockSlabIcons[0];
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		mordorRockSlabIcons = new IIcon[2];
		gondorRockSlabIcons = new IIcon[2];
		mordorRockSlabIcons[0] = iconregister.registerIcon("lotr:slab_mordorRock_top");
		mordorRockSlabIcons[1] = iconregister.registerIcon("lotr:slab_mordorRock_side");
		gondorRockSlabIcons[0] = iconregister.registerIcon("lotr:slab_gondorRock_top");
		gondorRockSlabIcons[1] = iconregister.registerIcon("lotr:slab_gondorRock_side");
    }
}
