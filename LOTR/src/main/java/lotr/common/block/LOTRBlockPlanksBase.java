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

public abstract class LOTRBlockPlanksBase extends Block
{
	@SideOnly(Side.CLIENT)
	private IIcon[] plankIcons;
	private String[] plankTypes;
	
	public LOTRBlockPlanksBase()
	{
		super(Material.wood);
		setHardness(2F);
		setResistance(5F);
		setStepSound(Block.soundTypeWood);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}
	
	protected void setPlankTypes(String... types)
	{
		plankTypes = types;
	}
	
    @Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		if (j >= plankTypes.length)
		{
			j = 0;
		}
		
		return plankIcons[j];
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
        plankIcons = new IIcon[plankTypes.length];
        for (int i = 0; i < plankTypes.length; i++)
        {
            plankIcons[i] = iconregister.registerIcon(getTextureName() + "_" + plankTypes[i]);
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
		for (int j = 0; j < plankTypes.length; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
}
