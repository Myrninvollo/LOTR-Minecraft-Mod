package lotr.common.block;

import java.util.List;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockPillar extends Block
{
	@SideOnly(Side.CLIENT)
	private IIcon[] pillarFaceIcons;
	@SideOnly(Side.CLIENT)
	private IIcon[] pillarSideIcons;
	@SideOnly(Side.CLIENT)
	private IIcon[] pillarSideTopIcons;
	@SideOnly(Side.CLIENT)
	private IIcon[] pillarSideMiddleIcons;
	@SideOnly(Side.CLIENT)
	private IIcon[] pillarSideBottomIcons;
	private String[] pillarNames = {"dwarven", "elven", "elvenCracked", "blueRock", "redRock"};
	
	public LOTRBlockPillar()
	{
		super(Material.rock);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int i, int j, int k, int side)
	{
		boolean pillarAbove = world.getBlock(i, j, k) == world.getBlock(i, j + 1, k);
		boolean pillarBelow = world.getBlock(i, j, k) == world.getBlock(i, j - 1, k);
		
		int meta = world.getBlockMetadata(i, j, k);
		if (meta >= pillarNames.length)
		{
			meta = 0;
		}
		
		if (side == 0 || side == 1)
		{
			return pillarFaceIcons[meta];
		}
		else
		{
			if (pillarAbove && pillarBelow)
			{
				return pillarSideMiddleIcons[meta];
			}
			else if (pillarAbove)
			{
				return pillarSideBottomIcons[meta];
			}
			else if (pillarBelow)
			{
				return pillarSideTopIcons[meta];
			}
			else
			{
				return pillarSideIcons[meta];
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		if (j >= pillarNames.length)
		{
			j = 0;
		}
		
		if (i == 0 || i == 1)
		{
			return pillarFaceIcons[j];
		}
		
		return pillarSideIcons[j];
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
		for (int j = 0; j <= 4; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
        pillarFaceIcons = new IIcon[pillarNames.length];
		pillarSideIcons = new IIcon[pillarNames.length];
		pillarSideTopIcons = new IIcon[pillarNames.length];
		pillarSideMiddleIcons = new IIcon[pillarNames.length];
		pillarSideBottomIcons = new IIcon[pillarNames.length];
		
        for (int i = 0; i < pillarNames.length; i++)
        {
			String s = getTextureName() + "_" + pillarNames[i];
            pillarFaceIcons[i] = iconregister.registerIcon(s + "_face");
			pillarSideIcons[i] = iconregister.registerIcon(s + "_side");
			pillarSideTopIcons[i] = iconregister.registerIcon(s + "_sideTop");
			pillarSideMiddleIcons[i] = iconregister.registerIcon(s + "_sideMiddle");
			pillarSideBottomIcons[i] = iconregister.registerIcon(s + "_sideBottom");
        }
    }
}
