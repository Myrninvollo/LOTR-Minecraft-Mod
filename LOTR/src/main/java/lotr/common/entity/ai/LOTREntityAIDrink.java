package lotr.common.entity.ai;

import lotr.common.LOTRFoods;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.item.LOTRItemMug;
import lotr.common.item.LOTRItemMugBrewable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LOTREntityAIDrink extends LOTREntityAIConsumeBase
{
	public LOTREntityAIDrink(LOTREntityNPC entity, LOTRFoods foods, int chance)
	{
		super(entity, foods, chance);
	}
	
	@Override
	protected ItemStack createConsumable()
	{
		ItemStack drink = foodPool.getRandomFood(rand);
		if (drink.getItem() instanceof LOTRItemMugBrewable)
		{
			drink.setItemDamage(1 + rand.nextInt(3));
		}
		return drink;
	}
	
	@Override
	protected void updateConsumeTick(int tick)
	{
		if (tick % 4 == 0)
		{
			theEntity.playSound("random.drink", 0.5F, rand.nextFloat() * 0.1F + 0.9F);
		}
	}
	
	@Override
	protected void consume()
	{
		ItemStack itemstack = theEntity.getHeldItem();
		Item item = itemstack.getItem();
		if (item instanceof LOTRItemMug)
		{
			LOTRItemMug drink = (LOTRItemMug)item;
			drink.applyToNPC(theEntity, itemstack);
		}
		else if (item instanceof LOTRItemMugBrewable)
		{
			LOTRItemMugBrewable drink = (LOTRItemMugBrewable)item;
			drink.applyToNPC(theEntity, itemstack);
		}
	}
}
