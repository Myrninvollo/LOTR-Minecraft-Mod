package lotr.common.block;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockTallGrass extends LOTRBlockGrass
{
	@SideOnly(Side.CLIENT)
	private IIcon[] grassIcons;
	@SideOnly(Side.CLIENT)
	private IIcon[] overlayIcons;
	
	public static String[] grassNames = {"short", "flower", "wheat", "thistle"};
	public static boolean[] grassOverlay = {false, true, true, true};
	
    public LOTRBlockTallGrass()
    {
        super();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return Blocks.tallgrass.getBlockColor();
    }
    
    @Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta)
	{
    	return getBlockColor();
	}

    @Override
    @SideOnly(Side.CLIENT)
   	public int colorMultiplier(IBlockAccess world, int i, int j, int k)
    {
    	return world.getBiomeGenForCoords(i, k).getBiomeGrassColor(i, j, k);
   	}
    
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		if (j >= grassNames.length)
		{
			j = 0;
		}
		
		if (i == -1)
		{
			return overlayIcons[j];
		}
		return grassIcons[j];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		grassIcons = new IIcon[grassNames.length];
		overlayIcons = new IIcon[grassNames.length];
		
        for (int i = 0; i < grassNames.length; i++)
        {
        	grassIcons[i] = iconregister.registerIcon(getTextureName() + "_" + grassNames[i]);

        	if (grassOverlay[i])
        	{
        		overlayIcons[i] = iconregister.registerIcon(getTextureName() + "_" + grassNames[i] + "_overlay");
        	}
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int j = 0; j < grassNames.length; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
}
