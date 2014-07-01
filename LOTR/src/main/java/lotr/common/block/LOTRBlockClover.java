package lotr.common.block;

import java.util.List;

import lotr.common.LOTRMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockClover extends LOTRBlockFlower
{
	@SideOnly(Side.CLIENT)
	public static IIcon stemIcon;
	@SideOnly(Side.CLIENT)
	public static IIcon petalIcon;
	
	public LOTRBlockClover()
	{
		super();
		setBlockBounds(0.2F, 0F, 0.2F, 0.8F, 0.4F, 0.8F);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
		double posX = (double)i;
		double posY = (double)j;
		double posZ = (double)k;
		long seed = (long)(i * 3129871) ^ (long)k * 116129781L ^ (long)j;
		seed = seed * seed * 42317861L + seed * 11L;
		posX += ((double)((float)(seed >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
		posZ += ((double)((float)(seed >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
        return AxisAlignedBB.getBoundingBox(posX + minX, posY + minY, posZ + minZ, posX + maxX, posY + maxY, posZ + maxZ);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		return petalIcon;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
        stemIcon = iconregister.registerIcon(getTextureName() + "_" + "stem");
		petalIcon = iconregister.registerIcon(getTextureName() + "_" + "petal");
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
		for (int j = 0; j <= 1; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
	
	@Override
	public int getRenderType()
	{
		return LOTRMod.proxy.getCloverRenderID();
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return ColorizerGrass.getGrassColor(1D, 1D);
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int i)
    {
        return getBlockColor();
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int i, int j, int k)
    {
        return world.getBiomeGenForCoords(i, k).getBiomeGrassColor(i, j, k);
    }
}
