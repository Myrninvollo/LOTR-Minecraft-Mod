package lotr.common.block;

import java.util.ArrayList;
import java.util.List;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityGulduril;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockGuldurilBrick extends Block
{
	public LOTRBlockGuldurilBrick()
	{
		super(Material.rock);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}
	
	public static int guldurilMetaForBlock(Block block, int i)
	{
		if (block == null)
		{
			return -1;
		}
		
		if (block == LOTRMod.brick && i == 0)
		{
			return 0;
		}
		if (block == LOTRMod.brick && i == 7)
		{
			return 1;
		}
		if (block == LOTRMod.brick2 && i == 0)
		{
			return 2;
		}
		if (block == LOTRMod.brick2 && i == 1)
		{
			return 3;
		}
		if (block == LOTRMod.brick2 && i == 8)
		{
			return 4;
		}
		if (block == LOTRMod.brick2 && i == 9)
		{
			return 5;
		}
		
		return -1;
	}
	
	public static ItemStack blockForGuldurilMeta(int i)
	{
		if (i == 0)
		{
			return new ItemStack(LOTRMod.brick, 1, 0);
		}
		if (i == 1)
		{
			return new ItemStack(LOTRMod.brick, 1, 7);
		}
		if (i == 2)
		{
			return new ItemStack(LOTRMod.brick2, 1, 0);
		}
		if (i == 3)
		{
			return new ItemStack(LOTRMod.brick2, 1, 1);
		}
		if (i == 4)
		{
			return new ItemStack(LOTRMod.brick2, 1, 8);
		}
		if (i == 5)
		{
			return new ItemStack(LOTRMod.brick2, 1, 9);
		}
		
		return null;
	}
	
	@Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int i, int j, int k)
    {
		return blockForGuldurilMeta(world.getBlockMetadata(i, j, k));
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		ItemStack itemstack = blockForGuldurilMeta(j);
		Item item = itemstack.getItem();
		if (item instanceof ItemBlock)
		{
			Block block = ((ItemBlock)item).field_150939_a;
			int meta = itemstack.getItemDamage();
			return block.getIcon(i, meta);
		}
		
		return LOTRMod.brick.getIcon(i, 0);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {}
	
    @Override 
    public ArrayList getDrops(World world, int i, int j, int k, int metadata, int fortune)
    {
        ArrayList drops = new ArrayList();
		
		ItemStack drop = blockForGuldurilMeta(metadata);
		if (drop != null)
		{
			drops.add(drop);
		}
		
		return drops;
	}

	@Override
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

	@Override
    public TileEntity createTileEntity(World world, int metadata)
	{
		return new LOTRTileEntityGulduril();
    }
	
	@Override
	protected boolean canSilkHarvest()
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int j = 0; j <= 5; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
}
