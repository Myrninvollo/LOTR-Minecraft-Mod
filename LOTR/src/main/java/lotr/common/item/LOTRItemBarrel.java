package lotr.common.item;

import lotr.common.entity.item.LOTREntityBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTRItemBarrel extends ItemBlock
{
    public LOTRItemBarrel(Block block)
    {
        super(block);
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
