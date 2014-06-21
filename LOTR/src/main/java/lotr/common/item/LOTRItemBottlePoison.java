package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class LOTRItemBottlePoison extends Item
{
	public LOTRItemBottlePoison()
	{
		super();
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabMisc);
		setContainerItem(Items.glass_bottle);
	}
	
	@Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.drink;
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		return itemstack;
	}
	
	@Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		if (!world.isRemote)
		{
			entityplayer.addPotionEffect(new PotionEffect(Potion.poison.id, 20 * (4 + world.rand.nextInt(4))));
		}

        return !entityplayer.capabilities.isCreativeMode ? new ItemStack(Items.glass_bottle) : itemstack;
    }
}
