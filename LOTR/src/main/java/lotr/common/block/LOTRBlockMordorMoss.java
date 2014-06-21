package lotr.common.block;

import java.util.ArrayList;
import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class LOTRBlockMordorMoss extends Block implements IShearable
{
    public LOTRBlockMordorMoss()
    {
        super(Material.plants);
		setBlockBounds(0F, 0F, 0F, 1F, 0.0625F, 1F);
		setTickRandomly(true);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
    }
	
    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return super.canPlaceBlockAt(world, i, j, k) && canBlockStay(world, i, j, k);
    }

    @Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
        if (j >= 0 && j < 256)
        {
            return world.getBlock(i, j - 1, k) == LOTRMod.rock && world.getBlockMetadata(i, j - 1, k) == 0;
        }
        else
        {
            return false;
        }
    }
	
	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block block)
    {
        super.onNeighborBlockChange(world, i, j, k, block);
        checkMossCanStay(world, i, j, k);
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        checkMossCanStay(world, i, j, k);
    }

    private void checkMossCanStay(World world, int i, int j, int k)
    {
        if (!canBlockStay(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockToAir(i, j, k);
        }
    }
	
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return null;
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
        drops.add(new ItemStack(this));
        return drops;
	}
}
