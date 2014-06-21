package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockHearth extends Block
{
	@SideOnly(Side.CLIENT)
	private IIcon[] blockIcons;
	
	public LOTRBlockHearth()
	{
		super(Material.rock);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
        if (i == 0)
        {
            return blockIcons[0];
        }
        else if (i == 1)
        {
            return blockIcons[1];
        }
        return blockIcons[2];
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		blockIcons = new IIcon[3];
        blockIcons[0] = iconregister.registerIcon(getTextureName() + "_bottom");
		blockIcons[1] = iconregister.registerIcon(getTextureName() + "_top");
		blockIcons[2] = iconregister.registerIcon(getTextureName() + "_side");
    }
	
	@Override
    public boolean isFireSource(World world, int i, int j, int k, ForgeDirection side)
    {
		return side == ForgeDirection.UP;
	}
}
