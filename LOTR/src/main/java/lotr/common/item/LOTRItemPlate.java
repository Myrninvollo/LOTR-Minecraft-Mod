package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.entity.projectile.LOTREntityPlate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRItemPlate extends ItemReed
{
	public LOTRItemPlate()
	{
		super(LOTRMod.plateBlock);
		setCreativeTab(LOTRCreativeTabs.tabFood);
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        LOTREntityPlate plate = new LOTREntityPlate(world, entityplayer);
        world.playSoundAtEntity(entityplayer, "random.bow", 1F, 1F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.25F);
        if (!world.isRemote)
        {
            world.spawnEntityInWorld(plate);
        }
		if (!entityplayer.capabilities.isCreativeMode)
		{
			itemstack.stackSize--;
		}
		return itemstack;
	}
}
