package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockQuenditeGrass extends Block
{
	@SideOnly(Side.CLIENT)
	private IIcon grassSideIcon;
	
	public LOTRBlockQuenditeGrass()
	{
		super(Material.grass);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
        if (i == 0)
		{
			return Blocks.dirt.getIcon(i, j);
		}
		if (i == 1)
		{
			return blockIcon;
		}
		return grassSideIcon;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
	{
		blockIcon = iconregister.registerIcon("lotr:quenditeGrass_top");
		grassSideIcon = iconregister.registerIcon("lotr:quenditeGrass_side");
	}
	
	@Override
	public Item getItemDropped(int i, Random random, int j)
	{
		return Item.getItemFromBlock(Blocks.dirt);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if (random.nextInt(3) == 0)
		{
			double d = i + (double)random.nextFloat();
			double d1 = j + 1D;
			double d2 = k + (double)random.nextFloat();
			LOTRMod.proxy.spawnParticle("quenditeSmoke", d, d1, d2, 0D, 0D, 0D);
		}
	}
}
