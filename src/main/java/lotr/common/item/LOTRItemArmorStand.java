package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRItemArmorStand extends Item
{
	public LOTRItemArmorStand()
	{
		super();
		setCreativeTab(LOTRCreativeTabs.tabDeco);
		setMaxStackSize(1);
	}
	
	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2)
    {
		if (world.isRemote)
		{
			return true;
		}
		
		i += Facing.offsetsXForSide[side];
		j += Facing.offsetsYForSide[side];
		k += Facing.offsetsZForSide[side];
		Block block = LOTRMod.armorStand;
		
		Block block1 = world.getBlock(i, j, k);
		Block block2 = world.getBlock(i, j + 1, k);
		if ((block1 != null && !block1.isReplaceable(world, i, j, k)) || (block2 != null && !block2.isReplaceable(world, i, j + 1, k)))
		{
			return false;
		}

		if (entityplayer.canPlayerEdit(i, j, k, side, itemstack) && entityplayer.canPlayerEdit(i, j + 1, k, side, itemstack))
		{
			if (!block.canPlaceBlockAt(world, i, j, k))
			{
				return false;
			}
			else
			{
				int l = MathHelper.floor_double((double)(entityplayer.rotationYaw * 4F / 360F) + 0.5D) & 3;
				world.setBlock(i, j, k, block, l, 3);
				world.setBlock(i, j + 1, k, block, l | 4, 3);
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
