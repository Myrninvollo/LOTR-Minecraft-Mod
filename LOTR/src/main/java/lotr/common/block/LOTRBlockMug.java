package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemMug;
import lotr.common.item.LOTRItemMugBrewable;
import lotr.common.tileentity.LOTRTileEntityMug;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockMug extends BlockContainer
{
	public LOTRBlockMug()
	{
		super(Material.circuits);
		float f = 0.1875F * 0.75F;
		float f1 = 0.5F * 0.75F;
		setBlockBounds(0.5F - f, 0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int i)
	{
		return new LOTRTileEntityMug();
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
		return -1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		return LOTRMod.blockOreStorage.getIcon(i, 1);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {}
	
	@Override
	public boolean canBlockStay(World world, int i, int j, int k)
	{
		Block block = world.getBlock(i, j - 1, k);
		return block.canPlaceTorchOnTop(world, i, j - 1, k);
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
			world.setBlockToAir(i, j, k);
		}
	}
	
	@Override
    public void onBlockHarvested(World world, int i, int j, int k, int meta, EntityPlayer entityplayer)
    {
        if (entityplayer.capabilities.isCreativeMode)
        {
            world.setBlockMetadataWithNotify(i, j, k, meta | 4, 4);
        }

        super.onBlockHarvested(world, i, j, k, meta, entityplayer);
    }
	
	@Override
    public void breakBlock(World world, int i, int j, int k, Block block, int meta)
    {
		if (!world.isRemote && (meta & 4) == 0)
		{
			dropBlockAsItem(world, i, j, k, getMugItem(world, i, j, k));
		}
		
		super.breakBlock(world, i, j, k, block, meta);
	}
	
	@Override
	public Item getItemDropped(int i, Random random, int j)
	{
		return null;
	}
	
	@Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int i, int j, int k)
    {
        return getMugItem(world, i, j, k);
    }
	
	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2)
    {
		ItemStack itemstack = entityplayer.getCurrentEquippedItem();
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityMug)
		{
			LOTRTileEntityMug mug = (LOTRTileEntityMug)tileentity;
			ItemStack mugItem = getMugItem(world, i, j, k);
			if (mugItem.getItem() != LOTRMod.mug && itemstack != null && itemstack.getItem() == LOTRMod.mug)
			{
				if (entityplayer.capabilities.isCreativeMode)
				{
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, mugItem);
				}
				else
				{
					itemstack.stackSize--;
					if (itemstack.stackSize <= 1)
					{
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, mugItem);
					}
					else if (!entityplayer.inventory.addItemStackToInventory(mugItem.copy()))
					{
						entityplayer.dropPlayerItemWithRandomChoice(mugItem.copy(), false);
					}
				}
				
				mug.setEmpty();
				world.markBlockForUpdate(i, j, k);
				world.playSoundAtEntity(entityplayer, "lotr:item.mug_fill", 0.5F, 0.8F + (world.rand.nextFloat() * 0.4F));
				return true;
			}
			else if (mugItem.getItem() == LOTRMod.mug && itemstack != null && itemstack.getItem() != LOTRMod.mug && (itemstack.getItem() instanceof LOTRItemMug || itemstack.getItem() instanceof LOTRItemMugBrewable))
			{
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(LOTRMod.mug));
				mug.mugItem = itemstack.getItem();
				mug.itemDamage = itemstack.getItemDamage();
				world.markBlockForUpdate(i, j, k);
				world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "lotr:item.mug_fill", 0.5F, 0.8F + (world.rand.nextFloat() * 0.4F));
				return true;
			}
			else if (mugItem.getItem() != LOTRMod.mug)
			{
				if ((mugItem.getItem() instanceof LOTRItemMugBrewable) || (mugItem.getItem() instanceof LOTRItemMug && (((LOTRItemMug)mugItem.getItem()).alwaysDrinkable || entityplayer.canEat(false))))
				{
					mugItem.getItem().onEaten(mugItem, world, entityplayer);
					mug.setEmpty();
					world.markBlockForUpdate(i, j, k);
					world.playSoundAtEntity(entityplayer, "random.drink", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
					return true;
				}
			}
		}
		return false;
	}
	
	public static ItemStack getMugItem(World world, int i, int j, int k)
	{
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityMug)
		{
			LOTRTileEntityMug mug = (LOTRTileEntityMug)tileentity;
			if (mug.mugItem == null)
			{
				return new ItemStack(LOTRMod.mug);
			}
			return new ItemStack(mug.mugItem, 1, mug.itemDamage);	
		}
		return new ItemStack(LOTRMod.mug);
	}
	
	public static void setMugItem(World world, int i, int j, int k, ItemStack itemstack)
	{
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityMug)
		{
			LOTRTileEntityMug mug = (LOTRTileEntityMug)tileentity;
			mug.mugItem = itemstack.getItem();
			mug.itemDamage = itemstack.getItemDamage();
		}
	}
}
