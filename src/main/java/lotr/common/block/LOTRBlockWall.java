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

public class LOTRBlockWall extends BlockWall
{
	public LOTRBlockWall()
	{
		super(LOTRMod.brick);
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
			return LOTRMod.brick.getIcon(i, 0);
		}
		if (j == 2)
		{
			return LOTRMod.rock.getIcon(i, 1);
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
		if (j == 8)
		{
			return LOTRMod.rock.getIcon(i, 2);
		}
		if (j == 9)
		{
			return LOTRMod.brick.getIcon(i, 7);
		}
		if (j == 10)
		{
			return LOTRMod.brick.getIcon(i, 11);
		}
		if (j == 11)
		{
			return LOTRMod.brick.getIcon(i, 12);
		}
		if (j == 12)
		{
			return LOTRMod.brick.getIcon(i, 13);
		}
		if (j == 13)
		{
			return LOTRMod.rock.getIcon(i, 3);
		}
		if (j == 14)
		{
			return LOTRMod.brick.getIcon(i, 14);
		}
		if (j == 15)
		{
			return LOTRMod.brick.getIcon(i, 15);
		}
		return LOTRMod.rock.getIcon(i, 0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int j = 0; j <= 15; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
}
