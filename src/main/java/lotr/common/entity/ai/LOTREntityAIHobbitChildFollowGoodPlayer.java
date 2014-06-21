package lotr.common.entity.ai;

import java.util.Iterator;
import java.util.List;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.entity.npc.LOTREntityHobbit;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class LOTREntityAIHobbitChildFollowGoodPlayer extends EntityAIBase
{
    private LOTREntityHobbit theHobbit;
    private EntityPlayer playerToFollow;
	private float range;
	private double speed;
    private int followDelay;

    public LOTREntityAIHobbitChildFollowGoodPlayer(LOTREntityHobbit hobbit, float f, double d)
    {
        theHobbit = hobbit;
        range = f;
		speed = d;
    }

    @Override
    public boolean shouldExecute()
    {
		if (theHobbit.familyInfo.getNPCAge() >= 0)
		{
			return false;
		}
		
		List list = theHobbit.worldObj.getEntitiesWithinAABB(EntityPlayer.class, theHobbit.boundingBox.expand((double)range, 3.0D, (double)range));
		EntityPlayer entityplayer = null;
		double distanceSq = Double.MAX_VALUE;
		Iterator iterator = list.iterator();

		while (iterator.hasNext())
		{
			EntityPlayer playerCandidate = (EntityPlayer)iterator.next();

			if (LOTRLevelData.getAlignment(playerCandidate, theHobbit.getFaction()) >= LOTRAlignmentValues.HOBBIT_CHILD_FOLLOW)
			{
				double d = theHobbit.getDistanceSqToEntity(playerCandidate);

				if (d <= distanceSq)
				{
					distanceSq = d;
					entityplayer = playerCandidate;
				}
			}
		}

		if (entityplayer == null)
		{
			return false;
		}
		else if (distanceSq < 9.0D)
		{
			return false;
		}
		else
		{
			playerToFollow = entityplayer;
			return true;
		}
    }

    @Override
    public boolean continueExecuting()
    {
        if (!playerToFollow.isEntityAlive() || theHobbit.familyInfo.getNPCAge() >= 0)
        {
            return false;
        }
        else
        {
            double distanceSq = theHobbit.getDistanceSqToEntity(playerToFollow);
            return distanceSq >= 9.0D && distanceSq <= 256.0D;
        }
    }

    @Override
    public void startExecuting()
    {
        followDelay = 0;
    }

    @Override
    public void resetTask()
    {
        playerToFollow = null;
    }

    @Override
    public void updateTask()
    {
        if (--followDelay <= 0)
        {
            followDelay = 10;
            theHobbit.getNavigator().tryMoveToEntityLiving(playerToFollow, speed);
        }
    }
}
