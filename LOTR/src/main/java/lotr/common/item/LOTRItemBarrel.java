package lotr.common.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lotr.common.entity.item.LOTREntityBarrel;
import lotr.common.tileentity.LOTRTileEntityBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class LOTRItemBarrel extends ItemBlock
{
    public LOTRItemBarrel(Block block)
    {
        super(block);
    }
    
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
    {
		if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("LOTRBarrelData"))
		{
			NBTTagCompound barrelData = itemstack.getTagCompound().getCompoundTag("LOTRBarrelData");
			LOTRTileEntityBarrel tileEntity = new LOTRTileEntityBarrel();
			tileEntity.readBarrelFromNBT(barrelData);
			list.add(tileEntity.getInvSubtitle());
		}
	}
    
	@Override
    public boolean placeBlockAt(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2, int metadata)
    {
		if (super.placeBlockAt(itemstack, entityplayer, world, i, j, k, side, f, f1, f2, metadata))
		{
			TileEntity tileentity = world.getTileEntity(i, j, k);
			if (tileentity != null && tileentity instanceof LOTRTileEntityBarrel)
			{
				LOTRTileEntityBarrel barrel = (LOTRTileEntityBarrel)tileentity;
				
				if (itemstack.hasTagCompound())
				{
					NBTTagCompound barrelData = itemstack.getTagCompound().getCompoundTag("LOTRBarrelData");
					barrel.readBarrelFromNBT(barrelData);
				}
			}
			return true;
		}
		return false;
    }
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        MovingObjectPosition m = getMovingObjectPositionFromPlayer(world, entityplayer, true);

        if (m == null)
        {
            return itemstack;
        }
		else if (m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
		{
			int i = m.blockX;
			int j = m.blockY;
			int k = m.blockZ;
			
			if (world.getBlock(i, j, k).getMaterial() != Material.water || world.getBlockMetadata(i, j, k) != 0)
			{
				return itemstack;
			}
			
			LOTREntityBarrel barrel = new LOTREntityBarrel(world, (double)((float)i + 0.5F), (double)((float)j + 1F), (double)((float)k + 0.5F));
			barrel.rotationYaw = (float)((MathHelper.floor_double((double)(entityplayer.rotationYaw * 4F / 360F) + 0.5D) & 3) - 1) * 90F;

			if (!world.getCollidingBoundingBoxes(barrel, barrel.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
			{
				return itemstack;
			}

			if (!world.isRemote)
			{
				if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("LOTRBarrelData"))
				{
					NBTTagCompound barrelData = itemstack.getTagCompound().getCompoundTag("LOTRBarrelData");
					barrel.barrelItemData = barrelData;
				}
				world.spawnEntityInWorld(barrel);
			}

			if (!entityplayer.capabilities.isCreativeMode)
			{
				--itemstack.stackSize;
			}
        }
		
		return itemstack;
    }
}
