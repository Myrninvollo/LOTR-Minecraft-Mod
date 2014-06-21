package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemEntDraught;
import lotr.common.tileentity.LOTRTileEntityEntJar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockEntJar extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	private IIcon[] jarIcons;
	
	public LOTRBlockEntJar()
	{
		super(Material.circuits);
		setBlockBounds(0.25F, 0F, 0.25F, 0.75F, 0.875F, 0.75F);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int i)
	{
		return new LOTRTileEntityEntJar();
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
		return LOTRMod.proxy.getEntJarRenderID();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		return i == 0 || i == 1 ? jarIcons[0] : jarIcons[1];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		jarIcons = new IIcon[2];
        jarIcons[0] = iconregister.registerIcon(getTextureName() + "_top");
		jarIcons[1] = iconregister.registerIcon(getTextureName() + "_side");
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
		if (tileentity != null && tileentity instanceof LOTRTileEntityEntJar)
		{
			LOTRTileEntityEntJar jar = (LOTRTileEntityEntJar)tileentity;
			if (itemstack != null && itemstack.getItem() instanceof LOTRItemEntDraught)
			{
				if (jar.fillFrom(itemstack))
				{
					if (!entityplayer.capabilities.isCreativeMode)
					{
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(Items.bowl));
					}
					world.markBlockForUpdate(i, j, k);
					world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "lotr:item.mug_fill", 0.5F, 0.8F + (world.rand.nextFloat() * 0.4F));
					return true;
				}
			}
			if (jar.drinkItem != null && jar.drinkItem instanceof LOTRItemEntDraught)
			{
				LOTRItemEntDraught drink = (LOTRItemEntDraught)jar.drinkItem;
				if (itemstack != null && itemstack.getItem() == Items.bowl)
				{
					if (!entityplayer.capabilities.isCreativeMode)
					{
						itemstack.stackSize--;
					}
					if (itemstack.stackSize <= 0)
					{
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(drink));
					}
					else if (!entityplayer.inventory.addItemStackToInventory(new ItemStack(drink)))
					{
						entityplayer.dropPlayerItemWithRandomChoice(new ItemStack(drink), false);
					}
					world.playSoundAtEntity(entityplayer, "lotr:item.mug_fill", 0.5F, 0.8F + (world.rand.nextFloat() * 0.4F));
					jar.consume();
					world.markBlockForUpdate(i, j, k);
					return true;
				}
				else if (drink.canPlayerDrink(entityplayer))
				{
					drink.onEaten(new ItemStack(drink), world, entityplayer);
					world.playSoundAtEntity(entityplayer, "random.drink", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
					jar.consume();
					world.markBlockForUpdate(i, j, k);
					return true;
				}
			}
		}
		return false;
	}
}
