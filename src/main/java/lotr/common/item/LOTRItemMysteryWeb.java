package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.entity.projectile.LOTREntityMysteryWeb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRItemMysteryWeb extends Item
{
	public LOTRItemMysteryWeb()
	{
		super();
		setCreativeTab(LOTRCreativeTabs.tabMisc);
	}

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		if (!entityplayer.capabilities.isCreativeMode)
		{
			itemstack.stackSize--;
		}
		world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if (!world.isRemote)
		{
			world.spawnEntityInWorld(new LOTREntityMysteryWeb(world, entityplayer));
		}
        return itemstack;
    }
}
