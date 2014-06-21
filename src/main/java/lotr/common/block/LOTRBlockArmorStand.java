package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityArmorStand;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockArmorStand extends Block
{
	public LOTRBlockArmorStand()
	{
		super(Material.circuits);
	}
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
		if (hasTileEntity(world.getBlockMetadata(i, j, k)))
		{
			return AxisAlignedBB.getAABBPool().getAABB((double)i, (double)j, (double)k, (double)i + 1D, (double)j + 0.125D, (double)k + 1D);
		}
		return null;
    }
	
	@Override
    public boolean hasTileEntity(int metadata)
    {
        return (metadata & 4) == 0;
    }

	@Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        if (hasTileEntity(metadata))
        {
            return new LOTRTileEntityArmorStand();
        }
        return null;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {}
	
	@Override
    @SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		return Blocks.planks.getIcon(i, 0);
	}
	
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
	public int getRenderType()
	{
		return -1;
	}
	
	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2)
    {
		if (!hasTileEntity(world.getBlockMetadata(i, j, k)))
		{
			j--;
		}
		
		if (hasTileEntity(world.getBlockMetadata(i, j, k)))
		{
			if (!world.isRemote)
			{
				entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_ARMOR_STAND, world, i, j, k);
			}
			return true;
		}
		return false;
    }
	
	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k)
	{
		return world.getBlock(i, j - 1, k).isSideSolid(world, i, j - 1, k, ForgeDirection.UP) && j < 255;
	}
	
	@Override
	public boolean canBlockStay(World world, int i, int j, int k)
	{
		int meta = world.getBlockMetadata(i, j, k);
		if (hasTileEntity(meta))
		{
			return world.getBlock(i, j - 1, k).isSideSolid(world, i, j - 1, k, ForgeDirection.UP) && world.getBlock(i, j + 1, k) == this;
		}
		else
		{
			return world.getBlock(i, j - 1, k) == this;
		}
	}
	
	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block block)
    {
        int meta = world.getBlockMetadata(i, j, k);
        if (hasTileEntity(meta))
        {
			if (!canBlockStay(world, i, j, k))
			{
				world.setBlockToAir(i, j, k);
				
                if (!world.isRemote)
                {
                    dropBlockAsItem(world, i, j, k, meta, 0);
                }
			}
        }
        else
        {
			if (!canBlockStay(world, i, j, k))
			{
				world.setBlockToAir(i, j, k);
			}
        }
    }
	
	@Override
    public void onBlockHarvested(World world, int i, int j, int k, int meta, EntityPlayer entityplayer)
    {
        if (entityplayer.capabilities.isCreativeMode && !hasTileEntity(meta) && world.getBlock(i, j - 1, k) == this)
        {
            world.setBlockToAir(i, j - 1, k);
        }
    }
	
	@Override
	public Item getItemDropped(int i, Random random, int j)
	{
		return hasTileEntity(i) ? LOTRMod.armorStandItem : null;
	}
	
	@Override
    public void breakBlock(World world, int i, int j, int k, Block block, int meta)
    {
		LOTRTileEntityArmorStand stand = (LOTRTileEntityArmorStand)world.getTileEntity(i, j, k);
		if (stand != null)
		{
			for (int slot = 0; slot < stand.getSizeInventory(); slot++)
			{
				ItemStack item = stand.getStackInSlot(slot);
				if (item != null)
				{
					float f = world.rand.nextFloat() * 0.8F + 0.1F;
					float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
					float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

					while (item.stackSize > 0)
					{
						int j1 = world.rand.nextInt(21) + 10;

						if (j1 > item.stackSize)
						{
							j1 = item.stackSize;
						}

						item.stackSize -= j1;
						EntityItem entityItem = new EntityItem(world, (double)((float)i + f), (double)((float)j + f1), (double)((float)k + f2), new ItemStack(item.getItem(), j1, item.getItemDamage()));

						if (item.hasTagCompound())
						{
							entityItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
						}

						entityItem.motionX = (double)((float)world.rand.nextGaussian() * 0.05F);
						entityItem.motionY = (double)((float)world.rand.nextGaussian() * 0.05F + 0.2F);
						entityItem.motionZ = (double)((float)world.rand.nextGaussian() * 0.05F);
						world.spawnEntityInWorld(entityItem);
					}
				}
			}
		}

        super.breakBlock(world, i, j, k, block, meta);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int i, int j, int k)
	{
		return LOTRMod.armorStandItem;
	}
}
