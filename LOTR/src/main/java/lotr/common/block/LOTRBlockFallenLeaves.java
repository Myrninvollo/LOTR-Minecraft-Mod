package lotr.common.block;

import java.util.*;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockFallenLeaves extends Block implements IShearable
{
	public static List<LOTRBlockFallenLeaves> allFallenLeaves = new ArrayList();
	private static Random leafRand = new Random();
	
	private Block[] leafBlocks;
	
	public LOTRBlockFallenLeaves(Block... blocks)
	{
		super(Material.vine);
		allFallenLeaves.add(this);
		setHardness(0.2F);
		setStepSound(Block.soundTypeGrass);
		useNeighborBrightness = true;
		leafBlocks = blocks;
		setCreativeTab(LOTRCreativeTabs.tabDeco);
	}
	
	public Object[] forFallenLeaf(int meta)
	{
		Block leaf = leafBlocks[meta / 4];
		int leafMeta = meta & 3;
		return new Object[] {leaf, leafMeta};
	}
	
	public static Object[] forLeaf(Block block, int meta)
	{
		meta &= 3;
		for (LOTRBlockFallenLeaves fallenLeaves : allFallenLeaves)
		{
			for (int i = 0; i < fallenLeaves.leafBlocks.length; i++)
			{
				Block leafBlock = fallenLeaves.leafBlocks[i];
				if (leafBlock == block)
				{
					return new Object[] {fallenLeaves, i * 4 + meta};
				}
			}
		}
		return null;
	}
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
    {
        long seed = (long)(i * 3129871) ^ (long)k * 116129781L ^ (long)j;
        leafRand.setSeed(seed);
        int height = 1 + leafRand.nextInt(3);
        setBlockBounds(0F, 0F, 0F, 1F, (float)height / 16F, 1F);
    }
	
	@Override
    public void setBlockBoundsForItemRender()
    {
		setBlockBounds(0F, 0F, 0F, 1F, 0.125F, 1F);
    }
	
	@Override
    public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB bb, List boxes, Entity entity) {}
	
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		Object[] obj = forFallenLeaf(j);
        return ((Block)obj[0]).getIcon(i, (Integer)obj[1]);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {}
	
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int i)
    {
		Object[] obj = forFallenLeaf(i);
        return ((Block)obj[0]).getRenderColor((Integer)obj[1]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int i, int j, int k)
    {
    	int meta = world.getBlockMetadata(i, j, k);
		Object[] obj = forFallenLeaf(meta);
		return ((Block)obj[0]).colorMultiplier(world, i, j, k);
    }

    @Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
        return world.getBlock(i, j - 1, k).isSideSolid(world, i, j - 1, k, ForgeDirection.UP);
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
    	return canBlockStay(world, i, j, k);
    }
    
    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block block)
    {
        if (!canBlockStay(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockToAir(i, j, k);
        }
    }
    
    @Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return null;
    }
    
    @Override
    public int damageDropped(int i)
    {
        return i;
    }
    
    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, int i, int j, int k)
    {
        return true;
    }

    @Override
    public ArrayList onSheared(ItemStack item, IBlockAccess world, int i, int j, int k, int fortune)
    {
        ArrayList drops = new ArrayList();
        drops.add(new ItemStack(this, 1, world.getBlockMetadata(i, j, k)));
        return drops;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for (int i = 0; i < leafBlocks.length; i++)
        {
        	Block leaf = leafBlocks[i];
        	List<ItemStack> leafTypes = new ArrayList();
        	leaf.getSubBlocks(Item.getItemFromBlock(leaf), leaf.getCreativeTabToDisplayOn(), leafTypes);
        	
        	for (ItemStack leafItem : leafTypes)
        	{
        		int meta = leafItem.getItemDamage();
        		list.add(new ItemStack(item, 1, i * 4 + meta));
        	}
        }
    }
}
