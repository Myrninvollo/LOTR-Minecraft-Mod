package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockOrcTorch extends Block
{
	@SideOnly(Side.CLIENT)
	private IIcon[] torchIcons;
	private boolean dropTorchAsItem = true;
	
	public LOTRBlockOrcTorch()
	{
		super(Material.circuits);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		return j == 1 ? torchIcons[1] : torchIcons[0];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		torchIcons = new IIcon[2];
        torchIcons[0] = iconregister.registerIcon(getTextureName() + "_bottom");
		torchIcons[1] = iconregister.registerIcon(getTextureName() + "_top");
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
	public int getRenderType()
	{
		return LOTRMod.proxy.getOrcTorchRenderID();
	}
	
    @Override
    public Item getItemDropped(int i, Random random, int j)
    {
        if (i == 0)
        {
        	 return LOTRMod.orcTorchItem;
        }
        else
        {
        	return null;
        }
    }

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, Block block)
	{
        if (!canBlockStay(world, i, j, k))
        {
            int l = world.getBlockMetadata(i, j, k);
            if (l == 0)
            {
               dropBlockAsItem(world, i, j, k, l, 0);

                if (world.getBlock(i, j + 1, k) == this)
                {
                    world.setBlock(i, j + 1, k, Blocks.air, 0, 2);
                }
            }

            world.setBlock(i, j, k, Blocks.air, 0, 2);
        }
	}
	
    @Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
        if (world.getBlock(i, j, k) != this)
        {
        	return super.canBlockStay(world, i, j, k);
        }

        int l = world.getBlockMetadata(i, j, k);
        return l == 1 ? world.getBlock(i, j - 1, k) == this : world.getBlock(i, j + 1, k) == this && canPlaceTorchOn(world, i, j - 1, k);
    }
	
	public static boolean canPlaceTorchOn(World world, int i, int j, int k)
	{
		return world.getBlock(i, j, k).canPlaceTorchOnTop(world, i, j, k);
	}
	
	@Override
    public void onBlockHarvested(World world, int i, int j, int k, int meta, EntityPlayer entityplayer)
    {
        if (meta == 1)
        {
            if (world.getBlock(i, j - 1, k) == this)
            {
                if (!entityplayer.capabilities.isCreativeMode)
                {
                	world.func_147480_a(i, j - 1, k, true);
                }
                else
                {
                    world.setBlockToAir(i, j - 1, k);
                }
            }
        }
        else if (entityplayer.capabilities.isCreativeMode && world.getBlock(i, j + 1, k) == this)
        {
            world.setBlock(i, j + 1, k, Blocks.air, 0, 2);
        }

        super.onBlockHarvested(world, i, j, k, meta, entityplayer);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
        setBlockBoundsBasedOnState(world, i, j, k);
        return super.getSelectedBoundingBoxFromPool(world, i, j, k);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
    {
        int meta = world.getBlockMetadata(i, j, k);
		if (meta == 0)
		{
			setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 1F, 0.6F);
		}
		else if (meta == 1)
		{
			setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.5375F, 0.6F);
		}
    }
	
	@Override
	public int getLightValue(IBlockAccess world, int i, int j, int k)
	{
		return world.getBlockMetadata(i, j, k) == 1 ? 14 : 0;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
		if (world.getBlockMetadata(i, j, k) == 1)
		{
			double d = (double)((float)i + 0.5F);
			double d1 = (double)((float)j + 0.6F);
			double d2 = (double)((float)k + 0.5F);
			world.spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", d, d1, d2, 0.0D, 0.0D, 0.0D);
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public Item getItem(World world, int i, int j, int k)
    {
        return LOTRMod.orcTorchItem;
    }
}
