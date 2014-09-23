package lotr.common.block;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockWoodBase extends BlockLog
{
	@SideOnly(Side.CLIENT)
	private IIcon[][] woodIcons;
	private String[] woodNames;
	
    public LOTRBlockWoodBase()
    {
        super();
        setHardness(2F);
        setStepSound(Block.soundTypeWood);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
    }
	
	public void setWoodNames(String... s)
	{
		woodNames = s;
	}
	
	@Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return Item.getItemFromBlock(this);
    }

	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		int j1 = j & 12;
		j &= 3;
		
		if (j >= woodNames.length)
		{
			j = 0;
		}
		
		if ((j1 == 0 && (i == 0 || i == 1)) || (j1 == 4 && (i == 4 || i == 5)) || (j1 == 8 && (i == 2 || i == 3)))
		{
			return woodIcons[j][0];
		}
		return woodIcons[j][1];
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
        woodIcons = new IIcon[woodNames.length][2];
        for (int i = 0; i < woodNames.length; i++)
        {
            woodIcons[i][0] = iconregister.registerIcon(getTextureName() + "_" + woodNames[i] + "_top");
			woodIcons[i][1] = iconregister.registerIcon(getTextureName() + "_" + woodNames[i] + "_side");
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int j = 0; j < woodNames.length; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
}
