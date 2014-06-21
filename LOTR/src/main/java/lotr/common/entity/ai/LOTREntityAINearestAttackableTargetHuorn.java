package lotr.common.entity.ai;

import java.util.List;

import lotr.common.entity.npc.LOTREntityHuornBase;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityCreature;

public class LOTREntityAINearestAttackableTargetHuorn extends LOTREntityAINearestAttackableTargetBasic
{
    public LOTREntityAINearestAttackableTargetHuorn(EntityCreature entity, Class targetClass, int chance, boolean flag)
    {
        super(entity, targetClass, chance, flag);
    }
	
    public LOTREntityAINearestAttackableTargetHuorn(EntityCreature entity, Class targetClass, int chance, boolean flag, IEntitySelector selector)
    {
        super(entity, targetClass, chance, flag, selector);
    }
	
    @Override
    public boolean shouldExecute()
    {
		int chance = 400;
		List nearbyHuorns = taskOwner.worldObj.getEntitiesWithinAABB(LOTREntityHuornBase.class, taskOwner.boundingBox.expand(24D, 8D, 24D));
		for (int i = 0; i < nearbyHuorns.size(); i++)
		{
			LOTREntityHuornBase huorn = (LOTREntityHuornBase)nearbyHuorns.get(i);
			if (huorn.getAttackTarget() != null)
			{
				chance /= 2;
			}
		}
		if (chance < 20)
		{
			chance = 20;
		}
		if (taskOwner.getRNG().nextInt(chance) != 0)
		{
			return false;
		}
		return super.shouldExecute();
	}
}
