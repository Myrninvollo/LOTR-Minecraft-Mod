package lotr.common.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class LOTREntityAIBurningPanic extends EntityAIBase
{
    private EntityCreature theEntity;
    private double speed;
    private double randPosX;
    private double randPosY;
    private double randPosZ;

    public LOTREntityAIBurningPanic(EntityCreature entity, double d)
    {
        theEntity = entity;
        speed = d;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
        if (!theEntity.isBurning())
        {
            return false;
        }
        else
        {
            Vec3 vec3 = RandomPositionGenerator.findRandomTarget(theEntity, 5, 4);
            if (vec3 == null)
            {
                return false;
            }
            else
            {
                randPosX = vec3.xCoord;
                randPosY = vec3.yCoord;
                randPosZ = vec3.zCoord;
                return true;
            }
        }
    }

    @Override
    public void startExecuting()
    {
        theEntity.getNavigator().tryMoveToXYZ(randPosX, randPosY, randPosZ, speed);
    }

    @Override
    public boolean continueExecuting()
    {
        return theEntity.isBurning() && !theEntity.getNavigator().noPath();
    }
}
