package lotr.common.item;

import lotr.common.entity.item.LOTREntityBannerWall;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class LOTRItemBannerWall extends LOTRItemBanner
{
    public LOTRItemBannerWall()
    {
        super();
        bFull3D = false;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2)
    {
        if (side == 0)
        {
            return false;
        }
        else if (side == 1)
        {
            return false;
        }
        else
        {
            if (!entityplayer.canPlayerEdit(i, j, k, side, itemstack))
            {
                return false;
            }
            else
            {
				int l = Direction.facingToDirection[side];
				LOTREntityBannerWall banner = new LOTREntityBannerWall(world, i, j, k, l);
                if (world.checkNoEntityCollision(banner.boundingBox) && !world.isAnyLiquid(banner.boundingBox) && banner.onValidSurface())
                {
					banner.setBannerType(itemstack.getItemDamage());
                    if (!world.isRemote)
                    {
                        world.spawnEntityInWorld(banner);
                    }
					world.playSoundAtEntity(banner, Blocks.planks.stepSound.func_150496_b(), (Blocks.planks.stepSound.getVolume() + 1F) / 2F, Blocks.planks.stepSound.getPitch() * 0.8F);
                    itemstack.stackSize--;
					return true;
                }
				banner.setDead();
				return false;
            }
        }
    }
}
