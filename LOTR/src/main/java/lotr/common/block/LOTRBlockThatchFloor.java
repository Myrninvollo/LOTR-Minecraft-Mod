package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTRBlockThatchFloor extends Block
{
	public LOTRBlockThatchFloor()
	{
		super(Material.carpet);
		setHardness(0.2F);
		setStepSound(Block.soundTypeGrass);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
		setBlockBounds(0F, 0F, 0F, 1F, 0.0625F, 1F);
	}

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
}
