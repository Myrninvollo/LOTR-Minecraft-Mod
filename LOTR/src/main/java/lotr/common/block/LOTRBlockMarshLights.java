package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockMarshLights extends Block
{
	public LOTRBlockMarshLights()
	{
		super(Material.circuits);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {}
	
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }
	
	@Override
    public boolean isCollidable()
    {
        return false;
    }

	@Override
	public int getRenderType()
	{
		return -1;
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k)
	{
		return canBlockStay(world, i, j, k);
	}
	
	@Override
	public boolean canBlockStay(World world, int i, int j, int k)
	{
		return world.getBlock(i, j - 1, k).getMaterial() == Material.water;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, Block block)
	{
		if (!canBlockStay(world, i, j, k))
		{
			world.setBlockToAir(i, j, k);
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if (random.nextInt(3) > 0)
		{
			if (random.nextInt(3) == 0)
			{
				LOTRMod.proxy.spawnParticle("marshFlame", i + random.nextFloat(), j - 0.5D, k + random.nextFloat(), 0D, (double)(0.05F + random.nextFloat() * 0.1F), 0D);
			}
			else
			{
				LOTRMod.proxy.spawnParticle("marshLight", i + random.nextFloat(), j - 0.5D, k + random.nextFloat(), 0D, (double)(0.05F + random.nextFloat() * 0.1F), 0D);
			}
		}
    }
}
