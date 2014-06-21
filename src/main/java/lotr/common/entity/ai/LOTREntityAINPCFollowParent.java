package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.ai.EntityAIBase;

public class LOTREntityAINPCFollowParent extends EntityAIBase
{
    private LOTREntityNPC theNPC;
    private LOTREntityNPC parentNPC;
    private double moveSpeed;
    private int followTick;

    public LOTREntityAINPCFollowParent(LOTREntityNPC npc, double d)
    {
        theNPC = npc;
        moveSpeed = d;
    }

    @Override
    public boolean shouldExecute()
    {
        if (theNPC.familyInfo.getNPCAge() >= 0)
        {
            return false;
        }
        else
        {
        	LOTREntityNPC parent = theNPC.familyInfo.getParentToFollow();

            if (parent == null)
            {
                return false;
            }
            else if (theNPC.getDistanceSqToEntity(parent) < 9D || theNPC.getDistanceSqToEntity(parent) >= 256D)
            {
                return false;
            }
            else
            {
                parentNPC = parent;
                return true;
            }
        }
    }

    @Override
    public boolean continueExecuting()
    {
        if (!parentNPC.isEntityAlive())
        {
            return false;
        }
        else
        {
            double d = theNPC.getDistanceSqToEntity(parentNPC);
            return d >= 9.0D && d <= 256.0D;
        }
    }

    @Override
    public void startExecuting()
    {
        followTick = 0;
    }

    @Override
    public void resetTask()
    {
        parentNPC = null;
    }

    @Override
    public void updateTask()
    {
        if (followTick-- <= 0)
        {
            followTick = 10;
            theNPC.getNavigator().tryMoveToEntityLiving(parentNPC, moveSpeed);
        }
    }
}
