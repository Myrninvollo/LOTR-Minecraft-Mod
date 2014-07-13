package lotr.common.entity.ai;

import lotr.common.LOTRFoods;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class LOTREntityAIEat extends LOTREntityAIConsumeBase
{
	public LOTREntityAIEat(LOTREntityNPC entity, LOTRFoods foods, int chance)
	{
		super(entity, foods, chance);
	}
	
	@Override
	protected ItemStack createConsumable()
	{
		return foodPool.getRandomFood(rand);
	}
	
	@Override
	protected void updateConsumeTick(int tick)
	{
		if (tick % 4 == 0)
		{
			theEntity.spawnFoodParticles();
			theEntity.playSound("random.eat", 0.5F + 0.5F * (float)rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1F);
		}
	}
	
	@Override
	protected void consume()
	{
		ItemStack itemstack = theEntity.getHeldItem();
		Item item = itemstack.getItem();
		if (item instanceof ItemFood)
		{
			ItemFood food = (ItemFood)item;
			theEntity.heal((float)food.func_150905_g(itemstack));
		}
	}
}
