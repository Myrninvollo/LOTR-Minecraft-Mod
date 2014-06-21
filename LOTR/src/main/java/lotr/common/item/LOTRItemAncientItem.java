package lotr.common.item;

import java.util.List;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemAncientItem extends Item
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	private String[] itemNames = {"sword", "dagger", "helmet", "body", "legs", "boots"};
	
	public LOTRItemAncientItem()
	{
		super();
		setMaxStackSize(1);
        setHasSubtypes(true);
        setMaxDamage(0);
		setCreativeTab(LOTRCreativeTabs.tabMisc);
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int slot, boolean currentItem)
	{
		if (!world.isRemote && entity instanceof EntityPlayer && !(((EntityPlayer)entity).capabilities.isCreativeMode))
		{
			((EntityPlayer)entity).inventory.setInventorySlotContents(slot, getRandomItem(itemstack));
		}
	}
	
	public static ItemStack getRandomItem(ItemStack itemstack)
	{
		ItemStack randomItem = new ItemStack(LOTRMod.scimitarOrc);
		if (itemstack.getItemDamage() == 0)
		{
			int j = itemRand.nextInt(3);
			switch (j)
			{
				case 0:
					randomItem = new ItemStack(LOTRMod.scimitarOrc);
					break;
				case 1:
					randomItem = new ItemStack(LOTRMod.swordHighElven);
					break;
				case 2:
					randomItem = new ItemStack(LOTRMod.swordGondor);
					break;
			}
		}
		else if (itemstack.getItemDamage() == 1)
		{
			int j = itemRand.nextInt(3);
			switch (j)
			{
				case 0:
					randomItem = new ItemStack(LOTRMod.daggerOrc);
					break;
				case 1:
					randomItem = new ItemStack(LOTRMod.daggerHighElven);
					break;
				case 2:
					randomItem = new ItemStack(LOTRMod.daggerGondor);
					break;
			}
		}
		else if (itemstack.getItemDamage() == 2)
		{
			int j = itemRand.nextInt(3);
			switch (j)
			{
				case 0:
					randomItem = new ItemStack(LOTRMod.helmetOrc);
					break;
				case 1:
					randomItem = new ItemStack(LOTRMod.helmetHighElven);
					break;
				case 2:
					randomItem = new ItemStack(LOTRMod.helmetGondor);
					break;
			}
		}
		else if (itemstack.getItemDamage() == 3)
		{
			int j = itemRand.nextInt(3);
			switch (j)
			{
				case 0:
					randomItem = new ItemStack(LOTRMod.bodyOrc);
					break;
				case 1:
					randomItem = new ItemStack(LOTRMod.bodyHighElven);
					break;
				case 2:
					randomItem = new ItemStack(LOTRMod.bodyGondor);
					break;
			}
		}
		else if (itemstack.getItemDamage() == 4)
		{
			int j = itemRand.nextInt(3);
			switch (j)
			{
				case 0:
					randomItem = new ItemStack(LOTRMod.legsOrc);
					break;
				case 1:
					randomItem = new ItemStack(LOTRMod.legsHighElven);
					break;
				case 2:
					randomItem = new ItemStack(LOTRMod.legsGondor);
					break;
			}
		}
		else if (itemstack.getItemDamage() == 5)
		{
			int j = itemRand.nextInt(3);
			switch (j)
			{
				case 0:
					randomItem = new ItemStack(LOTRMod.bootsOrc);
					break;
				case 1:
					randomItem = new ItemStack(LOTRMod.bootsHighElven);
					break;
				case 2:
					randomItem = new ItemStack(LOTRMod.bootsGondor);
					break;
			}
		}
		
		if (randomItem.isItemStackDamageable() && !randomItem.getHasSubtypes())
		{
			randomItem.setItemDamage(MathHelper.floor_double((float)randomItem.getMaxDamage() * (itemRand.nextFloat() * 0.5F)));
		}
		
		if (itemRand.nextInt(3) == 0 && randomItem.isItemEnchantable() && randomItem.getItem().getItemEnchantability() > 0)
		{
			int enchantability = EnchantmentHelper.calcItemStackEnchantability(itemRand, 1, 5, randomItem);
			EnchantmentHelper.addRandomEnchantment(itemRand, randomItem, enchantability);
		}
		
		return randomItem;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int i)
    {
        if (i >= icons.length)
		{
			i = 0;
		}
		return icons[i];
    }
	
	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return super.getUnlocalizedName() + "." + itemstack.getItemDamage();
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister)
    {
        icons = new IIcon[itemNames.length];
        for (int i = 0; i < itemNames.length; i++)
        {
            icons[i] = iconregister.registerIcon(getIconString() + "_" + itemNames[i]);
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int j = 0; j <= 5; j++)
        {
            list.add(new ItemStack(item, 1, j));
        }
    }
}
