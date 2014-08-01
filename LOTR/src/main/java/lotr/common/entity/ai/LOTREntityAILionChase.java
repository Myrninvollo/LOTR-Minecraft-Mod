package lotr.common.entity.ai;

import java.util.ArrayList;
import java.util.List;

import lotr.common.entity.animal.LOTREntityLionBase;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.Vec3;

public class LOTREntityAILionChase extends EntityAIBase
{
    private LOTREntityLionBase theLion;
    private EntityCreature targetEntity;
    private double speed;
    private int chaseTimer;
    private int lionRePathDelay;
    private int targetRePathDelay;

    public LOTREntityAILionChase(LOTREntityLionBase lion, double d)
    {
    	theLion = lion;
        speed = d;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
        if (theLion.isChild() || theLion.isInLove())
        {
            return false;
        }
        else if (theLion.getRNG().nextInt(800) != 0)
        {
            return false;
        }
        else
        {
            List entities = theLion.worldObj.getEntitiesWithinAABB(EntityAnimal.class, theLion.boundingBox.expand(12D, 12D, 12D));
            List validTargets = new ArrayList();

            for (int i = 0; i < entities.size(); i++)
            {
            	EntityAnimal entity = (EntityAnimal)entities.get(i);
            	if (entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage) != null)
            	{
            		continue;
            	}
            	
            	validTargets.add(entity);
            }
            
            if (validTargets.isEmpty())
            {
            	return false;
            }
            
            targetEntity = (EntityAnimal)validTargets.get(theLion.getRNG().nextInt(validTargets.size()));
            return true;
        }
    }
    
    @Override
    public void startExecuting()
    {
    	chaseTimer = 300 + theLion.getRNG().nextInt(400);
    }
    
    @Override
    public void updateTask()
    {
    	chaseTimer--;
    	theLion.getLookHelper().setLookPositionWithEntity(targetEntity, 30F, 30F);
    	
    	lionRePathDelay--;
		if (lionRePathDelay <= 0)
		{
			lionRePathDelay = 10;
			theLion.getNavigator().tryMoveToEntityLiving(targetEntity, speed);
		}
		
		if (targetEntity.getNavigator().noPath())
		{
			Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(targetEntity, 16, 7, Vec3.createVectorHelper(theLion.posX, theLion.posY, theLion.posZ));
			if (vec3 != null)
			{
				targetEntity.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 2D);
			}
		}
    }
    
    @Override
    public boolean continueExecuting()
    {
        return targetEntity != null && targetEntity.isEntityAlive() && chaseTimer > 0 && theLion.getDistanceSqToEntity(targetEntity) < 256D;
    }
    
    @Override
    public void resetTask()
    {
    	chaseTimer = 0;
    	lionRePathDelay = 0;
    	targetRePathDelay = 0;
    }
}