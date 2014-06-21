package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityGollum;
import net.minecraft.entity.ai.EntityAIBase;

public class LOTREntityAIGollumRemainStill extends EntityAIBase
{
    private LOTREntityGollum theGollum;

    public LOTREntityAIGollumRemainStill(LOTREntityGollum entity)
    {
        theGollum = entity;
        setMutexBits(5);
    }

    @Override
    public boolean shouldExecute()
    {
        if (theGollum.getGollumOwner() == null)
        {
            return false;
        }
        else if (theGollum.isInWater())
        {
            return false;
        }
        else if (!theGollum.onGround)
        {
            return false;
        }
        else
        {
            return theGollum.isGollumSitting();
        }
    }

    @Override
    public void startExecuting()
    {
        theGollum.getNavigator().clearPathEntity();
    }
}
