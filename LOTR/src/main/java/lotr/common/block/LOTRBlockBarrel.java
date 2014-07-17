package lotr.common.block;

import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockBarrel extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	private IIcon[] barrelIcons;
	
	public LOTRBlockBarrel()
	{
		super(Material.wood);
		setCreativeTab(LOTRCreativeTabs.tabFood);
		setBlockBounds(0.125F, 0F, 0.125F, 0.875F, 0.8125F, 0.875F);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int i)
	{
		return new LOTRTileEntityBarrel();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		if (i == -1)
		{
			return barrelIcons[2];
		}
		if (i < 2)
		{
			return barrelIcons[1];
		}
		return barrelIcons[0];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		barrelIcons = new IIcon[3];
        barrelIcons[0] = iconregister.registerIcon(getTextureName() + "_side");
		barrelIcons[1] = iconregister.registerIcon(getTextureName() + "_top");
		barrelIcons[2] = iconregister.registerIcon(getTextureName() + "_tap");
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
		return LOTRMod.proxy.getBarrelRenderID();
	}
	
    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entity, ItemStack itemstack)
    {
        int rotation = MathHelper.floor_double((double)(entity.rotationYaw * 4F / 360F) + 0.5D) & 3;
		
        if (rotation == 0)
        {
            world.setBlockMetadataWithNotify(i, j, k, 2, 2);
        }

        if (rotation == 1)
        {
            world.setBlockMetadataWithNotify(i, j, k, 5, 2);
        }

        if (rotation == 2)
        {
            world.setBlockMetadataWithNotify(i, j, k, 3, 2);
        }

        if (rotation == 3)
        {
            world.setBlockMetadataWithNotify(i, j, k, 4, 2);
        }
		
        if (itemstack.hasDisplayName())
        {
            ((LOTRTileEntityBarrel)world.getTileEntity(i, j, k)).setBarrelName(itemstack.getDisplayName());
        }
    }
	
	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2)
    {
		LOTRTileEntityBarrel barrel = (LOTRTileEntityBarrel)world.getTileEntity(i, j, k);
		ItemStack barrelDrink = barrel.getMugRefill();
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (barrelDrink != null && side == world.getBlockMetadata(i, j, k) && itemstack != null && itemstack.getItem() == LOTRMod.mug)
		{
			itemstack.stackSize--;
			if (itemstack.stackSize <= 0)
			{
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, barrelDrink);
			}
			else
			{
				if (!entityplayer.inventory.addItemStackToInventory(barrelDrink))
				{
					entityplayer.dropPlayerItemWithRandomChoice(barrelDrink, false);
				}
			}
			barrel.consumeMugRefill();
			world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "lotr:item.mug_fill", 0.5F, 0.8F + (world.rand.nextFloat() * 0.4F));
			return true;
		}
		if (!world.isRemote)
		{
			entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_BARREL, world, i, j, k);
		}
		return true;
    }
	
	@Override
    public void breakBlock(World world, int i, int j, int k, Block block, int meta)
    {
		LOTRTileEntityBarrel barrel = (LOTRTileEntityBarrel)world.getTileEntity(i, j, k);
		if (barrel != null)
		{
			barrel.setInventorySlotContents(9, null);
			LOTRMod.dropContainerItems(barrel, world, i, j, k);
		}

        super.breakBlock(world, i, j, k, block, meta);
    }
}
