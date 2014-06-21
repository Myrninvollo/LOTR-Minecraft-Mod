package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.ai.EntityAIBase;

public class LOTREntityAIHiredRemainStill extends EntityAIBase
{
    private LOTREntityNPC theNPC;

    public LOTREntityAIHiredRemainStill(LOTREntityNPC entity)
    {
        theNPC = entity;
        setMutexBits(5);
    }

    @Override
    public boolean shouldExecute()
    {
        if (!theNPC.hiredNPCInfo.isActive)
        {
            return false;
        }
        else if (theNPC.isInWater())
        {
            return false;
        }
        else if (!theNPC.onGround)
        {
            return false;
        }
        else
        {
            return theNPC.hiredNPCInfo.isHalted() && (theNPC.getAttackTarget() == null || !theNPC.getAttackTarget().isEntityAlive());
        }
    }

    @Override
    public void startExecuting()
    {
        theNPC.getNavigator().clearPathEntity();
    }
}
