package lotr.common.block;

import java.util.List;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityTrollTotem;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockTrollTotem extends BlockContainer
{
	public LOTRBlockTrollTotem()
	{
		super(Material.rock);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int i)
	{
		return new LOTRTileEntityTrollTotem();
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
		return LOTRMod.proxy.getTrollTotemRenderID();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		return Blocks.stone.getIcon(i, 0);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {}
	
    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entity, ItemStack itemstack)
    {
		int meta = itemstack.getItemDamage();
        int rotation = MathHelper.floor_double((double)(entity.rotationYaw * 4F / 360F) + 0.5D) & 3;
		world.setBlockMetadataWithNotify(i, j, k, meta | (rotation << 2), 2);
		
		if (meta == 0)
		{
			if (world.getBlock(i, j - 1, k) == this && (world.getBlockMetadata(i, j - 1, k) & 3) == 1)
			{
				world.setBlockMetadataWithNotify(i, j - 1, k, 1 | (rotation << 2), 3);
				if (world.getBlock(i, j - 2, k) == this && (world.getBlockMetadata(i, j - 2, k) & 3) == 2)
				{
					world.setBlockMetadataWithNotify(i, j - 2, k, 2 | (rotation << 2), 3);
				}
			}
		}
		
		if (meta == 1)
		{
			if (world.getBlock(i, j - 1, k) == this && (world.getBlockMetadata(i, j - 1, k) & 3) == 2)
			{
				world.setBlockMetadataWithNotify(i, j - 1, k, 2 | (rotation << 2), 3);
			}
			if (world.getBlock(i, j + 1, k) == this && (world.getBlockMetadata(i, j + 1, k) & 3) == 0)
			{
				world.setBlockMetadataWithNotify(i, j + 1, k, 0 | (rotation << 2), 3);
			}
		}
		
		if (meta == 2)
		{
			if (world.getBlock(i, j + 1, k) == this && (world.getBlockMetadata(i, j + 1, k) & 3) == 1)
			{
				world.setBlockMetadataWithNotify(i, j + 1, k, 1 | (rotation << 2), 3);
				if (world.getBlock(i, j + 2, k) == this && (world.getBlockMetadata(i, j + 2, k) & 3) == 0)
				{
					world.setBlockMetadataWithNotify(i, j + 2, k, 0 | (rotation << 2), 3);
				}
			}
		}
    }
	
	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2)
    {
		if ((world.getBlockMetadata(i, j, k) & 3) == 0)
		{
			TileEntity tileentity = world.getTileEntity(i, j, k);
			if (tileentity != null && tileentity instanceof LOTRTileEntityTrollTotem)
			{
				LOTRTileEntityTrollTotem totem = (LOTRTileEntityTrollTotem)tileentity;
				if (totem.canSummon())
				{
					ItemStack itemstack = entityplayer.inventory.getCurrentItem();
					if (itemstack != null && LOTRMod.isOreNameEqual(itemstack, "bone"))
					{
						if (!entityplayer.capabilities.isCreativeMode)
						{
							itemstack.stackSize--;
							if (itemstack.stackSize <= 0)
							{
								entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
							}
						}
						totem.summon();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public int damageDropped(int i)
	{
		return i & 3;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int j = 0; j <= 2; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
}
