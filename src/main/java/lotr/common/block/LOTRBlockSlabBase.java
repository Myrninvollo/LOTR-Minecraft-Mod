package lotr.common.block;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class LOTRBlockSlabBase extends BlockSlab
{
	public Block singleSlab;
	public Block doubleSlab;
	private int subtypes;
	
    public LOTRBlockSlabBase(boolean flag, Material material, int j)
    {
        super(flag, material);
		subtypes = j;
        setCreativeTab(LOTRCreativeTabs.tabBlock);
        useNeighborBrightness = true;
    }
	
	public static void registerSlabs(Block block, Block block1)
	{
		((LOTRBlockSlabBase)block).singleSlab = block;
		((LOTRBlockSlabBase)block).doubleSlab = block1;
		((LOTRBlockSlabBase)block1).singleSlab = block;
		((LOTRBlockSlabBase)block1).doubleSlab = block1;
	}

	@Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return Item.getItemFromBlock(singleSlab);
    }

	@Override
    protected ItemStack createStackedBlock(int i)
    {
        return new ItemStack(singleSlab, 2, i & 7);
    }

	@Override
    public String func_150002_b(int i)
    {
        return super.getUnlocalizedName() + "." + i;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int i, int j, int k, int l)
    {
        if (this == doubleSlab)
        {
            return super.shouldSideBeRendered(world, i, j, k, l);
        }
        else if (l != 1 && l != 0 && !super.shouldSideBeRendered(world, i, j, k, l))
        {
            return false;
        }
        else
        {
            int i1 = i + Facing.offsetsXForSide[Facing.oppositeSide[l]];
            int j1 = j + Facing.offsetsYForSide[Facing.oppositeSide[l]];
            int k1 = k + Facing.offsetsZForSide[Facing.oppositeSide[l]];
            boolean flag = (world.getBlockMetadata(i1, j1, k1) & 8) != 0;
            return flag ? (l == 0 ? true : (l == 1 && super.shouldSideBeRendered(world, i, j, k, l) ? true : world.getBlock(i, j, k) != singleSlab || (world.getBlockMetadata(i, j, k) & 8) == 0)) : (l == 1 ? true : (l == 0 && super.shouldSideBeRendered(world, i, j, k, l) ? true : world.getBlock(i, j, k) != singleSlab || (world.getBlockMetadata(i, j, k) & 8) != 0));
        }
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        if (item!= Item.getItemFromBlock(doubleSlab))
        {
            for (int j = 0; j <= subtypes; j++)
            {
                list.add(new ItemStack(item, 1, j));
            }
        }
    }

	@Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) 
    {
        int meta = world.getBlockMetadata(x, y, z);
        return (((meta & 8) == 8) || isOpaqueCube());
    }
	
	@Override
    public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side) 
    {
        return (((world.getBlockMetadata(x, y, z) & 8) == 8 && (side == 1)) || isOpaqueCube());
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public Item getItem(World world, int i, int j, int k)
    {
        return Item.getItemFromBlock(singleSlab);
    }
	
	public static class SlabItems
	{
		public static class WoodSlab1Single extends ItemSlab
		{
			public WoodSlab1Single(Block block)
			{
				super(block, (BlockSlab)LOTRMod.woodSlabSingle, (BlockSlab)LOTRMod.woodSlabDouble, false);
			}
		}
		
		public static class WoodSlab1Double extends ItemSlab
		{
			public WoodSlab1Double(Block block)
			{
				super(block, (BlockSlab)LOTRMod.woodSlabSingle, (BlockSlab)LOTRMod.woodSlabDouble, true);
			}
		}
		
		public static class WoodSlab2Single extends ItemSlab
		{
			public WoodSlab2Single(Block block)
			{
				super(block, (BlockSlab)LOTRMod.woodSlabSingle2, (BlockSlab)LOTRMod.woodSlabDouble2, false);
			}
		}
		
		public static class WoodSlab2Double extends ItemSlab
		{
			public WoodSlab2Double(Block block)
			{
				super(block, (BlockSlab)LOTRMod.woodSlabSingle2, (BlockSlab)LOTRMod.woodSlabDouble2, true);
			}
		}
		
		public static class Slab1Single extends ItemSlab
		{
			public Slab1Single(Block block)
			{
				super(block, (BlockSlab)LOTRMod.slabSingle, (BlockSlab)LOTRMod.slabDouble, false);
			}
		}
		
		public static class Slab1Double extends ItemSlab
		{
			public Slab1Double(Block block)
			{
				super(block, (BlockSlab)LOTRMod.slabSingle, (BlockSlab)LOTRMod.slabDouble, true);
			}
		}
		
		public static class Slab2Single extends ItemSlab
		{
			public Slab2Single(Block block)
			{
				super(block, (BlockSlab)LOTRMod.slabSingle2, (BlockSlab)LOTRMod.slabDouble2, false);
			}
		}
		
		public static class Slab2Double extends ItemSlab
		{
			public Slab2Double(Block block)
			{
				super(block, (BlockSlab)LOTRMod.slabSingle2, (BlockSlab)LOTRMod.slabDouble2, true);
			}
		}
		
		public static class Slab3Single extends ItemSlab
		{
			public Slab3Single(Block block)
			{
				super(block, (BlockSlab)LOTRMod.slabSingle3, (BlockSlab)LOTRMod.slabDouble3, false);
			}
		}
		
		public static class Slab3Double extends ItemSlab
		{
			public Slab3Double(Block block)
			{
				super(block, (BlockSlab)LOTRMod.slabSingle3, (BlockSlab)LOTRMod.slabDouble3, true);
			}
		}
		
		public static class Slab4Single extends ItemSlab
		{
			public Slab4Single(Block block)
			{
				super(block, (BlockSlab)LOTRMod.slabSingle4, (BlockSlab)LOTRMod.slabDouble4, false);
			}
		}
		
		public static class Slab4Double extends ItemSlab
		{
			public Slab4Double(Block block)
			{
				super(block, (BlockSlab)LOTRMod.slabSingle4, (BlockSlab)LOTRMod.slabDouble4, true);
			}
		}
		
		public static class ThatchSingle extends ItemSlab
		{
			public ThatchSingle(Block block)
			{
				super(block, (BlockSlab)LOTRMod.slabSingleThatch, (BlockSlab)LOTRMod.slabDoubleThatch, false);
			}
		}
		
		public static class ThatchDouble extends ItemSlab
		{
			public ThatchDouble(Block block)
			{
				super(block, (BlockSlab)LOTRMod.slabSingleThatch, (BlockSlab)LOTRMod.slabDoubleThatch, true);
			}
		}
	}
}
