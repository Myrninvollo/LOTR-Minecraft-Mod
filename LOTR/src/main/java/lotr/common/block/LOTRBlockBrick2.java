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

public class LOTRBlockBrick2 extends Block
{
	@SideOnly(Side.CLIENT)
	private IIcon[] brickIcons;
	private String[] brickNames = {"angmar", "angmarCracked", "redRock", "arnor", "arnorMossy", "arnorCracked", "arnorCarved"};
	
	public LOTRBlockBrick2()
	{
		super(Material.rock);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		if (j >= brickNames.length)
		{
			j = 0;
		}
		
		return brickIcons[j];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
        brickIcons = new IIcon[brickNames.length];
        for (int i = 0; i < brickNames.length; i++)
        {
            brickIcons[i] = iconregister.registerIcon(getTextureName() + "_" + brickNames[i]);
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
		for (int j = 0; j <= brickNames.length; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
}
