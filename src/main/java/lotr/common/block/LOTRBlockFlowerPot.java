package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityFlowerPot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockFlowerPot extends BlockFlowerPot implements ITileEntityProvider
{
	public LOTRBlockFlowerPot()
	{
		super();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int i)
	{
		return new LOTRTileEntityFlowerPot();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
        return Blocks.flower_pot.getIcon(i, j);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {}
	
	@Override
    public int getRenderType()
    {
        return LOTRMod.proxy.getFlowerPotRenderID();
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int i, int j, int k)
    {
        ItemStack itemstack = getPlant(world, i, j, k);
        return itemstack == null ? Items.flower_pot : itemstack.getItem();
    }
	
	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2)
    {
		return false;
	}
	
	@Override
    public void onBlockHarvested(World world, int i, int j, int k, int meta, EntityPlayer entityplayer)
    {
        if (entityplayer.capabilities.isCreativeMode)
        {
            world.setBlockMetadataWithNotify(i, j, k, 1, 4);
        }

        super.onBlockHarvested(world, i, j, k, meta, entityplayer);
    }
	
	@Override
    public void breakBlock(World world, int i, int j, int k, Block block, int meta)
    {
		if (!world.isRemote && meta == 0)
		{
			ItemStack itemstack = getPlant(world, i, j, k);
			if (itemstack != null)
			{
				dropBlockAsItem(world, i, j, k, itemstack);
			}
		}
		
		super.breakBlock(world, i, j, k, block, meta);
	}
	
	public static boolean canAcceptPlant(ItemStack itemstack)
	{
		Item item = itemstack.getItem();
		return item instanceof ItemBlock && ((ItemBlock)item).field_150939_a instanceof LOTRBlockFlower;
	}
	
	public static void setPlant(World world, int i, int j, int k, ItemStack itemstack)
	{
		TileEntity tileentity = (TileEntity)world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityFlowerPot)
		{
			LOTRTileEntityFlowerPot flowerPot = (LOTRTileEntityFlowerPot)tileentity;
			flowerPot.item = itemstack.getItem();
			flowerPot.meta = itemstack.getItemDamage();
			world.markBlockForUpdate(i, j, k);
		}
	}
	
	public static ItemStack getPlant(IBlockAccess world, int i, int j, int k)
	{
		TileEntity tileentity = (TileEntity)world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityFlowerPot)
		{
			LOTRTileEntityFlowerPot flowerPot = (LOTRTileEntityFlowerPot)tileentity;
			if (flowerPot.item == null)
			{
				return null;
			}
			return new ItemStack(flowerPot.item, 1, flowerPot.meta);
		}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if (getPlant(world, i, j, k) != null && getPlant(world, i, j, k).getItem() == Item.getItemFromBlock(LOTRMod.pipeweedPlant))
		{
			double d = i + 0.2D + (double)(random.nextFloat() * 0.6F);
			double d1 = j + 0.625D + (double)(random.nextFloat() * 0.1875F);
			double d2 = k + 0.2D + (double)(random.nextFloat() * 0.6F);
			world.spawnParticle("smoke", d, d1, d2, 0D, 0D, 0D);
		}
	}
}
