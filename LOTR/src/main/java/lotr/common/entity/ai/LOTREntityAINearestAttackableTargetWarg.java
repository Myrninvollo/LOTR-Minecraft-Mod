package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityWarg;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityCreature;

public class LOTREntityAINearestAttackableTargetWarg extends LOTREntityAINearestAttackableTargetBasic
{
    public LOTREntityAINearestAttackableTargetWarg(EntityCreature entity, Class targetClass, int chance, boolean flag)
    {
        super(entity, targetClass, chance, flag);
    }
	
    public LOTREntityAINearestAttackableTargetWarg(EntityCreature entity, Class targetClass, int chance, boolean flag, IEntitySelector selector)
    {
        super(entity, targetClass, chance, flag, selector);
    }
	
    @Override
    public boolean shouldExecute()
    {
		if (((LOTREntityWarg)taskOwner).getSaddled())
		{
			return false;
		}
		return super.shouldExecute();
	}
}
