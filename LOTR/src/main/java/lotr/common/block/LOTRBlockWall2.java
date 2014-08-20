package lotr.common.block;

import java.util.List;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockWall2 extends BlockWall
{
	public LOTRBlockWall2()
	{
		super(LOTRMod.brick2);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}
	
	@Override
	public boolean canPlaceTorchOnTop(World world, int i, int j, int k)
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		if (j == 1)
		{
			return LOTRMod.brick2.getIcon(i, 1);
		}
		if (j == 2)
		{
			return LOTRMod.rock.getIcon(i, 4);
		}
		if (j == 3)
		{
			return LOTRMod.brick2.getIcon(i, 2);
		}
		if (j == 4)
		{
			return LOTRMod.brick2.getIcon(i, 3);
		}
		if (j == 5)
		{
			return LOTRMod.brick2.getIcon(i, 4);
		}
		if (j == 6)
		{
			return LOTRMod.brick2.getIcon(i, 5);
		}
		if (j == 7)
		{
			return LOTRMod.brick2.getIcon(i, 7);
		}
		if (j == 8)
		{
			return LOTRMod.brick2.getIcon(i, 8);
		}
		if (j == 9)
		{
			return LOTRMod.brick2.getIcon(i, 9);
		}
		return LOTRMod.brick2.getIcon(i, 0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int j = 0; j <= 9; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
}
