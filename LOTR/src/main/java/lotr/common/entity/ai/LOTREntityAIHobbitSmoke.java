package lotr.common.entity.ai;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.projectile.LOTREntitySmokeRing;
import net.minecraft.item.ItemStack;

public class LOTREntityAIHobbitSmoke extends LOTREntityAIConsumeBase
{
	public LOTREntityAIHobbitSmoke(LOTREntityNPC entity, int chance)
	{
		super(entity, null, chance);
	}
	
	@Override
	protected ItemStack createConsumable()
	{
		return new ItemStack(LOTRMod.hobbitPipe);
	}
	
	@Override
	protected void updateConsumeTick(int tick) {}
	
	@Override
	protected void consume()
	{
		theEntity.worldObj.spawnEntityInWorld(new LOTREntitySmokeRing(theEntity.worldObj, theEntity));
		theEntity.playSound("lotr:item.puff", 1F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1F);
		theEntity.heal(2F);
	}
}
