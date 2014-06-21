package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRNPCMount;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class LOTREntityAIHorseFollowHiringPlayer extends EntityAIBase
{
    private LOTRNPCMount theHorse;
    private EntityCreature livingHorse;
    private EntityPlayer theHiringPlayer;
    private double moveSpeed;
    private int followTick;
    private float maxDist;
    private float minDist;
    private boolean avoidsWater;
	private boolean initSpeed;
	
	public LOTREntityAIHorseFollowHiringPlayer(LOTRNPCMount entity)
	{
        theHorse = entity;
        livingHorse = (EntityCreature)theHorse;
        minDist = 8F;
        maxDist = 4F;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
		if (!theHorse.getBelongsToNPC())
		{
			return false;
		}
		Entity rider = livingHorse.riddenByEntity;
		if (rider == null || !rider.isEntityAlive() || !(rider instanceof LOTREntityNPC))
		{
			return false;
		}
		
		LOTREntityNPC ridingNPC = (LOTREntityNPC)rider;
        if (!ridingNPC.hiredNPCInfo.isActive)
        {
            return false;
        }
        else
        {
            EntityPlayer entityplayer = ridingNPC.hiredNPCInfo.getHiringPlayer();
            if (entityplayer == null)
            {
                return false;
            }
			else if (!ridingNPC.hiredNPCInfo.shouldFollowPlayer())
			{
				return false;
			}
			else if (livingHorse.getDistanceSqToEntity(entityplayer) < (double)(minDist * minDist))
			{
				return false;
			}
			else
			{
				theHiringPlayer = entityplayer;
				return true;
			}
		}
    }

    @Override
    public boolean continueExecuting()
    {
		if (livingHorse.riddenByEntity == null || !livingHorse.riddenByEntity.isEntityAlive() || !(livingHorse.riddenByEntity instanceof LOTREntityNPC))
		{
			return false;
		}
		LOTREntityNPC ridingNPC = ((LOTREntityNPC)livingHorse.riddenByEntity);
        return ridingNPC.hiredNPCInfo.isActive && ridingNPC.hiredNPCInfo.getHiringPlayer() != null && ridingNPC.hiredNPCInfo.shouldFollowPlayer() && !livingHorse.getNavigator().noPath() && livingHorse.getDistanceSqToEntity(theHiringPlayer) > (double)(maxDist * maxDist);
    }

    @Override
    public void startExecuting()
    {
        followTick = 0;
        avoidsWater = livingHorse.getNavigator().getAvoidsWater();
        livingHorse.getNavigator().setAvoidsWater(false);
		if (!initSpeed)
		{
			double d = livingHorse.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
			moveSpeed = (1D / d) * 0.37D;
			initSpeed = true;
		}
    }

    @Override
    public void resetTask()
    {
        theHiringPlayer = null;
        livingHorse.getNavigator().clearPathEntity();
        livingHorse.getNavigator().setAvoidsWater(avoidsWater);
    }

    @Override
    public void updateTask()
    {
		LOTREntityNPC ridingNPC = (LOTREntityNPC)livingHorse.riddenByEntity;
        livingHorse.getLookHelper().setLookPositionWithEntity(theHiringPlayer, 10F, (float)livingHorse.getVerticalFaceSpeed());
		ridingNPC.rotationYaw = livingHorse.rotationYaw;
		ridingNPC.rotationYawHead = livingHorse.rotationYawHead;

        if (ridingNPC.hiredNPCInfo.shouldFollowPlayer())
        {
            if (--followTick <= 0)
            {
                followTick = 10;
                if (!livingHorse.getNavigator().tryMoveToEntityLiving(theHiringPlayer, moveSpeed) && ridingNPC.hiredNPCInfo.teleportAutomatically)
				{
					float f = (float)ridingNPC.getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
					f *= f;
					if (ridingNPC.getDistanceSqToEntity(theHiringPlayer) > f)
					{
						ridingNPC.hiredNPCInfo.tryTeleportToHiringPlayer();
					}
				}
            }
        }
    }
}
