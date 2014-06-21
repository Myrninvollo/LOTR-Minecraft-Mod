package lotr.common.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;

public class LOTREntityAIAvoidWithChance extends EntityAIAvoidEntity
{
    private EntityCreature theEntity;
	private float chance;

    public LOTREntityAIAvoidWithChance(EntityCreature entity, Class avoidClass, float f, double d, double d1, float f1)
    {
        super(entity, avoidClass, f, d, d1);
		theEntity = entity;
		chance = f1;
    }

    @Override
    public boolean shouldExecute()
    {
		return theEntity.getRNG().nextFloat() < chance && super.shouldExecute();
	}
}
