package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityBeacon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockBeacon extends BlockContainer
{
	public LOTRBlockBeacon()
	{
		super(Material.wood);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
		setBlockBounds(0F, 0F, 0F, 1F, 0.8125F, 1F);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		return Blocks.planks.getIcon(i, 0);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {}
	
	@Override
	public TileEntity createNewTileEntity(World world, int i)
	{
		return new LOTRTileEntityBeacon();
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
		return LOTRMod.proxy.getBeaconRenderID();
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
		else if (isLit(world, i, j, k) && world.getBlock(i, j + 1, k).getMaterial() == Material.water)
		{
			world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
			if (!world.isRemote)
			{
				setLit(world, i, j, k, false);
			}
		}
	}
	
	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2)
    {
		ItemStack itemstack = entityplayer.getCurrentEquippedItem();
        if (itemstack != null && itemstack.getItem() == Items.flint_and_steel && !isLit(world, i, j, k) && world.getBlock(i, j + 1, k).getMaterial() != Material.water)
        {
			world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "fire.ignite", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
			itemstack.damageItem(1, entityplayer);
			if (!world.isRemote)
			{
				setLit(world, i, j, k, true);
				LOTRLevelData.setBeaconState(1);
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.lightGondorBeacon);
			}
			return true;
        }
		else if (itemstack != null && itemstack.getItem() == Items.water_bucket && isLit(world, i, j, k))
		{
			world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
			if (!entityplayer.capabilities.isCreativeMode)
			{
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(Items.bucket));
			}
			if (!world.isRemote)
			{
				setLit(world, i, j, k, false);
				LOTRLevelData.setBeaconState(0);
			}
			return true;
		}
		return false;
    }
	
	@Override
	public int getLightValue(IBlockAccess world, int i, int j, int k)
	{
		return getLitCounter(world, i, j, k) == 100 ? 15 : 0;
	}
	
	public static boolean isLit(IBlockAccess world, int i, int j, int k)
	{
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityBeacon)
		{
			return ((LOTRTileEntityBeacon)tileentity).isLit;
		}
		return false;
	}
	
	public static int getLitCounter(IBlockAccess world, int i, int j, int k)
	{
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityBeacon)
		{
			return ((LOTRTileEntityBeacon)tileentity).litCounter;
		}
		return 0;
	}
	
	public static void setLit(World world, int i, int j, int k, boolean lit)
	{
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityBeacon)
		{
			((LOTRTileEntityBeacon)tileentity).isLit = lit;
			if (!lit)
			{
				((LOTRTileEntityBeacon)tileentity).litCounter = 0;
			}
			else
			{
				((LOTRTileEntityBeacon)tileentity).unlitCounter = 0;
			}
			world.updateLightByType(EnumSkyBlock.Block, i, j, k);
			world.markBlockForUpdate(i, j, k);
		}
	}
	
	@Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        if (entity.isBurning() && !isLit(world, i, j, k) && world.getBlock(i, j + 1, k).getMaterial() != Material.water)
        {
			world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "fire.ignite", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
			if (!world.isRemote)
			{
				setLit(world, i, j, k, true);
				LOTRLevelData.setBeaconState(1);
				entity.setDead();
			}
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if (!isLit(world, i, j, k))
		{
			return;
		}
		
        if (random.nextInt(24) == 0)
        {
            world.playSound((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), "fire.fire", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
        }
		
		for (int l = 0; l < 3; l++)
		{
			double d = i + (double)random.nextFloat();
			double d1 = j + (double)random.nextFloat() * 0.5D + 0.5D;
			double d2 = k + (double)random.nextFloat();
			world.spawnParticle("largesmoke", d, d1, d2, 0D, 0D, 0D);
		}
	}
}
