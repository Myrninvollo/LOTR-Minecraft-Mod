package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityWargBombardier;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.world.World;

public class LOTREntityAIWargBombardierAttack extends EntityAIBase
{
    private World worldObj;
    private LOTREntityWargBombardier theWarg;
    private EntityLivingBase entityTarget;
    private double moveSpeed;
    private PathEntity entityPathEntity;
    private int randomMoveTick;

    public LOTREntityAIWargBombardierAttack(LOTREntityWargBombardier entity, double speed)
    {
        theWarg = entity;
        worldObj = entity.worldObj;
        moveSpeed = speed;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase entity = theWarg.getAttackTarget();
        if (entity == null)
        {
            return false;
        }
        else
        {
            entityTarget = entity;
            entityPathEntity = theWarg.getNavigator().getPathToEntityLiving(entityTarget);
            return entityPathEntity != null;
        }
    }

    @Override
    public boolean continueExecuting()
    {
        EntityLivingBase entity = theWarg.getAttackTarget();
        return entity != null && entityTarget.isEntityAlive();
    }

    @Override
    public void startExecuting()
    {
        theWarg.getNavigator().setPath(entityPathEntity, moveSpeed);
        randomMoveTick = 0;
    }

    @Override
    public void resetTask()
    {
        entityTarget = null;
        theWarg.getNavigator().clearPathEntity();
		theWarg.setBombFuse(35);
    }

    @Override
    public void updateTask()
    {
        theWarg.getLookHelper().setLookPositionWithEntity(entityTarget, 30.0F, 30.0F);
        if (theWarg.getEntitySenses().canSee(entityTarget) && --randomMoveTick <= 0)
        {
            randomMoveTick = 4 + theWarg.getRNG().nextInt(7);
            theWarg.getNavigator().tryMoveToEntityLiving(entityTarget, moveSpeed);
        }
		
        if (theWarg.getDistanceSq(entityTarget.posX, entityTarget.boundingBox.minY, entityTarget.posZ) <= 16D)
        {
			if (theWarg.getBombFuse() > 20)
			{
				int i = theWarg.getBombFuse();
				while (i > 20)
				{
					i -= 10;
				}
				theWarg.setBombFuse(i);
			}
			else if (theWarg.getBombFuse() > 0)
			{
				theWarg.setBombFuse(theWarg.getBombFuse() - 1);
			}
            else
            {
				worldObj.createExplosion(theWarg, theWarg.posX, theWarg.posY, theWarg.posZ, (theWarg.getBombStrengthLevel() + 1) * 4F, worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
				theWarg.setDead();
            }
        }
		else
		{
			if (theWarg.getBombFuse() <= 20)
			{
				int i = theWarg.getBombFuse();
				while (i <= 20)
				{
					i += 10;
				}
				theWarg.setBombFuse(i);
			}
			else
			{
				theWarg.setBombFuse(theWarg.getBombFuse() - 1);
			}
		}
    }
}
