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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockPlanks extends Block
{
	@SideOnly(Side.CLIENT)
	private static IIcon[] plankIcons;
	private static String[] plankNames = {"shirePine", "mallorn", "mirkOak", "charred"};
	@SideOnly(Side.CLIENT)
	private static IIcon[] fruitPlankIcons;
	private static String[] fruitPlankNames = {"apple", "pear", "cherry", "mango"};
	@SideOnly(Side.CLIENT)
	private static IIcon[] plank2Icons;
	private static String[] plank2Names = {"lebethron", "beech", "holly", "banana"};
	@SideOnly(Side.CLIENT)
	private static IIcon[] plank3Icons;
	private static String[] plank3Names = {"maple", "larch", "datePalm"};
	
	public LOTRBlockPlanks()
	{
		super(Material.wood);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}
	
    @Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		if (j >= 12)
		{
			j -= 12;
			if (j >= plank3Names.length)
			{
				j = 0;
			}
			return plank3Icons[j];
		}
		if (j >= 8)
		{
			j -= 8;
			if (j >= plank2Names.length)
			{
				j = 0;
			}
			return plank2Icons[j];
		}
		else if (j >= 4)
		{
			j -= 4;
			if (j >= fruitPlankNames.length)
			{
				j = 0;
			}
			return fruitPlankIcons[j];
		}
		else
		{
			if (j >= plankNames.length)
			{
				j = 0;
			}
			return plankIcons[j];
		}
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
        plankIcons = new IIcon[plankNames.length];
        for (int i = 0; i < plankNames.length; i++)
        {
            plankIcons[i] = iconregister.registerIcon(getTextureName() + "_" + plankNames[i]);
        }
		
        fruitPlankIcons = new IIcon[fruitPlankNames.length];
        for (int i = 0; i < fruitPlankNames.length; i++)
        {
            fruitPlankIcons[i] = iconregister.registerIcon(getTextureName() + "_" + fruitPlankNames[i]);
        }
		
        plank2Icons = new IIcon[plank2Names.length];
        for (int i = 0; i < plank2Names.length; i++)
        {
            plank2Icons[i] = iconregister.registerIcon(getTextureName() + "_" + plank2Names[i]);
        }
        
        plank3Icons = new IIcon[plank3Names.length];
        for (int i = 0; i < plank3Names.length; i++)
        {
        	plank3Icons[i] = iconregister.registerIcon(getTextureName() + "_" + plank3Names[i]);
        }
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
		for (int j = 0; j < plankNames.length; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
		for (int j = 0; j < fruitPlankNames.length; j++)
		{
			list.add(new ItemStack(item, 1, j + 4));
		}
		for (int j = 0; j < plank2Names.length; j++)
		{
			list.add(new ItemStack(item, 1, j + 8));
		}
		for (int j = 0; j < plank3Names.length; j++)
		{
			list.add(new ItemStack(item, 1, j + 12));
		}
    }
}
