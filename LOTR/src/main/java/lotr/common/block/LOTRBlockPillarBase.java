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
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class LOTRBlockPillarBase extends Block
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
	
	private String[] pillarNames;
	
	public LOTRBlockPillarBase()
	{
		super(Material.rock);
		setHardness(1.5F);
		setResistance(10F);
		setStepSound(Block.soundTypeStone);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}
	
	protected void setPillarNames(String... names)
	{
		pillarNames = names;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int i, int j, int k, int side)
	{
		boolean pillarAbove = isBlockAndMetaEqual(world, i, j, k, i, j + 1, k);
		boolean pillarBelow = isBlockAndMetaEqual(world, i, j, k, i, j - 1, k);
		
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
	
	private boolean isBlockAndMetaEqual(IBlockAccess world, int i, int j, int k, int i1, int j1, int k1)
	{
		return world.getBlock(i, j, k) == world.getBlock(i1, j1, k1) && world.getBlockMetadata(i, j, k) == world.getBlockMetadata(i1, j1, k1);
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
		for (int j = 0; j < pillarNames.length; j++)
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
