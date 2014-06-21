package lotr.common.item;

import java.util.List;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.projectile.LOTREntitySmokeRing;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemHobbitPipe extends Item
{
	public LOTRItemHobbitPipe()
	{
		super();
		setMaxDamage(300);
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabMisc);
	}
	
	@Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (entityplayer.inventory.hasItem(LOTRMod.pipeweed) || entityplayer.capabilities.isCreativeMode)
		{
			itemstack.damageItem(1, entityplayer);
			if (!entityplayer.capabilities.isCreativeMode)
			{
				entityplayer.inventory.consumeInventoryItem(LOTRMod.pipeweed);
			}
			
			if (entityplayer.canEat(false))
			{
				entityplayer.getFoodStats().addStats(2, 0.3F);
			}
			
			if (!world.isRemote)
			{
				LOTREntitySmokeRing smoke = new LOTREntitySmokeRing(world, entityplayer);
				int i = 0;
				if (itemstack.getTagCompound() != null)
				{
					i = itemstack.getTagCompound().getInteger("SmokeColour");
				}
				smoke.setSmokeColour(i);
				world.spawnEntityInWorld(smoke);
				
				if (i == 16)
				{
					LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useMagicPipe);
				}
			}
			world.playSoundAtEntity(entityplayer, "lotr:item.puff", 1F, (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F + 1.0F);
		}
        return itemstack;
	}

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 40;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (entityplayer.inventory.hasItem(LOTRMod.pipeweed) || entityplayer.capabilities.isCreativeMode)
        {
            entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        }

        return itemstack;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
    {
		int i = 0;
		if (itemstack.getTagCompound() != null)
		{
			i = itemstack.getTagCompound().getInteger("SmokeColour");
		}

		list.add(StatCollector.translateToLocal(getUnlocalizedName() + ".subtitle." + i));
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int j = 0; j <= 16; j++)
        {
			ItemStack itemstack = new ItemStack(this);
			itemstack.setTagCompound(new NBTTagCompound());
			itemstack.getTagCompound().setInteger("SmokeColour", j);
            list.add(itemstack);
        }
    }
}
