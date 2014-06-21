package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.ai.EntityAIBase;

public class LOTREntityAINPCFollowSpouse extends EntityAIBase
{
    private LOTREntityNPC theNPC;
    private LOTREntityNPC theSpouse;
    private double moveSpeed;
	private int followTick;

    public LOTREntityAINPCFollowSpouse(LOTREntityNPC npc, double d)
    {
        theNPC = npc;
        moveSpeed = d;
    }

    @Override
    public boolean shouldExecute()
    {
		LOTREntityNPC spouse = theNPC.familyInfo.getSpouse();
        if (spouse == null)
        {
            return false;
        }
        else
        {
            if (!spouse.isEntityAlive() || theNPC.getDistanceSqToEntity(spouse) < 36D || theNPC.getDistanceSqToEntity(spouse) >= 256D)
            {
                return false;
            }
            else
            {
                theSpouse = spouse;
                return true;
            }
        }
    }

    @Override
    public boolean continueExecuting()
    {
        if (!theSpouse.isEntityAlive())
        {
            return false;
        }
        else
        {
            double d = theNPC.getDistanceSqToEntity(theSpouse);
            return d >= 36D && d <= 256D;
        }
    }
	
    @Override
    public void startExecuting()
    {
        followTick = 200;
    }

    @Override
    public void resetTask()
    {
        theSpouse = null;
    }

    @Override
    public void updateTask()
    {
		followTick--;
        if (theNPC.getDistanceSqToEntity(theSpouse) > 144D || followTick <= 0)
		{
			followTick = 200;
            theNPC.getNavigator().tryMoveToEntityLiving(theSpouse, moveSpeed);
        }
    }
}
