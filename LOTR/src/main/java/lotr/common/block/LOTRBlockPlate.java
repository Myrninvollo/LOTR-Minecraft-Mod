package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityPlate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockPlate extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	private IIcon[] plateIcons;
	
	public LOTRBlockPlate()
	{
		super(Material.circuits);
		setBlockBounds(0.125F, 0F, 0.125F, 0.875F, 0.125F, 0.875F);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int i)
	{
		return new LOTRTileEntityPlate();
	}
	
	@Override
    public Item getItemDropped(int i, Random random, int j)
	{
		return LOTRMod.plate;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int i, int j, int k)
    {
        return LOTRMod.plate;
    }
	
	@Override
    public void breakBlock(World world, int i, int j, int k, Block block, int meta)
    {
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (!world.isRemote && tileentity instanceof LOTRTileEntityPlate)
		{
			LOTRTileEntityPlate plate = (LOTRTileEntityPlate)tileentity;
			ItemStack foodItem = plate.getFoodItem();
			if (foodItem != null)
			{
				dropBlockAsItem(world, i, j, k, foodItem);
			}
		}
        super.breakBlock(world, i, j, k, block, meta);
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
		return LOTRMod.proxy.getPlateRenderID();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		return i == 1 ? plateIcons[0] : plateIcons[1];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		plateIcons = new IIcon[2];
        plateIcons[0] = iconregister.registerIcon(getTextureName() + "_top");
		plateIcons[1] = iconregister.registerIcon(getTextureName() + "_base");
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
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2)
    {
		ItemStack itemstack = entityplayer.getCurrentEquippedItem();
		TileEntity tileentity = world.getTileEntity(i, j, k);
		
		if (tileentity instanceof LOTRTileEntityPlate)
		{
			LOTRTileEntityPlate plate = (LOTRTileEntityPlate)tileentity;
			ItemStack plateItem = plate.getFoodItem();
			
			if (plateItem == null && itemstack != null && itemstack.getItem() instanceof ItemFood)
			{
				plate.setFoodItem(itemstack.copy());
				if (!entityplayer.capabilities.isCreativeMode)
				{
					itemstack.stackSize--;
				}
				return true;
			}
			else if (plateItem != null)
			{
				if (entityplayer.canEat(false))
				{
					plateItem.getItem().onEaten(plateItem, world, entityplayer);
					plate.setFoodItem(null);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static ItemStack getFoodItem(World world, int i, int j, int k)
	{
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity instanceof LOTRTileEntityPlate)
		{
			return ((LOTRTileEntityPlate)tileentity).getFoodItem();
		}
		return null;
	}
	
	public void dropPlateItem(LOTRTileEntityPlate plate)
	{
		dropBlockAsItem(plate.getWorldObj(), plate.xCoord, plate.yCoord, plate.zCoord, plate.getFoodItem());
	}
}
