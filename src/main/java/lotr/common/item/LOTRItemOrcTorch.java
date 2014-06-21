package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockOrcTorch;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRItemOrcTorch extends Item
{
    public LOTRItemOrcTorch()
    {
       	super();
		setCreativeTab(LOTRCreativeTabs.tabDeco);
    }
	
	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2)
    {
        Block block = world.getBlock(i, j, k);
        if (block == Blocks.snow_layer)
        {
            side = 1;
        }
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush)
        {
            if (side == 0)
            {
                --j;
            }
            if (side == 1)
            {
                ++j;
            }
            if (side == 2)
            {
                --k;
            }
            if (side == 3)
            {
                ++k;
            }
            if (side == 4)
            {
                --i;
            }
            if (side == 5)
            {
                ++i;
            }
        }

		Block block1 = LOTRMod.orcTorch;
        if (!entityplayer.canPlayerEdit(i, j, k, side, itemstack) || !entityplayer.canPlayerEdit(i, j + 1, k, side, itemstack))
        {
            return false;
        }
        else if (!world.canPlaceEntityOnSide(block, i, j, k, false, side, null, itemstack) || !world.canPlaceEntityOnSide(block, i, j + 1, k, false, side, null, itemstack))
        {
            return false;
        }
		else if (!LOTRBlockOrcTorch.canPlaceTorchOn(world, i, j - 1, k))
		{
			return false;
		}
        else
        {
			world.setBlock(i, j, k, block1, 0, 2);
			world.setBlock(i, j + 1, k, block1, 1, 2);
			SoundType stepSound = block1.stepSound;
			world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), stepSound.func_150496_b(), (stepSound.getVolume() + 1.0F) / 2.0F, stepSound.getPitch() * 0.8F);
			itemstack.stackSize--;
			return true;   
        }
    }
}