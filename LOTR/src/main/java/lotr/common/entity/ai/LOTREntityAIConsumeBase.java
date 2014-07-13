package lotr.common.entity.ai;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;

public abstract class LOTREntityAIConsumeBase extends EntityAIBase
{
	protected LOTREntityNPC theEntity;
	protected Random rand;
	protected LOTRFoods foodPool;
	private int chanceToConsume;
	private int consumeTick;
	
	public LOTREntityAIConsumeBase(LOTREntityNPC entity, LOTRFoods foods, int chance)
	{
		theEntity = entity;
		rand = theEntity.getRNG();
		foodPool = foods;
		chanceToConsume = chance;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute()
	{
		if (theEntity.isChild())
		{
			return false;
		}
		
		if (theEntity.getAttackTarget() != null)
		{
			return false;
		}
		
		boolean needsHeal = theEntity.getHealth() < theEntity.getMaxHealth();
		return (needsHeal && rand.nextInt(chanceToConsume / 4) == 0) || rand.nextInt(chanceToConsume) == 0;
	}
	
	@Override
	public void startExecuting()
	{
		theEntity.setDefaultHeldItem(theEntity.getHeldItem());
		theEntity.setCurrentItemOrArmor(0, createConsumable());
		consumeTick = 32;
	}
	
	@Override
	public void updateTask()
	{
		consumeTick--;
		
		updateConsumeTick(consumeTick);
		
		if (consumeTick == 0)
		{
			consume();
		}
	}
	
	protected abstract ItemStack createConsumable();
	
	protected abstract void updateConsumeTick(int tick);
	
	protected abstract void consume();
	
	@Override
	public boolean continueExecuting()
	{
		return consumeTick > 0 && theEntity.getHeldItem() != null && theEntity.getAttackTarget() == null;
	}
	
	@Override
	public void resetTask()
	{
		if (theEntity.hasDefaultHeldItem())
		{
			theEntity.setCurrentItemOrArmor(0, theEntity.getDefaultHeldItem());
			theEntity.clearDefaultHeldItem();
		}
		consumeTick = 0;
	}
}
