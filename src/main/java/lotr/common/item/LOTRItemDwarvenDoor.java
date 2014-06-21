package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRItemDwarvenDoor extends ItemDoor
{
	public LOTRItemDwarvenDoor()
	{
		super(Material.rock);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
	}
	
	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2)
    {
        if (side != 1)
        {
            return false;
        }
        else
        {
            j++;
            Block block = LOTRMod.dwarvenDoor;

            if (entityplayer.canPlayerEdit(i, j, k, side, itemstack) && entityplayer.canPlayerEdit(i, j + 1, k, side, itemstack))
            {
                if (!block.canPlaceBlockAt(world, i, j, k))
                {
                    return false;
                }
                else
                {
                    int i1 = MathHelper.floor_double((double)((entityplayer.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                    ItemDoor.placeDoorBlock(world, i, j, k, i1, block);
					world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                    itemstack.stackSize--;
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
    }
}
