package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTRItemBed extends Item
{
	private Block theBedBlock;
	
    public LOTRItemBed(Block block)
    {
        super();
		setMaxStackSize(1);
        setCreativeTab(LOTRCreativeTabs.tabDeco);
		theBedBlock = block;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2)
    {
        if (world.isRemote)
        {
            return true;
        }
        else if (side != 1)
        {
            return false;
        }
        else
        {
            j++;
            int i1 = MathHelper.floor_double((double)(entityplayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            byte b0 = 0;
            byte b1 = 0;

            if (i1 == 0)
            {
                b1 = 1;
            }

            if (i1 == 1)
            {
                b0 = -1;
            }

            if (i1 == 2)
            {
                b1 = -1;
            }

            if (i1 == 3)
            {
                b0 = 1;
            }

            if (entityplayer.canPlayerEdit(i, j, k, side, itemstack) && entityplayer.canPlayerEdit(i + b0, j, k + b1, side, itemstack))
            {
                if (world.isAirBlock(i, j, k) && world.isAirBlock(i + b0, j, k + b1) && world.getBlock(i, j - 1, k).isSideSolid(world, i, j - 1, k, ForgeDirection.UP) && world.getBlock(i + b0, j - 1, k + b1).isSideSolid(world, i + b0, j - 1, k + b1, ForgeDirection.UP))
                {
                    world.setBlock(i, j, k, theBedBlock, i1, 3);

                    if (world.getBlock(i, j, k) == theBedBlock)
                    {
                        world.setBlock(i + b0, j, k + b1, theBedBlock, i1 + 8, 3);
                    }
					
					world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, theBedBlock.stepSound.func_150496_b(), (theBedBlock.stepSound.getVolume() + 1.0F) / 2.0F, theBedBlock.stepSound.getPitch() * 0.8F);
                    itemstack.stackSize--;
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
    }
}
