package lotr.common.item;

import java.util.ArrayList;
import java.util.List;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class LOTRItemBowlDrink extends Item
{
	private int foodHealAmount;
	private float foodSaturationAmount;
	private List potionEffects = new ArrayList();
	
	public LOTRItemBowlDrink()
	{
		super();
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabFood);
	}
	
	public LOTRItemBowlDrink setDrinkStats(int i, float f)
	{
		foodHealAmount = i;
		foodSaturationAmount = f;
		return this;
	}
	
	public LOTRItemBowlDrink addPotionEffect(int i, int j)
	{
		potionEffects.add(new PotionEffect(i, j));
		return this;
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
		if (canPlayerDrink(entityplayer))
		{
			entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		}
		return itemstack;
	}
	
	public boolean canPlayerDrink(EntityPlayer entityplayer)
	{
		return !potionEffects.isEmpty() || entityplayer.canEat(false);
	}
	
	@Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		if (entityplayer.canEat(false))
		{
			entityplayer.getFoodStats().addStats(foodHealAmount, foodSaturationAmount);
		}
		
		if (!world.isRemote)
		{
			for (int i = 0; i < potionEffects.size(); i++)
			{
				PotionEffect effect = (PotionEffect)potionEffects.get(i);
				entityplayer.addPotionEffect(new PotionEffect(effect.getPotionID(), 20 * effect.getDuration()));
			}
		}

        return !entityplayer.capabilities.isCreativeMode ? new ItemStack(Items.bowl) : itemstack;
    }
}
