package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.entity.projectile.LOTREntityPebble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRItemSling extends Item
{
	public LOTRItemSling()
	{
		super();
		setMaxStackSize(1);
		setMaxDamage(250);
		setCreativeTab(LOTRCreativeTabs.tabCombat);
	}

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (entityplayer.inventory.hasItem(LOTRMod.pebble) || entityplayer.capabilities.isCreativeMode)
        {
            itemstack.damageItem(1, entityplayer);
			if (!entityplayer.capabilities.isCreativeMode)
			{
				entityplayer.inventory.consumeInventoryItem(LOTRMod.pebble);
			}
            world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            if (!world.isRemote)
            {
                world.spawnEntityInWorld(new LOTREntityPebble(world, entityplayer));
            }
        }
        return itemstack;
    }
	
	@Override
    public boolean getIsRepairable(ItemStack itemstack, ItemStack repairItem)
    {
        return repairItem.getItem() == Items.leather ? true : super.getIsRepairable(itemstack, repairItem);
    }
}
