package lotr.common.block;

import java.util.ArrayList;
import java.util.List;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockFence extends BlockFence
{
	private Block plankBlock;
	
	public LOTRBlockFence(Block planks)
	{
		super("", Material.wood);
		setHardness(2F);
		setResistance(5F);
		setStepSound(Block.soundTypeWood);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
		plankBlock = planks;
	}
	
	@Override
	public boolean canPlaceTorchOnTop(World world, int i, int j, int k)
	{
		return true;
	}
	
	@Override
	public int damageDropped(int i)
	{
		return i;
	}
	
	@Override
	public int getRenderType()
	{
		if (LOTRMod.proxy.isClient())
		{
			return LOTRMod.proxy.getFenceRenderID();
		}
		return super.getRenderType();
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		return plankBlock.getIcon(i, j);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		List plankTypes = new ArrayList();
		plankBlock.getSubBlocks(Item.getItemFromBlock(plankBlock), plankBlock.getCreativeTabToDisplayOn(), plankTypes);
		for (int j = 0; j < plankTypes.size(); j++)
		{
			Object obj = plankTypes.get(j);
			if (obj instanceof ItemStack)
			{
				int meta = ((ItemStack)obj).getItemDamage();
				list.add(new ItemStack(this, 1, meta));
			}
		}
    }
}
