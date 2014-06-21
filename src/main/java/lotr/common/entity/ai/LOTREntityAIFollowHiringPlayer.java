package lotr.common.entity.ai;

import java.util.ArrayList;
import java.util.List;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTRBannerBearer;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class LOTREntityAIFollowHiringPlayer extends EntityAIBase
{
    private LOTREntityNPC theNPC;
    private final boolean isBannerBearer;
    private EntityPlayer theHiringPlayer;
    private double moveSpeed;
    private int followTick;
    private float maxDist;
    private float minDist;
    private boolean avoidsWater;
    private EntityLiving bannerBearerTarget;
	
	public LOTREntityAIFollowHiringPlayer(LOTREntityNPC entity)
	{
        theNPC = entity;
        isBannerBearer = entity instanceof LOTRBannerBearer;
		double entityMoveSpeed = entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
        moveSpeed = (1D / entityMoveSpeed) * 0.37D;
        minDist = 8F;
        maxDist = 4F;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        if (!theNPC.hiredNPCInfo.isActive)
        {
            return false;
        }
        else
        {
            EntityPlayer entityplayer = theNPC.hiredNPCInfo.getHiringPlayer();
            if (entityplayer == null)
            {
                return false;
            }
            theHiringPlayer = entityplayer;
			if (!theNPC.hiredNPCInfo.shouldFollowPlayer())
			{
				return false;
			}
            
            if (isBannerBearer)
            {
				List alliesToFollow = new ArrayList();
				List nearbyEntities = theNPC.worldObj.getEntitiesWithinAABB(EntityLiving.class, theNPC.boundingBox.expand(16D, 16D, 16D));
				for (int i = 0; i < nearbyEntities.size(); i++)
				{
					EntityLiving entity = (EntityLiving)nearbyEntities.get(i);
					if (entity != theNPC && LOTRMod.getNPCFaction(entity) == theNPC.getFaction())
					{
						if (entity instanceof LOTREntityNPC)
						{
							LOTREntityNPC npc = (LOTREntityNPC)entity;
							if (!npc.hiredNPCInfo.isActive || npc.hiredNPCInfo.getHiringPlayer() != entityplayer)
							{
								continue;
							}
						}
						alliesToFollow.add(entity);
					}
				}
				
				EntityLiving entityToFollow = null;
				double d = Double.MAX_VALUE;
				
				for (int i = 0; i < alliesToFollow.size(); i++)
				{
					EntityLiving entity = (EntityLiving)alliesToFollow.get(i);
					double dist = theNPC.getDistanceSqToEntity(entity);
					if (dist < d && dist > (double)(minDist * minDist))
					{
						d = dist;
						entityToFollow = entity;
					}
				}
				
				if (entityToFollow != null)
				{
					bannerBearerTarget = entityToFollow;
					return true;
				}
            }

			if (theNPC.getDistanceSqToEntity(entityplayer) < (double)(minDist * minDist))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
    }

    @Override
    public boolean continueExecuting()
    {
        if (theNPC.hiredNPCInfo.isActive && theNPC.hiredNPCInfo.getHiringPlayer() != null && theNPC.hiredNPCInfo.shouldFollowPlayer() && !theNPC.getNavigator().noPath())
        {
        	EntityLivingBase target = bannerBearerTarget != null ? bannerBearerTarget : theHiringPlayer;
        	return theNPC.getDistanceSqToEntity(target) > (double)(maxDist * maxDist);
        }
        return false;
    }

    @Override
    public void startExecuting()
    {
        followTick = 0;
        avoidsWater = theNPC.getNavigator().getAvoidsWater();
        theNPC.getNavigator().setAvoidsWater(false);
    }

    @Override
    public void resetTask()
    {
        theHiringPlayer = null;
        bannerBearerTarget = null;
        theNPC.getNavigator().clearPathEntity();
        theNPC.getNavigator().setAvoidsWater(avoidsWater);
    }

    @Override
    public void updateTask()
    {
    	EntityLivingBase target = bannerBearerTarget != null ? bannerBearerTarget : theHiringPlayer;
    	
        theNPC.getLookHelper().setLookPositionWithEntity(target, 10F, (float)theNPC.getVerticalFaceSpeed());

        if (theNPC.hiredNPCInfo.shouldFollowPlayer())
        {
            if (--followTick <= 0)
            {
                followTick = 10;
                if (!theNPC.getNavigator().tryMoveToEntityLiving(target, moveSpeed) && theNPC.hiredNPCInfo.teleportAutomatically)
				{
					float f = (float)theNPC.getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
					f *= f;
					if (theNPC.getDistanceSqToEntity(theHiringPlayer) > f)
					{
						theNPC.hiredNPCInfo.tryTeleportToHiringPlayer();
					}
				}
            }
        }
    }
}
