package lotr.common.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class LOTREntityAIFlee extends EntityAIBase
{
    private EntityCreature theEntity;
    private double speed;
    private double attackerX;
    private double attackerY;
    private double attackerZ;
	private int timer;
	private boolean firstPath;

    public LOTREntityAIFlee(EntityCreature entity, double d)
    {
        theEntity = entity;
        speed = d;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
		EntityLivingBase attacker = theEntity.getAITarget();
        if (attacker == null)
        {
            return false;
        }
        else
        {
			attackerX = attacker.posX;
			attackerY = attacker.posY;
			attackerZ = attacker.posZ;
            return true;
        }
    }

    @Override
    public void startExecuting()
    {
		timer = 60 + theEntity.getRNG().nextInt(50);
    }

    @Override
    public boolean continueExecuting()
    {
        return timer > 0;
    }
	
	@Override
	public void updateTask()
	{
		timer--;
		if (!firstPath || theEntity.getNavigator().noPath())
		{
            Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(theEntity, 16, 7, theEntity.worldObj.getWorldVec3Pool().getVecFromPool(attackerX, attackerY, attackerZ));
            if (vec3 != null && theEntity.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, speed))
            {
				theEntity.setRevengeTarget(null);
				firstPath = true;
            }
		}
	}
	
	@Override
	public void resetTask()
	{
		theEntity.getNavigator().clearPathEntity();
		timer = 0;
		firstPath = false;
	}
}
